// 单位换算
function bytesToSize(bytes) {
	if (bytes === 0)
		return '0 B';
	var k = 1000, // or 1024
	sizes = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ], i = Math
			.floor(Math.log(bytes) / Math.log(k));
	return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

function showWorkFlowModalDialog(wfid) {
	if(wfid=='' || wfid==null){
		easyui_warn("该数据还没有工作流信息！",null);
		return;
	}
	var href = contextpath
			+ "manage/change/AccountChangeController/workFlowMessage.do?wfid="
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
 * 显示已上传的的表(样式2)
 * @param dialogId 要显示的div对象
 * @param isEdit 是否可删除（true：可以删除，false：不可以删除）
 * @param elementcode 业务类型
 * @param keyid 关联的业务主键
 * @param showFileLength 要显示的文件长度，取的文件名称值从omitFileName中取
 * @param stepid 节点id
 */
function showFileDiv(dialogId,isEdit,elementcode,keyid,stepid){
	var downloadFileUrl =  contextpath + "base/filemanage/fileManageController/downLoadFile.do";
	var rOlineFileUrl =  contextpath + "base/filemanage/fileManageController/showFile.do";
	$.post(contextpath + "/base/filemanage/fileManageController/queryFiles.do", {
		elementcode : elementcode,
		keyid : keyid,
		stepid : stepid,
		showFileLength : 50
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
	            			"<img src='component/uploadify/delete.gif' align='absmiddle' style='margin-right: 3px;' onclick='deleteFile("+result[i].itemid+",\""+result[i].usercode+"\")'></img>"+
	            			//"&nbsp"+
	            			//"<a href = '#' onClick='showFileDetail("+result[i].itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
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
	            			/*"<a href='"+rOlineFileUrl+"?itemid="+result[i].itemid+"' target='_blank'>预览</a>"+*/
	            			//"&nbsp"+
	            			//"<a href = '#' onClick='showFileDetail("+result[i].itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
	            			"</div>";
					}
				}
			
				dialogId.append(fileInfo);
				if(parent.$("#readOnline").length==0){
					dialogId.closest("form").eq(0).after("<div id='readOnline'></div>");
				}
				
	}, 'json');
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
					alert(swfPath);
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
 * 显示已上传的的表（样式1）
 * @param dialogId 要显示的div对象
 * @param isEdit 是否可删除（true：可以删除，false：不可以删除）
 * @param elementcode 业务类型
 * @param keyid 关联的业务主键
 * @param showFileLength 要显示的文件长度，取的文件名称值从omitFileName中取
 * @param stepid 节点id
 */
function showFileTable(dialogId,isEdit,elementcode,keyid,stepid){
	var downloadFileUrl =  contextpath + "base/filemanage/fileManageController/downLoadFile.do";
	$.post(contextpath + "/base/filemanage/fileManageController/queryFiles.do", {
		elementcode : elementcode,
		keyid : keyid,
		stepid : stepid,
		showFileLength : 43
		}, function(result) {
			var fileInfo = "";
				if(isEdit){
					for (var i=0; i<result.length; i++) {
						fileInfo += 
						"<tr id='"+result[i].itemid+"' fileName='"+result[i].originalfilename+"'>" +
						"<td style='width:74%;word-WRBP: break-word'>"+
						"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' style='text-decoration:none;color:#0000FF' title='文件名称："+result[i].originalfilename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"'>"+result[i].omitFileName+"</a></td>"+
						"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' style='text-decoration:none;color:#0000FF' >下载</a></td>"+
						"<td style='width:13%'><a href='#' style='text-decoration:none;color:#0000FF' onclick='deleteFile("+result[i].itemid+",\""+result[i].usercode+"\")'>删除</a></td></tr>";
					}
				}else{
					for (var i=0; i<result.length; i++) {
						fileInfo += 
						"<tr id='"+result[i].itemid+"'>" +
						"<td style='width:87%;word-WRBP: break-word'>"+
						"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' style='width:500px;text-decoration:none;color:#0000FF' title='文件名称："+result[i].originalfilename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"'>"+result[i].omitFileName+"</a></td>"+
						"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' style='width:100px;text-decoration:none;color:#0000FF' >下载</a></td>";
					}
				}
			
				dialogId.append(fileInfo);
			
	}, 'json');
}

/**
 * 上传控件（注意隐藏域的设定itemids）
 * @param control：上传附件的控件ID
 * @param tableControler ：显示的table对象
 * @param form 上传的form
 * @param sessionid：session
 * @param elementcode ：业务类型
 * @param fileQueue ：上传队列（显示上传队列的id）
 * @param fileItemmids 隐藏域
 */
