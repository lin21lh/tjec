/**
 * 委员会制度管理
 * 
 * create by zhangtiantian
 */
var baseUrl = contextpath + "aros/xwgl/controller/XwglController/";
var urls = {
	queryContentUrl: baseUrl+"queryContent.do",
	addContentUrl: baseUrl+"addContent.do",
	delContentUrl: baseUrl+"delContent.do",
	showContentUrl: baseUrl+"showContent.do",
	saveContentUrl: baseUrl+"saveContent.do",
	changeStatusUrl: baseUrl+"changeStatus.do",
	getClobContentVal: baseUrl+"getClobContentVal.do"
};

var panel_ctl_handles = [{
	panelname : '#queryPanel', // 要折叠的面板id
	gridname : "#contentDataGrid" // 刷新操作函数
}];

var types = {
	view: "view",
	add: "add",
	edit: "edit",
	remove: "remove"
};

//默认加载
$(function(){
	// 发布状态
	comboboxFuncByCondFilter(menuid, "status", "STATUS", "code", "name");
	// 在查询条件中添加删除按钮
	$("#status").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#title").textbox("addClearBtn", {iconCls:"icon-clear"});
	// 加载grid
	loadContentDataGrid(urls.queryContentUrl + "?contype=" + contype);
});

// grid
var contentDataGrid;
// 查询列表
function loadContentDataGrid(url)
{
	contentDataGrid = $("#contentDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		pageSize : 10,
		queryParams : {
			menuid : menuid
		},
		url: url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[
            {field:"conid", checkbox:true},
            {field:"title", title:"标题", halign:'center',width:500, sortable:true },
            {field:"statusname", title: "状态", halign:'center',width:100, sortable:true},
            {field:"receiveusername", title:"接收人",halign:'center', sortable:true,hidden:true},
            {field:"operator", title:"创建人", halign:'center',width:150, sortable:true},
            {field:"opttime", title:"创建时间", halign:'center',width:250, sortable:true},
	        {field:"contype", title:"类型编码", halign:'center', sortable:true, hidden:true},
	        {field:"status", title:"状态编码", halign:'center', sortable:true, hidden:true},
            {field:"receiveuserid", title:"接收人id", halign:'center', sortable:true , hidden:true}
		]]
	});
}

//点击查询
function doQuery()
{
	//查询参数获取
	var param = {
		menuid: menuid,
		status: $("#status").combobox("getValue"),
		title: $("#title").val()
	};
	contentDataGrid.datagrid("load", param);
}

// 重新加载
function showReload()
{
	contentDataGrid.datagrid('reload');
}

// 添加
function addContent()
{
	showModalDialog(830, 502, "contentForm", types.add, contentDataGrid, "新增", urls.addContentUrl + "?contype=" + contype, false);
}

// 修改
function editContent()
{
	
	showModalDialog(830, 502, "contentForm", types.edit, contentDataGrid, "修改", urls.addContentUrl, true);
}

//查看详情
function showContent()
{
	showModalDialog(830, 502, "contentForm", types.view, contentDataGrid, "详细信息", urls.showContentUrl, true);
}

//删除
function delContent()
{
	//获取选中的行
	var row = contentDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据！", null);
		return;
	}
	
	$.messager.confirm("确认删除", "确认要删除选中的数据吗?", function(record)
	{
		if (record)
		{
	    	$.ajax({    
	    	    url: urls.delContentUrl,
	    	    data: {
	    	    	conid: row[0].conid
	    	    },    
	    	    type: 'post',
	    	    cache: false,
	    	    dataType: 'json',
	    	    success: function(data)
	    	    {
	    	        if (true == data.success)
	    	        {
	    	        	showReload();
	    	        }
	    	        else
	    	        {
	    	        	easyui_warn(data.title, null);
	    	        }
	    	     },
	    	     error: function(data)
	    	     {
	    	    	 easyui_warn("删除过程中发生异常！", null);
	    	     }
	    	});
		}
	});
}
// 发布
function sendContent()
{
	// 获取选中的行
	var row = contentDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据！", null);
		return;
	}
	
	$.messager.confirm("确认发布", "确认要发布选中的数据", function(record)
	{
		if (record)
		{
	    	$.ajax({    
	    	    url: urls.changeStatusUrl,
	    	    data: {
	    	    	conid: row[0].conid,
	    	    	status: "02"
	    	    },    
	    	    type: 'post',
	    	    cache: false,
	    	    dataType: 'json',
	    	    success: function(data)
	    	    {
	    	        if (true == data.success)
	    	        {
	    	        	showReload();
	    	        }
	    	        else
	    	        {
	    	        	easyui_warn(data.title, null);
	    	        }
	    	     },
	    	     error: function(data)
	    	     {
	    	    	 easyui_warn("发布过程中发生异常！", null);
	    	     }
	    	});
		}
	});
}

