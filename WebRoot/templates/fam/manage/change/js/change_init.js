
var baseUrl = contextpath + "manage/change/AccountChangeController/";
var urls = {
	accountDataGridUrl : baseUrl + "queryAccount.do",
	changeAccountDataGridUrl : baseUrl + "queryAllAccount.do",
	changeAccountAddInit : baseUrl + "changeAccountAddInit.do",
	changeAccountAdd : baseUrl + "changeAccountSaveAdd.do",
	changeAccountTypeSelect : baseUrl + "changeAccountTypeSelect.do",
	changeAccountEdit : baseUrl + "changeAccountSaveEdit.do",
	changeAccountDelete : baseUrl + "changeAccountDelete.do",
	detailFormEntry : baseUrl + "detailFormEntry.do", //详情FORM表单界面
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	//导出变更申请表
	outRegisterAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRegisterAppicationForm.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#changeAccountDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	//工作流状态初始化
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [{text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
			case '1':
				$('#editBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
				$('#editBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
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
	        	data = sqdclData;
        	}else if(status == '2'){//已处理
	        	data = sqyclData;
        	}
	        $("#processedStatus").combobox("loadData",data);
		}
	});
	$("#processedStatus").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		value : "",
		data : sqdclData,
		onSelect : function(record) {
			topQuery();
			$('#backBtn').linkbutton('disable');
			if(record.value =='21'){
				$('#backBtn').linkbutton('enable');
			}else if(record.value =='22'){
				$('#backBtn').linkbutton('disable');
			}
		}
	});
	//默认加载
	$('#editBtn').linkbutton('enable');
	$('#delBtn').linkbutton('enable');
	$('#sendBtn').linkbutton('enable');
	$('#backBtn').linkbutton('disable');
	//加载grid
	loadChangeGrid(urls.changeAccountDataGridUrl);
	loadAccountGrid(urls.accountDataGridUrl);
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
			customSql :allbdgagency
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
	$("#topAccountNumber").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountName").combobox("addClearBtn", {iconCls:"icon-clear"});
	//down
	$('#downBdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'downbdgagencyHidden',
		prompt: "请选择预算单位",
		editable :false,
		multiSelect: false, //单选树
		dblClickRow: true,
		checkLevs: [1,2,3], //只选择3级节点
		queryParams : {
			menuid : menuid,
			customSql :allbdgagency
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		elementcode : "BDGAGENCY",
		filters:{
			code: "单位编码",
			name: "单位名称"
		}
	});
	comboboxFuncByCondFilter(menuid,"downAccountType", "ACCTTYPE", "code", "name");//账户类型
	comboboxFuncByCondFilter(menuid,"downAccountType02", "ACCTNATURE", "code", "name");//账户性质
	$("#downAccountType").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#downAccountType02").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#downAccountNum").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#downAccountName").combobox("addClearBtn", {iconCls:"icon-clear"});
});
//页面刷新
function showReload() {
	accountDataGrid.datagrid('reload'); 
	changeAccountDataGrid.datagrid('reload');  
}
function topQuery(){
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
			firstNode : true,
			lastNode : false
		};
	changeAccountDataGrid.datagrid("load", param);
}
//可变更账户grid
var accountDataGrid;
//已变更账户grid
var changeAccountDataGrid;
//加载可变更账户grid列表
function loadAccountGrid(url) {
	accountDataGrid = $("#accountDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			menuid : menuid
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_account",
		showFooter : true,
		columns : [ [  {field : "itemid",checkbox : true}  
		              ,{field : "bdgagencycode",title : "预算单位code",halign : 'center',width:100,sortable:true,hidden:true	}
		              ,{field : "bdgagencyname",title : "单位名称",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "deptNature",title : "单位性质",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "bdgagencycn",title : "单位名称",halign : 'center',align:'left',	width:180,sortable:true	}
		              ,{field : "accountName",title : "账户名称",halign : 'center',	width:150	,sortable:true}
		              ,{field : "accountNumber",title : "银行账号",halign : 'center',	width:150,sortable:true	}
		              ,{field : "bankCode",title : "开户行code",halign : 'center',	width:150,sortable:true,hidden:true		}
		              ,{field : "bankName",title : "开户行name",halign : 'center',	width:150,sortable:true,hidden:true		}
		              ,{field : "bankNameCn",title : "开户行",halign : 'center',	width:150,sortable:true	}
		              ,{field : "deptNatureName",title : "单位性质",halign : 'center',	width:150,sortable:true	}
		              ,{field : "accountType",title : "账户类型",halign : 'center',	width:100,sortable:true,hidden:true }
		              ,{field : "accountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
		              ,{field : "type02",title : "账户性质",halign : 'center',	width:150,sortable:true,hidden:true}
		              ,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
		              ,{field : "legalPerson",title : "法定代表人",halign : 'center',	width:100,sortable:true	}
		              ,{field : "idcardno",title : "法人身份证号",halign : 'center',	width:100,sortable:true	}
		              ,{field : "financialOfficer",title : "财务负责人",halign : 'center',	width:100,sortable:true	}
		              ,{field : "accountContent",title : "核算内容",halign : 'center',	width:150,sortable:true	}
		              ,{field : "iszero",title : "零余额账户",halign : 'center',	width:80,sortable:true,formatter: function(value){return iszero[value];}}
		              ,{field : "status",title : "当前状态",halign : 'center',	width:80,sortable:true,formatter: function(value){return account_status[value];}	}
		              ,{field : "deptAddress",title : "单位地址",halign : 'center',	width:150,sortable:true	}
		              ,{field : "phonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true	}
		              ,{field : "remark",title : "备注",halign : 'center',	width:150,sortable:true	}
		             ] ]
	});
}
//加载已变更grid列表
function loadChangeGrid(url) {
	changeAccountDataGrid = $("#changeAccountDataGrid").datagrid({
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
			firstNode : true,
			lastNode : false
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[{field : "applicationId",checkbox : true,align:'center', rowspan:2} 
					,{field : "bdgagencycode",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true	,align:'center', rowspan:2	}
					,{field : "bdgagencyname",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true,align:'center', rowspan:2		}
					,{field : "bdgagencycn",title : "预算单位",halign : 'center',width : 180,sortable:true,align:'left', rowspan:2	}
					,{field : "deptNature",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "deptNatureName",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	}
					,{field : "changeTypeName",title : "变更事项",halign : 'center',	width:100,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "applyReason",title : "变更原因",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	}
					,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	}
					,{field : "changeType",title : "变更事项",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	}
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
	              ,{field : "createUser",title : "createUser",halign : 'center',	width:150,sortable:true,hidden:true		}
                ]]
	
	});
}
/**
 * 新增按钮
 */
