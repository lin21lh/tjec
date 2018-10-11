//请求路径
var baseUrl = contextpath + "aros/dxal/controller/DxalController/";
var urls = {
	gridUrl:baseUrl + "dxalqueryList.do",			
	addUrl:baseUrl + "dxalAdd.do",
	addCommitUrl:baseUrl + "dxalSave.do",
	editUrl:baseUrl + "dxalEdit.do",
	editCommitUrl:baseUrl + "dxalReqEdit.do",
	delCommitUrl:baseUrl + "dxalDelete.do",
	detailUrl:baseUrl + "dxalDetail.do"
//	sendWFUrl:baseUrl + "xzfyReqFlow.do"
};

var panel_ctl_handles = [{
	panelname:"#dxalPanel", 		// 要折叠的面板id
	gridname:"#dxalDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];
//默认加载
$(function() {
	//加载Grid数据
	dxalLoadGrid(urls.gridUrl);
});

var dxalDataGrid;

function showReload(){
	dxalDataGrid.datagrid("reload"); 
}
//加载可项目grid列表
function dxalLoadGrid(url){
	dxalDataGrid = $("#dxalDataGrid").datagrid({
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
			      {field:"casetitle", title:"案件标题", halign:"center", width:150, sortable:true},       
			      {field:"ifpublishname", title:"是否发布", halign:"center", width:100, sortable:true},     
			      {field:"startdate", title:"发布开始日期", halign:"center", width:150, sortable:true},     
			      {field:"enddate", title:"发布到期日期", halign:"center", width:150, sortable:true}, 
			      {field:"operator", title:"操作人", halign:"center", width:100, sortable:true}, 
			      {field:"opttime", title:"操作时间", halign:"center", width:150, sortable:true}, 
			      {field:"casedesc", title:"案件描述", halign:"center", width:200, sortable:true,hidden:true}     
		 ]]
	});
	
}

/**
 * 条件查询
 */
function dxalQuery(){
	
	var param = {
		casetitle:$("#casetitle").val(),
//		casetitle:$("#casetitle").combobox('getValue'),
		menuid:menuid,
		firstNode:true,
		lastNode:false
	};
	dxalDataGrid.datagrid("load", param);
}

//新增典型案例页面
function dxalAdd(){
	parent.$.modalDialog({
		title:"典型案例",
		width:900,
		height:600,
		href:urls.addUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid, "ifpublish", "SYS_TRUE_FALSE", "code", "name", mdDialog);	//是否发布
		},
		buttons:[{
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					//判断起止日期的大小问题，如果到期日期小于开始日期则输入时间不合法
					var startdate = parent.$.modalDialog.handler.find('#startdate').datebox('getValue');
					var sdate = startdate.replace("-","").replace("-","");  
					var enddate = parent.$.modalDialog.handler.find('#enddate').datebox('getValue');
					var edate = enddate.replace("-","").replace("-",""); 
					if(edate<=sdate){
						easyui_warn("到期日期小于开始日期");
						return false;
					}else{
						parent.$.modalDialog.handler.find("#menuid").val(menuid);
						submitForm(urls.addCommitUrl, "dxalForm");
					}
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
				dxalDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 初始化典型案例修改页面
 */
function dxalEdit(){
	
	var selectRow = dxalDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条典型案例", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"典型案例修改",
		width:900,
		height:300,
		href:urls.editUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "ifpublish", "SYS_TRUE_FALSE", "code", "name", mdDialog);	//是否发布
			var row = selectRow[0];
			var f = mdDialog.find('#dxalForm');
			f.form("load", row);
		},
		buttons:[{
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				//判断起止日期的大小问题，如果到期日期小于开始日期则输入时间不合法
				var startdate = parent.$.modalDialog.handler.find('#startdate').datebox('getValue');
				var sdate = startdate.replace("-","").replace("-","");  
				var enddate = parent.$.modalDialog.handler.find('#enddate').datebox('getValue');
				var edate = enddate.replace("-","").replace("-",""); 
				
				if(edate<=sdate){
					easyui_warn("到期日期小于开始日期");
					return false;
				}else{
					parent.$.modalDialog.handler.find("#menuid").val(menuid);
					submitForm(urls.editCommitUrl, "dxalForm");
				}
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
 * 删除复议申请
 */
function dxalDelete(){
	
	var selectRow = dxalDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	
	var row = dxalDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中典型案例删除？", function(r) {
		if (r){
			$.post(urls.delCommitUrl, {id:row.id}, 
					function(result){
						easyui_auto_notice(result, function() {
							dxalDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

/**
 * 查看复议申请详细信息
 */
function dxalView(){
	
	var selectRow = dxalDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条典型案例", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"典型案例详情",
		width:900,
		height:600,
		href:urls.detailUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "ifpublish", "SYS_TRUE_FALSE", "code", "name", mdDialog);	//是否发布
			var row = dxalDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#dxalForm');
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