// 撤消
function backContent()
{
	//获取选中的行
	var row = contentDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据！", null);
		return;
	}
	
	$.messager.confirm("确认撤消", "确认要撤消选中的数据", function(record)
	{
		if (record)
		{
			$.ajax({    
	    	    url: urls.changeStatusUrl,
	    	    data: {
	    	    	conid: row[0].conid,
	    	    	status: "03"
	    	    },    
	    	    type: 'post',
	    	    cache: false,
	    	    dataType: 'json',
	    	    success: function(data) {
	    	        if (true == data.success)
	    	        {
	    	        	showReload();
	    	        }
	    	        else
	    	        {
	    	        	easyui_warn(data.title, null);
	    	        }
	    	     },
	    	     error: function(data)
	    	     {
	    	    	 easyui_warn("撤消过程中发生异常！", null);
	    	     }
	    	});
		}
	});
}

var editor;
var mdDialog;
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
 * @param fill
 *            是否自动填充表单，主要是查看和修改
 */
function showModalDialog(width, height, form, operType, dataGrid, title, href, fill){
	if (fill){
		var row = dataGrid.datagrid("getChecked");
		if (1 != row.length){
			easyui_warn("请选择一条数据！", null);
			return;
		}
		href += "?conid=" + row[0].conid;
	}
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title: title,
		iconCls: icon,
		width: width,
		height: height,
		href: href,
		onLoad: function(){
			//初始化findeditor编辑器
			mdDialog = parent.$.modalDialog.handler;
			//修改使用
			if (fill){
				if ('view' == operType)
				{
					//获取内容信息，在控件中展示
					$.post(urls.getClobContentVal, {conid:row[0].conid}, function(data){
						mdDialog.find("#content").html(data);
					}, "json");
					showFileDiv(mdDialog.find("#files"), false, "XW", row[0].conid, "");
				}
				else
				{
					mdDialog.find('#' + form).form("load", row[0]);
					showFileDiv(mdDialog.find("#files"), true, "XW", row[0].conid, "");
					//获取内容信息，在控件中展示
					$.post(urls.getClobContentVal, {conid:row[0].conid}, function(data){
						//给父窗口的控件设值
						window.parent.editor.html(data);
						//将编辑器值赋值给隐藏域content
						var contentVal = mdDialog.find("#content").val(data);
					}, "json");
				}
			}
			
			//获取内容信息，在控件中展示
			$.post(urls.getClobContentVal, {conid:row[0].conid}, function(data){
				//给父窗口的控件设值
				window.parent.editor.html(data);
				
				//将编辑器值赋值给隐藏域content
				var contentVal = mdDialog.find("#content").val(data);
			}, "json");
		},
		buttons: funcOperButtons(operType, dataGrid, form)
	});

}

/**
 * 根据操作类型来获取操作按钮
 * 
 * @param operType
 *            操作类型
 * @param url
 *            对应的操作URL
 * @returns 
 */
function funcOperButtons(operType, dataGrid, form) {
	var buttons;
	if (operType == types.view)
	{
		buttons = [{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}];
	}
	else
	{
		buttons = [ 
		{
			text: "保存",
			iconCls: "icon-save",
			handler: function() {
				var saveUrl = urls.saveContentUrl;
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//将编辑器值赋值给隐藏域content
				mdDialog.find("#content").val(window.parent.editor.html());
				submitForm(saveUrl, form);
			}
		},
		{
			text: "发布",
			iconCls: "icon-redo",
			handler: function() {
				//将编辑器值赋值给隐藏域content
				mdDialog.find("#content").val(window.parent.editor.html());
				var sendUrl = urls.saveContentUrl + "?flag=send";
				parent.$.modalDialog.openner_dataGrid = dataGrid;//添加成功后刷新父窗口
				submitForm(sendUrl, form);
			}
		},
		{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}];
	}
	return buttons;
}

/**
 * 提交表单
 * 
 * @param url
 *  表单url
 */
function submitForm(url, form)
{
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid)
	{
		return;
	}
	form.form("submit",
	{
		url: url,
		onSubmit: function() {
			parent.$.messager.progress({
				title: '提示',
				text: '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid)
			{
				return;
			}
		},
		success: function(result)
		{
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success)
			{
				parent.$.modalDialog.handler.dialog('close');
				showReload();
			}
			else
			{
				easyui_warn(result.title, null);
			}
		}
	});
}
