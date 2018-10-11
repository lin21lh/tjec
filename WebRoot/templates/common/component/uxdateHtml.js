(function($, undefined) {
	
	function create(target) {
		var state = $.data(target, "uxdateHtml"), opts = state.options,
		t = $(target).textbox($.extend({}, opts, {
			icons : [{
				iconCls : "icon-datebox",
				handler : function(e) {
					openCalendarDlg($(target));
				}
			}]
		}));
	}
	
	function openCalendarDlg(ctrlobj) {


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
		var url= path + "/calendar/calendar.htm";
		if ($.browser.safari) {
			var	calendarWindow=window.open(url,'CalendarWindow','left='+showx+',top='+showy+',height=200,width=200,toolbar=0,menubar=0,scrollbars=0,location=0,status=0');
			calendarWindow.ctrlobj = ctrlobj;
		} else {
			var retval = window.showModalDialog(url, "", "dialogWidth:250px;dialogHeight:210px;dialogLeft:" + showx
				+ "px;dialogTop:" + showy
				+ "px;status:no;scrollbars:no;resizable:no;");
			ctrlobj.textbox("setValue", retval);
		}
	
	}
	
	$.fn.uxdateHtml = function(options, param) {
		if (typeof options == "string") {
			var method = $.fn.uxdateHtml.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.textbox(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "uxdateHtml");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "uxdateHtml", {options : $.extend({}, $.fn.uxdateHtml.defaults, $.fn.uxdateHtml.parseOptions(this), options)});
				create(this);
			}
		});
	};
	
	$.fn.uxdateHtml.parseOptions = function(target) {
		return $.extend({},
				$.fn.textbox.parseOptions(target),
				$.parser.parseOptions(target, ["text"]));
	};
		
	$.fn.uxdateHtml.methods = {
			options : function(jq) {
				var opts = jq.textbox("options"), copts = $.data(jq[0], "uxdateHtml").options;
				return $.extend(copts, {
					disabled : opts.disabled, readonly : opts.readonly
				});
			},
			openCalendarDlg : function(ctrlobj) {

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
				var url= path + "/calendar/calendar.htm";
				if ($.browser.safari) {
					var	calendarWindow=window.open(url,'CalendarWindow','left='+showx+',top='+showy+',height=300,width=228,toolbar=0,menubar=0,scrollbars=0,location=0,status=0,z-look=yes');
	            	calendarWindow.ctrlobj = ctrlobj;
		            calendarWindow.opts = opts;
				} else {
					window.showModalDialog(url, "", "dialogWidth:250px;dialogHeight:210px;dialogLeft:" + showx
						+ "px;dialogTop:" + showy
						+ "px;status:no;scrollbars:no;resizable:no;");
				}
			
			},
			setValue : function(jq, value) {
				$(jq).textbox("setValue", value);
			}
	};
	
	$.fn.uxdateHtml.defaults = $.extend({}, $.fn.textbox.defaults, {
		title : "日期选择",
		type : "date"
		
		
		
		
		
	});
})(jQuery);