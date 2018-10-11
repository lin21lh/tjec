//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryXzfyAccendList.do",	
	editUrl:baseUrl + "xzfyAccendEdit.do",
	editSaveUrl:baseUrl + "xzfyAccendSaveEdit.do",
	sendWFUrl:baseUrl + "xzfyAccendFlow.do",
	returnUrl:baseUrl + "xzfyReturn.do",
	xzUrl : baseUrl + "queryGroupList.do",
	ajxzUrl : baseUrl + "ajxzInit.do",
	saveAjxzUrl : baseUrl + "saveAjxz.do"
};

var panel_ctl_handles = [{
	panelname:"#xzfyAcceptPanel", 		// 要折叠的面板id
	gridname:"#xzfyAccendDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 			// 折叠按钮id
}];

//默认加载
$(function() {
	
	$('#xzfyAcceptPanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	
	$("#opttype").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "未处理", value : "1", selected:true}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			xzfyReqQuery();
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
	loadXzfyAcceptGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#opttype").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	$("#admtype").textbox("addClearBtn", icons);
	$("#casetype").textbox("addClearBtn", icons);
	$("#deftype").textbox("addClearBtn", icons);
});

var xzfyDataGrid;
var userDataGrid;
function showReload(){
	xzfyDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadXzfyAcceptGrid(url) {
	xzfyDataGrid = $("#xzfyAccendDataGrid").datagrid({
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
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		onDblClickRow:function (rowIndex, rowData) {  
			openEditOrReview(rowData);
        },
        onClickRow:function(rowIndex,rowData){
        	viewTime();
        	viewFlow();
        },
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:120, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:80, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"apptypeMc", title:"申请人类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idtypeMc", title:"证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idcode", title:"证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"phone", title:"联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"address", title:"通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"postcode", title:"邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"deftypeMc", title:"被申请人类型", width:150, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:250, sortable:true},
		  {field:"depttype", title:"被申请人机构类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidtype", title:"被申请人证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidcode", title:"被申请人证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defphone", title:"被申请人联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defaddress", title:"被申请人通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defpostcode", title:"被申请人邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"noticeddate", title:"接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"actnoticeddate", title:"实际接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"admtypeMc", title:"行政管理类型", halign:"center", width:100, sortable:true},
		  {field:"casetypeMc", title:"申请复议事项", halign:"center", width:100, sortable:true},
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
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:80, sortable:true},
		  {field:"delaydays", title:"延期天数", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"region", title:"行政区", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"intro", title:"案件简介", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"isgreat", title:"是否重大案件备案", halign:"center", width:100, sortable:true, hidden:true} 
        ]]
	});
}

/**
 * 条件查询
 */
function xzfyReqQuery(){
	var param = {
		opttype:$("#opttype").combobox('getValue'),
		appname:$("#appname").val(),
		defname:$("#defname").val(),
		menuid:menuid,
		admtype:$("#admtype").combobox('getValue'),
		casetype:$("#casetype").combobox('getValue'),
		deftype:$("#deftype").combobox('getValue'),
		firstNode:true,
		lastNode:false
	};
	
	xzfyDataGrid.datagrid("load", param);
}

/**
 * 行政复议处理
 * @param index
 */
function editXzfyReq(){
	
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	
	var flag = $("#opttype").combobox('getValue');
	if(flag == "2"){ //处理标志为已处理
		easyui_warn("该案件已处理，请点击详情查看案件信息", null);
		return;
	}
	
    var row = $('#xzfyAccendDataGrid').datagrid("getSelected"); 
    if (row){  
    	openXzfyAccendDialog(row); 
    }  
}

function openEditOrReview(row)
{
	if ("7" == row.nodeid)
	{
		openXzfyAccendDialog(row);
	}
	else
	{
		xzfyAccendDetail();
	}
}

/**
 * 打开行政复议受理决定页面
 * @param row
 */
