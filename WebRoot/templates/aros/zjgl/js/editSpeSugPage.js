
//默认加载
$(function() {
	$("#remarkTab").focus();
    var groupid = $("#groupid").val();
	showFileDiv($("#filetd"), false, "XZFY_ZT_FJ", groupid , "");
	appJZFile($("#filetd"), "XZFY_ZT_JZ", groupid);
	window.setTimeout("removeFileTable()",100);
});

//移除没有附件的Table
function removeFileTable(){
		if(0 == $("#filetd").children().length){
			$("#filetable").remove();
		}
};
function appJZFile(dialog, elementcode, groupid){
	$.post(contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/queryFiles.do", {
		elementcode : elementcode,
		groupid : groupid,
		}, function(result) {
			for (var i=0; i<result.length; i++) {
				var fileInfo = 
					"<div id='"+result[i].noticeid + groupid + "' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].noticename+"' userCode='"+result[i].usercode+"'>" +
					"<img src='component/uploadify/default.gif' align='absmiddle' style='margin-right: 3px;'></img>"+
					"<a href='"+ contextpath + result[i].url +"' title='文件名称：" + result[i].originalfilename + "' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].noticename+"</a> " +
					"</div>";
				dialog.append(fileInfo);
			}
		}, 'json');
}
/**
 * 保存信息
 */
function saveFormInfo(){
	var form = $('#speSugInfoForm');
	var url = contextpath + "aros/zjgl/controller/BSpesugbaseinfoController/saveSpeSugInfo.do"; 
	var isValid = form.form("validate");
	if (!isValid) {
		parent.$.messager.alert('系统提示', "请录入意见后再保存！", 'info');
		return false;
	}
	form.form("submit", {
		url:url,
		onSubmit:function() {
			parent.$.messager.progress({
				title:"提示",
				text:"数据处理中，请稍后...."
			});
		},
		success:function(result) {
			parent.$.messager.progress("close");
			result = $.parseJSON(result);
			if (result.success) {
				easyui_info(result.title,function(){});
				var iframe = $("#iframe" , parent.document);
				iframe.attr("src", iframe.attr("src"))
			} else {
				parent.$.messager.alert("系统提示", result.title, "error");
				
			}
		}
	});
}