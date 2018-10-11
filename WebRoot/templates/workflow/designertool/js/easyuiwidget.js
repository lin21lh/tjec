function initEasyuiWidget() {

	activeLine = null;
	$('#widgets')
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
								opMode = 'select';
								drawVirtualBox.visible = false;
							} else if (rowData.name == 'transition') {
								opMode = 'transitionCreate';
							} else {
								opMode = 'activityCreate';
								toolbox.widget = rowData.name;
							}
							console.log('change to mode : ' + opMode);
						},
						onLoadSuccess : function() {

							$('#widgets').datagrid('selectRow', 0);
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
	$('#widgets').datagrid('loadData', data);
	$('#prop').propertygrid({
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
		toolbar : [ {
			iconCls : 'icon-ok',
			handler : function() {
				$('#prop').propertygrid('endEdit');
				changeProperties();
				propEditing = false;
			}
		}, '-', {
			iconCls : 'icon-undo',
			handler : function() {
				$('#prop').propertygrid('endEdit');
				showProperties(activeObjectName);
				propEditing = false;
			}
		} ],
		onBeforeEdit : function(rowIndex, rowData) {
			propEditing = true;
		},
		onClickRow : function(rowIndex, rowData) {
			$('#prop').propertygrid('beginEdit', rowIndex);
			eventFlag = rowData.field;
		}
	});
	showProcessProp();

}

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
	"name" : "表达式",
	"value" : "",
	"group" : "受托人属性",
	"editor" : "text",
	"field" : "assignee"
}, {
	"name" : "分配器类型",
	"value" : "",
	"group" : "受托分配器属性",
	"editor" : "text",
	"field" : "assignmentHandler"
}, {
	"name" : "参数",
	"value" : "",
	"group" : "受托分配器属性",
	"editor" : "text",
	"field" : "assignmentparam"
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
	"name" : "处理页面",
	"value" : "",
	"group" : "处理页面属性",
	"editor" : "text",
	"field" : "pageUrl"
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
} ];

var decision_prop = [ {
	"name" : "表达式",
	"value" : "",
	"group" : "逻辑属性",
	"editor" : {
		type : 'condtionsBox'
	},
	"field" : "expr"
}, {
	"name" : "Handler",
	"value" : "",
	"group" : "逻辑属性",
	"editor" : "text",
	"field" : "handler"
} ];

var decision_tran_prop = [ {
	"name" : "表达式",
	"value" : "",
	"group" : "逻辑属性",
	"editor" : "text",
	"field" : "expr"
} ];

/**
 * 显示组件的属性
 * 
 * @param widgetName
 */
function showProperties(actiName) {
	propEndEdit();
	oldActiName = actiName;
	var actiType = actiMap[actiName].tagName;
	var propModel = getPropertyModel(actiType);
	fillData(actiMap[actiName], propModel);
	displayProperties(propModel);
}
/**
 * 取得活动节点组件的属性模型
 * 
 * @param actiType
 *            活动节点组件的类型
 * @returns 属性模型
 */
function getPropertyModel(actiType) {
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
}

/**
 * 填充模型数据
 * 
 * @param actiObj
 * @param propModel
 */
function fillData(actiObj, propModel) {
	for ( var i in propModel) {
		if (propModel[i] instanceof Function)
			continue;

		propModel[i].value = actiObj[propModel[i].field];
	}
}

function displayProperties(propModel) {
	var modelPack = {};
	modelPack.rows = propModel;
	modelPack.total = propModel.length;
	$('#prop').propertygrid('loadData', modelPack);
}
/**
 * 显示流程的属性
 */
function showProcessProp() {
	var propModel = process_prop;
	for ( var i in propModel) {
		if (propModel[i] instanceof Function)
			continue;

		propModel[i].value = processInfo[propModel[i].field];
	}
	displayProperties(propModel);
}

/**
 * 显示迁移线的属性
 */
function showTransitionProp(activeLineArg) {

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
		}
	}
	$('#prop').propertygrid('loadData', modelPack);
}

var roleTreeSelection = null;

var opGridSelection = null;

function propEndEdit() {
	$('#prop').propertygrid('endEdit');
	propEditing = false;
}

