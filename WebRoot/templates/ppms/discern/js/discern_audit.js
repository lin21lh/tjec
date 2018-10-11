var baseUrl = contextpath + "ppms/discern/ProjectDiscernController/";
var urls = {
	queryUrl : baseUrl + "queryProject.do?audit=1",
	projectAuditUrl : baseUrl + "auditEntry.do",
	productQueryUrl : baseUrl + "queryProduct.do",
	auditCommitUrl : baseUrl + "auditCommit.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	thirdOrgQueryUrl : baseUrl + "queryThirdOrg.do",
	financeQueryUrl : baseUrl + "queryFinance.do",
	queryApprove : baseUrl + "queryApprove.do",
	auditOpinion : baseUrl + "auditOpinion.do",
	auditOperate : baseUrl + "auditOperate.do",
	projectDetailUrl : baseUrl + "projectSpDetail.do"
};
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
	loadProjectGrid(urls.queryUrl);
});
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
			firstNode : firstNode,
			lastNode : lastNode
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [  {field : "projectid",checkbox : true}  
		              ,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
		              ,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
		              ,{field : "amount",title : "项目总投资（万元）",halign : 'center',align:'right',width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
		              ,{field : "statusName",title : "项目状态",halign : 'center',	width:80,sortable:true}
		              ,{field : "proYear",title : "合作年限",halign : 'center',align:'left',	width:70,sortable:true	}
		              ,{field : "proTradeName",title : "所属行业",halign : 'center',	width:100	,sortable:true}
		              ,{field : "proPerateName",title : "运作方式",halign : 'center',	width:100,sortable:true	}
		              ,{field : "proReturnName",title : "回报机制",halign : 'center',	width:120,sortable:true}
		              ,{field : "proSendtime",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
		              ,{field : "proSendtypeName",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
		              ,{field : "proSendperson",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
		              ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
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
		             ] ]
	});
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
			firstNode : firstNode,
			lastNode : lastNode
		};
	projectDataGrid.datagrid("load", param);
}
/**
 * 审核
 */
function pushWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目筛选",
		iconCls : 'icon-edit',
		width : 900,
		height : 630,
		href : urls.projectAuditUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#projectAuditForm');
			//物有所值验证结果
			comboboxFuncByCondFilter(menuid,"vomResult", "JUDGERESULT", "code", "name", mdDialog);
			//财政承受能力验证结果
			comboboxFuncByCondFilter(menuid,"fcResult", "JUDGERESULT", "code", "name", mdDialog);
			comboboxFuncByCondFilter(menuid,"czResult", "APPROVERESULT", "code", "name", mdDialog);
			f.form("load", row);
			//加载附件
			showFileDiv(mdDialog.find("#ssfa"),false, row.proSchemepath, "30","proSchemepath");
			showFileDiv(mdDialog.find("#kxxyj"),false, row.proReportpath, "30","proReportpath");
			showFileDiv(mdDialog.find("#hpbg"),false, row.proConditionpath, "30","proConditionpath");
			showFileDiv(mdDialog.find("#xmccw"),false, row.proArticlepath, "30","proArticlepath");
			//项目产出物列表
			datagrid = productGrid(mdDialog, row.projectid);
			//第三方机构列表
			thirdOrganGrid(mdDialog, row.projectid);
			//财政预算支出列表
			financeGrid(mdDialog, row.projectid);
			//默认选中第二个页签
			mdDialog.find('#tabList').tabs('select', '物有所值验证');
			//加载识别过程信息
			$.post(urls.queryApprove, {
				projectid : row.projectid
			}, function(result) {
				var r = result.body.approve;
				if(r!=""){
					f.form("load", r);
					showFileDiv(mdDialog.find("#wyszpjbg"),true, r.vomAttachment, "30","vomAttachment");
					showFileDiv(mdDialog.find("#czcsnlbg"),true, r.fcAttachment, "50","fcAttachment");
					showFileDiv(mdDialog.find("#zfAttachment"),true, r.zfAttachment, "50","zfAttachment");
				}
			}, "json");
			mdDialog.find("#vomNetcost").numberbox({
			    "onChange":function(){
			    	calculate(mdDialog);
			    }
			  });
			mdDialog.find("#vomAdjust").numberbox({
				"onChange":function(){
					calculate(mdDialog);
				}
			});
			mdDialog.find("#vomRiskcost").numberbox({
				"onChange":function(){
					calculate(mdDialog);
			    }
			});
			mdDialog.find("#vomPpp").numberbox({
				"onChange":function(){
					calculate(mdDialog);
			    }
			});
			
			//计算
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				//第三方评审机构
				var thirdGrid = mdDialog.find("#thirdOrganGrid");
				var thirdData = thirdGrid.datagrid("getData");
				for(var i=0;i<thirdData.total;i++){
					thirdGrid.datagrid('endEdit', i);//把所有的编辑行锁定
					var flag = thirdGrid.datagrid('validateRow', i);//判断该行数据是否通过验证
					if(!flag){
						parent.$.messager.alert('系统提示', "第三方评审机构列表中第"+(i+1)+"行数据必须全部填写完成！", 'info');
						return;
					}
				}
				var thirdRows = thirdGrid.datagrid('getRows');
				parent.$.modalDialog.handler.find("#thirdGridList").val(JSON.stringify(thirdRows));
				//财政预算支出
				var financeGrid = mdDialog.find("#financeGrid");
				var financeData = financeGrid.datagrid("getData");
				for(var i=0;i<financeData.total;i++){
					financeGrid.datagrid('endEdit', i);//把所有的编辑行锁定
					var flag = financeGrid.datagrid('validateRow', i);//判断该行数据是否通过验证
					if(!flag){
						parent.$.messager.alert('系统提示', "财政预算支出列表中第"+(i+1)+"行数据必须全部填写完成！", 'info');
						return;
					}
				}
				var financeRows = financeGrid.datagrid('getRows');
				parent.$.modalDialog.handler.find("#financeGridList").val(JSON.stringify(financeRows));
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#activityId").val(activityId);
				submitForm(urls.auditCommitUrl,"projectAuditForm","");
			}
		}, {
			text : "审批",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				/*//第三方评审机构
				var thirdGrid = mdDialog.find("#thirdOrganGrid");
				var thirdData = thirdGrid.datagrid("getData");
				for(var i=0;i<thirdData.total;i++){
					thirdGrid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				//财政预算支出
				var financeGrid = mdDialog.find("#financeGrid");
				var financeData = financeGrid.datagrid("getData");
				for(var i=0;i<financeData.total;i++){
					financeGrid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var thirdRows = thirdGrid.datagrid('getRows');
				var financeRows = financeGrid.datagrid('getRows');
				parent.$.modalDialog.handler.find("#thirdGridList").val(JSON.stringify(thirdRows));
				parent.$.modalDialog.handler.find("#financeGridList").val(JSON.stringify(financeRows));*/
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#activityId").val(activityId);
				submitForm(urls.auditCommitUrl,"projectAuditForm","1");
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
//加载产出物列表
function productGrid(mdDialog,projectid){
	var grid = mdDialog.find("#projectAddGrid").datagrid({
		title: '年度产出计划',
        height: 130,
        width: 883,
        collapsible: false,
        url : urls.productQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'productid',
        columns: [[//显示的列
                   { field: 'year', title: '年度', width: 100,
                       editor: { type: 'text', options: {} }
                   },{ field: 'output', title: '产出物', width: 200,
                        editor: { type: 'text', options: {} }
                   },{ field: 'unit', title: '计量单位', width: 100,
                         editor: { type: 'text', options: {} }
                   },{ field: 'amount', title: '数量', width: 100,
                         editor: { type: 'text', options: {} }
                   },{ field: 'remark', title: '备注', width: 200,
                         editor: { type: 'text', options: {} }
                   }]]
    });
	return grid;
}

//第三方机构列表
function thirdOrganGrid(mdDialog,projectid){
	var grid = mdDialog.find("#thirdOrganGrid").datagrid({
        height: 240,
        width: 883,
        title: '第三方机构',
        collapsible: false,
        url : urls.thirdOrgQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'approrganid',
        columns: [[//显示的列
                   {field: 'approrganid', title: '序号', width: 100,  checkbox: true },
                   { field: 'organ_code', title: '机构代码', width: 130,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入机构代码',validType:{length:[0,50]}} }
                   },{ field: 'organ_name', title: '机构名称', width: 180,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入机构名称',validType:{length:[0,50]}} }
                   },{ field: 'consignor', title: '委托方', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入委托方',validType:{length:[0,50]}} }
                   },{ field: 'project_manager', title: '项目经理', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入项目经理',validType:{length:[0,50]}} }
                   },{ field: 'phone', title: '联系电话', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入联系电话',validType:{length:[0,25]}} }
                   },{ field: 'mobile', title: '手机号码', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,validType :'phonesIsRight'} }
                   },{ field: 'content', title: '主要服务内容', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: {required: true,validType:{length:[0,500]}} }
                   },{ field: 'entrust_time', title: '委托时间', width: 100,halign : 'center',
                         editor: { type: 'datebox', options: {required: true,editable:false,validType:{length:[0,20]}} }
                   }]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
            	var data = mdDialog.find("#thirdOrganGrid").datagrid("getData");//grid列表
            	var total = data.total;//grid的总条数
            	mdDialog.find("#thirdOrganGrid").datagrid('appendRow', {row: {}});//追加一行
            	grid.datagrid("beginEdit", total);//开启编辑
            }
        },'-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                var row = mdDialog.find("#thirdOrganGrid").datagrid('getChecked');
                if(row ==''){
                	easyui_warn("请选择要删除的数据！");
                	return;
                }else{
                	parent.$.messager.confirm("提示", "你确定要删除吗?", function (r) {
                         if (r) {
                        	 var index = mdDialog.find("#thirdOrganGrid").datagrid('getRowIndex',row[0]);
                        	 //选择要删除的行
                        	 mdDialog.find("#thirdOrganGrid").datagrid('deleteRow',index);
                         }
                     });
                }
             }
        }],
        onDblClickRow:function (rowIndex, rowData) {
        	mdDialog.find("#thirdOrganGrid").datagrid('beginEdit', rowIndex);
        },
        onClickRow:function(rowIndex,rowData){
        	mdDialog.find("#thirdOrganGrid").datagrid('beginEdit', rowIndex);
        }
    });
	return grid;
}
//第三方机构列表
function thirdOrganGridDetail(mdDialog,projectid){
	var grid = mdDialog.find("#thirdOrganGrid").datagrid({
        height: 240,
        width: 883,
        title: '第三方评审机构',
        collapsible: false,
        url : urls.thirdOrgQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'approrganid',
        columns: [[//显示的列
                   { field: 'organ_code', title: '机构代码', width: 130,halign : 'center',
                       editor: { type: 'validatebox', options: {} }
                   },{ field: 'organ_name', title: '机构名称', width: 200,halign : 'center',
                        editor: { type: 'validatebox', options: {} }
                   },{ field: 'consignor', title: '委托方', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: {} }
                   },{ field: 'project_manager', title: '项目经理', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: {} }
                   },{ field: 'phone', title: '联系电话', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: {} }
                   },{ field: 'mobile', title: '手机号码', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: {} }
                   },{ field: 'content', title: '主要服务内容', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: {} }
                   },{ field: 'entrust_time', title: '委托时间', width: 100,halign : 'center',
                         editor: { type: 'datebox', options: {} }
                   }]]
    });
	return grid;
}
var currentRow = -1;
//财政支出
function financeGrid(mdDialog,projectid){
	var grid = mdDialog.find("#financeGrid").datagrid({
        height: 230,
        width: 883,
        title: '财政预算支出',
        collapsible: false,
        url : urls.financeQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        showFooter : true,
        idField: 'budgetid',
        columns: [[//显示的列
                   {field: 'budgetid', title: '序号', width: 100, halign : 'center', checkbox: true },
                   { field: 'budget_year', title: '支出年度', width: 100,
                       editor: { type: 'numberspinner', options: {required: true,missingMessage:'请输入支出年度',min:2000,max:2099,editable:true}}
                   },{ field: 'budget_gqtz', title: '股权投资支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){
                	   return value==undefined?"":Number(value).toFixed(2);},
                        editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入股权投资支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_yybt', title: '运营补贴支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入运营补贴支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_fxcd', title: '风险承担支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入风险承担支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_pttr', title: '配套投入支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入配套投入支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'total', title: '合计', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {editable:false} }
                   }]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
            	var data = mdDialog.find("#financeGrid").datagrid("getData");//grid列表
            	var total = data.total;//grid的总条数
            	
        	    if(total!=0){
           		   if(mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
           			   	mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
           				mdDialog.find("#financeGrid").datagrid('appendRow', {row: {}});//追加一行
                      	mdDialog.find("#financeGrid").datagrid("beginEdit", total);//开启编辑
                      	currentRow = mdDialog.find('#financeGrid').datagrid('getRows').length-1;
                      	isEditFlag = true;
           		   }else{
           			   easyui_warn("当前存在正在编辑的行！");
           		   }
           	   }else{
                      	mdDialog.find("#financeGrid").datagrid('appendRow', {row: {}});//追加一行
                      	mdDialog.find("#financeGrid").datagrid("beginEdit", total);//开启编辑
                      	currentRow = mdDialog.find('#financeGrid').datagrid('getRows').length-1;
                      	isEditFlag = true;
           	   }
            }
        },'-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
            	var rowIndex = rowIndex= mdDialog.find("#financeGrid").datagrid('getRowIndex',row[0]);
            	
            	if(rowIndex == currentRow){
        			isEditFlag = false;
        		}
           		if(isEditFlag ==false){
           			parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
	                     if (r) {
	                    	mdDialog.find("#financeGrid").datagrid('deleteRow',rowIndex);
	                		calcMoney(mdDialog);
	                     }
                    });
           			
           		}else{
           			if(mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
           				mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
           				parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
      	                     if (r) {
      	                    	mdDialog.find("#financeGrid").datagrid('deleteRow',rowIndex);
      	                		calcMoney(mdDialog);
      	                     }
                           });
               			calcMoney(mdDialog);
           			}else{
           				easyui_warn("当前存在正在编辑的行！");
           			}
           		}
             }
        },'-',{text:'单位：万元'}],
        onDblClickRow:function (rowIndex, rowData) {
           //mdDialog.find("#financeGrid").datagrid('beginEdit', rowIndex);
        	
           //结束上一行编辑，开启新行的编辑
     	   if(currentRow != rowIndex && mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
     		   //结束上一行编辑
     		   mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
     		   //开启新行编辑
     		   mdDialog.find("#financeGrid").datagrid('beginEdit', rowIndex);
     		   currentRow = rowIndex;
     		   isEditFlag = true;
     	   }else{
     		  easyui_warn("当前存在正在编辑的行！");
     	   }
        },
        onClickRow:function(rowIndex,rowData){
    	    if( mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
      		   //结束上一行编辑
      		   mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
      		   currentRow = -1;
      		   isEditFlag = false;
      	    }
    	   
        },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
     	   calcMoney(mdDialog);
        },onBeginEdit:function(rowIndex, rowData){
        	
        	mdDialog.find("#financeGrid").datagrid('beginEdit', rowIndex);
    	    var objGrid = mdDialog.find("#financeGrid");     
    	    var budget_gqtz = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_gqtz'}); 
    	    var budget_yybt = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_yybt'}); 
    	    var budget_fxcd = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_fxcd'}); 
    	    var budget_pttr = objGrid.datagrid('getEditor', {index:rowIndex,field:'budget_pttr'}); 
    	    var total = objGrid.datagrid('getEditor', {index:rowIndex,field:'total'}); 
    	    mdDialog.find(budget_gqtz.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
    	    mdDialog.find(budget_yybt.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
    	    mdDialog.find(budget_fxcd.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
    	    mdDialog.find(budget_pttr.target).numberbox({
			    "onChange":function(){
		    	 var budget_gqtz_value = parseFloat(mdDialog.find(budget_gqtz.target).val());
		    	 var budget_yybt_value = parseFloat(mdDialog.find(budget_yybt.target).val());
		    	 var budget_fxcd_value = parseFloat(mdDialog.find(budget_fxcd.target).val());
		    	 var budget_pttr_value = parseFloat(mdDialog.find(budget_pttr.target).val());
		    	 mdDialog.find(total.target).numberbox("setValue", hj(budget_gqtz_value,budget_yybt_value,budget_fxcd_value,budget_pttr_value));
			    }
			  });
        }
    });
	return grid;
}
function hj(budget_gqtz,budget_yybt,budget_fxcd,budget_pttr){
	if (isNaN(budget_gqtz)) {  
		budget_gqtz =0;    
	} 
	if (isNaN(budget_yybt)) {  
		budget_yybt =0;    
	} 
	if (isNaN(budget_fxcd)) {  
		budget_fxcd =0;    
	} 
	if (isNaN(budget_pttr)) {  
		budget_pttr =0;    
	} 
	return budget_gqtz+budget_yybt+budget_fxcd+budget_pttr;
}
var count =0;
//计算
function calcMoney(mdDialog)
{
	var datagrid = mdDialog.find('#financeGrid')
	var rowsnum =  datagrid.datagrid('getRows');
	var gptzsum = 0;
	var yybtsum = 0;
	var fxcdsum = 0;
	var pttrsum = 0;
	var sum = 0;
	  for(var i=0;i<rowsnum.length;i++){
		  //股权投资支出额
		  if(rowsnum[i].budget_gqtz == undefined || rowsnum[i].budget_gqtz == null || rowsnum[i].budget_gqtz ==''){
			  gptzsum +=0;
		  }else{
			  gptzsum +=Number(rowsnum[i].budget_gqtz);
		  }
		  
		  //运营补贴支出额
		  if(rowsnum[i].budget_yybt == undefined || rowsnum[i].budget_yybt == null || rowsnum[i].budget_yybt ==''){
			  yybtsum +=0;
		  }else{
			  yybtsum +=Number(rowsnum[i].budget_yybt);
		  }
		  
		  //风险承担支出额
		  if(rowsnum[i].budget_fxcd == undefined || rowsnum[i].budget_fxcd == null || rowsnum[i].budget_fxcd ==''){
			  fxcdsum +=0;
		  }else{
			  fxcdsum +=Number(rowsnum[i].budget_fxcd);
		  }
		  
		  //配套投入支出额
		  if(rowsnum[i].budget_pttr == undefined || rowsnum[i].budget_pttr == null || rowsnum[i].budget_pttr ==''){
			  pttrsum +=0;
		  }else{
			  pttrsum +=Number(rowsnum[i].budget_pttr);
		  }
		  
		//总计
		  if(rowsnum[i].total == undefined || rowsnum[i].total == null || rowsnum[i].total ==''){
			  sum +=0;
		  }else{
			  sum +=Number(rowsnum[i].total);
		  }
		  
	  }
	  var rows = datagrid.datagrid('getFooterRows');
	  rows[0]['budget_gqtz'] = gptzsum;
	  rows[0]['budget_yybt'] = yybtsum;
	  rows[0]['budget_fxcd'] = fxcdsum;
	  rows[0]['budget_pttr'] = pttrsum;
	  rows[0]['total'] = sum;
	  datagrid.datagrid('reloadFooter');
}

