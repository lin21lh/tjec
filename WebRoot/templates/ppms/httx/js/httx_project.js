var baseUrl = contextpath + "ppms/httx/HttxController/";
var urls = {
	queryUrl : baseUrl + "queryHttxProject.do"
	
};

var httxProjectDataGrid;

/*合同体系项目查询初始化*/
$(function(){
	
	loadHttxProjectGrid(urls.queryUrl);
});

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadHttxProjectGrid(url) {
	httxProjectDataGrid = $("#httxProjectDataGrid").datagrid({
		fit : true,
		striped : false,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			
		},
		url : urls.queryUrl,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "checkbox",checkbox : true}  
        ,{field : "projectid",title : "主键",halign : 'center',width:190,sortable:true,hidden:true}
        ,{field : "proName",title : "项目名称",halign : 'center',width:220,sortable:true}
        ,{field : "proTypeName",title : "项目类型",halign : 'center',width:190,sortable:true}
        ,{field : "organName",title : "实施机构",halign : 'center',width:220,sortable:true}
        ,{field : "amount",title : "项目总投资（万元）",halign : 'center',width:190,sortable:true}
        
       ] ]
	});
}

/**
 * 查询
 */
function httxProjectQuery(){
	var param = {
			proName : $("#proName").val(),
			
		};
	httxProjectDataGrid.datagrid("load", param);
}
