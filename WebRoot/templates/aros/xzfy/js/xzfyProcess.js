//请求路径
var base = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var base1 = contextpath + "aros/ajbg/fycx/FycxController/";

var Urls = {
	reqDetailUrl: base1 + "fycxViewInit.do",                       
	queryNodeidsByCase : base + "queryNodeidsByCase.do"              //根据案件查询已审流程节点
};

/**
 * 查看复议程序详细信息
 */
function xzfyReqDetail(){
	openxzfyDetail(Urls.reqDetailUrl, "案件程序详情", 900, 550, "");
}

function openxzfyDetail(url, title, width, height, nodeid)
{
	var selectRow = xzfyDataGrid.datagrid("getChecked");
	if (selectRow == null || selectRow.length == 0 || selectRow.length > 1) {
		easyui_warn("请选择一条复议申请", null);
		return;
	}
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	
	parent.$.modalDialog({
		title:title,
		width:width,
		height:height,
		href:url,
		queryParams: {
			 ccrid: row.ccrid,
			 protype: row.protype,
			 nodeid: nodeid
		},
		onLoad:function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = mdDialog.find('#sessionId').val();
			if ("7" == nodeid)
			{
				isShowSendunit(mdDialog.find("#result").val());
			}
			if ("12" == nodeid)
			{
				isShowByProtype(row.protype, row.protype);// 延期天数是否展示
			}
		},
		buttons:[{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				parent.$.modalDialog.handler.dialog("close");
			}
		}]
	});
}
// 复议程序
function fyProcess()
{
	
	var fyProcessTable = $('#fyProcessTable');
	
	$("#fyProcessTable  tr:not(:first)").empty("");
	
	var row = $('#proctr');
	
	var td = $("<td name='nodeid_10' id='10' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("承 办 人");
	row.append(td);
	
	td = $("<td name='nodeid_20' id='20' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("科 室 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_30' id='30' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("机 构 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_40' id='40' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("机 关 负 责 人");
	row.append(td);
	
	td = $("<td name='nodeid_50' id='50' class='ancestorstd'  onClick='xzfyReqDetail()' valign='top'></td>");
	td.append("程 序 决 定");
	row.append(td);
	
	fyProcessTable.append(row);
	
}

/**
 * 显示流程进度
 */
function viewFlow(){
	$('th').css("backgroundColor", "");
	$('td[name^="nodeid_"]').css("backgroundColor", "");
	
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var nodeid = row.nodeid;
	var  ccrid = row.ccrid;
	var protype = row.protype;
	
	if(nodeid > 0 && nodeid != null){
		fyProcess();
		$('#proc_jd').css("background-color","#CCFF99");
		queryNodeidsByCase(ccrid,protype,nodeid,Urls.queryNodeidsByCase);
	}
	
}


function  queryNodeidsByCase(ccrid,protype,nodeid,url){
	
	$.ajax({
		type:'post',
		url:url,
		async : false,
		data : {
			ccrid : ccrid,
			protype: protype, 
			nodeid : nodeid,
			caseid : -1
		},
		success:function(data) {
			
				$('td[name^="nodeid_"]').each(function (index, domEle) {
					
					var nodeid_id = this.id;                                  //tb标签的id.
					
//					if(nodeid_id > nodeid){
//						$(domEle).css("display", "none"); 
//					}
					
					jQuery.each(data, function (i, item) {
						 if(nodeid_id == item.nodeid){
			                	$(domEle).css("backgroundColor", "#CCFF99");  
			                }
				        });
					
					if(nodeid_id == nodeid){
						$(domEle).css("backgroundColor", "#EE4000"); 
					}
				}) 
			
		},
		error : function() {
            alert('error');
        }
	});
}

/**
 * 实现时间进度
 */
/*
function viewTime(){
	
	//先显示所有办理期限的TD
//	$('td[name^="time_"]').show();   
//	$('td[name^="time_"]').css("backgroundColor", "");
	
	//办理天数，默认60天
	var total = 60;
	
	//获取当前日期
	var currDate = getNowFormatDate();
	
	//获取申请日期
	var row = xzfyDataGrid.datagrid("getSelections")[0];
	var optdate = row.appdate;
	//延期天数
	var delaydays = row.delaydays;
	
	//判断是否延期，延期则在办理期限上加上延期天数
//	var re = /^[0-9]+.?[0-9]*$/;
	if (parseInt(delaydays)> 0) {
		total += parseInt(delaydays);
	}
	
	var days = 10;
	
/*	jQuery('#inpt_slider').replaceWith(function(){
		return "<table id='inpt_slider' class='list' style='width: 100%; height: 100%;'>" +
					"<tr id='timeTr' style='height: 100%;'>"
						+"<td class='easyui-slider' data-options='showTip:true' >"
						+"</td>"  
					+"</tr>"
				+"</table>"
				;
	});*/
	
//	jQuery('#inpt_slider').attr({showTip:true, rule: [0,'|',25,'|',50,'|',75,'|',100] });
//	jQuery('#inpt_slider').replaceWith("<td  id='inpt_slider' class='easyui-slider' data-options='showTip:true,rule : [0,'|',"+days+",'|',"+total+"]'>"+" </td>");
//alert("<td  id='inpt_slider' class='easyui-slider' data-options='showTip:true,rule : [0,'|',"+days+",'|',"+total+"]'>"+" </td>");

	/*	var ttNum=0;
	if((total % 5) == 0){
		ttNum = parseInt(total / 5);	
	} else {
		ttNum = parseInt(total / 5) + 1;
	}
	for(var j = ttNum + 1; j <= 18; j++){
		
		var tdname = "time_" + (j*5);
		$('td[name="' + tdname + '"]').css('display', 'none');    
	}
	
	//计算时间间隔天数
	var days = DateDiff(currDate, optdate);
	
	$('td[name^="time_"]').each(function (index, domEle) {
		
		var time_id = this.id;
		var arr = time_id.split("-");
		var minDays = arr[0];
		var maxDays= arr[1];
		if (days > minDays && days > maxDays) {
        	$(domEle).css("backgroundColor", "#CCFF99");  
        }else if (days >= minDays && days <= maxDays) {
        	$(domEle).css("backgroundColor", "#FF8888");  
        }  else {
        	return;
        }
    });
}
*/
/**
 * 根据当前案件审结结果
 */
/*function isShowByProtype(protype, oldprotype)
{
	var form = parent.$.modalDialog.handler.find('#xzfyReviewForm');
	// 如果变更流程类型
	if (protype.indexOf("n") > -1)
	{
		protype = protype.replace("n","");
		if ("07" == protype) // 延期
		{
			form.find("#delay").show();
		}
	}
	else
	{
		form.find("#delay").hide();
	}
}

function isShowSendunit (id) {
	var modalDialog = parent.$.modalDialog.handler;
	if ("03" == id) {
		modalDialog.find("#sendunitTr").show();
	}
	else {
		modalDialog.find("#sendunitTr").hide();
	}
}*/