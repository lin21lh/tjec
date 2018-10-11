/**
 * 工作流设计器对象 v1.2 2015-4-21 hyf
 * 
 * @returns
 */
var WorkflowDesigner = function() {
	/*
	 * 工作流key
	 */
	this.key = undefined;

	/*
	 * 工作流版本
	 */
	this.version = undefined;

	/*
	 * jpdl 定义字符串
	 */
	this.jpdl = undefined;

	/*
	 * 流程图节点缓存
	 */
	this.actiMap = {};

	/*
	 * 流程图节点扩展属性缓存
	 */
	this.extAttrCache = [];

	this.toolboxGridId = undefined;

	this.canvasId = undefined;

	this.urls = {
		workflowJpdlUrl : contextpath
				+ 'workflow/WfProcessDefinitionController/getDeployedJpdlContent.do',

		workflowExtAttrsUrl : contextpath
				+ 'workflow/WfProcessDefinitionController/getExtendedAttributes.do'
	};

	this.actiArray = [];
	// 流程属性
	this.processInfo = {};

	this.imgs = {
		start : new Image(),
		end : new Image(),
		decision : new Image(),
		fork : new Image()
	};
	this.imgs.start.src = '../../templates/workflow/designertool/res/component/start.png';
	this.imgs.end.src = '../../templates/workflow/designertool/res/component/end.png';
	this.imgs.decision.src = '../../templates/workflow/designertool/res/component/decision.png';
	this.imgs.fork.src = '../../templates/workflow/designertool/res/component/fork.png';
	/**
	 * 虚拟控制框
	 */
	this.drawVirtualBox = {
		visible : false
	};
	/**
	 * 虚拟控制点
	 */
	this.virtualPoint = {
		visible : false
	};
	/**
	 * 编辑模式
	 */
	this.opMode = 'select';

	/**
	 * 鼠标是否按下
	 */
	this.mousePressed = false;

	this.mouseDownLoc = {};

	this.eventFlag = undefined;

	/**
	 * 是否正在编辑
	 */
	this.propEditing = false;

	this.transitionDraw = {
		src : {
			name : null
		},
		dest : {
			name : null
		}
	};

	this.activeLine = {};
	/**
	 * 流程版本的当时编辑模式 NEW为新流程未提交（可创建、修改所有的属性及流程图，除了KEY和VERSION属性）
	 * UPDATE为已为流程修改（仅能修改扩展属性，不能修改流程图，即JPDL不可修改），每项修改的提交会立即提交到数据库
	 */
	this.editMode = "NEW";

	// 默认的权限分配器
	this.default_assignment_handler = "com.jbf.workflow.common.WfAssignmentHandler";
	// 默认的结束处理器代理
	this.default_endevent_listener = 'com.jbf.workflow.listener.WfEndEventListener';
	// 默认的开始事件处理器
	this.default_startevent_listener = 'com.jbf.workflow.listener.WfStartEventListener';
	// 默认的迁移线事件处理器
	this.default_trans_event_listener = 'com.jbf.workflow.listener.WfTransitionEventListener';
	// 默认的分支处理器
	this.default_decision_handler = 'com.jbf.workflow.common.WfDecisionAgentHandler';

};

/**
 * 初始化设计器
 */
WorkflowDesigner.prototype.init = function(toolboxGridId, canvasId, propGirdId,
		jpdlViewer, editMode) {

	this.editMode = editMode;
	if (!(editMode == "NEW" || editMode == "UPDATE")) {
		this.warn("初始化参数的编辑模式不正确！");
	}

	if (editMode == "NEW") {
		// 设置保存按钮为灰
		$('#linkbutton_save_deploy').linkbutton('enable');
	} else {
		// 设置保存按钮为灰
		$('#linkbutton_save_deploy').linkbutton('disable');
	}
	this.toolboxGridId = toolboxGridId;

	this.toolbox = {};
	this.canvasId = canvasId;
	this.propGirdId = propGirdId;
	var _cvsid = canvasId;
	if (_cvsid.startsWith('#')) {
		_cvsid = _cvsid.substring(1);
	}
	this.canvas = document.getElementById(_cvsid);
	this.context = canvas.getContext("2d");
	// 初始化toolbox
	this._initToolbox();
	this._initPropertyGrid();
	// 绑定事件
	this._bindEvent();

	this.editingPointToMove = {};

	this.activeLine = null;

	this.activeObjectName = "";

	// ...
};

/**
 * 加载工作流流程图以及附带属性
 */
WorkflowDesigner.prototype.loadWorkflowVersion = function(procVersionId) {
	this.procVersionId = procVersionId;

	var wfdHandle = this;
	$.post(this.urls.workflowJpdlUrl, {
		procVersionId : procVersionId
	}, function(msg) {
		wfdHandle.jpdl = msg;
		// 建立模型
		wfdHandle._buildModel(function() {
			// 模型建立完毕后的回调
			console.log("模型加载完毕！");
			wfdHandle._showProcessProp();
			wfdHandle._showWorkflowChart();
		});

		// 将模型显示

		// 加载节点权限
		// loadWfPrivilege(node);

		// 加载退回属性
		// loadBackAttr(node);

		// 加载节点的url
		// loadTaskUrl(node);

		// 加载流程版本信息
		// loadProcVersion(node);
	});

	// ...
};

/**
 * 加载工作流流程图以及附带属性
 */
WorkflowDesigner.prototype.createNewWorkflowVersion = function(key, version,
		wfname) {

	this.jpdl = '<?xml version="1.0" encoding="UTF-8"?><process name="' + name
			+ '" xmlns="http://jbpm.org/4.4/jpdl" key="' + key + '" version="'
			+ version + '"></process>';

	// 建立模型
	this._parsePrcocessAndDiagram();
	this.processInfo.name = wfname;
	this.processInfo.firstnode="";
	this.extAttrCache = [];

	this._showProcessProp();
	this._showWorkflowChart();
	this.activeLine = null;
	// 将模型显示

	// 加载节点权限
	// loadWfPrivilege(node);

	// 加载退回属性
	// loadBackAttr(node);

	// 加载节点的url
	// loadTaskUrl(node);

	// 加载流程版本信息
	// loadProcVersion(node);

	// ...
};

/**
 * 模型持久化
 * 
 * @param k
 */
WorkflowDesigner.prototype.modelPersist = function(k) {

	// ...
};

WorkflowDesigner.prototype._bindEvent = function() {
	this.canvas.onmousedown = mouseDown;
	this.canvas.onmouseup = mouseUp;
	this.canvas.onmousemove = mouseMove;
	window.addEventListener('keydown', designerCanvasKeyDown, true);
};

function getWorkflowDesigner() {
	if (window.wfdesigner) {
	} else {
		window.wfdesigner = new WorkflowDesigner();
	}
	return window.wfdesigner;
}
function mouseDown(e) {
	getWorkflowDesigner().dealMouseDown(e);
}

function mouseUp(e) {
	getWorkflowDesigner().dealMouseUp(e);
}

function mouseMove(e) {
	getWorkflowDesigner().dealMouseMove(e);
}

function designerCanvasKeyDown(e) {
	getWorkflowDesigner().dealDesignerCanvasKeyDown(e);
}
