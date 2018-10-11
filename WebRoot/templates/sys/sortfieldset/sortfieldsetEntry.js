
/**
 * 排序字段设置模块-js脚本
 */
var baseUrl = contextpath + 'sys/sortfieldset/SysSortFieldSetController/';
var urls = {
	querySortFieldSet : baseUrl + 'query.do',
	formSortFieldSet : baseUrl + 'formEntry.do',
	addSortFieldSet : baseUrl + 'add.do',
	editSortFieldSet : baseUrl + 'edit.do',
	deleteSortFieldSet : baseUrl + 'delete.do'
	
};

var sortfieldSetDataGrid;
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : '#datagrid_sortfieldset', // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

//操作类型定义
var types = {
	view : "view",
	add : "add",
	edit : "edit"
};

$(function() {
	
	comboboxFunc('opertype', 'SYS_OPERTYPE', 'code', 'name');
	$('#qpanel1').panel('close');
	loadSortDataGrid(urls.queryLog);
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(sortfieldSetDataGrid);
	
	var icons = {iconCls:"icon-clear"};
	$('#opertype').combobox('addClearBtn', icons);
	$('#starttime').combobox('addClearBtn', icons);
	$('#endtime').combobox('addClearBtn', icons);
	
});

function loadSortDataGrid(url) {

	sortfieldSetDataGrid = $('#datagrid_sortfieldset').datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		view : scrollview,
		pageSize : 30,
		url : urls.querySortFieldSet,
		loadMsg : '正在加载，请稍候......',
		toolbar : '#toolbar_sort',
		columns : [ [ {
			field : 'sortid',
			checkbox : true
		}, {
			field : 'resourcename',
			halign : 'center',
			title : '资源（菜单）名称',
			width : 140
		}, {
			field : 'rolename',
			halign : 'center',
			title : '角色名称',
			width : 120,
			sortable:true
		}, {
			field : "username",
			halign : 'center',
			title : "用户名称",
			width : 100,
			sortable:true
		}, {
			field : 'scrareano',
			halign : 'center',
			title : '屏幕区域编号',
			width : 100,
			sortable:true
		}, {
			field : 'sortname',
			halign : 'center',
			title : '排序名称',
			width : 120,
			sortable:true
		}, {
			field : 'orderno',
			halign : 'center',
			title : '顺序号',
			width : 80,
			sortable:true
		}, {
			field : 'sortstr',
			halign : 'center',
			title : '排序字串',
			width : 100
		}, {
			field : 'status',
			halign : 'center',
			title : '状态',
			width : 80,
			formatter : function(value, row, index) {
				if (value == 0)
					return '启用';
				else
					return '停用';
			}
		} ] ]
	});

}

//查询
function querySort() {

	sortfieldSetDataGrid.datagrid('load', {
		sortname : $('#sortname_query').textbox('getValue')
	});
}

/**
 * 添加
 */
function addSortFieldSet() {
	var paramJson = {};
	paramJson.height = 360;
	paramJson.width = 600;
	paramJson.form = 'sortFieldSetForm';
	paramJson.operType = types.add;
	paramJson.title = '新增排序字段设置';
	paramJson.dataGrid = sortfieldSetDataGrid;
	paramJson.href = urls.formSortFieldSet;
	paramJson.url = urls.addSortFieldSet;
	paramJson.fill = false;
	showModalDialog(paramJson);
}

/**
 * 修改
 */
function editSortFieldSet() {
	var paramJson = {};
	paramJson.height = 360;
	paramJson.width = 600;
	paramJson.form = 'sortFieldSetForm';
	paramJson.operType = types.add;
	paramJson.title = '修改排序字段设置';
	paramJson.dataGrid = sortfieldSetDataGrid;
	paramJson.href = urls.formSortFieldSet;
	paramJson.url = urls.editSortFieldSet;
	paramJson.fill = true;
	showModalDialog(paramJson);
}

/**
 * 删除排序字段设置
 */
