var isExistDscope = false; //对应角色是否有数据权限依据

var menuLoadSuccess = false; //菜单树 是否加载成功
var userLoadSuccess = false; //用户列表是否加载成功

var roleTree = null;
var menuTree = null;
var userdatagrid = null;
$(function() {
	roleTree = $('#roletree').tree({
			url : contextpath + 'sys/SysRoleController/query.do',
			method : 'post',
			animate : true,
			onLoadSuccess : function(node, data) {
				node = node ? node : roleTree.tree('getRoot'); //获取根节点
				roleTree.tree('select', node.target); //默认选中根节点
				menuLoadSuccess = false;
				userLoadSuccess = false;
				getDscopeClickRoletree(node.id);
			},
			onClick : function(node) {
				$("input[id='isallmenu']").attr("checked", false);
				$("input[id='isalluser']").attr("checked", false);
				$('#datascopemain').gridDialog('setText', '');
				$('#datascopemain').gridDialog('setValue', '');
				menuLoadSuccess = false;
				userLoadSuccess = false;
				removeLockDiv($('#menuPanel'));
				removeLockDiv($('#userPanel'));
				colseAllTabs(); //关闭所有条件项

				getDscopeClickRoletree(node.id);
			}
	});
	
	menuTree = $('#menutree').tree({
			method : 'post',
			animate : true,
			onLoadSuccess : function(node, data) {
				menuLoadSuccess = true;
				if ($('#isallmenu').is(':checked')) {
					var roots = menuTree.tree('getRoots');
					var nodes = roots;
					if (nodes)
						menuTree.tree('select', nodes[0].target);
				}
			},
			onClick : function(node) {
				colseAllTabs(); //关闭所有条件项
				var roleid = roleTree.tree('getSelected').id;
				var menu = menuTree.tree('getSelected');
				var user = userdatagrid.datagrid('getSelected');
				var isallmenuC = $('#isallmenu').is(':checked');
				var isalluserC = $('#isalluser').is(':checked');
				var isallmenu = 1;
				var isalluser = 1;
				var resourceid = null;
				var userid = null;
				if (isallmenuC) { //分配到功能菜单上
					isallmenu = 0;
					if (!menu) {
						easyui_warn("请选择功能菜单！");
						return;
					} else {
						resourceid = menu.id;
					}
				}
				
				if (isalluserC) { //分配到用户上
					isalluser = 0;
					if (!user) {
						userdatagrid.datagrid('selectRow', 0);
						userid = userdatagrid.datagrid('getSelected').userid;
					} else {
						userid = user.userid;
					}
				} 
				getDataScopeDetail(roleid, isallmenu, resourceid, isalluser, userid);
			}
	});
	
	userdatagrid = $("#userdatagrid").datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		url : contextpath + "sys/SysUserController/getUserByRoleID.do",
		queryParams : {roleid:0},
		onLoadSuccess:function(data){
			userLoadSuccess = true;
			var isalluserC = $('#isalluser').is(':checked');
			if (isalluserC)
				userdatagrid.datagrid('selectRow', 0);
			
		},

		onClickRow : function(rowIndex, rowData) {
			colseAllTabs(); //关闭所有条件项
			var roleid = roleTree.tree('getSelected').id;
			var menu = menuTree.tree('getSelected');
			var user = userdatagrid.datagrid('getSelected');
			var isallmenuC = $('#isallmenu').is(':checked');
			var isalluserC = $('#isalluser').is(':checked');
			if (isallmenuC) {
				isallmenu = 0;
				resourceid = menu.id;
			}
			
			if (isalluserC) {
				isalluser = 0;
				userid = user.userid;
			}
			
			getDataScopeDetail(roleid, isallmenu, resourceid, isalluser, userid);
		},
		columns : [[
            {
            	field : "userid",
            	checked : true,
            	hidden : true
            }, 
            {
            	field : "usercode",
            	title : "用户编码",
            	width : 120
            },
            {
            	field : "username",
            	title : "用户名称",
            	width : 120
            }
		]]
	});
	
	$('#datascopemain').gridDialog({
		title :'数据表选择',
		dialogWidth : 500,
		dialogHeight : 400,
		singleSelect: true,
		dblClickRow : true,
		hiddenName:'datascopemainID',
		valueField:'id',
		textField:'text',
		prompt:"请选择数据权限",
		filter: {
					cncode: "表名",
					code: "tablecode",
					cnname: "数据权限名称",
					name : "text"
				},
		url :contextpath + 'base/datascope/datascopeController/findDataScopeMain.do',
		cols : [[
			{field : "ck", checkbox : true}, 
	    	{field : "text", title : "数据权限名称", width : 230}
	    ]],
	    assignFunc: function(row){
			$.post(contextpath + 'base/datascope/datascopeController/getDataScopeDetailByID.do', {scopemainid:row.id}, function(data){
				clickdatascopemainDetail(data, '450px', true);
			}, "json");
	    }
	});
});

