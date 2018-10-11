//请求路径
var spesugbaseUrl = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/";
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";

var urls = {
	gridUrl:spesugbaseUrl + "queryXzfyProcessList.do",                    // 查询案件列表
	saveAjZtUrl : spesugbaseUrl + "saveSpegrouprelainfo.do" 
};

var panel_ctl_handles = [{
	panelname:"#sendCasePanel", 		// 要折叠的面板id
	gridname:"#sendCaseGrid",  			// 刷新操作函数
	buttonid:"#openclose"	 			// 折叠按钮id
}];

//默认加载
$(function() {
	
	$('#sendCasePanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	//行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	//申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	//被申请人类型
	
	var icons = {iconCls:"icon-clear"};
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	$("#admtype").textbox("addClearBtn", icons);
	$("#casetype").textbox("addClearBtn", icons);
	$("#deftype").textbox("addClearBtn", icons);
	
	//加载Grid数据
	loadXzfyProcessGrid(urls.gridUrl);
});

var xzfyDataGrid;

function showReload(){
	xzfyDataGrid.datagrid("reload"); 
}

//加载可项目grid列表
function loadXzfyProcessGrid(url) {
	
	xzfyDataGrid = $("#sendCaseGrid").datagrid({
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
		onDblClickRow:function (rowIndex, rowData) {  
			discussAdd();
        },
        onClickRow:function(rowIndex, rowData){
        	viewTime();
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
		  {field:"deftypeMc", title:"被申请人类型", width:150, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:250, sortable:true},
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
		  {field:"appdate", title:"申请日期", halign:"center", width:80, sortable:true, hidden:true},
		  {field:"operator", title:"操作人", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"optdate", title:"操作日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"protype", title:"流程类型", halign:"center", width:200, sortable:true, hidden:true},
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
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:80, sortable:true},
		  {field:"delaydays", title:"延期天数", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"region", title:"行政区", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"intro", title:"案件简介", halign:"center", width:100, sortable:true, hidden:true},
		  {field:"isgreat", title:"是否重大案件备案", halign:"center", width:100, sortable:true, hidden:true} 
        ]]
	});
}

/**
 * 条件查询
 */
function sendCaseQuery(){
	var param = {
		appname:$("#appname").val(),
		defname:$("#defname").val(),
		admtype:$("#admtype").combobox('getValue'),
		casetype:$("#casetype").combobox('getValue'),
		deftype:$("#deftype").combobox('getValue'),
	};
	
	xzfyDataGrid.datagrid("load", param);
}

/**
 * 发起复议研讨
 */
function discussAdd() {
	
	var zjxzUrl = contextpath + "aros/zjgl/BGroupbaseinfoController/" + "discussListInit.do";
	
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条复议案件信息", null);
		return;
	}
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "复议研讨发起",
		width : 970,
		height : 550,
		href : zjxzUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			
			var mdDialog = parent.$.modalDialog.handler;
			var caseid = row.caseid;
			
			//可选委员列表
			zjDataGrid(mdDialog, caseid);
			//已选委员列表
			zjxzDataGrid(mdDialog, caseid);
			
			var selectRows;
			mdDialog.find("#addBtn").on("click",function(){
				
				selectRows = kxgrid.datagrid('getChecked');
				if (selectRows == null || selectRows.length == 0) {
					easyui_warn("请选择一位可选委员", null);
					return;
				}
				
				var selectNum = selectRows.length;
				for(var i = selectNum - 1; i >= 0; i--){
					
					var rowInfo = selectRows[i];
					var index = kxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjxzTable").datagrid('appendRow', rowInfo);	//追加一行
			    	mdDialog.find("#zjTable").datagrid('deleteRow',index);		//删除一行
				}
			});
			
			mdDialog.find("#delBtn").on("click",function(){
				
				selectRows = yxgrid.datagrid('getChecked');
				if (selectRows == null || selectRows.length == 0) {
					easyui_warn("请选择一位已选委员", null);
					return;
				}
				
				var selectNum = selectRows.length;
				for(var i = selectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = yxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjTable").datagrid('appendRow', rowInfo);	//追加一行
			    	mdDialog.find("#zjxzTable").datagrid('deleteRow', index);	//删除一行
				}
			});
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				setspeIds();
				submitSpecialistForm(urls.saveAjZtUrl, "zjxzForm", "");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

//可选委员列表
function zjDataGrid(mdDialog, caseid) {
	
	var zjUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "querySpecialists.do";
	
	kxgrid = mdDialog.find("#zjTable").datagrid({
		title : "可选委员列表",
		height : 400,
		width : '100%',
		collapsible : false,
		url : zjUrl,
		queryParams : {
			caseid : caseid,
			operflag : 'all'
		},
		singleSelect : false,
		rownumbers : true,
		idField : 'speid',
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 100,
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
		} ] ],
        onDblClickRow:function (rowIndex, rowData) {       	 
    	  mdDialog.find("#zjxzTable").datagrid('appendRow', rowData);//追加一行
    	  mdDialog.find("#zjTable").datagrid('deleteRow',rowIndex);//删除一行
        }
	});
	
	return kxgrid;
}

//已选委员列表
function zjxzDataGrid(mdDialog, caseid){
	
	var zjUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "querySpecialists.do";
	
	yxgrid = mdDialog.find("#zjxzTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选委员列表',
		collapsible: false,
		url : zjUrl,
		queryParams : {caseid:caseid,operflag : 'group'},
		singleSelect: false,
		rownumbers : true,
		idField: 'speid',
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 100,
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
		} ] ],
       onDblClickRow:function (rowIndex, rowData) {	
    	   mdDialog.find("#zjTable").datagrid('appendRow', rowData);//追加一行
     	   mdDialog.find("#zjxzTable").datagrid('deleteRow',rowIndex);//删除一行
       }
	});
	
	return yxgrid;
}

//设置委员ID
function setspeIds(){
	
	var speids = "";
	var rows = yxgrid.datagrid("getRows");
	for(var i = 0; i < rows.length; i++){
		speids = speids + rows[i].speid + ";"
	}
	
	parent.$.modalDialog.handler.find('#zjxzForm').find("#speids").val(speids);
}

/**
 * 提交选择委员信息
 * @param url
 * @param form
 * @param workflowflag
 */
function submitSpecialistForm(url, form, workflowflag) {
	
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
	var speids = parent.$.modalDialog.handler.find('#zjxzForm').find("#speids").val();
	if(speids == null || speids == ''){
		easyui_warn("请先为该案件选择委员", null);
		return;
	}
	
	form.form("submit", {
		url : url,
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid) {
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {
				easyui_info(result.title,function(){});
				parent.$.modalDialog.handler.dialog('close');
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}