
/**
 * 日志管理模块-js脚本
 */
var baseUrl = contextpath + 'sys/log/sysLogController/';
var urls = {
	queryLog : baseUrl + 'query.do',
	deleteLog : baseUrl + 'deleteLog.do'
};

var logDataGrid;
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : '#datagrid_log', // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

$(function() {
	
	comboboxFunc('opertype', 'SYS_OPERTYPE', 'code', 'name');
	$('#qpanel1').panel('close');
	loadLogDataGrid(urls.queryLog);
	
	//获取页面分页对象 针对于下拉刷新的datagrid只显示刷新按钮，提示信息显示格式'共{total}条'
	refDropdownPager(logDataGrid);
	
	var icons = {iconCls:"icon-clear"};
	$('#opertype').combobox('addClearBtn', icons);
	$('#starttime').combobox('addClearBtn', icons);
	$('#endtime').combobox('addClearBtn', icons);
	
});

function loadLogDataGrid(url) {

	logDataGrid = $('#datagrid_log').datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		view : scrollview,
		pageSize : 30,
		url : url,
		queryParams : {
			opertype : $('#opertype').combobox('getValue'),
			starttime : $('#starttime').datetimebox('getValue'),
			endtime : $('#endtime').datetimebox('getValue')
		},
		loadMsg : '正在加载，请稍候......',
		toolbar : '#toolbar_log',
		columns : [ [ {
			field : 'logid',
			checkbox : true
		}, {
			field : 'usercode',
			halign : 'center',
			title : '用户编码',
			hidden : true
		}, {
			field : 'username',
			halign : 'center',
			title : '用户名称',
			width : 120,
			sortable:true
		}, {
			field : "ip",
			halign : 'center',
			title : "IP地址",
			width : 100,
			sortable:true
		}, {
			field : 'opertypename',
			halign : 'center',
			title : '操作类型',
			width : 100,
			sortable:true
		},
		{
			field : 'opermessage',
			halign : 'center',
			title : '操作信息',
			width : 180,
			sortable:true
		}, {
			field : 'opertime',
			halign : 'center',
			title : '操作时间',
			width : 120,
			sortable:true
		}, {
			field : 'opersystem',
			halign : 'center',
			title : '操作系统',
			width : 100
		}, {
			field : 'operbrower',
			halign : 'center',
			title : '操作浏览器',
			width : 100
		} ] ]
	});
	datagrid_doCellTip(logDataGrid);
}

function datagrid_doCellTip(datagrid) {
	datagrid.datagrid('doCellTip', {
		specialShowFields : [],
		onlyShowInterrupt : true, //是否只有在文字被截断时才显示tip，默认值为false，即所有单元格都显示tip。
		position:'bottom',
		tipStyler : { maxWidth:'500px', boxShadow:'1px 1px 3px #292929'},   //'backgroundColor':'#fff000', borderColor:'#ff0000',
        contentStyler : { paddingLeft:'5px'}   //backgroundColor:'#333',
	});
}

//查询
function queryLog() {

	logDataGrid.datagrid('load', {
		opertype : $('#opertype').combobox('getValue'),
		starttime : $('#starttime').datetimebox('getValue'),
		endtime : $('#endtime').datetimebox('getValue')
	});
}

//删除日志
function deleteLog() {
	var rows = logDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		easyui_warn("请选择一行数据！");	
		return;
	}
	
	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var logids = '';
			rows = logDataGrid.datagrid("getSelections");
			for (var i=0; i<rows.length; i++) {
				if (logids.length > 0)
					logids += ',';
				logids += rows[i].logid;
			}
			$.post(urls.deleteLog, {
				logids : logids
			}, function(result) {
				easyui_auto_notice(result, function() {
					logDataGrid.datagrid("reload");
					datagrid_doCellTip(logDataGrid);
				});
			}, "json");
		}
	});

}