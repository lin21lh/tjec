/**
 * @Description: 信息公开
 * @author 张田田
 * @date 2016-09-10 
 */
var urls = {
		gridUrl: "CasePubWebController_query.do",			
		viewUrl: "CasePubWebController_view.do",
		getClobContentVal: "CasePubWebController_getClobContentVal.do"
};

var panel_ctl_handles = [{
	panelname:"#caseReqPanel",
	gridname:"#casePublishDataGrid" 	// 刷新操作函数
}];

$(function () 
{
	var icons = {iconCls:"icon-clear"};
	$("#starttime").datebox("addClearBtn", icons);
	$("#endtime").datebox("addClearBtn", icons);
	
	loadCasePublishDataGrid(urls.gridUrl);
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
	var starttime = $("#starttime").datebox("getValue");
	var endtime = $("#endtime").datebox("getValue");
	
	if(endtime < starttime && endtime != "")
	{
		easyui_warn("结束时间应大于开始时间！", null);
		return;
	}
	
	var param = {
		starttime: starttime,
		endtime: endtime
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
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var icon = 'icon-view';
	parent.$.modalDialog({
		title: "详情",
		iconCls: icon,
		width: 900,
		height: 600,
		href: urls.viewUrl,
		onLoad: function()
		{
			 var mdDialog = parent.$.modalDialog.handler;
			//获取内容信息，在div中展示
			$.post(urls.getClobContentVal, {conid:row[0].conid}, function(data){
				mdDialog.find("#content").html(data);
			}, "json");
		},
		buttons: [{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
	});
}