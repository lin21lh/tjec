//设置全局变量
var datagrid_devplan;
/**开发计划
 * devplan_init.js
 * */
var baseUrl = contextpath + "prepare/controller/DevelopmentPlanController/";
//路径
var urls = {
	qryDevPlan : baseUrl + "qryDevPlan.do",
	delDevPlan : baseUrl+ "delDevPlan.do",
	optDevPlanView : baseUrl+ "optDevPlanView.do",
	saveDevPlan : baseUrl+ "saveDevPlan.do",
	qryImlplanIsAudit : baseUrl +"qryImlplanIsAudit.do",
	outDevPlan : contextpath +"manage/outExcel/controller/OutApplicationFormController/outRegisterAppicationForm.do"
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
	datagrid_devplan = $("#datagrid_devplan").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryDevPlan,
		queryParams: {
			dealStatus : 1,
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_devplan",
		showFooter : true,
		columns : [ [ {	field : "projectid",checkbox : true	}
		, {field : "proName",title : "项目名称",halign : 'center',fixed : true,	width : 250,sortable:true}
		, {field : "proTypeName",title : "项目类型",halign : 'center',fixed : true,	width : 80,sortable:true}
		
		, {field : "deveYear",title : "开发年度",halign : 'center',fixed : true,	width : 60,sortable:true,align:'right'}
		, {field : "implementOrganName",title : "实施机构",halign : 'center',fixed : true,	width : 200,sortable:true,hidden:true}
		, {field : "implementPerson",title : "实施机构联系人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "implementPhone",title : "实施机构联系人电话",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "purchaseTypeName",title : "项目采购方式",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "remark",title : "项目情况说明",halign : 'center',fixed : true,	width : 120,sortable:true}
		
		, {field : "amount",title : "总投资",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "proYear",title : "合作年限",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "proTradeName",title : "所属行业",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proPerateName",title : "运作方式",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proReturnName",title : "回报机制",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proSendtypeName",title : "项目发起类型",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proPerson",title : "项目联系人",halign : 'center',fixed : true,	width : 120,sortable:true}
		
		
		, {field : "createusername",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "createuser",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "createtime",title : "创建时间",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "updateusername",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "updateuser",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "updatetime",title : "修改时间",halign : 'center',fixed : true,	width : 120,sortable:true}
		
		, {field : "deveid",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proType",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proPerate",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proTrade",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proReturn",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proSendtype",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "purchaseType",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "implementOrgan",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "devPath",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		]]
	});
}

function loadqryconditon(){
	$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "未处理", value : "1"},{text : "已处理", value : "2"}, ],
		onSelect : function(record) {
			qryDevPlan();
			switch (record.value) {
			case '2':
				$('#btn_add').linkbutton('disable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				break;
			case '1':
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('disable');
				$('#btn_remove').linkbutton('disable');
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
	
	$('#btn_edit').linkbutton('disable');
	$('#btn_remove').linkbutton('disable');
}
/**
 * 查询
 */
function qryDevPlan(){
	var param = {
			dealStatus : $("#dealStatus").combobox('getValue'),
			proName : $("#proName").textbox('getValue'),
			proTrade : $("#proTrade").treeDialog('getValue'),//所属行业
			proPerate : $("#proPerate").combobox("getValues").join(","),//项目运作方式
			proReturn : $("#proReturn").combobox('getValues').join(","),//回报机制
			proSendtype : $("#proSendtype").combobox('getValues').join(","),//项目发起类型
			proType : $('#proType').combobox('getValues').join(","),//项目类型
			proPerson :  $('#proPerson').textbox('getValue'),//项目联系人
			menuid : menuid
			
		};
	$("#datagrid_devplan").datagrid("load",param);
}

function addDevPlan(){
	var rows = datagrid_devplan.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(400, "devplan_form", types.add, datagrid_devplan, "开发计划录入",urls.optDevPlanView+"?optFlag="+types.add, urls.saveDevPlan+"?optFlag="+types.add);
}

function editDevPlan(){
	var rows = datagrid_devplan.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(400, "devplan_form", types.edit, datagrid_devplan, "开发计划修改",urls.optDevPlanView+"?optFlag="+types.edit, urls.saveDevPlan+"?optFlag="+types.edit);
}

function detDevPlan(){
	var rows = datagrid_devplan.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(400, "devplan_form", types.view, datagrid_devplan, "开发计划详情",urls.optDevPlanView+"?optFlag="+types.view, urls.saveDevPlan+"?optFlag="+types.view);
}

function showModalDialog(height, form, operType, dataGrid, title, href, url) {
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		//iconCls : icon,
		width : 770,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			if(operType!="view"){
				var deveids = row.deveid;
				if(operType=="add"){
					deveids = "";
				}
				$.post(urls.qryImlplanIsAudit, {
					deveid :deveids
				}, function(result) {
					if(result.success==true || result.success=='true'){
						mdDialog.find("#implementOrganName").attr("readonly",true);
					}else{
						//实施机构
						mdDialog.find('#implementOrganName').treeDialog({
							title :'选择实施机构',
							dialogWidth : 420,
							dialogHeight : 500,
							hiddenName:'implementOrgan',
							prompt: "请选择实施机构",
							multiSelect: false, //单选树
							dblClickRow: true,
							textModel:name,
							queryParams : {
								menuid : menuid
							},
							url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
							checkLevs: [1,2,3], //只选择3级节点
							elementcode : "BDGAGENCY",
							filters:{
								name: "机构名称"
							}
						});
					}
				}, "json");
				showFileDiv(mdDialog.find("#kfjh"),true, row.devPath, "20","devPath");//开发计划
			}else{
				showFileDiv(mdDialog.find("#kfjh"),false, row.devPath, "20","devPath");//开发计划
			}
			var f = parent.$.modalDialog.handler.find('#' + form);
			//加载表单不能放置在ajax中，要不然下拉列表的箭头不出来
			comboboxFuncByCondFilter(menuid,"purchaseType", "PURCHASE", "code", "name", mdDialog);//项目采购方式
			//加载表单内容必须放在最后，否则下拉列表不出现。
			f.form("load", row);
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};

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
	}else{
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url, form,operType);
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
					datagrid_devplan.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};

function delDevPlan(){
	var rows = datagrid_devplan.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条项目数据！");
		return;
	}
	var deveids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			deveids = rows[i].deveid;
		}else{
			deveids += ","+rows[i].deveid;
		}
	}
	parent.$.messager.confirm("确认撤回", "确定删除选择的项目数据？", function(r) {
		if (r) {
			$.post(urls.delDevPlan, {
				deveids :deveids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_devplan.datagrid("reload");
				});
			}, "json");
		}
	});
}

