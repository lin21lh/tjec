var baseUrl = contextpath + "aros/zjgl/controller/";

var projectDataGrid;
var datagrid;
var urls = {
	queryUrl : baseUrl + "queryList.do",
	addUrl : baseUrl + "add.do",
	updateUrl : baseUrl + "add.do",
	saveUrl : baseUrl + "save.do",
	deleteUrl : baseUrl + "delete.do",
	viewUrl : baseUrl + "view.do",
	traUrl: contextpath + "aros/ajpj/BCaseestbaseinfoController/init.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#projectDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];

/**
 * 默认加载
 */
$(function() {
	
	comboboxFuncByCondFilter(menuid, "title", "SPETITLE", "code", "name");// 职称
	comboboxFuncByCondFilter(menuid, "post", "SPEPOST", "code", "name");// 职务
	comboboxFuncByCondFilter(menuid, "degree", "SPEDEGREE", "code", "name");// 学历
	
	var icons = {iconCls:"icon-clear"};
	$("#spename").textbox("addClearBtn", icons);
	$("#title").combobox("addClearBtn", icons);
	$("#workunits").textbox("addClearBtn", icons);
	
	loadProjectGrid(urls.queryUrl);
});

/**
 * 页面刷新
 */
function showReload() {
	projectDataGrid.datagrid('reload');
}

/**
 * 加载可项目grid列表
 * 
 * @param url
 */
function loadProjectGrid(url) {
	projectDataGrid = $("#projectDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort : false,
		multiSort : true,
		pageSize : 30,
		queryParams : {
			menuid : menuid
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {
			field : "speid",
			checkbox : true
		}, {
			field : "spename",
			title : "委员姓名",
			halign : 'center',
			width : 150,
			sortable : true
		}, {
			field : "titlename",
			title : "职称",
			halign : 'center',
			width : 200,
			sortable : true
		}, {
			field : "postname",
			title : "职务",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "degreename",
			title : "学历",
			halign : 'center',
			width : 150,
			sortable : true
		}, {
			field : "workunits",
			title : "工作单位",
			halign : 'center',
			width : 250,
			sortable : true
		} , {
			field : "address",
			title : "通讯地址",
			halign : 'center',
			width : 250,
			sortable : true
		}, {
			field : "postcode",
			title : "邮编",
			halign : 'center',
			hidden:true
		}, {
			field : "spedesc",
			title : "专业领域",
			halign : 'center',
			hidden:true
		}, {
			field : "phone",
			title : "联系电话",
			halign : 'center',
			hidden:true
		}, {
			field : "intro",
			title : "简介",
			halign : 'center',
			hidden:true
		}, {
			field : "userid",
			title : "userid",
			halign : 'center',
			hidden:true
		}, {
			field : "orgcode",
			title : "orgcode",
			halign : 'center',
			hidden:true
		}, {
			field : "createuser",
			title : "createuser",
			halign : 'center',
			hidden:true
		}, {
			field : "createtime",
			title : "createtime",
			halign : 'center',
			hidden:true
		}, {
			field : "modifyuser",
			title : "modifyuser",
			halign : 'center',
			hidden:true
		}, {
			field : "modifytime",
			title : "modifytime",
			halign : 'center',
			hidden:true
		}, {
			field : "orgname",
			title : "orgname",
			halign : 'center',
			hidden:true
		}, {
			field : "usercode",
			title : "usercode",
			halign : 'center',
			hidden:true
		}
		] ]
	});
}

/**
 * 查询
 */
function projectQuery() {
	var param = {
		title : $("#title").combobox('getValues').join(","),
		spename : $("#spename").val(),
		workunits: $("#workunits").val(),
		menuid : menuid
	};
	projectDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function projectAdd() {
	parent.$.modalDialog({
		title : "委员新增",
		width : 800,
		height : 460,
		href : urls.addUrl+"?operflag=add",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "title", "SPETITLE", "code", "name", mdDialog);// 委员职称
			comboboxFuncByCondFilter(menuid, "post", "SPEPOST", "code", "name", mdDialog);// 职务
			comboboxFuncByCondFilter(menuid, "degree", "SPEDEGREE", "code", "name", mdDialog);// 学历
			comboboxFuncByCondFilter(menuid, "membertype", "SPEMEMBERTYPE", "code", "name", mdDialog);// 委员类型
			mdDialog.find("#searchbox_agency").treeDialog({
				title :'机构选择',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'orgcode',
				prompt: "请选择机构名称",
				multiSelect: false, //单选树
				dblClickRow: true,
				checkLevs: [1,2,3], //只选择3级节点
				url : contextpath + '/sys/dept/sysDeptController/queryDeptTree.do',
				filters:{
					code: "机构编码",
					name: "机构名称"
				}
			});
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "projectAddForm", "");
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

// 提交表单
function submitForm(url, f, workflowflag) {
	var form = parent.$.modalDialog.handler.find('#' + f);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	var userpswd = parent.$.modalDialog.handler.find("#userpswd").val();
	var confpswd = parent.$.modalDialog.handler.find("#confpswd").val();
	if (confpswd != userpswd) {
		parent.$.messager.alert("提示", "两次输入密码不一致，请重新输入",
			"warn", function() {
				$("#confpswd").val("");
				$("#confpswd").focus();
			});
		return false;
	}
	form.form("submit", {
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

/**
 * 修改
 */
function projectUpdate() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条委员信息", null);
		return;
	}
	parent.$.modalDialog({
		title : "委员信息修改",
		width : 800,
		height : 460,
		href : urls.updateUrl+"?operflag=edit",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "title", "SPETITLE", "code", "name", mdDialog);// 委员职称
			comboboxFuncByCondFilter(menuid, "post", "SPEPOST", "code", "name", mdDialog);// 职务
			comboboxFuncByCondFilter(menuid, "degree", "SPEDEGREE", "code", "name", mdDialog);// 学历
			comboboxFuncByCondFilter(menuid, "membertype", "SPEMEMBERTYPE", "code", "name", mdDialog);// 委员类型
			mdDialog.find("#searchbox_agency").treeDialog({
				title :'机构选择',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'orgcode',
				prompt: "请选择机构名称",
				multiSelect: false, //单选树
				dblClickRow: true,
				checkLevs: [1,2,3], //只选择3级节点
				url : contextpath + '/sys/dept/sysDeptController/queryDeptTree.do',
				filters:{
					code: "机构编码",
					name: "机构名称"
				}
			});
			var row = projectDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.saveUrl, "projectAddForm", "");
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

/**
 * 删除
 */
function projectDelete() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = projectDataGrid.datagrid("getSelections")[0];
			$.post(urls.deleteUrl, {
				speid : row.speid
			}, function(result) {
				easyui_auto_notice(result, function() {
					projectDataGrid.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 详情
 */
function projectView() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "委员信息详情",
		width : 800,
		height : 460,
		href : urls.viewUrl + "?speid=" + row.speid,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var f = parent.$.modalDialog.handler.find('#projectViewForm');
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
 * 案件评价
 */
function projectTrace() {
	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条数据！", null);
		return;
	}
	
	var row = projectDataGrid.datagrid("getSelections")[0];
	var caseid = "1000099";
	this.location.href=urls.traUrl+"?caseid="+caseid+"&menuid="+menuid;
	
	
}
