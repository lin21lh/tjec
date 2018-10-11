function clickUploadDiv(elementcode){
	var fjkeyid =$('#fjkeyid').val();
	var acaseid = $('#acaseid').val();
	fjkeyid = acaseid == "" ? fjkeyid : acaseid;
	showUploadifyModalDialog(parent.$('#uploadifydiv'), "itemids", elementcode, fjkeyid);
}

var caseDataGrid;

/**
 * 选择关联案件
 */
function selectCase(dialogId){
	
	var stage = $('#stage').combobox('getValue');
	//判断审理阶段是否为空
	if (stage == '' || stage == null){
		easyui_warn("请选择审理阶段", null);
		return;
	}
	//判断是否是一审
	if(stage == "01") {	
		easyui_warn("一审无需关联其它应诉案件", null);
		return;
	}
	
	var href = contextpath + "aros/ysaj/controller/AdmlitbaseinfoController/selectCase.do";
	
	dialogId.dialog({
		title:"选择关联案件",
		width:900,
		height:600,
		href:href,
		onLoad:function(){
			
			//所有该角色的用户
			openCaseDataGrid(dialogId, stage);
		},
		buttons:[{
			id:"saveBtn",
			text:"选择",
			iconCls:"icon-save",
			handler:function() {
				var selectRow = caseDataGrid.datagrid("getChecked");
				if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
					easyui_warn("请选择一条案件信息", null);
					return;
				}
				
				var row = caseDataGrid.datagrid("getSelections")[0];
				
				$("#relaacaseid").textbox('setValue', row.lcasecode);
				
				dialogId.dialog('close');
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				dialogId.dialog('close');
			}
		}]
	});
}

/**
 * 打开应诉案件Grid页面
 * @param mdDialog
 */
function openCaseDataGrid(dialog, stage) {
	
	var href = contextpath + "aros/ysaj/controller/AdmlitbaseinfoController/queryCaseList.do?stage="+stage;
	
	caseDataGrid = dialog.find("#caseTable").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		height:400,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		queryParams:{
			menuid:menuid,
			firstNode:true,
			lastNode:false 
		},
		pageSize:30,
		url:href,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_case",
		showFooter:true,
		columns:[[ 
			  {field:"acaseid", checkbox:true},
			  {field:"lcasecode", title:"本机关案号", halign:"center", width:250, sortable:true},
			  {field:"rcasecode", title:"司法机关案号", halign:"center", width:250, sortable:true},
			  {field:"rdeptname", title:"受案法院", halign:"center", width:150, sortable:true},
			  {field:"plaintiff", title:"原告", halign:"center", width:150, sortable:true} 
        ]]
	});
}