var baseTrialUrl = contextpath + "aros/xzfy/controller/BSurveyAndTrialController/";
var trialUrls = {
		//查询项目信息
		queryTrialUrl: baseTrialUrl + "queryTrialGrid.do",
		trialUrl: baseTrialUrl + "trialinit.do",
		delTrialUrl: baseTrialUrl + "toDeleteTrial.do",
		addTrialUrl: baseTrialUrl + "toSaveTrial.do"
};
var editflag;
var trialGrid;
function trialQuery()
{
	var parentGrid = $("#xzfyReviewDataGrid");
	var selectRow = parentGrid.datagrid('getChecked');
	if (null == selectRow || selectRow.length == 0||selectRow.length > 1)
	{
		easyui_warn("请先选择一个案件！",null);
		return;
	}
	var row = selectRow[0];
	var caseid = row.caseid;
	parent.$.modalDialog({
		title : '行政复议庭审记录',
		width : 1000,
		height : 600,
		href : trialUrls.trialUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			//庭审记录Grid
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
			// 庭审类型
			comboboxFuncByCondFilter(menuid, "trialtype", "TRIALTYPE", "code", "name", mdDialog);
			// 听证类型
			comboboxFuncByCondFilter(menuid, "htype", "HTYPE", "code", "name", mdDialog);
			// 操作按钮显示与置灰
			editflag = false;
			controllEditButtons(mdDialog, editflag);
			// 编辑时操作按钮显示
			mdDialog.find("input:enabled,textarea").on("change",function(){
				editflag = true;
				controllEditButtons(mdDialog, editflag);
			});
			
			// 动态绑定日期控件的onchange事件
			mdDialog.find("#casttime").datetimebox({
				onChange : function(date) {
					editflag = true;
					controllEditButtons(mdDialog, editflag);
				}
			});
			
			// 动态展示听证类型和主持人
			mdDialog.find("#trialtype").combobox({
				onSelect : function (param){
					var trialtype = param.id;
					// 庭审类型是听证，展示听证类型/主持人
					change(mdDialog, trialtype);
				}
			});
		}
	});
}

// 页面刷新
function showReload()
{
	parent.$.modalDialog.handler.find("#trialGrid").datagrid('reload'); 
}

