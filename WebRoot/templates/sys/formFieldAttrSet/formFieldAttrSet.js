/**
 * 表单字段属性配置维护
 */
var wftree_ = null; //工作流树
var wftask_ = null; //当前工作流节点列表
var allfields_ = null; //基本属性中所有字段列表
var sourceelementfields_ = null; //定值规则中有来源数据项的字段列表
var allrules_ = null; //定义规则列表
var edit_flag = false;  //是否正在编辑

$(function() {
	initWftree(); //初始化工作流树
	initWftaskDataGrid(); //初始化当前工作流节点列表
	initAllfieldsDataGrid(); //初始化基本属性中所有字段列表
	initSourceElementFields(); //初始化定值规则中有来源数据项的字段列表
	initControl(); //初始化表单控件
	initFixedValRule(); //初始化定值规则表单界面控件
	initAllRules(); //初始化定值规则列表
});

var baseUrl = contextpath + 'sys/formFieldAttrSet/FormFieldAttrSetController/';
var cvrBaseUrl = contextpath + 'sys/formFieldAttrSet/ElementRuleController/';
var urls = {
	getDetail : baseUrl + 'getDetail.do',
	add : baseUrl + 'add.do',
	edit : baseUrl + 'edit.do',
	del : baseUrl + 'delete.do',
	addConstantValRule : cvrBaseUrl + 'add.do',
	getConValRuleDetail : cvrBaseUrl + 'getDetail.do'
};

function reloadWfTask(node) {
	if (!node) {
		node = wftree_.tree('getSelected');
	}
	
	if (node) {
		var isLeaf = wftree_.tree('isLeaf', node.target);
		if (isLeaf && (node.key == undefined || node.key == null)) {
			var wfversion = node.text;
			var wfkey = wftree_.tree('getParent', node.target).key;
			wftask_.datagrid('load', {key:wfkey,wfversion:wfversion});
		} else {
			wftask_.datagrid('load', {key:'',wfversion:0});
		}
	} else {
		wftask_.datagrid('load', {key:'',wfversion:0});
	}
}

function reloadAllFields(node) {
	if (!node) {
		node = wftree_.tree('getSelected');
	}
	if (node) {
		if (node.key != undefined && node.key != null) {
			allfields_.datagrid('load', {wfkey:node.key,sourceElementFlag:false});
			sourceelementfields_.datagrid('load', {wfkey:node.key,sourceElementFlag:true});
		} else if (wftree_.tree('isLeaf', node.target)) {
			var wfkey = wftree_.tree('getParent', node.target).key;
			allfields_.datagrid('load', {wfkey:wfkey,sourceElementFlag:false});
			sourceelementfields_.datagrid('load', {wfkey:wfkey,sourceElementFlag:true});
		} else {
			allfields_.datagrid('load', {wfkey:'',sourceElementFlag:false});
			sourceelementfields_.datagrid('load', {wfkey:'',sourceElementFlag:true});
		}
	} else {
		allfields_.datagrid('load', {wfkey:'',sourceElementFlag:false});
		sourceelementfields_.datagrid('load', {wfkey:'',sourceElementFlag:true});
	}
}

function initWftree() {
	wftree_ = $('#wftree_').tree({
		url : contextpath + 'workflow/WfProcessDefinitionController/queryWorkflowTree.do',
		method : 'post',
		onSelect : function(node) {
			var isLeaf = wftree_.tree('isLeaf', node.target);
			if (isLeaf && (node.key == undefined || node.key == null)) {
				var wfversion = node.text;
				var wfkey = wftree_.tree('getParent', node.target).key;
				wftask_.datagrid('load', {key:wfkey,wfversion:wfversion});
			} else {
				wftask_.datagrid('load', {key:'',wfversion:0});
			}


			if (node.key != undefined && node.key != null) {
				allfields_.datagrid('load', {wfkey:node.key,sourceElementFlag:false});
				sourceelementfields_.datagrid('load', {wfkey:node.key,sourceElementFlag:true});
			} else if (isLeaf) {
				var wfkey = wftree_.tree('getParent', node.target).key;
				allfields_.datagrid('load', {wfkey:wfkey,sourceElementFlag:false});
				sourceelementfields_.datagrid('load', {wfkey:wfkey,sourceElementFlag:true});
			} else {
				allfields_.datagrid('load', {wfkey:'',sourceElementFlag:false});
				sourceelementfields_.datagrid('load', {wfkey:'',sourceElementFlag:true});
			}
		}
	});
}

