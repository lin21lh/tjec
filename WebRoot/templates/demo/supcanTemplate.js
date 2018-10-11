
/**
 * 日志管理模块-js脚本
 */
var baseUrl = contextpath + 'sys/log/sysLogController/';
var urls = {
	queryLog : baseUrl + 'query.do',
	deleteLog : baseUrl + 'deleteLog.do'
};

var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : '#datagrid_log', // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

$(function() {
	
	comboboxFunc('opertype', 'SYS_OPERTYPE', 'code', 'name');
	$('#qpanel1').panel('close');
	
	$('.datagrid-pager > table').hide();
	
	var icons = {iconCls:"icon-clear"};
	$('#opertype').combobox('addClearBtn', icons);
});