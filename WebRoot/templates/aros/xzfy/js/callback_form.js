var baseTrialUrl = contextpath + "aros/xzfy/controller/DeliveryReceiptAndCallbackController/";
var trialUrls = {
		//查询项目信息
		queryUrl : baseTrialUrl+"queryCallBackGrid.do",
		delUrl : baseTrialUrl+"toDeleteCallBack.do",
		addUrl : baseTrialUrl+"toSaveCallBack.do"
};

var trialGrid;
var editflag;
//默认加载
$(function() {
	var mdDialog = parent.$.modalDialog.handler;
	comboboxFuncByCondFilter(menuid, "isattitude", "ISORNOT", "code", "name");	                // 行政管理类型
	comboboxFuncByCondFilter(menuid, "islegal", "ISORNOT", "code", "name");	                // 行政管理类型
	comboboxFuncByCondFilter(menuid, "iscorruption", "ISORNOT", "code", "name");	                // 行政管理类型
	comboboxFuncByCondFilter(menuid, "isfavoritism", "ISORNOT", "code", "name");	                // 行政管理类型
	comboboxFuncByCondFilter(menuid, "issatisfaction", "ISORNOT", "code", "name");	                // 行政管理类型
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
	var title = "廉政回访列表"
	trialGrid = mdDialog.find("#Table").datagrid({
		height: 450,
		width:'100%',
		title: title,
		collapsible: false,
		url : trialUrls.queryUrl,
		queryParams : {caseid:caseid},
		singleSelect: true,
		rownumbers : true,
		pagination : true,
		pageSize : 10,
		loadMsg : "正在加载，请稍候......",
		columns: [ [ {field : "ciid",checkbox : true}
		,{field : "interviewee",title : "受访人",halign : 'center',	width:80,sortable:true}
		,{field : "contactway",title : "联系方式",halign : 'center',	width:80,sortable:true}
		,{field : "interviewtime",title : "受访时间",halign : 'center',	width:80,sortable:true}
        ,{field : "isattitudename",title : "是否热情",halign : 'center',	width:120,sortable:true}
        ,{field : "islegalname",title : "是否合法",halign : 'center',	width:150,sortable:true,hidden:true	}
        ,{field : "iscorruptionname",title : "是否贪污",halign : 'center',width:100,sortable:true}
        ,{field : "isfavoritismname",title : "是否徇私",halign : 'center',width:100,sortable:true}
        ,{field : "issatisfactionname",title : "是否满意",halign : 'center',	width:80,sortable:true}
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
	                	   toDelete(row[0].ciid);
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
				ciid : id
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
}

function uploadFileDiv(){
    var timeLong =$('#timeLong').val();
    var caseid = $('#caseid').val();
    showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", "XZFY_TS", caseid+"_"+timeLong, "filetd");
}


