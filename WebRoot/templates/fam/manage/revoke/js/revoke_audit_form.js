//初始加载数据
$(function(){
	//var row = $.parseJSON(rowData.defaultValue);
	
	var row = $.parseJSON($('#rowData').val());
	$('#revoke_audit_form').form('load',row);
	
	//是否零余额账户
	$("#oldIszero").combobox({ 
		valueField : "value",
		textField : "text",
		panelHeight : "auto",
		data : [{value : 1, text : "是"}, {value : 0, text : "否"}]
	});
	//账户性质
	comboboxFunc("oldType02", "ACCTNATURE", "code", "name");
	
});