function procDeploy() {

	$.post('testWFDeploy.do?filename=' + $('#filename').val(), {},
			function(msg) {
				easyui_auto_notice(msg, function() {

				});
			}, "JSON");
}

function deployQuery() {
	$('#dpgrid').datagrid({
		url : 'deployQuery.do',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'id',
			title : 'deploymentId ',
			width : 100
		}, {
			field : 'dbid',
			title : 'dbid',
			width : 100
		}, {
			field : 'name',
			title : 'name',
			width : 100
		}, {
			field : 'resourceNames',
			title : 'resourceNames',
			width : 400
		}, {
			field : 'state',
			title : 'state',
			width : 100
		} ] ],
		toolbar : '#tb0',
		singleSelect:true
	});

}

function viewJpdl() {
	var selected = $('#dpgrid').datagrid('getSelected');
	var id = selected.id;
	$.post('/ifmis/workflow/getDeployedJpdlContent.do?deploymentId=' + id, {},
			function(msg) {
	
				 $('#xml_window').window('open');
				 $('#jpdl').val(msg);
			});
}
function procDeployQuery() {

	$('#defgrid').datagrid({
		url : 'procDeployQuery.do',
		columns : [ [ {
			field : 'id',
			title : 'id',
			width : 100
		}, {
			field : 'deploymentId',
			title : 'deploymentId',
			width : 100
		}, {
			field : 'key',
			title : 'key',
			width : 100
		}, {
			field : 'version',
			title : 'version',
			width : 100
		}, {
			field : 'name',
			title : 'name',
			width : 100
		}, {
			field : 'imageResourceName',
			title : 'imageResourceName',
			width : 200
		}, {
			field : 'description',
			title : 'description',
			width : 100
		} ] ]
	});
}

function procInstanceQuery() {

	$('#instgrid').datagrid({
		url : 'procInstanceQuery.do',
		columns : [ [ {
			field : 'id',
			title : '流程实例ID',
			width : 100
		}, {
			field : 'dbid',
			title : 'dbid',
			width : 100
		}, {
			field : 'key',
			title : 'key',
			width : 100
		}, {
			field : 'activitiName',
			title : 'activitiName',
			width : 100
		}, {
			field : 'state',
			title : 'state',
			width : 100
		} ] ]
	});
}
/**
 * 任务查询
 * 
 * @returns
 */
function taskQuery() {
	$('#taskgrid').datagrid({
		url : 'taskQuery.do',
		columns : [ [ {
			field : 'id',
			title : 'id',
			width : 100
		}, {
			field : 'name',
			title : 'name',
			width : 100
		}, {
			field : 'assignee',
			title : 'assignee',
			width : 100
		}, {
			field : 'activityName',
			title : 'activityName',
			width : 100
		}, {
			field : 'execid',
			title : '流程实例ID',
			width : 100
		}, {
			field : 'outcomes',
			title : 'outcomes',
			width : 600
		} ] ]
	});
}

function candidateTaskQuery(){
	$('#ctaskgrid').datagrid({
		url : 'candidateTaskQuery.do',
		columns : [ [ {
			field : 'id',
			title : 'id',
			width : 100
		}, {
			field : 'name',
			title : 'name',
			width : 100
		}, {
			field : 'assignee',
			title : 'assignee',
			width : 100
		}, {
			field : 'activityName',
			title : 'activityName',
			width : 100
		}, {
			field : 'execid',
			title : '流程实例ID',
			width : 100
		}, {
			field : 'outcomes',
			title : 'outcomes',
			width : 600
		} ] ]
	});
	
}
function taskComplete() {
	$.post('taskComplete.do?taskid=' + enc($('#taskid').val()) + '&outcome='
			+ enc($('#outcome').val()), {}, function(msg) {
		easyui_auto_notice(msg, function() {

		});
	}, "JSON");

}

function enc(s) {
	return encodeURIComponent(encodeURIComponent(s));
}

function startProcessByKey() {
	$.post('startProcessByKey.do', {
		pdefkey : $('#pdefkey').val(),
		pkey : $('#pkey').val(),
		variables : $('#var').val()
	}, function(msg) {
		easyui_auto_notice(msg, function() {

		});
	}, "JSON");
}

function workflowImageView() {
	var url = "../workflow/getWorkflowImage.do?instanceid="
			+ $('#instid_for_imageview').val();
	$('#img_window').window('open');
	var ele = $('#img_loc');
	ele[0].src = url;
}

function hisTaskQuery() {
	$('#histaskgrid').datagrid({
		url : 'hisTaskQuery.do?assignee=' + $('#his_assignee').val(),
		columns : [ [ {
			field : 'id',
			title : 'id',
			width : 100
		}, {
			field : 'executionId',
			title : 'executionId',
			width : 100
		}, {
			field : 'assignee',
			title : 'assignee',
			width : 100
		}, {
			field : 'state',
			title : 'state',
			width : 100
		}, {
			field : 'execid',
			title : '流程实例ID',
			width : 100
		}, {
			field : 'endtime',
			title : 'endtime',
			width : 150
		}, {
			field : 'outcome',
			title : 'outcomes',
			width : 600
		} ] ]
	});

}
function getBackTask() {
	$.post('getBackTask.do', {
		histaskid : $('#getback_taskid').val()
	}, function(msg) {
		easyui_auto_notice(msg, function() {

		});
	}, "JSON");
}