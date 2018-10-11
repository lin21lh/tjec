var baseUrl = contextpath + "aros/homepage/HomepageController/";

var urls = {
	//所属区域案件统计
	myChart5Url : baseUrl + "myChart5Query.do",
};

/*图表全局变量定义*/
var myChart5 ;

option5 = {
    title : {
        text: '所属区域案件统计',
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
//    	orient : 'vertical',
        data:['案件占比']
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

/*var nowYear = parseInt((new Date()).getFullYear()); //当前年份
*/$(function(){
	
	/*高度初始化*/
	var bodyHeightPx = $("#layout").css("height");
	var bodyHeight = bodyHeightPx.replace('px','');
	var sep5Px = $("#sep5").css("height");
	var sep5 = sep5Px.replace('px','');
	var h = bodyHeight-sep5-5;
	$("#chart5").css("height",h+"px");
	
	/*年度时间选择初始化
	var years = new Array(10);
	for(var i=0; i<10; i++){
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
			在此添加查询
			myChart5Query();
		}
	});*/
	
	myChart5 = echarts.init($("#chart5").get(0));
	myChart5.setOption(option5);
	
	myChart5Query();
});