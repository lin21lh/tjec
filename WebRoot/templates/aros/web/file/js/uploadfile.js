var defaultLength = 100;
var defaultParentLength = 40;

$(function(){
	
	var fileItemmids = $("#fileItemmids").val();
	var sessionId = $("#sessionId").val();
	var tdId = $("#tdId").val();
	var elementcode = $("#elementcode").val();
	var keyid = $("#keyid").val();
	var downloadFileUrl = "FileManageController_download.do";
	
	//附件上传控件
	$("#uploadify").uploadify({
        swf:'component/uploadify/uploadify.swf',
        uploader: 'FileManageController_add.do?jsessionid=' + sessionId,//后台处理的请求
        cancelImg:'component/uploadify/uploadify-cancel.png',
        queueID:"fileQueue",//与下面的id对应
        queueSizeLimit : 50,//选择文件的个数
        uploadLimit : 50,//总上传文件数
        simUploadLimit : 50,//允许同时上传的个数
        //fileTypeExts : '*.rar;*.zip', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
        //fileTypeDesc : 'rar文件或zip文件',
        auto:true,//是否自动上传
        multi:true,
        rollover:true,
        progressData:'percentage', //进度 percentage或speed
        removeCompleted:true,
        removeTimeout:0,
        method:'post',
        fileObjName:'addfile',
        formData:{'elementcode':elementcode, 'savemode':"1", 'savePath':'', 'keyid':keyid},//和后台交互时，附加的参数    savemode:0,1 0-保存磁盘；1-保存数据库
        fileSizeLimit:'100MB', //上传文件大小设置 单位可以是B、KB、MB、GB 
        buttonText:'选择上传文件', //
        onUploadSuccess:function(file, data, response) {
        	var data = jQuery.parseJSON(data);
        	//设置隐藏域id
        	//var fileItemmids="itemids";
        	//检索附件所在的form
        	var form = parent.$("#"+tdId).parents("form");
        	
        	//寻找是否存在隐藏域
        	if(form.find("#" + fileItemmids).length == 0){
        		form.append("<input id='"+fileItemmids+"' type='hidden' name='"+fileItemmids+"'/>");
        	}
        	
        	//验证是否存在同名文件
        	var l = $("#"+tdId).children("div").length;
        	var flag = false;
        	for(var i=0; i<l; i++){
        		//var hasFileName=tableControl.find("tr").eq(i).children("td").eq(0).find("a").text();
        		var hasFileName = $("#"+tdId).children("div").eq(i).attr("fileName");
        		if(data.filename == hasFileName){
        			flag = true;
        		}
        	}
        	
        	if(flag == true){
        		
        		easyui_warn("已存在该同名文件");
    			$.post('FileManageController_delete.do', {
    				itemids : data.itemid
    			}, function(result) {
    			}, 'json');
    			
    			flag = false;
    			
        	}else{
        		
	        	var itemids = form.find("#"+fileItemmids).val();
	        	if(itemids == null || itemids == ""){
	        		form.find("#" + fileItemmids).val(data.itemid);
	        	}else{
	        		form.find("#" + fileItemmids).val(itemids+","+data.itemid);
	        	}
	        	
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
	        	
	        	var $tr = $("#fileTable").find("tr").eq(0);
	            if($tr.size() == 0){
	            	$("#fileTable").append(
	            			"<tr id='"+data.itemid+"' fileName='"+data.filename+"'>" +
							"<td style='width:74%;word-WRBP: break-word'>"+
							"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"'  title='"+data.filename+"' style='width:500px;text-decoration:none;color:#0000FF' >"+getsubstring(data.filename,defaultLength)+"</a>" +
									"<span>&nbsp;&nbsp;<font size='0.5' color='red'>(上传完成，未保存)</font></span></td>"+
							"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+data.itemid+"' style='width:100px;text-decoration:none;color:#0000FF' >下载</a></td>"+
							"<td style='width:13%'><a href='#' style='width:100px;text-decoration:none;color:#0000FF' onclick='deleteFile("+data.itemid+",\"self\")'>删除</a></td></tr>"	
	            	);
	            	
	            	$("#"+tdId).append(
	            			"<div id='"+data.itemid+"' fileName='"+data.filename+"' style='display:block;float:left;height: 18px; line-height: 14px;' isSave='0'>" +
	            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
	            			"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"' title='"+data.filename+"' style='width:500px;text-decoration:none;color:#0000FF' >"+getsubstring(data.filename,defaultParentLength)+"</a>" +
	            			//"<span>&nbsp;&nbsp;<font size='0.4' color='red'>(未保存)</font></span>"+
	            			"<img src='component/uploadify/delete.gif' align='absmiddle' style='margin-right: 3px;' onclick='deleteFile("+data.itemid+",\"self\")'></img>"+
	            			//"<a href = '#' onClick='showFileDetail("+data.itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
	            			"</div>"
	            	);
	            }else{
	            	
	            	$tr.before(
	            			"<tr id='"+data.itemid+"' fileName='"+data.filename+"'>" +
							"<td style='width:74%;word-WRBP: break-word'>"+
							"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"' style='width:500px;text-decoration:none;color:#0000FF' >"+getsubstring(data.filename,defaultLength)+"</a>" +
									"<span>&nbsp;&nbsp;<font size='0.5' color='red'>(上传完成，未保存)</font></span></td>"+
							"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+data.itemid+"'  title='"+data.filename+"' style='width:100px;text-decoration:none;color:#0000FF' >下载</a></td>"+
							"<td style='width:13%'><a href='#' style='width:100px;text-decoration:none;color:#0000FF' onclick='deleteFile("+data.itemid+",\"self\")'>删除</a></td></tr>"	
	            	);
	            	
	            	$("#"+tdId).append(
	            			"<div id='"+data.itemid+"' fileName='"+data.filename+"' style='display:block;float:left;height: 18px; line-height: 14px;'  isSave='0'>" +
	            			"<img src='"+fileImg+"' align='absmiddle' style='margin-right: 3px;'></img>"+
	            			"<a href='"+downloadFileUrl+"?itemid="+data.itemid+"' title='"+data.filename+"' style='width:500px;text-decoration:none;color:#0000FF' >"+getsubstring(data.filename,defaultParentLength)+"</a>" +
	            			//"<span>&nbsp;&nbsp;<font size='0.4' color='red'>(未保存)</font></span>"+
	            			"<img src='component/uploadify/delete.gif' align='absmiddle' style='margin-right: 3px;' onclick='deleteFile("+data.itemid+",\"self\")'></img>"+
	            			//"<a href = '#' onClick='showFileDetail("+data.itemid+");' style='width:500px;text-decoration:none;color:#0000FF'>预览</a>" +
	            			"</div>"
	            	);
	            }
        	}
        }
	 });
	
	/** 附件管理，查询附件数据（通过跟父页面的附件数据同步）*/
	var length = $("#"+tdId).children("div").length;//获取父页面的div个数，每个div代表一个附件
	var divs = $("#"+tdId).children("div");
	var fileInfo = "";//设置临时变量（暂存附件展示源码）
	for(var i=0; i<length; i++){//遍历每个附件
		var div = divs.eq(i);
			if(div.attr("isSave")=="0"){//是否是已保存的附件：0 代表未保存，标识该附件已上传，但未关联业务
				fileInfo += 
					"<tr id='"+div.attr("id")+"' >" +
					"<td style='width:74%;word-WRBP: break-word'>"+
					"<a href='"+downloadFileUrl+"?itemid="+div.attr("id")+"' style='text-decoration:none;color:#0000FF' title='"+div.children("a").attr("title")+"'>"+getsubstring(div.attr("fileName"),defaultLength)+"</a>"+
					"<span>&nbsp;&nbsp;<font size='0.5' color='red'>(上传完成，未保存)</font></span>"+
					"</td>"+
					"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+div.attr("id")+"' style='text-decoration:none;color:#0000FF' >下载</a></td>"+
					"<td style='width:13%'><a href='#' style='text-decoration:none;color:#0000FF' onclick='deleteFile(\""+div.attr("id")+"\",\"self\")'>删除</a></td></tr>";
			}else{
				fileInfo += 
					"<tr id='"+div.attr("id")+"' >" +
					"<td style='width:70%;word-WRBP: break-word'>"+
					"<a href='"+downloadFileUrl+"?itemid="+div.attr("id")+"' style='text-decoration:none;color:#0000FF' title='"+div.children("a").attr("title")+"'>"+getsubstring(div.attr("fileName"),defaultParentLength)+"</a>"+
					"</td>"+
					"<td style='width:13%'><a href='"+downloadFileUrl+"?itemid="+div.attr("id")+"' style='text-decoration:none;color:#0000FF' >下载</a></td>"+
					"<td style='width:13%'><a href='#' style='text-decoration:none;color:#0000FF' onclick='deleteFile(\""+div.attr("id")+"\",\""+div.attr("userCode")+"\")'>删除</a></td></tr>";
			}
	}
	$("#fileTable").append(fileInfo);//将附件信息追加到附件展示table中
});

/*
 * @itemid 附件主键
 * @param createUser 上传人
 */
function deleteFile(itemid, createUser) {
	
	if (createUser == "self") {
		
		parent.$.messager.confirm("删除确认", "确认要删除该附件？", function(r) {
			if (r) {
				$.post('FileManageController_delete.do', {
					itemids : itemid
				}, function(result) {
				}, 'json');
				$("'tr[id=" + itemid + "]'").remove();
				$("'div[id=" + itemid + "]'").remove();
			}
		});
	} else {
		
		easyui_warn("非本用户上传文件，不能删除");
	}
}

//截取字段长度
function getsubstring(content, sublength) {
	var length = getBt(content);
	var substring = "";
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
			substring = j > sublength ? content.substring(0, i)
					: content.substring(0, i + 1);
			
		}
	}
	return substring+"...";
}

//获取字符数
function getBt(str){
    var char = str.match(/[^\x00-\xff]/ig);
    return str.length + (char == null ? 0 : char.length);
}