//请求路径
var baseUrl = contextpath + "aros/ysaj/controller/AdmlitbaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryAdmlitbaseinfoViewList.do",	
	detailUrl:baseUrl + "admlitbaseinfoReqDetail.do",
	flowAddUrl:baseUrl + "updateOpttypeByCaseid.do" 
};

var panel_ctl_handles = [{
	panelname:"#ysajViewPanel", 		// 要折叠的面板id
	gridname:"#ysajViewDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 			// 折叠按钮id
}];

//默认加载
$(function() {
	
	//加载Grid数据
	loadYsajViewGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#rcasecode").textbox("addClearBtn", icons);
	$("#lcasecode").textbox("addClearBtn", icons);
	$("#rdeptname").textbox("addClearBtn", icons);
	$("#plaintiff").textbox("addClearBtn", icons);
});

var ysajViewDataGrid;
var userDataGrid;

function showReload(){
	ysajViewDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadYsajViewGrid(url) {
	
	ysajViewDataGrid = $("#ysajViewDataGrid").datagrid({
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
			//status:1,
			//activityId:activityId,
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"acaseid", checkbox:true},
		  {field:"lcasecode", title:"本机关案号", halign:"center", width:250, sortable:true},
		  {field:"rcasecode", title:"司法机关案号", halign:"center", width:250, sortable:true},
		  {field:"rdeptname", title:"受案法院", halign:"center", width:150, sortable:true},
		  {field:"plaintiff", title:"原告", halign:"center", width:150, sortable:true},
		  {field:"receiver", title:"收案人", halign:"center", width:80, sortable:true},
		  {field:"rectime", title:"收案时间", halign:"center", width:80, sortable:true, align:"center"},
		  {field:"opttime", title:"维护时间", halign:"center", width:80, sortable:true, align:"center"}
        ]]
	});
}

/**
 * 条件查询
 */
function ysajViewQuery(){
	
	var param = {
		opttype:$("#opttype").combobox('getValue'),
		rcasecode:$("#rcasecode").val(),
		lcasecode:$("#lcasecode").val(),
		rdeptname:$("#rdeptname").val(),
		plaintiff:$("#plaintiff").val(),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	ysajViewDataGrid.datagrid("load", param);
}

/**
 * 查看行政应诉案件详细信息
 */
function ysajViewView(){
	
	var selectRow = ysajViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条行政应诉案件", null);
		return;
	}
	
	var row = ysajViewDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"行政应诉案件详情",
		width:900,
		height:500,
		href:urls.detailUrl + "?acaseid=" + row.acaseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			showFileDiv(mdDialog.find("#filetd"), false, "REQ", row.acaseid, "");
			showFileDiv(mdDialog.find("#disposefiletd"), false, "DISPOSE", row.acaseid, "");
			showFileDiv(mdDialog.find("#auditfiletd"), false, "AUDIT", row.acaseid, "");
			showFileDiv(mdDialog.find("#resultfiletd"), false, "RESULT", row.acaseid, "");
		},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = ysajViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条行政应诉案件", null);
		return;
	}
	
	var acaseid = selectRow[0].acaseid;
	if(acaseid == '' || acaseid == null){
		easyui_warn("该案件没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForAdmlitbaseinfo.do?acaseid="
			+ acaseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:900,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}