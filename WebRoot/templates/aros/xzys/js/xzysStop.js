//请求路径
var base = contextpath + "aros/xzys/controller/";
var Urls = {
	receiveDetailUrl: base + "xzysReceiveDetail.do",               // 收案登记详情
	caseReviewUrl: base + "xzfyCaseReview.do",                       // 案件审查详情
	reqCtysDetailUrl: base + "xzysCtysDetail.do",			       // 出庭应诉详情
	queryNodeidsByCase : base + "queryNodeidsByCase.do"              //根据案件查询已审流程节点
	
};

/**
 * 查看行政应诉收案登记详细信息
 */
function xzysReqReceiveDetail(){
	openxzysDetail(Urls.receiveDetailUrl, "收案登记详情", 600, 270, "10");
}

/**
 * 查看行政应诉案件审查详细信息
 */
function xzysAcceptDetail(){
	openxzysDetail(Urls.caseReviewUrl, "案件审查详情", 600, 400, "20");
}

/**
 * 查看行政应诉出庭应诉详细信息
 */
function xzysCtysDetail(){
	openxzysDetail(Urls.reqCtysDetailUrl, "出庭应诉详情", 610, 450, "30");
}


function openxzysDetail(url, title, width, height, nodeid){
	var selectRow = projectDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条应诉案件", null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title:title,
		width:width,
		height:height,
		href:url,
		queryParams: {
			 id: row.id,
			 protype: row.protype,
			 nodeid: nodeid
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
//			var f = mdDialog.find("#"+form);
//			comboboxFuncByCondFilter(menuid, "regtype", "REGTYPE", "code", "name", mdDialog);// 审理阶段
//			comboboxFuncByCondFilter(menuid, "suetype", "SUETYPE", "code", "name", mdDialog);// 起诉方式
//			comboboxFuncByCondFilter(menuid, "jurilaw", "JURILAW", "code", "name", mdDialog);// 管辖法院
//			f.form("load", row);
//			if ("7" == nodeid)
//			{
//				isShowSendunit(mdDialog.find("#result").val());
//			}
//			if ("12" == nodeid)
//			{
//				isShowByProtype(row.protype, row.protype);// 延期天数是否展示
//			}
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
// 行政应诉   
function resproces(){
	
	var fyStpTable = $('#fyStpTable');
	
	$("#fyStpTable  tr:not(:first)").empty("");
	
	var row = $('#stptr');
	
	var td = $("<td name='nodeid_10' id='10' class='ancestorstd'  onClick='xzysReqReceiveDetail()' valign='top'></td>");
	td.append("收案登记");
	row.append(td);
	
	td = $("<td name='nodeid_20' id='20' class='ancestorstd'  onClick='xzysAcceptDetail()' valign='top'></td>");
	td.append("案件审查");
	row.append(td);
	
	td = $("<td name='nodeid_30' id='30' class='ancestorstd'  onClick='xzysCtysDetail()' valign='top'></td>");
	td.append("出庭应诉");
	row.append(td);
	
	td = $("<td name='nodeid_40' id='40' class='ancestorstd'  onClick='xzfyAcceptDetail()' valign='top'></td>");
	td.append("立案归档");
	row.append(td);
	
	fyStpTable.append(row);
	
}

/**
 * 显示流程进度
 */
function viewFlow(){
	$('th').css("backgroundColor", "");
	$('td[name^="nodeid_"]').css("backgroundColor", "");
	
	var row = projectDataGrid.datagrid("getSelections")[0];
	var nodeid = row.nodeid;
	var id = row.id;
	var protype = row.protype;
	
	if(nodeid > 0 && nodeid != null){
		resproces();
//		$('#stp_jd').css("background-color","#CCFF99");
		queryNodeidsByCase(id,protype,nodeid,Urls.queryNodeidsByCase);
	}
	
}

function  queryNodeidsByCase(id,protype,nodeid,url){
	
	$.ajax({
		type:'post',
		url:url,
		async : false,
		data : {
			id : id,
			protype: protype, 
			nodeid : nodeid,
		},
		success:function(data) {
				$('td[name^="nodeid_"]').each(function (index, domEle) {
					
					var nodeid_id = this.id;                                  //tb标签的id.
					if(nodeid_id > nodeid){
						$(domEle).css("backgroundColor", ""); 
					}
					
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


/**
 * 根据当前案件审结结果
 */
//function isShowByProtype(protype, oldprotype){
//	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
//	// 如果变更流程类型
//	if (protype.indexOf("n") > -1)
//	{
//		protype = protype.replace("n","");
//		if ("07" == protype) // 延期
//		{
//			form.find("#delay").show();
//		}
//	}
//	else
//	{
//		form.find("#delay").hide();
//	}
//}

//function isShowSendunit (id) {
//	var modalDialog = parent.$.modalDialog.handler;
//	if ("03" == id) {
//		modalDialog.find("#sendunitTr").show();
//	}
//	else {
//		modalDialog.find("#sendunitTr").hide();
//	}
//}