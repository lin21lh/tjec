//请求路径
var baseUrl = contextpath + "aros/bxzfy/controller/RcasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryRcasebaseinfoResultList.do",	
	editUrl:baseUrl + "rcasebaseinfoResultEditInit.do",
	editSaveUrl:baseUrl + "rcasebaseinfoResultSave.do",
	returnUrl:baseUrl + "rcasebaseinfoFlowForReturn.do",
	detailUrl:baseUrl + "rcasebaseinfoReqDetail.do",
	sendWFUrl:baseUrl + "rcasebaseinfoFlowForResult.do",
	flowAddUrl:baseUrl + "updateOpttypeByCaseid.do" 
};

var panel_ctl_handles = [{
	panelname:"#bxzfyResultPanel", 		// 要折叠的面板id
	gridname:"#bxzfyResultDataGrid",  	// 刷新操作函数
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
			bxzfyResultQuery();
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
	loadBxzfyResultGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#rcasecode").textbox("addClearBtn", icons);
	$("#lcasecode").textbox("addClearBtn", icons);
	$("#rdeptname").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
});

var bxzfyResultDataGrid;
var userDataGrid;

function showReload(){
	bxzfyResultDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadBxzfyResultGrid(url) {
	
	bxzfyResultDataGrid = $("#bxzfyResultDataGrid").datagrid({
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
function bxzfyResultQuery(){
	
	var param = {
		opttype:$("#opttype").combobox('getValue'),
		rcasecode:$("#rcasecode").val(),
		lcasecode:$("#lcasecode").val(),
		rdeptname:$("#rdeptname").val(),
		appname:$("#appname").val(),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	bxzfyResultDataGrid.datagrid("load", param);
}

/**
 * 查看被复议案件详细信息
 */
function bxzfyResultView(){
	
	var selectRow = bxzfyResultDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyResultDataGrid.datagrid("getSelections")[0];
	
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
 * 被行政复议送审
 */
function sendWF(){
	
	var selectRow = bxzfyResultDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyResultDataGrid.datagrid("getSelections")[0];
	
	parent.$.messager.confirm("发送确认", "确认要送审选中被复议案件？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				activityId:activityId,
				rcaseid:row.rcaseid 
			}, function(result) {
				easyui_auto_notice(result, function() {
					bxzfyResultDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = bxzfyResultDataGrid.datagrid("getChecked");
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

/**
 * 行政复议处理
 * @param index
 */
function editBxzfyResult(){
	
	var selectRow = bxzfyResultDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var flag = $("#opttype").combobox('getValue');
	if(flag == "2"){ //处理标志为已处理
		easyui_warn("该案件已处理，请点击详情查看案件信息", null);
		return;
	}
	
    var row = $('#bxzfyResultDataGrid').datagrid("getSelected"); 
    if (row){  
    	
    	//记录接收案件日志
    	var aj = $.ajax({    
    	    url:urls.flowAddUrl, 
    	    data:{    
    	    	rcaseid:row.rcaseid 
    	    },    
    	    type:'post',    
    	    cache:false,    
    	    dataType:'json',    
    	    success:function(data) { 
    	        if(data.success == true){    
    	        	openBxzfyResultDialog(row);
    	        }else{    
    	        	easyui_warn(data.title, null);
    	        }    
    	     },    
    	     error:function(data) {    
    	    	 easyui_warn("案件接收失败，请刷新页面重新接收", null);
    	     }    
    	});  
    }  
}

/**
 * 打开被复议案件处理页面
 * @param row
 */
function openBxzfyResultDialog(row){
	
	parent.$.modalDialog({
		title:"分案处理",
		width:900,
		height:400,
		href:urls.editUrl + "?rcaseid=" + row.rcaseid + "&nodeid=" + row.nodeid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			comboboxFuncByCondFilter(menuid, "result", "HEAR", "code", "name", mdDialog);				//审查结果

			var f = parent.$.modalDialog.handler.find('#bxzfyResultForm');
			f.form("load", row);
			
			showFileDiv(mdDialog.find("#filetd"), false, "BXZFYREQ", row.rcaseid, "");
			showFileDiv(mdDialog.find("#disposefiletd"), false, "DISPOSE", row.rcaseid, "");
			showFileDiv(mdDialog.find("#auditfiletd"), false, "AUDIT", row.rcaseid, "");
			showFileDiv(mdDialog.find("#resultfiletd"), true, "RESULT", row.rcaseid, "");
		},
		buttons:[{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				saveBxzfyResultForm(urls.editSaveUrl, "bxzfyResultForm", "");
			}
		},{
			id:"auditBtn",
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				sendResult(row);
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
 * 保存被复议案件处理信息
 * @param url
 * @param form
 */
function saveBxzfyResultForm(url, form){
	
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		easyui_warn("数据项未填写完整，请处理完整后再保存", null);
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
				bxzfyResultDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 退回被复议案件处理信息
 * @param url
 * @param form
 */
function saveBxzfyReturnForm(url, form){
	
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
				bxzfyResultDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 弹出页面送审
 */
function sendResult(row){
	
	parent.$.messager.confirm("发送确认", "确认要发送该被复议案件？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				activityId:activityId,
				rcaseid:row.rcaseid 
			}, function(result) {
				if (result.success) {
					bxzfyResultDataGrid.datagrid("reload");
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}

/**
 * 保存被复议案件审查信息
 * @param url
 * @param form
 */
function saveXzfyReviewForm(url, form){
	
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
				xzfyReviewDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 退回
 */
function bxzfyResultReturn(){
	
	var selectRow = bxzfyResultDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条被复议案件", null);
		return;
	}
	
	var row = bxzfyResultDataGrid.datagrid("getSelections")[0];
	
	parent.$.messager.confirm("退回确认", "确认要退回该被复议案件？", function(r) {
		if (r) {
			$.post(urls.returnUrl, {
				menuid:menuid,
				activityId:activityId,
				rcaseid:row.rcaseid 
			}, function(result) {
				if (result.success) { 
					bxzfyResultDataGrid.datagrid("reload");
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}