function accountAdd(){
	var selectRow = accountDataGrid.datagrid('getSelected');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请在可变更账户信息中选择一条账户信息！",null);
		return;
	}
	if(accountbgmodel==2){
		showModalDialogForChangeType();
	}else{
		showModalDialog("账户变更",630,900,"accountChangeForm","add",accountDataGrid,urls.changeAccountAddInit,urls.changeAccountAdd,true);
	}
}
/**
 * 新增变更前先选择变更事项
 */
function showModalDialogForChangeType() {
	parent.$.modalDialog({
		title : "账户变更事项",
		iconCls : "icon-add",
		width : 400,
		height : 300,
		href : urls.changeAccountTypeSelect,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"changeType", "CHANGETYPE", "code", "name", mdDialog);//变更事项
		},
		buttons :[ {
			text : "确定",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var changeType = mdDialog.find("#changeType").combobox('getValue');
				if(changeType ==''){
					easyui_warn("请选择变更事项！",null);
					return;
				}else{
					globalChangeType = changeType;
					parent.$.modalDialog.handler.dialog('close');
					showModalDialog("账户变更",630,900,"accountChangeForm","add",accountDataGrid,urls.changeAccountAddInit+'?simple=1&changeType='+changeType,urls.changeAccountAdd,true);
				}
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				globalChangeType = '';
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
	});
}
/**
 * 修改按钮
 */
