/**
 * 数据字典
 */
var dicElementDataGrid;

var edit_flag = false; //
var checkSubmitflag = false;

var last_recordID;

$(function(){
	elementtypeFunc();
	comboboxFunc("s_elementclass", "SYS_ELEMENTCLASS", 'code');
	//加载基础数据项数据
	loadDicElementDataGrid();
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(dicElementDataGrid);
	
	//${modelMap.columnSet};
	//dicElementDataGrid.datagrid("columnMoving");
	$('#toolbar_querybar').panel('close');
	setSaveButtonStatus(edit_flag);
	
	//数据项分类
	comboboxFunc("elementclass_edit", "SYS_ELEMENTCLASS", 'code');
	comboboxFunc("status_edit", "SYS_STATUS", "code");
	comboboxFunc("elementclass_view", "SYS_ELEMENTCLASS", 'code');
	comboboxFunc("status_view", "SYS_STATUS", "code");
	$("#elementclass_edit, #status_edit, #elementclass_view, #sourceelement_view, #status_view").combobox({
		onChange: function(){
			enable_onchange();
			form_onchange();
		}
	});
	$("#elementcode_edit, #elementname_edit, #datatype_edit, #codetype_edit, #codeformat_edit, #remark_edit, #elementcode_view, #elementname_view, #remark_view").textbox({
		onChange: function(){
			enable_onchange();
			form_onchange();
		}
	});
	$("#columnlength_edit, #scalelength_edit").numberbox({
		onChange: function(){
			enable_onchange();
			form_onchange();
		}
	});
	$("#startdate_edit, #enddate_edit, #startdate_view, #enddate_view").datebox({
		onChange: function(){
			enable_onchange();
			form_onchange();
		}
	});
	
	$('#tablecode_edit').gridDialog({
		title :'数据表选择',
		dialogWidth : 640,
		dialogHeight : 400,
		singleSelect: true,
		dblClickRow : true,
		hiddenName:'tablecode',
		valueField:'tablecode',
		textField:'tablename',
		prompt:"请选择数据项值集表",
		filter: {
					cncode: "表名",
					code: "tablecode",
					cnname: "中文表名",
					name : "tablename"
				},
		url : 'base/tabsdef/SysDicTableController/searchTables.do',
		cols : [[
			{field : "ck", checkbox : true}, 
			{field : "tablecode",title : "表名",width : 180},
	    	{field : "tablename", title : "中文表名", width : 180},
	    	{field : "cntabletype", title : "表类型", width : 200}
	    ]],
	    assignFunc: function(){
	    	enable_onchange();
	    	form_onchange();
	    },
	    clearFunc: function(){
	    	enable_onchange();
	    	form_onchange();
	    }
	});
	
	$('#dicELement_form').form({
		onLoadSuccess:function(){
			setSaveButtonStatus(edit_flag);
			enable_onchange();
		}
	});
	
	$('#dicViewElement_form').form({
		onLoadSuccess:function(){
			setSaveButtonStatus(edit_flag);
			enable_onchange();
		}
	});
	
	$("#sourceelement_view").combobox({
		url : urls.getSourceDicElements,
		valueField : "elementcode",
		textField : "elementname",
		panelHeight : 'auto',
		onSelect : function(record) {
			loadDataGrid($('#elementcode_view').textbox("getValue"));
		}
	});
	
	$("#s_elementclass").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#s_elementcode").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#s_elementname").textbox("addClearBtn", {iconCls:"icon-clear"});
	
	//$('#cc').layout('collapse','east');
});

var panel_ctl_handles = [{
	panelname : '#toolbar_querybar',	//要折叠的面板id
	gridname : "#datagrid_dicElementDef",
	buttonid : '#openclose' //折叠按钮id
}];

function disable_onchange(){
	onchange_enable=false;
}

function enable_onchange(){
	onchange_enable=true;
}
/**
 * 所有 form元素的onchange 事件
 */
function form_onchange(){
	if(onchange_enable){
		edit_flag=true;
		
		//置亮 save 和 cancel按钮
		setSaveButtonStatus(edit_flag);
	}
}

