//请求路径
var baseUrl = contextpath + "aros/ajbg/fytj/FytjController/";
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
		  {field:"deftypeMc", title:"被申请人类型", width:150, sortable:true, hidden:true},
		  {field:"admtypeMc", title:"行政管理类型", halign:"center", width:100, sortable:true},
		  {field:"casetypeMc", title:"行政行为类型", halign:"center", width:100, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:80, sortable:true},
		  {field:"idtypeMc", title:"证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idcode", title:"证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"phone", title:"联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"address", title:"通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"postcode", title:"邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"depttype", title:"被申请人机构类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidtype", title:"被申请人证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidcode", title:"被申请人证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defphone", title:"被申请人联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defaddress", title:"被申请人通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defpostcode", title:"被申请人邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"noticeddate", title:"接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"actnoticeddate", title:"实际接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"ifcompensationMc", title:"是否附带行政赔偿", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"amount", title:"赔偿金额", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appcase", title:"申请事项", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"factreason", title:"事实和理由", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"annex", title:"附件", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appdate", title:"申请日期", halign:"center", width:80, sortable:true, hidden:true},
		  {field:"operator", title:"操作人", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"optdate", title:"操作日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"protype", title:"流程类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"opttype", title:"处理标志", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"nodeid", title:"节点编号", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"lasttime", title:"数据最后更新时间", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"userid", title:"用户ID", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"oldprotype", title:"原流程类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"mobile", title:"联系手机", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"mailaddress", title:"邮寄地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"email", title:"邮箱", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defmobile", title:"被申请人联系手机", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defmailaddress", title:"被申请人邮寄地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defemail", title:"被申请人邮箱", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"receiveway", title:"收文方式", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"expresscom", title:"递送公司", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"couriernum", title:"递送单号", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:80, sortable:true, hidden:true}
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
