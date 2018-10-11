
var baseUrl = contextpath + "ppms/wdjg/WdjgController/";
var urls = {
	queryUrl : baseUrl + "queryWdjg.do",
	addUrl : baseUrl + "addWdjg.do",
	addCommitUrl : baseUrl + "addCommitWdjg.do",
	editCommitUrl : baseUrl + "editCommitWdjg.do",
	
	deleteUrl : baseUrl + "deleteWdjg.do"
};


//默认加载
$(function() {
	comboboxFuncByCondFilter(menuid,"xmhj", "PROXMDQHJ", "code", "name");//项目环节
	$("#xmhj").combobox("addClearBtn", {iconCls:"icon-clear"});
	comboboxFuncByCondFilter(menuid,"hjfl", "PROXMHJFL", "code", "name");//环节分类
	$("#hjfl").combobox("addClearBtn", {iconCls:"icon-clear"});
	
	loadWdjgGrid(urls.queryUrl);
});
var wdjgDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadWdjgGrid(url) {
	wdjgDataGrid = $("#wdjgDataGrid").datagrid({
		fit : true,//自动在父容器最大范围内调整大小
	//	striped : true,//是否显示斑马线效果
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,//是否允许多列排序
		pageSize : 30,
		queryParams: {
			
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "checkid",checkbox : true}  
			          ,{field : "wdjgid",title : "主键",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "xmhj",title : "项目环节",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "xmhjName",title : "项目环节",halign : 'center',width:100,sortable:true}
			          ,{field : "hjfl",title : "环节分类",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "hjflName",title : "环节分类",halign : 'center',width:100,sortable:true}
			          ,{field : "wdmc",title : "文档名称",halign : 'center',width:200,sortable:true}
			          ,{field : "glbm",title : "关联表名",halign : 'center',width:150,sortable:true}
			          ,{field : "glzd",title : "关联字段",halign : 'center',width:100,sortable:true}
			          ,{field : "plsx",title : "排列顺序",halign : 'center',width:100,sortable:true}
			          ,{field : "bz",title : "备注",halign : 'center',width:200,sortable:true}
		              
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var param = {
			xmhj : $("#xmhj").combobox("getValue"),
			hjfl : $("#hjfl").combobox("getValue"),
			wdmc : $("#wdmc").val()
			
		};
	wdjgDataGrid.datagrid("load", param);
}


/**
 * 新增
 */
function wdjgAdd(){
	parent.$.modalDialog({
		title : "文档结构新增",
		iconCls : 'icon-add',
		width : 900,
		height : 430,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"xmhj", "PROXMDQHJ", "code", "name", mdDialog);//项目当前环节
			comboboxFuncByCondFilter(menuid,"hjfl", "PROXMHJFL", "code", "name", mdDialog);//项目环节分类
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				submitForm(urls.addCommitUrl,"wdjgAddForm","");
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

/**
 * 新增、修改表单提交
 * @param url
 * @param form
 * @param workflowflag
 * @return
 */
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	var mdDialog = parent.$.modalDialog.handler;
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	
	form.form("submit",
			{
		url : url,
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			return true;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {
				easyui_auto_notice(result, function() {
					wdjgDataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				});
				
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
			}
		});
}


/**
 * 修改
 */
function wdjgEdit(){
	var selectRow = wdjgDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "文档结构修改",
		iconCls : 'icon-edit',
		width : 900,
		height : 430,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"xmhj", "PROXMDQHJ", "code", "name", mdDialog);//是否有效
			comboboxFuncByCondFilter(menuid,"hjfl", "PROXMHJFL", "code", "name", mdDialog);//项目环节分类
			
			var row = wdjgDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#wdjgAddForm');
			f.form("load", row);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				
				submitForm(urls.editCommitUrl,"wdjgAddForm","");
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

/**
 *删除
 */
function wdjgDelete(){
	var selectRow = wdjgDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = wdjgDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中指标删除？", function(r) {
		if (r) {
			$.post(urls.deleteUrl, {
				wdjgid : row.wdjgid
			}, function(result) {
				easyui_auto_notice(result, function() {
					wdjgDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}

