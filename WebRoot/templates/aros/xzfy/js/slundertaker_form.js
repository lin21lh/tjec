var baseUrl = contextpath + "aros/wyInfoManage/controller/ContentbaseinfoController/";
var urls = {
		
};

var types = {
	view: "view",
	add: "add",
	edit: "edit",
	remove: "remove"
};
var receiveortype = "接收人";
var receiveortypecode = "receiveusername";

//默认加载
$(function()
{
});

//选择接收人
var userCode;
function choisePerson(num)	
{
	parent.$.modalDialog2({
		title:"关联笔录",
		width:800,
		height:450,
		href:"aros/xzfy/controller/CasebaseinfoController/userListView.do?rolecode=70",
		onLoad:function() {
			
		},
		buttons : [{
				text : "确定",
				iconCls : "icon-save",
				handler : function() {
					var selectRow = parent.$.modalDialog.handler2.find("#datalist").datagrid('getChecked');
					if(selectRow==null || selectRow.length==0||selectRow.length>1){
						easyui_warn("请选择一条数据！",null);
						return;
					}
					var userid = selectRow[0].userid;
					var username = selectRow[0].username;
					if(num == 1){
						parent.$.modalDialog.handler.find("#slundertakerid").val(userid);
						parent.$.modalDialog.handler.find("#slundertaker").textbox("setValue",username);
					}if( num == 2 ){
						parent.$.modalDialog.handler.find("#slcoorganiser").textbox("setValue",username);
					}
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
