var urls = {
	totaskquery : 'getUserTasks.do',
	candidatetaskquery : 'getUserCandidateTask.do',
	historytaskquery : 'takeTask.do',
	takeTask : 'takeTask.do',
	getBackWorkflow : '../workflow/getBackWorkflow.do'
};

// 缓存
var formHtmlMap = new Array();

var curOutcomes = null;
var queryMode = null;

$(function() {

	$('#todogrid').datagrid({
		url : urls.totaskquery,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'processName',
			title : '流程名称',
			width : 100
		}, {
			field : 'name',
			title : '任务节点',
			width : 100
		}, {
			field : 'outcomes',
			title : '可选流向',
			width : 200
		}, {
			field : 'id',
			title : '任务ID',
			width : 100
		}, {
			field : 'execid',
			title : '流程实例ID',
			width : 100
		} ] ],
		toolbar : '#todotoolbar',
		singleSelect : true,
		fit : true,
		onSelect : function(rowIndex, rowData) {
			// 选中节点

			var outcomes = rowData.outcomes;
			curOutcomes = diffOutcomes(outcomes);

			if (curOutcomes.forwardOutcomes.length > 0) {
				$('#deal').linkbutton('enable');

			} else {
				$('#deal').linkbutton('disable');
			}

			if (curOutcomes.backOutcomes.length > 0) {
				$('#sendback').linkbutton('enable');
			} else {
				$('#sendback').linkbutton('disable');
			}

		}
	});

	$('#candidategrid').datagrid({
		url : urls.candidatetaskquery,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'processName',
			title : '流程名称',
			width : 100
		}, {
			field : 'name',
			title : '任务节点',
			width : 100
		}, {
			field : 'id',
			title : '任务ID',
			width : 100
		}, {
			field : 'execid',
			title : '流程实例ID',
			width : 100
		} ] ],
		toolbar : '#candidatetoolbar',
		singleSelect : true,
		fit : true
	});

	$('#historygrid').datagrid({
		url : '../workflow/getUserHistoryTasks.do',
		columns : [ [ {
			field : 'ck',
			checkbox : 'true'
		}, {
			field : 'execid',
			title : '流程ID',
			width : 100
		}, {
			field : 'activityName',
			title : '任务名称',
			width : 100
		}, {
			field : 'endTime',
			title : '处理时间',
			width : 200
		}, {
			field : 'outcome',
			title : '处理',
			width : 100
		} ] ],
		toolbar : '#historytoolbar',
		singleSelect : true,
		fit : true
	});
});

function todoquery() {
	$('#todogrid').datagrid({
		url : urls.totaskquery
	});
}

function cadidatequery() {
	$('#candidategrid').datagrid({
		url : urls.candidatetaskquery
	});
}

function takeTask() {
	var sel = $('#candidategrid').datagrid('getSelected');

	if (sel) {
		var id = sel.id;
		$.messager.confirm('确认', '是否接受该任务?', function(r) {
			if (r) {
				$.post(urls.takeTask, {
					taskid : id
				}, function(msg) {
					easyui_auto_notice(msg, function() {
						cadidatequery();
						todoquery();
					}, null, '接受任务时发生异常!');
				}, "JSON");
			}
		});
	} else {
		easyui_warn('请选择一个候选任务!');
	}
}

var sel_proc_key = null;
var sel_proc_version = null;
function showProcessSelectDialog() {
	parent.$
			.fastModalDialog({
				title : '流程选择',
				width : 450,
				height : 400,
				dialogID:'psdlg',
				iconCls : 'icon-search',
				html : '<div><div style="width:100%;height:290px"><ul id="tree_9850" ></ul></div><div style="text-align:center" >版本选择：<input type="text" id="input_8290" /></div></div>',
				onOpen : function() {

					var input_ = parent.$.fastModalDialog.handler['psdlg']
							.find('#input_8290');
					input_.combobox({});

					var tree_ = parent.$.fastModalDialog.handler['psdlg']
							.find('#tree_9850');
					tree_
							.tree({
								width : 440,
								height : 370,
								url : 'workflow/queryWorkflowTree.do?showVersion=false',
								fit : true,
								onSelect : function(node) {
									var key = node.key;
									if (key) {
										showVersions(key, input_);
									}
								}
							});

				},
				buttons : [
						{
							text : "下一步",
							iconCls : "icon-ok",
							handler : function() {
								var sel = parent.$.fastModalDialog.handler['psdlg']
										.find('#tree_9850').tree('getSelected');

								if (sel.type != 'workflow') {
									easyui_warn('请选择一个流程节点!');
									return;
								}
								sel_proc_key = sel.key;
								var input_ = parent.$.fastModalDialog.handler['psdlg']
										.find('#input_8290');

								sel_proc_version = input_.combobox('getValue');
								parent.$.fastModalDialog.handler['psdlg']
										.dialog('close');
								setTimeout(displayFormForAdd, 0);
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['psdlg']
										.dialog('close');
							}
						} ]
			});

}

