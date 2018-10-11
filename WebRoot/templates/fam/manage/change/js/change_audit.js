
var baseUrl = contextpath + "manage/change/AccountChangeController/";
var urls = {
		accountDataGrid : baseUrl+"queryAllAccount.do",
		auditEntryForm : baseUrl+"auditEntryForm.do",
		changeAccountApprove : baseUrl+"changeAccountApprove.do",
		detailFormEntry : baseUrl + "detailFormEntry.do", //详情FORM表单界面
		verifyWorkFlow :baseUrl+"verifyWorkFlow.do",
		getFormIsEdit :baseUrl+"getFormIsEdit.do",
		revokeWFUrl : baseUrl+"revokeWorkFlow.do",
		//导出变更申请表申请表
		outRegisterAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRegisterAppicationForm.do"
};
//默认加载
$(function() {
	var pstatus =spdclData;
	if(lastNode=='true'){
		pstatus =lastNodeSpdclData;
	}
	//工作流状态初始化
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [{text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			doQuery();
			switch (record.value) {
			case '1':
				$('#spBtn').linkbutton('enable');
				$('#spTyBtn').linkbutton('enable');
				$('#uodoBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
				$('#spBtn').linkbutton('disable');
				$('#spTyBtn').linkbutton('disable');
				$('#uodoBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				break;
			default:
				break;
			}
		},
		onHidePanel: function(){  
	        var status = $('#status').combobox('getValue');
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
			doQuery();
			$('#backBtn').linkbutton('disable');
			if(record.value =='21'){
				$('#backBtn').linkbutton('enable');
			}else if(record.value =='22'){
				$('#backBtn').linkbutton('disable');
			}
		}
	});
	$('#spBtn').linkbutton('enable');
	$('#spTyBtn').linkbutton('enable');
	$('#uodoBtn').linkbutton('enable');
	$('#backBtn').linkbutton('disable');
	//加载grid
	loadChangeGrid(urls.accountDataGrid);
	//top
	$('#topBdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'topbdgagencyHidden',
		prompt: "请选择预算单位",
		editable :false,
		multiSelect: false, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid,
			customSql : allbdgagency
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "BDGAGENCY",
		filters:{
			code: "单位编码",
			name: "单位名称"
		}
	});
	comboboxFuncByCondFilter(menuid,"topAccountType", "ACCTTYPE", "code", "name");//账户类型
	comboboxFuncByCondFilter(menuid,"topAccountType02", "ACCTNATURE", "code", "name");//账户性质
	$("#topAccountType").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountType02").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountNum").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountName").combobox("addClearBtn", {iconCls:"icon-clear"});
});
//页面刷新
function showReload() {
	accountDataGrid.datagrid('reload'); 
}
//可审批账户grid
var accountDataGrid;
//加载已变更grid列表
function loadChangeGrid(url) {
	accountDataGrid = $("#accountDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		queryParams: {
			status : 1,
			menuid : menuid,
			activityId : activityId,
			processedStatus : "",
			type : type,
			firstNode : firstNode,
			lastNode : lastNode
		},
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[{field : "applicationId",checkbox : true,align:'center', rowspan:2} 
					,{field : "bdgagencycode",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true	,align:'center', rowspan:2	}
					,{field : "bdgagencyname",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true,align:'center', rowspan:2		}
					,{field : "bdgagencycn",title : "预算单位",halign : 'center',width : 180,sortable:true,align:'left', rowspan:2	}
					,{field : "deptNature",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "deptNatureName",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	}
					,{field : "applyReason",title : "变更原因",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	}
					,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	}
                    ,{title:'原账户信息',colspan:6}
                    ,{title:'新账户信息',colspan:14}
                    ,{field : "deptAddress",title : "单位地址",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2	}
  	                ,{field : "phonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "remark",title : "备注",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "createUserName",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "createTime",title : "创建时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "updateUserName",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "updateTime",title : "修改时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "accountId",title : "账户id",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true	}
                ],[{field : "oldAccountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
                  ,{field : "oldAccountType",title : "账户类型",halign : 'center',	width:100,sortable:true ,hidden:true}
	              ,{field : "oldType02",title : "账户性质代码",halign : 'center',	width:150,sortable:true,hidden:true}
	              ,{field : "oldType02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
	              ,{field : "oldAccountName",title : "账户名称",halign : 'center',	width:150	,sortable:true}
	              ,{field : "oldAccountNumber",title : "银行账号",halign : 'center',	width:150,sortable:true	}
	              ,{field : "oldBankCode",title : "开户行code",halign : 'center',	width:150,sortable:true,hidden:true}
	              ,{field : "oldBankName",title : "开户行name",halign : 'center',	width:150,sortable:true	,hidden:true}
	              ,{field : "oldBankNameCn",title : "账户开户行",halign : 'center',	width:150,sortable:true	}
	              ,{field : "oldIszero",title : "零余额账户",halign : 'center',	width:80,sortable:true,formatter: function(value){return iszero[value];}}
	              
	              ,{field : "accountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
	              ,{field : "accountTypeCode",title : "账户类型",halign : 'center',	width:100,sortable:true,hidden:true }
	              ,{field : "type02",title : "账户性质",halign : 'center',	width:150,sortable:true,hidden:true}
	              ,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
	              ,{field : "accountName",title : "账户名称",halign : 'center',	width:150	,sortable:true}
	              ,{field : "accountNumber",title : "银行账号",halign : 'center',	width:150,sortable:true	}
	              ,{field : "bankCode",title : "开户行code",halign : 'center',	width:150,sortable:true,hidden:true		}
	              ,{field : "bankName",title : "开户行name",halign : 'center',	width:150,sortable:true,hidden:true		}
	              ,{field : "bankNameCn",title : "账户开户行",halign : 'center',	width:150,sortable:true	}
	              ,{field : "iszero",title : "零余额账户",halign : 'center',	width:80,sortable:true,formatter: function(value){return iszero[value];}}
	              ,{field : "wfid",title : "wfid",halign : 'center',	width:150,sortable:true,hidden:true		}
	              ]]
	
	});
}
/**
 * 新增、修改弹出窗口
 * z
 */
