//请求路径
var baseUrl = contextpath + "aros/sqbl/controller/ApplyRecordController/";
var urls = {
	gridUrl:baseUrl + "queryListByCase.do"
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
		  {field:"arid", checkbox:true},
		  {field:"appname", title:"申请人", halign:"center", sortable:true },
		  {field:"sexname", title:"性别", halign:"center", sortable:true},
		  {field:"age", title:"年龄", halign:"center",  sortable:true},
		  {field:"phone", title:"联系电话", halign:"center",  sortable:true},
		  {field:"address", title:"联系地址", halign:"center", sortable:true},
		  {field:"workunits", title:"工作单位", halign:"center", width:180, sortable:true},
		  {field:"reqtime", title:"时间", halign:"center", width:100, sortable:true},
		  {field:"place", title:"地址", halign:"center", width:150, sortable:true},
		  {field:"inquirer", title:"调查人", halign:"center", width:150, sortable:true},
		  {field:"noter", title:"记录人", halign:"center", width:180, sortable:true},
		  {field:"issue", title:"问", halign:"center", width:250,  sortable:true},
		  {field:"answer", title:"答", halign:"center", width:250, sortable:true},
        ]],
	});
}
