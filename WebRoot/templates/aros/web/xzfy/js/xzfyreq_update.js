//请求路径
var urls = {
	updateCommitUrl:"CbiWebController_update.do" 
};

//默认加载
$(function() {
	
	comboboxFuncByCond("apptype", "B_CASEBASEINFO_APPTYPE");	//申请人类型
	comboboxFuncByCond("idtype", "B_CASEBASEINFO_IDTYPE");		//证件类型
	comboboxFuncByCond("deftype", "B_CASEBASEINFO_DEFTYPE");	//被申请人类型
	comboboxFuncByCond("admtype", "ADMINLEVEL");				//行政管理类型
	comboboxFuncByCond("casetype", "B_CASEBASEINFO_CASETYPE");  //申请复议事项类型
	comboboxFuncByCond("ifcompensation", "IFCOMPENSATION"); 	//申请复议事项类型
	comboboxFuncByCond("region", "SYS_AREA");					//行政区域
	
	var caseid = $("#caseid").val();
	showWebFileDiv($("#filetd"), true, "XZFY", caseid, "");
});

//提交表单
function submitForm(){
	
	var isValid = $("#xzfyReqForm").form('validate');
	if (!isValid) {
		easyui_warn("请将申请信息填写完整", null);
		return;
	}
		
	$.messager.confirm("确定", "确认要提交复议申请？", function(r) {
		if (r) {
			$("#xzfyReqForm").form("submit", {
				url:urls.updateCommitUrl,
				onSubmit:function() {
					$.messager.progress({
						title:"提示",
						text:"数据处理中，请稍后...."
					});
					var isValid = $("#xzfyReqForm").form("validate");
					if (!isValid) {
						$.messager.progress("close");
						easyui_warn("请将申请信息填写完整", null);
					}
					return isValid;
				},
				success:function(result) {
					$.messager.progress("close");
					result = $.parseJSON(result);
					if (result.success) {
						easyui_warn("提交成功，查询码为【" + result.title +"】，请妥善保存以便查询", null);
					} else {
						$.messager.alert("错误", result.title, "error");
					}
				}
			});
		}
	});
}

/**
 * 附件上传
 * @param elementcode
 */
function uploadFile(elementcode){
	
	var fjkeyid = $('#caseid').val();
	
	var href = "FileManageController_uploadify.do?flag=dialog&fileItemmids=itemids&tdId=filetd&elementcode="+elementcode+"&keyid="+fjkeyid;
	
	$('#uploadifydiv').dialog({
		title:"附件管理",
		width:700,
		height:300,
		href:href,
		iconCls:'icon-files',
		modal:true,
		buttons:[{
			text:"确认",
			iconCls:"icon-save",
			handler:function(){
				$('#uploadifydiv').dialog("close");
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function(){
				$('#uploadifydiv').dialog("close");
			}
		}]
	});
}