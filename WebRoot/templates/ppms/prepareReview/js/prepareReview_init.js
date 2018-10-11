
var baseUrl = contextpath + "ppms/discern/ProjectPrepareReviewController/";
var urls = {
		queryUrl : baseUrl + "queryProject.do?vfmPjhj=1",
		prepareAddUrl : baseUrl + "prepareAdd.do",
		prepareDetail : baseUrl + "prepareDetail.do",
		qualExpertGrid : baseUrl+"qualExpertGrid.do",
		qryExpertByQ : baseUrl+"qryExpertByQ.do",
		showPszbList : baseUrl+"showPszbList.do",
		queryPszbList : baseUrl+"queryPszbList.do",
		pjzbSave : baseUrl + "pjzbSave.do",
		queryPjzbTable : baseUrl + "queryPjzbTable.do",
		queryIsExistPszb : baseUrl + "queryIsExistPszb.do",
		revokePszb : baseUrl + "revokePszb.do",
		sendPszb : baseUrl + "sendPszb.do"
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
 * 新增
 */
function projectAdd(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistPszb,
		  async: false,     
		  data: {projectid:selectRow[0].projectid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//新增
		parent.$.modalDialog({
			title : "评审准备",
			iconCls : 'icon-add',
			width : 900,
			height : 630,
			href : urls.prepareAddUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				//是否
				comboboxFuncByCondFilter(menuid,"vfmDlpj", "SYS_TRUE_FALSE", "code", "name",mdDialog);//项目类型
				//专家列表
				qualExpertGrid(mdDialog, selectRow[0].projectid);
				//指标列表
				pjzbTableGrid(mdDialog, selectRow[0].pszbid);
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
					forSubmit("2");//状态已提交
				}
			},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	}else {//修改
		projectEdit();
	}
}
/**
 *  修改
 */
function projectEdit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistPszb,
		  async: false,     
		  data: {projectid:selectRow[0].projectid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//新增
		projectAdd();
	} else {
		var projectid = selectRow[0].projectid;
		var pszbid = selectRow[0].pszbid;
		var vfmDlpj = selectRow[0].vfmDlpj;
		parent.$.modalDialog({
			title : "评审准备修改",
			iconCls : 'icon-add',
			width : 900,
			height : 630,
			href : urls.prepareAddUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(projectid);
				comboboxFuncByCondFilter(menuid,"vfmDlpj", "SYS_TRUE_FALSE", "code", "name",mdDialog);//项目类型
				mdDialog.find("#vfmDlpj").searchbox("setValue", vfmDlpj);
				mdDialog.find("#pszbid").val(pszbid);
				//是否
				//专家列表
				qualExpertGrid(mdDialog, projectid);
				//指标列表
				pjzbTableGrid(mdDialog, pszbid);
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
					forSubmit("2","1");//状态已提交
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
 * 表单提交
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
	//专家列表
	var expGrid = mdDialog.find("#qualExpert");
	var expData = expGrid.datagrid("getData");
	var expTotal = expData.total;
	for(var i=0;i<expTotal;i++){
		//打开行编辑、验证是否通过验证
		expGrid.datagrid('beginEdit', i);
		expGrid.datagrid('endEdit', i);//把所有的编辑行锁定
	}
	var expRows = expGrid.datagrid('getRows');
	if(expRows.length == 0){
		parent.$.messager.alert('系统提示', "请录入专家信息！", 'info');
		return;
	}
	/*重复专家验证*/
	for(var i=1;i<expRows.length;i++){
		for(var j=i;j<expRows.length;j++){
			if (expRows[i-1].expertId == expRows[j].expertId){
				parent.$.messager.alert('系统提示', "列表中有重复的专家信息，请删除！", 'info');
				return;
			}
		}
	}
	
	//评价指标
	var pjzbGrid = mdDialog.find("#pjzbTable");
	var pjzbData = pjzbGrid.datagrid("getData");
	var pjzbTotal = pjzbData.total;
	var isPass = true;
	for(var i=0;i<pjzbTotal;i++){
		//打开行编辑、验证是否通过验证
		pjzbGrid.datagrid('beginEdit', i);
		isPass = pjzbGrid.datagrid('validateRow', i);
		pjzbGrid.datagrid('endEdit', i);//把所有的编辑行锁定
		if(!isPass){
			break;
		}
	}
	if(!isPass){
		return;
	}
	var pjzbRows = pjzbGrid.datagrid('getRows');
	if(pjzbRows.length == 0){
		parent.$.messager.alert('系统提示', "请录入指标信息！", 'info');
		return;
	} 
	/*指标权重验证*/
	var totalPercent = 0;
	for(var i=0;i<pjzbRows.length;i++){
		totalPercent = totalPercent + parseFloat(pjzbRows[i].qz);
	}
	if (totalPercent != 100){
		parent.$.messager.alert('系统提示', "所有指标权重和不为100，请重新输入！", 'info');
		return;
	}
	parent.$.modalDialog.handler.find("#qualExpertData").val(JSON.stringify(expRows));
	parent.$.modalDialog.handler.find("#pjzbTableData").val(JSON.stringify(pjzbRows));
	var message =status ==1?"确认要保存？":"确认要提交？";
	parent.$.messager.confirm("操作确认",message, function (r) {
        if (r) {
        	form.form("submit",{
        		url : urls.pjzbSave,
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
//定性分析专家列表
var currentRow_expert = -1;
function qualExpertGrid(mdDialog,projectid){
	var grid = mdDialog.find("#qualExpert").datagrid({
		height: 240,
		title: '定性评价专家列表',
		collapsible: false,
		url : urls.qualExpertGrid,
		queryParams : {projectid:projectid,xmhj:xmhj},
		singleSelect: true,
		rownumbers : true,
		idField: 'qualexpertid',
		columns: [[//显示的列
		           {field: 'qualexpertid', title: '序号', width: 100,  checkbox: true },
		           { field: 'expertName', title: '专家名称', width: 130,halign : 'center',
		        	   editor: { 
		        		   type: 'combogrid',
		        		   options: { 
		        			   required: true,missingMessage:'请输入专家名称',
		        			   	panelWidth:600,    
		        			    idField:'name',
		        			    pagination : true,
		        			    rownumbers : true,
		        			    textField:'name',  
		        			    mode:'remote',
		        			    url:urls.qryExpertByQ,    
		        			    columns:[[    
		        			        {field:'expertid',title:'专家编码',width:60},    
		        			        {field:'name',title:'专家名称',width:100},    
		        			        {field:'sexName',title:'性别',width:120},
		        			        {field:'politicsStatusName',title:'政治面貌',width:120},
		        			        {field:'phoneNumber',title:'联系方式',width:120},
		        			        {field:'isEmergencyName',title:'应急专家',width:80},
		        			        {field:'expertTypeName',title:'专家类型',width:120},
		        			        {field:'highestDegreeName',title:'最高学历',width:80},
		        			        {field:'highestOfferingName',title:'最高学位',width:80},
		        			        {field:'majorTypeName',title:'从事专业类别',width:120},
		        			        {field:'industryName',title:'所属行业',width:120},
		        			        {field:'bidMajorName',title:'评标专业方向',width:250},
		        			        {field:'qualificationName',title:'执业资格证书',width:350},
		        			        {field:'research',title:'个人研究以及专业成就',width:350},
		        			    ]],
		        			    onClickRow: function (rowIndex, rowData) {
		        			       var  typeObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertType'});
		        			       var  phoneObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertPhone'});
		        			       var  majorObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'bidmajor'});
		        			       var  expertIdObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertId'});
		        			       mdDialog.find(typeObj.target).val(rowData.expertTypeName);
		        			       mdDialog.find(phoneObj.target).val(rowData.phoneNumber);
		        			       mdDialog.find(majorObj.target).val(rowData.bidMajorName);
		        			       mdDialog.find(expertIdObj.target).val(rowData.expertid);
		        			    }
		        		   } 
		        	   }
		           },{ field: 'expertType', title: '专家类型', width: 100,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,50]}} }
		           },{ field: 'expertPhone', title: '联系方式', width: 140,halign : 'center',
		        	   editor: { type: 'validatebox', options: {required: true,missingMessage:'请输入联系方式', editable:false,validType:{length:[0,30]}} }
		           },{ field: 'bidmajor', title: '评标专业', width: 150,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,200]}} }
		           },{ field: 'responsibleArea', title: '负责领域', width: 150,halign : 'center',
		        	   editor: { type: 'validatebox', options: {validType:{length:[0,50]}} }
		           },{ field: 'expertId', title: 'expertId', width: 100,halign : 'center',hidden:true,
		        	   editor: { type: 'validatebox', options: { editable:false} }
		           },{ field: 'projectid', title: 'projectid', width: 100,halign : 'center',hidden:true,
		        	   editor: { type: 'validatebox', options: { editable:false} }
		           },{ field: 'remark', title: '说明', width: 150,halign : 'center',
		        	   editor: { type: 'validatebox', options: { validType:{length:[0,100]}} }
		           }]],
		           toolbar: [{ id:"expert_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#qualExpert").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#qualExpert').datagrid('validateRow', currentRow_expert)){
		            			   	mdDialog.find('#qualExpert').datagrid('endEdit', currentRow_expert);
		            				mdDialog.find("#qualExpert").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#qualExpert").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_expert = mdDialog.find('#qualExpert').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#qualExpert").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#qualExpert").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_expert = mdDialog.find('#qualExpert').datagrid('getRows').length-1;
		            	   }
		        	   }
		           },'-', {id:"expert_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#qualExpert").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#qualExpert").datagrid('getRowIndex',row[0]);
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_expert==rowIndex){
		    	                        		currentRow_expert = -1;
		    	                        	}else if(rowIndex<currentRow_expert){
		    	                        		currentRow_expert = currentRow_expert-1;
		    	                        	}
		    	                       		mdDialog.find("#qualExpert").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_expert != rowIndex && mdDialog.find('#qualExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#qualExpert').datagrid('endEdit', currentRow_expert);
		        		   //开启新行编辑
		        			mdDialog.find("#qualExpert").datagrid('beginEdit', rowIndex);
		        			currentRow_expert = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#qualExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#qualExpert').datagrid('endEdit', currentRow_expert);
		        		   currentRow_expert = -1;
		        	   }
		           }
	});
	return grid;
}
//定性分析指标
var currentRow_zb = -1;
function pjzbTableGrid(mdDialog,pszbid){
	var grid = mdDialog.find("#pjzbTable").datagrid({
		height: 240,
		title: '定性评价指标',
		collapsible: false,
		url : urls.queryPjzbTable,
		queryParams : {pszbid:pszbid},
		singleSelect: true,
		rownumbers : true,
		idField: 'zbkid',
		columns: [[//显示的列
		           {field: 'zbkid', title: '序号', width: 100,  checkbox: true },
		           { field: 'zbmc', title: '指标名称', width: 150,halign : 'center'
		           },{ field: 'zblb', title: '指标类别', width: 120,halign : 'center'
		           },{ field: 'qz', title: '权重', width: 140,halign : 'center',align:'right',
		        	   editor: { type: 'numberbox', options: {required: true,missingMessage:'请输入权重',min:0,precision:2,max:100} }
		           },{ field: 'zbms', title: '指标说明', width: 300,halign : 'center'
		           }]],
		           toolbar: [{ id:"pjzb_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var rows = grid.datagrid('getRows');
		        		   showZbDialog(parent.$('#dxzbdiv'),rows);
		        	   }
		           },'-', {id:"pjzb_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#pjzbTable").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#pjzbTable").datagrid('getRowIndex',row[0]);
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
    	                       		mdDialog.find("#pjzbTable").datagrid('deleteRow',rowIndex);
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		           },
		           onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
			        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_zb != rowIndex && mdDialog.find('#pjzbTable').datagrid('validateRow', currentRow_zb)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#pjzbTable').datagrid('endEdit', currentRow_zb);
		        		   //开启新行编辑
		        			mdDialog.find("#pjzbTable").datagrid('beginEdit', rowIndex);
	        			   var editor = mdDialog.find("#pjzbTable").datagrid('getEditor', {index:rowIndex,field:"qz"});
	        			   $(editor.target).next('span').find('input').focus();//聚集焦点
	        			   $(editor.target).next('span').find('input').select();//选中
	        			   currentRow_zb = rowIndex;
		        	   }
		           }
	});
	return grid;
}
//选择指标页面
function showZbDialog(dialogId,data){
	dialogId.dialog({
		title : "选择指标",
		width : 800,
		height : 500,
		href : urls.showPszbList,
		iconCls : 'icon-edit',
		modal : true,
		onLoad : function() {
			doInit(dialogId,'',data);
		},
		buttons : funcOperButtons(dialogId)
	});
}
function funcOperButtons(dialogId){
	buttons= [{
		text : "确定",
		iconCls : "icon-redo",
		handler : function() {
			setValReceive();
			dialogId.dialog('close');
		}
	},
	{text : "关闭",
		iconCls : "icon-cancel",
		handler : function() {
			dialogId.dialog('close');
		}
	}];
	return buttons;
}
//往父页面填充值
function setValReceive(){
	var selRowAry = parent.$("#centraltab").find("#rgrid").datagrid('getData').rows;
	var mdDialog = parent.$.modalDialog.handler;
	//将原先数据清空
	mdDialog.find("#pjzbTable").datagrid('loadData', { total: 0, rows: []});
	//填充数据
	for(var i=0;i<selRowAry.length;i++){
		rowIndex= mdDialog.find("#pjzbTable").datagrid('appendRow',selRowAry[i]);
	}
}
//接收人页面加载数据
function doInit(jq, zbkid,data) {
	for(var i=0;i<data.length;i++){
		zbkid += data[i].zbkid;
		if(i!=data.length-1){
			zbkid += ",";
		}
	}
	var	param = {
			zbkid : zbkid
		};
	jq.find("#lgrid").datagrid({
		url : urls.queryPszbList,
		queryParams : param,
		title : '可选用指标',
		remoteSort : false,
		idField : 'zbkid',
		toolbar : $('#tb'),
		columns : [ 
		[ {field : 'zbkid',checkbox : true
		}, {field : 'zbmc',title : '指标名称',width : 85
		}, {field : 'zblb',title : '指标类别',width : 110
		}, {field : 'zbms',title : '指标描述',width : 110
		} ] ],
		fit : true,
		border : false,
		rownumbers : true,
		pagination : false,
		pageSize : 100,
		onDblClickRow : function(index, row) {
			dblClickChoiseUser(grid_left, grid_right,index, row);
		}
	});
	jq.find("#rgrid").datagrid({
		collapsible: false,
		singleSelect: true,
		rownumbers : true,
		url : urls.selectedUserTree,
		queryParams : param,
		title : '已选用指标',
		remoteSort : false,
		idField : 'userid',
		columns : [ 
		   		[ {field : 'zbkid',checkbox : true
				}, {field : 'zbmc',title : '指标名称',width : 85
				}, {field : 'zblb',title : '指标类别',width : 110
				}, {field : 'zbms',title : '指标描述',width : 110
				} ] ],
		fit : true,
		border : false,
		rownumbers : true,
		pagination : false,
		pageSize : 100,
		onDblClickRow : function(index, row) {
			dblClickChoiseUser(grid_right, grid_left,index, row);
		}
	});
	
	jq.find(".datagrid-pager > table").hide();

	var grid_left = jq.find("#lgrid");
	var grid_right = jq.find("#rgrid");
	
	// 初始化添加按钮
	jq.find('#addBtn').linkbutton({
		onClick : function(index) {
			choiseZb(grid_left, grid_right);
		}
	});
	jq.find("#delBtn").linkbutton({
		onClick : function() {
			choiseZb(grid_right, grid_left);
		}
	});
	if(data.length !=0 ){
		for(var i=0;i<data.length;i++){
			jq.find("#rgrid").datagrid('appendRow',data[i]);
		}
	}
}
function dblClickChoiseUser(grid_main, grid_sub, index, row) {
	grid_sub.datagrid('appendRow',row);
	grid_main.datagrid('deleteRow', index);
}

