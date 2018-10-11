var baseUrl = contextpath + "aros/homepage/HomepageController/";
var urls = {
		//查询项目信息
		myChart1Url : baseUrl + "myChart1Query.do",
		myChart2Url : baseUrl + "myChart2Query.do",
		myChart3Url : baseUrl + "myChart3Query.do",
		viewUrl : baseUrl + "msgView.do",
		todoUrl : baseUrl + "todo.do"
		
};

/*图表全局变量定义*/
var myChart1 ;
var myChart2 ;
var myChart3 ;

option1 = {
	    title : {
	        text: '行政复议案件统计',
	        x : 'center'
	    },
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
//	        formatter: "{b} <br/>{a} : {c} "
	    },
	    legend: {
	    	show : true,
	    	x : 'left',
	        data:['数量']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
//	    grid: {
//            top: 80,
//            bottom: 100,
//            left:150
//        },
	    calculable : true,
	    xAxis: {
            type:'category',
            axisLabel:{'interval':0},
            data:['案件总数','已完成','已受理','待受理'],
            splitLine: {show: false}
        },
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'数量',
	            type:'bar',
	            barMaxWidth: '30%',
	            label: {
	                normal: {
	                    show: true,
	                    position: 'top'
	                }
	            },
	            data:[50,10,30,10]
	        }
	    ]
	};

option2 = {
	    title : {//标题
	        text: '待处理',
	        x:'center'
	    },
	    tooltip : {//鼠标悬停提示组件
	        trigger: 'item',
//	        formatter: "{b} <br/>{a} : {c} ({d}%)"
	    },
	    label: {//显示提示样式
            normal: {
                formatter :  "{b} : {c}"
            }
        },
//	    legend: {//图例组件
//	        orient : 'vertical',
//	        x : 'left',
//	        data:['审批','中止','恢复','终止','和解','延期']
//	    },
	    toolbox: {//工具标签
	        show : true,
	        feature : {
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    series : [
	        {
	            name:'数量',
	            type:'pie',
	            radius : '60%',
	            center: ['50%', '60%'],
	            data:[
	                {value:335, name:'审批'},
	                {value:310, name:'中止'},
	                {value:0, name:'恢复'},
	                {value:234, name:'终止'},
	                {value:234, name:'和解'},
	                {value:2, name:'延期'}
	            ]
	        }
	    ]
	};

option3 = {
	    title : {
	        text: '行政复议按月统计',
	        x : 'center'
	    },
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
//	        formatter: "{b} <br/>{a} : {c} "
	    },
	    legend: {
	    	show : true,
	    	x : 'left',
//	    	orient : 'vertical',
	        data:['待受理','已受理','已完成']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
//	    grid: {
//            top: 80,
//            bottom: 100,
//            left:150
//        },
	    calculable : true,
	    xAxis: {
            type:'category',
            axisLabel:{'interval':0},
            data:['2016年8月','2016年7月','2016年6月','2016年5月'],
            splitLine: {show: false}
        },
	    yAxis : {
	            type : 'value'
	    },
	    series : [
	        {
	            name:'待受理',
	            type:'bar',
	            stack: '堆叠',
	            barMaxWidth: '30%',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideTop'
	                }
	            },
	            data:[50,10,30,10]
	        },{
	            name:'已受理',
	            type:'bar',
	            stack: '堆叠',
	            barMaxWidth: '30%',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideTop'
	                }
	            },
	            data:[70,100,2,90]
	        },{
	            name:'已完成',
	            type:'bar',
	            stack: '堆叠',
	            barMaxWidth: '30%',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideTop'
	                }
	            },
	            data:[30,50,50,90]
	        }
	    ]
	};

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
			myChart1.setOption(result);
		}
	});
}
function myChart3Query(){
	$.ajax({
		type : "post",
		url : urls.myChart3Url,
		data : {
			
		},
		dataType : 'json',
		async : false,
		success : function(result){
			myChart3.setOption(result);
		}
	});
	
}

