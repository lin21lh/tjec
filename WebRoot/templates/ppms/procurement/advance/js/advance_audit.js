
var baseUrl = contextpath + "ppms/procurement/ProjectAdvanceController/";
var sfArray = [{ "value": "1", "text": "是" }, { "value": "0", "text": "否" }];
var urls = {
	queryUrl : baseUrl + "queryProject.do?audit=1",
	organQueryUrl : baseUrl + "queryOrgan.do",
	advanceAuditUrl : baseUrl + "advanceAudit.do",
	addCommitUrl : baseUrl + "advanceAddCommit.do",
	editCommitUrl : baseUrl + "advanceAddCommit.do",
	projectDelete : baseUrl + "projectDelete.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	auditWFUrl : baseUrl + "auditWorkFlow.do",
	advanceExpertGrid : baseUrl + "advanceExpertGrid.do",
	auditOpinion : contextpath + "ppms/discern/ProjectDiscernController/auditOpinion.do",
	advanceDetailUrl : baseUrl + "advanceDetail.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#advanceDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	//工作流状态初始化
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [{text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
			case '1':
				$('#addBtn').linkbutton('enable');
				$('#editBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('enable');
				break;
			case '2':
				$('#addBtn').linkbutton('disable');
				$('#editBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				break;
			default:
				break;
			}
		}
	});
	comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name");//项目回报机制
	comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name");//项目发起类型
	$("#proTrade").treeDialog({
		title :'选择所属行业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'proTrade',
		prompt: "请选择所属行业",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "PROTRADE",
		filters:{
			code: "行业代码",
			name: "行业名称"
		}
	});
	loadProjectGrid(urls.queryUrl);
});
var advanceDataGrid;
//页面刷新
function showReload() {
	advanceDataGrid.datagrid('reload'); 
}
//加载可项目grid列表
function loadProjectGrid(url) {
	advanceDataGrid = $("#projectDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			menuid : menuid,
			status : 1,
			activityId : activityId,
			firstNode : firstNode,
			lastNode : lastNode
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "projectid",checkbox : true}  
				        ,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
				        ,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
				        ,{field : "statusName",title : "项目状态",halign : 'center',	width:80,sortable:true}
				        ,{field : "amount",title : "项目总投资（万元）",halign : 'right',align:'right',width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
				        ,{field : "noticeTime",title : "公告发布时间",halign : 'center',	width:100,sortable:true	}
				        ,{field : "publishMedia",title : "公告发布媒体",halign : 'center',	width:120,sortable:true	}
				        ,{field : "inquiryResultName",title : "预审结果",halign : 'center',	width:80,sortable:true	}
				        ,{field : "inquiryTime",title : "资格预审时间",halign : 'center',	width:80,sortable:true	}
				        ,{field : "proYear",title : "合作年限",halign : 'center',align:'right',width:100,sortable:true	}
				        ,{field : "proTradeName",title : "所属行业",halign : 'center',	width:150	,sortable:true}
				        ,{field : "proPerateName",title : "运作方式",halign : 'center',	width:150,sortable:true	}
				        ,{field : "proReturnName",title : "回报机制",halign : 'center',	width:150,sortable:true}
				        ,{field : "proSendtime",title : "项目发起时间",halign : 'center',	width:150,sortable:true}
				        ,{field : "proSendtypeName",title : "项目发起类型",halign : 'center',	width:150,sortable:true	}
				        ,{field : "proSendperson",title : "项目发起人名称",halign : 'center',	width:150,sortable:true	}
				        ,{field : "proSituation",title : "项目概况",halign : 'center',	width:100,sortable:true }
				        ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
				        ,{field : "proPhone",title : "联系人电话",halign : 'center',	width:150,sortable:true}
				        ,{field : "proScheme",title : "初步实施方案内容",halign : 'center',	width:150,sortable:true	}
				        ,{field : "proArticle",title : "项目产出物说明",halign : 'center',	width:100,sortable:true	}
				        ,{field : "createusername",title : "创建人",halign : 'center',	width:150,sortable:true	}
				        ,{field : "createtime",title : "创建时间",halign : 'center',	width:150,sortable:true	}
				        ,{field : "updateusername",title : "修改人",halign : 'center',	width:150,sortable:true	}
				        ,{field : "updatetime",title : "修改时间",halign : 'center',	width:150,sortable:true	}
				        ,{field : "status",title : "当前状态",halign : 'center',	width:80,sortable:true,hidden:true}
				        ,{field : "proType",title : "项目类型",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proTrade",title : "所属行业",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proPerate",title : "运作方式",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proReturn",title : "回报机制",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proSendtype",title : "项目发起类型",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proSchemepath",title : "实施方案附件路径",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proReportpath",title : "可行性研究报告路径",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proConditionpath",title : "环评报告路径",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "proArticlepath",title : "产出物附件路径",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "wfid",title : "WFID",halign : 'center',	width:150,sortable:true,hidden:true	}
				        ,{field : "advanceid",title : "advanceid",halign : 'center',	width:150,sortable:true,hidden:true	}
				       ] ]
	});
}
/**
 * 审批按钮
 */
