// 请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	noticeGridUrl:baseUrl + "queryNoticeList.do",                // 查询文书列表
	downloadNoticeUrl: baseUrl+"noticeDownload.do",			     // 文书下载
	getContentsForDetail: baseUrl + "getContentsForDetail.do"	 // 查询文书内容
};

var panel_ctl_handles = [{
	gridname:"#noticeDataGrid"  	// 刷新操作函数
}];

// 默认加载
$(function() {
	// 加载已有文书列表
	loadNoticeDataGrid(urls.noticeGridUrl);
});

// 加载已有文书列表列表
var noticeDataGrid;
function loadNoticeDataGrid(url) {
	noticeDataGrid = $("#noticeDataGrid").datagrid({
		fit:false,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:10,
		queryParams:{
			caseid: caseid,
			nodeid: nodeid,
			protype: protype
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#notice_toolbar",
		showFooter:true,
        onClickRow:function(rowIndex,rowData){
        	openNoticeInfo(rowData, '');
        },
		columns:[[ 
		  {field:"id", checkbox:true},
		  {field:"noticecode", title:"文书编号", halign:"center", width:60, sortable:true},
		  {field:"noticename", title:"文书名称", halign:"center", width:195, sortable:true},
		  {field:"buildtime", title:"生成时间", halign:"center", width:80, sortable:true},
		  {field:"opt", title:"操作", halign:"center", width:50, 
	        formatter: function (val, row, index) {
	        	var btn = "<span style='text-decoration:none;color:blue' onclick=download('"+ row.id +"')>下载</span>";
	            return btn;
	        }},
		  {field:"caseid", title:"案件编号", halign:"center", sortable:true, hidden:true}
        ]]
	});
}

//下载已有文书
function download (id) {
/*	$.post(urls.downloadUrl, {id:id}, 
		function(result) {
			if (result.success) { 
				parent.$.messager.alert("提示", result.title, "info");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}, "json");*/
	window.open(urls.downloadNoticeUrl + "?id=" + id);
}

/**
 * 打开文书处理页面
 * @param row
 */
function openNoticeInfo(row, tableFlag) {
	$("#noticeForm").form("load", row);
	$.post(urls.getContentsForDetail,
		{
			tempid: row.id,
			tableFlag: tableFlag
		}, function(data){
			$("#contents").html(data);
	}, "json");
}