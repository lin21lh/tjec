//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryXzfyDecisionList.do",	
	editUrl:baseUrl + "xzfyDecisionEdit.do",
	editSaveUrl:baseUrl + "xzfyDecisionSaveEdit.do",
	sendWFUrl:baseUrl + "xzfyDecisionFlow.do",
	xzUrl : baseUrl + "queryGroupList.do",
	ajxzUrl : baseUrl + "ajxzInit.do",
	saveAjxzUrl : baseUrl + "saveAjxz.do",
	saveAjZtUrl : baseUrl + "saveAjZt.do",
	sendUrl:baseUrl + "sendToOtherUserInit.do",
	sendToOtherUrl:baseUrl + "queryUserListForSend.do",
	returnUrl:baseUrl + "xzfyReturn.do",
	saveSendUrl:baseUrl + "saveCaseToOther.do"
};

var panel_ctl_handles = [{
	panelname:"#xzfyDecisionPanel", 	// 要折叠的面板id
	gridname:"#xzfyDecisionDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 			// 折叠按钮id
}];

//默认加载
$(function() {
	
	$('#xzfyDecisionPanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	
	$("#opttype").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "未处理", value : "1", selected:true}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			xzfyDecisionQuery();
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
	loadXzfyDecisionGrid(urls.gridUrl);
	
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

//加载复议审查grid列表
function loadXzfyDecisionGrid(url) {
	
	xzfyDataGrid = $("#xzfyDecisionDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:10,
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
function xzfyDecisionQuery(){
	
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
 * 复议审查
 * @param index
 */
function editXzfyDecision(){
	
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
	
    var row = $('#xzfyDecisionDataGrid').datagrid("getSelected"); 
    if (row){  
    	openXzfyDecisionDialog(row);
    }  
}

function openEditOrReview(row)
{
	if ("16" == row.nodeid)
	{
		openXzfyDecisionDialog(row);
	}
	else
	{
		xzfyDecisionDetail();
	}
}

/**
 * 打开复议审理决定
 * @param row
 */
function openXzfyDecisionDialog(row){
	
	parent.$.modalDialog({
		title:"审理决定",
		width:900,
		height:600,
		href:urls.editUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "result01", "HEARRESULT", "code", "name", mdDialog);             // 审结结果
			comboboxFuncByCondFilter(menuid, "result02", "AUDITRESULT", "code", "name", mdDialog);	    // 审理结果是否同意
			comboboxFuncByCondFilter(menuid, "isgreat", "SYS_TRUE_FALSE", "code", "name", mdDialog);	// 是否重大案件备案
			showFileDiv(mdDialog.find("#auditfiletd"), false, "XZFYSC", row.caseid, "");
			isShowByProtype(row.protype);// 中止原因等是否展示
		},
		buttons:[{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				saveXzfyDecisionForm(urls.editSaveUrl, "xzfyDecisionForm");
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
 * 保存复议申请审查信息
 * @param url
 * @param form
 */
function saveXzfyDecisionForm(url, form) {
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
 * 弹出页面发送
 */
function sendAudit(row){
	parent.$.messager.confirm("发送确认", "确认要发送该复议申请？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
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
 * 复议送审
 */
function sendWF(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	
	parent.$.messager.confirm("送审确认", "确认要审批选中复议申请？", function(r) {
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
/****************************案件小组维护****************************/
/**
 * 案件小组维护
 */

function ajxzEdit001() {

	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一个案件！", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "案件小组维护",
		width : 980,
		height : 450,
		href : urls.ajxzUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//可选小组列表
			kxxzDataGrid(mdDialog, row.caseid);
			//已选小组列表
			yxxzDataGrid(mdDialog, row.caseid);
			
			mdDialog.find("#addBtn").on("click",function(){
				triggerSaveAjxz(row.caseid,urls.saveAjxzUrl,"add");
			});
			mdDialog.find("#delBtn").on("click",function(){
				triggerSaveAjxz(row.caseid,urls.saveAjxzUrl,"delete");
			});
		}
	});
}

//可选小组列表
function kxxzDataGrid(mdDialog, caseid) {
	kxgrid = mdDialog.find("#kxxzTable").datagrid({
		title : "可选小组列表",
		height : 400,
		width : '100%',
		collapsible : false,
		url : urls.xzUrl,
		queryParams : {
			caseid : caseid,
			operflag : 'kx'
		},
		singleSelect : true,
		rownumbers : true,
		idField : 'groupid',
		columns : [ [ {
			field : "groupid",
			checkbox : true
		}, {
			field : "groupname",
			title : "小组名称",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "ifcheckname",
			title : "是否允许查看案件",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "casedesc",
			title : "案件描述",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "question",
			title : "咨询问题",
			halign : 'center',
			width : 100,
			sortable : true
		}]],
        onDblClickRow:function (rowIndex, rowData) {       	 
        	saveAjxz(rowData.groupid,caseid,urls.saveAjxzUrl,'add');
        }		
	});
	return kxgrid;
}

//已选小组列表
function yxxzDataGrid(mdDialog,caseid){
	yxgrid = mdDialog.find("#yxxzTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选小组列表',
		collapsible: false,
		url : urls.xzUrl,
		queryParams : {caseid:caseid,operflag : 'yx'},
		singleSelect: true,
		rownumbers : true,
		idField: 'groupid',
		columns : [ [ {
			field : "groupid",
			checkbox : true
		}, {
			field : "groupname",
			title : "小组名称",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "ifcheckname",
			title : "是否允许查看案件",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "casedesc",
			title : "案件描述",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "question",
			title : "咨询问题",
			halign : 'center',
			width : 100,
			sortable : true
		}]],
       onDblClickRow:function (rowIndex, rowData) {		        	 
    	   saveAjxz(rowData.groupid,caseid,urls.saveAjxzUrl,'delete');
       }
	});
	return yxgrid;
}
function triggerSaveAjxz(caseid,url,flag){
	var selectRow;
	var row;
	var title = "";
	if(flag=="add") {		
		selectRow = kxgrid.datagrid('getChecked');
		row = kxgrid.datagrid("getSelections")[0];
		title = "可选";
	}else {
		selectRow = yxgrid.datagrid('getChecked');
		row = yxgrid.datagrid("getSelections")[0];
		title = "已选";
	}
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一个"+title+"小组！", null);
		return;
	}
	saveAjxz(row.groupid,caseid,url,flag);
	
}
function saveAjxz(groupid,caseid,url,flag){
	var params = {
			groupid:groupid,
			caseid:caseid,
			operflag:flag
	};
	$.ajax({
		type:'post',
		url:url,
		data:params,
		success:function(data) {
			if (data.success) {
				kxgrid.datagrid("reload");
				kxgrid.datagrid("clearSelections");
				yxgrid.datagrid("reload");
				yxgrid.datagrid("clearSelections");
			} else {
				easyui_warn(data.title);
			}
		}
	});
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
			width : 150
		} ] ]
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
			$.post(urls.saveSendUrl, {
				menuid:menuid,
				activityId:activityId,
				caseid:caseid,
				userid:row.userid 
			}, function(result) {
				if (result.success) {
					xzfyAcceptDataGrid.datagrid("reload");
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}

/**
 * 小组成员维护
 */

function ajxzEdit() {
	var zjxzUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "zjxzInit.do";
	
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一个案件！", null);
		return;
	}
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var groupid = "";
	parent.$.modalDialog({
		title : "复议研讨",
		width : 970,
		height : 550,
		href : zjxzUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//所有委员列表
			zjDataGrid(mdDialog, groupid);
			//小组委员列表
			zjxzDataGrid(mdDialog, groupid);
			
			var selectRows;
			mdDialog.find("#addBtn").on("click",function(){
				selectRows = kxgrid.datagrid('getChecked');
				if (selectRows == null || selectRows.length == 0) {
					easyui_warn("请选择一个可选委员！", null);
					return;
				}
				var selectNum=selectRows.length;
				
				for(var i = selectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = kxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjxzTable").datagrid('appendRow', rowInfo);//追加一行
			    	mdDialog.find("#zjTable").datagrid('deleteRow',index);//删除一行
				}
			});
			mdDialog.find("#delBtn").on("click",function(){
				selectRows = yxgrid.datagrid('getChecked');
				if (selectRows == null || selectRows.length == 0) {
					easyui_warn("请选择一个已选委员！", null);
					return;
				}
				var selectNum=selectRows.length;
				for(var i = selectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = kxgrid.datagrid('getRowIndex',rowInfo);
					var index = yxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjTable").datagrid('appendRow', rowInfo);//追加一行
			    	mdDialog.find("#zjxzTable").datagrid('deleteRow', index);//删除一行
				}
			});
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				setspeIds();
				submitForm(urls.saveAjZtUrl, "zjxzForm", "");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

//可选委员列表
function zjDataGrid(mdDialog, groupid) {
	
	var zjUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "querySpeList.do";
	
	kxgrid = mdDialog.find("#zjTable").datagrid({
		title : "可选委员列表",
		height : 400,
		width : '100%',
		collapsible : false,
		url : zjUrl,
		queryParams : {
			groupid : groupid,
			operflag : 'all'
		},
		singleSelect : false,
		rownumbers : true,
		idField : 'speid',
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 100,
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
		} ] ],
        onDblClickRow:function (rowIndex, rowData) {       	 
    	  mdDialog.find("#zjxzTable").datagrid('appendRow', rowData);//追加一行
    	  mdDialog.find("#zjTable").datagrid('deleteRow',rowIndex);//删除一行
        }
	});
	return kxgrid;
}

//已选委员列表
function zjxzDataGrid(mdDialog,groupid){
	var zjUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "querySpeList.do";
	
	yxgrid = mdDialog.find("#zjxzTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选委员列表',
		collapsible: false,
		url : zjUrl,
		queryParams : {groupid:groupid,operflag : 'group'},
		singleSelect: false,
		rownumbers : true,
		idField: 'speid',
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 100,
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
		} ] ],
       onDblClickRow:function (rowIndex, rowData) {		        	 
    	   mdDialog.find("#zjTable").datagrid('appendRow', rowData);//追加一行
     	   mdDialog.find("#zjxzTable").datagrid('deleteRow',rowIndex);//删除一行
       }
	});
	return yxgrid;
}
function triggerSaveZjxz(groupid,url,flag){
	var selectRow;
	var row;
	var title = "";
	if(flag=="add") {		
		selectRow = kxgrid.datagrid('getChecked');
		row = kxgrid.datagrid("getSelections")[0];
		title = "可选";
	}else {
		selectRow = yxgrid.datagrid('getChecked');
		row = yxgrid.datagrid("getSelections")[0];
		title = "已选";
	}
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一个"+title+"委员！", null);
		return;
	}
	saveZjxz(row.speid,groupid,url,flag);
	
}
function saveZjxz(speid,groupid,url,flag){
	var params = {
			speid:speid,
			groupid:groupid,
			operflag:flag
	};
	$.ajax({
		type:'post',
		url:url,
		data:params,
		success:function(data) {
			if (data.success) {
				kxgrid.datagrid("reload");
				kxgrid.datagrid("clearSelections");
				yxgrid.datagrid("reload");
				yxgrid.datagrid("clearSelections");
			} else {
				easyui_warn(data.title);
			}
		}
	});
}

/**
 * 委员意见查看
 */
function sugView() {
	
	var viewUrl = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/view.do";
	
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一个案件！", null);
		return;
	}
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title : "委员意见查看",
		width : 900,
		height : 500,
		href : viewUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function setspeIds(){
	var speids="";
	var rows = yxgrid.datagrid("getRows");
	for(var i = 0; i < rows.length; i++){
		speids = speids + rows[i].speid + ";"
	}
	parent.$.modalDialog.handler.find('#zjxzForm').find("#speids").val(speids);
}

//提交表单
function submitForm(url, form, workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	var speids = parent.$.modalDialog.handler.find('#zjxzForm').find("#speids").val();
	if(speids == null || speids == ''){
		easyui_warn("请先为该主题分配委员！", null);
		return;
	}
	form.form("submit", {
		url : url,
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {
				easyui_info(result.title,function(){});
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
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

/**
 * 根据当前案件审结结果
 */
function isShowByProtype(protype)
{
	var form = parent.$.modalDialog.handler.find('#xzfyDecisionForm');
	if ("07" == protype) // 延期
	{
		form.find("#delay").show();
	}
	else
	{
		form.find("#delay").hide();
	}
}