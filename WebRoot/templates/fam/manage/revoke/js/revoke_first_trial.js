/**用户注销模块
 * revoke_init.js
 * */
var baseUrl = contextpath + "manage/revoke/controller/AccountRevokeController/";
//路径
var urls = {
	hasRevokelist : contextpath + "manage/change/AccountChangeController/queryAllAccount.do",
	operateRevokeInfo :baseUrl + "operateRevokeInfo.do",
	refuseRevokeInfo : baseUrl + "refuseRevokeInfo.do",
	accountBatchOperation : baseUrl + "accountBatchOperation.do",
	removeRevokeInfo : baseUrl+ "removeRevokeInfo.do",
	downloadFileUrl: contextpath + "base/filemanage/fileManageController/downLoadFile.do",
	deleteFileUrl: contextpath+"base/filemanage/fileManageController/delete.do",
	getFormIsEdit :contextpath+"manage/change/AccountChangeController/getFormIsEdit.do",
	//导出注销申请表
	outRegisterAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRegisterAppicationForm.do"
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};

var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#datagrid_hasRevoke", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

//设置table变量
var datagridHasRevoke;

$(function() {
	var pstatus =spdclData;
	if(lastNode=='true'){
		pstatus =lastNodeSpdclData;
	}
	//加载已注销的用户表
	loaddatagridHasRevoke(urls.hasRevokelist);
	
	$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			queryHasRevokeInfo();
			switch (record.value) {
			case '1':
				$('#btn_query').linkbutton('enable');
				$('#btn_ope').linkbutton('enable');
				$('#btn_appro').linkbutton('enable');
				$('#btn_refuse').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_flow').linkbutton('enable');
				break;
			case '2':
				$('#btn_query').linkbutton('enable');
				$('#btn_ope').linkbutton('disable');
				$('#btn_appro').linkbutton('disable');
				$('#btn_refuse').linkbutton('disable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_flow').linkbutton('enable');
				break; 
			default:
				break;
			}
		},
		onHidePanel: function(){  
	        var status = $('#dealStatus').combobox('getValue');
	        $("#processedStatus").combobox("setValue",'');
	        var data;  //
	        if(status == '1'){//待处理
	        	data = pstatus;
        	}else if(status == '2'){//已处理
	        	data = spyclData;
        	}
	        $("#processedStatus").combobox("loadData",data);
		}
	});
	$("#processedStatus").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		value : "",
		data : pstatus,
		onSelect : function(record) {
			queryHasRevokeInfo();
			$('#btn_revoke').linkbutton('disable');
			if(record.value =='21'){
				$('#btn_revoke').linkbutton('enable');
			}else if(record.value =='22'){
				$('#btn_revoke').linkbutton('disable');
			}
		}
	});
	//默认
	$('#btn_revoke').linkbutton('disable');
	//加载已注销的用户的查询条件
	loadHashRevokeSearch();
});
function showReload() {
	datagridHasRevoke.datagrid('reload');
}
function loaddatagridHasRevoke(url){
	datagridHasRevoke = $("#datagrid_hasRevoke").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : url,
		queryParams: {
			status : 1,
			menuid : menuid,
			activityId : activityId,
			processedStatus : "",
			type : type,
			firstNode : firstNode,
			lastNode : lastNode
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_hasrevoke",
		showFooter : true,
		columns : [ [ {	field : "applicationId",checkbox : true	}
		, {field : "bdgagencycode",title : "预算单位code",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "bdgagencyname",title : "预算单位全称",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "bdgagencycn",title : "预算单位",halign : 'center',fixed : true,	width : 190,sortable:true}
		, {field : "oldAccountName",title : "账户名称",halign : 'center',fixed : true,	width : 180,sortable:true}
		,{field : "oldAccountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
		, {field : "oldAccountType",title : "账户类型",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "oldAccountNumber",title : "银行账号",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "oldBankCode",title : "开户行代码",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "oldBankNameCn",title : "开户行",halign : 'center',fixed : true,	width : 190,sortable:true}
		,{field : "oldType02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
		, {field : "oldType02",title : "账户性质",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true	}
		, {field : "applyReason",title : "注销理由",halign : 'center',fixed : true,	width : 200,sortable:true}
		, {field : "type",title : "备案类型",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "typeName",title : "备案类型",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "deptNature",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true,hidden:true}
		, {field : "deptNatureName",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true}
		, {field : "supervisorDept",title : "主管部门",halign : 'center',fixed : true,	width : 190,sortable:true}
		,{field : "oldLegalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true	}
        ,{field : "oldIdcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true	}
        ,{field : "oldFinancialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
        ,{field : "oldAccountContent",title : "核算内容",halign : 'center',	width:120,sortable:true	}
		, {field : "accountNum",title : "户号",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "oldIszero",title : "零余额账户",halign : 'center',fixed : true,	width : 70,sortable:true,
			formatter: function(value){
				return iszero[value];
			}
		}
		, {field : "deptAddress",title : "单位地址",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "linkman",title : "联系人",halign : 'center',width : 120,sortable:true}
		, {field : "applPhonenumber",title : "联系人手机",halign : 'center',width : 120,sortable:true}
		, {field : "wfid",title : "工作流id",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "wfstatus",title : "工作流状态",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		//,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'center', rowspan:2	,hidden:true}
		, {field : "wfisback",title : "工作流是否退回",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "isopen",title : "是否开户 ",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "isregister",title : "备案",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		//, {field : "status",title : "状态",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "remark",title : "备注",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "changetype",title : "注销事项",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "createUserName",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "createTime",title : "创建时间",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "updateUserName",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "updateTime",title : "修改时间",halign : 'center',fixed : true,	width : 120,sortable:true}
			 ] ]
	});
};


//加载查询条件
function loadHashRevokeSearch(){
	
	//加载查询条件
	comboboxFuncByCondFilter(menuid,"oldAccountTypeSearch", "ACCTTYPE", "code", "name");//账户类型
//	comboboxFuncByCondFilter(menuid,"wfstatusSearch", "WFSTATUS", "code", "name");//当前状态
	$("#oldAccountTypeSearch").combobox("addClearBtn", {iconCls:"icon-clear"});
	//账户性质
	comboboxFuncByCondFilter(menuid,"oldAccountType02", "ACCTNATURE", "code", "name");
	$("#oldAccountType02").combobox("addClearBtn", {iconCls:"icon-clear"});
	$('#bdgagency1').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'bdgagencyHidden1',
		prompt: "请选择预算单位",
		multiSelect: false, //单选树
		queryParams : {
			menuid : menuid,
			customSql : allbdgagency
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		dblClickRow: true,
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "BDGAGENCY",
		filters:{
			code: "单位编码",
			name: "单位名称"
		}
	});
	
	
}

/**
 * 账户注销-查询（已提交注销）
 */
function queryHasRevokeInfo() {
	
	
	var param = {
			bdgagencycode : $("#bdgagency1").treeDialog('getValue'),
			accountType : $("#oldAccountTypeSearch").combobox('getValue'),
			processedStatus : $("#processedStatus").combobox("getValues").join(","),
			status : $("#dealStatus").combobox('getValue'),
			type02 : $("#oldAccountType02").textbox('getValue'),
			starttime : $('#starttime').datetimebox('getValue'),
			endtime :  $('#endtime').datetimebox('getValue'),
			accountNumber : $("#oldAccountNumber").textbox('getValue'),
			accountName : $("#oldAccountName").textbox('getValue'),
			menuid : $("#menuid").val(),
			activityId : $("#activityId").val(),
			type:$("#type").val(),
			firstNode : firstNode,
			lastNode : lastNode
			
		};
	$("#datagrid_hasRevoke").datagrid("load",param);
	
}

/**
 * 注销账户-详情
 */
function queryRevokeInfo() {
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据");
		return;
	}
	
	var wfid =rows[0].wfid;
	var cs ="?wfid="+wfid+"&activityId="+activityId+"&type="+type+"&optType="+types.view;
	
	showModalDialog(585, "revoke_audit", types.view, datagridHasRevoke, "账户注销-详情",
			"manage/revoke/controller/AccountRevokeController/revokeAuditForm.do"+cs, urls.operateRevokeInfo,true);
};

/**
 * 注销账户-审批
 */
function operateRevokeInfo() {
	
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据");
		return;
	}
	var wfid =rows[0].wfid;
	var cs ="?wfid="+wfid+"&activityId="+activityId+"&type="+type+"&optType="+types.edit;
	
	$.post(urls.getFormIsEdit, {
		wfid : wfid,
		activityId : activityId
	}, function(result) {
		if(result.body !=null){
			
			var formIsEdit =  result.body.formIsEdit;
			var isBackToFirstNode =  result.body.isBackToFirstNode;
			showModalDialog(585, "revoke_audit", types.edit, datagridHasRevoke, "账户注销-审批",
					"manage/revoke/controller/AccountRevokeController/revokeAuditForm.do"+cs, urls.operateRevokeInfo,true,formIsEdit,isBackToFirstNode);
		}
	}, "json");
	
	
};

/**
 * 批量审批同意、退回
 */
function batOperateRevokeInfo(flag){
	var selectRow = datagridHasRevoke.datagrid('getChecked');
	var isback="";
	var message="确认要将选中数据审批同意？";
	var confirm="审批同意确认";
	var text="同意";
	if(selectRow==null || selectRow.length==0){
		if(flag){
			easyui_warn("请选择要审批同意的数据！",null);
		}else{
			easyui_warn("请选择要退回的数据！",null);
		}
		return;
	}
	if(!flag){//退回
		isback=1;
		message="确认要将选中数据退回？";
		confirm="退回确认";
		text="退回";
	}
	var applicationIds = "";
	for (var int = 0; int < selectRow.length; int++) {
		applicationIds = applicationIds+ selectRow[int].applicationId;
		if(int!=selectRow.length-1){
			applicationIds =applicationIds+",";
		}
	}
	//var menuid = $("#menuid").val();
	//var activityId= $("#activityId").val();
	
	showAuditModalDialog(parent.$,datagridHasRevoke,text,menuid,activityId,applicationIds,isback);
	
}



/**
 * 撤回
 */

function removeRevokeInfo(){
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据");	
		return;
	}
	var applicationIds = "";
	
	for (var i=0; i<rows.length; i++) {
		if (i != 0){
			applicationIds = applicationIds +"," + rows[i].applicationId;
		}else{
			applicationIds = rows[i].applicationId;
		}
	}
	parent.$.messager.confirm("确认撤回", "是否确认撤回这"+rows.length+"条数据记录？", function(r) {
		if (r) {
			$.post(urls.removeRevokeInfo, {
				applicationIds : applicationIds,
				activityId : $("#activityId").val(),
				menuid : $("#menuid").val()
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagridHasRevoke.datagrid("reload");
				});
			}, "json");
		}
	});
};


