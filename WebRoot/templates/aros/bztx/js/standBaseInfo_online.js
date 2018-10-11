//请求路径
var baseUrl = contextpath + "aros/bztx/controller/StandBaseInfoController/";
var urls = {
	gridUrl:baseUrl + "queryStandbaseInfo.do"
	,addUrl: baseUrl + "add.do"
	,saveUrl: baseUrl + "save.do"
	,deleteUrl: baseUrl + "delete.do"
	,detailUrl: baseUrl + "detail.do"
};

var panel_ctl_handles = [{
	panelname:"#bztxPanel", 		// 要折叠的面板id
	gridname:"#onlineDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
	//加载Grid数据
	loadBxzfyReqGrid(urls.gridUrl);

});

var bxzfyReqDataGrid;

function showReload(){
	bxzfyReqDataGrid.datagrid("reload");
}

//加载可项目grid列表
function loadBxzfyReqGrid(url) {

	bxzfyReqDataGrid = $("#onlineDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:10,
		queryParams:{
			menuid:menuid,
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		onDblClickRow: function (rowIndex, rowData) {  
			bztxView();  //查看详情
        },
		columns:[[
		  {field:"standid", checkbox:true},
		  {field:"standardname", title:"标准名称", halign:"center", width:250, sortable:true},
		  {field:"systypename", title:"体系类型", halign:"center", width:150, sortable:true},
		  {field:"stagetypename", title:"阶段类型", halign:"center", width:250, sortable:true},
		  {field:"remark", title:"内容", halign:"center", width:300, sortable:true,formatter: formatString}
        ]]
	});
}

// 限制内容的长度
function formatString(value, row,index){
	if(value != null &&  value.length > 0){
		return value.substring(0,50);
	}else return value;
}

/**
 * 条件查询
 */
function bztxQuery(){
	var param = {
		standardName:$("#standardName").val(),
		sysType:$("#sysType").combobox('getValue'),
		starttime:$("#starttime").datebox('getValue'),
		endtime:$("#endtime").datebox('getValue'),
		menuid:menuid,
		firstNode:true,
		lastNode:false
	};
	bxzfyReqDataGrid.datagrid("load", param);
}

//初始复议申请页面
function bztxAdd(){
	parent.$.modalDialog({
		title:"标准体系新增",
		width:900,
		height:400,
		href:urls.addUrl + "?menuid="+ menuid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
		},
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.saveUrl, "bztxForm");
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
				bxzfyReqDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 
 */
function bztxEdit(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"标准体系修改",
		width:900,
		height:400,
		href:urls.addUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();

			var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#bztxForm');
			f.form("load", row);
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "bztxForm", "");
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
 * 删除被复议申请
 */
function bztxDelete(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	
	var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中数据删除？", function(r) {
		if (r){
			$.post(urls.deleteUrl, {id:row.standid},
					function(result){
						easyui_auto_notice(result, function() {
							bxzfyReqDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看被复议案件详细信息
 */
function bztxView(){
	
	var selectRow = bxzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	
	var row = bxzfyReqDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog2({
		title:"标准体系详情",
		width:900,
		height:400,
		href:urls.detailUrl + "?standid=" + row.standid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler2;
			var sessionId = parent.$.modalDialog.handler2.find('#sessionId').val();
		},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler2.dialog("close");
			}
		}]
	});
}

