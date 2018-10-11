
var urls = {
	saveDicElement : contextpath + 'base/dic/dicElementController/saveDicElement.do', //基础数据项保存路径
	saveDicElementView : contextpath + 'base/dic/dicElementController/saveDicElementView.do', //数据项视图保存路径
	saveElementVal : contextpath +  'base/dic/dicElementValSetController/saveElementVal.do', //数据项值集维护
	delDicElement : contextpath +  'base/dic/dicElementController/deleteDicElement.do', //基础数据项删除路径
	formDicElement : contextpath +  'base/dic/dicElementController/dicDefFormEntry.do', //基础数据项维护界面路径
	formDicElementView : contextpath +  'base/dic/dicElementController/dicViewFormEntry.do', //
	elementValSet : contextpath +  'base/dic/dicElementValSetController/entry.do', //数据项值集维护界面路径
	queryDicElement : contextpath +  'base/dic/dicElementController/queryPageDicElement.do', // 基础数据项查询路径
	tablecodeWin : contextpath +  'base/dic/dicElementController/tablecodeWin.do', //数据表窗口路径
	addDicDefForm : contextpath +  'base/dic/dicElementController/addDicDefForm.do', //数据项新增界面路径
	deleteDicElement : contextpath +  'base/dic/dicElementController/deleteDicElement.do', //数据项删除路径
	saveTreeElementVal : contextpath +  'base/dic/dicElementValSetController/saveTreeElementVal.do', //层码数据项维护路径
	validLevelElement : contextpath +'base/dic/validRuleController/validLevelElement.do', //层码级次校验
	deleteDicTreeElementVals : contextpath + 'base/dic/dicElementValSetController/deleteDicTreeElementVals.do', //层码数据项值集删除
	getSourceDicElements : contextpath + 'base/dic/dicElementController/getSourceDicElements.do',
	isExistUI : contextpath + 'base/tabsdef/SysDicUIDesignController/isExistUI.do'
};

var types = {
	view : "view",
	add : "add",
	edit : "edit",
	dicValEdit : "dicValEdit"
};

window.opts = {
	    topMost: false,
	    extToolbar: true,
	    toolbar: [
	        "<span style='font-size:small'>表名称：</span>",
	    { id: "tablecode", type: "textbox", options: { "width": 80 } },
	    "<span style='font-size:small'>中文名称：</span>",
	    { id: "tablename", type: "textbox", options: { "width": 80 } },
	    {
	        type: "button", options: {
	            text: "查询", iconCls: "icon-search", handler: function (toolbar) {
	                var bar = $(toolbar), tabcode = bar.find("#tablecode").val(), tabname = bar.find("#tablename").val(),
	                dg = bar.closest(".grid-selector-container").find(".grid-selector");
	                dg.datagrid("load", { tablecode: tabcode, tablename: tabname});
	            }
	        }
	    }
	    ],
	    method: "POST",
	    url: "base/tabsdef/SysDicTableController/query.do",
	    idField: "tablecode",
	    textField: "tablename",
	    singleSelect: true,
	    autoShowPanel: false,
	    multiple: false,
	    remoteSort: false,
	    pagination: true,
	    columns: [[
	        { field: "tablecode", title: "表名称", width: 120, sortable: true, filterable: true },
	        { field: "tablename", title: "中文名称", width: 140, sortable: true },
	        { field: "remark", title: "备注", width: 160, sortable: true}
	    ]],
	    onEnter: function (val) { $.easyui.messager.show(String(val.map(function (val) { return val.ID; }))); }
	};

/**
 * 
 * @param title
 * @param url
 * @param dataGrid
 * @param formID
 * @param fill
 * @param width
 * @param height
 */
function showDialog(title, url, dataGrid, formID, fill, width, height, operType, elementtype) {
	parent.$.modalDialog({
		title : title,
		width : width,
		height : height,
		href : url,
		onLoad: function(){
			parent.$.modalDialog.openner_dataGridURL = url;
			parent.$.modalDialog.openner_dataGrid = dataGrid;
			if(fill){
				var f = parent.$.modalDialog.handler.find('#' + formID);
				f.form("load", dataGrid.datagrid("getSelected"));
			}
		},
		buttons : operButtons(operType, url, dataGrid, formID, elementtype)
	});
}

/**
 * 根据操作类型来获取操作按钮
 * @param operType	操作类型
 * @param url	对应的操作URL
 * @returns {___anonymous3684_3690}
 */
