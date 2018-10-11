WorkflowDesigner.prototype._initToolbox = function() {

	var handle = this;
	$(this.toolboxGridId)
			.datagrid(
					{
						idField : 'name',
						columns : [ [ {
							field : 'cnname',
							title : '组件',
							width : 107,
							formatter : function(val, row) {
								return "<img class='activity_icon' src='../../templates/workflow/designertool/res/icon/"
										+ row.name + ".png' />" + val;
							}
						} ] ],
						width : 110,
						height : document.body.clientHeight - 70,
						view : groupview,
						groupField : 'group',
						groupFormatter : function(value, rows) {
							return value;
						},
						singleSelect : true,
						onClickRow : function(rowIndex, rowData) {

							if (rowData.name == 'select') {
								handle.opMode = 'select';
								handle.drawVirtualBox.visible = false;
							} else if (rowData.name == 'transition') {
								handle.opMode = 'transitionCreate';
							} else {
								handle.opMode = 'activityCreate';
								handle.toolbox.widget = rowData.name;
							}
							console.log('change to mode : ' + handle.opMode);
						},
						onLoadSuccess : function() {
							$(this.toolboxGridId).datagrid('selectRow', 0);
						}
					});
	var data = [ {
		name : 'select',
		cnname : '选择',
		group : '操作'
	}, {
		name : 'transition',
		cnname : '迁移线',
		group : '组件'
	}, {
		name : 'start',
		cnname : '开始',
		group : '组件'
	}, {
		name : 'end',
		cnname : '结束',
		group : '组件'
	}, {
		name : 'state',
		cnname : '状态',
		group : '组件'
	}, {
		name : 'task',
		cnname : '任务',
		group : '组件'
	}, {
		name : 'decision',
		cnname : '判断',
		group : '组件'
	}, {
		name : 'rule',
		cnname : '规则',
		group : '组件'
	}, {
		name : 'fork',
		cnname : '分支',
		group : '组件'
	}, {
		name : 'join',
		cnname : '合并',
		group : '组件'
	} ];
	$(this.toolboxGridId).datagrid('loadData', data);
};
/**
 * 初始化property grid
 */
WorkflowDesigner.prototype._initPropertyGrid = function() {
	var handle = this;

	var toolbar = null;
	if (this.editMode == "NEW") {
		toolbar = [ {
			iconCls : 'icon-ok',
			handler : function() {
				$(handle.propGirdId).propertygrid('endEdit');
				handle._changeProperties();
				handle.propEditing = false;
			}
		}, '-', {
			iconCls : 'icon-undo',
			handler : function() {
				$(handle.propGirdId).propertygrid('endEdit');
				handle._showProperties(handle.activeObjectName);
				handle.propEditing = false;
			}
		} ];
	} else if (this.editMode == "UPDATE") {
		toolbar = [ {
			iconCls : 'icon-save',
			handler : function() {
				$(handle.propGirdId).propertygrid('endEdit');
				handle._changeProperties();
				handle.propEditing = false;
				// 立刻提交更改到服务端
				// handle._commitChangesToServer();
			}
		}, '-', {
			iconCls : 'icon-undo',
			handler : function() {
				$(handle.propGirdId).propertygrid('endEdit');
				handle._showProperties(handle.activeObjectName);
				handle.propEditing = false;
			}
		} ];
	}
	$(this.propGirdId).propertygrid({
		url : '',
		showGroup : true,
		width : 220,
		height : document.body.clientHeight - 70,
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
		toolbar : toolbar,
		onBeforeEdit : function(rowIndex, rowData) {
			handle.propEditing = true;
		},
		onClickRow : function(rowIndex, rowData) {
			$(handle.propGirdId).propertygrid('beginEdit', rowIndex);
			handle.eventFlag = rowData.field;
		}
	});
};
var base_prop = [ {
	"name" : "ID",
	"value" : "",
	"group" : "基本属性",
	"editor" : "text",
	"field" : "name"
}, {
	"name" : "标签",
	"value" : "",
	"group" : "基本属性",
	"editor" : "text",
	"field" : "description"
}, {
	"name" : "x",
	"value" : 0,
	"group" : "坐标属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "x"
}, {
	"name" : "y",
	"value" : 0,
	"group" : "坐标属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "y"
}, {
	"name" : "w",
	"value" : 0,
	"group" : "坐标属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "w"
}, {
	"name" : "h",
	"value" : 0,
	"group" : "坐标属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "h"
} ];

