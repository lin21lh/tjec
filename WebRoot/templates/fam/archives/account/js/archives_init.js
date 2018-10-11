/**
 * account_query_init.js
 */
var baseUrl = contextpath + "archives/account/controller/AccountArchivesController/";
//路径
var urls = {
		queryAccountArchives : baseUrl+"queryAccountArchives.do",
		targetFileInfoForm : baseUrl+"targetFileInfoForm.do",
		saveFileInfo : baseUrl+"saveFileInfo.do",
		optFile : contextpath +"/base/filemanage/fileManageController/entry.do"
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
var datagridArchives;
$(function(){
	loaddatagridArchives(urls.queryAccountArchives);
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
});
function loaddatagridArchives(url){
	datagridArchives = $("#datagridArchives").datagrid({
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
		toolbar : "#toolbar_archives",
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
		, {field : "appids",title : "关联到的applicationid",halign : 'center',width : 80,sortable:true	,hidden:true} 
		, {field : "acctids",title : "关联到的accountid",halign : 'center',width : 80,sortable:true	,hidden:true} 
		] ]
	});
};
/**
 * 查询
 */
function queryArchivesInfo() {
	$("#datagridArchives").datagrid("load", {
		menuid : menuid,
		bdgagencycode : $("#bdgagency").treeDialog('getValue'),
		accountName : $("#accountNameSearch").textbox('getValue'),
		accountNumber : $("#accountNumberSearch").textbox('getValue')
		
	});
}

/**
 * 附件详情
 */
function queryFileInfo() {
	var rows = datagridArchives.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	var applicationId= rows[0].itemid;
	showModalDialog(400, "archives_files", "view", datagridArchives, "附件详情", urls.targetFileInfoForm+"?optType=view", urls.saveFileInfo,true);
};

/**
 * 附件管理
 */
//function optFile() {
//	var rows = datagridArchives.datagrid("getSelections");
//	if (rows.length != 1) {
//		easyui_warn("请选择一条数据！");
//		return;
//	}
//	var keyid= rows[0].itemid;
//	var elementcode = "FAARCHIVES";
//	parent.$.modalDialog({
//		title : "附件管理",
//		iconCls : "edit",
//		width : 600,
//		height : 400,
//		href : urls.optFile+"?keyid="+keyid+"&elementcode="+elementcode,
//		onLoad : function() {
//				
//		}
//	});
//};

function optFile() {
	
	var rows = datagridArchives.datagrid("getSelections");
	if(rows.length == 0){
		easyui_warn("请选择一条数据！");
		return;
	} else if (rows.length > 1) {
		easyui_warn("当且仅能选择一条数据！");
		return;
	}
	var keyid = rows[0].itemid;
	parent.$.modalDialog({
		title : '附件管理',
		width : 650,
		height : 380,
		href : contextpath + '/base/filemanage/fileManageController/entry.do?keyid=' + keyid + '&elementcode=FAARCHIVES',
		onBeforOpen: function(){
			console.log("open");
		},
		onLoad: function(){

		}
	});
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
function showModalDialog(height, form, operType, dataGrid, title, href, url,fill) {

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
		width : 750,
		height : height,
		href : href,
		onLoad : function() {
			
			var mdDialog = parent.$.modalDialog.handler;
			
			if (fill) {
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				var appids = row.appids;
				var acctids = row.acctids;
				var itemid = row.itemid;
				if(appids!="" && appids!=null && appids!="null"){
					var strs= new Array(); //定义一数组 
					strs=appids.split(","); //字符分割 
					for(var i=0;i<strs.length;i++){
						showFileDiv(mdDialog.find("#filetd1"),false,"FAAPPLICATION",strs[i],"");
					}
				}
				if(acctids!="" && acctids!=null && acctids!="null"){
					var strs= new Array(); //定义一数组 
					strs=appids.split(","); //字符分割 
					for(var i=0;i<strs.length;i++){
						showFileDiv(mdDialog.find("#filetd1"),false,"FAACCOUNT",strs[i],"");
					}
				}
				
				showFileDiv(mdDialog.find("#filetd"),true,"FAARCHIVES",itemid,"");
				
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
	
};

function funcOperButtons(operType, url, dataGrid, form) {

	var buttons = [ /*{
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"?isSub=0", form,operType);
			}
		}, {
			text : "附件管理",
			iconCls : "icon-files",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				showUploadifyModalDialog(parent.$('#uploadifydiv'),"itemids","FAARCHIVES");
			}
		} ,*/{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	return buttons;
	
};

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
					} else {
						parent.$.messager.alert('错误', result.title, 'error');
					}
				}
			});
};
