$(function(){
	
	
	$('#mlist').datagrid({
		fit : true,
		stripe : true, 
		singleSelect : true,
		rownumbers : true,
		pagination : true, 
		pageNumber : 1,
		pageSize : 10,
		pageList : [10,20,30,40,50],
		url : '',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar",
		columns : [[
            {
            	field : "ck",
            	checkbox : true
            },
            {
            	field : "formcode",
            	title : "表单编码",
            	width : 120
            },
            {
            	field : "formname",
            	title : "表单名称",
            	width : 200
            }
            ,
            {
            	field : "tabname",
            	title : "业务表名",
            	width : 200
            }
		]]
	});
});
