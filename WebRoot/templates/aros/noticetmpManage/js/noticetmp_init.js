/**
 * 文书模板管理
 * 
 * create by zhangtiantian
 */
var baseUrl = contextpath + "aros/noticetmpManage/controller/NoticetmpController/";
var urls = {
	queryNoticetmpUrl: baseUrl+"queryNoticetmp.do",
	addNoticetmpUrl: baseUrl+"addNoticetmp.do",
	noticetmpDetailUrl: baseUrl+"noticetmpDetail.do",
	delNoticetmpUrl: baseUrl+"delNoticetmp.do",
	saveNoticetmpUrl: baseUrl+"saveNoticetmp.do",
	getClobContentVal: baseUrl+"getClobContentVal.do"
};

var panel_ctl_handles = [{
	panelname : '#queryPanel', // 要折叠的面板id
	gridname : "#noticetmpDataGrid" // 刷新操作函数
}];

var types = {
	add: "add",
	edit: "edit",
	remove: "remove"
};

//默认加载
$(function()
{
	// 流程类型
	comboboxFuncByCondFilter(menuid, "protype", "B_CASEBASEINFO_PROTYPE", "code", "name");
	// 在查询条件中添加删除按钮
	var icons = {iconCls:"icon-clear"};
	$("#protype").combobox("addClearBtn", icons);
	$("#noticename").textbox("addClearBtn", icons);
	$("#starttime").datebox("addClearBtn", icons);
	$("#endtime").datebox("addClearBtn", icons);
	
	// 加载grid
	loadNoticetmpDataGrid(urls.queryNoticetmpUrl);
});

// grid
var noticetmpDataGrid;
// 查询列表
function loadNoticetmpDataGrid(url)
{
	noticetmpDataGrid = $("#noticetmpDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		pageSize : 20,
		queryParams : {
			menuid : menuid
		},
		url: url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[
            {field:"tempid", checkbox:true},
            {field:"noticename", title:"文书模板名称", halign:'center', width:300, sortable:true },
            {field:"typemc", title:"文书类型", halign:'center', width:100, sortable:true},
            {field:"protypemc", title: "流程类型", halign:'center', width:100, sortable:true},
            {field:"nodeidmc", title:"流程节点",halign:'center',width:100, sortable:true},
            {field:"operator", title:"创建人", halign:'center', width:100, sortable:true},
            {field:"opttime", title:"创建日期", halign:'center', width:150, sortable:true},
            {field:"type", title:"文书类型", halign:'center', hidden:true},
            {field:"protype", title: "流程类型", halign:'center', hidden:true},
            {field:"nodeid", title:"流程节点",halign:'center', hidden:true}
		]]
	});
}

//点击查询
function doQuery()
{
	var starttime = $("#starttime").datebox("getValue");
	var endtime = $("#endtime").datebox("getValue");
	
	if(endtime < starttime && endtime != "")
	{
		easyui_warn("结束时间应大于开始时间！", null);
		return;
	}
	
	//查询参数获取
	var param = {
		menuid: menuid,
		protype: $("#protype").combobox("getValue"),
		noticename: $("#noticename").val(),
		starttime: starttime,
		endtime: endtime
	};
	noticetmpDataGrid.datagrid("load", param);
}

// 重新加载
function showReload()
{
	noticetmpDataGrid.datagrid('reload');
}

// 添加
function addNoticetmp()
{
	showModalDialog(types.add, "新增", false);
}

// 修改
function editNoticetmp()
{
	showModalDialog(types.edit, "修改", true);
}

/**
 * 弹出模式窗口
 * 
 * @param title
 *            弹出窗的标题
 * @param href
 *            弹出窗的href
 * @param fill
 *            是否自动填充表单，主要是查看和修改
 */
var mdDialog;
function showModalDialog(operType, title, fill)
{
	if (fill)
	{
		var row = noticetmpDataGrid.datagrid("getChecked");
		if (1 != row.length)
		{
			easyui_warn("请选择一条数据！", null);
			return;
		}
	}
	var icon = "icon-" + operType;
	parent.$.modalDialog({
		title: title,
		iconCls: icon,
		width: 800,
		height: 500,
		href: urls.addNoticetmpUrl,
		onLoad: function()
		{
			// 初始化findeditor编辑器
			mdDialog = parent.$.modalDialog.handler;
			// 文书类型
			comboboxFuncByCondFilter(menuid, "type", "NOTICETMPTYPE", "code", "name", mdDialog);
			// 流程类型
			comboboxFuncByCondFilter(menuid, "protype", "B_CASEBASEINFO_PROTYPE", "code", "name", mdDialog);
			// 流程节点
			comboboxFuncByCondFilter(menuid, "nodeid", "PUB_PRONODEBASEINFO_NODENAME", "code", "name", mdDialog);
			
			// 是否预置
			if (fill)
			{
				mdDialog.find('#noticetmpForm').form("load", row[0]);
				//获取内容信息，在控件中展示
				$.post(urls.getClobContentVal, {tempid:row[0].tempid}, function(data){
					//给父窗口的控件设值
					window.parent.editor.html(data);
					//将编辑器值赋值给隐藏域content
					var contentVal = mdDialog.find("#contents").val(data);
				}, "json");
				
			}
		},
		onDestory: function () {
			showReload();
		},
		buttons: funcOperButtons()
	});
}


//详情
function noticetmpDetail() {
	var row = noticetmpDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var icon = 'icon-view';
	parent.$.modalDialog({
		title: "详情",
		iconCls: icon,
		width: 800,
		height: 500,
		href: urls.noticetmpDetailUrl,
		onLoad: function() {
			var mdDialog = parent.$.modalDialog.handler;
			//获取内容信息，在div中展示
			$.post(urls.getClobContentVal, {tempid:row[0].tempid}, function(data) {
				mdDialog.find("#contents").html(data);
			}, "json");
		},
		onDestory: function () {
			showReload();
		},
		buttons: [{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}]
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
function funcOperButtons() {
	var buttons;
	buttons = [ 
		{
			text: "保存",
			iconCls: "icon-save",
			handler: function() {
				//将编辑器值赋值给隐藏域content
				mdDialog.find("#contents").val(window.parent.editor.html());
				submitForm();
			}
		},
		{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		}];
	return buttons;
}

/**
 * 提交表单
 * 
 * @param url
 *  表单url
 */
function submitForm()
{
	var form = parent.$.modalDialog.handler.find('#noticetmpForm');
	var isValid = form.form('validate');
	if (!isValid)
	{
		return;
	}
	form.form("submit",
	{
		url: urls.saveNoticetmpUrl,
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

//删除
function delNoticetmp()
{
	//获取选中的行
	var row = noticetmpDataGrid.datagrid("getChecked");
	if (1 != row.length)
	{
		easyui_warn("请选择一条数据 !", null);
		return;
	}
	
	$.messager.confirm("确认删除", "确认要删除选中的数据吗？", function(record)
	{
		if (record)
		{
	    	$.ajax({    
	    	    url: urls.delNoticetmpUrl,
	    	    data: {
	    	    	tempid: row[0].tempid
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
