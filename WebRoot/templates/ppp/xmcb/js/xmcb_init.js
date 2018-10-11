
var baseUrl = contextpath + "ppp/projectXmcb/controller/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"queryProject.do",
		projectaddUrl : baseUrl+"projectAdd.do",
		addCommitUrl : baseUrl+"projectAddCommit.do",
		editCommitUrl : baseUrl+"projectEditCommit.do",
		sendWFUrl : baseUrl + "sendWorkFlow.do",
		projectDelete : baseUrl + "projectDelete.do",
		revokeWFUrl : baseUrl + "revokeWorkFlow.do",
		projectDetailUrl : baseUrl + "projectDetail.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
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
		elementcode : "DLJG",
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
		             ] ]
	});
}
var datagrid;
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
			firstNode : true,
			lastNode : false
		};
	projectDataGrid.datagrid("load", param);
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
 * 项目新增
 */
function projectAdd(){
	parent.$.modalDialog({
		title : "项目申报",
		width : 900,
		height : 600,
		href : urls.projectaddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid,"xmlx", "PROTYPE", "code", "name", mdDialog);//项目类型
			comboboxFuncByCondFilter(menuid,"yzfs", "PROOPERATE", "code", "name", mdDialog);//项目运作方式
			comboboxFuncByCondFilter(menuid,"hbjz", "PRORETURN", "code", "name", mdDialog);//项目回报机制
			comboboxFuncByCondFilter(menuid,"fqlx", "PROSENDTYPE", "code", "name", mdDialog);//项目发起类型
			comboboxFuncByCondFilter(menuid,"sfxm", "PROSFXM", "code", "name", mdDialog);//示范项目
			comboboxFuncByCondFilter(menuid,"sftjxm", "SYS_TRUE_FALSE", "code", "name", mdDialog);//推介项目
			comboboxFuncByCondFilter(menuid,"sfsqbt", "SYS_TRUE_FALSE", "code", "name", mdDialog);//申请补贴
			comboboxFuncByCondFilter(menuid,"ssxq", "SYS_AREA", "code", "name", mdDialog);//所属县区
			mdDialog.find("#sshyMc").treeDialog({
				title :'选择所属行业',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'sshy',
				prompt: "请选择所属行业",
				editable :false,
				multiSelect: false, //单选树
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
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.addCommitUrl,"projectAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.addCommitUrl,"projectAddForm","1");
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
 * 项目送审
 */
function sendWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("送审确认", "确认要送审选中项目？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				activityId :activityId,
				xmid : row.xmid,
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
 * 修改
 */
function projectEdit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目修改",
		width : 900,
		height : 630,
		href : urls.projectaddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid,"xmlx", "PROTYPE", "code", "name", mdDialog);//项目类型
			comboboxFuncByCondFilter(menuid,"yzfs", "PROOPERATE", "code", "name", mdDialog);//项目运作方式
			comboboxFuncByCondFilter(menuid,"hbjz", "PRORETURN", "code", "name", mdDialog);//项目回报机制
			comboboxFuncByCondFilter(menuid,"fqlx", "PROSENDTYPE", "code", "name", mdDialog);//项目发起类型
			comboboxFuncByCondFilter(menuid,"sfxm", "PROSFXM", "code", "name", mdDialog);//示范项目
			comboboxFuncByCondFilter(menuid,"sftjxm", "SYS_TRUE_FALSE", "code", "name", mdDialog);//推介项目
			comboboxFuncByCondFilter(menuid,"sfsqbt", "SYS_TRUE_FALSE", "code", "name", mdDialog);//申请补贴
			comboboxFuncByCondFilter(menuid,"ssxq", "SYS_AREA", "code", "name", mdDialog);//所属县区
			mdDialog.find("#sshyMc").treeDialog({
				title :'选择所属行业',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'sshy',
				prompt: "请选择所属行业",
				editable :false,
				multiSelect: false, //单选树
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
			var row = projectDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#filetd"),true,"XMCB",row.xmid,"");
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.editCommitUrl,"projectAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.editCommitUrl,"projectAddForm","1");
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
function projectDelete(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中项目删除？", function(r) {
		if (r) {
			$.post(urls.projectDelete, {
				xmid : row.xmid
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
		easyui_warn("请选择一个项目！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("撤回确认", "确认要将选中项目撤回？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				xmid : row.xmid,
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