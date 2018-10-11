function showSheetOfTmpl(row) {
	var id = row.id;

	$('#sheetgrid').datagrid({
		url : urls.sheetQuery + '?cfgid=' + id
	});

}
function sheetQuery() {
	$('#sheetgrid').datagrid('reload');
}
function sheetadd() {
	var sel = $('#tmplgrid').datagrid('getSelected');
	if (!sel) {
		easyui_warn("请先选择一个模板！");
		return;
	}

	parent.$
			.fastModalDialog({
				title : '工作表配置',
				width : 520,
				height : 200,
				iconCls : 'icon-add',
				html : $('#sheetWindow')[0].outerHTML,
				dialogID : 'sheetAddDlg',
				onOpen : function() {
					var form_3358 = parent.$.fastModalDialog.handler['sheetAddDlg']
							.find('#form_3358');

					form_3358.find("input[name='name']").textbox({
						required : true,
						missingMessage: "请输入工作表名称"
					});
					form_3358.find("input[name='id']").textbox({
						required : true,
						missingMessage: "请输入工作表索引号"
					});

					form_3358.find("input[name='cfgname']").textbox({
						editable: false
					}).textbox("setValue", sel.cfgname);
					form_3358.find("input[name='cfgid']").val(sel.id);

					form_3358.form('validate');
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								// 提交数据
								var form_3358 = parent.$.fastModalDialog.handler['sheetAddDlg']
										.find('#form_3358');

								form_3358
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/'
															+ urls.sheetAdd,
													onSubmit : function() {
														var result = form_3358
																.form('validate');
														if (!result) {
															easyui_warn('请录入必填项！');
														}
														return result;
													},
													success : function(data) {
														data = eval("(" + data
																+ ")");

														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['sheetAddDlg']
																			.dialog('close');
																	sheetQuery();
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
								parent.$.fastModalDialog.handler['sheetAddDlg']
										.dialog('close');
							}
						} ]
			});
}

function sheetedit() {
	var sel = $('#sheetgrid').datagrid('getSelected');
	if (!sel) {
		easyui_warn("请先选择一个工作表！");
		return;
	}
	parent.$
			.fastModalDialog({
				title : '工作表配置',
				width : 520,
				height : 200,
				iconCls : 'icon-add',
				html : $('#sheetWindow')[0].outerHTML,
				dialogID : 'sheetEditDlg',
				onOpen : function() {
					var form_3358 = parent.$.fastModalDialog.handler['sheetEditDlg']
							.find('#form_3358');

					form_3358.find("input[name='name']").textbox({
						required : true,
						missingMessage: "请输入工作表名称"
					});
					form_3358.find("input[name='id']").textbox({
						required : true,
						missingMessage: "请输入工作表索引号"
					});
					
					form_3358.find("input[name='cfgname']").textbox({
						editable: false
					});
					
					form_3358.form('load', sel);

					form_3358.form('validate');
				},
				buttons : [
						{
							text : "修改",
							iconCls : "icon-save",
							handler : function() {
								// 提交数据
								var form_3358 = parent.$.fastModalDialog.handler['sheetEditDlg']
										.find('#form_3358');

								form_3358
										.form(
												'submit',
												{
													url : 'base/excel/SysExcelImpCfgController/'
															+ urls.sheetEdit
															+ '?oldid='
															+ sel.id,
													onSubmit : function() {
														var result = form_3358
																.form('validate');
														if (!result) {
															easyui_warn('请录入必填项！');
														}
														return result;
													},
													success : function(data) {
														data = eval("(" + data
																+ ")");

														easyui_auto_notice(
																data,
																function() {
																	parent.$.fastModalDialog.handler['sheetEditDlg']
																			.dialog('close');
																	sheetQuery();
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
								parent.$.fastModalDialog.handler['sheetEditDlg']
										.dialog('close');
							}
						} ]
			});
}

function sheetdel() {
	var sel = $('#sheetgrid').datagrid('getSelected');
	if (!sel) {
		easyui_warn("请先选择一个工作表！");
		return;
	}
	parent.$.messager.confirm('确认', '是否删除该工作表配置？', function(r) {
		if (r) {
			$.post(urls.sheetDel, {
				id : sel.id,
				cfgid : sel.cfgid
			}, function(msg) {
				easyui_auto_notice(msg, function() {
					sheetQuery();
				}, null, "删除过程发生异常！");
			}, "JSON");
		}
	})
}

function opensheetCfgDetailWindow() {

	var sel = $('#sheetgrid').datagrid('getSelected');
	if (!sel) {
		easyui_warn("请先选择一个工作表！");
		return;
	}

	parent.$.modalDialog({
		width : 820,
		height : 600,
		title : '工作表配置明细',
		href : 'base/excel/SysExcelImpCfgController/dataImpCfgSheetDetailEntry.do',
		onLoad : function() {
			parent.$.modalDialog.handler.find('#datanode').data("cfgid",
					sel.cfgid);
			parent.$.modalDialog.handler.find('#datanode').data("sheetid",
					sel.id);
			var grid = parent.$.modalDialog.handler.find('#shtdtlgrid');
			grid.datagrid({
				url : 'base/excel/SysExcelImpCfgController/querySheetList.do?cfgid=' + sel.cfgid
						+ '&sheetid=' + sel.id,
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'typename',
					title : '分类',
					width : 100
				}, {
					field : 'name',
					title : '名称',
					width : 100
				}, {
					field : 'startrow',
					title : '超始行',
					width : 60
				}, {
					field : 'endrow',
					title : '结束行',
					width : 60
				}, {
					field : 'startcol',
					title : '起始列',
					width : 60
				}, {
					field : 'endcol',
					title : '结束列',
					width : 60
				}, {
					field : 'tablename',
					title : '数据表表名',
					width : 200
				}, {
					field : 'uuidfield',
					title : '批次标识字段',
					width : 100
				} ] ],
				singleSelect : true,
				fit : true,
				onClickRow : function(rowindex, rowData) {
					grid2.datagrid({
						url : 'base/excel/SysExcelImpCfgController/querySheetCol.do?cfgid=' + sel.cfgid
								+ '&sheetid=' + sel.id + '&typecode='
								+ rowData.typecode + '&typeid='
								+ rowData.id
					});
				}
			});

			var grid2 = parent.$.modalDialog.handler.find('#colgrid');
			grid2.datagrid({
				url : '',
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					title : '工作表索引号',
					field : 'sheetid',
					width : 100

				}, {
					title : '行号',
					field : 'rownum',
					width : 40
				}, {
					title : '列号',
					field : 'colnum',
					width : 40
				}, {
					title : '名称',
					field : 'text',
					width : 90
				}, {
					title : '字段名',
					field : 'fieldname',
					width : 120
				}, {
					title : '数据类型',
					field : 'datatypename',
					width : 60
				}, {
					title : '允许空值',
					field : 'allownullname',
					width : 55
				} ] ],
				fit : true,
				singleSelect : true
			});

		},
		buttons : [ {
			text : "取消",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
