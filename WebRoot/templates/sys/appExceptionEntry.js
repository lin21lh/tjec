
/**
 * 日志管理模块-js脚本
 */
var baseUrl = contextpath + 'sys/appexception/sysAppExceptionController/';
var urls = {
	queryAppex : baseUrl + 'query.do',
	saveAppex : baseUrl + 'saveAppException.do',
	deleteAppex : baseUrl + 'deleteAppException.do'
};
var appExDataGrid = null;
var edit_flag = false; // 是否正在编辑的标志 可以为true,false
var try_to_focus = null; // 需要选中的表 id
var last_edit_index = null; // 正在编辑的数据行

// 定义折叠所需参数（必需），数组分对应左、右两个datagrid上面的工具栏
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#datagrid_appexception", // 要调整的Grid
	// queryfunc : null, // 调整表格高度
	buttonid : '#openclose' // 折叠按钮id
} ];

var main_layout;
$(function() {
	
	$('#qpanel1').panel('close');

	// 初始化右侧表单
	initTabDefForm();
	
	// 加载数据表数据
	loadAppExDataGrid();
});

function initTabDefForm() {
	$("#excode").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});
	
	$("#exmsg").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});
	
	$("#extype").textbox().textbox("textbox").bind("change", function(){
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
function loadAppExDataGrid() {
	appExDataGrid = $("#datagrid_appexception").datagrid({
		fit : true,
		border : false,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		view : scrollview,
		pageSize : 100,
		url : urls.queryAppex,
		idField : 'excode',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_appexception",
		columns : [ [ {
			field : "excode",
			checkbox : true
		}, {
			field : "excode",
			title : "自定义异常代码",
			width : 140,
			sortable : true
		}, {
			field : "exmsg",
			title : "自定义异常信息",
			width : 140
		}, {
			field : "extype",
			title : "异常类型",
			width : 70,
			sortable : true
		}, {
			field : "remark",
			title : "备注",
			width : 225
		} ] ],
		onClickRow : function(rowIndex, rowData) {
			if (edit_flag) {
				if (rowIndex == last_edit_index) {
					return;
				}
				easyui_warn('当前有数据未保存，请先保存数据！');
				$('#datagrid_tabDef').datagrid('selectRow', last_edit_index);
				return;
			}
			loadTableForm(rowData.tableid);
		},
		onLoadSuccess : function(data) {
			var rows = data.rows;
			if (rows.length != 0) {
				// 选中编辑的行
				$(this).datagrid('selectRecord', try_to_focus);
				try_to_focus = null;
			}
		}
	});
}

function queryTables() {
	appExDataGrid.datagrid("load", {
		tablecode : $("#crit_tablecode").textbox("getValue"),
		tablename : $("#crit_tablename").textbox("getValue"),
		tabletype : $("#crit_tabletype").combobox("getValue")
	});
}

// 准备添加页面
function addAppException() {
	clearTableForm();
	edit_flag = true;
	setSaveButtonStatus(edit_flag);
}

function saveTable() {
	$('#appexForm').form('submit', {
		url : urls.editTable,
		onSubmit : function() {
			var bl = $(this).form('validate');
			if (!bl) {
				easyui_warn('请填写必填项！');
			}
			return bl;
		},
		success : function(data) {
			data = eval("(" + data + ")");
			easyui_auto_notice(data, function() {
				// 操作成功
				edit_flag = false;
				setSaveButtonStatus(edit_flag);
				try_to_focus = data.body.id;
				$('#datagrid_tabDef').datagrid('reload');
			}, null, '保存过程中发生异常！');
		}
	});
}

function delTable() {}

function cancelEdit() {
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
	var sel = $('#datagrid_tabDef').datagrid('getSelected');

	if (sel&&sel.tableid) {
		// 修改取消
		loadTableForm(sel.tableid);
		// $('#datagrid_tabDef').datagrid('reload');
	} else {
		// 添加取消
		clearTableForm();
		// queryTables();
		var rowindex = $('#datagrid_tabDef').datagrid('getRowIndex', sel);
		$('#datagrid_tabDef').datagrid('deleteRow', rowindex);
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
	var sel = $('#datagrid_tabDef').datagrid('getSelected');
	last_edit_index = $('#datagrid_tabDef').datagrid('getRowIndex', sel);
}

function loadTableForm(tableid) {
	$('#tabForm').form('load', urls.getTable + "?tableid=" + tableid);
}

function clearTableForm() {
	$('#tabForm').form('clear');
}