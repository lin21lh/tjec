var colUrls = {
	queryDbTableColumns : contextpath
			+ 'base/tabsdef/SysDicColumnController/queryDbTableColumns.do',
	query : contextpath + 'base/tabsdef/SysDicColumnController/query.do',
	getColumn : contextpath + "base/tabsdef/SysDicColumnController/get.do",
	editColumn : contextpath + "base/tabsdef/SysDicColumnController/edit.do",
	removeColumn : contextpath
			+ "base/tabsdef/SysDicColumnController/delete.do",
	elementcodeSelect : contextpath
			+ "base/dic/dicElementController/queryPageDicElement.do ",
	searchDicELes : contextpath + "base/dic/dicElementController/searchDicELes.do ",
	getCodeColumnDetail : contextpath
			+ "base/tabsdef/SysDicColumnController/getCodeColumnDetail.do "
};
var sel_tablename = null;
var sel_tablecode = null;
var tryToFocus = null; //刷新后尝试聚集的列字段 

var colCreating = false; //是否正在编辑
function tabColConfig() {
	
	if (!validEdit())
		return ;
	
	var sel = $('#datagrid_tabDef').datagrid('getSelected');
	if (!sel) {
		easyui_warn('请选择一个数据表！');
		return;
	}
	sel_tablecode = sel.tablecode;
	sel_tablename = sel.tablename;
	parent.$.modalDialog({
		title : '数据表字段设置( ' + sel_tablecode + ' - ' + sel_tablename + ' )',
		width : 980,
		height : 500,
		href : urls.colsDefDialog,
		onLoad : function() {
			var options = {
				id : {
					leftGridId : 'lgrid',
					rightGridId : 'rgrid',
					addButtonId : 'addBtn',
					delButtonId : 'delBtn',
					container : 'container_1'
				},
				dimension : {
					leftGridWidth : 315,
					rightGridWidth : 315
				},
				functions : {
					init : function() {
						doInit(parent.$.modalDialog.handler);
					}
				},
				jQueryHandler : parent.$.modalDialog.handler
			};

			var jq = parent.$.modalDialog.handler;
			makeDoubleDatagridSelect(options);
			// 初始化表单
			comboboxFunc('columntype', 'SYS_COLUMN_TYPE', 'code', null, jq);

//			jq.find("#cnsourceelementcode").searchbox("addClearBtn", {iconCls: "icon-clear"});
			
			jq.find("#cnsourceelementcode").gridDialog({
				title :'数据项选择',
				dialogWidth : 600,
				dialogHeight : 400,
				hiddenName:'sourceelementcode',
				dblClickRow: true,
				prompt: "请选择来源数据项",
				loadMsg: "正在加载数据项数据，请稍候",
				url : colUrls.searchDicELes,
				cols:[[
				    {
						field : "ck",
						checkbox : true
					}, {
						field : "elementname",
						title : "数据项名称",
						width : 120
					}, {
						field : "elementcode",
						title : "数据项编码",
						width : 200
					}, {
						field : "tablecode",
						title : "数据项值集表",
						width : 200
					}
				]],
				filter: {
					cncode: "数据项编码",
					code: "elementcode",
					cnname: "数据项名称",
					name: "elementname"
				},
				textField: "elementname",
				valueField: "elementcode",
				assignFunc: function(row){
					jq.find("#sourcetable").textbox("setValue", row.tablecode);
				},
				clearFunc: function(){
					jq.find("#sourcetable").textbox("setValue", "");
				}
			})
			
//			jq.find("#cnsourceelementcode").searchbox({
//				searcher : elementcodeSelect
//			});

			jq.find('#col_save_btn').linkbutton({
				onClick : col_save_btn_func
			});

			jq.find('#col_cancel_btn').linkbutton({
				onClick : col_cancel_btn_func
			});

//			var sckboxspan = jq.find('#cnsourceelementcode + span');
//			sckboxspan.find('input').attr("READONLY", "true");

		}
	});

}

