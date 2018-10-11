var urls = {
	add : '',
	edit : '',
	del : '',
	query : 'leaveQuery.do',
	wfstart : ''
};

$(function() {
	$('#grid').datagrid({
		url : '',
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'id',
			title : 'ID ',
			width : 100
		}, {
			field : 'applyer',
			title : '发起人',
			width : 100
		}, {
			field : 'reason',
			title : '原因',
			width : 200
		}, {
			field : 'opinion',
			title : '审核意见',
			width : 100
		}, {
			field : 'bkopinion',
			title : '退回意见',
			width : 100
		}, {
			field : 'startdate',
			title : '发起日期',
			width : 100
		}, {
			field : 'auditdate',
			title : '审核日期',
			width : 100
		}, {
			field : 'status',
			title : '状态',
			width : 100
		} ] ],
		toolbar : '#tb0',
		singleSelect : true
	});

	query();
});

function query() {
	$('#grid').datagrid({
		url : urls.query
	});
}

function add() {

	query();
}

function edit() {
	query();
}

function del() {

	query();
}

function wfstart() {
	var sel = $('#grid').datagrid('getSelected');
	WfUtil.startProcess('leave', 'formid:' + sel.id, easyui_auto_notice);
}

/*******************************************************************************
 * 流程公共函数 测试版本
 ******************************************************************************/

var WfUtil = {
	startProcess : function(key, variables, callbackFunc) {
		$.post("../workflow/startProcessByKeyAndPush.do", {
			pdefkey : key,
			variables : variables
		}, function(result) {
			callbackFunc(result);
		}, 'JSON');
	}

}
