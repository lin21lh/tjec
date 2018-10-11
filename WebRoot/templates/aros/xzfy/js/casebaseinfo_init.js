1//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	// 查询案件列表
	gridUrl:baseUrl + "queryXzfyList.do",
	// 接收材料编辑
	receiveUrl:baseUrl + "xzfyReceiveEdit.do",
	// 接收材料保存
	receiveSaveUrl:baseUrl + "xzfyReceiveSaveEdit.do",
	// 接收材料发送
	receiveSendUrl:baseUrl + "xzfyReceiveFlow.do",
	// 案件登记编辑
	reqUrl:baseUrl + "xzfyReqEdit.do",
	// 案件登记保存
	reqSaveUrl:baseUrl + "xzfyReqSaveEdit.do",
	// 案件登记发送
	reqSendUrl:baseUrl + "xzfyReqFlow.do",
	// 受理审批编辑
	acceptUrl:baseUrl + "xzfyAcceptEdit.do",
	// 受理审批保存
	acceptSaveUrl:baseUrl + "xzfyAcceptSaveEdit.do",
	// 受理审批发送
	acceptSendUrl:baseUrl + "xzfyAcceptFlow.do",
	// 受理决定编辑
	accendUrl:baseUrl + "xzfyAccendEdit.do",
	// 受理决定保存
	accendSaveUrl:baseUrl + "xzfyAccendSaveEdit.do",
	// 受理决定发送
	accendSendUrl:baseUrl + "xzfyAccendFlow.do",
	// 审理审批编辑
	reviewUrl:baseUrl + "xzfyReviewEdit.do",
	// 审理审批保存
	reviewSaveUrl:baseUrl + "xzfyReviewSaveEdit.do",
	// 审理审批发送
	reviewSendUrl:baseUrl + "xzfyReviewFlow.do",
	
	// 审理决定编辑
	decisionUrl:baseUrl + "xzfyDecisionEdit.do",
	// 审理决定保存
	decisionSaveUrl:baseUrl + "xzfyDecisionSaveEdit.do",
	// 审理决定发送
	decisionSendUrl:baseUrl + "xzfyDecisionFlow.do",
	
	// 转送登记表编辑
	transregistEditUrl:baseUrl + "transregistEdit.do",   
	// 转送登记表保存
	transregistSaveUrl:baseUrl + "transregistSaveEdit.do",
	// 转送登记表发送
	transregistSendUrl:baseUrl + "transregistFlow.do",
	
	// 指定承办人
	slundertakerEditUrl:baseUrl + "slundertakerEdit.do",   
	// 指定承办人保存
	slundertakerSaveUrl:baseUrl + "slundertakerSaveEdit.do",
	// 指定承办人发送
	slundertakerSendUrl:baseUrl + "slundertakerFlow.do",
	
	// 审理审批编辑
	hearUrl:baseUrl + "xzfyHearEdit.do",
	// 审理审批保存
	hearSaveUrl:baseUrl + "xzfyHearSaveEdit.do",
	// 审理审批发送
	hearSendUrl:baseUrl + "xzfyHearFlow.do",
	
	// 庭审/集体/委员编辑
	trialEditUrl:baseUrl + "xzfyTrialEdit.do",
	// 庭审/集体/委员 保存
	trialSaveUrl:baseUrl + "xzfyTrialSaveEdit.do",
	// 庭审/集体/委员 发送
	trialSendUrl:baseUrl + "xzfyTrialFlow.do",
	
	// 文书制作/结案归档编辑
	noticeUrl:baseUrl + "textProductionEdit.do",
	// 文书送达发送
	noticeSendUrl:baseUrl + "xzfyCaseEndNoticePlaceFlow.do",
	
	// 文书送达编辑
	deliveryUrl:baseUrl + "documentDeliveryEdit.do",
	// 文书送达保存
	deliverySaveUrl:baseUrl + "documentDeliverySaveEdit.do",
	// 文书送达发送
	deliverySendUrl:baseUrl + "xzfyDeliveryFlow.do",
	
	// 廉政回访编辑
	callbackUrl:baseUrl + "callbackEdit.do",
	// 廉政回访保存
	callbackSaveUrl:baseUrl + "callbackSaveEdit.do",
	// 廉政回访发送
	callbackSendUrl:baseUrl + "xzfycallbackFlow.do",
	
	
	// 回访单编辑
	docReviewUrl:baseUrl + "documentReviewEdit.do",
	// 回访单保存
	docReviewSaveUrl:baseUrl + "documentReviewSaveEdit.do",
	// 回访单发送
	docReviewSendUrl:baseUrl + "xzfyDocReviewFlow.do",
	// 备考表编辑
	summaryUrl:baseUrl + "summaryEdit.do",
	// 备考表保存
	summarySaveUrl:baseUrl + "summarySaveEdit.do",
	// 备考表发送
	summarySendUrl:baseUrl + "xzfySummaryFlow.do",
	// 回退
	backFlowPageUrl:baseUrl + "backFlowPage.do",
	
	returnUrl:baseUrl + "xzfyReturn.do",
	saveAjZtUrl : baseUrl + "saveAjZt.do",   //复议研讨
	xsajUrl:baseUrl+ "similcaseManagement_init.do",	// 相似案件
	deleteCaseUrl:baseUrl+ "deleteCase.do"	// 删除案件
	/*// 查询附件列表
	queryFileUrl:baseUrl + "queryFileList.do",
	// 下载附件
	downloadFileUrl: "FileDownController_download.do",
	// 查询文书列表
	noticeGridUrl:baseUrl + "queryNoticeList.do",
	// 查询文书模板列表
	NoticeTmpGridUrl:baseUrl + "queryNoticeTmpList.do",
	// 新增文书 
	addUrl:baseUrl + "noticeInfoAdd.do",
	// 查询文书内容
	getClobContentVal: baseUrl+"getClobContentVal.do",
	// 查询文书详情
	getContentsForDetail: baseUrl + "getContentsForDetail.do",
	// 下载文书
	downloadNoticeUrl: baseUrl+"noticeDownload.do",
	// 保存文书
	noticeSaveUrl:baseUrl + "noticeInfoSave.do",
	// 删除已有文书
	noticeDelUrl:baseUrl + "noticeInfoDelete.do",
	// 发送
	NoticeSendUrl:baseUrl + "xzfyNoticeFlow.do",
	// 查询回访单内容
	getClobContentValue: baseUrl+"getClobContentValue.do"*/
};

