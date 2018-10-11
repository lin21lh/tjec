var isinitPorp=true;

var edit_flag = false; // 是否正在编辑的标志 可以为true,false
var fd = new JbfFormDesigner();
$(function() {
	
	$("#schemename").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});
	
	$("#remark").textbox().textbox("textbox").bind("change", function(){
		formchanged();
	});

	$('#schemalist').datagrid({
		fit : true,
		border : false,
		singleSelect : true,
		url : contextpath + 'base/tabsdef/SysDicUIDesignController/queryDicUIScheme.do',
		queryParams : {
			'tablecode' : tablecode
		},
		columns : [ [ {
			field : 'schemeid',
			hidden : true
		},{
			title : '界面名称',
			field : 'schemename',
			width : 200
		}, {
			title : '是否启用',
			field : 'used',
			width : 80,
			formatter: function(value){
				if (value == 1){
					return '启用';
				} else {
					return '未启用';
				}
			}
		} ] ],
		toolbar : '#schematb',
		pagination : false,
		onClickRow : function(rowIndex, rowData) {
			$('#uiForm').form('load', rowData);
			loadColumnsTree(tablecode, rowData.schemeid);
			loadFormDesigner(rowData.schemeid);
			loadDatagridHeader(tablecode, rowData.schemeid);
		}
	});
	window.formdesigner_ = fd;
	fd.build({
		canvasId : 'fcanvas',
		propertyGridId : '#prop'
	});

	fd.initPropertyGrid();
	$('#designertab').tabs({
		onSelect:function(title,index){
			if(index==0){
				if(isinitPorp==true){
					isinitPorp=false;
					//初始化propertygrid
				
					fd.initPropertyGrid();
					
				}
			}
		}
	});
});

function loadColumnsTree(tablecode, schemeid) {
	$('#columnfields').tree({
		url : contextpath + 'base/tabsdef/SysDicUIDesignController/getColumns.do?tablecode=' + tablecode + '&schemeid=' + schemeid,
		method : 'post',
		animate : true,
		checkbox : true,
		onClick : function(node) {
			var checkedNodes = $('#columnfields').tree('getChecked');
			var pand = true;
			for (var i=0; i<checkedNodes.length; i++) {
				if (checkedNodes[i].columncode == node.columncode) {
					pand = false;
					 break;
				}
			}
			$('#columnfields').tree(pand ? 'check' : 'uncheck', node.target);
		},
		onCheck : function(node, checked) {
			$('#datagrid_UIDesign').datagrid(checked ? 'showColumn' : 'hideColumn', node.columncode);
		} 
	});
}

function loadFormDesigner(uischemaid) {
	// 取得xml定义
	var xml_url = contextpath
			+ 'base/tabsdef/SysDicUIDesignController/getUiSchemaContent.do';
	$.post(xml_url,{ 	schemeid : uischemaid},
			
			function(xml) {
				$.post(contextpath+ 'base/tabsdef/SysDicUIDesignController/queryTableColumns.do',
								{
									tablecode : tablecode
								},
								function(cols) {
									var model = fd.parseModel(xml);
									//
									for ( var i in model.controls) {
										for ( var j in cols) {
											if (model.controls[i].name
													.toUpperCase() == cols[j].name
													.toUpperCase()) {
												cols[j].used = true;
											}
										}
									}
									model.optionalControls = cols;
									fd.loadModel(model); // 加载数据
									

								}, "JSON");

					});
}

function loadDatagridHeader(tablecode, schemeid) {
	  $.post(contextpath + 'base/tabsdef/SysDicUIDesignController/getDatagridUIDesign.do', {'tablecode' :  tablecode, 'schemeid' : schemeid}, function(data) {
			$('#datagrid_UIDesign').datagrid({
				fit : true,
				border : false,
				enableHeaderContextMenu : false,
				loadMsg : "正在加载，请稍候......",
				frozenColumns : [data.frozenColumns],
				columns : [data.columns],
				data : [{}],
				remoteSort : false
			});
			$('#datagrid_UIDesign').datagrid("columnMoving");
	},'JSON');
}

function getListDesigner() {
	var xml = new XmlWriter();

	// xml头部编写
	xml.beginnode("config");
	xml.beginnode("grid");
	var frozenColumns = $('#datagrid_UIDesign').datagrid("getColumnFields", true);
	var columns = $('#datagrid_UIDesign').datagrid("getColumnFields");
	var opt;
	var pand = true;
	for (var i=0; i<frozenColumns.length; i++) {
		xml.beginnode("col");
		opt = $('#datagrid_UIDesign').datagrid("getColumnOption", frozenColumns[i]);
		xml.attrib("name", "" + opt.field);
		xml.attrib("width", "" + opt.width);
		xml.attrib("is-hidden", "" + (opt.hidden ? 1: 0));
		xml.attrib("is-frozen", "1");
		xml.endnode();
		if (!opt.hidden && pand)
			pand = false;
	 }
	for (var i=0; i<columns.length; i++) {
		xml.beginnode("col");
		opt = $('#datagrid_UIDesign').datagrid("getColumnOption", columns[i]);
		xml.attrib("name", "" + opt.field);
		xml.attrib("width", "" + opt.width);
		xml.attrib("is-hidden", "" + (opt.hidden ? 1: 0));
		xml.attrib("is-frozen", "0");
		xml.endnode();
		if (!opt.hidden && pand)
			pand = false;
	}
	xml.endnode();
	xml.endnode();
	
	if (pand)
		return '';
	
	var header = '<?xml version="1.0" encoding="UTF-8"?>';
	var xmlContent = header + xml.tostring();
	return xmlContent;
}