var elementtypeft = function(value) {
    if(value==0)
        return '基础数据项';
else
        return '数据项视图';
};
var codetypeft = function(value) {
	switch (value) {
	case 0:
		return '0-无编码格式';
	case 1:
		return '1-顺序码';
	case 2:
		return '2-层码';
	default:
		return '';
	}
};
var iselementsft = function(value){
    if(value==0)
            return '否';
    else
            return '是';
};
var datatypeft = function(value){
	switch(value) {
	case 'C':
		return '代码集';
	case 'S':
		return '文本';
	case 'N':
		return '数字';
	case 'Y':
		return '金额';
	case 'D':
		return '日期';
	default:
		return '';
	}
};
var statusft =  function(value){
	switch(value) {
	case 0:
		return '正常';
	case 9:
		return '停用';
}
};

//数据项类型
function elementtypeFunc() {
	$("#elementtype").combobox({
		valueField: 'id',
		textField: 'text',
		panelHeight : 'auto',
		editable:false,
		data: [{
			id: '0',
			text: '基础数据项'
		},{	
			id: '1',
			text: '数据项视图'
		}],
		value : '0',	
		onSelect : function(){
			 var elementtype=$("#elementtype").combobox("getValue");
			 if (elementtype == 0) {
				 $("#dicdef_win").panel("open");
				 $("#dicview_win").panel("close");
			 } else {
				 $("#dicdef_win").panel("close");
				 $("#dicview_win").panel("open");
			 }
		 }
	 });
}

//加载数据项
function loadDicElementDataGrid(){
	
	dicElementDataGrid = $("#datagrid_dicElementDef").datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		rownumbers : true,//默认为false，是否显示序号列
		view : scrollview,
		pagination : true, //默认为false，是否显示在底部分页栏
		pageSize : 50,//默认分页数为10条
		url : urls.queryDicElement,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_dicElementDef",
		remoteSort : true,
		multiSort : true,
		idField : 'elementid',
		onBeforeCheck : function(rowIndex, rowData) {
			if (edit_flag) {
				easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
					if (c != undefined) { //取消
						if (c) { //是
							saveElement();
						} else { //否
							rejectEdit();
						}
					} else {
						
					}
				});
				return false;
			} else {
				return true;
			}
		},
		onBeforeSelect : function(rowIndex, rowData) {
			if (edit_flag) {
				easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
					if (c != undefined) { //取消
						if (c) { //是
							saveElement();
						} else { //否
							rejectEdit();
						}
					} else {
						
					}
				});
				return false;
			} else {
				return true;
			}
		},
        headerContextMenu: [{
        		text: "冻结该列", iconCls : "icon-standard-clock", disabled: function (e, field) { return dicElementDataGrid.datagrid("getColumnFields", true).contains(field); },
        		handler: function (e, field) { dicElementDataGrid.datagrid("freezeColumn", field); }
        	}, {
        		text: "取消冻结该列", iconCls : "icon-standard-lock-open", disabled: function (e, field) { return dicElementDataGrid.datagrid("getColumnFields", false).contains(field); },
        		handler: function (e, field) { dicElementDataGrid.datagrid("unfreezeColumn", field); }
        	}
        ],
        onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
            e.preventDefault(); //阻止浏览器捕获右键事件
            $(this).datagrid("clearSelections"); //取消所有选中项
            $(this).datagrid("selectRow", rowIndex); //根据索引选中该行
        },
        onLoadSuccess : function(data) {
        	if (last_recordID) {
        		var last_rowIndex = dicElementDataGrid.datagrid('getRowIndex', last_recordID);
        		dicElementDataGrid.datagrid('checkRow', last_rowIndex);
        	} else
        		dicElementDataGrid.datagrid('checkRow', 0);
        		
        },
        onSelect : function(rowIndex, rowData) {
        	//last_recordID = rowData.elementid;
    		$('#elementtype').combobox('setValue', rowData.elementtype);
    		$('#elementtype').combobox('readonly', true);
    		
    		comboboxFunc("elementclass_edit", "SYS_ELEMENTCLASS", 'code');
    		comboboxFunc("status_edit", "SYS_STATUS", 'code');
    		comboboxFunc("elementclass_view", "SYS_ELEMENTCLASS", 'code');
    		
    		$("#elementclass_edit").combobox({
    			onChange: function(){
    				enable_onchange();
    				form_onchange();
    			}
    		});
    		if (rowData.elementtype == 0) {
    			$('#dicELement_form').form("clear").form("load", rowData);
    			$('#remark_edit').textbox('setValue', rowData.remark);
    			$('#tablecode_edit').searchbox('setValue', rowData.tablename);
    			$("#dicdef_win").panel("open");
    			$('#elementcode_edit').attr('readonly', true);
    			$("#dicview_win").panel("close");
    		} else {
    			
    			$("#dicdef_win").panel("close");
    			$('#dicViewElement_form').form("load", rowData);
    			$('#remark_edit').textbox('setValue', rowData.remark);
    			$('#elementviewid').val(rowData.elementid);
    			$("#dicview_win").panel("open");
    			$('#elementcode_view').attr('readonly', true);
    			loadDataGrid(rowData.elementcode);
    		 }
    		
    		edit_flag = false;
    		setSaveButtonStatus(edit_flag);
    		
    		if ($('#east_panel').panel('options').collapsed)
    			$('#cc').layout('expand','east');
    	},
		frozenColumns : [[{
            	field : "elementid",
            	checkbox : true
		}, {
        	field : "elementname",
        	title : "名称",
        	width : 120,
        	sortable : true
        }, {
        	field : "elementcode",
        	title : "编码",
        	width : 120,
        	sortable : true
        }]], 
		columns : [[
		   {
        	field : "tablecode",
        	title : "数据项值集表",
        	width : 120,
        	sortable : true
        }, {
        	   field : 'cnelementclass',
        	   title : '数据项分类',
        	   width : 80,
           	sortable : true
           }, {
            	field : "elementtype",
            	title : '数据项类型',
            	width : 80,
            	formatter : elementtypeft,
            	sortable : true
            }, {
            	field : "codetype",
            	title : "编码类型",
            	width : 100,
            	formatter : codetypeft,
            	sortable : true
            }, {
            	field : "codeformat",
            	title : "编码格式",
            	width : 60
            }, {
            	field : "iselements",
            	title : "是否要素",
            	width : 60,
            	formatter : iselementsft
            }, {
            	field : "datatype",
            	title : "数据类型",
            	width : 60,
            	formatter : datatypeft
            }, {
            	field : "status",
            	title : "状态",
            	width : 40,
            	formatter : statusft
            }, {
            	field : "startdate",
            	title : "启用日期",
            	width : 80
            }, {
            	field : "remark",
            	hidden : true
            }
		]]
	});
	dicElementDataGrid.datagrid("columnMoving");
	
	
	
	//datagrid 加单击事件
	//dicElementDataGrid.datagrid({);
	
	
}

