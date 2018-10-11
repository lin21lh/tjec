
var baseUrl = contextpath + "manage/record/AccountRecordController/";
var urls = {
	recordAccountDataGridUrl : baseUrl + "queryChangeAccount.do",
	recordAuditInitUrl : baseUrl + "recordAuditInitUrl.do",
	operateRecordInfo : baseUrl + "operateRecordInfo.do",
	removeRecordInfo :  baseUrl + "removeRecordInfo.do",
	submitRecordInfo : baseUrl + "submitRecordInfo.do",
	getFormIsEdit :contextpath+"manage/change/AccountChangeController/getFormIsEdit.do",
	//导出变更申请表
	outRecordAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRecordAppicationForm.do"
};
//默认加载
$(function() {
	var pstatus =spdclData;
	if(lastNode=='true'){
		pstatus =lastNodeSpdclData;
	}
	
	//加载grid
	loadChangeGrid(urls.recordAccountDataGridUrl);
	//top
	$('#topBdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'topbdgagencyHidden',
		prompt: "请选择预算单位",
		multiSelect: false, //单选树
		dblClickRow: true,
		checkLevs: [1,2,3], //只选择3级节点
		queryParams : {
			menuid : menuid,
			customSql : allbdgagency
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		elementcode : "BDGAGENCY",
		filters:{
			code: "单位编码",
			name: "单位名称"
		}
	});
	
	$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			downGridQuery();
			switch (record.value) {
			case '1':
				$('#btn_query').linkbutton('enable');//查询
				$('#btn_ope').linkbutton('enable');//审批
				$('#btn_appro').linkbutton('enable');//审批同意
				$('#btn_refuse').linkbutton('enable');//退回
				$('#btn_revoke').linkbutton('disable');//撤回
				break;
			case '2':
				$('#btn_query').linkbutton('enable');
				$('#btn_ope').linkbutton('disable');
				$('#btn_appro').linkbutton('disable');
				$('#btn_refuse').linkbutton('disable');
				$('#btn_revoke').linkbutton('disable');
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
			downGridQuery();
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
});
//已变更账户grid
var recordAccountDataGrid;
//加载已变更grid列表
function loadChangeGrid(url) {
	recordAccountDataGrid = $("#recordAccountDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			status : 1,
			processedStatus : "",
			type : "",//备案类型
			menuid : $('#menuid').val(),//菜单id
			activityId : $('#activityId').val(),//节点id
			firstNode : $('#firstNode').val(),
			lastNode : $('#lastNode').val()
		},
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
					,{field : "type",title : "备案类型",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2,hidden:true}
					,{field : "typeName",title : "备案类型",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2}
					,{field : "applyReason",title : "变更原因",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	}
					,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "acctWfstatusName",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	}
					,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "changetype",title : "事项",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "changetypeName",title : "事项",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{title:'原账户信息',colspan:6}
                    ,{title:'新账户信息',colspan:14}
                    ,{field : "deptAddress",title : "单位地址",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2	}
                    ,{field : "wfid",title : "工作流id",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2	,hidden:true}
                    ,{field : "acctWfid",title : "工作流id",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2,hidden:true	}
  	                ,{field : "applPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "acctPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
  	                ,{field : "remark",title : "备注",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "createUser",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2,hidden:true		}
  	                ,{field : "createUserName",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
  	                ,{field : "createTime",title : "创建时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
  	                ,{field : "updateUser",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2,hidden:true		}
  	                ,{field : "updateUserName",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true	}
  	                ,{field : "updateTime",title : "修改时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true	}
  	                ,{field : "modifyUser",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true	}
	                ,{field : "modifyUserName",title : "备案人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
	                ,{field : "modifyTime",title : "备案时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2			}
  	                ,{field : "accountId",title : "账户id",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true	}
                ] ,[{field : "oldAccountTypeName",title : "账户类型",halign : 'center',	width:100,sortable:true }
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
	              ,{field : "accountType",title : "账户类型",halign : 'center',	width:100,sortable:true,hidden:true }
	              ,{field : "type02",title : "账户性质",halign : 'center',	width:150,sortable:true,hidden:true}
	              ,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
	              ,{field : "accountName",title : "账户名称",halign : 'center',	width:150	,sortable:true}
	              ,{field : "accountNumber",title : "银行账号",halign : 'center',	width:150,sortable:true	}
	              ,{field : "bankCode",title : "开户行code",halign : 'center',	width:150,sortable:true,hidden:true		}
	              ,{field : "bankName",title : "开户行name",halign : 'center',	width:150,sortable:true,hidden:true		}
	              ,{field : "bankNameCn",title : "账户开户行",halign : 'center',	width:150,sortable:true	}
	              ,{field : "iszero",title : "零余额账户",halign : 'center',	width:80,sortable:true,formatter: function(value){return iszero[value];}}
                ]]
	});
}

/**
 * 账户备案-详情
 */
function qryRecordInfo() {
	
	var rows = recordAccountDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一行数据");
		return;
	}
	
	var wfid =rows[0].acctWfid;
	var activityId = $("#activityId").val();//节点id
	var cs ="?wfid="+wfid+"&activityId="+activityId+"&optType=view";
	
	showModalDialog("账户备案-审批",660,850, "recordChangeForm", "view", recordAccountDataGrid,urls.recordAuditInitUrl+cs, urls.operateRecordInfo,true);
};
/**
 * 账户备案审批
 */
function operateRecordInfo() {
	
	var rows = recordAccountDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一行数据");
		return;
	}
	
	var wfid =rows[0].acctWfid;
	var activityId = $("#activityId").val();//节点id
	
	var cs ="?wfid="+wfid+"&activityId="+activityId+"&optType=edit";
	
	$.post(urls.getFormIsEdit, {
		wfid : wfid,
		activityId : activityId
	}, function(result) {
		if(result.body !=null){
			var formIsEdit =  result.body.formIsEdit;
			var isBackToFirstNode =  result.body.isBackToFirstNode;
			showModalDialog("账户备案-审批",660,850, "recordChangeForm", "edit", recordAccountDataGrid,urls.recordAuditInitUrl+cs, urls.operateRecordInfo,true,formIsEdit,isBackToFirstNode);
		}
	}, "json");
	
	
};
/**
 * 审批弹出窗口
 */
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
			
			if(operType=="edit" || operType=="add"){
				//该属性不能放在load之后，因为其中有隐藏域
				mdDialog.find("#bank").treeDialog({
					title :'选择开户行',
					dialogWidth : 420,
					dialogHeight : 500,
					hiddenName:'bankCode',
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
			}
			
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + formid);
			f.form("load", row);
			//赋值
			mdDialog.find("#phonenumber").textbox("setValue",row.acctPhonenumber);
			
			if(row.type=="1"){
				//mdDialog.find("#typeName").textbox("setValue","账户开立");
				mdDialog.find("#changetypetype").remove();
				mdDialog.find("#otherFileTr").children("th").html("开立附件");
			}else if(row.type=="2"){
				//mdDialog.find("#typeName").textbox("setValue","账户变更");
				mdDialog.find("#changetypetype").children("th").html("变更事项");
				mdDialog.find("#otherFileTr").children("th").html("变更附件");
			}else if(row.type=="3"){
				//mdDialog.find("#typeName").textbox("setValue","账户注销");
				mdDialog.find("#changetypetype").children("th").html("注销事项");
				mdDialog.find("#otherFileTr").children("th").html("注销附件");
				//注销的时候不可编辑
				mdDialog.find("#accountNumber").textbox("readonly",true);
				mdDialog.find("#openTime").datebox("readonly",true);
			}
			
			if(operType=="edit" || operType=="add"){
				
				if(formIsEdit==true){
					showFileDiv(mdDialog.find("#filetd"),true,"FAACCOUNT",row.accountId,"");
				}else{
					showFileDiv(mdDialog.find("#filetd"),false,"FAACCOUNT",row.accountId,"");
				}
				
				
			}else{
				if(row.iszero=="0"){
					mdDialog.find("#iszero").textbox("setValue","否");
				}else{
					mdDialog.find("#iszero").textbox("setValue","是");
				}
				
				if(row.oldIszero=="0"){
					mdDialog.find("#oldIszero").textbox("setValue","否");
				}else{
					mdDialog.find("#oldIszero").textbox("setValue","是");
				}
				
				//新账户信息附件
				//showFileTable(mdDialog.find("#fileTable2"),false,"FAACCOUNT",row.accountId,"");
				showFileDiv(mdDialog.find("#filetd"),false,"FAACCOUNT",row.accountId,"");
			}
			
			//备案账户信息附件
			//showFileTable(mdDialog.find("#fileTable1"),false,"FAAPPLICATION",row.applicationId,"");
			showFileDiv(mdDialog.find("#otherFiletd"),false,"FAAPPLICATION",row.applicationId,"");
		},
		buttons : funcOperButtons(operType, url, dataGrid,formid,formIsEdit,isBackToFirstNode)
	});
}
/**
 * 加载按钮
 * @param operType
 * @param url
 * @param dataGrid
 * @param formid
 * @returns
 */
