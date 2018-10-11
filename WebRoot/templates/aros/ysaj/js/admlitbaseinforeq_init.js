//请求路径
var baseUrl = contextpath + "aros/ysaj/controller/AdmlitbaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryAdmlitbaseinfoReqList.do",			
	addUrl:baseUrl + "admlitbaseinfoAddInit.do",
	addCommitUrl:baseUrl + "admlitbaseinfoReqSave.do",
	editUrl:baseUrl + "admlitbaseinfoEditInit.do",
	editCommitUrl:baseUrl + "admlitbaseinfoReqEdit.do",
	delCommitUrl:baseUrl + "admlitbaseinfoReqDelete.do",
	detailUrl:baseUrl + "admlitbaseinfoReqDetail.do",
	sendWFUrl:baseUrl + "admlitbaseinfoFlowForReq.do" 
};

var panel_ctl_handles = [{
	panelname:"#ysajReqPanel", 		// 要折叠的面板id
	gridname:"#ysajReqDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
	/*$("#opttype").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "已提交", value : "0"}, {text : "已退回", value : "9"}]
	});*/
	
	//加载Grid数据
	loadYsajReqGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#rcasecode").textbox("addClearBtn", icons);
	$("#lcasecode").textbox("addClearBtn", icons);
	$("#rdeptname").textbox("addClearBtn", icons);
});

var ysajReqDataGrid;

function showReload(){
	ysajReqDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadYsajReqGrid(url) {
	
	ysajReqDataGrid = $("#ysajReqDataGrid").datagrid({
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
			{field:"plaintiff", title:"原告", halign:"center", width:180, sortable:true},
			{field:"receiver", title:"收案人", halign:"center", width:80, sortable:true},
			{field:"rectime", title:"收案时间", halign:"center", width:80, sortable:true, align:"center"},
			{field:"opttime", title:"维护时间", halign:"center", width:80, sortable:true, align:"center"}
        ]]
	});
}

/**
 * 条件查询
 */
function ysajReqQuery(){
	
	var param = {
		rcasecode:$("#rcasecode").val(),
		lcasecode:$("#lcasecode").val(),
		rdeptname:$("#rdeptname").val(),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	ysajReqDataGrid.datagrid("load", param);
}

//初始复议申请页面
function ysajReqAdd(){
	
	parent.$.modalDialog({
		title:"行政应诉案件维护",
		width:900,
		height:500,
		href:urls.addUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			comboboxFuncByCondFilter(menuid, "stage", "STAGETYPE", "code", "name", mdDialog);	
			comboboxFuncByCondFilter(menuid, "partytype", "PARTYTYPE", "code", "name", mdDialog);	
			comboboxFuncByCondFilter(menuid, "rectype", "RECCASETYPE", "code", "name", mdDialog);	
		},
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.addCommitUrl, "ysajReqForm");
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
				ysajReqDataGrid.datagrid("reload");
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
function ysajReqEdit(){
	
	var selectRow = ysajReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条行政应诉案件", null);
		return;
	}
	
	var row = ysajReqDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"行政应诉案件维护",
		width:900,
		height:500,
		href:urls.editUrl + "?relaacaseid=" + row.relaacaseid + "&defendant=" + row.defendant,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();

			comboboxFuncByCondFilter(menuid, "stage", "STAGETYPE", "code", "name", mdDialog);	
			comboboxFuncByCondFilter(menuid, "partytype", "PARTYTYPE", "code", "name", mdDialog);	
			comboboxFuncByCondFilter(menuid, "rectype", "RECCASETYPE", "code", "name", mdDialog);
			
			var f = parent.$.modalDialog.handler.find('#ysajReqForm');
			f.form("load", row);
			
			showFileDiv(mdDialog.find("#filetd"), true, "REQ", row.acaseid, "");
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.editCommitUrl, "ysajReqForm", "");
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
function ysajReqDelete(){
	
	var selectRow = ysajReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条行政应诉案件", null);
		return;
	}
	
	var row = ysajReqDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中行政应诉案件删除？", function(r) {
		if (r){
			$.post(urls.delCommitUrl, {acaseid:row.acaseid}, 
					function(result){
						easyui_auto_notice(result, function() {
							ysajReqDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看行政应诉案件详细信息
 */
function ysajReqView(){
	
	var selectRow = ysajReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条行政应诉案件", null);
		return;
	}
	
	var row = ysajReqDataGrid.datagrid("getSelections")[0];
	
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
 * 行政应诉案件送审
 */
function sendWF(){
	
	var selectRow = ysajReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条行政应诉案件", null);
		return;
	}
	
	var row = ysajReqDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("发送确认", "确认要发送选中行政应诉案件？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				activityId:activityId,
				acaseid:row.acaseid 
			}, function(result) {
				easyui_auto_notice(result, function() {
					ysajReqDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = ysajReqDataGrid.datagrid("getChecked");
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
		height:500,
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