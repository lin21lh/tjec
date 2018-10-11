var baseUrl = contextpath + "aros/gzgl/controller/BRulebaseinfoController/";

var ruleInfoDataGrid;

var urls = {
	queryUrl : baseUrl + "queryList.do",
	addUrl   : baseUrl + "addBRuleInfo.do",
	saveUrl  : baseUrl + "saveRuleInfo.do",
	viewUrl  : baseUrl + "view.do",
	deleteUrl: baseUrl + "delete.do",
	redoUrl  : baseUrl + "redo.do"
	
};
var panel_ctl_handles = [ {
	panelname : '#queryPanel', // 要折叠的面板id
	buttonid:"#openclose"
} ];

/**
 * 默认加载
 */
$(function() {
	loadRuleInfoDataGrid(urls.queryUrl);
	comboboxFuncByCondFilter(menuid, "status", "RULE_STATUAS", "code", "name");
	//在查询上默认添加清除按钮
	var icons = {iconCls:"icon-clear"};
	$("#selectruelname").textbox("addClearBtn", icons);
	$("#status").combobox("addClearBtn", icons);
	$('#queryPanel').panel('close');
});

/**
 * 加载可项目grid列表
 * 
 * @param url
 */
function loadRuleInfoDataGrid(url) {
	ruleInfoDataGrid = $("#ruleInfoDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		pageSize : 30,
		queryParams : {
			menuid : menuid
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {
			field : "ruleid",
			checkbox : true
		}, {
			field : "rulename",
			title : "规则名称",
			halign : 'center',
			width : '30%',
			sortable : true
		}, {
			field : "statusname",
			title : "状态",
			halign : 'center',
			width : '10%',
			sortable : true
		}, {
			field : "tabname",
			title : "表名称",
			halign : 'center',
			width : '25%',
			sortable : true
		}, {
			field : "fieldname",
			title : "字段名称",
			halign : 'center',
			width : '20%',
			sortable : true
		}, {
			field : "limit",
			title : "期限",
			halign : 'center',
			width : '10%',
			sortable : true
		} ] ]
	});
}

/**
 * 查询
 */
function toQuery() {
	var param = {
		menuid : menuid,
		status : $("#status").combobox('getValue'),
		selectruelname : $("#selectruelname").val()
	};
	ruleInfoDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function ruleAdd() {
	parent.$.modalDialog({
		title : "新增规则",
		width : 800,
		height : 300,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			// comboboxFuncByCondFilter(menuid, "ifcheck", "ISORNOT", "code", "name", mdDialog);// 是否允许查看案件
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "ruleInfoForm", "");
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

/**
 * 修改
 */
function ruleUpdate() {
	var selectRow = ruleInfoDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	parent.$.modalDialog({
		title : "规则修改",
		width : 800,
		height : 300,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = ruleInfoDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#ruleInfoForm');
			f.form("load", row);
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				var id = mdDialog.find("#ruleid").val();
				submitForm(urls.saveUrl, "ruleInfoForm", "");
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

/**
 * 删除
 */
function ruleDelete() {
	var selectRow = ruleInfoDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = ruleInfoDataGrid.datagrid("getSelections")[0];
			$.post(urls.deleteUrl, {
				ruleid : row.ruleid
			}, function(result) {
				easyui_auto_notice(result, function() {
					ruleInfoDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 详情
 */
function ruleView() {
	var selectRow = ruleInfoDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var row = ruleInfoDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "规则详情",
		width : 800,
		height : 300,
		href : urls.viewUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var f = mdDialog.find('#ruleInfoForm');
			f.form("load", row);
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
/**
 * 发布规则
 */
function redo(){
	var selectRow = ruleInfoDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var row = ruleInfoDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认发布", "是否要发布选中数据？", function(r) {
		if (r) {
			$.post(urls.redoUrl, {
				ruleid : row.ruleid
			}, function(result) {
				easyui_auto_notice(result, function() {
					ruleInfoDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
	
	
}
//提交表单
function submitForm(url, f, workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + f);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit", {
		url : url,
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {

				ruleInfoDataGrid.datagrid('reload');
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}