function initWftaskDataGrid() {
	wftask_ = $('#wftask_').datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		url : contextpath + 'workflow/BussinessWorkFlowController/findActivitiesByKey.do',
		queryParams : {key:'',wfversion : 0},
		onLoadSuccess:function(data){
		},
		oncheck : function(index, row) {
		},
		onSelect : function(index, row) {
			var node = wftree_.tree('getSelected');
			var isLeaf = wftree_.tree('isLeaf', node.target);

			if (node.key != undefined && node.key != null) {
				allfields_.datagrid('load', {wfkey:node.key,sourceElementFlag:false});
				sourceelementfields_.datagrid('load', {wfkey:node.key,sourceElementFlag:true});
			} else if (isLeaf) {
				var wfkey = wftree_.tree('getParent', node.target).key;
				allfields_.datagrid('load', {wfkey:wfkey,sourceElementFlag:false});
				sourceelementfields_.datagrid('load', {wfkey:wfkey,sourceElementFlag:true});
			} else {
				allfields_.datagrid('load', {wfkey:'',sourceElementFlag:false});
				sourceelementfields_.datagrid('load', {wfkey:'',sourceElementFlag:true});
			}
		},
		columns : [[ {
			field : 'ck',
			checkbox : true
			},
            {
            	field : "activitiyid",
            	title : "节点ID",
            	width : 120
            },
            {
            	field : "activitiyname",
            	title : "节点名称",
            	width : 120
            }
		]]
	});
}

function initAllfieldsDataGrid() {
	allfields_ = $('#allfields_').datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		url : contextpath + 'workflow/BussinessWorkFlowController/findColumnByWfKey.do',
		queryParams : {wfkey:'',sourceElementFlag : false},
		onLoadSuccess:function(data){
			$('#fieldAttrForm').form('reset');
			if (data.total > 0) {
				allfields_.datagrid('selectRow', 0);
			}
		},
		onSelect : function(index, row) {
			$('#fieldAttrForm').form('reset');
			$('#fieldcode').textbox('setValue', row.columncode);
			if (row.sourceelementcode == '' || row.sourceelementcode == null) {
				$('#defvaltype').combobox('clear');
				$('#defvaltype').combobox('loadData', [{id : '0', text: '无缺省值'},{id: '1',text: '手动录入'}, {id: '3',text: '函数'}]);
				$('#defvaltype').combobox('readonly', false);
				$('#defvalue').textbox('enable');
				$('#defvalue').textbox('clear');
				$('#ordermodel').combobox('clear');
				$('#ordercontrol').textbox('clear');
				$('#ordermodel').combobox('readonly', true);
				$('#ordercontrol').textbox('readonly', true);
			} else {
				$('#defvaltype').combobox('loadData', [{id: '2',text: '来源数据项'}]);
				$('#defvaltype').combobox('setValue', '2');
				$('#defvaltype').combobox('readonly', true);
				$('#defvalue').textbox('setValue', '请在定值规则中设置缺省值');
				$('#defvalue').textbox('disable');
				$('#ordermodel').combobox('readonly', false);
				$('#ordercontrol').textbox('readonly', false);
			}
			var wfkey = '';
			var wfversion = '';
			var wfnode = '';
			var wfkeyNode = wftree_.tree('getSelected');
			if (wfkeyNode) {
				var isLeaf = wftree_.tree('isLeaf', wfkeyNode.target);
				if (isLeaf && (wfkeyNode.key == undefined || wfkeyNode.key == null)) {
					wfversion = wfkeyNode.text;
					wfkey = wftree_.tree('getParent', wfkeyNode.target).key;
				} else if (wfkeyNode.key != undefined && wfkeyNode.key != null) {
					wfkey = wfkeyNode.key;
				}
			}
			var wfnodeRecord = wftask_.datagrid('getSelected');
			if (wfnodeRecord != null) {
				wfnode = wfnodeRecord.activitiyid;
			}
			$.post(urls.getDetail, {
				wfkey : wfkey,
				wfversion : wfversion,
				wfnode : wfnode,
				fieldcode : row.columncode
			}, function(result) {
				
				if (result != null) {
					$('#fieldAttrForm').form('load', result);
				}
			}, 'json');
		},
		columns : [[ {
			field : 'columnid',
			checkbox : true
			}, {
            	field : "columncode",
            	title : "字段名",
            	width : 120
            }, {
            	field : "columnname",
            	title : "中文名称",
            	width : 120
            }, {
            	field : "tablecode",
            	title : "表名",
            	width : 120
            }
		]]
	});
}

