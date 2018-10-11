function clickUploadDiv(elementcode, files)
{
	var fjkeyid =$('#fjkeyid').val();
	var id = $('#id').val();
	fjkeyid = id == "" ? fjkeyid : id;
	showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", elementcode, fjkeyid, files);
}


function showSendBtn(){
	$("#sendBtn").linkbutton('enable');;
}

//二级弹窗                                                                  
var baseUrl = contextpath + "aros/xzys/controller/";
var relatednumgrid;
//选择法规规章发布信息
function choiseTutor() {
	parent.$.modalDialog2({
		title:"关联案号",
		width:800,
		height:450,
		href:baseUrl + "relatednum.do",
		onLoad:function() {
			var mdDialog2 = parent.$.modalDialog.handler2;
			//加载grid
			initRelatedSelectGrid();
			//绑定查询按钮事件
			mdDialog2.find("#searchBtn").on("click", function(){
				var lawid = mdDialog2.find("#lawid").textbox("getValue");
				var param = {
					lawid : lawid,
					menuid : menuid
				};
				relatednumgrid.datagrid("load", param);
			});
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

//加载选择页面gird
function initRelatedSelectGrid() {
	var mdDialog = parent.$.modalDialog.handler;
	var regtype = mdDialog.find("#regtype").combobox("getValue");
	relatednumgrid = parent.$.modalDialog.handler2.find("#relatednumTable").datagrid({
		height: 320,
		width:'100%',
		title: '',
		collapsible: false,
		url : baseUrl + "relatedListQuery.do?regtype="+regtype,
		queryParams : {},
		singleSelect: true,
		rownumbers : true,
		pagination : true,
		columns : [ [ 
				{
					field : "id",
					checkbox : true
				}, {
					field : "lawid",
					title : "案号",
					halign : 'center',
					width : 100,
					sortable : true
				}, {
					field : "regtypename",
					title : "审理阶段",
					halign : 'center',
					width : 150,
					sortable : true
				}, {
					field : "jurilawname",
					title : "受案法院",
					halign : 'center',
					width : 150,
					sortable : true
				}, {
					field : "suetypename",
					title : "起诉类型",
					halign : 'center',
					width : 180,
					sortable : true
				}, {
					field : "suerequest",
					title : "原告情况",
					halign : 'center',
					width : 100,
					sortable : true
				}
	    ] ],
		onDblClickRow : function(rowIndex, rowData) {
			parent.$.modalDialog.handler.find("#caseid").val(rowData.id);
			parent.$.modalDialog.handler.find("#glahlawid").textbox("setValue", rowData.lawid);
			
			//延时关闭 防止回调时找不到窗口对象(从载入后延迟指定的时间去执行一个表达式或者是函数;)
			setTimeout(closedWin, 5);
		}
	});
}
//关闭二级窗口
function closedWin(){
	parent.$.modalDialog.handler2.dialog('close');
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

