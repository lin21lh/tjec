var baseUrl = contextpath + "base/tabsdef/SysDicTableController/";
var urls = {
	queryTables : baseUrl + "query.do",
	editTable : baseUrl + "edit.do",
	removeTable : baseUrl + "delete.do",
	getTable : baseUrl + "get.do",
	colsDefDialog : baseUrl + "colsDefDialog.do",
	saveXml : baseUrl + "saveXml.do",
	uiDesignDialog : baseUrl + "uiDesignDialog.do"
};

var caseid;

var caseListDataGrid = null;
var caseTraceDataGrid = null;
var edit_flag = false; // 是否正在编辑的标志 可以为true,false
var last_edit_index = null; // 正在编辑的数据行

var checksubmitflg = false; // 防止重复提交参数 true ：表示当前已提交；false：表示当前未提交

// 定义折叠所需参数（必需），数组分对应左、右两个datagrid上面的工具栏
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#caseListTab", // 要调整的Grid
	buttonid : '#openclose' // 折叠按钮id
} ];

$(function() {
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");
	// 在查询条件中添加删除按钮
	$("#casetype").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#appname").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#defname").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#csaecode").textbox("addClearBtn", {iconCls:"icon-clear"});
	// 加载数据表数据
	loadCaseListDataGrid();
});

/**
 * 加载表字段定义数据表格
 */
function loadCaseListDataGrid() {
	caseListDataGrid = $("#caseListTab").datagrid({
		fit : true,
		border : false,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		view : scrollview,
		pageSize : 10,
		url : "queryCaseBaseinfoList.do",
		idField : 'caseid',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_list",
		columns : [ [ {field : "caseid",checkbox : true
		
		},  {
			field : "csaecode",
			title : "案件编号",
			halign : 'center',
			width : 120,
			sortable : true
		}, {
			field : "appname",
			title : "申请人",
			halign : 'center',
			width : 130,
			sortable : true
		}, {
			field : "defname",
			title : "被申请人",
			halign : 'center',
			width : 130,
			sortable : true
		},{
			field : "casetype",
			title : "复议事项类型",
			halign : 'center',
			width : 130,
			sortable : true
		},{
			field : "appdate",
			title : "申请时间",
			halign : 'center',
			width : 100,
			sortable : true
		} ] ],
		onBeforeCheck : function(rowIndex, rowData) {
			if (edit_flag) {
				easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
					if (c != undefined) { //取消
						if (c) { //是
							saveTable();
						} else { //否
							cancelEdit();
						}
					} else {
						
					}
				});
				
				return false;
			} else
				return true;
		},
		onBeforeSelect : function(rowIndex, rowData) {
			if (edit_flag) {
				easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
					if (c != undefined) { //取消
						if (c) { //是
							saveTable();
						} else { //否
							cancelEdit();
						}
					} else {
						
					}
				});
				
				return false;
			} else
				return true;
		},
		onSelect : function(rowIndex, rowData) {
			if (rowData){
				caseid=rowData.caseid;				
				loadCaseTraceDataGrid(caseid);
			}
				
		},
		onLoadSuccess : function(data) {
			caseListDataGrid.datagrid('clearSelections'); //数据表列表加载成功之后清除以前选项
			if (last_edit_index != null)
				caseListDataGrid.datagrid('selectRow', last_edit_index);
			else
				caseListDataGrid.datagrid('selectRow', 0);
		},
		onLoadError : function() {
			
		}
	});
}

/**
 * 加载表字段定义数据表格
 */
function loadCaseTraceDataGrid(caseid) {
	$("#s_caseid").val(caseid);
	caseTraceDataGrid = $("#caseTraceTab").datagrid({
		fit : true,
		border : false,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		view : scrollview,
		pageSize : 100,
		url : "queryList.do"+"?caseid="+caseid+"&menuid="+menuid,
		idField : 'id',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_trace",
		columns : [ [ {
			field : "id",
			checkbox : true
		}, {
			field : "csaecode",
			title : "案件编号",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "defname",
			title : "被申请人",
			halign : 'center',
			width : 100,
		} , {
			field : "exectypename",
			title : "被执行人执行情况",
			halign : 'center',
			width : 120,
			sortable : true
		} , {
			field : "remark",
			title : "执行情况说明",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "opttime",
			title : "录入日期",
			halign : 'center',
			width : 150,
			sortable : true
		}] ],
		onBeforeCheck : function(rowIndex, rowData) {
			if (edit_flag) {
				easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
					if (c != undefined) { //取消
						if (c) { //是
							saveTable();
						} else { //否
							cancelEdit();
						}
					} else {
						
					}
				});
				
				return false;
			} else
				return true;
		},
		onBeforeSelect : function(rowIndex, rowData) {
			if (edit_flag) {
				easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
					if (c != undefined) { //取消
						if (c) { //是
							saveTable();
						} else { //否
							cancelEdit();
						}
					} else {
						
					}
				});
				
				return false;
			} else
				return true;
		},
		
		onLoadSuccess : function(data) {
			caseTraceDataGrid.datagrid('clearSelections'); //数据表列表加载成功之后清除以前选项
			caseTraceDataGrid.datagrid('selectRow', 0);
		},
		onLoadError : function() {
			
		}
	});
}

