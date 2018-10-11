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
var caseEstDataGrid = null;
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
	
	// 加载数据表数据
	loadCaseDataGrid();
	
});



/**
 * 加载数据表格
 */
function loadCaseDataGrid() {
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
		
		},{
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
		}, {
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
				loadCaseEstDataGrid(caseid);
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
 * 加载数据表格
 */
function loadCaseEstDataGrid(caseid) {
	$("#s_caseid").val(caseid);
	caseEstDataGrid = $("#caseEstTab").datagrid({
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
		toolbar : "#toolbar_est",
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
			field : "appname",
			title : "申请人",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "quatypename",
			title : "案件执行质量",
			halign : 'center',
			width : 100,
			sortable : true
		} , {
			field : "remark",
			title : "评价说明",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "opttime",
			title : "录入日期",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "defname",
			title : "被申请人",
			halign : 'center',
			hidden : true
		}  ] ],
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
			var rows = caseEstDataGrid.datagrid('getRows');
			if (rows == null || rows.length == 0 ) {
				return false;
			}
			caseEstDataGrid.datagrid('clearSelections'); //数据表列表加载成功之后清除以前选项
			if (last_edit_index != null)
				caseEstDataGrid.datagrid('selectRow', last_edit_index);
			else
				caseEstDataGrid.datagrid('selectRow', 0);
		},
		onLoadError : function() {
			
		}
	});
}

//校验当前表单数据状态
function validEdit() {
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
}

function queryTables() {

	if (!validEdit())
		return ;
	
	datagrid_tabDef.datagrid("load", {
		tablecode : $("#crit_tablecode").textbox("getValue"),
		tablename : $("#crit_tablename").textbox("getValue"),
		tabletype : $("#crit_tabletype").combobox("getValues").join(",")
	});
	
}

// 准备添加页面
function addTable() {
	if (!validEdit())
		return ;
	clearTableForm();
	$('#tabletype').combobox('setValue', '0'); //赋默认值
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
	datagrid_tabDef.datagrid('clearSelections'); //点击添加清除以前选项
}

//保存操作
function saveTable() {
	
	if (checksubmitflg) // 未响应之间不允许再次提交。
		return;
	$('#tabForm').form('submit', {
		url : urls.editTable,
		onSubmit : function() {
			var bl = $(this).form('validate');
			if (!bl) {
				easyui_warn('数据表表单信息未验证通过！', function() {
					$('#tabForm').form('validate');
				});
			} else {
				checksubmitflg = true;
			}
			return bl;
		},
		success : function(data) {
			data = eval("(" + data + ")");
			easyui_auto_notice(data, function() {
				// 操作成功
				edit_flag = false;
				setSaveButtonStatus(edit_flag);
				last_edit_index = null;
				checksubmitflg = false; // 响应之后 将参数置为未提交状态
				datagrid_tabDef.datagrid('reload');
			}, function() {
				edit_flag = false;
				checksubmitflg = false; // 响应之后 将参数置为未提交状态
			}, '保存过程中发生异常！');
		}
	});
}

// 删除操作
function delTable() {

	if (!validEdit())
		return ;
	
	var rows = datagrid_tabDef.datagrid('getSelections');
	if(rows.length != 1){
		easyui_warn("请选择一条数据！");
		return;
	}
	
	var sel = datagrid_tabDef.datagrid('getSelected');
	if (sel&&sel.tableid == 0) {
		// 临时数据
		var index = datagrid_tabDef.datagrid('getRowIndex', sel);
		datagrid_tabDef.datagrid('deleteRow', index);
		$('#tabForm').form('clear');
		// 改成非编辑状态
		edit_flag = false;
		setSaveButtonStatus(edit_flag);

	} else {
		parent.$.messager.confirm('确认', '是否删除数据表及关联的字段、界面方案?', function(r) {
			if (r) {
				if (checksubmitflg) // 未响应之间不允许再次提交。
					return;
				
				checksubmitflg = true; 
				$.post(urls.removeTable, {
					tableid : sel.tableid
				}, function(msg) {
					checksubmitflg = false;
					easyui_auto_notice(msg, function() {
						queryTables();
						clearTableForm();
					}, function() {
						checksubmitflg = false;
					}, "删除失败！");
				}, 'json');
			}
		});

	}
}

//取消编辑
function cancelEdit() {
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
	var sel = datagrid_tabDef.datagrid('getSelected');
	if (sel&&sel.tableid) {
		// 修改取消
		loadTableForm(sel);
	} else {
		// 添加取消
		clearTableForm();
		datagrid_tabDef.datagrid('selectRow', 0);
	}
}

