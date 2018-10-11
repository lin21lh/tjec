//请求路径
var baseUrl = contextpath + "aros/zlpc/controller/ZlpcController/";
var urls = {
	queryUrl:baseUrl + "zbQuery.do",			
	formUrl:baseUrl + "zbForm.do",
	addUrl:baseUrl + "zbAdd.do",
	updateUrl:baseUrl + "zbUpdate.do",
	deleteUrl:baseUrl + "zbDelete.do",
	detailUrl:baseUrl + "zbDetail.do"
};

var panel_ctl_handles = [{
	panelname:"#zbfzbPanel", 		// 要折叠的面板id
	gridname:"#dataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	$('#zbfzbPanel').panel('close');
	//加载Grid数据
	loadDataGrid(urls.queryUrl);
	
	comboboxFuncByCondFilter(menuid,"stagetype", "ZB_STAGETYPE", "code", "name");
	comboboxFuncByCondFilter(menuid,"inditype", "ZB_INDITYPE", "code", "name");
});
//全局变量
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
			menuid:menuid
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"standid", checkbox:true}
		  ,{field:"standardname", title:"指标名称", halign:"center", width:250, sortable:true}
		  ,{field:"stagetypeMc", title:"阶段类型", halign:"center", width:150, sortable:true}
		  ,{field:"inditypeMc", title:"指标类型", halign:"center", width:150, sortable:true}
		  ,{field:"score", title:"分值", halign:"center", width:150, sortable:true}
		  ,{field:"seqno", title:"展示顺序", halign:"center", width:150, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function zbQuery(){
	
	var param = {
		menuid:menuid,
		standardname :  $("#standardname").val(),
		stagetype : $("#stagetype").combobox('getValues').join(","),
		inditype : $("#inditype").combobox('getValues').join(",")
	};
	dataGrid.datagrid("load", param);
}

function zbAddForm(){
	var row = dataGrid.datagrid("getChecked")[0];
	parent.$.modalDialog({
		title : "质量评查指标",
		width : 900,
		height : 330,
		href : urls.formUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"stagetype", "ZB_STAGETYPE", "code", "name", mdDialog);
			comboboxFuncByCondFilter(menuid,"inditype", "ZB_INDITYPE", "code", "name", mdDialog);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				submitForm(urls.addUrl,"dataForm","");
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

function zbUpdateForm(){
	var selectRow = dataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = dataGrid.datagrid("getChecked")[0];
	parent.$.modalDialog({
		title : "质量评查指标",
		width : 900,
		height : 330,
		href : urls.formUrl + "?standid=" + row.standid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"stagetype", "ZB_STAGETYPE", "code", "name", mdDialog);
			comboboxFuncByCondFilter(menuid,"inditype", "ZB_INDITYPE", "code", "name", mdDialog);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				submitForm(urls.updateUrl,"dataForm","");
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

function submitForm(url,form,flag){
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
			return true;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			//字符串转化对象
			result = $.parseJSON(result);
			if (result.success) {
				easyui_auto_notice(result, function() {
					dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				});
				
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

function zbDelete(){
	var selectRow = dataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = dataGrid.datagrid("getChecked")[0];
	parent.$.messager.confirm("确认删除", "确认删除选中数据？", 
			function(r) {
				if (r) {
					$.post(urls.deleteUrl, 
						{standid : row.standid}, 
						function(result) {
							easyui_auto_notice(result, function() {
								dataGrid.datagrid('reload');
							});
						},
						"json");
				}
			});
}

function zbDetail(){
	var selectRow = dataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = dataGrid.datagrid("getChecked")[0];
	parent.$.modalDialog({
		title : "质量评查指标",
		width : 900,
		height : 330,
		href : urls.detailUrl + "?standid="+row.standid,
		onLoad : function() {
			
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

