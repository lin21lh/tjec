var baseUrl = contextpath + "aros/jzgl/controller/CaseFileManageController/";
var urls = {
	saveUrl : baseUrl + "saveAddCaseFile.do",
	placeOnFile: baseUrl + "palceOnFile.do"
};

var caseid;

var caseListDataGrid = null;
var caseFileDataGrid = null;

$(function() {	
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");
	// 在查询条件中添加删除按钮
	var icons = {iconCls:"icon-clear"};
	$("#csaecode").textbox("addClearBtn", icons);
	$("#casetype").combobox("addClearBtn", icons);
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	// 加载数据表数据
	loadCaseListDataGrid();
});

/**
 * 加载表字段定义数据表格
 */
function loadCaseListDataGrid() {
	caseListDataGrid = $("#caseListTab").datagrid({
		fit : true,
		border : false,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		pageSize : 30,
		url : "queryCaseBaseinfoList.do?caseid=" + caseid,
		idField : 'caseid',
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_list",
		columns : [ [ {field : "caseid",checkbox : true
		
		},  {
			field : "csaecode",
			title : "案件编号",
			halign : 'center',
			width : 200,
			sortable : true
		}, {
			field : "appname",
			title : "申请人",
			halign : 'center',
			width : 150,
			sortable : true
		}, {
			field : "defname",
			title : "被申请人",
			halign : 'center',
			width : 150,
			sortable : true
		}, {
			field : "casetype",
			title : "复议事项类型",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "appdate",
			title : "申请日期",
			halign : 'center',
			width : 100,
			sortable : true
		} ] ],
		onSelect : function(rowIndex, rowData) {
			loadCaseFileDataGrid(rowData);
		},
		onLoadSuccess : function(data) {
			caseListDataGrid.datagrid('clearSelections'); //数据表列表加载成功之后清除以前选项
			caseListDataGrid.datagrid('selectRow', 0);
		},
		onLoadError : function() {
			
		}
	});
}

/**
 * 加载表字段定义数据表格
 */
function loadCaseFileDataGrid(rowData) {
	var caseid = rowData.caseid;
	$("#allDownBtn").attr("href", "#");
	if(rowData.state == cantAddFile){
		$("#addBtnDiv").attr("hidden",true);
	}else{
		$("#addBtnDiv").attr("hidden",false);
	}
	caseFileDataGrid = $("#caseFileTab").datagrid({
		fit : true,
		border : false,
		singleSelect : true,
		idField : 'id',
		url : contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do?caseid="+caseid+"&menuid="+menuid,
		loadMsg : "正在加载，请稍候......",
		columns : [ [ {
			field : "id",
			checkbox : true
		}, {
			field : "csaecode",
			halign : 'center',
			title : "案件编号",
			width : 200,
		},{  
			field : "doctype",
			halign : 'center',
			title : "文档名称",
			width : 200,
		},{  
			field : "protype",
			halign : 'center',
			title : "流程类型",
			width : 100,
			hidden:true
		},{  
			field : "buildtime",
			halign : 'center',
			title : "生成时间",
			width : 120,
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
			$("#s_caseid").val(caseid);
			var url = contextpath + "aros/jzgl/controller/CaseFileManageController/downLoadAllFile.do?caseid=" + caseid;
			// 设置全部下载按钮
			if($("#caseFileTab").datagrid("getRows").length >0){
				$("#allDownBtn").attr("href", url);
			}
		},
		onLoadError : function() {
		}
	});
}

//提交表单
function submitForm(url, fromid) {

	var form = parent.$.modalDialog.handler.find('#' + fromid);
	var isValid = form.form('validate');
	if (!isValid) {
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
				caseFileDataGrid.datagrid('reload');
				parent.$.modalDialog.handler.dialog('close');
				easyui_info(result.title,function(){});
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

/**
 * 
 */
function fileAdd(){
	var row = $('#caseListTab').datagrid("getSelected"); 
    var caseid = row.caseid;
	parent.$.modalDialog({
		title : "文档上传",
		width : 400,
		height : 200,
		href : "aros/jzgl/controller/CaseFileManageController/addCaseFile.do?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				submitForm(urls.saveUrl, "addCaseFileForm");
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
//查询
function toQuery(){
	var param = {
			appname : $("#appname").val(),
			defname : $("#defname").val(),
			csaecode : $("#csaecode").val(),
			casetype : $("#casetype").combobox('getValue'),
		};
	caseListDataGrid.datagrid("load", param);
}
// 案件归档
function placeOnFile(){
	var viewUrl = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/view.do";
	var selectRow = caseListDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一个案件", null);
		return;
	}
	easyui_solicit('确定要将选中的案件归档吗？', function(c) {
		if (c != undefined) { 
			if (c) {
				var row = caseListDataGrid.datagrid("getSelections")[0];
				var caseid = row.caseid;
				$.post(urls.placeOnFile, {
					caseid : caseid,
				}, function(result) {
					easyui_auto_notice(result, function() {
						caseListDataGrid.datagrid("reload");
					});
				}, "json");
			}
		}
	});
}

function printAllFile(){
	easyui_info("敬请期待！！",function(){});
}

