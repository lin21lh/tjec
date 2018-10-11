var urls = {
	add : '',
	edit : '',
	del : '',
	query : '../workflow/getUserTasks.do',
	historytaskquery : '../workflow/getUserHistoryTasks.do',
	wfstart : '',
	getBackWorkflow : '../workflow/getBackWorkflow.do'
};

var curOutcomes = null;
var queryMode = null;
$(function() {

	$('#grid').datagrid({
		url : urls.query,
		columns : [ [ {
			field : 'ck',
			checkbox : 'true'
		}, {
			field : 'execid',
			title : '流程实例ID',
			width : 100
		} , {
			field : 'id',
			title : '任务ID',
			width : 100
		},{
			field : 'name',
			title : '节点名称',
			width : 100
		}, {
			field : 'assignee',
			title : '受托人',
			width : 100
		}, {
			field : 'outcomes',
			title : '可选流程流向',
			width : 200
		} ] ],
		rownumbers : true,
		singleSelect : true,
		onSelect : function(rowIndex, rowData) {
			// 选中节点
			if (queryMode == '0') {
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
			} else if (queryMode == '1') {
				$('#getback').linkbutton('enable');
			}

		}
	});

	$('#status').combobox({
		textField : 'label',
		valueField : 'value',
		data : [ {
			label : '待处理',
			value : '0'
		}, {
			label : '已处理',
			value : '1'
		} ],
		onLoadSuccess : function() {
			$(this).combobox('select', '0');
			query();
		}
	});
});

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
function query() {

	$('#deal').linkbutton('disable');
	$('#sendback').linkbutton('disable');
	$('#getback').linkbutton('disable');
	var taskFlag = $('#status').combobox('getValue');

	queryMode = taskFlag;
	switch (taskFlag) {
	// 待处理
	case '0':
		$('#grid').datagrid({
			columns : [ [ {
				field : 'ck',
				checkbox : 'true'
			}, {
				field : 'execid',
				title : '流程实例ID',
				width : 100
			} , {
				field : 'id',
				title : '任务ID',
				width : 100
			},{
				field : 'name',
				title : '节点名称',
				width : 100
			}, {
				field : 'assignee',
				title : '受托人',
				width : 100
			}, {
				field : 'outcomes',
				title : '可选流程流向',
				width : 200
			} ] ],
			url : urls.query
		});

		break;
	// 已处理
	case '1':
		$('#grid').datagrid({
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
			url : urls.historytaskquery + '?key=leave'
		});

		break;
	}

}

function getSel() {
	return $('#grid').datagrid('getSelected');
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
	var sel = getSel();
	if (sel) {
		$.messager.confirm("确认", "是否确认撤回流程?", function(r) {
			if (r) {
				$.post(urls.getBackWorkflow, {
					execid:sel.execid,
					activityName:sel.activityName,
					variables:''
				}, function(msg) {
					easyui_auto_notice(msg,function(){
						query();
					},null,"撤回时发生异常!");
				}, "JSON");
			}
		});
	} else {
		easyui_warn("请选择一行数据!");
	}
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
			query();
		});
	}, "JSON");
}

function viewform() {
	var sel = getSel();
	var formid = sel.formid;

	$('#form_window').window('open');

	$('#fm1').form('load', '../test/leaveDetail.do?id=' + sel.formid);
}

function closeForm() {
	$('#form_window').window('close');
}
