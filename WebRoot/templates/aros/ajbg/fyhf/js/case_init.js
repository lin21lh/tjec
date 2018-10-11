//请求路径
var baseUrl = contextpath + "aros/ajbg/fyhf/FyhfController/";
var urls = {
	gridUrl:baseUrl + "queryCaseList.do"
};

//默认加载
$(function() {
	
	//加载Grid数据
	loadDataGrid(urls.gridUrl);
});

var dataGrid;

//加载可项目grid列表
function loadDataGrid(url) {
	
	dataGrid = $("#datalist").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		queryParams:{
			menuid:menuid,
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:220, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:200, sortable:true},
		  {field:"intro", title:"案件名称", halign:"center", width:300, sortable:true}
        ]],
	});
}

/**
 * 条件查询
 */
function xzfyQuery() {
	
	var param = {
		csaecode:parent.$.modalDialog.handler2.find("#csaecode").val(),
		appname:parent.$.modalDialog.handler2.find("#appname").val(),
		defname:parent.$.modalDialog.handler2.find("#defname").val(),
		firstNode:true,
		lastNode:false
	};
	
	dataGrid.datagrid("load", param);
}
