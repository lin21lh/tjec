var baseUrl = contextpath + "aros/tjfx/controller/";

var projectDataGrid;
var datagrid;
var urls = {
	queryUrl : baseUrl + "queryList.do?anaType=11",
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
						{"title":"委员名称",field: 'speid'},
						{"title":"参与案件总数",field: 'ccount'},
						{"title":"复议研讨总数",field: 'tcount'}
                     ]],
		columns : [ 
					[
					 
					 {"title":"被申请人机构类型","colspan":8},
					 {"title":"复议事项类型","colspan":10},
					 {"title":"行政赔偿","colspan":2}
					], 
		            [ 
		              { field : "c01",title : "省级行政机关",halign : 'center',	width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "c02",title : "省级政府部门",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "c03",	title : "市（地）级政府",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "c04",title : "市（地）级政府部门",halign : 'center',width : 150,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "c05",title : "县级政府",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}} , 
		              {	field : "c06",title : "县级政府部门",halign : 'center',	width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "c07",title : "乡镇政府",halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "c08",title : "其他",halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "d01",title : "行政处罚",	halign : 'center',width : 100,sortable : true},
		              {	field : "d02",title : "行政强制措施",	halign : 'center',width : 100,sortable : true},
		              { field : "d03",title : "行政征收",halign : 'center',	width : 100,sortable : true}, 
		              {	field : "d04",title : "行政许可",	halign : 'center',width : 100,sortable : true}, 
		              {	field : "d05",title : "行政确权",	halign : 'center',width : 100,sortable : true}, 
		              {	field : "d06",title : "行政确认",halign : 'center',width : 100,sortable : true}, 
		              {	field : "d07",title : "信息公开",	halign : 'center',width : 100,sortable : true} , 
		              {	field : "d08",title : "举报投诉处理",halign : 'center',	width : 100,sortable : true}, 
		              {	field : "d09",title : "行政不作为",halign : 'center',width : 100,sortable : true}, 
		              {	field : "d10",title : "其他",halign : 'center',	width : 100,sortable : true}, 		              
		              {	field : "icount",title : "案件总数",halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}, 
		              {	field : "iamount",title : "赔偿金额（元）",	halign : 'center',width : 100,sortable : true,styler:function(value,row,index){return 'background-color:#F0F0F0;';}}
		           ] 
		         ]
	});	
	
}

function compute() {//计算函数
    var rows = $('#projectDataGrid').datagrid('getRows')//获取当前的数据行
    var ccountT = 0
	    ,tcountT=0
	    c01T=0,c02T=0,c03T=0,c04T=0,c05T=0,c06T=0,c07T=0,c08T=0,
	    d01T=0,d02T=0,d03T=0,d04T=0,d05T=0,d06T=0,d07T=0,d08T=0,d09T=0,d10T=0,
	    icountT=0,iamountT=0;
    if(rows.length == 0){
    	$('#projectDataGrid').datagrid('appendRow',{});
    } else if(rows.length>0){
    	for (var i = 0; i < rows.length; i++) {
    		ccountT += rows[i]['ccount'];
    		tcountT += rows[i]['tcount'];
  
    		c01T += rows[i]['c01'];
        	c02T += rows[i]['c02'];
        	c03T += rows[i]['c03'];
        	c04T += rows[i]['c04'];
        	c05T += rows[i]['c05'];
        	c06T += rows[i]['c06'];
        	c07T += rows[i]['c07'];
        	c08T += rows[i]['c08'];
        	
        	d01T += rows[i]['d01'];
        	d02T += rows[i]['d02'];
        	d03T += rows[i]['d03'];
        	d04T += rows[i]['d04'];
        	d05T += rows[i]['d05'];
        	d06T += rows[i]['d06'];
        	d07T += rows[i]['d07'];
        	d08T += rows[i]['d08'];
        	d09T += rows[i]['d09'];
        	d10T += rows[i]['d10'];
        	
        	icountT += rows[i]['icount'];
        	iamountT += rows[i]['iamount'];
        	
        }
        //新增一行显示统计信息
        $('#projectDataGrid').datagrid('appendRow', 
        		{ 
        			speid: '统计', 
        			ccount: ccountT,
        			tcount: tcountT, 
        			c01:c01T,c02:c02T,c03:c03T,c04:c04T,c05:c05T,c06:c06T,c07:c07T,c08:c08T,
        			d01:d01T,d02:d02T,d03:d03T,d04:d04T,d05:d05T,d06:d06T,d07:d07T,d08:d08T,d09:d09T,d10:d10T,
        			icount:icountT,iamount:iamountT
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
			title : '按委员参与案件统计',
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
