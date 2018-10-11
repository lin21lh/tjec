var baseTrialUrl = contextpath + "aros/xzfy/controller/BSurveyAndTrialController/";
var trialUrls = {
		//查询项目信息
		queryTrialUrl : baseTrialUrl+"queryTrialGrid.do",
		trialUrl : baseTrialUrl+"trialinit.do",
		delTrialUrl : baseTrialUrl+"toDeleteTrial.do",
		addTrialUrl : baseTrialUrl+"toSaveTrial.do"
};

var trialGrid;
var editflag;
//默认加载
var reviewtype = "";
$(function() {
	var mdDialog = parent.$.modalDialog.handler;
	reviewtype = mdDialog.find("#reviewtypeForm").val()
	var caseid = mdDialog.find("#caseidinput").val();
	mdDialog.find("#caseid").val(caseid);
	mdDialog.find("#caseidForm").val(caseid);
	//庭审笔录Grid
	trialDataGrid(mdDialog, caseid,reviewtype);
	mdDialog.find("#trialForm").find("#trialtype").val(reviewtype);
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
	var caseid = mdDialog.find("#caseidinput").val();
	var form = parent.$.modalDialog.handler.find('#trialForm');
	form.find("#caseid").val(caseid);
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
				$("#filetd").html("<img src='component/jquery-easyui-1.4/themes/icons/files.png' style='float:right;cursor:pointer' onClick='uploadFileDiv()'/>");
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
		//trialGrid.datagrid('se；ctRow', 0);
	}
	editflag=false;
	controllEditButtons(parent.$.modalDialog.handler,editflag);
}

//庭审笔录列表
function trialDataGrid(mdDialog,caseid,reviewtype){
	var title = "庭审笔录列表"
	if(reviewtype == '03'){
		title = "委员审议列表"
	}else if(reviewtype == '02'){
		title = "集体讨论列表"
	}
	trialGrid = mdDialog.find("#trialTable").datagrid({
		height: 450,
		width:'100%',
		title: title,
		collapsible: false,
		url : trialUrls.queryTrialUrl + "?reviewtype="+ reviewtype,
		queryParams : {caseid:caseid},
		singleSelect: true,
		rownumbers : true,
		pagination : true,
		pageSize : 10,
		idField: 'trid',
		loadMsg : "正在加载，请稍候......",
		columns: [ [ {field : "trialid",checkbox : true}  
	        ,{field : "trialtime",title : "时间",halign : 'center',	width:140,sortable:true}
	        ,{field : "trialplace",title : "地点",halign : 'center',	width:120,sortable:true}
	        ,{field : "recorder",title : "记录人员",halign : 'center',	width:150,sortable:true,hidden:true	}
	        ,{field : "joiner",title : "参与人",halign : 'center',width:100,sortable:true}
	        ,{field : "matters",title : "事项",halign : 'center',width:100,sortable:true}
	        ,{field : "contents",title : "内容",halign : 'center',	width:80,sortable:true}
	        ] ],toolbar: [{ id:"trial_add",
	        	   text: '添加', iconCls: 'icon-add', handler: function ()
	        	   {
	        		   editflag = true;
	       			   controllEditButtons(mdDialog, editflag);
	        		   var form = mdDialog.find("#trialForm");//form
	        		   // 附件keyid
	   				   form.form("clear");
	   				   form.find("#caseid").val(caseid);
	   				   form.find("#fjkeyid").val(caseid);
	   				   form.find("#trialtype").val(reviewtype);
		   			   // 清空文件列表
	   				   $("#filetd").html("<img src='component/jquery-easyui-1.4/themes/icons/files.png' style='float:right;cursor:pointer' onClick='uploadFileDiv()'/>");
					   // 隐藏听证类型和主持人
	      			   change(form, "");
	        	   }
	           },'-', {id:"trial_del",
	        	   text: '删除', iconCls: 'icon-remove', handler: function ()
	        	   {
	        		   var row = mdDialog.find("#trialTable").datagrid('getSelections');
	                   if (row.length==0)
	                   {
	                   		easyui_warn("请选择要删除的数据！");
	                   		return;
	                   }
	                   else
	                   {
	                	   toTrialDelete(row[0].trialid);
	                   }
	        	   }
	           }],
		onClickRow:function(rowIndex, rowData){//单击打开修改页面
			var form = mdDialog.find("#trialForm");//form
			form.form("load",rowData);
			$("#filetd").html("<img src='component/jquery-easyui-1.4/themes/icons/files.png' style='float:right;cursor:pointer' onClick='uploadFileDiv()'/>");
			showFileDiv($("#filetd"),false,"XZFY_TS_"+rowData.trialtype,rowData.trialid,"");
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
}

function uploadFileDiv(){
    var caseid = $('#caseid').val();
    var trialid = $('#trialid').val();
    var trialtype = $('#trialtype').val();
    if(trialid == null || trialid == '' ||trialid == undefined){
    	trialid = caseid;
    }
    showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", "XZFY_TS_"+trialtype, trialid, "filetd");
}