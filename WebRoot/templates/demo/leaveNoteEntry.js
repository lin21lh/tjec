$(function() {
	initGrid();
});

function initGrid() {
	$('#list').datagrid({
		url : '../../demo/DemoWorkflowManageController/leaveNoteQuery.do',
		fit : true,
		pagination : false,
		singleSelect : true,
		columns : [ [ {
			field : 'ck',
			checkbox : 'true'
		}, {
			field : 'uuid',
			title : 'uuid',
			width : 100
		}, {
			field : 'content',
			title : '内容',
			width : 100
		}, {

			title : '申请人',
			field : 'applyer',
			width : 100
		}, {
			field : 'wfid',
			title : '工作流id',
			width : 100
		}, {
			field : 'wfnode',
			title : '节点名称',
			width : 100
		}, {
			field : 'crdate',
			title : '创建日期',
			width : 100
		} ] ]
	});
}

function startwf() {

	var url = '../../demo/DemoWorkflowManageController/startLeaveWorkflow.do';

	var sel = $('#list').datagrid('getSelected');
	$.post(url, {
		leaveid : sel.uuid
	}, function(msg) {
		easyui_info(msg.title);
	}, "JSON");

}

function workflowImageView() {
	var sel = $('#list').datagrid('getSelected');

	var url = contextpath
			+ "workflow/WfProcessDefinitionController/getWorkflowImage.do?instanceid="
			+ sel.wfid + '&serialNo=' + Math.random();
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

	$.post(contextpath
			+ "demo/DemoWorkflowManageController/completeTaskByExecId.do", {
		execId : sel.wfid,
		outcome : $('#outcome').val(),
		vars : '',
		opinion : $('#opinion').val()
	}, function(msg) {
		easyui_info(msg.title, function() {
			initGrid();
		});
	}, 'JSON');

}

function query1() {

	var key = $('#wfkey1').val();
	var actiid = $('#actiid1').val();
	$.post(contextpath
			+ "demo/DemoWorkflowManageController/getUserTodoExecidsByWfKey.do", {
		wfkey : key,
		activityId : actiid
	}, function(msg) {
		$('#result').val(msg.wfids);
	}, 'JSON');
}
function query2() {
	var key = $('#wfkey1').val();
	var actiid = $('#actiid1').val();
	$.post(contextpath
			+ "demo/DemoWorkflowManageController/getUserHistoryExecidsByWfKey.do", {
		wfkey : key,
		activityId : actiid
	}, function(msg) {
		$('#result').val(msg.wfids);
	}, 'JSON');
}