function uploadify_form(control, tableControl,form,sessionid, elementcode,fileQueue,fileItemmids) {
	
	var downloadFileUrl =  contextpath + "base/filemanage/fileManageController/downLoadFile.do";
	control.uploadify({
        swf : 'component/uploadify/uploadify.swf',
        uploader : contextpath + 'base/filemanage/fileManageController/add.do;jsessionid=' + sessionid,//后台处理的请求
        cancelImg : 'component/uploadify/uploadify-cancel.png',
        queueID : fileQueue,//与下面的id对应
        queueSizeLimit : 10,//选择文件的个数
        uploadLimit : 10,//总上传文件数
        simUploadLimit : 10,//允许同时上传的个数
       // fileTypeExts : '*.rar;*.zip', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
        //fileTypeDesc : 'rar文件或zip文件',
        auto : true,//是否自动上传
        multi : true,
        rollover : true,
        progressData : 'percentage', //进度 percentage或speed
        removeCompleted : true,
        removeTimeout:0,
        method : 'post',
        fileObjName : 'addfile',
        formData : {'elementcode' : elementcode, 'savemode' : "1", 'savePath' : ''},//和后台交互时，附加的参数    savemode:0,1 0-保存磁盘；1-保存数据库
        fileSizeLimit : '100MB', //上传文件大小设置 单位可以是B、KB、MB、GB 
        buttonText : '选择上传文件', //
        onUploadSuccess : function(file, data, response) {
        	var data = jQuery.parseJSON(data);
        	
        	if(form.find("#"+fileItemmids).length==0){
        		form.append("<input id='"+fileItemmids+"' type='hidden' name='"+fileItemmids+"'/>");
        	}
        	
        	//验证是否存在同名文件
        	var l = tableControl.find("tr").length;
        	var flag=false;
        	for(var i=0;i<l;i++){
        		//var hasFileName=tableControl.find("tr").eq(i).children("td").eq(0).find("a").text();
        		var hasFileName=tableControl.find("tr").eq(i).attr("fileName");
        		if(data.filename==hasFileName){
        			flag=true;
        		}
        	}
        	
        	if(flag==true){
        		easyui_warn("已存在该同名文件！");
    			$.post(contextpath + 'base/filemanage/fileManageController/delete.do', {
    				itemids : data.itemid
    			}, function(result) {
    			}, 'json');
    			flag=false;
        	}else{
        		
	        	var itemids = form.find("#"+fileItemmids).val();
	        	if(itemids==null || itemids==""){
	        		form.find("#"+fileItemmids).val(data.itemid);
	        	}else{
	        		form.find("#"+fileItemmids).val(itemids+","+data.itemid);
	        	}
	        	
	        	var $tr=tableControl.find("tr").eq(0);
	            if($tr.size()==0){
	            	tableControl.append(
	            			"<tr id='"+data.itemid+"' fileName='"+data.filename+"'>" +
							"<td style='width:74%;word-WRBP: break-word'>"+
							"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"' style='width:500px;text-decoration:none;color:#0000FF' >"+data.filename+"</a>" +
									"<span>&nbsp;&nbsp;<font size='0.5' color='red'>(上传完成，未保存)</font></span></td>"+
							"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+data.itemid+"' style='width:100px;text-decoration:none;color:#0000FF' >下载</a></td>"+
							"<td style='width:13%'><a href='#' style='width:100px;text-decoration:none;color:#0000FF' onclick='deleteFile("+data.itemid+",\"self\")'>删除</a></td></tr>"	
	            	);
	            }else{
	            	$tr.before(
	            			"<tr id='"+data.itemid+"' fileName='"+data.filename+"'>" +
							"<td style='width:74%;word-WRBP: break-word'>"+
							"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"' style='width:500px;text-decoration:none;color:#0000FF' >"+data.filename+"</a>" +
									"<span>&nbsp;&nbsp;<font size='0.5' color='red'>(上传完成，未保存)</font></span></td>"+
							"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+data.itemid+"' style='width:100px;text-decoration:none;color:#0000FF' >下载</a></td>"+
							"<td style='width:13%'><a href='#' style='width:100px;text-decoration:none;color:#0000FF' onclick='deleteFile("+data.itemid+",\"self\")'>删除</a></td></tr>"	
	            	);
	            }
        	}
        }
	 });
};

//单位换算
function bytesToSize(bytes) {
    if (bytes === 0) return '0 B';
    var k = 1000, // or 1024
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
   return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

/*
 * @itemid 附件主键
 * @param createUser 上传人
 */
function deleteFile(itemid,createUser) {
	if(createUser==_global_usercode_ || createUser=="self"){
		$.post(contextpath + 'base/filemanage/fileManageController/delete.do', {
			itemids : itemid
		}, function(result) {
		}, 'json');
		$("'tr[id=" + itemid + "]'").remove();
		$("'div[id=" + itemid + "]'").remove();
	}else{
		easyui_warn("非本用户上传文件，不能删除！");
	}
}
function panelctlForFam(_index) {
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
 *上传附件组件
 * @param dialogId div
 * @param fileItemmids 隐藏域
 * @param elementcode 业务类型
 */
function showUploadifyModalDialog(dialogId,fileItemmids,elementcode,keyid){
	var href = contextpath+ "base/filemanage/fileManageController/uploadify.do?fileItemmids="+fileItemmids+"&tdId=filetd&elementcode="+elementcode+"&keyid="+keyid;
	dialogId.dialog({
				title : "附件管理",
				width : 700,
				height : 300,
				href : href,
				iconCls : 'icon-files',
				modal : true,
				onLoad : function() {
				},
				buttons : [{
							text : "确认",
							iconCls : "icon-save",
							handler : function() {
								dialogId.dialog('close');
							}
						},{
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								dialogId.dialog('close');
							}
						}]
			});
}
//添加遮罩层
function addLockDiv(panel) {
	$("<div class=\"window-mask\"></div>").css({ display: "block", width: panel.width(), height: panel.height()}).appendTo(panel);
}

//移除遮罩层
function removeLockDiv(panel) {
    panel.find("div.window-mask").remove();  
}

