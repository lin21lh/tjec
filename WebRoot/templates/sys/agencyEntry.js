/**
 * 页面初始化jQuery脚本
 */
var baseUrl = contextpath + 'sys/dept/sysDeptController/';
var urls = {
		getAllAgencyTree : baseUrl + 'queryDeptTree.do',
		getAgencyDetail : baseUrl + 'get.do',
		saveAgencyDetail : baseUrl + 'save.do',
		queryUnselectedExp : baseUrl + 'queryUnselectedExp.do',
		querySelectedExp : baseUrl + 'querySelectedExp.do',
		saveAgencyExpcfg : baseUrl + 'saveDeptExpCfg.do',
		deleteAgency : baseUrl + 'delete.do',
		getExpColumnsHTML : baseUrl + 'getExpColumnsHTML.do'
};

var last_node = null; //记住上次所选数据 NODE
var edit_flag = false;  //是否正在编辑

var left_datagrid = null; //左侧可选择配置项
var right_datagrid = null; //右侧已选择配置项
var delTRs = ""; //可配置选项
var agencyTree = null;

$(function() {
	agencyTreeDataLoad();
	comboboxLoadData();
	$('#content').form({
		onLoadSuccess:function(){
			edit_flag = false;
			setSaveButtonStatus(edit_flag);
		}
	});
	
	leftRightDataGrid();
});

function comboboxLoadData() {
	comboboxFunc("area", "SYS_AREA", "code");
	comboboxFunc("agencycat", "AGENCYCAT", "code");
	comboboxFunc("q_agencycat", "AGENCYCAT", "code");
	comboboxFunc("status", "SYS_STATUS", "code");
	$('#agencycat').combobox({
		onSelect : function(record) {
			if (delTRs.length > 0) {
				var trids = delTRs.split("~");
				for (var i=0; i<trids.length; i++) {
					$("input[id="+trids[i]+"]").parent().parent().remove();
				}
			}
			$.post(urls.getExpColumnsHTML, {
				agencycat : record.id
			}, function(result) {
				if (result.success) {
					if (result.title.length > 0) {
						var res = result.title.split("`");
						$("#agencytab").append(res[0]);
						delTRs = res[1];
						var textboxs = delTRs.split("~");
						for(var i=0; i<textboxs.length; i++){
							$("#" + textboxs[i]).textbox();
						}
						
						if(res.length >= 3){
							var comboxs = res[2];
							var comboss = comboxs.split("~");
							for (var n=0; n<comboss.length; n++) {
								var comb = comboss[n].split(",");
								comboboxFunc(comb[0], comb[1]);
							}
						}
						
					}
					
				} else {
					easyui_warn(result.title);
				}
			}, 'json');
		}
	});
	$('#q_agencycat').combobox({
		onSelect : function(record) {
			left_datagrid.datagrid("load", {agencycat : record.id});
			right_datagrid.datagrid("load", {agencycat : record.id});
		}
	});
}

//左侧可选扩展属性 右侧已选扩展属性
function leftRightDataGrid() {
	left_datagrid = $("#left_data").datagrid({
		fit : true,
		stripe : true, 
		rownumbers : true,
		url : urls.queryUnselectedExp,
		loadMsg : "正在加载，请稍候......",
		onDblClickRow : function(rowIndex, rowData) {
			$(this).datagrid("deleteRow", rowIndex);
			right_datagrid.datagrid("appendRow", rowData);
		},
		columns : [[
		    {
		    	field: "columnid",
		    	checkbox: true
		    }, {
            	field : "columncode",
            	title : "列名",
            	width : 120
            }, {
            	field : "columnname",
            	title : "中文名称",
            	width : 120
            }
		]]
	});
	
	right_datagrid = $("#right_data").datagrid({
		fit : true,
		stripe : true, 
		rownumbers : true,
		url : urls.querySelectedExp,
		loadMsg : "正在加载，请稍候......",
		onDblClickRow : function(rowIndex, rowData) {
			$(this).datagrid("deleteRow", rowIndex);
			left_datagrid.datagrid("appendRow", rowData);
		},
		columns : [[
			{
				field: "columnid",
				checkbox: true
			}, {
            	field : "columncode",
            	title : "列名",
            	width : 120
            }, {
            	field : "columnname",
            	title : "中文名称",
            	width : 120
            }
		]]
	});
}

/**
 * 加载树
 */
