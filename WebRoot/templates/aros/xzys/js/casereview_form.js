var panel_ctl_handles = [{
	gridname:"#fileDataGrid" 	// 刷新操作函数
}];

var baseUrl = contextpath + "aros/xzys/controller/";
var urls = {
	queryFileUrl:baseUrl + "queryFileList.do",     //查询上传附件列表
	deleteUrl:baseUrl + "deleteFileInfo.do"       //  删除附件
};

var lawid = $("#lawid").val();
var fileDataGrid;
$(function (){
	loadFileDataGrid(urls.queryFileUrl);
})

//加载可项目grid列表
function loadFileDataGrid(url){
	fileDataGrid = $("#fileDataGrid").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:5,
		pageList: [5],
		queryParams:{
			lawid:lawid,
			nodeid:nodeid,
			firstNode:true,
			lastNode:false
		},
		url:url,
		loadMsg:"正在加载，请稍候......",
		showFooter:false,
		onLoadSuccess: function () {
			var rows = fileDataGrid.datagrid("getRows");
			if (rows.length > 0 ) {
				showSendBtn();
			}
		},
		columns:[[ 
          {field:"itemid", checkbox:true },
		  {field:"filename", title:"附件名称", halign:"center", align:"left", width:300, sortable:true},
		  {field:"createtime",title:"上传时间", halign:"center",  align:"left", width:200, sortable:true},
		  {field:"usercode",title:"上传人", halign:"center",  align:"left", width:150, sortable:true}
	    ]]
	});
}

/**
 * 重新加载
 */
function reloadFileDatagrid(){
	var param = {
		lawid:lawid,
		nodeid:nodeid,
		firstNode:true,
		lastNode:false
	};
	fileDataGrid.datagrid("load", param);
}

function clickUploadDiv2(elementcode) {
	lawid = $("#lawid").val();
	showUploadifyModalDialog2(parent.$('#uploadifydiv'), "itemids", elementcode, lawid);
}

function choiseTutor() {
	parent.$.modalDialog2({
		title:"ddd",
		width:800,
		height:450,
		href:"/index.do",
		onLoad:function() {
			var mdDialog2 = parent.$.modalDialog.handler2;
			
		},
		buttons : [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler2.dialog('close');
			}
		} ]
	});
}


/**
 * 删除附件信息
 */
function _delete(){
	var selectRow = fileDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	var row = fileDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中数据删除？", function(r) {
		if (r){
			$.post(urls.deleteUrl,
					{
				     id:row.itemid
				     },
					function(result){
						easyui_auto_notice(result, function() {
							fileDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}


/**
 *上传附件组件
 * @param dialogId div
 * @param fileItemmids 隐藏域
 * @param elementcode 业务类型
 */
function showUploadifyModalDialog2(dialogId, fileItemmids,elementcode, keyid) {
	var href = contextpath+ "base/filemanage/fileManageController/uploadify.do?fileItemmids="+fileItemmids+"&tdId=filetd&elementcode="+elementcode+"&keyid="+keyid;
	dialogId.dialog({
		title : "附件管理",
		width : 700,
		height : 300,
		href : href,
		iconCls : 'icon-files',
		modal : true,
		onClose : function() {
			reloadFileDatagrid();
		},
		buttons : [{
			text : "确认",
			iconCls : "icon-save",
			handler : function() {
				dialogId.dialog('close');
				showSendBtn();
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				dialogId.dialog('close');
			}
		}]
	});
}

function showFileBtn()
{
	$("#fileBtn").linkbutton('enable'); // 可上传附件
}

function showSendBtn()
{
	$("#sendBtn").linkbutton('enable'); // 可发送
}

//在modalDialog窗口上弹出窗口
$.modalDialog2 = function(options) {
	if ($.modalDialog.handler2 == undefined) {// 避免重复弹出
		var opts = $.extend( {
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.modalDialog.handler2 = undefined;
				$(this).dialog('destroy');
			}
		}, options);
		opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
		return $.modalDialog.handler2 = $('<div/>').dialog(opts);
	}
};
