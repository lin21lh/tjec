var actiArray;
var actiMap = null;
var processInfo = {
	name : '测试流程1',
	key : 'testworkflow1',
	version : '1'
};
var imgs = {
	start : new Image(),
	end : new Image(),
	decision : new Image(),
	fork : new Image()
};
imgs.start.src = '../../templates/workflow/designertool/res/component/start.png';
imgs.end.src = '../../templates/workflow/designertool/res/component/end.png';
imgs.decision.src = '../../templates/workflow/designertool/res/component/decision.png';
imgs.fork.src = '../../templates/workflow/designertool/res/component/fork.png';

var mousePressed = false;
var propEditing = false;

// transition绘制过程使用的变量
var transitionDraw = {
	src : {
		name : null,
		cx : 0,
		cy : 0
	},
	dest : {
		name : null,
		cx : 0,
		cy : 0
	}
};

var activeObjectName = null;
var activeLine = {
	name : '',
	from : null,
	to : null,
	transition : {}
};
var mouseDownLoc = {
	x : 0,
	y : 0
};

var drawVirtualBox = {
	visible : false,
	x : 0,
	y : 0,
	w : 0,
	h : 0
};
var virtualPoint = {
	visible : false,
	x : 0,
	y : 0
};

var opMode = 'select'; // 可以为select , transitionCreate , activityCreate ,edit

var toolbox = {
	widget : null
}; // 保存工具箱中选中的工具

var local_context = null;

var editingPointToMove = {
	type : null,
	index : -1,
	destX : 0,
	destY : 0
};

// 存储当前编辑的activity名称，用于名称变更
var oldActiName = null;
// 默认的权限分配器
var default_assignment_handler = "com.jbf.workflow.common.WfAssignmentHandler";
// 默认的事件处理器代理
var default_event_listener = 'com.jbf.workflow.listener.WfEndEventListener';

var default_startevent_listener = 'com.jbf.workflow.listener.WfStartEventListener';
// task等的正在编辑的事件类型标识
var eventFlag = null;

CanvasRenderingContext2D.prototype.roundRect = function(x, y, width, height,
		radius, fill, stroke) {
	if (typeof stroke == "undefined") {
		stroke = true;
	}
	if (typeof radius === "undefined") {
		radius = 5;
	}
	this.beginPath();
	this.moveTo(x + radius, y);
	this.lineTo(x + width - radius, y);
	this.quadraticCurveTo(x + width, y, x + width, y + radius);
	this.lineTo(x + width, y + height - radius);
	this
			.quadraticCurveTo(x + width, y + height, x + width - radius, y
					+ height);
	this.lineTo(x + radius, y + height);
	this.quadraticCurveTo(x, y + height, x, y + height - radius);
	this.lineTo(x, y + radius);
	this.quadraticCurveTo(x, y, x + radius, y);
	this.closePath();

	if (fill) {
		this.fill();
	}
	if (stroke) {
		this.stroke();
	}
};
