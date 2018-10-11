var baseUrl = contextpath + "aros/ajpj/BCaseestbaseinfoController/";

var projectDataGrid;
var datagrid;

var urls = {
	queryUrl : baseUrl + "queryList.do",
	addUrl : baseUrl + "add.do",
	updateUrl : baseUrl + "add.do",
	saveUrl : baseUrl + "save.do",
	deleteUrl : baseUrl + "delete.do",
	viewUrl : baseUrl + "view.do",
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
	
	alert("in");
	
	comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name");// 案件执行质量
	$("#quatype").combobox("addClearBtn", {iconCls:"icon-clear"});
	loadProjectGrid(urls.queryUrl);
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
		pageSize : 30,
		queryParams : {
			menuid : menuid
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {
			field : "id",
			checkbox : true
		}, {
			field : "caseid",
			title : "案件编号",
			halign : 'center',
			width : 250,
			sortable : true
		}, {
			field : "appname",
			title : "申请人",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "defname",
			title : "被申请人",
			halign : 'center',
			width : 250,
			sortable : true
		}, {
			field : "quatypename",
			title : "案件执行质量",
			halign : 'center',
			width : 250,
			sortable : true
		} ] ]
	});
}

/**
 * 查询
 */
function projectQuery() {
	var param = {
		quatype : $("#quatype").combobox('getValues').join(","),
		menuid : menuid
	};
	projectDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function projectAdd() {
	parent.$.modalDialog({
		title : "案件评价管理",
		width : 850,
		height : 600,
		href : urls.addUrl+"?caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量			
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

// 提交表单
function submitForm(url, form, workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + form);
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
	
	var row = projectDataGrid.datagrid("getSelections")[0];
	var id = row.id;	
	var caseid = row.caseid;
	
	parent.$.modalDialog({
		title : "委员信息修改",
		width : 850,
		height : 600,
		href : urls.updateUrl+"?id="+id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量						
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
				id : row.id
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
	var id = row.id;	
	var caseid = row.caseid;
	
	parent.$.modalDialog({
		title : "案件跟踪详情",
		width : 850,
		height : 600,
		href : urls.viewUrl + "?id=" + id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
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