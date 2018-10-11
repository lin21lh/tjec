var urls = {
	queryDataImpCfg : 'queryDataImpCfg.do',
	tmplSave : 'SysExcelImpCfgController/addDataImpCfg.do',
	tmplEdit : 'SysExcelImpCfgController/editDataImpCfg.do',
	tmplDel : 'delDataImpCfg.do',
	tmplLoad : 'SysExcelImpCfgController/loadDataImpCfg.do',
	sheetQuery : 'querySheets.do',
	sheetDel : 'delSheet.do',
	sheetAdd : 'addSheet.do',
	sheetEdit : 'updateSheet.do',
	sheetConfig : 'configSheet.do'
};

var panel_ctl_handles = [ {
	panelname : '#qpanel1',	//要折叠的面板id
//	queryfunc : tmplquery, //刷新操作函数
	gridname:"#tmplgrid",
	buttonid : '#openclose' //折叠按钮id
} ]
$(function() {
	
	$("#cfgcategory").textbox("addClearBtn", {iconCls: "icon-clear"});
	$("#cfgname").textbox("addClearBtn", {iconCls: "icon-clear"});
	
	$('#qpanel1').panel('close');
	$('#tmplgrid').datagrid({
		url : urls.queryDataImpCfg,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'cfgname',
			title : '模板名称',
			width : 300
		}, {
			field : 'cfgcategory',
			title : '模板类别',
			width : 200
		}, {
			field : 'status',
			title : '状态',
			width : 60
		} ] ],
		fit : true,
		singleSelect : true,
		border : false,
		toolbar : '#toolbar_tmpl',
		onClickRow : function(rowIndex, rowData) {
			showSheetOfTmpl(rowData);
		}
	});

	$('#sheetgrid').datagrid({
		url : '',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'cfgname',
			title : '模板名称',
			width : 300
		}, {
			field : 'id',
			title : '索引号',
			width : 80
		}, {
			field : 'name',
			title : '工作表名称',
			width : 300
		} ] ],
		fit : true,
		singleSelect : true,
		border : false,
		toolbar : '#toolbar_sheet'
	});
});

function tmplquery() {
	$('#tmplgrid').datagrid("load", {
		cfgcategory : $("#cfgcategory").textbox("getValue"),
		cfgname : $("#cfgname").textbox("getValue")
	});
}

function tmpladd() {
	parent.$
			.fastModalDialog({
				title : '模板配置',
				width : 520,
				height : 270,
				iconCls : 'icon-add',
				html : $('#tmplWindow')[0].outerHTML,
				dialogID : 'tmplDlg',
				onOpen : function() {
					var form_3356 = parent.$.fastModalDialog.handler['tmplDlg']
							.find('#form_3356');

					form_3356.find("input[name='cfgcategory']").textbox({
						required : true,
						missingMessage: "请输入配置类别"
					});
					form_3356.find("input[name='cfgname']").textbox({
						required : true,
						missingMessage: "请输入配置名称"
					});

					form_3356.find("input[name='dataexception']").combobox({
						required : true,
						editable: false,
						missingMessage: "请输入选择数据异常处理",
						panelHeight: "auto",
						data : [ {
							text : '抛出异常，终止导入',
							value : 'throwerr'
						}, {
							text : '继续导入，最后提示异常信息 ',
							value : 'insert'
						} ]
					});
					
					form_3356.find("input[name='classname']").textbox();
					form_3356.find("input[name='remark']").textbox();
					
					form_3356.form('validate');
				},
				buttons : [
						{
							text : "保存",
							iconCls : "icon-save",
							handler : function() {
								// 提交数据
								var form_3356 = parent.$.fastModalDialog.handler['tmplDlg']
										.find('#form_3356');

								form_3356
										.form(
												'submit',
												{
													url : 'base/excel/'
															+ urls.tmplSave,
													onSubmit : function() {
														var result = form_3356
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
																	parent.$.fastModalDialog.handler['tmplDlg']
																			.dialog('close');
																	tmplquery();
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
								parent.$.fastModalDialog.handler['tmplDlg']
										.dialog('close');
							}
						} ]
			});
}

function tmpledit() {

	var sel = $('#tmplgrid').datagrid('getSelected');
	if (!sel) {
		easyui_warn('请选择一个配置模板！');
		return;
	}
	parent.$
			.fastModalDialog({
				title : '模板配置',
				width : 520,
				height : 270,
				iconCls : 'icon-edit',
				dialogID : 'tmplEditDlg',
				html : $('#tmplWindow')[0].outerHTML,
				onOpen : function() {
					var form_3356 = parent.$.fastModalDialog.handler['tmplEditDlg']
							.find('#form_3356');

					form_3356.find("input[name='cfgcategory']").textbox({
						required : true,
						missingMessage:"请输入配置类别"
					});
					form_3356.find("input[name='cfgname']").textbox({
						required : true,
						missingMessage:"请输入配置名称"
					});

					form_3356.find("input[name='dataexception']").combobox({
						required : true,
						data : [ {
							text : '抛出异常，终止导入',
							value : 'throwerr'
						}, {
							text : '继续导入，最后提示异常信息 ',
							value : 'insert'
						} ]
					});

					form_3356.find("input[name='classname']").textbox();
					form_3356.find("input[name='remark']").textbox();
					
					
					form_3356.form({
						onLoadSuccess : function() {
							form_3356.form('validate');
						}
					});

					form_3356.form('load', 'base/excel/' + urls.tmplLoad + '?id='
							+ sel.id);
				},
				buttons : [
						{
							text : "修改",
							iconCls : "icon-save",
							handler : function() {
								// 提交数据
								var form = parent.$.fastModalDialog.handler['tmplEditDlg']
										.find('#form_3356');
								form
										.form(
												'submit',
												{
													url : 'base/excel/'
															+ urls.tmplEdit,
													onSubmit : function() {
														var result = form
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
																	parent.$.fastModalDialog.handler['tmplEditDlg']
																			.dialog('close');
																	tmplquery();
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
								parent.$.fastModalDialog.handler['tmplEditDlg']
										.dialog('close');
							}
						} ]
			});
}

function tmpldel() {
	var sel = $('#tmplgrid').datagrid('getSelected');
	if (!sel) {
		easyui_warn('请选择一个配置模板！');
		return;
	}

	parent.$.messager.confirm('确认', '是否删除选中的配置？', function(r) {
		if (r) {
			$.post(urls.tmplDel, {
				id : sel.id
			}, function(msg) {
				easyui_auto_notice(msg, function() {
					tmplquery();

					$('#sheetgrid').datagrid('loadData', []);
				}, null, "删除过程中发生异常！");
			}, "JSON");
		}
	})
}
