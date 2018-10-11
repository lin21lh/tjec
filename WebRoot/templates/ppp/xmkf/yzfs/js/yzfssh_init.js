var baseUrl = contextpath + "ppp/xmkf/controller/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"yzfsshQuery.do"
		,auditUrl : baseUrl+"yzfsshAudit.do"
		,detailUrl : baseUrl+"yzfsshDetail.do"
};
//列表信息
var projectDataGrid;

//默认加载
$(function() {
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
				$('#auditBtn').linkbutton('enable');
				$('#backBtn').linkbutton('enable');
				break;
			case '2':
				$('#auditBtn').linkbutton('disable');
				$('#backBtn').linkbutton('disable');
				break;
			default:
				break;
			}
		}
	});
	comboboxFuncByCondFilter(menuid,"xmlx", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"yzfs", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"hbjz", "PRORETURN", "code", "name");//项目回报机制
	comboboxFuncByCondFilter(menuid,"fqlx", "PROSENDTYPE", "code", "name");//项目发起类型
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
	loadProjectGrid(urls.queryUrl);
});

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
//			menuid : menuid,
//			activityId : activityId,
//			firstNode : true,
//			lastNode : false
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
	     columns : [ [ {field : "xmidProde",checkbox : true}  
		     ,{field : "xmmcProde",title : "项目名称",halign : 'center',width:250,sortable:true}
		     ,{field : "sftyyzfsMc",title : "同意PPP运作",halign : 'center',width:100,sortable:true}
		     ,{field : "spyj",title : "政府审批意见",halign : 'center',width:150,sortable:true}
		     ,{field : "orgnameProde",title : "申报单位",halign : 'center',width:100,sortable:true}
		     ,{field : "xmlxMcProde",title : "项目类型",halign : 'center',	width:80,sortable:true}
		     ,{field : "xmzjeProde",title : "项目总投资（万元）",halign : 'right',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
		     ,{field : "xmztMcProde",title : "项目状态",halign : 'center',	width:80,sortable:true}
		     ,{field : "hznxProde",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
		     ,{field : "sshyMcProde",title : "所属行业",halign : 'center',	width:100	,sortable:true}
		     ,{field : "yzfsMcProde",title : "运作方式",halign : 'center',	width:100,sortable:true	}
		     ,{field : "hbjzMcProde",title : "回报机制",halign : 'center',	width:120,sortable:true}
		     ,{field : "fqsjProde",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
		     ,{field : "fqlxMcProde",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
		     ,{field : "fqrmcProde",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
		     ,{field : "xmlxrProde",title : "项目联系人",halign : 'center',	width:100,sortable:true }
		     ,{field : "sfxmMcProde",title : "示范项目",halign : 'center',	width:100,sortable:true }
		     ,{field : "tjxmMcProde",title : "推介项目",halign : 'center',	width:80,sortable:true }
		     ,{field : "sfsqbtMcProde",title : "申请补贴",halign : 'center',	width:80,sortable:true }
		     ,{field : "btjeProde",title : "补贴金额（万元）",halign : 'right',align:'right'}
		     ,{field : "xmgkProde",title : "项目概况",halign : 'center',	width:150,sortable:true }
		     ,{field : "lxrdhProde",title : "联系人电话",halign : 'center',	width:150,sortable:true}
		     ,{field : "cjrMcProde",title : "创建人",halign : 'center',	width:130,sortable:true,hidden:true	}
		     ,{field : "cjsjProde",title : "创建时间",halign : 'center',	width:130,sortable:true,hidden:true	}
		     ,{field : "xgrMcProde",title : "修改人",halign : 'center',	width:130,sortable:true,hidden:true	}
		     ,{field : "xgsjProde",title : "修改时间",halign : 'center',	width:130,sortable:true,hidden:true	}
		     ,{field : "dqztProde",title : "当前状态",halign : 'center',	width:80,sortable:true,hidden:true}
		     ,{field : "xmlxProde",title : "项目类型",halign : 'center',	width:150,sortable:true,hidden:true	}
		     ,{field : "sshyProde",title : "所属行业",halign : 'center',	width:150,sortable:true,hidden:true	}
		     ,{field : "proPerateProde",title : "运作方式",halign : 'center',	width:150,sortable:true,hidden:true	}
		     ,{field : "hbjzProde",title : "回报机制",halign : 'center',	width:150,sortable:true,hidden:true	}
		     ,{field : "wfidProde",title : "WFID",halign : 'center',	width:150,sortable:true,hidden:true	}
		     ,{field : "sfxmProde",title : "示范项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
		     ,{field : "tjxmProde",title : "推介项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
		     ,{field : "sqbtProde",title : "申请补贴",halign : 'center',	width:100,sortable:true ,hidden:true	}
		     ,{field : "zfzyzcProde",title : "政府资源支出",halign : 'center',	width:100,sortable:true ,hidden:true	}
	    ] ]
	});
}


/*查询*/
function topQuery(){
	var param = {
//			menuid : menuid,
//			activityId : activityId,
//			firstNode : true,
//			lastNode : false,
//			status : $("#status").combobox('getValue'),
			xmmc : $("#xmmc").val(),
			xmlxr : $("#xmlxr").val(),
			sshy :  $("#sshy").val(),
			yzfs : $("#yzfs").combobox('getValues').join(","),
			hbjz : $("#hbjz").combobox('getValues').join(","),
			fqlx : $("#fqlx").combobox('getValues').join(","),
			xmlx : $("#xmlx").combobox('getValues').join(","),
		};
	projectDataGrid.datagrid("load", param);
}
/*审核*/
function yzfsshAudit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目运作审核",
		width : 900,
		height : 630,
		href : urls.auditUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
//			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#yzfsshForm');
			f.form("load", row);
			//备案隐藏域加载内容
			f.find("#xmid").val(f.find("#xmidProde").val());
			//显示附件
			showFileDiv(mdDialog.find("#filetdProde"),false,"XMCB",row.xmidProde,"");
			showFileDiv(mdDialog.find("#filetd"),false,"YZFS",row.xmid,"");
		},
		buttons : [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
//				var mdDialog = parent.$.modalDialog.handler;
//				parent.$.modalDialog.handler.find("#menuid").val(menuid);
//				parent.$.modalDialog.handler.find("#firstNode").val(firstNode);
//				parent.$.modalDialog.handler.find("#lastNode").val(lastNode);
//				parent.$.modalDialog.handler.find("#activityId").val(activityId);
//				submitForm(urls.auditWorkFlow,"projectAuditForm","");
			}
		}, {
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
//				var mdDialog = parent.$.modalDialog.handler;
//				parent.$.modalDialog.handler.find("#menuid").val(menuid);
//				parent.$.modalDialog.handler.find("#firstNode").val(firstNode);
//				parent.$.modalDialog.handler.find("#lastNode").val(lastNode);
//				parent.$.modalDialog.handler.find("#activityId").val(activityId);
//				submitForm(urls.addSaveUrl,"yzfslrForm","");
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

/*详情*/
function yzfsshDetail(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目运作审核",
		width : 900,
		height : 630,
		href : urls.detailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
//			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#yzfslrForm');
			f.form("load", row);
			//备案隐藏域加载内容
			f.find("#xmid").val(f.find("#xmidProde").val());
			//显示附件
			showFileDiv(mdDialog.find("#filetdProde"),false,"XMCB",row.xmidProde,"");
			//已录入备案信息
			showFileDiv(mdDialog.find("#filetd"),false,"YZFS",row.xmid,"");
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


