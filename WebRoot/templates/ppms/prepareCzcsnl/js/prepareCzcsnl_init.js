
var baseUrl = contextpath + "ppms/discern/ProjectPrepareCznlcsController/";
var urls = {
		queryUrl : baseUrl + "queryProject.do?vfmPjhj=1",//d默认条件定量评价=1
		queryIsExistCzcsnl : baseUrl +"queryIsExistCzcsnl.do",
		czcsnlAdd :baseUrl +"czcsnlAdd.do",
		czcsnlSave :baseUrl +"czcsnlSave.do",
		queryCzcsnlForm : baseUrl +"queryCzcsnlForm.do",
		financeQuery : baseUrl +"financeQuery.do",
		sendCzcsnl : baseUrl +"sendCzcsnl.do",
		revokeCzcsnl : baseUrl +"revokeCzcsnl.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#changeAccountDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	$('#backBtn').linkbutton('disable');
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [{text : "待处理", value : "1"}, {text : "已提交", value : "2"}],
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
			status : "1",
			menuid : menuid,
			xmhj :xmhj,
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
			          ,{field : "czResultName",title : "项目状态",halign : 'center',	width:80,sortable:true}
			          ,{field : "vfmDlpjName",title : "是否VFM定量评价",halign : 'center',	width:100,sortable:true}
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
			          ,{field : "vfmDlpj",title : "VFM定量评价",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "pszbid",title : "pszbid",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualVerifytime",title : "定性评估验证完成时间",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualConclusion",title : "定性评估描述",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualDf",title : "定性评价得分",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualResult",title : "定性评估结果",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualPath",title : "定性评估附件",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualResultName",title : "定性评估结果",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "dxpjid",title : "dxpjid",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "dlpjid",title : "dlpjid",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "czcsnlid",title : "czcsnlid",halign : 'center',	width:100,sortable:true ,hidden:true	}
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
			xmhj : xmhj
		};
	projectDataGrid.datagrid("load", param);
}
/**
 * 财政承受能力新增
 */
function czcsnlAdd(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistCzcsnl,
		  async: false,     
		  data: {dlpjid:selectRow[0].dlpjid,xmhj:xmhj,projectid:selectRow[0].projectid}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有做过财政承受能力
		//新增
		parent.$.modalDialog({
			title : "财政承受能力评价",
			width : 900,
			height : 600,
			href : urls.czcsnlAdd,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
				mdDialog.find("#dlpjid").val(selectRow[0].dlpjid);
				mdDialog.find("#czcsnlid").val(selectRow[0].czcsnlid);
				comboboxFuncByCondFilter(menuid,"fcResult", "JUDGERESULT", "code", "name", mdDialog);
				//财政预算支出列表
				financeGrid(mdDialog, selectRow[0].projectid);
			},
			buttons : [ {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("1");//保存未提交
				}
			}, {
				text : "提交",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("2");//提交
				}
			},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	}else{
		czcsnlEdit();
	}
}
var currentRow = -1;
//财政支出
function financeGrid(mdDialog,projectid){
	var grid = mdDialog.find("#financeGrid").datagrid({
      height: 330,
      width: 883,
      title: '财政预算支出',
      collapsible: false,
      url : urls.financeQuery,
      queryParams : {projectid:projectid,budgetType :"1"},
      singleSelect: true,
      rownumbers : true,
      showFooter : true,
      idField: 'budgetid',
      columns: [[//显示的列
                 {field: 'budgetid', title: '序号', width: 100, halign : 'center', checkbox: true },
                 { field: 'budget_year', title: '支出年度', width: 100,
                     editor: { type: 'numberspinner', options: {required: true,missingMessage:'请输入或选择支出年度',min:2010,max:2099,editable:true}}
                 },{ field: 'budget_gqtz', title: '股权投资', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){
              	   return value==undefined?"":Number(value).toFixed(2);},
                      editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入股权投资支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_yybt', title: '运营补贴', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入运营补贴支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_fxcd', title: '风险承担', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入风险承担支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_pttr', title: '配套投入', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入配套投入支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_ybggys', title: '一般公共预算', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入一般公共预算',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'total', title: '合计', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
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
         			   easyui_warn("当前行正在编辑！");
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
	                		//calcMoney(mdDialog);
	                     }
                  });
         		}else{
         			if(mdDialog.find('#financeGrid').datagrid('validateRow', currentRow)){
         				mdDialog.find('#financeGrid').datagrid('endEdit', currentRow);
         				parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
    	                     if (r) {
    	                    	mdDialog.find("#financeGrid").datagrid('deleteRow',rowIndex);
    	                		//calcMoney(mdDialog);
    	                     }
                         });
             			//calcMoney(mdDialog);
         			}else{
         				easyui_warn("当前行正在编辑！");
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
   	   //calcMoney(mdDialog);
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
/**
 * 行合计
 * @param budget_gqtz
 * @param budget_yybt
 * @param budget_fxcd
 * @param budget_pttr
 * @returns {Number}
 */
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
/**
 * 提交
 */
function forSubmit(status,flag){
	parent.$.modalDialog.handler.find("#status").val(status);
	parent.$.modalDialog.handler.find("#xmhj").val(xmhj);
	parent.$.modalDialog.handler.find("#updateFlag").val(flag);
	var form = parent.$.modalDialog.handler.find('#prepareAddForm');
	var isValid = form.form('validate');
	if (!isValid) {//验证表单
		return;
	}
	var mdDialog = parent.$.modalDialog.handler;
	//评价指标
	var financeGrid = mdDialog.find("#financeGrid");
	var financeData = financeGrid.datagrid("getData");
	var isPass = true;
	for(var i=0;i<financeData.total;i++){
		//打开行编辑、验证是否通过验证
		financeGrid.datagrid('beginEdit', i);
		isPass = financeGrid.datagrid('validateRow', i);
		financeGrid.datagrid('endEdit', i);//把所有的编辑行锁定
		if(!isPass){
			break;
		}
	}
	//验证评分是否都已经填写完成
	if(!isPass){
		return;
	}
	var financeRows = financeGrid.datagrid('getRows');
	parent.$.modalDialog.handler.find("#financeGridData").val(JSON.stringify(financeRows));
	var message =status ==1?"确认要保存？":"确认要提交？";
	parent.$.messager.confirm("操作确认",message, function (r) {
        if (r) {
        	form.form("submit",{
        		url : urls.czcsnlSave,
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
}
//计算
function calcMoney(mdDialog){
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
/**
 * 编辑
 */
function czcsnlEdit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistCzcsnl,
		  async: false,     
		  data: {dlpjid:selectRow[0].dlpjid,xmhj:xmhj,projectid:selectRow[0].projectid}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有做过财政承受能力
		czcsnlAdd();
	} else {
		//修改
		parent.$.modalDialog({
			title : "财政承受能力评价",
			width : 900,
			height : 610,
			href : urls.czcsnlAdd,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
				mdDialog.find("#dlpjid").val(selectRow[0].dlpjid);
				mdDialog.find("#czcsnlid").val(selectRow[0].czcsnlid);
				comboboxFuncByCondFilter(menuid,"fcResult", "JUDGERESULT", "code", "name", mdDialog);
				//财政预算支出列表
				financeGrid(mdDialog, selectRow[0].projectid);
				var f = parent.$.modalDialog.handler.find('#prepareAddForm');
				$.post(urls.queryCzcsnlForm, {
					czcsnlid : selectRow[0].czcsnlid
				}, function(result) {
					var r = result.body.czcsnlForm;
					if(r!=""){
						f.form("load", r);
					}
				}, "json");
			},
			buttons : [ {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("1","1");//保存未提交
				}
			}, {
				text : "提交",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("2","1");//提交
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
 * 提交
 */
function sendWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var projectid = selectRow[0].projectid;
	var czcsnlid = selectRow[0].czcsnlid;
	if(czcsnlid == null){
		easyui_warn("该数据未保存录入，请先新增数据！",null);
		return;
	}
	parent.$.messager.confirm("操作确认", "确认提交选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.sendCzcsnl,
        		async: false,     
        		data: {projectid:projectid,czcsnlid:czcsnlid}
        	}).responseText; 
        	result = $.parseJSON(result);
        	easyui_auto_notice(result, function() {
        		topQuery();
			});
        }
	});
}
/**
 * 撤回
 */
function revokeWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var projectid = selectRow[0].projectid;
	/*是否可撤回验证*/
	if (!recallYN(xmhj,"cz",projectid)){
		easyui_warn("选中项目的下一环节已录入，不允许撤回！",null);
		return;
	}
	var czcsnlid = selectRow[0].czcsnlid;
	parent.$.messager.confirm("撤回确认", "确认撤回选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.revokeCzcsnl,
        		async: false,     
        		data: {projectid:projectid,czcsnlid:czcsnlid,xmhj:xmhj}
        	}).responseText; 
        	result = $.parseJSON(result);
        	easyui_auto_notice(result, function() {
        		topQuery();
			});
        }
	});
}
/**
 * 详情
 */
