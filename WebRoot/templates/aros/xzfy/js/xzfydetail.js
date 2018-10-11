//请求路径
var base = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var Urls = {
	shenqblDetailUrl: base + "applyDetailByCase.do",				//复议申请笔录
	applyRecordDetailUrl:base + "applyDetailByCase.do",            // 申请笔录详情
	receiveDetailUrl: base + "xzfyReceiveDetail.do",               // 接收材料详情
	reqDetailUrl: base + "xzfyReqDetail.do",                       // 案件登记详情
	transregistUrl: base + "transregistDetail.do",              // 转送登记表
	slundertakerUrl: base + "slundertakerDetail.do",              // 指定承办人
	acceptDetailUrl: base + "xzfyAcceptDetail.do",			       // 受理详情
	accendDetailUrl: base + "xzfyAccendDetail.do",			       // 受理决定详情
	reviewDetailUrl: base + "xzfyHearDetail.do",				   // 审理详情
	decisionDetailUrl: base + "xzfyDecisionDetail.do",			   // 审理决定详情
	textproductionDetailUrl: base + "textProductionDetail.do",     // 文书制作详情
	documentDeliveryDetailUrl: base + "documentDeliveryDetail.do", // 文书送达详情
	callbackDetailUrl: base + "callbackDetail.do", 					// 廉政回访
	documentReviewDetailUrl: base + "documentReviewDetail.do",     // 廉政回访详情
	zhuansdjDetailUrl:      base + "transregistDetail.do",		//转送登记表详情
	viewTingshenUrl :       base + "xzfyTrialDetail.do", 				//庭审详情
	viewDiscussionUrl :     base + "summaryDetail.do", 				//集体讨论
	documentDecisionDetailUrl: base + "documentDeliveryDetail.do", // 行政复议决定书详情
	viewWeiysyUrl	:       base  + "summaryDetail.do", 			// 委员审议	
	documentCloseDetailUrl: base  + "textProductionDetail.do",            // 结案归档
	summaryDetailUrl:       base + "summaryDetail.do",                   // 备考表详情
	viewUrl: contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/spesugbaseinfoViewInit.do", // (按钮)委员意见详情
	queryNodeidsByCase : base + "queryNodeidsByCase.do"              //根据案件查询已审流程节点
	
};


/**
 * 查看复议复议申请笔录详细信息
 */
function xzfyShenqblDetail(){
	openxzfyDetail(Urls.applyRecordDetailUrl, "复议申请笔录", 600, 350, "10");
}
/**
 * 查看行政复议接收材料详细信息
 */
function xzfyReqReceiveDetail(){
	openxzfyDetail(Urls.receiveDetailUrl, "接收申请材料详情", 600, 350, "20");
}

/**
 * 查看行政复议接收材料详细信息
 */
function xzfyResReceiveDetail(){
	openxzfyDetail(Urls.receiveDetailUrl, "接收答复材料详情", 600, 350, "11");
}

/**
 * 查看行政复议案件登记详细信息
 */
function xzfyReqDetail(){
	openxzfyDetail(Urls.reqDetailUrl, "案件登记详情", 900, 550, "");
}

/**
 * 查看行政复议受理详情详细信息
 */
function xzfyAcceptDetail(){
	openxzfyDetail(Urls.acceptDetailUrl, "受理详情", 900, 550, "30");
}

/**
 * 查看行政复议转送登记表
 */
function xzfytransregistDetail(){
	openxzfyDetail(Urls.transregistUrl, "转送登记表", 900, 550, "90");
}
/**
 * 查看行政复议指定承办人
 */
function xzfyslundertakerUrlDetail(){
	openxzfyDetail(Urls.slundertakerUrl, "指定承办人", 500, 300, "100");
}
/**
 * 查看行政复议受理决定详细信息
 */
function xzfyAccendDetail(){
	openxzfyDetail(Urls.accendDetailUrl, "受理决定详情", 600, 300, "70");
}

/**
 * 查看行政复议审理详细信息
 */
function xzfyReviewDetail(){
	openxzfyDetail(Urls.reviewDetailUrl, "审理详情", 900, 550, "110");
}

/**
 * 查看行政复议审理决定详细信息
 */
