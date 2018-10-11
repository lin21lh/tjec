//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryXzfyReqList.do",					   // 查询接收材料以及案件登记列表
	reqEditUrl:baseUrl + "xzfyReqEdit.do",							   // 打开案件登记页面
	editCommitUrl:baseUrl + "xzfyReqEdit.do",					   // 保存案件登记
	sendReqUrl:baseUrl + "xzfyReqFlow.do",						   // 发送到受理节点
	returnUrl:baseUrl + "xzfyReturn.do",                 // 回退
	receiveUrl:baseUrl + "xzfyReceiveEdit.do",						   // 打开接收材料页面
	saveReceiveUrl:baseUrl + "xzfyReceiveSaveEdit.do",				   // 保存接收材料
	sendReceiveUrl:baseUrl + "xzfyReceiveFlow.do"				   // 发送到案件登记
};

var panel_ctl_handles = [{
	panelname:"#xzfyReqPanel", 		// 要折叠的面板id
	gridname:"#xzfyReqDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
	$('#xzfyReqPanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	
	$("#opttype").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "已提交", value : "0"}, {text : "已退回", value : "9"}]
	});
	
	//加载Grid数据
	loadXzfyReqGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#opttype").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	$("#admtype").textbox("addClearBtn", icons);
	$("#casetype").textbox("addClearBtn", icons);
	$("#deftype").textbox("addClearBtn", icons);
});

var xzfyDataGrid;
function showReload(){
	xzfyDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadXzfyReqGrid(url) {
	
	xzfyDataGrid = $("#xzfyReqDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:5,
		pageList: [5, 10, 15],
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
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:80, sortable:true, hidden:true}
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

function openEditOrReview(row)
{
	var nodeid = row.nodeid;
	if ("1" == nodeid)
	{
		xzfyReceiveByCaseid(row.caseid);
	}
	else if ("2" == nodeid)
	{
		xzfyReqEdit(row);
	}
	else
	{
		xzfyReqDetail();
	}
}

/**
 * 初始化案件登记页面
 */
function xzfyReqEdit(row){
	parent.$.modalDialog({
		title:"案件登记",
		width:900,
		height:600,
		href:urls.reqEditUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "apptype", "B_CASEBASEINFO_APPTYPE", "code", "name", mdDialog);	    //申请人类型
			comboboxFuncByCondFilter(menuid, "idtype", "B_CASEBASEINFO_IDTYPE", "code", "name", mdDialog);		    //证件类型
			comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name", mdDialog);	    //被申请人类型
			comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name", mdDialog);	                //行政管理类型
			comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name", mdDialog);	    //申请复议事项类型
			comboboxFuncByCondFilter(menuid, "ifcompensation", "SYS_TRUE_FALSE", "code", "name", mdDialog);	        //是否附带行政赔偿
			comboboxFuncByCondFilter(menuid, "receiveway", "PUB_PROBASEINFO_RECEIVEWAY", "code", "name", mdDialog);	//收文方式
			comboboxFuncByCondFilter(menuid, "thtype", "B_CASEBASEINFO_APPTYPE", "code", "name", mdDialog);	        //第三人类型
			comboboxFuncByCondFilter(menuid, "thidtype", "B_CASEBASEINFO_IDTYPE", "code", "name", mdDialog);	    //第三人证件类型
			var f = parent.$.modalDialog.handler.find('#xzfyReqForm');
			f.form("load", row);
		},
		buttons:[{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitReqForm(urls.editCommitUrl, "xzfyReqForm");
			}
		},{
			id:"sendBtn",
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

//提交表单
function submitReqForm(url, form){
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
			$.post(urls.sendReqUrl, {
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
 * 初始行政复议接收材料页面
 */
function xzfyReceive() {
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	var caseid = "";
	if(null != selectRow && selectRow.length == 1){
		var row = xzfyDataGrid.datagrid("getSelections")[0];
		caseid = row.caseid;
	}
	xzfyReceiveByCaseid(caseid);
}

/**
 * 初始行政复议接收材料页面
 */
function xzfyReceiveByCaseid(caseid) {
	parent.$.modalDialog({
		title:"接收材料",
		width:600,
		height:350,
		href:urls.receiveUrl + "?caseid=" + caseid,
		buttons:[{
				id: "fileBtn",
				disabled:true,
				text:"附件",
				iconCls:"icon-save",
				handler:function() {
					parent.clickUploadDiv2('XZFYCL');
				}
			},{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					submitReceiveForm(urls.saveReceiveUrl, "xzfyReceiveForm");
				}
			},{
				id:"sendBtn",
				disabled:true,
				text:"发送",
				iconCls:"icon-redo",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					caseid = mdDialog.find("#caseid").val();
					sendReceive(caseid);
				}
			}]
	});
}

/**
 * 提交表单
 */
function submitReceiveForm(url, form) {
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
				parent.$.messager.alert("提示", result.title, "success");
				var caseid = result.body.caseid;
				form.find("#caseid").val(caseid);
				parent.showFileBtn();
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 发送
 */
function sendReceive(caseid) {
	parent.$.messager.confirm("送审确认", "确认要发送选中复议申请？", function(r) {
		if (r) {
			$.post(urls.sendReceiveUrl, {
				menuid:menuid,
				caseid:caseid 
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