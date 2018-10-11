var baseUrl = contextpath + "sys/notice/SysNoticeManageController/";
var urls = {
		qrySendAllNotice : baseUrl+"qrySendAllNotice.do",
		addNoticeUrl : baseUrl+"addNoticeMsg.do",
		sendNoticeUrl: baseUrl+"sendNoticeMsg.do",
		delNoticeUrl : baseUrl+"delNoticeMsg.do",
		editNoticeUrl : baseUrl+"aditNoticeMsg.do",
		backNoticeUrl : baseUrl+"backNoticeMsg.do",
		showCommonListUrl : baseUrl+"showCommonList.do",
		showHisListUrl:baseUrl+"showHisList.do",
		noticeCommonListUrl :  baseUrl+"noticeCommonList.do",
		getClobContentVal : baseUrl+"getClobContentVal.do",
		showBaseDetail : baseUrl+"showBaseDetail.do"
};

var types = {
	view : "view",
	add : "add",
	edit : "edit",
	remove : "remove",
	redo : "redo",//发布
	undo : "undo"//撤回
};
function showReload() {
	noticeDataGrid.datagrid('reload');
}


//默认加载
$(function() {
	
	
	//状态初始化
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		value : "4",
		data : [{text : "待发布", value : "4"},{text : "已发布", value : "1"}],
		onSelect : function(record) {
			
			switch (record.value) {
			case '1':
				$('#addBtn').linkbutton('enable');
				$('#upBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
				$('#showDetailBtn').linkbutton('enable');
				$('#historyBtn').linkbutton('enable');
				$('#commentBtn').linkbutton('enable');
				$('#backBtn').linkbutton('enable');
				break;
			case '4':
				$('#showDetailBtn').linkbutton('enable');
				$('#historyBtn').linkbutton('disable');
				$('#commentBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				$('#addBtn').linkbutton('enable');
				$('#upBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				
				break;

			default:
				break;
			}
		},
		onHidePanel: function(){  
	        $("#substatus").combobox("setValue",'');
	        var getVal = $('#status').combobox('getValue');        
	          
	        var data;
	        
	        if(getVal == '1')
	        {
	        	data = [{text : "发布", value : "1"}];
	        	//给子状态设置默认值
			    $("#substatus").combobox("setValue",'1');
	        }
	        else
	        {
	        	data = [{text : "全部", value : "5"},{text : "保存", value : "0"},{text : "撤销", value : "2"}];
	        	//给子状态设置默认值
			    $("#substatus").combobox("setValue",'5');
	        }
	        
	        $("#substatus").combobox("loadData",data);
	        doQuery();
	    }  
	});

	//子状态初始化
	$("#substatus").combobox({
		valueField : "value",
		textField : "text",
		editable : false,
		data : [{text : "全部", value : "5"},{text : "保存", value : "0"},{text : "撤销", value : "2"}],
		onSelect : function(record) {
		 	doQuery();
			switch (record.value) {
			case '0':
				$('#showDetailBtn').linkbutton('enable');
				$('#historyBtn').linkbutton('disable');
				$('#commentBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				$('#addBtn').linkbutton('enable');
				$('#upBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				
				break;
			case '2':
				$('#showDetailBtn').linkbutton('enable');
				$('#historyBtn').linkbutton('disable');
				$('#commentBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				$('#addBtn').linkbutton('enable');
				$('#upBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				break;
			case '3':
				$('#showDetailBtn').linkbutton('enable');
				$('#historyBtn').linkbutton('disable');
				$('#commentBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				$('#addBtn').linkbutton('enable');
				$('#upBtn').linkbutton('disable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('disable');
				break;
			case '5':
				$('#showDetailBtn').linkbutton('enable');
				$('#historyBtn').linkbutton('disable');
				$('#commentBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				$('#addBtn').linkbutton('enable');
				$('#upBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				break;
			}
		}
	});
	
	$('#backBtn').linkbutton('disable');
	$('#historyBtn').linkbutton('disable');
	$('#commentBtn').linkbutton('disable');
	//给子状态设置默认值
    $("#substatus").combobox("setValue",'5');
   
    
	//在查询条件中添加删除按钮	
	var icons = {iconCls:"icon-clear"};
	$("#title").textbox("addClearBtn", icons);
//	$("#status").combobox("addClearBtn", icons);
//	$("#substatus").combobox("addClearBtn", icons);
	//加载grid
	loadChangeGrid(urls.qrySendAllNotice);
	hiddenColumn();
	
});


//发送留言公告grid
var noticeDataGrid;
//查询所有发布留言公告列表
function loadChangeGrid(url)
{
	noticeDataGrid = $("#noticeDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		queryParams: {
			status : $("#status").combobox("getValue"),
			title : $("#title").val(),
			releasetime : $("#releasetime").val(),
			messageType : messageType,
			substatus : $("#substatus").combobox("getValue")
		},
		pageSize : 30,
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [[
	            {field : "noticeid",checkbox : true,halign:'center',width:100}
	            ,{field : "title",title : "标题",halign : 'center',	width:250,sortable:true }
	            ,{field : "messageflag",title : "发送短信",halign : 'center',width:50,sortable:true,hidden:true}
	            ,{field : "statusname",title : "状态",halign : 'center',	width:70,sortable:true}
	            ,{field : "noticestatus",title : "状态",halign : 'center',	width:70,sortable:true}
	            ,{field : "receiveusername",title : "接收人",halign : 'center',width:380,sortable:true}
	            ,{field : "receiveusercode",title : "接收人编码",halign : 'center',width:100,sortable:true ,hidden:true}
		        ,{field : "status",title : "状态编码",halign : 'center',width:100,sortable:true ,hidden:true}
		        ,{field : "priorlevelname",title : "重要性",halign : 'center', width : 60,sortable : true}
		        ,{field : "priorlevel",title : "重要性编码",halign : 'center',width:100,sortable:true ,hidden:true}
		        ,{field : "remark",title : "备注",halign : 'center',	width:220,sortable:true}
		        ,{field : "createtime",title : "创建时间",halign : 'center',	width:130,sortable:true}
		        ,{field : "releasetime",title : "发布时间",halign : 'center',width:130,sortable:true}
		        ,{field : "validtime",title : "有效时间",halign : 'center',width:130	,sortable:true}
		        ,{field : "messagetype",title : "类别",halign : 'center',width:100,sortable:true ,hidden:true}
		        ]]
	});
}

function hiddenColumn()
{
	if('2'==messageType)
	{
		$('#noticeDataGrid').datagrid('hideColumn', 'priorlevelname'); 
		$('#noticeDataGrid').datagrid('hideColumn', 'validtime'); 
		$('#noticeDataGrid').datagrid('hideColumn', 'noticestatus'); 
	}
	else if('1'==messageType)
	{
		$('#noticeDataGrid').datagrid('hideColumn', 'receiveusername'); 
		$('#noticeDataGrid').datagrid('hideColumn', 'statusname'); 
	}

}

//点击查询
function doQuery(){
	var startTime = $('#starttime').datetimebox('getValue');
	var endtime = $('#endtime').datetimebox('getValue');
	//查询参数获取
	var param = {
			status : $("#status").combobox("getValue"),
			title : $("#title").val(),
			starttime : startTime,
			endtime : endtime,
			substatus : $("#substatus").combobox("getValue"),
			messageType : messageType
			
		};
	if(endtime < startTime && endtime != "")
	{
		parent.$.messager.alert('提示', "结束时间应大于开始时间！", 'warnning');
		return;
	}
	
	noticeDataGrid.datagrid("load", param);
}


//查看留言公告主体详情
function showDetailMsg()
{
	var rows = noticeDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		parent.$.messager.alert('提示', "请选择一条数据！", 'warnning');
		return;
	}
	var noticeId = rows[0].noticeid;
	var setHeight;
	if(messageType == '1')
	{
		setHeight =450;
	}
	else
	{
		setHeight =460;
	}
	showModalDialog(800,setHeight, "noticeDetailFrom", types.view, noticeDataGrid, "详细信息",
			"sys/notice/SysNoticeManageController/showDetailMsg.do?messageType="+messageType+"&noticeId="+noticeId+"&operType=view", null, true);
}

//查看阅读历史
function listHistory()
{
	var rows = noticeDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		parent.$.messager.alert('提示', "请选择一条数据！", 'warnning');
		return;
	}
	var noticeId = rows[0].noticeid;
	var setHeight;
	if(messageType == '1')
	{
		setHeight =530;
	}
	else
	{
		setHeight =540;
	}
	showlistModalDialog(setHeight, "baseDetailForm", noticeDataGrid, "阅读历史",
			urls.showBaseDetail+"?messageType="+messageType+"&flag=hislist&noticeId="+noticeId,urls.showHisListUrl,true,"hislist");
}


//查看评论详情
function commentDetail()
{
	var rows = noticeDataGrid.datagrid("getSelections");
	if (rows.length != 1) {
		parent.$.messager.alert('提示', "请选择一条数据！", 'warnning');
		return;
	}
	var setHeight;
	if(messageType == '1')
	{
		setHeight =530;
	}
	else
	{
		setHeight =540;
	}
	showlistModalDialog(setHeight, "baseDetailForm", noticeDataGrid, "反馈详情",
			urls.showBaseDetail+"?messageType="+messageType+"&flag=commonlist&noticeId="+rows[0].noticeid,urls.noticeCommonListUrl,true,"commonlist");
}

//添加留言公告
function addNotice() {
	var setHeight;
	if(messageType == '1')
	{
		setHeight =500;
	}
	else
	{
		setHeight =520;
	}
	showModalDialog(800,setHeight, "noticeMsgFrom", types.add, noticeDataGrid, "新增",
			"sys/notice/SysNoticeManageController/addNoticeMsgInit.do?messageType=" + messageType,null,false);
}

//修改留言公告
function editNotice()
{
	var setHeight;
	if(messageType == '1')
	{
		setHeight =500;
	}
	else
	{
		setHeight =520;
	}
	showModalDialog(800,setHeight, "noticeMsgFrom", types.edit, noticeDataGrid, "修改",
			"sys/notice/SysNoticeManageController/addNoticeMsgInit.do?messageType="+messageType,null,true);
}

//撤回留言公告
function backNotice()
{
	//获取选中的行
	var rows = noticeDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		parent.$.messager.alert('提示', "请选择要撤回的数据！", 'warnning');
		return;
	}
	
	$.messager.confirm("确认撤回", "确认要撤回选中的数据", function(record){
		var backIds = "";
		for (var i=0; i<rows.length; i++) {
			if (i != 0)
				backIds += ",";
			backIds += rows[i].noticeid;
		}
	
		if (record){
			$.post(urls.backNoticeUrl, {backIds:backIds}, function(data){
				if(rows.length == 1)
				{
					try {
						data = eval("(" + data + ")");
					} catch (e) {

					}
					easyui_auto_notice(data,function(){
						noticeDataGrid.datagrid("reload");
					},function(){
					},'撤回过程中发生异常！');
				}
				else
				{
					if(data.title== "")
					{
						data.title="撤回成功！";
					}
					parent.$.messager.alert('提示', data.title, 'warnning');
					noticeDataGrid.datagrid("reload");
				}
			}, "json");
		}
	});
}

//发布留言公告
function sendNotice()
{
	//获取选中的行
	var rows = noticeDataGrid.datagrid("getSelections");
	if (rows.length == 0) {
		parent.$.messager.alert('提示', "请选择要发布的数据！", 'warnning');
		return;
	}
	
	$.messager.confirm("确认发布", "确认要发布选中的数据", function(record){
		var noticeIds = "";
		for (var i=0; i<rows.length; i++) {
			if (i != 0)
				noticeIds += ",";
			noticeIds += rows[i].noticeid;
		}
		var msgFlags ="";
		for (var i=0; i<rows.length; i++) {
			if (i != 0)
				msgFlags += ",";
			msgFlags += rows[i].messageflag;
		}
	
		if (record){
			$.post(urls.sendNoticeUrl, {noticeIds:noticeIds,messageFlags:msgFlags,messageType:messageType}, function(data){
				try {
					data = eval("(" + data + ")");
				} catch (e) {

				}
				easyui_auto_notice(data,function(){
					noticeDataGrid.datagrid("reload");
				},function(){
				},'发布过程中发生异常！');
			}, "json");
		}
	});
}

//删除留言公告
function delNotice()
{
	//获取选中的行
	var selRows = noticeDataGrid.datagrid("getSelections");
	if (selRows.length == 0) {
		parent.$.messager.alert('提示',"请选择要删除的数据！", 'warnning');
		return;
	}
	
	$.messager.confirm("确认删除", "确认要删除选中的数据", function(record){
		var delIds = "";
		for (var i=0; i<selRows.length; i++) {
			if (i != 0)
				delIds += ",";
			delIds += selRows[i].noticeid;
		}
		if (record){
			$.post(urls.delNoticeUrl, {delIds:delIds}, function(data){
				try {
					data = eval("(" + data + ")");
				} catch (e) {

				}
				easyui_auto_notice(data,function(){
					noticeDataGrid.datagrid("reload");
				},function(){
				},'删除过程中发生异常！');
			}, "json");
		}
	});
}

var editor;
var mdDialog;
function showlistModalDialog(width, form, dataGrid, title,href,url,fill,type)
{
	if (fill) {
		var rows = dataGrid.datagrid("getSelections");
		if (rows.length != 1) {
			parent.$.messager.alert('提示',"请选择一条数据！", 'warnning');
			return;
		}
	}
	var datagridHigt;
	if(messageType == '1')
	{
		datagridHigt =413;
	}
	else
	{
		datagridHigt =423;
	}
	var icon = 'icon-view';
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 800,
		height : width,
		href : href,
		onLoad : function() {
			
			mdDialog = parent.$.modalDialog.handler;
			
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			f.form("load", row);
			
			//将获取内容的值赋值给div
			$.post(urls.getClobContentVal, {noticeid:row.noticeid}, function(data){
				//给父窗口的控件设值
				mdDialog.find("#contentEdit").html(data);
			}, "json");
			
			
			//加载datagrid
			if('hislist' == type)
			{
				mdDialog.find("#readhistory").datagrid({
					width : 784,
					height : datagridHigt,
					stripe : true,
					singleSelect : true,
					rownumbers : true,
					pagination : true,
					remoteSort:false,
					multiSort:true,
					pageSize : 30,
					url : url+"?noticeid="+rows[0].noticeid+"&messageType="+messageType,
					showFooter : true,
					columns : [ [ 
					{field : "title",title : "标题",halign : 'center',width : 270,sortable:true}
					, {field : "readuser",title : "阅读人",halign : 'center',width : 200,sortable:true}
					, {field : "readtime",title : "阅读时间",halign : 'center',width : 200,sortable:true}
					] ]
				});
			}
			else if('commonlist'== type)
			{
				
				mdDialog.find("#noticecommonlist").datagrid({
					width : 784,
					height : datagridHigt,
					stripe : true,
					singleSelect : true,
					rownumbers : true,
					pagination : true,
					remoteSort:false,
					multiSort:true,
					pageSize : 30,
					url : url+"?noticeid="+rows[0].noticeid+"&messageType="+messageType,
					showFooter : true,
					columns : [ [ 
					{field : "title",title : "标题",halign : 'center',width : 200,sortable:true}
					, {field : "commentusername",title : "反馈人",halign : 'center',width : 100,sortable:true}
					, {field : "commentcontent",title : "反馈内容",halign : 'center',width : 200,sortable:true}
					, {field : "commenttime",title : "反馈时间",halign : 'center',width : 150,sortable:true}
					] ]
				});
			}
			
			//展示上传附件
			showFileDiv(mdDialog.find("#filetd"),false,"NOTICE",row.noticeid,"");
			
		},
		buttons:[{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
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
var priorLevel;
var oldStatus;
var contentVal;
function showModalDialog(width, height,form, operType, dataGrid, title, href, url,
		fill) {

	if (fill) {
		var rows = dataGrid.datagrid("getSelections");
		if (rows.length != 1) {
			parent.$.messager.alert('提示', "请选择一条数据！", 'warnning');
			return;
		}
	}
	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : width,
		height : height,
		href : href,
		onLoad : function() {
			//重要级别
			var jqHandler = parent.$.modalDialog.handler;
			var jLev = jqHandler.find("#priorlevel");
			comboboxFunc(jLev, "NOTICELEVEL", "code", "name");
			
			jLev.combobox({
				onSelect: function(record){
					//选择时，重新赋值
					priorLevel=record.id;
				}
			});
			//初始化findeditor编辑器
			mdDialog= parent.$.modalDialog.handler;
			
			//修改使用
			if (fill) {
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				
				//获取内容信息，在控件中展示
				$.post(urls.getClobContentVal, {noticeid:row.noticeid}, function(data){
					if('view' == operType)
					{
						mdDialog.find("#contentDiv").html(data);
					}
					else
					{
						//给父窗口的控件设值
						window.parent.editor.html(data);
						//将编辑器值赋值给隐藏域content
						contentVal = mdDialog.find("#content").val(data);
						//修改时对选择人查询按钮隐藏
						//mdDialog.find("#tb").hide();
					}
					
				}, "json");
				//修改发送时，获取原来状态保持不变
				oldStatus = row.status;
				//获取重要级别值
				priorLevel = row.priorlevel;
				
				//重新给easyui_validatebox赋值
				var messageVal = row.messageflag;
				if('0' == messageVal)
				{
					parent.$.modalDialog.handler.find('#messageflag2').attr("checked",true);
				}
				else
				{
					parent.$.modalDialog.handler.find('#messageflag1').attr("checked",true);
				}
				
				if('view'==operType)
				{
					//展示上传附件
					showFileDiv(mdDialog.find("#filetd"),false,"NOTICE",row.noticeid,"");
				}
				else if('edit'==operType)
				{
					//展示上传附件
					showFileDiv(mdDialog.find("#filetd"),true,"NOTICE",row.noticeid,"");
				}
			}
				
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
 * @returns {___anonymous3684_3690}
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
	} else if (operType == types.add) {
		buttons = [ 
		{
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var saveUrl = urls.addNoticeUrl+"?status=0&messageType="+messageType+"&priorLevel="+priorLevel+"&opertype=save";
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//将编辑器值赋值给隐藏域content
				mdDialog.find("#content").val(window.parent.editor.html());
				submitForm(saveUrl, form);
			}
		},
		{
			text : "发布",
			iconCls : "icon-redo",
			handler : function() {
				//将编辑器值赋值给隐藏域content
				mdDialog.find("#content").val(window.parent.editor.html());
				var sendUrl = urls.addNoticeUrl+"?status=1&messageType="+messageType+"&priorLevel="+priorLevel+"&opertype=redo";
				parent.$.modalDialog.openner_dataGrid = dataGrid;//添加成功后刷新父窗口
				submitForm(sendUrl, form);
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
	else if(operType == types.edit)
	{
		buttons = [ 
		{
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				//将编辑器值赋值给隐藏域content window.parent.editor//获取父窗口的对象
				mdDialog.find("#content").val(window.parent.editor.html());
			 	var editSaveUrl = urls.editNoticeUrl+"?status="+oldStatus+"&priorLevel="+priorLevel+"&opertype=save&messageType="+messageType;
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(editSaveUrl, form);
			}
		}, 
		{
			text : "发布",
			iconCls : "icon-redo",
			handler : function() {
				//将编辑器值赋值给隐藏域content
				mdDialog.find("#content").val(window.parent.editor.html());
			    var editSendUrl = urls.editNoticeUrl+"?status=1&priorLevel="+priorLevel+"&opertype=redo&messageType="+messageType;
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				submitForm(editSendUrl, form);
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
	else if(operType == types.remove)
	{
		buttons = [{
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
 * 提交表单
 * 
 * @param url
 *  表单url
 */
function submitForm(url, form) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	form.form("submit",
			{
				url : url,
				onSubmit : function() {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					var isValid = form.form('validate');
					
					var title = parent.$.modalDialog.handler.find("#title").val();
					if("" == title)
					{
						parent.$.messager.alert('提示', "请填写标题！", 'warnning');
						parent.$.messager.progress('close');
						return false;
					}
					if(messageType == 1)
					{
						var priorlevel = parent.$.modalDialog.handler.find("#priorlevel").combobox("getValue");
						if("" == priorlevel)
						{
							parent.$.messager.alert('提示', "请选择重要性！", 'warnning');
							parent.$.messager.progress('close');
							return false;
						}
						var validateTime =  parent.$.modalDialog.handler.find("#validtime").datetimebox('getValue');
						validateTime = validateTime.replace(/-/g,"\/");
						
						if("" == validateTime)
						{
							parent.$.messager.alert('提示', "请选择有效时间！", 'warnning');
							parent.$.messager.progress('close');
							return false;
						}
						
						var nowDate = new Date();
						var date_str=nowDate.getFullYear()+'/'+(nowDate.getMonth()+1)+'/'+nowDate.getDate();
						if((new Date(validateTime)-new Date(date_str)) <0)
						{
							parent.$.messager.alert('提示', "有效时间应大于当前系统时间！", 'warnning');
							parent.$.messager.progress('close');
							return false;
						}
						
					}else{
						var personVal = parent.$.modalDialog.handler.find("#receiveusername").val();
						if("" == personVal && messageType == 2)
						{
							parent.$.messager.alert('提示', "请选择接收人！", 'warnning');
							parent.$.messager.progress('close');
							return false;
						}
						if(personVal.length >2000)
						{
							parent.$.messager.alert('提示', "选择的接收人长度介于0至2000之间！", 'warnning');
							parent.$.messager.progress('close');
							return false;
						}
					}
					
					var contentVal = window.parent.editor.html();
					if("" == contentVal)
					{
						var alertMsg ;
						if(messageType == '2')
						{
							alertMsg="请填写留言内容！";
						}
						else
						{
							alertMsg="请填写公告内容！";
						}
						parent.$.messager.alert('提示', alertMsg, 'warnning');
						parent.$.messager.progress('close');
						return false;
					}
					
					if(!isValid)
					{
						parent.$.messager.progress('close');
					}
					return isValid;
				},
				success : function(result) {
					parent.$.messager.progress('close');
					result = $.parseJSON(result);
					if (result.success) {
						
						parent.$.modalDialog.openner_dataGrid.datagrid({'positionScrollTop' : true});
						parent.$.modalDialog.openner_dataGrid.datagrid('reload');
						parent.$.modalDialog.handler.dialog('close');
					} else {
						parent.$.messager.alert('提示', result.title, 'warnning');
					}
				}
			});
}