function conditions_onclick(cond, transition_names, decisionname) {
	decisionname = encodeURI(decisionname);
	decisionname = encodeURI(decisionname);
	parent.$
			.modalDialog({
				title : '执行动作条件',
				width : 900,
				height : 640,
				href : contextpath
						+ 'workflow/WfProcessDefinitionController/wfDataScopeEntry.do?transition_names='
						+ transition_names + '&wfkey=' + processInfo.key
						+ '&wfversion=' + processInfo.version
						+ '&&decisionname=' + decisionname,
				onLoad : function() {
				}
			});
}

function privalige_onclick(ele) {
	parent.$.modalDialog({
		title : '工作流角色选择',
		width : 500,
		height : 400,
		href : 'workflow/WfProcessDefinitionController/wfPrivilegeDialog.do',
		onLoad : function() {
			roleTreeSelection = null;
			var roletree = parent.$.modalDialog.handler.find('#priv_roletree');
			roletree.tree({
				url : 'sys/SysRoleController/query.do',
				onClick : function(node) {
					roleTreeSelection = {
						text : node.text,
						id : node.id
					};
				}
			});
		},
		buttons : [ {
			text : "确定",
			iconCls : "icon-ok",
			handler : function() {
				if (roleTreeSelection == null) {
					easyui_warn('请选择一个角色!');
					return;
				}
				$(ele).val(roleTreeSelection.text);
				parent.$.modalDialog.handler.dialog('close');

				propEndEdit();
				if (activeObjectName) {
					var acti = actiMap[activeObjectName];
					acti.privilege = roleTreeSelection.text;
					acti.privilegeid = roleTreeSelection.id;
				}
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function event_onclick(ele) {

	var sel = getTreeSelect();

	var par = $('#wftree').tree('getParent', sel.target);

	parent.$
			.modalDialog({
				title : '工作流事件对应操作选择',
				width : 300,
				height : 200,
				href : 'workflow/WfProcessDefinitionController/wfOpSelDialog.do',
				onLoad : function() {
					opGridSelection = null;
					var sel_grid = parent.$.modalDialog.handler
							.find('#oper_select_grid');
					sel_grid
							.datagrid({
								url : 'workflow/WfProcessDefinitionController/findWorkflowOpdef.do'
										+ '?key=' + par.key,
								columns : [ [ {
									field : 'ck',
									checkbox : true
								}, {
									field : 'name',
									title : '操作名称',
									width : 210
								} ] ],
								onSelect : function(ridx, data) {
									opGridSelection = {
										code : data.code,
										name : data.name
									};
								},
								rownumbers : true,
								singleSelect : true,
								fit : true
							});
				},
				buttons : [
						{
							text : "确定",
							iconCls : "icon-ok",
							handler : function() {
								if (opGridSelection == null) {
									easyui_warn('请选择一个操作!');
									return;
								}
								$(ele).val(opGridSelection.name);
								parent.$.modalDialog.handler.dialog('close');

								propEndEdit();
								if (activeObjectName) {
									var acti = actiMap[activeObjectName];
									// 判断是start event还是end event
									if (eventFlag == 'startEvent'
											|| eventFlag == "endEvent") {
										acti[eventFlag] = opGridSelection.name;
									} else {
										alert('错误的事件类型:' + eventFlag);
									}
								} else if (activeLine) {
									var from_ = activeLine.from;
									for ( var i in from_.transition) {
										if (from_.transition[i] instanceof Function)
											continue;

										if (from_.transition[i].to == activeLine.to) {
											from_.transition[i].event = opGridSelection.name;
										}
									}
								}
							}
						},
						{
							text : "清空",
							iconCls : "icon-reload",
							handler : function() {

								$(ele).val("");
								parent.$.modalDialog.handler.dialog('close');
								propEndEdit();
								if (activeObjectName) {
									var acti = actiMap[activeObjectName];
									// 判断是start event还是end event
									if (eventFlag == 'startEvent'
											|| eventFlag == "endEvent") {
										acti[eventFlag] = undefined;
									} else {
										alert('错误的事件类型:' + eventFlag);
									}
								} else if (activeLine) {
									var from_ = activeLine.from;
									for ( var i in from_.transition) {
										if (from_.transition[i] instanceof Function)
											continue;
										if (from_.transition[i].to == activeLine.to) {
											from_.transition[i].event = undefined;
										}
									}
								}
							}
						}, {
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.modalDialog.handler.dialog('close');
							}
						} ]
			});
}
