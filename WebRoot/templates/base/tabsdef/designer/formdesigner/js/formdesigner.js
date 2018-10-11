function JbfFormDesigner() {
}

JbfFormDesigner.prototype = {
	canvasId : '',
	propertyGridId : '',
	canvas : null,
	context : null,
	dimension : { // 画布尺寸
		w : 500,
		h : 400
	},
	offset : {
		x : 180,
		y : 10,
		opx : 10
	// 可选控件的左加距
	},
	baseDataModel : {
		columnCount : 2,
		labelCellWidth : 100,
		controlCellWidth : 150,
		controlCellHeight : 30,
		controlWidth : 140,
		controlHeight : 22,
		controls : [],
		optionalControls : []
	},
	dataModel : {},
	variables : {
		opCtlCurPage : 0, // 当前页索引
		opCtlPageSize : 10, // 页容量
		opCtlTotal : 3, // 控件总数
		opCtlTotalPage : 1, // 总页数

		creatingCtlIndex : -1,

		mouseStartX : 0,
		mouseStartY : 0,
		mouseMovingX : 0,
		mouseMovingY : 0
	},
	flags : {
		operflag : 'select',
		mouseStartObjType : 'blank',
		controlCreating : false,
		controlReordering : false,
		paintTrashTool : false
	},
	activeObject : {
		activeControl : null,
		tempLocation : {
			col : null,
			row : null
		}
	},
	imgs : {
		searchIcon : new Image(),
		calendarIcon : new Image(),
		comboIcon : new Image(),
		controlCreating : new Image(),
		trash : new Image()
	},
	getColumnCount : function() {
		return this.dataModel.columnCount;
	},
	setColumnCount : function(num) {
		this.dataModel.columnCount = num;
	},
	build : function(param) {
		// 初始化控件
		this.propertyGridId = param.propertyGridId;
		this.initPropertyGrid();
		this.canvasId = param.canvasId;
		this.dimension.w = this.canvasId.width;
		this.dimension.h = this.canvasId.height;
		this.canvas = document.getElementById(this.canvasId);
		this.context = this.canvas.getContext("2d");

		this.imgs.calendarIcon.onload = function() {

			window.formdesigner_.paint();
		};

		this.imgs.searchIcon.onload = function() {
			window.formdesigner_.paint();
		};

		this.imgs.comboIcon.onload = function() {
			window.formdesigner_.paint();
		};

		this.imgs.calendarIcon.src = contextpath
				+ 'templates/base/tabsdef/designer/formdesigner/img/calendar_icon.png';
		this.imgs.searchIcon.src = contextpath
				+ 'templates/base/tabsdef/designer/formdesigner/img/search_icon.png';
		this.imgs.comboIcon.src = contextpath
				+ 'templates/base/tabsdef/designer/formdesigner/img/combo_icon.png';
		this.imgs.controlCreating.src = contextpath
				+ 'templates/base/tabsdef/designer/formdesigner/img/control_creating.png';
		this.imgs.trash.src = contextpath
				+ 'templates/base/tabsdef/designer/formdesigner/img/trash.png';
		// 绑定事件
		this.canvas.onmousedown = this.mouseDown;
		this.canvas.onmouseup = this.mouseUp;
		this.canvas.onmousemove = this.mouseMove;

		window.addEventListener('keydown', this.designer_canvas_keyDown, true);
	},
	initPropertyGrid : function() {
		$(this.propertyGridId).propertygrid({
			url : '',
			showGroup : true,
			border : false,
			fit : 'true',
			columns : [ [ {
				field : 'name',
				title : '属性',
				width : 100,
				sortable : true
			}, {
				field : 'value',
				title : '值',
				width : 160,
				resizable : false
			} ] ],
			toolbar : [ {
				iconCls : 'icon-ok',
				text : '提交',
				handler : function() {
					$('#prop').propertygrid('endEdit');
					changeProperties();
					formchanged();
				}
			}, '-', {
				iconCls : 'icon-undo',
				text : '还原',
				handler : function() {
					$('#prop').propertygrid('endEdit');
				}
			} ],
			onBeforeEdit : function(rowIndex, rowData) {
			},
			onClickRow : function(rowIndex, rowData) {

			}
		});
		
		this.loadProperty(this.propertyTemplate.form, this.properties.form);
	},
	loadModel : function(model) {

		this.variables.opCtlTotal = model.optionalControls.length; // 设置总数量
		this.variables.opCtlTotalPage = Math // 设置总页数
		.floor(model.optionalControls.length / this.variables.opCtlPageSize);
		if (model.optionalControls.length % this.variables.opCtlPageSize > 0) {
			this.variables.opCtlTotalPage += 1;
		}
		// 加载模型
		var md = $.extend({}, this.baseDataModel, model);
		this.dataModel = md;
		console.log("do loadModel");

		this.loadProperty(this.propertyTemplate.form, this.properties.form);
		this.paint();

	},
	parseXml : function(xml) {
		// 解析xml文件
	},

	getImageByType : function(type) {
		switch (type) {
		case 'combobox':
		case 'combotree':
		case 'enumbox':
			return this.imgs.comboIcon;
			break;
		case 'datebox':
		case 'timebox':
			return this.imgs.calendarIcon;
			break;
		case 'searchbox':
			return this.imgs.searchIcon;
			break;
		default:
			null;
		}
	},
	getControlByPosition : function(row, col) {
		var ctls = this.dataModel.controls;
		for ( var i in ctls) {
			if (ctls[i].row == row) {
				// 发现控件
				if (ctls[i].col == col) {
					return ctls[i];
				}
				if ((+ctls[i].col) + (+ctls[i].colspan) - 1 >= col
						&& (+ctls[i].col) <= col) {
					return ctls[i];
				}
			}
		}
		return null;
	},
	loadProperty : function(propTmpl, data, control) {

		for ( var i in propTmpl) {
			propTmpl[i].value = data[propTmpl[i].field];
		}

		var modelPack = {};
		modelPack.rows = propTmpl;
		modelPack.total = propTmpl.length;
		$(this.propertyGridId).propertygrid('loadData', modelPack);
	},
	properties : {

		form : {
			schemaName : '新界面方案',
			colCount : 2
		}
	},
	propertyTemplate : {
		common : [ {
			"name" : "中文名称",
			"value" : "",
			"group" : "基本属性",
			"editor" : {
				type : 'textbox',
				options : {
					readonly : true
				}
			},
			"field" : "label"
		}, {
			"name" : "字段",
			"value" : "",
			"group" : "基本属性",
			"editor" : {
				type : 'textbox',
				options : {
					readonly : true
				}
			},
			"field" : "name"
		}, {
			"name" : "非空标识",
			"value" : "否",
			"group" : "基本属性",
			"editor" : {
				"type" : 'checkbox',
				"options" : {
					"on" : "是",
					"off" : "否"
				}
			},
			"field" : "notNull"
		}, {
			"name" : "唯一性标识",
			"value" : "否",
			"group" : "基本属性",
			"editor" : {
				"type" : 'checkbox',
				"options" : {
					"on" : "是",
					"off" : "否"
				}
			},
			"field" : "isUnique"
		}, {
			"name" : "类型",
			"value" : "",
			"group" : "控件属性",
			"editor" : {
				type : 'combobox',
				options : {
					valueField : 'code',
					textField : 'text',
					data : [ {
						code : 'textbox',
						text : '文本框 - textbox'
					}, {
						code : 'numberbox',
						text : '数字框 - numberbox'
					}, {
						code : 'combobox',
						text : '下拉框 - combobox'
					}, {
						code : 'enumbox',
						text : '枚举框 - enumbox'
					}, {
						code : 'searchbox',
						text : '搜索框 - searchbox'
					}, {
						code : 'datebox',
						text : '日期框 - datebox'
					}, {
						code : 'timebox',
						text : '时间框 - timebox'
					}, {
						code : 'textarea',
						text : '文本区域 - textarea'
					} ]
				}
			},
			"field" : "type"
		}, {
			"name" : "行位置",
			"value" : "",
			"group" : "位置属性",
			"editor" : {
				type : 'textbox',
				options : {
					readonly : true
				}
			},
			"field" : "row"
		}, {
			"name" : "列位置",
			"value" : "",
			"group" : "位置属性",
			"editor" : {
				type : 'textbox',
				options : {
					readonly : true
				}
			},
			"field" : "col"
		}, {
			"name" : "跨列数",
			"value" : "",
			"group" : "位置属性",
			"editor" : {
				type : 'numberbox',
				options : {
					precision : 0
				}
			},
			"field" : "colspan"
		} ],

		form : [ {
			"name" : "方案名称",
			"value" : "",
			"group" : "基本属性",
			"editor" : "text",
			"field" : "schemaName"
		}, {
			"name" : "列数",
			"value" : 0,
			"group" : "基本属性",
			"editor" : "numberbox",
			"field" : "colCount"
		} ]
	}

};

function viewXml() {
	var xml = window.formdesigner_.toXml();
	document.getElementById("xmltext").value = xml;
	$('#xmlDialog').window('open');
}
