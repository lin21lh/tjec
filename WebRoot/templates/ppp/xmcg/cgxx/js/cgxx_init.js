
var baseUrl = contextpath + "ppp/projectXmcg/cgxx/controller/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"queryProject.do",
		projectCgxxAddUrl : baseUrl+"projectCgxxAdd.do",
		projectCgxxAddCommitUrl : baseUrl+"projectCgxxAddCommit.do",
		sendWFUrl : baseUrl + "sendWorkFlow.do",
		delCgxx : baseUrl + "deleteCgxx.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	//加载查询条件
	loadqryconditon();
	loadProjectGrid(urls.queryUrl);
});

function loadqryconditon(){
	//采购信息状态
	$("#xxstatus").combobox({
		valueField : "value",
		textField : "text",
		value : "0",
		editable : false,
		data : [ {text : "未提交", value : "0"},{text : "已提交", value : "1"}, ],
		onSelect : function(record) {
			loadProjectGrid(urls.queryUrl);
			switch (record.value) {
			case '1':
				$('#addBtn').linkbutton('disable');
				$('#editBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
				break;
			case '0':
				$('#addBtn').linkbutton('enable');
				$('#editBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
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
			status : 0
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
			          //隐藏字段--采购信息表
		              ,{field : "cgxxid",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
		              ,{field : "cglxr",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "lxrdh",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "ysje",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "cgfs",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "dljgid",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "zgbmyj",title : "隐藏",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "pppyj",title : "隐藏",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "zfcgglkyj",title : "隐藏",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "dljg",title : "隐藏",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ] ]
	});
}
var datagrid;
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			status : $("#xxstatus").combobox('getValue'),
			xmmc : $("#xmmc").val(),
			xmlxr : $("#xmlxr").val(),
			sshy :  $("#sshy").val(),
			yzfs : $("#yzfs").combobox('getValues').join(","),
			hbjz : $("#hbjz").combobox('getValues').join(","),
			fqlx : $("#fqlx").combobox('getValues').join(","),
			xmlx : $("#xmlx").combobox('getValues').join(","),
			menuid : menuid
		};
	projectDataGrid.datagrid("load", param);
}

/**
 * 采购信息新增/修改
 */
function projectCgxxAdd(operflag){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "采购信息",
		width : 900,
		height : 600,
		href : urls.projectCgxxAddUrl+"?operflag="+operflag,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid,"cgfs", "PURCHASE", "code", "name", mdDialog);//采购方式
			mdDialog.find("#dljg").treeDialog({
				title :'选择代理机构',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'dljgid',
				prompt: "请选择代理机构",
				editable :false,
				multiSelect: false, //单选树
				dblClickRow: true,
				queryParams : {
					menuid : menuid
				},
				url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
				checkLevs: [1,2,3], //只选择3级节点
				elementcode : "DLJG",
				filters:{
					code: "机构代码",
					name: "机构名称"
				}
			});
			showFileDiv(mdDialog.find("#filetd"),true,"CGXX",row.cgxxid,"");
			var f = parent.$.modalDialog.handler.find('#projectCgxxAddForm');
			f.form("load", row);
		},
		buttons : funcOperButtons(operflag, urls.projectCgxxAddCommitUrl, projectDataGrid, "projectCgxxAddForm")
	});
}
function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if(operType=="view"){
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else{
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(url,form,"");
			}
		},{
			text : "提交",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(url,form,"1");
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	
	return buttons;
};
//删除项目采购信息
function projectCgxxDelete(){
	var rows = projectDataGrid.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条项目数据！");
		return;
	}
	var row = rows[0];
	var cgxxid = row.cgxxid;
	if(cgxxid <=0 ) {
		//未维护采购信息
		easyui_warn("所选项目没有可以删除的采购信息！");
		return;
	}
	parent.$.messager.confirm("确认删除", "确定删除选择的项目采购信息？", function(r) {
		if (r) {
			$.post(urls.delCgxx, {
				menuid : menuid,
				cgxxid : row.cgxxid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}
//新增提交表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	if(workflowflag=="1"){//提交
		parent.$.messager.confirm("提交确认", "确认要提交？", function(r) {
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
 * 项目提交
 */
function sendWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var cgxxid = row.cgxxid;
	if(cgxxid <=0 ) {
		//未维护采购信息
		easyui_warn("请先维护所选项目的采购信息！");
		return;
	}
	parent.$.messager.confirm("提交确认", "确认要提交选中项目？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				cgxxid : row.cgxxid,
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
