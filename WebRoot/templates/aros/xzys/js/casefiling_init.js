// 请求路径
var baseUrl = contextpath + "aros/xzys/controller/";
var urls = {
	noticeGridUrl:baseUrl + "queryNoticeList.do",                  // 查询文书列表
	NoticeTmpGridUrl:baseUrl + "queryNoticeTmpList.do",            // 查询文书模板列表
	addUrl:baseUrl + "noticeInfoAdd.do",                           // 新增文书 
	getClobContentVal: baseUrl+"getClobContentVal.do",			   // 查询文书内容
	downloadNoticeUrl: baseUrl+"noticeDownload.do",			       // 文书下载
	noticeSaveUrl:baseUrl + "noticeInfoSave.do",                   // 保存文书
	noticeDelUrl:baseUrl + "noticeInfoDelete.do",                  // 删除已有文书
//	NoticeSendUrl:baseUrl + "xzfyNoticeFlow.do",                   // 发送
	returnUrl:baseUrl + "xzfyReturn.do"                            // 回退
};

var panel_ctl_handles = [{
	gridname:"#noticeDataGrid"  	// 刷新操作函数
}];

var editor;
// 默认加载
$(function() {
	// 初始化编码器
	editor = KindEditor.create($('#contentEdit'), {
		resizeType : 1,
		allowPreviewEmoticons : false,
		allowImageUpload : false,
		items : [
			'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
			'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
			'insertunorderedlist', '|', 'emoticons']//, 'image', 'link']
	});	
	// 加载已有文书列表
	loadNoticeDataGrid(urls.noticeGridUrl);
});

// 加载已有文书列表列表
var noticeDataGrid;
function loadNoticeDataGrid(url) {
	noticeDataGrid = $("#noticeDataGrid").datagrid({
		fit:false,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:10,
		queryParams:{
			caseid: caseid,
			nodeid: nodeid,
			protype: protype,
			firstNode: true,
			lastNode: false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#notice_toolbar",
		showFooter:true,
        onClickRow:function(rowIndex,rowData){
        	openNoticeInfo("", rowData, '');
        },
		columns:[[ 
		  {field:"id", checkbox:true},
		  {field:"noticecode", title:"文书编号", halign:"center", width:60, sortable:true},
		  {field:"noticename", title:"文书名称", halign:"center", width:195, sortable:true},
		  {field:"buildtime", title:"生成时间", halign:"center", width:80, sortable:true},
		  {field:"opt", title:"操作", width:50,  align:"center",
	            formatter:function(val, row, index){
	            	var btn = "<span style='text-decoration:none;color:blue' onclick=download('"+ row.id +"')>下载</span>";
	                return btn; 
	            }},
		  {field:"caseid", title:"案件编号", halign:"center", sortable:true, hidden:true},
		  {field:"tempid", title:"模板编号", halign:"center", sortable:true, hidden:true}
        ]]
	});
}

// 下载已有文书
function download (id) {
	/*$.post(urls.downloadNoticeUrl, {id:id}, 
		function(result) {
			if (result.success) { 
				parent.$.messager.alert("提示", result.title, "info");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}, "json");*/
	window.open(urls.downloadNoticeUrl + "?id=" + id);
}

// 重新加载已有文书列表
function showNoticeReload(){
	noticeDataGrid.datagrid("reload"); 
}

function noticeAdd(dialogId) {
	dialogId.dialog({
		title: "文书模板列表",
		width: 600,
		height: 350,
		href:urls.addUrl,
		onLoad: function() {
			loadNoticeTmp(dialogId);
		},
		buttons: [{
			text: "关闭",
			iconCls: "icon-cancel",
			handler: function() {
				dialogId.dialog('close');
			}
		}]
	});
}

// 加载文书模板列表
var noticeTmpDataGrid;
function loadNoticeTmp(dialogId) {
	noticeTmpDataGrid = dialogId.find("#noticeTmpDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:10,
		queryParams:{
			id: id,
			nodeid: nodeid,
			protype: protype,
			firstNode: true,
			lastNode: false
		},
		url:urls.NoticeTmpGridUrl,
		loadMsg:"正在加载，请稍候......",
		showFooter:true,
		onClickRow:function(rowIndex,rowData){
        	openNoticeInfo(dialogId, rowData, 'tmp');
        },
		columns:[[ 
		  {field:"tempid", checkbox:true, hidden:true},
		  {field:"noticename", title:"文书名称", halign:"center", width:460, sortable:true},
		  {field:"buildtime", title:"生成时间", halign:"center", width:100, sortable:true}
        ]]
	});
}

