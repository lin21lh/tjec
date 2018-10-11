/**
 * @Description: 文件下载
 * @author 张田田
 * @date 2016-09-08 
 */
//请求路径
var urls =
{
	queryUrl : "FileDownController_queryDocList.do",
	downloadUrl : "FileDownController_download.do"
};

var panel_ct1_handler = [{
	panelname:"docReqPanel",
	gridname:"docDataGrid"
}];

var docDataGrid;

//默认加载
$(function()
{	
	var icons = {iconCls:"icon-clear"};
	$("#filename").textbox("addClearBtn", icons);
	//加载grid数据
	loadDocDataGrid(urls.queryUrl);
});

//加载可项目grid列表
function loadDocDataGrid(url)
{
	docDataGrid = $("#docDataGrid").datagrid({
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
          {field:"itemid", checkbox:true},
		  {field:"filename", title:"文件名称", halign:"center", align:"left", width:800, sortable:true},
		  {field:"createtime",title:"发布日期", halign:"center",  align:"left", width:140, sortable:true}
	    ]]
	});
}

/**
 * 条件查询
 */
function docQuery()
{
	var param = {
		filename:$("#filename").val()
	};
	docDataGrid.datagrid("load", param);
}

function down()
{
	var row = docDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据");
		return;
	}
	
	window.open(urls.downloadUrl + "?itemid=" + row[0].itemid, "_blank");
}