function agencyTreeDataLoad() {
	agencyTree = $('#agencyTree').tree({
				url : urls.getAllAgencyTree,
				method : 'post',
				animate : true,
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
					if (node.status == '0') {
						return node.text;
					} else {
						return "<span style='color:red'>" + node.text + "</span>";
					}
						
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
						selectNode = agencyTree.tree('find', last_node.id);
					
					if (selectNode) {
						agencyTree.tree('expandTo', selectNode.target);
						agencyTree.tree('select', selectNode.target);
						// showAgencyDetail(firstNode);
					}
				},
				onBeforeSelect : function(node) {
					return validFormEdit();
				},
				onSelect : function(node) {
					$('#opertype').val('edit');
					// 如果没有正在编辑
					showAgencyDetail(node);
					last_node = node;
					edit_flag = false;
					setSaveButtonStatus(edit_flag);
				}
			});
}

/**
 * 
 * @param node
 */
function showAgencyDetail(node) {
	var n_id = node.id;
	if (delTRs.length > 0) {
		var trids = delTRs.split("~");
		for (var i=0; i<trids.length; i++) {
			$("input[id="+trids[i]+"]").parent().parent().remove();
		}
	}
	
	$.post(urls.getExpColumnsHTML, {
		agencycat : '',
		itemid : n_id
		}, function(result) {
			if (result.success) {
				if (result.title.length > 0) {
					var res = result.title.split("`");
					$("#agencytab").append(res[0]);
					delTRs = res[1];
					var textboxs = res[1].split("~");
					for(var i=0; i<textboxs.length; i++){
						$("#" + textboxs[i]).textbox();
					}
					
					if (res.length > 2) {
						var comboxs = res[2];
						var comboss = comboxs.split("~");
						for (var n=0; n<comboss.length; n++) {
							var comb = comboss[n].split(",");
							comboboxFunc(comb[0], comb[1]);
						}
					}
				}
				$('#content').form('load', urls.getAgencyDetail + '?id=' + n_id);
			} else {
				easyui_warn(result.title);
			}
	}, 'json');
}


// 保存修改
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
		showAgencyDetail(last_node);
	
	$('#opertype').val('edit');
}

/**
 * 刷新树
 */
function agency_refresh() {
	agencyTree.tree({
		url : urls.getAllAgencyTree
	});
}

/**
 * 添加下级事件请求
 */
function agency_adddown_req() {
	var node = getTreeSelect();
	if (node != undefined) {
		if (node.levelno == codeformat.split('-').length) {
			$.messager.alert('警告', '编码格式为【' + codeformat + '】，当前级次为' + node.levelno + '级，不允许新增下级！', 'warnning');
			return ;
		}
			
		$('#itemid').val('');
		prepareNewAgency(node.code, node.id);
		$('#opertype').val('adddown');

		edit_flag = false;
		setSaveButtonStatus(edit_flag);
	}
}

/**
 *  删除事件请求
 */
function agency_del_req() {
	$.messager.confirm('确认', '是否删除该机构?', function(r) {
		if (r) {
			var node = getTreeSelect();
			if (node != undefined) {
				var nid = node.id;
				$.post(urls.deleteAgency, {
					itemid : nid
				}, function(result) {
					if (result.success) {
						$.messager.show({
							title : '提示',
							msg : result.title,
							timeout : 2000,
							showType : 'fade'
						});
						agency_refresh();
					} else {
						if (result.title) {
							$.messager.alert('警告', result.title, 'warnning');
						} else {
							$.messager.alert('警告', '删除过程中发生异常', 'warnning');
						}
					}
				}, 'json');
			} else {
				$.messager.alert('警告', '取选 中节点过程中发生异常!', 'warnning');
			}
		}
	});

}

/**
 * 添加机构事件
 */
function agency_add_req() {
	if (delTRs.length > 0) {
		var trids = delTRs.split("~");
		for (var i=0; i<trids.length; i++) {
			$("input[id="+trids[i]+"]").parent().parent().remove();
		}
	}
	$('#content').form('reset');
	$('#itemid').val('');
	$('#superitemid').val('');
	$('#opertype').val('add');
	$('#content').form('validate');
	
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}
/**
 * 取得树上选中的节点
 */ 
function getTreeSelect() {
	return agencyTree.tree("getSelected");
}

/**
 * 准备添加下级单位数据
 * @param code
 * @param superitemid
 */
function prepareNewAgency(code, superitemid) {
	$('#content').form('reset');
	$('#code').textbox('setValue', code);
	$('#superitemid').val(superitemid);
	$('#content').form('validate');
}

// 取得第一个节点，
function getFirstLeafNode() {
	var roots = agencyTree.tree('getRoots');
	return roots[0];
}

/**
 * 所有 form元素的onchange 事件
 */