/**
 * 打开文书处理页面
 * @param row
 */
function openNoticeInfo(dialogId, row, tableFlag) {
	// 先清空
	cancel();
	$("#xzfyFilingForm").form("load", row);
	var tempid = "";
	if("tmp" == tableFlag)
	{
		tempid = row.tempid;
		// 文书内容
		$("#id").val("");
	}
	else {
		tempid = row.id;
	}
	$.post(urls.getClobContentVal, {
			tempid: tempid,
			id: id,
			tableFlag: tableFlag
		}, function(data){
			// 关闭模板列表弹窗
			if("tmp" == tableFlag)
			{
				dialogId.dialog("close");
			}
			//给父窗口的控件设值
			editor.html(data);
			//将编辑器值赋值给隐藏域content
			$("#contents").val(data);
	}, "json");
	
	$("#saveBtn").linkbutton('enable');
	$("#cancelBtn").linkbutton('enable');
}

/**
 * 保存文书信息
 * @param url
 * @param form
 */
function saveNotice() {
	var form = $("#xzfyFilingForm");
	var isValid = form.form('validate');
	if (!isValid) {
		easyui_warn("数据项未填写完整，请处理完整后再保存", null);
		return;
	}
	// 将编辑器的内容赋值给隐藏域
	form.find("#contents").val(editor.html());
	form.form("submit", {
		url:urls.noticeSaveUrl,
		onSubmit:function() {
			parent.$.messager.progress({
				title:"提示",
				text:"数据处理中，请稍后...."
			});
			var isValid = form.form("validate");
			if (!isValid) {
				parent.$.messager.progress("close");
			}
			return isValid;
		},
		success:function(result) {
			result = $.parseJSON(result);
			if (result.success) {
				parent.$.messager.alert("提示", result.title, "info");
				cancel();
				showNoticeReload();
				$("#saveBtn").linkbutton('disable');
				$("#cancelBtn").linkbutton('disable');
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
			parent.$.messager.progress("close");
		}
	});
}

/**
 * 取消文书制作
 */
function cancel() {
	// 文书名称
	$("#noticename").textbox('clear');
	// 文书编号
	$("#noticecode").textbox('clear');
	// 文书内容
	$("#contents").val("");
	editor.html("");
}

/**
 * 发送
 */
function sendNotice() {
	parent.$.messager.confirm("发送确认", "确认要发送该复议申请？", function(r) {
		if (r) {
			$.post(urls.NoticeSendUrl, {
				menuid:menuid,
				caseid:caseid
			}, function(result) {
				if (result.success) {
					parent.$.messager.alert("提示", result.title, "success");
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}

/**
 * 删除
 */
function delNotice() {
	var selectRow = noticeDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条文书记录", null);
		return;
	}
	var row = noticeDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("删除确认", "确认要删除该文书？", function(r) {
		if (r) {
			$.post(urls.noticeDelUrl, {
				menuid:menuid,
				id:row.id 
			}, function(result) {
				if (result.success) { 
					cancel();
					showNoticeReload();
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}

/**
 * 回退
 */
function returnNotice() {
	parent.$.messager.confirm("回退确认", "确认要回退该复议申请？", function(r) {
		if (r) {
			$.post(urls.returnUrl, {
				menuid:menuid,
				caseid:caseid 
			}, function(result) {
				if (result.success) { 
					parent.$.modalDialog.handler.dialog("close");
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
}
