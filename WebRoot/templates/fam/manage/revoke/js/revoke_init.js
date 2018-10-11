/**用户注销模块
 * revoke_init.js
 * */
var baseUrl = contextpath + "manage/revoke/controller/AccountRevokeController/";
//路径
var urls = {
	//noRevokelist : baseUrl + "queryNoRevoke.do",
	noRevokelist : contextpath + "manage/change/AccountChangeController/queryAccount.do",
	hasRevokelist : contextpath + "manage/change/AccountChangeController/queryAllAccount.do",
	addRevokeInfo : baseUrl + "addRevokeInfo.do",
	editRevokeInfo : baseUrl + "editRevokeInfo.do",
	deleteRevokeInfo : baseUrl + "deleteRevokeInfo.do",
	removeRevokeInfo : baseUrl+ "removeRevokeInfo.do",
	submitRevokeInfo : baseUrl+ "submitRevokeInfo.do",
	downloadFileUrl: contextpath + "base/filemanage/fileManageController/downLoadFile.do",
	deleteFileUrl: contextpath+"base/filemanage/fileManageController/delete.do",
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
var datagridNoRevoke;
var itemids ="";
$(function() {
	
	//加载已注销的用户表
	loaddatagridHasRevoke(urls.hasRevokelist);
	//加载可以注销的用户表
	loaddatagridNoRevoke(urls.noRevokelist);
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
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_submit').linkbutton('enable');
				$('#btn_flow').linkbutton('enable');
				break;
			case '2':
				$('#btn_query').linkbutton('enable');
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('disable');
				$('#btn_remove').linkbutton('disable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_submit').linkbutton('disable');
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
			queryHasRevokeInfo();
			$('#btn_revoke').linkbutton('disable');
			if(record.value =='21'){
				$('#btn_revoke').linkbutton('enable');
			}else if(record.value =='22'){
				$('#btn_revoke').linkbutton('disable');
			}
		}
	});
	//默认加载
	$('#btn_revoke').linkbutton('disable');
	
	//加载已注销的用户的查询条件
	loadHashRevokeSearch();
	//加载可以注销的用户的查询条件
	loadNoRevokeSearch();
});

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
			firstNode : true,
			lastNode : false
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
		, {field : "changetype",title : "注销事项",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "type",title : "备案类型",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "deptNature",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true,hidden:true}
		, {field : "deptNatureName",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true}
		, {field : "supervisorDept",title : "主管部门",halign : 'center',fixed : true,	width : 190,sortable:true}
		 ,{field : "oldLegalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true	}
         ,{field : "oldIdcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true	}
         ,{field : "oldFinancialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
         ,{field : "oldAccountContent",title : "核算内容",halign : 'center',	width:120,sortable:true		}
		, {field : "accountNum",title : "户号",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "oldIszero",title : "零余额账户",halign : 'center',fixed : true,	width : 70,sortable:true,
			formatter: function(value){
				return iszero[value];
			}
		}
		, {field : "deptAddress",title : "单位地址",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "linkman",title : "联系人",halign : 'center',width : 120,sortable:true}
		, {field : "applPhonenumber",title : "联系人电话",halign : 'center',width : 120,sortable:true}
		, {field : "wfid",title : "工作流id",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "wfstatus",title : "工作流状态",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		//,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'center', rowspan:2	,hidden:true}
		, {field : "wfisback",title : "工作流是否退回",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "isopen",title : "是否开户 ",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "isregister",title : "备案",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		//, {field : "status",title : "状态",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "remark",title : "备注",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "createUserName",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "createUser",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "createTime",title : "创建时间",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "updateUserName",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "updateUser",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "updateTime",title : "修改时间",halign : 'center',fixed : true,	width : 120,sortable:true}
			 ] ]/*,
		
		onSelect : function(rowIndex, rowData) {
			if (rowData.applicationId)
				authUserWin(rowData);
		}*/
	});
};

