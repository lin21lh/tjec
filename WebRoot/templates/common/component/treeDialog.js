(function ($) {
	$.fn.treeDialog = function (options, param) {
		if (typeof (options) == "string") {
			var method = $.fn.treeDialog.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.tree(options, param);
			}
		}
		
		options = options || {};
		return this.each(function () {
			var state = $.data(this, "treeDialog");  
	        if (state) {  
	            $.extend(state.options, options);  
	        } else {  
	            $.data(this, "treeDialog", { options: $.extend({}, $.fn.treeDialog.defaults, $.fn.treeDialog.parseOptions(this), options)});  
	        };
	        buildTreeDialog(this);
	     });  
	};
	
	function recurseTreeNodes(kind, value, nodes, ndata){
		for(var i=0; i<nodes.length; i++){
			var node = nodes[i];
			node.state = "open"; 
			
			var flag = true;
			if(kind == "code"){
				flag = node[kind].indexOf(value)  == 0;
			}else{
				flag = node[kind].indexOf(value) != -1;
			}
			
			if(flag){
				ndata.push(node);
				var cdata = null;
				if(node.children != null){
					cdata = new Array();
					recurseTreeNodes(kind, value, node.children, cdata);
				}
				node.children = cdata;
			}else{
				var cdata = new Array();
				if(node.children != null){
					recurseTreeNodes(kind, value, node.children, cdata);
				}
				node.children = cdata;
				if(cdata.length != 0){
					ndata.push(node);
				}
			}
		}
	}
	
	function buildTreeDialog(target) {
		
	    var opts = $.data(target, 'treeDialog').options;
	    var _tree = null;
	    
	    function checkNode(valueModel, valArray, nodes) {
	    	for (var i=0; i<nodes.length; i++) {
	    		findLeaf(valueModel, valArray, nodes[i]);
			}
	    }
	    
	    function findLeaf(valueModel, valArray, node) {
	    	if (!_tree.tree('isLeaf', node.target)) {
	    		check(valueModel, valArray, node);
	    		var nodes = _tree.tree('getChildren', node.target);
	    		checkNode(valueModel, valArray, nodes);
			} else {
				check(valueModel, valArray, node);
			}
	    }
	    
	    function check(valueModel, valArray, node) {
	    	if (valueModel && valueModel == 'id') {
				if (valArray.indexOf(node.id) != -1) {
					_tree.tree('check', node.target);
				}
			} else {
				if (valArray.indexOf(node.code) != -1) {
					_tree.tree('check', node.target);
				}
			}
	    }

	    $(target).treeDialog("onRender");
	    $(target).treeDialog("onChange");
	    $(target).searchbox($.extend({}, opts, {
	    	searcher : function(value, name) {
	    	    if (opts.beforeSearchFunc) {
	    	    	var dynamicParams = (opts.beforeSearchFunc)();
	    	    	
	    	    	if (dynamicParams == false)
	    	    		return ;
	    	    	
	    	    	for(var paramKey in dynamicParams) {
	    	    		opts.queryParams[paramKey] = dynamicParams[paramKey];
	    	    	}
	    	    	opts.queryParams;
	    		}
	    		var buttons = [{
	    							text:"确定",
	    							iconCls: "icon-ok",
	    							handler: function(){
	    								var node = parent.$.fastModalDialog.handler[opts.dialogID].find('#' + opts.treeID).tree('getSelected');
	    								var text = '';
	    								var val = '';
	    								if (opts.multiSelect == true) {
	    									var nodes = _tree.tree('getChecked');
	    									if (nodes.length == 0) {
	    										easyui_warn('请选择数据！');
	    										return ;
	    									}
	    									for (var i=0; i<nodes.length; i++) {
	    										if (text.length > 0)
	    											text += ',';
	    										text += nodes[i].text;
	    										
	    										if (val.length > 0)
	    											val += ',';
	    										
	    										if (opts.valueModel && opts.valueModel == 'id') {
		    										val += nodes[i].id;
		    									} else {
		    										val += nodes[i].code;
		    									}
	    									}
	    								} else {
	    									text = node.text;
	    									if (opts.valueModel && opts.valueModel == 'id') {
	    										val = node.id;
	    									} else {
	    										val = node.code;
	    									}
	    								}
	    								$(target).treeDialog('setText', text);
	    								$(target).treeDialog('setValue', val);
	    								parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
	    							}
	    						}, {
									text : "清空",
									iconCls : "icon-reload",
									handler : function() {
										$(target).treeDialog('setText', '');
		    							$(target).treeDialog('setValue', '');
										parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
									}
	    					    }, {
	    					    	text : "关闭",
	    					    	iconCls : "icon-cancel",
	    					    	handler : function() {
	    					    		parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
	    					    	}
	    					    }];
	    		
	    		var content = '<div>'+
	    						'<div id="layout_agency" class="easyui-layout">' + 
		    						'<div region="north" style="height:30px;background-color:#eee;padding: 0px 5px;" border="true">' + 
		    							'<select id="agencyCat" type="text" class="easyui-combobox" panelHeight="auto" name="agencyCat" style="width:auto;">';
	    								if (opts.filters.code)
	    									content += '<option value="code" checked>' + opts.filters.code + '</option>';
	    								if (opts.filters.name)
	    									content += '<option value="text" checked>' + opts.filters.name + '</option>';
	    								content += '</select>'+
		    							'&nbsp;&nbsp;&nbsp;&nbsp;' +
		    							'<input id="agencyVal" type="text" class="easyui-textbox" name="agencyVal" style="margin:1px 3px;heigth:20px;"></input>'+
		    							'&nbsp;&nbsp;&nbsp;&nbsp;' +
		    							'<a id="btn_query" class="easyui-linkbutton" href="#" iconCls="icon-search">查询</a>'+
	    							'</div>'+
		    						'<div region="center">' + 
		    							'<ul id="'+opts.treeID+'" ></ul>'+
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
	    					msg: "正在加载数据，请稍候..."
	    				});
	    				
	    				//设置布局样式
	    				var jqDialog = parent.$.fastModalDialog.handler[opts.dialogID];
	    				var layout_agency = jqDialog.find("#layout_agency");
	    				layout_agency.layout({
	    					fit:true
	    				});
	    				
	    				var agencyCat = jqDialog.find("#agencyCat");
	    				agencyCat.combobox({
	    					editable: false
	    				});
	    				
	    				var agencyVal = jqDialog.find("#agencyVal");
	    				agencyVal.textbox().textbox("addClearBtn", 
    						{
    							iconCls: "icon-clear", 
    							clearFunc:function(){
	    							_tree.tree();
	    						}
    						}
	    				);

	    				agencyVal.textbox("textbox").bind("keyup", function(e){
	    					if(e.keyCode == 13){
	    						_tree.tree();
	    					}
	    				});
	    				
	    				var btn_query = jqDialog.find("#btn_query");
	    				btn_query.linkbutton({
	    					onClick: function(){
	    						_tree.tree();
	    					}
	    				});
	    				var queryParams = opts.queryParams;
	    				if (!queryParams)
	    					queryParams = {};
	    				queryParams.elementcode = opts.elementcode;
	    				queryParams.textModel = opts.textModel;
	    				
    					_tree = jqDialog.find('#' + opts.treeID);
    					_tree.tree({
    						url : opts.url,
    						method : 'post',
    						queryParams : queryParams,
    						animate : true,
    						dnd : false,
    						checkbox: opts.multiSelect,
    						cascadeCheck: false,
//    						onlyLeafCheck: true,
    						loadFilter: function(data, parent){
								var ndata = new Array();
								var kind = agencyCat.combobox("getValue");
								var value = agencyVal.textbox("textbox").val();
								if(value != null && value != ""){
									recurseTreeNodes(kind, value, data, ndata);
								}else{
									ndata = data;
								}
								return ndata;
							},
    						onBeforeSelect: function(node){
    							if (node.isLeaf == true)
    								return true;
    							if(opts.checkLevs.length != 0 && $.inArray(node.levelno, opts.checkLevs) == -1 || node.isleaf ) {
    								if(node.target && node.children != null){
    									if(node.expand){
    										_tree.tree("collapse", node.target);
    										node.expand = false;
    									}else{
    										_tree.tree("expand", node.target);
    										node.expand = true;
    									}
    								}
    								return false;
    							}
    							return true;
    						},
    						onClick: function(node){
    							_tree.tree("check", node.target);
    						},
    						onDblClick : function(node) {
    							if (!opts.dblClickRow){
    								return ;
    							}
    							
    							if(opts.checkLevs.length != 0 && $.inArray(node.levelno, opts.checkLevs) == -1){
    								return;
    							}
    							
  								$(target).treeDialog('setText', node.text);
  								if (opts.valueModel && opts.valueModel == 'id') {
  									$(target).treeDialog('setValue', node.id);
  								} else {
  									$(target).treeDialog('setValue', node.code);
  								}
								parent.$.fastModalDialog.handler[opts.dialogID].dialog('close');
    						},
    						onLoadSuccess: function(node, data){
    							if (opts.multiSelect == true) {
    								var values = $('#'+opts.hiddenName).val();
    								if (values != null && values.length > 0) {
    									var valArray = values.split(',');
    									var nodes = _tree.tree('getRoots');
    									checkNode(opts.valueModel, valArray, nodes);
    								}
    							}
    							parent.$.messager.progress("close");
    						}
	    				});
	    			},
	    			buttons: opts.multiSelect ? buttons : null
	    		});
	    	},
	    	getValue : function() {
	    		return $('#'+opts.hiddenName).val();
	    	},
	    	getText : function() {
	    		return $(target).searchbox('getValue');
	    	}
	    }));
	    
	    //清空searchbox的文本值和隐藏域值
	    $(target).searchbox("textbox").bind("change", function(value){
	    	var text = $(this).val();
	    	if(text == null || text == ""){
	    		$('#'+opts.hiddenName).val("");
	    	}
	    });
	    
	    //添加清空按钮
	    $(target).searchbox({
	    	onChange:function(nv, ov){
	    		var icon = $(this).textbox('getIcon', 0);
				if (nv) {
					icon.css('visibility', 'visible');
				} else {
					icon.css('visibility', 'hidden');
					$('#'+opts.hiddenName).val("");
					if(opts.clearFunc){
						(opts.clearFunc)();
					}
				}
	    	}
	    });
	    
	    $(target).searchbox("addClearBtn", {iconCls:"icon-clear"});
	}  
	
	$.fn.treeDialog.parseOptions = function(target) {
		 return $.extend({}, $.fn.searchbox.parseOptions(target), {});
	};
	$.fn.treeDialog.methods = {
	    options : function (jq) {  
	        var opts = $.data(jq[0], "treeDialog").options;  
	        return opts;  
	    },
	    onRender : function(jq) {
	    	var dgDialog = $(jq);
	    	var opts = $.data(jq[0], "treeDialog").options;
	    	var _hiddenName = opts.hiddenName;
	    	if (_hiddenName) {
	    		dgDialog.before("<input type='hidden' id='" + _hiddenName + "' name='" + _hiddenName + "' />");
	    	}
	    },
	    onChange : function(jq) {
	    	
	    },
	    setText : function(jq, value) {
	    	$(jq).searchbox('setValue', value);
	    },
	    setValue : function(jq, value) {
	    	var opts = $.data(jq[0], "treeDialog").options;
	    	var _hiddenName = opts.hiddenName;
	    	$('#'+_hiddenName).val(value);
	    	if(opts.assignFunc){
	    		(opts.assignFunc)(value);
	    	}
	    },
    	getValue : function(jq) {
    		var dgDialog = $(jq);
	    	var opts = $.data(jq[0], "treeDialog").options;
    		return $('#'+opts.hiddenName).val();
    	},
    	getText : function(jq) {
    		return $(jq).searchbox('getValue');
    	},
    	clear : function(jq) {
    		$(jq).searchbox('setValue', null);
    		var opts = $.data(jq[0], "treeDialog").options;
	    	var _hiddenName = opts.hiddenName;
	    	$('#'+_hiddenName).val(null);
    	}
    	
	};
	$.fn.treeDialog.defaults = $.extend({}, $.fn.searchbox.defaults, {
		title : '',
		dialogWidth : 360,
		dialogHeight : 500,
		name : null,
		hiddenName : null, 
		queryParams : {},
//		iconCls : [],
		dblClickRow : true,
		singleSelect : false,
		valueModel : "code", //取值模式：id、code
		textModel : "code_name", //显示文本模式：name(名称)、shortname(简称)、wholename(全称)、code_name(编码-名称)、code_shortname(编码-简称)、code_wholename(编码-全称)
		multiSelect: false, //是否支持多选，显示树节点前的复选框
		checkLevs: [], //是否支持级次选择
		dialogID : 'dlg90',
		treeID : 'tree_0007',
		url : contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementVals.do', //queryDicTreeElementValsByCFilter、queryDicTreeElementVals
		elementcode : null, //数据项编码
		clearFunc:function(){
			//TODO 点击清空按钮触发清除事件
		},
		assignFunc:function(value){
			//TODO 双击选择回填时触发赋值事件
		},
		beforeSearchFunc:function() {
			
		},
		filters:{
			code: "编码",
			name: "名称"
		}
	}
);})(jQuery);