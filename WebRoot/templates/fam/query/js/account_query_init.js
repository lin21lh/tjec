/**
 * account_query_init.js
 */
var baseUrl = contextpath + "query/account/controller/AccountQueryController/";
//路径
var urls = {
	queryAccount : baseUrl+"queryAccount.do",
	queryAccountHis : baseUrl+"queryAccountHis.do",
	queryApplicationInfo : baseUrl+"queryApplicationInfo.do",
	getAccountForm : baseUrl +"getAccountForm.do"
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};
//零余额 
var iszero={
	"0" : "否",
	"1" : "是"
};
//账户状态（是否撤销）
var account_status = {
		"1" : "正常",
		"9" : "撤销"
};
//折叠
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#datagridAccount", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
var datagridAccount;
$(function(){
	loaddatagridAccount(urls.queryAccount);
	$('#bdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'bdgagencyHidden',
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
	//开户行
	$("#bank").treeDialog({
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
	comboboxFuncByCondFilter(menuid,"status", "ACCOUNTSTATUS", "code", "name");//账户状态
	$("#status").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#accountNameSearch").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#accountNumberSearch").combobox("addClearBtn", {iconCls:"icon-clear"});
});
function loaddatagridAccount(url){
	datagridAccount = $("#datagridAccount").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		queryParams : {
			menuid : menuid
		},
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_account",
		showFooter : true,
		columns : [ [ {field : "itemid",checkbox : true	}
		, {field : "elementcode",title : "数据项编码",halign : 'center',width : 120,sortable:true,hidden:true}
		, {field : "bdgagencycn",title : "预算单位",halign : 'center',	width : 230,sortable:true}
		, {field : "bdgagency",title : "预算单位ID",halign : 'center',	width : 120,sortable:true,hidden:true}
		, {field : "bdgagencycode",title : "预算单位编码",halign : 'center',width : 80,	sortable:true,hidden:true}
		, {field : "bdgagencyname",title : "预算单位名称",halign : 'center',width : 120,sortable:true,hidden:true}
		, {field : "deptNature",title : "单位性质",halign : 'center',width : 80,sortable:true,hidden:true	} 
		, {field : "deptNatureName",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true}
		, {field : "status",title : "状态",	halign : 'center',width : 50,sortable:true,hidden:true}
		, {field : "accountName",title : "账户名称",halign : 'center',width : 220,sortable:true}
		, {field : "accountNumber",title : "银行账号",	halign : 'center',width : 170,sortable:true}
		, {field : "bankid",title : "开户银行ID",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "bankCode",title : "开户银行编码",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "bankName",title : "开户银行名称",halign : 'center',width : 180,sortable:true,hidden:true}
		, {field : "bankNameCn",title : "开户银行名称",halign : 'center',width : 230,sortable:true}
		, {field : "accountType",title : "账户类型",halign : 'center',	width : 120,sortable:true,hidden:true}
		, {field : "accountTypeName",title : "账户类型",halign : 'center',	width : 120,sortable:true}
		, {field : "type",title : "备案类型",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "type01",title : "预留",	halign : 'center',width : 120,	sortable:true,hidden:true}
		, {field : "type02",title : "账户性质",	halign : 'center',width : 80,sortable:true,hidden:true}
		,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
		, {field : "iszeroName",title : "零余额账户",halign : 'center',width : 80,sortable:true} 
		, {field : "statusName",title : "状态",	halign : 'center',width : 50,sortable:true}
		, {field : "iszero",title : "零余额账户",	halign : 'center',width : 50,sortable:true,hidden:true}
		
		, {field : "deptAddress",title : "单位地址",halign : 'center',width : 120,sortable:true}
		, {field : "linkman",title : "联系人",halign : 'center',width : 120,sortable:true}
		, {field : "phonenumber",title : "联系人电话",halign : 'center',width : 120,sortable:true}
		, {field : "remark",title : "注释",halign : 'center',width : 80,	sortable:true}
		, {field : "startdate",title : "启用日期",halign : 'center',width : 80,	sortable:true,hidden:true}
		, {field : "enddate",title : "停用日期",	halign : 'center',	width : 120,sortable:true,hidden:true}
		, {field : "createUser",title : "创建人",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "createUserName",title : "创建人",halign : 'center',width : 80,sortable:true}
		, {field : "createTime",title : "创建时间",	halign : 'center',width : 120,sortable:true}
		, {field : "updateUser",title : "修改人",halign : 'center',width : 80,sortable:true,hidden:true}
		, {field : "updateUserName",title : "修改人",halign : 'center',width : 80,sortable:true}
		, {field : "updateTime",title : "修改时间",	halign : 'center',width : 120,sortable:true}
		, {field : "ischange",title : "是否变更、撤销",halign : 'center',width : 120,sortable:true,hidden:true}
		, {field : "applicationId",title : "申请序号",halign : 'center',width : 80,sortable:true	,hidden:true} 
		] ]
	});
};
/**
 * 查询
 */
function queryAccountInfo() {
	$("#datagridAccount").datagrid("load", {
		menuid : menuid,
		bdgagencycode : $("#bdgagency").treeDialog('getValue'),
		bankCode : $("#bank").treeDialog('getValue'),
		accountName : $("#accountNameSearch").val(),
		accountNumber : $("#accountNumberSearch").val(),
		status : $("#status").combobox('getValue')
	});
}

function out_excel(includeCurrentPage) {
	var content = '<tr>';
	content += '<th>导出类型：</th>';
	content += '<td style="height:30px">';
	content += '<select id="outExcelType" class="easyui-combobox" required="true" data-options="panelHeight:\'auto\'" value="1" missingMessage="请选择导出类型" name="outExcelType" style="width:180px">';
		content += '<option value="1">导出选中数据</option>';
		if (includeCurrentPage) {
			content += '<option value="2">导出本页</option>';
		}
		content += '<option value="3">导出所有</option>';
		content += '<option value="4">导出全部</option>';
	content += '</select>';
	content += '</td></tr><tr style="height:30px">';
	content += '<th>导出版本：</th>';
	content += '<td>';
	content += '<select id="outExcelVersion" class="easyui-combobox" required="true" data-options="panelHeight:\'auto\'" value="1" missingMessage="请选择导出Excel版本" name="outExcelVersion" style="width:180px">';
	content += '<option value="2003">Excel97-2003工作薄(*.xls)</option>';
	content += '<option value="2007">Excel工作薄(*.xlsx)</option>';
content += '</select>';
	content += '</td></tr>';
	parent.$.modalDialog({
		title : '导出Excel',
		iconCls : 'icon-excel',
		width : 300,
		height : 140,
		content : content,
		onLoad : function() {
		},
		buttons : [{
			text:'导出',
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var outExcelType = mdDialog.find('#outExcelType').combobox('getValue');
				var outExcelVersion = mdDialog.find('#outExcelVersion').combobox('getValue');
				outExcelAccount(outExcelType, outExcelVersion);
			}
		}, {
			text:'关闭',
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
	});
}

/**
 * 导出excel
 */
function outExcelAccount(outExcelModel, outExcelVersion) {
	switch (outExcelModel) {
	case '1': //导出选中行
		var paramJSON = {
			includeHidden : false,
			title : 'account',
			excelVersion: outExcelVersion,
			outExcelModel : outExcelModel
		};
		datagridAccount.datagrid("outExcel",paramJSON);
		break;
	case '2': //当前页（不分页方式，此项选择无）
		var paramJSON = {
			includeHidden : false,
			title : 'account',
			excelVersion: outExcelVersion,
			outExcelModel : outExcelModel
		};
		datagridAccount.datagrid("outExcel",paramJSON);
		break;
	case '3': //导出所有（现条件下所有包含数据权限范围内）
		var paramJSON = {};
		
		paramJSON.filename = 'outAccountExcel';
		paramJSON.excelVersion = outExcelVersion;
		paramJSON.bdgagencycode = $("#bdgagency").treeDialog('getValue');
		paramJSON.bankCode = $("#bank").treeDialog('getValue');
		paramJSON.accountName = $("#accountNameSearch").val();
		paramJSON.menuid = menuid;
		paramJSON.accountNumber = $("#accountNumberSearch").val();
		paramJSON.status = $("#status").combobox('getValue');
		paramJSON.outExcelModel = outExcelModel;
		
		outExcel('', paramJSON);
		break;
	case '4': //导出全部（权限范围内全部）
		var paramJSON = {};
		
		paramJSON.filename = 'outAccountExcel';
		paramJSON.excelVersion = outExcelVersion;
		paramJSON.bdgagencycode = $("#bdgagency").treeDialog('getValue');
		paramJSON.bankCode = $("#bank").treeDialog('getValue');
		paramJSON.accountName = $("#accountNameSearch").val();
		paramJSON.menuid = menuid;
		paramJSON.accountNumber = $("#accountNumberSearch").val();
		paramJSON.status = $("#status").combobox('getValue');
		paramJSON.outExcelModel = outExcelModel;
		
		outExcel('', paramJSON);
		break;
	default:
		break;
	}
};
/**
 * 变更历史
 */
function initAccountHis() {
	var rows = datagridAccount.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	
	showModalDialog(1000,550, "account_query_init", types.view, datagridAccount, "变更历史",
			baseUrl+"accountHisView.do?type=" + types.view+"&menuid="+menuid, urls.queryAccountHis, true);
};
/**
 * 在途变更
 */
function onWayAccount() {
	var rows = datagridAccount.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	var applicationId= rows[0].applicationId;
	if(applicationId==''||applicationId==null){
		easyui_warn("该数据没有在途变更数据！");
		return;
	}
	showModalDialogForOnWay(850,550, "ApplicationViewForm", types.view, "", "在途变更",
			baseUrl+"loadApplicationView.do?applicationId="+applicationId, urls.queryApplicationInfo, true,applicationId);
};
function showModalDialogForOnWay(width,height, form, operType, dataGrid, title, href, url,
		fill,applicationId) {
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : width,
		height : height,
		href : href,
		modal:true,
		resizable: true,
		onLoad : function() {
			$.post(urls.getAccountForm, {
				applicationId : applicationId
			}, function(result) {
				var mdDialog = parent.$.modalDialog.handler;
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", result);
				showFileTable(mdDialog.find("#fileTable"),false,"FAAPPLICATION",applicationId,"");
			}, "json");
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
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
function showModalDialog(width,height, form, operType, dataGrid, title, href, url,
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
		width : width,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			parent.$.modalDialog.openner_dataGrid = dataGrid;
			mdDialog.find("#menuid").val(menuid);
			mdDialog.find("#allbdgagency").val(allbdgagency);
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
			mdDialog.find("#datagridHisAccount").datagrid({
				fit : true,
				stripe : true,
				singleSelect :false,
				rownumbers : true,
				pagination : true,
				remoteSort:false,
				multiSort:true,
				pageSize : 30,
				url : url+"?itemid="+rows[0].itemid+"&menuid="+menuid,
				loadMsg : "正在加载，请稍候......",
				toolbar : "#toolbar_account",
				showFooter : true,
				columns : [ [ {field : "itemid",checkbox : true	}
				, {field : "elementcode",title : "数据项编码",halign : 'center',width : 120,sortable:true,hidden:true}
				, {field : "bdgagencycn",title : "预算单位",halign : 'center',	width : 230,sortable:true}
				, {field : "bdgagency",title : "预算单位ID",halign : 'center',	width : 120,sortable:true,hidden:true}
				, {field : "bdgagencycode",title : "预算单位编码",halign : 'center',width : 80,	sortable:true,hidden:true}
				, {field : "bdgagencyname",title : "预算单位名称",halign : 'center',width : 120,sortable:true,hidden:true}
				, {field : "deptNature",title : "单位性质",halign : 'center',width : 80,sortable:true,hidden:true	} 
				, {field : "deptNatureName",title : "单位性质",halign : 'center',fixed : true,	width : 100,sortable:true}
				, {field : "accountName",title : "账户名称",halign : 'center',width : 220,sortable:true}
				, {field : "accountNumber",title : "银行账号",	halign : 'center',width : 170,sortable:true}
				, {field : "bankid",title : "开户银行ID",halign : 'center',width : 80,sortable:true,hidden:true}
				, {field : "bankCode",title : "开户银行编码",halign : 'center',width : 80,sortable:true,hidden:true}
				, {field : "bankName",title : "开户银行名称",halign : 'center',width : 180,sortable:true,hidden:true}
				, {field : "bankNameCn",title : "开户银行名称",halign : 'center',width : 230,sortable:true}
				, {field : "accountType",title : "账户类型",halign : 'center',	width : 120,sortable:true,hidden:true}
				, {field : "accountTypeName",title : "账户类型",halign : 'center',	width : 120,sortable:true}
				, {field : "type",title : "备案类型",halign : 'center',width : 80,sortable:true,hidden:true}
				, {field : "type01",title : "预留",	halign : 'center',width : 120,	sortable:true,hidden:true}
				, {field : "type02",title : "账户性质",	halign : 'center',width : 80,sortable:true,hidden:true}
				,{field : "type02Name",title : "账户性质",halign : 'center',	width:150,sortable:true	}
				, {field : "iszeroName",title : "零余额账户",halign : 'center',width : 80,sortable:true} 
				, {field : "statusName",title : "状态",	halign : 'center',width : 50,sortable:true}
				, {field : "iszero",title : "零余额账户",	halign : 'center',width : 50,sortable:true,hidden:true}
				, {field : "status",title : "状态",	halign : 'center',width : 50,sortable:true,hidden:true}
				, {field : "deptAddress",title : "单位地址",halign : 'center',width : 120,sortable:true}
				, {field : "linkman",title : "联系人",halign : 'center',width : 120,sortable:true}
				, {field : "phonenumber",title : "联系人电话",halign : 'center',width : 120,sortable:true}
				, {field : "remark",title : "注释",halign : 'center',width : 80,	sortable:true}
				, {field : "startdate",title : "启用日期",halign : 'center',width : 80,	sortable:true,hidden:true}
				, {field : "enddate",title : "停用日期",	halign : 'center',	width : 120,sortable:true,hidden:true}
				, {field : "createUser",title : "创建人",halign : 'center',width : 80,sortable:true,hidden:true}
				, {field : "createUserName",title : "创建人",halign : 'center',width : 80,sortable:true,hidden:true}
				, {field : "createTime",title : "创建时间",	halign : 'center',width : 120,sortable:true,hidden:true}
				, {field : "updateUser",title : "修改人",halign : 'center',width : 80,sortable:true,hidden:true}
				, {field : "updateUserName",title : "变更人",halign : 'center',width : 80,sortable:true}
				, {field : "updateTime",title : "变更时间",	halign : 'center',width : 120,sortable:true}
				, {field : "ischange",title : "是否变更、撤销",halign : 'center',width : 120,sortable:true,hidden:true}
				, {field : "applicationId",title : "申请序号",halign : 'center',width : 80,sortable:true	,hidden:true} 
				] ]
			});
			
			}
	});
};
