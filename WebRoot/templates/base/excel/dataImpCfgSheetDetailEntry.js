/**
 * sheet 明细 添加横向列表
 */
function shtdtl_hlist_add() {
	var win_html = $('#add_hlist_window')[0].outerHTML;
	parent.$
			.fastModalDialog({
				title : '添加',
				width : 450,
				height : 250,
				iconCls : 'icon-add',
				html : win_html,
				dialogID : 'hlistDlg',
				onOpen : function() {
					var fm = parent.$.fastModalDialog.handler['hlistDlg']
							.find('#hlistform');
					fm.find("input[name='name']").textbox({
						required : true,
						missingMessage: "请输入名称"
					});

					fm.find("input[name='startrow']").numberbox({
						required : true,
						missingMessage: "请输入起始行"
					});
					fm.find("input[name='endrow']").numberbox({

					});
					fm.find("input[name='tablename']").textbox({
						required : true,
						missingMessage: "请输入数据表表名"
					});
					
					fm.find("input[name='uuidfield']").textbox();
					
					// 取出cfgid
					fm.find("input[name='cfgid']").val(
							parent.$.modalDialog.handler.find('#datanode')
									.data('cfgid'));
					fm.find("input[name='sheetid']").val(
							parent.$.modalDialog.handler.find('#datanode')
									.data('sheetid'));
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								var fm = parent.$.fastModalDialog.handler['hlistDlg']
										.find('#hlistform');

								fm.find("input[name='typecode']").val('1');
								if (!fm.form('validate')) {
									easyui_warn('请填写必填字段！');
									return;
								}
								fm
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/addSheetList.do',
													success : function(data) {
														data = eval("(" + data
																+ ")");
														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['hlistDlg']
																			.dialog('close');
																	parent.$.modalDialog.handler
																			.find(
																					'#shtdtlgrid')
																			.datagrid(
																					'reload');
																}, null,
																'保存过程中发生异常！');
													}
												});
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['hlistDlg']
										.dialog('close');
							}
						} ]
			});
}
/**
 * sheet 明细 添加竖向列表
 */
function shtdtl_vlist_add() {
	var win_html = $('#add_vlist_window')[0].outerHTML;

	parent.$
			.fastModalDialog({
				title : '添加',
				width : 450,
				height : 270,
				iconCls : 'icon-add',
				html : win_html,
				dialogID : 'vlistDlg',
				onOpen : function() {
					var fm = parent.$.fastModalDialog.handler['vlistDlg']
							.find('#vlistform');
					fm.find("input[name='name']").textbox({
						required : true,
						missingMessage: "请输入名称"
					});

					fm.find("input[name='startcol']").numberbox({
						required : true,
						missingMessage: "请输入起始列"
					});
					fm.find("input[name='endcol']").numberbox();
					fm.find("input[name='tablename']").textbox({
						required : true,
						missingMessage: "请输入数据表表名"
					});
					
					fm.find("input[name='uuidfield']").textbox();
					
					// 取出cfgid
					fm.find("input[name='cfgid']").val(
							parent.$.modalDialog.handler.find('#datanode')
									.data('cfgid'));
					fm.find("input[name='sheetid']").val(
							parent.$.modalDialog.handler.find('#datanode')
									.data('sheetid'));
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								var fm = parent.$.fastModalDialog.handler['vlistDlg']
										.find('#vlistform');
								
								fm.find("input[name='typecode']").val('2');
								
								if (!fm.form('validate')) {
									easyui_warn('请填写必填字段！');
									return;
								}
								fm
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/addSheetList.do',
													success : function(data) {
														data = eval("(" + data
																+ ")");
														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['vlistDlg']
																			.dialog('close');
																	parent.$.modalDialog.handler
																			.find(
																					'#shtdtlgrid')
																			.datagrid(
																					'reload');
																}, null,
																'保存过程中发生异常！');
													}
												});
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['vlistDlg']
										.dialog('close');
							}
						} ]
			});
}
/**
 * sheet 明细 添加form
 */
