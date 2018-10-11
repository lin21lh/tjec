var baseUrl = contextpath + "aros/xzys/controller/";
var projectDataGrid;
var urls = {
	queryUrl : baseUrl + "queryList.do",   // 查询案件列表
	addUrl : baseUrl + "add.do",           // 收案登记编辑页面
	saveUrl : baseUrl + "save.do",         // 应诉案件保存
	sendUrl : baseUrl + "send.do",          // 应诉案件发送
	deleteUrl : baseUrl + "delete.do",      // 应诉案件删除
	// 回退
	backFlowPageUrl:baseUrl + "backFlowPage.do",
	returnUrl:baseUrl + "xzysReturn.do",     // 行政应诉案件回退
	
	
	reviewUrl:baseUrl + "xzysReview.do",      // 案件审查编辑页面 
	examSaveUrl:baseUrl+ "xzysExamSave.do",                //案件审查保存
	examSendUrl:baseUrl+"xzysExamSend.do",                  //案件审查发送
	appeartUrl:baseUrl+"xzysAppeart.do",             //出庭应诉编辑界面
	appeartSaveUrl:baseUrl+ "xzysAppeartSave.do",                //出庭应诉保存
	appeartSendUrl:baseUrl+"xzysAppeartSend.do",                  //出庭应诉发送
	
	
	filingUrl:baseUrl+"xzysFiling.do",                    //行政应诉立案归档编辑页面
	queryCaseBaseinfoList:baseUrl+"queryCaseBaseinfoList.do",   //查询应诉案件列表信息
	addCaseFileUrl: baseUrl+"addCaseFile.do",             //行政应诉新增案件文档
	saveAddCaseFile: baseUrl+"saveAddCaseFile.do",        // 保存新增的文档信息
	caseFileListPage:baseUrl+"caseFileListPage.do",       //案件卷宗查看列表
	caseFileInit :baseUrl+"caseFileInit.do",              //应诉案件查询列表查询
	noticeSendUrl:baseUrl + "xzfyCaseEndNoticePlaceFlow.do", // 应诉案件结案归档
	
	
}; 
var panel_ctl_handles = [{
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

/**
 * 默认加载
 */
$(function() {
    $('#qpanel1').panel('close');
	
	var icons = {iconCls:"icon-clear"};
	$("#lawid").textbox("addClearBtn", icons);
	$("#suerequest").textbox("addClearBtn", icons);
	$("#recdate").datebox("addClearBtn", icons);
	
	
	loadProjectGrid(urls.queryUrl);
	//判断操作按钮是否隐藏
	var flag = $("#flag").val();
	if (flag == 1) {
		$("#LABtn").show();
		$("#SLBtn").show();
	}else{
		$("#LABtn").hide();
		$("#SLBtn").hide();
		
	}
});

/**
 * 页面刷新
 */
function showReload() {
	projectDataGrid.datagrid('reload');
}

/**
 * 加载可项目grid列表
 * 
 * @param url
 */
function loadProjectGrid(url) {
	projectDataGrid = $("#projectDataGrid").datagrid({
		fit : true,
		fitColumns:true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		pageSize : 30,
		queryParams : {
			menuid : menuid
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[
        {field : "id",checkbox : true},
        {field : "lawid",title : "案号",halign : 'center',width : 100,sortable : true}, 
	    {field : "regtypename",title : "审理阶段",halign : 'center',width : 80,sortable : true},
		{field : "jurilawname",title : "受案法院",halign : 'center',width : 80,sortable : true},
		{field : "suetypename",title : "起诉类型",halign : 'center',width : 80,sortable : true},
		{field : "suerequest",title : "原告情况",halign : 'center',width : 150,sortable : true},
		{field : "recdate",title : "收案时间",halign : 'center',width : 80,sortable : true},
		{field: "nodeid", title:"节点编号", halign:"center", width:50, sortable:true, hidden:true},
		]],
		onDblClickRow: function (rowIndex, rowData) {  
			xzysEditDialog(rowData);
        },
		onClickRow: function(rowIndex,rowData) {
        	viewFlow();
        	showHiddenDeleteBtn(rowData.nodeid);
        }
		
	});
}
/**
 * 初始化案件登记页面
 */
var nodeidtmp = 10;
function xzysEditDialog(row) {
	nodeidtmp = row.nodeid;
	if (10 == nodeidtmp ) {
		xzfyEdit("收案登记", 900, 380, urls.addUrl, row, "projectAddForm", urls.saveUrl, urls.sendUrl);
	}
	else if (20 == nodeidtmp) {
		xzfyEdit("案件审查", 700, 600, urls.reviewUrl, row, "xzysExamForm", urls.examSaveUrl, urls.examSendUrl);
	}
	else if (30 == nodeidtmp) {
		xzfyEdit("出庭应诉", 720, 600, urls.appeartUrl, row, "xzfyAppearForm", urls.appeartSaveUrl, urls.appeartSendUrl);
	}
	else if (40 == nodeidtmp) {
		xzfyEdit("立案归档", 1000, 550, urls.filingUrl, row, "xzfyFilingForm", "", urls.noticeSendUrl);
	}
}

function xzfyEdit(title, width, height, editUrl, row, form, saveUrl, sendUrl) {
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: editUrl,
		queryParams: {
//			lawid: row.lawid,
			protype: row.protype,
			nodeid: nodeidtmp,
			id : row.id
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			var f = mdDialog.find("#"+form);
			
			if(10 == nodeidtmp){
				mdDialog.find("#regtype").combobox({
					onLoadSuccess : function(){
						changeSsbgTr(mdDialog);
					},
					onSelect : function(){
						changeSsbgTr(mdDialog);
					}
				});
				comboboxFuncByCondFilter(menuid, "regtype", "REGTYPE", "code", "name", mdDialog);// 审理阶段
				comboboxFuncByCondFilter(menuid, "suetype", "SUETYPE", "code", "name", mdDialog);// 起诉方式
				comboboxFuncByCondFilter(menuid, "jurilaw", "JURILAW", "code", "name", mdDialog);// 管辖法院
				f.form("load", row);
				
			}else if(30 == nodeidtmp ){
				comboboxFuncByCondFilter(menuid, "trailresult", "TRAILRESULT", "code", "name", mdDialog);// 应诉结果
				comboboxFuncByCondFilter(menuid, "ifsite", "ISORNOT", "code", "name", mdDialog);    // 负责人是否出庭
			}
		},
		onDestroy: function() {
			showReload();
		},
		buttons: getButtons(row, form, saveUrl, sendUrl)
	});
}



