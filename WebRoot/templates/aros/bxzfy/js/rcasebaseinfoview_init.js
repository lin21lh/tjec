//请求路径
var baseUrl = contextpath + "aros/bxzfy/controller/RcasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryRcasebaseinfoViewList.do",	
	detailUrl:baseUrl + "rcasebaseinfoReqDetail.do",
	flowAddUrl:baseUrl + "updateOpttypeByCaseid.do" 
};

var panel_ctl_handles = [{
	panelname:"#bxzfyViewPanel", 		// 要折叠的面板id
	gridname:"#bxzfyViewDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 			// 折叠按钮id
}];

//默认加载
$(function() {
	
	$("#opttype").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "未处理", value : "1", selected:true}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			bxzfyViewQuery();
			switch (record.value) {
			case '1':
				$('#sendBtn').linkbutton('enable');
				$('#returnBtn').linkbutton('enable');
				break;
			case '2':
				$('#sendBtn').linkbutton('disable');
				$('#returnBtn').linkbutton('disable');
				break;
			default:
				break;
			}
		}
	});
	
	//加载Grid数据
	loadBxzfyViewGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#rcasecode").textbox("addClearBtn", icons);
	$("#lcasecode").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#rdeptname").textbox("addClearBtn", icons);
});

var bxzfyViewDataGrid;
var userDataGrid;

function showReload(){
	bxzfyViewDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadBxzfyViewGrid(url) {
	
	bxzfyViewDataGrid = $("#bxzfyViewDataGrid").datagrid({
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
		  {field:"rcaseid", checkbox:true},
		  {field:"lcasecode", title:"本机关案号", halign:"center", width:250, sortable:true},
		  {field:"rcasecode", title:"复议机关案号", halign:"center", width:250, sortable:true},
		  {field:"rdeptname", title:"复议机关", halign:"center", width:150, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:180, sortable:true},
		  {field:"receiver", title:"收案人", halign:"center", width:80, sortable:true},
		  {field:"rectime", title:"收案时间", halign:"center", width:80, sortable:true, align:"center"},
		  {field:"opttime", title:"维护时间", halign:"center", width:80, sortable:true, align:"center"}
        ]]
	});
}

/**
 * 条件查询
 */
function bxzfyViewQuery(){
	
	var param = {
		opttype:$("#opttype").combobox('getValue'),
		rcasecode:$("#rcasecode").val(),
		lcasecode:$("#lcasecode").val(),
		appname:$("#appname").val(),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	bxzfyViewDataGrid.datagrid("load", param);
}

/**
 * 查看被复议案件详细信息
 */
function bxzfyViewView(){
	
	var selectRow = bxzfyViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyViewDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"被复议案件详情",
		width:900,
		height:400,
		href:urls.detailUrl + "?rcaseid=" + row.rcaseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			showFileDiv(mdDialog.find("#filetd"), false, "BXZFYREQ", row.rcaseid, "");
			showFileDiv(mdDialog.find("#disposefiletd"), false, "DISPOSE", row.rcaseid, "");
			showFileDiv(mdDialog.find("#auditfiletd"), false, "AUDIT", row.rcaseid, "");
			showFileDiv(mdDialog.find("#resultfiletd"), false, "RESULT", row.rcaseid, "");
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
	
	var selectRow = bxzfyViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var rcaseid = selectRow[0].rcaseid;
	if(rcaseid == '' || rcaseid == null){
		easyui_warn("该案件没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForRcasebaseinfo.do?rcaseid="
			+ rcaseid;
	
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