//财政支出
function financeGridDetail(mdDialog,projectid){
	var grid = mdDialog.find("#financeGrid").datagrid({
        height: 230,
        width: 883,
        title: '财政预算支出',
        collapsible: false,
        url : urls.financeQueryUrl,
        queryParams : {projectid:projectid},
        singleSelect: true,
        rownumbers : true,
        idField: 'budgetid',
        columns: [[//显示的列
                   { field: 'budget_year', title: '支出年度', width: 100,halign : 'center',
                       editor: { type: 'numberspinner', options: {}}
                   },{ field: 'budget_gqtz', title: '股权投资支出额', width: 140,halign : 'center',
                        editor: { type: 'numberbox', options: {} }
                   },{ field: 'budget_yybt', title: '运营补贴支出额', width: 140,halign : 'center',
                         editor: { type: 'numberbox', options: {} }
                   },{ field: 'budget_fxcd', title: '风险承担支出额', width: 140,halign : 'center',
                         editor: { type: 'numberbox', options: {} }
                   },{ field: 'budget_pttr', title: '配套投入支出额', width: 140,halign : 'center',
                         editor: { type: 'numberbox', options: {} }
                   },{ field: 'total', title: '合计', width: 140,halign : 'center',
                         editor: { type: 'numberbox', options: {} }
                   }]],
        toolbar: [{text:'单位：万元'}]
    });
	return grid;
}
//新增提交表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	if(workflowflag=="1"){//审批
		var mdDialog = parent.$.modalDialog.handler;
		//第三方评审机构
		var thirdGrid = mdDialog.find("#thirdOrganGrid");
		var thirdData = thirdGrid.datagrid("getData");
		//财政预算支出
		var financeGrid = mdDialog.find("#financeGrid");
		var financeData = financeGrid.datagrid("getData");
		if(thirdData.total<1){
			parent.$.messager.alert('系统提示', "请录入第三方评审机构信息！", 'info');
			mdDialog.find('#tabList').tabs('select', '物有所值验证');
			return;
		}
		if(financeData.total<1){
			parent.$.messager.alert('系统提示', "请录入财政预算支出信息！", 'info');
			mdDialog.find('#tabList').tabs('select', '财政承受能力验证');
			return;
		}
		for(var i=0;i<thirdData.total;i++){
			thirdGrid.datagrid('beginEdit', i);//打开编辑
		}
		for(var i=0;i<financeData.total;i++){
			financeGrid.datagrid('beginEdit', i);//打开编辑
		}
		var isValid = form.form('validate');
		if (!isValid) {
			easyui_warn("物有所值验证、财政承受能力验证、财政审批意见有必填内容未填写！",null);
			return;
		}
		for(var i=0;i<thirdData.total;i++){
			thirdGrid.datagrid('endEdit', i);//关闭编辑
		}
		for(var i=0;i<financeData.total;i++){
			financeGrid.datagrid('endEdit', i);//关闭编辑
		}
		var thirdRows = thirdGrid.datagrid('getRows');
		var financeRows = financeGrid.datagrid('getRows');
		parent.$.modalDialog.handler.find("#thirdGridList").val(JSON.stringify(thirdRows));
		parent.$.modalDialog.handler.find("#financeGridList").val(JSON.stringify(financeRows));
		var isValid = form.form('validate');
		parent.$.messager.confirm("审批确认", "确认审批同意？", function(r) {
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
				return true;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					projectDataGrid.datagrid('reload');
					easyui_info("保存成功！");
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
				}
			});
	}
}
/**
 * 计算
 * @param mdDialog
 */