function funcOperButtons(operType, url, dataGrid,formid,formIsEdit,isBackToFirstNode) {
	var buttons;
	if (operType == "view") {
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == "edit" && formIsEdit==true) {
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+formid).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#acctWfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+$("#activityId").val(),formid,1,wfid,activityId,applicationId,"1");
//				submitForm(url+"?isback=&activityId="+$("#activityId").val(), formid,operType,"确认同意该数据？");
			}
		}, {
			text : "退回",
			iconCls : "icon-undo",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+formid).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#acctWfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+$("#activityId").val(),formid,2,wfid,activityId,applicationId,"1");
				//submitForm(url+"?isback=1&activityId="+$("#activityId").val(), formid,operType,"确认退回该数据？");
			}
		},  {
			text : "短信服务",
			iconCls : "icon-message",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var savephonenumbers = parent.$.modalDialog.handler.find("#savephonenumbers").val();
				var savemessage = parent.$.modalDialog.handler.find("#savemessage").val();
				var phonenumber = dataGrid.datagrid("getSelections")[0].applPhonenumber;
				//如果在临时保存数据里没有值，则取业务数据里的手机号，都则就取临时数据里的手机号
				if(savephonenumbers==null || savephonenumbers==""){
					showMessageModalDialog(parent.$('#messageService'),phonenumber,"",$("#activityId").val());
				}else{
					showMessageModalDialog(parent.$('#messageService'),savephonenumbers,savemessage,$("#activityId").val());
				}
			}
		},{
			text : "原账户信息",
			iconCls : "icon-view",
			handler : function() {
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				if(applicationId==""){
					parent.$.messager.alert('错误', "查询原账户信息失败或原账户信息不存在", 'error');
					return;
				}
				showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
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
					var isValid = parent.$.modalDialog.handler.find("#"+formid).form('validate');
					if (!isValid) {
						return;
					}
					var wfid = parent.$.modalDialog.handler.find("#acctWfid").val();
					var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
					
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=2&activityId="+$("#activityId").val(),formid,3,wfid,activityId,applicationId,"1");
					
					//submitForm(url+"?isback=2&activityId="+$("#activityId").val(), formid,operType,"确认将该数据退回至首节点？");
				}
			});
		}
		
	}else{
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+formid).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#acctWfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=&activityId="+$("#activityId").val(),formid,1,wfid,activityId,applicationId,"1");
				
				//submitForm(url+"?isback=&activityId="+$("#activityId").val(), formid,operType,"确认同意该数据？");
			}
		}, {
			text : "退回",
			iconCls : "icon-undo",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+formid).form('validate');
				if (!isValid) {
					return;
				}
				var wfid = parent.$.modalDialog.handler.find("#acctWfid").val();
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				
				showOperationDialog(parent.$('#operationdiv'),url+"?isback=1&activityId="+$("#activityId").val(),formid,2,wfid,activityId,applicationId,"1");
				
				//submitForm(url+"?isback=1&activityId="+$("#activityId").val(), formid,operType,"确认退回该数据？");
			}
		},  {
			text : "短信服务",
			iconCls : "icon-message",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var savephonenumbers = parent.$.modalDialog.handler.find("#savephonenumbers").val();
				var savemessage = parent.$.modalDialog.handler.find("#savemessage").val();
				var phonenumber = dataGrid.datagrid("getSelections")[0].applPhonenumber;
				//如果在临时保存数据里没有值，则取业务数据里的手机号，都则就取临时数据里的手机号
				if(savephonenumbers==null || savephonenumbers==""){
					showMessageModalDialog(parent.$('#messageService'),phonenumber,"",$("#activityId").val());
				}else{
					showMessageModalDialog(parent.$('#messageService'),savephonenumbers,savemessage,$("#activityId").val());
				}
			}
		},{
			text : "原账户信息",
			iconCls : "icon-view",
			handler : function() {
				var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
				if(applicationId==""){
					parent.$.messager.alert('错误', "查询原账户信息失败或原账户信息不存在", 'error');
					return;
				}
				showAccountDetail(parent.$('#accountDetail'),applicationId,"1");
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
			buttons.splice(2, 0, {
				text : "退回首节点",
				iconCls : "icon-undo",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					
					var isValid = parent.$.modalDialog.handler.find("#"+formid).form('validate');
					if (!isValid) {
						return;
					}
					var wfid = parent.$.modalDialog.handler.find("#acctWfid").val();
					var applicationId = parent.$.modalDialog.handler.find("#applicationId").val();
					
					showOperationDialog(parent.$('#operationdiv'),url+"?isback=2&activityId="+$("#activityId").val(),formid,1,wfid,activityId,applicationId,"1");
					
					//submitForm(url+"?isback=2&activityId="+$("#activityId").val(), formid,operType,"确认将该数据退回至首节点？");
				}
			});
		}
	}
	return buttons;
}
/**
 * 提交表单
 * @param url
 * @param form
 * @param workflowflag
 */