function xzfyDecisionDetail(){
	openxzfyDetail(Urls.decisionDetailUrl, "审理决定详情", 600, 300, "");
}

/**
 * 委员意见详情
 */
function spesugbaseinfoView() {
	openxzfyDetail(Urls.viewUrl, "委员意见详情", 900, 550, "");
}

/**
 * 委员审议详情
 */
function xzfyWeiysyView() {
	openxzfyDetail(Urls.viewTingshenUrl+"?reviewtype=03", "委员审议详情", 900, 550, "130");
}

/**
 * 庭审详情
 */
function xzfyTingshenView() {
	openxzfyDetail(Urls.viewTingshenUrl+"?reviewtype=01", "庭审详情", 900, 550, "");
}

/**
 * 集体讨论详情
 */
function xzfyDiscussionView() {
	openxzfyDetail(Urls.viewTingshenUrl+"?reviewtype=02", "集体讨论详情", 900, 550, "140");
}


/**
 * 查看转送登记表
 */
function xzfyZhuansdjDetail() {
	openxzfyDetail(Urls.zhuansdjDetailUrl, "转送登记表详情", 1000, 550, "90");
}

/**
 * 查看行政复议文书制作详细信息_8
 */
function xzfyTextProductionDetail() {
	openxzfyDetail(Urls.textproductionDetailUrl, "文书制作详情", 1000, 550, "8");
}

/**
 * 打开文书送达详情页面_9
 * @param row
 */
function documentDeliveryDetail() {
	openxzfyDetail(Urls.documentDeliveryDetailUrl, "文书送达详情", 900, 550, "9");
}

/**
 * 打开回访单详情页面_10
 * @param row
 */
function documentReviewDetail() {
	openxzfyDetail(Urls.documentReviewDetailUrl, "回访单详情", 900, 550, "10");
}

/**
 * 查看行政复议文书制作详细信息_17
 */
function xzfyTextProductionDetailHear() {
	var view = "view";
	openxzfyDetail(Urls.textproductionDetailUrl, "文书制作详情", 1000, 550, "170");
}

/**
 * 打开文书送达详情页面_18
 * @param row
 */
function documentDeliveryDetailHear() {
	openxzfyDetail(Urls.documentDeliveryDetailUrl, "文书送达详情", 900, 550, "180");
}
 
/**
 * 行政复议决定书详情
 * @param row
 */
function  documentDecisionDetail(){
	openxzfyDetail(Urls.zhuansdjDetailUrl, "行政复议决定书详情", 900, 550, "190");
}
/**
 * 廉政回访详情
 * @param row
 */
function  callbackDetail(){
	openxzfyDetail(Urls.callbackDetailUrl, "廉政回访详情", 900, 550, "210");
}

/**
 * 廉政回访详情
 * @param row
 */
function documentReviewDetailHear() {
	openxzfyDetail(Urls.documentReviewDetailUrl, "廉政回访详情", 900, 550, "210");
}

/**
 * 结案归档详情
 * @param row
 */
function documentCloseDetailHear() {
	openxzfyDetail(Urls.documentCloseDetailUrl, "结案归档详情", 900, 550, "220");
}

/**
 * 打开备考表详情页面_20
 * @param row
 */
function summaryDetail() {
	openxzfyDetail(Urls.summaryDetailUrl, "备考表详情", 900, 550, "200");
}

