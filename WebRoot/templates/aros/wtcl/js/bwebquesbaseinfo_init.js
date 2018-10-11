var baseUrl = contextpath + "aros/wtcl/BWebquesbaseinfoController/";

var projectDataGrid;
var datagrid;
var urls = {
	queryUrl : "BWebquesbaseinfoController_queryList.do",
	addUrl : "BWebquesbaseinfoController_add.do",
	updateUrl : baseUrl + "add.do",
	saveUrl : "BWebquesbaseinfoController_save.do",
	deleteUrl : baseUrl + "delete.do",
	viewUrl : "BWebquesbaseinfoController_view.do",
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
	loadProjectGrid(urls.queryUrl);
	//在查询上默认添加清除按钮
	var icons = {iconCls:"icon-clear"};
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
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		height:570,
		pageSize : 15,
		pageList :[15],
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
			width : 640,
			sortable : true
		}, {
			field : "asktime",
			title : "提出日期",
			halign : 'center',
			align : 'left',
			width : 150,
			sortable : true
		}, {
			field : "opttime",
			title : "回复日期",
			halign : 'center',
			align : 'left',
			width : 150,
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
		quesdesc : $("#quesdesc").val(),
		menuid:1
	};
	
	projectDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function projectAdd() {
	
	$.modalDialog({
		title : "问题编辑",
		width : 800,
		height : 190,
		href : urls.addUrl,		
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = $.modalDialog.handler;
				submitForm(urls.saveUrl, "projectAddForm", "");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

// 提交表单
function submitForm(url, f, workflowflag) {
	var form = $.modalDialog.handler.find('#' + f);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
	form.form("submit", {
		url : url,
		onSubmit : function() {
			$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid) {
				$.messager.progress('close');
			}
			return isValid;
		},
		success : function(result) {
			$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {

				projectDataGrid.datagrid('reload');
				$.modalDialog.handler.dialog('close');
			} else {
				$.messager.alert('错误', result.title, 'error');
			}
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
		title : "问题详情",
		width : 800,
		height : 500,
		href : urls.viewUrl + "?id=" + row.quesid,
		onLoad : function() {
			var mdDialog = $.modalDialog.handler;			
			var f = $.modalDialog.handler.find('#projectViewForm');
			f.form("load", row);
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				$.modalDialog.handler.dialog('close');
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