function operButtons(operType, url, dataGrid, formID, elementtype) {
	
	var buttons = [];
	if(operType == types.view){
		buttons = [
		   		{
					text:"关闭",
					iconCls: "icon-cancel",
					handler: function(){
						parent.$.modalDialog.handler.dialog('close');
					}
				}
			];
	}else if(operType == types.add){
		buttons = [
       		{
				text:"保存",
				iconCls: "icon-save",
				handler: function(){
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var form = parent.$.modalDialog.handler.find('#'+formID);
					var elementtype = parent.$.modalDialog.handler.find('#add_elementtype').combobox("getValue");
					if (elementtype == "0")
						saveDicElement(urls.saveDicElement, form);
					else {
						form = parent.$.modalDialog.handler.find('#dicViewElement_form');
						saveDicElementView(urls.saveDicElementView, form);
					}
				}
		    },
		    {
		    	text:"关闭",
		    	iconCls: "icon-cancel",
		    	handler: function(){
		    		parent.$.modalDialog.handler.dialog('close');
		    	}
		    }
		];
	}else if(operType == types.edit){
		buttons = [
      		{
   				text:"修改",
   				iconCls: "icon-save",
   				handler: function(){
   					parent.$.modalDialog.openner_dataGrid = dataGrid;
   					var form = parent.$.modalDialog.handler.find('#'+formID);
					if (elementtype == 0)
						saveDicElement(urls.saveDicElement, form);
					else
						saveDicElementView(urls.saveDicElementView, form);
   				}
   		    },
   		    {
   		    	text:"关闭",
   		    	iconCls: "icon-cancel",
   		    	handler: function(){
   		    		parent.$.modalDialog.handler.dialog('close');
   		    	}
   		    }
	   	];
	} else if (operType == types.dicValEdit) {
		buttons = [];
	}
	return buttons;
}

//保存
function saveDicElement(url, form) {
	form.form('submit', {
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
		success : function(data) {
			parent.$.messager.progress('close');
			try {
				data = eval("(" + data + ")");
			} catch (e) {

			}
			if (data == null || data == undefined) {
				easyui_warn('保存失败！');
				return;
			}
			if (data.success == true) {
				parent.$.messager.show({
					title:'提示',
					msg:data.title,
					position : 'center',
					timeout: 1000,
					style:{
						right:'',
						top:document.body.height/2-125,
						bottom:'',
						left:document.body.width/2-50
					}
				});
				setTimeout(function() {
					parent.$.modalDialog.handler.dialog('close');
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
				},0);

			} else {
				easyui_warn(data.title);
			}

		}
	});
}

var editIndex = undefined;
//function endEditing(){
//    if (editIndex == undefined){return true;}
//    if ($('#dicViewConditions').datagrid('validateRow', editIndex)){
//        var ed = $('#dicViewConditions').datagrid('getEditor', {index:editIndex, field:'operator'});
//        var operatorname = $(ed.target).combobox('getText');
//        $('#dicViewConditions').datagrid('getRows')[editIndex]['operatorname'] = operatorname;
//        $('#dicViewConditions').datagrid('endEdit', editIndex);
//        editIndex = undefined;
//        return true;
//    } else {
//        return false;
//    }
//}
//function onClickRow(index){
//    if (editIndex != index){
//        if (endEditing()){
//            $('#dicViewConditions').datagrid('selectRow', index).datagrid('beginEdit', index);
//            editIndex = index;
//        } else {
//            $('#dicViewConditions').datagrid('selectRow', editIndex);
//        }
//    }
//}
//function append(){
//	var validmsg = appendValid();
//	if (validmsg) {
//		$.messager.alert('提示', validmsg, 'warnning');
//		return;
//	}
//	$("#editor_columncode").combobox("reload", url);
//    if (endEditing()){
//        $('#dicViewConditions').datagrid('appendRow',{});	
//        
//        editIndex = $('#dicViewConditions').datagrid('getRows').length-1;
//        $('#dicViewConditions').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
//    	var url = contextpath + 'dic/getColumnsByElementcode.do?elementcode=' + $("#sourceelement_view").combobox("getValue");
//    	
//    	
//    	var ed = $('#dicViewConditions').datagrid('getEditor', {index:editIndex,field:'columncode'});
//    	$(ed.target).combobox("reload", url);
//    }
//}
var columntype = '';
function addViewFilter(oper) {
	var validmsg = appendValid(oper);
	if (validmsg) {
		$.messager.alert('提示', validmsg, 'warnning');
		return;
	}
	var elementcode = oper == 'add' ? $("#sourceelement_view").combobox("getValue") : $("#sourceelement_edit").val();
	var url = contextpath + 'base/dic/dicElementController/getColumnsByElementcode.do?elementcode=' + elementcode;
	$('#columncode_edit').combobox({
		url : url,
		valueField : 'columncode',
		textField : 'columnname',
		panelHeight : 'auto',
		onSelect : function(record) {
			$('#minvalue_edit').validatebox({});
			$('#maxvalue_edit').validatebox({});
			if (record.columntype == 'C') {
				$('#minvalue_edit').combobox({
					url : contextpath + "base/dic/dicElementValSetController/queryByElementcode.do?elementcode=" + record.sourceelementcode,
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto'
				});
				
				$('#maxvalue_edit').combobox({
					url : contextpath + "base/dic/dicElementValSetController/queryByElementcode.do?elementcode=" + record.sourceelementcode,
					valueField : 'id',
					textField : 'text',
					panelHeight : 'auto'
				});
				columntype = 'C';
			} else if (record.columntype == 'D') {
				$('#minvalue_edit').datebox({
				});
				
				$('#maxvalue_edit').datebox({
				});
				columntype = 'D';
			} else if (record.columntype == 'T') {
				$('#minvalue_edit').datetimebox({
				});
				
				$('#maxvalue_edit').datetimebox({
				});
				columntype = 'T';
			} else {
				$('#minvalue_edit').validatebox({
					type : 'text'
				});
				
				$('#maxvalue_edit').validatebox({
					type : 'text'
				});
				columntype = 'S';
			}
		}
	});
	$('#viewFilter_form').form('clear');
	$('#viewFilter').dialog('open');
}