/**
 * 弹出模式窗口
 * 
 * @param operType
 *            操作类型，主要包括查看，添加和修改
 * @param dataGrid
 *            操作的数据表格
 * @param title
 *            弹出窗的标题
 * @param href
 *            弹出窗的href
 * @param url
 *            对应操作的url
 * @param fill
 *            是否自动填充表单，主要是查看和修改
 */
function showModalDialog(height, form, operType, dataGrid, title, href, url,
		fill,formIsEdit,isBackToFirstNode) {
	if (fill) {
		var rows = dataGrid.datagrid("getSelections");
		if (rows.length != 1) {
			easyui_warn("请选择一条数据！");
			return;
		}
	}
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 850,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			if (fill) {
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				if(operType=="edit"){//编辑或新增
					
					comboboxFuncByCondFilter(menuid,"deptNature", "AGENCYTYPE", "code", "name", mdDialog); //单位性质
					//comboboxFuncByCondFilter(menuid,"accountType", "ACCTTYPE", "code", "name", mdDialog);//账户类型
					//comboboxFuncByCondFilter(menuid,"type02", "ACCTNATURE", "code", "name", mdDialog);//账户性质
					comboboxFuncByCondFilter(menuid,"changeType", "REVOKETYPE", "code", "name", mdDialog);//注销事项
					
					//添加数据
					mdDialog.find("#phonenumber").searchbox("setValue",row.applPhonenumber);
					mdDialog.find("#accountName").searchbox("setValue",row.oldAccountName);
					mdDialog.find("#bank").searchbox("setValue",row.oldBankNameCn);
					mdDialog.find("#accountNumber").searchbox("setValue",row.oldAccountNumber);
					mdDialog.find("#type02").val(row.oldType02);
					mdDialog.find("#accountType").val(row.oldAccountType);
					mdDialog.find("#iszero").searchbox("setValue",row.oldIszero);
					mdDialog.find("#accountContent").searchbox("setValue",row.oldAccountContent);
					mdDialog.find("#legalPerson").searchbox("setValue",row.oldLegalPerson);
					mdDialog.find("#idcardno").searchbox("setValue",row.oldIdcardno);
					mdDialog.find("#financialOfficer").searchbox("setValue",row.oldFinancialOfficer);
					
					mdDialog.find("#type02Name").textbox("setValue",row.oldType02Name);
					mdDialog.find("#accountTypeName").textbox("setValue",row.oldAccountTypeName);
					//是否零余额账户
					mdDialog.find("#iszero").combobox({ 
						valueField : "value",
						textField : "text",
						panelHeight : "auto",
						data : [{value : 1, text : "是"}, {value : 0, text : "否"}]
					});
					
					//是否可编辑
					
					if(formIsEdit==true){
						showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
					}else{
						showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
					}
					
				}else{//详情
					showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
					comboboxFuncByCondFilter(menuid,"changeType", "REVOKETYPE", "code", "name", mdDialog);//注销事项
					if(row.oldIszero=="0"){
						mdDialog.find("#iszero").textbox("setValue","否");
					}else{
						mdDialog.find("#iszero").textbox("setValue","是");
					}
				}
				
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form,formIsEdit,isBackToFirstNode)
		,onBeforeClose:function(){
			//添加窗口关闭事件，将全局变量置为“”
			parent.$.modalDialog.handler.find("#itemids").val("");
		}
	});
	
};
/**
 * 根据操作类型来获取操作按钮
 * 
 * @param operType
 *            操作类型
 * @param url
 *            对应的操作URL
 * @returns {___anonymous3684_3690}
 */