function initSourceElementFields() {
	sourceelementfields_ = $('#sourceelementfields_').datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		url : contextpath + 'workflow/BussinessWorkFlowController/findColumnByWfKey.do',
		queryParams : {wfkey:'', sourceElementFlag : true},
		onLoadSuccess:function(data){
			
		},
		onClickRow : function(rowIndex, rowData) {
			
		},
		onSelect : function(rowIndex, rowData) {
			var wfkey = '';
			var wfversion = '';
			var wfnode = '';
			var wfkeyNode = wftree_.tree('getSelected');
			if (wfkeyNode) {
				var isLeaf = wftree_.tree('isLeaf', wfkeyNode.target);
				if (isLeaf && (wfkeyNode.key == undefined || wfkeyNode.key == null)) {
					wfversion = wfkeyNode.text;
					wfkey = wftree_.tree('getParent', wfkeyNode.target).key;
				} else if (wfkeyNode.key != undefined && wfkeyNode.key != null) {
					wfkey = wfkeyNode.key;
				}
			}
			var wfnodeRecord = wftask_.datagrid('getSelected');
			if (wfnodeRecord != null) {
				wfnode = wfnodeRecord.activitiyid;
			}
			
			allrules_.datagrid('reload', {wfkey : wfkey, wfversion : wfversion, wfnode : wfnode, elementcode : rowData.sourceelementcode});
		},
		columns : [[ {
			field : 'columnid',
			checkbox : true
			}, {
            	field : "columncode",
            	title : "字段编码",
            	width : 100
            }, {
            	field : "sourceelementcode",
            	title : "数据项编码",
            	width : 120
            }, {
            	field : "columnname",
            	title : "名称",
            	width : 120
            }
		]]
	});
}

function initControl() {
	$('#defvaltype').combobox({
		panelHeight : 'auto',
		valueField : 'id',
		textField : 'text',
		onSelect : function(record) {
			switch (record.id) {
			case '0':
				$('#defvalue').textbox('disable');
				break;
			case '1':
				$('#defvalue').textbox('enable');
				break;
			case '2':
				$('#defvalue').textbox('setValue', '请在定值规则中设置缺省值');
				$('#defvalue').textbox('disable');
				break;
			case '3':
				//$('#defvalue').combobox();
				break;

			default:
				break;
			}
		},
		onChange : function(oldValue, newValue) {
			if (oldValue != newValue) {
				form_onchange();
			}
		}
	});
	
	$('#ordermodel').combobox({
		panelHeight : 'auto',
		valueField : 'id',
		textField : 'text',
		data : [{id:'0',text:'至多'},{id:'1',text:'等于'},{id:'2',text:'至少'}],
		onChange : function(oldValue, newValue) {
			if (oldValue != newValue) {
				form_onchange();
			}
		}
	});
}