function showModalDialog(title,height,width, formid, operType, dataGrid, href, url,fill,formIsEdit,isBackToFirstNode) {
	var icon = 'icon-' + operType;
	var messageIsUse ;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : width,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var formIsEdit = parent.$.modalDialog.handler.find('#formIsEdit').val();
			messageIsUse =  parent.$.modalDialog.handler.find('#messageIsUse').val();
			if(fill){
				var row = dataGrid.datagrid("getSelections")[0];
				mdDialog.find("#menuid").val(menuid);
				mdDialog.find("#activityId").val(activityId);
				if(formIsEdit){
					mdDialog.find("#bank").treeDialog({
						title :'选择开户行',
						dialogWidth : 420,
						dialogHeight : 500,
						hiddenName:'bankCode',
						prompt: "请选择开户行",
						editable :false,
						multiSelect: false, //单选树
						dblClickRow: true,
						queryParams : {
							menuid : menuid
						},
						url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
						checkLevs: [1,2], //只选择3级节点
						elementcode : "BANK",
						filters:{
							code: "银行编码",
							name: "银行名称"
						}
					});
				}
				var f = parent.$.modalDialog.handler.find('#' + formid);
				f.form("load", row);
				var changetype = row.changetype;
				if(changetype == 3){//银行账号
					mdDialog.find("#accountType").searchbox("setValue", row.oldAccountType);
					mdDialog.find("#type02").searchbox("setValue", row.oldType02);
					mdDialog.find("#accountName").textbox("setValue", row.oldAccountName);
					mdDialog.find("#bank").textbox("setValue", row.oldBankName);
					mdDialog.find("#financialOfficer").textbox("setValue", row.oldFinancialOfficer);
					mdDialog.find("#legalPerson").textbox("setValue", row.oldLegalPerson);
					mdDialog.find("#idcardno").textbox("setValue", row.oldIdcardno);
					mdDialog.find("#accountContent").textbox("setValue", row.oldAccountContent);
					mdDialog.find("#bankCode").val(row.oldBankCode);
				}else if(changetype == 4){//开户行
					//填充值
					mdDialog.find("#accountType").searchbox("setValue", row.oldAccountType);
					mdDialog.find("#type02").searchbox("setValue", row.oldType02);
					mdDialog.find("#accountName").textbox("setValue", row.oldAccountName);
					mdDialog.find("#financialOfficer").textbox("setValue", row.oldFinancialOfficer);
					mdDialog.find("#legalPerson").textbox("setValue", row.oldLegalPerson);
					mdDialog.find("#idcardno").textbox("setValue", row.oldIdcardno);
					mdDialog.find("#accountContent").textbox("setValue", row.oldAccountContent);
				}
			}
			if(formIsEdit=='true'){
				//附件初始化
				comboboxFuncByCondFilter(menuid,"deptNature", "AGENCYTYPE", "code", "name", mdDialog);//单位性质
				comboboxFuncByCondFilter(menuid,"type02", "ACCTNATURE", "code", "name", mdDialog);//账户性质
				comboboxFuncByCondFilter(menuid,"accountType", "ACCTTYPE", "code", "name", mdDialog);//账户类型
				comboboxFuncByCondFilter(menuid,"changeType", "CHANGETYPE", "code", "name", mdDialog);//变更事项
				showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
			}else{
				showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
			}
			//禁用组件
			if(changetype == 3){
				mdDialog.find("#accountType").searchbox("readonly");
				mdDialog.find("#type02").searchbox("readonly");
				mdDialog.find("#accountName").textbox("readonly");
				mdDialog.find("#financialOfficer").textbox("readonly");
				mdDialog.find("#legalPerson").textbox("readonly");
				mdDialog.find("#idcardno").textbox("readonly");
				mdDialog.find("#accountContent").textbox("readonly");
				mdDialog.find("#bank").searchbox("readonly");
			}else if(changetype == 4){
				mdDialog.find("#accountType").searchbox("readonly");
				mdDialog.find("#type02").searchbox("readonly");
				mdDialog.find("#accountName").textbox("readonly");
				mdDialog.find("#financialOfficer").textbox("readonly");
				mdDialog.find("#legalPerson").textbox("readonly");
				mdDialog.find("#idcardno").textbox("readonly");
				mdDialog.find("#accountContent").textbox("readonly");
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid,formid,messageIsUse,formIsEdit,isBackToFirstNode)
	});
}
function funcOperButtons(operType, url, dataGrid,form,messageIsUse,formIsEdit,isBackToFirstNode) {
	var buttons;
	if(supply == 'true'){//单位信息补充
		buttons = [ {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				parent.$.messager.confirm("送审确认", "确定要送审账户变更信息？", function(r) {
					if (r) {
						submitFormDialog(url+"?isback=&activityId="+activityId+"&menuid="+menuid, form, 1, "")
					}
				});
				/*var wfid = parent.$.modalDialog.handler.find("#wfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId,form,1,wfid,activityId,applicationId,"");*/
			}
		},{
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
			}
		},{
			text : "原账户信息",
			iconCls : "icon-view",
			handler : function() {
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				if(applicationId ==""){
					showAccountDetail(parent.$('#accountDetail'),"","");
				}else{
					showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
				}
			}
		},{
			text : "消息服务",
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
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else{
		if(formIsEdit){//formIsEdit
			if(isBackToFirstNode){
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
					}
				},{
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
						showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId,form,2,wfid,activityId,applicationId,"");}
				},{
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
					}
				},{
					text : "附件管理",
					iconCls : "icon-files",
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;
						showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
					}
				},{
					text : "原账户信息",
					iconCls : "icon-view",
					handler : function() {
						var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
						if(applicationId ==""){
							showAccountDetail(parent.$('#accountDetail'),"","");
						}else{
							showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
						}
					}
				},{
					text : "消息服务",
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
						parent.$.modalDialog.handler.dialog('close');
					}
				} ];
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
					}
				},{
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
						showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId,form,2,wfid,activityId,applicationId,"");}
				},{
					text : "附件管理",
					iconCls : "icon-files",
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;
						showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
					}
				},{
					text : "原账户信息",
					iconCls : "icon-view",
					handler : function() {
						var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
						if(applicationId ==""){
							showAccountDetail(parent.$('#accountDetail'),"","");
						}else{
							showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
						}
					}
				},{
					text : "消息服务",
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
						parent.$.modalDialog.handler.dialog('close');
					}
				} ];
			}
		}else{//
			if(isBackToFirstNode){
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
					}
				},{
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
						showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId,form,2,wfid,activityId,applicationId,"");}
				},{
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
					}
				},{
					text : "原账户信息",
					iconCls : "icon-view",
					handler : function() {
						var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
						if(applicationId ==""){
							showAccountDetail(parent.$('#accountDetail'),"","");
						}else{
							showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
						}
					}
				}, {
					text : "消息服务",
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
						parent.$.modalDialog.handler.dialog('close');
					}
				} ];
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
					}
				},{
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
						showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId,form,2,wfid,activityId,applicationId,"");}
				},{
					text : "原账户信息",
					iconCls : "icon-view",
					handler : function() {
						var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
						if(applicationId ==""){
							showAccountDetail(parent.$('#accountDetail'),"","");
						}else{
							showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
						}
					}
				}, {
					text : "消息服务",
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
						parent.$.modalDialog.handler.dialog('close');
					}
				} ];
			}
		}
	}
	return buttons;
}
//填写新账户信息时，进行必填校验
function subValidate(url, form,isback,message){
	var accountName = parent.$.modalDialog.handler.find("#accountName").val();
	var accountNumber = parent.$.modalDialog.handler.find("#accountNumber").val();
	if(accountName=="" && accountNumber==""){
		submitForm(url, form,isback,message);
	}else{
		
		if(parent.$.modalDialog.handler.find("#accountName").val()==""){
			easyui_warn("请填写账户名称！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#accountNumber").val()==""){
			easyui_warn("请填写银行账号！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		var re = /^[0-9]*$/;
		if(!re.test(parent.$.modalDialog.handler.find("#accountNumber").val())){
			easyui_warn("银行账号只能填写数字！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#bank").val()==""){
			easyui_warn("请填写开户行！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#accountNum").val()==""){
			easyui_warn("请填写开户号！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#type02").textbox("getValue")==""){
			easyui_warn("请填写账户性质！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#accountType").textbox("getValue")==""){
			easyui_warn("请填写账户类型！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#openTime").datebox("getValue")==""){
			easyui_warn("请填写开户日期！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#responsiblePerson").val()==""){
			easyui_warn("请填写经办人！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#acctPhonenumber").val()==""){
			easyui_warn("请填写联系手机！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		if(parent.$.modalDialog.handler.find("#accountContent").val()==""){
			easyui_warn("请填写核算内容！");
			parent.$.modalDialog.handler.find("#tt").tabs("select","变更后账户账号");
			return;
		}
		submitForm(url, form,isback,message);
	}
}
function submitForm(url, form,isback,message) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#isback").val(isback);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	parent.$.messager.confirm("确认操作", message, function(r) {//异步
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
						accountDataGrid.datagrid('reload');
						parent.$.modalDialog.handler.dialog('close');
					} else {
						parent.$.messager.alert('错误', result.title, 'error');
					}
				}
			});
		}
	});
}
//批量撤回
function revokeWF(){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请选择要撤回的数据！",null);
		return;
	}
	var applicationIds = "";
	for (var int = 0; int < selectRow.length; int++) {
		applicationIds = applicationIds+ selectRow[int].applicationId;
		if(int!=selectRow.length-1){
			applicationIds =applicationIds+",";
		}
	}
	parent.$.messager.confirm("确认撤回", "确认要将选中数据撤回？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				applicationId : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					accountDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
//退回
function revokeWF(){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请选择要退回的数据！",null);
		return;
	}
	var applicationIds = "";
	for (var int = 0; int < selectRow.length; int++) {
		applicationIds = applicationIds+ selectRow[int].applicationId;
		if(int!=selectRow.length-1){
			applicationIds =applicationIds+",";
		}
	}
	parent.$.messager.confirm("退回确认", "确认要将选中数据退回？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId : activityId,
				applicationId : applicationIds,
				isback : 1
			}, function(result) {
				easyui_auto_notice(result, function() {
					accountDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
//查询
function doQuery(){
	var arrayS = $("#processedStatus").combobox("getValues");
	var v ="";
	for (var int = 0; int < arrayS.length; int++) {
		v += arrayS[int];
		if(int != arrayS.length-1){
			v += ",";	
		}
	}
	var param = {
			bdgagencycode : $("#topbdgagencyHidden").val(),
			accountType : $("#topAccountType").combobox('getValue'),
			type02 : $("#topAccountType02").combobox('getValue'),
			accountNumber : $("#topAccountNumber").val(),
			accountName : $("#topAccountName").val(),
			starttime : $('#starttime').datetimebox('getValue'),
			endtime :  $('#endtime').datetimebox('getValue'),
			status : $("#status").combobox("getValue"),
			menuid : menuid,
			activityId : activityId,
			processedStatus : v,
			type:type,
			firstNode : firstNode,
			lastNode : lastNode
		};
	accountDataGrid.datagrid("load", param);
}
//审批
function pushWF(){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条要审批的数据！",null);
		return;
	}
	var wfid =selectRow[0].wfid;
	var cs ="?wfid="+wfid+"&activityId="+activityId+"&type="+type;
	$.post(urls.getFormIsEdit, {
		wfid : wfid,
		activityId : activityId
	}, function(result) {
		if(result.body !=null){
			var formIsEdit =  result.body.formIsEdit;
			var isBackToFirstNode =  result.body.isBackToFirstNode;
			var title = "账户变更审批";
			if(supply == 'true'){
				title = "变更信息录入";
			}
			showModalDialog(title,620,900,"accountChangeForm","edit",accountDataGrid,urls.auditEntryForm+cs,urls.verifyWorkFlow,true,formIsEdit,isBackToFirstNode);
		}
	}, "json");
}
//批量审批或退回
function pushAgreeWF(flag){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		if(flag){
			easyui_warn("请选择要审批的数据！",null);
		}else{
			easyui_warn("请选择要退回的数据！",null);
		}
		return;
	}
	var isback="";
	var message="确认要将选中数据审批同意？";
	var confirm="审批同意确认";
	var text="同意";
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
	showAuditModalDialog(parent.$,accountDataGrid,text,menuid,activityId,applicationIds,isback);
}
function workflowMessage(){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
//详情
function accountView(){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var wfid =selectRow[0].wfid;
	var cs ="?wfid="+wfid;
	showModalDialogForDetail("账户变更详情",600,900,"accountChangeForm","view",accountDataGrid,urls.detailFormEntry+cs,true);
}
function showModalDialogForDetail(title,height,width, formid, operType, dataGrid, href, fill) {
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : width,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			if(fill){
				var row = dataGrid.datagrid("getSelections")[0];
				mdDialog.find("#menuid").val($("#menuid").val());
				var f = parent.$.modalDialog.handler.find('#' + formid);
				f.form("load", row);
				showFileTable(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
			}
		},
		buttons : [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
/**
 * 变更申请表
 */
function outRegisterApplicationform(){
	var selectRow = accountDataGrid.datagrid('getChecked');
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
	window.location.href=urls.outRegisterAppicationForm+"?applicationIds="+applicationIds+"&type=2";
}