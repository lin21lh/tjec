//请求路径
var baseUrl = contextpath + "aros/sqbl/controller/ApplyRecordController/";
var urls = {
	gridUrl:baseUrl + "query.do"
	,addUrl: baseUrl + "add.do"
	,saveUrl: baseUrl + "save.do"
	,sendUrl: baseUrl + "save.do?sendflag=1"
	,deleteUrl: baseUrl + "delete.do"
	,detailUrl: baseUrl + "detail.do"
};

var panel_ctl_handles = [{
	panelname:"#panel", 		// 要折叠的面板id
	gridname:"#dataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
	//加载Grid数据
	comboboxFuncByCondFilter(menuid, "issend", "ISORNOT", "code", "name");	                // 行政管理类型
	comboboxFuncByCondFilter(menuid, "iscase", "ISORNOT", "code", "name");	                // 行政管理类型
	$("#issend").combobox("setValue","0");
	$("#iscase").combobox("setValue","0");
	loadDataGrid(urls.gridUrl);
	var icons = {iconCls:"icon-clear"};
	$("#appname").textbox("addClearBtn", icons);
	$("#inquirer").textbox("addClearBtn", icons);
	$("#starttime").textbox("addClearBtn", icons);
	$("#endtime").textbox("addClearBtn", icons);
	$("#issend").textbox("addClearBtn", icons);
	$("#iscase").textbox("addClearBtn", icons);
//	$('#panel').panel('close');
});

var dataGrid;

function showReload(){
	dataGrid.datagrid("reload");
}

//加载可项目grid列表
function loadDataGrid(url) {
	
	dataGrid = $("#dataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		queryParams:{
			menuid:menuid,
			appname:$("#appname").val(),
			inquirer:$("#inquirer").val(),
			starttime:$("#starttime").datebox('getValue'),
			endtime:$("#endtime").datebox('getValue'),
			issend:$("#issend").combobox('getValue'),
			iscase:$("#iscase").combobox('getValue')
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[
		  {field:"arid", checkbox:true},
		  {field:"appname", title:"申请人", halign:"center", sortable:true },
		  {field:"sexname", title:"性别", halign:"center", sortable:true},
		  {field:"age", title:"年龄", halign:"center",  sortable:true},
		  {field:"phone", title:"联系电话", halign:"center",  sortable:true},
		  {field:"address", title:"联系地址", halign:"center", sortable:true},
		  {field:"workunits", title:"工作单位", halign:"center", width:180, sortable:true},
		  {field:"reqtime", title:"时间", halign:"center", width:100, sortable:true},
		  {field:"place", title:"地址", halign:"center", width:150, sortable:true},
		  {field:"inquirer", title:"调查人", halign:"center", width:150, sortable:true},
		  {field:"noter", title:"记录人", halign:"center", width:180, sortable:true},
		  {field:"issolvename", title:"是否和解", halign:"center", width:50, sortable:true},
		  {field:"issue", title:"问", halign:"center", width:250,  sortable:true},
		  {field:"answer", title:"答", halign:"center", width:250, sortable:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function query(){
	var param = {
			appname:$("#appname").val(),
			inquirer:$("#inquirer").val(),
		starttime:$("#starttime").datebox('getValue'),
		endtime:$("#endtime").datebox('getValue'),
		issend:$("#issend").combobox('getValue'),
		iscase:$("#iscase").combobox('getValue'),
		menuid:menuid
	};
	dataGrid.datagrid("load", param);
}

//初始复议申请页面
function add(){
	parent.$.modalDialog({
		title:"新增",
		width:900,
		height:500,
		href:urls.addUrl + "?menuid="+ menuid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
		},
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.saveUrl, "form");
				}
			},{
				text:"发送",
				iconCls:"icon-redo",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.sendUrl, "form");
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
		}]
	});
}

//提交表单
function submitForm(url, form){
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
		
	form.form("submit", {
		url:url,
		onSubmit:function() {
			parent.$.messager.progress({
				title:"提示",
				text:"数据处理中，请稍后...."
			});
			var isValid = form.form("validate");
			if (!isValid) {
				parent.$.messager.progress("close");
			}
			return isValid;
		},
		success:function(result) {
			parent.$.messager.progress("close");
			result = $.parseJSON(result);
			if (result.success) {
				dataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 
 */
function edit(){
	
	var selectRow = dataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"修改",
		width:900,
		height:500,
		href:urls.addUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();

			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#form');
			f.form("load", row);
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "form", "");
			}
		},{
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.sendUrl, "form");
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}

/**
 * 删除被复议申请
 */
function _delete(){
	
	var selectRow = dataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	
	var row = dataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中数据删除？", function(r) {
		if (r){
			$.post(urls.deleteUrl, {id:row.arid},
					function(result){
						easyui_auto_notice(result, function() {
							dataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看被复议案件详细信息
 */
function detail(){
	var selectRow = dataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	var row = dataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title:"详情",
		width:900,
		height:500,
		href:urls.detailUrl + "?id=" + row.arid,
		onLoad:function() {},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}