function openXzfyAccendDialog(row){
	
	parent.$.modalDialog({
		title:"行政复议受理決定",
		width:900,
		height:600,
		href:urls.editUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid, "b", "SYS_TRUE_FALSE", "code", "name", mdDialog);	// 依据
			comboboxFuncByCondFilter(menuid, "result", "PUB_PROBASEINFO_RESULT", "code", "name", mdDialog); // 处理结果
			comboboxFuncByCondFilter(menuid, "isgreat", "SYS_TRUE_FALSE", "code", "name", mdDialog);	//是否重大案件备案
		},
		buttons:[{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				saveXzfyAccendForm(urls.editSaveUrl, "xzfyAccendForm");
			}
		},{
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				sendAudit(row);
			}
		},{
			id:"backBtn",
			text:"回退",
			iconCls:"icon-undo",
			handler:function() {
				xzfyReturn(row);
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
 * 保存复议申请处理信息
 * @param url
 * @param form
 */
function saveXzfyAccendForm(url, form){
	
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
				parent.$.messager.alert("提示", result.title, "info");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 行政复议送审
 */
function sendWF(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("送审确认", "确认要送审选中复议申请？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				caseid:row.caseid 
			}, function(result) {
				easyui_auto_notice(result, function() {
					xzfyDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 行政复议送审
 */
function sendAudit(row){
	parent.$.messager.confirm("发送确认", "确认要发送该复议申请？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				caseid:row.caseid 
			}, function(result) {
				easyui_auto_notice(result, function() {
					parent.$.messager.alert("提示", result.title, "info");
					xzfyDataGrid.datagrid("reload");
					parent.$.modalDialog.handler.dialog("close");
				});
			}, "json");
		}
	});
}

/**
 * 回退
 */
function xzfyReturn(row){
	parent.$.messager.confirm("回退确认", "确认要回退该复议申请？", function(r) {
		if (r) {
			$.post(urls.returnUrl, {
				menuid:menuid,
				caseid:row.caseid 
			}, function(result) {
				if (result.success) { 
					parent.$.messager.alert("提示", result.title, "info");
					xzfyDataGrid.datagrid("reload");
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	showFlowModalDialogForReq(selectRow[0].caseid);
}

/**
 * 将案件发送给其他用户
 */
function sendToOther(){
	
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	parent.$.modalDialog({
		title:"选择转送人",
		width:900,
		height:600,
		href:urls.sendUrl,
		onLoad:function(){
			
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			var row = xzfyDataGrid.datagrid("getSelections")[0];
			
			//所有该角色的用户
			openUserDataGrid(mdDialog, row.nodeid);
		},
		buttons:[{
			id:"saveBtn",
			text:"选择",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				var row = xzfyDataGrid.datagrid("getSelections")[0];
				saveSend(row.caseid);
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

//可选委员列表
function openUserDataGrid(mdDialog, nodeid) {
	
	userDataGrid = mdDialog.find("#userTable").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		height:400,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		queryParams:{
			menuid:menuid,
			nodeid:nodeid,
			firstNode:true,
			lastNode:false
		},
		pageSize : 30,
		url : urls.sendToOtherUrl,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_user",
		showFooter : true,
		columns : [ [ {
			field : "userid",
			checkbox : true
		}, {
			field : "usercode",
			title : "用户编码",
			halign : 'center',
			width : 100
		}, {
			field : "username",
			title : "用户名称",
			halign : 'center',
			fixed : true,
			width : 120,
			sortable:true
		}, {
			field : "orgcodeMc",
			title : "机构名称",
			halign : 'center',
			width : 220,
			fixed : true,
			sortable:true
		}, {
			field : "createtime",
			title : "创建日期",
			halign : 'center',
			width :150
		}] ]
	});
}

/**
 * 保存转发信息
 * @param caseid
 * @param userid
 */
function saveSend(caseid){
	
	var selectRow = userDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一位用户信息", null);
		return;
	}
	
	var row = userDataGrid.datagrid("getSelections")[0];
	
	parent.$.messager.confirm("发送转发", "确认要转发该复议申请？", function(r) {
		if (r) {
			//记录接收案件日志
	    	var aj = $.ajax({    
	    	    url:urls.flowAddUrl, 
	    	    data:{    
	    	    	caseid:caseid 
	    	    },    
	    	    type:'post',    
	    	    cache:false,    
	    	    dataType:'json',    
	    	    success:function(data) { 
	    	        if(data.success == true){    
	    	        	$.post(urls.saveSendUrl, {
	    					menuid:menuid,
	    					activityId:activityId,
	    					caseid:caseid,
	    					userid:row.userid 
	    				}, function(result) {
	    					if (result.success) {
	    						xzfyDataGrid.datagrid("reload");
	    						parent.$.modalDialog.handler.dialog("close");
	    					} else {
	    						parent.$.messager.alert("错误", result.title, "error");
	    					}
	    				}, "json");
	    	        }else{    
	    	        	easyui_warn(data.title, null);
	    	        }    
	    	     },    
	    	     error:function(data) {    
	    	    	 easyui_warn("案件接收失败，请刷新页面重新接收", null);
	    	     }    
	    	});  
		}
	});
}


/**
 * 相似案件管理
 */
function similarCaseManagement(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var deftype=row.deftype;
	var admtype=row.admtype;
	var casetype=row.casetype;
	window.open('similcaseManagement_init.do?deftype='+deftype+'&admtype='+admtype+'&casetype='+casetype,'newwindow','width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-60)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no') 
	
}

function isReadonlyByNodeid(nodeid)
{
	var form = parent.$.modalDialog.handler.find('#xzfyAcceptForm');
	if ("3" == nodeid) // 受理
	{
		form.find("#b").combobox({required:true});
		form.find("#agentRemark").textbox({required:true});
		form.find("#b").combobox({"readonly":false});
		form.find("#agentRemark").textbox({"readonly":false});
	}
	else if ("4" == nodeid) // 科室
	{
		form.find("#sectionRemark").textbox({required:true});
		form.find("#sectionRemark").textbox({"readonly":false});
	}
	else if ("5" == nodeid) // 机构
	{
		form.find("#organRemark").textbox({required:true});
		form.find("#organRemark").textbox({"readonly":false});
	}
	else if ("6" == nodeid) // 机关
	{
		form.find("#officeRemark").textbox({required:true});
		form.find("#officeRemark").textbox({"readonly":false});
	}
}