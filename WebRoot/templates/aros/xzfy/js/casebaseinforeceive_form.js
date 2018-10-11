var panel_ctl_handles = [{
	gridname:"#fileDataGrid" 	// 刷新操作函数
}];

var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	queryFileUrl:baseUrl + "queryFileList.do"
};

var caseid = $("#caseid").val();
var fileDataGrid;
$(function (){
	loadFileDataGrid(urls.queryFileUrl);
	if ("1" == showFile) {
		showFileBtn();
	}
	if ("11" == nodeid) {
		$("#csaecode").textbox({'disabled':true});
		$("#saveBtn").linkbutton('disable');
	}
})

//加载可项目grid列表
function loadFileDataGrid(url)
{
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
			caseid:caseid,
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
          {field:"itemid", checkbox:false, hidden:true},
		  {field:"filename", title:"材料名称", halign:"center", align:"left", width:'70%', sortable:true},
		  {field:"createtime",title:"接收日期", halign:"center",  align:"left", width:'25%', sortable:true}
	    ]]
	});
}

/**
 * 重新加载
 */
function reloadFileDatagrid(){
	var param = {
		caseid:caseid,
		nodeid:nodeid,
		firstNode:true,
		lastNode:false
	};
	fileDataGrid.datagrid("load", param);
}

function clickUploadDiv2(elementcode) {
	caseid = $("#caseid").val();
	showUploadifyModalDialog2(parent.$('#uploadifydiv'), "itemids", elementcode, caseid);
}

function clickPhotosDiv(elementcode) {
	caseid = $("#caseid").val();
	showPhotosModalDialog(parent.$('#uploadifydiv'), "itemids", elementcode, caseid);
}

function choiseSqbl(){
	parent.$.modalDialog2({
		title:"关联笔录",
		width:800,
		height:450,
		href:"aros/sqbl/controller/ApplyRecordController/list.do",
		onLoad:function() {},
		buttons : [{
				text : "确定",
				iconCls : "icon-save",
				handler : function() {
					var selectRow = parent.$.modalDialog.handler2.find("#datalist").datagrid('getChecked');
					if(selectRow==null || selectRow.length==0||selectRow.length>1){
						easyui_warn("请选择一条数据！",null);
						return;
					}
					var id = selectRow[0].arid;
					parent.$.modalDialog.handler.find("#arid").val(id);
					parent.$.modalDialog.handler.find("#linkbl").html("已关联");
					parent.$.modalDialog.handler2.dialog('close');
				}
				},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler2.dialog('close');
			}
		} ]
	});
}
function viewSqbl(){
	var sqblid = $("#arid").val();
	if(sqblid == '' || sqblid == undefined){
		return;
	}
	parent.$.modalDialog2({
		title:"关联笔录信息",
		width:800,
		height:450,
		href:"aros/sqbl/controller/ApplyRecordController/detail.do?id="+sqblid,
		onLoad:function() {},
		buttons : [{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler2.dialog('close');
			}
		} ]
	});
}

function changeLinkBl(id){
	$("#arid").val(rowData.arid);
	$("#linkbl").html("已关联");
}

/**
 *上传附件组件
 * @param dialogId div
 * @param fileItemmids 隐藏域
 * @param elementcode 业务类型
 */
function showUploadifyModalDialog2(dialogId, fileItemmids, elementcode, keyid) {
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

/**
 *上传附件组件
 * @param dialogId div
 * @param fileItemmids 隐藏域
 * @param elementcode 业务类型
 */
function showPhotosModalDialog(dialogId, fileItemmids, elementcode, keyid) {
	var href = contextpath+ "base/filemanage/fileManageController/photosify.do?fileItemmids="+fileItemmids+"&tdId=filetd&elementcode="+elementcode+"&keyid="+keyid;
	dialogId.dialog({
		title : "拍照管理",
		width : 900,
		height : 600,
		href : href,
		iconCls : 'icon-files',
		modal : true,
		onClose : function() {
			reloadFileDatagrid();
		},
		buttons : [{
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
	$("#photosBtn").linkbutton('enable');	//可拍照上传
}

function showSendBtn()
{
	$("#sendBtn").linkbutton('enable'); // 可发送
}
