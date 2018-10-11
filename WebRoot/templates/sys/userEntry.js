
/**
 * 用户管理模块-js脚本
 */
var baseUrl = contextpath + "sys/SysUserController/";
var urls = {
	userlist : "query.do",
	addUser : "SysUserController/add.do",
	editUser : "SysUserController/edit.do",
	removeUser : "delete.do",
	getRoleTree : '../SysUserRoleController/queryRoleByUser.do',
	editUserRoles : "../SysUserRoleController/editUserRoles.do",
	getAllAgencyTree : contextpath + 'sys/dept/sysDeptController/queryDeptTree.do',
	resetUserPasw: baseUrl + "resetUserPasw.do",
	resetAllUserPasw : baseUrl + 'resetAllUserPasw.do'
};

var userDataGrid;
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#datagrid_user", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
$(function() {

	// $("#layout").layout("collapse","east");
	$('#qpanel1').panel('close');
	loadUserDataGrid(urls.userlist);
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(userDataGrid);
	
	var icons = {iconCls:"icon-clear"};
	$("#usercode").textbox("addClearBtn", icons);
	$("#username").textbox("addClearBtn", icons);
});

function doUserQry() {
	var param = {
		usercode : $("#usercode").val(),
		username : $("#username").val()
	};
	$("#datagrid_user").datagrid("load", param);
}

function clearUserQry() {

	$("#usercode").val("");
	$("#username").val("");
	$("#datagrid_user").datagrid("reload");
}

function viewUserWin() {
	showModalDialog(280, "userForm", types.view, userDataGrid, "用户详情",
			"sys/SysUserController/userForm.do", null, true);
}

function addUserWin() {
	showModalDialog(300, "userForm", types.add, userDataGrid, "用户添加",
			"sys/SysUserController/userForm.do?type=" + types.add, urls.addUser);
}

function editUserWin() {
	var rows = userDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一行数据");
		return;
	}
	
	var usertype = rows[0].usertype;
	if(usertype == "2"){
		easyui_warn("不可修改或删除超级用户的信息！");
		return;
	}
	
	
	showModalDialog(280, "userForm", types.edit, userDataGrid, "用户修改",
			"sys/SysUserController/userForm.do", urls.editUser, true);
}

