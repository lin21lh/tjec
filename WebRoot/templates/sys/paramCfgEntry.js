
/**
 * 系统参数管理模块-js脚本
 */
var baseUrl = contextpath + 'sys/SysParamCfgController/';
var urls = {
	queryParam : baseUrl + 'query.do',
	addParam : baseUrl + 'add.do',
	editParam : baseUrl + 'edit.do',
	deleteParam : baseUrl + 'delete.do',
	formParam : baseUrl + 'formEntry.do'
};

var paramDataGrid;
var panel_ctl_handles = [{
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : '#datagrid_param', // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
}];

//操作类型定义
var types = {
	view : "view",
	add : "add",
	edit : "edit"
};

$(function() {
	
	$('#qpanel1').panel('close');
	
	comboboxFunc("scenecode_query", "SYS_SCENE", "code", "", null, true);
	
	loadParamDataGrid(urls.queryParam);
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(paramDataGrid);
	
	var icons = {iconCls:"icon-clear"};
	$('#opertype').combobox('addClearBtn', icons);
	
});

function loadParamDataGrid(url) {

	paramDataGrid = $('#datagrid_param').datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		view : scrollview,
		pageSize : 30,
		url : url,
		queryParams : {
			scenecode : $('#scenecode_query').combobox('getValue'),
			paramcode : $('#paramcode').textbox('getValue'),
			paramname : $('#paramname').textbox('getValue')
		},
		loadMsg : '正在加载，请稍候......',
		toolbar : '#toolbar_paramCfg',
		columns : [ [ {
			field : 'paramid',
			checkbox : true
		}, {
			field : 'admivcode',
			title : '区域代码',
			hidden : true
		}, {
			field : 'scenename',
			title : '子系统或者模块',
			width : 120,
			sortable:true
		}, {
			field : "paramcode",
			title : "参数编码",
			width : 100,
			sortable:true
		}, {
			field : 'paramname',
			title : '参数名称',
			width : 100,
			sortable:true
		},
		{
			field : 'paramvalue',
			title : '参数值',
			width : 120,
			sortable:true
		}, {
			field : 'paramdesc',
			title : '参数描述',
			width : 140,
			sortable : true,
			styler : function(value, row, index) {
				return ;
			}
		}, {
			field : 'status',
			title : '启用状态',
			formatter : function(value) {
				if (value == '0')
					return '未启用';
				else
					return '启用';
			},
			width : 100
		} ] ]
	});
	
	datagrid_doCellTip(paramDataGrid);

}

function datagrid_doCellTip(datagrid) {
	datagrid.datagrid('doCellTip', {
		specialShowFields : [],
		onlyShowInterrupt : true, //是否只有在文字被截断时才显示tip，默认值为false，即所有单元格都显示tip。
		position:'bottom',
		tipStyler : { maxWidth:'500px', boxShadow:'1px 1px 3px #292929'},   //'backgroundColor':'#fff000', borderColor:'#ff0000',
        contentStyler : { paddingLeft:'5px'}   //backgroundColor:'#333',
	});
}

/**
 * 查询
 */
function queryParam() {

	paramDataGrid.datagrid('load', {
		scenecode : $('#scenecode_query').combobox('getValue'),
		paramcode : $('#paramcode').textbox('getValue'),
		paramname : $('#paramname').textbox('getValue')
	});
}

/**
 * 添加
 */
function addParam() {
	showModalDialog(340, "paramCfgForm", types.add, paramDataGrid, "新增系统参数", urls.formParam, urls.addParam, false);
}

/**
 * 修改
 */
function editParam() {
	showModalDialog(340, "paramCfgForm", types.add, paramDataGrid, "修改系统参数", urls.formParam, urls.editParam, true);
}

/**
 * 删除
 */
function deleteParam() {
	var rows = paramDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		easyui_warn("请选择一行数据！");	
		return;
	}
	
	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			row = paramDataGrid.datagrid("getSelected");
	
			$.post(urls.deleteParam, {
				paramid : row.paramid
			}, function(result) {
				easyui_auto_notice(result, function() {
					paramDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 弹出式窗口
 * @param height 高度
 * @param form 
 * @param operType
 * @param dataGrid
 * @param title
 * @param href
 * @param url
 * @param fill
 */
function showModalDialog(height, form, operType, dataGrid, title, href, url, fill) {

	if (fill) {
		var rows = dataGrid.datagrid("getSelections");
		if (rows.length != 1) {
			easyui_warn("请选择一行数据！");
			return;
		}
	}
	
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 600,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFunc("scenecode", "SYS_SCENE", "code", "", mdDialog, true);
			
			if (fill) {
				var row = dataGrid.datagrid("getSelections")[0];
				var f = mdDialog.find('#' + form);
				f.form("load", row);
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
}

/**
 * 提交表单
 * @param operType 操作类型
 * @param url 请求路径
 * @param dataGrid
 * @param form
 * @returns {___anonymous3768_3774}
 */
function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if (operType == types.view) {
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == types.add) {
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url, form);
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	} else if (operType == types.edit) {
		buttons = [ {
			text : "修改",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(url, form);
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	return buttons;
}

/**
 * 提交表单
 * @param url
 * @param form
 */
function submitForm(url, form) {
	var form = parent.$.modalDialog.handler.find('#' + form);
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
				parent.$.modalDialog.openner_dataGrid.datagrid({'positionScrollTop' : true});
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
				refDropdownPager(parent.$.modalDialog.openner_dataGrid);
				datagrid_doCellTip(parent.$.modalDialog.openner_dataGrid);
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}