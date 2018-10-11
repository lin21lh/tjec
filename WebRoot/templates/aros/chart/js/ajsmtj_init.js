var baseUrl = contextpath + "aros/tjfx/ChartController/";
var urls = {
	queryUrl : baseUrl + "ajsmtjQuery.do",
};

$(function() {	
	comboboxFuncByCondFilter(menuid, "anafrequency", "ANAFREQUENCY", "code", "name");// 职称
	comboboxFuncByCondFilter(menuid, "startyear", "ANALYSISYEAR", "code", "name");// 职称
	comboboxFuncByCondFilter(menuid, "endyear", "ANALYSISYEAR", "code", "name");// 职称
	comboboxFuncByCondFilter(menuid, "startmonth", "ANALYSISMONTH", "code", "name");// 职称
	comboboxFuncByCondFilter(menuid, "endmonth", "ANALYSISMONTH", "code", "name");// 职称
	
	
	$('#anafrequency').combobox({
		onChange : getAnafrequencyChange
	});
	$("#anafrequency").combobox("setValue","01");
	var nowtime = new Date();
	var year = nowtime.getFullYear();
	var month = nowtime.getMonth() + 1;
	if(month < 10){
		month = "0" + month;
	}
	$("#startyear").combobox("setValue",year);
	$("#endyear").combobox("setValue",year);
	$("#startmonth").combobox("setValue","01");
	$("#endmonth").combobox("setValue",month);
	
	var bodyHeightPx = $("#layout").css("height");
	var bodyHeight = bodyHeightPx.replace('px','');
	var sepHeightPx = $("#sep").css("height");
	var sepHeight = sepHeightPx.replace('px','');
	var toolbarHeightPx = $("#toolbar_center").css("height");
	var toolbarHeight = toolbarHeightPx.replace('px','');
	var height = bodyHeight -sepHeight- toolbarHeight - 2;
	$("#chart").css("height",height+"px");
	$("#chart1").css("height",height+"px");
	$("#mid").css("height",height+"px");
	$("#chart2").css("height",height+"px");
	
	myChart1 = echarts.init($("#chart1").get(0));
	myChart2 = echarts.init($("#chart2").get(0));
	myChart1.setOption(option1);
	myChart2.setOption(option2);
	
	chartQuery();
	
});

option1 = {
	    title : {
	        text: '案件数目趋势统计',
	        x : 'center'
	    },
	    tooltip : {
	        trigger: 'axis',
	    },
	    legend: {
	    	show : true,
	    	x : 'left',
	        data:['案件总数','已审结','已受理']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis: {
            type:'category',
            axisLabel:{'interval':0},
            data:['2015','2016','2017','2018'],
            splitLine: {show: false}
        },
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'案件总数',
	            type:'line',
	            stack:'数目',
	            data:[50,10,30,10]
	        },
	        {
	            name:'已审结',
	            type:'line',
	            stack:'数目',
	            data:[100,20,10,110]
	        },
	        {
	            name:'已受理',
	            type:'line',
	            stack:'数目',
	            data:[20,15,88,17]
	        }
	    ]
	};

option2 = {
	    title : {//标题
	        text: '案件数目占比统计',
	        x:'center'
	    },
	    tooltip : {//鼠标悬停提示组件
	        trigger: 'item',
//	        formatter: "{b} <br/>{a} : {c} ({d}%)"
	    },
//	    label: {//显示提示样式
//            normal: {
//                formatter :  "{b} : {c}"
//            }
//        },
	    legend: {//图例组件
	        orient : 'vertical',
	        x : 'left',
	        data:['2015','2016','2017','2018']
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
	            name:'案件总数',
	            type:'pie',
	            radius : [0,'20%'],
	            center: ['50%', '60%'],
	            label : {
		        	normal : {
		        		show : true,
		        		position : 'inside',
		        		formatter :  "{d}%"
		        	}
		        },
	            data:[
	                {value:335, name:'2015'},
	                {value:310, name:'2016'},
	                {value:0, name:'2017'},
	                {value:234, name:'2018'}
	            ]
	        },
	        {
	            name:'已审结',
	            type:'pie',
	            radius : ['30%','45%'],
	            center: ['50%', '60%'],
	            label : {
		        	normal : {
		        		show : true,
		        		position : 'inside',
		        		formatter :  "{d}%"
		        	}
		        },
	            data:[
	                {value:335, name:'2015'},
	                {value:310, name:'2016'},
	                {value:0, name:'2017'},
	                {value:234, name:'2018'}
	            ]
	        },
	        {
	            name:'已受理',
	            type:'pie',
	            radius : ['55%','70%'],
	            center: ['50%', '60%'],
	            label : {
		        	normal : {
		        		show : true,
		        		position : 'outside',
		        		formatter :  "{b} : {d}%"
		        	}
		        },
	            data:[
	                {value:335, name:'2015'},
	                {value:310, name:'2016'},
	                {value:0, name:'2017'},
	                {value:234, name:'2018'}
	            ]
	        }
	    ]
	};

function chartQuery(){
	var startyear = $("#startyear").combobox("getValue");
	var endyear = $("#endyear").combobox("getValue");
	var startmonth = $("#startmonth").combobox("getValue");
	var endmonth = $("#endmonth").combobox("getValue");
	if((startyear > endyear) || (startyear == endyear && startmonth > endmonth)){
		easyui_warn("统计开始日期不能小于结束日期！",null);
		return;
	}
	$.ajax({
		type : "post",
		url : urls.queryUrl,
		data : {
			anafrequency : $("#anafrequency").combobox("getValue"),
			startyear : $("#startyear").combobox("getValue"),
			endyear : $("#endyear").combobox("getValue"),
			startmonth : $("#startmonth").combobox("getValue"),
			endmonth : $("#endmonth").combobox("getValue")
		},
		dataType : 'json',
		async : false,
		success : function(data){
			$.each(data,function(i,n){
				if(i==0){
					myChart1.setOption(n);
				} else if (i==1){
					myChart2.setOption(n);
				}
			});
		}
	});
}

/**
 * 页面刷新
 */
function getAnafrequencyChange() {
	var anaType =$('#anafrequency').combobox('getValue');
	if(anaType != null || anaType!=''){
		if(anaType=='01'){
			$('#startm').attr("style", "display:block");
			$('#endm').attr("style", "display:block");
		} else if(anaType=='02'){
			$('#startm').attr("style", "display:none");
			$('#endm').attr("style", "display:none");
		}
	}
}