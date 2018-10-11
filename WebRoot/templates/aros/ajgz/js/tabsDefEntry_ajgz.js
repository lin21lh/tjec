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

var datagrid_tabDef = null;
var edit_flag = false; // 是否正在编辑的标志 可以为true,false
var last_edit_index = null; // 正在编辑的数据行

var checksubmitflg = false; // 防止重复提交参数 true ：表示当前已提交；false：表示当前未提交

// 定义折叠所需参数（必需），数组分对应左、右两个datagrid上面的工具栏
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#datagrid_tabDef", // 要调整的Grid
	buttonid : '#openclose' // 折叠按钮id
} ];

$(function() {
	
	// 加载数据表数据
	loadTabDefDataGrid();
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(datagrid_tabDef);

	//查询条件-表类型
	comboboxFunc('crit_tabletype', 'SYS_TABLE_TYPE', 'code');
	
	$('#crit_tabletype').combobox({multiple : true, width : '200px', 'onHidePanel' : function() {queryTables();}});
	
	//查询条件控件添加清空按钮
	var iconOpts = {iconCls:'icon-clear'};
	$("#crit_tablecode").textbox('addClearBtn', iconOpts);
	$("#crit_tablename").textbox('addClearBtn', iconOpts);
	
});

function initTabDefForm() {
	$("#tablecode").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});
	
	$("#tablename").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});
	
	$("#remark").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});
	
	
	//初始化表类型
	comboboxFunc('tabletype', 'SYS_TABLE_TYPE', 'code');
	$('#tabletype').combobox({
		onChange : formchanged
	});
	setSaveButtonStatus(false);
}

/**
 * 加载表字段定义数据表格
 */
function loadTabDefDataGrid() {
	datagrid_tabDef = $("#datagrid_tabDef").datagrid({
		fit : true,
		border : false,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		view : scrollview,
		pageSize : 10,
		url : "queryCaseBaseinfoList.do",
		idField : 'tableid',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_tabDef",
		columns : [ [ {
			field : "caseid",
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
			field : "defname",
			title : "被申请人",
			halign : 'center',
			width : 150,
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
				loadTabDefDataGrid12(caseid);
			}
				
		},
		onLoadSuccess : function(data) {
			datagrid_tabDef.datagrid('clearSelections'); //数据表列表加载成功之后清除以前选项
			//console.log('load :');
			if (last_edit_index != null)
				datagrid_tabDef.datagrid('selectRow', last_edit_index);
			else
				datagrid_tabDef.datagrid('selectRow', 0);
		},
		onLoadError : function() {
			
		}
	});
}

/**
 * 加载表字段定义数据表格
 */
function loadTabDefDataGrid12(caseid) {
	datagrid_tabDef12 = $("#datagrid_tabDef12").datagrid({
		fit : true,
		border : false,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		view : scrollview,
		pageSize : 100,
		url : "queryList.do"+"?caseid="+caseid+"&menuid="+menuid,
		idField : 'tableid',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_tabDef",
		columns : [ [ {
			field : "id",
			checkbox : true
		}, {
			field : "caseid",
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
			field : "defname",
			title : "被申请人",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "exectypename",
			title : "被执行人执行情况",
			halign : 'center',
			width : 150,
			sortable : true
		} , {
			field : "remark",
			title : "执行情况说明",
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
		
		onLoadSuccess : function(data) {
			
			//console.log('load :');
			if (last_edit_index != null)
				datagrid_tabDef12.datagrid('selectRow', last_edit_index);
			else
				datagrid_tabDef12.datagrid('selectRow', 0);
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
	var url =contextpath + "aros/ajgz/BCasetracebaseinfoController/add.do"+"?caseid="+caseid;
	var saveUrl=contextpath + "aros/ajgz/BCasetracebaseinfoController/save.do"+"?caseid="+caseid;
	parent.$.modalDialog({
		title : "案件评价管理",
		width : 850,
		height : 600,
		href : url,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量			
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
function submitForm(url, form, workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + form);
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

				datagrid_tabDef12.datagrid('reload');
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
	
	
	var selectRow = datagrid_tabDef12.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}
	
	var row = datagrid_tabDef12.datagrid("getSelections")[0];
	var id = row.id;	
	var caseid = row.caseid;
	var url =contextpath + "aros/ajgz/BCasetracebaseinfoController/add.do";
	var saveUrl=contextpath + "aros/ajgz/BCasetracebaseinfoController/save.do";
	
	parent.$.modalDialog({
		title : "委员信息修改",
		width : 850,
		height : 600,
		href : url+"?id="+id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "quatype", "CASEQUATYPE", "code", "name", mdDialog);// 案件处理质量						
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
	
	var selectRow = datagrid_tabDef12.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = datagrid_tabDef12.datagrid("getSelections")[0];
			$.post(url, {
				id : row.id
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_tabDef12.datagrid("reload");
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
	
	var selectRow = datagrid_tabDef12.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	
	var row = datagrid_tabDef12.datagrid("getSelections")[0];
	var id = row.id;	
	var caseid = row.caseid;
	
	parent.$.modalDialog({
		title : "案件跟踪详情",
		width : 850,
		height : 600,
		href : url + "?id=" + id+"&caseid="+caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
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