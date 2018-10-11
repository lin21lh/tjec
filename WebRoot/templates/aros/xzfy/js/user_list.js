//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryUserList.do"
};

//默认加载
$(function() {
	//加载Grid数据
	loadDataGrid(urls.gridUrl);
});

var dataGrid;

//加载可项目grid列表
function loadDataGrid(url) {
	var rolecode = $("#rolecode").val();
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
			rolecode:rolecode
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[
		  {field:"userid", checkbox:true},
		  {field:"usercode", title:"用户编码", halign:"center",width:'30%', sortable:true },
		  {field:"username", title:"用户名称", halign:"center",width:'65%', sortable:true}
        ]],
	});
}
