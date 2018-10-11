/**
 * 页面初始化jQuery脚本
 */
var baseUrl = contextpath + "sys/SysRoleController/";
var roleResourceUrl = contextpath + "sys/SysRoleResourceController/";
var sysUserRoleUrl = contextpath + "sys/SysUserRoleController/";
var urls = {
	detail : baseUrl + 'get.do',
	save : baseUrl + 'save.do',
	del : baseUrl + 'delete.do',
	getRoleTree : baseUrl + 'query.do',
	menuTree : roleResourceUrl + 'query.do',
	saveResourceToRole : roleResourceUrl + 'saveResourceToRole.do',
	roleUnselectedUserTree : sysUserRoleUrl + 'queryUnselectedUserByRole.do',
	roleSelectedUserTree : sysUserRoleUrl + 'querySelectedUserByRole.do',
	addUserToRole : sysUserRoleUrl + 'addUserToRole.do',
	removeUserFromRole : sysUserRoleUrl + 'removeUserFromRole.do',
	queryRoleResourceOper : roleResourceUrl + 'queryRoleResourceOper.do',
	editRoleResourceOper : sysUserRoleUrl + 'editRoleResourceOper.do',
	querySelectedUsers : roleResourceUrl + "querySelectedUsers.do",
	queryUnselectUsers : roleResourceUrl + "queryUnselectUsers.do",
	addRoleUser : sysUserRoleUrl + "addUserToRole.do",
	removeRoleUser : sysUserRoleUrl + "removeUserFromRole.do"
};

var last_node = null;
var tab_index = 0;
var flag_edit = false; // 是否正在编辑

