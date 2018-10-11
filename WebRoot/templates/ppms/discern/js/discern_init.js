
var baseUrl = contextpath + "ppms/discern/ProjectDiscernController/";
var urls = {
	queryUrl : baseUrl + "queryProject.do",
	productQueryUrl : baseUrl + "queryProduct.do",
	projectaddUrl : baseUrl + "projectAdd.do",
	addCommitUrl : baseUrl + "projectAddCommit.do",
	editCommitUrl : baseUrl + "projectEditCommit.do",
	projectDelete : baseUrl + "projectDelete.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	projectDetailUrl : baseUrl + "projectDetail.do"
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
				$('#editBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
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
	$("#proReturn").combobox("addClearBtn", {iconCls:"icon-clear"});
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
			          ,{field : "sfxmName",title : "示范项目",halign : 'center',	width:100,sortable:true }
			          ,{field : "tjxmName",title : "推介项目",halign : 'center',	width:80,sortable:true }
			          ,{field : "sqbtName",title : "申请补贴",halign : 'center',	width:80,sortable:true }
			          ,{field : "btje",title : "补贴金额（万元）",halign : 'right',align:'right'}
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
		              ,{field : "sfxm",title : "示范项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "tjxm",title : "推介项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "sqbt",title : "申请补贴",halign : 'center',	width:100,sortable:true ,hidden:true	}
		             ] ]
	});
}
/**
 * 新增按钮
 */