function isChecked(id) {
	var pand = $('#'+id).is(':checked');
	return pand;
}

function checkFunc(id) {
	if (isExistDscope) {
		var pand = isChecked(id);
		$("input[id='"+id+"']").attr("checked", !pand);
		easyui_warn('要改变此项，必须先删除该角色的数据权限！');
	}
		
	var pand = $('#'+id).is(':checked');
	if (id == 'isallmenu') {
		var panel = $('#menuPanel');
		if (pand)
			removeLockDiv(panel);
		else
			addLockDiv(panel);
	} else {
		var panel = $('#userPanel');
		if (pand)
			removeLockDiv(panel);
		else
			addLockDiv(panel);
	}
}
/**
function loadMenu(roleid) {
	$.post(contextpath + 'sys/SysResourceController/getResourceTreeByRole.do', {roleid:roleid}, function(data){
		menuTree.tree('loadData', data);
	}, "json");
}*/
function loadMenu(roleid) {
	menuTree.tree('options').url = contextpath + 'sys/SysResourceController/getResourceTreeByRole.do?roleid=' + roleid; //加载对应功能菜单
	menuTree.tree('reload');
}


function loadUser(roleid) {
	roleid = roleid ? roleid : '';
	userdatagrid.datagrid('load', {roleid:roleid});
}

//保存配置的数据权限
function save() {
	var roleid = roleTree.tree('getSelected').id;
	var menu = menuTree.tree('getSelected');
	var user = userdatagrid.datagrid('getSelected');
	var isallmenu = $('#isallmenu').is(':checked');
	var isalluser = $('#isalluser').is(':checked');
	var datascopemain = $('#datascopemain').gridDialog('getValue');
	if (isallmenu && !menu) {
		easyui_warn('请选择功能菜单！');
		return;
	}
	if (isalluser && !user) {
		easyui_warn('请选择用户！');
		return;
	}
	if (!datascopemain) {
		easyui_warn('请选择数据权限！');
		return;
	}
	var data = "\"roleid\":" + roleid;
	if (isallmenu)
		data += ",\"isallmenu\":0,\"resourceid\":" + menu.id;
	else
		data += ",\"isallmenu\":1";
	if (isalluser) {
		if (!user) {
			easyui_warn("请选择用户！");
			return;
		} else 
			data += ",\"isalluser\":0,\"userid\":" + user.userid;
	}
		
	else
		data += ",\"isalluser\":1";
	data += ",\"scopemainid\":" + datascopemain;
	 $.post(contextpath + "base/datascope/datascopeController/save.do",{
         data:  "{" + data + "}"}, function (result) {
        	 if (result.success)
        		 easyui_show(result.title);
        	 else
        		 easyui_warn(result.title);
    }, 'JSON');
}

function datascopeEdit() {
	showDialog("数据权限维护", contextpath + "base/datascope/datascopeController/editEntry.do", null, null, false, 900, 640);
}

function showDialog(title, url, rowData, formID, fill, width, height) {
	parent.$.modalDialog({
		title : title,
		width : width ? width : 'auto',
		height : height ? height : 'auto',
		href : url,
		maximizable : true,
		onBeforOpen: function(){
			console.log("open");
		},
		onLoad: function(){
			console.log("load");
			parent.$.modalDialog.openner_dataGridURL = url;
			if(fill){
				var f = parent.$.modalDialog.handler.find('#' + formID);
				f.form("load", rowData);
			}
		}
	});
}

