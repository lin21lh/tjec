/**
 * @Description: 通知书跟踪js
 * @author 张田田
 * @date 2016-08-26 
 */
//请求路径
var baseUrl = contextpath + "aros/noticefollow/controller/NoticeFollowController/";
var urls =
{
	// 查询已生成的通知书列表
	queryUrl:baseUrl + "queryNotice.do",
	// 文书发送维护页面
	sendManageUrl:baseUrl + "sendManage.do",
	// 文书送达维护页面
	receiveManageUrl:baseUrl + "receiveManage.do",
	// 保存
	saveUrl:baseUrl + "save.do"
};
var panel_ctl_handles = [{
	// 要折叠的面板id
	panelname:"#noticePanel",
	// 刷新操作函数
	gridname:"#noticeDataGrid"
}];
var noticeDataGrid;

// 默认加载
$(function() {	
	comboboxFuncByCondFilter(menuid, "doctype", "DOCTYPE", "code", "name");
	var icons = {iconCls:"icon-clear"};
	$("#doctype").combobox("addClearBtn", icons);
	$("#intro").textbox("addClearBtn", icons);
	$("#orgperson").textbox("addClearBtn", icons);
	
	//加载grid数据
	loadNoticeGrid(urls.queryUrl);
});

//加载grid列表
function loadNoticeGrid(url)
{
	noticeDataGrid = $("#noticeDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"id", checkbox:true},
		  {field:"intro", title:"案件名称", halign:"center", width:400, sortable:true},
		  {field:"doctypename", title:"通知书类型", halign:"center", width:200, sortable:true},
		  {field:"orgperson", title:"受送达人", halign:"center", width:200, sortable:true},		  
		  {field:"deliverydate", title:"送达日期", halign:"center", width:100, sortable:true},
		  {field:"noticeid", title:"通知书id", halign:"center", ortable:true, hidden:true},
		  {field:"doctype", title:"通知书类型编码", halign:"center", sortable:true, hidden:true}
        ]]
	});
}

/**
 * 条件查询
 */
function noticeQuery()
{
	var param = {
		// 通知书类型
		doctype:$("#doctype").combobox("getValue"),
		// 案件名称
		intro:$("#intro").val(),
		// 受送达人
		orgperson:$("#orgperson").val()
	};
	
	noticeDataGrid.datagrid("load", param);
}

/**
 * 重新加载grid
 */
function showReload()
{
	noticeDataGrid.datagrid("reload");
}

/**
 * 发送文书维护
 */
function sendManage()
{
	showMdDialog("send", "文书发送维护", 800, 300, urls.sendManageUrl, 'sendForm');
}

/**
 * 送达通知书维护
 */
function receiveManage()
{
	showMdDialog("receive", "文书送达维护", 800, 300, urls.receiveManageUrl, 'receiveForm');
}

/**
 * 弹窗
 */
function showMdDialog (type,title, width, height, href, form)
{
	var selectRow = noticeDataGrid.datagrid('getChecked');
	if(1 != selectRow.length)
	{
			easyui_warn("请先选择一条数据！", null);
			return;
	}
	
	var id = selectRow[0].id;
	if ("receive" == type)
	{
		if (undefined == id || null == id || "" == id)
		{
			easyui_warn("请先进行发送维护，再进行送达维护！", null);
			return;
		}
	}
	
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: href + "?id=" + id,
		onLoad:function()
		{
			var mdDialog = parent.$.modalDialog.handler;
			if ("send" == type)
			{
				// 送达方式
				comboboxFuncByCondFilter(menuid, "deliverytype", "DELIVERYTYPE", "code", "name", mdDialog);
			}
			mdDialog.find('#' + form).form("load", selectRow[0]);
		},
		buttons: [{
			text:"保存",
			iconCls:"icon-save",
			handler:function()
			{
				submitForm(urls.saveUrl, form);
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function()
			{
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
	});
}

/**
 * 提交表单
 */
function submitForm(url, form)
{
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid)
	{
		return;
	}
	
	form.form("submit", {
		url: url,
		onSubmit: function() {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid)
			{
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			
			if (result.success)
			{
				parent.$.modalDialog.handler.dialog('close');
				showReload();
			}
			else
			{
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}
