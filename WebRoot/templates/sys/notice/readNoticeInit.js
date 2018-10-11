var baseUrl = contextpath + "sys/notice/SysNoticeManageController/";
var urls = {
		qryReadAllMessage : baseUrl+"qryReadAllMessage.do",
		feedbackNotice : baseUrl+"feedbackNotice.do",
		qryReadAllNotice : baseUrl+"qryReadAllNotice.do",
		getClobContentVal : baseUrl+"getClobContentVal.do",
		showCommonListUrl : baseUrl+"showCommonList.do",
		showCommonBySelfUrl :  baseUrl+"showCommonBySelf.do",
		showCommonDialogUrl : baseUrl+"showCommonDialog.do"
		
};

//默认加载
$(function() {

	//根据类型区分，请求url不同
	var reqUrl;
	if(messageType == '2')
	{
		reqUrl=urls.qryReadAllMessage;
	}
	else
	{
		reqUrl=urls.qryReadAllNotice;
	}
	
	//加载grid
	loadChangeGrid(reqUrl);
	hiddenColumn();

	//在查询条件中添加删除按钮
	$("#title").combobox("addClearBtn", {iconCls:"icon-clear"});
});

function showReload() {
	readDataGrid.datagrid('reload');
}


//发送留言公告grid
var readDataGrid;
function loadChangeGrid(url)
{
	readDataGrid = $("#readMsgDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		queryParams: {
			title : $("#title").val(),
			starttime : $("#starttime").datetimebox('getValue'),
			endtime : $("#endtime").datetimebox('getValue'),
			messageType : messageType
		},
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[
		            {field : "noticeid",checkbox : true,halign:'center',width:150} 
		            ,{field : "title",title : "标题",halign : 'center',	width:300,sortable:true }
		            ,{field : "username",title : "发布人",halign : 'center',	width:150,sortable:true }
		            ,{field : "usercode",title : "发布人编码",halign : 'center',	width:250,sortable:true,hidden:true}
		            ,{field : "releasetime",title : "发布时间",halign : 'center',	width:150,sortable:true }
		            ,{field : "validtime",title : "有效时间",halign : 'center',	width:130,sortable:true}
		            ,{field : "status",title : "状态",halign : 'center',	width:100,sortable:true }
		            ,{field : "priorlevelname",title : "重要性",halign : 'center',	width:150,sortable:true }
		            ,{field : "flag",title : "阅读回执",halign : 'content',width:100,sortable:true}
			        ,{field : "remark",title : "备注",halign : 'center',	width:220,sortable:true,hidden:true}
			        ,{field : "createtime",title : "创建时间",halign : 'center',	width:130,sortable:true	,hidden:true}
			        ,{field : "receiveid",title : "接收编码",halign : 'center',	width:130	,sortable:true,hidden:true}
			        ,{field : "receiveuser",title : "接收人编码",halign : 'center',	width:130	,sortable:true,hidden:true}
			        ,{field : "readflag",title : "阅读标示",halign : 'center',	width:130	,sortable:true,hidden:true}
			        ,{field : "readtime",title : "阅读时间",halign : 'center',	width:130	,sortable:true,hidden:true}
			        ]]
	});
}

function hiddenColumn()
{
	if('2'==messageType)
	{
		$('#readMsgDataGrid').datagrid('hideColumn', 'priorlevelname'); 
		$('#readMsgDataGrid').datagrid('hideColumn', 'validtime'); 
	}
	else if('1'==messageType)
	{
		$('#readMsgDataGrid').datagrid('hideColumn', 'flag'); 
	}
}


//查询按钮
function doQuery()
{
	var startTime = $('#starttime').datetimebox('getValue');
	var endTime = $('#endtime').datetimebox('getValue');
	//查询参数获取
	var param = {
			title : $("#title").val(),
			starttime : startTime,
			endtime : endTime,
			messageType : messageType
		};
	if(startTime > endTime && endTime != "")
	{
		parent.$.messager.alert('提示', "结束时间应大于开始时间！", 'warnning');
		return;
	}
	readDataGrid.datagrid("load", param);
	
}

var types = {
		view : "view",//查阅
		edit : "edit"//反馈
	};

