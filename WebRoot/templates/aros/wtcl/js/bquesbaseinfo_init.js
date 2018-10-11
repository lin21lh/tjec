var baseUrl = contextpath + "aros/zjgl/controller/";

var projectDataGrid;
var datagrid;
var urls = {
	queryUrl : "BquesbaseinfoController_queryList.do",
	addUrl : baseUrl + "add.do",
	updateUrl : "BquesbaseinfoController_add.do",
	saveUrl : "BquesbaseinfoController_save.do",
	deleteUrl : baseUrl + "delete.do",
	viewUrl : baseUrl + "view.do",
	traUrl: contextpath + "aros/ajpj/BCaseestbaseinfoController/init.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

/**
 * 默认加载
 */
$(function() {
	comboboxFuncByCondFilter(menuid, "ifanswer", "ISORNOT", "code", "name");// 职务
	loadProjectGrid(urls.queryUrl);
	//在查询上默认添加清除按钮
	var icons = {iconCls:"icon-clear"};
	$("#ifanswer").combobox("addClearBtn", icons);
	$("#quesdesc").textbox("addClearBtn", icons);
});

/**
 * 页面刷新
 */
function showReload() {
	projectDataGrid.datagrid('reload');
}

/**
 * 加载可项目grid列表
 * 
 * @param url
 */
function loadProjectGrid(url) {
	projectDataGrid = $("#projectDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		height:586,
		pageSize : 30,
		queryParams : {
			menuid : menuid
		},
		url:url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {
			field : "quesid",
			checkbox : true
		}, {
			field : "quesdesc",
			title : "问题描述",
			halign : 'center',
			align : 'left',
			width : 800,
			sortable : true
		}, {
			field : "asktime",
			title : "提出日期",
			halign : 'center',
			align : 'left',
			width : 200,
			sortable : true
		}
		] ]
	});

}

/**
 * 查询
 */
function projectQuery() {
	var param = {
			ifanswer:$("#ifanswer").combobox('getValue'),
			quesdesc : $("#quesdesc").val(),
		menuid:1
	};
	
	projectDataGrid.datagrid("load", param);
}

// 提交表单
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

				projectDataGrid.datagrid('reload');
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

/**
 * 修改
 */
function projectUpdate() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	parent.$.modalDialog({
		title : "问题答疑",
		width : 800,
		height : 280,
		href : urls.updateUrl+"?operflag=edit",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();			
			var row = projectDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "projectAddForm", "");
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
function projectDelete() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = projectDataGrid.datagrid("getSelections")[0];
			$.post(urls.deleteUrl, {
				speid : row.speid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 详情
 */
function projectView() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "委员信息详情",
		width : 800,
		height : 460,
		href : urls.viewUrl + "?id=" + row.speid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			
			var f = parent.$.modalDialog.handler.find('#projectViewForm');
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
 * 案件评价
 */
function projectTrace() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	
	var row = projectDataGrid.datagrid("getSelections")[0];
	var caseid = "1000099";
	
	
	this.location.href=urls.traUrl+"?caseid="+caseid+"&menuid="+menuid;
	
	
}
