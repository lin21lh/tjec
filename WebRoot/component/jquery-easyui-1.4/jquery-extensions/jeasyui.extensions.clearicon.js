$.extend($.fn.textbox.methods, {
	addClearBtn : function(jq, iconOpts) {
		return jq.each(function() {
			var t = $(this);
			var tb = t.textbox("textbox");
			var addon = tb.prev("span.textbox-addon");
			if(addon.length != 0){
				addon.prepend("<a href=\"javascript:void(0)\" class=\"textbox-icon icon-clear\" icon-index=\"-1\" style=\"width: 18px; height: 20px; visibility: visible;\"></a>");
			}else{
				tb.before("<span class=\"textbox-addon textbox-addon-right\" style=\"right: 0px;\"><a href=\"javascript:void(0)\" class=\"textbox-icon icon-clear\" icon-index=\"-1\" style=\"width: 18px; height: 20px; visibility: visible;\"></a></span>");
			}
			tb.css("width", (tb.width() - 14) + "px");
		
			addon = tb.prev("span.textbox-addon");
			var clearBtn = addon.find("a:first");
			clearBtn.bind("click", function(){
				clearBtn.css('visibility', 'hidden');
				t.textbox('clear').textbox('textbox').focus();
				if(iconOpts.clearFunc){
					iconOpts.clearFunc();
				}
			});
		
			if (!t.textbox('getText')) {
				clearBtn.css('visibility', 'hidden');
			}
		
			tb.bind("keyup", function(){
				if ($(this).val()) {
					clearBtn.css('visibility', 'visible');
				} else {
					clearBtn.css('visibility', 'hidden');
					}
				});
			});
		}
});