function funcOperButtons(operType, url, dataGrid, form,formIsEdit,isBackToFirstNode) {
	//var formIsEdit = parent.$.modalDialog.handler.find("#formIsEdit").val();
	var buttons;
	if (operType == types.view) {
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == types.edit && formIsEdit==true ) {
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#wfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId,form,1,wfid,activityId,applicationId,"");
				//submitForm(url+"?isback=&activityId="+activityId, form,operType,"确认同意该数据？");
			}
		}, {
			text : "退回",
			iconCls : "icon-undo",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#wfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId,form,2,wfid,activityId,applicationId,"");
				//submitForm(url+"?isback=1&activityId="+activityId, form,operType,"确认退回该数据？");
			}
		}, {
			text : "短信服务",
			iconCls : "icon-message",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var savephonenumbers = parent.$.modalDialog.handler.find("#savephonenumbers").val();
				var savemessage = parent.$.modalDialog.handler.find("#savemessage").val();
				var phonenumber = dataGrid.datagrid("getSelections")[0].applPhonenumber;
				//如果在临时保存数据里没有值，则取业务数据里的手机号，都则就取临时数据里的手机号
				if(savephonenumbers==null || savephonenumbers==""){
					showMessageModalDialog(parent.$('#messageService'),phonenumber,"",activityId);
				}else{
					showMessageModalDialog(parent.$('#messageService'),savephonenumbers,savemessage,activityId);
				}
			}
		},{
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.find("#itemids").val("");
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
		if(isBackToFirstNode==true){
			//如果可以退回到首节点
			buttons.splice(2, 0,  {
					text : "退回首节点",
					iconCls : "icon-undo",
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;
						var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
						if (!isValid) {
							return;
						}
						var wfid = parent.$.modalDialog.handler.find("#wfid").val();
						var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
						showOperationDialog(parent.$('#operationdiv'),url+"?isback=2&activityId="+activityId,form,3,wfid,activityId,applicationId,"");
						//submitForm(url+"?isback=2&activityId="+activityId, form,operType,"确认将该数据退回至首节点？");
					}
				});
		}
	}else{
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#wfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId,form,1,wfid,activityId,applicationId,"");
				
				//submitForm(url+"?isback=&activityId="+activityId, form,operType,"确认同意该数据？");
			}
		}, {
			text : "退回",
			iconCls : "icon-undo",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#wfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId,form,2,wfid,activityId,applicationId,"");
				//submitForm(url+"?isback=1&activityId="+activityId, form,operType,"确认退回该数据？");
			}
		}, {
			text : "短信服务",
			iconCls : "icon-message",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var savephonenumbers = parent.$.modalDialog.handler.find("#savephonenumbers").val();
				var savemessage = parent.$.modalDialog.handler.find("#savemessage").val();
				var phonenumber = dataGrid.datagrid("getSelections")[0].applPhonenumber;
				//如果在临时保存数据里没有值，则取业务数据里的手机号，都则就取临时数据里的手机号
				if(savephonenumbers==null || savephonenumbers==""){
					showMessageModalDialog(parent.$('#messageService'),phonenumber,"",activityId);
				}else{
					showMessageModalDialog(parent.$('#messageService'),savephonenumbers,savemessage,activityId);
				}
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.find("#itemids").val("");
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
		if(isBackToFirstNode==true){
			//如果可以退回到首节点
			buttons.splice(2, 0,  {
					text : "退回首节点",
					iconCls : "icon-undo",
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;
						var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
						if (!isValid) {
							return;
						}
						var wfid = parent.$.modalDialog.handler.find("#wfid").val();
						var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
						showOperationDialog(parent.$('#operationdiv'),url+"?isback=2&activityId="+activityId,form,3,wfid,activityId,applicationId,"");
						//submitForm(url+"?isback=2&activityId="+activityId, form,operType,"确认将该数据退回至首节点？");
					}
				});
		}
	}
	return buttons;
	
};
/**
 * 提交表单
 * 
 * @param url
 *            表单url
 */
