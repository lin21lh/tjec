var baseUrl = contextpath + "manage/register/AccountRegisterController/";
var urls = {
	query : contextpath + "manage/change/AccountChangeController/queryAllAccount.do", //查询
	edit : baseUrl + "edit.do", //修改
	formEntry : baseUrl + "auditEntryForm.do", //FORM表单界面
	auditWf : baseUrl + "auditWorkFlow.do", //工作流送审
	detailFormEntry : baseUrl + "detailFormEntry.do", //详情FORM表单界面
	revokeWorkFlow : baseUrl + "revokeWorkFlow.do", //工作流撤回
	getFormIsEdit :contextpath+"manage/change/AccountChangeController/getFormIsEdit.do",
	plcl : baseUrl + "plEntryForm.do", //批量处理form
	//导出开立申请表
	outRegisterAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRegisterAppicationForm.do"
};
var types = {
	view : "view", //查阅
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
	var pstatus =spdclData;
	if(lastNode=='true'){
		pstatus =lastNodeSpdclData;
	}
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
	$('#backBtn').linkbutton('disable');
	//加载grid
	loadAccRegDataGrid(urls.query);
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
		queryParams: {
			status : 1,
			menuid : menuid,
			activityId : activityId,
			processedStatus : "",
			type : type,
			firstNode : firstNode,
			lastNode : lastNode
		},
		multiSort:true,
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_account",
		columns : [ [ 
						{field : "applicationId",checkbox : true,align:'center'} 
						,{field : "bdgagencycode",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true	,align:'center'	}
						,{field : "bdgagencyname",title : "预算单位",halign : 'center',width : 100,sortable:true,hidden:true,align:'center'		}
						,{field : "bdgagencycn",title : "预算单位",halign : 'center',width : 180,sortable:true,align:'left'	}
						,{field : "deptNature",title : "单位性质",halign : 'center',	width:80,sortable:true,align:'left'	,hidden:true}
						,{field : "applyReason",title : "开户原因",halign : 'center',	width:150,sortable:true,align:'left'	}
						,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left'	}
						,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left'	,hidden:true}
						,{field : "accountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
			            ,{field : "accountTypeCode",title : "账户类型",halign : 'center',	width:100,sortable:true,hidden:true }
			            ,{field : "type02",title : "账户性质",halign : 'center',	width:150,sortable:true,hidden:true}
			            ,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
			            ,{field : "accountName",title : "账户名称",halign : 'center',	width:150	,sortable:true}
			            ,{field : "bankNameCn",title : "账户开户行",halign : 'center',	width:150,sortable:true	}
			            ,{field : "accountNumber",title : "银行账号",halign : 'center',	width:150,sortable:true	}
			            ,{field : "bankCode",title : "开户行code",halign : 'center',	width:150,sortable:true,hidden:true		}
			            ,{field : "bankName",title : "开户行name",halign : 'center',	width:150,sortable:true,hidden:true		}
			            ,{field : "iszero",title : "零余额账户",halign : 'center',	width:80,sortable:true,formatter: function(value){return iszero[value];}}
			            ,{field : "legalPerson",title : "法定代表人",halign : 'center',	width:120,sortable:true,hidden:true		}
			            ,{field : "idcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true,hidden:true		}
			            ,{field : "financialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true,hidden:true		}
			            ,{field : "wfid",title : "wfid",halign : 'center',	width:150,sortable:true,hidden:true		}
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
				              ]],
		onSelect : function(rowIndex, rowData) {
			
		},
		onLoadSuccess : function(data) {
			
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
	accRegDatagrid.datagrid("load", param);
}
function showModalDialog(title,height,width, formid, operType, dataGrid, href, url,fill,formIsEdit,isBackToFirstNode) {
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
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			//附件初始化
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
			mdDialog.find('#bdgagency').treeDialog({
				title :'选择预算单位',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'bdgagencyCode',
				editable :false,
				prompt: "请选择预算单位",
				multiSelect: false, //单选树
				queryParams : {
					menuid : menuid
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
			if(fill){
				var row = accRegDatagrid.datagrid("getSelections")[0];
				mdDialog.find("#menuid").val($("#menuid").val());
				var f = parent.$.modalDialog.handler.find('#' + formid);
				f.form("load", row);
				parent.$.modalDialog.handler.find('#deptNature').textbox('setValue', row.deptNature);
				parent.$.modalDialog.handler.find('#linkman').textbox('setValue', row.linkman);
				parent.$.modalDialog.handler.find('#applPhonenumber').textbox('setValue', row.applPhonenumber);
				parent.$.modalDialog.handler.find('#deptAddress').val(row.deptAddress);
				var formIsEdit = parent.$.modalDialog.handler.find('#formIsEdit').val();
				if(formIsEdit=='false'){
					showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
				}else{
					showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
				}
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid,formid,formIsEdit,isBackToFirstNode)
	});
}
function showModalDialogForlr(title,height,width, formid, operType, dataGrid, href, url,fill,formIsEdit,isBackToFirstNode) {
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
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			//附件初始化
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
			mdDialog.find('#bdgagency').treeDialog({
				title :'选择预算单位',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'bdgagencyCode',
				editable :false,
				prompt: "请选择预算单位",
				multiSelect: false, //单选树
				queryParams : {
					menuid : menuid
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
			if(fill){
				var row = accRegDatagrid.datagrid("getSelections")[0];
				mdDialog.find("#menuid").val($("#menuid").val());
				var f = parent.$.modalDialog.handler.find('#' + formid);
				f.form("load", row);
				parent.$.modalDialog.handler.find('#deptNature').textbox('setValue', row.deptNature);
				parent.$.modalDialog.handler.find('#linkman').textbox('setValue', row.linkman);
				parent.$.modalDialog.handler.find('#applPhonenumber').textbox('setValue', row.applPhonenumber);
				parent.$.modalDialog.handler.find('#deptAddress').val(row.deptAddress);
				var formIsEdit = parent.$.modalDialog.handler.find('#formIsEdit').val();
				if(formIsEdit=='false'){
					showFileDiv(mdDialog.find("#filetd"),false,"FAAPPLICATION",row.applicationId,"");
				}else{
					showFileDiv(mdDialog.find("#filetd"),true,"FAAPPLICATION",row.applicationId,"");
				}
			}
		},
		buttons : funcOperButtonsForlr(operType, url, dataGrid,formid,formIsEdit,isBackToFirstNode)
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
function funcOperButtons(operType, url, dataGrid, form,formIsEdit,isBackToFirstNode) {
	var buttons = [];
	if(formIsEdit){
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId+"&menuid="+menuid,form,1,wfid,activityId,applicationId,"");
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId+"&menuid="+menuid,form,2,wfid,activityId,applicationId,"");
				}
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=2&activityId="+activityId+"&menuid="+menuid,form,3,wfid,activityId,applicationId,"");
				}
			},{
				text : "附件管理",
				iconCls : "icon-files",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
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
			}, {
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId+"&menuid="+menuid,form,1,wfid,activityId,applicationId,"");
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId+"&menuid="+menuid,form,2,wfid,activityId,applicationId,"");
				}
			},{
				text : "附件管理",
				iconCls : "icon-files",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
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
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ];

		}
	}else{
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId+"&menuid="+menuid,form,1,wfid,activityId,applicationId,"");
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId+"&menuid="+menuid,form,2,wfid,activityId,applicationId,"");
				}
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=2&activityId="+activityId+"&menuid="+menuid,form,3,wfid,activityId,applicationId,"");
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
			}, {
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+activityId+"&menuid="+menuid,form,1,wfid,activityId,applicationId,"");
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
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+activityId+"&menuid="+menuid,form,2,wfid,activityId,applicationId,"");
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
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ];
		}
	}
	return buttons;
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
function funcOperButtonsForlr(operType, url, dataGrid, form,formIsEdit,isBackToFirstNode) {
	var buttons = [];
	if(formIsEdit){
		if(isBackToFirstNode){
			buttons = [ {
				text : "送审",
				iconCls : "icon-save",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
					if (!isValid) {
						return;
					}
					parent.$.messager.confirm("送审确认", "确定要送审账户信息？", function(r) {
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
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ];
		}else{
			buttons = [ {
				text : "送审",
				iconCls : "icon-save",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
					if (!isValid) {
						return;
					}
					parent.$.messager.confirm("送审确认", "确定要送审账户信息？", function(r) {
						if (r) {
							submitFormDialog(url+"?isback=&activityId="+activityId+"&menuid="+menuid, form, 1, "")
						}
					});
				}
			},{
				text : "附件管理",
				iconCls : "icon-files",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAAPPLICATION");
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
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ];

		}
	}else{
		if(isBackToFirstNode){
			buttons = [ {
				text : "送审",
				iconCls : "icon-save",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
					if (!isValid) {
						return;
					}
					parent.$.messager.confirm("送审确认", "确定要送审账户信息？", function(r) {
						if (r) {
							submitFormDialog(url+"?isback=&activityId="+activityId+"&menuid="+menuid, form, 1, "")
						}
					});
				}
			},{
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
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ];
		}else{
			buttons = [ {
				text : "送审",
				iconCls : "icon-save",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
					if (!isValid) {
						return;
					}
					parent.$.messager.confirm("送审确认", "确定要送审账户信息？", function(r) {
						if (r) {
							submitFormDialog(url+"?isback=&activityId="+activityId, form, 1, "")
						}
					});
				}
			},{
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
			}, {
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ];
		}
	}
	return buttons;
}

//填写新账户信息时，进行必填校验
function subValidate(url,  form,isback,message){
   submitForm(url, form,isback,message);
}

/**
 * 提交表单
 * 
 * @param url
 *            表单url
 */
function submitForm(url, form,isback,message) {
	var formEntry = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#isback").val(isback);
	parent.$.modalDialog.handler.find("#activityId").val(activityId);
	var isValid=false;
	isValid = formEntry.form('validate');
	if(!isValid){
		return;
	}
	parent.$.messager.confirm("操作确认", message, function(r) {
		if(r){
			formEntry.form("submit", {
				url : url,
				onSubmit : function() {
					var isValid=false;
						if (r) {
							parent.$.messager.progress({
								title : '提示',
								text : '数据处理中，请稍后....'
							});
							isValid = formEntry.form('validate');
							if (!isValid) {
								parent.$.messager.progress('close');
							}
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
	});
}
//审批
function pushWF(){
	var selectRow = accRegDatagrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
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
			if(supply =='true'){
				showModalDialogForlr("账户开立录入",630,900,"accountRegisterForm","edit",accRegDatagrid,urls.formEntry+cs,urls.auditWf,true,formIsEdit,isBackToFirstNode);
			}else{
				showModalDialog("账户开立审批",630,900,"accountRegisterForm","edit",accRegDatagrid,urls.formEntry+cs,urls.auditWf,true,formIsEdit,isBackToFirstNode);
			}
		}
	}, "json");
}
//批量审批或退回
function pushAgreeWF(flag){
	var selectRow = accRegDatagrid.datagrid('getChecked');
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
	showAuditModalDialog(parent.$,accRegDatagrid,text,menuid,activityId,applicationIds,isback);
}
function showModalDialogForSp(title,height,width,isback,applicationIds) {
	var button =[];
	if(isback == 1){
		button = [ {
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else{
		button = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				var form = parent.$.modalDialog.handler.find('#plclForm');
				var isValid = form.form('validate');
				if(isValid){
					$.post("", {
						applicationId : applicationIds
					}, function(result) {
						alert(result)
						easyui_auto_notice(result, function() {
							accRegDatagrid.datagrid('reload');
						});
					}, "json");
				}
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	parent.$.modalDialog({
		title : title,
		width : width,
		height : height,
		href : urls.plcl,
		onLoad : function() {
			
		},
		buttons : button
	});
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
	var cs ="?wfid="+wfid+"&activityId="+activityId+"&type="+type;
	showModalDialogForDetail("账户开立详情",630,900,"accountRegisterForm",types.view,accRegDatagrid,urls.detailFormEntry+cs,urls.edit,true);
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
 * 撤回
 */
function backWf() {
	var selectRow = accRegDatagrid.datagrid('getChecked');
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
	parent.$.messager.confirm("撤回确认", "确认要将选中数据撤回？", function(r) {
		if (r) {
			$.post(urls.revokeWorkFlow, {
				menuid : menuid,
				activityId :activityId,
				applicationId : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					accRegDatagrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
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