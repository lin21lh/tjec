
var baseUrl = contextpath + "ppp/projectXmcg/xqgs/controller/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"queryProject.do",
		projectXqgsAuditUrl : baseUrl+"projectXqgsAudit.do",
		sendWFUrl : baseUrl + "sendWorkFlow.do",
		backWFUrl : baseUrl + "backWorkFlow.do",
		projectXqgsViewUrl : baseUrl+"projectXqgsView.do",
		auditOpinion : baseUrl + "auditOpinion.do",
		
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	//$('#backBtn').linkbutton('disable');
	//加载查询条件
	loadqryconditon();
	loadProjectGrid(urls.queryUrl);
});

function loadqryconditon(){
	//采购信息状态
	$("#xqstatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "未处理", value : "1"},{text : "已处理", value : "2"}, ],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
			case '2':
				$('#auditBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				break;
			case '1':
				$('#auditBtn').linkbutton('enable');
				$('#backBtn').linkbutton('enable');
				break; 
			default:
				break;
			}
		}
	});
	comboboxFuncByCondFilter(menuid,"fqlx", "PROSENDTYPE", "code", "name");//项目发起类型
	comboboxFuncByCondFilter(menuid,"yzfs", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"xmlx", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"hbjz", "PRORETURN", "code", "name");//回报机制
	
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

}

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
			firstNode : firstNode,
			lastNode : lastNode
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "xmid",checkbox : true}  
			          ,{field : "xmmc",title : "项目名称",halign : 'center',width:250,sortable:true}
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
			          ,{field : "xmlxrdh",title : "联系人电话",halign : 'center',	width:150,sortable:true}
			          ,{field : "cjrmc",title : "创建人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "cjsj",title : "创建时间",halign : 'center',	width:130,sortable:true	}
			          ,{field : "xgrmc",title : "修改人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "xgsj",title : "修改时间",halign : 'center',	width:130,sortable:true	}
			          //隐藏字段--需求公示信息表
		              ,{field : "xqgsid",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
		              ,{field : "cgr",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "cgrdz",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "cglxr",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "cgrlxfs",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "cgxmmc",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "cgpmdm",title : "隐藏",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "cgpmmc",title : "隐藏",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "cgksrq",title : "隐藏",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "cgjsrq",title : "隐藏",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "gsts",title : "隐藏",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "bz",title : "隐藏",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ] ]
	});
}
var datagrid;
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			status : $("#xqstatus").combobox('getValue'),
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
 * 需求公示信息审核/退回
 */
function projectXqgsAudit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "需求公示审核",
		width : 900,
		height : 600,
		href : urls.projectXqgsAuditUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			showFileDiv(mdDialog.find("#filetd"),false,"XQGS",row.xqgsid,"");
			var f = parent.$.modalDialog.handler.find('#projectXqgsAuditForm');
			f.form("load", row);
		},
		buttons : [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#firstNode").val(firstNode);
				parent.$.modalDialog.handler.find("#lastNode").val(lastNode);
				parent.$.modalDialog.handler.find("#activityId").val(activityId);
				submitForm(urls.sendWFUrl,"projectXqgsAuditForm","1");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		},{
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#firstNode").val(firstNode);
				parent.$.modalDialog.handler.find("#lastNode").val(lastNode);
				parent.$.modalDialog.handler.find("#activityId").val(activityId);
				submitForm(urls.backWFUrl,"projectXqgsAuditForm","2");
			}
		} ] 
	});
}

/**
 * 需求公示信息详情
 */
function projectXqgsView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "采购需求公示",
		width : 900,
		height : 600,
		href : urls.projectXqgsViewUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			showFileDiv(mdDialog.find("#filetd"),false,"XQGS",row.xqgsid,"");
			var f = parent.$.modalDialog.handler.find('#projectXqgsViewForm');
			f.form("load", row);
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

//新增审核表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);

	if(parent.$.modalDialog.handler.find("#spyj").val()==""){
		parent.$.messager.alert('提示', "请填写审核意见！", 'info');
		return;
	}
	var isValid = form.form('validate');
	if (!isValid) {
		parent.$.modalDialog.handler.find('#tabList').tabs('select', '项目审核信息');
		return;
	}
	var tips = workflowflag=="1"?"确认要审核？":"确认要退回？";
	parent.$.messager.confirm("审核确认", tips, function(r) {
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
}


/**
 * 退回
 */
function projectXqgsBack(){
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
									$.post(urls.backWFUrl, {
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
