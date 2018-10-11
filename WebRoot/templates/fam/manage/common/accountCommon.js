
//零余额 
var iszero = {
	"0" : "否",
	"1" : "是"
};
var recordType ={
		"1" : "账户开立",
		"2" : "账户变更",
		"3" : "账户撤销"
};

// 账户状态（是否撤销）
var account_status = {
	"1" : "正常",
	"9" : "撤销"
};
//申请待处理
var sqdclData =[{text : "全部", value : ""},{text : "已录入", value : "11"},{text : "退回", value : "12"},{text : "撤回", value : "13"}];
//申请已处理
var sqyclData = [{text : "全部", value : ""},{text : "可撤回", value : "21"},{text : "不可撤回", value : "22"}];
//审批待处理
var spdclData =[{text : "全部", value : ""},{text : "未处理", value : "11"},{text : "退回", value : "12"},{text : "撤回", value : "13"}];
//末节点审批待处理
var lastNodeSpdclData =[{text : "全部", value : ""},{text : "未处理", value : "11"}];
//审批已处理
var spyclData = [{text : "全部", value : ""},{text : "审批通过", value : "21"},{text : "审批退回", value : "22"}];
var itemids="";

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
 * 消息服务对象（注意要暂存变量的隐藏域）
 * @param dialogId 控件对象
 * @param phonenumber 手机号码
 * @param message 短信内容
 * @param activityId 节点
 */
function showMessageModalDialog(dialogId, phonenumber, message,activityId) {
	var href = contextpath+ "manage/revoke/controller/AccountRevokeController/message.do";
	var url = contextpath+ "manage/revoke/controller/AccountRevokeController/sendMessage.do";
	var re = /^1[0-9][0-9]\d{8}(\,1[0-9][0-9]\d{8})*\s*$/;
	dialogId.dialog(
					{
						title : "短信服务",
						width : 500,
						height : 250,
						href : href,
						iconCls : 'icon-message',
						modal : true,
						onLoad : function() {
							if (phonenumber != null && phonenumber != "") {
								parent.$("#phonenumbers").textbox("setValue",
										phonenumber);
							}
							if (message != null && message != "") {
								parent.$("#message").textbox("setValue",
										message);
							}
						},
						buttons : [
								
								{
									text : "发送",
									iconCls : "icon-save",
									handler : function() {
										var phonenumbers = parent.$("#phonenumbers").textbox("getValue");
										var message = parent.$("#message").textbox("getValue");
										if (phonenumbers == null
												|| phonenumbers == ""
												|| message == null
												|| message == "") {
											return;
										}
										
										var isValid = parent.$('#message_form').form('validate');
										if (!isValid)
											return;
										
										// 暂存数据
										parent.$('#savephonenumbers').val(phonenumbers);
										parent.$('#savemessage').val(message);
										
										//ajax启动短信服务
										$.post(url, {
											savephonenumbers:phonenumbers,
											savemessage:message,
											activityId:activityId,
											applicationId : parent.$('#applicationId').val()
										}, function(result) {
											easyui_auto_notice(result, function() {
												
											});
										}, "json");
										
									}
								},
								{
									text : "取消",
									iconCls : "icon-remove",
									handler : function() {

										// 将数据清空
										parent.$('#savephonenumbers').val("");
										parent.$('#savemessage').val("");

										parent.$('#messageService').dialog(
												'close');
									}
								},
								{
									text : "关闭",
									iconCls : "icon-cancel",
									handler : function() {
										parent.$('#messageService').dialog(
												'close');
									}
								} ]
					});
}
/**
 * 审批处理（批量）
 * @param dialogId 控件id
 * @param datagrid 要刷新的grid
 * @param text	按钮显示
 * @param menuid 菜单id
 * @param activityId 节点id
 * @param applicationIds 业务主键
 * @param isback 同意还是退回（1，代表退回）
 * @param isba '1' 代表备案
 */
