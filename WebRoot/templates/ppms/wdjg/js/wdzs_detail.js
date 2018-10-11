var baseUrl = contextpath + "ppms/wdjg/WdzsController/";
var urls = {
	queryWdzsUrl : baseUrl + "queryWdzs.do"
	
};
/*默认加载，初始化table列表*/
$(function() {
	var downloadFileUrl =  contextpath + "base/filemanage/fileManageController/downLoadFile.do";
	$.ajax({
		type : "post",
		url : urls.queryWdzsUrl,
		data : {
			wd_projectid : $("#wd_projectid").val()
		},
		dataType : 'json',
		async : false,
		success : function(data){
			var sty = 'height="30" align="center" valign="middle" bgcolor="#E6F0F7"';
			//构造table
			var column_h = "";
			var column_f = "";
			var tr = "";
			for(var i=0;i<data.length;i++){
				/*下载和预览显示*/
				if (data[i].itemid == ''){
					var downshow = "未上传文档";
				} else {
					var downshow = "<a href='"+downloadFileUrl+"?itemid="+data[i].itemid+"' id='"+data[i].itemid+"'>下载</a>";
					
				}
				if (i==0){
					tr = "<tr><td "+sty+" rowspan='1' id='"+data[i].wdjgid + "_h" +"'>" + data[i].xmhj_name + 
							"</td><td "+sty+" rowspan='1' id='"+data[i].wdjgid + "_f" +"'>" + data[i].hjfl_name + 
							"</td><td "+sty+" rowspan='1'>" + data[i].wdmc + 
							"</td><td "+sty+" rowspan='1'>" + downshow +
							"</td></tr>"
					$("#wdzsTable").append(tr);
					column_h = data[i].wdjgid + "_h";
					column_f = data[i].wdjgid + "_f";
				}else{
					if (data[i].xmhj_name == data[i-1].xmhj_name){
						$("#"+column_h).attr("rowspan",parseInt($("#"+column_h).attr("rowspan"))+1);//修改第一列合并
						if (data[i].hjfl_name == data[i-1].hjfl_name){
							$("#"+column_f).attr("rowspan",parseInt($("#"+column_f).attr("rowspan"))+1);//修改第二列合并
							tr = "<tr><td "+sty+" rowspan='1'>" + data[i].wdmc + 
								"</td><td "+sty+" rowspan='1'>" + downshow +
								"</td></tr>";
							$("#wdzsTable").append(tr);
						} else {
							tr = "<tr><td "+sty+" rowspan='1' id='"+data[i].wdjgid + "_f" +"'>" + data[i].hjfl_name + 
								"</td><td "+sty+" rowspan='1'>" + data[i].wdmc + 
								"</td><td "+sty+" rowspan='1'>" + downshow +
								"</td></tr>";
							$("#wdzsTable").append(tr);
							column_f = data[i].wdjgid + "_f";
						}
						
					} else {
						/*新的一行*/
						tr = "<tr><td "+sty+" rowspan='1' id='"+data[i].wdjgid + "_h" +"'>" + data[i].xmhj_name + 
								"</td><td "+sty+" rowspan='1' id='"+data[i].wdjgid + "_f" +"'>" + data[i].hjfl_name + 
								"</td><td "+sty+" rowspan='1'>" + data[i].wdmc + 
								"</td><td "+sty+" rowspan='1'>" + downshow +
								"</td></tr>"
						$("#wdzsTable").append(tr);
						column_h = data[i].wdjgid + "_h";
						column_f = data[i].wdjgid + "_f";
					}
				}
			}
		}
	});
});
	