function initAllRules() {
	allrules_ = $('#allrules_').datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		url : contextpath + 'sys/formFieldAttrSet/ElementRuleController/query.do',
		queryParams : {elementcode : '', wfkey:'', wfversion : 0, wfnode : ''},
		onLoadSuccess:function(data){
			$('#constantValueForm').form('clear');
			var condTabs = $('#condtions');
			var all_tabs = condTabs.tabs('tabs');
			for (var n=all_tabs.length-1; n>=0; n--) {
				condTabs.tabs('close', condTabs.tabs('getTabIndex',all_tabs[n]));
			}
			
		},
		onClickRow : function(rowIndex, rowData) {
			
		},
		onSelect : function(rowIndex, rowData) {
			$('#constantValueForm').form('clear');
			$.post(urls.getConValRuleDetail, {ruleid : rowData.ruleid},  function(data){
				$('#constantValueForm').form('load', data);
				$.post(contextpath + 'base/datascope/datascopeController/getDataScopeDetailByID.do', {scopemainid:data.scopemainid}, function(data){
					clickdatascopemainDetail(data, '360px', false);
				}, "json");
			});
		},
		columns : [[ {
			field : 'ruleid',
			checkbox : true
			}, {
            	field : "rulename",
            	title : "规则名称",
            	width : 100
            }, {
            	field : "elementcode",
            	title : "主数据项编码",
            	width : 120
            }
		]]
	});
}

function initFixedValRule() {
	$('#namelist').treeDialog({
		title :'选择内容',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'valuelist',
		prompt: "请选择值集",
		editable :false,
		multiSelect: true, //多选树
		dblClickRow: false,
		url :contextpath + '/base/dic/QueryDicElementValController/findTreeListData.do',
		filters:{
			code: "编码",
			name: "名称"
		},
		clearFunc:function(){
			//TODO 点击清空按钮触发清除事件
		},
		assignFunc:function(value){
			//TODO 双击选择回填时触发赋值事件
			
		},
		beforeSearchFunc : function() {
			var record = sourceelementfields_.datagrid('getSelected');
			if (record == null) {
				easyui_warn('请选择对应的字段！');
				return false;
			}
			return {'sourceelementcode' : record.sourceelementcode,'enableKey' : '0', 'valueModel' : '', 'values' : ''};
		}
	});
	$('#defaultname').treeDialog({
		title :'选择缺省值',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'defaultvalue',
		prompt: "请选择缺省值",
		editable :false,
		multiSelect: false, //多选树
		dblClickRow: true,
		url :contextpath + '/base/dic/QueryDicElementValController/findTreeListData.do',
		filters:{
			code: "编码",
			name: "名称"
		},
		clearFunc:function(){
			//TODO 点击清空按钮触发清除事件
		},
		assignFunc:function(value){
			//TODO 双击选择回填时触发赋值事件
			
		},
		beforeSearchFunc : function() {
			var valuelist = $('#namelist').treeDialog('getValue');
			if (valuelist == null ||valuelist == '') {
				easyui_warn('请确定值集范围！');
				return false;
			}
			return {'enableKey' : '1', 'valueModel' : 'code', 'values' : valuelist};
		}
	});
}

/**
 * 所有 form元素的onchange 事件
 */