//反馈信息
function feedbackNotice()
{
	var rows = readDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		parent.$.messager.alert('提示', "请选择一条数据！", 'warnning');
		return;
	}
	
	showModalDialog(570, "readNoticeForm", types.edit, readDataGrid, "反馈信息",
			"sys/notice/SysNoticeManageController/feedbackNoticeInit.do?messageType="+messageType+"&noticeId="+rows[0].noticeid+"&readflag="+rows[0].readflag, urls.feedbackNotice);
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
function showModalDialog(width, form, operType, dataGrid, title, href, url) {

	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : '810',
		height :width,
		href : href,
		onLoad : function() {
			
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			f.form("load", row);
		
			//获取内容信息，在控件中展示
			$.post(urls.getClobContentVal, {noticeid:row.noticeid}, function(data){
				//window.parent.editor.html(data);
				mdDialog.find("#contentEdit").html(data);
				//将编辑器值赋值给隐藏域content
				contentVal = mdDialog.find("#content").val(data);
			}, "json");
			
			//展示上传附件
			showFileDiv(mdDialog.find("#filetd"),false,"NOTICE",row.noticeid,"");
			
			//判断是否上传附件datagrid展示不同的高度
			var fileCount =mdDialog.find("#fileCount").val();
			var setHeight;
			if(fileCount > 0)
			{
				setHeight='134px';
			}
			else
			{
				setHeight='174px';
			}
			//将layout折叠
			//mdDialog.find("#layoutDiv").layout('collapse','south');
			
			mdDialog.find("#commonlist").datagrid({
				height:setHeight,
				width:'100%',
				stripe : true,
				singleSelect : true,
				rownumbers : true,
				pagination : true,
				remoteSort:false,
				multiSort:true,
				pageSize : 30,
				url : urls.showCommonBySelfUrl+"?noticeid="+row.noticeid+"&messageType="+messageType,
				loadMsg : "正在加载，请稍后......",
				toolbar : "#toolbar_common",
				showFooter : true,
				columns : [ [ 
				{field : "title",title : "标题",halign : 'center',width : 200,sortable:true}
				, {field : "commentusername",title : "反馈人",halign : 'center',width : 140,sortable:true}
				, {field : "commentcontent",title : "反馈内容",halign : 'center',width : 250,sortable:true}
				, {field : "commenttime",title : "反馈时间",halign : 'center',width : 150,sortable:true}
				] ]
			});
			
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
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
function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if (operType == types.view) {
		buttons = [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	else if(operType == types.edit)
	{
		buttons = [ {
			text : "反馈",
			iconCls : "icon-edit",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//弹出反馈信息框
				showCommonModalDialog(parent.$('#commontService'),url);
			}
		},
		{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	return buttons;
}

/**
 * 展示反馈内容
 * @param div
 * @param dialog
 * @return
 */
function showCommonModalDialog(div,url)
{
	div.dialog(
	{
		title : "反馈信息",
		width : 500,
		height : 200,
		href : urls.showCommonDialogUrl,
		iconCls : 'icon-edit',
		modal : true,
		buttons : [
			{
				text : "确定",
				iconCls : "icon-save",
				handler : function() {
				    var commonCxt = parent.parent.$("#commentContent").val();
				    if(commonCxt == '' || commonCxt == null)
				    {
				    	parent.$.messager.alert('提示', "请填写反馈内容！", 'warnning');
				    }
				    else
				    {
				    	if(commonCxt.length > 250)
				    	{
				    		parent.$.messager.alert('提示', "反馈内容长度必须介于0和250之间！", 'warnning');
				    		return;
				    	}
				    	
				    	//ajax请求，添加评论信息
						$.post(url, {
							commentContent:commonCxt,
							receiveid:parent.$("#receiveid").val(),
							noticeid:parent.$("#noticeid").val()
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$('#commontService').dialog(
								'close');
								//刷新历史反馈列表
								var param = {
										noticeid : parent.$("#noticeid").val(),
										messageType : messageType
										
									};
								parent.$('#commonlist').datagrid("load", param);
							});
						}, "json");
				    }
					
				}
			},
			{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$('#commontService').dialog(
							'close');
				}
			}]
	});
}