function shtdtl_form_add() {
	var win_html = $('#add_fm_window')[0].outerHTML;
	parent.$
			.fastModalDialog({
				title : '添加',
				width : 450,
				height : 250,
				iconCls : 'icon-add',
				html : win_html,
				dialogID : 'fmDlg',
				onOpen : function() {
					var fm = parent.$.fastModalDialog.handler['fmDlg']
							.find('#fmform');
					fm.find("input[name='name']").textbox({
						required : true,
						missingMessage: "请输入名称"
					});

					fm.find("input[name='tablename']").textbox({
						required : true,
						missingMessage:"请输入数据表表名"
					});
					
					fm.find("input[name='uuidfield']").textbox();
					
					// 取出cfgid
					fm.find("input[name='cfgid']").val(
							parent.$.modalDialog.handler.find('#datanode')
									.data('cfgid'));
					fm.find("input[name='sheetid']").val(
							parent.$.modalDialog.handler.find('#datanode')
									.data('sheetid'));
					fm.find("input[name='typecode']").val('3');
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								var fm = parent.$.fastModalDialog.handler['fmDlg']
										.find('#fmform');
								if (!fm.form('validate')) {
									easyui_warn('请填写必填字段！');
									return;
								}

								fm
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/addSheetList.do',
													success : function(data) {
														data = eval("(" + data
																+ ")");
														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['fmDlg']
																			.dialog('close');
																	parent.$.modalDialog.handler
																			.find(
																					'#shtdtlgrid')
																			.datagrid(
																					'reload');
																}, null,
																'保存过程中发生异常！');
													}
												});

							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['fmDlg']
										.dialog('close');
							}
						} ]
			});
}

function shtdtl_del() {
	var sel = parent.$.modalDialog.handler.find('#shtdtlgrid').datagrid(
			'getSelected');
	if (!sel) {
		easyui_warn('请选择一行数据！');
	}

	var _cfgid = parent.$.modalDialog.handler.find('#datanode').data('cfgid');
	var _sheetid = parent.$.modalDialog.handler.find('#datanode').data(
			'sheetid');
	parent.$.messager.confirm('确认', '是否删除该工作表中的列表？', function(r) {
		if (r) {
			$.post('base/excel/SysExcelImpCfgController/delSheetList.do', {
				cfgid : _cfgid,
				sheetid : _sheetid,
				id : sel.id
			}, function(msg) {
				easyui_auto_notice(msg, function() {
					parent.$.modalDialog.handler.find('#shtdtlgrid').datagrid(
							'reload');

					parent.$.modalDialog.handler.find('#colgrid').datagrid(
							'loadData', []); // 清空数据
				}, null, "删除过程中发生异常");
			}, "JSON");
		}
	})

}
function shtdtl_edit() {
	var sel = parent.$.modalDialog.handler.find('#shtdtlgrid').datagrid(
			'getSelected');
	if (!sel) {
		easyui_warn('请选择一行数据！');
	}
	switch (sel.typecode) {
	case '1':
		shtdtl_edit_list(sel,'hlist');
		break;
	case '2':
		shtdtl_edit_list(sel,'vlist');
		break;
	case '3':
		shtdtl_edit_list(sel,'fm');
		break;
	}

}

function shtdtl_edit_list(typevo,dlgType) {
	var win_html = $('#add_'+dlgType+'_window')[0].outerHTML;

	var _cfgid = parent.$.modalDialog.handler.find('#datanode').data('cfgid');
	var _sheetid = parent.$.modalDialog.handler.find('#datanode').data(
			'sheetid');
	parent.$
			.fastModalDialog({
				title : '修改',
				width : 450,
				height : 280,
				iconCls : 'icon-edit',
				html : win_html,
				dialogID : dlgType+'Dlg',
				onOpen : function() {
					var fm = parent.$.fastModalDialog.handler[dlgType+'Dlg']
							.find('#'+dlgType+'form');
					
					if (!fm.form('validate')) {
						easyui_warn('请填写必填字段！');
						return;
					}
					
					fm.find("input[name='name']").textbox({
						required : true,
						missingMessage: "请输入名称"
					});

					fm.find("input[name='startrow']").numberbox({
						required : true,
						missingMessage: "请输入起始行"
					});
					fm.find("input[name='endrow']").numberbox();
					
					fm.find("input[name='startcol']").numberbox({
						required : true,
						missingMessage: "请输入起始列"
					});
					fm.find("input[name='endcol']").numberbox();
					
					fm.find("input[name='tablename']").textbox({
						required : true,
						missingMessage: "请输入数据表表名"
					});
					
					fm.find("input[name='uuidfield']").textbox();
					
					fm.form({
						onLoadSuccess:function(){
							fm.find("input[name='cfgid'").val(_cfgid);
							fm.find("input[name='sheetid'").val(_sheetid);
						}
					});
					fm.form('load', typevo);
				},
				buttons : [
						{
							text : "修改",
							iconCls : "icon-save",
							handler : function() {
								var fm = parent.$.fastModalDialog.handler[dlgType+'Dlg']
										.find('#'+dlgType+'form');

								// 取出cfgid
								fm.find("input[name='cfgid']").val(
										parent.$.modalDialog.handler.find(
												'#datanode').data('cfgid'));
								fm.find("input[name='sheetid']").val(
										parent.$.modalDialog.handler.find(
												'#datanode').data('sheetid'));
								fm
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/editSheetList.do',
													success : function(data) {
														data = eval("(" + data
																+ ")");
														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler[dlgType+'Dlg']
																			.dialog('close');
																	parent.$.modalDialog.handler
																			.find(
																					'#shtdtlgrid')
																			.datagrid(
																					'reload');
																}, null,
																'修改过程中发生异常！');
													}
												});
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler[dlgType+'Dlg']
										.dialog('close');
							}
						} ]
			});
}

