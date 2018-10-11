function panelctlForPro(_index) {
	if (panel_flag[_index] == '0') {
		$(panel_ctl_handles[_index].panelname).panel('open');
		$(panel_ctl_handles[_index].buttonid).linkbutton({
			iconCls : 'icon-collapse'
		});
		panel_flag[_index] = '1';
	} else {
		$(panel_ctl_handles[_index].panelname).panel('close');
		$(panel_ctl_handles[_index].buttonid).linkbutton({
			iconCls : 'icon-expand'
		});
		panel_flag[_index] = '0';
	}
	var gridname = panel_ctl_handles[_index].gridname;
	var panelname = panel_ctl_handles[_index].panelname;
	autoResizeGrid(gridname, panelname);
}
/**
 * 工作流流程信息
 * @param wfid
 */
function showWorkFlowModalDialog(wfid) {
	if(wfid=='' || wfid==null){
		easyui_warn("该数据还没有工作流信息！",null);
		return;
	}
	var href = contextpath
			+ "ppms/discern/ProjectDiscernController/workFlowMessage.do?wfid="
			+ wfid;
	parent.$.modalDialog({
		title : "流程信息",
		width : 700,
		height : 580,
		href : href,
		onLoad : function() {
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

/**
 *上传附件组件
 * @param dialogId div
 * @param fileName 页面要显示文件的名称
 * @param fileKey 数据库要存的值
 * @param showFileLength 名字显示长度
 */
function showUploadifyModalDialog(dialogId,fileName,fileKey,showFileLength){
	var href = contextpath+ "ppms/discern/ProjectDiscernController/uploadify.do?elementcode=0";
	var downloadFileUrl =  contextpath + "base/filemanage/fileManageController/downLoadFile.do";
	dialogId.dialog(
			{
				title : "选择附件",
				width : 600,
				height : 150,
				href : href,
				iconCls : 'icon-files',
				modal : true,
				onLoad : function() {
					//附件上传控件
					var upl = $("#uploadify").uploadify({
				        swf : 'component/uploadify/uploadify.swf',
				        uploader : contextpath + 'base/filemanage/fileManageController/add.do;jsessionid=' + sessionId,//后台处理的请求
				        cancelImg : 'component/uploadify/uploadify-cancel.png',
				        queueID : "fileQueue",//与下面的id对应
				        queueSizeLimit : 1,//选择文件的个数
				        uploadLimit : 3,//总上传文件数
				        simUploadLimit : 1,//允许同时上传的个数
				        fileTypeExts : '*.doc;*.docx;*.pdf;*.zip;*.rar', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
				        fileTypeDesc : '上传文件格式只能为：doc、docx和pdf格式',
				        auto : true,//是否自动上传
				        multi : true,
				        rollover : true,
				        progressData : 'percentage', //进度 percentage或speed
				        removeCompleted : true,
				        removeTimeout:0,
				        method : 'post',
				        fileObjName : 'addfile',
				        formData : {'elementcode' : "0", 'savemode' : "1", 'savePath' : ''},//和后台交互时，附加的参数    savemode:0,1 0-保存磁盘；1-保存数据库
				        fileSizeLimit : '100MB', //上传文件大小设置 单位可以是B、KB、MB、GB 
				        buttonText : '选择文件', //
				        onInit : function(){
				        },
				        onUploadSuccess : function(file, data, response) {
				        	var data = jQuery.parseJSON(data);
				        	var form = parent.$("#"+fileName).parents("form");
				        	var fileType = file.type;
				        	var fileImg = "";
				        	if(fileType==".doc" || fileType==".docx"){
				        		fileImg="component/uploadify/doc.gif";
				        	}else if(fileType==".xls" || fileType==".xlsx"){
				        		fileImg="component/uploadify/xls.gif";
				        	}else if(fileType==".rar" || fileType==".zip"){
				        		fileImg="component/uploadify/zip.gif";
				        	}else if(fileType==".txt"){
				        		fileImg="component/uploadify/txt.gif";
				        	}else{
				        		fileImg="component/uploadify/default.gif";
				        	}
				        	var l = parent.$("#"+fileName).children("div").length;
				        	for(var i=0;i<l;i++){
				        		//移除之前的附件
				        		parent.$("#"+fileName).children("div").eq(i).remove();
				        	}
				        	var form = parent.$("#"+fileName).parents("form");
				        	//寻找是否存在隐藏域
				        	if(form.find("#"+fileKey).length==0){
				        		form.append("<input id='"+fileKey+"' type='hidden' name='"+fileKey+"'/>");
				        	}
				        	var olditemid = form.find("#"+fileKey).val();
				        	//如果是替换附件，则将原先附件删除
				        	if(olditemid != ""){
				        		deleteFile(olditemid, '', '');
				        	}
				        	form.find("#"+fileKey).val(data.itemid);
				        	parent.$("#"+fileName).append(
			            			"<div id='"+data.itemid+"' fileName='"+data.filename+"' style='display:block;float:left;height: 18px; line-height: 14px;' isSave='0'>" +
			            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
			            			"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"' title='"+data.filename+"' style='width:500px;text-decoration:none;color:#0000FF' >"+getsubstring(data.filename,showFileLength)+"</a>" +
			            			"<img src='component/uploadify/delete.gif' align='absmiddle' style='margin-right: 3px;' onclick='deleteFile("+data.itemid+",\"self\",\""+fileKey+"\")'></img>"+
			            			"&nbsp"+
			            			"<a href = '#' onClick='showFileDetail("+data.itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
			            			"</div>"
			            	);
				        	dialogId.dialog('close');
				        }
					 });
				}
			});
}
//上传附件
function uploadFile(fileName,fileKey,showFileLength){
	var l = parent.$("#"+fileName).children("div").length;
	if(l>0){
		parent.$.messager.confirm("附件替换确认", "已上传附件，重新上传将替换原附件！", function(r) {
			if (r) {
				showUploadifyModalDialog($('#uploadifydiv'),fileName,fileKey,showFileLength);
			}
		});
	}else{
		showUploadifyModalDialog($('#uploadifydiv'),fileName,fileKey,showFileLength);
	}
}
/*
 * @itemid 附件主键
 * @param createUser 上传人
 */
function deleteFile(itemid,createUser,fileKey) {
	$.post(contextpath + 'base/filemanage/fileManageController/delete.do', {
		itemids : itemid
	}, function(result) {
	}, 'json');
	//移除附件
	parent.$("'div[id=" + itemid + "]'").remove();
	//将文件itemid滞空
	parent.$("'input[id="+fileKey+"]'").val("");
}
//截取字段长度
function getsubstring(content, sublength) {
	var length = getBt(content);
	var substring="";
	if (sublength >= length) {
		return content;
	} else {
		if (sublength < 0) {
			
		} else {
			var i = 0;
			var j = 0;
			for (; i < length; i++) {
				if (getBt(content.substring(0, i + 1)) >= sublength) {
					j = getBt(content.substring(0, i + 1));
					break;
				}
			}
			substring = j > sublength ? content.substring(0, i): content.substring(0, i + 1);
		}
	}
	return substring+"...";
}
//获取字符数
function getBt(str){
    var char = str.match(/[^\x00-\xff]/ig);
    return str.length + (char == null ? 0 : char.length);
}
/**
 * 
 * @param dialogId 要追加显示文档内容的id
 * @param itemid 文件itemid
 * @param isEdit 是否带有删除按钮
 * @param showFileLength 显示名称长度
 * @param fileKey 存放文件的关键字
 */
function showFileDiv(dialogId,isEdit,itemid,showFileLength,fileKey){
	var downloadFileUrl =  contextpath + "base/filemanage/fileManageController/downLoadFile.do";
	var rOlineFileUrl =  contextpath + "base/filemanage/fileManageController/showFile.do";
	$.post(contextpath + "/base/filemanage/fileManageController/queryFilesByItemid.do", {
		itemid : itemid,
		showFileLength : showFileLength
		}, function(result) {
			var fileInfo = "";
			if(isEdit){
				for (var i=0; i<result.length; i++) {
					var f= result[i].originalfilename.split(".");
					var fileType = "."+f[f.length-1];
					var fileImg = "";
		        	if(fileType==".doc" || fileType==".docx"){
		        		fileImg="component/uploadify/doc.gif";
		        	}else if(fileType==".xls" || fileType==".xlsx"){
		        		fileImg="component/uploadify/xls.gif";
		        	}else if(fileType==".rar" || fileType==".zip"){
		        		fileImg="component/uploadify/zip.gif";
		        	}else if(fileType==".txt"){
		        		fileImg="component/uploadify/txt.gif";
		        	}else if(fileType==".pdf"){
		        		fileImg="component/uploadify/pdf.png";
		        	}else if(fileType==".ppt"){
		        		fileImg="component/uploadify/ppt.png";
		        	}else{
		        		fileImg="component/uploadify/default.gif";
		        	}
					
					fileInfo += 
						"<div id='"+result[i].itemid+"' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].originalfilename+"' userCode='"+result[i].usercode+"'>" +
            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
            			"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' title='文件名称："+result[i].originalfilename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].omitFileName+"</a>" +
            			"<img src='component/uploadify/delete.gif' align='absmiddle' style='margin-right: 3px;' onclick='deleteFile("+result[i].itemid+",\""+result[i].usercode+"\",\""+fileKey+"\")'></img>"+
            			"&nbsp"+
            			"<a href = '#' onClick='showFileDetail("+result[i].itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
            			"</div>";
				}
			}else{
				for (var i=0; i<result.length; i++) {
					var f= result[i].originalfilename.split(".");
					var fileType = "."+f[f.length-1];
					var fileImg = "";
		        	if(fileType==".doc" || fileType==".docx"){
		        		fileImg="component/uploadify/doc.gif";
		        	}else if(fileType==".xls" || fileType==".xlsx"){
		        		fileImg="component/uploadify/xls.gif";
		        	}else if(fileType==".rar" || fileType==".zip"){
		        		fileImg="component/uploadify/zip.gif";
		        	}else if(fileType==".txt"){
		        		fileImg="component/uploadify/txt.gif";
		        	}else if(fileType==".pdf"){
		        		fileImg="component/uploadify/pdf.png";
		        	}else if(fileType==".ppt"){
		        		fileImg="component/uploadify/ppt.png";
		        	}else{
		        		fileImg="component/uploadify/default.gif";
		        	}
					fileInfo += 
						"<div id='"+result[i].itemid+"' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].originalfilename+"' userCode='"+result[i].usercode+"'>" +
            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
            			"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' title='文件名称："+result[i].originalfilename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].omitFileName+"</a> " +
            			"&nbsp"+
            			"<a href = '#' onClick='showFileDetail("+result[i].itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
            			"</div>";
				}
			}	
			dialogId.append(fileInfo);
			if(parent.$("#readOnline").length==0){
				dialogId.closest("form").eq(0).after("<div id='readOnline'></div>");
			}
	}, 'json');
}
//单位换算
function bytesToSize(bytes) {
	if (bytes === 0)
		return '0 B';
	var k = 1000, // or 1024
	sizes = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ], i = Math
			.floor(Math.log(bytes) / Math.log(k));
	return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}
function showFileDetail(itemid){
	var rOlineFileUrl =  contextpath + "base/filemanage/fileManageController/showFile.do?itemid="+itemid;
	
	$("#readOnline").dialog(
			{
				title : "附件预览",
				width : 875,
				height : 630,
				href : rOlineFileUrl,
				iconCls : 'icon-files',
				modal : true,
				onLoad : function() {
					//alert(swfPath);
				},
				buttons : [
						{
							text : "确认",
							iconCls : "icon-save",
							handler : function() {
								$("#readOnline").dialog('close');
							}
						},
						{
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								$("#readOnline").dialog('close');
							}
						} ]
				,onClose:function(){
					var delSwfFile =  contextpath + "base/filemanage/fileManageController/delSwfFile.do";
					
					var aj = $.ajax({    
					    url:delSwfFile,// 跳转到 action    
					    data:{    
					    	delswf:swfPath 
					    },    
					    type:'post',    
					    cache:false,    
					    dataType:'json',    
					    success:function(data) {    
					         
					     },    
					     error : function() {    
					           
					     }    
					});  
				}
			});
}

/**
 * 是否可撤回验证
 * 返回一个flag标志
 * true可返回，false不可返回
 * @param xmhj
 * @param dqjd
 * @param projectid
 * @return
 */
function recallYN(xmhj,dqjd,projectid){
	var flag = true;
	$.ajax({
		type : "post",
		url : contextpath + "ppms/discern/ProjectPrepareReviewController/recallYN.do",
		data : {
			xmhj : xmhj,//项目环节
			dqjd : dqjd,//当前阶段
			projectid : projectid//项目主键id
		},
		async : false,
		success : function(result){
			if (result.success){
				flag = true;
			} else {
				flag = false;
			}
		}
	});
	return flag;
}
