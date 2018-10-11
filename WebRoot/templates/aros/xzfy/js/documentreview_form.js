//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	getClobContentValue: baseUrl+"getClobContentValue.do" // 查询回访单内容
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
	var typeFlag = "review";
	// 转送登记表
	// 转送登记表
	if(nodeid == '90'){
		typeFlag = "transregist";
	}else if(nodeid == '190'){
		// 行政复议决定书
		typeFlag = "decisiondoc";
	}
	$.post(urls.getClobContentValue, {
			caseid: caseid,
			nodeid: nodeid,
			protype: protype,
			typeFlag: typeFlag
		}, function(data){
			//给父窗口的控件设值
			editor.html(data);
			//将编辑器值赋值给隐藏域content
			$("#contents").val(data);
	}, "json");
}
