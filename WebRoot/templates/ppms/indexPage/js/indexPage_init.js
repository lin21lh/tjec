var baseUrl = contextpath + "ppms/indexPage/IndexPageController/";
var baseUrl2 = contextpath + "sys/notice/SysNoticeManageController/";
var urls = {
	myChart1Url : baseUrl + "queryMyChart1.do",
	myChart2Url : baseUrl + "queryMyChart2.do",
	myChart3Url : baseUrl + "queryMyChart3.do",
	
	//留言和公告
	feedbackNotice : baseUrl2+"feedbackNotice.do",
	getClobContentVal : baseUrl2+"getClobContentVal.do",
	showCommonBySelfUrl :  baseUrl2+"showCommonBySelf.do",
	showCommonDialogUrl : baseUrl2+"showCommonDialog.do"
};



/*全局变量*/
var nowDate = new Date();
var nowYear = parseInt(nowDate.getFullYear());//当前年份
var timeSpan = 10;//时间轴年度长度
var dateopt3 = [];//option3的timeline轴日期
for(var i=0;i<timeSpan;i++){
	dateopt3[i] = nowYear + i + '-01-01';
}
/*图表全局变量定义*/
var myChart1 ;
var myChart2 ;
var myChart3 ;

option1 = {
    tooltip: {
        trigger: 'item',//数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用
        formatter: "{b} <br/>{a}: {c} ({d}%)"//格式化
    },
    
    series: [
        {
            name:'项目数量',
            type:'pie',
          
            radius: [0, '50%'],//饼的内外半径
            label: {
                normal: {
                    position: 'inner',
                    formatter :  "{b}\n({d}%)"
                }
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
//            color : [],
            data:[
                {value:335, name:'使用者付费'},
                {value:679, name:'缺口补助'},
                {value:1548, name:'政府付费'}
            ]
        },
        {
            name:'项目金额',
            type:'pie',
            radius: ['65%', '85%'],
            label: {
                normal: {
                    formatter :  "{b}\n({d}%)"
                }
            },
//            color : [],//色盘颜色
            data:[
                {value:335, name:'使用者付费'},
                {value:310, name:'缺口补助'},
                {value:234, name:'政府付费'}
            ]
        }
    ]
};

option2 = {
	    tooltip : {//浮动提示
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: function (params, ticket, callback) {//格式化显示提示
	            var str = '<table style="color:#fff;font-size:14;">';
	            for (var i=0;i<params.length;i++){
	            	if (i%2 == 1){
	            		
	            	} else {
	            		str += "<tr>";
	            	}
	            	str += "<td>"+params[i].seriesName + ':' + params[i].value +"</td>";
	            	if (i%2 == 1){
	            		str += '<tr/>';
	            	} else {
	            		
	            	}
	            }
	            str += "</table>"
	            return str;
	        }
	    },
	    legend: {
	        data: ['O&M', 'BOOT','BOT','BOO','TOT','ROT','BT','BTO']
	    },
//	    color : ['red','blue'],
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis:  {
	    	 type: 'category',
		     data: ['项目识别','项目准备','项目采购','项目执行','项目移交'],
		     axisLabel :{
	    			interval:0
	    		}//强制显示所有标签
	    },
	    
	    yAxis: {
//	    	name: '投资（万元）',
	    	type: 'value'
	    },
	    series: [
	        {
	            name: 'O&M',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        },
	        {
	            name: 'BOOT',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	        		show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [32, 312, 201, 334, 39]
	        },
	        {
	            name: 'BOT',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        },
	        {
	            name: 'BOO',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        },
	        {
	            name: 'TOT',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        },
	        {
	            name: 'ROT',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        },
	        {
	            name: 'BT',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        },
	        {
	            name: 'BTO',
	            type: 'bar',
	            stack: '金额',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideRight'
	                }
	            },
	            data: [320, 302, 301, 334, 39]
	        }
	        
	    ]
	};

