var baseUrl = contextpath + "/ppp/projectXmcb/controller/";
var urls = {
	queryUrl : baseUrl + "queryProject.do?audit=1",
	projectAuditUrl : baseUrl + "auditEntry.do",
	auditWorkFlow : baseUrl + "auditWorkFlow.do",
	backWorkFlow : baseUrl + "backWorkFlow.do",
	auditOpinion : baseUrl + "auditOpinion.do",
	projectDetailUrl : baseUrl + "projectDetail.do"
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
	comboboxFuncByCondFilter(menuid,"xmlx", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"yzfs", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"hbjz", "PRORETURN", "code", "name");//项目回报机制
	comboboxFuncByCondFilter(menuid,"fqlx", "PROSENDTYPE", "code", "name");//项目发起类型
	$("#sshy").treeDialog({
		title :'选择所属行业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'sshy',
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
		columns : [ [ {field : "xmid",checkbox : true}  
        ,{field : "xmmc",title : "项目名称",halign : 'center',width:250,sortable:true}
        ,{field : "orgname",title : "申报单位",halign : 'center',width:100,sortable:true}
        ,{field : "xmlxMc",title : "项目类型",halign : 'center',	width:80,sortable:true}
        ,{field : "xmzje",title : "项目总投资（万元）",halign : 'right',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
        ,{field : "xmztMc",title : "项目状态",halign : 'center',	width:80,sortable:true}
        ,{field : "hznx",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
        ,{field : "sshyMc",title : "所属行业",halign : 'center',	width:100	,sortable:true}
        ,{field : "yzfsMc",title : "运作方式",halign : 'center',	width:100,sortable:true	}
        ,{field : "hbjzMc",title : "回报机制",halign : 'center',	width:120,sortable:true}
        ,{field : "fqsj",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
        ,{field : "fqlxMc",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
        ,{field : "fqrmc",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
        ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
        ,{field : "sfxmName",title : "示范项目",halign : 'center',	width:100,sortable:true }
        ,{field : "tjxmName",title : "推介项目",halign : 'center',	width:80,sortable:true }
        ,{field : "sqbtName",title : "申请补贴",halign : 'center',	width:80,sortable:true }
        ,{field : "btje",title : "补贴金额（万元）",halign : 'right',align:'right'}
        ,{field : "xmgk",title : "项目概况",halign : 'center',	width:150,sortable:true }
        ,{field : "lxrdh",title : "联系人电话",halign : 'center',	width:150,sortable:true}
        ,{field : "cjrMc",title : "创建人",halign : 'center',	width:130,sortable:true	}
        ,{field : "cjsj",title : "创建时间",halign : 'center',	width:130,sortable:true	}
        ,{field : "xgrMc",title : "修改人",halign : 'center',	width:130,sortable:true	}
        ,{field : "xgsj",title : "修改时间",halign : 'center',	width:130,sortable:true	}
        ,{field : "dqztMc",title : "当前状态",halign : 'center',	width:80,sortable:true}
        ,{field : "xmlx",title : "项目类型",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "sshy",title : "所属行业",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "proPerate",title : "运作方式",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "hbjz",title : "回报机制",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "wfid",title : "WFID",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "sfxm",title : "示范项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
        ,{field : "tjxm",title : "推介项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
        ,{field : "sqbt",title : "申请补贴",halign : 'center',	width:100,sortable:true ,hidden:true	}
        ,{field : "zfzyzc",title : "政府资源支出",halign : 'center',	width:100,sortable:true ,hidden:true	}
       ] ]
	});
}
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			status : $("#status").combobox('getValue'),
			xmmc : $("#xmmc").val(),
			xmlxr : $("#xmlxr").val(),
			sshy :  $("#sshy").val(),
			yzfs : $("#yzfs").combobox('getValues').join(","),
			hbjz : $("#hbjz").combobox('getValues').join(","),
			fqlx : $("#fqlx").combobox('getValues').join(","),
			xmlx : $("#xmlx").combobox('getValues').join(","),
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
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目审核",
		width : 900,
		height : 630,
		href : urls.projectAuditUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#projectAuditForm');
			f.form("load", row);
			//显示附件
			showFileDiv(mdDialog.find("#filetd"),false,"XMCB",row.xmid,"");
			comboboxFuncByCondFilter(menuid,"zfzyzc", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否
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
				submitForm(urls.auditWorkFlow,"projectAuditForm","");
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
//新增提交表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
		var isValid = form.form('validate');
		if(workflowflag=="1"){//退回
			if(parent.$.modalDialog.handler.find("#spyj").val()==""){
				parent.$.modalDialog.handler.find('#tabList').tabs('select', '项目审核信息');
				parent.$.messager.alert('提示', "请填写审核意见！", 'info');
				//默认选中第二个页签
				return;
			}
		}else{//同意
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
/**
 * 工作流信息
 */
function workflowMessage(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
/**
 * 退回
 */
function pushBackWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
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
							var opinion = parent.$.modalDialog.handler.find('#spyj').val();
							if (opinion == null || opinion == "") {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认将该项目退回？", function(r) {
								if(r){
									$.post(urls.backWorkFlow, {
										menuid : menuid,
										activityId : activityId,
										wfid : wfid,
										spyj : opinion
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
/**
 * 查看详情
 */
function projectView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目详情",
		width : 900,
		height : 630,
		href : urls.projectDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var row = projectDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#projectAuditForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#filetd"),false,"XMCB",row.xmid,"");
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