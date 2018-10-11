$(function(){
		addTaskFlag();
		$('.zoom_span').Zoomer({speedView:200,speedRemove:400,altAnim:false,speedTitle:400,debug:false});
	});

//退出系统
function logoutSystem() {
	$.messager.confirm('确认', '是否注销用户?', function(r) {
		if (r) {
			window.location.href = contextpath;
		}
	});
}

//添加待办任务数量标志
function addTaskFlag(){
	if($("#LA")[0]!=null && waitTask != 0){
		var pos = getPos($("#LA")[0]);
		var pos_d = getPos($(".area_box")[0]);
		var cir = '<div style=" width:30px; height:30px; background-color:#F00; border-radius:15px;position:absolute;top: '+(pos.y-pos_d.y-15)+'px;left: '+(pos.x-pos_d.x+235)+'px;">';
		cir += '<span style="height:30px; line-height:30px; display:block; color:#FFF; text-align:center">'+waitTask+'</span>';
		cir += '</div>';
		$("#LA").after(cir);
	}
}
//获取某个html元素的定位  
function getPos(obj) {  
	var pos = new Object();  
	pos.x = obj.offsetLeft;  
	pos.y = obj.offsetTop;  
	while (obj = obj.offsetParent) {  
		pos.x += obj.offsetLeft;  
		pos.y += obj.offsetTop;  
	}  
	return pos;  
}; 