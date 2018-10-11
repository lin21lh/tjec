
var baseUrl = contextpath + "demo/DemoController/controller/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"queryProject.do",
		projectaddUrl : baseUrl+"add.do",
		addCommitUrl : baseUrl+"demoSave.do",
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
	comboboxFuncByCondFilter(menuid,"xmlx", "NOTICELEVEL", "code", "name");//项目类型
	$("#sshy").treeDialog({
		title :'选择机构',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'sshy',
		prompt: "请选择机构",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "AGENCY",
		filters:{
			code: "机构代码",
			name: "机构名称"
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
		columns : [ [ {field : "id",checkbox : true}  
			          ,{field : "name",title : "姓名",halign : 'center',width:250,sortable:true}
			          ,{field : "sexMc",title : "性别",halign : 'center',width:100,sortable:true}
			          ,{field : "ssjgMc",title : "所属机构",halign : 'center',	width:80,sortable:true}
			          ,{field : "ssjg",title : "所属机构",halign : 'center',	width:80,sortable:true,hidden:true}
			          ,{field : "remark",title : "备注",halign : 'center',	width:80,sortable:true}
			          ,{field : "cjr",title : "创建人",halign : 'center',	width:80,sortable:true}
			          ,{field : "cjsj",title : "创建时间",halign : 'center',	width:80,sortable:true}
			          ,{field : "xgr",title : "修改人",halign : 'center',	width:80,sortable:true}
			          ,{field : "xgsj",title : "修改时间",halign : 'center',	width:80,sortable:true}
		             ] ]
	});
}
var datagrid;
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			xmlx : $("#xmlx").combobox('getValues').join(","),
			menuid : menuid,
			activityId : activityId,
			firstNode : true,
			lastNode : false
		};
	projectDataGrid.datagrid("load", param);
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
			comboboxFuncByCondFilter(menuid,"sex", "SEX", "code", "name", mdDialog);//性别
			mdDialog.find("#ssjgMc").treeDialog({
				title :'选择所属机构',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'sshy',
				prompt: "选择所属机构",
				editable :false,
				multiSelect: false, //单选树
				dblClickRow: true,
				queryParams : {
					menuid : menuid
				},
				url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
				checkLevs: [1,2,3], //只选择3级节点
				elementcode : "AGENCY",
				filters:{
					code: "机构代码",
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
				submitForm(urls.addCommitUrl,"projectAddForm","");
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
			comboboxFuncByCondFilter(menuid,"sex", "SEX", "code", "name", mdDialog);//性别
			mdDialog.find("#ssjgMc").treeDialog({
				title :'选择所属机构',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'sshy',
				prompt: "选择所属机构",
				editable :false,
				multiSelect: false, //单选树
				dblClickRow: true,
				queryParams : {
					menuid : menuid
				},
				url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
				checkLevs: [1,2,3], //只选择3级节点
				elementcode : "AGENCY",
				filters:{
					code: "机构代码",
					name: "机构名称"
				}
			});
			var row = projectDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#projectAddForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#filetd"),true,"XMCB",row.id,"");
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