function displayFormForAdd() {
	getFormHtml(showDynamicFormDialogForAdd, sel_proc_key);
}

function getFormHtml(callBack, key) {

	if (formHtmlMap[key]) {
		callBack(formHtmlMap[key]);
	} else {
		// 取得流程对应的表
		if (key) {
			$.post('getWorkflowTableFormByKey.do', {
				key : key
			}, function(msg) {
				formHtmlMap[key] = msg;
				callBack(msg);
			});
		} else {
			easyui_warn('key is null');
		}
	}
}

function displayFormForView() {
	var sel = getSel();
	if (sel) {
		var s = sel.execid.split(".");
		getFormHtml(showDynamicFormDialogForView, s[0]);
	}

}

/**
 * 创建form表单
 * 
 * @param formHtml
 */
function showDynamicFormDialogForAdd(formHtml) {
	parent.$.fastModalDialog({
		title : '表单填写',
		width : 450,
		height : 400,
		dialogID:'dfdlg',
		iconCls : 'icon-search',
		html : '<div><form id="form_8926"  method="post"><table class="list">'
				+ formHtml + '</table></form></div>',
		onOpen : function() {

			var databoxes = parent.$.fastModalDialog.handler['dfdlg']
					.find('.easyui-datebox')
			databoxes.datebox({});
		},
		buttons : [
				{
					text : "完成",
					iconCls : "icon-ok",
					handler : function() {
						var form_ = parent.$.fastModalDialog.handler['dfdlg']
								.find('#form_8926');
						startNewWorkFlowWithForm(form_);

					}
				}, {
					text : "取消",
					iconCls : "icon-cancel",
					handler : function() {
						parent.$.fastModalDialog.handler['dfdlg'].dialog('close');
					}
				} ]
	});
}

function showDynamicFormDialogForView(formHtml) {
	parent.$.fastModalDialog({
		title : '表单查看',
		width : 450,
		height : 400,
		dialogID:'vdlg',
		iconCls : 'icon-search',
		html : '<div><form id="form_8926"  method="post"><table class="list">'
				+ formHtml + '</table></form></div>',
		onOpen : function() {
			// 加载数据
			var form_ = parent.$.fastModalDialog.handler['vdlg'].find('#form_8926');
			var sel = getSel();
			if (sel) {

				form_.form("load", "workflow/getWorkflowFormData.do?execid="
						+ sel.execid + '&formid=' + sel.formid);
				var databoxes = parent.$.fastModalDialog.handler['vdlg']
						.find('.easyui-datebox')
				databoxes.datebox({});
			}

		},
		buttons : [
				{
					text : "保存",
					iconCls : "icon-save",
					handler : function() {
						var form_ = parent.$.fastModalDialog.handler['vdlg']
								.find('#form_8926');
						saveForm(form_);

					}
				}, {
					text : "关闭",
					iconCls : "icon-cancel",
					handler : function() {
						parent.$.fastModalDialog.handler['vdlg'].dialog('close');
					}
				} ]
	});
}

function saveForm(form) {
	var sel = getSel();

	var url = 'workflow/saveWorkflowFormData.do?_FORMID_=' + sel.formid
			+ '&_EXECID_=' + sel.execid;

	form.form('submit', {
		url : url,
		onSubmit : function() {
			return true;
		},
		success : function(data) {
			data = eval("(" + data + ")");
			easyui_auto_notice(data, function() {
				parent.$.fastModalDialog.handler.dialog('close');
			})
		}
	});
}
// 保存表单并启动流程
function startNewWorkFlowWithForm(dataForm) {
	// 生成url

	var url = 'workflow/startNewWorkFlowWithForm.do?WF_KEY_=' + sel_proc_key
			+ '&WF_VERSION_=' + sel_proc_version;

	dataForm.form('submit', {
		url : url,
		onSubmit : function() {
			return true;
		},
		success : function(data) {
			data = eval("(" + data + ")");
			easyui_auto_notice(data, function() {
				parent.$.fastModalDialog.handler.dialog('close');
				todoquery();
				cadidatequery();
				hisQuery();
			})
		}
	});
}
/**
 * 显示最新版本
 * 
 * @param key
 * @param combobox
 */
