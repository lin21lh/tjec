//请求路径
var speSugbaseUrl = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/";
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";

var urls = {
	gridUrl: speSugbaseUrl + "querySpeDealCaseList.do",
	editUrl:speSugbaseUrl  + "querySpesugbaseinfo.do",
	editSaveUrl:speSugbaseUrl + "saveSpeSugInfo.do",
	saveSpesugbaseinfoUrl:speSugbaseUrl + "saveSpesugbaseinfo.do"
};

var panel_ctl_handles = [{
	panelname:"#spesugbaseinfoPanel", 		// 要折叠的面板id
	gridname:"#xzfyReqDataGrid",  			// 刷新操作函数
	buttonid:"#openclose"	 				// 折叠按钮id
}];

//默认加载
$(function() {
	
	$('#spesugbaseinfoPanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");
	
	var icons = {iconCls:"icon-clear"};
	$("#casecode").textbox("addClearBtn", {iconCls:"icon-clear"});
	$("#casetype").textbox("addClearBtn", {iconCls:"icon-clear"});
	
	//加载Grid数据
	loadxzfyReqDataGrid(urls.gridUrl);
});

var xzfyDataGrid;

function showReload(){
	xzfyDataGrid.datagrid("reload");
}

//加载grid列表
function loadxzfyReqDataGrid(url) {
	
	xzfyDataGrid = $("#xzfyReqDataGrid").datagrid({
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
			editSpeSug(rowIndex);
        },
        onClickRow:function(rowIndex, rowData){
 //       	viewTime();
        	viewFlow();
        },
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:300, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:80, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:200, sortable:true},
		  {field:"casetypeMc", title:"申请复议事项类型", halign:"center", width:120, sortable:true}
        ]]
	});
}

/**
 * 条件查询
 */
function toQuery(){
	
	var param = {
		casetype:$("#casetype").combobox('getValue'),
		startTime:$("#startTime").datebox('getValue'),
		endTime:$("#endTime").datebox('getValue'),
		casecode:$("#casecode").val(),
		menuid:menuid,
		firstNode:true,
		lastNode:false
	};
	
	xzfyDataGrid.datagrid("load", param);
}

/**
 * 委员意见处理
 * @param index
 */
function editSpeSug(index){
	
	$("#xzfyReqDataGrid").datagrid("selectRow", index);
    var row = $('#xzfyReqDataGrid').datagrid("getSelected"); 
    
    if (row){
    	parent.$.modalDialog({
    		title:"委员意见录入",
    		width:800,
    		height:600,
    		href:urls.editUrl + "?caseid=" + row.caseid,
    		onLoad:function() {
    			var mdDialog = parent.$.modalDialog.handler;
    			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
    			
    			loadCaseFileDataGrid(mdDialog, row);
    		},
    		buttons : [ {
    			text : "保存",
    			iconCls : "icon-save",
    			handler : function() {
    				var mdDialog = parent.$.modalDialog.handler;
    				submitSpesugbaseinfoForm(urls.saveSpesugbaseinfoUrl, "spesugbaseinfoForm", "");
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
}

/**
 * 加载案件卷宗列表
 */
function loadCaseFileDataGrid(mdDialog, rowData) {
	
	mdDialog.find("#allDownBtn").attr("href", "#");
	
	var caseid = rowData.caseid;
	
	caseFileDataGrid = mdDialog.find("#caseFileTab").datagrid({
		height : 469,
		fit : false,
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

/**
 * 保存委员评论
 * @param url
 * @param form
 * @param workflowflag
 */
function submitSpesugbaseinfoForm(url, form, workflowflag) {
	
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid) {
		easyui_warn("请输入意见", null);
		return;
	}
	
	form.form("submit", {
		url:url,
		onSubmit:function() {
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

/**
 * 打开委员意见录入页面
 * @param row
 */
function openSpeSugDialog(row){
	
	parent.$.modalDialog({
		title:"委员意见录入",
		width:800,
		height:600,
		href:urls.editUrl + "?caseid=" + row.caseid,
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			
			/*var f = parent.$.modalDialog.handler.find('#caseInfo');
			f.form("load", row);
			parent.$.modalDialog.handler.find('#caseid').val(row.caseid);*/
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