function accountEdit(){
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if(selectRow[0].createUser != userid){
		easyui_warn("无权限修改其他人录入的数据！");
		return;
	}
	if(accountbgmodel==2){
		var cs = "?simple=1&wfid="+selectRow[0].wfid+"&changeType="+selectRow[0].changetype+"&activityId="+activityId;
		showModalDialog("账户变更",630,900,"accountChangeForm","edit",changeAccountDataGrid,urls.changeAccountAddInit+cs,urls.changeAccountEdit,true);
	}else{
		showModalDialog("账户变更",630,900,"accountChangeForm","edit",changeAccountDataGrid,urls.changeAccountAddInit,urls.changeAccountEdit,true);
	}	
}
/**
 * 新增、修改弹出窗口
 * z
 */
function showModalDialog(title,height,width, formid, operType, dataGrid, href, url,fill) {
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : width,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
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
			if(fill){
				var row = dataGrid.datagrid("getSelections")[0];
				mdDialog.find("#menuid").val($("#menuid").val());
				if(operType=='add'){
					//隐藏属性赋值需要用val
					mdDialog.find("#bdgagencycode").val(row.bdgagencycode);
					mdDialog.find("#bankCode").val(row.bankCode);
					mdDialog.find("#oldBankCode").val(row.bankCode);
					mdDialog.find("#itemid").val(row.itemid);
					mdDialog.find("#oldType02").val(row.type02);
					mdDialog.find("#oldType02Name").val(row.type02Name);
					mdDialog.find("#oldAccountType").val(row.accountType);
					mdDialog.find("#oldAccountTypeName").val(row.accountTypeName);
					mdDialog.find("#oldAccountName").val(row.accountName);
					mdDialog.find("#oldAccountNumber").val(row.accountNumber);
					mdDialog.find("#oldBankName").val(row.bankName);
					mdDialog.find("#oldFinancialOfficer").val(row.financialOfficer);
					mdDialog.find("#oldLegalPerson").val(row.legalPerson);
					mdDialog.find("#oldIdcardno").val(row.idcardno);
					mdDialog.find("#oldAccountContent").val(row.accountContent);
					mdDialog.find("#oldIszero").val(row.iszero);
					//
					mdDialog.find("#bdgagencyname").textbox("setValue", row.bdgagencyname);
					mdDialog.find("#deptNature").searchbox("setValue", row.deptNature);
					mdDialog.find("#linkman").textbox("setValue", row.linkman);
					mdDialog.find("#applPhonenumber").textbox("setValue", row.phonenumber);
					mdDialog.find("#deptAddress").textbox("setValue", row.deptAddress);
					mdDialog.find("#changetype").searchbox("setValue", globalChangeType);
					var f = parent.$.modalDialog.handler.find('#' + formid);
					f.form("load", row);
				}else if(operType=='edit'){
					var f = parent.$.modalDialog.handler.find('#' + formid);
					f.form("load", row);
				}
				showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
			}
			comboboxFuncByCondFilter(menuid,"deptNature", "AGENCYTYPE", "code", "name", mdDialog);//单位性质
			comboboxFunc("iszero", "SYS_TRUE_FALSE", "code", "name", mdDialog);//零余额账户
			comboboxFuncByCondFilter(menuid,"type02", "ACCTNATURE", "code", "name", mdDialog);//账户性质
			comboboxFuncByCondFilter(menuid,"accountType", "ACCTTYPE", "code", "name", mdDialog);//账户类型
			comboboxFuncByCondFilter(menuid,"type02", "ACCTNATURE", "code", "name", mdDialog);//账户性质
			comboboxFuncByCondFilter(menuid,"accountType", "ACCTTYPE", "code", "name", mdDialog);//账户类型
			comboboxFuncByCondFilter(menuid,"changeType", "CHANGETYPE", "code", "name", mdDialog);//变更事项
			if(operType == 'add' && accountbgmodel ==2){
				if(globalChangeType==1){//账户名称
					mdDialog.find("#accountName").textbox("setValue", '');
					//其他项设为只读
					mdDialog.find("#legalPerson").textbox("readonly");
					mdDialog.find("#idcardno").textbox("readonly");
					mdDialog.find("#accountType").searchbox("readonly");
					mdDialog.find("#type02").searchbox("readonly");
					mdDialog.find("#accountNumber").textbox("readonly");
					mdDialog.find("#bank").searchbox("readonly");
				}else if(globalChangeType==2){//法定代表人
					mdDialog.find("#legalPerson").textbox("setValue", '');
					mdDialog.find("#idcardno").textbox("setValue", '');
					mdDialog.find("#accountName").textbox("readonly");
					mdDialog.find("#accountType").searchbox("readonly");
					mdDialog.find("#type02").searchbox("readonly");
					mdDialog.find("#accountNumber").textbox("readonly");
					mdDialog.find("#bank").searchbox("readonly");
				}else if(globalChangeType==5){//账户类型
					mdDialog.find("#accountType").searchbox("setValue", '');
					mdDialog.find("#legalPerson").textbox("readonly");
					mdDialog.find("#idcardno").textbox("readonly");
					mdDialog.find("#accountName").textbox("readonly");
					mdDialog.find("#type02").searchbox("readonly");
					mdDialog.find("#accountNumber").textbox("readonly");
					mdDialog.find("#bank").searchbox("readonly");
				}else if(globalChangeType==6){//账户性质
					mdDialog.find("#type02").searchbox("setValue", '');
					mdDialog.find("#legalPerson").textbox("readonly");
					mdDialog.find("#idcardno").textbox("readonly");
					mdDialog.find("#accountName").textbox("readonly");
					mdDialog.find("#accountType").searchbox("readonly");
					mdDialog.find("#accountNumber").textbox("readonly");
					mdDialog.find("#bank").searchbox("readonly");
				}
			}else{
				mdDialog.find("#changetype").searchbox("readonly",false);
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid,formid)
	});
}
function funcOperButtons(operType, url, dataGrid,formid) {
	var buttons;
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				//subValidate(operType, url, dataGrid, formid,"");
				submitForm(url, formid,"");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
//				subValidate(operType, url, dataGrid, formid,"1");
				submitForm(url, formid,"1");
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
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	return buttons;
}
//填写新账户信息时，进行必填校验
function subValidate(operType, url, dataGrid, formid,isSub){
	var accountName = parent.$.modalDialog.handler.find("#accountName").val();
	var accountNumber = parent.$.modalDialog.handler.find("#accountNumber").val();
	if(accountName=="" && accountNumber==""){
		submitForm(url, formid,isSub);
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
		submitForm(url, formid,isSub);
	}
}
function submitForm(url, form,workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	parent.$.modalDialog.handler.find("#activityId").val(activityId);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	if(workflowflag=="1"){
		parent.$.messager.confirm("送审确认", "确认要送审？", function(r) {
			if (r) {
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
									changeAccountDataGrid.datagrid('reload');
									parent.$.modalDialog.handler.dialog('close');
								} else {
									parent.$.messager.alert('错误', result.title, 'error');
								}
							}
						});
			}
		});
	}else{
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
							changeAccountDataGrid.datagrid('reload');
							parent.$.modalDialog.handler.dialog('close');
						} else {
							parent.$.messager.alert('错误', result.title, 'error');
						}
					}
				});
	
	}
}
/**
 * 账户变更删除
 */
