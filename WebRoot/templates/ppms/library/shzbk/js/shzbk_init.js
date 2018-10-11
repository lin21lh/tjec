
var baseUrl = contextpath + "ppms/library/shzbk/ShzbkController/";
var urls = {
	queryUrl : baseUrl + "queryShzbk.do",
	addUrl : baseUrl + "addShzbk.do",
	addCommitUrl : baseUrl + "addCommitShzbk.do",
	editCommitUrl : baseUrl + "editCommitShzbk.do",
	deleteUrl : baseUrl + "deleteShzbk.do",
	relevanceUrl : baseUrl + "relevanceShzbk.do",
	relevanceCommitUrl : baseUrl + "relevanceCommitShzbk.do",
	checkUrl : baseUrl + "checkShzbk.do",
	detailUrl : baseUrl + "detailShzbk.do"
};


//默认加载
$(function() {
	
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "4",
		editable : false,
		data : [{text : "PPP中心录入", value : "4"}, {text : "用户录入", value : "1"}],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
				case '4':
					$('#editBtn').linkbutton('enable');
					$('#delBtn').linkbutton('enable');
					$('#relevanceBtn').linkbutton('enable');
					break;
				case '1':
					$('#editBtn').linkbutton('disable');
					$('#delBtn').linkbutton('disable');
					$('#relevanceBtn').linkbutton('disable');
					break;
				default:
					break;
			}
		}
	});
	
	comboboxFuncByCondFilter(menuid,"isrelevance", "SYS_TRUE_FALSE", "code", "name");//是否联合体
	$("#isrelevance").combobox("addClearBtn", {iconCls:"icon-clear"});

	comboboxFuncByCondFilter(menuid,"categoryCode", "CATEGORY", "code", "name");//所属行业
	$("#categoryCode").combobox("addClearBtn", {iconCls:"icon-clear"});
	
	$("#preferencesCode").treeDialog({
		title :'选择投资偏好',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'preferencesCode',
		prompt: "请选择投资偏好",
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
	
	loadShzbkGrid(urls.queryUrl);
	
});
var shzbkDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadShzbkGrid(url) {
	shzbkDataGrid = $("#shzbkDataGrid").datagrid({
		fit : true,
		striped : false,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			status : $("#status").combobox("getValue")
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "shzbkid",checkbox : true}  
			          ,{field : "socialid",title : "主键",halign : 'center',width:190,sortable:true,hidden:true}
			          ,{field : "usercode",title : "用户编码",halign : 'center',	width:80,sortable:true,hidden:true}
			          ,{field : "username",title : "用户名称",halign : 'center',width:125,sortable:true,hidden:true}
			          ,{field : "userpswd",title : "用户密码",halign : 'center',	width:80,sortable:true,hidden:true}
			          ,{field : "orgname",title : "社会资本名称",halign : 'center',	width:200,sortable:true,}
			          ,{field : "linkperson",title : "联系人",halign : 'center',width:80,sortable:true }
			          ,{field : "linkphone",title : "联系人号码",halign : 'center',	width:100	,sortable:true}
			          ,{field : "isrelevance",title : "是否关联",halign : 'center',align:'center',	width:60,sortable:true }
			          ,{field : "relevanceUserName",title : "关联(社会资本名称)",halign : 'center',	width:200,sortable:true	}
			          ,{field : "categoryName",title : "所属行业",halign : 'center',	width:150,sortable:true	}
			          ,{field : "preferencesName",title : "投资偏好",halign : 'center',	width:150,sortable:true	}
			          ,{field : "status",title : "用户类别",halign : 'center',	width:120,sortable:true,
			        	  formatter : function(value,row,index){
			        	  		switch(value){
				        	  		case '1':
				        	  			return "用户录入";
				        	  			break;
				        	  		case '4':
				        	  			return "PPP中心录入";
				        	  			break;
			        	  		}
			          		}
			          }
			          ,{field : "remark",title : "备注",halign : 'center',	width:240,sortable:true }
			          ,{field : "orgcode",title : "组织机构代码",halign : 'center',	width:100,sortable:true	,hidden:true}
			          ,{field : "iscombo",title : "是否是联合体",halign : 'center',	width:120,sortable:true,hidden:true}
			          ,{field : "applicationTime",title : "申请时间",halign : 'center',	width:120,sortable:true,hidden:true	}
			          ,{field : "auditTime",title : "审批时间",halign : 'center',	width:100,sortable:true,hidden:true }
			          ,{field : "auditUser",title : "审批人",halign : 'center',	width:150,sortable:true,hidden:true}
			          ,{field : "categoryCode",title : "所属行业编码",halign : 'center',	width:150,sortable:true,hidden:true	}
			          ,{field : "preferencesCode",title : "投资偏好编码",halign : 'center',	width:130,sortable:true,hidden:true	}
			          ,{field : "relevanceUser",title : "关联用户id",halign : 'center',	width:130,sortable:true	,hidden:true}
			          ,{field : "weixin",title : "微信标识",halign : 'center',	width:130,sortable:true	,hidden:true}
			          ,{field : "iscomboName",title : "是否联合体",halign : 'center',	width:130,sortable:true	,hidden:true}
		              
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var param = {
			status : $("#status").combobox("getValue"),
			orgname : $("#orgname").val(),
			linkphone : $("#linkphone").val(),
			isrelevance : $("#isrelevance").combobox("getValue"),
			categoryCode : $("#categoryCode").combobox("getValue"),
			preferencesCode : $("#preferencesCode").val()
			
		};
	
	shzbkDataGrid.datagrid("load", param);
}
/**
 * 社会资本名称验证
 * @param socialid
 * @param orgname
 * @return
 */
function isRepeat(socialid,orgname,isAdd){
	var flag = true;//默认不重名

	$.ajax({
		type : "post",
		url : urls.checkUrl,
		data : {
			socialid : socialid,
			orgname : orgname,
			flag : isAdd
		},
		async : false,
		success : function(result){
			if (result.success) {
				
			} else {
				easyui_warn("社会资本名称已被占用，请重新输入！",null);
				
				parent.$.modalDialog.handler.find("#orgname").next('span').find('input').focus();
				flag = false;
			}
		}
	});
	return flag;
}
/**
 * 新增
 */
function shzbkAdd(){
	parent.$.modalDialog({
		title : "社会资本新增",
		iconCls : 'icon-add',
		width : 900,
		height : 430,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"iscombo", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否联合体
			mdDialog.find("#iscombo").combobox("addClearBtn", {iconCls:"icon-clear"});
			mdDialog.find("#iscombo").combobox("setValue","0");//默认非联合体
			
			comboboxFuncByCondFilter(menuid,"categoryCode", "CATEGORY", "code", "name", mdDialog);//所属行业
			mdDialog.find("#categoryCode").combobox("addClearBtn", {iconCls:"icon-clear"});
			
			mdDialog.find("#preferencesName").treeDialog({
				title :'选择投资偏好',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'preferencesCode',
				prompt: "请选择投资偏好",
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
			
			//用户关联搜索框
			mdDialog.find("#relevanceUserName").searchbox({
				onChange: function(nv, ov) {
					var icon = mdDialog.find("#relevanceUserName").searchbox("textbox").prev("span.textbox-addon").find("a:first");
					console.log(icon);
					if (nv) {
						icon.css('visibility', 'visible');
					} else {
						icon.css('visibility', 'hidden');
					}
				},
				searcher : function(value,name){
					parent.$.modalDialog2({
						title : "用户关联",
						iconCls : 'icon-add',
						width : 900,
						height : 430,
						href : urls.relevanceUrl,
						queryParams : {
							status : '1'//查询状态为用户注册的记录
						},
						onLoad : function() {
							
						},
						buttons : [ {
							text : "确定",
							iconCls : "icon-save",
							handler : function() {
								var selectRow = parent.$.modalDialog.handler2.find("#shzbkRelevanceDataGrid").datagrid('getChecked');
								if(selectRow==null || selectRow.length==0||selectRow.length>1){
									easyui_warn("请选择一条数据！",null);
									return;
								}
								var sid = selectRow[0].socialid;
								var orgname = selectRow[0].orgname;
								parent.$.modalDialog.handler.find("#relevanceUserName").searchbox('setValue',orgname);
								parent.$.modalDialog.handler.find("#relevanceUser").val(sid);
								parent.$.modalDialog.handler2.dialog('close');
								
							}
						},{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.modalDialog.handler2.dialog('close');
							}
						} ]
					});
				}
			}).searchbox("addClearBtn", {iconCls:"icon-clear"});
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				if (!isRepeat("",parent.$.modalDialog.handler.find("#orgname").val(),"add")){
					return;
				}
				submitForm(urls.addCommitUrl,"shzbkAddForm","");
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
 * 新增、修改表单提交
 * @param url
 * @param form
 * @param workflowflag
 * @return
 */
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	var mdDialog = parent.$.modalDialog.handler;
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	//用户关联搜索框隐藏域验证
	var relevanceUserName = mdDialog.find("#relevanceUserName").val();
	if (relevanceUserName == ''){
		mdDialog.find("#relevanceUser").val("");
	}
	mdDialog.find("#categoryName").val(mdDialog.find("#categoryCode").combobox("getText"));
	
	form.form("submit",
			{
		url : url,
		onSubmit : function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			return true;
		},
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success) {
				easyui_auto_notice(result, function() {
					shzbkDataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				});
				
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

/**
 * 修改
 */
function shzbkEdit(){
	var selectRow = shzbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "社会资本修改",
		iconCls : 'icon-edit',
		width : 900,
		height : 430,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"iscombo", "SYS_TRUE_FALSE", "code", "name", mdDialog);//是否联合体
			mdDialog.find("#iscombo").combobox("addClearBtn", {iconCls:"icon-clear"});
			
			comboboxFuncByCondFilter(menuid,"categoryCode", "CATEGORY", "code", "name", mdDialog);//所属行业
			mdDialog.find("#categoryCode").combobox("addClearBtn", {iconCls:"icon-clear"});
			
			mdDialog.find("#preferencesName").treeDialog({
				title :'选择投资偏好',
				dialogWidth : 420,
				dialogHeight : 500,
				hiddenName:'preferencesCode',
				prompt: "请选择投资偏好",
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
			
			//用户关联搜索框
			mdDialog.find("#relevanceUserName").searchbox({
				onChange: function(nv, ov) {
					var icon = mdDialog.find("#relevanceUserName").searchbox("textbox").prev("span.textbox-addon").find("a:first");
					console.log(icon);
					if (nv) {
						icon.css('visibility', 'visible');
					} else {
						icon.css('visibility', 'hidden');
					}
				},
				searcher : function(value,name){
					parent.$.modalDialog2({
						title : "用户关联",
						iconCls : 'icon-add',
						width : 900,
						height : 430,
						href : urls.relevanceUrl,
						queryParams : {
							socialid : parent.$.modalDialog.handler.find("#socialid").val(),
							status : parent.$.modalDialog.handler.find("#status").val()!=4?4:1
							
						},
						onLoad : function() {
							
						},
						buttons : [ {
							text : "确定",
							iconCls : "icon-save",
							handler : function() {
								var selectRow = parent.$.modalDialog.handler2.find("#shzbkRelevanceDataGrid").datagrid('getChecked');
								if(selectRow==null || selectRow.length==0||selectRow.length>1){
									easyui_warn("请选择一条数据！",null);
									return;
								}
								var sid = selectRow[0].socialid;
								var orgname = selectRow[0].orgname;
								parent.$.modalDialog.handler.find("#relevanceUserName").searchbox('setValue',orgname);
								parent.$.modalDialog.handler.find("#relevanceUser").val(sid);
								parent.$.modalDialog.handler2.dialog('close');
								
							}
						},{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								parent.$.modalDialog.handler2.dialog('close');
							}
						} ]
					});
				}
			}).searchbox("addClearBtn", {iconCls:"icon-clear"});
			
			var row = shzbkDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#shzbkAddForm');
			f.form("load", row);
			
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				if (!isRepeat(parent.$.modalDialog.handler.find("#socialid").val(),parent.$.modalDialog.handler.find("#orgname").val(),"edit")){
					return;
				}
				submitForm(urls.editCommitUrl,"shzbkAddForm","");
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
 *删除
 */
function shzbkDelete(){
	var selectRow = shzbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = shzbkDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中社会资本删除？", function(r) {
		if (r) {
			$.post(urls.deleteUrl, {
				socialid : row.socialid
			}, function(result) {
				easyui_auto_notice(result, function() {
					shzbkDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}

/**
 * 用户关联
 * @return
 */
function shzbkRelevance(){
	var selectRow = shzbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}

	parent.$.modalDialog({
		title : "用户关联",
		iconCls : 'icon-view',
		width : 900,
		height : 430,
		href : urls.relevanceUrl,
		queryParams : {
			socialid : shzbkDataGrid.datagrid("getSelections")[0].socialid,
			status : shzbkDataGrid.datagrid("getSelections")[0].status!=4?4:1
		},
		onLoad : function() {
			
		},
		buttons : [ {
			text : "确定",
			iconCls : "icon-save",
			handler : function() {
				var sid1 = shzbkDataGrid.datagrid("getSelections")[0].socialid;
				var sid3 = shzbkDataGrid.datagrid("getSelections")[0].relevanceUser;
				var selectRow = parent.$.modalDialog.handler.find("#shzbkRelevanceDataGrid").datagrid('getSelections');
				if(selectRow==null || selectRow.length==0||selectRow.length>1){
					easyui_warn("请选择一条数据！",null);
					return;
				}
				var sid2 = selectRow[0].socialid;
				$.post(urls.relevanceCommitUrl, 
					{
						sid1 : sid1,//关联用户
						sid2 : sid2,//新被关联用户
						sid3 : sid3//原被关联用户
					},
					function(result) {
						if (result.success) {
							shzbkDataGrid.datagrid('reload');
							parent.$.modalDialog.handler.dialog('close');
						} else {
							parent.$.messager.alert('错误', result.title, 'error');
						}
						shzbkDataGrid.datagrid('reload');
					},
					"json"
				);
			}
		},{
			text : "取消",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});

}

/**
 * 详情
 * @return
 */
function shzbkView(){
	var selectRow = shzbkDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "社会资本详情",
		iconCls : 'icon-add',
		width : 900,
		height : 430,
		href : urls.detailUrl,
		onLoad : function() {
			var row = shzbkDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#shzbkDetailForm');
			f.form("load", row);
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