option3 = {
	    baseOption: {
	        timeline: {
	            axisType: 'category',
	            autoPlay: true,
	            playInterval: 2000,
//	            currentIndex : 5,//默认选中节点option[5]
	            data: dateopt3,
	            label: {
	                formatter : function(s) {
	                    return (new Date(s)).getFullYear();
	                }
	            }
	        },
//	        title: {
//	            subtext: '数据来自PPP中心'
//	        },
	        tooltip: {},//浮动提示
	        calculable : true,
	        grid: {
	            top: 80,
	            bottom: 100
	        },
	        xAxis: {
	                'type':'category',
	                'axisLabel':{'interval':0},
	                'data':[
	                    '铁路','\n公路','机场','\n市政建设','土地储备','\n保障性住房','生态建设和环境保护','\n政权建设',
	                    '教育','\n科学','文化','\n医疗卫生','社会保障','\n粮油物资储备','农林水利建设','\n其他'
	                    
	                ],
	                splitLine: {show: false}
	            },
	        yAxis:{
	            	//  max: 60000,
	                type: 'value',
	                name: '预算（万元）'
	            },
	        series: [
	            {name: '预算', type: 'bar',
//	            	label: {//柱形图显示具体数据
//		                normal: {
//		                    show: true,
//		                    position: 'top'
//		                }
//		            }
	            }
	            
	        ]
	    },
	    options: [
	        {
	            title: {text: nowYear +'年各行业总预算汇总'},
	            series: [
	                {data: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]}
	            ]
	        },
	        {
	            title : {text: nowYear +1 +'年各行业总预算汇总'},
	            series : [
	                {data: [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17]}
	            ]
	        },
	        {
	            title : {text: nowYear +2 +'年各行业总预算汇总'},
	            series : [
	                {data:  [3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18]}
	            ]
	        },
	        {
	            title : {text: nowYear +3 +'年各行业总预算汇总'},
	            series : [
	                {data:  [4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]}
	            ]
	        },
	        {
	            title : {text: nowYear +4 +'年各行业总预算汇总'},
	            series : [
	                {data: [5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]}
	            ]
	        },
	        {
	            title : {text: nowYear +5 +'年各行业总预算汇总'},
	            series : [
	                {data: [6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21]}
	            ]
	        },
	        {
	            title : {text: nowYear +6 +'年各行业总预算汇总'},
	            series : [
	                {data: [7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22]}
	            ]
	        },
	        {
	            title : {text: nowYear +7 +'年各行业总预算汇总'},
	            series : [
	                {data: [8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]}
	            ]
	        },
	        {
	            title : {text: nowYear +8 +'年各行业总预算汇总'},
	            series : [
	                {data: [9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]}
	            ]
	        },
	        {
	            title : {text: nowYear +9 +'年各行业总预算汇总'},
	            series : [
	                {data: [10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]}
	            ]
	        }
	    ]
	};

/*图表1查询*/
function myChart1Query(){
	$.ajax({
		type : "post",
		url : urls.myChart1Url,
		data : {
			year : $("#year").combobox("getValue")
		},
		dataType : 'json',
		async : false,
		success : function(result){
			var option = {series:[{data:result.data1},{data:result.data2}]};
			myChart1.setOption(option);
		}
	});
	
}
/*图表2查询*/
function myChart2Query(){
	$.ajax({
		type : "post",
		url : urls.myChart2Url,
		data : {
			year : $("#year").combobox("getValue")
		},
		dataType : 'json',
		async : false,
		success : function(result){
			myChart2.setOption(result);
		}
	});
}

/**
 * 图表3查询
 * @return
 */
function myChart3Query(){
	
	$.ajax({
		type : "post",
		url : urls.myChart3Url,
		data : {
			year : nowYear,//起始年份
			timeSpan : timeSpan//时间轴长度
		},
		dataType : 'json',
		async : false,
		success : function(result){
			myChart3.setOption(result);
		}
	});
}