function doInit(jq) {
	jq.find("#lgrid").datagrid({
		url : colUrls.queryDbTableColumns + '?tablecode=' + sel_tablecode,
		title : '数据库表字段',
		sortName : 'colname',
		remoteSort : false,
		sortOrder : 'asc',
		idField : 'colname',
		columns : [ [ {
			field : 'ck',
			checkbox : true,
			styler : function(value, row, index) {
				if (row.used == 1) {
					return 'background-color:#eee;color:gray';
				}
			}
		}, {
			field : 'colname',
			title : '列名',
			width : 130,
			styler : function(value, row, index) {
				if (row.used == 1) {
					return 'background-color:#eee;color:gray';
				}
			}
		}, {
			field : 'datatype',
			title : '数据类型',
			width : 110,
			styler : function(value, row, index) {
				if (row.used == 1) {
					return 'background-color:#eee;color:gray';
				}
			}
		}, {
			field : 'remarks',
			title : '列注释',
			width : 110,
			styler : function(value, row, index) {
				if (row.used == 1) {
					return 'background-color:#eee;color:gray';
				}
			}
		} ]],
		fit : true,
		border : false,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		// view : scrollview,
		pageSize : 100,
		onDblClickRow : function(rowIndex, rowData) {
			add_btn_click();
		}
	});

	jq.find("#rgrid").datagrid({
		url : colUrls.query + '?tablecode=' + sel_tablecode,
		title : '数据表字段',
		sortName : 'columncode',
		remoteSort : false,
		sortOrder : 'asc',
		idField : 'columnid',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'columncode',
			title : '字段名',
			width : 100,
			styler : function(value, row, index) {
				if (row.disagree == 1) {
					return 'font-weight:bold;color:red';
				}
			},
			formatter : function(value, row, index) {
				if (row.disagree == 1) {
					return "<a title='" + row.remark + "'>" + value + "</a>";
				} else {
					return value;
				}
			}

		}, {
			field : 'columnname',
			title : '中文名称',
			width : 140,
			styler : function(value, row, index) {
				if (row.disagree == 1) {
					return 'font-weight:bold;color:red';
				}
			},
			formatter : function(value, row, index) {
				if (row.disagree == 1) {
					return "<a title='" + row.remark + "'>" + value + "</a>";
				} else {
					return value;
				}
			}
		} ] ],
		fit : true,
		border : false,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		// view : scrollview,
		pageSize : 100,
		onClickRow : function(rowIndex, rowData) {
			loadColumnDetail(rowData);
		},
		onLoadSuccess : function() {
			
			if (tryToFocus) {
				
				var rows = jq.find("#rgrid").datagrid('getRows');
				for ( var i = 0; i < rows.length; i++) {
					if (rows[i].columncode == tryToFocus) {
						// 选中
						jq.find("#rgrid").datagrid('selectRow', i);
						jq.find("#rgrid").datagrid('checkRow', i);
						// 重新加载
						loadColumnDetail(rows[i]);
						break;
					}
				}
				tryToFocus = null;
			}else{
				var rows = jq.find("#rgrid").datagrid('getRows');
				if(rows.length>0){
					jq.find("#rgrid").datagrid('selectRow', 0);
					jq.find("#rgrid").datagrid('checkRow', 0);
					// 重新加载
					loadColumnDetail(rows[0]);
					
					
				}
			}
			col_copy_btn_disable_func();
		},
		toolbar : [ {
			iconCls : 'icon-add',
			handler : col_add_btn_func,
			text : '添加'
		} ,{
			iconCls : 'icon-remove',
			handler : col_del_btn_func,
			text : '删除'
		} ,{
			iconCls : 'icon-copy',
			id : 'col_copy',
			handler : col_copy_btn_func,
			text : '复制'
		} ]
	});
	
	jq.find('#col_copy').tooltip({
	    position: 'right',
	    content: '<span style="color:#fff">当数据表字段未定义任何字段时，可以参照其他表复制字段。</span>',
	    onShow: function(){
	    	jq.find('#col_copy').tooltip('tip').css({
	            backgroundColor: '#666',
	            borderColor: '#666'
	        });
	    }
	});

	jq.find(".datagrid-pager > table").hide();
	jq.find("#delBtn").hide();

	// 初始化添加按钮
	jq.find('#addBtn').linkbutton({
		onClick : add_btn_click
	});
}

function col_add_btn_func() {
	
	var jq = parent.$.modalDialog.handler;
	jq.find('#columncode').textbox('readonly', false);
	var form = jq.find("#colForm");
	form.form("clear");
	
	jq.find('#columntype').combobox('setValue', 'S');
	jq.find('#startmark').attr('checked', true);
	jq.find('#tablecode').val(sel_tablecode);
	jq.find('#systempretag').val(0);
}

