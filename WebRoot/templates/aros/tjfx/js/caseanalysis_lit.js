var baseUrl = contextpath + "aros/tjfx/controller/";

var projectDataGrid;
var datagrid;
var urls = {
	queryUrl : baseUrl + "queryList.do?anaType=9",
	addUrl : baseUrl + "add.do"
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
	comboboxFuncByCondFilter(menuid, "startyear", "ANALYSISYEAR", "code", "name");// 职称
	$("#startyear").combobox("addClearBtn", {iconCls:"icon-clear"});
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
		onLoadSuccess: compute,//加载完毕后执行计算
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
		frozenColumns: [[    
						{"title":"裁判结果",field: 'result'},
						{"title":"裁判总数",field: 'pcount'}    
                     ]],
		columns : [ 
					[
					 
					 {"title":"审理阶段","colspan":3},
					 {"title":"收案方式","colspan":4},
					 {"title":"当事人类型","colspan":2},
					 {"title":"是否上诉","colspan":2}
					], 
		            [ 
		              { field : "s01",title : "一审",halign : 'center',	width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "s02",title : "二审",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "s03",	title : "再审",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              
		              {	field : "r01",title : "直接应诉",halign : 'center',width : 150,sortable : true}, 
		              {	field : "r02",title : "复议维持应诉",	halign : 'center',width : 100,sortable : true} , 
		              {	field : "r03",title : "复议改变应诉",halign : 'center',	width : 100,sortable : true}, 
		              {	field : "r04",title : "其他",halign : 'center',width : 100,sortable : true}, 
		              
		              {	field : "t01",title : "单独被告",halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "t02",title : "共同被告",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}},
		             	              
		              {	field : "i01",title : "是",halign : 'center',width : 100,sortable : true}, 
		              {	field : "i02",title : "否",	halign : 'center',width : 100,sortable : true}
		           ] 
		         ]
	});	
	
}

function compute() {//计算函数
    var rows = $('#projectDataGrid').datagrid('getRows')//获取当前的数据行
    var pcountT=0,
	    s01T=0,s02T=0,s03T=0
	    r01T=0,r02T=0,r03T=0,r04T=0,
	    t01T=0,t02T=0,
	    i01T=0,i02T=0;
    if(rows.length == 0){
    	$('#projectDataGrid').datagrid('appendRow',{});
    } else if(rows.length>0){
    	for (var i = 0; i < rows.length; i++) {
        	pcountT += rows[i]['pcount'];
        	s01T += rows[i]['s01'];
        	s02T += rows[i]['s02'];
        	s03T += rows[i]['s03'];
        	        	
        	r01T += rows[i]['r01'];
        	r02T += rows[i]['r02'];
        	r03T += rows[i]['r03'];
        	r04T += rows[i]['r04'];
        	
        	t01T += rows[i]['t01'];
        	t02T += rows[i]['t02'];
        	
        	i01T += rows[i]['i01'];
        	i02T += rows[i]['i02'];
        }
        //新增一行显示统计信息
        $('#projectDataGrid').datagrid('appendRow', 
        		{ 
        			result: '统计', 
        			pcount: pcountT,
        			s01:s01T,s02:s02T,s03:s03T,
        			r01:r01T,r02:r02T,r03:r03T,r04:r04T,
        			t01:t01T,t02:t02T,
        			i01:i01T,i02:i02T
        		}
        );
    }
    
}

/**
 * 查询
 */
function projectQuery() {
	var param = {
			startyear : $("#startyear").combobox('getValues').join(","),
			menuid : menuid
		};
	projectDataGrid.datagrid("load", param);
}

/**
 * 导出为Excel
 */
function outExcel() {
	var paramJSON = {
			includeHidden : false,
			title : '按应诉裁判结果统计',
			outExcelModel:'2',
			excelVersion: '2007'
	};
	$("#projectDataGrid").datagrid("outExcel",paramJSON);
}

/**
 * 新增
 */
function projectAdd() {
	parent.$.modalDialog({
		title : "委员管理",
		width : 800,
		height : 460,
		href : urls.addUrl+"?operflag=add",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid, "title", "SPETITLE", "code", "name", mdDialog);// 委员职称
			comboboxFuncByCondFilter(menuid, "post", "SPEPOST", "code", "name", mdDialog);// 职务
			comboboxFuncByCondFilter(menuid, "degree", "SPEDEGREE", "code", "name", mdDialog);// 学历
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
		easyui_warn("请选择一条记录！", null);
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
		easyui_warn("请选择一条记录！", null);
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
		href : urls.viewUrl + "?id=" + row.speid,
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
		easyui_warn("请选择一条记录！", null);
		return;
	}
	
	var row = projectDataGrid.datagrid("getSelections")[0];
	var caseid = "1000099";
	
	
	this.location.href=urls.traUrl+"?caseid="+caseid+"&menuid="+menuid;
	
	
}