function loaddatagridNoRevoke(url){
	datagridNoRevoke = $("#datagrid_noRevoke").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
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
		toolbar : "#toolbar_norevoke",
		showFooter : true,
		columns : [ [ {field : "itemid",checkbox : true	}
		, {field : "elementcode",title : "数据项编码",halign : 'center',width : 120,sortable:true,hidden:true}
		, {field : "bdgagencycn",title : "预算单位",halign : 'center',	width : 190,sortable:true}
		, {field : "bdgagency",title : "预算单位ID",halign : 'center',	width : 120,sortable:true,hidden:true}
		, {field : "bdgagencycode",title : "预算单位编码",halign : 'center',width : 80,	sortable:true,hidden:true}
		, {field : "bdgagencyname",title : "预算单位名称",halign : 'center',width : 120,sortable:true,hidden:true}
		, {field : "deptNature",title : "单位性质",halign : 'center',width : 80,sortable:true,hidden:true	} 
		, {field : "deptNatureName",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true}
		, {field : "accountName",title : "账户名称",halign : 'center',width : 200,sortable:true}
		, {field : "accountNumber",title : "银行账号",	halign : 'center',width : 170,sortable:true}
		, {field : "bankid",title : "开户银行ID",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "bankCode",title : "开户银行编码",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "bankName",title : "开户银行名称",halign : 'center',width : 180,sortable:true,hidden:true}
		, {field : "bankNameCn",title : "开户银行名称",halign : 'center',width : 180,sortable:true,hidden:true}
		, {field : "accountType",title : "账户类型",halign : 'center',	width : 120,sortable:true,hidden:true}
		, {field : "accountTypeName",title : "账户类型",halign : 'center',	width : 120,sortable:true}
		, {field : "type",title : "备案类型",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "type01",title : "预留",	halign : 'center',width : 120,	sortable:true,hidden:true}
		, {field : "type02",title : "账户性质",	halign : 'center',width : 80,sortable:true,hidden:true}
		,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
		, {field : "iszero",title : "零余额账户",halign : 'center',width : 80,sortable:true,
			formatter: function(value){
				return iszero[value];
			}
		} 
		, {field : "status",title : "状态",	halign : 'center',width : 50,sortable:true,
			formatter: function(value){
			return account_status[value];
			}
		}
		
		,{field : "legalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true		}
		,{field : "idcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true		}
		,{field : "financialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
		,{field : "accountContent",title : "核算内容",halign : 'center',	width:120,sortable:true		}
		, {field : "deptAddress",title : "单位地址",halign : 'center',width : 120,sortable:true}
		, {field : "linkman",title : "联系人",halign : 'center',width : 120,sortable:true}
		, {field : "phonenumber",title : "联系人电话",halign : 'center',width : 120,sortable:true}
		, {field : "remark",title : "注释",halign : 'center',width : 80,	sortable:true}
		, {field : "startdate",title : "启用日期",halign : 'center',width : 80,	sortable:true}
		, {field : "enddate",title : "停用日期",	halign : 'center',	width : 120,sortable:true}
		, {field : "createUserName",title : "创建人",halign : 'center',width : 80,sortable:true}
		, {field : "createTime",title : "创建时间",	halign : 'center',width : 120,sortable:true}
		, {field : "updateUserName",title : "修改人",halign : 'center',width : 80,sortable:true}
		, {field : "updateTime",title : "修改时间",	halign : 'center',width : 120,sortable:true}
		, {field : "ischange",title : "是否变更、注销",halign : 'center',width : 120,sortable:true,hidden:true}
		, {field : "applicationId",title : "申请序号",halign : 'center',width : 80,sortable:true	,hidden:true} 
		] ]
	});
};

//加载查询条件
function loadHashRevokeSearch(){
	
	//加载查询条件
	comboboxFuncByCondFilter(menuid,"oldAccountTypeSearch", "ACCTTYPE", "code", "name");//账户类型
	//comboboxFuncByCondFilter(menuid,"wfstatusSearch", "WFSTATUS", "code", "name");//当前状态
	//账户性质
	comboboxFuncByCondFilter(menuid,"oldAccountType02", "ACCTNATURE", "code", "name");
	$("#oldAccountType02").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#oldAccountTypeSearch").combobox("addClearBtn", {iconCls:"icon-clear"});
	
	
	$('#bdgagency1').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'bdgagencyHidden1',
		prompt: "请选择预算单位",
		multiSelect: false, //单选树
		queryParams : {
			menuid : menuid,
			customSql :allbdgagency
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

function loadNoRevokeSearch(){
	
	//账户类型
	comboboxFuncByCondFilter(menuid,"accountTypeSearch", "ACCTTYPE", "code", "name");
	//账户性质
	comboboxFuncByCondFilter(menuid,"type02Search", "ACCTNATURE", "code", "name");
	$("#accountTypeSearch").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#type02Search").combobox("addClearBtn", {iconCls:"icon-clear"});
	
	$("#accountNameSearch").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#accountNumberSearch").combobox("addClearBtn", {iconCls:"icon-clear"});

	//预算单位
	$('#bdgagency2').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'bdgagencyHidden2',
		prompt: "请选择预算单位",
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
			accountNumber : $("#oldAccountNumber").textbox('getValue'),
			starttime : $('#starttime').datetimebox('getValue'),
			endtime :  $('#endtime').datetimebox('getValue'),
			accountName : $("#oldAccountName").textbox('getValue'),
			menuid : menuid,
			activityId : activityId,
			type:type,
			firstNode : true,
			lastNode : false
			
		};
	$("#datagrid_hasRevoke").datagrid("load",param);
	
}
/**
 * 账户注销-查询（可注销）
 */
function queryNoRevokeInfo() {
	$("#datagrid_noRevoke").datagrid("load", {
		bdgagencycode : $("#bdgagency2").treeDialog('getValue'),
		accountType : $("#accountTypeSearch").combobox('getValue'),
		type02 : $("#type02Search").combobox('getValue'),
		accountName : $("#accountNameSearch").val(),
		accountNumber : $("#accountNumberSearch").val(),
		
		menuid : menuid,
		activityId : activityId,
		type : type,
		firstNode : true,
		lastNode : false
	});
};
/**
 * 注销账户-新增
 */
function addRevokeInfo() {
	
	var rows = datagridNoRevoke.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请在“可注销账户”里选择一条数据！");
		return;
	}
	
	showModalDialog(485, "revoke_add", types.add, datagridNoRevoke, "账户注销-新增",
			"manage/revoke/controller/AccountRevokeController/revoke_add.do?optType=" + types.add, urls.addRevokeInfo,true);
};

/**
 * 注销账户-编辑
 */
function editRevokeInfo() {
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	if(rows[0].createUser != userid){
		easyui_warn("无权限修改其他人录入的数据！");
		return;
	}
	showModalDialog(485, "revoke_add", types.edit, datagridHasRevoke, "账户注销-修改",
			"manage/revoke/controller/AccountRevokeController/revoke_add.do?optType=" + types.edit, urls.editRevokeInfo, true);
};
/**
 * 注销账户-详情
 */
function queryRevokeInfo() {
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	showModalDialog(485, "revoke_add", types.view, datagridHasRevoke, "账户注销-详情",
			"manage/revoke/controller/AccountRevokeController/revoke_add.do?optType=" + types.view, urls.editRevokeInfo, true);
};

/**
 * 删除
 */
function deleteRevokeInfo() {
	var rows = datagridHasRevoke.datagrid("getSelections");
	var num=rows.length;
	if (num < 1) {
		easyui_warn("请至少选择一条数据！");	
		return;
	}
	var applicationIds = "";
	var canDelete = false;//是否可删除
	var selectRowsId ="";
	for (var int = 0; int < rows.length; int++) {
		if(rows[int].createUser != userid){
			if(!canDelete){
				canDelete = true;
			}
			selectRowsId = selectRowsId+""+(int+1)+","
		}else{
			applicationIds = applicationIds+ rows[int].applicationId;
			if(int!=rows.length-1){
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
			$.post(urls.deleteRevokeInfo, {
				applicationIds : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagridHasRevoke.datagrid("reload");
					datagridNoRevoke.datagrid("reload");
				});
			}, "json");
		}
	});

};

