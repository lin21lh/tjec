var test_target = null;
function loadDataGrid(resourceid){
	test_target = $("#test_target").datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		singleSelect : true,//默认为false，是否选中单行
		rownumbers : true,//默认为false，是否显示序号列
		pagination : true, 
		view : bufferview,
		pageSize : 200,
		url : contextpath + 'test/target/targetController/query.do?resourceid=' + resourceid,
		loadMsg : "正在加载，请稍候......",
		frozenColumns : [[{
            	field : "billid",
            	checkbox : true
		}]], 
		idField : 'billid',
		columns : [[
             {
            	field : "billcode",
            	title : "指标编号",
            	width : 120,
            	sortable : true
            }, {
            	field : "paytypecode",
            	title : "支付编码",
            	width : 120
            }
		]]
	});
}