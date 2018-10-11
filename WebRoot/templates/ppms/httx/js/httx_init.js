var baseUrl = contextpath + "ppms/httx/HttxController/";
var urls = {
	queryUrl : baseUrl + "queryHttx.do",
	addUrl : baseUrl + "addHttx.do",
	appendUrl : baseUrl + "appendHttx.do",
	addCommitUrl : baseUrl + "addCommitHttx.do",
	appendCommitUrl : baseUrl + "appendCommitHttx.do",
	editCommitUrl : baseUrl + "editCommitHttx.do",
	projectUrl : baseUrl + "projectHttx.do",
	detailUrl : baseUrl + "detailHttx.do"
};


//默认加载
$(function() {
	comboboxFuncByCondFilter(menuid,"htlb", "PROHTLB", "code", "name");//合同类别
	$("#htlb").combobox("addClearBtn", {iconCls:"icon-clear"});
	
	loadHttxGrid(urls.queryUrl);
});
var httxDataGrid;

/**
 * 加载datagrid列表
 * @param url
 * @return
 */
function loadHttxGrid(url) {
	httxDataGrid = $("#httxDataGrid").datagrid({
		fit : true,//自动在父容器最大范围内调整大小
	//	striped : true,//是否显示斑马线效果
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,//是否允许多列排序
		pageSize : 30,
		queryParams: {
			
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "checkid",checkbox : true}
			          ,{field : "httxid",title : "主键",halign : 'center',width:120,sortable:true,hidden:true}
			          ,{field : "projectid",title : "项目id",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "xmmc",title : "项目名称",halign : 'center',width:200,sortable:true}
			          ,{field : "implementOrgan",title : "实施机构",halign : 'center',width:150,sortable:true}
			          ,{field : "htmc",title : "合同名称",halign : 'center',width:200,sortable:true}
			          ,{field : "htlbname",title : "合同类别",halign : 'center',width:100,sortable:true}
			          ,{field : "jfmc",title : "甲方（采购单位）",halign : 'center',width:150,sortable:true}
			          ,{field : "yfmc",title : "乙方（供货单位）",halign : 'center',width:150,sortable:true}
			          ,{field : "htqdrq",title : "合同签订日期",halign : 'center',width:80,sortable:true}
			          ,{field : "htje",title : "合同金额（元）",halign : 'center',align:'right',width:100,sortable:true,
			        	  formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
			          ,{field : "htztname",title : "合同状态",halign : 'center',align:'center',width:80,sortable:true}
			          ,{field : "sfzj",title : "是否追加合同",halign : 'center',align:'center',width:100,sortable:true,
			        	  formatter:function(value,row,index){
			        	  if(value == 0){
			        		  return "否";
			        	  } else {
			        		  return "是";
			        	  }
			          }
			          }
			          ,{field : "htnr",title : "合同内容",halign : 'center',width:250,sortable:true}
			          ,{field : "htlb",title : "合同类别",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "jflxr",title : "甲方联系人",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "jflxrdh",title : "甲方联系人电话",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "yflxr",title : "乙方联系人",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "yflxrdh",title : "乙方联系人电话",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "htzxksrq",title : "合同执行开始日期",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "htzxjsrq",title : "合同执行结束日期",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "htfj",title : "合同附件",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "htzt",title : "合同状态",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "cjr",title : "创建人",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "cjsj",title : "创建时间",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "xgr",title : "修改人",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "xgsj",title : "修改时间",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "sjhttxid",title : "上级合同体系id",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "orgcode",title : "录入人单位",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "cjrname",title : "创建人",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "orgcodename",title : "录入人单位",halign : 'center',width:100,sortable:true,hidden:true}
			          ,{field : "xgrname",title : "修改人",halign : 'center',width:100,sortable:true,hidden:true}
		              
		             ] ]
	});
}

/**
 * 查询
 */
function topQuery(){
	var htqdrqBegin = $('#htqdrqBegin').datebox('getValue');
	var htqdrqEnd = $('#htqdrqEnd').datebox('getValue');
	
	if(htqdrqEnd < htqdrqBegin && htqdrqEnd != "")
	{
		parent.$.messager.alert('提示', "结束时间应大于开始时间！", 'warnning');
		return;
	}
	var param = {
			htmc :　$("#htmc").val(),
			jfmc : $("#jfmc").val(),
			yfmc : $("#yfmc").val(),
			htqdrqBegin : htqdrqBegin,
			htqdrqEnd : htqdrqEnd,
			htlb : $("#htlb").combobox("getValue"),
			xmmc :　$("#xmmc").val()
			
		};
	httxDataGrid.datagrid("load", param);
}

/**
 * 新增
 */
