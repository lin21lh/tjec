//请求路径
var base = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var Urls = {
	receiveDetailUrl: base + "xzfyReceiveDetail.do",               // 接收材料详情
	reqDetailUrl: base + "xzfyReqDetail.do",                       // 案件登记详情
	acceptDetailUrl: base + "xzfyAcceptDetail.do",			       // 受理详情
	accendDetailUrl: base + "xzfyAccendDetail.do",			       // 受理决定详情
	reviewDetailUrl: base + "xzfyReviewDetail.do",				   // 审理详情
	decisionDetailUrl: base + "xzfyDecisionDetail.do",			   // 审理决定详情
	textproductionDetailUrl: base + "textProductionDetail.do",     // 文书制作详情
	documentDeliveryDetailUrl: base + "documentDeliveryDetail.do", // 文书送达详情
	documentReviewDetailUrl: base + "documentReviewDetail.do",     // 回访单详情
	summaryDetailUrl: base + "summaryDetail.do",                   // 备考表详情
	viewUrl: contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/spesugbaseinfoViewInit.do" // 委员意见详情
};

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
	openxzfyDetail(Urls.acceptDetailUrl, "受理详情", 900, 550, "3");
}

/**
 * 查看行政复议受理决定详细信息
 */
function xzfyAccendDetail(){
	openxzfyDetail(Urls.accendDetailUrl, "受理决定详情", 600, 300, "7");
}

/**
 * 查看行政复议审理详细信息
 */
function xzfyReviewDetail(){
	openxzfyDetail(Urls.reviewDetailUrl, "审理详情", 900, 550, "12");
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
	openxzfyDetail(Urls.textproductionDetailUrl, "文书制作详情", 1000, 550, "17");
}

/**
 * 打开文书送达详情页面_18
 * @param row
 */
function documentDeliveryDetailHear() {
	openxzfyDetail(Urls.documentDeliveryDetailUrl, "文书送达详情", 900, 550, "18");
}

/**
 * 打开回访单详情页面_19
 * @param row
 */
function documentReviewDetailHear() {
	openxzfyDetail(Urls.documentReviewDetailUrl, "回访单详情", 900, 550, "19");
}

/**
 * 打开备考表详情页面_20
 * @param row
 */
function summaryDetail() {
	openxzfyDetail(Urls.summaryDetailUrl, "备考表详情", 900, 550, "20");
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

/**
 * 显示流程进度
 */
function viewFlow(){
	
	$('th[name^="nodeid_"]').css("backgroundColor", "");
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var nodeid = row.nodeid;
	$('th[name^="nodeid_"]').each(function (index, domEle) {
		
		var nodeid_id = this.id;
        if (nodeid_id < nodeid) {
        	$(domEle).css("backgroundColor", "#CCFF99");  
        } else if (nodeid_id == nodeid){
        	
        	var opttype = row.opttype;
        	if (opttype == 3 || opttype == 4) {
        		$(domEle).css("backgroundColor", "#CCFF99");
        	} else {
        		$(domEle).css("backgroundColor", "#FF8888"); 
        	}
        	 
        } else {
        	return;
        }
    });
}

/**
 * 实现时间进度
 */
function viewTime(){
	
	//先显示所有办理期限的TD
	$('td[name^="time_"]').show();   
	$('td[name^="time_"]').css("backgroundColor", "");
	
	//办理天数，默认60天
	var total = 60;
	
	//获取当前日期
	var currDate = getNowFormatDate();
	
	//获取申请日期
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var optdate = row.appdate;
	//延期天数
	var delaydays = row.delaydays;
	
	//判断是否延期，延期则在办理期限上加上延期天数
	var re = /^[0-9]+.?[0-9]*$/;
	if (re.test(delaydays)) {
		total += parseInt(delaydays);
	}
	
	var ttNum=0;
	if((total % 5) == 0){
		ttNum = parseInt(total / 5);	
	} else {
		ttNum = parseInt(total / 5) + 1;
	}
	for(var j = ttNum + 1; j <= 18; j++){
		
		var tdname = "time_" + (j*5);
		$('td[name="' + tdname + '"]').css('display', 'none');    
	}
	
	//计算时间间隔天数
	var days = DateDiff(currDate, optdate);
	
	$('td[name^="time_"]').each(function (index, domEle) {
		
		var time_id = this.id;
		var arr = time_id.split("-");
		var minDays = arr[0];
		var maxDays= arr[1];
		if (days > minDays && days > maxDays) {
        	$(domEle).css("backgroundColor", "#CCFF99");  
        }else if (days >= minDays && days <= maxDays) {
        	$(domEle).css("backgroundColor", "#FF8888");  
        }  else {
        	return;
        }
    });
}

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