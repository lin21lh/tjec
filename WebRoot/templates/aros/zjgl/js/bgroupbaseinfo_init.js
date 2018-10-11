var baseUrl = contextpath + "aros/zjgl/BGroupbaseinfoController/";

var projectDataGrid;
var datagrid;
var kxgrid;
var yxgrid;
var urls = {
	queryUrl : baseUrl + "queryList.do",
	addUrl : baseUrl + "add.do",
	updateUrl : baseUrl + "add.do",
	saveUrl : baseUrl + "save.do",
	deleteUrl : baseUrl + "delete.do",
	viewUrl : baseUrl + "view.do",
	zjUrl : baseUrl + "querySpeList.do",
	zjxzUrl : baseUrl + "zjxzInit.do",
	saveZjxzUrl : baseUrl + "saveZjxz.do"
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
	var icons = {iconCls:"icon-clear"};
	$("#groupname").textbox("addClearBtn", icons);
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
			field : "groupid",
			checkbox : true
		}, {
			field : "groupname",
			title : "小组名称",
			halign : 'center',
			width : 150,
			sortable : true
		}, {
			field : "ifcheckname",
			title : "是否允许查看案件",
			halign : 'center',
			width : 150,
			sortable : true
		}, {
			field : "casedesc",
			title : "案件描述",
			halign : 'center',
			width : 250,
			sortable : true
		}, {
			field : "question",
			title : "咨询问题",
			halign : 'center',
			width : 250,
			sortable : true
		}, {
			field : "cjrMc",
			title : "创建人",
			halign : 'center',
			width : 150,
			sortable : true
		},{
			field : "opttime",
			title : "创建时间",
			halign : 'center',
			
			sortable : true
		},{
			field : "operator",
			title : "创建人",
			halign : 'center',
			hidden:true
		}, {
			field : "ifcheck",
			title : "是否允许查看案件",
			halign : 'center',
			hidden:true
		} ] ]
	});
}

/**
 * 查询
 */
function projectQuery() {
	var param = {
		menuid : menuid
	};
	projectDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function projectAdd() {
	parent.$.modalDialog({
		title : "委员小组管理",
		width : 800,
		height : 300,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid, "ifcheck", "ISORNOT", "code", "name", mdDialog);// 是否允许查看案件
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
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
		easyui_warn("请选择一条记录！", null);
		return;
	}
	parent.$.modalDialog({
		title : "委员信息修改",
		width : 800,
		height : 300,
		href : urls.updateUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid, "ifcheck", "ISORNOT", "code", "name", mdDialog);// 委员职称			
			var row = projectDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#menuid").val(menuid);
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
		easyui_warn("请选择一条记录！", null);
		return;
	}

	parent.$.messager.confirm("确认删除", "是否要删除选中数据？", function(r) {
		if (r) {
			var row = projectDataGrid.datagrid("getSelections")[0];
			$.post(urls.deleteUrl, {
				groupid : row.groupid
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
		title : "委员小组信息详情",
		width : 800,
		height : 300,
		href : urls.viewUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var f = mdDialog.find('#projectViewForm');
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
 * 小组成员维护
 */

function projectCreate() {

	var selectRow = projectDataGrid.datagrid('getChecked');
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请先选择一个小组！", null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "小组成员维护",
		width : 950,
		height : 450,
		href : urls.zjxzUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//所有委员列表
			zjDataGrid(mdDialog, row.groupid);
			//小组委员列表
			zjxzDataGrid(mdDialog, row.groupid);
			
			mdDialog.find("#addBtn").on("click",function(){
				triggerSaveZjxz(row.groupid,urls.saveZjxzUrl,"add");
			});
			mdDialog.find("#delBtn").on("click",function(){
				triggerSaveZjxz(row.groupid,urls.saveZjxzUrl,"delete");
			});
		}
	});
}

//可选委员列表
function zjDataGrid(mdDialog, groupid) {
	kxgrid = mdDialog.find("#zjTable").datagrid({
		title : "可选委员列表",
		height : 400,
		width : '100%',
		collapsible : false,
		url : urls.zjUrl,
		queryParams : {
			groupid : groupid,
			operflag : 'all'
		},
		singleSelect : true,
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
			sortable : true
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
			sortable : true
		} ] ],
        onDblClickRow:function (rowIndex, rowData) {       	 
//     	  mdDialog.find("#zjxzTable").datagrid('appendRow', rowData);//追加一行
//    	  mdDialog.find("#zjTable").datagrid('deleteRow',rowIndex);//删除一行
        	saveZjxz(rowData.speid,groupid,urls.saveZjxzUrl,'add');
        }		
	});
	return kxgrid;
}

//已选委员列表
function zjxzDataGrid(mdDialog,groupid){
	yxgrid = mdDialog.find("#zjxzTable").datagrid({
		height: 400,
		width:'100%',
		title: '已选委员列表',
		collapsible: false,
		url : urls.zjUrl,
		queryParams : {groupid:groupid,operflag : 'group'},
		singleSelect: true,
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
			sortable : true
		}, {
			field : "titlename",
			title : "委员职称",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "postname",
			title : "委员职务",
			halign : 'center',
			width : 100,
			sortable : true
		}, {
			field : "degreename",
			title : "委员学历",
			halign : 'center',
			width : 90,
			sortable : true
		} ] ],
       onDblClickRow:function (rowIndex, rowData) {		        	 
//    	   mdDialog.find("#zjTable").datagrid('appendRow', rowData);//追加一行
//     	   mdDialog.find("#zjxzTable").datagrid('deleteRow',rowIndex);//删除一行
    	   saveZjxz(rowData.speid,groupid,urls.saveZjxzUrl,'delete');
       }
	});
	return yxgrid;
}
function triggerSaveZjxz(groupid,url,flag){
	var selectRow;
	var row;
	var title = "";
	if(flag=="add") {		
		selectRow = kxgrid.datagrid('getChecked');
		row = kxgrid.datagrid("getSelections")[0];
		title = "可选";
	}else {
		selectRow = yxgrid.datagrid('getChecked');
		row = yxgrid.datagrid("getSelections")[0];
		title = "已选";
	}
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一个"+title+"委员！", null);
		return;
	}
	saveZjxz(row.speid,groupid,url,flag);
	
}
function saveZjxz(speid,groupid,url,flag){
	var params = {
			speid:speid,
			groupid:groupid,
			operflag:flag
	};
	$.ajax({
		type:'post',
		url:url,
		data:params,
		success:function(data) {
			if (data.success) {
				kxgrid.datagrid("reload");
				kxgrid.datagrid("clearSelections");
				yxgrid.datagrid("reload");
				yxgrid.datagrid("clearSelections");
			} else {
				easyui_warn(data.title);
			}
		}
	});
}
