var baseUrl = contextpath + "base/excel/SysExcelDataImpController/";

var urls = {
	queryDataImpCfg : '../SysExcelImpCfgController/queryDataImpCfg.do',
	uploadExcelDataFile : 'uploadExcelDataFile.do',

	queryDataImpLog : baseUrl + "queryDataImplLog.do"
};

var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	// queryfunc : query, // 刷新操作函数
	gridname : "#tmplgrid",
	buttonid : '#openclose' // 折叠按钮id
} ];
$(function() {
	
	$("#cfgname").textbox("addClearBtn", {iconCls:"icon-clear"});
	
	$('#qpanel1').panel('close');
	$('#tmplgrid').datagrid({
		url : urls.queryDataImpCfg,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'cfgcategory',
			title : '类别',
			width : 200
		}, {
			field : 'cfgname',
			title : '名称',
			width : 300
		} ] ],
		fit : true,
		singleSelect : true,
		border : false,
		toolbar : '#toolbar_tmpl'
	});

	loadImpLogDataGrid();

	$(".datagrid-pager > table").hide();
});

function loadImpLogDataGrid() {

	$("#loggrid").datagrid({
		fit : true,
		singleSelect : true,
		stripe : true,
		rownumbers : true,
		pagination : true,
		view : scrollview,
		pageSize : 100,
		toolbar : "#toolbar_log",
		loadMsg : "正在加载，请稍候......",
		url : urls.queryDataImpLog,
		columns : [ [ {
			field : "id",
			checkbox : true
		}, {
			field : "usercode",
			title : "操作用户编码",
			width : 120
		}, {
			field : "username",
			title : "操作用户名称",
			width : 120
		}, {
			field : "ipaddr",
			title : "客户端IP地址",
			width : 160
		}, {
			field : "impdate",
			title : "导入操作日期",
			width : 130
		}, {
			field : "message",
			title : "日志信息",
			width : 480
		} ] ]
	});

}

function queryDataImplLogs() {
	$("#loggrid").datagrid("reload");
}

function query() {
	$('#tmplgrid').datagrid("load", {
		cfgname : $("#cfgname").textbox("getValue")
	});
}

function clear() {
	$('#cfgname').val('');
	query();
}
function doImport() {
	var sel = $('#tmplgrid').datagrid('getSelected');
	if (sel) {
		$('#fileWin').window('open');
	} else {
		easyui_warn('请选择一行数据！');
	}
}

function btn_cancel() {
	$('#fileWin').window('close');
}

function btn_ok() {
	var sel = $('#tmplgrid').datagrid('getSelected');
	$('#cfgid').val(sel.id);
	$('#fileForm').form('submit', {
		url : urls.uploadExcelDataFile,
		onSubmit : function() {
			return true;
		},
		success : function(data) {
			easyui_auto_notice(eval("(" + data + ")"), function() {
				$('#fileWin').window('close');
				queryDataImplLogs();
			}, function() {
				queryDataImplLogs();
			}, "导入过程发生异常！");
		}
	});

}

function showDetail() {

	var select = $("#loggrid").datagrid("getSelected");
	if (!select) {
		easyui_warn('请选择一条日志信息！');
		return;
	}

	parent.$
			.fastModalDialog({
				title : '日志信息详情',
				width : 420,
				height : 250,
				iconCls : 'icon-view',
				html : "<div style='overflow:hidden'><div id='ta090265' style='width:390px;height:200px;padding:10px' ></div></div>",
				dialogID : 'ndlg',
				onOpen : function() {
					var text = parent.$.fastModalDialog.handler['ndlg']
							.find('#ta090265');
					text.html(select.message);
				}
			});

}