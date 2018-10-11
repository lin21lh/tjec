
/**
 * account_query_history.js
 */
var baseUrl = contextpath + "query/account/controller/AccountQueryController/";
//路径
var urls = {
	queryAccount : baseUrl+"queryAccount.do",
	queryApplicationInfo : baseUrl+"queryApplicationInfo.do",
	getAccountForm : baseUrl +"getAccountForm.do"
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};
//零余额 
var iszero={
	"0" : "否",
	"1" : "是"
};
//账户状态（是否撤销）
var account_status = {
		"1" : "正常",
		"9" : "撤销"
};
//折叠
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#datagridHisAccount", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

var datagridHisAccount = $("#datagridHisAccount");

$(function(){
	//$('#qpanel1').panel('close');
	
	/*$('#bdgagency').treeDialog({
		title :'选择预算单位',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'bdgagencyHidden',
		prompt: "请选择预算单位",
		multiSelect: false, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : $("#menuid").val(),
			customSql :$("#allbdgagency").val()
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "BDGAGENCY",
		filters:{
			code: "单位编码",
			name: "单位名称"
		}
	});*/
	//开户行
	
});


/**
 * 查询
 */
function queryAccountHisInfo(){
	$("#datagridHisAccount").datagrid("load", {
		bankCode : $("#bank").treeDialog('getValue'),
		accountName : $("#accountNameSearch").val(),
		accountNumber : $("#accountNumberSearch").val()
	});
} 

/**
 * 
 */
function qryApplicationInfo(){
	var rows = $("#datagridHisAccount").datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	var applicationId= rows[0].applicationId;
	showModalDialog(850,550, "ApplicationViewForm", types.view, "", "变更详情",
			baseUrl+"loadApplicationView.do?applicationId="+applicationId, urls.queryApplicationInfo, true,applicationId);
}

/**
 * 弹出模式窗口
 * 
 * @param operType
 *            操作类型，主要包括查看，添加和修改
 * @param dataGrid
 *            操作的数据表格
 * @param title
 *            弹出窗的标题
 * @param href
 *            弹出窗的href
 * @param url
 *            对应操作的url
 * @param fill
 *            是否自动填充表单，主要是查看和修改
 */
function showModalDialog(width,height, form, operType, dataGrid, title, href, url,
		fill,applicationId) {
	if (fill) {
		var rows = $("#datagridHisAccount").datagrid("getSelections");
		if (rows.length != 1) {
			easyui_warn("请选择一行数据！");
			return;
		}
	}
	var icon = 'icon-' + operType;
	$('#dd').show().dialog({
		title : title,
		iconCls : icon,
		width : width,
		height : height,
		href : href,
		modal:true,
		resizable: true,
		onLoad : function() {
			$.post(urls.getAccountForm, {
				applicationId : applicationId
			}, function(result) {
				var f = $('#dd').find('#' + form);
				f.form("load", result);
				//showFileTable(mdDialog.find("#fileTable"),true,"FAAPPLICATION",row.applicationId,"");
				showFileDiv($('#dd').find("#filetd"),false,"FAAPPLICATION",applicationId,"");
			}, "json");
			}
	});
};

/**
 * 导出
 * @param flag
 */
function outExcelAccountHis(flag) {
	if(flag=='1'){
		var paramJSON = {
				includeHidden : false,
				title : 'accountHis',
				excelVersion: '2007'
		};
		$("#datagridHisAccount").datagrid("outExcel",paramJSON);
	}else{
		var paramJSON = {};
		
		paramJSON.filename = 'outAccountHisExcel';
		paramJSON.excelVersion = '2007';
		paramJSON.bdgagencycode = $("#bdgagency").treeDialog('getValue');
		paramJSON.bankCode = $("#bank").treeDialog('getValue');
		paramJSON.accountName = $("#accountNameSearch").val();
		paramJSON.accountNumber = $("#accountNumberSearch").val();
		
		outExcel('', paramJSON);
	}
};

