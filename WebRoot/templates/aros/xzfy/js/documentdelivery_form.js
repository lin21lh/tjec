var baseTrialUrl = contextpath + "aros/xzfy/controller/DeliveryReceiptAndCallbackController/";
var trialUrls = {
		//查询项目信息
		queryReceiptUrl : baseTrialUrl+"queryReceiptGrid.do",
		delUrl : baseTrialUrl+"toDeleteReceipt.do",
		addUrl : baseTrialUrl+"toSaveReceipt.do"
};

var trialGrid;
var editflag;
//默认加载
$(function() {
	var mdDialog = parent.$.modalDialog.handler;
	var caseid = mdDialog.find("#caseidinput").val();
	mdDialog.find("#caseidForm").val(caseid);
	//庭审笔录Grid
	trialDataGrid(mdDialog, caseid);
	//保存按钮绑定触发事件
	$("#linkbutton_save",mdDialog).on("click",function(){
		submitTrialForm();
	});
	//取消按钮绑定触发事件
	$("#linkbutton_cancel",mdDialog).on("click",function(){
		rejectTrialEdit();
	});
});


//保存
function submitTrialForm(){
	var mdDialog = parent.$.modalDialog.handler;
	var form = mdDialog.find('#form');
	var caseid = mdDialog.find("#caseidinput").val();
	form.find("#caseid").val(caseid);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	} 
	form.form("submit",
			{
		url : trialUrls.addUrl,
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
	var caseid = md.find('#form').find("#caseid").val();
	var sel = trialGrid.datagrid('getSelections');
	if (sel.length==1) {
		// 取消修改
		
		parent.$.modalDialog.handler.find('#form').form("load",sel[0]);
	} else {
		// 添加取消
		parent.$.modalDialog.handler.find('#form').form('clear');
		md.find('#form').find("#caseid").val(caseid);
		//trialGrid.datagrid('se；ctRow', 0);
	}
	editflag=false;
	controllEditButtons(parent.$.modalDialog.handler,editflag);
}

//庭审笔录列表
function trialDataGrid(mdDialog,caseid){
	var title = "送达列表"
	trialGrid = mdDialog.find("#Table").datagrid({
		height: 480,
		width:'100%',
		title: title,
		collapsible: false,
		url : trialUrls.queryReceiptUrl,
		queryParams : {caseid:caseid},
		singleSelect: true,
		rownumbers : true,
		pagination : true,
		pageSize : 10,
		loadMsg : "正在加载，请稍候......",
		columns: [ [ {field : "cdrid",checkbox : true}  
	        ,{field : "docname",title : "送达文书名称及编号",halign : 'center',	width:140,sortable:true}
	        ,{field : "receiver",title : "受送达人",halign : 'center',	width:120,sortable:true}
	        ,{field : "deliverydate",title : "送达日期",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "deliverysite",title : "送达地点",halign : 'center',width:100,sortable:true}
	        ,{field : "deliveryway",title : "送达方式",halign : 'center',width:100,sortable:true}
	        ,{field : "processagent",title : "代收人",halign : 'center',	width:80,sortable:true}
	        ,{field : "processserver",title : "送达人",halign : 'center',	width:80,sortable:true}
	        ,{field : "remark",title : "备注",halign : 'center',	width:80,sortable:true}
	        ] ],
	           toolbar: [{ id:"trial_add",
	        	   text: '添加', iconCls: 'icon-add', handler: function () {
	        		   var form = mdDialog.find("#form");//form
	            	   form.form("clear");
	            	   form.find("#caseid").val(caseid);
	        	   }
	           },'-', {id:"trial_del",
	        	   text: '删除', iconCls: 'icon-remove', handler: function () {
	        		   var row = mdDialog.find("#Table").datagrid('getSelections');
	                   if(row.length==0){
	                   		easyui_warn("请选择要删除的数据！");
	                   		return;
	                   }else{
	                	   toDelete(row[0].cdrid);
	                   }
	        	   }
	           }]
		,onClickRow:function(rowIndex, rowData){//单击打开修改页面
			var form = mdDialog.find("#form");//form
			form.form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
         },
		onSelect : function(rowIndex, rowData) {
			mdDialog.find("#form").form("load",rowData);
			editflag=false;
			controllEditButtons(mdDialog,editflag);
		}
	});
	return trialGrid;
}

//删除
function toDelete(id)
{
	parent.$.messager.confirm("确认删除", "确认删除所选数据？", function(r) {
		if (r) {
			$.post(trialUrls.delUrl, {
				menuid : menuid,
				cdrid : id
			}, function(result) {
				easyui_auto_notice(result, function() {
					trialGrid.datagrid("reload");
					trialGrid.datagrid('selectRow',0);
					var mdDialog = parent.$.modalDialog.handler;
					var form = mdDialog.find('#form');
					 form.form("clear");
					easyui_info("删除成功！",function(){});
				});
			}, "json");
		}
	});
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

function uploadFileDiv(){
    var timeLong =$('#timeLong').val();
    var caseid = $('#caseid').val();
    showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", "XZFY_TS", caseid+"_"+timeLong, "filetd");
}