function showAuditModalDialog(dialogId, datagrid, text, menuid, activityId,
		applicationIds, isback,isba) {
	var path = contextpath
			+ "manage/revoke/controller/AccountRevokeController/";
	dialogId.modalDialog({
		title : "审批处理",
		width : 450,
		height : 210,
		iconCls : 'icon-edit',
		href : path + "accountBatchOperation.do",
		onLoad : function() {
			if(isback=="1"){
				parent.$.modalDialog.handler.find("#opinion").textbox("setValue","不同意。");
				
			}else{
				parent.$.modalDialog.handler.find("#opinion").textbox("setValue","同意。");
				
			}
		},
		buttons : funcOperButtonsCommon(dialogId, datagrid, text, menuid, activityId,
				applicationIds, isback,isba)
	});
}

/**
 * 按钮
 * @param dialogId
 * @param datagrid
 * @param text
 * @param menuid
 * @param activityId
 * @param applicationIds
 * @param isback
 * @param isba
 * @returns
 */
function funcOperButtonsCommon(dialogId, datagrid, text, menuid, activityId,
		applicationIds, isback,isba){
	var path = contextpath
	+ "manage/revoke/controller/AccountRevokeController/";
	var buttons;
	if (isback=="") {//同意
		buttons = [
					{
						text : text,
						iconCls : "icon-save",
						handler : function() {
							var outcome = dialogId.modalDialog.handler.find(
									'#outcome').val();
							var opinion = dialogId.modalDialog.handler.find(
									'#opinion').val();
							if (opinion == null || opinion == "") {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认"+text+"该数据？", function(r) {
							if(r){
								$.post(path + "refuseRevokeInfo.do", {
									menuid : menuid,
									activityId : activityId,
									applicationId : applicationIds,
									isback : isback,
									outcome : outcome,
									opinion : opinion,
									isba :isba
								
								}, function(result) {
									easyui_auto_notice(result, function() {
										datagrid.datagrid('reload');
									});
								}, "json");
								
								dialogId.modalDialog.handler.dialog('close');
							
							}
							});
						}
					}, {
						text : "关闭",
						iconCls : "icon-cancel",
						handler : function() {
							dialogId.modalDialog.handler.dialog('close');
						}
					} ];
	} else {
		buttons =[
					 {
						text : text,
						iconCls : "icon-save",
						handler : function() {
							var outcome = dialogId.modalDialog.handler.find(
									'#outcome').val();
							var opinion = dialogId.modalDialog.handler.find(
									'#opinion').val();
							if (opinion == null || opinion == "") {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认"+text+"该数据？", function(r) {
							if(r){
								$.post(path + "refuseRevokeInfo.do", {
									menuid : menuid,
									activityId : activityId,
									applicationId : applicationIds,
									isback : isback,
									outcome : outcome,
									opinion : opinion,
									isba :isba
								}, function(result) {
									easyui_auto_notice(result, function() {
										datagrid.datagrid('reload');
									});
								}, "json");
	
								dialogId.modalDialog.handler.dialog('close');
							}
							});
						}
					},{
						text : "退回首节点",
						iconCls : "icon-save",
						handler : function() {
							var outcome = dialogId.modalDialog.handler.find(
									'#outcome').val();
							var opinion = dialogId.modalDialog.handler.find(
									'#opinion').val();
							if (opinion == null || opinion == "") {
								return;
							}
							parent.$.messager.confirm("确认操作", "确认将该数据退回至首节点？", function(r) {
							if(r){
									$.post(path + "refuseRevokeInfo.do", {
									menuid : menuid,
									activityId : activityId,
									applicationId : applicationIds,
									isback : "2",
									outcome : outcome,
									opinion : opinion,
									isba :isba
								}, function(result) {
									easyui_auto_notice(result, function() {
										datagrid.datagrid('reload');
									});
								}, "json");
								
								dialogId.modalDialog.handler.dialog('close');
							}
							});
						}
					},{
						text : "关闭",
						iconCls : "icon-cancel",
						handler : function() {
							dialogId.modalDialog.handler.dialog('close');
						}
					} ];
	
	} 
	return buttons;
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
		showFileLength : 11
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
function showUploadifyModalDialog(dialogId,fileItemmids,elementcode){
	var href = contextpath+ "manage/revoke/controller/AccountRevokeController/uploadify.do?fileItemmids="+fileItemmids+"&tdId=filetd&elementcode="+elementcode;
	dialogId.dialog(
			{
				title : "附件管理",
				width : 700,
				height : 300,
				href : href,
				iconCls : 'icon-files',
				modal : true,
				onLoad : function() {
					
				},
				buttons : [
						{
							text : "确认",
							iconCls : "icon-save",
							handler : function() {
								dialogId.dialog('close');
							}
						},
						{
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								dialogId.dialog('close');
							}
						} ]
			});
}
/**
 * 原账户信息
 * @param dialogId 
 * @param applicationId 申请id 与sjly配合使用
 * @param sjly 数据来源，1：数据查询，如果从数据查询，则必须要传applicationId；0 本地获取
 */
