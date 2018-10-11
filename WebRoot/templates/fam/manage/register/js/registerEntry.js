var baseUrl = contextpath + "manage/register/AccountRegisterController/";
var urls = {
	//query : baseUrl +  "query.do", //查询
	query : contextpath + "manage/change/AccountChangeController/queryAllAccount.do", //查询
	add : baseUrl +  "add.do", //新增
	edit : baseUrl + "edit.do", //修改
	remove : baseUrl + "delete.do", //删除
	formEntry : baseUrl + "formEntry.do", //FORM表单界面
	detailFormEntry : baseUrl + "detailFormEntry.do", //详情FORM表单界面
	sendWf : baseUrl + "sendWorkFlow.do", //工作流送审
	revokeWorkFlow : baseUrl + "revokeWorkFlow.do", //工作流撤回
	bdgagencyInfo : baseUrl + "getBdgagency.do", //根据单位获取单位相应信息
	//导出开立申请表
	outRegisterAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRegisterAppicationForm.do"
};
var types = {
	view : "view", //查阅
	add : "add", //新增
	edit : "edit" //修改
};
var accRegDatagrid;
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#accRegDatagrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

$(function() {
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
				$('#addBtn').linkbutton('enable');
				$('#editBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
				$('#editBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
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
	        	data = sqdclData;
        	}else if(status == '2'){//已处理
	        	data =sqyclData;
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
			doQuery();
			$('#backBtn').linkbutton('disable');
			if(record.value =='21'){
				$('#backBtn').linkbutton('enable');
			}else if(record.value =='22'){
				$('#backBtn').linkbutton('disable');
			}
		}
	});
	$('#backBtn').linkbutton('disable');
	$('#topBdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'topbdgagencyHidden',
		prompt: "请选择预算单位",
		multiSelect: false, //单选树
		editable :false,
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
	comboboxFuncByCondFilter(menuid,"topAccountType", "ACCTTYPE", "code", "name");//账户类型
	comboboxFuncByCondFilter(menuid,"topAccountType02", "ACCTNATURE", "code", "name");//账户性质
	$("#topAccountType").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountType02").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountNum").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#topAccountName").combobox("addClearBtn", {iconCls:"icon-clear"});
	loadAccRegDataGrid(urls.query);
});
//页面刷新
function showReload() {
	accRegDatagrid.datagrid('reload'); 
}
function loadAccRegDataGrid(url) {

	accRegDatagrid = $("#accRegDatagrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort:true,
		queryParams: {
			status : 1,
			menuid : menuid,
			activityId : activityId,
			processedStatus : "",
			type : type,
			firstNode : true,
			lastNode : false
		},
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_account",
		columns : [ [ 
				{field : "applicationId",checkbox : true,align:'center'} 
				,{field : "bdgagencycode",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true	,align:'center'	}
				,{field : "bdgagencyname",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true,align:'center'		}
				,{field : "bdgagencycn",title : "预算单位",halign : 'center',width : 180,sortable:true,align:'left'	}
				,{field : "applyReason",title : "开户原因",halign : 'center',	width:150,sortable:true,align:'left'	}
				,{field : "deptNature",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left'	,hidden:true}
				,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left'	}
				,{field : "accountName",title : "账户名称",halign : 'center',	width:150	,sortable:true}
				,{field : "accountNumber",title : "银行账号",halign : 'center',	width:150,sortable:true	}
				,{field : "accountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
	            ,{field : "accountTypeCode",title : "账户类型",halign : 'center',	width:100,sortable:true,hidden:true }
	            ,{field : "type02",title : "账户性质",halign : 'center',	width:150,sortable:true,hidden:true}
	            ,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
	            ,{field : "bankCode",title : "开户行code",halign : 'center',	width:150,sortable:true,hidden:true		}
	            ,{field : "bankName",title : "开户行name",halign : 'center',	width:150,sortable:true,hidden:true		}
	            ,{field : "bankNameCn",title : "账户开户行",halign : 'center',	width:150,sortable:true	}
	            ,{field : "iszero",title : "零余额账户",halign : 'center',	width:80,sortable:true,formatter: function(value){return iszero[value];}}
	            ,{field : "legalPerson",title : "法定代表人",halign : 'center',	width:120,sortable:true,hidden:true		}
	            ,{field : "idcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true,hidden:true		}
	            ,{field : "financialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true,hidden:true		}
	            ,{field : "wfid",title : "wfid",halign : 'center',	width:150,sortable:true,hidden:true		}
				,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left'	,hidden:true}
				,{field : "deptNatureName",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left'	}
				,{field : "deptAddress",title : "单位地址",halign : 'center',	width:150,sortable:true	,align:'left'	}
			    ,{field : "applPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left'		}
			    ,{field : "remark",title : "备注",halign : 'center',	width:150,sortable:true,align:'left'		}
			    ,{field : "createUserName",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left'		}
			    ,{field : "createTime",title : "创建时间",halign : 'center',	width:150,sortable:true,align:'left'		}
			    ,{field : "updateUserName",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left'		}
			    ,{field : "updateTime",title : "修改时间",halign : 'center',	width:150,sortable:true,align:'left'		}
			    ,{field : "accountId",title : "账户id",halign : 'center',	width:150,sortable:true,align:'left'		,hidden:true	}
			    ,{field : "linkman",title : "联系人",halign : 'center',	width:150,sortable:true,align:'left'		,hidden:true	}
			    ,{field : "accountNum",title : "开户号",halign : 'center',	width:150,sortable:true,align:'left'		,hidden:true	}
			    ,{field : "iszeroName",title : "零余额账户",halign : 'center',	width:150,sortable:true,align:'left'		,hidden:true	}
			    ,{field : "createUser",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left'		,hidden:true	}
			  
		              ]],
		onSelect : function(rowIndex, rowData) {
			
		},
		onLoadSuccess : function(data) {
			
		}
	});
}

/**
 * 账户开立新增界面
 */
function add() {
	showModalDialog("账户开立申请",600,900,"accountRegisterForm",types.add,accRegDatagrid,urls.formEntry,urls.add,false);
}

/**
 * 账户开立修改界面
 */
function edit() {
	var records = accRegDatagrid.datagrid("getSelections");
	if (records.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	if(records[0].createUser != userid){
		easyui_warn("无权限修改其他人录入的数据！");
		return;
	}
	showModalDialog("账户开立申请修改",600,900,"accountRegisterForm",types.edit,accRegDatagrid,urls.formEntry+"?editFlag=1&activityId="+activityId+"&wfid="+records[0].wfid ,urls.edit,true);
}

/**
 * 账户开立详情查看界面
 */
function view() {
	var records = accRegDatagrid.datagrid("getSelections");
	if (records.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	var wfid =records[0].wfid;
	var cs ="?wfid="+wfid;
	showModalDialogForDetail("账户开立详情",630,900,"accountRegisterForm",types.view,accRegDatagrid,urls.detailFormEntry+cs ,urls.edit,true);
}
function showModalDialogForDetail(title,height,width, formid, operType, dataGrid, href, url,fill) {
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
		buttons : funcOperButtons(operType, url, dataGrid,formid)
	});
}
/**
 * 账户开立删除
 */
function del() {
	var selectRow = accRegDatagrid.datagrid('getChecked');
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
	parent.$.messager.confirm("删除确认", message, function(r) {
		if (r) {
			$.post(urls.remove, {
				applicationId : applicationIds
			}, function(result) {
				accRegDatagrid.datagrid('reload');
				easyui_auto_notice(result, function() {
				});
			}, "json");
		}
	});
}

function sendWf() {
	var selectRow = accRegDatagrid.datagrid('getChecked');
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
			$.post(urls.sendWf, {
				menuid : menuid,
				activityId :activityId,
				applicationId : applicationIds
			}, function(result) {
				accRegDatagrid.datagrid('reload');
				easyui_auto_notice(result, function() {
				});
			}, "json");
		}
	});
}

/**
 * 撤回
 */
function backWf() {
	var selectRow = accRegDatagrid.datagrid('getChecked');
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
			$.post(urls.revokeWorkFlow, {
				menuid : menuid,
				activityId :activityId,
				applicationId : applicationIds
			}, function(result) {
				accRegDatagrid.datagrid('reload');
				easyui_auto_notice(result, function() {
				});
			}, "json");
		}
	});
}

function doQuery() {
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
			status : $("#status").combobox("getValue"),
			starttime : $('#starttime').datetimebox('getValue'),
			endtime :  $('#endtime').datetimebox('getValue'),
			menuid : menuid,
			activityId : activityId,
			processedStatus : v,
			type:type,
			firstNode : true,
			lastNode : false
		};
	accRegDatagrid.datagrid("load", param);
}

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
			comboboxFuncByCondFilter(menuid,"deptNature", "AGENCYTYPE", "code", "name", mdDialog);//单位性质
			comboboxFuncByCondFilter(menuid,"type02", "ACCTNATURE", "code", "name", mdDialog);//账户性质
			comboboxFunc("iszero", "SYS_TRUE_FALSE", "code", "name", mdDialog);//零余额账户
			comboboxFuncByCondFilter(menuid,"accountType", "ACCTTYPE", "code", "name", mdDialog);//账户类型
			mdDialog.find("#bank").treeDialog({
				title :'选择开户行',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'bankCode',
				editable :false,
				prompt: "请选择开户行",
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
			mdDialog.find('#bdgagencyname').treeDialog({
				title :'选择预算单位',
				dialogWidth : 420,
				dialogHeight : 500,
				editable :false,
				hiddenName:'bdgagencycode',
				prompt: "请选择预算单位",
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
				},
				assignFunc : function(value){
					var a  = mdDialog.find('#bdgagencyname').treeDialog("getText");
					mdDialog.find('#bdgagencyname').treeDialog("setText",a.substr(a.indexOf("-")+1))
					//选择单位后，查询数据库的值并填充至页面
					$.post(urls.bdgagencyInfo, {
						bdgagencycode : value
					}, function(result) {
						if(result.body !=null){
							parent.$.modalDialog.handler.find('#deptNature').combobox('setValue', result.body.deptNature);
							parent.$.modalDialog.handler.find('#linkman').textbox('setValue', result.body.linkman);
							parent.$.modalDialog.handler.find('#applPhonenumber').textbox('setValue', result.body.phonenumber);
							parent.$.modalDialog.handler.find('#deptAddress').val(result.body.deptDddress);
						}
					}, "json");
				
				}
			});
			if(fill){
				var row = accRegDatagrid.datagrid("getSelections")[0];
				mdDialog.find("#menuid").val($("#menuid").val());
				if(operType=='edit'){
					var f = parent.$.modalDialog.handler.find('#' + formid);
					f.form("load", row);
					parent.$.modalDialog.handler.find('#deptNature').combobox('setValue', row.deptNature);
					parent.$.modalDialog.handler.find('#linkman').textbox('setValue', row.linkman);
					parent.$.modalDialog.handler.find('#applPhonenumber').textbox('setValue', row.applPhonenumber);
					parent.$.modalDialog.handler.find('#deptAddress').val(row.deptAddress);
					showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
				}else{
					showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
				}
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid,formid)
	});
}
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
	var buttons = [];
	if (operType == types.view) {
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == types.add) {
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				subValidate(operType, url, dataGrid, form,"");
			}
		},{
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				subValidate(operType, url, dataGrid, form,"1");
			}
		},{
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == types.edit) {
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				subValidate(operType, url, dataGrid, form,"");
			}
		},{
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				subValidate(operType, url, dataGrid, form,"1");
			}
		},{
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == types.view) {
		buttons = [  {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	return buttons;
}
//填写新账户信息时，进行必填校验
function subValidate(operType, url, dataGrid, form,isSub){
	var accountName = parent.$.modalDialog.handler.find("#accountName").val();
	var accountNumber = parent.$.modalDialog.handler.find("#accountNumber").val();
	
	submitForm(url, form, operType,isSub);
}
/**
 * 提交表单
 * 
 * @param url
 *            表单url
 */
function submitForm(url,  form, operType,workflowflag) {
	var formEntry = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	parent.$.modalDialog.handler.find("#menuid").val(menuid);
	parent.$.modalDialog.handler.find("#activityId").val(activityId);
	formEntry.form("submit", {
		url : url,
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据保存中，请稍后....'
			});
			var isValid = formEntry.form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}

			return isValid;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}
/**
 * 工作流程信息
 */
function workflowMessage(){
	var selectRow = accRegDatagrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}

/**
 * 开立申请表
 */
function outRegisterApplicationform(){
	var selectRow = accRegDatagrid.datagrid('getChecked');
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
	
	window.location.href=urls.outRegisterAppicationForm+"?applicationIds="+applicationIds+"&type=1";
}