function editViewFilter() {
	
}

function saveViewFilter() {
//	var form = $('#viewFilter_form');
//	if (!form.form('validate'))
//		return;
	var columncodeVal = $('#columncode_edit').combobox('getValue');
	var columnnameVal = $('#columncode_edit').combobox('getText');
	var minvalueV = '';
	var minvaluenameV = '';
	var maxvalueV = '';
	var maxvaluenameV = '';
	if (columntype = 'C') {
		minvalueV = $('#minvalue_edit').combobox('getValue');
		minvaluenameV = $('#minvalue_edit').combobox('getText');
		maxvalueV = $('#maxvalue_edit').combobox('getValue');
		maxvaluenameV = $('#maxvalue_edit').combobox('getText');
	} else if (columntype == 'D') {
		minvalueV = $('#minvalue_edit').datebox('getValue');
		minvaluenameV = $('#minvalue_edit').datebox('getValue');
		maxvalueV = $('#maxvalue_edit').datebox('getValue');
		maxvaluenameV = $('#maxvalue_edit').datebox('getValue');
	} else if (columntype == 'T') {
		minvalueV = $('#minvalue_edit').datetimebox('getValue');
		minvaluenameV = $('#minvalue_edit').datetimebox('getValue');
		maxvalueV = $('#maxvalue_edit').datetimebox('getValue');
		maxvaluenameV = $('#maxvalue_edit').datetimebox('getValue');
	} else {
		minvalueV = $('#minvalue_edit').val();
		minvaluenameV = $('#minvalue_edit').val();
		maxvalueV = $('#maxvalue_edit').val();
		maxvaluenameV = $('#maxvalue_edit').val();
	}
	$('#dicViewConditions').datagrid('appendRow', {seqno:$('#seqno_edit').val(), 
		leftbracket : $('#leftbracket_edit').attr('checked') ? '(' : '', notsymbol : $('#notsymbol_edit').combobox('getValue'),
		columncode : columncodeVal, columnname : columnnameVal, operator : $('#operator_edit').combobox('getValue'),
		minvalue : minvalueV, minvaluename: minvaluenameV, maxvalue:maxvalueV, maxvaluename : maxvaluenameV,
		rightbracket : $('#rightbracket_edit').attr('checked') ? ')' : '', logicalsymbol : $('#logicalsymbol_edit').combobox('getValue')});
	$('#viewFilter').dialog('close');
}

//function removeit(){
//    if (editIndex == undefined){return;}
//    $('#dicViewConditions').datagrid('cancelEdit', editIndex)
//            .datagrid('deleteRow', editIndex);
//    editIndex = undefined;
//}
//function accept(){
//	if ($('#elementclass_edit'))
//    if (endEditing()){
//        $('#dicViewConditions').datagrid('acceptChanges');
//    }
//}
//function reject(){
//    $('#dicViewConditions').datagrid('rejectChanges');
//    editIndex = undefined;
//}

function appendValid(oper) {
	var elementclass = oper == 'add' ? $('#elementclass_view').combobox("getValue") : $('#elementclass_edit').combobox('getValue');
	var sourceelement = oper == 'add' ? $('#sourceelement_view').combobox("getValue") : $('#sourceelement_edit').val();
	var elementcode = oper == 'add' ? $('#elementcode_view').val() : $('#elementcode_edit').val();
	if (!elementclass)
		return '请先选择数据项分类';
	
	if (!sourceelement)
		return '请先选择源数据项';
	
	if (!elementcode)
		return '请先输入编码';
	
	return null;
}

//保存
function saveDicElementView(url, form) {
//	if (editIndex) {
//		easyui_warn( '当前条件项正在编辑状态！');
//		return;
//	}
	parent.$.modalDialog.handler.find('#elementviewfilters').val(JSON.stringify(parent.$.modalDialog.handler.find('#dicViewConditions').datagrid('getData').rows));
	form.form('submit', {
		url : url,
		onSubmit : function() {
			
			return form.form('validate');
		},
//		data : {
//			conditions : parent.$.modalDialog.handler.find('#dicViewConditions').datagrid('getData')
//		},
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
				parent.$.modalDialog.handler.dialog('close');
				easyui_info(data.title, function() {parent.$.modalDialog.openner_dataGrid.datagrid('reload');});
			} else {
				easyui_warn(data.title);

			}

		}
	});
}