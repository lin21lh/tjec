
var baseUrl = contextpath + "ppms/mesNotification/MesNotificationController/";
var urls = {
	queryUrl : baseUrl + "queryMessage.do",
	addUrl : baseUrl + "addMessage.do",
	addCommitUrl : baseUrl + "addCommitMessage.do",
	editCommitUrl : baseUrl + "editCommitMessage.do",
	deleteUrl : baseUrl + "deleteMessage.do",
	sendUrl : baseUrl + "sendMessage.do",
	
	detailUrl : baseUrl + "detailMessage.do"
};


//默认加载
$(function() {
	
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "0",
		editable : false,
		data : [{text : "保存未发送", value : "0"}, {text : "已发送", value : "1"}],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
				case '0':
					$('#editBtn').linkbutton('enable');
					$('#sendBtn').linkbutton('enable');
					$('#delBtn').linkbutton('enable');
					break;
				case '1':
					$('#editBtn').linkbutton('disable');
					$('#sendBtn').linkbutton('disable');
					$('#delBtn').linkbutton('disable');
					break;
				default:
					break;
			}
		}
	});
	loadMessageGrid(urls.queryUrl);
});
var messageDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadMessageGrid(url) {
	messageDataGrid = $("#messageDataGrid").datagrid({
		fit : true,//自动在父容器最大范围内调整大小
	//	striped : true,//是否显示斑马线效果
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,//是否允许多列排序
		pageSize : 30,
		queryParams: {
			status : $("#status").combobox("getValue")
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "checkid",checkbox : true}  
			          ,{field : "messageid",title : "主键",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "messageType",title : "消息类型",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "messageContext",title : "消息内容",halign : 'center',width:250,sortable:true}
			          ,{field : "receiveName",title : "接收人",halign : 'center',width:200,sortable:true}
			          ,{field : "messageTypeName",title : "消息类型",halign : 'center',width:100,sortable:true}
			          ,{field : "status",title : "状态",halign : 'center',width:80,sortable:true,
			        	  formatter : function(value,row,index){
			        	  switch(value){
			        	  case '0':
			        		  return "保存未发送";
			        		  break;
			        	  case '1':
			        		  return "已发送";
			        		  break;
			        	  }
			          	}
			          }
			          ,{field : "remark",title : "备注",halign : 'center',width:200,sortable:true}
			          ,{field : "createUserName",title : "创建人",halign : 'center',width:120,sortable:true}
			          ,{field : "createTime",title : "创建时间",halign : 'center',width:150,sortable:true}
			          ,{field : "createUser",title : "创建人",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "updateUserName",title : "修改人",halign : 'center',width:120,sortable:true}
			          ,{field : "updateTime",title : "修改时间",halign : 'center',width:150,sortable:true}
			          ,{field : "updateUser",title : "修改人",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "viewId",title : "视图id",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "receiveId",title : "接收id",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "receiveLb",title : "接收类别",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "receivePhone",title : "接收电话",halign : 'center',width:120,sortable:true,hidden:true}
			          
		              
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var createTimeBegin = $('#createTimeBegin').datetimebox('getValue');
	var createTimeEnd = $('#createTimeEnd').datetimebox('getValue');
	
	if(createTimeEnd < createTimeBegin && createTimeEnd != "")
	{
		parent.$.messager.alert('提示', "结束时间应大于开始时间！", 'warnning');
		return;
	}
	var param = {
			status : $("#status").combobox("getValue"),
			createTimeBegin : createTimeBegin,
			createTimeEnd : createTimeEnd,
			messageContext : $("#messageContext").val(),
			receive : $("#receive").val()
			
		};
	messageDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function messageAdd(){
	parent.$.modalDialog({
		title : "消息通知新增",
		iconCls : 'icon-add',
		width : 900,
		height : 430,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"messageType", "MESSAGE_TYPE", "code", "name", mdDialog);//消息类型
			
		},
		buttons : [
		{
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#status").val("0");//设置状态标识为保存
				submitForm(urls.addCommitUrl,"messageAddForm","");
			}
		},{
			text : "发送",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#status").val("1");//设置状态标识为发送
				submitForm(urls.addCommitUrl,"messageAddForm","");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} 
		]
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
					messageDataGrid.datagrid('reload');
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
function messageEdit(){
	var selectRow = messageDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "消息通知修改",
		iconCls : 'icon-edit',
		width : 900,
		height : 430,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"messageType", "MESSAGE_TYPE", "code", "name", mdDialog);//消息类型
			
			var row = messageDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#messageAddForm');
			f.form("load", row);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#status").val("0");//设置状态标识为保存
				submitForm(urls.editCommitUrl,"messageAddForm","");
			}
		}, {
			text : "发送",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#status").val("1");//设置状态标识为发送
				submitForm(urls.editCommitUrl,"messageAddForm","");
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
 * 消息发送
 * @return
 */
function messageSend(){
	var selectRow = messageDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	
	var row = messageDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认发送", "确认要将选中消息发送？", function(r) {
		if (r) {
			$.post(urls.sendUrl, {
				messageid : row.messageid
			}, function(result) {
				easyui_auto_notice(result, function() {
					messageDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}

/**
 *删除
 */
function messageDelete(){
	var selectRow = messageDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	
	var row = messageDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中消息删除？", function(r) {
		if (r) {
			$.post(urls.deleteUrl, {
				messageid : row.messageid
			}, function(result) {
				easyui_auto_notice(result, function() {
					messageDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}

/**
 * 详情
 * @return
 */
function messageView(){
	var selectRow = messageDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "消息通知详情",
		iconCls : 'icon-view',
		width : 900,
		height : 330,
		href : urls.detailUrl,
		onLoad : function() {
			var row = messageDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#messageDetailForm');
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
