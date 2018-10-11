
	
function uploadify_form(sessionid, keyid, elementcode) {
	
	 //附件上传控件
 	$("#uploadify").uploadify({
         swf :  contextpath + 'component/uploadify/uploadify.swf',
         uploader : contextpath + 'base/filemanage/fileManageController/add.do;jsessionid=' + sessionid,//后台处理的请求
         cancelImg : contextpath + 'component/uploadify/uploadify-cancel.png',
         queueID : "fileQueue",//与下面的id对应
         queueSizeLimit : 50,//选择文件的个数
         uploadLimit : 50,//总上传文件数
         simUploadLimit : 50,//允许同时上传的个数
         //fileTypeExts : '*.rar;*.zip', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
         //fileTypeDesc : 'rar文件或zip文件',
         auto : false,//是否自动上传
         multi : true,
         rollover : true,
         progressData : 'percentage', //进度 percentage或speed
         removeCompleted : false,
         removeTimeout:0,
         method : 'post',
         fileObjName : 'addfile',
         formData : {'elementcode' : elementcode, 'savemode' : "1", 'keyid' : keyid},//和后台交互时，附加的参数    savemode:0,1 0-保存磁盘；1-保存数据库
         fileSizeLimit : '100MB', //上传文件大小设置 单位可以是B、KB、MB、GB 
         buttonText : '选择上传文件', //
         onQueueComplete : onQueueComplete
	 });
}

function save_files() {
	$("#uploadify").uploadify('settings', 'formData', { 'title' : $("#file_title").val(), 'remark' : $("#file_remark").val(), 'savemode': 1});
	$("#uploadify").uploadify("upload", "*");
}

var onQueueComplete = function(stats) {
	easyui_info("文件上传成功！", function() {
		$("#addFileDiv").dialog('close');
		$("#fileDataGrid").datagrid('reload');
	});
}