var process_prop = [ {
	"name" : "名称",
	"value" : "",
	"group" : "流程属性",
	"editor" : "text",
	"field" : "name"
}, {
	"name" : "标识",
	"value" : "",
	"group" : "流程属性",
	"editor" : "text",
	"field" : "key"
}, {
	"name" : "版本",
	"value" : 1,
	"group" : "流程属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "version"
}, {
	"name" : "首节点ID",
	"value" : "",
	"group" : "流程属性",
	"editor" : "text",
	"field" : "firstnode"
},{
	"name" : "启用日期",
	"value" : "",
	"group" : "流程属性",
	"editor" : {
		type : 'datebox'
	},
	"field" : "startdate"
}, {
	"name" : "停用日期",
	"value" : "",
	"group" : "流程属性",
	"editor" : {
		type : 'datebox'
	},
	"field" : "enddate"
} ];

var task_prop = [ {
	"name" : "分配器类型",
	"value" : "",
	"group" : "受托分配器属性",

	"field" : "assignmentHandler",
	"editor" : {
		type : 'combotree',
		options : {
			data : [ {
				id : '角色分配器',
				text : '角色分配器'
			}, {
				id : '流程发起人分配器',
				text : '流程发起人分配器'
			}, {
				id : '固定用户分配器',
				text : '固定用户分配器'
			} ]
		}
	}
}, {
	"name" : "参数",
	"value" : "",
	"group" : "受托分配器属性",
	"editor" : {
		type : 'assignBox'
	},
	"field" : "assignmentArgs"
}, {
	"name" : "操作",
	"value" : "",
	"group" : "开始事件属性",
	"editor" : {
		type : 'eventBox'
	},
	"field" : "startEvent"
}, {
	"name" : "参数",
	"value" : "",
	"group" : "开始事件属性",
	"editor" : "text",
	"field" : "startArgs"
}, {
	"name" : "操作",
	"value" : "",
	"group" : "结束事件属性",
	"editor" : {
		type : 'eventBox'
	},
	"field" : "endEvent"
}, {
	"name" : "参数",
	"value" : "",
	"group" : "结束事件属性",
	"editor" : "text",
	"field" : "endArgs"
}, {
	"name" : "是否可退回",
	"value" : "",
	"group" : "退回撤回属性",
	"editor" : {
		"type" : "checkbox",
		"options" : {
			"on" : true,
			"off" : false
		},
	},
	"field" : "returnable"
}, {
	"name" : "是否可撤回",
	"value" : "",
	"group" : "退回撤回属性",
	"editor" : {
		"type" : "checkbox",
		"options" : {
			"on" : true,
			"off" : false
		}
	},
	"field" : "withdrawable"

}, {
	"name" : "可退首节点",
	"value" : "",
	"group" : "退回撤回属性",
	"editor" : {
		"type" : "checkbox",
		"options" : {
			"on" : true,
			"off" : false
		}
	},
	"field" : "bkfirstnode"
},{
	"name" : "处理页面",
	"value" : "",
	"group" : "处理页面属性",
	"editor" : "text",
	"field" : "pageUrl"
}, {
	"name" : "表单编辑",
	"value" : "",
	"group" : "业务表单属性",
	"editor" : {
		"type" : "checkbox",
		"options" : {
			"on" : true,
			"off" : false
		}
	},
	"field" : "formeditable"
} ];

var transition_prop = [ {
	"name" : "名称",
	"value" : "",
	"group" : "迁称线属性",
	"editor" : "text",
	"field" : "name"
}, {
	"name" : "x",
	"value" : 0,
	"group" : "坐标属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "x"
}, {
	"name" : "y",
	"value" : 0,
	"group" : "坐标属性",
	"editor" : {
		type : 'numberbox'
	},
	"field" : "y"
}, {
	"name" : "操作",
	"value" : "",
	"group" : "事件属性",
	"editor" : {
		type : 'eventBox'
	},
	"field" : "event"
}, {
	"name" : "参数",
	"value" : "",
	"group" : "事件属性",
	"editor" : "text",
	"field" : "args"
}, {
	"name" : "过后状态",
	"value" : "",
	"group" : "过后业务数据状态",

	"field" : "donestatus",
	"editor" : {
		type : 'combotree',
		
		options : {
		url:contextpath+'/base/dic/dicElementValSetController/queryByElementcode.do?elementcode=WFSTATUS&idColumn=code'

		}
	}
} ];