function advanceAudit(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	var advanceid = row.advanceid;
	parent.$.modalDialog({
		title : "预审结果审批",
		iconCls : 'icon-edit',
		width : 900,
		height : 650,
		href : urls.advanceAuditUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			advanceGrid(mdDialog, row.projectid,row.advanceid);
			qualExpertGridDetail(mdDialog, row.advanceid);
			var f = parent.$.modalDialog.handler.find('#advanceAddForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#ysjg"),false, row.inquiryDeclarePath, "24","inquiryDeclarePath");
		},
		buttons : [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var isValid =mdDialog.find("#advanceAddForm").form('validate');
				if (!isValid) {
					return;
				}
				var wfid = mdDialog.find("#wfid").val();
				var advanceid = mdDialog.find("#advanceid").val();
				var opinion =  mdDialog.find("#opinion").val();
				parent.$.messager.confirm("审核确认", "确认审核同意？", function(r) {
					if (r) {
						$.post(urls.auditWFUrl, {
							wfid :wfid,
							activityId :activityId,
							opinion : opinion,
							isback : "",
							advanceid : advanceid
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								advanceDataGrid.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		}, {
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var isValid =mdDialog.find("#advanceAddForm").form('validate');
				if (!isValid) {
					return;
				}
				var form = parent.$.modalDialog.handler.find('#revoke_audit');
				var isValid = form.form('validate');
				if (!isValid) {
					return;
				}
				var wfid = mdDialog.find("#wfid").val();
				var advanceid = mdDialog.find("#advanceid").val();
				var opinion =  mdDialog.find("#opinion").val();
				parent.$.messager.confirm("退回确认", "确认退回？", function(r) {
					if (r) {
						$.post(urls.auditWFUrl, {
							wfid :wfid,
							activityId :activityId,
							opinion : opinion,
							isback : "1",
							advanceid : advanceid
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								advanceDataGrid.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
//预审单位
function advanceGrid(mdDialog,projectid,advanceid){
	var grid = mdDialog.find("#advanceAddGrid").datagrid({
		title : "资格预审机构",
        height: 160,
        width: 885,
        collapsible: false,
        url : urls.organQueryUrl,
        queryParams : {projectid:projectid,advanceid:advanceid},
        singleSelect: true,
        rownumbers : true,
        idField: 'organid',
        columns: [[//显示的列
                   { field: 'organ_code', title: '机构代码', width: 150, sortable: true,
                       editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'organ_name', title: '机构名称', width: 200,
                        editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'iscombo', title: '是否联合体', width: 100,formatter: sfformatter,
                         editor: { 
                        	 type : 'combobox', 
                        	 options : {  
                        		 required: true,
                        		 data: sfArray,
                        		 editable :false,
                        		 valueField: "value", 
                        		 textField: "text"}
                        	 }
                   },{ field: 'ispass', title: '是否通过预审', width: 100,formatter: sfformatter,
                	   editor: { 
	                  	 type : 'combobox', 
	                	 options : {  
	                		 required: true,
	                		 data: sfArray,
	                		 editable :false,
	                		 valueField: "value", 
	                		 textField: "text"}
	                	 }
                   },{ field: 'score', title: '评分', width: 90,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,validType:{length:[0,20]}} }
                   },{ field: 'remark', title: '备注', width: 200,
                         editor: { type: 'text', options: { required: true} }
                   }]]
    });
	return grid;
}
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			status : $("#status").combobox('getValue'),
			proName : $("#proName").val(),
			proPerson : $("#proPerson").val(),
			proTrade :  $("#proTrade").val(),
			proPerate : $("#proPerate").combobox('getValues').join(","),
			proReturn : $("#proReturn").combobox('getValues').join(","),
			proSendtype : $("#proSendtype").combobox('getValues').join(","),
			proType : $("#proType").combobox('getValues').join(","),
			menuid : menuid,
			activityId : activityId,
			firstNode : firstNode,
			lastNode : lastNode
		};
	advanceDataGrid.datagrid("load", param);
}
/**
 * 工作流信息
 */
function workflowMessage(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
/**
 * 预审单位中下拉列表字典转换
 * @param value
 * @param rowData
 * @param rowIndex
 * @returns
 */
function sfformatter(value, rowData, rowIndex) {
    for (var i = 0; i < sfArray.length; i++) {
        if (sfArray[i].value == value) {
            return sfArray[i].text;
        }
    }
}
function revokeWF(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	var wfid = row.wfid;
	var advanceid = row.advanceid;
	parent.$.modalDialog({
		title : "退回处理",
		width : 450,
		height : 210,
		iconCls : 'icon-edit',
		href : urls.auditOpinion,
		buttons : [{text : "退回",
						iconCls : "icon-save",
						handler : function() {
							var opinion = parent.$.modalDialog.handler.find('#opinion').val();
							if (opinion == null || opinion == "") {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认将该项目退回？", function(r) {
								if(r){
									$.post(urls.auditWFUrl, {
										wfid :wfid,
										activityId :activityId,
										opinion : opinion,
										isback : "1",
										advanceid : advanceid
									}, function(result) {
										easyui_auto_notice(result, function() {
											advanceDataGrid.datagrid('reload');
										});
									}, "json");
									parent.$.modalDialog.handler.dialog('close');
								}
							});
						}
					}, {
						text : "关闭",
						iconCls : "icon-cancel",
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					}]
	});
}
/**
 * 详情
 */
function advanceView(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "预审结果详情",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.advanceDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			advanceGridDetail(mdDialog, row.projectid,row.advanceid);
			qualExpertGridDetail(mdDialog, row.advanceid);
			var f = parent.$.modalDialog.handler.find('#advanceAddForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#ysjg"),false, row.inquiryDeclarePath, "24","inquiryDeclarePath");
		},
		buttons : [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
function advanceGridDetail(mdDialog,projectid,advanceid){
	var grid = mdDialog.find("#advanceAddGrid").datagrid({
		title : "资格预审机构",
        height: 200,
        width: 885,
        collapsible: false,
        url : urls.organQueryUrl,
        queryParams : {projectid:projectid,advanceid:advanceid},
        rownumbers : true,
        singleSelect: true,
        idField: 'organid',
        columns: [[//显示的列
                   { field: 'organ_code', title: '机构代码', width: 150, sortable: true,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'organ_name', title: '机构名称', width: 200,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'iscombo', title: '是否联合体', width: 100,halign : 'center',formatter: sfformatter,
                         editor: { 
                        	 type : 'combobox', 
                        	 options : {  
                        		 required: true,
                        		 data: sfArray,
                        		 editable :false,
                        		 valueField: "value", 
                        		 textField: "text"}
                        	 }
                   },{ field: 'ispass', title: '是否通过预审', width: 100,halign : 'center',formatter: sfformatter,
                	   editor: { 
	                  	 type : 'combobox', 
	                	 options : {  
	                		 required: true,
	                		 data: sfArray,
	                		 editable :false,
	                		 valueField: "value", 
	                		 textField: "text"}
	                	 }
                   },{ field: 'score', title: '评分', width: 90,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,validType:{length:[0,20]}} }
                   },{ field: 'remark', title: '备注', width: 200,halign : 'center',
                         editor: { type: 'text', options: { required: true} }
                   }]]
    });
	return grid;
}
function qualExpertGridDetail(mdDialog,advanceid){
	var grid = mdDialog.find("#advanceExpert").datagrid({
		height: 160,
		title: '预审专家列表',
		collapsible: false,
		url : urls.advanceExpertGrid,
		queryParams : {advanceid:advanceid},
		singleSelect: true,
		rownumbers : true,
		idField: 'purexpertid',
		columns: [[//显示的列
		           {field: 'purexpertid', title: '序号', width: 100,  checkbox: true },
		           { field: 'expertName', title: '专家名称', width: 130,halign : 'center'
		           },{ field: 'expertType', title: '专家类型', width: 100,halign : 'center'
		           },{ field: 'expertPhone', title: '联系方式', width: 140,halign : 'center'
		           },{ field: 'bidmajor', title: '评标专业', width: 300,halign : 'center'
		           },{ field: 'responsibleArea', title: '负责领域', width: 150,halign : 'center'
		           },{ field: 'expertid', title: 'expertId', width: 100,halign : 'center',hidden:true
		           },{ field: 'advanceid', title: 'advanceid', width: 100,halign : 'center',hidden:true
		           },{ field: 'remark', title: '说明', width: 300,halign : 'center'
		           }]]
	});
	return grid;
}