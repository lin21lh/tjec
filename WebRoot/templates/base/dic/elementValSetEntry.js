/**
 * 顺序码数据项值集维护
 */
var edit_flag=false;  //是否正在编辑

/**
 * 所有 form元素的onchange 事件
 */
function form_onchange(){
	edit_flag=true;
	//置亮 save 和 cancel按钮
	setSaveButtonStatus(edit_flag);
}

function init() {
	$('#datagrid_elementVal').datagrid({
		onBeforeCheck : function(rowIndex, rowData) {
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
		},
		onBeforeSelect : function(rowIndex, rowData) {
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
		},
		onSelect : function(rowIndex, rowData) {
			loadFormData(rowData);
			edit_flag = false;
			setSaveButtonStatus(false);
		}
	});
	
}

function loadFormData(rowData) {
	$('#content').form('load', rowData);
}

//设置保存按钮的状态
function setSaveButtonStatus(isOn) {
	if (isOn) {
		$('#elemvalset_save').linkbutton('enable');
		$('#elemvalset_cancel').linkbutton('enable');
	} else {
		$('#elemvalset_save').linkbutton('disable');
		$('#elemvalset_cancel').linkbutton('disable');
	}
}

//枚举项新增
function addenumitem() {
	if (!validFormEdit())
		return ;
	
	var records = $('#datagrid_elementVal').datagrid('getSelections');
	if (records.length > 0) {
		 $('#datagrid_elementVal').datagrid('clearSelections');
	}
	
	$('#content').form('reset');
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}

//保存
function saveEdit() {
	$('#content').form('submit', {
		url : urls.saveElementVal,
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
				easyui_info( data.title, function() {$("#datagrid_elementVal").datagrid("reload");
					edit_flag=false;
					setSaveButtonStatus(edit_flag);
				});
			} else {
				easyui_warn(data.title);
			}

		}
	});
}

//撤销修改
function rejectEdit() {
	var sel = $('#datagrid_elementVal').datagrid('getSelected');
	if (sel) {
		// 取消修改
		loadFormData(sel);
	} else {
		// 添加取消
		$('#content').form('clear');
		$('#datagrid_elementVal').datagrid('selectRow', 0);
	}
	
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}

//删除
function deleteItem() {
	if (!validFormEdit())
		return ;
	
	var record = $('#datagrid_elementVal').datagrid('getSelections');
	
	if (record.length == 0) {
		easyui_warn('请选择要删除的数据！');
		return;
	}
	
	$.messager.confirm("确认删除", "确认要删除选中的记录吗？", function(r){
		var ids = "";
		for (var i=0; i<record.length; i++) {
			if (i != 0)
				ids += ",";
			ids += record[i][keycolumn];
		}
		if (r){
			$.post(contextpath + 'base/dic/dicElementValSetController/deleteDicElementVals.do', {ids:ids, tablecode:tablecode}, function(data){
				easyui_auto_notice(data, function(){
					$('#datagrid_elementVal').datagrid("reload");
				});
			}, "json");
		}
	}); 
}

//校验当前表单数据状态
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