// 删除
function toTrialDelete(trialid)
{
	parent.$.messager.confirm("确认删除", "确认删除所选数据？", function(r) {
		if (r) {
			$.post(trialUrls.delTrialUrl, {
				menuid : menuid,
				trialid : trialid
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
function submitTrialForm()
{
	if (!editflag)
	{
		return;
	}
	
	var form = parent.$.modalDialog.handler.find('#trialForm');
	var type = form.find('#trialtype').combobox('getValue');
	if ("01" != type)
	{
		form.find("#htype").combobox({required:false});
		form.find("#moderator").textbox({required:false});
		// 清空原来的值
		form.find("#htype").combobox('setValue', '');
		form.find("#moderator").textbox('setValue', '');
	}
	else
	{
		var value = form.find("#htype").combobox("getValue");
		form.find("#htype").combobox({required:true});
		form.find("#moderator").textbox({required:true});
		form.find("#htype").combobox("setValue", value);
	}
	
	var isValid = form.form('validate');
	if (!isValid)
	{
		return;
	} 
	form.form("submit", {
		url : trialUrls.addTrialUrl,
		onSubmit: function() {
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			var isValid = form.form('validate');
			if (!isValid)
			{
				parent.$.messager.progress('close');
			}
			return isValid;
		},
		success: function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success)
			{
				var newgrid = trialGrid.datagrid('reload');
				trialGrid.datagrid('clearSelections');
				
				var caseid = form.find("#caseid").val();
				// 附件keyid
				var fjkeyid = form.find("#fjkeyid").val();
				form.form("clear");
				form.find("#caseid").val(caseid);
				form.find("#fjkeyid").val(fjkeyid);
				// 清空文件列表
				parent.$.modalDialog.handler.find("#files > div").remove();
				// 隐藏听证类型和主持人
				change(form, "");
				
				easyui_info("保存成功！",function(){});
			}
			else
			{
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

//撤销修改
function rejectTrialEdit()
{
	if (!editflag)
	{
		return;
	}
	
	var md = parent.$.modalDialog.handler;
	var caseid = md.find('#trialForm').find("#caseid").val();
	var fjkeyid = md.find('#trialForm').find("#fjkeyid").val();
	var sel = trialGrid.datagrid('getSelections');
	
	if (sel.length == 1)
	{
		// 取消修改
		md.find('#trialForm').form("load",sel[0]);
		var triltype = md.find('#trialForm').find("#triltype").val();
		// 隐藏听证类型和主持人
		change(md, triltype);
	}
	else
	{
		// 添加取消
		md.find('#trialForm').form('clear');
		md.find('#trialForm').find("#caseid").val(caseid);
		md.find('#trialForm').find("#fjkeyid").val(fjkeyid);
		// 清空文件列表
	    md.find("#files > div").remove();
	    // 隐藏听证类型和主持人
	    change(md, "");
	}
	editflag = true;
	controllEditButtons(md, editflag);
}

//庭审记录列表
var currentRow_trial = -1;
function trialDataGrid(mdDialog, caseid)
{
	trialGrid = mdDialog.find("#trialTable").datagrid({
		height: 540,
		width:'100%',
		title: '庭审笔录列表',
		collapsible: false,
		url: trialUrls.queryTrialUrl,
		queryParams: {caseid:caseid},
		singleSelect: true,
		rownumbers: true,
		pagination: true,
		pageSize: 10,
		idField: 'trialid',
		loadMsg: "正在加载，请稍候......",
		columns: [[ 
            {field:"trialid", checkbox:true},  
	        {field:"trialtypename", title:"庭审类型", halign:'center', width:100, sortable:true},
	        {field:"trialtime", title:"庭审时间", halign:'center', width:140, sortable:true},
	        {field:"trialplace", title:"庭审地点", halign:'center',	width:120, sortable:true},
	        {field:"recorder", title:"记录人", halign:'center', width:100, ortable:true},
	        {field:"joiner", title:"参加人", halign:'center', width:100, sortable:true},
	        //隐藏字段--庭审类型
	        {field:"trialtype", title:"隐藏", halign:'center', width:80, sortable:true, hidden:true},
	        {field:"htype", title:"隐藏", halign:'center', width:80, sortable:true, hidden:true}
	        ]],
	           toolbar: [{ id:"trial_add",
	        	   text: '添加', iconCls: 'icon-add', handler: function ()
	        	   {
	        		   editflag = true;
	       			   controllEditButtons(mdDialog, editflag);
	        		   var form = mdDialog.find("#trialForm");//form
	        		   // 附件keyid
	            	   var fjkeyid =  form.find("#fjkeyid").val();
	   				   form.form("clear");
	   				   form.find("#caseid").val(caseid);
	   				   form.find("#fjkeyid").val(fjkeyid);
		   			   // 清空文件列表
	      			   parent.$.modalDialog.handler.find("#files > div").remove();
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
		onClickRow: function(rowIndex, rowData){//单击打开修改页面
			var form = mdDialog.find("#trialForm");//form
			form.form("load",rowData);
			editflag = true;
			controllEditButtons(mdDialog, editflag);
			// 先清空文件列表
			parent.$.modalDialog.handler.find("#files > div").remove();
			// 再展示文件列表
			showFileDiv(mdDialog.find("#files"), true, "TSJL", rowData.trialid, "");
			// 动态展示听证类型和主持人
			var trialtype = rowData.trialtype;
			change(mdDialog, trialtype);
         },
		onSelect: function(rowIndex, rowData) {
			mdDialog.find("#trialForm").form("load",rowData);
			editflag = true;
			controllEditButtons(mdDialog, editflag);
			// 先清空文件列表
			parent.$.modalDialog.handler.find("#files > div").remove();
			// 再展示文件列表
			showFileDiv(mdDialog.find("#files"), true, "TSJL", rowData.tailid, "");
			// 动态展示听证类型和主持人
			var trialtype = rowData.trialtype;
			change(mdDialog, trialtype);
		}
	});
	return trialGrid;
}

function controllEditButtons(md, editflag)
{
	if (editflag)
	{
		md.find('#linkbutton_save').linkbutton('enable');
		md.find('#linkbutton_cancel').linkbutton('enable');
	}
	else
	{
		md.find('#linkbutton_save').linkbutton('disable');
		md.find('#linkbutton_cancel').linkbutton('disable');
	}
}

// 根据庭审类型判断是否展示听证类型和主持人，标签展示
function change(handler, trialtype)
{
	handler.find("tr[show]").attr("style", "display:none");
	if ("01" == trialtype) 
	{
		handler.find("tr[show]").removeAttr("style");
		handler.find("span[content]").text("听证");
	}
	else if ("02" == trialtype)
	{
		handler.find("span[content]").text("讨论");
	}
	else if ("03" == trialtype)
	{
		handler.find("span[content]").text("审议");
	}
	else
	{
		handler.find("span[content]").text("");
	}
}