function httxAdd(){
	parent.$.modalDialog({
		title : "合同新增",
		iconCls : 'icon-add',
		width : 900,
		height : 530,
		href : urls.addUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			comboboxFuncByCondFilter(menuid,"htlb", "PROHTLB", "code", "name", mdDialog);//合同类别
			comboboxFuncByCondFilter(menuid,"htzt", "PROHTZT", "code", "name", mdDialog);//合同状态
			mdDialog.find("#htzt").combobox("setValue","1");//合同状态设置为有效
			//项目关联
			mdDialog.find("#xmmc").searchbox({
				editable : false,
				onChange: function(nv, ov) {
					var icon = mdDialog.find("#xmmc").searchbox("textbox").prev("span.textbox-addon").find("a:first");
					console.log(icon);
					if (nv) {
						icon.css('visibility', 'visible');
					} else {
						icon.css('visibility', 'hidden');
					}
				},
				searcher : function(value,name){
					parent.$.modalDialog2({
						title : "项目选择",
						iconCls : 'icon-add',
						width : 900,
						height : 430,
						href : urls.projectUrl,
						queryParams : {
							
						},
						onLoad : function() {
							
						},
						buttons : [ {
							text : "确定",
							iconCls : "icon-save",
							handler : function() {
								var selectRow = parent.$.modalDialog.handler2.find("#httxProjectDataGrid").datagrid('getChecked');
								if(selectRow==null || selectRow.length==0||selectRow.length>1){
									easyui_warn("请选择一条数据！",null);
									return;
								}
								var projectid = selectRow[0].projectid;
								var proName = selectRow[0].proName;
								parent.$.modalDialog.handler.find("#xmmc").searchbox('setValue',proName);
								parent.$.modalDialog.handler.find("#projectid").val(projectid);
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
		buttons : [
		{
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				
				submitForm(urls.addCommitUrl,"httxAddForm","");
			}
		},{
			
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} 
		]
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
	var htzxksrq = mdDialog.find('#htzxksrq').datebox('getValue');
	var htzxjsrq =  mdDialog.find('#htzxjsrq').datebox('getValue');
	
	if(htzxjsrq < htzxksrq && htzxjsrq != "")
	{
		parent.$.messager.alert('提示', "合同执行结束日期应大于开始日期！", 'warnning');
		return;
	}
	
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
			//转换对象
			result = $.parseJSON(result);
			if (result.success) {
				easyui_auto_notice(result, function() {
					httxDataGrid.datagrid('reload');
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
function httxEdit(){
	var selectRow = httxDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if(selectRow[0].sfzj == 0){
		parent.$.modalDialog({
			title : "合同修改",
			iconCls : 'icon-edit',
			width : 900,
			height : 430,
			href : urls.addUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				comboboxFuncByCondFilter(menuid,"htlb", "PROHTLB", "code", "name", mdDialog);//合同类别
				comboboxFuncByCondFilter(menuid,"htzt", "PROHTZT", "code", "name", mdDialog);//合同状态
				//项目关联
				mdDialog.find("#xmmc").searchbox({
					editable : false,
					onChange: function(nv, ov) {
						var icon = mdDialog.find("#xmmc").searchbox("textbox").prev("span.textbox-addon").find("a:first");
						console.log(icon);
						if (nv) {
							icon.css('visibility', 'visible');
						} else {
							icon.css('visibility', 'hidden');
						}
					},
					searcher : function(value,name){
						parent.$.modalDialog2({
							title : "项目选择",
							iconCls : 'icon-add',
							width : 900,
							height : 430,
							href : urls.projectUrl,
							queryParams : {
								
							},
							onLoad : function() {
								
							},
							buttons : [ {
								text : "确定",
								iconCls : "icon-save",
								handler : function() {
									var selectRow = parent.$.modalDialog.handler2.find("#httxProjectDataGrid").datagrid('getChecked');
									if(selectRow==null || selectRow.length==0||selectRow.length>1){
										easyui_warn("请选择一条数据！",null);
										return;
									}
									var projectid = selectRow[0].projectid;
									var proName = selectRow[0].proName;
									parent.$.modalDialog.handler.find("#xmmc").searchbox('setValue',proName);
									parent.$.modalDialog.handler.find("#projectid").val(projectid);
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
				
				var row = httxDataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#httxAddForm');
				f.form("load", row);
				
				//加载附件
				showFileDiv(mdDialog.find("#htfjtd"),true, row.htfj, "30","htfj");
				
			},
			buttons : [ {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
				
					submitForm(urls.editCommitUrl,"httxAddForm","");
				}
			},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	} else {//追加合同修改
		parent.$.modalDialog({
			title : "追加合同修改",
			iconCls : 'icon-edit',
			width : 900,
			height : 430,
			href : urls.appendUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var row = httxDataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#httxAppendForm');
				f.form("load", row);
				
				//加载附件
				showFileDiv(mdDialog.find("#htfjtd"),true, row.htfj, "30","htfj");
				
			},
			buttons : [ {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
				
					submitForm(urls.editCommitUrl,"httxAppendForm","");
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
}

/**
 * 合同追加
 */
function httxAppend(){
	var selectRow = httxDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	
	parent.$.modalDialog({
		title : "合同追加",
		iconCls : 'icon-add',
		width : 900,
		height : 430,
		href : urls.appendUrl,
		onLoad : function() {
			var row = httxDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#httxAppendForm');
			f.form("load", row);
			if (row.sfzj == 0){
				f.find("#sjhttxid").val(row.httxid);
			} else {
				f.find("#sjhttxid").val(row.sjhttxid);
			}
			f.find("#htfj").val("");
			f.find("#httxid").val("");
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
			
				submitForm(urls.appendCommitUrl,"httxAppendForm","");
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
 * 详情
 * @return
 */
function httxView(){
	var selectRow = httxDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "合同详情",
		iconCls : 'icon-view',
		width : 900,
		height : 430,
		href : urls.detailUrl,
		onLoad : function() {
			var row = httxDataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#httxDetailForm');
			f.form("load", row);
			
			//加载附件
			showFileDiv(parent.$.modalDialog.handler.find("#htfjtd"),false, row.htfj, "30","htfj");
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
