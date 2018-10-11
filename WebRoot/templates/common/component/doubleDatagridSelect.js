/**
 * 创建左右两个datagrid的选择控件 左边为可选,右边为已选
 * 
 * @param options
 *            参数格式如下：
 * 
 */

function makeDoubleDatagridSelect(options) {
	var baseHtml = "<div class='easyui-layout' id='easyui_layout_990' fit='true' >";
	baseHtml += "<div region='west' split='false' style='width:"
			+ options.dimension.leftGridWidth + "px' ><table id='"
			+ options.id.leftGridId + "'></table></div>";
	baseHtml += "<div region='center' split='true' style='background-color:#eee;text-align:center'>"
			+ "<div id='space_holder' ></div>"
			+ "<a class='easyui-linkbutton' iconCls='icon-right-arrow' style='width:30px' id='"
			+ options.id.addButtonId
			+ "' ></a><br/><a class='easyui-linkbutton' iconCls='icon-left-arrow' style='width:30px' id='"
			+ options.id.delButtonId + "' ></a></div>";
	baseHtml += "<div region='east' split='false' style='width:"
			+ options.dimension.rightGridWidth + "px'><table id='"
			+ options.id.rightGridId + "'></table></div>";
	baseHtml += '</div>';
	options.jQueryHandler.find('#' + options.id.container).html(baseHtml);

	options.jQueryHandler.find('#easyui_layout_990').layout();

	options.jQueryHandler.find('#' + options.id.addButtonId).linkbutton();
	options.jQueryHandler.find('#' + options.id.delButtonId).linkbutton();

	options.functions.init();
	
	var cpanel = options.jQueryHandler.find('#easyui_layout_990').layout(
			'panel', 'center');
	//适用于主页面
	cpanel.panel({
		onResize : function(w, h) {
			if(h){
				var button_height = 0;
				var display_btns = cpanel.find("a:visible");
				display_btns.each(function(i) {
					button_height += 30;
				});
				options.jQueryHandler.find('#space_holder').height(
						(h - button_height) / 2);
			}
		}
	});
	//适用于弹出框
	var total_height = cpanel.panel('options').height;
	
	var button_height = 0;
	var display_btns = cpanel.find("a:visible");
	display_btns.each(function(i) {
		button_height += 30;
	});
	options.jQueryHandler.find('#space_holder').height(
			(total_height - button_height) / 2);
	
	
	
}