function col_del_btn_func() {
	var sel = parent.$.modalDialog.handler.find('#rgrid').datagrid(
			'getSelected');
	if (sel) {

		if (sel.columnid == 0) {
			col_cancel_btn_func();// 调用取消
			return;
		}
		$.post(colUrls.removeColumn, {
			columnid : sel.columnid
		}, function(msg) {
			easyui_auto_notice(msg, function() {
				parent.$.modalDialog.handler.find('#rgrid').datagrid(
						'clearSelections');
				parent.$.modalDialog.handler.find('#rgrid').datagrid('reload');
				parent.$.modalDialog.handler.find('#lgrid').datagrid('reload');
				parent.$.modalDialog.handler.find('#colForm').form('clear');

			}, function() {

			}, "删除过程发生异常！");
		});
	} else {
		easyui_warn('请选择一个数据表字段！');
	}

}

function col_copy_btn_disable_func() {
	var rows = parent.$.modalDialog.handler.find("#rgrid").datagrid('getRows');
	if (rows.length > 0)
		parent.$.modalDialog.handler.find("#col_copy").linkbutton('disable');
	else
		parent.$.modalDialog.handler.find("#col_copy").linkbutton('enable');
}

function col_copy_btn_func() {
	var rows = parent.$.modalDialog.handler.find("#rgrid").datagrid('getRows');
	if (rows.length > 0) {
		easyui_warn('该表已有设置字段，不允许参照表复制！');
		return;
	}
	
	var content = '<div>'+
		'<div  id="layout_gridDialog" class="easyui-layout" >' + 
			'<div region="north" style="height:30px;background-color:#eee;padding: 0px 5px;" border="true">' + 
				'表编码&nbsp;&nbsp;&nbsp;&nbsp;<input id="tablecode" class="easyui-textbox" style="width:100px;" />&nbsp;&nbsp;&nbsp;&nbsp;' +
				'表名称&nbsp;&nbsp;&nbsp;&nbsp;<input id="tablename" class="easyui-textbox" style="width:100px;"/>&nbsp;&nbsp;&nbsp;&nbsp;' +
				'<a id="btn_query" class="easyui-linkbutton" href="#" iconCls="icon-search">查询</a>'+
			'</div>'+
			'<div region="center"  style="height:300px;">' + 
				'<table id="copyToTable"></table>'+
			'</div></div></div>' ;
	
	parent.$.fastModalDialog({
		title : '数据表字段复制',
		width : 450,
		height : 400,
		iconCls:'icon-search',
		html : content,
		dialogID : 'copyToTableDialog',
		onOpen: function(){
				var jqDialog = parent.$.fastModalDialog.handler['copyToTableDialog'];
				var layout_gridDialog = jqDialog.find("#layout_gridDialog");
				layout_gridDialog.layout({
					fit:true
				});
				var grid_ = jqDialog.find('#copyToTable');
				
				jqDialog.find("#btn_query").linkbutton({
					onClick: function(){
						grid_.datagrid("load", {
							tablecode: jqDialog.find("#tablecode").val(),
							tablename: jqDialog.find("#tablename").val()
						});
					}
				});
				grid_.datagrid({
					url : contextpath + 'base/tabsdef/SysDicTableController/queryTabsToCopyCol.do',
					columns :  [[{field : "ck", checkbox : true}, {field : "tablecode",title : "表名",width : 120},
					 	    	{field : "tablename", title : "中文表名", width : 200}]],
					singleSelect:true,
					fit:true
				});
		},
		buttons: [	
					{
						text:"确定复制",
						iconCls: "icon-ok",
						handler: function(){
							var record = parent.$.fastModalDialog.handler['copyToTableDialog'].find('#copyToTable').datagrid('getSelected');
							if (!record) {
								easyui_warn('请选择一条参照表数据！');
								return;
							}
							if (record.tablecode == sel_tablecode) {
								easyui_warn('不允许选择目标表（' + sel_tablecode + '-' + sel_tablename + '）！');
								return;
							}
							parent.$.messager.confirm('确认', '确认将表（' + record.tablecode + '-' + record.tablename + '）的字段复制到表（' + sel_tablecode + '-' + sel_tablename + '）中?', function(r) {
								if (r) {
									$.post(contextpath + 'base/tabsdef/SysDicColumnController/copyColToTargetTab.do', {sourceTablecode : record.tablecode, targetTablecode : sel_tablecode}, function(result) {
							        	 if (result.success) {
							        		 parent.$.messager.show({
							        				title : '提示',
							        				msg : result.title,
							        				timeout : 2000,
							        				showType : 'fade',
							        				style : {
							        					right : '',
							        					bottom : ''
							        				}
							        		});
							 				parent.$.modalDialog.handler.find('#rgrid').datagrid('reload');
											parent.$.modalDialog.handler.find('#lgrid').datagrid('reload');
							        		parent.$.fastModalDialog.handler['copyToTableDialog'].dialog('close');
							        	 } else
							        		 easyui_warn(result.title);
										
									});
								}
							});
							
							
						}
					}]
	});
	
}

