/**
 * 页面初始化jQuery脚本
 */
var baseUrl = contextpath + 'sys/SysResourceController/';
var baseOperUrl = contextpath + 'sys/SysResourceOperController/';

var urls = {
	detail : baseUrl + 'get.do',
	save : baseUrl + 'save.do',
	getAll : baseUrl + 'query.do',
	del : baseUrl + 'delete.do',
	saveResourceOper : baseOperUrl + 'saveResourceOper.do',
	addCustomOper : baseOperUrl + 'addCustomOper.do',
	addPresetOper : baseOperUrl + 'addPresetOper.do',
	delMenuOper : baseOperUrl + 'delete.do',
	editMenuOper : baseOperUrl + 'edit.do',
	queryResourceOper : baseOperUrl + 'queryOper.do',
	queryPresetOper : baseOperUrl + 'queryPresetOper.do',
	getOper : baseOperUrl + 'get.do',
	resourceReorder : baseUrl + 'resourceReorder.do'
}

var last_node = null; //记住上次所选数据NODE
var edit_flag = false; // 是否正在编辑
var resourceTree = null;

$(function() {

	comboboxFunc("istodorem", "SYS_TRUE_FALSE", "code");
	comboboxFunc("datascopemode", "SYS_DATASCOPE_PRIORITY_MODE", "code");
	comboboxFunc("iconCls", "SYS_RESOURCE_ICON", "code", "name");
	comboboxFunc("restype", "SYS_RES_USER_TYPE", "code");
	comboboxFunc("oper_opermode", "SYS_DATASCOPE_PRIORITY_MODE", "code");

	comboboxFunc("oper_iconcls", "SYS_RESOURCE_ICON", "code", "name");
	
	var oldWfkey = null; //记住上次选择的工作流KEY
	$("#wfkeyname").treeDialog({
		title :'工作流选择',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'wfkey',
		prompt: "请选择工作流",
		multiSelect: false, //单选树
		dblClickRow: true,
		url : contextpath + 'workflow/WfProcessDefinitionController/queryWorkflowConfigTree.do',
		filters:{
			name: "工作流名称"
		},
		beforeSearchFunc :function() {
			oldWfkey = $('#wfkeyname').treeDialog('getValue');
		},
		clearFunc:function(){
			//TODO 点击清空按钮触发清除事件
			
			$('#activityname').gridDialog('disableValidation');
			$("#wfversion").combobox('loadData', []);
			$("#wfversion").combobox('disableValidation');
			$('#wftasknode').combobox('disableValidation');				
			$('#activityname').gridDialog('clear');
			form_onchange();
		},
		assignFunc:function(value){
			//TODO 双击选择回填时触发赋值事件
			if (oldWfkey != value) {
				// 选定工作流后加载当前工作流所有的版本列表，并默认选中当前正在运行的工作流版本
				$.post(contextpath + 'workflow/BussinessWorkFlowController/findVersionInfoByKey.do', {
					key : value
				}, function(result) {
					if (result.success == 'false' || result.success == false) {
						easyui_warn(result.title);
						$('#wfversion').combobox('loadData', []);
						$('#wfversion').combobox('clear');
						$('#activityname').gridDialog('clear');
						$('#wftasknode').combobox('clear');
						return ;
					}
					$('#wfversion').combobox({
						editable: false,
						panelHeight : 'auto',
						data : result.body.versionList,
						value : result.body.currentVersion,
						onChange : function() {
							form_onchange();
						},
						onChange : function(newValue, oldValue) {
							if (newValue != oldValue) {
								$('#activityname').gridDialog('clear');
								$("#wftasknode").combobox('clear');
							}
						}
					});
				}, 'json');
				$('#activityname').gridDialog('enableValidation');
				$('#wfversion').combobox('enableValidation');
				$('#wftasknode').combobox('enableValidation');
			}
			form_onchange();
		}
	});
	
	$('#activityname').gridDialog({
		title :'工作流节点选择',
		dialogWidth : 640,
		dialogHeight : 400,
		singleSelect: true,
		dblClickRow : true,
		hiddenName:'activityid',
		valueField:'activitiyid',
		textField:'activitiyname',
		prompt:"请选择工作流节点",
		filter: {
					cncode: "节点ID",
					code: "activitiyid",
					cnname: "节点名称",
					name : "activitiyname"
				},
		url : 'workflow/BussinessWorkFlowController/findActivitiesByKey.do',
		cols : [[
			{field : "ck", checkbox : true}, 
			{field : "activitiyid",title : "节点ID",width : 180},
	    	{field : "activitiyname", title : "节点名称", width : 180},
	    	{field : "type", title : "节点类型", width : 200}
	    ]],
	    assignFunc: function(value){
	    	form_onchange();
	    },
	    clearFunc: function(){
	    	$("#wftasknode").combobox('clear');
	    	form_onchange();
	    },
	    beforeSearchFunc:function() {
	    	var wfkey = $('#wfkeyname').treeDialog('getValue');
	    	var wfversion = $('#wfversion').combobox('getValue');
			if (!wfkey) {
				easyui_warn('请先选工作流！');
				return false;
			}
			if (!wfversion) {
				easyui_warn('请先选工作流版本号！');
				return false;
			}
			
			return {'key' : wfkey, 'wfversion' : wfversion};
		}
	});
	
	$("#wftasknode").combobox({
		editable: false,
		valueField : "id",
		textField : "text",
		panelHeight : 'auto',
		data : [{id:'0', text:'0-首任务节点'}, {id:'1',text:'1-中间任务节点'},{id:'2',text:'2-末任务节点'}],
		onChange : function() {
			form_onchange();
		}
	});

	$("#datascopemode").combobox({
		onChange : function() {
			form_onchange();
		}
	});

	$("#restype").combobox({
		onChange : function() {
			form_onchange();
		}
	});

	$("#iconCls").combobox({
		onChange : function() {
			form_onchange();
			$('#resourceIcon').linkbutton({
				iconCls : $("#iconCls").combobox('getValue')
			});
		},
		formatter : function(row) {
			return "<span style='display:inline-block;width:16px;height:16px;' class='"
					+ row.text
					+ "'></span>&nbsp;<span style='position:relative;top:-4px'>"
					+ row.text + "</span>";
		}
	});

	$("#oper_iconcls").combobox({
		onChange : function() {
			form_onchange();
			$('#resourceOperIcon').linkbutton(
					{
						iconCls : $("#oper_iconcls").combobox(
								'getValue')
					});
		},
		formatter : function(row) {
			return "<span style='display:inline-block;width:16px;height:16px;' class='"
					+ row.text
					+ "'></span>&nbsp;<span style='position:relative;top:-4px'>"
					+ row.text + "</span>";
		}
	});

	$("#oper_opermode").combobox({
		onChange : function() {
			form_onchange();
		}
	});
	
	$("#remindtype").combobox({
		valueField : 'value',
		textField : 'text',
		panelHeight : 'auto',
		data : [{
			value : '0',
			text : '不统计'
		}, {
			value : '1',
			text : '业务流程节点'
		}],
		onChange : function() {
			form_onchange();
		}
	});
	
	var resourceTree = $('#menutree').tree({
		url : urls.getAll,
		method : 'post',
		animate : false,
		dnd : true,
		onContextMenu : function(e, node) {
			if (validFormEdit()) {
				e.preventDefault();

				$(this).tree('select', node.target);
				$('#contextmenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		},
		formatter : function(node) {
			if (node.status == '1') {
				return node.text;
			} else
				return "<span style='color:gray'>" + node.text
						+ "</span>";
		},
		onLoadSuccess : function(node, data) {
			//如果没有数据则添加下级按钮不能用
			var item = $('#contextmenu').menu('findItem', '添加下级');
			if (data.length == 1 && data[0].id == "-1")
				$('#contextmenu').menu('disableItem', item.target);
			else
				$('#contextmenu').menu('enableItem', item.target);
			
			var selectNode = getFirstLeafNode();
			if (last_node != null)
				selectNode = resourceTree.tree('find', last_node.id);
			
			if (selectNode) {
				resourceTree.tree('expandTo', selectNode.target);
				resourceTree.tree('select', selectNode.target);
				// showAgencyDetail(firstNode);
			}
			
//			var firstNode = getFirstLeafNode();
//			if (firstNode) {
//				$('#menutree').tree('select', firstNode.target);
//				$('#menutree').tree('expandTo', firstNode.target);
//				showMenuDetail(firstNode);
//			}
//			switch (addFlag) {
//			case 0: // 页面刷新
//				var firstNode = getFirstLeafNode();
//				if (firstNode) {
//					$('#menutree').tree('select', firstNode.target);
//					$('#menutree').tree('expandTo', firstNode.target);
//					showMenuDetail(firstNode);
//				}
//				
//				break;
//			case 1:// 增加新节点
//				if (try_to_select_node) {
//					var node = $('#menutree').tree('find',
//							try_to_select_node);
//					$('#menutree').tree('select', node.target);
//					$('#menutree').tree('expandTo', node.target);
//				}
//				break;
//			}
		},
		onBeforeSelect : function() {
			return validFormEdit();
		},
		onSelect : function(node) {
			parent.$.messager.progress({
				title : '提示',
				text : '数据加载中，请稍后....'
			});
			showMenuDetail(node);
			last_node = node;
			$('#operform').form('clear');
			$('#oper_opermode').combobox('setValue', 0);
			edit_flag = false;
			setSaveButtonStatus(edit_flag);
			
		},
		onBeforeDrop : function(target, source, point) {
			// 判断是不是同一父节点
			var srcPid = source.pid;
			var tgtPid = $('#menutree').tree('getNode', target).pid;

			console.log('src:' + srcPid + " tgt:" + tgtPid);
			if (srcPid) {
				if (srcPid == tgtPid
						&& (point == 'top' || point == 'bottom')) {
					return true;
				}
			}
			return false;
		},
		onDrop : function(target, source, point) {
			// 完成排序

			doResourceReorder(target, source, point, '#menutree');
		}
	});

	$('#content').form({
		onLoadSuccess : function() {
			$('#linkbutton_save').linkbutton("disable");
			$('#linkbutton_cancel').linkbutton("disable");
			$('#opertype').val('edit');
			$('#resourceIcon').linkbutton({
				iconCls : $("#iconCls").combobox('getValue')
			});
		}
	});
	initOpergrid();
	
});

/**
 * 校验当前表单数据状态
 * @returns {Boolean} true表示 当前表单数据未发生修改； false 表示 当前表单数据已被修改
 */
function validFormEdit() {
	if (edit_flag) {
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


//保存修改
function saveEdit() {
	var isValid = $('#content').form('validate');
	if (!isValid)
		return;

	$('#content').form('submit', {
		url : urls.saveAgencyDetail,
		onSubmit : function() {
			return true;
		},
		success : function(data) {
			try {
				data = eval("(" + data + ")");
			} catch (e) {

			}
			if (data == null || data == undefined) {
				easyui_warn('保存失败!');
				return;
			}
			if (data.success == true) {
				 easyui_info(data.title);
				
				agency_refresh();
				edit_flag = false;
				setSaveButtonStatus(edit_flag);
				
			} else {
				$.messager.alert('警告', data.title, 'warnning');
			}
			$('#opertype').val('edit');
		}
	});

}
/**
*  撤销修改
*/
function rejectEdit() {
	$("#content").form("clear");
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
	if (last_node)
		showMenuDetail(last_node);
	
	$('#opertype').val('edit');
}

function showMenuDetail(node) {
	var n_id = node.id;
	if (node.wfkey != null) {
		$.post(contextpath + 'workflow/BussinessWorkFlowController/findVersionInfoByKey.do', {
			key : node.wfkey
		}, function(result) {
			if (result.success == 'false' || result.success == false) {
				easyui_warn(result.title);
				return ;
			}
			$('#wfversion').combobox({
				editable: false,
				panelHeight : 'auto',
				data : result.body.versionList,
				value : result.body.currentVersion,
				onChange : function() {
					form_onchange();
				},
				onChange : function(newValue, oldValue) {
					if (newValue != oldValue) {
						$('#activityname').gridDialog('clear');
						$("#wftasknode").combobox('clear');
					}
				}
			});
			if (node.wfkey != null) {
				$('#activityname').gridDialog('enableValidation');
				$('#wfversion').combobox('enableValidation');
				$('#wftasknode').combobox('enableValidation');
				$('#activityname').gridDialog('clear');
			} else {
				$('#activityname').gridDialog('disableValidation');
				$("#wfversion").combobox('loadData', []);
				$("#wfversion").combobox('disableValidation');
				$('#wftasknode').combobox('disableValidation');				
				$('#activityname').gridDialog('clear');
			}
			//改动了一下，放在后面的话，ajax提交为异步导致出现界面异常 刘俊波 2015-09-25
			$('#content').form('clear');
			$('#content').form('load', urls.detail + '?id=' + n_id);
			$('#content').form({'onLoadSuccess' : function() {
				edit_flag = false;
				setSaveButtonStatus(edit_flag);
				if (parent.$.messager.progress)
					parent.$.messager.progress('close');
			}});
			loadMenuOperations(node);
			
			edit_flag = false;
			
		}, 'json');
	} else {
		$('#activityname').gridDialog('disableValidation');
		$("#wfversion").combobox('loadData', []);
		$("#wfversion").combobox('disableValidation');
		$('#wftasknode').combobox('disableValidation');
		$('#activityname').gridDialog('clear');
		
		$('#content').form('clear');
		$('#content').form('load', urls.detail + '?id=' + n_id);
		$('#content').form({'onLoadSuccess' : function() {
			edit_flag = false;
			setSaveButtonStatus(edit_flag);
			if (parent.$.messager.progress)
				parent.$.messager.progress('close');
		}});
		loadMenuOperations(node);
		

		
		edit_flag = false;
	}
	
	
}

// 保存修改
function saveEdit() {

	var resid = $('#resourceid').val();

	if (resid == "") {
		// 设置resorder
		var curnode = $('#menutree').tree('getSelected');
		var parent = $('#menutree').tree('getParent', curnode.target);

		var children = null;
		if (parent) {
			children = $('#menutree').tree('getChildren', parent.target);
		} else {
			children = $('#menutree').tree('getRoots');
		}
		var maxOrder = 1;
		for ( var i in children) {
			if (children[i].resorder) {
				if (+children[i].resorder >= maxOrder) {
					maxOrder = +children[i].resorder + 1;
				}
			}
		}
		console.log('resorder=' + maxOrder);
		$('#resorder').val(maxOrder);
	}
	
	$('#content').form('submit', {
		url : urls.save,
		onSubmit : function() {
			return $('#content').form('validate');
		},
		success : function(data) {
			try {
				data = eval("(" + data + ")");
			} catch (e) {

			}

			easyui_auto_notice(data, function() {
				$('#linkbutton_save').linkbutton("disable");

				$('#linkbutton_cancel').linkbutton("disable");

				edit_flag = false;
				try_to_select_node = data.body.id;
				menu_refresh(1);
			}, function() {
				try_to_select_node = null;
			});
		}
	});

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

// 刷新树

function menu_refresh(willSelNode) {
	addFlag = willSelNode;
	$('#menutree').tree({
		url : urls.getAll
	});
}

// 添加事件请求
function menu_add_req() {
	if (edit_flag) {
		$.messager.alert('警告', '当前正在进行数据编辑,请先结束编辑状态再进行下一步操作!', 'warnning');
		return;
	}
	var node = getTreeSelect();
	if (node != undefined) {

		if (node.isleaf == true) {
			$.messager.alert('警告', '不能在实体资源上添加子资源！', 'warning');
			return;
		}

		opertype_toset = "adddown";
		addFlag = 1;
		$('#menutree').tree('expand', node.target);

		var newmeun_levelno = (+node.levelno) + 1;
		prepareNewMenu(newmeun_levelno, node.id);

		var nm = $('#menutree').tree('find', newmenu_id);
		last_node_handle = nm;
		$('#menutree').tree('select', nm.target);

		$('#linkbutton_save').linkbutton("enable");
		$('#linkbutton_cancel').linkbutton("enable");

		edit_flag = true;

	}
}
// 删除事件请求
function menu_del_req() {
	if (edit_flag) {
		$.messager.alert('警告', '当前正在进行数据编辑,请先结束编辑状态再进行下一步操作!', 'warnning');
		return;
	}
	$.messager.confirm('确认', '是否删除该资源?', function(r) {
		if (r) {
			var node = getTreeSelect();
			if (node != undefined) {
				var nid = node.id;
				$.post(urls.del, {
					id : nid
				}, function(result) {

					easyui_auto_notice(result, function() {
						$('#operform').form('clear');
						menu_refresh(0);
					});
				}, 'json');
			} else {
				$.messager.alert('警告', '取选 中节点过程中发生异常!', 'warnning');
			}
		}
	});

}

/**
 * 添加一级资源事件
 */
function menu_addtop_req() {
	$('#content').form('reset');
	$('#resourceid').val('');
	$('#parentresid').val('0');
	$('#levelno').val('');
	$('#opertype').val('add');
	$('#content').form('validate');
	
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}
/**
 * 取得树上选中的节点
 */
function getTreeSelect() {
	return $('#menutree').tree("getSelected");
}
/**
 * 
 * @param menuname
 *            要新建的资源name
 * @param parentMenuId
 *            要新建的资源的parentid
 */
function prepareNewMenu(menulevelno, parentMenuId) {
	$('#content').form('clear');
	//$('#name').textbox("setValue", menuname);
	//$('#code').val(menucode);
	$('#levelno').val(menulevelno);
	$('#parentresid').val(parentMenuId)
	$('#istodorem').val(0);
	$('#status').attr('checked', 'true');
	$('#restype').combobox('setValue', 1);
	$('#datascopemode').combobox('setValue', 0);

	var date = new Date();
	var date_str = date.getFullYear() + '-' + (date.getMonth() + 1) + '-'
			+ date.getDate();
	$('#createtime').val(date_str);
	$('#opertype').val('add');
	$('#content').form('validate');
}

// 取得第一个节点，
function getFirstLeafNode() {
	var roots = $('#menutree').tree('getRoots');
	return roots[0];
}

/**
 * 所有 form元素的onchange 事件
 */
function form_onchange() {
	edit_flag = true;
	setSaveButtonStatus(edit_flag);
}

function initOpergrid() {

	$('#opergrid').datagrid({
		url : '',
		columns : [ [ {
			field : "ck",
			checkbox : true
		}, {
			field : "name",
			title : "操作名称",
			width : 80
		}, {
			field : "code",
			title : "操作编码",
			width : 80
		}, {
			field : "position",
			title : "位置",
			width : 120
		}, {
			field : "cnopermode",
			title : "优先模式",
			width : 80
		} ] ],
		singleSelect : true,
		fit : true,
		toolbar : '#opertoolbar',
		border : false,
		onClickRow : function(rowIndex, rowData) {
			showOperDetail(rowData);
		}
	});
}
/**
 * 加载资源的操作列表
 * 
 * @param node
 */
function loadMenuOperations(node) {
	$('#opergrid').datagrid({
		url : urls.queryResourceOper + '?resourceid=' + node.id
	});

}
/**
 * 操作添加
 */
function oper_add_func() {

	// 清空form

	$('#operform').form('clear');

	var n = $('#menutree').tree('getSelected');
	$('#oper_resourceid').val(n.id);

	$('#oper_opermode').combobox('setValue', '0');

	// 清除选择
	$('#opergrid').datagrid('clearSelections');
}
/**
 * 向服务端请求保存
 */

function oper_save_func() {
	$('#operform').form(
			'submit',
			{
				url : urls.saveResourceOper,
				onSubmit : function() {
					var v = $('#oper_name').searchbox('getValue');
					var icon = $('#oper_iconcls').combobox('getValue');
					var pos = $('#oper_position').textbox("getValue");
					var bl = v == '' || v == undefined || icon == ''
							|| icon == undefined || pos == ''
							|| pos == undefined;
					if (bl) {
						$('#operform').form('validate');
					}
					return !bl;
				},
				success : function(data) {
					data = eval("(" + data + ")");
					easyui_auto_notice(data, function() {
						$('#opergrid').datagrid('reload');
					}, null, '保存过程中发生异常！');
				}
			});
}
/**
 * 操作删除
 */
function oper_del_func() {

	var select = $('#opergrid').datagrid('getSelected');

	parent.$.messager.confirm('确认', '是否删除选中的操作?', function(r) {
		if (r) {
			$.post(urls.delMenuOper, {
				id : select.id
			}, function(r) {
				easyui_auto_notice(r, function() {
					$('#opergrid').datagrid('reload');
					$('#opergrid').datagrid('clearSelections');
					$('#operform').form('clear');

				}, null, "删除过程发生异常!");
			}, "JSON");
		}
	});
}

function oper_edit_func() {

	var select = $('#opergrid').datagrid('getSelections');
	if (select.length == 0) {
		easyui_warn('请选择一个自定义操作！');
		return;
	}
	if (select.length > 1) {
		easyui_warn('仅能选择一个操作进行修改！');
		return;
	}

	if (select[0].custom == false) {
		easyui_warn('预定义操作不能修改，请选择一个自定义操作！');
		return;
	}
	parent.$.fastModalDialog({
		title : '自定义操作修改',
		width : 450,
		height : 200,
		iconCls : 'icon-edit',
		html : $('#operAddWindow')[0].outerHTML,
		dialogID : 'ndlg',
		onOpen : function() {

			var form_ = parent.$.fastModalDialog.handler['ndlg']
					.find('#operForm');
			form_.find("input[name='custom']").val(select[0].custom);
			form_.find("input[name='code']").attr('READONLY', "true");
			form_.find("input[name='code']").validatebox({
				required : true
			});
			form_.find("input[name='code']").val(select[0].code);
			form_.find("input[name='name']").validatebox({
				required : true,
				value : select[0].name
			});
			form_.find("input[name='name']").val(select[0].name);
			form_.find("input[name='id']").val(select[0].id);
			form_.form('validate');
		},
		buttons : [
				{
					text : "保存",
					iconCls : "icon-save",
					handler : function() {

						var selected = $('#menutree').tree('getSelected');
						var resourceid = selected.id;

						var form_ = parent.$.fastModalDialog.handler['ndlg']
								.find('#operForm');

						var res = form_.form('validate');
						if (!res) {
							easyui_warn('请填写必填项！');
							return;
						}

						form_.find("input[name='flag']").val('');
						form_.find("input[name='opersToAdd']").val(
								form_.find("input[name='code']").val()
										+ ','
										+ form_.find("input[name='name']")
												.val());
						form_.form('submit', {
							url : 'sys/' + urls.editMenuOper
									+ '?flag=CUSTOM&resourceid=' + resourceid,
							onSubmit : function() {
								return true;
							},
							success : function(data) {
								data = eval("(" + data + ")");
								easyui_auto_notice(data, function() {
									$('#opergrid').datagrid('reload');
								}, null, '保存过程中发生异常！');

							}
						});
						parent.$.fastModalDialog.handler['ndlg']
								.dialog('close');
					}
				},
				{
					text : "取消",
					iconCls : "icon-cancel",
					handler : function() {
						parent.$.fastModalDialog.handler['ndlg']
								.dialog('close');
					}
				} ]
	});

}

function showOperDetail(row) {
	var id = row.id;

	$.post(urls.getOper, {
		id : id
	}, function(obj) {
		$('#operform').form({
			onLoadSuccess : function() {

				$('#resourceOperIcon').linkbutton({
					iconCls : $("#oper_iconcls").combobox('getValue')
				});
			}
		});
		$('#operform').form('load', obj);
		$('#oper_name').searchbox('setValue', obj.name);
	}, 'json');

}

function showPresetOper(value, name) {
	parent.$.fastModalDialog({
		title : '预定义操作选择',
		width : 450,
		height : 300,
		iconCls : 'icon-add',
		html : '<div><table id="grid_9981" ></table></div>',
		dialogID : 'ndlg',
		onOpen : function() {
			var grid_ = parent.$.fastModalDialog.handler['ndlg']
					.find('#grid_9981');
			grid_.datagrid({
				url : urls.queryPresetOper + '?resourceid='
						+ $('#menutree').tree('getSelected').id,
				columns : [ [ {
					field : "ck",
					checkbox : true
				}, {
					field : "code",
					title : "操作编码",
					width : 120
				}, {
					field : "name",
					title : "操作名称",
					width : 200
				}, {
					field : "position",
					title : "位置",
					width : 200
				}

				] ],
				singleSelect : true,
				fit : true,
				pagination : false,
				onDblClickRow : function(index, row) {
					var sel = parent.$.fastModalDialog.handler['ndlg'].find('#grid_9981').datagrid('getSelected');
					// 设置名称
					$('#oper_name').searchbox('setValue', sel.name);

					// 设置code
					$('#oper_code').textbox("setValue", sel.code);
					parent.$.fastModalDialog.handler['ndlg'].dialog('close');
				}
			});
		},
		buttons : [
				{
					text : "选择",
					iconCls : "icon-ok",
					handler : function() {
						var sel = parent.$.fastModalDialog.handler['ndlg'].find('#grid_9981').datagrid('getSelected');
						if (sel) {
							// 设置名称
							$('#oper_name').searchbox('setValue', sel.name);

							// 设置code
							$('#oper_code').textbox("setValue", sel.code);
							parent.$.fastModalDialog.handler['ndlg'].dialog('close');
						} else {
							easyui_warn('请选择一行数据！');
						}
					}
				},
				{
					text : "关闭",
					iconCls : "icon-cancel",
					handler : function() {
						parent.$.fastModalDialog.handler['ndlg']
								.dialog('close');
					}
				} ]
	});

}

function doResourceReorder(target, sourceNode, point, treeid) {
	var tree = $(treeid);
	var targetNode = tree.tree('getNode', target);

	$.post(urls.resourceReorder, {
		pid : targetNode.pid,
		srcid : sourceNode.id,
		tgtid : targetNode.id,
		point : point
	}, function(msg) {
		if (msg.success) {
			easyui_info('排序成功！');
		} else {
			easyui_warn('排序过程发生异常！');
		}
	}, 'json');

}
