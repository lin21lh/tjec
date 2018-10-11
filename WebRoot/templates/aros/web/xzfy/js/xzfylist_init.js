//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:"CbiWebController_queryList.do",			
	editUrl:"CbiWebController_updateIframeInit.do",
	editCommitUrl:"CbiWebController_update.do",
	cancelUrl:"CbiWebController_cancelInit.do",
	cancelCommitUrl:"CbiWebController_cancel.do",
	detailUrl:"CbiWebController_view.do",
	flowUrl:"CbiWebController_flow.do",
	noticeUrl:"CbiWebController_noticeInit.do",
	queryNoticeUrl:"CbiWebController_queryNoticeList.do"
};

var panel_ctl_handles = [{
	panelname:"#xzfyReqPanel", 		// 要折叠的面板id
	gridname:"#xzfyReqDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
	//加载Grid数据
	loadXzfyReqGrid(urls.gridUrl);
	
	var icons = {iconCls:"icon-clear"};
	$("#idcode").textbox("addClearBtn", icons);
	$("#key").textbox("addClearBtn", icons);
});

var xzfyReqDataGrid;

function showReload(){
	xzfyReqDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadXzfyReqGrid(url) {
	
	xzfyReqDataGrid = $("#xzfyReqDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:15,
		pageList:[15],
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
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:150, sortable:true, hidden:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true, align:"left"},
		  {field:"apptypeMc", title:"申请人类型", halign:"center", width:150, sortable:true, hidden:true},
		  {field:"idtypeMc", title:"证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idcode", title:"证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"phone", title:"联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"address", title:"通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"postcode", title:"邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"deftypeMc", title:"被申请人类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defname", title:"被申请人", halign:"center", width:300, sortable:true,align:"left"},
		  {field:"depttype", title:"被申请人机构类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidtype", title:"被申请人证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidcode", title:"被申请人证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defphone", title:"被申请人联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defaddress", title:"被申请人通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defpostcode", title:"被申请人邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"noticeddate", title:"接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"actnoticeddate", title:"实际接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"admtypeMc", title:"行政管理类型", halign:"center", width:160, sortable:true,align:"left"},
		  {field:"casetypeMc", title:"申请复议事项", halign:"center", width:100, sortable:true,align:"left"},
		  {field:"ifcompensationMc", title:"是否附带行政赔偿", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"amount", title:"赔偿金额", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appcase", title:"申请事项", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"factreason", title:"事实和理由", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"annex", title:"附件", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"operator", title:"操作人", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"optdate", title:"操作日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"protype", title:"流程类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"opttype", title:"处理标志", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"nodeidMc", title:"处理进度", halign:"center", width:80, sortable:true},
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
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"appdate", title:"申请日期", halign:"center", width:95, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function xzfyReqQuery(){
	
	var idcode = $("#idcode").val();
	if(idcode == null || idcode == ''){
		easyui_warn("请输入申请人证件号码", null);
		return;
	}
	
	var key = $("#key").val();
	if(key == null || key == ''){
		easyui_warn("请输入查询码", null);
		return;
	}
	
	var param = {
		idcode:$("#idcode").val(),
		key:$("#key").val(),
		menuid:menuid,
		//activityId:activityId,
		firstNode:true,
		lastNode:false
	};
	
	xzfyReqDataGrid.datagrid("load", param);
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
				xzfyReqDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
				easyui_warn("提交成功，查询码为【" + result.title +"】，请妥善保存以便查询", null);
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 初始化受理案件补正页面
 */
function xzfyReqEdit(){
	
	var selectRow = xzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条受理案件  !", null);
		return;
	}
	
	var row = xzfyReqDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"复议申请补正",
		width:900,
		height:600,
		href:urls.editUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
		},
		onClose:function(){
			window.location.reload();
		}
	});
}

/**
 * 撤销受理案件
 */
function xzfyReqCancel(){
	
	var selectRow = xzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条受理案件  !", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"受理撤销",
		width:900,
		height:300,
		href:urls.cancelUrl,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();

			var row = xzfyReqDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#xzfyCancelReqForm');
			f.form("load", row);
		},
		buttons:[{
			text:"提交",
			iconCls:"icon-save",
			handler:function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitCancelForm(urls.cancelCommitUrl, "xzfyCancelReqForm", "");
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
 * 受理案件提交
 * @param url
 * @param form
 */
function submitCancelForm(url, form){
	
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
				xzfyReqDataGrid.datagrid("reload");
				parent.$.modalDialog.handler.dialog("close");
				easyui_warn("撤销申请发起成功", null);
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 查看受理案件详细信息
 */
function xzfyReqView(){
	
	var selectRow = xzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条受理案件  !", null);
		return;
	}
	
	var row = xzfyReqDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"受理案件详情",
		width:900,
		height:500,
		href:urls.detailUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();

			showWebFileDiv(mdDialog.find("#filetd"), false, "XZFY", row.caseid, "");
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



/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = xzfyReqDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条受理案件  !", null);
		return;
	}
	
	var row = xzfyReqDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:"进度信息",
		width:900,
		height:500,
		href:urls.flowUrl + "?caseid=" + row.caseid,
		onLoad:function() {
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

/**
 * @Description: 通知书
 * @author 张田田
 * @date 2016-09-09 
 */
function notice()
{
	var selectRow = xzfyReqDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1)
	{
		easyui_warn("请选择一条受理案件  !", null);
		return;
	}
	
	parent.$.modalDialog({
		title:"通知书列表",
		width:800,
		height:400,
		href:urls.noticeUrl,
		onLoad:function()
		{
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			loadNoticeDataGrid(mdDialog, selectRow[0].caseid);
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


/**
 * @Description: 获取通知书列表
 * @author 张田田
 * @date 2016-09-09 
 */
function loadNoticeDataGrid(mdDialog, caseid)
{
	mdDialog.find("#noticeDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:10,
		queryParams:{
			firstNode:true,
			lastNode:false
		},
		url: urls.queryNoticeUrl + "?caseid=" + caseid,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
		columns:[[ 
		  {field:"noticeid", hidden:true},
		  {field:"doctype", title:"通知书类型", halign:"center", width:580, sortable:true, aligh:"left"},
		  {field:"buildtime", title:"生成时间", halign:"center", width:100, sortable:true},
		  {field:"", title:"操作", halign:"center", width:120, sortable:true, 
			  formatter:function(val, row, index){
	            	var hrefUrl = "CbiWebController_downLoadFile.do?noticeid=" + row.noticeid;
	                var btn = '<a style="text-decoration:none;font-size:12px;color:blue;" href="'+ hrefUrl +'">下载</a>';  
	                return btn;
	            }}
        ]]
	});
}