function submitForm(url, form,workflowflag,message) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
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
						recordAccountDataGrid.datagrid('reload');
						parent.$.modalDialog.handler.dialog('close');
					} else {
						parent.$.messager.alert('错误', result.title, 'error');
					}
				}
			});
		}
	});
}
//可变更列表查询
function downGridQuery(){
	
		var param = {
				bdgagencycode : $("#topBdgagency").treeDialog('getValue'),
				processedStatus : $("#processedStatus").combobox("getValues").join(","),//处理类型
				status : $("#dealStatus").combobox('getValue'),//已处理未处理
				starttime : $('#starttime').datetimebox('getValue'),
				endtime :  $('#endtime').datetimebox('getValue'),
				menuid : $('#menuid').val(),//菜单id
				activityId : $('#activityId').val(),//节点id
				firstNode : $('#firstNode').val(),
				lastNode : $('#lastNode').val(),
				type : $("#type").combobox("getValues").join(","),//备案类型
				accountName : $("#topAccountName").val()//账户名称
			};
		$("#recordAccountDataGrid").datagrid("load",param);
}
/**
 * 撤回
 */
function removeRecordInfo(){
	var rows = recordAccountDataGrid.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一行数据");	
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
	parent.$.messager.confirm("确认撤销", "是否确认撤销这"+rows.length+"条数据记录？", function(r) {
		if (r) {
			$.post(urls.removeRecordInfo, {
				applicationIds : applicationIds,
				menuid : $("#menuid").val(),
				activityId :$("#activityId").val(),
				isba : 1//备案撤销标识
			}, function(result) {
				easyui_auto_notice(result, function() {
					recordAccountDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
};


//查询工作流信息
function workflowMessage(){
	
	var selectRow = recordAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].acctWfid);
}

/**
 * 批量审批同意、退回
 */
function batOperateRecordInfo(flag){
	var selectRow = recordAccountDataGrid.datagrid('getChecked');
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
	var menuid = $("#menuid").val();
	var activityId= $("#activityId").val();
	
	showAuditModalDialog(parent.$,recordAccountDataGrid,text,menuid,activityId,applicationIds,isback,"1");
	
}
//点击左边菜单刷新右边
function showReload() {
	recordAccountDataGrid.datagrid('reload');
}

/**
 * 备案申请表
 */
function outRecordAppicationForm(){
	var selectRow = recordAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length<1){
		easyui_warn("请至少选择一条记录！",null);
		return;
	}
	var applicationIds ="";
	for(var i=0;i<selectRow.length;i++){
		applicationIds +=selectRow[i].applicationId+",";
	}
	window.location.href=urls.outRecordAppicationForm+"?applicationIds="+applicationIds+"&type=";
}