// 设置保存按钮的状态
function setSaveButtonStatus(isOn) {
	if (isOn) {
		$('#btn_save').linkbutton('enable');
		$('#btn_cancel').linkbutton('enable');
	} else {
		$('#btn_save').linkbutton('disable');
		$('#btn_cancel').linkbutton('disable');
	}
}

function formchanged(nv, ov) {
	edit_flag = true;
	setSaveButtonStatus(true);
	// 保存正在修改的id
	var sel = datagrid_tabDef.datagrid('getSelected');
	last_edit_index = datagrid_tabDef.datagrid('getRowIndex', sel);
}

/**
function loadTableForm(tableid) {
	$('#tabForm').form('load', urls.getTable + "?tableid=" + tableid);
} */

function loadTableForm(rowData) {
	$('#tabForm').form('load', rowData);
}

function clearTableForm() {
	$('#tabForm').form('clear');
}

function tableColsConfig() {
	
	if (!validEdit())
		return ;
	
	tabColConfig();
}

/**
 * 界面设计
 */
function tabUiDesign() {
	
	if (!validEdit())
		return ;
	
	var records = datagrid_tabDef.datagrid('getSelections');
	if (records.length == 0) {
		easyui_warn('请选择一个数据表！');
		return;
	} else if (records.length > 1) {
		easyui_warn('当前仅能选择一个数据表！');
		return;
	} 
	var sel_tablecode = records[0].tablecode;
	var sel_tablename = records[0].tablename;
	var url_ = contextpath+ "base/tabsdef/SysDicUIDesignController/formDesignerEntry.do?tablecode=" + sel_tablecode;
	parent.$.fastModalDialog({
				title : '界面设计器（' + sel_tablecode + '-' + sel_tablename + '）',
				width : 980,
				height : 660,
				iconCls : 'icon-design',
				html : "<div><iframe src='"
						+ url_
						+ "' frameborder='0' width='100%' height='100%' scrolling='no' /></div>",
				dialogID : 'ndlg',
				onOpen : function() {
					var grid_ = parent.$.fastModalDialog.handler['ndlg'].find('#grid_9981');

				},
				buttons : null
			});

}

/**
 * 新增
 */
function projectAdd() {
	var url =contextpath + "aros/ajpj/BCaseestbaseinfoController/add.do"+"?caseid="+caseid;
	var saveUrl=contextpath + "aros/ajpj/BCaseestbaseinfoController/save.do"+"?caseid="+caseid;
	parent.$.modalDialog({
		title : "案件评价管理",
		width : 900,
		height : 480,
		href : url,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量			
			showFileDiv(mdDialog.find("#filetd"), false, "XZFY", $("#s_caseid").val(), "");
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
	
	var quatype = form.find("#quatype").combobox("getValue");
	if(quatype==""){
		parent.$.messager.alert('系统提示', "请录入案件完成质量！", 'info');
		form.find("#tabList").tabs("select",2);
		return false;
	}
	var remark = form.find("#remark").val();
	if(remark==""){
		parent.$.messager.alert('系统提示', "请录入评价说明！", 'info');
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

				caseEstDataGrid.datagrid('reload');
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
	
	
	var selectRow = caseEstDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	
	var row = caseEstDataGrid.datagrid("getSelections")[0];
	var id = row.id;	
	var caseid = row.caseid;
	var url =contextpath + "aros/ajpj/BCaseestbaseinfoController/add.do";
	var saveUrl=contextpath + "aros/ajpj/BCaseestbaseinfoController/save.do";
	
	parent.$.modalDialog({
		title : "委员信息修改",
		width : 900,
		height : 480,
		href : url+"?id="+id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量						
			showFileDiv(mdDialog.find("#filetd"), false, "XZFY", $("#s_caseid").val(), "");
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
	var url =contextpath + "aros/ajpj/BCaseestbaseinfoController/delete.do";
	
	var selectRow = caseEstDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = caseEstDataGrid.datagrid("getSelections")[0];
			$.post(url, {
				id : row.id
			}, function(result) {
				easyui_auto_notice(result, function() {
					caseEstDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 详情
 */
function projectView() {
	var url =contextpath + "aros/ajpj/BCaseestbaseinfoController/view.do";
	
	var selectRow = caseEstDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	
	var row = caseEstDataGrid.datagrid("getSelections")[0];
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
			form.find("#h_quatypename").text(row.quatypename);
			showFileDiv(mdDialog.find("#filetd"), false, "XZFY", $("#s_caseid").val(), "");
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
		};
	caseListDataGrid.datagrid("load", param);
}