var decision_prop = [ {
	"name" : "处理器类型",
	"value" : "",
	"group" : "分支逻辑属性",
	"editor" : {
		type : 'combotree',
		options : {
			data : [ {
				id : '自定义',
				text : '自定义'
			} ]
		}
	},
	"field" : "decisionHandlerType"
}, {
	"name" : "处理器参数",
	"value" : "",
	"group" : "分支逻辑属性",
	"editor" : "text",
	"field" : "decisionHandlerParam"
} ];

var decision_tran_prop = [ {
	"name" : "表达式",
	"value" : "",
	"group" : "逻辑属性",
	"editor" : "text",
	"field" : "expr"
} ];
/**
 * 显示流程属性
 */
WorkflowDesigner.prototype._showProcessProp = function() {
	var propModel = process_prop;
	for ( var i in propModel) {
		if (propModel[i] instanceof Function)
			continue;

		propModel[i].value = this.processInfo[propModel[i].field];
	}
	this._displayProperties(propModel);
};
/**
 * 显示属性到property grid
 * 
 * @param propModel
 */
WorkflowDesigner.prototype._displayProperties = function(propModel) {
	var modelPack = {};
	modelPack.rows = propModel;
	modelPack.total = propModel.length;
	$(this.propGirdId).propertygrid('loadData', modelPack);
};

WorkflowDesigner.prototype._propEndEdit = function() {
	$(this.propGirdId).propertygrid('endEdit');
	this.propEditing = false;
};

/**
 * 取得活动节点组件的属性模型
 * 
 * @param actiType
 *            活动节点组件的类型
 * @returns 属性模型
 */
WorkflowDesigner.prototype._getPropertyModel = function(actiType) {
	var model = new Array();
	switch (actiType) {
	case 'task':
		return model.concat(base_prop, task_prop);
		break;
	case 'decision':
		return model.concat(base_prop, decision_prop);
		break;
	default:
		return model.concat(base_prop);
		break;
	}
};

/**
 * 填充模型数据
 * 
 * @param actiObj
 * @param propModel
 */
WorkflowDesigner.prototype._fillData = function(actiObj, propModel) {
	for ( var i in propModel) {
		if (propModel[i] instanceof Function)
			continue;
		propModel[i].value = actiObj[propModel[i].field];
	}
};

/**
 * 显示迁移线的属性
 */
WorkflowDesigner.prototype._showTransitionProp = function(activeLineArg) {

	var modelPack = {};
	modelPack.rows = new Array();

	var acti = activeLineArg.from;
	if (acti.tagName == 'decision') {
		modelPack.rows = modelPack.rows.concat(transition_prop,
				decision_tran_prop);
	} else {
		modelPack.rows = modelPack.rows.concat(transition_prop);
	}

	modelPack.total = modelPack.rows.length;
	for ( var i in modelPack.rows) {
		if (modelPack.rows[i] instanceof Function)
			continue;

		switch (modelPack.rows[i].field) {
		case 'name':
			modelPack.rows[i].value = activeLineArg.name;
			break;
		case 'x':
			modelPack.rows[i].value = activeLineArg.transition.text.relativeX;
			break;
		case 'y':
			modelPack.rows[i].value = activeLineArg.transition.text.relativeY;
			break;
		case 'expr':
			modelPack.rows[i].value = activeLineArg.transition.expr;
			break;
		case 'args':
			modelPack.rows[i].value = activeLineArg.transition.args;
			break;
		case 'event':
			modelPack.rows[i].value = activeLineArg.transition.event;
			break;
		case 'donestatus':
			modelPack.rows[i].value = activeLineArg.transition.donestatus;
			break;
		}
	}
	$(this.propGirdId).propertygrid('loadData', modelPack);
};
/**
 * 在editMode为update时立即提交修改到服务端
 */
WorkflowDesigner.prototype._commitChangesToServer = function(category,
		actiName, transName, fieldName, fieldValue) {
	console.log("保存属性：" + category + " - " + fieldName + " - " + fieldValue);
	var obj = {
		key : this.processInfo.key,
		version : this.processInfo.version,
		category : category,
		actiName : actiName,
		transName : transName,
		fieldName : fieldName,
		fieldValue : fieldValue
	};
	var handle = this;
	$.post("workflowExtAttrUpdate.do", obj, function(result) {
		if (result.success) {
			$(handle.propGirdId).propertygrid('acceptChanges', 'updated');
			handle.info(result.title);
		} else {
			handle.warn(result.title);
		}
	}, "JSON");

};
