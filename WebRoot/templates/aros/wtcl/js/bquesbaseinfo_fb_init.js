
var Urls = {
		//查询项目信息
		queryQuesUrl : "/aros/yjfk_queryList.do",
		quesUrl : "yjfk_init.do",
		addQuesUrl : "yjfk_save.do"
};

var editflag = false;

//默认加载
$(function() {

});

var quesGrid;
function quesfdQuery() {
	
	var parentGrid = $("#xzfyAcceptDataGrid");
	var selectRow = parentGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请先选择一个案件！",null);
		return;
	}
	var row = parentGrid.datagrid("getSelections")[0];
	var caseid = row.caseid;
	parent.$.modalDialog({
		title : '意见反馈维护',
		width : 1150,
		height : 600,
		href : Urls.quesUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//调查笔录Grid
			quesDataGrid(mdDialog, caseid);
						
			//caseid赋值
			mdDialog.find("#quesForm").find("#caseid").val(caseid);
			//保存按钮绑定触发事件
			$("#linkbutton_save",mdDialog).on("click",function(){
				submitQuesForm(mdDialog);
			});
			//取消按钮绑定触发事件
			$("#linkbutton_cancel",mdDialog).on("click",function(){
				rejectQuesEdit();
			});
			//操作按钮显示与置灰
			editflag=false;
			controllEditButtons(mdDialog,editflag);
			// 编辑时操作按钮显示
			mdDialog.find("input:enabled,textarea").on("change",function(){
				editflag=true;
				controllEditButtons(mdDialog,editflag);
			});			
		}
	});
}

//页面刷新
function showReload() {
	quesGrid.datagrid('reload'); 
}



//删除
function toQuesDelete(srid){

	parent.$.messager.confirm("确认删除", "确认删除所选数据？", function(r) {
		if (r) {
			$.post(Urls.delQuesUrl, {
				menuid : menuid,
				srid : srid
			}, function(result) {
				easyui_auto_notice(result, function() {
					quesGrid.datagrid("reload");
					quesGrid.datagrid('selectRow',0);
					easyui_info("删除成功！",function(){});
				});
			}, "json");
		}
	});
}

//保存
function submitQuesForm(md){
	var form = parent.$.modalDialog.handler.find('#quesForm');
	var question = form.find("#quesdesc").val();
	if ("" == question){
		easyui_warn("没有问题需反馈！", null);
		return;
	}
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	} 

	form.form("submit",
			{
		url : Urls.addQuesUrl,
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
				quesGrid.datagrid('reload');					

				quesGrid.datagrid('clearSelections');
				var caseid = form.find("#caseid").val();
				form.form("clear");
				form.find("#caseid").val(caseid);

				editflag=false;
				controllEditButtons(md,editflag);
				
				quesGrid.datagrid('selectRow', 0);
				
				easyui_info("保存成功！",function(){});
				
				
			} else {
				parent.$.messager.alert('错误', result.title, 'error');
			}
			}
		});

}

//撤销修改
function rejectQuesEdit() {
	var md = parent.$.modalDialog.handler;
	var caseid = md.find('#quesForm').find("#caseid").val();
	var sel = quesGrid.datagrid('getSelections');
	if (sel.length==1) {
		// 取消修改		
		md.find('#quesForm').form("load",sel[0]);
	} else {
		// 添加取消
		md.find('#quesForm').form('clear');
		md.find('#quesForm').find("#caseid").val(caseid);
	}
	editflag=false;
	controllEditButtons(md,editflag);
}


//调查笔录列表
var currentRow_ques = -1;
function quesDataGrid(mdDialog,caseid){
	quesGrid = mdDialog.find("#quesTable").datagrid({
		height: 540,
		width:'100%',
		title: '反馈问题列表',
		collapsible: false,
		url : Urls.queryQuesUrl,
		queryParams : {caseid:caseid},
		singleSelect: true,
		rownumbers : true,
		pagination : true,		
		pageSize : 10,
		idField: 'quesid',
		loadMsg : "正在加载，请稍候......",
		columns: [ [ {field : "quesid",checkbox : true}  
	        ,{field : "quesdesc",title : "问题描述",halign : 'center',width:310,sortable:true}
	        ,{field : "asktime",title : "提交日期",halign : 'center',	width:100,sortable:true}
	        ,{field : "ifanswername",title : "是否回复",halign : 'center',	width:100,sortable:true}
	        //隐藏字段
	        ,{field : "caseid",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "quetype",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "askername",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "phone",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "operator",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "opttime",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ,{field : "ifanswer",title : "隐藏",halign : 'center',	width:80,sortable:true,hidden:true}
	        ] ]
		,onClickRow:function(rowIndex, rowData){//单击打开修改页面
			var form = mdDialog.find("#quesForm");//form
			form.form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
         },
		onSelect : function(rowIndex, rowData) {
			mdDialog.find("#quesForm").form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
		}
	});
	return quesGrid;
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