$(function() {
	
	$('#roleTree').tree({
		url : urls.getRoleTree,
		method : 'post',
		animate : true,
		onContextMenu : function(e, node) {
			e.preventDefault();
			if (validFormEdit()) {
				$(this).tree('select', node.target);
				$('#contextmenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		},
		onLoadSuccess : function(node, data) {

			var firstNode = getFirstLeafNode();
			if (last_node != null) {
				showDetail(last_node);
			} else if (firstNode != null) {
				$('#roleTree').tree('select', firstNode.target);
				showDetail(firstNode);
			}
		},
		onBeforeSelect : function(node) {
			return validFormEdit();
		},
		onSelect : function(node) {
			show_tab(tab_index);
		}
	});
	$('#content').form({
		onLoadSuccess : function() {
			flag_edit = false;
			setSaveButtonStatus(flag_edit);
		}
	});

	$('#centraltab').tabs({
		onSelect : function(title, index) {
			show_tab(index);
			tab_index = index;
		}
	});

	$('#roleMenuPanel').panel({
		onResize : function(w, h) {
			if (w > 0 && h > 0) {			
				$('#roleMenuTree')[0].style.width = (500) + 'px';
				$('#roleMenuTree')[0].style.height = (h - 55) + 'px';
				$('#oper_tree_td')[0].style.width = (w - 504) + 'px';
				$('#oper_tree_td')[0].style.height = (h - 55) + 'px';
			}
		}
	});

	$('#roleMenuOperTree').tree({
		url : ''
	});
	
	
	// 角色选择用户
	var options = {
		id : {
			leftGridId : 'lgrid',
			rightGridId : 'rgrid',
			addButtonId : 'addBtn',
			delButtonId : 'delBtn',
			container : 'roleUserPanel'
		},
		dimension : {
			leftGridWidth : 380,
			rightGridWidth : 380
		},
		functions : {
			init : function() {
				doInit($("#roleUserPanel"));
			}
		},
		jQueryHandler : $("#centraltab")
	};

	makeDoubleDatagridSelect(options);

});

function show_tab(tab_index) {
	var node = $('#roleTree').tree('getSelected');
	switch (tab_index) {
	case 0:
		showDetail(node);
		break;
	case 1:
		showRoleMenusInTree(node);// 显示角色对应的菜单树
		clearRoleMenuOperTree();
		break;
	case 2:
		showRoleUsersInTree(node); // 显示角色对应的用户
		doInit($("#roleUserPanel").parent());
		var h_ = $('#centraltab').tabs('options').height - 100;
		$('#roleUserTree_unsel').css({
			height : h_
		});
		$('#roleUserTree_sel').css({
			height : h_
		});
		break;
	default:
		break;
	}
}

function doInit(jq) {

	var param = null, node = $('#roleTree').tree("getSelected");
	if (node) {
		param = {
			roleid : node.id
		};
	}

	jq.find("#lgrid").datagrid({
		url : urls.queryUnselectUsers,
		queryParams : param,
		title : '可选用户列表',
		sortName : 'usercode',
		remoteSort : false,
		sortOrder : 'asc',
		idField : 'usercode',
		columns : [ [ {
			field : 'userid',
			checkbox : true
		}, {
			field : 'usercode',
			title : '用户编码',
			width : 85
		}, {
			field : 'username',
			title : '用户名称',
			width : 110
		}, {
			field : 'orgname',
			title : '机构名称',
			width : 160
		} ] ],
		fit : true,
		border : false,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		// view : scrollview,
		pageSize : 100,
		onDblClickRow : function(index, row) {
			authRole2User(node, urls.addRoleUser, grid_left, grid_right);
		}
	});

	jq.find("#rgrid").datagrid({
		url : node ? urls.querySelectedUsers : null,
		queryParams : param,
		title : '已选用户列表',
		sortName : 'usercode',
		remoteSort : false,
		sortOrder : 'asc',
		idField : 'userid',
		columns : [ [ {
			field : 'userid',
			checkbox : true
		}, {
			field : 'usercode',
			title : '用户编码',
			width : 85
		}, {
			field : 'username',
			title : '用户名称',
			width : 110
		}, {
			field : 'orgname',
			title : '机构名称',
			width : 160
		} ] ],
		fit : true,
		border : false,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		// view : scrollview,
		pageSize : 100,
		onDblClickRow : function(index, row) {
			authRole2User(node, urls.removeRoleUser, grid_right, grid_left);
		}
	});

	jq.find(".datagrid-pager > table").hide();
	// jq.find("#delBtn").hide();

	var grid_left = jq.find("#lgrid");
	var grid_right = jq.find("#rgrid");

	// 初始化添加按钮
	jq.find('#addBtn').linkbutton({
		onClick : function() {
			authRole2User(node, urls.addRoleUser, grid_left, grid_right);
		}
	});

	jq.find("#delBtn").linkbutton({
		onClick : function() {
			authRole2User(node, urls.removeRoleUser, grid_right, grid_left);
		}
	});
}

function authRole2User(node, url, grid_main, grid_sub) {

	if (node == null) {
		easyui_warn("请选择角色");
		return;
	}

	var rows = grid_main.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择用户！");
		return;
	}

	var row = rows[0];
	var params = {
		userids : row.userid,
		roleid : node.id
	};

	$.post(url, params, function(data) {
		if (data.success) {
			grid_main.datagrid("reload");
			grid_main.datagrid("clearSelections");
			grid_sub.datagrid("reload");
			grid_sub.datagrid("clearSelections");
		} else {
			easyui_warn(data.title);
		}
	}, "JSON");

}

function showDetail(node) {
	var n_id = node.id;
	$('#content').form('load', urls.detail + '?id=' + n_id);
	last_node = node;
}
function showRoleMenusInTree(node) {
	var n_id = node.id;
	$('#roleMenuTree').tree({
		url : urls.menuTree + '?roleid=' + n_id,
		checkbox : true,
		cascadeCheck : false,
		onBeforeCheck : function(node, checked) {
			if (node.id && node.id != -1)
				return true;
			else
				return false;
		},
		onLoadError : function(o) {
			alert('加载数据时发生异常！');
		},
		onSelect : function(node) {
			// 选中菜单
			
			if (node.id && node.id != -1) {
				showMenuOpers(node.id);
			}
		}
	});
}

function showRoleUsersInTree(node) {
	var n_id = node.id;

	$('#roleUserTree_unsel').tree({
				url : urls.roleUnselectedUserTree + '?roleId=' + n_id,
				checkbox : true,
				cascadeCheck : false,
				onLoadError : function(o) {
					alert('加载数据时发生异常！');
				}
			});

	$('#roleUserTree_sel').tree(
			{
				url : urls.roleSelectedUserTree + '?roleId=' + n_id,
				checkbox : true,
				cascadeCheck : false,
				onLoadError : function(o) {
					alert('加载数据时发生异常！');
				}
			});

}

// 保存修改
function saveEdit() {
//	$('#linkbutton_edit').linkbutton("enable");
//	$('#linkbutton_edit').linkbutton({
//		"toggle" : false,
//		iconCls : 'icon-lock',
//		text : '开始编辑'
//	});

	$('#content').form('submit', {
		url : urls.save,
		onSubmit : function() {
			var validate_result = $('#content').form('validate');
			if (validate_result == false) {
				$.messager.alert('警告', '信息录入不完整，请继续填写!', 'warnning');
				return false;
			}
			return true;
		},
		success : function(data) {
			try {
				data = eval("(" + data + ")");
			} catch (e) {

			}

			easyui_auto_notice(data, function() {
				menu_refresh();
				flag_edit = false;
				setSaveButtonStatus(flag_edit);
			}, function() {

			});

		}
	});

}
// 撤销修改
function rejectEdit() {
	showDetail(last_node);
	flag_edit = false;
	setSaveButtonStatus(flag_edit);
}

// 刷新树

function menu_refresh() {
	$('#roleTree').tree({
		url : urls.getRoleTree
	});
}

// 添加事件请求
function menu_add_req() {
	var node = getTreeSelect();
	if (node != undefined) {
		if (node.type == "role") {
			$.messager.alert('警告', '请右击角色组添加角色！', 'warning');
			return;
		}

		var new_level = (+(node.levelno)) + 1;
		prepareNewMenu('', node.code, new_level, node.id);
		$('#content').form('validate');
		edit_flag = false;
		setSaveButtonStatus(edit_flag);
	}
}

function menu_addtop_req() {
	$('#roleTree').tree('');
	prepareNewMenu('', '', 1, 0);

	$('#content').form('validate');
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}
// 删除事件请求
function menu_del_req() {
	$.messager.confirm('确认', '是否删除该角色以及角色资源关联、角色用户关联?', function(r) {
		if (r) {
			var node = getTreeSelect();
			if (node != undefined) {
				var nid = node.id;
				$.post(urls.del, {
					id : nid
				}, function(result) {

					easyui_auto_notice(result, function() {
						menu_refresh();
					}, function() {

					}, '删除过程中发生异常');

				}, 'json');
			} else {
				$.messager.alert('警告', '取选 中节点过程中发生异常!', 'warnning');
			}
		}
	});

}
// 取得树上选中的节点
function getTreeSelect() {
	return $('#roleTree').tree("getSelected");
}
/**
 * 
 * @param menuname
 *            要新建的菜单name
 * @param parentMenuId
 *            要新建的菜单的parentid
 */
function prepareNewMenu(nodename, parentGroupCode, levelno, parentid) {
	$('#content').form('clear');
	$('#rolename').textbox("setValue", nodename);
	$('#grpcode').val(parentGroupCode);

	$('#status').val(0);

	var date = new Date();
	var start_date_str = date.getFullYear() + '-' + (date.getMonth() + 1) + '-'
			+ date.getDate();
	var end_date_str = (date.getFullYear() + 1) + '-' + (date.getMonth() + 1)
			+ '-' + date.getDate();

	$('#startdate').datebox('setValue', start_date_str);
	$('#enddate').datebox('setValue', end_date_str);
	$('#content').form('validate');
	$('#levelno').val(levelno);
	$('#parentroleid').val(parentid);
	$('#isleaf').val(1);
}

// 取得第一个节点，
function getFirstLeafNode() {
	var roots = $('#roleTree').tree('getRoots');
	return roots[0];
}

/**
 * 所有 form元素的onchange 事件
 */
function form_onchange() {
	flag_edit = true;
	setSaveButtonStatus(flag_edit);
}
// 保存角色对应的菜单
function menutree_saveEdit() {

	var role_node = $('#roleTree').tree('getSelected');
	var roleid = role_node.id;
	var nodes = $('#roleMenuTree').tree('getChecked');
	var ids = new Array();
	for ( var i in nodes) {
		ids.push(nodes[i].id);
	}

	$.post(urls.saveResourceToRole, {
		roleid : roleid,
		resourceIds : ids.join(",")
	}, function(msg) {
		easyui_auto_notice(msg);
	}, "JSON");

}
/**
 * 过滤可选用户与已选用户
 */
function roletree_query() {
	var node = $('#roleTree').tree('getSelected');
	if (node != null) {
		showRoleUsersInTree(node);
	}
}
/**
 * 清空过滤可选用户与已选用户的条件
 */
function roletree_clear() {
	roletree_query();
}

/**
 * 给用户授权
 */
function addUserToRole() {
	// 角色id
	var node = $('#roleTree').tree('getSelected');
	var roleid = node.id;

	// 取得选中的用户id
	var selection = $('#roleUserTree_unsel').tree('getChecked');
	if (selection.length == 0) {
		easyui_warn("请选择至少一个用户!");
		return;
	}
	var array = new Array();
	for ( var i in selection) {
		array.push(selection[i].id);
	}
	var ids = array.join(",");

	$.post(urls.addUserToRole, {
		roleid : roleid,
		userids : ids
	}, function(result) {
		easyui_auto_notice(result, function() {
			roletree_query();
		});
	}, "JSON");
}
/**
 * 取消用户授权
 */
function removeUserFromRole() {
	// 角色id
	var node = $('#roleTree').tree('getSelected');
	var roleid = node.id;

	// 取得选中的用户id
	var selection = $('#roleUserTree_sel').tree('getChecked');

	if (selection.length == 0) {
		easyui_warn("请选择至少一个用户!");
		return;
	}
	var array = new Array();
	for ( var i in selection) {
		array.push(selection[i].id);
	}
	var ids = array.join(",");

	$.post(urls.removeUserFromRole, {
		roleid : roleid,
		userids : ids
	}, function(result) {
		easyui_auto_notice(result, function() {
			roletree_query();
		});
	}, "JSON");
}

function showMenuOpers(resourceid) {
	var role = $('#roleTree').tree('getSelected');

	$('#roleMenuOperTree').tree(
			{
				url : urls.queryRoleResourceOper + '?resourceId=' + resourceid
						+ '&roleId=' + role.id,
				checkbox : true,
				cascadeCheck : false,
				onLoadError : function(o) {
					easyui_warn('数据加载过程发生异常！');
				}
			});
}

function saveMenuOpers() {
	var role = $('#roleTree').tree('getSelected');

	var menu = $('#roleMenuTree').tree('getSelected');

	var checkedNodes = $('#roleMenuOperTree').tree('getChecked', 'checked');

	var array = new Array();
	for ( var i in checkedNodes) {
		array.push(checkedNodes[i].id);
	}

	$.post(urls.editRoleResourceOper, {
		resourceId : menu.id,
		roleId : role.id,
		operIds : array.join(",")
	}, function(r) {
		easyui_auto_notice(r, function() {
			$('#roleMenuOperTree').tree('reload');
		}, null, "操作失败！");
	}, "JSON");

}

function clearRoleMenuOperTree() {
	$('#roleMenuOperTree').tree('loadData', []);
}

//设置保存按钮的状态
function setSaveButtonStatus(isOn) {
	if (isOn) {
		$('#linkbutton_save').linkbutton('enable');
		$('#linkbutton_cancel').linkbutton('enable');
	} else {
		$('#linkbutton_save').linkbutton('disable');
		$('#linkbutton_cancel').linkbutton('disable');
	}
}

/**
 * 校验当前表单数据状态
 * @returns {Boolean} true表示 当前表单数据未发生修改； false 表示 当前表单数据已被修改
 */
function validFormEdit() {
	if (flag_edit) {
		easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
			if (c != undefined) { //取消
				if (c) { //是
					saveEdit();
				} else { //否
					rejectEdit();
				}
			} else {
				
			}
		});
		
		return false;
	} else
		return true;
}