//已选和未选用户操作
function choiseZb(grid_main, grid_sub) {
	var rows = grid_main.datagrid("getSelections");
	if (rows.length < 1) {
		parent.$.messager.alert('提示', "请选择指标！", 'warnning');
		return;
	}
	//删除数据时，datagrid会自动变更index,多行删除时，需要把删除的row放在一个数组，然后进行删除
	var oldRows = new Array();
	for (var i=0; i<rows.length; i++) {
		grid_sub.datagrid('appendRow',rows[i]);
		oldRows.push(rows[i]);
	}
	for (var i=0; i<oldRows.length; i++) {
		grid_main.datagrid('deleteRow', grid_main.datagrid('getRowIndex', oldRows[i]));
	}
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
	if (!recallYN(xmhj,"ps",projectid)){
		easyui_warn("选中项目定性评价已提交，不允许撤回！",null);
		return;
	}
	
	var pszbid = selectRow[0].pszbid;
	parent.$.messager.confirm("撤回确认", "确认撤回选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.revokePszb,
        		async: false,     
        		data: {projectid:projectid,pszbid:pszbid}
        	}).responseText; 
        	result = $.parseJSON(result);
        	easyui_auto_notice(result, function() {
        		topQuery();
			});
        }
	});
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
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistPszb,
		  async: false,     
		  data: {projectid:selectRow[0].projectid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//空的
		easyui_warn("该数据未保存录入，请先新增数据！",null);
		return;
	}
	
	var projectid = selectRow[0].projectid;
	var pszbid = selectRow[0].pszbid;
	parent.$.messager.confirm("操作确认", "确认提交选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.sendPszb,
        		async: false,     
        		data: {projectid:projectid,pszbid:pszbid}
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
function projectView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "评审准备详情",
		iconCls : 'icon-view',
		width : 900,
		height : 630,
		href : urls.prepareDetail,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			mdDialog.find("#vfmDlpjName").textbox("setValue", selectRow[0].vfmDlpjName);
			//专家列表
			qualExpertGridDetail(mdDialog, selectRow[0].projectid);
			//指标列表
			pjzbTableGridDetail(mdDialog, selectRow[0].pszbid);
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
 * 定性分析专家列表详情
 * @param mdDialog
 * @param projectid
 * @returns
 */
function qualExpertGridDetail(mdDialog,projectid){
	var grid = mdDialog.find("#qualExpert").datagrid({
		height: 250,
		title: '定性评价专家列表',
		collapsible: false,
		url : urls.qualExpertGrid,
		queryParams : {projectid:projectid},
		singleSelect: true,
		rownumbers : true,
		idField: 'qualexpertid',
		columns: [[//显示的列
		           { field: 'expertName', title: '专家名称', width: 130,halign : 'center'
		           },{ field: 'expertPhone', title: '联系方式', width: 140,halign : 'center'
		           },{ field: 'bidmajor', title: '评标专业', width: 150,halign : 'center'
		           },{ field: 'responsibleArea', title: '负责领域', width: 150,halign : 'center'
		           },{ field: 'expertId', title: 'expertId', width: 100,halign : 'center',hidden:true
		           },{ field: 'projectid', title: 'projectid', width: 100,halign : 'center',hidden:true
		           },{ field: 'remark', title: '说明', width: 150,halign : 'center'
		           }]]
	});
	return grid;
}
/**
 * 定性分析指标详情
 * @param mdDialog
 * @param pszbid
 * @returns
 */
function pjzbTableGridDetail(mdDialog,pszbid){
	var grid = mdDialog.find("#pjzbTable").datagrid({
		height: 260,
		title: '定性评价指标',
		collapsible: false,
		url : urls.queryPjzbTable,
		queryParams : {pszbid:pszbid},
		singleSelect: true,
		rownumbers : true,
		idField: 'zbkid',
		columns: [[//显示的列
		           { field: 'zbmc', title: '指标名称', width: 150,halign : 'center'
		           },{ field: 'zblb', title: '指标类别', width: 120,halign : 'center'
		           },{ field: 'qz', title: '权重', width: 140,halign : 'center',align:'right'
		           },{ field: 'zbms', title: '指标说明', width: 300,halign : 'center'
		           }]]
	});
	return grid;
}