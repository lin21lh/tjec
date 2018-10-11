/**
 * 选择案件
 */
function choiseCase(){
	
	parent.$.modalDialog2({
		title:"选择需调解案件",
		width:900,
		height:600,
		href:"aros/ajbg/fytj/FytjController/caseInit.do",
		onLoad:function() {},
		buttons : [{
				text : "确定",
				iconCls : "icon-save",
				handler : function() {
					var selectRow = parent.$.modalDialog.handler2.find("#datalist").datagrid('getChecked');
					if(selectRow==null || selectRow.length==0||selectRow.length>1){
						easyui_warn("请选择一条数据！",null);
						return;
					}
					var caseid = selectRow[0].caseid;
					var csaecode = selectRow[0].csaecode;	
					var intro = selectRow[0].intro;	
					var appname = selectRow[0].appname;	
					var defname = selectRow[0].defname;	
					parent.$.modalDialog.handler.find("#caseid").val(caseid);
					parent.$.modalDialog.handler.find("#csaecode").val(csaecode);
					parent.$.modalDialog.handler.find("#intro").textbox("setValue", intro);
					parent.$.modalDialog.handler.find("#appname").textbox("setValue", appname);
					parent.$.modalDialog.handler.find("#defname").textbox("setValue", defname);
					parent.$.modalDialog.handler2.dialog('close');
				}
				},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler2.dialog('close');
			}
		} ]
	});
}