function accountDelete(){
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请选择要删除的数据！",null);
		return;
	}
	var applicationIds = "";
	var canDelete = false;//是否可删除
	var selectRowsId ="";
	for (var int = 0; int < selectRow.length; int++) {
		if(selectRow[int].createUser != userid){
			if(!canDelete){
				canDelete = true;
			}
			selectRowsId = selectRowsId+""+(int+1)+","
		}else{
			applicationIds = applicationIds+ selectRow[int].applicationId;
			if(int!=selectRow.length-1){
				applicationIds =applicationIds+",";
			}
		}
	}
	var message="确认要将选中数据删除？";
	if(applicationIds==''){
		easyui_warn("无权限操作选中数据！",null);
		return;
	}
	if(canDelete){
		selectRowsId =selectRowsId.substring(0,selectRowsId.length>=1?selectRowsId.length-1:selectRowsId.length)
		message ="无权限操作选中第"+selectRowsId+"条数据，确定要将其他选中数据删除？";
	}
	parent.$.messager.confirm("确认删除", message, function(r) {
		if (r) {
			$.post(urls.changeAccountDelete, {
				applicationId : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					accountDataGrid.datagrid('reload');
					changeAccountDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
//可变更列表查询
function downGridQuery(){
	var param = {
			menuid : menuid,
			bdgagencycode : $("#downbdgagencyHidden").val(),
			accountType : $("#downAccountType").combobox('getValue'),
			type02 : $("#downAccountType02").combobox('getValue'),
			accountNumber : $("#downAccountNumber").val(),
			accountName : $("#downAccountName").val()
		};
		$("#accountDataGrid").datagrid("load", param);
}
//批量撤销
function revokeWF(){
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请选择要撤回的数据！",null);
		return;
	}
	var applicationIds = "";
	var canDelete = false;//是否可删除
	var selectRowsId ="";
	for (var int = 0; int < selectRow.length; int++) {
		if(selectRow[int].createUser != userid){
			if(!canDelete){
				canDelete = true;
			}
			selectRowsId = selectRowsId+""+(int+1)+","
		}else{
			applicationIds = applicationIds+ selectRow[int].applicationId;
			if(int!=selectRow.length-1){
				applicationIds =applicationIds+",";
			}
		}
	}
	var message="确认要将选中数据撤回？";
	if(applicationIds==''){
		easyui_warn("无权限操作选中数据！",null);
		return;
	}
	if(canDelete){
		selectRowsId =selectRowsId.substring(0,selectRowsId.length>=1?selectRowsId.length-1:selectRowsId.length)
		message ="无权限操作选中第"+selectRowsId+"条数据，确定要将其他选中数据撤回？";
	}
	parent.$.messager.confirm("确认撤回",message, function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				applicationId : applicationIds
			}, function(result) {
				changeAccountDataGrid.datagrid('reload');
				easyui_auto_notice(result, function() {
				});
			}, "json");
		}
	});
}
//批量送审
function sendWF(){
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请选择要送审的数据！",null);
		return;
	}
	var applicationIds = "";
	var canDelete = false;//是否可删除
	var selectRowsId ="";
	for (var int = 0; int < selectRow.length; int++) {
		if(selectRow[int].createUser != userid){
			if(!canDelete){
				canDelete = true;
			}
			selectRowsId = selectRowsId+""+(int+1)+","
		}else{
			applicationIds = applicationIds+ selectRow[int].applicationId;
			if(int!=selectRow.length-1){
				applicationIds =applicationIds+",";
			}
		}
	}
	var message="确认要将选中数据送审？";
	if(applicationIds==''){
		easyui_warn("无权限操作选中数据！",null);
		return;
	}
	if(canDelete){
		selectRowsId =selectRowsId.substring(0,selectRowsId.length>=1?selectRowsId.length-1:selectRowsId.length)
		message ="无权限操作选中第"+selectRowsId+"条数据，确定要将其他选中数据送审？";
	}
	parent.$.messager.confirm("送审确认", message, function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : $("#menuid").val(),
				activityId : activityId,
				applicationId : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					accountDataGrid.datagrid('reload');
					changeAccountDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
function workflowMessage(){
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
//详情
function accountView(){
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var wfid =selectRow[0].wfid;
	var cs ="?wfid="+wfid;
	if(accountbgmodel==2){
		cs += "&simple=1";
	}
	showModalDialogForDetail("账户变更详情",600,900,"accountChangeForm","view",changeAccountDataGrid,urls.detailFormEntry+cs,true);
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
				showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
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
	var selectRow = changeAccountDataGrid.datagrid('getChecked');
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
