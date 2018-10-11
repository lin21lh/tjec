
var baseUrl = contextpath + "ppp/projectXmcg/xqgs/controller/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"queryProject.do",
		projectXqgsAddUrl : baseUrl+"projectXqgsAdd.do",
		projectXqgsAddCommitUrl : baseUrl+"projectXqgsAddCommit.do",
		sendWFUrl : baseUrl + "sendWorkFlow.do",
		revokeWFUrl : baseUrl + "revokeWorkFlow.do",
		delXqgs : baseUrl + "deleteXqgs.do",
		projectXqgsViewUrl : baseUrl+"projectXqgsView.do",
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	$('#backBtn').linkbutton('disable');
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
				$('#addBtn').linkbutton('disable');
				$('#editBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
				$('#backBtn').linkbutton('enable');
				break;
			case '1':
				$('#addBtn').linkbutton('enable');
				$('#editBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
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
			firstNode : true,
			lastNode : false
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
 * 需求公示信息新增/修改
 */
function projectXqgsAdd(){
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
		href : urls.projectXqgsAddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			showFileDiv(mdDialog.find("#filetd"),true,"XQGS",row.xqgsid,"");
			var f = parent.$.modalDialog.handler.find('#projectXqgsAddForm');
			f.form("load", row);
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.projectXqgsAddCommitUrl,"projectXqgsAddForm","");
			}
		},{
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.projectXqgsAddCommitUrl,"projectXqgsAddForm","1");
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


//删除需求公示信息
function projectXqgsDelete(){
	var rows = projectDataGrid.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条项目数据！");
		return;
	}
	var row = rows[0];
	var xqgsid = row.xqgsid;
	if(xqgsid <=0 ) {
		//未维护采购信息
		easyui_warn("所选项目没有可以删除的采购信息！");
		return;
	}
	parent.$.messager.confirm("确认删除", "确定删除选择项目的需求公示信息？", function(r) {
		if (r) {
			$.post(urls.delXqgs, {
				menuid : menuid,
				xqgsid : row.xqgsid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}
//新增送审表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	parent.$.modalDialog.handler.find("#activityId").val(activityId);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	if(workflowflag=="1"){//送审
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
 * 需求公示送审
 */
function sendWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var xqgsid = row.xqgsid;
	if(xqgsid <=0 ) {
		//未维护采购信息
		easyui_warn("请先维护所选项目的需求公示信息！");
		return;
	}
	parent.$.messager.confirm("送审确认", "确认要送审选中项目？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				xqgsid : row.xqgsid,
				activityId :activityId,
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
 * 已送审撤回
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
				xqgsid : row.xqgsid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
