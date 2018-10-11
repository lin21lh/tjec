
var baseUrl = contextpath + "ppms/library/zbk/ZbkController/";
var urls = {
	queryUrl : baseUrl + "queryZbk.do",
	addUrl : baseUrl + "addZbk.do",
	addCommitUrl : baseUrl + "addCommitZbk.do",
	editCommitUrl : baseUrl + "editCommitZbk.do",
	deleteUrl : baseUrl + "deleteZbk.do",
	checkUrl : baseUrl + "checkZbk.do",
	detailUrl : baseUrl + "detailZbk.do"
};


//默认加载
$(function() {
	comboboxFuncByCondFilter(menuid,"sfyx", "SYS_TRUE_FALSE", "code", "name");//是否联合体
	$("#sfyx").combobox("addClearBtn", {iconCls:"icon-clear"});
	comboboxFuncByCondFilter(menuid,"zblb", "PROZBLB", "code", "name");//是否联合体
	$("#zblb").combobox("addClearBtn", {iconCls:"icon-clear"});
	
	loadZbkGrid(urls.queryUrl);
});
var zbkDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadZbkGrid(url) {
	zbkDataGrid = $("#zbkDataGrid").datagrid({
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
			          ,{field : "zbkid",title : "主键",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "zbmc",title : "指标名称",halign : 'center',width:200,sortable:true}
			          ,{field : "zblbName",title : "指标类别",halign : 'center',width:150,sortable:true}
			          ,{field : "sfyxName",title : "是否有效",halign : 'center',width:100,sortable:true}
			          ,{field : "zbms",title : "指标描述",halign : 'center',width:300,sortable:true}
			          ,{field : "zblb",title : "指标类别",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "sfyx",title : "是否有效",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "isDelete",title : "是否可删除",halign : 'center',width:120,sortable:true,hidden:true}
		              
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var param = {
			zbmc : $("#zbmc").val(),
			zblb : $("#zblb").combobox("getValue"),
			sfyx : $("#sfyx").combobox("getValue")
			
		};
	zbkDataGrid.datagrid("load", param);
}

/**
 * 重名验证
 * @param zbkid
 * @param attributeName
 * @param attributeCode
 * @param isAdd
 * @return
 */
function isRepeat(zbkid,attributeName,attributeCode,isAdd){
	var flag = true;//默认不重名
	
	$.ajax({
		type : "post",
		url : urls.checkUrl,
		data : {
			zbkid : zbkid,
			attributeName : attributeName,
			attributeCode : attributeCode,
			flag : isAdd
		},
		async : false,
		success : function(result){
			if (result.success) {
				
			} else {
				flag = false;
			}
		}
	});
	return flag;
}

/**
 * 新增
 */
function zbkAdd(){
	parent.$.modalDialog({
		title : "指标新增",
		iconCls : 'icon-add',
		width : 900,
		height : 330,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"sfyx", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否有效
			comboboxFuncByCondFilter(menuid,"zblb", "PROZBLB", "code", "name", mdDialog);//指标类别
			mdDialog.find("#sfyx").combobox("setValue","1");
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				if(!isRepeat("",'zbmc',parent.$.modalDialog.handler.find("#zbmc").val(),"add")){
					easyui_warn("指标名称已被占用，请重新输入！",null);
					parent.$.modalDialog.handler.find("#zbmc").next('span').find('input').focus();
					return ;
				}
				submitForm(urls.addCommitUrl,"zbkAddForm","");
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
					zbkDataGrid.datagrid('reload');
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
function zbkEdit(){
	var selectRow = zbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "指标修改",
		iconCls : 'icon-edit',
		width : 900,
		height : 330,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"sfyx", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否有效
			comboboxFuncByCondFilter(menuid,"zblb", "PROZBLB", "code", "name", mdDialog);//指标类别
			
			var row = zbkDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#zbkAddForm');
			f.form("load", row);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				if(!isRepeat(parent.$.modalDialog.handler.find("#zbkid").val(),'zbmc',parent.$.modalDialog.handler.find("#zbmc").val(),"edit")){
					easyui_warn("指标名称已被占用，请重新输入！",null);
					parent.$.modalDialog.handler.find("#zbmc").next('span').find('input').focus();
					return ;
				}
				submitForm(urls.editCommitUrl,"zbkAddForm","");
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
function zbkDelete(){
	var selectRow = zbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if (selectRow[0].isDelete>0){
		easyui_warn("此数据已被关联，不允许删除！",null);
		return;
	}
	var row = zbkDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中指标删除？", function(r) {
		if (r) {
			$.post(urls.deleteUrl, {
				zbkid : row.zbkid
			}, function(result) {
				easyui_auto_notice(result, function() {
					zbkDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}

/**
 * 详情
 * @return
 */
function zbkView(){
	var selectRow = zbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "指标详情",
		iconCls : 'icon-view',
		width : 900,
		height : 330,
		href : urls.detailUrl,
		onLoad : function() {
			var row = zbkDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#zbkDetailForm');
			f.form("load", row);
		},
		buttons : [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