function col_save_btn_func() {

	parent.$.modalDialog.handler.find('#colForm').form(
			'submit',
			{
				url : colUrls.editColumn,
				onSubmit : function() {
					var bl = parent.$.modalDialog.handler.find('#colForm')
							.form('validate');
					return bl;
				},
				success : function(data) {
					data = eval("(" + data + ")");
					easyui_auto_notice(data, function() {
						
						 tryToFocus = parent.$.modalDialog.handler.find(
								'#colForm').find("#columncode").val();
						//parent.$.modalDialog.handler.find('#lgrid').datagrid(
						//		'reload');
						parent.$.modalDialog.handler.find('#rgrid').datagrid(
								'reload');

						colCreating = false;

					}, null, '保存过程中发生异常！');

				}
			});
}

function col_cancel_btn_func() {
	parent.$.modalDialog.handler.find('#rgrid').datagrid('clearSelections');
	parent.$.modalDialog.handler.find('#rgrid').datagrid('reload');
	parent.$.modalDialog.handler.find('#lgrid').datagrid('clearSelections');
	//parent.$.modalDialog.handler.find('#lgrid').datagrid('reload');
	parent.$.modalDialog.handler.find('#colForm').form('clear');
	colCreating = false;
}
/**
 * 加载详情到form中
 * 
 * @param colDetail
 */
function loadColumnDetail(colDetail) {
	var jq = parent.$.modalDialog.handler;

	$.post(colUrls.getColumn + '?id=' + colDetail.columnid, {}, function(obj) {
		var form = jq.find("#colForm");
		form.form({
			onLoadSuccess : function(data) {
				form.find('#cnsourceelementcode').searchbox('setValue',
						data.cnsourceelementcode);
			}
		});
		form.form('load', obj);
		jq.find('#columncode').textbox('readonly', true);
	}, "json");

}

function elementcodeSelect(value, name) {
	var namectl = '#cnsourceelementcode';
	var codectl = '#sourceelementcode';
	// 显示弹出框

	parent.$
			.fastModalDialog({
				title : '数据项选择',
				width : 600,
				height : 400,
				iconCls : 'icon-search',
				html : '<div><table id="grid_tab_ele" ></table></div>',
				dialogID : 'dlg_tab_ele',
				onOpen : function() {
					var grid_ = parent.$.fastModalDialog.handler['dlg_tab_ele']
							.find('#grid_tab_ele');
					grid_.datagrid({
						url : colUrls.elementcodeSelect,
						columns : [ [ {
							field : "ck",
							checkbox : true
						}, {
							field : "elementname",
							title : "数据项名称",
							width : 120
						}, {
							field : "elementcode",
							title : "数据项编码",
							width : 200
						}, {
							field : "tablecode",
							title : "数据项值集表",
							width : 200
						}

						] ],
						singleSelect : true,
						fit : true,
						pagination : true,
						pageSize : 100
					});
				},
				buttons : [
						{
							text : "确定",
							iconCls : "icon-ok",
							handler : function() {
								var sel = parent.$.fastModalDialog.handler['dlg_tab_ele']
										.find('#grid_tab_ele').datagrid(
												'getSelected');

								var yyy=parent.$.modalDialog.handler.find(namectl);
								parent.$.modalDialog.handler.find(namectl)
										.searchbox('setValue', sel.elementname);
								
								var xxx=parent.$.modalDialog.handler.find(codectl);
								
								parent.$.modalDialog.handler.find(codectl).val(
										sel.elementcode);

								parent.$.modalDialog.handler.find(
										'#sourcetable').val(sel.tablecode);

								// 异步取得数据类型，数据长度，小数位数

								fillElementInfo(sel.tablecode,
										parent.$.modalDialog.handler
												.find('#columntype'),
										parent.$.modalDialog.handler
												.find('#columnlength'),
										parent.$.modalDialog.handler
												.find('#scalelength'));

								parent.$.fastModalDialog.handler['dlg_tab_ele']
										.dialog('close');
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['dlg_tab_ele']
										.dialog('close');
							}
						},
						{
							text : "清空",
							iconCls : "icon-reload",
							handler : function() {
								parent.$.fastModalDialog.handler['dlg_tab_ele']
										.dialog('close');

								setElementRelatedColReadonly(false);

								// 清空数据

								parent.$.modalDialog.handler.find(namectl)
										.searchbox('setValue', '');

								parent.$.modalDialog.handler.find(
										'#columnlength').numberbox('setValue',
										null);
								parent.$.modalDialog.handler.find(
										'#scalelength').numberbox('setValue',
										null);
								parent.$.modalDialog.handler.find(codectl).val(
										'');

								parent.$.modalDialog.handler.find(
										'#sourcetable').val('');
							}
						} ]
			});
}
/**
 * 取得表的的编码字段信息并填入
 * 
 * @param tablecode
 * @param columntypeCtl
 * @param columnlengthCtl
 * @param scalelengthCtl
 */
