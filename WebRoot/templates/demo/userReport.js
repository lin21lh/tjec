
var baseUrl = contextpath + 'base/report/userReportConrtoller/';
var urls = {
	query : baseUrl + 'query.do'
};

var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : '#datagrid_log', // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

function OnReady() {
	var height = $(document.body).height() - 60;
	
	AF.height = height;

	AF.func('Build', contextpath + 'component/supcan/templates/demo/userReportSimple.xml');
	AF.func('SetSource', urls.query);
	
	AF.func("CallFunc", "301 \r\n 3"); //打开工具箱
	
	AF.func("calc", "mode=asynch;range=full");
}

function OnEvent(id, Event, p1, p2, p3, p4) { 
}