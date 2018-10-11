//设置全局变量
var datagrid_imlplan;
/**实施方案
 * imlplan_init.js
 * */
var baseUrl = contextpath + "prepare/controller/ImplementationPlanController/";
//路径
var urls = {
	qryImlPlan : baseUrl + "qryImlPlan.do",
	delImlPlan : baseUrl+ "delImlPlan.do",
	optImlPlanView : baseUrl+ "optImlPlanView.do",
	saveImlPlan : baseUrl+ "saveImlPlan.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do"
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
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
		, {field : "orgcode",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		
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
			case '3':
				$('#btn_add').linkbutton('disable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				$('#btn_send').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('disable');
				break;
			case '1':
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				$('#btn_send').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_detail').linkbutton('enable');
				break; 
			case '2':
				$('#btn_add').linkbutton('disable');
				$('#btn_edit').linkbutton('disable');
				$('#btn_remove').linkbutton('disable');
				$('#btn_send').linkbutton('disable');
				$('#btn_revoke').linkbutton('enable');
				$('#btn_detail').linkbutton('enable');
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
	$('#btn_revoke').linkbutton('disable');
}

/**
 * 查询
 */
function qryImlPlan(){
	var param = {
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
 * 新增
 */
function addImlPlan(){
	var rows = datagrid_imlplan.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}else{
		if(rows[0].solutionid!=null){
			easyui_warn("已录入的数据，不能再次录入！");
			return;
		}
	}
	showModalDialog(600, "imlplan_form", types.add, datagrid_imlplan, "实施方案新增",urls.optImlPlanView+"?optFlag="+types.add, urls.saveImlPlan+"?optFlag="+types.add);
}

/**
 * 修改
 */
function editImlPlan(){
	var rows = datagrid_imlplan.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}else{
		if(rows[0].solutionid==null){
			easyui_warn("该数据还未录入，请先录入数据！");
			return;
		}
	}
	showModalDialog(600, "imlplan_form", types.edit, datagrid_imlplan, "实施方案修改",urls.optImlPlanView+"?optFlag="+types.edit, urls.saveImlPlan+"?optFlag="+types.edit);
}

/**
 * 详情
 */
function detImlPlan(){
	var rows = datagrid_imlplan.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(600, "imlplan_form", types.view, datagrid_imlplan, "实施方案详情",urls.optImlPlanView+"?optFlag="+types.view, urls.saveImlPlan+"?optFlag="+types.view);
}

/**
 * 窗口
 * @param height
 * @param form
 * @param operType
 * @param dataGrid
 * @param title
 * @param href
 * @param url
 */
function showModalDialog(height, form, operType, dataGrid, title, href, url) {
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 850,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			f.form("load", row);
			if(operType=="view"){
				//加载附件
				showFileDiv(mdDialog.find("#imlplanfile"),false, row.implementationPlanPath, "56","implementationPlanPath");
			}else{
				//加载附件
				showFileDiv(mdDialog.find("#imlplanfile"),true, row.implementationPlanPath, "56","implementationPlanPath");
			}
			
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};

/**
 * 按钮
 * @param operType
 * @param url
 * @param dataGrid
 * @param form
 * @returns
 */
function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if(operType=="view"){
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if(operType=="edit"){
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url, form,operType);
			}
		},{
			text : "保存并送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"&sendFlag=1&menuid="+menuid+"&activityId="+activityId, form,operType);
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if(operType=="add"){
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url, form,operType);
			}
		},{
			text : "保存并送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url+"&sendFlag=1&menuid="+menuid+"&activityId="+activityId, form,operType);
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
};

/**
 * 提交
 * @param url
 * @param form
 * @param operType
 */
function submitForm(url, form,operType) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit",{
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
					easyui_info(result.title);
					datagrid_imlplan.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};

/**
 * 删除
 */
function delImlPlan(){
	var rows = datagrid_imlplan.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条项目数据！");
		return;
	}
	var solutionids = "";
	for(var i=0;i<rows.length;i++){
		if(rows[i].solutionid==null){
			easyui_warn("数据还未录入，请先录入数据！");
			return;
		}
		if(i==0){
			solutionids = rows[i].solutionid;
		}else{
			solutionids += ","+rows[i].solutionid;
		}
	}
	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function(r) {
		if (r) {
			$.post(urls.delImlPlan, {
				solutionids :solutionids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_imlplan.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 项目送审
 */
function sendWF(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	var solutionids = "";
	var wfids = "";
	for(var i=0;i<selectRow.length;i++){
		if(i==0){
			solutionids = selectRow[i].solutionid;
			if(selectRow[i].solutionid ==null){
				easyui_warn("该数据还未录入，请先录入数据！",null);
				return;
			}
			wfids = selectRow[i].wfid;
		}else{
			if(selectRow[i].solutionid ==null){
				easyui_warn("该数据还未录入，请先录入数据！",null);
				return;
			}
			solutionids += ","+selectRow[i].solutionid;
			wfids += ","+selectRow[i].wfid;
		}
	}
	parent.$.messager.confirm("送审确认", "确认送审选中的数据？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				activityId :activityId,
				solutionid : solutionids, 
				firstNode : firstNode,
				lastNode : lastNode,
				wfid :wfids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_imlplan.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 项目已送审撤回
 */
function revokeWF(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	var solutionids = "";
	var wfids = "";
	for(var i=0;i<selectRow.length;i++){
		if(i==0){
			solutionids = selectRow[i].solutionid;
			wfids = selectRow[i].wfid;
		}else{
			solutionids += ","+selectRow[i].solutionid;
			wfids += ","+selectRow[i].wfid;
		}
	}
	parent.$.messager.confirm("撤回确认", "确认撤回选中的数据？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				solutionid : solutionids,
				wfid :wfids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_imlplan.datagrid('reload');
				});
			}, "json");
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