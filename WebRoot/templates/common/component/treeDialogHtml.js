(function ($) {
	$.fn.treeDialogHtml = function (options, param) {
		if (typeof (options) == "string") {
			var method = $.fn.treeDialogHtml.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.tree(options, param);
			}
		}
		
		options = options || {};
		return this.each(function () {
			var state = $.data(this, "treeDialogHtml");  
	        if (state) {  
	            $.extend(state.options, options);  
	        } else {  
	            $.data(this, "treeDialogHtml", { options: $.extend({}, $.fn.treeDialogHtml.defaults, $.fn.treeDialogHtml.parseOptions(this), options)});  
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
		
	    var opts = $.data(target, 'treeDialogHtml').options;
	    $(target).treeDialogHtml("onRender");
	    $(target).treeDialogHtml("onChange");
	    $(target).searchbox($.extend({}, opts, {
	    	searcher : function(value, name) {

				showx = event.screenX - event.offsetX - 4 - 210;
				showy = event.screenY - event.offsetY + 18;
				newWINwidth = 210 + 4 + 18;
				var path = "", arrScript = document.getElementsByTagName("script");
				for(var i=0; i<arrScript.length; i++) {
					var src = arrScript[i].src;
					var index = src.indexOf('uxdateHtml.js');		//假设uxdateHtml.js文件名没变
					if(index >= 0) {
						path = src.substring(0, index);
						if(path.charAt(0) == '/') {
							src = location.href;
							index = src.indexOf('//');
							if(index != -1) {
								index = src.indexOf('/', index+2);
								if(index != -1) src = src.substring(0, index);
								path = src + path;
							}
						}
						break;
					}
				 }
				var url= path + "/treeDialog/treeDialog.htm";
				if ($.browser.safari) {
					var	calendarWindow=window.open(url,'CalendarWindow','left='+showx+',top='+showy+',height=520px,width=450px,toolbar=0,menubar=0,scrollbars=0,location=0,status=0');
					calendarWindow.ctrlobj = $(target);
					calendarWindow.opts = opts;
				} else {
					var returnVal = window.showModalDialog(url, opts, "dialogWidth:450px;dialogHeight:520px;dialogLeft:" + showx
						+ "px;dialogTop:" + showy + "px;status:no;scrollbars:no;resizable:no;");
					if (returnVal) {
						$(target).treeDialogHtml("setText", returnVal.text);
						$(target).treeDialogHtml("setValue", returnVal.code);
					}
				}
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
	};
	
	$.fn.treeDialogHtml.parseOptions = function(target) {
		 return $.extend({}, $.fn.searchbox.parseOptions(target), {});
	};
	$.fn.treeDialogHtml.methods = {
	    options : function (jq) {  
	        var opts = $.data(jq[0], "treeDialogHtml").options;  
	        return opts;  
	    },
	    onRender : function(jq) {
	    	var dgDialog = $(jq);
	    	var opts = $.data(jq[0], "treeDialogHtml").options;
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
	    	var opts = $.data(jq[0], "treeDialogHtml").options;
	    	var _hiddenName = opts.hiddenName;
	    	$('#'+_hiddenName).val(value);
	    	if(opts.assignFunc){
	    		(opts.assignFunc)(value);
	    	}
	    }
	}
	
	$.fn.treeDialogHtml.defaults = $.extend({}, $.fn.searchbox.defaults, {
		title : '',
		dialogWidth : 360,
		dialogHeight : 500,
		name : null,
		hiddenName : null, 
		dblClickRow : true,
		singleSelect : false,
		multiSelect: false, //是否支持多选，显示树节点前的复选框
		checkLevs: [], //是否支持级次选择
		dialogID : 'dlg90',
		treeID : 'tree_0007',
		url : contextpath + '/sys/dept/sysDeptController/queryDeptTree.do',
		clearFunc:function(){
			//TODO 点击清空按钮触发清除事件
		},
		assignFunc:function(value){
			//TODO 双击选择回填时触发赋值事件
		},
		filters:{
			code: "编码",
			name: "名称"
		}
	}
);})(jQuery);