function deleteSortFieldSet() {
	var rows = sortfieldSetDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		easyui_warn("请选择一行数据！");	
		return;
	}
	
	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var sortids = '';
			rows = sortfieldSetDataGrid.datagrid("getSelections");
			for (var i=0; i<rows.length; i++) {
				if (sortids.length > 0)
					logids += ',';
				sortids += rows[i].sortid;
			}
			$.post(urls.deleteSortFieldSet, {
				sortid : sortids
			}, function(result) {
				easyui_auto_notice(result, function() {
					sortfieldSetDataGrid.datagrid("reload");
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
function showModalDialog(paramJSON) {

	if (paramJSON.fill) {
		var rows = paramJSON.dataGrid.datagrid("getSelections");
		if (rows.length != 1) {
			easyui_warn("请选择一行数据！");
			return;
		}
	}
	
	var icon = 'icon-' + paramJSON.operType;
	parent.$.modalDialog({
		title : paramJSON.title,
		iconCls : icon,
		width : paramJSON.width,
		height : paramJSON.height,
		href : paramJSON.href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var oldResourceValue;
			mdDialog.find('#resourcename').treeDialog({
				title :'选择功能菜单',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'resourceid',
				valueModel : 'id',
				prompt: "请选择功能菜单",
				editable :false,
				multiSelect: false, //单选树
				dblClickRow: true,
				clearFunc:function(){
					//TODO 点击清空按钮触发清除事件
					mdDialog.find('#rolename').treeDialog('clear');
				},
				assignFunc:function(value){
					//TODO 双击选择回填时触发赋值事件
					if (oldResourceValue != value)
						mdDialog.find('#rolename').treeDialog('clear');
				},
				beforeSearchFunc : function() {
					oldResourceValue =  mdDialog.find('#resourcename').treeDialog('getValue');
				},
				url :contextpath + '/sys/SysResourceController/queryBusiness.do',
				checkLevs: [1,2,3], //只选择3级节点
				filters:{
					code: "单位编码",
					name: "单位名称"
				}
			});
			var oldRoleValue;
			mdDialog.find('#rolename').treeDialog({
				title :'选择角色',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'roleid',
				prompt: "请选择角色",
				valueModel : 'id',
				editable :false,
				multiSelect: false, //单选树
				dblClickRow: true,
				url :contextpath + '/sys/SysRoleResourceController/findRoleListByResource.do',
				checkLevs: [1,2,3], //只选择3级节点
				filters:{
					code: "角色编码",
					name: "角色名称"
				},
				clearFunc:function(){
					//TODO 点击清空按钮触发清除事件
					mdDialog.find('#username').gridDialog('clear');
				},
				assignFunc:function(value){
					//TODO 双击选择回填时触发赋值事件
					if (oldRoleValue != value)
						mdDialog.find('#username').gridDialog('clear');
				},
				beforeSearchFunc : function() {
					var resourceid = mdDialog.find('#resourcename').treeDialog('getValue');
					oldRoleValue = mdDialog.find('#rolename').treeDialog('getValue');
					if (!resourceid) {
						easyui_warn('请先选择功能菜单！');
						return false;
					}
					
					return {'resourceid' : resourceid};
				}
			});
			
			mdDialog.find('#username').gridDialog({
				title :'用户列表选择',
				dialogWidth : 640,
				dialogHeight : 400,
				singleSelect: true,
				dblClickRow : true,
				hiddenName:'userid',
				valueField:'userid',
				textField:'username',
				prompt:"请选择用户",
				filter: {
							cncode: "用户编码",
							code: "usercode",
							cnname: "用户名称",
							name : "username"
						},
				url : 'sys/SysUserController/findUserListByRoleid.do',
				cols : [[
					{field : "userid", checkbox : true}, 
					{field : "usercode",title : "用户编码",width : 140},
			    	{field : "username", title : "用户名称", width : 180},
			    	{field : "orgname", title : "机构", width : 100},
			    	{field : "cnstatus", title : "状态", width : 80}
			    ]],
			    beforeSearchFunc:function() {
			    	var roleid = mdDialog.find('#rolename').treeDialog('getValue');
					if (!roleid) {
						easyui_warn('请先选择角色！');
						return false;
					}
					
					return {'roleid' : roleid};
				}
			});
			
			if (paramJSON.fill) {
				var row = paramJSON.dataGrid.datagrid("getSelections")[0];
				var f = mdDialog.find('#' + paramJSON.form);
				f.form("load", row);
			}
		},
		buttons : funcOperButtons(paramJSON.operType, paramJSON.url, paramJSON.dataGrid, paramJSON.form)
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
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}