$(function(){
	/*初始化chart容器高度*/
	var height = $("#west").css("height");
	var num = height.replace('px','');
	$("#chart").css("height",(num-50)/2+'px');
	$("#chart3").css("height",(num-50)/2+'px');
	
	/*年度时间选择初始化*/
	var years = new Array(5);
	for(var i=0;i<5;i++){
		years[i] = nowYear - i;
	}
	$("#year").combobox({
		valueField : "value",
		textField : "text",
		value : years[0],
		editable : false,
		data : [
		        {text : years[0], value : years[0]}, 
		        {text : years[1], value : years[1]}, 
		        {text : years[2], value : years[2]}, 
		        {text : years[3], value : years[3]}, 
		        {text : years[4], value : years[4]} 
		      
		],
		onChange : function(ne,ol) {
			/*在此添加查询*/
			myChart1Query();
			myChart2Query();

		}
	});
	
	myChart1 = echarts.init($("#chart1").get(0));
	myChart2 = echarts.init($("#chart2").get(0));
	myChart3 = echarts.init($("#chart3").get(0));
	
	myChart1.setOption(option1);
	myChart2.setOption(option2);
	myChart3.setOption(option3);
	
	myChart1Query();
	myChart2Query();
	myChart3Query();
	
	/*待办事项*/
	showWaitWorkDatagrid();
	loadDataToWaitWorkgrid();
	//留言和公告
	showMessageAndNoticegrid();
	
});


var waitworkDatagrid;
function showWaitWorkDatagrid() {
	waitworkDatagrid = $('#waitworkDatagrid').datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		onDblClickRow : function(index, row) {
			window.parent.addTab(row.title, row.webpath, row.resourceid);
		},
		columns : [ [{field : "wholename",title : "类别",halign : 'center', width:'80%',sortable:false}
		              ,{field : "count",title : "任务数量",halign : 'center',align : 'right',	width:'20%',sortable:false	}
		             ] ]
	});
}

function loadDataToWaitWorkgrid() {
	$.post(contextpath + 'sys/toRemind/ToRemindController/findWaitWorkInfo.do', {}, function(data){
		var wwdata = new Array();
		
		if (data.length > 0) {
			for (var n = 0; n < data.length; n++) {
					wwdata.push(data[n]);
			}
			waitworkDatagrid.datagrid('loadData', wwdata);
		} else {
			waitworkDatagrid.datagrid('loadData', wwdata);
		}
	}, "json");
}

var showMessageAndNoticegrid;
function showMessageAndNoticegrid()
{
	showMessageAndNoticegrid= $("#showMessageAndNoticegrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		url : contextpath + 'sys/notice/SysNoticeManageController/qryReadAllMessageAndNoticeIndex.do?',
		onDblClickRow : function(index, row) {
			
			if (row.messagetype == 1){//公告
				showNoticeMsg(row.noticeid);
			} else if (row.messagetype ==2){//留言
				showMessageMsg(row.noticeid);
			}
		},
		columns : [ [
		             {field : "indextitle",title : "标题",halign : 'center', width:'76%',sortable:false}
		              ,{field : "releasetime",title : "时间",halign : 'center',width:'24%',sortable:false	}
		              
		             ] ]
	});
}


//查看留言公告主体详情
function showNoticeMsg(noticeid)
{
	showModalDialog(570, "readNoticeForm", "edit", showMessageAndNoticegrid, "反馈信息",
			"sys/notice/SysNoticeManageController/feedbackNoticeInit.do?messageType=1&noticeId="+noticeid, urls.feedbackNotice,'1');
	
}

function showMessageMsg(noticeid)
{
	showModalDialog(570, "readNoticeForm", "edit", showMessageAndNoticegrid, "反馈信息",
			"sys/notice/SysNoticeManageController/feedbackNoticeInit.do?messageType=2&noticeId="+noticeid, urls.feedbackNotice,'2');
}


