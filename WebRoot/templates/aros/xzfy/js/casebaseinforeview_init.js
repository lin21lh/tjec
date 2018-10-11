//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryXzfyReviewList.do",	
	editUrl:baseUrl + "xzfyReviewEdit.do",
	editSaveUrl:baseUrl + "xzfyReviewSaveEdit.do",
	sendWFUrl:baseUrl + "xzfyReviewFlow.do",
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
	panelname:"#xzfyReviewPanel", 		// 要折叠的面板id
	gridname:"#xzfyReviewDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 			// 折叠按钮id
}];

//默认加载
$(function() {
	
	$('#xzfyReviewPanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	
	$("#opttype").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "未处理", value : "1", selected:true}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			xzfyReviewQuery();
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
	loadXzfyReviewGrid(urls.gridUrl);
	
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

//加载复议审理grid列表
function loadXzfyReviewGrid(url) {
	
	xzfyDataGrid = $("#xzfyReviewDataGrid").datagrid({
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
			nodeid:nodeid,
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
function xzfyReviewQuery(){
	
	var param = {
		opttype:$("#opttype").combobox('getValue'),
		appname:$("#appname").val(),
		defname:$("#defname").val(),
		menuid:menuid,
		admtype:$("#admtype").combobox('getValue'),
		casetype:$("#casetype").combobox('getValue'),
		deftype:$("#deftype").combobox('getValue'),
		nodeid:nodeid,
		firstNode:true,
		lastNode:false
	};
	
	xzfyDataGrid.datagrid("load", param);
}

/**
 * 复议审理
 * @param index
 */
function editXzfyReview(){
	
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
	
    var row = $('#xzfyReviewDataGrid').datagrid("getSelected"); 
    if (row){  
    	openXzfyReviewDialog(row); 
    }  
}

function openEditOrReview(row)
{
	if (nodeid == row.nodeid || 11 == nodeid)
	{
		openXzfyReviewDialog(row);
	}
	else
	{
		xzfyReviewDetail();
	}
}

/**
 * 打开复议审理页面
 * @param row
 */
function openXzfyReviewDialog(row){
	parent.$.modalDialog({
		title:"行政复议(决定)审批表",
		width:900,
		height:600,
		href:urls.editUrl + "?caseid=" + row.caseid + "&protype=" + row.protype,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "result", "HEAR", "code", "name", mdDialog); // 审结结果
			isShowByProtypeFuc(row.protype, row.protype);// 延期是否展示
			isReadonlyByNodeid(nodeid);// 审批意见等是否只读
			mdDialog.find("#result").combobox({
				onSelect: function(record) {
					isShowByProtypeFuc(record.id, row.protype);
				}
			});
		},
		buttons:[{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				saveXzfyReviewForm(urls.editSaveUrl, "xzfyReviewForm");
			}
		},{
			id:"auditBtn",
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
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
 * 保存复议申请审理信息
 * @param url
 * @param form
 */
function saveXzfyReviewForm(url, form){
	var form = parent.$.modalDialog.handler.find('#' + form);
	var protype = form.find("#result").combobox('getValue');
	if (protype.indexOf("n") > -1)
	{
		protype = protype.replace("n","")
	}
	else
	{
		protype = "01";
	}
	
	var isValid = form.form('validate');
	if (!isValid) {
		easyui_warn("数据项未填写完整，请处理完整后再保存", null);
		return;
	}
		
	form.form("submit", {
		url:url + "?protype=" + protype,
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
 * 弹出页面送审
 */
function sendAudit(row){
	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
	var protype = form.find("#result").combobox('getValue');
	if (protype.indexOf("n") > -1)
	{
		protype = protype.replace("n","")
	}
	else
	{
		protype = "01";
	}
	parent.$.messager.confirm("发送确认", "确认要发送该复议申请？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid:menuid,
				protype:protype,
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
function sendWF( ){
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
				protype:row.protype,
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
 * 复议主题界面
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
			// 加载所有卷宗
			jzDataGrid(mdDialog, groupid, row.caseid);
			// 加载已选卷宗
			jzyxDataGrid(mdDialog, groupid, row.caseid);
			
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
					var index = yxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjTable").datagrid('appendRow', rowInfo);//追加一行
			    	mdDialog.find("#zjxzTable").datagrid('deleteRow', index);//删除一行
				}
			});
			
			// 卷宗选择
			mdDialog.find("#jz_addBtn").on("click",function(){
				selectRows = jzgrid.datagrid('getSelections');
				if (selectRows == null || selectRows.length == 0) {
					return;
				}
				var jzSelectNum=selectRows.length;
				for(var i = jzSelectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = jzgrid.datagrid('getRowIndex',rowInfo);
					jzyxgrid.datagrid('appendRow', rowInfo);//追加一行
					jzgrid.datagrid('deleteRow', index);//删除一行
				}
			});
			mdDialog.find("#jz_delBtn").on("click",function(){
				selectRows = jzyxgrid.datagrid('getSelections');
				if (selectRows == null || selectRows.length == 0) {
					return;
				}
				var jzSelectNum=selectRows.length;
				for(var i = jzSelectNum - 1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = jzyxgrid.datagrid('getRowIndex',rowInfo);
					jzgrid.datagrid('appendRow', rowInfo);//追加一行
					jzyxgrid.datagrid('deleteRow', index);//删除一行
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
				setJZIds();
				submitForm(urls.saveAjZtUrl, "zjxzForm");
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

// 加载可选卷宗信息
function jzDataGrid(mdDialog,groupid,caseid){

	var jzUrl=contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do";
	
	jzgrid = mdDialog.find("#jzTable").datagrid({
		height: 400,
		width:'100%',
		title: '可选卷宗列表',
		collapsible: false,
		url : jzUrl,
		queryParams : {groupid:groupid,caseid:caseid},
		singleSelect: false,
		rownumbers : true,
		idField: 'noticeid',
		columns : [ [ {
			field : "noticeid",
			checkbox : true
		},{  
			field : "doctype",
			title : "文档名称",
			halign : 'center',
			width : 100,
			sortable : false
		},{  
			field : "protype",
			title : "流程类型",
			halign : 'center',
			width : 100,
			sortable : false
		},{  
			field : "buildtime",
			title : "生成时间",
			halign : 'center',
			width : 100,
			sortable : false
		}
		] ],
       onDblClickRow:function (rowIndex, rowData) {       	 
    	   mdDialog.find("#jzyxTable").datagrid('appendRow', rowData);//追加一行
     	   mdDialog.find("#jzTable").datagrid('deleteRow',rowIndex);//删除一行
       }
	});
	return jzgrid;
}
// 加载已选卷宗信息
function jzyxDataGrid(mdDialog,groupid,caseid){
	var jzUrl=contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do";
	jzyxgrid = mdDialog.find("#jzyxTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选卷宗列表',
		collapsible: false,
		url : jzUrl,
		queryParams : {groupid:groupid,caseid:'01'},
		singleSelect: false,
		rownumbers : true,
		idField: 'noticeid',
		columns : [ [ {
			field : "noticeid",
			checkbox : true
		},{  
			field : "doctype",
			title : "文档名称",
			halign : 'center',
			width : 100,
			sortable : false
		},{
			field : "protype",
			title : "流程类型",
			halign : 'center',
			width : 100,
			sortable : false
		},{
			field : "buildtime",
			title : "生成时间",
			halign : 'center',
			width : 100,
			sortable : false
		}
		] ],
		onDblClickRow:function (rowIndex, rowData) {
			mdDialog.find("#jzTable").datagrid('appendRow', rowData);//追加一行
			mdDialog.find("#jzyxTable").datagrid('deleteRow',rowIndex);//删除一行
		}
	});
	return jzyxgrid;
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
// 设置委员ID
function setspeIds(){
	var speids="";
	var rows = yxgrid.datagrid("getRows");
	for(var i = 0; i < rows.length; i++){
		speids = speids + rows[i].speid + ";"
	}
	parent.$.modalDialog.handler.find('#zjxzForm').find("#speids").val(speids);
}
// 设置卷宗ID
function setJZIds(){
	var jzids="";
	var rows = jzyxgrid.datagrid("getRows");
	for(var i = 0; i < rows.length; i++){
		jzids = jzids + rows[i].noticeid;
		if(rows[i].savemode == '0'){
			jzids = jzids + "_JZ_FJ"
		}
		jzids += ";";
	}
	parent.$.modalDialog.handler.find('#zjxzForm').find("#jzids").val(jzids);
}

//提交表单
function submitForm(url, form) {
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
 * 根据当前案件节点
 */
function isReadonlyByNodeid(nodeid)
{
	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
	if ("12" == nodeid) // 审理承办人
	{
		form.find("#result").combobox({required:true});
		form.find("#agentRemark").textbox({required:true});
		form.find("#result").combobox({"readonly":false});
		form.find("#agentRemark").textbox({"readonly":false});
	}
	else if ("13" == nodeid) // 科室
	{
		form.find("#sectionRemark").textbox({required:true});
		form.find("#sectionRemark").textbox({"readonly":false});
	}
	else if ("14" == nodeid) // 机构
	{
		form.find("#organRemark").textbox({required:true});
		form.find("#organRemark").textbox({"readonly":false});
	}
	else if ("15" == nodeid) // 机关
	{
		form.find("#officeRemark").textbox({required:true});
		form.find("#officeRemark").textbox({"readonly":false});
	}
}

/**
 * 根据当前案件审结结果
 */
function isShowByProtypeFuc(protype, oldprotype)
{
	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
	// 如果变更流程类型
	if (protype.indexOf("n") > -1)
	{
		protype = protype.replace("n","");
	}
	
	if ("07" == protype) // 延期
	{
		form.find("#delay").show();
		form.find("#delaydays").textbox({required:true});
		form.find("#delaydays").textbox({"readonly":false});
	}
	else
	{
		form.find("#delay").hide();
		form.find("#delaydays").textbox({required:false});
		form.find("#delaydays").textbox({"readonly":true});
	}
	
	// 审结结果是否变更
	if (oldprotype != protype)
	{
		form.find("#agentRemark").textbox('clear');
		form.find("#sectionRemark").textbox('clear');
		form.find("#organRemark").textbox('clear');
		form.find("#officeRemark").textbox('clear');
	}
}