function form_onchange(){
	edit_flag = true;
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

//保存修改
function saveEdit() {
	var wftreeNode = wftree_.tree('getSelected');
	if (!wftreeNode) {
		easyui_warn('请选择工作流！');
		return ;
	}
	var wfkey = '';
	var wfversion = '';
	var wfnode = '';
	var isLeaf = wftree_.tree('isLeaf', wftreeNode.target);
	if (isLeaf && (wftreeNode.key == undefined || wftreeNode.key == null)) {
		wfversion = wftreeNode.text;
		$('#wfversion').val(wftreeNode.text);
		wfkey = wftree_.tree('getParent', wftreeNode.target).key;
		$('#wfkey').val(wfkey);
	} else if (wftreeNode.key != undefined && wftreeNode.key != null) {
		wfkey = wftreeNode.key;
		$('#wfkey').val(wfkey);
	} else {
		easyui_warn('请选择有效工作流节点！');
		return ;
	}
	
	var wfnodeRecord = wftask_.datagrid('getSelected');
	if (wfnodeRecord != null) {
		wfnode = wfnodeRecord.activitiyid;
		$('#wfnode').val(wfnodeRecord.activitiyid);
	}
	
	var tab = $('#fieldAttrTabs').tabs('getSelected');
	var index = $('#fieldAttrTabs').tabs('getTabIndex',tab);
	switch (index) {
	case 0: //基本属性
		
		var isValid = $('#fieldAttrForm').form('validate');
		if (!isValid) {
			easyui_warn('请完善表单信息！');
			return;
		}

		$('#fieldAttrForm').form('submit', {
			url : urls.add,
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
			}
		});
		break;
	case 1: //定值规则
		
		var tabs_all = $('#condtions').tabs('tabs');
		if (!tabs_all) {
			easyui_warn("请设置数据范围规则！");
			return;
		}
		
		var record = sourceelementfields_.datagrid('getSelected');
		if (record == null) {
			easyui_warn("请选择字段！");
			return;
		}
		var data = "[{";
		$.each(tabs_all, function (i, item) {
			var scopesubname = item.panel('options').title;
			if (i != 0)
			data += "}, {";
			data += "\"scopesubname\":\"" + scopesubname + "\"";
			data += ", \"items\":[";
			var forms = item.find("form");
			$.each(forms, function(j, form) {
				if (j != 0)
					data += ", ";
				var fd = $(form).form("getData", true);
				var scopevalues = ""; 
				if (fd.matchtype == 1) {
					var nodes = $(form).find("ul[id='scopevalTree']").tree("getChecked");
					
					for(var i=0; i<nodes.length; i++) {
						if (scopevalues.length > 0)
							scopevalues += ",";
						
						scopevalues += nodes[i].id;
					}
				}

				data += JSON.stringify(fd);
				data =	data.substring(0, data.length -1) + ",\"scopevalues\":\"" + scopevalues + "\"}";
			});
			data += "]";
		});
		data += "}]";
		data = "{ \"scopemainid\":\"" + '' + "\", \"subs\":" + data + "}";
		
		$('#constantValueForm').form('submit', {
			url : urls.addConstantValRule,
			queryParams : { wfkey : wfkey, wfversion : wfversion, wfnode : wfnode, scopedata : data, elementcode : record.sourceelementcode },
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
			}
		});
		break;

	default:
		break;
	}


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
 * 条件选中
 * @param id
 */
function divClick(id) {
	currenttdid = id;
	$("div[name$='cond']").css("border", "solid 0px");
	$("div[id$='" + id + "']").css("border", "solid 1px yellow");
}

/**
 * 新增条件项
 */
function addPanel(height){
    $.messager.prompt('录入规则名', '请录入规则', function(msg){
        if (msg){
        	var id = 'ct_'+msg;
            $('#condtions').tabs('add', {
                title: msg,
                content: "<tr id=" + id + " class=" + id + "></tr>",
                closable: false
            });
            
            addCondition(height, false);
        }
    });

}

/**
 * 新增条件
 */
