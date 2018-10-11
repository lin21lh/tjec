/**
 * 委员意见汇总
 * 
 * @remaek zhangtiantian 2016/9/22
 */
//请求路径
var urls = {
	gridUrl: contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/querySpeDealCaseList.do",
	opinionSumUrl: contextpath + "aros/wyInfoManage/controller/ContentbaseinfoController/opinionSum.do"
};

//默认加载
$(function()
{
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");
	
	var icons = {iconCls:"icon-clear"};
	$("#casecode").textbox("addClearBtn", icons);
	$("#casetype").combobox("addClearBtn", icons);
	$("#starttime").datebox("addClearBtn", icons);
	$("#endtime").datebox("addClearBtn", icons);
	
	//加载Grid数据
	loadCaseDataGrid(urls.gridUrl);
});

function showReload() {
	caseDataGrid.datagrid("reload"); 
}

//加载grid列表
var caseDataGrid;
function loadCaseDataGrid(url)
{
	caseDataGrid = $("#caseDataGrid").datagrid({
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
		url: url,
		loadMsg: "正在加载，请稍候......",
		toolbar: "#toolbar_center",
		showFooter: true,
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:250, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:150, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:150, sortable:true},
		  {field:"casetypeMc", title:"申请复议事项类型", halign:"center", width:300, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function doQuery(){
	
	var starttime = $("#starttime").datebox("getValue");
	var endtime = $("#endtime").datebox("getValue");
	
	if(endtime < starttime && endtime != "")
	{
		easyui_warn("结束时间应大于开始时间！", null);
		return;
	}
	
	var param = {
		casecode: $("#casecode").val(),
		casetype: $("#casetype").combobox('getValue'),
		startTime: starttime,
		endTime: endtime,
		menuid: menuid
	};
	
	caseDataGrid.datagrid("load", param);
}

/**
 * 打开委员意见汇总页面
 * @param row
 */
function openOpinionSum()
{
	//获取选中的行
	var row = caseDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请先选择一条案件", null);
		return;
	}
	
	var caseid = row[0].caseid;
	parent.$.modalDialog({
		title: "委员意见汇总",
		width: 900,
		height: 600,
		href: urls.opinionSumUrl + "?caseid=" + caseid,
		onLoad: function()
		{
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			
			mdDialog.find('#caseInfo').form("load", row[0]);
			showFileDiv(mdDialog.find("#filetd"), true, "XZFY", caseid, "");
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
