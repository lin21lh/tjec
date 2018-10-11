//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	detailUrl:baseUrl + "xzfyReqDetail.do",
	noticeUrl:baseUrl+ "notice_view.do",
	gridUrl:baseUrl+"querySimcaseManageList.do"
};

var panel_ctl_handles = [{
	panelname:"#xzfyViewPanel", 	// 要折叠的面板id
	gridname:"#xzfyViewDataGrid",  	// 刷新操作函数
	buttonid:"#openAclose"	 		// 折叠按钮id
}];
var xzfyViewDataGrid;
//默认加载
$(function() {
	
	var menuid = $("#menuid").val();
	//菜单ID
	comboboxFuncByCondFilter(menuid, "protype", "B_CASEBASEINFO_PROTYPE", "code", "name");	//流程类型
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	//加载Grid数据
	loadXzfyViewGrid(urls.gridUrl);
});



//加载复议案件  相似案件查询grid列表页
function loadXzfyViewGrid(url) {
	var deftype = $("#deftype").val();
	var admtype = $("#admtype").val();
	var casetype = $("#casetype").val();
	var menuid = $("#menuid").val();
	xzfyViewDataGrid = $("#xzfyViewDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		queryParams:{
			deftype:deftype,
			admtype:admtype,
			casetype:casetype,
			menuid:menuid,
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_centers",
		showFooter:true,
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:200, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:100, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"deftypeMc", title:"被申请人类型", width:150, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:200, sortable:true},
		  {field:"admtypeMc", title:"行政管理类型", halign:"center", width:150, sortable:true},
		  {field:"casetypeMc", title:"申请复议事项", halign:"center", width:150, sortable:true},
		  {field:"protypeMc", title:"流程类型", halign:"center", width:80, sortable:true},
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function xzfySimilQuery() {
	var param = {
		menuid: menuid,
		keyval: $("#keyval").val(),
		firstNode:true,
		lastNode:false
	};
	
	xzfyViewDataGrid.datagrid("load", param);
}
/**
 * 查看复议案件详细信息
 */
function xzfyReqView(){
	var selectRow = xzfyViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	var row = xzfyViewDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog2({
		title:"复议案件详情",
		width:900,
		height:600,
		href:urls.detailUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var mdDialog2 = parent.$.modalDialog.handler2;
			var sessionId = mdDialog2.find('#sessionId').val();
			showFileDiv(mdDialog2.find("#filetd"), false, "XZFY", row.caseid, "");
			showFileDiv(mdDialog2.find("#accfiletd"), false, "XZFYSL", row.caseid, "");
			showFileDiv(mdDialog2.find("#auditfiletd"), false, "XZFYSC", row.caseid, "");
			showFileDiv(mdDialog2.find("#decisionfiletd"), false, "XZFYJD", row.caseid, "");
		},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler2.dialog('close');
			}
		}]
	});
}

/**
 * 委员意见查看
 */
function sugView() {
	
	var viewUrl = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/view.do";
	
	var selectRow = xzfyViewDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一条复议案件 !", null);
		return;
	}
	
	var row = xzfyViewDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog2({
		title : "委员意见查看",
		width : 900,
		height : 500,
		href : viewUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog2 = parent.$.modalDialog.handler2;
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler2.dialog('close');
			}
		} ]
	});
}
/**
 * 查看流程信息
 */
function workflowMessage(){
	var selectRow = xzfyViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	
	showFlowModalDialogForReq_simcase(selectRow[0].caseid);
}


/**
 * 加载表字段定义数据表格（通知书查看）
 */
function noticeView() {
	var selectRow = xzfyViewDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	var row = xzfyViewDataGrid.datagrid("getSelections")[0];
	var caseid = row.caseid;  
	parent.$.modalDialog2({
		title:"卷宗列表",
		width:842,
		height:600,
		href : urls.noticeUrl + "?caseid=" + caseid,
		onLoad:function() {
	    $("#caseFileTab").datagrid({
		fit : true,
		border : false,
		singleSelect : true,
		idField : 'id',
		url : contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do?caseid="+caseid+"&menuid="+menuid,
		loadMsg : "正在加载，请稍候......",
		columns : [[{
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
			width : 200,
		},{  
			field : "protype",
			halign : 'center',
			title : "流程类型",
			width : 150,
		},{  
			field : "buildtime",
			halign : 'center',
			title : "生成时间",
			width : 150,
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
       }]],
		onLoadError : function() {
		}
	})
	},
	buttons:[{
		text:"关闭",
		iconCls:"icon-cancel",
		handler:function() {
			parent.$.modalDialog.handler2.dialog('close');
		}
	}]
	})
}

