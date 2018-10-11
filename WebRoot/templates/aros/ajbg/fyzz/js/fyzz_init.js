//请求路径
var baseUrl = contextpath + "aros/ajbg/fyzz/FyzzController/";
var urls = {
	// 查询复议中止案件列表
	gridUrl:baseUrl + "queryFyzzList.do",
	// 复议中止发起初始化
	fyzzAddInitUrl:baseUrl + "fyzzAddInit.do",
	// 复议中止发起保存
	fyzzAddSaveUrl:baseUrl + "fyzzAddSave.do",
	// 复议中止发起发送
	fyzzAddSendUrl:baseUrl + "fyzzAddFlow.do",
	// 复议中止审批编辑
	fyzzAuditInitUrl:baseUrl + "fyzzAuditInit.do",
	// 复议中止审批保存
	fyzzAuditSaveUrl:baseUrl + "fyzzAuditSave.do",
	// 复议中止审批发送
	fyzzAuditSendUrl:baseUrl + "fyzzAuditFlow.do",
	// 处理决定编辑
	fyzzDecideInitUrl:baseUrl + "fyzzDecideInit.do",
	// 处理决定保存
	fyzzDecideSaveUrl:baseUrl + "fyzzDecideSave.do",
	// 处理决定发送
	fyzzDecideSendUrl:baseUrl + "fyzzDecideFlow.do",
	// 回退
	backFlowPageUrl:baseUrl + "backFlowPage.do",
	// 回退
	returnUrl:baseUrl + "fyzzReturnFlow.do",
	// 删除
	deleteUrl:baseUrl + "fyzzDelete.do"
};

var panel_ctl_handles = [{
	panelname:"#xzfyPanel", 	// 要折叠的面板id
	gridname:"#xzfyDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 	// 折叠按钮id
}];

//默认加载
$(function() {
	$('#xzfyPanel').panel('close');
	
	var icons = {iconCls:"icon-clear"};
	$("#csaecode").textbox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	
	//加载Grid数据
	loadXzfyGrid(urls.gridUrl);
	
	//判断操作按钮是否隐藏
	var flag = $("#flag").val();
	if (flag == 0) {
		$("#operSpan").hide();
	}
});

//加载复议中止列表
var xzfyDataGrid;

