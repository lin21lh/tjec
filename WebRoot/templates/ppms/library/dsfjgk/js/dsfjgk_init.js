
var baseUrl = contextpath + "ppms/library/dsfjgk/DsfjgkController/";
var urls = {
	queryUrl : baseUrl + "queryDsfjgk.do",
	addUrl : baseUrl + "addDsfjgk.do",
	addCommitUrl : baseUrl + "addCommitDsfjgk.do",
	editCommitUrl : baseUrl + "editCommitDsfjgk.do",
	deleteUrl : baseUrl + "deleteDsfjgk.do",
	checkUrl : baseUrl + "checkDsfjgk.do",
	detailUrl : baseUrl + "detailDsfjgk.do"
};


//默认加载
$(function() {
	
	loadDsfjgkGrid(urls.queryUrl);
	
});
var dsfjgkDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadDsfjgkGrid(url) {
	dsfjgkDataGrid = $("#dsfjgkDataGrid").datagrid({
		fit : true,
		striped : false,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "checkid",checkbox : true}  
			          ,{field : "dsfjgid",title : "主键",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "organCode",title : "机构代码",halign : 'center',width:140,sortable:true}
			          ,{field : "organName",title : "机构名称",halign : 'center',width:200,sortable:true}
			          ,{field : "consignor",title : "委托方",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "projectManager",title : "项目经理",halign : 'center',width:120,sortable:true}
			          ,{field : "phone",title : "联系电话",halign : 'center',width:150,sortable:true,hidden:true}
			          ,{field : "mobile",title : "手机号码",halign : 'center',width:150,sortable:true}
			          ,{field : "content",title : "服务内容",halign : 'center',width:250,sortable:true}
			          ,{field : "weixin",title : "微信标识",halign : 'center',width:150,sortable:true}
			          ,{field : "sfyx",title : "是否有效",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "sfyxName",title : "是否有效",halign : 'center',width:80,sortable:true}
			          ,{field : "isDelete",title : "是否可删除",halign : 'center',width:80,sortable:true,hidden:true}
			          
		              
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var param = {
			organCode : $("#organCode").val(),
			organName : $("#organName").val(),
			phone : $("#phone").val(),
			weixin : $("#weixin").val()
			
		};
	
	dsfjgkDataGrid.datagrid("load", param);
}

/**
 * 页面元素验证
 * @param dsfjgid
 * @param attributeName 需验证的字段
 * @param attributeCode	字段值
 * @param isAdd
 * @return
 */
function isRepeat(dsfjgid,attributeName,attributeCode,isAdd){
	var flag = true;//默认不重名
	
	$.ajax({
		type : "post",
		url : urls.checkUrl,
		data : {
			dsfjgid : dsfjgid,
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
function dsfjgkAdd(){
	parent.$.modalDialog({
		title : "第三方机构新增",
		iconCls : 'icon-add',
		width : 900,
		height : 330,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"sfyx", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否有效
			mdDialog.find("#sfyx").combobox("setValue","1");
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				if(!isRepeat("",'organ_code',parent.$.modalDialog.handler.find("#organCode").val(),"add")){
					easyui_warn("机构代码已被占用，请重新输入！",null);
					parent.$.modalDialog.handler.find("#organCode").next('span').find('input').focus();
					return ;
				}
				if(!isRepeat("",'organ_name',parent.$.modalDialog.handler.find("#organName").val(),"add")){
					easyui_warn("机构名称已被占用，请重新输入！",null);
					parent.$.modalDialog.handler.find("#organName").next('span').find('input').focus();
					return ;
				}
				submitForm(urls.addCommitUrl,"dsfjgkAddForm","");
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
					dsfjgkDataGrid.datagrid('reload');
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
function dsfjgkEdit(){
	var selectRow = dsfjgkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "第三方机构修改",
		iconCls : 'icon-edit',
		width : 900,
		height : 330,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"sfyx", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否有效
			
			var row = dsfjgkDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#dsfjgkAddForm');
			f.form("load", row);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				if(!isRepeat(parent.$.modalDialog.handler.find("#dsfjgid").val(),'organ_code',parent.$.modalDialog.handler.find("#organCode").val(),"edit")){
					easyui_warn("机构代码已被占用，请重新输入！",null);
					parent.$.modalDialog.handler.find("#organCode").next('span').find('input').focus();
					return ;
				}
				if(!isRepeat(parent.$.modalDialog.handler.find("#dsfjgid").val(),'organ_name',parent.$.modalDialog.handler.find("#organName").val(),"edit")){
					easyui_warn("机构名称已被占用，请重新输入！",null);
					parent.$.modalDialog.handler.find("#organName").next('span').find('input').focus();
					return ;
				}
				submitForm(urls.editCommitUrl,"dsfjgkAddForm","");
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
function dsfjgkDelete(){
	var selectRow = dsfjgkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if (selectRow[0].isDelete>0){
		easyui_warn("此数据已被关联，不允许删除！",null);
		return;
	}
	var row = dsfjgkDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中第三方机构删除？", function(r) {
		if (r) {
			$.post(urls.deleteUrl, {
				dsfjgid : row.dsfjgid
			}, function(result) {
				easyui_auto_notice(result, function() {
					dsfjgkDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}

/**
 * 详情
 * @return
 */
function dsfjgkView(){
	var selectRow = dsfjgkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "第三方机构详情",
		iconCls : 'icon-view',
		width : 900,
		height : 330,
		href : urls.detailUrl,
		onLoad : function() {
			var row = dsfjgkDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#dsfjgkDetailForm');
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

