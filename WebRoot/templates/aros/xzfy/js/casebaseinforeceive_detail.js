var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	queryFileUrl:baseUrl + "queryFileList.do",
	downloadFileUrl: "FileDownController_download.do"
};

var fileDataGrid;
$(function (){
	loadFileDataGrid(urls.queryFileUrl);
})

//加载可项目grid列表
function loadFileDataGrid(url)
{
	fileDataGrid = $("#fileDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:5,
		pageList: [5],
		queryParams:{
			caseid:caseid,
			nodeid:nodeid,
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		showFooter:false,
		columns:[[ 
          {field:"itemid", checkbox:false, hidden:true},
		  {field:"filename", title:"材料名称", halign:"center", align:"left", width:350, sortable:true},
		  {field:"createtime",title:"接收日期", halign:"center",  align:"left", width:150, sortable:true},
		  {field:"opt", title:"操作", width:50,  align:"center",
	            formatter:function(val, row, index){
	            	var btn = '<span style="text-decoration:none;color:blue" onclick="download('+ row.itemid +')">下载</span>';
	                return btn; 
	            }},
	    ]]
	});
}

function download(itemid)
{
	window.open(urls.downloadFileUrl + "?itemid=" + itemid);
}
