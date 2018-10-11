var baseUrl = contextpath + "ppms/discern/ProjectDiscernController/";
var urls = {
	queryUrl : baseUrl + "queryProject.do?audit=1",
	projectAuditUrl : contextpath + "ppms/discern/ProjectVerifyController/auditEntry.do",
	productQueryUrl : baseUrl + "queryProduct.do",
	auditCommitUrl : baseUrl + "auditCommit.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	thirdOrgQueryUrl : baseUrl + "queryThirdOrg.do",
	financeQueryUrl : baseUrl + "queryFinance.do",
	queryApprove : baseUrl + "queryApprove.do",
	auditOpinion : baseUrl + "auditOpinion.do",
	auditOperate :  contextpath + "ppms/discern/ProjectVerifyController/auditOperate.do",
	projectDetailUrl : baseUrl + "projectSpDetail.do",
	sendWorkFlow : contextpath + "ppms/discern/ProjectVerifyController/sendWorkFlow.do",
	backWorkFlow : contextpath + "ppms/discern/ProjectVerifyController/backWorkFlow.do",
	projectView : contextpath + "ppms/discern/ProjectVerifyController/projectView.do"
};
//默认加载
$(function() {
	$('#backBtn').linkbutton('disable');
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
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
				$('#addBtn').linkbutton('disable');
				$('#editBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
				$('#backBtn').linkbutton('enable');
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
function loadProjectGrid(url) {
	projectDataGrid = $("#projectDataGrid").datagrid({
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
		columns : [ [  {field : "projectid",checkbox : true}  
		              ,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
		              ,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
		              ,{field : "amount",title : "项目总投资（万元）",halign : 'center',align:'right',width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
		              ,{field : "statusName",title : "项目状态",halign : 'center',	width:80,sortable:true}
		              ,{field : "proYear",title : "合作年限",halign : 'center',align:'left',	width:70,sortable:true	}
		              ,{field : "proTradeName",title : "所属行业",halign : 'center',	width:100	,sortable:true}
		              ,{field : "proPerateName",title : "运作方式",halign : 'center',	width:100,sortable:true	}
		              ,{field : "proReturnName",title : "回报机制",halign : 'center',	width:120,sortable:true}
		              ,{field : "proSendtime",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
		              ,{field : "proSendtypeName",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
		              ,{field : "proSendperson",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
		              ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
		              ,{field : "proSituation",title : "项目概况",halign : 'center',	width:150,sortable:true }
		              ,{field : "proPhone",title : "联系人电话",halign : 'center',	width:150,sortable:true}
		              ,{field : "proScheme",title : "初步实施方案内容",halign : 'center',	width:150,sortable:true	}
		              ,{field : "proArticle",title : "项目产出物说明",halign : 'center',	width:150,sortable:true	}
		              ,{field : "implementOrgan",title : "implementOrgan",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "czResultName",title : "审核结果",halign : 'center',	width:150,sortable:true	}
		              ,{field : "opinion",title : "审核意见",halign : 'center',	width:150,sortable:true	}
		              ,{field : "czResult",title : "审核结果",halign : 'center',	width:150,sortable:true	,hidden:true,hidden:true}
		              ,{field : "vfmPjhj",title : "VFM评价环节",halign : 'center',	width:150,sortable:true	}
		              ,{field : "vfmPjhjName",title : "审核结果",halign : 'center',	width:150,sortable:true	,hidden:true}
		              ,{field : "implementOrganName",title : "项目实施机构",halign : 'center',	width:150,sortable:true	}
		              ,{field : "implementPerson",title : "实施机构联系人",halign : 'center',	width:150,sortable:true}
		              ,{field : "implementPhone",title : "联系人电话",halign : 'center',	width:150,sortable:true	}
		              ,{field : "createusername",title : "创建人",halign : 'center',	width:130,sortable:true	}
		              ,{field : "createtime",title : "创建时间",halign : 'center',	width:130,sortable:true	}
		              ,{field : "updateusername",title : "修改人",halign : 'center',	width:130,sortable:true	}
		              ,{field : "updatetime",title : "修改时间",halign : 'center',	width:130,sortable:true	}
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
		              ,{field : "governmentPath",title : "governmentPath",halign : 'center',	width:150,sortable:true,hidden:true	}
		              
		             ] ]
	});
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
	projectDataGrid.datagrid("load", param);
}
/**
 * 审核
 */
function pushWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目审核",
		iconCls : 'icon-edit',
		width : 900,
		height : 630,
		href : urls.projectAuditUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#projectAuditForm');
			f.form("load", row);
			
			comboboxFuncByCondFilter(menuid,"czResult", "APPROVERESULT", "code", "name", mdDialog);
			comboboxFuncByCondFilter(menuid,"vfmPjhj", "PROVFMPJHJ", "code", "name", mdDialog);
			//加载附件
			showFileDiv(mdDialog.find("#ssfa"),false, row.proSchemepath, "30","proSchemepath");
			showFileDiv(mdDialog.find("#kxxyj"),false, row.proReportpath, "30","proReportpath");
			showFileDiv(mdDialog.find("#hpbg"),false, row.proConditionpath, "30","proConditionpath");
			showFileDiv(mdDialog.find("#xmccw"),false, row.proArticlepath, "30","proArticlepath");
			//项目产出物列表
			datagrid = productGrid(mdDialog, row.projectid);
			
			//计算
		},
		buttons : [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#firstNode").val(firstNode);
				parent.$.modalDialog.handler.find("#lastNode").val(lastNode);
				parent.$.modalDialog.handler.find("#activityId").val(activityId);
				submitForm(urls.sendWorkFlow,"projectAuditForm","");
			}
		}, {
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#firstNode").val(firstNode);
				parent.$.modalDialog.handler.find("#lastNode").val(lastNode);
				parent.$.modalDialog.handler.find("#activityId").val(activityId);
				submitForm(urls.backWorkFlow,"projectAuditForm","1");
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
//加载产出物列表
function productGrid(mdDialog,projectid){
	var grid = mdDialog.find("#projectAddGrid").datagrid({
		title: '年度产出计划',
        height: 320,
        width: 883,
        collapsible: false,
        url : urls.productQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'productid',
        columns: [[//显示的列
                   { field: 'year', title: '年度', width: 100,
                       editor: { type: 'text', options: {} }
                   },{ field: 'output', title: '产出物', width: 200,
                        editor: { type: 'text', options: {} }
                   },{ field: 'unit', title: '计量单位', width: 100,
                         editor: { type: 'text', options: {} }
                   },{ field: 'amount', title: '数量', width: 100,
                         editor: { type: 'text', options: {} }
                   },{ field: 'remark', title: '备注', width: 200,
                         editor: { type: 'text', options: {} }
                   }]]
    });
	return grid;
}

//新增提交表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
		var isValid = form.form('validate');
		if(workflowflag=="1"){
			if(parent.$.modalDialog.handler.find("#opinion").val()==""){
				parent.$.messager.alert('提示', "请填写审核意见！", 'info');
				//默认选中第二个页签
				parent.$.modalDialog.handler.find('#tabList').tabs('select', '项目审核信息');
				return;
			}
		}else{
			if (!isValid) {
				parent.$.modalDialog.handler.find('#tabList').tabs('select', '项目审核信息');
				return;
			}
		}
		
		var msg = workflowflag ==""?"确认审核同意？":"确认审核退回？";
		parent.$.messager.confirm("审核确认", msg, function(r) {
			if (r) {
				form.form("submit",
						{
					url : url,
					onSubmit : function() {
						parent.$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
					},
					success : function(result) {
						parent.$.messager.progress('close');
						result = $.parseJSON(result);
						if (result.success) {
							parent.$.messager.progress('close');
							projectDataGrid.datagrid('reload');
							parent.$.modalDialog.handler.dialog('close');
						} else {
							parent.$.messager.progress('close');
							parent.$.messager.alert('错误', result.title, 'error');
						}
					}
						});
			}
		});
}

function pushBackWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var wfid = row.wfid;
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
							var form = parent.$.modalDialog.handler.find('#revoke_audit');
							var isValid = form.form('validate');
							if (!isValid) {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认将该项目退回？", function(r) {
								if(r){
									$.post(urls.backWorkFlow, {
										menuid : menuid,
										activityId : activityId,
										wfid : wfid,
										opinion : opinion
									}, function(result) {
										easyui_auto_notice(result, function() {
											projectDataGrid.datagrid('reload');
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
function workflowMessage(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
/**
 * 查看详情
 */
function projectView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目详情",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.projectView,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#projectAuditForm');
			f.form("load", row);
			//加载附件
			showFileDiv(mdDialog.find("#ssfa"),false, row.proSchemepath, "30","proSchemepath");
			showFileDiv(mdDialog.find("#kxxyj"),false, row.proReportpath, "30","proReportpath");
			showFileDiv(mdDialog.find("#hpbg"),false, row.proConditionpath, "30","proConditionpath");
			showFileDiv(mdDialog.find("#xmccw"),false, row.proArticlepath, "30","proArticlepath");
			//showFileDiv(mdDialog.find("#szfqpwj"),false, row.governmentPath, "30","governmentPath");//政府签批文件
			//项目产出物列表
			datagrid = productGrid(mdDialog, row.projectid);
			
			//计算
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
//加载产出物列表
function productGridDetail(mdDialog,projectid){
	var grid = mdDialog.find("#projectAddGrid").datagrid({
        height: 320,
        width: 880,
        collapsible: true,
        url : urls.productQueryUrl,
        queryParams : {projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'productid',
        columns: [[//显示的列
                   { field: 'year', title: '年度', width: 100, sortable: true,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'output', title: '产出物', width: 200,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'unit', title: '计量单位', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'amount', title: '数量', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'remark', title: '备注', width: 200,halign : 'center',
                         editor: { type: 'text', options: { required: true} }
                   }]]
    });
	return grid;
}