//消息提醒
var message;
function showMessage(){
	message= $("#message").datagrid({
		title:'消息提醒',
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		url : contextpath + 'aros/xxtx/controller/BMsgbaseinfoController/loginPageQueryMsg.do',
		onDblClickRow : function(index, row) {
			
		},
		onLoadSuccess : function (data){
			var num = $("#message").datagrid("getRows").length;
			if(num < 10)
			for(var i = 10-num; i > 0 ;i--){
				$('#message').datagrid('appendRow',{});
			}
			// 移除空行行号
			var nowNum = $("#message").datagrid("getRows").length;
			for(var j= nowNum-1;j > num-1; j--){
				$("#datagrid-row-r1-1-"+j + " div.datagrid-cell-rownumber")[0].innerText = "";
			}
		},
		onClickRow : function(rowIndex, rowData){
			if(undefined != rowData.msgcontent){
				msgInfoView(rowData);
			}
		},
		columns : [ [
		             {field : "msgcontent",title : "内容",halign : 'center', width:'60%',sortable:false}
		              ,{field : "msgtype",title : "类型",halign : 'center',width:'20%',sortable:false	}
		              ,{field : "gnetime",title : "时间",halign : 'center',width:'16%',sortable:false	}
	             ] ]
	});
}

var tiaoUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urlmap={
	    w01_1 : tiaoUrl + "xzfyReqInit.do",
	    w01_2 : tiaoUrl + "xzfyAcceptInit.do",
	    w01_3 : tiaoUrl + "xzfyReviewInit.do",
	    w01_4 : tiaoUrl + "xzfyDecisionInit.do",
	    w02_1 : tiaoUrl + "xzfySuspendReqInit.do",
	    w02_2 : tiaoUrl + "xzfySuspendAuditInit.do",
	    w03_1 : tiaoUrl + "xzfyRecoverReqInit.do",
	    w03_2 : tiaoUrl + "xzfyRecoverAuditInit.do",
	    w04_1 : tiaoUrl + "xzfyEndReqInit.do",
	    w04_2 : tiaoUrl + "xzfyEndAuditInit.do",
	    w05_1 : tiaoUrl + "xzfyCompromiseReqInit.do",
	    w05_2 : tiaoUrl + "xzfyCompromiseAuditInit.do",
	    w06_1 : tiaoUrl + "xzfyCancelReqInit.do",
	    w06_2 : tiaoUrl + "xzfyCancelAuditInit.do",
	    w07_1 : tiaoUrl + "xzfyDelayReqInit.do",
	    w07_2 : tiaoUrl + "xzfyDelayAuditInit.do",
	    w08_1 : tiaoUrl + "xzfyAvoidReqInit.do",
	    w08_2 : tiaoUrl + "xzfyAvoidAuditInit.do"
	};
var titlemap={
	    w01_1 : "复议申请",
	    w01_2 : "复议受理",
	    w01_3 : "复议审理",
	    w01_4 : "复议决定",
	    w02_1 : "中止发起",
	    w02_2 : "中止审批",
	    w03_1 : "恢复发起",
	    w03_2 : "恢复审批",
	    w04_1 : "终止发起",
	    w04_2 : "终止审批",
	    w05_1 : "和解发起",
	    w05_2 : "和解审批",
	    w06_1 : "撤销发起",
	    w06_2 : "撤销审批",
	    w07_1 : "延期发起",
	    w07_2 : "延期审批",
	    w08_1 : "回避发起",
	    w08_2 : "回避审批"
	};