function addUI() {
	
	if (!validEdit())
		return ;
	
	$('#uiForm').form('clear');
	$('#uiForm').find("input[name$='tablecode']").val(tablecode);
	loadColumnsTree(tablecode, 0);
	loadFormDesigner(0);
	loadDatagridHeader(tablecode, 0);
	
}

function deleteUI() {
	
	if (!validEdit())
		return ;
	
	var record = $('#schemalist').datagrid("getSelected");
	if (!record) {
		easyui_warn('请选择一条记录！');
		return;
	}
	parent.$.messager.confirm('确认', '确认要删除该界面方案?', function(r) {
		if (r) {
			$.post(contextpath + 'base/tabsdef/SysDicUIDesignController/deleteDicUIScheme.do', {'schemeid' : record.schemeid}, function(data) {
				if (data.success == true) {
					easyui_show(data.title);
					$('#schemalist').datagrid("reload");
					loadColumnsTree(tablecode, 0);
					loadFormDesigner(0);
					loadDatagridHeader(tablecode, 0);
					$('#uiForm').form('clear');
				} else {
					easyui_warn(data.title);
				}
			},'JSON');
		}
	});
}

function copyUI() {
	
	if (!validEdit())
		return ;
	
	var record = $('#schemalist').datagrid("getSelected");
	if (!record) {
		easyui_warn('请选择一条记录！');
		return;
	}
	
	parent.$.messager.confirm('确认', '确认要复制该界面方案?', function(r) {
		if (r) {
			$.post(contextpath + 'base/tabsdef/SysDicUIDesignController/copyUIToTable.do', {'schemeid' : record.schemeid}, function(result) {
	        	 if (result) {
	        		 parent.$.messager.show({
	        				title : '提示',
	        				msg : '复制成功！',
	        				timeout : 2000,
	        				showType : 'fade',
	        				style : {
	        					right : '',
	        					bottom : ''
	        				}
	        		});
	     			$('#schemalist').datagrid("reload");
	     			$('#schemalist').datagrid({'onLoadSuccess' : function() {
		     			var rows = $('#schemalist').datagrid('getRows');
		     			for (var i=rows.length-1; i>=0; i--) {
		     				if (rows[i].schemeid == result) {
		     					$('#schemalist').datagrid('selectRow', $('#schemalist').datagrid('getRowIndex', rows[i]));
		     					$('#uiForm').form('load', rows[i]);
		     					loadColumnsTree(tablecode, result);
		     					loadFormDesigner(result);
		     					loadDatagridHeader(rows[i].tablecode, result);
		     					break;
		     				}	
		     			}
	     			}});

	     			
	        	 } else
	        		 easyui_warn('复制失败！');
				
			});
		}
	});
}

function saveUI() {
	var listScheme = getListDesigner();
	var formScheme = window.formdesigner_.toXml();
	var defaultFsch = '<?xml version="1.0" encoding="UTF-8"?><config><layout col-count="2" col-width="140" label-cell-width="100" control-cell-width="150"><fields/></layout></config>';
	if (listScheme == '' && formScheme == defaultFsch) {
		easyui_warn('没有设计表单或列表方案！');
		return;
	}
	$('#uiForm').find("input[name$='listscheme']").val(listScheme);
	$('#uiForm').find("input[name$='formscheme']").val(formScheme);
	
	$('#uiForm').form('submit', {
		url : contextpath + 'base/tabsdef/SysDicUIDesignController/saveDicUIScheme.do',
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			var isValid = $('#uiForm').form('validate');
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
				easyui_info(data.title, function() {
					$('#schemalist').datagrid("reload");
			});
			} else {
				easyui_warn(data.title);
			}

		}
	});
}

function cancelUI() {
	var schemeid = $('#uiForm').find("input[name$='schemeid']").val();
	schemeid = schemeid ? schemeid : 0;
	var rowData = $('#schemalist').datagrid('getSelected');
	if (rowData)
		$('#uiForm').form('load', rowData);
	else
		$('#uiForm').form('clear');
	loadColumnsTree(tablecode, schemeid);
	loadFormDesigner(schemeid);
	loadDatagridHeader(tablecode, schemeid);
	
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}

//校验当前表单数据状态
function validEdit() {
	if (edit_flag) {
		easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
			if (c != undefined) { //取消
				if (c) { //是
					saveUI();
				} else { //否
					cancelUI();
				}
			} else {
				
			}
		});
		
		return false;
	} else
		return true;
}

function formchanged() {
	edit_flag = true;
	setSaveButtonStatus(true);
}

//设置保存按钮的状态
function setSaveButtonStatus(isOn) {
	if (isOn) {
		$('#btn_save').linkbutton('enable');
		$('#btn_cancel').linkbutton('enable');
	} else {
		$('#btn_save').linkbutton('disable');
		$('#btn_cancel').linkbutton('disable');
	}
}