function loadXzfyGrid(url) {
	xzfyDataGrid = $("#xzfyDataGrid").datagrid({
		fit: true,
		stripe: true,
		singleSelect: true,
		rownumbers: true,
		pagination: true,
		remoteSort: false,
		multiSort: true,
		pageSize: 10,
		queryParams: {
			menuid:menuid,
			firstNode:true,
			lastNode:false
		},
		url: url,
		loadMsg: "正在加载，请稍候......",
		toolbar: "#toolbar_center",
		showFooter: true,
		onDblClickRow: function (rowIndex, rowData) {  
			xzfyEditDialog(rowData);
        },
        onClickRow: function(rowIndex,rowData) {
  //      	viewTime();
        	viewFlow();
        },
		columns:[[ 
		  {field:"ccrid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:220, sortable:true},
		  {field:"intro", title:"案件名称", halign:"center", width:400, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:200, sortable:true},
		  {field:"opttime", title:"操作日期", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function xzfyQuery() {
	var param = {
		menuid: menuid,
		csaecode: $("#csaecode").val(),
		appname: $("#appname").val(),
		defname: $("#defname").val(),
		firstNode:true,
		lastNode:false
	};
	
	xzfyDataGrid.datagrid("load", param);
}

/**
 * 重载
 */
function showReload() {
	xzfyDataGrid.datagrid("reload"); 
}

/**
 * 中止发起
 */
function fyzzAdd(title, code, ccrid, nodeid) {
	parent.$.modalDialog({
		title: "复议中止发起",
		width: 900,
		height: 550,
		href: urls.fyzzAddInitUrl,
		queryParams: {
			ccrid: ccrid,
			nodeid: 10 
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			
			comboboxFuncByCondFilter(menuid, "suspendreason", "PUB_PROBASEINFO_SUSPEND", "code", "name", mdDialog);       
		},
		onDestroy: function() {
			showReload();
		},
		buttons:[{
				id: "saveBtn",
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					submitForm(urls.fyzzAddSaveUrl, "fyzzAddForm", 10);
				}
			},{
				id: "sendBtn",
				disabled:true,
				text:"发送",
				iconCls:"icon-redo",
				handler:function() {
					send(urls.fyzzAddSendUrl, ccrid, 10);
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
 * 提交表单
 */
function submitForm(url, form, nodeid) {
	
	var form = parent.$.modalDialog.handler.find('#' + form);
	var protype = "02";
	
	var isValid = form.form('validate');
	
	if (!isValid) {
		return;
	}
		
	form.form("submit", {
		url:url,
		queryParams: {
			protype: protype,
			nodeid: nodeid
		},
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
				if (10 == nodeid) {
					var ccrid = result.body.ccrid;
					form.find("#ccrid").val(ccrid);
					parent.showSendBtn();
				}
				parent.$.messager.alert("提示", result.title, "success");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 发送
 */
function send(url, ccrid, nodeid) {
	var modalDialog = parent.$.modalDialog.handler;
	var protype = "02";
	
	if (10 == nodeid) {
		ccrid = modalDialog.find("#ccrid").val();
	}
	
	parent.$.messager.confirm("送审确认", "确认要发送选中复议申请？", function(r) {
		if (r) {
			$.post(url, {
				menuid:menuid,
				ccrid:ccrid,
				protype:protype
			}, function(result) {
				if (result.success) {
					parent.$.messager.alert("提示", result.title, "success");
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
/*function xzfyReturn(row) {
	parent.$.messager.confirm("回退确认", "确认要回退该复议申请？", function(r) {
		if (r) {
			$.post(urls.returnUrl, {
				menuid:menuid,
				ccrid:row.ccrid 
			}, function(result) {
				if (result.success) {
					parent.$.messager.alert("提示", result.title, "info");
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}*/

/**
 * 初始化案件登记页面
 */
var nodeidtmp = 1;
function xzfyEditDialog(row) {
	nodeidtmp = row.nodeid;

	if (10 == nodeidtmp) {
		xzfyEdit("复议中止发起", 900, 550, urls.fyzzAddInitUrl, row, "fyzzAddForm", urls.fyzzAddSaveUrl, urls.fyzzAddSendUrl);
	}
	else if (20 == nodeidtmp || 30 == nodeidtmp || 40 == nodeidtmp) {
		xzfyEdit("复议中止审批", 900, 550, urls.fyzzAuditInitUrl, row, "fyzzAuditForm", urls.fyzzAuditSaveUrl, urls.fyzzAuditSendUrl);
	}
	else if (50 == nodeidtmp) {
		xzfyEdit("复议中止决定", 900, 550, urls.fyzzDecideInitUrl, row, "xzfyDecideForm", urls.fyzzDecideSaveUrl, urls.fyzzDecideSendUrl);
	}
}

/**
 * 弹出操作窗口
 * @param title
 * @param width
 * @param height
 * @param editUrl
 * @param row
 * @param form
 * @param saveUrl
 * @param sendUrl
 */
function xzfyEdit(title, width, height, editUrl, row, form, saveUrl, sendUrl) {
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: editUrl,
		queryParams: {
			ccrid: row.ccrid,
			protype: row.protype,
			nodeid: nodeidtmp
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			var f = mdDialog.find("#"+form);
			
			comboboxFuncByCondFilter(menuid, "suspendreason", "PUB_PROBASEINFO_SUSPEND", "code", "name", mdDialog);       
			
			if (10 == nodeidtmp) {
				f.form("load", row); 
			} else if (50 == nodeidtmp) {
				comboboxFuncByCondFilter(menuid, "result", "AUDITRESULT", "code", "name", mdDialog);       // 处理结果
			} else {
				isReadonlyByNodeid(nodeidtmp);
			}
		},
		onDestroy: function() {
			showReload();
		},
		buttons: getButtons(row, form, saveUrl, sendUrl)
	});
}

/**
 * 获取操作按钮
 * @param row
 * @param form
 * @param saveUrl
 * @param sendUrl
 * @returns {___anonymous7081_7087}
 */
function getButtons (row, form, saveUrl, sendUrl) {
	
	var buttons;
	buttons= [{
		id:"saveBtn",
		text:"保存",
		iconCls:"icon-save",
		handler:function() {
			var mdDialog = parent.$.modalDialog.handler;
			
			submitForm(saveUrl, form, nodeidtmp);
		}
	},{
		id:"sendBtn",
		text:"发送",
		iconCls:"icon-redo",
		handler:function() {
			send(sendUrl, row.ccrid, row.nodeid);
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
	}];
	
	return buttons;
}

/**
 * 
 * @param nodeid
 */
function isReadonlyByNodeid(nodeid){
	
	var form = parent.$.modalDialog.handler.find('#fyzzAuditForm');
	if (20 == nodeid) { // 科室
		form.find("#sectionRemark").textbox({required:true});
		form.find("#sectionRemark").textbox({"readonly":false});
	}
	else if (30 == nodeid) {// 机构
		form.find("#organRemark").textbox({required:true});
		form.find("#organRemark").textbox({"readonly":false});
	}
	else if (40 == nodeid) { // 机关
		form.find("#officeRemark").textbox({required:true});
		form.find("#officeRemark").textbox({"readonly":false});
	}
}

/**
 * 删除申请
 */
function fyzzDelete() {
	
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请先选择一条中止申请", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	
	parent.$.messager.confirm("删除确认", "确认要删除该中止申请？", function(r) {
		if (r) {
			$.post(urls.deleteUrl, {
				menuid:menuid,
				ccrid:row.ccrid 
			}, function(result) {
				if (result.success) {
					easyui_info(result.title, null)
					showReload();
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
function xzfyReturn(row) {
	parent.$.modalDialog2({
		title:"回退意见",
		width:500,	
		height:160,
		href:urls.backFlowPageUrl,
		onLoad:function() {
		},
		buttons:[{
			text:"确认回退",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler2;
				var htyjform = mdDialog.find("#thyjform");
				var isValid = htyjform.form("validate");
				if (!isValid) {
					parent.$.messager.progress("close");
					return isValid;
				}
				var htyj = htyjform.find("#htyj").val();
				$.post(urls.returnUrl, {
					menuid:menuid,
					ccrid:row.ccrid,
					htyj :htyj 
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert("提示", result.title, "info");
						parent.$.modalDialog.handler2.dialog("close");
						parent.$.modalDialog.handler.dialog("close");
					} else {
						parent.$.messager.alert("错误", result.title, "error");
					}
				}, "json");
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler2.dialog("close");
			}
		}]
	});
}