function openxzfyDetail(url, title, width, height, nodeid)
{
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:title,
		width:width,
		height:height,
		href:url,
		queryParams: {
			 caseid: row.caseid,
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

function jiedai(){

	var rows = xzfyDataGrid.datagrid("getSelections")[0];
	var nodeid; 
	
	var flowTable = $('#flowTable');
	
	$("#flowTable  tr:not(:first)").empty("");
	
	var row = $('#nodetr');
	var td = $("<td name='nodeid_10' id='10' class='ancestorstd'  onClick='xzfyShenqblDetail()' valign='top'></td>");
	td.append("复议申请笔录");
	row.append(td);
	flowTable.append(row);
	
	if(typeof(rows) != "undefined") {
		nodeid = rows.nodeid;
		if(nodeid != null && nodeid > 0 ){
			if(nodeid == 10)
				$("td[name='nodeid_10']").css("backgroundColor", "#EE4000"); 
			else
				$("td[name='nodeid_10']").css("backgroundColor", "#CCFF99"); 
		}
	}
}

function lian(){
	
	var rows = xzfyDataGrid.datagrid("getSelections")[0];
	
	var flowTable = $('#flowTable');
	
	$("#flowTable  tr:not(:first)").empty("");
	
	var row = $('#nodetr');
	var td = $("<td></td>");
	td.append("");
	row.append(td);
	var td = $("<td name='nodeid_20' id='20' class='ancestorstd' onClick='xzfyReqReceiveDetail()' valign='top'></td>");
	td.append("接 受 申 请 材 料");
	row.append(td);
	
	td = $("<td name='nodeid_30' id='30' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("案 件 登 记");
	row.append(td);
	
	td = $("<td name='nodeid_40' id='40' class='ancestorstd'  onClick='xzfyAcceptDetail()' valign='top'></td>");
	td.append("立 案 承 办 人");
	row.append(td);
	
	td = $("<td name='nodeid_50' id='50' class='ancestorstd'  onClick='xzfyAcceptDetail()' valign='top'></td>");
	td.append("立 案 科 室 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_60' id='60' class='ancestorstd'  onClick='xzfyAcceptDetail()' valign='top'></td>");
	td.append("立 案 机 构 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_70' id='70' class='ancestorstd'  onClick='xzfyAcceptDetail()' valign='top'></td>");
	td.append("立 案 机 关 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_80' id='80'  class='ancestorstd'  onClick='xzfyAccendDetail()' valign='top'></td>");
	td.append("受 理 决 定");
	row.append(td);
	
    td = $("<td name='nodeid_90' id='90' class='ancestorstd'  onClick='xzfyZhuansdjDetail()' valign='top'></td>");
	td.append("转 送 登 记 表");
	row.append(td);
	flowTable.append(row);
	
	if(typeof(rows) != "undefined"){
		queryNodeidsByCase(rows.caseid, rows.protype, rows.nodeid, Urls.queryNodeidsByCase);
	}
	
}

function shenli(){

	var rows = xzfyDataGrid.datagrid("getSelections")[0];
	
	var flowTable = $('#flowTable');
	
	$("#flowTable  tr:not(:first)").empty("");
	
	var row = $('#nodetr');
	var td = $("<td></td>");
	td.append("");
	row.append(td);
	var td = $("<td></td>");
	td.append("");
	row.append(td);
	var td = $("<td name='nodeid_100' id='100' class='ancestorstd'  onClick='xzfyslundertakerUrlDetail()' valign='top'></td>");
	td.append("指 定 审 理 承 办 人");
	row.append(td);

	td = $("<td name='nodeid_110' id='110' class='ancestorstd'  onClick='xzfyReviewDetail()' valign='top'></td>");
	td.append("审 理 承 办 人");
	row.append(td);

	td = $("<td name='nodeid_120' id='120' class='ancestorstd'  onClick='xzfyTingshenView()' valign='top'></td>");
	td.append("庭   审");
	row.append(td);
	
	td = $("<td name='nodeid_130' id='130' class='ancestorstd'  onClick='xzfyWeiysyView()' valign='top'></td>");
	td.append("委 员 审 议");
	row.append(td);
	
	td = $("<td name='nodeid_140' id='140' class='ancestorstd'  onClick='xzfyDiscussionView()' valign='top'></td>");
	td.append("集 体 讨 论");
	row.append(td);
	
	td = $("<td name='nodeid_150' id='150' class='ancestorstd'  onClick='xzfyReviewDetail()' valign='top'></td>");
	td.append("审 理 科 室 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_160' id='160'  class='ancestorstd' onClick='xzfyReviewDetail()' valign='top'></td>");
	td.append("审 理 机 构 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_170' id='170' class='ancestorstd'  onClick='xzfyReviewDetail()' valign='top'></td>");
	td.append("审 理 机 关 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_180' id='180' class='ancestorstd'  onClick='xzfyDecisionDetail()' valign='top'></td>");
	td.append("审 理 决 定");
	row.append(td);
	flowTable.append(row);
	
	if(typeof(rows) != "undefined"){
		queryNodeidsByCase(rows.caseid, rows.protype, rows.nodeid, Urls.queryNodeidsByCase);
	}
}

function jueding(){
	
	var rows = xzfyDataGrid.datagrid("getSelections")[0];
	
	var flowTable = $('#flowTable');
	
	$("#flowTable  tr:not(:first)").empty("");
	
	var row = $('#nodetr');
	var td = $("<td></td>");
	td.append("");
	row.append(td);
	var td = $("<td></td>");
	td.append("");
	row.append(td);
	var td = $("<td></td>");
	td.append("");
	row.append(td);
	var td = $("<td name='nodeid_190' id='190'  class='ancestorstd' onClick='documentDecisionDetail()' valign='top'></td>");
	td.append("行 政 复 议 决 定 书");
	row.append(td);
	
	td = $("<td name='nodeid_200' id='200' class='ancestorstd'  onClick='documentDeliveryDetailHear()' valign='top'></td>");
	td.append("文 书 送 达");
	row.append(td);
	
	td = $("<td name='nodeid_210' id='210'  class='ancestorstd'  onClick='callbackDetail()' valign='top'></td>");
	td.append("廉 政 回 访");
	row.append(td);
	
	td = $("<td name='nodeid_220' id='220' class='ancestorstd'  onClick='documentCloseDetailHear()' valign='top'></td>");
	td.append("结 案 归 档");
	row.append(td);
	flowTable.append(row);
	
	if(typeof(rows) != "undefined"){
		queryNodeidsByCase(rows.caseid, rows.protype, rows.nodeid, Urls.queryNodeidsByCase);
	}
	
}
/**
 * 显示流程进度
 */
function viewFlow(){
	$('th').css("backgroundColor", "");
	$('td[name^="nodeid_"]').css("backgroundColor", "");
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var nodeid = row.nodeid;
	var caseid = row.caseid;
	var protype = row.protype;
	
	if(nodeid < 20){
		jiedai();
		$('#td_jd').css("background-color","#EE4000");
		queryNodeidsByCase(caseid,protype,nodeid,Urls.queryNodeidsByCase);
	}
	
	if(nodeid < 100 && nodeid >10){
		lian();
		$('#td_jd').css("background-color","#CCFF99");
		$('#td_la').css("background-color","#EE4000");
		queryNodeidsByCase(caseid,protype,nodeid,Urls.queryNodeidsByCase);
	}
	if( nodeid > 90 && nodeid < 190){
		shenli();
		$('#td_jd').css("background-color","#CCFF99");
		$('#td_la').css("background-color","#CCFF99");
		$('#td_sl').css("background-color","#EE4000");
		queryNodeidsByCase(caseid,protype,nodeid,Urls.queryNodeidsByCase);
	}
	if(nodeid > 180){
		jueding();
		$('#td_jd').css("background-color","#CCFF99");
		$('#td_la').css("background-color","#CCFF99");
		$('#td_sl').css("background-color","#CCFF99");
		$('#td_jy').css("background-color","#EE4000");
		queryNodeidsByCase(caseid,protype,nodeid,Urls.queryNodeidsByCase);
	}
	
}

//ajax 根据caseid 查询 经过的流程节点，并将其之色；
function  queryNodeidsByCase(caseid,protype,nodeid,url){
	
	$.ajax({
		type:'post',
		url:url,
		async : false,
		data : {
			caseid : caseid,
			protype: protype, 
			nodeid : nodeid
		},
		success:function(data) {
			
				$('td[name^="nodeid_"]').each(function (index, domEle) {
					
					var nodeid_id = this.id;                                  //tb标签的id.
//		alert(nodeid_id+"--nodeid_id"+nodeid);			
					if(nodeid_id > nodeid){
						$(domEle).css("display", "none"); 
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
 * 实现时间进度
 */

/**
 * 根据当前案件审结结果
 */
function isShowByProtype(protype, oldprotype)
{
	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
	// 如果变更流程类型
	if (protype.indexOf("n") > -1)
	{
		protype = protype.replace("n","");
		if ("07" == protype) // 延期
		{
			form.find("#delay").show();
		}
	}
	else
	{
		form.find("#delay").hide();
	}
}

function isShowSendunit (id) {
	var modalDialog = parent.$.modalDialog.handler;
	if ("03" == id) {
		modalDialog.find("#sendunitTr").show();
	}
	else {
		modalDialog.find("#sendunitTr").hide();
	}
}