function showModalDialog(width, form, operType, dataGrid, title, href, url,messageType) {

	var icon = 'icon-' + operType;
	var receiveid="";
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 810,
		height : width,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			f.form("load", row);
		    
			
			if(messageType == '2')
			{
				receiveid=row.receiveid;
			}
			
			//获取内容信息，在控件中展示
			$.post(urls.getClobContentVal, {noticeid:row.noticeid}, function(data){
				//window.parent.editor.html(data);
				mdDialog.find("#contentEdit").html(data);
				//将编辑器值赋值给隐藏域content
				contentVal = mdDialog.find("#content").val(data);
			}, "json");
			
			//展示上传附件
			showFileDiv(mdDialog.find("#filetd"),false,"NOTICE",row.noticeid,"");

			//判断是否上传附件datagrid展示不同的高度
			var fileCount =mdDialog.find("#fileCount").val();
			var setHeight;
			if(fileCount > 0)
			{
				setHeight='134px';
			}
			else
			{
				setHeight='174px';
			}
			mdDialog.find("#commonlist").datagrid({
				height:setHeight,
				width:'100%',
				stripe : true,
				singleSelect : true,
				rownumbers : true,
				pagination : true,
				remoteSort:false,
				multiSort:true,
				pageSize : 30,
				url : urls.showCommonBySelfUrl+"?noticeid="+row.noticeid+"&messageType="+messageType,
				loadMsg : "正在加载，请稍后......",
				toolbar : "#toolbar_common",
				showFooter : true,
				columns : [ [ 
				{field : "title",title : "标题",halign : 'center',width : 200,sortable:true}
				, {field : "commentusername",title : "评论人",halign : 'center',width : 140,sortable:true}
				, {field : "commentcontent",title : "评论内容",halign : 'center',width : 250,sortable:true}
				, {field : "commenttime",title : "评论时间",halign : 'center',width : 150,sortable:true}
				] ]
			});
			
		},
		buttons : funcOperButtons(operType, url, dataGrid, form,receiveid,messageType)
	});

}

/**
 * 根据操作类型来获取操作按钮
 * 
 * @param operType
 *            操作类型
 * @param url
 *            对应的操作URL
 * @returns
 */
function funcOperButtons(operType, url, dataGrid, form,receiveid,messageType) {

	var buttons;
	if(operType == "edit")
	{
		buttons = [ {
			text : "反馈",
			iconCls : "icon-edit",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//弹出反馈信息框
				showCommonModalDialog(parent.$('#commontService'),url,receiveid,messageType);
			}
		},
		{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	return buttons;
}
/**
 * 展示反馈内容
 * @param div
 * @param dialog
 * @return
 */
function showCommonModalDialog(div,url,receiveid, messageType)
{
	div.dialog(
	{
		title : "反馈信息",
		width : 500,
		height : 200,
		href : urls.showCommonDialogUrl,
		iconCls : 'icon-edit',
		modal : true,
		buttons : [
			{
				text : "确定",
				iconCls : "icon-save",
				handler : function() {
					parent.$('#commontService').dialog.openner_dataGrid = parent.$("#commonlist");
				    var commonCxt = parent.parent.$("#commentContent").val();
				    if(commonCxt == '' || commonCxt == null)
				    {
				    	parent.$.messager.alert('提示', "请填写反馈内容！", 'warnning');
				    }
				    else
				    {
				    	//ajax请求，添加评论信息
						$.post(url, {
							commentContent:commonCxt,
							receiveid:receiveid,
							noticeid:parent.$("#noticeid").val()
						}, function(result) {
							easyui_auto_notice(result, function() {
								//刷新历史反馈列表
								var noticeidVal = parent.$("#noticeid").val();
								var param = {
										noticeid : noticeidVal,
										messageType : messageType
										
									};
								parent.$('#commontService').dialog.openner_dataGrid.datagrid("load", param);
								parent.$('#commontService').dialog('close');
								
								
							});
						}, "json");
				    }
					
				}
			},
			{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$('#commontService').dialog('close');
				}
			}]
	});
}