//撤销修改
function rejectEdit() {
	
	var rows = $("#datagrid_dicElementDef").datagrid("getSelections");
	if(rows.length != 1){
		$("#dicELement_form").form("clear");
		$("#dicViewElement_form").form("clear");
	}else{
		var rowData = rows[0];
		$('#elementtype').combobox('setValue', rowData.elementtype);
		$('#elementtype').combobox('readonly', true);
		
		comboboxFunc("elementclass_edit", "SYS_ELEMENTCLASS", 'code');
		comboboxFunc("status_edit", "SYS_STATUS");
		comboboxFunc("elementclass_view", "SYS_ELEMENTCLASS", 'code');
		
		$("#elementclass_edit, #status_edit, #elementclass_view").combobox({
			onChange: function(){
				enable_onchange();
				form_onchange();
			}
		});
		
		if (rowData.elementtype == 0) {
			$('#dicELement_form').form("clear").form("load", rowData);
			$('#tablecode_edit').searchbox('setValue', rowData.tablename);
			$("#dicdef_win").panel("open");
			$('#elementcode_edit').attr('readonly', true);
			$("#dicview_win").panel("close");
		} else {
			
			$("#dicdef_win").panel("close");
			$('#dicViewElement_form').form("clear").form("load", rowData);
			$('#elementviewid').val(rowData.elementid);
			$("#dicview_win").panel("open");
			$('#elementcode_view').attr('readonly', true);
			loadDataGrid(rowData.elementcode);
		 }
		if ($('#east_panel').panel('options').collapsed)
			$('#cc').layout('expand','east');
	}
	
	edit_flag=false;
	setSaveButtonStatus(edit_flag);
}

var opers = [{oper:'=', opername : '等于'}, {oper:'<>', opername : '不等于'}, {oper:'>', opername : '大于'}, 
                 {oper:'>=', opername : '大于等于'}, {oper:'<', opername : '小于'}, {oper:'<=', opername : '小于等于'}
                 , {oper:'betten', opername : '在...之间'}, {oper:'within', opername : '在...之内'}, {oper:'likebegin', opername : '匹配开始'}
                 , {oper:'likeend', opername : '匹配结尾'}, {oper:'likeall', opername : '匹配所有'}];

