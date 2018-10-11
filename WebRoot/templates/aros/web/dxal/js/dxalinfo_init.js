/**
 * @Description: 典型案例
 * @author zhangtiantian
 * @date 2016-09-23 
 */
//请求路径
var urls = {
	gridUrl: "DxalWebController_queryList.do",			
	viewUrl: "DxalWebController_view.do",
	getClobContentVal: "DxalWebController_getClobContentVal.do"
};

var panel_ctl_handles = [{
	panelname:"#queryPanel", 		// 要折叠的面板id
	gridname:"#dxalDataGrid"  	// 刷新操作函数
}];
//默认加载
$(function()
{
	var icons = {iconCls:"icon-clear"};
	$("#title").textbox("addClearBtn", icons);
	//加载Grid数据
	dxalLoadGrid(urls.gridUrl);
});

var dxalDataGrid;
//加载可项目grid列表
function dxalLoadGrid(url)
{
	dxalDataGrid = $("#dxalDataGrid").datagrid({
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
			url: url,
			loadMsg:"正在加载，请稍候......",
			toolbar:"#toolbar_center",
			showFooter:false,
			columns: [[
			            {field:"conid", checkbox:true},
			            {field:"title", title:"案例名称", halign:'center', align:"left", width:800, sortable:true },
			            {field:"opttime", title:"发布日期", halign:'center', align:"left", width:140, sortable:true},
			            {field:"operator", title:"创建人", halign:'center', sortable:true, hidden:true}
					]]
	});
}

/**
 * 条件查询
 */
function doQuery(){
	
	//查询参数获取
	var param = {
		title: $("#title").val()
	};
	dxalDataGrid.datagrid("load", param);
}
//重新加载
function showReload()
{
	dxalDataGrid.datagrid('reload');
}

var editor;
//查看详情
function showView()
{
	var row = dxalDataGrid.datagrid("getChecked");
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
		href: urls.viewUrl,
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

$.modalDialog = function(options) {
	if ($.modalDialog.handler == undefined) {// 避免重复弹出
		var opts = $.extend( {
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.modalDialog.handler = undefined;
				$(this).dialog('destroy');
			}
		}, options);
		opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
		return $.modalDialog.handler = $('<div/>').dialog(opts);
	}
};