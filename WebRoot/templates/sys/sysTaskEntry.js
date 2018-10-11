
/**
 * 日志管理模块-js脚本
 */
var baseUrl = contextpath + 'sys/log/sysTaskController/';
var urls = {
	queryTask : baseUrl + 'query.do',
	pauseTask : baseUrl + 'pauseTask.do',
	resumeTask : baseUrl + 'resumeTask.do',
	removeTask : baseUrl + 'removeTask.do'
};

var taskDataGrid;
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : '#datagrid_task', // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

$(function() {
	
	$('#qpanel1').panel('close');
	loadLogDataGrid(urls.queryTask);
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(taskDataGrid);
	
	var icons = {iconCls:"icon-clear"};
	$('#opertype').combobox('addClearBtn', icons);
});

function loadLogDataGrid(url) {

	taskDataGrid = $('#datagrid_task').datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		url : url,
		queryParams : {
			group : ''
		},
		loadMsg : '正在加载，请稍候......',
		toolbar : '#toolbar_task',
		columns : [ [ {
			field : 'taskid',
			checkbox : true
		}, {
			field : 'group',
			title : '定时任务模块',
			width : 120,
			sortable:true
		}, {
			field : 'name',
			title : '定时任务名称',
			width : 120,
			sortable:true
		}, {
			field : "description",
			title : "定时任务描述",
			width : 100,
			sortable:true
		}, {
			field : 'previousFireTime',
			title : '上次执行时间',
			width : 120,
			sortable:true
		}, {
			field : 'nextFireTime',
			title : '下次执行时间',
			width : 120,
			sortable:true
		}, {
			field : 'triggerStateCn',
			title : '状态',
			width : 100
		}, {
			field : 'startTime',
			title : '开始时间',
			width : 120,
			sortable:true
		}, {
			field : 'endTime',
			title : '结束时间',
			width : 100
		}, {
			field : 'triggerType',
			title : 'Trigger类型',
			width : 100
		} ] ],
		onSelect : function(rowIndex, rowData) {
			
		}
	});

}

//查询
function queryTask() {
	taskDataGrid.datagrid('load', {
		group : $('#group').textbox('getValue')
	});
}

//删除日志
function pauseTask() {
	var row = taskDataGrid.datagrid("getSelections");
	if (row.length == 0) {
		easyui_warn("请选择一行数据！");	
		return;
	}
	
	parent.$.messager.confirm("确认暂停", "是否要暂停选中定时任务？", function(r) {
		if (r) {
			row = taskDataGrid.datagrid("getSelected");

			$.post(urls.pauseTask, {
				name : row.name,
				group : row.group
			}, function(result) {
				easyui_auto_notice(result, function() {
					taskDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});

}

//删除日志
function resumeTask() {
	var rows = taskDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		easyui_warn("请选择一行数据！");	
		return;
	}
	
	parent.$.messager.confirm("确认恢复", "是否要恢复选中定时任务？", function(r) {
		if (r) {
			row = taskDataGrid.datagrid("getSelected");

			$.post(urls.resumeTask, {
				name : row.name,
				group : row.group
			}, function(result) {
				easyui_auto_notice(result, function() {
					taskDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});

}

//删除日志
function removeTask() {
	var rows = taskDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		easyui_warn("请选择一行数据！");	
		return;
	}
	
	parent.$.messager.confirm("确认删除", "是否要删除选中定时任务？", function(r) {
		if (r) {
			row = taskDataGrid.datagrid("getSelected");

			$.post(urls.removeTask, {
				name : row.name,
				group : row.group
			}, function(result) {
				easyui_auto_notice(result, function() {
					taskDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});

}