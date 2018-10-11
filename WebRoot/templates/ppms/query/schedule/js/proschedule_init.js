
var baseUrl = contextpath + "query/controller/ProjectScheduleController/";
//路径
var urls = {
	querySchedule : baseUrl + "querySchedule.do"
};


//默认加载
$(function() {
	comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name");//项目回报机制
	comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name");//项目发起类型
	$("#proTrade").treeDialog({
		title :'选择所属行业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'proTrade',
		prompt: "请选择所属行业",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "PROTRADE",
		filters:{
			code: "行业代码",
			name: "行业名称"
		}
	});
	$("#proReturn").combobox("addClearBtn", {iconCls:"icon-clear"});
	loadProjectGrid(urls.querySchedule);
});
var projectDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadProjectGrid(url) {
	projectDataGrid = $("#projectDataGrid").datagrid({
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
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "checkid",checkbox : true}  
			          ,{field : "projectid",title : "主键",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "proName",title : "项目名称",halign : 'center',width:200,sortable:true}
			          ,{field : "xmsb",title : "项目识别",halign : 'center',width:100,sortable:true,
			        	  styler: function(value,row,index){
							if(row.xmdqhj > 1){
								return 'background-color:#2EF217;';
							} else if (row.xmdqhj == 1 ){
								return 'background-color:yellow;';
							}
						}
			          }
			          ,{field : "xmzb",title : "项目准备",halign : 'center',width:100,sortable:true,
			        	  styler: function(value,row,index){
							if(row.xmdqhj > 2){
								return 'background-color:#2EF217;';
							} else if (row.xmdqhj == 2 ){
								return 'background-color:yellow;';
							}
						}
			          }
			          ,{field : "xmcg",title : "项目采购",halign : 'center',width:100,sortable:true,
			        	  styler: function(value,row,index){
							if(row.xmdqhj > 3){
								return 'background-color:#2EF217;';
							} else if (row.xmdqhj == 3 ){
								return 'background-color:yellow;';
							}
						}
			          }
			          ,{field : "xmzx",title : "项目执行",halign : 'center',width:100,sortable:true,
			        	  styler: function(value,row,index){
							if(row.xmdqhj > 4){
								return 'background-color:#2EF217;';
							} else if (row.xmdqhj == 4 ){
								return 'background-color:yellow;';
							}
						}
			          }
			          ,{field : "xmyj",title : "项目移交",halign : 'center',width:100,sortable:true,
			        	  styler: function(value,row,index){
							if(row.xmdqhj > 5){
								return 'background-color:#2EF217;';
							} else if (row.xmdqhj == 5 ){
								return 'background-color:yellow;';
							}
						}
			          }
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var param = {
			proName : $("#proName").val(),
			proPerson : $("#proPerson").val(),
			proTrade :  $("#proTrade").val(),
			proPerate : $("#proPerate").combobox('getValues').join(","),
			proReturn : $("#proReturn").combobox('getValues').join(","),
			proSendtype : $("#proSendtype").combobox('getValues').join(","),
			proType : $("#proType").combobox('getValues').join(",")
		};
	
	projectDataGrid.datagrid("load", param);
}