function out_excel_his(includeCurrentPage) {
	var content = '<tr>';
	content += '<th>导出类型：</th>';
	content += '<td style="height:30px">';
	content += '<select id="outExcelType" class="easyui-combobox" required="true" value="1" missingMessage="请选择导出类型" name="outExcelType" style="width:180px">';
		content += '<option value="1">导出选中数据</option>';
		if (includeCurrentPage) {
			content += '<option value="2">导出本页</option>';
		}
		content += '<option value="3">导出所有</option>';
		content += '<option value="4">导出全部</option>';
	content += '</select>';
	content += '</td></tr><tr style="height:30px">';
	content += '<th>导出版本：</th>';
	content += '<td>';
	content += '<select id="outExcelVersion" class="easyui-combobox" required="true" value="1" missingMessage="请选择导出Excel版本" name="outExcelVersion" style="width:180px">';
	content += '<option value="2003">Excel97-2003工作薄(*.xls)</option>';
	content += '<option value="2007">Excel工作薄(*.xlsx)</option>';
content += '</select>';
	content += '</td></tr>';
	$('#d1').show().dialog({
		title : '导出Excel',
		iconCls : 'icon-excel',
		width : 300,
		height : 140,
		content : content,
		onLoad : function() {
		},
		buttons : [{
			text:'导出',
			handler : function() {
				var outExcelType = $('#d1').find('#outExcelType').combobox('getValue');
				var outExcelVersion = $('#d1').find('#outExcelVersion').combobox('getValue');
				outExcelAccount1(outExcelType, outExcelVersion);
			}
		}, {
			text:'关闭',
			handler : function() {
				$('#d1').dialog('close');
			}
		}]
	});
}

/**
 * 导出excel
 */
function outExcelAccount1(outExcelModel, outExcelVersion) {
	switch (outExcelModel) {
	case '1': //导出选中行
		var paramJSON = {
			includeHidden : false,
			title : 'account',
			excelVersion: outExcelVersion,
			outExcelModel : outExcelModel
		};
		$("#datagridHisAccount").datagrid("outExcel",paramJSON);
		break;
	case '2': //当前页（不分页方式，此项选择无）
		var paramJSON = {
			includeHidden : false,
			title : 'account',
			excelVersion: outExcelVersion,
			outExcelModel : outExcelModel
		};
		$("#datagridHisAccount").datagrid("outExcel",paramJSON);
		break;
	case '3': //导出所有（现条件下所有包含数据权限范围内）
		var paramJSON = {};
		var rows = parent.$.modalDialog.openner_dataGrid.datagrid("getSelections");
		paramJSON.filename = 'outAccountHisExcel';
		paramJSON.excelVersion = outExcelVersion;
		paramJSON.bankCode = $("#bank").treeDialog('getValue');
		paramJSON.itemid = rows[0].itemid;
		paramJSON.accountName = $("#accountNameSearch").val();
		paramJSON.accountNumber = $("#accountNumberSearch").val();
		paramJSON.outExcelModel = outExcelModel;
		
		outExcel('', paramJSON);
		break;
	case '4': //导出全部（权限范围内全部）
		var paramJSON = {};
		var rows = parent.$.modalDialog.openner_dataGrid.datagrid("getSelections");
		paramJSON.filename = 'outAccountHisExcel';
		paramJSON.excelVersion = outExcelVersion;
		paramJSON.bankCode = $("#bank").treeDialog('getValue');
		paramJSON.itemid = rows[0].itemid;
		paramJSON.accountName = $("#accountNameSearch").val();
		paramJSON.accountNumber = $("#accountNumberSearch").val();
		paramJSON.outExcelModel = outExcelModel;
		
		outExcel('', paramJSON);
		break;
	default:
		break;
	}
};



//单位换算
function bytesToSize(bytes) {
    if (bytes === 0) return '0 B';
    var k = 1000, // or 1024
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
   return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}