function getButtons (row, form, saveUrl, sendUrl) {
	var buttons;
	if (10 == nodeidtmp) {
		buttons= [{
			id: "saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				submitForm(saveUrl, form, nodeidtmp);
			}
		},{
			id:"sendBtn",
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				send(sendUrl, row.id, row.nodeid);
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	}else if (20 == nodeidtmp || 30 == nodeidtmp) {
		var code = "XZYS_AJSC";
		if(30 == nodeidtmp){
			 code = "XZYS_CTYS";
		}
		buttons= [{
			id: "fileBtn",
			text:"附件",
			iconCls:"icon-save",
			handler:function() {
				parent.clickUploadDiv2(code);
			}
		},{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				submitForm(saveUrl, form, nodeidtmp);
			}
		},{
			id:"sendBtn",
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				send(sendUrl, row.caseid, row.nodeid);
			}
		},{
			id:"backBtn",
			text:"回退",
			iconCls:"icon-undo",
			handler:function() {
				xzysReturn(row);
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	}else if(40 == nodeidtmp){
		buttons= [{
			id:"sendBtn",
			text:"立案归档",
			iconCls:"icon-redo",
			handler:function() {
				send(sendUrl, row.id, row.nodeid);
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	}
	
	return buttons;
}
/**
 * 回退
 */
function xzysReturn(row) {
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
							lawid:row.id,
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
/**
 * 查询
 */
function xzysQuery() {
	var param = {
		lawid : $("#lawid").textbox('getValue'),
		suerequest : $("#suerequest").textbox('getValue'),
		srecdate : $("#srecdate").datebox('getValue'),
		erecdate : $("#erecdate").datebox('getValue'),
		menuid : menuid
	};
	projectDataGrid.datagrid("load", param);
}

/**
 * 收案登记
 */
function xzysReceive(nodeid,id) {
	parent.$.modalDialog({
		title : "发起立案申请",
		width : 870,
		height : 370,
		href : urls.addUrl,
		queryParams: {
			nodeid: 10,
			id : id
		},
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "regtype", "REGTYPE", "code", "name", mdDialog);// 审理阶段
			comboboxFuncByCondFilter(menuid, "suetype", "SUETYPE", "code", "name", mdDialog);// 起诉方式
			comboboxFuncByCondFilter(menuid, "jurilaw", "JURILAW", "code", "name", mdDialog);// 管辖法院
			mdDialog.find("#regtype").combobox({
				onChange : function(){
					changeSsbgTr(mdDialog);
				}
			});

		},
		buttons : [ {
			id: "saveBtn",
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "projectAddForm", 10);
			}
		},{
			id: "sendBtn",
			text : "发送",
			disabled:true,
			iconCls : "icon-redo",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				send(urls.sendUrl, id, nodeid);
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

function changeSsbgTr(mdDialog){
	var regtype = mdDialog.find("#regtype").combobox("getValue");
	if(regtype=="01") {
		//关联案号
		 mdDialog.find("#glah").hide();
		 mdDialog.find("#glahlawid").textbox("clear");
		 mdDialog.find("#caseid").val("");
	}else {
		 mdDialog.find("#glah").show();
	}
}


// 提交表单
function submitForm(url, form, nodeid) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	var protype = "20";
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit", {
		url : url,
		queryParams: {
			protype: protype,
			nodeid: nodeid
		},
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
				var modalDialog = parent.$.modalDialog.handler;
				modalDialog.find("#id").val(result.body.id);
				if (10 == nodeid) {
					parent.showSendBtn();
				}
				projectDataGrid.datagrid('reload');
				parent.$.messager.alert("提示", result.title, "success");
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}



/**
 * 发送
 */
function send(url, id, nodeid) {
	var modalDialog = parent.$.modalDialog.handler;
	var protype = "20";
	id = modalDialog.find("#id").val();
	if(10 == nodeid){
		id = modalDialog.find("#id").val();
	}else if(20 == nodeid){
		id = modalDialog.find("#lawid").val();
	}else if(30 == nodeid){
		id = modalDialog.find("#lawid").val();
	}else if(40 == nodeid){
		id = modalDialog.find("#caseid").val();
	}
	var prompt = "确认要发送选中应诉案件？";
	if(nodeid == '40'){
		 prompt = "确认要将案件结案归档？";
	}
	parent.$.messager.confirm("送审确认", prompt, function(r) {
		if (r) {
			$.post(url, {
				menuid:menuid,
				id:id,
				protype:protype
			}, function(result) {
				if (result.success) {
					parent.$.messager.alert("提示", result.title, "success");
					parent.$.modalDialog.handler.dialog("close");
					projectDataGrid.datagrid('reload');
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
	
	
}

function showHiddenDeleteBtn(nodeid){
	if(10 == nodeid){
		$("#SLBtn").show();
	}else{
		$("#SLBtn").hide();
	}
}

/**
 * 删除
 */
function xzfyDelete() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = projectDataGrid.datagrid("getSelections")[0];
			$.post(urls.deleteUrl, {
				id : row.id
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}


/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = projectDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条应诉案件", null);
		return;
	}
	showFlowModalDialogForReq(selectRow[0].id, selectRow[0].protype);
}

/**
 * 行政复议申请流程信息查看
 * @param caseid
 */
function showFlowModalDialogForReq(id, protype) {
	
	if(id == '' || id == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	var href = contextpath
			+ "aros/xzys/controller/queryFlowForReq.do?id="+ id + "&protype="+protype;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
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