//删除分配的数据权限
function deleteDataRelation() {
	var role = roleTree.tree('getSelected');
	var menu = menuTree.tree('getSelected');
	var user = userdatagrid.datagrid('getSelected');
	var isallmenu = $('#isallmenu').is(':checked');
	var isalluser = $('#isalluser').is(':checked');
	if (isallmenu && !menu) {
		easyui_warn('请选择功能菜单！');
		return;
	}
	if (isalluser && !user) {
		easyui_warn('请选择用户！');
		return;
	}
	var msg = '是否删除' + role.text;
	if (isallmenu)
		msg += '，' + menu.text;
	if (isalluser)
		msg += '，' + user.username;
	msg += '关联的数据权限？';
	$.messager.confirm('确认', msg, function(r) {
		if (r) {
			 $.post(contextpath + "base/datascope/datascopeController/deleteDataScopeRelation.do",{
		         roleid: role.id, isallmenu:isallmenu? 0:1, resourceid:menu?menu.id:0, isalluser:isalluser? 0:1, userid:user?user.userid:0}, function (result) {
		        	 if (result.success) {
		        		 easyui_show(result.title);
		        		 colseAllTabs(); //关闭所有条件项
		        		 $('#datascopemain').gridDialog('setText', '');
		 				$('#datascopemain').gridDialog('setValue', '');
		        		 result.body.isExistDscope == 1 ? isExistDscope=true : isExistDscope = false;
		        	 } else
		        		 easyui_warn(result.title);
		    }, 'JSON');
		}
	});
}
// 选中角色时 触发事件
function getDscopeClickRoletree(roleid) {
	$.post(contextpath + 'base/datascope/datascopeController/get.do', {roleid : roleid}, function(data) {
			if (data) {
				isExistDscope = true;
				if (data.isalluser == 0) {
					$("input[id='isalluser']").attr("checked", true);
				}	else
					addLockDiv($('#userPanel')); //不允许选用户
				
				if (data.isallmenu == 0) {
					$("input[id='isallmenu']").attr("checked", true);
				} else
					addLockDiv($('#menuPanel')); //不允许选菜单
					
			if (data.isallmenu == 1 && data.isalluser == 1) { //数据权限直接授权给角色
				$('#datascopemain').gridDialog('setText', data.scopemainname);
				$('#datascopemain').gridDialog('setValue', data.scopemainid);
				getDataScopeDetail(data.roleid, data.isallmenu, data.resourceid, data.isalluser, data.userid);
			}
		} else {
			isExistDscope = false;
			addLockDiv($('#menuPanel')); 
			addLockDiv($('#userPanel')); //不允许选用户
		}
		loadMenu(roleid); //加载对应的功能菜单
		loadUser(roleid); //加载对应用户
	});
}

//获取 配置数据权限
function getDataScopeDetail(roleid, isallmenu, resourceid, isalluser, userid) {
	resourceid = isallmenu == 1 ? null : resourceid;
	userid = isalluser == 1 ? null : userid;
	$.post(contextpath + 'base/datascope/datascopeController/getDataScopeDetail.do', {roleid : roleid, isallmenu : isallmenu, resourceid : resourceid, isalluser : isalluser, userid : userid}, function(data){
		if (data && data.body) {
			var scopemain = data.body;
			$('#datascopemain').gridDialog('setText', scopemain.scopemainname);
			$('#datascopemain').gridDialog('setValue', scopemain.scopemainid);
		}
		clickdatascopemainDetail(data, '450px', true);
	}, "json");
}

//关闭所有的页签
function colseAllTabs() {
	var condTabs = $('#condtions');
	var all_tabs = condTabs.tabs('tabs');
	for (var n=all_tabs.length-1; n>=0; n--) {
		condTabs.tabs('close', condTabs.tabs('getTabIndex',all_tabs[n]));
	}
}

//添加遮罩层
function addLockDiv(panel) {
	$("<div class=\"window-mask\"></div>").css({ display: "block", width: panel.width(), height: panel.height()}).appendTo(panel);
}

//移除遮罩层
function removeLockDiv(panel) {
    panel.find("div.window-mask").remove();  
}