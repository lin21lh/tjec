// 单位换算
function bytesToSize(bytes) {
	if (bytes === 0)
		return '0 B';
	var k = 1000, // or 1024
	sizes = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ], i = Math
			.floor(Math.log(bytes) / Math.log(k));
	return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

/**
 * 行政复议申请流程信息查看
 * @param caseid
 */
function showFlowModalDialogForReq(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForReq.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}


/**
 * 行政复议申请流程信息查看(相似案件查看)
 * @param caseid
 */
function showFlowModalDialogForReq_simcase(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForReq_simcase.do?caseid="
			+ caseid;

	parent.$.modalDialog2({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler2.dialog("close");
			}
		} ]
	});
}


/**
 * 行政复议和解流程信息查看
 * @param caseid
 */
function showFlowModalDialogForCompromise(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForCompromise.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 行政复议恢复流程信息查看
 * @param caseid
 */
function showFlowModalDialogForRecover(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForRecover.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 行政复议终止流程信息查看
 * @param caseid
 */
function showFlowModalDialogForEnd(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForEnd.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 行政复议中止流程信息查看
 * @param caseid
 */
function showFlowModalDialogForSuspend(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForSuspend.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 案件查询
 * @param caseid	案件ID
 * @param protype	流程类型
 */
function showFlowModalDialogForView(caseid, protype) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForView.do?caseid="
			+ caseid + "&protype=" + protype;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 行政复议撤销流程信息查看
 * @param caseid
 */
function showFlowModalDialogForCancel(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForCancel.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 行政复议延期流程信息查看
 * @param caseid
 */
function showFlowModalDialogForDelay(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForDelay.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		} ]
	});
}

/**
 * 行政复议回避流程信息查看
 * @param caseid
 */
function showFlowModalDialogForAvoid(caseid) {
	
	if(caseid == '' || caseid == null){
		easyui_warn("该数据还没有工作流信息", null);
		return;
	}
	
	var href = contextpath
			+ "aros/flow/controller/ProbaseinfoController/queryFlowForAvoid.do?caseid="
			+ caseid;
	
	parent.$.modalDialog({
		title:"流程信息",
		width:800,
		height:600,
		href:href,
		onLoad:function() {
		},
		buttons:[ {
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
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
							"<div id='"+result[i].itemid+"' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].filename+"' userCode='"+result[i].usercode+"'>" +
	            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
	            			"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' title='文件名称："+result[i].filename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].filename+"</a>" +
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
							"<div id='"+result[i].itemid+"' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].filename+"' userCode='"+result[i].usercode+"'>" +
	            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
	            			"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' title='文件名称："+result[i].filename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].filename+"</a> " +
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
		parent.$.messager.confirm("删除确认", "确认要删除该附件？", function(r) {
			if (r) {
				
				$.post(contextpath + 'base/filemanage/fileManageController/delete.do', {
					itemids : itemid
				}, function(result) {
				}, 'json');
				$("'tr[id=" + itemid + "]'").remove();
				$("'div[id=" + itemid + "]'").remove();
			}
		});
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

/**
 * 上传附件组件（一个页面多个上传文件）
 * @param dialogId div
 * @param fileItemmids 隐藏域
 * @param elementcode 业务类型
 */
function showUploadifyModalDialogForMultiple(dialogId,fileItemmids,elementcode,keyid,filetd){
	var href = contextpath+ "base/filemanage/fileManageController/uploadify.do?fileItemmids="+fileItemmids+"&tdId="+filetd+"&elementcode="+elementcode+"&keyid="+keyid;
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

/**
 * js数据验证
 * @author ybb
 * 2016年8月24日17:21:46
 */
$.extend($.fn.validatebox.defaults.rules, {
    idcard: {// 验证身份证
        validator: function (value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message: '身份证号码格式不正确（18位）.'
    },
    minLength: {
        validator: function (value, param) {
            return value.length >= param[0];
        },
        message: '请输入至少（2）个字符.'
    },
    length: { 
    	validator: function (value, param) {
        var len = $.trim(value).length;
        return len >= param[0] && len <= param[1];
    	},
        message: "输入内容长度必须介于{0}和{1}之间."
    },
    phone: {// 验证电话号码
        validator: function (value) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '格式不正确,请使用下面格式:020-88888888'
    },
    intOrFloat: {// 验证整数或小数
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '请输入数字，并确保格式正确'
    },
    currency: {// 验证货币
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '货币格式不正确'
    },
    qq: {// 验证QQ,从10000开始
        validator: function (value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message: 'QQ号码格式不正确'
    },
    integer: {// 验证整数 可正负数
        validator: function (value) {
            //return /^[+]?[1-9]+\d*$/i.test(value);

            return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
        },
        message: '请输入整数'
    },
    age: {// 验证年龄
        validator: function (value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message: '年龄必须是0到120之间的整数'
    },

    chinese: {// 验证中文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value);
        },
        message: '请输入中文'
    },
    english: {// 验证英语
        validator: function (value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message: '请输入英文'
    },
    unnormal: {// 验证是否包含空格和非法字符
        validator: function (value) {
            return /.+/i.test(value);
        },
        message: '输入值不能为空和包含其他非法字符'
    },
    username: {// 验证用户名
        validator: function (value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message: '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    faxno: {// 验证传真
        validator: function (value) {
            //            return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '传真号码不正确'
    },
    zip: {// 验证邮政编码
        validator: function (value) {
            return /^[1-9]\d{5}$/i.test(value);
        },
        message: '邮政编码格式不正确'
    },
    ip: {// 验证IP地址
        validator: function (value) {
            return /d+.d+.d+.d+/i.test(value);
        },
        message: 'IP地址格式不正确'
    },
    name: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
        },
        message: '请输入姓名'
    },
    date: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            //格式yyyy-MM-dd或yyyy-M-d
            return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
        },
        message: '清输入合适的日期格式'
    },
    msn: {
        validator: function (value) {
            return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
        },
        message: '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    },
    same: {
        validator: function (value, param) {
            if ($("#" + param[0]).val() != "" && value != "") {
                return $("#" + param[0]).val() == value;
            } else {
                return true;
            }
        },
        message: '两次输入的密码不一致'
    },
    //经度验证
    longitude: {
    	 validator: function (value) {
    		 return /^[\-\+]?(0?\d{1,2}\.\d{1,6}|1[0-7]?\d{1}\.\d{1,6}|180\.0{1,6})$/.test(value);
    	 },
    	 message:  '请输入正确的经度(-180~180)'
    },
    //纬度验证
    latitude: {
    	 validator: function (value) {
    		 return  /^[\-\+]?([0-8]?\d{1}\.\d{1,6}|90\.0{1,6})$/.test(value);
    	 },
    	 message:  '请输入正确的纬度(-90~90)'
    },
    //验证汉字
    CHS: {
        validator: function (value) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: "只能输入汉字"
    },
    //移下手机号码验证
    mobile: {//value值为文本框中的值
        validator: function (value) {
            var reg = /^1[3|4|5|7|8|9]\d{9}$/;
            return reg.test(value);
        },
        message: "输入手机号码格局不正确"
    },
    //国内邮编验证
    zipcode: {
        validator: function (value) {
            var reg = /^[1-9]\d{5}$/;
            return reg.test(value);
        },
        message: "邮编必须长短0开端的6位数字"
    },
    //用户账号验证（只能包含 _ 数字 字母） 
    account: {//param的值为[]中值
        validator: function (value, param) {
            if (value.length < param[0] || value.length > param[1]) {
                $.fn.validatebox.defaults.rules.account.message = "用户名长度必须在" + param[0] + "至" + param[1] + "局限";
                return false;
            } else {
                if (!/^[\w]+$/.test(value)) {
                    $.fn.validatebox.defaults.rules.account.message = "用户名只能数字、字母、下划线构成";
                    return false;
                } else {
                    return true;
                }
            }
        }, 
        message: ""
    }
});

/**
 * 上传文件
 * @param elementcode
 * @param accfiletd
 */
function clickUploadDiv(elementcode, accfiletd){
	var caseid = $('#caseid').val();
	var fjkeyid = caseid;
	showUploadifyModalDialogForMultiple(parent.$('#uploadifydiv'), "itemids", elementcode, fjkeyid, accfiletd);
}

var noticeUrls =
{
	queryDoctypeUrl: contextpath + "aros/print/controller/NoticeController/queryDoctype.do?1=1",
	generateUrl: contextpath + "aros/print/controller/NoticeController/generateNotice.do",
	contentUrl: contextpath + "aros/print/controller/NoticeController/noticeContent.do",
	saveUrl: contextpath + "aros/print/controller/NoticeController/saveNotice.do"
};

/**
 * 生成通知书
 */
function generateNotice(datagrid, protype, nodeid, result)
{
	var grid = $("#" + datagrid).datagrid();
	var selectRow = grid.datagrid('getChecked');
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1)
	{
			easyui_warn("请选择一条数据！",null);
			return;
	}
	
	var url = noticeUrls.queryDoctypeUrl;
	// 流程类型
	if (undefined != protype && "" != protype)
	{
		url += "&protype=" + protype;
	}
	// 节点ID 
	if (undefined != nodeid && "" != nodeid)
	{
		url += "&nodeid=" + nodeid;
	}
	// 处理结果
	if (undefined != result && "" != result)
	{
		url += "&result=" + result;
	}
	
	parent.$.modalDialog({
		title:"生成通知书",
		width:600,
		height:300,
		href: noticeUrls.generateUrl,
		onLoad:function()
		{
			var mdDialog = parent.$.modalDialog.handler;
			// 通知书类型
			mdDialog.find('#doctype').combobox({    
			    url: url,
			    valueField:'doctype', 
			    textField:'name'
			});
		},
		buttons:[{
				text:"生成",
				iconCls:"icon-save",
				handler:function()
				{
					var doctype = parent.$.modalDialog.handler.find('#doctype').combobox('getValue');
					if ("" == doctype || undefined == doctype)
					{
						easyui_warn("请选择通知书类型！", null);
						return;
					}
					generateNoticeContent(selectRow[0].caseid, doctype);
				}
			}]
	});
}

/**
 * 生成
 */
function generateNoticeContent(caseid, doctype)
{
	parent.$.modalDialog.handler.dialog('close');
	parent.$.modalDialog({
		title:"通知书",
		width:800,
		height:600,
		href: noticeUrls.contentUrl + "?caseid=" + caseid + "&doctype=" + doctype,
		buttons:[{
				text:"打印",
				iconCls:"icon-print",
				handler:function()
				{
					submitForm(noticeUrls.saveUrl, "noticeForm", caseid, doctype);
				}
			},
		    {
				text:"保存",
				iconCls:"icon-save",
				handler:function()
				{
					checkQuiredInput(caseid, doctype);
				}
			},{
				text:"关闭",
				iconCls:"icon-cancel",
				handler:function()
				{
					parent.$.modalDialog.handler.dialog('close');
				}
			}]
	});
}

function submitNoticeForm(url, form, caseid, doctype)
{
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid)
	{
		return;
	}
	form.form("submit", {
		url : url + "?caseid=" + caseid + "&doctype=" + doctype,
		onSubmit : function() 
		{
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
		success : function(result) {
			parent.$.messager.progress('close');
			result = $.parseJSON(result);
			if (result.success)
			{
				parent.$.modalDialog.handler.dialog('close');
			}
			else
			{
				parent.$.messager.alert('错误', result.title, 'error');
			}
		}
	});
}

function checkQuiredInput(caseid, doctype)
{
	var flag = true;
	
	var inputs = parent.$.modalDialog.handler.find("input[type='text']");
	var length = inputs.length;
	for (var i = 0 ; i < length; i++)
	{
	  if (length > 0)
	  {
		if ($(inputs[i]).val() == '')
		{
			easyui_show_parent("请把光标处填写完整！");
			$(inputs[i]).focus();
			flag = false;
			break;
		}
	  }
	}
	
	var year = parent.$.modalDialog.handler.find("#year").val();
	var month = parent.$.modalDialog.handler.find("#month").val();
	var day = parent.$.modalDialog.handler.find("#day").val();
	if (undefined != year && undefined != month && undefined != day)
	{
		var date = year + "/" + month + "/" + day;
		if (new Date(date).getDate() != day)
		{
			easyui_show_parent("日期输入不正确！");
			parent.$.modalDialog.handler.find("#day").focus();
			flag = false;
		}
	}
	
	if (flag)
	{
		submitNoticeForm(noticeUrls.saveUrl, "noticeForm", caseid, doctype);
	}
}

/**
 * 数据字典下拉菜单 (数据权限范围内)
 * @param comboid 下拉框ID
 * @param elementcode 数据项编码
 * @returns
 */
function comboboxFuncByCond(comboid, elementcode) {
	
	var url = "queryCbByElementcode.do?elementcode="+ elementcode;

	var jcomboBox = $("#" + comboid);
	
	return jcomboBox.combobox({
		editable: false,
		url : url,
		valueField : "id",
		textField : "text",
		panelHeight : 'auto',
		panelMaxHeight :150,
		onChange: function(nv, ov) {
			var icon = jcomboBox.combobox("textbox").prev("span.textbox-addon").find("a:first");
			if (nv) {
				icon.css('visibility', 'visible');
			} else {
				icon.css('visibility', 'hidden');
			}
		}
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
function showWebFileDiv(dialogId, isEdit, elementcode, keyid, stepid){
	var downloadFileUrl = "FileManageController_download.do";
	var rOlineFileUrl = "FileManageController_showFile.do";
	
	$.post("FileManageController_queryFiles.do", {
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
							"<div id='"+result[i].itemid+"' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].filename+"' userCode='"+result[i].usercode+"'>" +
	            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
	            			"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' title='文件名称："+result[i].filename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].filename+"</a>" +
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
							"<div id='"+result[i].itemid+"' style='display:block;float:left;height: 18px; line-height: 14px;' fileName='"+result[i].filename+"' userCode='"+result[i].usercode+"'>" +
	            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
	            			"<a href='"+downloadFileUrl+"?itemid="+result[i].itemid+"' title='文件名称："+result[i].filename+"&#10;上传用户："+result[i].userName+"&#10;上传时间："+result[i].createtime+"&#10;文件大小："+bytesToSize(result[i].filesize)+"' style='width:500px;text-decoration:none;color:#0000FF' >"+result[i].filename+"</a> " +
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

/**
 * 附件上传
 * @param elementcode
 */
function uploadFile(elementcode){
	var fjkeyid = $('#caseid').val();
	
	var href = "FileManageController_uploadify.do?fileItemmids=itemids&tdId=filetd&elementcode="+elementcode+"&keyid="+fjkeyid;
	
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
				parent.$('#uploadifydiv').dialog("close");
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function(){
				parent.$('#uploadifydiv').dialog("close");
			}
		}]
	});
}

/**
 * 获取当前日期，返回yyyy-MM-dd格式
 * @returns {String}
 */
function getNowFormatDate() {
	
    var date = new Date();
    var seperator1 = "-";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    
    return currentdate;
}

/**
 * 计算2个日期之间的时间间隔
 * @param sDate1
 * @param sDate2
 * @returns
 */
function DateDiff(sDate1, sDate2) {
	
    var aDate, oDate1, oDate2, iDays;
    
    aDate = sDate1.split("-");
    oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);  //转换为yyyy-MM-dd格式
    aDate = sDate2.split("-");
    oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
    
    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24); //把相差的毫秒数转换为天数
    
    return iDays;  //返回相差天数
}

//在modalDialog窗口上弹出窗口
$.modalDialog2 = function(options) {
	if ($.modalDialog.handler2 == undefined) {// 避免重复弹出
		var opts = $.extend( {
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.modalDialog.handler2 = undefined;
				$(this).dialog('destroy');
			}
		}, options);
		opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
		return $.modalDialog.handler2 = $('<div/>').dialog(opts);
	}
};