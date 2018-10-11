var show_xml = contextpath+"/templates/aros/tjfx/xml/newIC.xml";

$(function(){	
	initDate();
	initVars();
});
/**
 * url对象
 */
var urls = {
		query: "/aros/tjfx/controller/queryList.do"
	

};

var panel_ctl_handles;

/**
 *  定义初始变量，便于后期维护使用。
 */
function initVars(){
	/*
	 * 定义折叠面板的选项
	 */
	panel_ctl_handles=[{
		panelname:'#qpanel',		//要折叠的面板ID
		buttonid:'#openclose',//折叠按钮ID
		gridname:'#tList'				//刷新的列表ID
	}];	
}

/**
 * 时间初始化
 */
function initDate(){
	
}

function OnReady(id) {
	AF.func("Build", show_xml);		
	AF.func("SeparateView", "2\r\n4 "); //分屏冻结
	AF.func("SubscribeEvent", "SelChanged,EditChanged,DblClicked,Clicked");//预订事件
	AF.func("addEditAbleOnly", "droplist; checkbox;"); 
	AF.func("Calc", "");

	doQuery();
}

/**
 *  查询按钮-响应事件处理函数
 */
function doQuery(){
	var tempRan=Math.floor(Math.random()*10000000+1);
	
	var q_url = contextpath+urls.query+"?r_time="+tempRan;	
	q_url="http://localhost:8080/aros/aros/tjfx/controller/queryList.do?r_time="+tempRan;	
	AF.func("setCookie", sessionId);
	AF.func("setSource", "ds1 \r\n "+q_url);
	AF.func("Calc", "");
}

function OnEvent(id, Event, p1, p2, p3, p4) {	
}


/**
 *  清空按钮-响应事件处理方法
 */
function doClear(){
	initDate();
	doQuery();
}

function doExport() {
	AF.func("CallFunc", "105\r\n");
}