function showAccountDetail(dialogId,applicationId,sjly){
	var href = contextpath+ "manage/change/AccountChangeController/oldAccountDetial.do?applicationId="+applicationId;
	dialogId.dialog(
			{
				title : "原账户信息",
				width : 700,
				height : 300,
				href : href,
				iconCls : 'icon-view',
				modal : true,
				onLoad : function() {
					if(sjly==0){
						var oldAccountName = parent.$.modalDialog.handler.find("#oldAccountName").val();
						var oldAccountNumber = parent.$.modalDialog.handler.find("#oldAccountNumber").val();
						var oldBankName = parent.$.modalDialog.handler.find("#oldBankName").val();
						var oldType02Name = parent.$.modalDialog.handler.find("#oldType02Name").val();
						var oldAccountTypeName = parent.$.modalDialog.handler.find("#oldAccountTypeName").val();
						var oldFinancialOfficer = parent.$.modalDialog.handler.find("#oldFinancialOfficer").val();
						var oldLegalPerson = parent.$.modalDialog.handler.find("#oldLegalPerson").val();
						var oldIdcardno = parent.$.modalDialog.handler.find("#oldIdcardno").val();
						var oldAccountContent = parent.$.modalDialog.handler.find("#oldAccountContent").val();
						dialogId.find("#oldAccountName").textbox("setValue",oldAccountName);
						dialogId.find("#oldAccountNumber").textbox("setValue",oldAccountNumber);
						dialogId.find("#oldBankName").textbox("setValue",oldBankName);
						dialogId.find("#oldType02Name").textbox("setValue",oldType02Name);
						dialogId.find("#oldAccountTypeName").textbox("setValue",oldAccountTypeName);
						dialogId.find("#oldFinancialOfficer").textbox("setValue",oldFinancialOfficer);
						dialogId.find("#oldLegalPerson").textbox("setValue",oldLegalPerson);
						dialogId.find("#oldIdcardno").textbox("setValue",oldIdcardno);
						dialogId.find("#oldAccountContent").textbox("setValue",oldAccountContent);
					}
				},
				buttons : [
						{
							text : "关闭",
							iconCls : "icon-cancel",
							handler : function() {
								dialogId.dialog('close');
							}
						} ]
			});
}
/**
 * 初审跟终审（单个审批处理）
 * 说明：弹出页面的元素获取（opinion：审批意见，singlePhonenumbers：手机号，singleMessage：短信内容）
 * @param dialogId 弹出层div
 * @param url 提交表单路径
 * @param form 表单id
 * @param operType 操作类型（1：同意，2：退回，3：退回首节点）
 * @param alertMessage 先备用
 */
