/**
 * @Description: 信息公开
 * @author 张田田
 * @date 2016-09-10 
 */
var urls = {
	queryUrl : "CasePublishController_query.do",
	noticeContentUrl : "CasePublishController_noticeContent.do"
	// noticeContentUrl : "CasePublishController_showFile.do"
};

var panel_ctl_handles = [{
	panelname:"caseReqPanel",
	gridname:"casePublishDataGrid" 	// 刷新操作函数
}];

$(function () 
{
	comboboxFuncByCond("doctype", "DOCTYPE");
	
	var icons = {iconCls:"icon-clear"};
	$("#doctype").combobox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	
	loadCasePublishDataGrid(urls.queryUrl);
});

var casePublishDataGrid;
function loadCasePublishDataGrid(url)
{
	casePublishDataGrid = $("#casePublishDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:15,
		pageList: [15],
		queryParams:{
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:false,
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"title", title:"标题", halign:"center", align:"left", width:800, sortable:true},
		  {field:"opttime", title:"发布日期", halign:"center", align:"left", width:140, sortable:true},
		  {field:"doctype", title:"", halign:"center", width:0, sortable:true, hidden:true}
        ]]
	});
}

/**
 * 条件查询
 */
function casePublishQuery()
{
	var param = {
		doctype:$("#doctype").combobox("getValue"),
		appname: $("#appname").val(),
		defname: $("#defname").val()
	};
	casePublishDataGrid.datagrid("load", param);
}

/**
 * 展示
 */
function show()
{
	var row = casePublishDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据");
		return;
	}
	
	parent.$.modalDialog({
		title:"通知书",
		width:800,
		height:600,
		href: urls.noticeContentUrl + "?caseid=" + row[0].caseid + "&doctype=" + row[0].doctype,
		// href: urls.noticeContentUrl + "?noticeid=" + row[0].noticeid,
		onLoad:function () {
			var mdDialog = parent.$.modalDialog.handler;
			mdDialog.find("input").attr("readonly", "readonly");
		},
		buttons:[{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function()
				{
					parent.$.modalDialog.handler.dialog('close');
					var fso = new ActiveXObject("Scripting.FileSystemObject");
    				// 删除文件
    				fso.DeleteFolder ("$contextpath/component/ronline/upload/tmp.doc");  
				}
			}]
	});
}