function shtdtl_edit_vlist(typevo) {
	var win_html = $('#add_vlist_window')[0].outerHTML;
	var _cfgid = parent.$.modalDialog.handler.find('#datanode').data('cfgid');
	var _sheetid = parent.$.modalDialog.handler.find('#datanode').data(
			'sheetid');
	parent.$
			.fastModalDialog({
				title : '修改竖向列表',
				width : 450,
				height : 250,
				iconCls : 'icon-edit',
				html : win_html,
				dialogID : 'vlistDlg',
				onOpen : function() {
					var fm = parent.$.fastModalDialog.handler['vlistDlg']
							.find('#vlistform');
					fm.form('load', typevo);
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								var fm = parent.$.fastModalDialog.handler['vlistDlg']
										.find('#vlistform');

								if (!fm.form('validate')) {
									easyui_warn('请填写必填字段！');
									return;
								}

								// 取出cfgid
								fm.find("input[name='cfgid']").val(
										parent.$.modalDialog.handler.find(
												'#datanode').data('cfgid'));
								fm.find("input[name='sheetid']").val(
										parent.$.modalDialog.handler.find(
												'#datanode').data('sheetid'));
								fm
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/editSheetList.do',
													success : function(data) {
														data = eval("(" + data
																+ ")");
														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['vlistDlg']
																			.dialog('close');
																	parent.$.modalDialog.handler
																			.find(
																					'#shtdtlgrid')
																			.datagrid(
																					'reload');
																}, null,
																'修改过程中发生异常！');
													}
												});
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['vlistDlg']
										.dialog('close');
							}
						} ]
			});
}

function shtdtl_edit_form(typevo) {
	var win_html = $('#add_hlist_window')[0].outerHTML;

	var _cfgid = parent.$.modalDialog.handler.find('#datanode').data('cfgid');
	var _sheetid = parent.$.modalDialog.handler.find('#datanode').data(
			'sheetid');
	parent.$
			.fastModalDialog({
				title : '修改横向列表',
				width : 450,
				height : 250,
				iconCls : 'icon-edit',
				html : win_html,
				dialogID : 'hlistDlg',
				onOpen : function() {
					var fm = parent.$.fastModalDialog.handler['hlistDlg']
							.find('#hlistform');
					fm.form('load', typevo);
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								var fm = parent.$.fastModalDialog.handler['hlistDlg']
										.find('#hlistform');

								if (!fm.form('validate')) {
									easyui_warn('请填写必填字段！');
									return;
								}

								// 取出cfgid
								fm.find("input[name='cfgid']").val(
										parent.$.modalDialog.handler.find(
												'#datanode').data('cfgid'));
								fm.find("input[name='sheetid']").val(
										parent.$.modalDialog.handler.find(
												'#datanode').data('sheetid'));
								fm
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/editSheetList.do',
													success : function(data) {
														data = eval("(" + data
																+ ")");
														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['hlistDlg']
																			.dialog('close');
																	parent.$.modalDialog.handler
																			.find(
																					'#shtdtlgrid')
																			.datagrid(
																					'reload');
																}, null,
																'修改过程中发生异常！');
													}
												});
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['hlistDlg']
										.dialog('close');
							}
						} ]
			});
}


function shtdtl_close() {
	parent.$.modalDialog.handler.dialog('close');
}

var btn_txts = {
	add : "保存",
	edit: "修改"
};