function removeUserWin() {
	var rows = userDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一行数据");	
		return;
	}

	var usertype = rows[0].usertype;
	if(usertype == "2"){
		easyui_warn("不可修改或删除超级用户的信息！");
		return;
	}
	
	parent.$.messager.confirm("确认删除", "是否确认删除这条数据记录？", function(r) {
		if (r) {
			$.post(urls.removeUser, {
				userid : rows[0].userid
			}, function(result) {
				easyui_auto_notice(result, function() {
					userDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});

}

function authUserWin(row) {

	var tree_roles = $('#lefttree');
	tree_roles.tree({
		url : urls.getRoleTree + '?userid=' + row.userid,
		method : 'post',
		animate : true,
		checkbox : true,
		cascadeCheck: false
	});

}

function checkRoleTree(data, result) {
	for ( var i = 0; i < data.length; i++) {
		var nodeid = data[i].id;
		if ($.inArray(nodeid, result) != -1) {
			var node = $("#lefttree").tree("find", nodeid);
			$("#lefttree").tree("check", node.target);
		}

		if (data[i].children) {
			checkRoleTree(data[i].children, result);
		}
	}
}
// 修改用户的角色授权
function addAuthUser() {

	var rows = $("#datagrid_user").datagrid("getSelections");
	var userid = rows[0].userid;

	var roleids = "";
	var nodes = $("#lefttree").tree("getChecked");
	for ( var i = 0; i < nodes.length; i++) {
		var node = nodes[i];
		if (i == 0) {
			roleids += node.id;
		} else {
			roleids += "," + node.id;
		}
	}

	$.post(urls.editUserRoles, {
		userid : userid,
		roleids : roleids
	}, function(result) {
		easyui_auto_notice(result);
	}, "json");

}

var user_types = {
	"0" : "普通用户",
	"1" : "管理用户",
	"2" : "超级用户"
};

var user_status = {
	"0" : "正常",
	"9" : "停用"
};

var yes_or_no = {
	"1" : "是",
	"0" : "否"
};

function loadUserDataGrid(url) {

	userDataGrid = $("#datagrid_user").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		view : scrollview,
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_user",
		showFooter : true,
		columns : [ [ {
			field : "userid",
			checkbox : true
		}, {
			field : "usercode",
			title : "用户编码",
			halign : 'center',
			width : 100
		}, {
			field : "username",
			title : "用户名称",
			halign : 'center',
			fixed : true,
			width : 120,
			sortable:true
		}, {
			field : "usertype",
			title : "用户类型",
			halign : 'center',
			width : 80,
			sortable:true,
			formatter: function(value){
				return user_types[value];
			}
		}, {
			field : "orgname",
			title : "机构名称",
			halign : 'center',
			width : 220,
			fixed : true,
			sortable:true
		},
		{
			field : "cnstatus",
			title : "状态",
			halign : 'center',
			width : 40,
			sortable:true
		}, {
			field : "createtime",
			title : "创建日期",
			halign : 'center',
			width : 80
		}, {
			field : 'updatetime',
			title :'修改日期',
			halign : 'center',
			width : 80
		}, {
			field : "overduedate",
			title : "过期日期",
			halign : 'center',
			width : 80
		} ] ],
		onSelect : function(rowIndex, rowData) {
			if (rowData.userid)
				authUserWin(rowData);
		},
		onLoadSuccess : function(data) {
			var rows = data.rows;
			if (rows.length != 0) {
				$("#datagrid_user").datagrid("checkRow", 0);
			}
		}
	});
}

function queryUsers() {

	$("#datagrid_user").datagrid("load", {
		usercode : $("#usercode").val(),
		username : $("#username").val()
	});
}

function clearUsers() {
	$("#usercode").val("");
	$("#username").val("");
	$("#datagrid_user").datagrid("load", {});
}

/**
 * 展示字段定义模板
 */
function showRoleDefPanel() {

	var main_layout = $("#layout");
	var width = main_layout.width();
	var east = main_layout.layout("panel", "east");

	var opts = east.panel("options");
	if (opts.collapsible) {
		opts.collapsible = false;
		east.panel("resize", {
			width : width / 4
		});
		main_layout.layout("expand", "east");
	}
}

var types = {
	view : "view",
	add : "add",
	edit : "edit"
};

/**
 * 弹出模式窗口
 * 
 * @param operType
 *            操作类型，主要包括查看，添加和修改
 * @param dataGrid
 *            操作的数据表格
 * @param title
 *            弹出窗的标题
 * @param href
 *            弹出窗的href
 * @param url
 *            对应操作的url
 * @param fill
 *            是否自动填充表单，主要是查看和修改
 */
function showModalDialog(width, form, operType, dataGrid, title, href, url,
		fill) {

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
		height : width,
		href : href,
		onLoad : function() {
			
			var mdDialog = parent.$.modalDialog.handler;
			
			mdDialog.find("#searchbox_agency").treeDialog({
				title :'机构选择',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'orgcode',
				prompt: "请选择机构名称",
				multiSelect: false, //单选树
				dblClickRow: true,
				checkLevs: [1,2,3], //只选择3级节点
				url : contextpath + '/sys/dept/sysDeptController/queryDeptTree.do',
				filters:{
					code: "机构编码",
					name: "机构名称"
				}
			});
			
			if (fill) {
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				mdDialog.find("#searchbox_agency").searchbox("setValue", row.orgname);
			}
			
			if(operType == types.view){
//				mdDialog.find("#usercode").textbox("disable");
//				mdDialog.find("#username").textbox("disable");
//				mdDialog.find("#datatype_edit").combobox("disable");
//				mdDialog.find("#searchbox_agency").searchbox("disable");
//				mdDialog.find("#searchbox_agency").searchbox("getIcon", 0).css("visibility","hidden");
//				mdDialog.find("#createtime").datebox("disable");
//				mdDialog.find("#overduedate").datebox("disable");
//				mdDialog.find("#remark").textbox("disable");
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});

}

/**
 * 根据操作类型来获取操作按钮
 * 
 * @param operType
 *            操作类型
 * @param url
 *            对应的操作URL
 * @returns {___anonymous3684_3690}
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
 * 
 * @param url
 *            表单url
 */
function submitForm(url, form) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	form.form("submit",
			{
				url : "sys/" + url,
				onSubmit : function() {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					var isValid = form.form('validate');
					if (!isValid) {
						parent.$.messager.progress('close');
					}

					var userpswd = parent.$.modalDialog.handler.find(
							"#userpswd").val();
					var confpswd = parent.$.modalDialog.handler.find(
							"#confpswd").val();

					if (confpswd != userpswd) {
						parent.$.messager.progress('close');
						parent.$.messager.alert("提示", "两次输入密码不一致，请重新输入",
								"warn", function() {
									$("#confpswd").val("");
									$("#confpswd").focus();
								});
						return false;
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


/**
 * 重置用户密码
 */
function resetUserPasw(){
	var rows = $("#datagrid_user").datagrid("getSelections");
	if(rows.length != 1){
		easyui_warn("请选择一条记录！");
		return;
	}
	
	var row = rows[0];
	var usertype = row.usertype;
	if(usertype == "2"){
		easyui_warn("不可重置超级用户的密码！");
		return;
	}
	
	parent.$.messager.confirm("重置密码", "是否确认重置用户" + row.username + "的密码？", function(r) {
		if (r) {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			$.post(urls.resetUserPasw, {userid:row.userid, pasw:"123456"}, function(data){
				parent.$.messager.progress('close');
				if(data.success){
					easyui_info("重置密码成功，默认密码为123456");
				}else{
					easyui_warn("重置密码操作失败！");
				}
			}, "JSON");	
		}
	});
	
}

function resetAllUserPasw() {
	parent.$.messager.confirm("重置密码", "是否确认重置全部用户的密码？", function(r) {
		if (r) {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			$.post(urls.resetAllUserPasw, {pasw:"123456"}, function(data){
				parent.$.messager.progress('close');
				if(data.success){
					easyui_warn("重置密码成功，默认密码为123456");
				}else{
					easyui_warn("重置密码操作失败！");
				}
			}, "JSON");	
		}
	});
}

function outExcelUserData() {
//	var param ={includeHidden : false, title : 'user', excelVersion : 2003, numberCols : ['userid']};
//	$("#datagrid_user").datagrid("outExcel", param);
	
	var paramJSON = {};
	
	paramJSON.filename = 'simpleDemo';
	paramJSON.excelVersion = '2007';
	paramJSON.usercode = $("#usercode").val();
	paramJSON.username = $("#username").val();
	
	outExcel('', paramJSON);
}