var datagrid;
function projectAdd(){
	parent.$.modalDialog({
		title : "项目录入",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.projectaddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name", mdDialog);//项目类型
			comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name", mdDialog);//项目运作方式
			comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name", mdDialog);//项目回报机制
			comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name", mdDialog);//项目发起类型
			comboboxFuncByCondFilter(menuid,"sfxm", "PROSFXM", "code", "name", mdDialog);//示范项目
			comboboxFuncByCondFilter(menuid,"tjxm", "SYS_TRUE_FALSE", "code", "name", mdDialog);//推介项目
			comboboxFuncByCondFilter(menuid,"sqbt", "SYS_TRUE_FALSE", "code", "name", mdDialog);//申请补贴
			
			mdDialog.find("#proTradeName").treeDialog({
				title :'选择所属行业',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'proTrade',
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
			datagrid = productGrid(mdDialog, '');
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#projectAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#projectduct").val(rowstr);
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.addCommitUrl,"projectAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#projectAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');//获取所有的grid数据
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#projectduct").val(rowstr);
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
	var mdDialog = parent.$.modalDialog.handler;
	var grid = mdDialog.find("#projectAddGrid");
	var data = grid.datagrid("getData");
	var total = data.total;
	if(total<1){
		parent.$.messager.alert('系统提示', "请录入项目产出物！", 'info');
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
//加载产出物列表
function productGrid(mdDialog,projectid){
	var grid = mdDialog.find("#projectAddGrid").datagrid({
        height: 320,
        url : urls.productQueryUrl,
        queryParams : {projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'productid',
        columns: [[//显示的列
                   {field: 'productid', title: '序号', width: 100, sortable: true, checkbox: true },
                   { field: 'year', title: '年度', width: 100, sortable: true,
                       editor: { type: 'numberspinner', options: {min:2000,max:2099,editable:true,required: true,missingMessage:'请输入年度'}}
                   },{ field: 'output', title: '产出物', width: 200,
                        editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入产出物',validType:{length:[0,500]}} }
                   },{ field: 'unit', title: '计量单位', width: 100,
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入计量单位',validType:{length:[0,10]}} }
                   },{ field: 'amount', title: '数量', width: 100,
                         editor: { type: 'numberbox', options: { required: true,missingMessage:'请输入数量',validType:{length:[0,10]}} }
                   },{ field: 'remark', title: '备注', width: 200,
                         editor: { type: 'text', options: {validType:{length:[0,500]}} }
                   }]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
            	var data = mdDialog.find("#projectAddGrid").datagrid("getData");//grid列表
            	var total = data.total;//grid的总条数
            	mdDialog.find("#projectAddGrid").datagrid('appendRow', {row: {}});//追加一行
            	grid.datagrid("beginEdit", total);//开启编辑
            }
        },'-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                var row = mdDialog.find("#projectAddGrid").datagrid('getChecked');
                if(row ==''){
                	easyui_warn("请选择要删除的数据！");
                	return;
                }else{
                	parent.$.messager.confirm("提示", "你确定要删除吗?", function (r) {
                         if (r) {
                        	 var index = mdDialog.find("#projectAddGrid").datagrid('getRowIndex',row[0]);
                        	 //选择要删除的行
                        	 mdDialog.find("#projectAddGrid").datagrid('deleteRow',index);
                         }
                     });
                }
             }
        }],
        onDblClickRow:function (rowIndex, rowData) {
        	mdDialog.find("#projectAddGrid").datagrid('beginEdit', rowIndex);
        },
        onClickRow:function(rowIndex,rowData){
        	mdDialog.find("#projectAddGrid").datagrid('beginEdit', rowIndex);
        }
    });
	return grid;
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
function projectEdit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目修改",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.projectaddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name", mdDialog);//项目类型
			comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name", mdDialog);//项目运作方式
			comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name", mdDialog);//项目回报机制
			comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name", mdDialog);//项目发起类型
			comboboxFuncByCondFilter(menuid,"sfxm", "PROSFXM", "code", "name", mdDialog);//示范项目
			comboboxFuncByCondFilter(menuid,"tjxm", "SYS_TRUE_FALSE", "code", "name", mdDialog);//推介项目
			comboboxFuncByCondFilter(menuid,"sqbt", "SYS_TRUE_FALSE", "code", "name", mdDialog);//申请补贴
			
			mdDialog.find("#proTradeName").treeDialog({
				title :'选择所属行业',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'proTrade',
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
			datagrid = productGrid(mdDialog, row.projectid);
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);
			//加载附件
			showFileDiv(mdDialog.find("#ssfa"),true, row.proSchemepath, "30","proSchemepath");
			showFileDiv(mdDialog.find("#kxxyj"),true, row.proReportpath, "30","proReportpath");
			showFileDiv(mdDialog.find("#hpbg"),true, row.proConditionpath, "30","proConditionpath");
			showFileDiv(mdDialog.find("#xmccw"),true, row.proArticlepath, "30","proArticlepath");
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#projectAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#projectduct").val(rowstr);
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.editCommitUrl,"projectAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#projectAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');//获取所有的grid数据
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#projectduct").val(rowstr);
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
		easyui_warn("请选择一条数据！",null);
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
	parent.$.messager.confirm("送审确认", "确认要选中项目送审？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				activityId :activityId,
				projectid : row.projectid,
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
				projectid : row.projectid,
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
 * 工作流信息
 */
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
function projectView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目详情",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.projectDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var row = projectDataGrid.datagrid("getSelections")[0];
			//加载产出物列表
			productGridDetail(mdDialog, row.projectid);
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);
			//加载附件
			showFileDiv(mdDialog.find("#ssfa"),false, row.proSchemepath, "30","proSchemepath");
			showFileDiv(mdDialog.find("#kxxyj"),false, row.proReportpath, "30","proReportpath");
			showFileDiv(mdDialog.find("#hpbg"),false, row.proConditionpath, "30","proConditionpath");
			showFileDiv(mdDialog.find("#xmccw"),false, row.proArticlepath, "30","proArticlepath");
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
//加载产出物列表
function productGridDetail(mdDialog,projectid){
	var grid = mdDialog.find("#projectAddGrid").datagrid({
        height: 320,
        width: 880,
        collapsible: true,
        url : urls.productQueryUrl,
        queryParams : {projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'productid',
        columns: [[//显示的列
                   { field: 'year', title: '年度', width: 100, sortable: true,
                       editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'output', title: '产出物', width: 200,
                        editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'unit', title: '计量单位', width: 100,
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'amount', title: '数量', width: 100,
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'remark', title: '备注', width: 200,
                         editor: { type: 'text', options: { required: true} }
                   }]]
    });
	return grid;
}