var baseSurveyUrl = contextpath + "aros/xzfy/controller/BSurveyAndTrialController/";
var surveyUrls = {
		//查询项目信息
		querySurveyUrl : baseSurveyUrl+"querySurveyGrid.do",
		surveyUrl : baseSurveyUrl+"surveyinit.do",
		delSurveyUrl : baseSurveyUrl+"toDeleteSurvey.do",
		addSurveyUrl : baseSurveyUrl+"toSaveSurvey.do"
};

var editflag = false;

//默认加载
$(function() {

});
var surveyGrid;
function surveyQuery() {
	var parentGrid = $("#xzfyReviewDataGrid");
	var selectRow = parentGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请先选择一个案件！",null);
		return;
	}
	var row = parentGrid.datagrid("getSelections")[0];
	var caseid = row.caseid;
	parent.$.modalDialog({
		title : '行政复议调查笔录',
		width : 1000,
		height : 600,
		href : surveyUrls.surveyUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//调查笔录Grid
			surveyDataGrid(mdDialog, caseid);
			//加载combobox
			comboboxFuncByCondFilter(menuid,"sex", "SEX", "code", "name",mdDialog);//项目发起类型
			
			//caseid赋值
			mdDialog.find("#surveyForm").find("#caseid").val(caseid);
			//保存按钮绑定触发事件
			$("#linkbutton_save",mdDialog).on("click",function(){
				submitSurveyForm();
			});
			//取消按钮绑定触发事件
			$("#linkbutton_cancel",mdDialog).on("click",function(){
				rejectSurveyEdit();
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
			mdDialog.find("#investtime").datebox({
				onChange : function(date) {
					editflag=true;
					controllEditButtons(mdDialog,editflag);
				}
			});
			//动态绑定combobox控件的onchange事件
			mdDialog.find("#sex").combobox({
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
	surveyGrid.datagrid('reload'); 
}



//删除
function toSurveyDelete(srid){

	parent.$.messager.confirm("确认删除", "确认删除所选数据？", function(r) {
		if (r) {
			$.post(surveyUrls.delSurveyUrl, {
				menuid : menuid,
				srid : srid
			}, function(result) {
				easyui_auto_notice(result, function() {
					surveyGrid.datagrid("reload");
					surveyGrid.datagrid('selectRow',0);
					easyui_info("删除成功！",function(){});
				});
			}, "json");
		}
	});
}

//保存
function submitSurveyForm(){
	var form = parent.$.modalDialog.handler.find('#surveyForm');
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	} 

	form.form("submit",
			{
		url : surveyUrls.addSurveyUrl,
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
				surveyGrid.datagrid('reload');					

				surveyGrid.datagrid('clearSelections');
				var caseid = form.find("#caseid").val();
				form.form("clear");
				form.find("#caseid").val(caseid);

				//trialGrid.datagrid('selectRow',0);
				easyui_info("保存成功！",function(){});
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
			}
		});

}

//撤销修改
function rejectSurveyEdit() {
	var md = parent.$.modalDialog.handler;
	var caseid = md.find('#surveyForm').find("#caseid").val();
	var sel = surveyGrid.datagrid('getSelections');
	if (sel.length==1) {
		// 取消修改
		
		md.find('#surveyForm').form("load",sel[0]);
	} else {
		// 添加取消
		md.find('#surveyForm').form('clear');
		md.find('#surveyForm').find("#caseid").val(caseid);
		//surveyGrid.datagrid('selectRow', 0);
	}
	editflag=false;
	controllEditButtons(md,editflag);
}


//调查笔录列表
var currentRow_survey = -1;
function surveyDataGrid(mdDialog,caseid){
	surveyGrid = mdDialog.find("#surveyTable").datagrid({
		height: 540,
		width:'100%',
		title: '调查笔录列表',
		collapsible: false,
		url : surveyUrls.querySurveyUrl,
		queryParams : {caseid:caseid},
		singleSelect: true,
		rownumbers : true,
		pagination : true,
		
		pageSize : 10,
		idField: 'srid',
		loadMsg : "正在加载，请稍候......",
		columns: [ [ {field : "srid",checkbox : true}  
	        ,{field : "investtime",title : "调查时间",halign : 'center',width:140,sortable:true}
	        ,{field : "place",title : "调查地点",halign : 'center',	width:120,sortable:true}
	        ,{field : "invester",title : "调查人名称",halign : 'center',width:100,sortable:true}
	        ,{field : "efinvester",title : "被调查人名称",halign : 'center',width:100,sortable:true	}
	        ,{field : "recorder",title : "记录人名称",halign : 'center',width:100,sortable:true}
	        
	        //隐藏字段--采购结果信息表
	        ,{field : "caseid",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "sex",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "phone",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "obligrights",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "remark",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "item",title : "隐藏",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ] ],
	           toolbar: [{ id:"survey_add",
	        	   text: '添加', iconCls: 'icon-add', handler: function () {
	        		   var form = mdDialog.find("#surveyForm");//form
	            	   form.form("clear");
	            	   form.find("#caseid").val(caseid);
	        	   }
	           },'-', {id:"survey_del",
	        	   text: '删除', iconCls: 'icon-remove', handler: function () {
	        		   var row = mdDialog.find("#surveyTable").datagrid('getSelections');
	                   if(row.length==0){
	                   		easyui_warn("请选择要删除的数据！");
	                   		return;
	                   }else{
	                	  toSurveyDelete(row[0].srid);
	                   }
	        	   }
	           }]
		,onClickRow:function(rowIndex, rowData){//单击打开修改页面
			var form = mdDialog.find("#surveyForm");//form
			form.form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
         },
		onSelect : function(rowIndex, rowData) {
			mdDialog.find("#surveyForm").form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
		}
	});
	return surveyGrid;
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
