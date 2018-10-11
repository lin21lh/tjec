/**
 * 数据项值集维护
 */
function form_onchange(){
	flag_edit=true;
	setSaveButtonStatus(flag_edit);
}

//保存
function saveEdit() {
	var isValid = $('#content').form('validate');
	if (!isValid)
		return;
	
	$('#contentTree').form('submit', {
		url : urls.saveTreeElementVal,
		onSubmit : function() {
			return $(this).form('validate');
		},
		success : function(data) {
			try {
				data = eval("(" + data + ")");
			} catch (e) {

			}
			if (data == null || data == undefined) {
				easyui_warn('保存失败！');
				return;
			}
			if (data.success == true) {
				$.messager.show({
					title : '提示',
					msg : data.title,
					timeout : 2000,
					showType : 'fade',
					style:{
	                    right:'',
	                    bottom:''
	                }
				});
				tree_refresh();
				
				flag_edit = false;
				setSaveButtonStatus(flag_edit);
			} else {
				$.messager.alert('提示', data.title, 'warnning');
			}
		}
	});
}

//撤销修改
function rejectEdit() {
	if (last_node_handle != null)
		$('#contentTree').form('load', contextpath + 'base/dic/dicElementValSetController/getDetailById.do?tablecode=' + tablecode + '&id=' + last_node_handle);
	flag_edit=false;
	setSaveButtonStatus(flag_edit);
}

// 刷新树

function tree_refresh() {
	addFlag = 0;
	$('#tree_elementVal').tree({
		url : contextpath + 'base/dic/dicElementValSetController/queryDicTreeElementVals.do?elementcode=' + getElementcode()
	});
}

// 添加下级
function dicElementVal_addDown_req() {
	if (!validFormEdit())
		return ;
	
	var node = getTreeSelect();
	if (node != undefined) {
		if (!node.id) {
			$.messager.alert('警告', '未选中父节点数据！', 'warnning');
			return;
		}
		$.post(urls.validLevelElement, {
			elementcode : getElementcode(),
			levelno : node.levelno
		}, function(result) {
			if (result.success) {
				addFlag = 1;
				prepareNewDownElement(node.code, node.id);
				flag_edit = false;
				setSaveButtonStatus(flag_edit);
			} else {
				if (result.title) {
					$.messager.alert('警告', result.title, 'warnning');
				} else {
					$.messager.alert('警告', '删除过程中发生异常！', 'warnning');
				}
			}
		}, 'json');
	}
	
}

/**
 * 添加
 */
function dicElementVal_add_req() {
	if (!validFormEdit())
		return ;
	
	var node = getTreeSelect();
	
	if (node != undefined) {

		addFlag = 1;
		prepareNewElement();
	}
	
	flag_edit = false;
	setSaveButtonStatus(flag_edit);
}

//删除
function dicTreeElementVal_del_req() {
	if (!validFormEdit())
		return ;
	
	var node = getTreeSelect();
	if (node != undefined) {
		if (!node.id) {
			$.messager.alert('警告', '无数据需要删除！', 'warnning');
			return;
		} 
		$.messager.confirm('确认', '是否删除该项数据?', function(r) {
			if (r) {
					$.post(urls.deleteDicTreeElementVals, {
						id : node.id,
						elementcode : getElementcode()
					}, function(result) {
						if (result.success) {
							$.messager.show({
								title : '提示',
								msg : result.title,
								timeout : 2000,
								showType : 'fade'
							});
							
							tree_refresh();
						} else {
							if (result.title) {
								$.messager.alert('警告', result.title, 'warnning');
							} else {
								$.messager.alert('警告', '删除过程中发生异常', 'warnning');
							}
						}
					}, 'json');
	
			}
		});
	} else {
		$.messager.alert('警告', '取选 中节点过程中发生异常!', 'warnning');
	}
}

/**
 * 取得树上选中的节点
 */ 
function getTreeSelect() {
	return $('#tree_elementVal').tree("getSelected");
}


function prepareNewDownElement(code, parentId) {
	$('#contentTree').form('reset');
	$('#codeedit').val(code);
	$('#superitemid').val(parentId);
	$('#content').form('validate');
}

function prepareNewElement() {
	$('#contentTree').form('reset');
	$('#'+keycolumn).val('');
	$('#content').form('validate');
}

function getElementcode() {
	return $('#elementcode').val();
}

//校验当前表单数据状态
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