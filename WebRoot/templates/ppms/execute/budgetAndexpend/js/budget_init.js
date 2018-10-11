
var baseUrl = contextpath + "ppms/execute/controller/ProjectBudgetAndExpendController/";
var urls = {
	queryUrl : baseUrl + "queryProject.do",
	productQueryUrl : baseUrl + "queryProduct.do",
	budgetAddUrl : baseUrl + "budgetAdd.do",
	budgetQueryUrl : baseUrl + "budgetQuery.do",
	budgetSaveUrl : baseUrl + "budgetSave.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#changeAccountDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
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
			status : 1
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "projectid",checkbox : true}  
			        ,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
			        ,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
			        ,{field : "amount",title : "项目总投资（万元）",halign : 'center',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
			        ,{field : "proYear",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
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
			       ] ]
	});
}
/**
 * 新增按钮
 */
function add(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if(selectRow[0].xmdqhj == '5'){
		easyui_warn("此数据已移交，不可录入！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var projectid = row.projectid;
	var budgetTitle = budgetType=="1" ? "预算录入" : "支出录入";
	var modalTitle = budgetType=="1" ? "项目预算" : "项目支出";
	parent.$.modalDialog({
		title : modalTitle,
		iconCls : 'icon-add',
		width : 900,
		height : 500,
		href : urls.budgetAddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			budgetGrid(mdDialog, projectid,budgetType,budgetTitle);
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#budgetAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#budgetGrid").val(rowstr);
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(projectid);
				parent.$.modalDialog.handler.find("#budgetType").val(budgetType);
				submitForm(urls.budgetSaveUrl,"budgetAddForm","");
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
	var mdDialog = parent.$.modalDialog.handler;
	var grid = mdDialog.find("#budgetAddGrid");
	var data = grid.datagrid("getData");
	var total = data.total;
	var modalTitle = budgetType=="1" ? "项目预算" : "项目支出";
	if(total<1){
		parent.$.messager.alert('系统提示', "请录入"+modalTitle+"！", 'info');
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
 * 项目查询
 */
function topQuery(){
	var param = {
			proName : $("#proName").val(),
			proPerson : $("#proPerson").val(),
			proTrade :  $("#proTrade").val(),
			proPerate : $("#proPerate").combobox('getValues').join(","),
			proReturn : $("#proReturn").combobox('getValues').join(","),
			proSendtype : $("#proSendtype").combobox('getValues').join(","),
			proType : $("#proType").combobox('getValues').join(","),
			menuid : menuid
		};
	projectDataGrid.datagrid("load", param);
}
/**
 * 修改
 */
function edit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	if(selectRow[0].xmdqhj == '5'){
		easyui_warn("此数据已移交，不可录入！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var projectid = row.projectid;
	var budgetTitle = budgetType=="1" ? "预算录入修改" : "支出录入修改";
	var modalTitle = budgetType=="1" ? "项目预算" : "项目支出";
	parent.$.modalDialog({
		title : modalTitle,
		iconCls : 'icon-add',
		width : 900,
		height : 500,
		href : urls.budgetAddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			budgetGrid(mdDialog, projectid,budgetType,budgetTitle);
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#budgetAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#budgetGrid").val(rowstr);
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(projectid);
				parent.$.modalDialog.handler.find("#budgetType").val(budgetType);
				submitForm(urls.budgetSaveUrl,"budgetAddForm","");
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
 * 预算和支出列表
 * @param mdDialog
 * @param projectid
 * @param budgetType
 * @returns
 */
var isEditFlag = false;
var currentRow = -1;
function budgetGrid(mdDialog,projectid,budgetType,budgetTitle){
	var grid = mdDialog.find("#budgetAddGrid").datagrid({
        height: 330,
        width: 883,
        title: budgetTitle,
        collapsible: false,
        url : urls.budgetQueryUrl,
        queryParams : {projectid:projectid,budgetType:budgetType},
        singleSelect: true,
        rownumbers : true,
        showFooter : true,//显示合计行
        idField: 'budgetid',
        columns: [[//显示的列
                   {field: 'budgetid', title: '序号', width: 100, halign : 'center', checkbox: true },
                   { field: 'budget_year', title: '支出年度', width: 100,
                       editor: { type: 'numberspinner', options: {required: true,missingMessage:'请输入支出年度',min:2000,max:2099,editable:true}}
                   },{ field: 'budget_gqtz', title: '股权投资支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined || value=='' || value==null?"":Number(value).toFixed(2);},
                        editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入股权投资支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_yybt', title: '运营补贴支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined || value=='' || value==null?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入运营补贴支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_fxcd', title: '风险承担支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined || value=='' || value==null?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入风险承担支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'budget_pttr', title: '配套投入支出额', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined || value=='' || value==null?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入配套投入支出额',min:0,precision:2,max:99999999999999.99} }
                   },{ field: 'total', title: '合计', width: 140,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined || value=='' || value==null?"":Number(value).toFixed(2);},
                         editor: { type: 'numberbox', options: {editable:false} },editable:false
                   }]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
            	var data = mdDialog.find("#budgetAddGrid").datagrid("getData");//grid列表
            	var total = data.total;//grid的总条数
            	if(total!=0){
            		 if(mdDialog.find('#budgetAddGrid').datagrid('validateRow', currentRow)){
         			   	mdDialog.find('#budgetAddGrid').datagrid('endEdit', currentRow);
         				mdDialog.find("#budgetAddGrid").datagrid('appendRow', {row: {}});//追加一行
                        	mdDialog.find("#budgetAddGrid").datagrid("beginEdit", total);//开启编辑
                        	currentRow = mdDialog.find('#budgetAddGrid').datagrid('getRows').length-1;
                        	isEditFlag = true;
         		   }else{
         			   easyui_warn("当前存在正在编辑的行！");
         		   }
            	}else{
            		mdDialog.find("#budgetAddGrid").datagrid('appendRow', {row: {}});//追加一行
                   	mdDialog.find("#budgetAddGrid").datagrid("beginEdit", total);//开启编辑
                   	currentRow = mdDialog.find('#budgetAddGrid').datagrid('getRows').length-1;
                   	isEditFlag = true;
            	}
            }
        },'-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
            	var row = mdDialog.find("#budgetAddGrid").datagrid('getSelections');
                if(row ==''){
                		easyui_warn("请选择要删除的数据！");
                		return;
                }else{
                	var rowIndex = rowIndex= mdDialog.find("#budgetAddGrid").datagrid('getRowIndex',row[0]);
                	
                	if(rowIndex == currentRow){
            			isEditFlag = false;
            		}
               		if(isEditFlag ==false){
               			parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
   	                     if (r) {
   	                    	mdDialog.find("#budgetAddGrid").datagrid('deleteRow',rowIndex);
   	                		calcMoney(mdDialog);
   	                     }
                        });
               			
               		}else{
               			if(mdDialog.find('#budgetAddGrid').datagrid('validateRow', currentRow)){
               				mdDialog.find('#budgetAddGrid').datagrid('endEdit', currentRow);
               				parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
          	                     if (r) {
          	                    	mdDialog.find("#budgetAddGrid").datagrid('deleteRow',rowIndex);
          	                		calcMoney(mdDialog);
          	                     }
                               });
                   			calcMoney(mdDialog);
               			}else{
               				easyui_warn("当前存在正在编辑的行！");
               			}
               		}
               		
                	
                }
             }
        },'-',{text:'单位：万元'}],
        onDblClickRow:function (rowIndex, rowData) {
        	 //结束上一行编辑，开启新行的编辑
     	   if(currentRow != rowIndex && mdDialog.find('#budgetAddGrid').datagrid('validateRow', currentRow)){
     		   //结束上一行编辑
     		   mdDialog.find('#budgetAddGrid').datagrid('endEdit', currentRow);
     		   //开启新行编辑
     			mdDialog.find("#budgetAddGrid").datagrid('beginEdit', rowIndex);
     			currentRow = rowIndex;
     			isEditFlag = true;
     	   }else{
    		   easyui_warn("当前存在正在编辑的行！");
    	   }
        },
        onClickRow:function(rowIndex,rowData){
        	if( mdDialog.find('#budgetAddGrid').datagrid('validateRow', currentRow)){
     		   //结束上一行编辑
     		   mdDialog.find('#budgetAddGrid').datagrid('endEdit', currentRow);
     		   currentRow = -1;
     		   isEditFlag = false;
     	   }
        	
        },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
     	   calcMoney(rowIndex,mdDialog);
        },onBeginEdit:function(rowIndex, rowData){
        	
        	mdDialog.find("#budgetAddGrid").datagrid('beginEdit', rowIndex);
    	    var objGrid = mdDialog.find("#budgetAddGrid");     
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

//计算
function calcMoney(rowIndex,mdDialog)
{
	var datagrid = mdDialog.find('#budgetAddGrid')
	var rowsnum =  datagrid.datagrid('getRows');
	var sum = 0;
	var sum_budget_gqtz = 0;
	var sum_budget_yybt = 0;
	var sum_budget_fxcd = 0;
	var sum_budget_pttr = 0;
	  for(var i=0;i<rowsnum.length;i++){
		  if(rowsnum[i].budget_gqtz == undefined || rowsnum[i].budget_gqtz == null || rowsnum[i].budget_gqtz ==''){
			  sum_budget_gqtz +=0;
		  }else{
			  sum_budget_gqtz +=Number(rowsnum[i].budget_gqtz);
		  }
		  
		  if(rowsnum[i].budget_yybt == undefined || rowsnum[i].budget_yybt == null || rowsnum[i].budget_yybt ==''){
			  sum_budget_yybt +=0;
		  }else{
			  sum_budget_yybt +=Number(rowsnum[i].budget_yybt);
		  }
		  
		  if(rowsnum[i].budget_fxcd == undefined || rowsnum[i].budget_fxcd == null || rowsnum[i].budget_fxcd ==''){
			  sum_budget_fxcd +=0;
		  }else{
			  sum_budget_fxcd +=Number(rowsnum[i].budget_fxcd);
		  }
		  
		  if(rowsnum[i].budget_pttr == undefined || rowsnum[i].budget_pttr == null || rowsnum[i].budget_pttr ==''){
			  sum_budget_pttr +=0;
		  }else{
			  sum_budget_pttr +=Number(rowsnum[i].budget_pttr);
		  }
		  
		  if(rowsnum[i].total == undefined || rowsnum[i].total == null || rowsnum[i].total ==''){
			  sum +=0;
		  }else{
			  sum +=Number(rowsnum[i].total);
		  }
	  }
	  var rows = datagrid.datagrid('getFooterRows');
	  rows[0]['total'] = sum;
	  rows[0]['budget_pttr'] = sum_budget_pttr;
	  rows[0]['budget_fxcd'] = sum_budget_fxcd;
	  rows[0]['budget_yybt'] = sum_budget_yybt;
	  rows[0]['budget_gqtz'] = sum_budget_gqtz;
	  datagrid.datagrid('reloadFooter');
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
//详情
function detail(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = projectDataGrid.datagrid("getSelections")[0];
	var projectid = row.projectid;
	var budgetTitle = budgetType=="1" ? "预算录入详情" : "支出录入详情";
	var modalTitle = budgetType=="1" ? "项目预算" : "项目支出";
	parent.$.modalDialog({
		title : modalTitle,
		iconCls : 'icon-add',
		width : 900,
		height : 500,
		href : urls.budgetAddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			budgetGridDetail(mdDialog, projectid,budgetType,budgetTitle);
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
//详情
function budgetGridDetail(mdDialog,projectid,budgetType,budgetTitle){
	var grid = mdDialog.find("#budgetAddGrid").datagrid({
        height: 330,
        width: 883,
        title: budgetTitle,
        collapsible: false,
        url : urls.budgetQueryUrl,
        queryParams : {projectid:projectid,budgetType:budgetType},
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