/**
 * 注销
 */
function removeRevokeInfo(){
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");	
		return;
	}
	var applicationIds = "";
	var canDelete = false;//是否可删除
	var selectRowsId ="";
	for (var int = 0; int < rows.length; int++) {
		if(rows[int].createUser != userid){
			if(!canDelete){
				canDelete = true;
			}
			selectRowsId = selectRowsId+""+(int+1)+","
		}else{
			applicationIds = applicationIds+ rows[int].applicationId;
			if(int!=rows.length-1){
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
	parent.$.messager.confirm("确认撤回", message, function(r) {
		if (r) {
			$.post(urls.removeRevokeInfo, {
				applicationIds : applicationIds,
				menuid : $("#menuid").val(),
				activityId :$("#activityId").val()
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagridHasRevoke.datagrid("reload");
				});
			}, "json");
		}
	});
};

/**
 * 送审
 */
function submitRevokeInfo(){
	var rows = datagridHasRevoke.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");	
		return;
	}
	var applicationIds = "";
	var canDelete = false;//是否可删除
	var selectRowsId ="";
	for (var int = 0; int < rows.length; int++) {
		if(rows[int].createUser != userid){
			if(!canDelete){
				canDelete = true;
			}
			selectRowsId = selectRowsId+""+(int+1)+","
		}else{
			applicationIds = applicationIds+ rows[int].applicationId;
			if(int!=rows.length-1){
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
	parent.$.messager.confirm("确认送审", message, function(r) {
		if (r) {
			$.post(urls.submitRevokeInfo, {
				menuid : $("#menuid").val(),
				activityId : activityId,
				applicationIds : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagridHasRevoke.datagrid("reload");
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
		fill) {

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
				
				if(operType=="edit" || operType=="add"){//编辑或详情
					if(operType=="edit"){
						
						//加载数据
						mdDialog.find("#phonenumber").searchbox("setValue",row.applPhonenumber);
						mdDialog.find("#accountName").searchbox("setValue",row.oldAccountName);
						mdDialog.find("#bank").searchbox("setValue",row.oldBankNameCn);
						mdDialog.find("#accountNumber").searchbox("setValue",row.oldAccountNumber);
						mdDialog.find("#type02").val(row.oldType02);
						mdDialog.find("#accountType").val(row.oldAccountType);
						mdDialog.find("#accountContent").searchbox("setValue",row.oldAccountContent);
						mdDialog.find("#legalPerson").searchbox("setValue",row.oldLegalPerson);
						mdDialog.find("#idcardno").searchbox("setValue",row.oldIdcardno);
						mdDialog.find("#financialOfficer").searchbox("setValue",row.oldFinancialOfficer);
						mdDialog.find("#iszero").searchbox("setValue",row.oldIszero);
						mdDialog.find("#type02Name").textbox("setValue",row.oldType02Name);
						mdDialog.find("#accountTypeName").textbox("setValue",row.oldAccountTypeName);
						
					}
					//comboboxFunc("iszero", "SYS_TRUE_FALSE", "code", "name", mdDialog);//零余额账户
					comboboxFunc("deptNature", "AGENCYTYPE", "code", "name", mdDialog); //单位性质
					//comboboxFunc("accountType", "ACCTTYPE", "code", "name", mdDialog);//账户类型
					//comboboxFunc("type02", "ACCTNATURE", "code", "name", mdDialog);//账户性质
					comboboxFuncByCondFilter(menuid,"changeType", "REVOKETYPE", "code", "name", mdDialog);//注销事项
					
					if(operType=="edit"){//编辑
						//查询附件
						showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
					}
					
				}else if(operType=="view"){//详情
					//showFileTable(mdDialog.find("#fileTable"),false,"FAAPPLICATION",row.applicationId,"");
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
		buttons : funcOperButtons(operType, url, dataGrid, form)
		,onBeforeClose:function(){
			//添加窗口关闭事件，将全局变量置为“”
			
			//parent.$.modalDialog.handler.find("#itemids").val("");
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
function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if (operType == types.view) {
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
				//parent.$.modalDialog.handler.find("#itemids").val("");
			}
		} ];
	} else if (operType == types.add) {
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"?isSub=0", form,operType);
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"?isSub=1&menuid="+menuid, form,operType);
			}
		},{
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
			}
		} ,{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
				//parent.$.modalDialog.handler.find("#itemids").val("");
			}
		} ];
	} else if (operType == types.edit) {
		buttons = [ 
		   {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"?isSub=0", form,operType);
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"?isSub=1&menuid="+menuid+"&activityId="+activityId, form,operType);
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
				parent.$.modalDialog.handler.dialog('close');
				//parent.$.modalDialog.handler.find("#itemids").val("");
			}
		} ];
	}
	return buttons;
	
};
/**
 * 提交表单
 * 
 * @param url
 *            表单url
 */
function submitForm(url, form,operType) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
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
//单位换算
function bytesToSize(bytes) {
    if (bytes === 0) return '0 B';
    var k = 1000, // or 1024
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
   return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}
//点击左边菜单刷新右边
function showReload() {
	datagridHasRevoke.datagrid('reload');
	datagridNoRevoke.datagrid('reload');
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