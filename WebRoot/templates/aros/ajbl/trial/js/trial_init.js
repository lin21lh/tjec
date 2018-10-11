
var baseTrialUrl = contextpath + "aros/xzfy/controller/BSurveyAndTrialController/";
var trialUrls = {
		//查询项目信息
		queryTrialUrl : baseTrialUrl+"queryTrialGrid.do",
		trialUrl : baseTrialUrl+"trialinit.do",
		delTrialUrl : baseTrialUrl+"toDeleteTrial.do",
		addTrialUrl : baseTrialUrl+"toSaveTrial.do"
};


//默认加载
$(function() {

});
var trialGrid;
function trialQuery() {
	var parentGrid = $("#xzfyDecisionDataGrid");
	var selectRow = parentGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请先选择一个案件！",null);
		return;
	}
	var row = parentGrid.datagrid("getSelections")[0];
	var caseid = row.caseid;
	parent.$.modalDialog({
		title : '行政复议庭审笔录',
		width : 1000,
		height : 600,
		href : trialUrls.trialUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//庭审笔录Grid
			trialDataGrid(mdDialog, caseid);
			mdDialog.find("#trialForm").find("#caseid").val(caseid);
			//保存按钮绑定触发事件
			$("#linkbutton_save",mdDialog).on("click",function(){
				submitTrialForm();
			});
			//取消按钮绑定触发事件
			$("#linkbutton_cancel",mdDialog).on("click",function(){
				rejectTrialEdit();
			});
			
			//操作按钮显示与置灰
			editflag=true;
			controllEditButtons(mdDialog,editflag);
			// 编辑时操作按钮显示
			mdDialog.find("input:enabled,textarea").on("change",function(){
				editflag=true;
				controllEditButtons(mdDialog,editflag);
			});
			
			//动态绑定日期控件的onchange事件
			mdDialog.find("#casttime").datetimebox({
				onChange : function(date) {
					editflag=true;
					controllEditButtons(mdDialog,editflag);
				}
			});
		}
	});
}

//页面刷新
function showReload() {
	parent.$.modalDialog.handler.find("#trialGrid").datagrid('reload'); 
}



//删除
function toTrialDelete(trid){

	parent.$.messager.confirm("确认删除", "确认删除所选数据？", function(r) {
		if (r) {
			$.post(trialUrls.delTrialUrl, {
				menuid : menuid,
				trid : trid
			}, function(result) {
				easyui_auto_notice(result, function() {
					trialGrid.datagrid("reload");
					trialGrid.datagrid('selectRow',0);
					easyui_info("删除成功！",function(){});
				});
			}, "json");
		}
	});
}

//保存
function submitTrialForm(){
	var form = parent.$.modalDialog.handler.find('#trialForm');
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	} 

	form.form("submit",
			{
		url : trialUrls.addTrialUrl,
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
				var newgrid = trialGrid.datagrid('reload');
				trialGrid.datagrid('clearSelections');
				var caseid = form.find("#caseid").val();
				form.form("clear");
				form.find("#caseid").val(caseid);
				easyui_info("保存成功！",function(){});
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
			}
		});

}

//撤销修改
function rejectTrialEdit() {
	var md = parent.$.modalDialog.handler;
	var caseid = md.find('#trialForm').find("#caseid").val();
	var sel = trialGrid.datagrid('getSelections');
	if (sel.length==1) {
		// 取消修改
		
		parent.$.modalDialog.handler.find('#trialForm').form("load",sel[0]);
	} else {
		// 添加取消
		parent.$.modalDialog.handler.find('#trialForm').form('clear');
		md.find('#trialForm').find("#caseid").val(caseid);
		//trialGrid.datagrid('selectRow', 0);
	}
	editflag=false;
	controllEditButtons(parent.$.modalDialog.handler,editflag);
}


//庭审笔录列表
var currentRow_trial = -1;
function trialDataGrid(mdDialog,caseid){
	trialGrid = mdDialog.find("#trialTable").datagrid({
		height: 540,
		width:'100%',
		title: '庭审笔录列表',
		collapsible: false,
		url : trialUrls.queryTrialUrl,
		queryParams : {caseid:caseid},
		singleSelect: true,
		rownumbers : true,
		pagination : true,
		
		pageSize : 10,
		idField: 'trid',
		loadMsg : "正在加载，请稍候......",
		columns: [ [ {field : "trid",checkbox : true}  
	        ,{field : "casename",title : "案件名称",halign : 'center',width:100,sortable:true}
	        ,{field : "casttime",title : "时间",halign : 'center',	width:140,sortable:true}
	        ,{field : "place",title : "地点",halign : 'center',	width:120,sortable:true}
	        ,{field : "courter",title : "庭审人员",halign : 'center',width:100,sortable:true	}
	        ,{field : "moderator",title : "主持人",halign : 'center',width:100,sortable:true}
	        
	        //隐藏字段--采购结果信息表
	        ,{field : "caseid",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "recorder",title : "书记员",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "participants",title : "庭审参加人员",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "remark",title : "庭审记录",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ] ],
	           toolbar: [{ id:"trial_add",
	        	   text: '添加', iconCls: 'icon-add', handler: function () {
	        		   var form = mdDialog.find("#trialForm");//form
	            	   form.form("clear");
	            	   form.find("#caseid").val(caseid);
	        	   }
	           },'-', {id:"trial_del",
	        	   text: '删除', iconCls: 'icon-remove', handler: function () {
	        		   var row = mdDialog.find("#trialTable").datagrid('getSelections');
	                   if(row.length==0){
	                   		easyui_warn("请选择要删除的数据！");
	                   		return;
	                   }else{
	                	   toTrialDelete(row[0].trid);
	                   }
	        	   }
	           }]
		,onClickRow:function(rowIndex, rowData){//单击打开修改页面
			var form = mdDialog.find("#trialForm");//form
			form.form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
         },
		onSelect : function(rowIndex, rowData) {
			mdDialog.find("#trialForm").form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
		}
	});
	return trialGrid;
}

function controllEditButtons(md,editflag){
	if(editflag){
		md.find('#linkbutton_save').linkbutton('enable');
		md.find('#linkbutton_cancel').linkbutton('enable');
	}else {
		md.find('#linkbutton_save').linkbutton('disable');
		md.find('#linkbutton_cancel').linkbutton('disable');
	}
}

