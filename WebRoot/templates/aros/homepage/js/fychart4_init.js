var baseUrl = contextpath + "aros/homepage/HomepageController/";
var urls = {
		//查询项目信息
		myChart4Url : baseUrl + "myChart4Query.do",
		
};

/*图表全局变量定义*/
var myChart4 ;


option4 = {
	    title : {
	        text: '行政事项统计',
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
	        data:['待办任务','已受理','已完成']
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
            data:['行政处罚','行政强制措施','行政征收','行政许可','行政确权','信息公开','举报投诉处理','行政不作为'],
            splitLine: {show: false}
        },
	    yAxis : {
	            type : 'value'
	    },
	    series : [
	        {
	            name:'待办任务',
	            type:'bar',
	            stack: '堆叠',
	            barMaxWidth: '50%',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideTop'
	                }
	            },
	            data:[102,15,56,12,25,11,34,200]
	        },{
	            name:'已受理',
	            type:'bar',
	            stack: '堆叠',
	            barMaxWidth: '50%',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideTop'
	                }
	            },
	            data:[30,32,46,21,24,8,30,125]
	        },{
	            name:'已完成',
	            type:'bar',
	            stack: '堆叠',
	            barMaxWidth: '50%',
	            label: {
	                normal: {
	                    show: false,
	                    position: 'insideTop'
	                }
	            },
	            data:[50,22,12,12,33,19,67,49]
	        }
	    ]
	};


function myChart4Query(){
	$.ajax({
		type : "post",
		url : urls.myChart4Url,
		data : {
			year : $("#year").combobox("getValue")
		},
		dataType : 'json',
		async : false,
		success : function(result){
			myChart4.setOption(result);
		}
	});
	
}

var nowYear = parseInt((new Date()).getFullYear());//当前年份
$(function(){
	/*高度初始化*/
	var bodyHeightPx = $("#layout").css("height");
	var bodyHeight = bodyHeightPx.replace('px','');
	var sep1Px = $("#sep1").css("height");
	var sep1 = sep1Px.replace('px','');
	var h = bodyHeight-sep1-3;
	$("#chart4").css("height",h+"px");
	
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
			myChart4Query();
			
		}
	});
	myChart4 = echarts.init($("#chart4").get(0));
	myChart4.setOption(option4);
	
	myChart4Query();
	
});