/**
 * 新增
 */
function projectAdd() {
	var url =contextpath + "aros/ajgz/BCasetracebaseinfoController/add.do"+"?caseid="+caseid;
	var saveUrl=contextpath + "aros/ajgz/BCasetracebaseinfoController/save.do"+"?caseid="+caseid;
	parent.$.modalDialog({
		title : "案件跟踪新增",
		width : 900,
		height : 480,
		href : url,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			//comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量			
			comboboxFuncByCondFilter(menuid, "exectype", "BZXRZXQK", "code", "name", mdDialog);// 被执行人执行情况	
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(saveUrl, "projectAddForm", "");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

//提交表单
function submitForm(url, f, workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + f);
	var exectype = form.find("#exectype").combobox("getValue");
	if(exectype==""){
		parent.$.messager.alert('系统提示', "请录入被执行人执行情况！", 'info');
		form.find("#tabList").tabs("select",2);
		return false;
	}
	var remark =form.find("#remark").val();
	if(remark=="") {
		parent.$.messager.alert('系统提示', "请录入执行情况说明！", 'info');
		form.find("#tabList").tabs("select",2);
		return false;
	}
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit", {
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

				caseTraceDataGrid.datagrid('reload');
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

/**
 * 修改
 */
function projectUpdate() {
	
	
	var selectRow = caseTraceDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	
	var row = caseTraceDataGrid.datagrid("getSelections")[0];
	var id = row.id;	
	var caseid = row.caseid;
	var url =contextpath + "aros/ajgz/BCasetracebaseinfoController/add.do";
	var saveUrl=contextpath + "aros/ajgz/BCasetracebaseinfoController/save.do";
	
	parent.$.modalDialog({
		title : "案件跟踪修改",
		width : 900,
		height : 480,
		href : url+"?id="+id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			//comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量						
			comboboxFuncByCondFilter(menuid, "exectype", "BZXRZXQK", "code", "name", mdDialog);// 被执行人执行情况
			showFileDiv(mdDialog.find("#filetd"), false, "XZFY", $("#s_caseid").val(), "");
			showFileDiv(mdDialog.find("#caseTraceFile"), true, "ANGZ", id, "");
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(saveUrl, "projectAddForm", "");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

/**
 * 删除
 */
function projectDelete() {
	var url =contextpath + "aros/ajgz/BCasetracebaseinfoController/delete.do";
	
	var selectRow = caseTraceDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = caseTraceDataGrid.datagrid("getSelections")[0];
			$.post(url, {
				id : row.id
			}, function(result) {
				easyui_auto_notice(result, function() {
					caseTraceDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 详情
 */
function projectView() {
	var url =contextpath + "aros/ajgz/BCasetracebaseinfoController/view.do";
	
	var selectRow = caseTraceDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	
	var row = caseTraceDataGrid.datagrid("getSelections")[0];
	var id = row.id;	
	var caseid = row.caseid;
	
	parent.$.modalDialog({
		title : "案件跟踪详情",
		width : 900,
		height : 480,
		href : url + "?id=" + id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var form = mdDialog.find("#projectViewForm");
			form.find("#h_exectype").text(row.exectypename);
			showFileDiv(mdDialog.find("#filetd"), false, "XZFY", $("#s_caseid").val(), "");
			showFileDiv(mdDialog.find("#tracefiletd"), false, "ANGZ", id, "");
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

//查询
function toQuery(){
	var param = {
			appname : $("#appname").val(),
			defname : $("#defname").val(),
			csaecode : $("#csaecode").val(),
			casetype : $("#casetype").combobox('getValue'),
		};
	caseListDataGrid.datagrid("load", param);
}