var baseUrl = contextpath + "aros/homepage/HomepageController/";
var baseUrl1 = contextpath + "aros/tjfx/ChartController/";

var urls = {
	myChart5Url : baseUrl + "myChart5Query.do",
	myChart3Url : baseUrl1 + "caseStateQuery.do"
};

/*图表全局变量定义*/
var myChart5 ;
var myChart3 ;

option5 = {
    title : {
        text: '案件所属区域统计',
        x : 'center'
    },
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        formatter: "{b} <br/>{a} : {c} %"
    },
    legend: {
    	show : true,
    	x : 'left',
//	    	orient : 'vertical',
        data:['案件占比']
    },
    toolbox: {
        show : true,
        feature : {
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
//		    grid: {
//	            top: 80,
//	            bottom: 100,
//	            left:150
//	        },
    calculable : true,
    xAxis: {
        type:'category',
        axisLabel:{'interval':0},
        data:['1','2','3','4','5','6','7','8','9'],
        splitLine: {show: false}
    },
    yAxis: [  
        {  
            type: 'value',  
            axisLabel: {  
                  show: true,  
                  interval: 'auto',  
                  formatter: '{value} %'  
            }
        }  
    ],  
    series : [
  	        {
  	            name : '案件占比',
  	            type : 'bar',
  	            barMaxWidth : '30%',
  	            itemStyle : {
  	            	normal : {
  	            		label : {
  	            			show : true,
  	        				position : 'top',
	  	        			formatter : '{c} %'
  	            		}
  	            	}
  	            },
  	            data:[50,10,30,10]
  	        }
  	    ]
};

option3 = {
	title : {//标题
        text: '全市案件办理结果统计',
        x:'center'
    },
    tooltip : {//鼠标悬停提示组件
        trigger: 'item',
        formatter: "{b} <br/>{a} : {c} %"
    },
    label: {//显示提示样式
        normal: {
            formatter :  "{b} : {d}%"
        }
    },
    legend: {//图例组件
        orient : 'vertical',
        x : 'left',
        data:['收案未立案','立案未结案','结案未归档','结案已归档']
    },
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
            radius : [0,'60%'],
            center: ['50%', '60%'],
            data:[
                {value:335, name:'收案未立案'},
                {value:310, name:'立案未结案'},
                {value:0, name:'结案未归档'},
                {value:234, name:'结案已归档'}
            ]
        }
    ]
};

function myChart5Query(){
	$.ajax({
		type : "post",
		url : urls.myChart5Url,
		data : {
			/*year : $("#year").combobox("getValue")*/
		},
		dataType : 'json',
		async : false,
		success : function(result){
			myChart5.setOption(result);
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

$(function(){
	
	/*高度初始化*/
	var bodyHeightPx = $("#layout").css("height");
	var bodyHeight = bodyHeightPx.replace('px','');
	var h = bodyHeight-3;
	$("#chart12").css("height",h+"px");
	
	myChart5 = echarts.init($("#chart5").get(0));
	myChart5.setOption(option5);
	
	myChart5Query();
	
	myChart3 = echarts.init($("#chart3").get(0));
	myChart3.setOption(option3);
	
	myChart3Query();
	
});