//待处理任务
var todoDiv;
function showToDo(){
	var title =  "<div style='float:left;'>待办任务</div><div style='float:left;'> &nbsp;（以剩余天数预警：【</div>" +
			"<div style='float:left;width:15px;height:15px;border-radius:50px;border:solid rgb(100,100,100) 0px;background-color: red'></div>" +
			"<div style='float:left;'>=0天；</div>" +
			"<div style='float:left;width:15px;height:15px;border-radius:50px;border:solid rgb(100,100,100) 0px;background-color: orange'></div>" +
			"<div style='float:left;'><=12天；</div>" +
			"<div style='float:left;width:15px;height:15px;border-radius:50px;border:solid rgb(100,100,100) 0px;background-color: yellow'></div>" +
			"<div style='float:left;'><=24天】）</div>";
	todoDiv= $("#todo").datagrid({
		title:title,
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : false,
		remoteSort:false,
		multiSort : false,
		scrollbarSize :5,
		url : urls.todoUrl,
		onDblClickRow : function(index, row) {
		},
		onLoadSuccess : function (data){
			var num = $("#todo").datagrid("getRows").length;
			if(num < 10)
			for(var i = 10-num; i > 0 ;i--){
				$('#todo').datagrid('appendRow',{});
			}
			// 移除空行行号
			var nowNum = $("#todo").datagrid("getRows").length;
			for(var j= nowNum-1;j > num-1; j--){
				$("#datagrid-row-r2-1-"+j + " div.datagrid-cell-rownumber")[0].innerText = "";
			}
		},
		onClickRow : function(rowIndex, rowData){
			var menuid = 510;
			var title = titlemap["w"+rowData.key];
			var url = urlmap["w"+rowData.key];
			parent.addTab(title,url,menuid);
		},
		columns : [ [
					{field:"opt", title:"",width:'30px', align:"center",
						formatter: function(value,row,index){
							if (row.day <= 0){
								return '<div style="width:15px;height:15px;border-radius:50px;border:solid rgb(100,100,100) 0px;background-color: red"></div>';
							}
							else if(row.day <= 12){
								return '<div style="width:15px;height:15px;border-radius:50px;border:solid rgb(100,100,100) 0px;background-color: orange"></div>';
							}else if(row.day <= 24){
								return '<div style="width:15px;height:15px;border-radius:50px;border:solid rgb(100,100,100) 0px;background-color: yellow"></div>';
							}else { return '';}
						}
					},
					{
						field : "day",
						title : "剩余天数",
						halign : 'center',
						width:'10%',
					 },
					 {
						field : "nodename",
						title : "流程节点",
						halign : 'center',
						width:'10%',
					 },
					 {
						field : "proname",
						title : "流程类型",
						halign : 'center',
						width:'10%',
					},{
		     			field : "csaecode",
		     			title : "案件编号",
		     			halign : 'center',
		     			width:'25%',
		     		}, {
		     			field : "appname",
		     			title : "申请人",
		     			halign : 'center',
		     			width:'17%',
		     		}, {
		     			field : "defname",
		     			title : "被申请人",
		     			halign : 'center',
		     			width:'20%',
		     		}
	             ] ]
	});
}
/**
 * 消息详情
 */
function msgInfoView(rowData) {
	parent.$.modalDialog({
		title : "消息详情",
		width : 530,
		height : 200,
		href : urls.viewUrl+ "?id=" + rowData.id,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var f = mdDialog.find('#ruleInfoForm');
			f.form("load", rowData);
		},
		buttons : [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
				$("#message").datagrid("reload");
			}
		} ]
	});
}

var nowYear = parseInt((new Date()).getFullYear());//当前年份
$(function(){
	/*高度初始化*/
	var bodyHeightPx = $("#layout").css("height");
	var bodyHeight = bodyHeightPx.replace('px','');
	var sep1Px = $("#sep1").css("height");
	var sep1 = sep1Px.replace('px','');
	var sep2Px = $("#sep2").css("height");
	var sep2 = sep2Px.replace('px','');
	var h = (bodyHeight-sep1-sep2-3)/2;
	$("#chart12").css("height",h+"px");
	$("#chart34").css("height",h+"px");
	
	/*年度时间选择初始化*/
	var years = new Array(10);
	for(var i=0;i<10;i++){
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
		        {text : years[4], value : years[4]},
		        {text : years[5], value : years[5]},
		        {text : years[6], value : years[6]},
		        {text : years[7], value : years[7]},
		        {text : years[8], value : years[8]},
		        {text : years[9], value : years[9]}
		      
		],
		onChange : function(ne,ol) {
			/*在此添加查询*/
			myChart1Query();
			
		}
	});
	
	myChart1 = echarts.init($("#chart1").get(0));
	myChart3 = echarts.init($("#chart3").get(0));
	
	myChart1.setOption(option1);
	myChart3.setOption(option3);
	
	myChart1.on('click',function(param){
		console.log(param);
	});
	
	myChart3.on('click',function(param){
		console.log(param);
	});
	
	myChart1Query();
	myChart3Query();
	
	showMessage();
	showToDo();
	
});