function showVersions(key, combobox) {
	combobox.combobox({
		url : 'workflow/queryWorkflowVersionsByKey.do?key=' + key,
		valueField : 'version',
		textField : 'version',
		onLoadSuccess : function() {
			var data = combobox.combobox('getData');
			var max = 0;
			for ( var i in data) {
				if (+data[i].version > max) {
					max = +data[i].version;
				}
			}
			if (max != 0) {
				combobox.combobox('select', max);
			}
		}
	});
}

function getSel() {
	return $('#todogrid').datagrid('getSelected');
}
function viewchart() {
	var sel = getSel();
	var execid = sel.execid;

	var url = "../workflow/getWorkflowImage.do?instanceid=" + execid;
	$('#img_window').window('open');
	var ele = $('#img_loc');
	ele[0].src = url;
}

/**
 * 处理
 */
function deal(flag) {
	var outcomeArray;
	if (flag == 'b') {
		outcomeArray = curOutcomes.backOutcomes;
	} else {
		outcomeArray = curOutcomes.forwardOutcomes;
	}
	var model = new Array();
	for ( var i in outcomeArray) {
		var value = outcomeArray[i];
		var row = {
			id : value,
			text : value
		};
		model.push(row);
	}
	$('#outcome_widget').combobox({
		valueField : 'id',
		textField : 'text',
		data : model,
		onLoadSuccess : function() {
			var data = $(this).combobox('options').data;
			if (data.length > 0) {
				$(this).combobox('select', data[0].id);
			}
		}
	});
	$('#process_window').window('open');
}

/**
 * 撤回
 */
function getback() {

	var sel = getHisSel();
	if (sel) {
		$.messager.confirm("确认", "是否确认撤回流程?", function(r) {
			if (r) {

				$.post(urls.getBackWorkflow, {
					execid : sel.execid,
					activityName : sel.activityName,
					variables : ''
				}, function(msg) {
					easyui_auto_notice(msg, function() {
						hisQuery();
						cadidatequery();
						todoquery();
					}, null, "撤回时发生异常!");
				}, "JSON");
			}
		});
	} else {
		easyui_warn("请选择一行数据!");
	}
}

function diffOutcomes(outcomes) {
	var outcomeObj = {};
	outcomeObj.forwardOutcomes = new Array();
	outcomeObj.backOutcomes = new Array();

	if (outcomes != undefined && outcomes != null && outcomes.trim().length > 0) {
		outcomes = outcomes.replace("[", "");
		outcomes = outcomes.replace("]", "");
		var oc = outcomes.split(",");

		for ( var i in oc) {
			oc[i] = oc[i].trim();
			if (oc[i].indexOf("退回") == 0) {
				outcomeObj.backOutcomes.push(oc[i]);
			} else {
				outcomeObj.forwardOutcomes.push(oc[i])
			}
		}
	}
	return outcomeObj;
}

function btncancel() {

	$('#process_window').window('close');
}
function btnok() {
	var sel = getSel();
	$.post("../workflow/completeTaskWithVariables.do", {
		taskid : sel.id,
		outcome : $('#outcome_widget').combobox('getValue'),
		variables : 'opinion:' + $('#opinion').val()
	}, function(r) {
		easyui_auto_notice(r, function() {
			$('#process_window').window('close');
			todoquery();
			cadidatequery();
			hisQuery();
		});
	}, "JSON");
}

function getHisSel() {
	return $('#historygrid').datagrid('getSelected');
}

function hisQuery() {

	$('#historygrid').datagrid({
		columns : [ [ {
			field : 'ck',
			checkbox : 'true'
		}, {
			field : 'execid',
			title : '流程ID',
			width : 100
		}, {
			field : 'activityName',
			title : '任务名称',
			width : 100
		}, {
			field : 'endTime',
			title : '处理时间',
			width : 200
		}, {
			field : 'outcome',
			title : '处理',
			width : 100
		} ] ],
		url : '../workflow/getUserHistoryTasks.do'
	});
}

function viewchart_his() {
	var sel = getHisSel();
	var execid = sel.execid;

	var url = "../workflow/getWorkflowImage.do?instanceid=" + execid;
	$('#img_window').window('open');
	var ele = $('#img_loc');
	ele[0].src = url;
}