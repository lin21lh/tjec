(function ($) {
	$.fn.datagridDialogBox = function (options, param) {
		
		if (typeof (options) == "string") {
			var method = $.fn.datagridDialogBox.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.treegrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function () {
			var state = $.data(this, "datagridDialogBox");  
	        if (state) {  
	            $.extend(state.options, options);  
	        } else {  
	            $.data(this, "datagridDialogBox", { options: $.extend({}, $.fn.datagridDialogBox.defaults, $.fn.datagridDialogBox.parseOptions(this), options)});  
	        };
	        buildDatagridDialogBox(this);
	     });  
	};
	
	function buildDatagridDialogBox(target) {
		
	    var opts = $.data(target, 'datagridDialogBox').options;
	    $(target).datagridDialogBox("onRender");
	    $(target).searchbox($.extend({}, opts, {
	    	searcher : function(value, name) {
	    		parent.$.fastModalDialog({
	    			title :opts.title,
	    			width : opts.dialogWidth,
	    			height : opts.dialogHeight,
	    			iconCls:'icon-search',
	    			html:'<div><table id="'+opts.tabledatagridID+'" ></table></div>',
	    			dialogID : opts.dialogID,
	    			onOpen: function(){
	    					var grid_ = parent.$.fastModalDialog.handler[opts.dialogID].find('#' + opts.tabledatagridID);
	    					grid_.datagrid({
	    						url : opts.datagridUrl,
	    						columns : opts.columns,
	    						view : scrollview,
	    						//pagination : true, //默认为false，是否显示在底部分页栏
	    						pageSize : 100,//默认分页数为10条
	    						singleSelect:true,
	    						fit:true,
	    						onDblClickRow : function(rowIndex, rowData) {
	    							if (!opts.dblClickRow)
	    								return ;
	    							
	    							$(target).datagridDialogBox('setText', rowData[opts.textField]);
	    							$(target).datagridDialogBox('setValue', rowData[opts.valueField]);
    								parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
	    						}
	    					});
	    			},
	    			buttons: [	
	    						{
	    							text:"选择",
	    							iconCls: "icon-ok",
	    							handler: function(){
	    								var sel=parent.$.fastModalDialog.handler[opts.dialogID].find('#' + opts.tabledatagridID).datagrid('getSelected');
	    								$(target).datagridDialogBox('setText', sel[opts.textField]);
		    							$(target).datagridDialogBox('setValue', sel[opts.valueField]);
	    								parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
	    							}
	    						},
	    					    {
	    					    	text:"取消",
	    					    	iconCls: "icon-cancel",
	    					    	handler: function(){
	    					    		parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
	    					    	}
	    					    }, {
								text : "清空",
								iconCls : "icon-reload",
								handler : function() {
									$(target).datagridDialogBox('setText', '');
	    							$(target).datagridDialogBox('setValue', '');
									parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
								}
	    					  }]
	    		});
	    		$(".datagrid-pager > table").hide();
	    	},
	    	getValue : function() {
	    		return $('#'+opts.valueField).val();
	    	},
	    	getText : function() {
	    		return $(target).searchbox('getValue');
	    	}
	    }));
	}  
	
	$.fn.datagridDialogBox.parseOptions = function(target) {
		 return $.extend({}, $.fn.searchbox.parseOptions(target), {});
	};
	$.fn.datagridDialogBox.methods = {
	    options : function (jq) {  
	        var opts = $.data(jq[0], "datagridDialogBox").options;  
	        return opts;  
	    },
	    onRender : function(jq) {
	    	
	    	var dgDialog = $(jq[0]);
	    	var opts = $.data(jq[0], "datagridDialogBox").options;
	    	var valueField = opts.valueField;
	    	if (valueField) {
	    		ddbObj = dgDialog.before("<input type='hidden' id='" + valueField + "' name='" + valueField + "' />")[0];
	    	}
	    },
	    setText : function(jq, value) {
	    	$(jq).searchbox('setValue', value);
	    },
	    setValue : function(jq, value) {
	    	var opts = $.data(jq[0], "datagridDialogBox").options;
	    	var valueField = opts.valueField;
	    	$('#'+valueField).val(value);
	    }
	};
	$.fn.datagridDialogBox.defaults = $.extend({}, $.fn.searchbox.defaults, {
		title : null,
		dialogWidth : 450,
		dialogHeight : 400,
		textField : null,
		valueField : null, 
		iconCls : 'icon-search',
		dblClickRow : true,
		singleSelect : true,
		dialogID : 'dlg90',
		tabledatagridID : 'grid_9981',
		datagridUrl : null,
		columns : null
	
	}
);})(jQuery);