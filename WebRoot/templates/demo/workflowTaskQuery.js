var wfselkey = '';
$(function() {

	initGrid();

});

function initGrid() {

	$('#list')
			.datagrid(
					{
						url : '../../workflow/WfProcessInstanceController/getUserTasks.do?key='
								+ $('#key').val()
								+ "&actiid="
								+ $('#actiid').val(),
						fit : true,
						pagination : false,
						singleSelect : true,
						onLoadSuccess : function() {
							$('#list').datagrid('clearSelections');
						},
						columns : [ [ {
							field : 'ck',
							checkbox : true
						}, {
							field : 'execid',
							title : '流程实例ID',
							width : 100
						}, {
							title : '节点名称',
							field : 'activityName',
							width : 100
						}, {
							title : '受托人',
							field : 'assignee',
							width : 100
						}, {
							title : '退回标志',
							field : 'backflag',
							width : 100
						}, {
							title : '是否候选任务',
							field : 'iscandidate',
							width : 100
						} ] ]
					});
}

function showWFSelectDialog() {

	parent.$
			.fastModalDialog({
				title : '数据表选择',
				width : 520,
				height : 440,
				iconCls : 'icon-search',
				html : "<div id='treediv' style='width:500px;height:400px'><ul id='treeul'></ul></div>",
				dialogID : 'ndlg',
				onOpen : function() {

					var tree_ = parent.$.fastModalDialog.handler['ndlg']
							.find('#treeul');
					tree_
							.tree({
								url : contextpath
										+ 'workflow/WfProcessDefinitionController/queryWorkflowConfigTree.do'
							});
				},
				buttons : [
						{
							text : "选择",
							iconCls : "icon-ok",
							handler : function() {
								var tree_ = parent.$.fastModalDialog.handler['ndlg']
										.find('#treeul');

								var node = tree_.tree('getSelected');
								$('#wfsel').searchbox('setValue', node.text);
								wfselkey = node.key;

								parent.$.fastModalDialog.handler['ndlg']
										.dialog('close');
							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.fastModalDialog.handler['ndlg']
										.dialog('close');
							}
						} ]
			});
}

// 启动流程
function startWf() {

	var paramA = $('#paramA').val();
	var paramB = $('#paramB').val();

	var variables = "paramA:" + paramA + ",paramB:" + paramB;

	var url = contextpath
			+ 'workflow/WfProcessInstanceController/startProcessByKeyAndPush.do';
	alert(url);
	$.post(url, {
		pdefkey : wfselkey,
		variables : variables
	}, function(msg) {
		easyui_info(msg.title, function() {
			query();
		}, function() {

		}, '流程启动时发生异常！');
	}, 'json');

}

function query() {
	$('#list')
			.datagrid(
					{
						url : contextpath
								+ '/workflow/WfProcessInstanceController/queryWorkflowInstances.do'
								+ '?key=' + wfselkey
					});
}

function workflowImageView() {
	var sel = $('#list').datagrid('getSelected');

	var url = contextpath
			+ "workflow/WfProcessDefinitionController/getWorkflowImage.do?instanceid="
			+ sel.execid + '&serialNo=' + Math.random();

	// var url = contextpath
	// +
	// "workflow/WfProcessDefinitionController/getWorkflowImage.do?instanceid=LEAVE.20112"
	// + '&serialNo=' + Math.random();
	//	
	$('#img_window').window({
		onOpen : function() {
			var ele = $('#img_loc');
			ele[0].src = url;
		}
	});
	$('#img_window').window('open');

}

function completeTask() {

	var sel = $('#list').datagrid('getSelected');

	$.post(
			contextpath
					+ "workflow/WfProcessInstanceController/completeTask.do", {
				execId : sel.execid,
				outcome : $('#outcome').val(),
				actiId:sel.activityName
			}, function(msg) {
				easyui_info(msg.title, function() {
					initGrid();
				});
			}, 'JSON');

}
// 撤回流程
function getback() {
	var sel = $('#list').datagrid('getSelected');
	var backNode = $('#actiid').val();
	if (backNode) {
		$.post(contextpath
				+ "workflow/WfProcessInstanceController/getBackWorkflow.do", {
			execid : sel.execid,
			actiId : backNode,
			variables : ''

		}, function(msg) {
			easyui_info(msg.title, function() {
				//initGrid();
			});
		}, 'JSON');
	} else {
		easyui_warn("请填入要撤回的目标节点ID！");
	}

}

function workflowDebugger() {
	$.post(contextpath
			+ "workflow/WfProcessInstanceController/workflowComponentTest.do",
			{}, function(msg) {
				easyui_info(msg.title);
			}, 'JSON');
}

function workflowDebugger2() {
	$.post(contextpath
			+ "workflow/WfProcessInstanceController/workflowComponentTest2.do",
			{}, function(msg) {
				easyui_info(msg.title);
			}, 'JSON');
}