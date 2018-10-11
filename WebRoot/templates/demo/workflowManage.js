var wfselkey = '';
$(function() {
	initSearchbox('#wfsel');
	initGrid();

});

function initSearchbox(boxid) {
	$(boxid).searchbox({
		searcher : function(value, name) {
			showWFSelectDialog();
		}
	});
}
function initGrid() {

	$('#list')
			.datagrid(
					{
						url : '../../workflow/WfProcessInstanceController/queryWorkflowInstances.do',
						fit : true,
						pagination : false,
						singleSelect : true,
						columns : [ [ {
							field : 'ck',
							checkbox : 'true'
						}, {
							field : 'id',
							title : '流程实例ID',
							width : 100
						}, {
							field : 'name',
							title : '节点名称',
							width : 100
						}] ]
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

	var variables = "paramA:" + paramA + ";paramB" + paramB;

	var url = contextpath
			+ 'workflow/WfProcessInstanceController/startProcessByKeyAndPush.do';
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
						url : '../../workflow/WfProcessInstanceController/queryWorkflowInstances.do'
								+ '?key=' + wfselkey
					});
}