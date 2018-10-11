//请求路径
var baseUrl = contextpath + "aros/bxzfy/controller/RcasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryRcasebaseinfoReqList.do",			
	addUrl:baseUrl + "rcasebaseinfoAddInit.do",
	addCommitUrl:baseUrl + "rcasebaseinfoReqSave.do",
	editUrl:baseUrl + "rcasebaseinfoEditInit.do",
	editCommitUrl:baseUrl + "rcasebaseinfoReqEdit.do",
	delCommitUrl:baseUrl + "rcasebaseinfoReqDelete.do",
	detailUrl:baseUrl + "rcasebaseinfoReqDetail.do",
	sendWFUrl:baseUrl + "rcasebaseinfoFlowForReq.do" 
};

var panel_ctl_handles = [{
	panelname:"#bxzfyReqPanel", 		// 要折叠的面板id
	gridname:"#bxzfyReqDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
	//加载Grid数据
	loadBxzfyReqGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#rcasecode").textbox("addClearBtn", icons);
	$("#lcasecode").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
});

var bxzfyReqDataGrid;

function showReload(){
	bxzfyReqDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadBxzfyReqGrid(url) {
	
	bxzfyReqDataGrid = $("#bxzfyReqDataGrid").datagrid({
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
function bxzfyReqQuery(){
	
	var param = {
		rcasecode:$("#rcasecode").val(),
		lcasecode:$("#lcasecode").val(),
		appname:$("#appname").val(),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	bxzfyReqDataGrid.datagrid("load", param);
}

//初始复议申请页面
function bxzfyReqAdd(){
	
	parent.$.modalDialog({
		title:"被复议案件维护",
		width:900,
		height:400,
		href:urls.addUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
		},
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.addCommitUrl, "bxzfyReqForm");
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
		}]
	});
}

//提交表单
function submitForm(url, form){
	
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
		
	form.form("submit", {
		url:url,
		onSubmit:function() {
			parent.$.messager.progress({
				title:"提示",
				text:"数据处理中，请稍后...."
			});
			var isValid = form.form("validate");
			if (!isValid) {
				parent.$.messager.progress("close");
			}
			return isValid;
		},
		success:function(result) {
			parent.$.messager.progress("close");
			result = $.parseJSON(result);
			if (result.success) {
				bxzfyReqDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 初始化被行政复议修改页面
 */
function bxzfyReqEdit(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"被复议案件维护",
		width:900,
		height:400,
		href:urls.editUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();

			var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#bxzfyReqForm');
			f.form("load", row);
			
			showFileDiv(mdDialog.find("#filetd"), true, "BXZFYREQ", row.rcaseid, "");
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.editCommitUrl, "bxzfyReqForm", "");
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}

/**
 * 删除被复议申请
 */
function bxzfyReqDelete(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中被复议案件删除？", function(r) {
		if (r){
			$.post(urls.delCommitUrl, {rcaseid:row.rcaseid}, 
					function(result){
						easyui_auto_notice(result, function() {
							bxzfyReqDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看被复议案件详细信息
 */
function bxzfyReqView(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
	
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
 * 被复议案件送审
 */
function sendWF(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("发送确认", "确认要发送选中被复议案件？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				activityId:activityId,
				rcaseid:row.rcaseid 
			}, function(result) {
				easyui_auto_notice(result, function() {
					bxzfyReqDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
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