function czcsnlView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//新增
	parent.$.modalDialog({
		title : "财政承受能力评价",
		width : 900,
		height : 610,
		href : urls.czcsnlAdd+"?detail=1",
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			mdDialog.find("#projectid").val(selectRow[0].projectid);
			mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
			mdDialog.find("#dlpjid").val(selectRow[0].dlpjid);
			mdDialog.find("#czcsnlid").val(selectRow[0].czcsnlid);
			comboboxFuncByCondFilter(menuid,"fcResult", "JUDGERESULT", "code", "name", mdDialog);
			//财政预算支出列表
			financeGridDetail(mdDialog, selectRow[0].projectid);
			var f = parent.$.modalDialog.handler.find('#prepareAddForm');
			$.post(urls.queryCzcsnlForm, {
				czcsnlid : selectRow[0].czcsnlid
			}, function(result) {
				var r = result.body.czcsnlForm;
				if(r!=""){
					f.form("load", r);
				}
			}, "json");
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
/**
 * 详情
 * @param mdDialog
 * @param projectid
 * @returns
 */
function financeGridDetail(mdDialog,projectid){
	var grid = mdDialog.find("#financeGrid").datagrid({
      height: 330,
      width: 883,
      title: '财政预算支出',
      collapsible: false,
      url : urls.financeQuery,
      queryParams : {projectid:projectid,budgetType :"1"},
      singleSelect: true,
      rownumbers : true,
      showFooter : true,
      idField: 'budgetid',
      columns: [[//显示的列
                 { field: 'budget_year', title: '支出年度', width: 100,
                     editor: { type: 'numberspinner', options: {required: true,missingMessage:'请输入或选择支出年度',min:2010,max:2099,editable:true}}
                 },{ field: 'budget_gqtz', title: '股权投资', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){
              	   return value==undefined?"":Number(value).toFixed(2);},
                      editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入股权投资支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_yybt', title: '运营补贴', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入运营补贴支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_fxcd', title: '风险承担', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入风险承担支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_pttr', title: '配套投入', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入配套投入支出额',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'budget_ybggys', title: '一般公共预算', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入一般公共预算',min:0,precision:2,max:99999999999999.99} }
                 },{ field: 'total', title: '合计', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
                       editor: { type: 'numberbox', options: {editable:false} }
                 }]]
  });
	return grid;
}