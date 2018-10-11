/**
 * 委员会制度管理查看
 * 
 * create by zhangtiantian
 */
var baseUrl = contextpath + "aros/wyInfoManage/controller/ContentbaseinfoController/";
var urls = {
	queryByCurrentUserUrl: baseUrl+"queryByCurrentUser.do",
	showViewUrl: baseUrl+"showView.do",
	getClobContentVal: baseUrl+"getClobContentVal.do"
};

var panel_ctl_handles = [{
	panelname : '#queryPanel', // 要折叠的面板id
	gridname : "#showDataGrid" // 刷新操作函数
}];

//默认加载
$(function()
{
	var icons = {iconCls:"icon-clear"};
	$("#title").textbox("addClearBtn", icons);
	
	// 加载grid
	loadShowDataGrid(urls.queryByCurrentUserUrl + "?contype=" + contype);
});

// grid
var showDataGrid;
// 查询列表
function loadShowDataGrid(url)
{
	showDataGrid = $("#showDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		pageSize : 10,
		queryParams : {
			menuid : menuid
		},
		url: url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[
            {field:"conid", checkbox:true},
            {field:"title", title:"标题", halign:'center', width:450, sortable:true },
            {field:"operator", title:"创建人", halign:'center', width:100, sortable:true},
            {field:"opttime", title:"创建时间", halign:'center', width:150, sortable:true},
	        {field:"contype", title:"类型编码", halign:'center', sortable:true, hidden:true},
	        {field:"status", title:"状态编码", halign:'center', sortable:true, hidden:true},
            {field:"receiveuserid", title:"接收人id", halign:'center', sortable:true , hidden:true}
		]]
	});
}

//点击查询
function doQuery(){
	//查询参数获取
	var param = {
		menuid: menuid,
		title: $("#title").val()
	};
	showDataGrid.datagrid("load", param);
}


// 重新加载
function showReload(){
	showDataGrid.datagrid('reload');
}


//查看详情
function showView(){
	var row = showDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var icon = 'icon-view';
	parent.$.modalDialog({
		title: "详情",
		iconCls: icon,
		width: 900,
		height: 600,
		href: urls.showViewUrl,
		onLoad: function()
		{
			 var mdDialog = parent.$.modalDialog.handler;
			//获取内容信息，在div中展示
			$.post(urls.getClobContentVal, {conid:row[0].conid}, function(data){
				mdDialog.find("#content").html(data);
			}, "json");
		},
		buttons: [{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
	});
}