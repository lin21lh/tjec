//设置全局变量
var datagrid_imlplan;
/**开发计划
 * imlplan_audit.js
 * */
var baseUrl = contextpath + "prepare/controller/ImplementationPlanController/";
var proUrl = contextpath + "ppms/discern/ProjectDiscernController/";
//路径
var urls = {
	qryImlPlan : baseUrl + "qryImlPlan.do",
	delImlPlan : baseUrl+ "delImlPlan.do",
	optImlPlanView : baseUrl+ "optImlPlanView.do",
	saveImlPlan : baseUrl+ "saveImlPlan.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	backWorkFlow : baseUrl + "backWorkFlow.do",
	thirdOrgQueryUrl : baseUrl + "queryThirdOrg.do",
	financeQueryUrl : baseUrl + "queryFinance.do",
	queryApprove : baseUrl + "queryApprove.do",
	qualUnitGrid : baseUrl+"qualUnitGrid.do",
	qualExpertGrid : baseUrl+"qualExpertGrid.do",
	financeUnitGrid : baseUrl+"financeUnitGrid.do",
	qryExpertByQ : baseUrl+"qryExpertByQ.do",
	saveAuditData : baseUrl+"saveAuditData.do"
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
 * 窗口
 * @param height
 * @param form
 * @param operType
 * @param dataGrid
 * @param title
 * @param href
 * @param url
 */
function showModalDialogAudit(height, form, operType, dataGrid, title, href) {
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	
	var wfids = "";
	for(var i=0;i<selectRow.length;i++){
		if(i==0){
			wfids = selectRow[i].wfid;
		}else{
			wfids += ","+selectRow[i].wfid;
		}
	}
	var icon = 'icon-edit';
	if("audit_view"==operType){
		icon = "icon-view";
	}
	
	if("audit_single"==operType || "audit_view"==operType){
		parent.$.modalDialog({
			title : title,
			iconCls : icon,
			width : 900,
			height : height,
			href : href,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				//物有所值验证结果
				comboboxFuncByCondFilter(menuid,"vomResult", "JUDGERESULT", "code", "name", mdDialog);
				//财政承受能力验证结果
				comboboxFuncByCondFilter(menuid,"fcResult", "JUDGERESULT", "code", "name", mdDialog);
				//审核意见中的财政审批结果
				comboboxFuncByCondFilter(menuid,"czResult", "JUDGERESULT", "code", "name", mdDialog);
				//定性分析结果
				comboboxFuncByCondFilter(menuid,"qualResult", "JUDGERESULT", "code", "name", mdDialog);
				
				f.form("load", row);
				
				showFileDiv(mdDialog.find("#imlplanfile"),false, row.implementationPlanPath, "56","implementationPlanPath");//实施方案附件
				
				//定性分析专家列表
				qualExpertGrid(mdDialog, row.projectid);
				//第三方机构列表
				thirdOrganGrid(mdDialog, row.projectid);
				//财政预算支出列表
				financeGrid(mdDialog, row.projectid);
				//定性分析组织单位
				qualUnitGrid(mdDialog,row.projectid);
				//财政承受能力验证组织单位
				financeUnitGrid(mdDialog,row.projectid);
				//默认选中第二个页签
				//加载信息
				$.post(urls.queryApprove, {
					projectid : row.projectid
				}, function(result) {
					var r = result.body.approve;
					if(r!=""){
						f.form("load", r);
						showFileDiv(mdDialog.find("#dxfx"),true, r.qualPath, "20","qualPath");//定性分析附件
						showFileDiv(mdDialog.find("#dlfx"),true, r.vomAttachment, "20","vomAttachment");//定量分析附件
						showFileDiv(mdDialog.find("#czcsnlbg"),true, r.fcAttachment, "20","fcAttachment");//财政承受能力验证附件
						showFileDiv(mdDialog.find("#zfshgw"),true, r.zfAttachment, "56","zfAttachment");//审核意见附件
					}
				}, "json");
				mdDialog.find("#vomNetcost").numberbox({
				    "onChange":function(){
				    	calculate(mdDialog);
				    }
				  });
				mdDialog.find("#vomAdjust").numberbox({
					"onChange":function(){
						calculate(mdDialog);
					}
				});
				mdDialog.find("#vomRiskcost").numberbox({
					"onChange":function(){
						calculate(mdDialog);
				    }
				});
				mdDialog.find("#vomPpp").numberbox({
					"onChange":function(){
						calculate(mdDialog);
				    }
				});
				
				if("audit_view"==operType){
					//将所有详情页datdagrid按钮置灰
					mdDialog.find("#expert_add").linkbutton('disable');
					mdDialog.find("#expert_del").linkbutton('disable');
					mdDialog.find("#qualunit_add").linkbutton('disable');
					mdDialog.find("#qualunit_del").linkbutton('disable');
					mdDialog.find("#third_add").linkbutton('disable');
					mdDialog.find("#third_del").linkbutton('disable');
					mdDialog.find("#fin_add").linkbutton('disable');
					mdDialog.find("#fin_del").linkbutton('disable');
					mdDialog.find("#finunit_add").linkbutton('disable');
					mdDialog.find("#finunit_del").linkbutton('disable');
				}
			},
			buttons : funcOperButtons(operType, dataGrid, form,"")
		});
	}else{
		parent.$.modalDialog({
			title : title,
			iconCls : icon,
			width : 450,
			height : 210,
			href : href,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				//加载附件
				showFileDiv(mdDialog.find("#imlplanfile"),true, row.implementationPlanPath, "30","implementationPlanPath");
			},
			buttons : funcOperButtons(operType,dataGrid, form,wfids)
		});
	}
	
};

