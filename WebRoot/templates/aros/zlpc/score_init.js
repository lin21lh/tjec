//请求路径
var baseUrl = contextpath + "aros/zlpc/controller/ZlpcController/";
var urls = {
	queryUrl:baseUrl + "scoreQuery.do",
	formUrl:baseUrl + "scoreForm.do",
	saveUrl:baseUrl + "scoreSave.do",
	deleteUrl:baseUrl + "scoreDelete.do",
	detailUrl:baseUrl + "scoreDetail.do",
};

var panel_ctl_handles = [{
	panelname:"#xzfyViewPanel", 
	gridname:"#xzfyViewDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	//查询条件隐藏
	$("#xzfyViewPanel").panel("close");
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	comboboxFuncByCondFilter(menuid, "isbigcase", "SYS_TRUE_FALSE", "code", "name");	//是否重大案件
	comboboxFuncByCondFilter(menuid, "casestatus", "CASESTATE", "code", "name");	//案件状态
	
	$("#admtype").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#casetype").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#deftype").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#isbigcase").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#casestatus").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#casecode").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#appname").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#defname").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#yearstart").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#yearend").textbox("addClearBtn", {iconCls:"icon-clear"});
	
	//加载Grid数据
	loadXzfyViewGrid(urls.queryUrl);
});

var xzfyDataGrid;

function showReload(){
	xzfyDataGrid.datagrid("reload"); 
}

//加载案件列表grid列表
function loadXzfyViewGrid(url) {
	xzfyDataGrid = $("#xzfyViewDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		queryParams:{
			menuid:menuid
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		onClickRow:function(rowIndex,rowData){
        	viewFlow();
        },
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:120, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:80, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"apptypeMc", title:"申请人类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idtypeMc", title:"证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idcode", title:"证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"phone", title:"联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"address", title:"通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"postcode", title:"邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"deftypeMc", title:"被申请人类型", width:100, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:200, sortable:true},
		  {field:"depttype", title:"被申请人机构类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidtype", title:"被申请人证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidcode", title:"被申请人证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defphone", title:"被申请人联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defaddress", title:"被申请人通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defpostcode", title:"被申请人邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"noticeddate", title:"接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"actnoticeddate", title:"实际接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"admtypeMc", title:"行政管理类型", halign:"center", width:100, sortable:true},
		  {field:"casetypeMc", title:"申请复议事项", halign:"center", width:100, sortable:true},
		  {field:"ifcompensationMc", title:"是否附带行政赔偿", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"amount", title:"赔偿金额", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appcase", title:"申请事项", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"factreason", title:"事实和理由", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"annex", title:"附件", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appdate", title:"申请日期", halign:"center", width:80, sortable:true},
		  {field:"operator", title:"操作人", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"optdate", title:"操作日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"opttype", title:"处理标志", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"nodeid", title:"节点编号", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"lasttime", title:"数据最后更新时间", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"userid", title:"用户ID", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"oldprotype", title:"原流程类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"mobile", title:"联系手机", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"mailaddress", title:"邮寄地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"email", title:"邮箱", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defmobile", title:"被申请人联系手机", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defmailaddress", title:"被申请人邮寄地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defemail", title:"被申请人邮箱", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"receiveway", title:"收文方式", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"expresscom", title:"递送公司", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"couriernum", title:"递送单号", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:100, sortable:true}
        ]]
	});
}

function xzfyViewQuery(){
	var param = {
		isbigcase:$("#isbigcase").combobox('getValue'),
		csaecode:$("#csaecode").textbox('getValue'),
		casestatus:$("#casestatus").combobox('getValue'),
		admtype:$("#admtype").combobox('getValue'),
		casetype:$("#casetype").combobox('getValue'),
		appname:$("#appname").textbox('getValue'),
		defname:$("#defname").textbox('getValue'),
		yearstart:$("#yearstart").datebox('getValue'),
		yearend:$("#yearend").datebox('getValue'),
		appdatestart:$("#appdatestart").datebox('getValue'),
		appdateend:$("#appdateend").datebox('getValue'),
		menuid:menuid
	};
	$("#xzfyViewDataGrid").datagrid("load", param);
}

function scoreAddForm(){
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getChecked")[0];
	if(row.zlpcnum >0){
		easyui_warn("已存在评查结果，请通过修改功能进行修改！",null);
		return;
	}
	parent.$.modalDialog({
		title : "质量评查",
		width : 900,
		height : 530,
		href : urls.formUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				scoreSave("dataForm");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function scoreUpdateForm(){
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getChecked")[0];
	if(row.zlpcnum == 0){
		easyui_warn("不存在评查结果，请通过新增功能进行添加！",null);
		return;
	}
	parent.$.modalDialog({
		title : "质量评查",
		width : 900,
		height : 530,
		href : urls.formUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				scoreSave("dataForm");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function scoreSave(form){
	var form = parent.$.modalDialog.handler.find('#' + form);
	var data = "";
	form.find("input[id$=_n]").each(function(){
		data = data + $(this).attr("data") + "," + $(this).val() + ";"
	});
	console.log(data);
	$.ajax({
		type : "post",
		url : urls.saveUrl,
		data : {
			caseid : form.find("#caseid").val(),
			data : data
		},
		dataType : 'json',
		async : false,
		success : function(data){
			if (data.success) {
				easyui_auto_notice(data, function() {
					xzfyDataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				});
			} else {
				parent.$.messager.alert('错误', data.title, 'error');
			}
		}
	});
}

function scoreDelete(){
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getChecked")[0];
	parent.$.messager.confirm("确认删除", "确认删除选中数据？", 
			function(r) {
				if (r) {
					$.post(urls.deleteUrl, 
						{caseid : row.caseid}, 
						function(result) {
							easyui_auto_notice(result, function() {
								xzfyDataGrid.datagrid('reload');
							});
						},
						"json");
				}
			});
}

function scoreDetail(){
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getChecked")[0];
	parent.$.modalDialog({
		title : "质量评查",
		width : 900,
		height : 530,
		href : urls.detailUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}