var columncode = undefined;
var columnname = undefined;
var columntype = undefined;
var sourceelementcode = undefined;

function loadDataGrid(elementcode) {
	$('#dicViewConditions').datagrid({
		singleSelect : true,
		toolbar: '#dicViewCondButtons',
//		method: 'post', 
		queryParams :{elementcode:elementcode}, 
		//onClickRow: onClickRow,
		onClickCell : onClickCell,
		url : contextpath +  'base/dic/dicElementController/queryDicElementViewFilters.do',
		frozenColumns : [[{
	        	field : "filterid",
	        	checkbox : true
		}]], 
		columns : [[
	         {
	        	field : "seqno",
	        	title : "顺序",
	        	width : 30,
	        	sortable : true,
	        	editor : {
	        		type : 'numberbox',
	        		options : {
	        			required : true, 
	        			onChange : function() {
	        				//endEditing();
	        				enable_onchange();
	        		    	form_onchange();
	        			}
	        		}
	        	}
	        }, {
	        	field : "leftbracket",
	        	title : "(",
	        	width : 30,
	        	editor : {
	        		type : 'textbox',
	        		options : {
		        		onChange : function() {
		        			//endEditing();
		        			enable_onchange();
		        	    	form_onchange();
	        			}
	        		}
	        	}
	        }, {
	        	field : 'notsymbol',
	        	title : '包含或排除',
	        	width : 70,
	        	editor : {
	    		   type : 'combobox',
	               options : {
	                   valueField : 'notsymbol',
	                   textField : 'notsymbolname',
	                   required : true,
	                   panelHeight : 'auto',
	                   data: [{
	                	   notsymbol : '0',
	                	   notsymbolname : '包含'
		        		},{	
		        			notsymbol : '1',
		        			notsymbolname : '排除'
		        		}],
		        		onChange : function() {
		        			//endEditing();
		        			enable_onchange();
		        	    	form_onchange();
	        			}
	               }
	        	},
	        	formatter : function(value) {
	        		if (value == 0)
	        			return '包含';
	        		else if (value == 1)
	        			return '排除';
	        		else
	        			return '';
	        	}
	        }, {
	        	field : "columnname",
	        	title : "字段名称",
	        	width : 80,
	        	editor : {
	        		type : 'combobox',
	        		options : {
	        			url: contextpath + 'base/dic/dicElementController/getColumnsByElementcode.do?elementcode=' + $('#sourceelement_view').combobox("getValue"),
	                    method:'POST',
	                    valueField:'columncode',
	                    textField:'columnname',
	                    panelHeight:'auto',
	            		onSelect : function(record) {
	            			columncode = record.columncode;
	            			columnname = record.columnname;
	            			columntype = record.columntype;
	            			sourceelementcode = record.sourceelementcode;
	            		},
		        		onChange : function() {
		        			// endEditing();
		        			enable_onchange();
		        	    	form_onchange();
	        			}
	        		}
	        	}
	        }, {
	        	field : "operator",
	        	title : '操作符',
	        	width : 60,
	        	editor : {
	    		   type : 'combobox',
	               options : {
	                   valueField : 'oper',
	                   textField : 'opername',
	                   required : true,
	                   panelHeight : 'auto',
	                   data: opers,
	                   onChange : function() {
	                	  // endEditing();
	                	   enable_onchange();
	           	    	   form_onchange();
	        			}
	               }
	        	},
	        	formatter : function(value) {
	        		if (!value)
	        			return '';
	        		for (var i=0; i<opers.length; i++) {
	        			if (opers[i]['oper'] == value)
	        				return opers[i]['opername'];
	        		}
	        	}
	        }, {
	        	field : "minvaluename",
	        	title : "第一个值",
	        	width : 70
	        },{
	        	field : "maxvaluename",
	        	title : "第二个值",
	        	width : 70
	        }, {
	        	field : "rightbracket",
	        	title : ")",
	        	width : 30,
	        	editor : {
	        		type : 'text',
	        		options : {
	        			onChange : function() {
	        				//endEditing();
	        				enable_onchange();
	        		    	form_onchange();
	        			}
	        		}
	        	}
	        }, {
	        	field : "logicalsymbol",
	        	title : "逻辑符",
	        	width : 50,
	        	editor : {
	    		   type : 'combobox',
	               options : {
	                   valueField : 'notsymbol',
	                   textField : 'notsymbolname',
	                   panelHeight : 'auto',
	                   data: [{
	                	   notsymbol : 'and',
	                	   notsymbolname : '并且'
		        		},{	
		        			notsymbol : 'or',
		        			notsymbolname : '或者'
		        		}],
		        		onChange : function() {
		        			//endEditing();
		        			enable_onchange();
		        	    	form_onchange();
	        			}
	               }
	        	},
	        	formatter : function(value) {
	        		if (value =='and')
	        			return '并且';
	        		else if (value == 'or')
	        			return '或者';
	        		else
	        			return '';
	        	}
	        }
		]]
	});
}

