// 请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	getClobContentValue: baseUrl+"getClobContentValue.do"			   // 查询文书内容
};

var editor;
// 默认加载
$(function() {
	// 初始化编码器
	editor = KindEditor.create($('#contentEdit'), {
		resizeType : 1,
		allowPreviewEmoticons : false,
		allowImageUpload : false,
		items : [
			'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
			'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
			'insertunorderedlist', '|', 'emoticons']
	});
	openNoticeInfo();
});

/**
 * 打开文书处理页面
 * @param row
 */
function openNoticeInfo() {
	$.post(urls.getClobContentValue, {
			caseid: caseid,
			nodeid: nodeid,
			protype: protype,
			typeFlag: "summary"
		}, function(data){
			//给父窗口的控件设值
			editor.html(data);
			//将编辑器值赋值给隐藏域content
			$("#contents").val(data);
	}, "json");
}
