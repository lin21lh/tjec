//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	//gridUrl:baseUrl + "queryXzfyViewList.do",	
	gridUrl:contextpath + "aros/tjfx/controller/queryCaseViewList.do",
	addUrl:baseUrl + "xzfyView.do",
	detailUrl:baseUrl + "xzfyReqDetail.do" 
};

var panel_ctl_handles = [{
	panelname:"#xzfyViewPanel", 
	gridname:"#xzfyViewDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 		// 折叠按钮id
}];

//默认加载
$(function() {
	
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
	loadXzfyViewGrid(urls.gridUrl, 0);
});

var xzfyDataGrid;
var caseFileDataGrid;

function showReload(){
	xzfyDataGrid.datagrid("reload"); 
}

//加载案件列表grid列表
function loadXzfyViewGrid(url, flag) {
	
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
			menuid:menuid,
			firstNode:true,
			lastNode:false,
			flag:flag 
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

/**
 * 条件查询
 */
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
		menuid:menuid,
		firstNode:true,
		lastNode:false,
		flag:1 
	};
	$("#xzfyViewDataGrid").datagrid("load", param);
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	
	showFlowModalDialogForView(selectRow[0].caseid, selectRow[0].protype);
}

/**
 * 加载下拉选择框
 * @param comboboxId
 * @param data
 */
function loadCombobox(comboboxId, data){
	$("#" + comboboxId).combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : data,
		onSelect : function(record) {}
	});
}

/**
 * 案件卷宗
 */
function fileDownload(){
	
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	
	var fileUrl = contextpath + "aros/jzgl/controller/CaseFileManageController/caseFileInit.do";
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "案件卷宗",
		width : 700,
		height : 500,
		href : fileUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			
			var mdDialog = parent.$.modalDialog.handler;
			
			loadCaseFileDataGrid(mdDialog, row);
		},
		buttons : [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
	});
}

/**
 * 加载案件卷宗列表
 */
function loadCaseFileDataGrid(mdDialog, rowData) {
	
	mdDialog.find("#allDownBtn").attr("href", "#");
	
	var caseid = rowData.caseid;
	
	caseFileDataGrid = mdDialog.find("#caseFileTab").datagrid({
		fit : true,
		border : false,
		singleSelect : true,
		idField : 'id',
		url : contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do?caseid="+caseid,
		loadMsg : "正在加载，请稍候......",
		columns : [ [ {
			field : "id",
			checkbox : true
		}, {
			field : "csaecode",
			halign : 'center',
			title : "案件编号",
			width : 250,
		},{  
			field : "doctype",
			halign : 'center',
			title : "文档名称",
			width : 250,
		},{
			field:"opt",
			title:"操作",
			width:50, 
			align:"center",
            formatter:function(val, row, index){
            	var hrefUrl = contextpath + "aros/jzgl/controller/CaseFileManageController/downLoadFile.do?noticeid=" + row.noticeid;
            	if(row.filetype == '0'){
            		hrefUrl = contextpath + "base/filemanage/fileManageController/downLoadFile.do?itemid="+ row.noticeid;
            	}
                var btn = '<a style="text-decoration:none;" href="'+ hrefUrl +'">下载</a>';  
                return btn;
            }
		}
		] ],
		onLoadSuccess : function(data) {
			
			var url = contextpath + "aros/jzgl/controller/CaseFileManageController/downLoadAllFile.do?caseid=" + caseid;
			
			// 设置全部下载按钮
			if(mdDialog.find("#caseFileTab").datagrid("getRows").length > 0){
				mdDialog.find("#allDownBtn").attr("href", url);
			}
		},
		onLoadError : function() {
		}
	});
}