function coledit(mode) {
	var sel = parent.$.modalDialog.handler.find('#shtdtlgrid').datagrid(
			'getSelected');

	var colsel = parent.$.modalDialog.handler.find('#colgrid').datagrid(
			'getSelected');
	
	switch (mode) { // 编辑模式判断
	case 'add':
		if (!sel) {
			easyui_warn('请选择一个列表！');
			return;
		}
		break;
	case 'edit':
		if (!colsel) {
			easyui_warn('请选择一个字段！');
			return;
		}
		break;
	}

	var _cfgid = parent.$.modalDialog.handler.find('#datanode').data('cfgid');
	var _sheetid = parent.$.modalDialog.handler.find('#datanode').data(
			'sheetid');

	var win_html = $('#add_col_window')[0].outerHTML;

	parent.$.fastModalDialog({
		title : '数据表选择',
		width : 450,
		height : 350,
		iconCls : 'icon-add',
		html : win_html,
		dialogID : 'colDlg',
		onOpen : function() {
			var fm = parent.$.fastModalDialog.handler['colDlg']
					.find('#col_form');
			
			fm.find("input[name='text']").textbox({
				required : true,
				missingMessage: "请输入中文名"
			});

			fm.find("input[name='fieldname']").textbox({
				required : true,
				missingMessage: "请输入字段名"
			});

			var validateFlag = null;
			switch (sel.typecode) {
			case '1':
				validateFlag = [ false, true ];
				break;
			case '2':
				validateFlag = [ true, false ];
				break;
			case '3':
				validateFlag = [ true, true ];
				break;
			}
			
			if (validateFlag[0]) {
				fm.find("input[name='rownum']").textbox({
					required : true,
					missingMessage: "请输入行号"
				});
			}else{
				fm.find("input[name='rownum']").textbox();
			}
			if (validateFlag[1]) {
				fm.find("input[name='colnum']").textbox({
					required : true,
					missingMessage: "请输入列号"
				});
			}else{
				fm.find("input[name='colnum']").textbox();
			}

			fm.find("input[name='datatype']").combobox({
				required : true,
				editable:false,
				panelHeight: "auto",
				data : [ {
					text : '字符型',
					value : 'string'
				}, {
					text : '数字型',
					value : 'decimal'
				} ],
				missingMessage: "请选择数据类型"
			});

			fm.find("input[name='allownull']").combobox({
				required : true,
				editable:false,
				panelHeight: "auto",
				data : [ {
					text : '允许',
					value : 'true'
				}, {
					text : '不允许',
					value : 'false'
				} ],
				missingMessage: "请选择是否为空"
			});

			switch (mode) { // 编辑模式判断
			case 'edit':

				fm.form({
					onLoadSuccess : function() {
						fm.find("input[name='cfgid']").val(_cfgid);

						fm.find("input[name='sheetid']").textbox({
							editable: false
						}).textbox("setValue", _sheetid);

						fm.find("input[name='typeid']").val(sel.id);

						fm.find("input[name='typecode']").val(sel.typecode);

						fm.find("input[name='flag']").val('edit');
					}
				});

				fm.form('load', colsel);
				break;
			case 'add':
				fm.find("input[name='cfgid']").val(_cfgid);

				fm.find("input[name='sheetid']").textbox({
					editable:false
				}).textbox("setValue", _sheetid);

				fm.find("input[name='typeid']").val(sel.id);

				fm.find("input[name='typecode']").val(sel.typecode);

				fm.find("input[name='flag']").val('add');

				break;
			}
		},
		buttons : [
				{
					text : btn_txts[mode],
					iconCls : "icon-save",
					handler : function() {
						var fm = parent.$.fastModalDialog.handler['colDlg']
								.find('#col_form');
						
						if (!fm.form('validate')) {
							easyui_warn('请填写必填字段！');
							return;
						}
						fm.form('submit', {
							url : 'base/excel/SysExcelImpCfgController/editSheetCol.do',
							success : function(data) {
								data = eval("(" + data + ")");
								easyui_auto_notice(data, function() {
									parent.$.fastModalDialog.handler['colDlg']
											.dialog('close');
									parent.$.modalDialog.handler.find(
											'#colgrid').datagrid('reload');

								}, null, '保存过程中发生异常！');
							}
						});

					}
				},
				{
					text : "取消",
					iconCls : "icon-cancel",
					handler : function() {
						parent.$.fastModalDialog.handler['colDlg']
								.dialog('close');
					}
				} ]
	});
}

function coldel() {
	var sel = parent.$.modalDialog.handler.find('#shtdtlgrid').datagrid(
			'getSelected');

	var colsel = parent.$.modalDialog.handler.find('#colgrid').datagrid(
			'getSelected');

	if (!colsel) {
		easyui_warn('请选择一个列表对象！');
		return;
	}

	var _cfgid = parent.$.modalDialog.handler.find('#datanode').data('cfgid');
	var _sheetid = parent.$.modalDialog.handler.find('#datanode').data(
			'sheetid');

	parent.$.messager.confirm('确认', '是否删除选中的条目?', function(r) {
		if (r) {
			$.post('base/excel/SysExcelImpCfgController/delSheetCol.do', {
				cfgid : _cfgid,
				sheetid : _sheetid,
				typeid : sel.id,
				typecode : sel.typecode,
				fieldname : colsel.fieldname
			}, function(msg) {
				easyui_auto_notice(msg, function() {
					parent.$.modalDialog.handler.find('#colgrid').datagrid(
							'reload');

				}, null, "删除过程发生异常");
			}, "JSON")
		}
	});
}