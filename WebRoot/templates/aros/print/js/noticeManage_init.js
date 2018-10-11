/**
 * @Description: 通知书打印
 * @author 张田田
 * @date 2016-08-26 
 */
//请求路径
var baseUrl = contextpath + "aros/print/controller/NoticeController/";
var urls =
{
	queryUrl:baseUrl + "queryXzfyList.do"
};
var panel_ctl_handles = [{
	// 要折叠的面板id
	panelname:"#xzfyReqPanel",
	// 刷新操作函数
	gridname:"#xzfyReqDataGrid"
}];
var xzfyReqDataGrid;

//默认加载
$(function()
{	
	var icons = {iconCls:"icon-clear"};
	$("#casecode").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	
	//加载grid数据
	loadXzfyReqGrid(urls.queryUrl);
});

//加载可项目grid列表
function loadXzfyReqGrid(url)
{
	xzfyReqDataGrid = $("#xzfyReqDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		queryParams:{
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:200, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:240, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:240, sortable:true},		  
		  {field:"admtype", title:"行政管理类型", halign:"center", width:240, sortable:true},		 
		  {field:"appdate", title:"申请日期", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function xzfyReqQuery()
{
	var param = {
		casecode:$("#casecode").val(),
		appname:$("#appname").val(),
		defname:$("#defname").val(),
		firstNode:true,
		lastNode:false
	};
	
	xzfyReqDataGrid.datagrid("load", param);
}