function form_onchange(){
	edit_flag = true;
	setSaveButtonStatus(edit_flag);
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

//保存机构扩展属性配置
function savecolExpcfg() {
	var datas = right_datagrid.datagrid("getData");
	var agencycat = $("#q_agencycat").combobox("getValue");
	
	if (!agencycat) {
		easyui_warn("请选择机构类别！");
		return;
	}
	if (datas.total == 0) {
		easyui_warn("请选择可扩展属性！");
		return;
	}
	var ss = agencycat +"`";
	for (var i=0; i<datas.total; i++) {
		if (i > 0)
			ss += "~";
		ss += datas.rows[i].columncode;
	}
	
	$.post(urls.saveAgencyExpcfg, {
		str : ss
	}, function(msg) {
		easyui_auto_notice(msg);
	}, "JSON");
}

function leftAllClick(){
	$("#left_data").datagrid("selectAll");
	leftClick();
	$("#left_data").datagrid("unselectAll");
}

function leftClick() {
	var records = $("#left_data").datagrid('getSelections');
	if (records.length == 0) {
		easyui_warn('请选择可选扩展属性!');
		return;
	}
	var rowIndex = null;
	for (var i=0; i<records.length; i++) {
		rowIndex =  $("#left_data").datagrid('getRowIndex', records[i]);
		$("#left_data").datagrid('deleteRow', rowIndex);
		$("#right_data").datagrid('appendRow', records[i]);
	}
}

function rightAllClick(){
	$("#right_data").datagrid("selectAll");
	rightClick();
	$("#right_data").datagrid("unselectAll");
}

function rightClick() {
	var records = $("#right_data").datagrid('getSelections');
	if (records.length == 0) {
		easyui_warn('请选择已选扩展属性!');
		return;
	}
	var rowIndex = null;
	for (var i=0; i<records.length; i++) {
		rowIndex =  $("#right_data").datagrid('getRowIndex', records[i]);
		$("#right_data").datagrid('deleteRow', rowIndex);
		$("#left_data").datagrid('appendRow', records[i]);
	}
}

function upClick(){
	var records = $("#right_data").datagrid('getSelections');
	if (records.length == 0) {
		easyui_warn('请选择已选扩展属性，进行排序!');
		return;
	}
	
	for(var i=0; i<records.length; i++){
		var curIndex = $("#right_data").datagrid("getRowIndex", records[i]);
		if(records.length == 1 && curIndex == 0){
//			easyui_warn("无法向上排序！");
			return;
		}
		
		if(i == 0){
			continue;
		}
		
		var lastIndex = $("#right_data").datagrid("getRowIndex", records[i-1]);
		if(lastIndex == 0){
//			easyui_warn("无法向上排序！");
			return;
		}
		
		if(curIndex - lastIndex != 1){
			easyui_warn("请选择一条或者多条相邻的扩展属性记录，进行排序！");
			return;
		}
		
	}
	
	var firstIndex = parseInt($("#right_data").datagrid("getRowIndex", records[0]));
	var lastIndex = firstIndex - 1;
	var lastRow = $("#right_data").datagrid("getRows")[lastIndex];
	$("#right_data").datagrid("deleteRow", lastIndex);
	$("#right_data").datagrid("insertRow", {index: firstIndex + records.length -1, row:lastRow});
	
}

function downClick(){
	var records = $("#right_data").datagrid('getSelections');
	if (records.length == 0) {
		easyui_warn('请选择已选扩展属性，进行排序!');
		return;
	}
	
	var finalIndex = $("#right_data").datagrid("getRows").length-1;
	
	for(var i=0; i<records.length; i++){
		var curIndex = $("#right_data").datagrid("getRowIndex", records[i]);
		if(records.length == 1 && curIndex == finalIndex){
//			easyui_warn("无法向下排序！");
			return;
		}
		
		if(i+1 > records.length -1){
			continue;
		}
		
		var nextIndex = $("#right_data").datagrid("getRowIndex", records[i+1]);
		if(nextIndex == finalIndex){
//			easyui_warn("无法向下排序！");
			return;
		}
		
		if(nextIndex - curIndex != 1){
			easyui_warn("请选择一条或者多条相邻的扩展属性记录，进行排序！");
			return;
		}
		
	}
	
	var firstIndex = parseInt($("#right_data").datagrid("getRowIndex", records[records.length-1]));
	var nextIndex = firstIndex + 1;
	var nextRow = $("#right_data").datagrid("getRows")[nextIndex];
	$("#right_data").datagrid("deleteRow", nextIndex);
	$("#right_data").datagrid("insertRow", {index: nextIndex - records.length, row:nextRow});
	
}
