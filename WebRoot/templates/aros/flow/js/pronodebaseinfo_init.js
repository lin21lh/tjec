//请求路径
var baseUrl = contextpath + "aros/flow/controller/PronodebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryPronodebaseinfoList.do",			
	addUrl:baseUrl + "pronodebaseinfoAddInit.do",
	addCommitUrl:baseUrl + "pronodebaseinfoSave.do",
	editUrl:baseUrl + "pronodebaseinfoEditInit.do",
	editCommitUrl:baseUrl + "pronodebaseinfoEdit.do",
	delCommitUrl:baseUrl + "pronodebaseinfoDelete.do",
	detailUrl:baseUrl + "pronodebaseinfoDetail.do",
	roleUrl:baseUrl + "queryRoleList.do" 
};

var panel_ctl_handles = [{
	panelname:"#pronodebaseinfoPanel", 		// 要折叠的面板id
	gridname:"#pronodebaseinfoDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 				// 折叠按钮id
}];

//默认加载
$(function() {
	
	comboboxFuncByCondFilter(menuid, "protype", "B_CASEBASEINFO_PROTYPE", "code", "name");	//流程类型
	
	//加载Grid数据
	loadPronodebaseinfoGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#protype").textbox("addClearBtn", icons);
});

var pronodebaseinfoDataGrid;

function showReload(){
	pronodebaseinfoDataGrid.datagrid("reload"); 
}

//加载grid列表
function loadPronodebaseinfoGrid(url) {
	
	pronodebaseinfoDataGrid = $("#pronodebaseinfoDataGrid").datagrid({
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
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"id", checkbox:true},
		  {field:"proname", title:"流程名称", halign:"center", width:150, sortable:true},
		  {field:"nodename", title:"节点名称", halign:"center", width:150, sortable:true},
		  {field:"nodeid", title:"节点编号", halign:"center", width:100, sortable:true},
		  {field:"roleid", title:"角色ID", halign:"center", width:100, sortable:true},
		  {field:"rolename", title:"角色名称", halign:"center", width:150, sortable:true},
		  {field:"menuurl", title:"菜单路径", halign:"center", width:250, sortable:true},
		  {field:"optionalMc", title:"是否可选", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function pronodebaseinfoQuery(){
	
	var param = {
		protype:$("#protype").combobox('getValue'),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	pronodebaseinfoDataGrid.datagrid("load", param);
}

//初始复议申请页面
function pronodebaseinfoAdd(){
	
	parent.$.modalDialog({
		title:"流程配置维护",
		width:900,
		height:200,
		href:urls.addUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			comboboxFuncByCondFilter(menuid, "protype", "B_CASEBASEINFO_PROTYPE", "code", "name", mdDialog);	//流程类型
			comboboxFuncByCondFilter(menuid, "optional", "SYS_TRUE_FALSE", "code", "name", mdDialog);	        //是否可选
			
			mdDialog.find("#roleid").combobox({
				url:urls.roleUrl,
				valueField:'roleid',
				textField:'rolename' 
			});
		},
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					mdDialog.find("#menuid").val(menuid);
					
					var proname = mdDialog.find("#protype").combobox('getText');
					mdDialog.find("#proname").val(proname);
					
					var rolename = mdDialog.find("#roleid").combobox('getText');
					mdDialog.find("#rolename").val(rolename);
					
					submitForm(urls.addCommitUrl, "pronodebaseinfoForm", "");
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
				pronodebaseinfoDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 初始化修改页面
 */
function pronodebaseinfoEdit(){
	
	var selectRow = pronodebaseinfoDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条流程配置信息", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"流程配置修改",
		width:900,
		height:200,
		href:urls.editUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			comboboxFuncByCondFilter(menuid, "protype", "B_CASEBASEINFO_PROTYPE", "code", "name", mdDialog);	//流程类型
			comboboxFuncByCondFilter(menuid, "optional", "SYS_TRUE_FALSE", "code", "name", mdDialog);	        //是否可选

			mdDialog.find("#roleid").combobox({
				url:urls.roleUrl,
				valueField:'roleid',
				textField:'rolename' 
			});
			
			var row = pronodebaseinfoDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#pronodebaseinfoForm');
			f.form("load", row);
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				
				var proname = mdDialog.find("#protype").combobox('getText');
				mdDialog.find("#proname").val(proname);
				
				var rolename = mdDialog.find("#roleid").combobox('getText');
				mdDialog.find("#rolename").val(rolename);
				
				submitForm(urls.editCommitUrl, "pronodebaseinfoForm", "");
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
 * 删除流程配置信息
 */
function pronodebaseinfoDelete(){
	
	var selectRow = pronodebaseinfoDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条流程配置", null);
		return;
	}
	
	var row = pronodebaseinfoDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中流程配置删除？", function(r) {
		if (r){
			$.post(urls.delCommitUrl, {id:row.id}, 
					function(result){
						easyui_auto_notice(result, function() {
							pronodebaseinfoDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看流程配置详情信息
 */
function pronodebaseinfoView(){
	
	var selectRow = pronodebaseinfoDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条流程配置", null);
		return;
	}
	
	var row = pronodebaseinfoDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"流程配置详情",
		width:900,
		height:200,
		href:urls.detailUrl + "?id=" + row.id,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
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