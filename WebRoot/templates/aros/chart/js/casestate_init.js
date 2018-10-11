var baseUrl = contextpath + "aros/tjfx/ChartController/";

var urls = {
	//全市案件办理结果统计
	queryUrl : baseUrl + "caseStateQuery.do",
};

$(function() {	
	
	var bodyHeightPx = $("#layout").css("height");
	var bodyHeight = bodyHeightPx.replace('px','');
	
	var sepHeightPx = $("#sep").css("height");
	var sepHeight = sepHeightPx.replace('px','');
	
	var height = bodyHeight - sepHeight- 2;
	
	$("#chart").css("height", height+"px");
	$("#chart2").css("height", height+"px");
	
	myChart2 = echarts.init($("#chart2").get(0));
	myChart2.setOption(option2);
	
	chartQuery();
});

option2 = {
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

function chartQuery(){
	
	$.ajax({
		type : "post",
		url : urls.queryUrl,
		data : {
		},
		dataType : 'json',
		async : false,
		success : function(data){
			myChart2.setOption(data);
		}
	});
}