function addCondition(height, readonly) {
	var tab = $('#condtions').tabs('getSelected');
	if (tab) {
		var title = tab.panel('options').title;
		$('#ct_' + title + '').append(addContent(title, height, readonly));
		$('#lay_' + currenttdid).layout();
		//数据项
		
		var matchtypeCombo = $("div[id$='" + currenttdid + "']").find("input[name$='matchtype']");
		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
		$("div[id$='" + currenttdid + "']").find("input[name$='elementname']").gridDialog({
			title :'要素选择',
			dialogWidth : 500,
			dialogHeight : 400,
			singleSelect: true,
			dblClickRow : true,
			showSearchWin : !readonly,
			showClearBtn : !readonly,
			missingMessage:"请选择条件字段",
			hiddenID : 'elementcode_' + currenttdid,
			hiddenName:'elementcode',
			valueField:'elementcode',
			textField:'elementname',
			filter: {
						cncode: "数据项编码",
						code: "elementcode",
						cnname: "数据项名称",
						name : "elementname"
					},
			url : contextpath + 'base/dic/dicElementController/findDicElementToDataScope.do',
			cols : [[
				{field : "ck", checkbox : true}, 
				{field : "elementcode",title : "数据项编码",width : 180},
		    	{field : "elementname", title : "数据项名称", width : 180},
		    	{field : "datatype", hidden : true}
		    ]],
		    assignFunc : function(row) {
	            switch (row.datatype) {
					case 'C':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude'][value=1]").attr("checked", "checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", false);
			           	$.post(contextpath + 'base/dic/dicElementValSetController/queryDicToDataScope.do', {'elementcode' : row.elementcode}, function(data) {
		    				$("div[id$='" + currenttdid + "']").find('ul').tree(
		    						{
		    							data : data.treeData,
		    							animate : true,
		    							checkbox : true,
		    							cascadeCheck : false,
		    							onClick : function(node) {
		    								if (!readonly)
		    									$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree('check', node.target);
		    							}
		    						}
		    				);
		            	},'JSON');
			           	scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			           	scopeValBox.textbox({value : '', type : 'text', iconCls : null, readonly : true});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						$("div[id$='" + currenttdid + "']").find("input[name$='matchtype']")
						matchtypeCombo.combobox('loadData', [{ id : '0', text : '全部'}, { id : '1', text : '多选'}]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
					    break;
					case 'S':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '8', text : '左匹配'}, { id : '9', text : '右匹配'}, { id : '10', text : '全匹配'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.textbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'N':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.numberbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'Y':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.numberbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'D':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.datebox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
	        	}
			}
		});
		matchtypeCombo.combobox({
			valueField : 'id',
			textField : 'text',
			data : [],
			onSelect : function(record) {
				if(record.id != 1) {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
				} else {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").show();
				}	
			}
		});
		
		scopeValBox.textbox({readonly : false});
	} else {
		addPanel(height);
	}
}

/**
 * 新增条件项内容
 * @param title
 * @param height
 * @param readonly
 * @returns {String}
 */
function addContent(title, height, readonly) {
	var tdid = parseInt(Math.random() *100000+1);
	currenttdid = tdid;
	var content = '<td class="' + tdid + '">';
	content += '<form>';
	content += '<div name="cond" class="cond" id=' + tdid + ' onclick="divClick(' + tdid + ')" style="border:solid 1px white;height:' + height+ ';width:310px;background-color:#eee;float:left;">';
	content += '<div id="lay_' + tdid + '" class="easyui-layout" data-options="fit:true">';
	content += '<div data-options="region:\'north\',split:true,border:false" style="height:120px;background-color:#eee;float:left;">';
	content += '<table>';
	content += '<tr><td>';
	content += '<input id="exists" class="easyui-validatebox" name="isinclude" type="radio" checked="checked" required="true" value="1"';
	if (readonly)
		content += ' disabled=true';
	content +=	'>包含</input>';
	content += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
	content += '<input id="noExists" class="easyui-validatebox" name="isinclude" type="radio" required="true" value="0"';
	if (readonly)
		content += 'disabled=true';
	content += '>排除</input>';
	content += '</td></tr>';
	content += '<tr><td>';
	content += '<input id="elementname" class="easyui-validatebox combobox" name="elementname" style="width:180px" required="true"';
	if (readonly)
		content += ' readonly=true';
	content+=' />';
	content += '</td></tr>';
	content += '<tr><td>';
	content += '<input id="matchtype" class="easyui-combobox" style="width:180px" name="matchtype"';
	if (readonly)
		content += ' readonly=true';
	content += '/>';
	content += '</td></tr>';
	content += '<tr><td id="scopevalTD">';
	content += '<input type="text" name="scopevalue" style="width:180px"';
	if (readonly)
		content += ' readonly=true';
	content += '/>';
	content += '</td></tr>';
	content += '</table>';
	content += '</div>';
	content += '<div name="scopevalPanel" data-options="region:\'center\',border:false" style="background-color:#eee;float:left;">';
	content += '<ul id="scopevalTree" class="easyui-tree"></ul>';
	content += '</div>';
	content += '</div>';
	content += '</form>';
	content += '</td>';
	
	return content;
}

/**
 * 删除条件项
 */
function removePanel(){
    $.messager.confirm('提示', '确定要删除吗?', function(r){
    	if (r) {
	        var tab = $('#condtions').tabs('getSelected');
	        if (tab){
	            var index = $('#condtions').tabs('getTabIndex', tab);
	            $('#condtions').tabs('close', index);
	        }
    	}
    });
}

/**
 * 新增条件
 */
function addCondition(height, readonly) {
	var tab = $('#condtions').tabs('getSelected');
	if (tab) {
		var title = tab.panel('options').title;
		$('#ct_' + title + '').append(addContent(title, height, readonly));
		$('#lay_' + currenttdid).layout();
		//数据项
		
		var matchtypeCombo = $("div[id$='" + currenttdid + "']").find("input[name$='matchtype']");
		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
		$("div[id$='" + currenttdid + "']").find("input[name$='elementname']").gridDialog({
			title :'要素选择',
			dialogWidth : 500,
			dialogHeight : 400,
			singleSelect: true,
			dblClickRow : true,
			showSearchWin : !readonly,
			showClearBtn : !readonly,
			missingMessage:"请选择条件字段",
			hiddenID : 'elementcode_' + currenttdid,
			hiddenName:'elementcode',
			valueField:'elementcode',
			textField:'elementname',
			filter: {
						cncode: "数据项编码",
						code: "elementcode",
						cnname: "数据项名称",
						name : "elementname"
					},
			url : contextpath + 'base/dic/dicElementController/findDicElementToDataScope.do',
			cols : [[
				{field : "ck", checkbox : true}, 
				{field : "elementcode",title : "数据项编码",width : 180},
		    	{field : "elementname", title : "数据项名称", width : 180},
		    	{field : "datatype", hidden : true}
		    ]],
		    assignFunc : function(row) {
	            switch (row.datatype) {
					case 'C':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude'][value=1]").attr("checked", "checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", false);
			           	$.post(contextpath + 'base/dic/dicElementValSetController/queryDicToDataScope.do', {'elementcode' : row.elementcode}, function(data) {
		    				$("div[id$='" + currenttdid + "']").find('ul').tree(
		    						{
		    							data : data.treeData,
		    							animate : true,
		    							checkbox : true,
		    							cascadeCheck : false,
		    							onClick : function(node) {
		    								if (!readonly)
		    									$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree('check', node.target);
		    							}
		    						}
		    				);
		            	},'JSON');
			           	scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			           	scopeValBox.textbox({value : '', type : 'text', iconCls : null, readonly : true});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						$("div[id$='" + currenttdid + "']").find("input[name$='matchtype']")
						matchtypeCombo.combobox('loadData', [{ id : '0', text : '全部'}, { id : '1', text : '多选'}]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
					    break;
					case 'S':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '8', text : '左匹配'}, { id : '9', text : '右匹配'}, { id : '10', text : '全匹配'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.textbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'N':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.numberbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'Y':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.numberbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'D':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.datebox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
	        	}
			}
		});
		matchtypeCombo.combobox({
			valueField : 'id',
			textField : 'text',
			data : [],
			onSelect : function(record) {
				if(record.id != 1) {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
				} else {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").show();
				}	
			}
		});
		
		scopeValBox.textbox({readonly : false});
	} else {
		addPanel(height);
	}
}

/**
 * 删除条件
 */
function removeCondition() {
	var tab = $('#condtions').tabs('getSelected');
	if (tab) {
		var title = tab.panel('options').title;
		$('.'+currenttdid+'').remove();
		if ($("#ct_" + title).find("td").length == 0) {
		     var index = $('#condtions').tabs('getTabIndex', tab);
		     $('#condtions').tabs('close', index);
		}
	}
}

/**
 * 点击数据权限树 赋值
 * @param data
 */
function clickdatascopemainDetail(data, height, readonly) {
	
	if (data.success) {
		var condTabs = $('#condtions');
		var all_tabs = condTabs.tabs('tabs');
		for (var n=all_tabs.length-1; n>=0; n--) {
			condTabs.tabs('close', condTabs.tabs('getTabIndex',all_tabs[n]));
		}
		
		var scopemain = data.body;
		$("input[name$='scopemainid']").val(scopemain.scopemainid);
		$("input[name$='scopemainname']").val(scopemain.scopemainname);
		var scopesubs = scopemain.subList ? scopemain.subList : new Array();
		for (var i=0; i<scopesubs.length; i++) {
        	var id = 'ct_'+scopesubs[i].scopesubname;
            $('#condtions').tabs('add', {
                title: scopesubs[i].scopesubname,
                content: "<tr id=" + id + " class=" + id + "></tr>",
                closable: false
            });
            var scopeitems = scopesubs[i].itemList;
            for (var j=0; j<scopeitems.length; j++) {
            	addCondition(height, readonly);
            	var mtCombobox = $("div[id$='" + currenttdid + "']").find("input[id$='matchtype']");
            	mtCombobox.combobox({
            		onSelect : function(record) {
            			if (record.id == '12') {
            				scopeValBox.textbox('clear');
            				scopeValBox.textbox('disable');
            			} else {
            				scopeValBox.textbox('enable');
            			}
            		}
            	});
            	$("div[id$='" + currenttdid + "']").find("input[id$='elementname']").gridDialog('setValue', scopeitems[j].elementcode);
            	$("div[id$='" + currenttdid + "']").find("input[id$='elementname']").gridDialog('setText', scopeitems[j].elementname);
            	switch (scopeitems[j].datatype) {
					case 'C':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude'][value="+scopeitems[j].isinclude+"]").attr("checked", "checked");
						mtCombobox.combobox('loadData', [{ id : '0', text : '全部'}, { id : '1', text : '多选'}]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", false);
						scopeValBox.textbox({readonly : true});
						
		            	$.post(contextpath + 'base/dic/dicElementValSetController/queryDicToDataScope.do', {'elementcode' :  scopeitems[j].elementcode,'scopeitemid':scopeitems[j].scopeitemid, currenttdid : currenttdid}, function(data) {
		            		$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree(
		    						{
		    							data : data.treeData,
		    							checkbox : true,
		    							cascadeCheck : false,
		    							onBeforeCheck : function(node, checked) {
		    								if (node.isChecked == '1' && checked)
		    									return true;
		    								else {
		    									return !readonly;
		    								}
		    							},
		    							onClick : function(node) {
		    								if (!readonly)
		    									$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree('check', node.target);
		    							}
		    						
		    						}
		    				);
		            		
		            	},'JSON');
						
					    break;
					case 'S':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '8', text : '左匹配'}, { id : '9', text : '右匹配'}, { id : '10', text : '全匹配'}, {id : '12', text : '为空'}, { id : '13', text : '不为空' }]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.textbox({readonly : false, value : scopeitems[j].scopevalue});
						break;
					case 'N':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, {id : '12', text : '为空'}, { id : '13', text : '不为空' }]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.numberbox({readonly : false, value : scopeitems[j].scopevalue});
						break;
					case 'Y':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, {id : '12', text : '为空'}, { id : '13', text : '不为空' }]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.numberbox({readonly : false, value : scopeitems[j].scopevalue});
						break;
					case 'D':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, {id : '12', text : '为空'}]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.datebox({readonly : false, value : scopeitems[j].scopevalue});
						break;
            	}
            	
            	$("div[id$='" + currenttdid + "']").find("input[id$='matchtype']").combobox('setValue', scopeitems[j].matchtype);

				if(scopeitems[j].matchtype == 0) {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
				} else
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").show();
			
            }
		}
	} else {
		easyui_warn(data.title);
	}
		
}