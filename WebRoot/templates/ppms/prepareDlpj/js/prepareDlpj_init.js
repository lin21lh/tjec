
var baseUrl = contextpath + "ppms/discern/ProjectPrepareDlpjController/";
var urls = {
		queryUrl : baseUrl + "queryProject.do?vfmPjhj=1",//d默认条件定量评价=1
		queryIsExistDlpj : baseUrl +"queryIsExistDlpj.do",
		dlpjAddUrl : baseUrl +"dlpjAdd.do",
		queryDsfJg : baseUrl +"queryDsfJg.do",
		dlpjSave : baseUrl +"dlpjSave.do",
		querythirdOrgan : baseUrl +"querythirdOrgan.do",
		thirdOrgQueryUrl : baseUrl +"thirdOrgQuery.do",
		sendDlpj : baseUrl +"sendDlpj.do",
		revokeDlpj : baseUrl +"revokeDlpj.do",
		dlpjDetailUrl : baseUrl +"dlpjDetail.do"
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
			xmhj : xmhj,
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
 * 定量评价新增
 */
function dlpjAdd(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistDlpj,
		  async: false,     
		  data: {dxpjid:selectRow[0].dxpjid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有做过定量分析过
		//新增
		parent.$.modalDialog({
			title : "VFM定量评价",
			width : 900,
			height : 610,
			href : urls.dlpjAddUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
				comboboxFuncByCondFilter(menuid,"vomResult", "JUDGERESULT", "code", "name", mdDialog);
				//第三方机构grid
				thirdOrganGrid(mdDialog, selectRow[0].dxpjid);
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
		dlpjEdit();
	}
}
//第三方机构列表
var currentRow_third = -1;
function thirdOrganGrid(mdDialog,dxpjid){
	var grid = mdDialog.find("#thirdOrganGrid").datagrid({
        height: 280,//310
        title: '第三方机构',
        collapsible: false,
        url : urls.thirdOrgQueryUrl,
        queryParams : {dxpjid:dxpjid},
        singleSelect: true,
        rownumbers : true,
        idField: 'dsfjgid',
        columns: [[//显示的列
                   {field: 'id', title: '序号', width: 100,  checkbox: true },
                   { field: 'dsfjgid', title: '机构代码', width: 130,halign : 'center',hidden:true,
                       editor: { type: 'validatebox', options: {validType:{length:[0,50]}} }
                   }, { field: 'organName', title: '机构名称', width: 180,halign : 'center',
					   editor: { 
		        		   type: 'combogrid',
		        		   options: { 
		        			   required: true,missingMessage:'请输入机构名称',
		        			   	panelWidth:600,    
		        			    idField:'name',
		        			    pagination : true,
		        			    rownumbers : true,
		        			    textField:'name',  
		        			    mode:'remote',
		        			    url:urls.queryDsfJg,    
		        			    columns:[[    
		        			        {field:'dsfjgid',title:'机构id',width:120,hidden:true},    
		        			        {field:'organName',title:'机构名称',width:120},    
		        			        {field:'organCode',title:'机构代码',width:100},    
		        			        {field:'consignor',title:'委托方',width:120},
		        			        {field:'projectManager',title:'项目经理',width:120},
		        			        {field:'phone',title:'联系电话',width:120},
		        			        {field:'mobile',title:'手机号码',width:120},
		        			        {field:'content',title:'服务内容',width:150}
		        			    ]],
		        			    onClickRow: function (rowIndex, rowData) {
		        			       var  dsfjgid = grid.datagrid('getEditor', {index:currentRow_third,field:'dsfjgid'});
		        			       mdDialog.find(dsfjgid.target).val(rowData.dsfjgid);
		        			       var  organ_name = grid.datagrid('getEditor', {index:currentRow_third,field:'organName'});
		        			       var  organ_code = grid.datagrid('getEditor', {index:currentRow_third,field:'organCode'});
		        			       var  consignor = grid.datagrid('getEditor', {index:currentRow_third,field:'consignor'});
		        			       var  projectManager = grid.datagrid('getEditor', {index:currentRow_third,field:'projectManager'});
		        			       mdDialog.find(organ_name.target).combogrid("setValue",rowData.organName);
		        			       mdDialog.find(organ_code.target).val(rowData.organCode);
		        			       mdDialog.find(consignor.target).val(rowData.consignor);
		        			       mdDialog.find(projectManager.target).val(rowData.projectManager);
		        			       var  phone = grid.datagrid('getEditor', {index:currentRow_third,field:'phone'});
		        			       mdDialog.find(phone.target).val(rowData.phone);
		        			       var  mobile = grid.datagrid('getEditor', {index:currentRow_third,field:'mobile'});
		        			       mdDialog.find(mobile.target).val(rowData.mobile);
		        			       var  content = grid.datagrid('getEditor', {index:currentRow_third,field:'content'});
		        			       mdDialog.find(content.target).val(rowData.content);
		        			    }
		        		   } 
		        	   }
				   },{ field: 'organCode', title: '机构代码', width: 130,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入机构代码',validType:{length:[0,50]}} }
                   },{ field: 'consignor', title: '委托方', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入委托方',validType:{length:[0,50]}} }
                   },{ field: 'projectManager', title: '项目经理', width: 100,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入项目经理',validType:{length:[0,50]}} }
                   },{ field: 'phone', title: '联系电话', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入联系电话',validType:{length:[0,25]}} }
                   },{ field: 'mobile', title: '手机号码', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'content', title: '主要服务内容', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: {required: true,validType:{length:[0,500]}} }
                   },{ field: 'entrust_time', title: '委托时间', width: 100,halign : 'center',
                         editor: { type: 'datebox', options: {editable:false,validType:{length:[0,20]}} }
                   }]],
                   toolbar: [{id:"third_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#thirdOrganGrid").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#thirdOrganGrid').datagrid('validateRow', currentRow_third)){
		            			   	mdDialog.find('#thirdOrganGrid').datagrid('endEdit', currentRow_third);
		            				mdDialog.find("#thirdOrganGrid").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#thirdOrganGrid").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_third = mdDialog.find('#thirdOrganGrid').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#thirdOrganGrid").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#thirdOrganGrid").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_third = mdDialog.find('#thirdOrganGrid').datagrid('getRows').length-1;
		            	   }
		        	   }
		           },'-', {id:"third_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#thirdOrganGrid").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#thirdOrganGrid").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_third==rowIndex){
		    	                        		currentRow_third = -1;
		    	                        	}else if(rowIndex<currentRow_third){
		    	                        		currentRow_third = currentRow_third-1;
		    	                        	}
		    	                       		mdDialog.find("#thirdOrganGrid").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_third != rowIndex && mdDialog.find('#thirdOrganGrid').datagrid('validateRow', currentRow_third)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#thirdOrganGrid').datagrid('endEdit', currentRow_third);
		        		   //开启新行编辑
		        			mdDialog.find("#thirdOrganGrid").datagrid('beginEdit', rowIndex);
		        			currentRow_third = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#thirdOrganGrid').datagrid('validateRow', currentRow_third)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#thirdOrganGrid').datagrid('endEdit', currentRow_third);
		        		   currentRow_third = -1;
		        	   }
		           }
    });
	return grid;
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
	//评价指标
	var thirdOrganGrid = mdDialog.find("#thirdOrganGrid");
	var thirdOrganData = thirdOrganGrid.datagrid("getData");
	var thirdOrganTotal = thirdOrganData.total;
	var isPass = true;
	for(var i=0;i<thirdOrganTotal;i++){
		//打开行编辑、验证是否通过验证
		thirdOrganGrid.datagrid('beginEdit', i);
		isPass = thirdOrganGrid.datagrid('validateRow', i);
		thirdOrganGrid.datagrid('endEdit', i);//把所有的编辑行锁定
		if(!isPass){
			break;
		}
	}
	//验证评分是否都已经填写完成
	if(!isPass){
		return;
	}
	var zbdfRow = thirdOrganGrid.datagrid('getRows');
	parent.$.modalDialog.handler.find("#thirdOrganGridData").val(JSON.stringify(zbdfRow));
	var message =status ==1?"确认要保存？":"确认要提交？";
	parent.$.messager.confirm("操作确认",message, function (r) {
        if (r) {
        	form.form("submit",{
        		url : urls.dlpjSave,
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
/**
 * 
 */
function dlpjEdit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistDlpj,
		  async: false,     
		  data: {dxpjid:selectRow[0].dxpjid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有做过定量分析过
		dlpjAdd();
	} else {
		//修改
		parent.$.modalDialog({
			title : "VFM定量评价",
			width : 900,
			height : 610,
			href : urls.dlpjAddUrl,
			onLoad : function() {
				var f = parent.$.modalDialog.handler.find('#prepareAddForm');
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
				comboboxFuncByCondFilter(menuid,"vomResult", "JUDGERESULT", "code", "name", mdDialog);
				$.post(urls.querythirdOrgan, {
					dlpjid : selectRow[0].dlpjid
				}, function(result) {
					var r = result.body.thirdOrgan;
					if(r!=""){
						f.form("load", r);
					}
				}, "json");
				thirdOrganGrid(mdDialog, selectRow[0].dxpjid);
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
	var dlpjid = selectRow[0].dlpjid;
	if(dlpjid == null){
		easyui_warn("该数据未保存录入，请先新增数据！",null);
		return;
	}
	parent.$.messager.confirm("操作确认", "确认提交选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.sendDlpj,
        		async: false,     
        		data: {projectid:projectid,dlpjid:dlpjid}
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
	if (!recallYN(xmhj,"dl",projectid)){
		easyui_warn("选中项目财政承受能力评价已提交，不允许撤回！",null);
		return;
	}
	var dlpjid = selectRow[0].dlpjid;
	parent.$.messager.confirm("撤回确认", "确认撤回选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.revokeDlpj,
        		async: false,     
        		data: {projectid:projectid,dlpjid:dlpjid}
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
function dlpjView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var projectid = selectRow[0].projectid;
	var dlpjid = selectRow[0].dlpjid;
	if(dlpjid == null){
		easyui_warn("该数据未保存录入，请先新增数据！",null);
		return;
	}
	//新增
	parent.$.modalDialog({
		title : "VFM定量评价",
		width : 900,
		height : 610,
		href : urls.dlpjDetailUrl,
		onLoad : function() {
			var f = parent.$.modalDialog.handler.find('#prepareAddForm');
			var mdDialog = parent.$.modalDialog.handler;
			mdDialog.find("#projectid").val(selectRow[0].projectid);
			mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
			$.post(urls.querythirdOrgan, {
				dlpjid : selectRow[0].dlpjid
			}, function(result) {
				var r = result.body.thirdOrgan;
				if(r!=""){
					f.form("load", r);
				}
			}, "json");
			thirdOrganGridDetail(mdDialog, selectRow[0].dxpjid);
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
function thirdOrganGridDetail(mdDialog,dxpjid){
	var grid = mdDialog.find("#thirdOrganGrid").datagrid({
        height: 300,//310
        title: '第三方机构',
        collapsible: false,
        url : urls.thirdOrgQueryUrl,
        queryParams : {dxpjid:dxpjid},
        singleSelect: true,
        rownumbers : true,
        idField: 'dsfjgid',
        columns: [[//显示的列
                   { field: 'dsfjgid', title: '机构代码', width: 130,halign : 'center',hidden:true
                   }, { field: 'organName', title: '机构名称', width: 180,halign : 'center' 
				   },{ field: 'organCode', title: '机构代码', width: 130,halign : 'center'
                   },{ field: 'consignor', title: '委托方', width: 100,halign : 'center'
                   },{ field: 'projectManager', title: '项目经理', width: 100,halign : 'center'
                   },{ field: 'phone', title: '联系电话', width: 150,halign : 'center'
                   },{ field: 'mobile', title: '手机号码', width: 150,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'content', title: '主要服务内容', width: 200,halign : 'center'
                   },{ field: 'entrust_time', title: '委托时间', width: 100,halign : 'center'
                   }]]
    });
	return grid;
}