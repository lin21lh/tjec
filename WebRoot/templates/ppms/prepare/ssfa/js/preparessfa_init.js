//设置全局变量
var datagrid_imlplan;
/**方案审核
 * preparessfa_init.js
 * */
var baseUrl = contextpath + "prepare/controller/ImplementationPlanController/";
var proUrl = contextpath + "ppms/discern/ProjectDiscernController/";
//路径
var urls = {
	qryImlPlan : baseUrl + "qryImlPlan.do",
	delImlPlan : baseUrl+ "delImlPlan.do",
	pageForward : baseUrl+ "pageForward.do",
	queryApprove : baseUrl + "queryApprove.do",
	sendWFUrl : baseUrl + "sendWorkFlowForVerify.do",
	backWorkFlow : baseUrl + "backWorkFlow.do",
	auditOpinion : contextpath + "ppms/discern/ProjectDiscernController/auditOpinion.do",
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit",
		audit_single : "audit_single", 
		audit_multiple :"audit_multiple",
		audit_view :"audit_view",
		back_multiple :"back_multiple"
};
$(function() {
	//加载数据
	loaddatagrid();
	//加载查询条件
	loadqryconditon();
});
function loaddatagrid(){
	datagrid_imlplan = $("#datagrid_imlplan").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryImlPlan,
		queryParams: {
			dealStatus : 1,
			isAudit :1,
			activityId :activityId,
			firstNode : firstNode,
			lastNode : lastNode,
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_imlplan",
		showFooter : true,
		columns : [ [ {	field : "projectid",checkbox : true, rowspan:2,	align:'left'}
		, {field : "proName",title : "项目名称",halign : 'center',	width : 250,sortable:true,align:'left', rowspan:2}
		, {field : "proTypeName",title : "项目类型",halign : 'center',	width : 80,sortable:true, align:'left',rowspan:2}
		, {field : "statusName",title : "状态",halign : 'center',	width : 80,sortable:true, align:'left',rowspan:2}
		,{title:'实施方案信息',colspan:9}
		,{title:'项目信息',colspan:12}
		, {field : "createusername",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		, {field : "createuser",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "createtime",title : "创建时间",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		, {field : "updateusername",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		, {field : "updateuser",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "updatetime",title : "修改时间",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		
		, {field : "proType",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proPerate",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proTrade",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proReturn",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proSendtype",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "solutionid",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		]
		, [
			 {field : "solutionid",title : "主键id",halign : 'center',	width : 120,align:'left',sortable:true,rowspan:1,hidden:true}
			, {field : "datatype",title : "数据类型",halign : 'center',	width : 120,align:'left',sortable:true,rowspan:1,hidden:true}
			, {field : "riskAllocation",title : "风险分配框架",halign : 'center',	width : 120,sortable:true}
			, {field : "projectFinance",title : "项目融资结构",halign : 'center',	width : 120,sortable:true}
			, {field : "repayMechanism",title : "回报机制",halign : 'center',	width : 120,sortable:true}
			, {field : "suitedPlan",title : "配套安排",halign : 'center',	width : 120,sortable:true}
			, {field : "contractSystem",title : "项目合同体系",halign : 'center',	width : 120,sortable:true}
			, {field : "contractCoreContent",title : "项目合同核心内容",halign : 'center',	width : 120,sortable:true}
			, {field : "supervisoryRegime",title : "监管架构",halign : 'center',	width : 120,sortable:true}
			, {field : "advancePublishTime",title : "预审公告发布时间",halign : 'center',	width : 120,sortable:true}
			, {field : "purchaseNoticeTime",title : "采购公告发布时间",halign : 'center',	width : 120,sortable:true}
			, {field : "implementationPlanPath",title : "实施方案附件路径",halign : 'center',	width : 120,sortable:true,hidden:true}
			, {field : "wfid",title : "工作流ID",halign : 'center',	width : 120,sortable:true,hidden:true}
			, {field : "status",title : "项目状态",halign : 'center',	width : 120,sortable:true,hidden:true}
		   
			, {field : "amount",title : "总投资",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
			, {field : "proYear",title : "合作年限",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
			, {field : "proTradeName",title : "所属行业",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proPerateName",title : "运作方式",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proReturnName",title : "回报机制",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proSendtypeName",title : "项目发起类型",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proPerson",title : "项目联系人",halign : 'center',fixed : true,	width : 120,sortable:true}
		]]
	});
}

function loadqryconditon(){
	$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			qryImlPlan();
			switch (record.value) {
			case '1':
				$('#btn_send').linkbutton('enable');
				$('#btn_back').linkbutton('enable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('enable');
				break;
			case '2':
				$('#btn_send').linkbutton('disable');
				$('#btn_back').linkbutton('disable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('enable');
				break; 
			default:
				break;
			}
		}
	});
	comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name");//项目发起类型
	comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name");//回报机制
	
	/*$("#proSendtype").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#proPerate").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#proType").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#proReturn").combobox("addClearBtn", {iconCls:"icon-clear"});*/
	//comboboxFuncByCondFilter(menuid,"proSendtype", "PROTRADE", "code", "name");//所属行业
	
	$("#proTrade").treeDialog({
		title :'选择所属行业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'proTrade1',
		prompt: "请选择所属行业",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "PROTRADE",
		filters:{
			code: "行业代码",
			name: "行业名称"
		}
	});
	$('#btn_add').linkbutton('disable');
}
/**
 * 查询
 */
function qryImlPlan(){
	var param = {
			isAudit :1,
			dealStatus : $("#dealStatus").combobox('getValue'),
			proName : $("#proName").textbox('getValue'),
			proTrade : $("#proTrade").treeDialog('getValue'),//所属行业
			proPerate : $("#proPerate").combobox("getValues").join(","),//项目运作方式
			proReturn : $("#proReturn").combobox('getValues').join(","),//回报机制
			proSendtype : $("#proSendtype").combobox('getValues').join(","),//项目发起类型
			proType : $('#proType').combobox('getValues').join(","),//项目类型
			proPerson :  $('#proPerson').textbox('getValue'),//项目联系人
			activityId :activityId,
			firstNode : firstNode,
			lastNode : lastNode,
			menuid : menuid
		};
	$("#datagrid_imlplan").datagrid("load",param);
}
/**
 * 审批
 */
function sendWF(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}else if(selectRow.length==1){
		showModalDialogAudit();
	}
}
/**
 *审核页面
 */
function showModalDialogAudit() {
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	var wfids = selectRow[0].wfid
	parent.$.modalDialog({
		title : "实施方案审核",
		width : 900,
		height : 630,
		href : urls.pageForward+"?pageName=auditForm",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//加载信息
			var f = parent.$.modalDialog.handler.find('#imlplan_form');
			f.form("load",selectRow[0]);
		},
		buttons : [{
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				saveSubmit(wfids,"1");
			}
		},{
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				saveSubmit(wfids,"3");//3代表退回
				}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
/**
 * 
 * @param operType
 * @param wfids
 */
function saveSubmit(wfids,commitFlag){
	parent.$.modalDialog.openner_dataGrid = datagrid_imlplan;
	var form = parent.$.modalDialog.handler.find("#imlplan_form");
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	var message = "确认要审核同意？";
	if(commitFlag==3){
		message = "确认要退回？";
	}
	parent.$.messager.confirm("审核确认", message, function(r) {
		if (r) {
			form.form("submit",{
				url : urls.sendWFUrl,
				queryParams:{
					menuid : menuid,
					activityId :activityId,
					solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
					firstNode : firstNode,
					lastNode : lastNode,
					flag:1,
					backFlag : commitFlag,
					wfid :parent.$.modalDialog.handler.find('#wfid').val(),
					opinion: parent.$.modalDialog.handler.find('#opinion').val()
				},
				onSubmit : function() {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
				},
				success : function(result) {
					parent.$.messager.progress('close');
					result = $.parseJSON(result);
					if (result.success) {
						//重新加载gride
						parent.$.modalDialog.handler.dialog('close');
						easyui_info(result.title);
						datagrid_imlplan.datagrid("reload");
					} else {
						parent.$.messager.alert('错误', result.title, 'error');
					}
				}
			});
	}
	});
}
/**
 * 流程信息
 */
function workflowMessage(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
/**
 * 退回
 */
function backWorkFlow(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = datagrid_imlplan.datagrid("getSelections")[0];
	var wfid = row.wfid;
	parent.$.modalDialog({
		title : "退回处理",
		width : 450,
		height : 210,
		iconCls : 'icon-edit',
		href : urls.auditOpinion,
		buttons : [{text : "退回",
						iconCls : "icon-save",
						handler : function() {
							var opinion = parent.$.modalDialog.handler.find('#opinion').val();
							if (opinion == null || opinion == "") {
								return;
							}
							var form = parent.$.modalDialog.handler.find('#revoke_audit');
							var isValid = form.form('validate');
							if (!isValid) {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认将该项目退回？", function(r) {
								if(r){
									$.post(urls.sendWFUrl, {
										menuid : menuid,
										activityId : activityId,
										wfid : wfid,
										backFlag : "3",
										opinion : opinion
									}, function(result) {
										easyui_auto_notice(result, function() {
											datagrid_imlplan.datagrid('reload');
										});
									}, "json");
									parent.$.modalDialog.handler.dialog('close');
								}
							});
						}
					}, {
						text : "关闭",
						iconCls : "icon-cancel",
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					}]
	});
}
/**
 *详情
 */
function showDetail() {
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var wfids = selectRow[0].wfid
	parent.$.modalDialog({
		title : "实施方案详情",
		width : 900,
		height : 630,
		href : urls.pageForward+"?pageName=detailForm",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//加载信息
			var f = parent.$.modalDialog.handler.find('#imlplan_form');
			f.form("load",selectRow[0]);
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