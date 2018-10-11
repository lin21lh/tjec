//设置全局变量
var datagrid_procount;
/**查询
 * procount_init.js
 * */
var baseUrl = contextpath + "query/controller/ProjectCountController/";
//路径
var urls = {
	qryProCount : baseUrl + "qryProCount.do"
	
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};
//是、否
var isOrNot = {
		"0" : "否",
		"1" : "是"
	};
$(function() {
	//加载数据
	loaddatagrid();
	
	//加载查询条件
	loadqryconditon();
});
function loaddatagrid(){
	datagrid_procount = $("#datagrid_procount").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryProCount,
		queryParams: {
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_procount",
		showFooter : true,
		columns : [ [ {	field : "code",checkbox : true,align:'left'}
		, {field : "name",title : "单位",halign : 'center',	width : 250,sortable:true,align:'left'}
		, {field : "zsl",title : "总数量",halign : 'center',	width : 80,sortable:true,align:'right'}
		, {field : "xmsb",title : "项目识别",halign : 'center',	width : 80,sortable:true,align:'right'}
		, {field : "xmzb",title : "项目准备",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "xmcg",title : "项目采购",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "xmzx",title : "项目执行",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "xmyj",title : "项目移交",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		]]
	});
}

function loadqryconditon(){
	$("#name").treeDialog({
		title :'选择单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'code',
		prompt: "请选择所属行业",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "BDGAGENCY",
		filters:{
			code: "行业代码",
			name: "行业名称"
		}
	});
}

/**
 * 查询
 */
function qryProCount(){
	var param = {
			endTime : $("#endTime").datebox('getValue'),
			startTime : $("#startTime").datebox('getValue'),
			code : $("#name").treeDialog('getValue'),//所属行业
			menuid : menuid
		};
	$("#datagrid_procount").datagrid("load",param);
}