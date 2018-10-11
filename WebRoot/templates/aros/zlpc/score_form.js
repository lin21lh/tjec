var baseUrl = contextpath + "aros/zlpc/controller/ZlpcController/";
var urls = {
	tableUrl : baseUrl + "scoreTable.do"
	
};
/*默认加载，初始化table列表*/
$(function() {
	$.ajax({
		type : "post",
		url : urls.tableUrl,
		data : {
			caseid : $("#caseid").val()
		},
		dataType : 'json',
		async : false,
		success : function(data){
			var sty = 'height="30" align="center" valign="middle" bgcolor="#E6F0F7" style="font-size:14;"';
			var styleft = 'height="30" align="left" valign="middle" bgcolor="#E6F0F7" style="font-size:14;"';
			//构造table
			var column_h = "";
			var column_f = "";
			var tr = "";
			var theory = 0;
			var actual = 0;
			for(var i=0;i<data.length;i++){
				theory = theory + (data[i].score==null?0:data[i].score);
				actual = actual + (data[i].actscore==null?0:data[i].actscore);
				if (i==0){
					tr = "<tr><td "+sty+" rowspan='1' id='"+data[i].standid + "_h" +"'>" + data[i].stagetypename + 
							"</td><td "+sty+" rowspan='1' id='"+data[i].standid + "_f" +"'>" + data[i].inditypename + 
							"</td><td "+styleft+" rowspan='1'>" + data[i].standardname + 
							"</td><td "+sty+" rowspan='1'>" + data[i].score +
							"</td><td "+sty+" rowspan='1'>" + "<input id='"+data[i].standid+"_n' value='"+(data[i].actscore==null?"":data[i].actscore)+"' style='width:60px;height:20px;font-size:14;' onchange='caltotal();' data='"+data[i].standid+"'/><font color='red'>*</font>" +
							"</td></tr>"
					$("#wdzsTable").append(tr);
					column_h = data[i].standid + "_h";
					column_f = data[i].standid + "_f";
				}else{
					if (data[i].stagetypename == data[i-1].stagetypename){
						$("#"+column_h).attr("rowspan",parseInt($("#"+column_h).attr("rowspan"))+1);//修改第一列合并
						if (data[i].inditypename == data[i-1].inditypename){
							$("#"+column_f).attr("rowspan",parseInt($("#"+column_f).attr("rowspan"))+1);//修改第二列合并
							tr = "<tr><td "+styleft+" rowspan='1'>" + data[i].standardname + 
								"</td><td "+sty+" rowspan='1'>" + data[i].score +
								"</td><td "+sty+" rowspan='1'>" + "<input id='"+data[i].standid+"_n' value='"+(data[i].actscore==null?"":data[i].actscore)+"' style='width:60px;height:25px;font-size:14;' onchange='caltotal();' data='"+data[i].standid+"'/><font color='red'>*</font>" +
								"</td></tr>"
							$("#wdzsTable").append(tr);
						} else {
							tr = "<tr><td "+sty+" rowspan='1' id='"+data[i].standid + "_f" +"'>" + data[i].inditypename + 
								"</td><td "+styleft+" rowspan='1'>" + data[i].standardname + 
								"</td><td "+sty+" rowspan='1'>" + data[i].score +
								"</td><td "+sty+" rowspan='1'>" + "<input id='"+data[i].standid+"_n' value='"+(data[i].actscore==null?"":data[i].actscore)+"' style='width:60px;height:25px;font-size:14;' onchange='caltotal();' data='"+data[i].standid+"'/><font color='red'>*</font>" +
								"</td></tr>"
							$("#wdzsTable").append(tr);
							column_f = data[i].standid + "_f";
						}
						
					} else {
						/*新的一行*/
						tr = "<tr><td "+sty+" rowspan='1' id='"+data[i].standid + "_h" +"'>" + data[i].stagetypename + 
							"</td><td "+sty+" rowspan='1' id='"+data[i].standid + "_f" +"'>" + data[i].inditypename + 
							"</td><td "+styleft+" rowspan='1'>" + data[i].standardname + 
							"</td><td "+sty+" rowspan='1'>" + data[i].score +
							"</td><td "+sty+" rowspan='1'>" + "<input id='"+data[i].standid+"_n' value='"+(data[i].actscore==null?"":data[i].actscore)+"' style='width:60px;height:25px;font-size:14;' onchange='caltotal();' data='"+data[i].standid+"'/><font color='red'>*</font>" +
							"</td></tr>"
						$("#wdzsTable").append(tr);
						column_h = data[i].standid + "_h";
						column_f = data[i].standid + "_f";
					}
				}
			}
			tr = "<tr><td "+sty+" rowspan='1' colspan='3' >合计</td>"+
				"<td "+sty+">"+theory+"</td>"+
				"<td id='theory_total' "+sty+">"+actual+"</td></tr>";
			$("#wdzsTable").append(tr);
		}
	});
});

function caltotal(){
	var total = 0;
	$("input[id$=_n]").each(function(){
		var num = $(this).val();
		if(num !='' && !isNaN(num)){
			total += parseInt(num);
		}
	});
	$("#theory_total").html(total);
}

	