var editIndex = undefined;

function onClickCell(index, field){
	var rowData = $('#dicViewConditions').datagrid('getData').rows[index];
	if (field == 'minvaluename' || field == 'maxvaluename') {
		$("#dicViewConditions").datagrid('removeEditor', field);
		var currentcolumntype = rowData.columntype;
		if (!currentcolumntype)
			currentcolumntype = columntype;
		
		var currentsecode = rowData.sourceelementcode;
		if (!currentsecode)
			currentsecode= sourceelementcode;
		
		if (currentcolumntype == 'S' && currentsecode != null && currentsecode != '') {
			if (field == 'minvaluename') {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'minvaluename',
	                editor : {
	                    type : 'combobox',
	                    options : {
	                        required : true,
	                    	url : contextpath + "base/dic/dicElementValSetController/queryByElementcode.do?elementcode=" + currentsecode,
	    					valueField : 'id',
	    					textField : 'text',
	    					panelHeight : 'auto',
	    					onChange : function() {
	    						//endEditing();
	    						enable_onchange();
	    				    	form_onchange();
	    					}
	                    }
	                }
	            });
			} else {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'maxvaluename',
	                editor : {
	                    type : 'combobox',
	                    options : {
	                    	url : contextpath + "base/dic/dicElementValSetController/queryByElementcode.do?elementcode=" + currentsecode,
	    					valueField : 'id',
	    					textField : 'text',
	    					panelHeight : 'auto',
	    					onChange : function() {
	    						//endEditing();
	    						enable_onchange();
	    				    	form_onchange();
	    					}
	                    }
	                }
	            });
			}
		} else if (currentcolumntype == 'D') {
			if (field == 'minvaluename') {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'minvaluename',
	                editor : {
	                    type : 'datebox',
	                    options : {
	                    	required : true,
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			} else {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'maxvaluename',
	                editor : {
	                    type : 'datebox',
	                    options : {
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			}
		} else if (currentcolumntype == 'T') {
			if (field == 'minvaluename') {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'minvaluename',
	                editor : {
	                    type : 'datetimebox',
	                    options : {
	                    	required : true,
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			} else {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'maxvaluename',
	                editor : {
	                    type : 'datetimebox',
	                    options : {
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			}
		} else if (currentcolumntype == 'N' || currentcolumntype == 'Y') {
			if (field == 'minvaluename') {
	  			$("#dicViewConditions").datagrid('addEditor', {
	                field : 'minvaluename',
	                editor : {
	                    type : 'numberbox',
	                    options : {
	                    	required : true,
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			} else {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'maxvaluename',
	                editor : {
	                    type : 'numberbox',
	                    options : {
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			}
		} else {
			if (field == 'minvaluename') {
	  			$("#dicViewConditions").datagrid('addEditor', {
	                field : 'minvaluename',
	                editor : {
	                    type : 'textbox',
	                    options : {
	                    	required : true,
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                }
	            });
			} else {
				$("#dicViewConditions").datagrid('addEditor', {
	                field : 'maxvaluename',
	                editor : {
	                    type : 'textbox',
	                    options : {
	                    	onChange : function() {
	                    		//endEditing();
	                    		enable_onchange();
	                	    	form_onchange();
	    					}
	                    }
	                
	                }
	            });
			}
		}
	
	}
    if (endEditing()){
        $('#dicViewConditions').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
        editIndex = index;
    }
}

//停止编辑
function endEditing(){
    if (editIndex == undefined){return true;}
    if ($('#dicViewConditions').datagrid('validateRow', editIndex)){
    	
    	var minvalueed = $('#dicViewConditions').datagrid('getEditor', {index:editIndex,field:'minvaluename'});
    	var maxvalueed = $('#dicViewConditions').datagrid('getEditor', {index:editIndex,field:'maxvaluename'});

    	var minvalue = null;
    	var minvaluename = null;
    	var maxvalue = null;
    	var maxvaluename = null;
    	var rowData = $('#dicViewConditions').datagrid('getData').rows[editIndex];
    	if (minvalueed) {
    		if (rowData.columntype == 'S') {
    			if (rowData.sourceelementcode != null && rowData.sourceelementcode != '') {
    				minvalue = $(minvalueed.target).combobox('getValue');
        			minvaluename = $(minvalueed.target).combobox('getText');
    			} else {
    				minvalue = $(minvalueed.target).val();
    			}
    		} else if (rowData.columntype == 'N') {
    			minvalue = $(minvalueed.target).numberbox('getValue');
    		} else if (rowData.columntype == 'Y') {
    			minvalue = $(minvalueed.target).numberbox('getValue');
    		} else if (rowData.columntype == 'D') {
    			minvalue = $(minvalueed.target).datebox('getValue');
    		} else if (rowData.columntype == 'T') {
    			minvalue = $(minvalueed.target).datetimebox('getValue');
    		}
    	}
    	
    	if (maxvalueed) {
    		if (rowData.columntype == 'C') {
    			maxvalue = $(maxvalueed.target).combobox('getValue');
    			maxvaluename = $(maxvalueed.target).combobox('getText');
    		} else if (rowData.columntype == 'S') {
    			maxvalue = $(maxvalueed.target).val();
    		} else if (rowData.columntype == 'N') {
    			maxvalue = $(maxvalueed.target).numberbox('getValue');
    		} else if (rowData.columntype == 'Y') {
    			maxvalue = $(maxvalueed.target).numberbox('getValue');
    		} else if (rowData.columntype == 'D') {
    			maxvalue = $(maxvalueed.target).datebox('getValue');
    		} else if (rowData.columntype == 'T') {
    			maxvalue = $(maxvalueed.target).datetimebox('getValue');
    		}
    	}
    	
    	$('#dicViewConditions').datagrid('endEdit', editIndex);
    	if (columncode) {
	    	rowData.columncode = columncode;
	    	rowData.columnname = columnname;
	    	rowData.columntype = columntype;
	    	rowData.sourceelementcode = sourceelementcode;
    	}
    	
    	if (minvalueed) {
    		if (minvalue)
    			rowData.minvalue = minvalue;
    		
    		if (minvaluename)
    			rowData.minvaluename = minvaluename;
    	}
    	
    	if (maxvalueed) {
    		if (maxvalue)
    			rowData.maxvalue = maxvalue;
    		
    		if (maxvaluename)
    			rowData.maxvaluename = maxvaluename;
    	}
    	if (rowData != undefined)
    		$('#dicViewConditions').datagrid('updateRow', {index : editIndex, row :  rowData});
    	
    	columncode = undefined; columnname = undefined; columntype = undefined; sourceelementcode = undefined;
    	minvalue = null; minvaluename = null; maxvalue = null; maxvaluename = null;
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
//条件项保存
function accept(){
	
    if (endEditing()){
        $('#dicViewConditions').datagrid('acceptChanges');
    }
}
// 条件项 取消编辑
function reject(){
	$('#dicViewConditions').datagrid('rejectChanges');
	editIndex = undefined;
}

/**
 * 新增过滤条件项
 */
function append(){
	var validmsg = appendValid();
	if (validmsg) {
		$.messager.alert('提示', validmsg, 'warnning');
		return;
	}
	
    if (endEditing()){
    	 $('#dicViewConditions').datagrid('acceptChanges');
        $('#dicViewConditions').datagrid('appendRow',{});
        editIndex = $('#dicViewConditions').datagrid('getRows').length-1;
    }
}

function deleteViewFilter() {
	var record = $('#dicViewConditions').datagrid('getSelections');
	if (record.length == 0) {
		easyui_warn('请选择要删除的数据！');
		return;
	}
	
	record =  $('#dicViewConditions').datagrid('getSelected');
	$('#dicViewConditions').datagrid('deleteRow', $('#dicViewConditions').datagrid('getRowIndex', record));
	
	edit_flag = true;
	setSaveButtonStatus(edit_flag);
}

function appendValid(oper) {
	var elementclass = $('#elementclass_view').combobox("getValue");
	var sourceelement = $('#sourceelement_view').combobox("getValue");
	var elementcode = $('#elementcode_view').textbox("getValue");
	if (!elementclass)
		return '请先选择数据项分类';
	
	if (!sourceelement)
		return '请先选择源数据项';
	
	if (!elementcode)
		return '请先输入编码';
	
	return null;
}

/**
 * 查询
 */
function doSearch() {
	var elementclass = $("#s_elementclass").combobox("getValue");
	var elementcode = $("#s_elementcode").textbox("getValue");
	var elementname = $("#s_elementname").textbox("getValue");
	dicElementDataGrid.datagrid("load", {
		elementclass : elementclass,
		elementcode : elementcode,
		elementname : elementname
	});
}
/**
 * 清空
 */
function doClear() {
	$("#s_elementclass").combobox("clear");
	$("#s_elementcode").val("");
	$("#s_elementname").val("");
	dicElementDataGrid.datagrid("load", {});
}
//新增数据项
function addDic() {
	if (!validFormEdit())
		return ;
	
	var records = dicElementDataGrid.datagrid("getSelections"); //判断是否有已选项 如果有 则 清除已选项
	if (records.length > 0)
		dicElementDataGrid.datagrid("clearSelections");
	
	comboboxFunc("elementclass_edit", "SYS_ELEMENTCLASS", 'code');
	comboboxFunc("status_edit", "SYS_STATUS");
	
	$("#elementclass_edit, #status_edit").combobox({
		onChange: function(){
			enable_onchange();
			form_onchange();
		}
	});
	
	$("#sourceelement_view").combobox({
		url : urls.getSourceDicElements,
		valueField : "elementcode",
		textField : "elementname",
		panelHeight : 'auto',
		onSelect : function(record) {
			loadDataGrid($('#elementcode_view').textbox("getValue"));
		},
		onChange: function(){
			enable_onchange();
			form_onchange();
		}
	});
	
	$('#dicELement_form').form("reset");
	$('#dicViewElement_form').form("reset");
	
	$('#elementtype').combobox('setValue', 0);
	$('#elementtype').combobox('readonly', false);
	$('#elementcode_view').attr('readonly', false);
	$('#elementcode_edit').attr('readonly', false);
	
	$("#dicdef_win").panel("open");
	$("#dicview_win").panel("close");
	loadDataGrid();
	if ($('#east_panel').panel('options').collapsed)
		$('#cc').layout('expand','east');
	
	edit_flag = false;
	setSaveButtonStatus(edit_flag);
}

//修改
function editDicElement() {
	var rowData = dicElementDataGrid.datagrid("getSelections");
	if (rowData.length == 0) {
		parent.$.messager.alert('提示', '请选择一条记录！', 'warnning');
		return;
	} else if (rowData.length > 1) {
		parent.$.messager.alert('提示', '当且仅能选择一条记录！', 'warnning');
		return;
	}
	rowData = dicElementDataGrid.datagrid("getSelected");
	if (rowData.elementtype == 0) {
		showDialog("基础数据项修改", urls.formDicElement, dicElementDataGrid, 'dicELement_form', true, 620, 430, types.edit, 0);
		$("#elementcode_edit").attr("readOnly", true);
	} else {
		
		showDialog("数据项视图修改", urls.formDicElementView + '?elementcode=' + rowData.elementcode, dicElementDataGrid, 'dicViewElement_form', true, 620, 530, types.edit, 1);
	}
}
// 保存
function saveElement() {
	var elementtype = $('#elementtype').combobox('getValue');
	if (elementtype == 0)
		saveDicElement(urls.saveDicElement, $('#dicELement_form'));
	else
		saveDicElementView(urls.saveDicElementView, $('#dicViewElement_form'));
}

//保存 基础数据项
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
				easyui_info(data.title, function() {
				//	dicElementDataGrid.datagrid({'positionScrollTop' : true});
					dicElementDataGrid.datagrid('reload');
					edit_flag=false;
					setSaveButtonStatus(edit_flag);
			});
			} else {
				easyui_warn(data.title);
			}

		}
	});
}

//保存 数据项视图
function saveDicElementView(url, form) {
	if (editIndex) {
		easyui_warn( '当前条件项正在编辑状态！');
		return;
	}
	$('#elementviewfilters').val(JSON.stringify($('#dicViewConditions').datagrid('getData').rows));
	form.form('submit', {
		url : url,
		onSubmit : function() {
			
			return form.form('validate');
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
				easyui_info(data.title, function() {
					dicElementDataGrid.datagrid({'positionScrollTop' : true});
					dicElementDataGrid.datagrid('reload');
					edit_flag=false;
					setSaveButtonStatus(edit_flag);
				});
				
			} else {
				easyui_warn(data.title);

			}
		}
	});
}

/**
 * 数据项值集维护
 */
 
function elementValSet() {
	if (!validFormEdit())
		return ;
	var rowData = dicElementDataGrid.datagrid("getSelections");
	if (rowData.length == 0) {
		parent.$.messager.alert('提示', '请选择一条记录！', 'warnning');
		return;
	} else if (rowData.length > 1) {
		parent.$.messager.alert('提示', '当且仅能选择一条记录！', 'warnning');
		return;
	}
	rowData = dicElementDataGrid.datagrid("getSelected");
	
	if (rowData.elementtype == 1) {
		parent.$.messager.alert('提示', '数据项视图不能进行值集维护，请维护源数据项【' + rowData.sourceelement + '】值集！', 'warnning');
		return;
	}
	if (rowData.isedit == '0') {
		parent.$.messager.alert('提示', '数据项【' + rowData.elementcode + '】值集不由数据字典维护！', 'warnning');
		return;
	}
	
	$.post(urls.isExistUI, {tablecode : rowData.tablecode}, function(data){
		if(data.success) {
			var width = 900;
			if (rowData.codetype == 2) {
				width = data.body.width + 350;
			} else {
				width = data.body.width + 440;
			}
			
			showDialog('【' + rowData.elementname + '】' + '数据项值集维护', urls.elementValSet 
					+ "?elementcode=" +rowData.elementcode + "&tablecode=" + rowData.tablecode
					, rowData, null, false, width, 600, types.dicValEdit);
		} else 
			easyui_warn(data.title);

	}, "json");
	

}

//删除数据项
function deleteDicElement() {
	if (!validFormEdit())
		return ;
	var rows = dicElementDataGrid.datagrid("getSelections");
	if(rows.length == 0){
		easyui_warn("请选择一条数据！");
		return;
	}
	$.messager.confirm("确认删除", "确认要删除选中的记录吗？", function(r){
		var elementids = "";
		for (var i=0; i<rows.length; i++) {
			if (i != 0)
				elementids += ",";
			elementids += rows[i].elementid;
		}
		if (r){
			$.post(urls.deleteDicElement, {elementids:elementids}, function(data){
				easyui_warn(data.title, function(){
					dicElementDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

//设置保存按钮的状态
function setSaveButtonStatus(isOn) {
	if (isOn) {
		$('#linkbutton_save').linkbutton('enable');
		$('#linkbutton_cancel').linkbutton('enable');
	} else {
		$('#linkbutton_save').linkbutton("disable");
		$('#linkbutton_cancel').linkbutton("disable");
	}
}

//校验当前表单数据是否发生改变
function validFormEdit() {
	if (edit_flag) {
		easyui_solicit('当前有数据未保存，是否保存数据？', function(c) {
			if (c != undefined) { //取消
				if (c) { //是
					saveTable();
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

//保存列设置
function saveColSet() {
	saveColumnsSet(dicElementDataGrid, "dicElement", menuid);
}
 //还原列设置
function resetColSet() {
	resetColumnSet("dicElement", menuid);
}
//附件管理
function filemanage() {
	
	var rows = dicElementDataGrid.datagrid("getSelections");
	if(rows.length == 0){
		easyui_warn("请选择一条数据！");
		return;
	} else if (rows.length > 1) {
		easyui_warn("当且仅能选择一条数据！");
		return;
	}
	var keyid = rows[0].elementid;
	parent.$.modalDialog({
		title : '附件管理',
		width : 600,
		height : 400,
		href : contextpath + '/base/filemanage/fileManageController/entry.do?keyid=' + keyid + '&elementcode=DICELEMENT',
		onBeforOpen: function(){
			console.log("open");
		},
		onLoad: function(){

		}
	});
}