function fillElementInfo(tablecode, columntypeCtl, columnlengthCtl,
		scalelengthCtl) {
	$.post(colUrls.getCodeColumnDetail, {
		tablecode : tablecode
	}, function(msg) {
		if (msg != null) {

			columntypeCtl.combobox('setValue', msg.columntype);

			columnlengthCtl.numberbox('setValue', msg.columnlength);
			scalelengthCtl.numberbox('setValue', msg.scalelength);

			setElementRelatedColReadonly(true);
		} else {
			easyui_warn('在取得表' + tablecode + '的编码字段信息时发生异常！');
		}
	}, 'json');
}

function setElementRelatedColReadonly(isreadonly) {
	if (isreadonly) {
		parent.$.modalDialog.handler.find('#sourcetable')
				.attr('READONLY', true);
		parent.$.modalDialog.handler.find('#columnlength').attr('READONLY',
				true);
		parent.$.modalDialog.handler.find('#scalelength')
				.attr('READONLY', true);
		parent.$.modalDialog.handler.find('#columntype').combobox('readonly',
				true);
	} else {
		parent.$.modalDialog.handler.find('#columntype').combobox('readonly',
				false);
		parent.$.modalDialog.handler.find('#columnlength').removeAttr(
				'READONLY');
		parent.$.modalDialog.handler.find('#scalelength')
				.removeAttr('READONLY');
		parent.$.modalDialog.handler.find('#sourceelementcode').removeAttr(
				'READONLY');

		parent.$.modalDialog.handler.find('#sourcetable')
				.removeAttr('READONLY');
	}
}
/*
 * 按钮>的响应事件
 */
function add_btn_click() {
	// 如果正在编辑，拒绝添加
	if (colCreating) {
		easyui_warn('请先完成当前字段的保存后再添加字段！');
		return;
	}
	colCreating = true;
	var jq = parent.$.modalDialog.handler;
	var lgrid = jq.find("#lgrid");
	var rgrid = jq.find('#rgrid');
	// 取得选中
	var sel = lgrid.datagrid('getSelected');
	if (!sel) {
		easyui_warn('请先选中一个列！');
		return;
	}
	if (sel.used == 1) {
		easyui_warn('该列已存在！');
		return;
	}
	var collength = getColumnLen(sel.datatype);
	var newcol = {
		columnid : 0,
		columncode : sel.colname,
		columnname : sel.remarks
	};
	// 填入右边 grid
	rgrid.datagrid('appendRow', newcol);
	rgrid.datagrid('selectRecord', newcol.columnid);
	// 填入form

	jq.find('#colForm').form('clear');

	jq.find('#columncode').textbox('setValue', sel.colname);
	jq.find('#columncode').textbox('readonly', true);
	jq.find('#columnname').textbox('setValue', sel.remarks);
	var type = 'S';
	if (sel.datatype.indexOf('NUMBER') >= 0) {
		type = 'N';
	} else if (sel.datatype.indexOf('DATE') >= 0) {
		type = 'D';
	}
	jq.find('#columntype').combobox('setValue', type);
	jq.find('#columnlength').numberbox('setValue', collength);
	jq.find('#nullable').attr('checked', true);
	jq.find('#startmark').attr('checked', true);
	// 左边选中的行修改状态

	jq.find('#tablecode').val(sel_tablecode);
	jq.find('#systempretag').val(0);
	sel.used = 1;
	var index = lgrid.datagrid('getRowIndex', sel);
	lgrid.datagrid('updateRow', {
		index : index,
		row : sel
	});
	
	col_copy_btn_disable_func();
}

function getColumnLen(colname) {
	var start = colname.indexOf('(') + 1;
	var end1 = colname.indexOf(")", start);
	var end2 = colname.indexOf(",", start);
	var end = end2 == -1 ? end1 : Math.min(end1, end2);
	var lenstr = colname.substring(start, end);
	console.log("lenstr:" + lenstr);
	var len = 0;

	len = +lenstr;
	if (isNaN(len)) {
		len = 0;
	}
	return len;
}