function submitForm(url, form,operType,mesg) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	var phonenumber = parent.$.modalDialog.handler.find("#savephonenumbers").val();
	var message = parent.$.modalDialog.handler.find("#savemessage").val();
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
	parent.$.messager.confirm("确认操作", mesg, function(r) {
		if(r){
		form.form("submit",
			{
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
						
						//重新加载gride
						parent.$.modalDialog.handler.dialog('close');
						parent.$.modalDialog.openner_dataGrid.datagrid('reload');
						datagridHasRevoke.datagrid('reload');
						
					} else {
						parent.$.messager.alert('错误', result.title, 'error');
					}
				}
			});
		}
	});
};

//查询工作流信息
function workflowMessage(){
	var selectRow = datagridHasRevoke.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
//点击左边菜单刷新右边
function showReload() {
	datagridHasRevoke.datagrid('reload');
}

/**
 * 注销申请表
 */
function outRegisterApplicationform(){
	var selectRow = datagridHasRevoke.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	
	var applicationIds = "";
	for(var i=0;i<selectRow.length;i++){
		if(i==selectRow.length-1){
			applicationIds+=selectRow[i].applicationId;
		}else{
			
			applicationIds+=selectRow[i].applicationId+",";
		}
	}
	window.location.href=urls.outRegisterAppicationForm+"?applicationIds="+applicationIds+"&type=3";
}