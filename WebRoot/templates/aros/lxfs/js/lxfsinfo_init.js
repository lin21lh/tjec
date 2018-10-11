//请求路径
var baseUrl = contextpath + "aros/lxfs/controller/LxfsController/";
var urls = {
	gridUrl:baseUrl + "lxfsqueryList.do",			
	addUrl:baseUrl + "lxfsAdd.do",
	addCommitUrl:baseUrl + "lxfsSave.do",
	editUrl:baseUrl + "lxfsEdit.do",
	editCommitUrl:baseUrl + "lxfsReqEdit.do",
	delCommitUrl:baseUrl + "lxfsDelete.do",
	detailUrl:baseUrl + "lxfsDetail.do"
//	sendWFUrl:baseUrl + "xzfyReqFlow.do"
};

var panel_ctl_handles = [{
	panelname:"#lxfsPanel", 		// 要折叠的面板id
	gridname:"#lxfsDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];
//默认加载
$(function() {
	//加载Grid数据
	lxfsLoadGrid(urls.gridUrl);
	// 在查询条件中添加删除按钮
//	var icons = {iconCls:"icon-clear"};
//	$("#person").textbox("addClearBtn", icons);
});

var lxfsDataGrid;

//加载可项目grid列表
function lxfsLoadGrid(url){
	lxfsDataGrid = $("#lxfsDataGrid").datagrid({
			fit:true,
			stripe:true,
			singleSelect:true,
			rownumbers:true,
			pagination:true,
			remoteSort:false,
			multiSort:true,
			pageSize:30,
			queryParams:{
				menuid:menuid,
				//status:1,
				//activityId:activityId,
				firstNode:true,
				lastNode:false
			},
			url: url,
			loadMsg:"正在加载，请稍候......",
			toolbar:"#toolbar_center",
			showFooter:true,
			columns:[[ 
			      {field:"id", checkbox:true},     
			      {field:"person", title:"联系人", halign:"center", width:150, sortable:true},       
			      {field:"deptname", title:"机关名称", halign:"center", width:270, sortable:true},     
			      {field:"address", title:"地址", halign:"center", width:300, sortable:true},     
			      {field:"phone", title:"联系电话", halign:"center", width:150, sortable:true}, 
			      {field:"xcoor", title:"X坐标", halign:"center", width:150, sortable:true},
			      {field:"ycoor", title:"Y坐标", halign:"center", width:150, sortable:true}
		 ]]
	});
	
}

/**
 * 条件查询
 */
function lxfsQuery(){
	
	var param = {
		person:$("#person").val(),
		menuid:menuid,
		firstNode:true,
		lastNode:false
	};
	lxfsDataGrid.datagrid("load", param);
}

//新增联系方式页面
function lxfsAdd(){
	parent.$.modalDialog({
		title:"新增联系方式",
		width:900,
		height:300,
		href:urls.addUrl,
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.addCommitUrl, "lxfsForm");
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
		}]
	});
}

//提交表单
function submitForm(url, form){
	
	var form = parent.$.modalDialog.handler.find('#' + form);
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
		
	form.form("submit", {
		url:url,
		onSubmit:function() {
			parent.$.messager.progress({
				title:"提示",
				text:"数据处理中，请稍后...."
			});
			var isValid = form.form("validate");
			if (!isValid) {
				parent.$.messager.progress("close");
			}
			return isValid;
		},
		success:function(result) {
			parent.$.messager.progress("close");
			result = $.parseJSON(result);
			if (result.success) {
				lxfsDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 初始化联系方式修改页面
 */
function lxfsEdit(){
	var selectRow = lxfsDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条联系方式", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"联系方式修改",
		width:900,
		height:300,
		href:urls.editUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			var row = selectRow[0];
			var f = mdDialog.find('#lxfsForm');
			f.form("load", row);
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.editCommitUrl, "lxfsForm");
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}

/**
 * 删除联系方式
 */
function lxfsDelete(){
	
	var selectRow = lxfsDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条联系方式", null);
		return;
	}
	
	var row = lxfsDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中联系方式删除吗？", function(r) {
		if (r){
			$.post(urls.delCommitUrl, {id:row.id}, 
					function(result){0
						easyui_auto_notice(result, function() {
							lxfsDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看联系方式详细信息
 */
function lxfsView(){
	
	var selectRow = lxfsDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条联系方式", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"联系方式详情",
		width:900,
		height:300,
		href:urls.detailUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var row = lxfsDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#lxfsForm');
			f.form("load", row);
		},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}
