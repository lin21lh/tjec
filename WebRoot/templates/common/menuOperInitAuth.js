/**
 * 
 */

$(function(){
	
	var tab = parent.$("#tabs").tabs("getSelected");
	var menuid = tab.panel("options").id;
	
//	$(".toolbar_buttons a").hide();
	$.post(contextpath + "menuOper.do", {menuid:menuid}, function(data){
		console.log(data);
		if(data){
			for(var i=0; i<data.length; i++){
				var d = data[i];
				$(".toolbar_buttons a#"+d.code+"]").show();
			}	
		}
		
	}, "json");
	
});