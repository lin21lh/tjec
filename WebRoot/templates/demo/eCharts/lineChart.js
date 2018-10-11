 // 路径配置
require.config({
    paths: {
        echarts: contextpath + '/component/eCharts/dist'
    }
});

var option = {
	//backgroundColor : '#87cefa', //全图默认背景 支持rgba，默认为无，透明
	renderAsImage : false, //非IE8-支持渲染为图片
	animation : true, //是否开启动画，默认开启
	title : { //标题（详见title），每个图表最多仅有一个标题控件
	    text: '未来一周气温变化', //主标题文本，'\n'指定换行
	    link : 'http://www.baidu.com', //主标题文本超链接
	    subtext: '纯属虚构' //副标题文本，'\n'指定换行
	},
	toolbox: {
	    show : true, //显示策略，可选为：true（显示） | false（隐藏）
	    orient : 'vertical', //布局方式，默认为水平布局，可选为：'horizontal' | 'vertical'
	    feature : {
	       // mark : {show: true},
	        dataView : {show: true, readOnly: false},
	        magicType : {show: true, type: ['line', 'bar'], title : {line : '折线图'}},
	        restore : {show: true},
	        saveAsImage : {show: true}
	    }
	},
	tooltip : {
	    trigger: 'axis'
	},
	legend: {
	    data:['最高气温','最低气温']
	},
	
	calculable : true,
	xAxis : [
	    {
	        type : 'category',
	        boundaryGap : false,
	        data : ['周一','周二','周三','周四','周五','周六','周日']
	    }
	],
	yAxis : [
	    {
	        type : 'value',
	        axisLabel : {
	            formatter: '{value} °C'
	        }
	    }
	],
	series : [
	    {
	        name:'最高气温',
	        type:'line',
	        data:[11, 11, 15, 13, 12, 13, 10],
	        markPoint : {
	            data : [
	                {type : 'max', name: '最大值'},
	                {type : 'min', name: '最小值'}
	            ]
	        },
	        markLine : {
	            data : [
	                {type : 'average', name: '平均值'}
	            ]
	        }
	    },
	    {
	        name:'最低气温',
	        type:'line',
	        data:[1, -2, 2, 5, 3, 2, 0],
	        markPoint : {
	            data : [
	                {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
	            ]
	        },
	        markLine : {
	            data : [
	                {type : 'average', name : '平均值'}
	            ]
	        }
	    }
	]
};


// 使用
require(
    [
        'echarts',
        'echarts/chart/line', 
        'echarts/chart/bar'// 使用柱状图就加载bar模块，按需加载
    ],
    function (ec) {
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main')); 

        // 为echarts对象加载数据 
        myChart.setOption(option); 
    }
);