function showOperationDialog(dialogId,url,form,operType,wfid,activityId,applicationId,isBa){
	var href = contextpath+ "manage/revoke/controller/AccountRevokeController/accountSingleOperation.do";
	dialogId.dialog({
		title : "审核",
		width : 520,
		height : 380,
		href : href+"?activityId="+activityId+"&wfid="+wfid+"&applicationId="+applicationId+"&isBa="+isBa+"&cllx="+operType,
		iconCls : 'icon-edit',
		modal : true,
		onLoad : function() {
			var formbody =parent.$('#'+form);
			if(formbody.find("#opinion").length==0){
				formbody.append("<input id='opinion' type='hidden' name='opinion'/>");
				if(operType==1){
					parent.$("#opinionDialog").textbox("setValue","同意。");
				}else if(operType==2){
					parent.$("#opinionDialog").textbox("setValue","不同意。");
				}else if(operType==3){
					parent.$("#opinionDialog").textbox("setValue","不同意。");
				}
			}else if(formbody.find("#opinion").val()!=""){
				parent.$("#opinionDialog").textbox("setValue",formbody.find("#opinion").val());
			}else{
				if(operType==1){
					parent.$("#opinionDialog").textbox("setValue","同意。");
				}else if(operType==2){
					parent.$("#opinionDialog").textbox("setValue","不同意。");
				}else if(operType==3){
					parent.$("#opinionDialog").textbox("setValue","不同意。");
				}
				
			}
			if(formbody.find("#singlePhonenumbers").length==0){
				formbody.append("<input id='singlePhonenumbers' type='hidden' name='singlePhonenumbers'/>");
			}else if(formbody.find("#singlePhonenumbers").val()!=""){
				parent.$("#singlePhonenumbersDialog").combobox("setValues",formbody.find("#singlePhonenumbers").val());
			}
			if(formbody.find("#singleMessage").length==0){
				formbody.append("<input id='singleMessage' type='hidden' name='singleMessage'/>");
				parent.$("#singleMessageDialog").textbox("setValue",parent.$("#messageContent").val());
			}else{
				parent.$("#singleMessageDialog").textbox("setValue",parent.$("#messageContent").val());
				//parent.$("#singleMessageDialog").textbox("setValue",formbody.find("#singleMessage").val());
			}
		},
		buttons : [
					{
						text : "确认",
						iconCls : "icon-save",
						handler : function() {
							var opinion = parent.$("#opinionDialog").textbox("getValue");
							if(opinion=="" || opinion==null){
								easyui_warn("请输入审批意见！");
								return;
							}
							parent.$("#opinion").val(opinion);
							
							if(parent.$('#isSendMesssage').is(':checked')){//如果启用短息服务
								var phonenumbers = parent.$("#singlePhonenumbersDialog").combobox("getValues");
								var message = parent.$("#singleMessageDialog").textbox("getValue");
								
								if(phonenumbers=="" || phonenumbers==null){
									easyui_warn("请至少选择一个手机号！");
									return;
								}
								if(message==null || message==""){
									easyui_warn("请输入短信内容！");
									return;
								}
								var singlePhonenumbers="";
								for(var j=0;j<phonenumbers.length;j++){
									if(j==phonenumbers.length-1){
										singlePhonenumbers+=phonenumbers[j].substring(2);
									}else{
										singlePhonenumbers+=phonenumbers[j].substring(2)+",";
									}
								}
								parent.$("#singlePhonenumbers").val(singlePhonenumbers);
								
								parent.$("#singleMessage").val(message);
							}
							
							//提交表单
							submitFormDialog(url, form,operType,"");
							
							dialogId.dialog('close');
						}
					},
					{
						text : "关闭",
						iconCls : "icon-cancel",
						handler : function() {
							
							dialogId.dialog('close');
						}
					} ]
					
	});
}

/**
 * 提交表单
 * 
 * @param url
 *            表单url
 */
function submitFormDialog(url, form,operType,mesg) {
	var form = parent.$('#' + form);
	form.form("submit",
		{
			url : url,
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
					//alert("成功!!");
					//重新加载gride
					parent.$.modalDialog.handler.dialog('close');
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					//parents("").datagrid('reload');
					
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};
function checkFunc(id) {
	var pand = parent.$('#'+id).is(':checked');
		var panel = $('#messagePanel');
		if (pand){
			removeLockDiv(panel);
		}else{
			addLockDiv(panel);
			//$("#tableChange").remove();
		}
}

//添加遮罩层
function addLockDiv(panel) {
	$("<div class=\"window-mask\"></div>").css({ display: "block", width: panel.width(), height: panel.height()}).appendTo(panel);
}

//移除遮罩层
function removeLockDiv(panel) {
    panel.find("div.window-mask").remove();  
}
