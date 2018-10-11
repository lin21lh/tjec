function doQuery(){
	//获取已选择的用户
	var selRows = $("#centraltab").find("#rgrid").datagrid('getData').rows;
	var znkids="";
	for(var j=0;j<selRows.length;j++){	
		var selRow = selRows[j];
		if(j !=0){
			znkids += ",";
		}
		znkids += selRow.zbkid;
	}
	//查询参数获取
	var param = {
			zbmc : $("#centraltab").find("#searchVal").val(),
			zbkid: znkids
		};	
	$("#centraltab").find("#lgrid").datagrid("load", param);
}
