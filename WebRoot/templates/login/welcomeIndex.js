/**
 * 加载页面初始化jQuery脚本
 */
$(function() {
	showWaitWorkDatagrid();
	
	loadDataToWaitWorkgrid();
	
	//展示公告
	showNoticegrid();
	
	//展示留言
	showMessagegrid();
});
var baseUrl = contextpath + "sys/notice/SysNoticeManageController/";
var urls = {
		feedbackNotice : baseUrl+"feedbackNotice.do",
		getClobContentVal : baseUrl+"getClobContentVal.do",
		showCommonBySelfUrl :  baseUrl+"showCommonBySelf.do",
		showCommonDialogUrl : baseUrl+"showCommonDialog.do"
};

var showMessagegrid;
function showMessagegrid()
{
	showMessagegrid = $("#showMessagegrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		url : contextpath +  'sys/notice/SysNoticeManageController/qryReadAllMessageIndex.do?messageType=2',
		onDblClickRow : function(index, row) {
			showMessageMsg(row.noticeid);
		},
		columns : [ [{field : "title",title : "标题",halign : 'center', width:'30%',sortable:false}
		              ,{field : "username",title : "留言人",halign : 'center',	width:'22%',sortable:false	}
		              ,{field : "releasetime",title : "留言时间",halign : 'center',	width:'12%',sortable:false	}
		              ,{field : "flag",title : "阅读回执",halign : 'center',width:'6%',sortable:false	}
		              ,{field : "remark",title : "备注",halign : 'center',width:'30%',sortable:false	}
		              ,{field : "noticeid",title : "编码",halign : 'center',align : 'right',	width:'0%',sortable:false,hidden:true	}
		              ,{field : "receiveid",title : "接收编码",halign : 'center',align : 'right',	width:'0%',sortable:false,hidden:true	}
		             ] ]
	});
}

var showNoticegrid;
function showNoticegrid()
{
	showNoticegrid= $("#showNoticegrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		url : contextpath + 'sys/notice/SysNoticeManageController/qryReadAllNoticeIndex.do?messageType=1',
		onDblClickRow : function(index, row) {
			showNoticeMsg(row.noticeid);
		},
		columns : [ [{field : "indextitle",title : "标题",halign : 'center', width:'30%',sortable:false}
					,{field : "title",title : "标题",halign : 'center',width:'0%',sortable:false,hidden:true	}
		              ,{field : "priorlevelname",title : "重要性",halign : 'center',width:'6%',sortable:false	}
		              ,{field : "username",title : "发布人",halign : 'center',width:'22%',sortable:false	}
		              ,{field : "releasetime",title : "发布时间",halign : 'center',width:'12%',sortable:false	}
		              ,{field : "remark",title : "备注",halign : 'center',width:'30%',sortable:false	}
		              ,{field : "noticeid",title : "编码",halign : 'center',	width:'0%',sortable:false,hidden:true	}
		             ] ]
	});
}

var waitworkDatagrid1;
var waitworkDatagrid2;
function showWaitWorkDatagrid() {
	waitworkDatagrid1 = $('#waitworkDatagrid1').datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		onDblClickRow : function(index, row) {
			window.parent.addTab(row.title, row.webpath, row.resourceid);
		},
		columns : [ [{field : "wholename",title : "类别",halign : 'center', width:'80%',sortable:false}
		              ,{field : "count",title : "任务数量",halign : 'center',align : 'right',	width:'20%',sortable:false	}
		             ] ]
	});
	
	waitworkDatagrid2 = $('#waitworkDatagrid2').datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		onDblClickRow : function(index, row) {
			window.parent.addTab(row.title, row.webpath, row.resourceid);
		},
		columns : [ [{field : "wholename",title : "类别",halign : 'center',width:'80%',sortable:false}
		              ,{field : "count",title : "任务数量",halign : 'center',align : 'right',	width:'20%',sortable:false	}
		             ] ]
	});
}

function loadDataToWaitWorkgrid() {
	$.post(contextpath + 'sys/toRemind/ToRemindController/findWaitWorkInfo.do', {}, function(data){
		var wwdata1 = new Array();
		var wwdata2 = new Array();
		if (data.length > 0) {
			
			for (var n = 0; n < data.length; n++) {
				if (n%2 == 0) {
					wwdata1.push(data[n]);
				} else {
					wwdata2.push(data[n]);
				}
			}
			waitworkDatagrid1.datagrid('loadData', wwdata1);
			waitworkDatagrid2.datagrid('loadData', wwdata2);
		} else {
			waitworkDatagrid1.datagrid('loadData', wwdata1);
			waitworkDatagrid2.datagrid('loadData', wwdata2);
		}
	}, "json");
}

function showReload() {
	loadDataToWaitWorkgrid();
}

//查看留言公告主体详情
function showNoticeMsg(noticeid)
{
	showModalDialog(570, "readNoticeForm", "edit", showNoticegrid, "反馈信息",
			"sys/notice/SysNoticeManageController/feedbackNoticeInit.do?messageType=1&noticeId="+noticeid, urls.feedbackNotice,'1');
	
}

function showMessageMsg(noticeid)
{
	showModalDialog(570, "readNoticeForm", "edit", showMessagegrid, "反馈信息",
			"sys/notice/SysNoticeManageController/feedbackNoticeInit.do?messageType=2&noticeId="+noticeid, urls.feedbackNotice,'2');
}


function showModalDialog(width, form, operType, dataGrid, title, href, url,messageType) {

	var icon = 'icon-' + operType;
	var receiveid="";
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 810,
		height : width,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			f.form("load", row);
		    
			
			if(messageType == '2')
			{
				receiveid=row.receiveid;
			}
			
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
				, {field : "commentusername",title : "评论人",halign : 'center',width : 140,sortable:true}
				, {field : "commentcontent",title : "评论内容",halign : 'center',width : 250,sortable:true}
				, {field : "commenttime",title : "评论时间",halign : 'center',width : 150,sortable:true}
				] ]
			});
			
		},
		buttons : funcOperButtons(operType, url, dataGrid, form,receiveid,messageType)
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
function funcOperButtons(operType, url, dataGrid, form,receiveid,messageType) {

	var buttons;
	if(operType == "edit")
	{
		buttons = [ {
			text : "反馈",
			iconCls : "icon-edit",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//弹出反馈信息框
				showCommonModalDialog(parent.$('#commontService'),url,receiveid,messageType);
			}
		},
		{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
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
function showCommonModalDialog(div,url,receiveid, messageType)
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
					parent.$('#commontService').dialog.openner_dataGrid = parent.$("#commonlist");
				    var commonCxt = parent.parent.$("#commentContent").val();
				    if(commonCxt == '' || commonCxt == null)
				    {
				    	parent.$.messager.alert('提示', "请填写反馈内容！", 'warnning');
				    }
				    else
				    {
				    	//ajax请求，添加评论信息
						$.post(url, {
							commentContent:commonCxt,
							receiveid:receiveid,
							noticeid:parent.$("#noticeid").val()
						}, function(result) {
							easyui_auto_notice(result, function() {
								//刷新历史反馈列表
								var noticeidVal = parent.$("#noticeid").val();
								var param = {
										noticeid : noticeidVal,
										messageType : messageType
										
									};
								parent.$('#commontService').dialog.openner_dataGrid.datagrid("load", param);
								parent.$('#commontService').dialog('close');
								
								
							});
						}, "json");
				    }
					
				}
			},
			{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$('#commontService').dialog('close');
				}
			}]
	});
}