/**
 * 按钮
 * @param operType
 * @param url
 * @param dataGrid
 * @param form
 * @returns
 */
function funcOperButtons(operType, dataGrid, formid,wfids) {

	var buttons;
	if("audit_single"==operType){
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				saveSubmit(operType, dataGrid, formid,wfids,1);
			}
		},{
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				saveSubmit(operType, dataGrid, formid,wfids,2);
			}
		},{
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				saveSubmit(operType, dataGrid, formid,wfids,3);
				}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if("audit_multiple"==operType){
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				var opinion = parent.$.modalDialog.handler.find('#opinion').val();
				parent.$.messager.confirm("审核确认", "确认要审核同意？", function(r) {
					if (r) {
						$.post(urls.sendWFUrl, {
							menuid : menuid,
							activityId :activityId,
							solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
							firstNode : firstNode,
							lastNode : lastNode,
							opinion : opinion,
							wfid :wfids
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								datagrid_imlplan.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if("back_multiple"==operType){
		buttons = [ {
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				
				var opinion = parent.$.modalDialog.handler.find('#opinion').val()
				parent.$.messager.confirm("退回确认", "确认要退回？", function(r) {
					if (r) {
						$.post(urls.backWorkFlow, {
							menuid : menuid,
							activityId :activityId,
							solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
							firstNode : firstNode,
							lastNode : lastNode,
							opinion : opinion,
							wfid :wfids
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								datagrid_imlplan.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else{
		buttons = [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
		
	
	return buttons;
};
function saveSubmit(operType, dataGrid, formid,wfids,type){
	parent.$.modalDialog.openner_dataGrid = dataGrid;
	var form = parent.$.modalDialog.handler.find("#"+formid);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
	//1.定性分析 专家列表
	var o=parent.$.modalDialog.handler.find("#qualExpert");
	if(o.datagrid('getRows').length<1){
		easyui_warn("请至少录入一条专家信息！",null);
		parent.$.modalDialog.handler.find('#tabList').tabs('select', '物有所值定性分析验证');
		return;
	}
	var data = o.datagrid("getData");
	var total = data.total;
	for(var i=0;i<total;i++){
		o.datagrid('endEdit', i);//把所有的编辑行锁定
	}
	var subStr = JSON.stringify(o.datagrid('getRows'));
	parent.$.modalDialog.handler.find("#qualExpertData").val(subStr);
	
	
	//2.定性分析 组织单位
	var o=parent.$.modalDialog.handler.find("#qualUnit");
	if(o.datagrid('getRows').length<1){
		easyui_warn("请至少录入一条组织单位信息！",null);
		parent.$.modalDialog.handler.find('#tabList').tabs('select', '物有所值定性分析验证');
		return;
	}
	var data = o.datagrid("getData");
	var total = data.total;
	for(var i=0;i<total;i++){
		o.datagrid('endEdit', i);//把所有的编辑行锁定
	}
	var subStr = JSON.stringify(o.datagrid('getRows'));
	parent.$.modalDialog.handler.find("#qualUnitData").val(subStr);
	
	
	
	//3.定量分析 第三方机构
	var o=parent.$.modalDialog.handler.find("#thirdOrganGrid");
	if(o.datagrid('getRows').length<1){
		easyui_warn("请至少录入一条第三方机构信息！",null);
		parent.$.modalDialog.handler.find('#tabList').tabs('select', '物有所值定量分析验证');
		return;
	}
	var data = o.datagrid("getData");
	var total = data.total;
	for(var i=0;i<total;i++){
		o.datagrid('endEdit', i);//把所有的编辑行锁定
	}
	var subStr = JSON.stringify(o.datagrid('getRows'));
	parent.$.modalDialog.handler.find("#thirdOrganGridData").val(subStr);
	
	
	//4.财政承受能力验证 第三方机构
	var o=parent.$.modalDialog.handler.find("#financeGrid");
	if(o.datagrid('getRows').length<1){
		easyui_warn("请至少录入一条第三方机构信息！",null);
		parent.$.modalDialog.handler.find('#tabList').tabs('select', '财政承受能力验证');
		return;
	}
	var data = o.datagrid("getData");
	var total = data.total;
	for(var i=0;i<total;i++){
		o.datagrid('endEdit', i);//把所有的编辑行锁定
	}
	var subStr = JSON.stringify(o.datagrid('getRows'));
	parent.$.modalDialog.handler.find("#financeGridData").val(subStr);
	
	
	//5.财政承受能力验证 组织单位
	var o=parent.$.modalDialog.handler.find("#financeUnit");
	if(o.datagrid('getRows').length<1){
		easyui_warn("请至少录入一条第三方机构信息！",null);
		parent.$.modalDialog.handler.find('#tabList').tabs('select', '财政承受能力验证');
		return;
	}
	var data = o.datagrid("getData");
	var total = data.total;
	for(var i=0;i<total;i++){
		o.datagrid('endEdit', i);//把所有的编辑行锁定
	}
	var subStr = JSON.stringify(o.datagrid('getRows'));
	parent.$.modalDialog.handler.find("#financeUnitData").val(subStr);
	
	if(type==1){
		form.form("submit",{
			url : urls.saveAuditData,
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
	}else if(type==2){
		parent.$.messager.confirm("审核确认", "确认要审核同意？", function(r) {
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
	}else{
		parent.$.messager.confirm("退回确认", "确认要退回？", function(r) {
			if (r) {
				form.form("submit",{
						url : urls.backWorkFlow,
						queryParams:{
							menuid : menuid,
							activityId :activityId,
							solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
							firstNode : firstNode,
							lastNode : lastNode,
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
	
	
}
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
		easyui_warn("请至少选择一条数据！");
		return;
	}
	var solutionids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			solutionids = rows[i].solutionid;
		}else{
			solutionids += ","+rows[i].solutionid;
		}
	}
	parent.$.messager.confirm("确认撤回", "确定删除选中的数据？", function(r) {
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
 * 项目审核
 */
function sendWF(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}else if(selectRow.length==1){
		
		showModalDialogAudit(650, "imlplan_form", types.audit_single, datagrid_imlplan, "实施方案审核",urls.optImlPlanView+"?optFlag="+types.audit_single);
		
	}else{
		showModalDialogAudit(650, "audit_multiple", types.audit_multiple, datagrid_imlplan, "实施方案审核",urls.optImlPlanView+"?optFlag="+types.audit_multiple);
	}
	
}
/**
 * 退回
 */
function backWorkFlow(){
	var selectRow = datagrid_imlplan.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	showModalDialogAudit(650, "audit_multiple", types.back_multiple, datagrid_imlplan, "实施方案退回",urls.optImlPlanView+"?optFlag="+types.back_multiple);
	/*var row = datagrid_imlplan.datagrid("getSelections")[0];
	parent.$.messager.confirm("退回确认", "确认要退回所选数据？", function(r) {
		if (r) {
			$.post(urls.backWorkFlow, {
				menuid : menuid,
				activityId :activityId,
				firstNode : firstNode,
				lastNode : lastNode,
				solutionid : row.solutionid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_imlplan.datagrid('reload');
				});
			}, "json");
		}
	});*/
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
	showModalDialogAudit(650, "imlplan_form", types.audit_view, datagrid_imlplan, "实施方案详情",urls.optImlPlanView+"?optFlag="+types.audit_view);
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

//定性分析专家列表
var currentRow_expert = -1;
function qualExpertGrid(mdDialog,projectid){
	var grid = mdDialog.find("#qualExpert").datagrid({
		height: 210,
		title: '定性分析专家列表',
		collapsible: false,
		url : urls.qualExpertGrid,
		queryParams : {projectid:projectid},
		singleSelect: true,
		rownumbers : true,
		idField: 'qualexpertid',
		columns: [[//显示的列
		           {field: 'qualexpertid', title: '序号', width: 100,  checkbox: true },
		           { field: 'expertName', title: '专家名称', width: 130,halign : 'center',
		        	   editor: { 
		        		   type: 'combogrid',
		        		   options: { 
		        			   	panelWidth:600,    
		        			    idField:'name',
		        			    pagination : true,
		        			    rownumbers : true,
		        			    textField:'name',  
		        			    mode:'remote',
		        			    url:urls.qryExpertByQ,    
		        			    columns:[[    
		        			        {field:'expertid',title:'专家编码',width:60},    
		        			        {field:'name',title:'专家名称',width:100},    
		        			        {field:'sexName',title:'性别',width:120},
		        			        {field:'politicsStatusName',title:'政治面貌',width:120},
		        			        {field:'phoneNumber',title:'联系方式',width:120},
		        			        {field:'isEmergencyName',title:'应急专家',width:80},
		        			        {field:'expertTypeName',title:'专家类型',width:120},
		        			        {field:'highestDegreeName',title:'最高学历',width:80},
		        			        {field:'highestOfferingName',title:'最高学位',width:80},
		        			        {field:'majorTypeName',title:'从事专业类别',width:120},
		        			        {field:'industryName',title:'所属行业',width:120},
		        			        {field:'bidMajorName',title:'评标专业方向',width:250},
		        			        {field:'qualificationName',title:'执业资格证书',width:350},
		        			        {field:'research',title:'个人研究以及专业成就',width:350},
		        			        
		        			    ]],
		        			    onClickRow: function (rowIndex, rowData) {
		        			       var  typeObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertType'});
		        			       var  phoneObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertPhone'});
		        			       var  majorObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'bidmajor'});
		        			       var  expertIdObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertId'});
		        			       mdDialog.find(typeObj.target).val(rowData.expertTypeName);
		        			       mdDialog.find(phoneObj.target).val(rowData.phoneNumber);
		        			       mdDialog.find(majorObj.target).val(rowData.bidMajorName);
		        			       mdDialog.find(expertIdObj.target).val(rowData.expertid);
		        			    }
		           	
		        		   } 
		        	   }
		           },{ field: 'expertType', title: '专家类型', width: 100,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,50]}} }
		           },{ field: 'expertPhone', title: '联系方式', width: 140,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,30]}} }
		           },{ field: 'bidmajor', title: '评标专业', width: 300,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,200]}} }
		           },{ field: 'responsibleArea', title: '负责领域', width: 150,halign : 'center',
		        	   editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入负责区域',validType:{length:[0,50]}} }
		           },{ field: 'expertId', title: 'expertId', width: 100,halign : 'center',hidden:true,
		        	   editor: { type: 'validatebox', options: { editable:false} }
		           },{ field: 'projectid', title: 'projectid', width: 100,halign : 'center',hidden:true,
		        	   editor: { type: 'validatebox', options: { editable:false} }
		           },{ field: 'remark', title: '说明', width: 300,halign : 'center',
		        	   editor: { type: 'validatebox', options: { validType:{length:[0,100]}} }
		           }]],
		           toolbar: [{ id:"expert_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#qualExpert").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#qualExpert').datagrid('validateRow', currentRow_expert)){
		            			   	mdDialog.find('#qualExpert').datagrid('endEdit', currentRow_expert);
		            				mdDialog.find("#qualExpert").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#qualExpert").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_expert = mdDialog.find('#qualExpert').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#qualExpert").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#qualExpert").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_expert = mdDialog.find('#qualExpert').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"expert_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#qualExpert").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#qualExpert").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_expert==rowIndex){
		    	                        		currentRow_expert = -1;
		    	                        	}else if(rowIndex<currentRow_expert){
		    	                        		currentRow_expert = currentRow_expert-1;
		    	                        	}
		    	                       		mdDialog.find("#qualExpert").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_expert != rowIndex && mdDialog.find('#qualExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#qualExpert').datagrid('endEdit', currentRow_expert);
		        		   //开启新行编辑
		        			mdDialog.find("#qualExpert").datagrid('beginEdit', rowIndex);
		        			currentRow_expert = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#qualExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#qualExpert').datagrid('endEdit', currentRow_expert);
		        		   currentRow_expert = -1;
		        	   }
		           }
	});
	return grid;
}
//定性分析组织单位
var currentRow_qualunit=-1;
function qualUnitGrid(mdDialog,projectid){
	var grid = mdDialog.find("#qualUnit").datagrid({
        height: 190,
        title: '定性分析组织单位',
        collapsible: false,
        url : urls.qualUnitGrid,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'qualunitid',
        columns: [[//显示的列
                   {field: 'qualunitid', title: '序号', width: 100,  checkbox: true },
                   { field: 'unitName', title: '单位名称', width: 130,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入单位名称',validType:{length:[0,50]}} }
                   },{ field: 'unitPerson', title: '负责人', width: 180,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入负责人',validType:{length:[0,50]}} }
                   },{ field: 'unitTelphone', title: '负责人电话', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入负责人电话',validType:{length:[0,50]}} }
                   },{ field: 'projectid', title: 'projectid', width: 100,halign : 'center',hidden:true
                   },{ field: 'remark', title: '说明', width: 300,halign : 'center',
                         editor: { type: 'validatebox', options: { validType:{length:[0,100]}} }
                   }]],
                   toolbar: [{id:"qualunit_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#qualUnit").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#qualUnit').datagrid('validateRow', currentRow_qualunit)){
		            			   	mdDialog.find('#qualUnit').datagrid('endEdit', currentRow_qualunit);
		            				mdDialog.find("#qualUnit").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#qualUnit").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_qualunit = mdDialog.find('#qualUnit').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#qualUnit").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#qualUnit").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_qualunit = mdDialog.find('#qualUnit').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"qualunit_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#qualUnit").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#qualUnit").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_qualunit==rowIndex){
		    	                        		currentRow_qualunit = -1;
		    	                        	}else if(rowIndex<currentRow_qualunit){
		    	                        		currentRow_qualunit = currentRow_qualunit-1;
		    	                        	}
		    	                       		mdDialog.find("#qualUnit").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_qualunit != rowIndex && mdDialog.find('#qualUnit').datagrid('validateRow', currentRow_qualunit)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#qualUnit').datagrid('endEdit', currentRow_qualunit);
		        		   //开启新行编辑
		        			mdDialog.find("#qualUnit").datagrid('beginEdit', rowIndex);
		        			currentRow_qualunit = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#qualUnit').datagrid('validateRow', currentRow_qualunit)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#qualUnit').datagrid('endEdit', currentRow_qualunit);
		        		   currentRow_qualunit = -1;
		        	   }
		           }
    });
	return grid;
}

//第三方机构列表
var currentRow_third = -1;
function thirdOrganGrid(mdDialog,projectid){
	var grid = mdDialog.find("#thirdOrganGrid").datagrid({
        height: 200,//310
        title: '第三方机构',
        collapsible: false,
        url : urls.thirdOrgQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'approrganid',
        columns: [[//显示的列
                   {field: 'approrganid', title: '序号', width: 100,  checkbox: true },
                   { field: 'organ_code', title: '机构代码', width: 130,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入机构代码',validType:{length:[0,50]}} }
                   },{ field: 'organ_name', title: '机构名称', width: 180,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入机构名称',validType:{length:[0,50]}} }
                   },{ field: 'consignor', title: '委托方', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入委托方',validType:{length:[0,50]}} }
                   },{ field: 'project_manager', title: '项目经理', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入项目经理',validType:{length:[0,50]}} }
                   },{ field: 'phone', title: '联系电话', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入联系电话',validType:{length:[0,25]}} }
                   },{ field: 'mobile', title: '手机号码', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,validType :'phonesIsRight'} }
                   },{ field: 'content', title: '主要服务内容', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: {required: true,validType:{length:[0,500]}} }
                   },{ field: 'entrust_time', title: '委托时间', width: 100,halign : 'center',
                         editor: { type: 'datebox', options: {required: true,editable:false,validType:{length:[0,20]}} }
                   }]],
                   toolbar: [{id:"third_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#thirdOrganGrid").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#thirdOrganGrid').datagrid('validateRow', currentRow_third)){
		            			   	mdDialog.find('#thirdOrganGrid').datagrid('endEdit', currentRow_third);
		            				mdDialog.find("#thirdOrganGrid").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#thirdOrganGrid").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_third = mdDialog.find('#thirdOrganGrid').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#thirdOrganGrid").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#thirdOrganGrid").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_third = mdDialog.find('#thirdOrganGrid').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"third_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#thirdOrganGrid").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#thirdOrganGrid").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_third==rowIndex){
		    	                        		currentRow_third = -1;
		    	                        	}else if(rowIndex<currentRow_third){
		    	                        		currentRow_third = currentRow_third-1;
		    	                        	}
		    	                       		mdDialog.find("#thirdOrganGrid").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_third != rowIndex && mdDialog.find('#thirdOrganGrid').datagrid('validateRow', currentRow_third)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#thirdOrganGrid').datagrid('endEdit', currentRow_third);
		        		   //开启新行编辑
		        			mdDialog.find("#thirdOrganGrid").datagrid('beginEdit', rowIndex);
		        			currentRow_third = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#thirdOrganGrid').datagrid('validateRow', currentRow_third)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#thirdOrganGrid').datagrid('endEdit', currentRow_third);
		        		   currentRow_third = -1;
		        	   }
		           }
    });
	return grid;
}

var currentRow = -1;
//财政支出
function financeGrid(mdDialog,projectid){
	var grid = mdDialog.find("#financeGrid").datagrid({
        height: 210,
        title: '财政预算支出',
        collapsible: false,
        url : urls.financeQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        showFooter : true,
        idField: 'budgetid',
        columns: [[//显示的列
                   {field: 'budgetid', title: '序号', width: 100, halign : 'center', checkbox: true },
                   { field: 'budget_year', title: '支出年度', width: 100,
                       editor: { type: 'numberspinner', options: {required: true,missingMessage:'请输入支出年度',min:2000,max:2099,editable:true}}
                   },{ field: 'budget_gqtz', title: '股权投资支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){
                	   return value==undefined?"":Number(value).toFixed(2);},
                        editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入股权投资支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_yybt', title: '运营补贴支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入运营补贴支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_fxcd', title: '风险承担支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入风险承担支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_pttr', title: '配套投入支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入配套投入支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'total', title: '合计', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {editable:false} }
                   }]],
        toolbar: [{id:"fin_add",
            text: '添加', iconCls: 'icon-add', handler: function () {
            	var data = mdDialog.find("#financeGrid").datagrid("getData");//grid列表
            	var total = data.total;//grid的总条数
            	
        	    if(total!=0){
           		   if(mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
           			   	mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
           				mdDialog.find("#financeGrid").datagrid('appendRow', {row: {}});//追加一行
                      	mdDialog.find("#financeGrid").datagrid("beginEdit", total);//开启编辑
                      	currentRow = mdDialog.find('#financeGrid').datagrid('getRows').length-1;
           		   }else{
           			   easyui_warn("当前存在正在编辑的行！");
           		   }
           	   }else{
                      	mdDialog.find("#financeGrid").datagrid('appendRow', {row: {}});//追加一行
                      	mdDialog.find("#financeGrid").datagrid("beginEdit", total);//开启编辑
                      	currentRow = mdDialog.find('#financeGrid').datagrid('getRows').length-1;
           	   }
            }
        },'-', {id:"fin_del",
            text: '删除', iconCls: 'icon-remove', handler: function () {
            	var row = mdDialog.find("#thirdOrganGrid").datagrid('getSelections');
            	var rowIndex = mdDialog.find("#financeGrid").datagrid('getRowIndex',row[0]);
            	
           		if(rowIndex == currentRow){
           			parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
	                     if (r) {
	                    	 currentRow = -1;
	                    	mdDialog.find("#financeGrid").datagrid('deleteRow',rowIndex);
	                		calcMoney(mdDialog);
	                     }
                    });
           			
           		}else{
           			if(mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
           				mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
           				parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
      	                     if (r) {
      	                    	if(rowIndex<currentRow){
      	                    		currentRow = currentRow-1;
	                        	}
      	                    	mdDialog.find("#financeGrid").datagrid('deleteRow',rowIndex);
      	                		calcMoney(mdDialog);
      	                     }
                           });
               			calcMoney(mdDialog);
           			}else{
           				easyui_warn("当前存在正在编辑的行！");
           			}
           		}
             }
        },'-',{text:'单位：万元'}],
        onDblClickRow:function (rowIndex, rowData) {
           //mdDialog.find("#financeGrid").datagrid('beginEdit', rowIndex);
        	
           //结束上一行编辑，开启新行的编辑
     	   if(currentRow != rowIndex && mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
     		   //结束上一行编辑
     		   mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
     		   //开启新行编辑
     		   mdDialog.find("#financeGrid").datagrid('beginEdit', rowIndex);
     		   currentRow = rowIndex;
     		   isEditFlag = true;
     	   }else{
     		  easyui_warn("当前存在正在编辑的行！");
     	   }
        },
        onClickRow:function(rowIndex,rowData){
    	    if( mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
      		   //结束上一行编辑
      		   mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
      		   currentRow = -1;
      		   isEditFlag = false;
      	    }
    	   
        },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
     	   calcMoney(mdDialog);
        },onBeginEdit:function(rowIndex, rowData){
        	
        	mdDialog.find("#financeGrid").datagrid('beginEdit', rowIndex);
    	    var objGrid = mdDialog.find("#financeGrid");     
    	    var budget_gqtz = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_gqtz'}); 
    	    var budget_yybt = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_yybt'}); 
    	    var budget_fxcd = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_fxcd'}); 
    	    var budget_pttr = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_pttr'}); 
    	    var total = objGrid.datagrid('getEditor', {index:rowIndex,field:'total'}); 
    	    mdDialog.find(budget_gqtz.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
    	    mdDialog.find(budget_yybt.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
    	    mdDialog.find(budget_fxcd.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
    	    mdDialog.find(budget_pttr.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
        }
    });
	return grid;
}
function hj(budget_gqtz,budget_yybt,budget_fxcd,budget_pttr){
	if (isNaN(budget_gqtz)) {  
		budget_gqtz =0;    
	} 
	if (isNaN(budget_yybt)) {  
		budget_yybt =0;    
	} 
	if (isNaN(budget_fxcd)) {  
		budget_fxcd =0;    
	} 
	if (isNaN(budget_pttr)) {  
		budget_pttr =0;    
	} 
	return budget_gqtz+budget_yybt+budget_fxcd+budget_pttr;
}
var count =0;
//计算
function calcMoney(mdDialog)
{
	var datagrid = mdDialog.find('#financeGrid')
	var rowsnum =  datagrid.datagrid('getRows');
	var gptzsum = 0;
	var yybtsum = 0;
	var fxcdsum = 0;
	var pttrsum = 0;
	var sum = 0;
	  for(var i=0;i<rowsnum.length;i++){
		  //股权投资支出额
		  if(rowsnum[i].budget_gqtz == undefined || rowsnum[i].budget_gqtz == null || rowsnum[i].budget_gqtz ==''){
			  gptzsum +=0;
		  }else{
			  gptzsum +=Number(rowsnum[i].budget_gqtz);
		  }
		  
		  //运营补贴支出额
		  if(rowsnum[i].budget_yybt == undefined || rowsnum[i].budget_yybt == null || rowsnum[i].budget_yybt ==''){
			  yybtsum +=0;
		  }else{
			  yybtsum +=Number(rowsnum[i].budget_yybt);
		  }
		  
		  //风险承担支出额
		  if(rowsnum[i].budget_fxcd == undefined || rowsnum[i].budget_fxcd == null || rowsnum[i].budget_fxcd ==''){
			  fxcdsum +=0;
		  }else{
			  fxcdsum +=Number(rowsnum[i].budget_fxcd);
		  }
		  
		  //配套投入支出额
		  if(rowsnum[i].budget_pttr == undefined || rowsnum[i].budget_pttr == null || rowsnum[i].budget_pttr ==''){
			  pttrsum +=0;
		  }else{
			  pttrsum +=Number(rowsnum[i].budget_pttr);
		  }
		  
		//总计
		  if(rowsnum[i].total == undefined || rowsnum[i].total == null || rowsnum[i].total ==''){
			  sum +=0;
		  }else{
			  sum +=Number(rowsnum[i].total);
		  }
		  
	  }
	  var rows = datagrid.datagrid('getFooterRows');
	  rows[0]['budget_gqtz'] = gptzsum;
	  rows[0]['budget_yybt'] = yybtsum;
	  rows[0]['budget_fxcd'] = fxcdsum;
	  rows[0]['budget_pttr'] = pttrsum;
	  rows[0]['total'] = sum;
	  datagrid.datagrid('reloadFooter');
}


/**
 * 计算
 * @param mdDialog
 */
function calculate(mdDialog){
	var vomNetcost =parseFloat(mdDialog.find("#vomNetcost").numberbox('getValue'));
	var vomAdjust =parseFloat(mdDialog.find("#vomAdjust").numberbox('getValue'));
	var vomRiskcost =parseFloat(mdDialog.find("#vomRiskcost").numberbox('getValue'));
	var vomPpp =parseFloat(mdDialog.find("#vomPpp").numberbox('getValue'));
	if (isNaN(vomNetcost)) {    
		vomNetcost =0;    
    }   
	if (isNaN(vomAdjust)) {    
		vomAdjust =0;    
    }  
	if (isNaN(vomRiskcost)) {    
		vomRiskcost =0;    
    }  
	if (isNaN(vomPpp)) {  
		vomPpp =0;    
	}  
	var vomPsc = vomNetcost+vomAdjust+vomRiskcost;
	mdDialog.find('#vomPsc').numberbox('setValue',vomPsc);
	var vomVfm  = vomPsc - vomPpp;
	mdDialog.find('#vomVfm').numberbox('setValue',vomVfm);
}

//财政承受能力验证组织单位
var currentRow_finunit = -1;
function financeUnitGrid(mdDialog,projectid){
	var grid = mdDialog.find("#financeUnit").datagrid({
        height: 180,
        title: '组织单位',
        collapsible: false,
        url : urls.financeUnitGrid,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'finunitid',
        columns: [[//显示的列
                   {field: 'finunitid', title: '序号', width: 100,  checkbox: true },
                   { field: 'unitName', title: '单位名称', width: 130,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入单位名称',validType:{length:[0,50]}} }
                   },{ field: 'unitPerson', title: '负责人', width: 180,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入负责人',validType:{length:[0,50]}} }
                   },{ field: 'unitTelphone', title: '负责人电话', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入负责人电话',validType:{length:[0,50]}} }
                   },{ field: 'projectid', title: 'projectid', width: 100,halign : 'center',hidden:true
                   },{ field: 'remark', title: '说明', width: 300,halign : 'center',
                         editor: { type: 'validatebox', options: { validType:{length:[0,100]}} }
                   }]],
                   toolbar: [{id:"finunit_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#financeUnit").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#financeUnit').datagrid('validateRow', currentRow_finunit)){
		            			   	mdDialog.find('#financeUnit').datagrid('endEdit', currentRow_finunit);
		            				mdDialog.find("#financeUnit").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#financeUnit").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_finunit = mdDialog.find('#financeUnit').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#financeUnit").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#financeUnit").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_finunit = mdDialog.find('#financeUnit').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"finunit_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#financeUnit").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#financeUnit").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_finunit==rowIndex){
		    	                        		currentRow_finunit = -1;
		    	                        	}else if(rowIndex<currentRow_finunit){
		    	                        		currentRow_finunit = currentRow_finunit-1;
		    	                        	}
		    	                       		mdDialog.find("#financeUnit").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_finunit != rowIndex && mdDialog.find('#financeUnit').datagrid('validateRow', currentRow_finunit)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#financeUnit').datagrid('endEdit', currentRow_finunit);
		        		   //开启新行编辑
		        			mdDialog.find("#financeUnit").datagrid('beginEdit', rowIndex);
		        			currentRow_finunit = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#financeUnit').datagrid('validateRow', currentRow_finunit)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#financeUnit').datagrid('endEdit', currentRow_finunit);
		        		   currentRow_finunit = -1;
		        	   }
		           }
    });
	return grid;
}
