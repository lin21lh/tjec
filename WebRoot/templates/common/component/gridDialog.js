(function ($) {
	$.fn.gridDialog = function (options, param) {
		if (typeof (options) == "string") {
			var method = $.fn.gridDialog.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.searchbox(options, param);
			}
		}
		
		options = options || {};
		return this.each(function () {
			var state = $.data(this, "gridDialog");  
	        if (state) {  
	            $.extend(state.options, options);  
	        } else {  
	            $.data(this, "gridDialog", { options: $.extend({}, $.fn.gridDialog.defaults, $.fn.gridDialog.parseOptions(this), options)});  
	        };
	        createGridDialog(this);
	     });  
	};
	
	function createGridDialog(target) {
	    var opts = $.data(target, 'gridDialog').options;
	    $(target).gridDialog("onRender");
	    $(target).gridDialog("onChange");
	    
	    if(opts.showSearchWin){
	    
		    $(target).searchbox($.extend({}, opts, {
		    	searcher : function(value, name) {
		    		if (opts.beforeSearchFunc) {
		    	    	var dynamicParams = (opts.beforeSearchFunc)();
		    	    	
		    	    	if (dynamicParams == false)
		    	    		return ;
		    	    	
		    	    	for(var paramKey in dynamicParams) {
		    	    		opts.queryParams[paramKey] = dynamicParams[paramKey];
		    	    	}
		    	    	
		    		}
		    		var buttons = [{
		    							text:"确定",
		    							iconCls: "icon-ok",
		    							handler: function(){
		    								var node = parent.$.fastModalDialog.handler[opts.dialogID].find('#' + opts.gridID).tree('getSelected');
		    								$(target).gridDialog('setText', node.text);
			    							$(target).gridDialog('setValue', node.code);
		    								parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
		    							}
		    						}, {
										text : "清空",
										iconCls : "icon-reload",
										handler : function() {
											$(target).gridDialog('setText', '');
			    							$(target).gridDialog('setValue', '');
											parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
										}
		    					    }];
		    		
		    		var content = '<div>'+
		    						'<div id="layout_gridDialog" class="easyui-layout">' + 
			    						'<div region="north" style="height:30px;background-color:#eee;padding: 0px 5px;" border="true">' + 
				    						opts.filter.cncode + '&nbsp;&nbsp;&nbsp;&nbsp;<input id="code" class="easyui-textbox" style="width:120px;" />&nbsp;&nbsp;&nbsp;&nbsp;' +
			    							opts.filter.cnname + '&nbsp;&nbsp;&nbsp;&nbsp;<input id="name" class="easyui-textbox" style="width:120px;"/>&nbsp;&nbsp;&nbsp;&nbsp;' +
			    							'<a id="btn_query" class="easyui-linkbutton" href="#" iconCls="icon-search">查询</a>'+
			    						'</div>'+
			    						'<div region="center">' + 
			    							'<table id="'+opts.gridID+'" ></table>'+
			    						'</div>'+
		    						'</div>'+
		    					  '</div>';
		    		
		    		parent.$.fastModalDialog({
		    			title :opts.title,
		    			width : opts.dialogWidth,
		    			height : opts.dialogHeight,
		    			iconCls:'icon-search',
		    			html: content,
		    			dialogID : opts.dialogID,
		    			onOpen: function(){
		    				
		    				parent.$.messager.progress({
		    					msg: opts.loadMsg
		    				});
		    				
		    				var jqDialog = parent.$.fastModalDialog.handler[opts.dialogID];
		    				var layout_gridDialog = jqDialog.find("#layout_gridDialog");
		    				layout_gridDialog.layout({
		    					fit:true
		    				});
		    				
		    				var iconOpts = {
	    						iconCls:'icon-clear'
	//    						fn:function(){
	//		    					_grid.datagrid("reload", {
	//	    							elementcode: jqDialog.find("#code").val(),
	//	    							elementname: jqDialog.find("#name").val()
	//	    						});
	//    						}
		    				};
		    				
		    				jqDialog.find("#code").textbox().textbox('addClearBtn', iconOpts);
		    				jqDialog.find("#name").textbox().textbox('addClearBtn', iconOpts);
		    				
		    				jqDialog.find("#btn_query").linkbutton({
		    					onClick: function(){
				    				var params = opts.queryParams;
				    				params[opts.filter.code] = jqDialog.find("#code").textbox("getValue");
				    				params[opts.filter.name] = jqDialog.find("#name").textbox("getValue");
		    						_grid.datagrid("reload", params);
		    					}
		    				});
		    				
		    				var _grid = jqDialog.find("#" + opts.gridID);
		    				_grid.datagrid({
		    					fit : true,
		    					singleSelect : opts.singleSelect,
		    					rownumbers: true,
								pagination : false,
								queryParams : opts.queryParams,
	//							view : scrollview,
	//							pageSize : 100,
								url : opts.url,
								columns : opts.cols,
								onDblClickRow: function(index, row){
									$(target).gridDialog('setText', row[opts.textField]);
	    							$(target).gridDialog('setValue', row[opts.valueField]);
	    							if(opts.assignFunc){
	    					    		(opts.assignFunc)(row);
	    					    	}
									parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
								},
	    						onLoadSuccess: function(data){
	    							parent.$.messager.progress("close");
	    						}
							});
		    				jqDialog.find(".datagrid-pager > table").hide();
		    			}
		    		});
		    	},
		    	getValue : function() {
		    		var _hiddenID = opts.hiddenID != null ? opts.hiddenID : opts.hiddenName;
		    		return $('#'+_hiddenID).val();
		    	},
		    	getText : function() {
		    		return $(target).searchbox('getValue');
		    	}
		    }));
	    }else{
	    	$(target).searchbox($.extend({}, opts, {}));
	    }
	    //清空searchbox的文本值和隐藏域值
	    $(target).searchbox("textbox").bind("change", function(value){
	    	var text = $(this).val();
	    	if(text == null || text == ""){
	    		var _hiddenID = opts.hiddenID != null ? opts.hiddenID : opts.hiddenName;
	    		$('#'+_hiddenID).val("");
	    	}
	    });
	    if(opts.showClearBtn){
	    	//增加清空按钮
		    $(target).searchbox({
		    	onChange:function(nv, ov){
		    		var icon = $(this).textbox('getIcon', 0);
					if (nv) {
						icon.css('visibility', 'visible');
					} else {
						icon.css('visibility', 'hidden');
						var _hiddenID = opts.hiddenID != null ? opts.hiddenID : opts.hiddenName;
						$('#'+_hiddenID).val("");
						(opts.clearFunc)();
					}
		    	}
		    }).searchbox("addClearBtn", {iconCls:"icon-clear"});
	    }
	    
	}
	
	$.fn.gridDialog.parseOptions = function(target) {
		 return $.extend({}, $.fn.searchbox.parseOptions(target), {});
	};
	$.fn.gridDialog.methods = {
	    options : function (jq) {  
	        var opts = $.data(jq[0], "gridDialog").options;  
	        return opts;  
	    },
	    onRender : function(jq) {
	    	var dgDialog = $(jq);
	    	var opts = $.data(jq[0], "gridDialog").options;
	    	var _hiddenID = opts.hiddenID != null ? opts.hiddenID : opts.hiddenName;
	    	dgDialog.before("<input type='hidden' id='" + _hiddenID + "' name='" + opts.hiddenName + "' />");

	    },
	    onChange : function(jq) {
	    	
	    },
	    setText : function(jq, value) {
	    	$(jq).searchbox('setValue', value);
	    },
	    setValue : function(jq, value) {
	    	var opts = $.data(jq[0], "gridDialog").options;
	    	var _hiddenID = opts.hiddenID != null ? opts.hiddenID : opts.hiddenName;
	    	$('#'+_hiddenID).val(value);
	    
	    },
	    getValue : function(jq) {
	    	var opts = $.data(jq[0], "gridDialog").options;
	    	var _hiddenID = opts.hiddenID != null ? opts.hiddenID : opts.hiddenName;
	    	return $('#'+_hiddenID).val();
	    },
	    clear : function(jq) {
    		$(jq).searchbox('setValue', null);
    		var opts = $.data(jq[0], "gridDialog").options;
	    	var _hiddenName = opts.hiddenName;
	    	$('#'+_hiddenName).val(null);
    	}
	};
	$.fn.gridDialog.defaults = $.extend({}, $.fn.searchbox.defaults, {
		title : '',
		dialogWidth : 360,
		dialogHeight : 500,
		name : null,
		hiddenID : null,
		hiddenName : null, 
		dblClickRow : true,
		queryParams : {
			
		},
		singleSelect : true,
		dialogID : 'dlg999999',
		gridID : 'grid_999999',
		url: contextpath + "base/dic/dicElementController/queryPageDicElement.do ",
		loadMsg: "正在加载数据，请稍候......",
		cols : [[
		]],
		filter: {
			code: "编码",
			name: "名称"
		},
		textField: "",
		valueField: "",
		assignFunc: function(row){
			//双击数据回填时的赋值事件处理
		},
		clearFunc: function(){
			//点击清空触发的事件处理
		},
		beforeSearchFunc:function() {
			
		},
		showSearchWin : true,
		showClearBtn : true
	}
);})(jQuery);