function calculate(mdDialog){
	var vomNetcost =parseFloat(mdDialog.find("#vomNetcost").numberbox('getValue'));
	var vomAdjust =parseFloat(mdDialog.find("#vomAdjust").numberbox('getValue'));
	var vomRiskcost =parseFloat(mdDialog.find("#vomRiskcost").numberbox('getValue'));
	var vomPpp =parseFloat(mdDialog.find("#vomPpp").numberbox('getValue'));
	if (isNaN(vomNetcost)) {    
		vomNetcost =0;    
    }   
	if (isNaN(vomAdjust)) {    
		vomAdjust =0;    
    }  
	if (isNaN(vomRiskcost)) {    
		vomRiskcost =0;    
    }  
	if (isNaN(vomPpp)) {  
		vomPpp =0;    
	}  
	var vomPsc = vomNetcost+vomAdjust+vomRiskcost;
	mdDialog.find('#vomPsc').numberbox('setValue',vomPsc);
	var vomVfm  = vomPsc - vomPpp;
	mdDialog.find('#vomVfm').numberbox('setValue',vomVfm);
}
function pushBackWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var wfid = row.wfid;
	parent.$.modalDialog({
		title : "退回处理",
		width : 450,
		height : 210,
		iconCls : 'icon-edit',
		href : urls.auditOpinion,
		buttons : [{text : "退回",
						iconCls : "icon-save",
						handler : function() {
							var opinion = parent.$.modalDialog.handler.find('#opinion').val();
							if (opinion == null || opinion == "") {
								return;
							}
							var form = parent.$.modalDialog.handler.find('#revoke_audit');
							var isValid = form.form('validate');
							if (!isValid) {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认将该项目退回？", function(r) {
								if(r){
									$.post(urls.auditOperate, {
										menuid : menuid,
										activityId : activityId,
										wfid : wfid,
										opinion : opinion
									}, function(result) {
										easyui_auto_notice(result, function() {
											projectDataGrid.datagrid('reload');
										});
									}, "json");
									parent.$.modalDialog.handler.dialog('close');
								}
							});
						}
					}, {
						text : "关闭",
						iconCls : "icon-cancel",
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					}]
	});
}
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
	var row = projectDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "项目详情",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.projectDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			var f = parent.$.modalDialog.handler.find('#projectAuditForm');
			//物有所值验证结果
			comboboxFuncByCondFilter(menuid,"vomResult", "JUDGERESULT", "code", "name", mdDialog);
			//财政承受能力验证结果
			comboboxFuncByCondFilter(menuid,"fcResult", "JUDGERESULT", "code", "name", mdDialog);
			comboboxFuncByCondFilter(menuid,"czResult", "APPROVERESULT", "code", "name", mdDialog);
			f.form("load", row);
			//加载附件
			showFileDiv(mdDialog.find("#ssfa"),false, row.proSchemepath, "30","proSchemepath");
			showFileDiv(mdDialog.find("#kxxyj"),false, row.proReportpath, "30","proReportpath");
			showFileDiv(mdDialog.find("#hpbg"),false, row.proConditionpath, "30","proConditionpath");
			showFileDiv(mdDialog.find("#xmccw"),false, row.proArticlepath, "30","proArticlepath");
			//项目产出物列表
			datagrid = productGrid(mdDialog, row.projectid);
			//第三方机构列表
			thirdOrganGridDetail(mdDialog, row.projectid);
			//财政预算支出列表
			financeGridDetail(mdDialog, row.projectid);
			//加载识别过程信息
			$.post(urls.queryApprove, {
				projectid : row.projectid
			}, function(result) {
				var r = result.body.approve;
				if(r!=""){
					f.form("load", r);
					showFileDiv(mdDialog.find("#wyszpjbg"),false, r.vomAttachment, "26","vomAttachment");
					showFileDiv(mdDialog.find("#czcsnlbg"),false, r.fcAttachment, "50","fcAttachment");
					showFileDiv(mdDialog.find("#zfshgw"),false, r.zfAttachment, "50","zfAttachment");
				}
			}, "json");
			//计算
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
        height: 150,
        width: 880,
        collapsible: true,
        url : urls.productQueryUrl,
        queryParams : {projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'productid',
        columns: [[//显示的列
                   { field: 'year', title: '年度', width: 100, sortable: true,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'output', title: '产出物', width: 200,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'unit', title: '计量单位', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'amount', title: '数量', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'remark', title: '备注', width: 200,halign : 'center',
                         editor: { type: 'text', options: { required: true} }
                   }]]
    });
	return grid;
}