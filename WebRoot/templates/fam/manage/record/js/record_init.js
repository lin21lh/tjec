
var baseUrl = contextpath + "manage/record/AccountRecordController/";
var urls = {
	recordAccountDataGridUrl : baseUrl + "queryChangeAccount.do",
	AccountDataGridUrl : baseUrl + "queryAccount.do",
	recordAddInitUrl : baseUrl + "recordAddInit.do",
	recordSaveAddUrl : baseUrl + "recordSaveAdd.do",
	removeRecordInfo :  baseUrl + "removeRecordInfo.do",
	submitRecordInfo : baseUrl + "submitRecordInfo.do",
	//导出变更申请表
	outRecordAppicationForm : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRecordAppicationForm.do"
};
//默认加载
$(function() {
	//加载grid
	loadChangeGrid(urls.recordAccountDataGridUrl);
	loadAccountGrid(urls.AccountDataGridUrl);
	//top
	$('#topBdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'topbdgagencyHidden',
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
	
	$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			topGridQuery();
			switch (record.value) {
			case '1':
				$('#btn_query').linkbutton('enable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_submit').linkbutton('enable');
				$('#btn_flow').linkbutton('enable');
				break;
			case '2':
				$('#btn_query').linkbutton('enable');
				$('#btn_edit').linkbutton('disable');
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
			topGridQuery();
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
	//down
	$('#downBdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'downbdgagencyHidden',
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
	comboboxFuncByCondFilter(menuid,"downAccountType", "ACCTTYPE", "code", "name");//账户类型
	$("#downAccountType").combobox("addClearBtn", {iconCls:"icon-clear"});
});
//已变更账户grid
var recordAccountDataGrid;
//可备案历史表
var accountDataGrid;
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
		queryParams: {
			status : 1,
			processedStatus : "",
			type : "",//备案类型
			menuid : $('#menuid').val(),//菜单id
			activityId : $('#activityId').val(),//节点id
			firstNode : $('#firstNode').val(),
			lastNode : $('#lastNode').val()
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
					,{field : "type",title : "备案类型",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2,hidden:true}
					,{field : "typeName",title : "备案类型",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2}
					,{field : "applyReason",title : "变更原因",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	}
					,{field : "wfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "acctWfstatus",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
					,{field : "acctWfstatusName",title : "当前状态",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	}
					,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "changetype",title : "事项",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
					,{field : "changetypeName",title : "事项",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
                    ,{title:'原账户信息',colspan:10}
                    ,{title:'新账户信息',colspan:18}
                    ,{field : "deptAddress",title : "单位地址",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2	}
                    ,{field : "wfid",title : "工作流id",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2,hidden:true}
                    ,{field : "acctWfid",title : "账户表工作流id",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2,hidden:true}
  	                ,{field : "applPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "acctPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
  	                ,{field : "remark",title : "备注",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "accountId",title : "账户id",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true	}
  	                ,{field : "createUser",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2,hidden:true		}
	                ,{field : "createUserName",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
	                ,{field : "createTime",title : "创建时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
	                ,{field : "updateUser",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2,hidden:true		}
	                ,{field : "updateUserName",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true	}
	                ,{field : "updateTime",title : "修改时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,hidden:true	}
  	                ,{field : "modifyUser",title : "备案人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true	}
  	                ,{field : "modifyUserName",title : "备案人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "modifyTime",title : "备案时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2			}
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
	              ,{field : "oldLegalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true	}
	              ,{field : "oldIdcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true	}
	              ,{field : "oldFinancialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
	              ,{field : "oldAccountContent",title : "核算内容",halign : 'center',	width:120,sortable:true		}
	              
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
	              ,{field : "legalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true	}
	              ,{field : "idcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true	}
	              ,{field : "financialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
	              ,{field : "accountContent",title : "核算内容",halign : 'center',	width:120,sortable:true		}
	              ]]
	});
}
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
		url : url,
		queryParams: {
			//status : 1,
			type : "",//备案类型
			menuid : $('#menuid').val(),//菜单id
			activityId : $('#activityId').val(),//节点id
			firstNode : $('#firstNode').val(),
			lastNode : $('#lastNode').val()
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_account",
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
					,{field : "wfstatusName",title : "当前状态",halign : 'center',	width:80,sortable:true,align:'left', rowspan:2	,hidden:true}
                    ,{title:'原账户信息',colspan:10}
                    ,{title:'新账户信息',colspan:18}
                    ,{field : "deptAddress",title : "单位地址",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2	}
                    ,{field : "wfid",title : "工作流id",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2,hidden:true}
                    ,{field : "acctWfid",title : "账户表工作流id",halign : 'center',	width:150,sortable:true	,align:'left', rowspan:2}
  	                ,{field : "applPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "acctPhonenumber",title : "电话号码",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		,hidden:true}
  	                ,{field : "remark",title : "备注",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "createUser",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,	hidden:true}
  	                ,{field : "createUserName",title : "创建人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "createTime",title : "创建时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "updateUser",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2	,	hidden:true	}
  	                ,{field : "updateUserName",title : "修改人",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
  	                ,{field : "updateTime",title : "修改时间",halign : 'center',	width:150,sortable:true,align:'left', rowspan:2		}
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
	              ,{field : "oldLegalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true	}
	              ,{field : "oldIdcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true	}
	              ,{field : "oldFinancialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
	              ,{field : "oldAccountContent",title : "核算内容",halign : 'center',	width:120,sortable:true		}
	              
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
	              ,{field : "legalPerson",title : "法人名称",halign : 'center',	width:120,sortable:true	}
	              ,{field : "idcardno",title : "法人身份证号",halign : 'center',	width:120,sortable:true	}
	              ,{field : "financialOfficer",title : "财务负责人",halign : 'center',	width:120,sortable:true		}
	              ,{field : "accountContent",title : "核算内容",halign : 'center',	width:120,sortable:true		}
                ]]
	});
}
/**
 * 详情
 */
function recordInitView(){
	var selectRow = recordAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showModalDialog("账户备案",560,850,"recordChangeForm","view",recordAccountDataGrid,urls.recordAddInitUrl+"?optType=view",urls.recordSaveAddUrl,true);
}
/**
 * 修改
 */
function recordInitEdit(){
	var selectRow = recordAccountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if(selectRow[0].createUser != userid){
		easyui_warn("无权限修改其他人录入的数据！");
		return;
	}
	showModalDialog("账户备案",560,850,"recordChangeForm","edit",recordAccountDataGrid,urls.recordAddInitUrl+"?optType=edit",urls.recordSaveAddUrl,true);
}
/**
 * 新增按钮
 */
function recordInit(){
	var selectRow = accountDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if(selectRow[0].createUser != userid){
		easyui_warn("无权限修改其他人录入的数据！");
		return;
	}
	showModalDialog("账户备案",560,850,"recordChangeForm","add",accountDataGrid,urls.recordAddInitUrl+"?optType=add",urls.recordSaveAddUrl,true);
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
			
			if(operType=="edit" || operType=="add"){
				//该属性不能放在load之后，因为其中有隐藏域
				mdDialog.find("#bank").treeDialog({
					title :'选择开户行',
					dialogWidth : 420,
					dialogHeight : 500,
					hiddenName:'bankCode',
					prompt: "请选择开户行",
					multiSelect: false, //单选树
					queryParams : {
						menuid : menuid
					},
					url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
					dblClickRow: true,
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
				//comboboxFuncByCondFilter(menuid,"changeType", "CHANGETYPE", "code", "name", mdDialog);//变更事项
			}else if(row.type=="3"){
				//mdDialog.find("#typeName").textbox("setValue","账户注销");
				mdDialog.find("#changetypetype").children("th").html("注销事项");
				mdDialog.find("#otherFileTr").children("th").html("注销附件");
				//注销的时候不可编辑
				mdDialog.find("#accountNumber").textbox("readonly",true);
				mdDialog.find("#openTime").datebox("readonly",true);
				//comboboxFuncByCondFilter(menuid,"changeType", "REVOKETYPE", "code", "name", mdDialog);//注销事项
			}
			
			if(operType=="edit" || operType=="add"){
				
				showFileDiv(mdDialog.find("#filetd"),true,"FAACCOUNT",row.accountId,"");
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
				showFileDiv(mdDialog.find("#filetd"),false,"FAACCOUNT",row.accountId,"");
				//showFileTable(mdDialog.find("#fileTable2"),false,"FAACCOUNT",row.accountId,"");
			}
			
			
			//备案账户信息附件
			showFileDiv(mdDialog.find("#otherFiletd"),false,"FAAPPLICATION",row.applicationId,"");
			
		},
		buttons : funcOperButtons(operType, url, dataGrid,formid)
	});
}
function funcOperButtons(operType, url, dataGrid,formid) {
	var buttons;
	if (operType == "view") {
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == "edit" || operType == "add") {
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				submitForm(url+"?isSub=0&menuid="+$("#menuid").val()+"&optType="+operType+"&activityId="+activityId, formid,"");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				
				submitForm(url+"?isSub=1&menuid="+$("#menuid").val()+"&optType="+operType+"&activityId="+activityId, formid,"1");
			}
		},{
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAACCOUNT");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	return buttons;
}
function submitForm(url, form,workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
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
						parent.$.modalDialog.handler.dialog('close');
						recordAccountDataGrid.datagrid('reload');
						accountDataGrid.datagrid('reload');
					} else {
						parent.$.messager.alert('错误', result.title, 'error');
					}
				}
			});
}
//可变更列表查询
function topGridQuery(){
	var param = {
			bdgagencycode : $("#topBdgagency").treeDialog('getValue'),
			processedStatus : $("#processedStatus").combobox("getValues").join(","),//处理类型
			status : $("#dealStatus").combobox('getValue'),//已处理未处理
			type : $("#type").combobox("getValues").join(","),//备案类型
			accountName : $("#topAccountName").val(),//账户名称
			starttime : $('#starttime').datetimebox('getValue'),
			endtime :  $('#endtime').datetimebox('getValue'),
			menuid : $('#menuid').val(),//菜单id
			activityId : $('#activityId').val(),//节点id
			firstNode : $('#firstNode').val(),
			lastNode : $('#lastNode').val()
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
			$.post(urls.removeRecordInfo, {
				applicationIds : applicationIds,
				menuid : $("#menuid").val(),
				activityId :$("#activityId").val()
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
function downGridQuery(){
	var param = {
			bdgagencycode : $("#downBdgagency").treeDialog('getValue'),
			type : $("#downtype").combobox("getValues").join(","),//备案类型
			accountName : $("#downAccountName").val(),//账户名称
			accountType : $("#downAccountType").combobox('getValue'),//账户名称
			menuid : $('#menuid').val(),//菜单id
			activityId : $('#activityId').val(),//节点id
			firstNode : $('#firstNode').val(),
			lastNode : $('#lastNode').val()
		};
	$("#accountDataGrid").datagrid("load",param);
}

/**
 * 送审
 */
function submitRecordInfo(){
	var rows = recordAccountDataGrid.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一行数据");	
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
			$.post(urls.submitRecordInfo, {
				menuid : $("#menuid").val(),
				activityId : activityId,
				applicationIds : applicationIds
			}, function(result) {
				easyui_auto_notice(result, function() {
					recordAccountDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
};

//点击左边菜单刷新右边
function showReload() {
	recordAccountDataGrid.datagrid('reload');
	accountDataGrid.datagrid('reload');
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