var panel_ctl_handles = [{
	panelname:"#xzfyPanel", 	// 要折叠的面板id
	gridname:"#xzfyDataGrid",  	// 刷新操作函数
	buttonid:"#openclose"	 	// 折叠按钮id
}];

//默认加载
$(function() {
	$('#xzfyPanel').panel('close');
	
	comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name");	                // 行政管理类型
	comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name");	// 申请复议事项类型
	comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name");	    // 被申请人类型
	comboboxFuncByCondFilter(menuid, "apptype", "B_CASEBASEINFO_APPTYPE", "code", "name");	    // 申请人类型
	
	var icons = {iconCls:"icon-clear"};
	$("#appname").textbox("addClearBtn", icons);
	$("#defname").textbox("addClearBtn", icons);
	$("#admtype").textbox("addClearBtn", icons);
	$("#casetype").textbox("addClearBtn", icons);
	$("#deftype").textbox("addClearBtn", icons);
	$("#apptype").textbox("addClearBtn", icons);
	
	if ("LA" == key) {
		// 可发起立案
		$("#LABtn").linkbutton('enable');
	}
	else if ("SL" == key) {
		// 可发起立案
		$("#SLBtn").linkbutton('enable');
	}
	
	//加载Grid数据
	loadXzfyGrid(urls.gridUrl);
});

