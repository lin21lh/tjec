
var baseUrl = contextpath + "ppp/xmkf/ProjectLzbgController/";
var urls = {
		//查询项目信息
		queryUrl : baseUrl+"queryProject.do",
		lzbgAdd : baseUrl+"lzbgAdd.do"
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
		elementcode : "DLJG",
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
			status : 1,
			activityId : activityId,
			firstNode : true,
			lastNode : false
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "lzbgid",checkbox : true}  
			          ,{field : "xmmc",title : "项目名称",halign : 'center',width:250,sortable:true}
			          ,{field : "orgname",title : "申报单位",halign : 'center',width:100,sortable:true}
			          ,{field : "xmlxMc",title : "项目类型",halign : 'center',	width:80,sortable:true}
			          ,{field : "xmzje",title : "项目总投资（万元）",halign : 'right',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
			          ,{field : "xmztMc",title : "项目状态",halign : 'center',	width:80,sortable:true}
			          ,{field : "hznx",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
			          ,{field : "sshyMc",title : "所属行业",halign : 'center',	width:100	,sortable:true}
			          ,{field : "yzfsMc",title : "运作方式",halign : 'center',	width:100,sortable:true	}
			          ,{field : "hbjzMc",title : "回报机制",halign : 'center',	width:120,sortable:true}
			          ,{field : "fqsj",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
			          ,{field : "fqlxMc",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
			          ,{field : "fqrmc",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
			          ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
			          ,{field : "sfxmName",title : "示范项目",halign : 'center',	width:100,sortable:true }
			          ,{field : "tjxmName",title : "推介项目",halign : 'center',	width:80,sortable:true }
			          ,{field : "sqbtName",title : "申请补贴",halign : 'center',	width:80,sortable:true }
			          ,{field : "btje",title : "补贴金额（万元）",halign : 'right',align:'right'}
			          ,{field : "xmgk",title : "项目概况",halign : 'center',	width:150,sortable:true }
			          ,{field : "lxrdh",title : "联系人电话",halign : 'center',	width:150,sortable:true}
			          ,{field : "cjrMc",title : "创建人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "cjsj",title : "创建时间",halign : 'center',	width:130,sortable:true	}
			          ,{field : "xgrMc",title : "修改人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "xgsj",title : "修改时间",halign : 'center',	width:130,sortable:true	}
		              ,{field : "dqztMc",title : "当前状态",halign : 'center',	width:80,sortable:true}
		              ,{field : "xmlx",title : "项目类型",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "sshy",title : "所属行业",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proPerate",title : "运作方式",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "hbjz",title : "回报机制",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "wfid",title : "WFID",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "sfxm",title : "示范项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "tjxm",title : "推介项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "sqbt",title : "申请补贴",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "xmid",title : "项目id",halign : 'center',	width:100,sortable:true ,hidden:true	}
		             ] ]
	});
}
var datagrid;
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			status : $("#status").combobox('getValue'),
			xmmc : $("#xmmc").val(),
			xmlxr : $("#xmlxr").val(),
			sshy :  $("#sshy").val(),
			yzfs : $("#yzfs").combobox('getValues').join(","),
			hbjz : $("#hbjz").combobox('getValues').join(","),
			fqlx : $("#fqlx").combobox('getValues').join(","),
			xmlx : $("#xmlx").combobox('getValues').join(","),
			menuid : menuid,
			activityId : activityId,
			firstNode : true,
			lastNode : false
		};
	projectDataGrid.datagrid("load", param);
}
/**
 * 工作流信息
 */
function workflowMessage(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
/**
 * 论证报告录入
 */
function lzbgAdd(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一个项目！",null);
		return;
	}
	parent.$.modalDialog({
		title : "论证报告录入",
		width : 900,
		height : 630,
		href : urls.lzbgAdd,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			//评价专家列表
			pjzjTable(mdDialog, selectRow[0].lzbgid);
			//指标列表
			pjzbTableGrid(mdDialog, selectRow[0].lzbgid);
			//财政承受能力
			financeGrid(mdDialog, selectRow[0].lzbgid);
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.addCommitUrl,"lzbgAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				submitForm(urls.addCommitUrl,"lzbgAddForm","1");
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
//定性分析专家列表
var currentRow_expert = -1;
function pjzjTable(mdDialog,lzbgid){
	var grid = mdDialog.find("#pjzj").datagrid({
		height: 240,
		title: '评价专家列表',
		collapsible: false,
		url : urls.qualExpertGrid,
		queryParams : {lzbgid:lzbgid},
		singleSelect: true,
		rownumbers : true,
		idField: 'pjzjid',
		columns: [[//显示的列
		           {field: 'zjlbid', title: '序号', width: 100,  checkbox: true },
		           { field: 'zjmc', title: '专家名称', width: 130,halign : 'center',
		        	   editor: { 
		        		   type: 'validatebox',
		        		   options: {} 
		        	   }
		           },{ field: 'zjlx', title: '专家类型', width: 100,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,50]}} }
		           },{ field: 'zjlxfs', title: '联系方式', width: 140,halign : 'center',
		        	   editor: { type: 'validatebox', options: {required: true,missingMessage:'请输入联系方式', editable:false,validType:{length:[0,30]}} }
		           },{ field: 'pbzy', title: '评标专业', width: 150,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,200]}} }
		           },{ field: 'scly', title: '负责领域', width: 150,halign : 'center',
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
		        		   var data = mdDialog.find("#pjzj").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#pjzj').datagrid('validateRow', currentRow_expert)){
		            			   	mdDialog.find('#pjzj').datagrid('endEdit', currentRow_expert);
		            				mdDialog.find("#pjzj").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#pjzj").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_expert = mdDialog.find('#pjzj').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#pjzj").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#pjzj").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_expert = mdDialog.find('#pjzj').datagrid('getRows').length-1;
		            	   }
		        	   }
		           },'-', {id:"expert_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#pjzj").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#pjzj").datagrid('getRowIndex',row[0]);
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_expert==rowIndex){
		    	                        		currentRow_expert = -1;
		    	                        	}else if(rowIndex<currentRow_expert){
		    	                        		currentRow_expert = currentRow_expert-1;
		    	                        	}
		    	                       		mdDialog.find("#pjzj").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_expert != rowIndex && mdDialog.find('#pjzj').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#pjzj').datagrid('endEdit', currentRow_expert);
		        		   //开启新行编辑
		        			mdDialog.find("#pjzj").datagrid('beginEdit', rowIndex);
		        			currentRow_expert = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#pjzj').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#pjzj').datagrid('endEdit', currentRow_expert);
		        		   currentRow_expert = -1;
		        	   }
		           }
	});
	return grid;
}
//定性分析指标
var currentRow_zb = -1;
function pjzbTableGrid(mdDialog,lzbgid){
	var grid = mdDialog.find("#pjzbTable").datagrid({
		height: 240,
		title: '评价指标',
		collapsible: false,
		url : urls.queryPjzbTable,
		queryParams : {lzbgid:lzbgid},
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
var currentRow = -1;
//财政支出
function financeGrid(mdDialog,lzbgid){
	var grid = mdDialog.find("#financeGrid").datagrid({
    height: 330,
    width: 883,
    title: '财政预算支出',
    collapsible: false,
    url : urls.financeQuery,
    queryParams : {lzbgid:lzbgid},
    singleSelect: true,
    rownumbers : true,
    showFooter : true,
    idField: 'budgetid',
    columns: [[//显示的列
               {field: 'czcsnlid', title: '序号', width: 100, halign : 'center', checkbox: true },
               { field: 'zcnd', title: '支出年度', width: 100,
                   editor: { type: 'numberspinner', options: {required: true,missingMessage:'请输入或选择支出年度',min:2010,max:2099,editable:true}}
               },{ field: 'zcje', title: '合计', width: 120,halign : 'center',align:'right',formatter:function(value,row,index){return value==undefined?"":Number(value).toFixed(2);},
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
    }
});
	return grid;
}
