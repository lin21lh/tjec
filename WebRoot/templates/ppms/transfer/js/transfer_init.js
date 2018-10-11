
var baseUrl = contextpath + "ppms/transfer/ProjectTransferController/";
var urls = {
	queryUrl : baseUrl + "queryProject.do",
	transferAddUrl : baseUrl +"transferAdd.do",
	transferSaveUrl : baseUrl + "transferSave.do",
	queryTransfer : baseUrl + "queryTransfer.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	transferDetailUrl : baseUrl +"transferDetail.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#changeAccountDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
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
var projectDataGrid;
//页面刷新
function showReload() {
	projectDataGrid.datagrid('reload'); 
}
//加载可项目grid列表
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
			firstNode : true,
			lastNode : false
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "projectid",checkbox : true}  
						,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
						,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
						,{field : "amount",title : "项目总投资（万元）",halign : 'right',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
						,{field : "statusName",title : "项目状态",halign : 'center',	width:80,sortable:true}
						,{field : "proYear",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
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
						,{field : "transferid",title : "transferid",halign : 'center',	width:150,sortable:true,hidden:true	}
		             ] ]
	});
}
/**
 * 新增按钮
 */
function add(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var transferid = row.transferid;
	var projectid = row.projectid;
	if(transferid != null){
		//已经录入了移交情况，则修改
		edit();
		return;
	}
	parent.$.modalDialog({
		title : "项目移交情况录入",
		iconCls : 'icon-add',
		width : 900,
		height : 550,
		href : urls.transferAddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(projectid);
				submitForm(urls.transferSaveUrl,"transferAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(projectid);
				submitForm(urls.transferSaveUrl,"transferAddForm","1");
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
//新增提交表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	parent.$.modalDialog.handler.find("#activityId").val(activityId);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	if(workflowflag=="1"){//提交
		parent.$.messager.confirm("送审确认", "确认要送审？", function(r) {
			if (r) {
				form.form("submit",
						{
							url : url,
							onSubmit : function() {
								parent.$.messager.progress({
									title : '提示',
									text : '数据处理中，请稍后....'
								});
								var isValid = form.form('validate');
								if (!isValid) {
									parent.$.messager.progress('close');
								}
								return isValid;
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);
								if (result.success) {
									projectDataGrid.datagrid('reload');
									parent.$.modalDialog.handler.dialog('close');
								} else {
									parent.$.messager.alert('错误', result.title, 'error');
								}
							}
						});
			}
		});
	}else{
		form.form("submit",
				{
			url : url,
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = form.form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					projectDataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
				}
			});
	}
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
			firstNode : true,
			lastNode : false
		};
	projectDataGrid.datagrid("load", param);
}
/**
 * 修改
 */
function edit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var transferid = row.transferid;
	var projectid = row.projectid;
	if(transferid==null){
		easyui_warn("该项目未录入移交情况，请先录入！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目修改",
		iconCls : 'icon-add',
		width : 900,
		height : 550,
		href : urls.transferAddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#transferAddForm');
			//采用同步ajax
			$.ajax({  
				  type : "post",  
				  url : urls.queryTransfer,  
				  data : "transferid=" + transferid,  
				  async : false,  
				  success : function(result){  
		          var r = result.body.transfer;
					if(r!=""){
						f.form("load", r);
						showFileDiv(mdDialog.find("#zcpgjg"),true, r.assetAssessPath, "30","assetAssessPath");
						showFileDiv(mdDialog.find("#xmxncejg"),true, r.transferPropertyPath, "30","transferPropertyPath");
						showFileDiv(mdDialog.find("#xmjxpjbg"),true, r.projectPerformancePath, "30","projectPerformancePath");
					}
			    }  
			 });  
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				mdDialog.find("#transferid").val(transferid);
				mdDialog.find("#projectid").val(projectid);
				submitForm(urls.transferSaveUrl,"transferAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
				mdDialog.find("#transferid").val(transferid);
				mdDialog.find("#projectid").val(projectid);
				submitForm(urls.transferSaveUrl,"transferAddForm","1");
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
/**
 *项目删除
 */
function pdelete(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中项目删除？", function(r) {
		if (r) {
			$.post(urls.projectDelete, {
				projectid : row.projectid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 项目送审
 */
function sendWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var  transferid = row.transferid;
	if(transferid==null){
		easyui_warn("该项目未录入移交情况，请先录入！",null);
		return;
	}
	parent.$.messager.confirm("送审确认", "确认要将选中项目送审？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				activityId :activityId,
				transferid : row.transferid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 项目已送审撤回
 */
function revokeWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("撤回确认", "确认要将选中项目撤回？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				transferid : row.transferid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid('reload');
				});
			}, "json");
		}
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
function detail(){
	var row = projectDataGrid.datagrid("getSelections")[0];
	var transferid = row.transferid;
	var projectid = row.projectid;
	if(transferid==null){
		easyui_warn("该项目未录入移交情况，请先录入！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目移交详情",
		width : 900,
		height : 550,
		href : urls.transferDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#transferAddForm');
			//采用同步ajax
			$.ajax({  
				  type : "post",  
				  url : urls.queryTransfer,  
				  data : "transferid=" + transferid,  
				  async : false,  
				  success : function(result){  
		          var r = result.body.transfer;
					if(r!=""){
						f.form("load", r);
						showFileDiv(mdDialog.find("#zcpgjg"),false, r.assetAssessPath, "30","assetAssessPath");
						showFileDiv(mdDialog.find("#xmxncejg"),false, r.transferPropertyPath, "30","transferPropertyPath");
						showFileDiv(mdDialog.find("#xmjxpjbg"),false, r.projectPerformancePath, "30","projectPerformancePath");
					}
			    }  
			 });  
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}