//加载可项目grid列表
var xzfyDataGrid;
function loadXzfyGrid(url) {
	xzfyDataGrid = $("#xzfyDataGrid").datagrid({
		fit: true,
		stripe: true,
		singleSelect: true,
		rownumbers: true,
		pagination: true,
		remoteSort: false,
		multiSort: true,
		pageSize: 10,
		queryParams: {
			menuid:menuid,
			firstNode:true,
			lastNode:false
		},
		url: url,
		loadMsg: "正在加载，请稍候......",
		toolbar: "#toolbar_center",
		showFooter: true,
		onDblClickRow: function (rowIndex, rowData) {  
			xzfyEditDialog(rowData);
        },
        onClickRow: function(rowIndex,rowData) {
        	viewTime();
        	viewFlow();
        	showHiddenDeleteBtn(rowData.nodeid);
        },
		columns:[[ 
		  {field:"caseid", checkbox:true},
		  {field:"csaecode", title:"案件编号", halign:"center", width:220, sortable:true},
		  {field:"appname", title:"申请人", halign:"center", width:200, sortable:true},
		  {field:"apptypeMc", title:"申请人类型", halign:"center", width:150, sortable:true},
		  {field:"defname", title:"被申请人", halign:"center", width:200, sortable:true},
		  {field:"deftypeMc", title:"被申请人类型", width:150, sortable:true},
		  {field:"admtypeMc", title:"行政管理类型", halign:"center", width:100, sortable:true},
		  {field:"casetypeMc", title:"申请复议事项", halign:"center", width:100, sortable:true},
		  {field:"receivedate", title:"收文日期", halign:"center", width:80, sortable:true},
		  {field:"idtypeMc", title:"证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"idcode", title:"证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"phone", title:"联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"address", title:"通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"postcode", title:"邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"depttype", title:"被申请人机构类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidtype", title:"被申请人证件类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defidcode", title:"被申请人证件号码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defphone", title:"被申请人联系电话", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defaddress", title:"被申请人通讯地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defpostcode", title:"被申请人邮政编码", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"noticeddate", title:"接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"actnoticeddate", title:"实际接受告知日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"ifcompensationMc", title:"是否附带行政赔偿", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"amount", title:"赔偿金额", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appcase", title:"申请事项", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"factreason", title:"事实和理由", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"annex", title:"附件", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"appdate", title:"申请日期", halign:"center", width:80, sortable:true, hidden:true},
		  {field:"operator", title:"操作人", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"optdate", title:"操作日期", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"protype", title:"流程类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"opttype", title:"处理标志", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"nodeid", title:"节点编号", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"lasttime", title:"数据最后更新时间", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"userid", title:"用户ID", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"oldprotype", title:"原流程类型", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"mobile", title:"联系手机", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"mailaddress", title:"邮寄地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"email", title:"邮箱", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defmobile", title:"被申请人联系手机", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defmailaddress", title:"被申请人邮寄地址", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"defemail", title:"被申请人邮箱", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"receiveway", title:"收文方式", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"expresscom", title:"递送公司", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"couriernum", title:"递送单号", halign:"center", width:200, sortable:true, hidden:true},
		  {field:"datasourceMc", title:"数据来源", halign:"center", width:80, sortable:true, hidden:true}
        ]]
	});
}

/**
 * 条件查询
 */
function xzfyQuery() {
	var param = {
		menuid: menuid,
		appname: $("#appname").val(),
		defname: $("#defname").val(),
		admtype: $("#admtype").combobox('getValue'),
		casetype:$("#casetype").combobox('getValue'),
		deftype: $("#deftype").combobox('getValue'),
		apptype: $("#apptype").combobox('getValue'),
		firstNode:true,
		lastNode:false
	};
	
	xzfyDataGrid.datagrid("load", param);
}

/**
 * 重载
 */
function showXzfyDataGridReload() {
	xzfyDataGrid.datagrid("reload"); 
}

/**
 * 初始行政复议接收申请材料页面
 */
function xzfyReceive(title, code, caseid, nodeid) {
	parent.$.modalDialog({
		title: "接收申请材料",
		width: 600,
		height: 350,
		href: urls.receiveUrl,
		queryParams: {
			caseid: caseid,
			nodeid: 20,
		},
		onDestroy: function() {
			showXzfyDataGridReload();
		},
		buttons:[{
				id: "fileBtn",
				disabled:true,
				text:"附件",
				iconCls:"icon-save",
				handler:function() {
					parent.clickUploadDiv2("SQCL");
				}
			},{
				id: "photosBtn",
				disabled:true,
				text:"拍照上传",
				iconCls:"icon-save",
				handler:function() {
					parent.clickPhotosDiv(code);
				}
			},{
				id: "saveBtn",
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					submitForm(urls.receiveSaveUrl, "xzfyReceiveForm", 20);
				}
			},{
				id: "sendBtn",
				disabled:true,
				text:"发送",
				iconCls:"icon-redo",
				handler:function() {
					send(urls.receiveSendUrl, caseid, 1, 'xzfyReceiveForm');
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
			}]
	});
}

/**
 * 打开复议审理页面
 */
function xzfyReview() {
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	if (row.nodeid < 11) {
		easyui_warn("受理流程未结束 !", null);
		return;
	}
	if (row.nodeid == 11) {
		easyui_warn("请先双击案件接收答复材料 !", null);
		return;
	}
	
	parent.$.modalDialog({
		title: "行政复议(决定)审批表",
		width: 900,
		height: 550,
		href: urls.reviewUrl,
		queryParams: {
			caseid: row.caseid,
			protype: row.protype
		},
		onLoad: function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "result", "HEAR", "code", "name", mdDialog); // 审结结果
			isReadonlyByNodeid(12);
			mdDialog.find("#result").combobox({
				onSelect: function(record) {
					isShowByProtypeFuc(record.id, row.protype);
				}
			});
			clearRemark();
		},
		onDestroy: function() {
			showXzfyDataGridReload();
		},
		buttons:[{
			id:"saveBtn",
			text:"保存",
			iconCls:"icon-save",
			handler:function() {
				submitForm(urls.reviewSaveUrl, "xzfyReviewForm", 12);
			}
		},{
			id:"auditBtn",
			text:"发送",
			iconCls:"icon-redo",
			handler:function() {
				send(urls.reviewSendUrl, row.caseid, 12);
			}
		},{
			id:"backBtn",
			text:"回退",
			iconCls:"icon-undo",
			handler:function() {
				xzfyReturn(row);
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}

/**
 * 初始化案件登记页面
 */
var nodeidtmp = 1;
function xzfyEditDialog(row) {
	nodeidtmp = row.nodeid;
//	if ("LA" == key && nodeidtmp > 10)
//	{
//		easyui_warn("请到审理入口处理 !", null);
//		return;
//	}
//	else if ("SL" == key && nodeidtmp < 11)
//	{
//		easyui_warn("请到立案入口处理 !", null);
//		return;
//	}
//	if ("SL" == key && nodeidtmp == 20 && "02" == row.protype && "3" == row.opttype)
//	{
//		easyui_warn("该案件已中止请恢复后再处理 !", null);
//		return;
//	}
	if (20 == nodeidtmp || 11 == nodeidtmp) {
		xzfyEdit("接收材料", 600, 350, urls.receiveUrl, row, "xzfyReceiveForm", urls.receiveSaveUrl, urls.receiveSendUrl);
	}
	else if (30 == nodeidtmp) {
		xzfyEdit("案件登记", 900, 550, urls.reqUrl, row, "xzfyReqForm", urls.reqSaveUrl, urls.reqSendUrl);
	}
	else if (40 == nodeidtmp || 50 == nodeidtmp || 60 == nodeidtmp || 70 == nodeidtmp) {
		xzfyEdit("受理审批", 900, 550, urls.acceptUrl, row, "xzfyAcceptForm", urls.acceptSaveUrl, urls.acceptSendUrl);
	}
	else if (80 == nodeidtmp) {
		xzfyEdit("受理决定", 900, 550, urls.accendUrl, row, "xzfyAccendForm", urls.accendSaveUrl, urls.accendSendUrl);
	}
	else if (90 == nodeidtmp) {
		xzfyEdit("转送登记表", 900, 560, urls.transregistEditUrl, row, "documentReviewForm", urls.transregistSaveUrl, urls.transregistSendUrl);
	}
	else if (100 == nodeidtmp) {
		xzfyEdit("指定承办人", 700, 350, urls.slundertakerEditUrl, row, "form", urls.slundertakerSaveUrl, urls.slundertakerSendUrl);
	}
	else if (110 == nodeidtmp || 150 == nodeidtmp || 160 == nodeidtmp || 170 == nodeidtmp) {
		xzfyEdit("审理审批", 900, 550, urls.hearUrl, row, "xzfyhearForm", urls.hearSaveUrl, urls.hearSendUrl);
	}
	else if (120 == nodeidtmp) {
		xzfyEdit("庭审", 900, 550, urls.trialEditUrl +"?reviewtype=01", row, "caseIdForm", urls.trialSaveUrl, urls.trialSendUrl);
	}
	else if (130 == nodeidtmp) {
		xzfyEdit("委员审议", 900, 550, urls.trialEditUrl+"?reviewtype=03", row, "caseIdForm", urls.trialSaveUrl, urls.trialSendUrl);
	}
	else if (140 == nodeidtmp) {
		xzfyEdit("集体讨论", 900, 550, urls.trialEditUrl +"?reviewtype=02", row, "caseIdForm", urls.trialSaveUrl, urls.trialSendUrl);
	}
	else if (180 == nodeidtmp) {
		xzfyEdit("审理决定", 900, 300, urls.decisionUrl, row, "xzfyDecisionForm", urls.decisionSaveUrl, urls.decisionSendUrl);
	}
	else if (190 == nodeidtmp) {
		xzfyEdit("行政复议决定书", 900, 550, urls.transregistEditUrl, row, "documentReviewForm", urls.transregistSaveUrl, urls.transregistSendUrl);
	}
	else if (200 == nodeidtmp) {
		xzfyEdit("文书送达", 900, 550, urls.deliveryUrl, row, "caseIdForm", urls.deliverySaveUrl, urls.deliverySendUrl);
	}
	else if (210 == nodeidtmp) {
		xzfyEdit("廉政回访", 1000, 550, urls.callbackUrl, row, "caseIdForm", urls.callbackSaveUrl, urls.callbackSendUrl);
	}
	else if (220 == nodeidtmp) {
		xzfyEdit("结案归档", 1200, 550, urls.noticeUrl, row, "noticeForm", "", urls.noticeSendUrl);
	}
//	else if (11 == nodeidtmp || 19 == nodeidtmp) {
//		xzfyEdit("回访单", 900, 550, urls.docReviewUrl, row, "documentReviewForm", urls.docReviewSaveUrl, urls.docReviewSendUrl);
//	}
//	else if (12 == nodeidtmp || 13 == nodeidtmp || 14 == nodeidtmp || 15 == nodeidtmp) {
//		xzfyEdit("审理审批", 900, 550, urls.reviewUrl, row, "xzfyReviewForm", urls.reviewSaveUrl, urls.reviewSendUrl);
//	}
//	else if (16 == nodeidtmp) {
//		xzfyEdit("审理决定", 900, 300, urls.decisionUrl, row, "xzfyDecisionForm", urls.decisionSaveUrl, urls.decisionSendUrl);
//	}
//	else if (20 == nodeidtmp) {
//		xzfyEdit("备考表", 900, 550, urls.summaryUrl, row, "summaryForm", urls.summarySaveUrl, urls.summarySendUrl);
//	}
}
/**
 * 初始化文书制作与送达回执
 */

function xzfyEditDocument(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
		xzfyEdit("文书制作", 1000, 550, urls.noticeUrl, row, "noticeForm", "", "");
}

function xzfyEditDocumentResult(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条复议案件 !", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	xzfyEdit("文书送达", 900, 550, urls.deliveryUrl, row, "documentDeliveryForm", "", "");
}

function xzfyEdit(title, width, height, editUrl, row, form, saveUrl, sendUrl) {
	parent.$.modalDialog({
		title: title,
		width: width,
		height: height,
		href: editUrl,
		queryParams: {
			caseid: row.caseid,
			protype: row.protype,
			nodeid: nodeidtmp
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			var f = mdDialog.find("#"+form);
			if (30 == nodeidtmp) {
				comboboxFuncByCondFilter(menuid, "sex", "SEX", "code", "name", mdDialog);	    //申请人性别
				comboboxFuncByCondFilter(menuid, "apptype", "B_CASEBASEINFO_APPTYPE", "code", "name", mdDialog);	    //申请人类型
				comboboxFuncByCondFilter(menuid, "idtype", "B_CASEBASEINFO_IDTYPE", "code", "name", mdDialog);		    //证件类型
				comboboxFuncByCondFilter(menuid, "deftype", "B_CASEBASEINFO_DEFTYPE", "code", "name", mdDialog);	    //被申请人类型
				comboboxFuncByCondFilter(menuid, "admtype", "ADMINLEVEL", "code", "name", mdDialog);	                //行政管理类型
				comboboxFuncByCondFilter(menuid, "casetype", "B_CASEBASEINFO_CASETYPE", "code", "name", mdDialog);	    //申请复议事项类型
				comboboxFuncByCondFilter(menuid, "ifcompensation", "SYS_TRUE_FALSE", "code", "name", mdDialog);	        //是否附带行政赔偿
				comboboxFuncByCondFilter(menuid, "receiveway", "PUB_PROBASEINFO_RECEIVEWAY", "code", "name", mdDialog);	//收文方式
				comboboxFuncByCondFilter(menuid, "thtype", "B_CASEBASEINFO_APPTYPE", "code", "name", mdDialog);	        //第三人类型
				comboboxFuncByCondFilter(menuid, "thidtype", "B_CASEBASEINFO_IDTYPE", "code", "name", mdDialog);	    //第三人证件类型
				comboboxFuncByCondFilter(menuid, "type", "CASEREASONTYPE", "code", "name", mdDialog);	    //第三人证件类型
				comboboxFuncByCondFilter(menuid, "thtype", "B_CASEBASEINFO_APPTYPE", "code", "name", mdDialog);	    //第三人类型
				comboboxFuncByCondFilter(menuid, "thidtype", "B_CASEBASEINFO_IDTYPE", "code", "name", mdDialog);    //第三人证件类型
				f.form("load", row);
			}
			else if (40 == nodeidtmp || 50 == nodeidtmp || 60 == nodeidtmp || 70 == nodeidtmp) {
				comboboxFuncByCondFilter(menuid, "b", "SYS_TRUE_FALSE", "code", "name", mdDialog);//依据
				comboboxFuncByCondFilter(menuid, "slcasesort", "CASESORT", "code", "name", mdDialog);//案件流程程序类型  普通 简易
				isReadonlyByNodeid(nodeidtmp);
			}
			else if (80 == nodeidtmp) {
				comboboxFuncByCondFilter(menuid, "result", "PUB_PROBASEINFO_RESULT", "code", "name", mdDialog);   // 处理结果
				comboboxFuncByCondFilter(menuid, "isgreat", "SYS_TRUE_FALSE", "code", "name", mdDialog);	      // 是否重大案件备案
				isShowSendunit(mdDialog.find("#result").val());
				mdDialog.find("#result").combobox({
					onSelect: function(record) {
						isShowSendunit(record.id);
					}
				});
				// 审理审核  承办人->科室->机构->机关
			}else if (110 == nodeidtmp || 150 == nodeidtmp || 160 == nodeidtmp || 170 == nodeidtmp) {
				comboboxFuncByCondFilter(menuid, "isreview", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否委员审议
				comboboxFuncByCondFilter(menuid, "isdiscuss", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否集体讨论
				comboboxFuncByCondFilter(menuid, "casesort", "CASESORT", "code", "name", mdDialog);//案件流程程序类型  普通 简易
				hearisReadonlyByNodeid(nodeidtmp);
				mdDialog.find("#casesort").combobox({
					onSelect: function(record) {
						var casesort = mdDialog.find("#casesort").combobox("getValue");
						if(casesort == '01'){
							mdDialog.find("#isreview").combobox("setValue",'0');
							mdDialog.find("#isdiscuss").combobox("setValue",'0');
							mdDialog.find("#isreviewtr").hide();
							mdDialog.find("#isdiscusstr").hide();
						}else if(casesort == '02'){
							mdDialog.find("#isreviewtr").show();
							mdDialog.find("#isdiscusstr").show();
						}
					}
				});
				if(row.casesort == '01'){
					mdDialog.find("#isreview").combobox("setValue",'0');
					mdDialog.find("#isdiscuss").combobox("setValue",'0');
					mdDialog.find("#isreviewtr").hide();
					mdDialog.find("#isdiscusstr").hide();
				}
			}else if (120 == nodeidtmp || 130 == nodeidtmp || 140 == nodeidtmp || 170 == nodeidtmp) {
			}
			else if (180 == nodeidtmp) {
				comboboxFuncByCondFilter(menuid, "result01", "HEARRESULT", "code", "name", mdDialog);       // 审结结果
				comboboxFuncByCondFilter(menuid, "result02", "AUDITRESULT", "code", "name", mdDialog);	    // 审理结果是否同意
				comboboxFuncByCondFilter(menuid, "isgreat", "SYS_TRUE_FALSE", "code", "name", mdDialog);	// 是否重大案件备案
				isShow(row.protype);// 延期是否展示
			}
//			else if (12 == nodeidtmp || 13 == nodeidtmp || 14 == nodeidtmp || 15 == nodeidtmp) {
//				comboboxFuncByCondFilter(menuid, "result", "HEAR", "code", "name", mdDialog); // 审结结果
//				isShowByProtypeFuc(row.protype, row.protype);// 延期是否展示
//				isReadonlyByNodeid(nodeidtmp);// 审批意见等是否只读
//				mdDialog.find("#result").combobox({
//					onSelect: function(record) {
//						isShowByProtypeFuc(record.id, row.protype);
//					}
//				});
//			}
//			else if (16 == nodeidtmp) {
//				comboboxFuncByCondFilter(menuid, "result01", "HEARRESULT", "code", "name", mdDialog);       // 审结结果
//				comboboxFuncByCondFilter(menuid, "result02", "AUDITRESULT", "code", "name", mdDialog);	    // 审理结果是否同意
//				comboboxFuncByCondFilter(menuid, "isgreat", "SYS_TRUE_FALSE", "code", "name", mdDialog);	// 是否重大案件备案
//				isShow(row.protype);// 延期是否展示
//			}
		},
		onDestroy: function() {
			showXzfyDataGridReload();
		},
		buttons: getButtons(row, form, saveUrl, sendUrl)
	});
}

function getButtons (row, form, saveUrl, sendUrl) {
	var buttons;
	if(saveUrl == '' && sendUrl==''){
		buttons= [{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {parent.$.modalDialog.handler.dialog("close");}
			}]
	}else{
		if (20 == nodeidtmp) {
			var code = "SQCL";
			if (11 == nodeidtmp) {
				code = "DFCL";
			}
			buttons= [{
				id: "fileBtn",
				disabled:true,
				text:"附件",
				iconCls:"icon-save",
				handler:function() {
					parent.clickUploadDiv2(code);
				}
			},{
				id: "photosBtn",
				disabled:true,
				text:"拍照上传",
				iconCls:"icon-save",
				handler:function() {
					parent.clickPhotosDiv(code);
				}
			},{
				id: "saveBtn",
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					submitForm(saveUrl, form, nodeidtmp);
				}
			},{
				id:"sendBtn",
				disabled:true,
				text:"发送",
				iconCls:"icon-redo",
				handler:function() {
					send(sendUrl, row.caseid, row.nodeid);
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
			}]
		}else if(220 == nodeidtmp){
			buttons= [{
				id:"sendBtn",
				text:"结案归档",
				iconCls:"icon-redo",
				handler:function() {
					send(sendUrl, row.caseid, row.nodeid);
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
			}]
		}else {
			buttons= [{
				id:"saveBtn",
				text:"保存",
				iconCls:"icon-save",
				handler:function() {
					var mdDialog = parent.$.modalDialog.handler;
					if (90 == nodeidtmp || 190 == nodeidtmp) {
						//将编辑器值赋值给隐藏域content
						mdDialog.find("#contents").val(window.parent.editor.html());
					}
					submitForm(saveUrl, form, nodeidtmp);
				}
			},{
				id:"sendBtn",
				text:"发送",
				iconCls:"icon-redo",
				handler:function() {
					send(sendUrl, row.caseid, row.nodeid);
				}
			},{
				id:"backBtn",
				text:"回退",
				iconCls:"icon-undo",
				handler:function() {
					xzfyReturn(row);
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function() {
					parent.$.modalDialog.handler.dialog("close");
				}
			}]
		}
	}
	return buttons;
}

/**
 * 提交表单
 */
function submitForm(url, form, nodeid) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	var protype = "01";
	if (12 == nodeid || 13 == nodeid || 14 == nodeid || 15 == nodeid) {
		protype = form.find("#result").combobox('getValue');
		if (protype.indexOf("n") > -1) {
			protype = protype.replace("n","")
		}
		else {
			protype = "01";
		}
	}
	
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit", {
		url:url,
		queryParams: {
			protype: protype,
			nodeid: nodeid
		},
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
			parent.$.messager.progress("close");
			result = $.parseJSON(result);
			if (result.success) {
				if (20 == nodeid || 1 == nodeid) {
					var caseid = result.body.caseid;
					form.find("#caseid").val(caseid);
					parent.showFileBtn();
				}
				parent.$.messager.alert("提示", result.title, "success");
			} else {
				parent.$.messager.alert("错误", result.title, "error");
			}
		}
	});
}

/**
 * 发送
 */
function send(url, caseid, nodeid) {
	var modalDialog = parent.$.modalDialog.handler;
	var protype = "01";
	if (12 == nodeid || 13 == nodeid || 14 == nodeid || 15 == nodeid) {
		protype = modalDialog.find("#result").combobox('getValue');
		if (protype.indexOf("n") > -1) {
			protype = protype.replace("n","")
		}
		else {
			protype = "01";
		}
	}
	var arid = '';
	if (20 == nodeid || 1 == nodeid) {
		caseid = parent.$.modalDialog.handler.find('#xzfyReceiveForm').find("#caseid").val();
		arid = parent.$.modalDialog.handler.find('#xzfyReceiveForm').find("#arid").val();
	}
	var prompt = "确认要发送选中复议申请？";
	if(nodeid == '220'){
		 prompt = "确认要将案件结案归档？";
	}
	parent.$.messager.confirm("送审确认", prompt, function(r) {
		if (r) {
			$.post(url, {
				menuid:menuid,
				caseid:caseid,
				arid : arid,
				protype:protype
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
 * 回退
 */
function xzfyReturn(row) {
		parent.$.modalDialog2({
			title:"回退意见",
			width:500,	
			height:160,
			href:urls.backFlowPageUrl,
			onLoad:function() {
			},
			buttons:[{
					text:"确认回退",
					iconCls:"icon-save",
					handler:function() {
						var mdDialog = parent.$.modalDialog.handler2;
						var htyjform = mdDialog.find("#thyjform");
						var isValid = htyjform.form("validate");
						if (!isValid) {
							parent.$.messager.progress("close");
							return isValid;
						}
						var htyj = htyjform.find("#htyj").val();
						$.post(urls.returnUrl, {
							menuid:menuid,
							caseid:row.caseid,
							htyj :htyj 
						}, function(result) {
							if (result.success) {
								parent.$.messager.alert("提示", result.title, "info");
								parent.$.modalDialog.handler2.dialog("close");
								parent.$.modalDialog.handler.dialog("close");
							} else {
								parent.$.messager.alert("错误", result.title, "error");
							}
						}, "json");
					}
				},{
					text:"关闭",
					iconCls:"icon-cancel",
					handler:function() {
						parent.$.modalDialog.handler2.dialog("close");
					}
				}]
		});
}

function clearRemark() {
	var mdDialog = parent.$.modalDialog.handler;
	mdDialog.find("#result").combobox('clear');
	mdDialog.find("#agentRemark").textbox('clear');
	mdDialog.find("#sectionRemark").textbox('clear');
	mdDialog.find("#organRemark").textbox('clear');
	mdDialog.find("#officeRemark").textbox('clear');
}

function isReadonlyByNodeid(nodeid)
{
	var form = parent.$.modalDialog.handler.find('#xzfyAcceptForm');
	if (40 == nodeid) // 承办人
	{
		form.find("#b").combobox({required:true});
		form.find("#agentRemark").textbox({required:true});
		form.find("#slcasesort").textbox({required:true});
		form.find("#b").combobox({"readonly":false});
		form.find("#agentRemark").textbox({"readonly":false});
		form.find("#slcasesort").combobox({"readonly":false});
	}
	else if (50 == nodeid) // 科室
	{
		form.find("#sectionRemark").textbox({required:true});
		form.find("#sectionRemark").textbox({"readonly":false});
	}
	else if (60 == nodeid) // 机构
	{
		form.find("#organRemark").textbox({required:true});
		form.find("#organRemark").textbox({"readonly":false});
	}
	else if (70 == nodeid) // 机关
	{
		form.find("#officeRemark").textbox({required:true});
		form.find("#officeRemark").textbox({"readonly":false});
	}
}

function hearisReadonlyByNodeid(nodeid)
{
	var form = parent.$.modalDialog.handler.find('#xzfyhearForm');
	if (110 == nodeid) // 承办人
	{
		form.find("#isreview").combobox({required:true});
		form.find("#isdiscuss").combobox({required:true});
		form.find("#agentRemark").textbox({required:true});
		form.find("#casesort").textbox({required:true});
		form.find("#isreview").combobox({"readonly":false});
		form.find("#isdiscuss").combobox({"readonly":false});
		form.find("#agentRemark").textbox({"readonly":false});
		form.find("#casesort").combobox({"readonly":false});
	}
	else if (150 == nodeid) // 科室
	{
		form.find("#sectionRemark").textbox({required:true});
		form.find("#sectionRemark").textbox({"readonly":false});
	}
	else if (160 == nodeid) // 机构
	{
		form.find("#organRemark").textbox({required:true});
		form.find("#organRemark").textbox({"readonly":false});
	}
	else if (170 == nodeid) // 机关
	{
		form.find("#officeRemark").textbox({required:true});
		form.find("#officeRemark").textbox({"readonly":false});
	}
}

/**
 * 根据当前案件审结结果
 */
function isShowByProtypeFuc(protype, oldprotype)
{
	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
	// 如果变更流程类型
	if (protype.indexOf("n") > -1)
	{
		protype = protype.replace("n","");
	}
	
	if ("07" == protype) // 延期
	{
		form.find("#delay").show();
		form.find("#delaydays").textbox({required:true});
		form.find("#delaydays").textbox({"readonly":false});
	}
	else
	{
		form.find("#delay").hide();
		form.find("#delaydays").textbox({required:false});
		form.find("#delaydays").textbox({"readonly":true});
	}
	
	// 审结结果是否变更
	if (oldprotype != protype)
	{
		form.find("#agentRemark").textbox('clear');
		form.find("#sectionRemark").textbox('clear');
		form.find("#organRemark").textbox('clear');
		form.find("#officeRemark").textbox('clear');
	}
}

/**
 * 是否显示延期天数
 */
function isShow(protype)
{
	var form = parent.$.modalDialog.handler.find('#xzfyDecisionForm');
	if ("07" == protype) // 延期
	{
		form.find("#delay").show();
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

function setspeIds(){
	var speids="";
	var rows = yxgrid.datagrid("getRows");
	for(var i = 0; i < rows.length; i++){
		speids = speids + rows[i].speid + ";"
	}
	parent.$.modalDialog.handler.find('#zjxzForm').find("#speids").val(speids);
}

//可选委员列表
function zjDataGrid(mdDialog, groupid) {
	
	var zjUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "querySpeList.do";
	
	kxgrid = mdDialog.find("#zjTable").datagrid({
		title : "可选委员列表",
		height : 400,
		width : '100%',
		collapsible : false,
		url : zjUrl,
		queryParams : {
			groupid : groupid,
			operflag : 'all'
		},
		singleSelect : false,
		rownumbers : true,
		idField : 'speid',
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 100,
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
		} ] ],
        onDblClickRow:function (rowIndex, rowData) {       	 
    	  mdDialog.find("#zjxzTable").datagrid('appendRow', rowData);//追加一行
    	  mdDialog.find("#zjTable").datagrid('deleteRow',rowIndex);//删除一行
        }
	});
	return kxgrid;
}

//已选委员列表
function zjxzDataGrid(mdDialog,groupid){
	var zjUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "querySpeList.do";
	
	yxgrid = mdDialog.find("#zjxzTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选委员列表',
		collapsible: false,
		url : zjUrl,
		queryParams : {groupid:groupid,operflag : 'group'},
		singleSelect: false,
		rownumbers : true,
		idField: 'speid',
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 100,
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
		} ] ],
       onDblClickRow:function (rowIndex, rowData) {		        	 
    	   mdDialog.find("#zjTable").datagrid('appendRow', rowData);//追加一行
     	   mdDialog.find("#zjxzTable").datagrid('deleteRow',rowIndex);//删除一行
       }
	});
	return yxgrid;
}

//加载可选卷宗信息
function jzDataGrid(mdDialog,groupid,caseid){

	var jzUrl=contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do";
	
	jzgrid = mdDialog.find("#jzTable").datagrid({
		height: 400,
		width:'100%',
		title: '可选卷宗列表',
		collapsible: false,
		url : jzUrl,
		queryParams : {groupid:groupid,caseid:caseid},
		singleSelect: false,
		rownumbers : true,
		idField: 'noticeid',
		columns : [ [ {
			field : "noticeid",
			checkbox : true
		},{  
			field : "doctype",
			title : "文档名称",
			halign : 'center',
			width : 100,
			sortable : false
		},{  
			field : "protype",
			title : "流程类型",
			halign : 'center',
			width : 100,
			sortable : false
		},{  
			field : "buildtime",
			title : "生成时间",
			halign : 'center',
			width : 100,
			sortable : false
		}
		] ],
       onDblClickRow:function (rowIndex, rowData) {       	 
    	   mdDialog.find("#jzyxTable").datagrid('appendRow', rowData);//追加一行
     	   mdDialog.find("#jzTable").datagrid('deleteRow',rowIndex);//删除一行
       }
	});
	return jzgrid;
}

// 加载已选卷宗信息
function jzyxDataGrid(mdDialog,groupid,caseid){
	var jzUrl=contextpath + "aros/jzgl/controller/CaseFileManageController/queryAllFile.do";
	jzyxgrid = mdDialog.find("#jzyxTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选卷宗列表',
		collapsible: false,
		url : jzUrl,
		queryParams : {groupid:groupid,caseid:'01'},
		singleSelect: false,
		rownumbers : true,
		idField: 'noticeid',
		columns : [ [ {
			field : "noticeid",
			checkbox : true
		},{  
			field : "doctype",
			title : "文档名称",
			halign : 'center',
			width : 100,
			sortable : false
		},{
			field : "protype",
			title : "流程类型",
			halign : 'center',
			width : 100,
			sortable : false
		},{
			field : "buildtime",
			title : "生成时间",
			halign : 'center',
			width : 100,
			sortable : false
		}
		] ],
		onDblClickRow:function (rowIndex, rowData) {
			mdDialog.find("#jzTable").datagrid('appendRow', rowData);//追加一行
			mdDialog.find("#jzyxTable").datagrid('deleteRow',rowIndex);//删除一行
		}
	});
	return jzyxgrid;
}

/**
 * 复议研讨
 */
function ajxzEdit() {
	var zjxzUrl=contextpath + "aros/zjgl/BGroupbaseinfoController/" + "zjxzInit.do";
	
	var selectRow = xzfyDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一个案件！", null);
		return;
	}
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var groupid = "";
	parent.$.modalDialog({
		title : "复议研讨",
		width : 1010,
		height : 550,
		href : zjxzUrl + "?caseid=" + row.caseid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//所有委员列表
			zjDataGrid(mdDialog, groupid);
			//小组委员列表
			zjxzDataGrid(mdDialog, groupid);
			// 加载所有卷宗
			jzDataGrid(mdDialog, groupid, row.caseid);
			// 加载已选卷宗
			jzyxDataGrid(mdDialog, groupid, row.caseid);
			
			var selectRows;
			mdDialog.find("#addBtn").on("click",function(){
				selectRows = kxgrid.datagrid('getChecked');
				if (selectRows == null || selectRows.length == 0) {
					easyui_warn("请选择一个可选委员！", null);
					return;
				}
				var selectNum=selectRows.length;
				
				for(var i = selectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = kxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjxzTable").datagrid('appendRow', rowInfo);//追加一行
			    	mdDialog.find("#zjTable").datagrid('deleteRow',index);//删除一行
				}
			});
			mdDialog.find("#delBtn").on("click",function(){
				selectRows = yxgrid.datagrid('getChecked');
				if (selectRows == null || selectRows.length == 0) {
					easyui_warn("请选择一个已选委员！", null);
					return;
				}
				var selectNum=selectRows.length;
				for(var i = selectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = kxgrid.datagrid('getRowIndex',rowInfo);
					var index = yxgrid.datagrid('getRowIndex',rowInfo);
					mdDialog.find("#zjTable").datagrid('appendRow', rowInfo);//追加一行
			    	mdDialog.find("#zjxzTable").datagrid('deleteRow', index);//删除一行
				}
			});
			
			// 卷宗选择
			mdDialog.find("#jz_addBtn").on("click",function(){
				selectRows = jzgrid.datagrid('getSelections');
				if (selectRows == null || selectRows.length == 0) {
					return;
				}
				var jzSelectNum=selectRows.length;
				for(var i = jzSelectNum-1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = jzgrid.datagrid('getRowIndex',rowInfo);
					jzyxgrid.datagrid('appendRow', rowInfo);//追加一行
					jzgrid.datagrid('deleteRow', index);//删除一行
				}
			});
			mdDialog.find("#jz_delBtn").on("click",function(){
				selectRows = jzyxgrid.datagrid('getSelections');
				if (selectRows == null || selectRows.length == 0) {
					return;
				}
				var jzSelectNum=selectRows.length;
				for(var i = jzSelectNum - 1; i>= 0; i--){
					var rowInfo = selectRows[i];
					var index = jzyxgrid.datagrid('getRowIndex',rowInfo);
					jzgrid.datagrid('appendRow', rowInfo);//追加一行
					jzyxgrid.datagrid('deleteRow', index);//删除一行
				}
			});
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				setspeIds();
				submitForm(urls.saveAjZtUrl, "zjxzForm", "");
			}
		}, {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function similarCaseManagement(){
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条记录 !", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var deftype = row.deftype;
	var admtype = row.admtype;
	var casetype = row.casetype;
	var menuid = $("#menuid").val();
	parent.$.modalDialog({
		title : "相似案件推荐",
		width : 1050,
		height : 600,
		href : urls.xsajUrl+"?deftype="+deftype+"&admtype="+admtype+"&casetype="+casetype+"&menuid="+menuid,
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

/**
 * 查看流程信息
 */
function workflowMessage(){
	
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	
	showFlowModalDialogForReq(selectRow[0].caseid, selectRow[0].protype);
}

function showHiddenDeleteBtn(nodeid){
	if(20== nodeid){
		$("#deletecase").show();
	}else{
		$("#deletecase").hide();
	}
}

function deleteCase(){

	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	parent.$.messager.confirm("删除确认", "确定要删除案件？", function(r) {
		if (r) {
			$.post(urls.deleteCaseUrl, {
				caseid:selectRow[0].caseid,
			}, function(result) {
				if (result.success) {
					parent.$.messager.alert("提示", result.title, "info");
					showXzfyDataGridReload();
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}, "json");
		}
	});
	
}
