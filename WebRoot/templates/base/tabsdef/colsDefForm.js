$(function() {

	window.opts = {
		topMost : false,
		extToolbar : true,
		toolbar : [
				"<span style='font-size:small'>数据项编码：</span>",
				{
					id : "elementcode",
					type : "textbox",
					options : {
						"width" : 80
					}
				},
				"<span style='font-size:small'>数据项名称：</span>",
				{
					id : "elementname",
					type : "textbox",
					options : {
						"width" : 80
					}
				},
				{
					type : "button",
					options : {
						text : "查询",
						iconCls : "icon-search",
						plain : false,
						handler : function(toolbar) {
							var bar = $(toolbar), elementcode = bar.find(
									"#elementcode").val(), elementname = bar
									.find("#elementname").val(), dg = bar
									.closest(".grid-selector-container").find(
											".grid-selector");
							dg.datagrid("load", {
								elementcode : elementcode,
								elementname : elementname
							});
						}
					}
				} ],
		method : "POST",
		url : contextpath
				+ 'base/dic/dicElementController/queryPageDicElement.do',
		idField : "elementcode",
		textField : "elementname",
		singleSelect : true,
		autoShowPanel : false,
		multiple : false,
		remoteSort : false,
		pagination : true,
		columns : [ [ {
			field : "elementcode",
			title : "数据项编码",
			width : 200,
			sortable : true,
			filterable : true
		}, {
			field : "elementname",
			title : "数据项名称",
			width : 140,
			sortable : true
		}, {
			field : "elementtype",
			title : "数据项类型",
			width : 80,
			sortable : true,
			formatter : function(value) {
				return value == "0" ? "基础数据项" : "数据项视图";
			}
		} ] ],
		onEnter : function(val) {
			$.easyui.messager.show(String(val.map(function(val) {
				return val.ID;
			})));
		}
	};

	$("#src_Ele_Code").comboselector($.extend({
		title : '数据项列表'
	}, window.opts));

});
