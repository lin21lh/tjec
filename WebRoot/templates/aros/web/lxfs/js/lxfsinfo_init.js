/**
 * @Description: 联系方式
 * @author 陈仲朋
 * @date 2016-09-22
 */
//请求路径
var baseUrl = contextpath + "aros/web/lxfs/controller/LxfsWebController/";
var urls = {
	gridUrl: "LxfsWebController_queryList.do",//查询list主页面	
};

var panel_ctl_handles = [{
	gridname:"#lxfsDataGrid",  	// 刷新操作函数
}];
//默认加载
$(function(){
	//加载Grid数据
	lxfsLoadGrid(urls.gridUrl);
});

var lxfsDataGrid;
var selectMap = null;


var editor;
//查看地图
function showMap(rowIndex, rowData){
	var deptname = encodeURIComponent(rowData.deptname);
	var address = encodeURIComponent(rowData.address);
	var phone = rowData.phone;
	var xx = rowData.xcoor;
	var yy = rowData.ycoor;
	
	$("#mapIframe").attr('src',contextpath +"templates/aros/web/lxfs/map.html"+"?xx="+xx+"&yy="+yy+"&deptname="+deptname+"&address="+address+"&phone="+phone);
}
//加载可项目grid列表
function lxfsLoadGrid(url){
	lxfsDataGrid = $("#lxfsDataGrid").datagrid({
			fit:true,
			stripe:true,
			singleSelect:true,
			rownumbers:true,
			pagination:false,
			remoteSort:false,
			multiSort:true,
			pageSize:30,
			width: 100,
			height: 100,
			queryParams:{
				menuid:menuid,
				firstNode:true,
				lastNode:false
			},
			url: url,
			loadMsg:"正在加载，请稍候......",
			toolbar:"#toolbar_center",
			showFooter:true,
			columns:[[ 
			      {field:"deptname", title:"机关名称", halign:"center", width:180, sortable:true},     
			      {field:"address", title:"地址", halign:"center", width:200, sortable:true}, 
			      {field:"phone", title:"联系电话", halign:"center", width:170, sortable:true}
		 ]],
		   onClickRow: function(rowIndex, rowData){
						showMap(rowIndex, rowData);
				}
	});
}
