//请求路径
var base = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var base1 = contextpath + "aros/ajbg/fyzz/FyzzController/";

var Urls = {
	reqDetailUrl: base1 + "fyzzViewInit.do",                       // 案件登记详情
	queryNodeidsByCase : base + "queryNodeidsByCase.do"              //根据案件查询已审流程节点
};

/**
 * 查看复议中止详细信息
 */
function xzfyReqDetail(){
	openxzfyDetail(Urls.reqDetailUrl, "案件中止详情", 900, 550, "");
}

function openxzfyDetail(url, title, width, height, nodeid)
{
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条中止申请", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:title,
		width:width,
		height:height,
		href:url,
		queryParams: {
			 ccrid: row.ccrid,
			 protype: row.protype,
			 nodeid: nodeid
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			if ("7" == nodeid)
			{
				isShowSendunit(mdDialog.find("#result").val());
			}
			if ("12" == nodeid)
			{
				isShowByProtype(row.protype, row.protype);// 延期天数是否展示
			}
		},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}
// 复议中止
function fyStop()
{
	
	var fyStpTable = $('#fyStpTable');
	
	$("#fyStpTable  tr:not(:first)").empty("");
	
	var row = $('#stptr');
	
	var td = $("<td name='nodeid_10' id='10' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("承 办 人");
	row.append(td);
	
	td = $("<td name='nodeid_20' id='20' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("科 室 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_30' id='30' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("机 构 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_40' id='40' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("机 关 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_50' id='50' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("中 止 决 定");
	row.append(td);
	
	fyStpTable.append(row);
	
}

/**
 * 显示流程进度
 */
function viewFlow(){
	$('th').css("backgroundColor", "");
	$('td[name^="nodeid_"]').css("backgroundColor", "");
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var nodeid = row.nodeid;
	var  ccrid = row.ccrid;
	var protype = row.protype;
	
	if(nodeid > 0 && nodeid != null){
		fyStop();
		$('#stp_jd').css("background-color","#CCFF99");
		queryNodeidsByCase(ccrid,protype,nodeid,Urls.queryNodeidsByCase);
	}
	
}

function  queryNodeidsByCase(ccrid,protype,nodeid,url){
	
	$.ajax({
		type:'post',
		url:url,
		async : false,
		data : {
			ccrid : ccrid,
			protype: protype, 
			nodeid : nodeid,
			caseid : -1
		},
		success:function(data) {
			
				$('td[name^="nodeid_"]').each(function (index, domEle) {
					
					var nodeid_id = this.id;                                  //tb标签的id.
				
//					if(nodeid_id > nodeid){
//						$(domEle).css("display", "none"); 
//					}
					
					jQuery.each(data, function (i, item) {
						 if(nodeid_id == item.nodeid){
			                	$(domEle).css("backgroundColor", "#CCFF99");  
			                }
				        });
					
					if(nodeid_id == nodeid){
						$(domEle).css("backgroundColor", "#EE4000"); 
					}
				}) 
			
		},
		error : function() {
            alert('error');
        }
	});
}