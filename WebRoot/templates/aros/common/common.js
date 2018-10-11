$(function(){
	/**
	 * 页面模块处理方法
	 */
	$.each($(".title-ctrl"), function(i,item){
	    if($(item).data("attch") && $(item).data("attch") === "fold") {
	        $(".eui-icon", $(this)).css("background-position", "0px -950px")  ;
            if($(".ext-ctrl", $(this))){$(".ext-ctrl", $(this)).hide() ;}
            $(this).next(".content-ctrl").hide() ;
	    } else {
	        $(".eui-icon", $(this)).css("background-position", "0px -966px")  ;
            if($(".ext-ctrl", $(this))){$(".ext-ctrl", $(this)).show() ;}
            $(this).next(".content-ctrl").show() ;
	    };
	    if (!($(item).data("invalid") && $(item).data("invalid") === "invalid")) {
		    $(item).bind("click", function() {
		        var $c = $(this).next(".content-ctrl") ;
		        if ($c.is(":hidden")) {
		            $(".eui-icon", $(this)).css("background-position", "0px -966px")  ;
	                if($(".ext-ctrl", $(this))){$(".ext-ctrl", $(this)).show() ;}
	                $c.show() ;
	                //
	                _offsetInnerTypePrompt($(".content-ctrl"), "input") ;
	                _offsetInnerTypePrompt($(".operateDiv"), "button") ;
		        } else {
		            $(".eui-icon", $(this)).css("background-position", "0px -950px")  ;
	                if($(".ext-ctrl", $(this))){$(".ext-ctrl", $(this)).hide() ;}
	                $c.hide() ;
	                hideDivPrompt($c) ;
	                _offsetInnerTypePrompt($(".content-ctrl"), "input") ;
	                _offsetInnerTypePrompt($(".operateDiv"), "button") ;
		        }
		    }) ;
	    }
	});
	
	/**
	 * example>
	 * 有参数：
	 * 	1控制一个指定ID的grid function({gridName:"ExampleListGrid"}) 
	 *  2控制多个指定样式的grid function({gridTop:".datagrid-top"}) 
	 * 无参数：
	 *  默认控制页面中所有grid 采用系统样式 参考有参数2
	 */
	$.gridTopHandler = function(options) {
		if (options) {
			var gridName = options.gridName || "" ;
			var gridTop = options.gridTop || ".datagrid-top" ;
			if(!$.isEmpty(gridName)) {
				$("#" + gridName + "-top").css("display", "none") ;
				$("#" + gridName + "tfoot-div").css("position", "relative") ;
				return false ;
			}
			$.each($(gridTop), function(i, item) {
				$(item).css("display", "none") ;
			}) ;
		} else {
			$.each($(".datagrid-top"), function(i, item) {
				$(item).css("display", "none") ;
			}) ;
		}
		$.each($(".datagridtfoot-div"), function(i, item) {
			$(item).css("position", "relative") ;
		}) ;
	};
	
	/**
	 * example>
     * 无参数：
     *  默认处理页面中的tabs元素，默认选中第一个，默认采用单击事件
     *  *当页面含有多个tabs时 ，禁采用无参数调用此方法
     * 有参数：
     *  传入类数组对象，
     *  mouseEvent:true表示开启 鼠标悬浮事件处理方法，此时鼠标点击事件失效
     *  active:1 表示默认选中第几个，下标从0开始
     *  tabsId:'tabsId' 整个tabsDivId 当页面只有一个tabs时可以不传递内容 默认为null 
	 */
    $.extTabsHandler = function(ps) {
	    var options = {
	       tabsId : null,
	       active : 0 ,
	       mouseEvent : false,
	       mouseChange : true,
	       clickEvent : true  
	    } ;
	    $.extend(options, ps) ;
	    var that = this ;
	    var $header = $(".tabs-ctrl .tabs-div .li-ctrl");
	    var $body = $(".tabs-ctrl .fct-ctrl");
	    if (options.tabsId) {
	        $header = $("#" + options.tabsId + ">.tabs-div").find(".li-ctrl");
	        $body = $("#" + options.tabsId + ">.fct-ctrl") ;
	    } 
	    var _hideAll = function() {
            $body.each(function(i, field) {
                if (!$(this).is(":hidden")) {
                    $(this).hide() ;
                }
            }) ;
        } ;
        var _hideExtAll = function(tg) {
            $body.each(function(i, field) {
                if ($(this).attr("id") !== tg){
                    if (!$(this).is(":hidden")) {
                        $(this).hide() ;
                    }
                }
            }) ;
        } ;
        var _changeTagAll = function() {
            $header.each(function(i,item){
                $(item).css({"background":"#8BD2EF","color":"#FFF"}) ;
            });
        } ;
	    $(".tabs-ctrl .fct-ctrl").each(function(i, field) {
	        if (i !== options.active && !$(this).is(":hidden")) {
	            $(this).hide() ;
	        }
	    }) ;
	    $.each($header, function(i,item){
	        if (i === options.active) {
	            $(item).css({"background":"none","color":"#555"}) ;
	        }
	        options.mouseChange && options.mouseChange == 1 ? $(item).hover(function() {
	            $(this).css("font-weight", "bold") ;
	        }, function() {
	            $(this).css("font-weight", "normal") ;
	        }) : "" ;
	        if (options.mouseEvent && options.mouseEvent == 1) {
	           $(item).bind("mouseover", "span", function() {
    	            var tg = $(item).find("span").data("anchor") ;
    	             _changeTagAll.call(that) ;
                     $(item).css({"background":"none","color":"#555"}) ;
                    _hideAll.call(that) ;
                    $("#" + tg).show() ;
	           });
	        } else {
	             $(item).unbind("mouseover", "span") ;
	        }
	        if ( !options.mouseEvent && options.clickEvent && options.clickEvent == 1 ) {
    	           $(item).bind("click", "span", function() {
        	            var tg = $(item).find("span").data("anchor") ;
        	            _changeTagAll.call(that) ;
        	            $(item).css({"background":"none","color":"#555"}) ;
        	            _hideAll.call(that) ;
        	            $("#" + tg).show() ;
    	           });
	        } else {
	           $(item).unbind("click", "span") ;
	        } 
	    }) ;
	};
	
/** 关闭指定区域内容所有提示信息*/
function hideDivPrompt(tag){
    var tagobj = $(tag);
    tagobj.find("input").each(function(i){
        hidePrompt($(this)) ;
    });
    tagobj.find("a").each(function(i){
        hidePrompt($(this)) ;
    });
    tagobj.find("button").each(function(i){
        hidePrompt($(this)) ;
    });
    tagobj.find("textarea").each(function(i){
        hidePrompt($(this)) ;
    });
    tagobj.find("select").each(function(i){
         hidePrompt($(this)) ;
    });
}

/**点击收缩时触发更改提示框的位置*/
function _offsetInnerTypePrompt($content, cstype, level) {
    var _base = [228, 177, 40] ;
    if (level && level == "one") {
        _base = [276, 241, 40] ; 
    }
    var _left = (typeof (cstype) == "string" && cstype == "button") ? 12 : _base[0] ;
    !$.isEmpty(cstype) && $content.find(cstype).each(function(i, item){
        if (cstype != "button") {
            var _td = $(item).parent("td").next("td") ;
            !_td.is("td") ? _left = _base[1] : _left += 0 ;
        }
        if(!$.isEmpty($(item).attr("id"))) {
            var _id = $(item).attr("id") ;
            var of = $(item).offset() ;
            var _error = $("."+_id+"formError") ;
            if (!$.isEmpty(_error.data("from"))) {
                _error.offset({top:of.top - _base[2],left:of.left + _left}) ;
            }
        }
    }) ; 
}

/**处理文本域中的文字部分  */
$.tuneTextHandler = function(maxlength, length, allselector, define) {
    var _length = length || 50 ;
    var len = (maxlength%_length==0 ? maxlength/_length : maxlength/_length+1) || 4 ;
    var _allselector = allselector || ".text-area" ;
    $.each($(_allselector), function() {
        var text = $(this).find("span").html();
        var util = new SplitiNum(String(text), _length);
        var content = [] ;
        if (!define) {
             content = util.getContent() ;
        } else {
            content = util.byDefine(define) ;
        }
        for (var i = 0; i < content.length; i++) {
            var $div = $("<div class='dv-ta-underline' style=''></div>");
            $div.text(content[i]);
            $(this).append($div);
        }
        if (!define) {
            var _len = (len - (content.length || 0) < 0) ? 0 : len - (content.length || 0);
            for (var i = 0; i < _len; i++) {
                var $div = $("<div class='dv-ta-underline'></div>");
                $(this).append($div);
            }
        }
    });
} ;

/**处理文本域中的文字部分 自定义分割内容define */
$.tuneTextDefHandler = function(thtml, length, allselector, define) {
    var _length = length || 60 ;
    var _allselector = allselector || "#hint-text" ;
    $.each($(_allselector), function() {
        var text = thtml || $(this).find("span").html() ;
        var util = new SplitiNum(String(text), _length);
        var content = util.byDefine(define) ;
        for (var i = 0; i < content.length; i++) {
            var $div = $("<div class='dv-ta-underline' style=''></div>");
            $div.text(content[i]);
            $(this).append($div);
        }
    });
};

});

/**
 * @param {String} info 待分隔字符串
 * @param {Number} len 分隔的限度数
 * 调用方法：
 * var ss = new SplitiNum("d", 4) ;
 */
function SplitiNum(info, len) {
    var _info = String(info) ;
    var _len = len ;
    var content = []; //存放分隔字符串容器
    var __init__ = function(_info, _len) {
        //匹配中文 及 中文符号的正则
        var reg = /[\u4E00-\u9FA5]|[，。？：；‘’！“”—……、]|(－{2})|(（）)|(【】)|({})|(《》)/i;
        var counter = 0;
        //计数器
        var v_counter = '';
        //字符统计器
        for (var i = 0; i < _info.length; i++) {
            var _char = _info.charAt(i);
            if (reg.test(_char)) {
                counter += 2;
            } else {
                counter += 1;
            };
            v_counter = v_counter.concat(_char);
            if (counter >= (_len << 1)) {
                content.push(v_counter);
                counter = 0;
                v_counter = '';
            };
        };
        if (v_counter) {
            content.push(v_counter);
        };
    };
    __init__.call(this, _info, _len) ;
    this.getContent = function() {
        return content ;
    };
    this.prepend = function(begin){
        for (var i in content){
            content[i] = begin + content[i] ;
        };
        return content ;
    };
    this.byDefine = function(define) {
        content = [] ;
        var _arrc = info.toLowerCase().split(define.toString().toLowerCase()) ;//兼容ie
        for (var i in _arrc) {
            if (typeof _arrc[i] === 'string') {//兼容ie
             __init__.call(this, _arrc[i],  _len) ;   
            }
        }
        return content ;
    };
};

/**
 * return 0>两者相等 
 *             1>y大于x  
 *           -1>与1结果相反
 * @param {Object} x    任意数值
 * @param {Object} y   任意数值
 * @param {Object} fix 两个数值的比较精度
 * 使用与验证填写内容数据
 * exp:
 *  if (cmpFixValue((a1+a2), totalA, 2) === -1) {
 *     error ...
 * }
 */
function cmpFixValue(x, y, fix) {
    fix = fix || 3 ;
    if (arguments.lenght > 3 || isNaN(parseFloat(x)) || isNaN(parseFloat(y)) || isNaN(parseInt(fix)))
        throw "arguments invalid";
    var _x = parseFloat(x).toFixed(fix);
    var _y = parseFloat(y).toFixed(fix);
    if (Math.abs(_x - _y) < 0.0001)
        return 0;
    if (!(_y > Math.max(_x, _y) || _y < Math.max(_x, _y)))
        return 1;
    else
        return -1;
}

/**
 * 操作表单 显示按钮提示内容
 */
function showBtnErr($btn) { 
	if($btn) {		
		var handler = $btn.attr("id") ;
		$(".formError." + handler + "formError").show() ;
	}
}

/**
 * 操作表单 显示按钮（多个）提示内容
 */
function showBtnsErr(args) {
    for (var i = 0; i < arguments.length; i++){
           showBtnErr(arguments[i]) ;
    }
}

/**对一些字典格式转换 之后得到json需要使用$.parseJSON() */
function createValues(values) {
	var str = new String(values) ;
	var ks = str.substring(1, str.length-1).split(",") ;
	var result = "{" ;
	for (var i = 0; i < ks.length; i++) {
		var _ks = ks[i].split("=") ;
		result += "\"" + strim(_ks[0]) + "\":\"" + strim(_ks[1]) + "\"," ;
	}
	result = result.substring(0, result.length-1) ;
	result += "}" ;
	return result  ;
}

/**全面去除空格 */
function strim(str) {
	var reg = /\s/i ;
	var _info = str ;
	for (var i=0;i<str.length;i++) {
		if (reg.test(str.charAt(i))) {
			_info = _info.replace(" ", "") ;
		}
	}
	return _info ;
}

/**
 * append_form_data($('#form'), $...$form)
 * 将大于1个的参数from提交数据转移（使用div包裹）到第1个form表单内部
 * 并且在处理之前删除之前添加的数据，用于处理多次提交
 * @param $form
 */
function append_form_data($form) {
	var args = arguments ;
	if ($form.is("form")) {
		for (var i=1; i < args.length; i++) {
			var _arrays = $(args[i]).serializeArray() ;
			$.fitRemove($("#hidden_" + $(args[i]).attr("id"), $form)) ;
			var $_div = $("<div id='hidden_" + $(args[i]).attr("id") + "'></div>") ;
			$.each(_arrays, function(i, kv) {
				var _input = $("<input type='hidden' name='"+ kv.name +"' value='"+ kv.value +"' />") ;
				$_div.append(_input) ;
			}) ;
			$form.append($_div) ;
		}
	}
}

//给Array类型添加IndexOf 方法. 以兼容IE
if(!Array.indexOf)
{
    Array.prototype.indexOf = function(obj)
    {               
        for(var i=0; i<this.length; i++)
        {
            if(this[i]==obj)
            {
                return i;
            }
        }
        return -1;
    };
}

function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str){ //删除左边的空格
	return str.replace(/(^\s*)/g,"");
}
function rtrim(str){ //删除右边的空格
	return str.replace(/(\s*$)/g,"");
}

//给Array类型添加remove方法
/*
Array.prototype.remove = function(val) {

       var index = this.indexOf(val);

       if (index > -1) {

           this.splice(index, 1);

       }

   };
   */ 
//生成唯一的随机数字符串   
function genKey(){
	
	//第一步先获得当前毫秒的16进制串，接着产生1到2之间的随机小数，
	//并把小数点向右移动16-当前毫秒串length的长度（即把该长度做为16的指数，再和随机数相乘），
	//同时把该数取整再转成16进制字符串后保留其小数位。相加这两个16进制字串即获得了16位长度的uniqid。
	var uid = new Date().getTime().toString(16);
    uid += Math.floor((1 + Math.random()) * Math.pow(16, (16 - uid.length)))
        .toString(16).substr(1);    
    return  uid;
}   

$.urlParam = function(name) {
	var results = new RegExp('[?&]' + name + '=([^&#]*)').exec(window.location.href);
	if (!results) {
		return 0;
	}
	return results[1] || 0;
}; 

UrlParm = function() { // url参数
	  var data, index;
	  (function init() {
	    data = [];
	    index = {};
	    var u = window.location.search.substr(1);
	    if (u != '') {
	      var parms = decodeURIComponent(u).split('&');
	      for (var i = 0, len = parms.length; i < len; i++) {
	        if (parms[i] != '') {
	          var p = parms[i].split("=");
	          if (p.length == 1 || (p.length == 2 && p[1] == '')) {
	            data.push(['']);
	            index[p[0]] = data.length - 1;
	          } else if (typeof(p[0]) == 'undefined' || p[0] == '') {
	            data[0] = [p[1]];
	          } else if (typeof(index[p[0]]) == 'undefined') { // c=aaa
	            data.push([p[1]]);
	            index[p[0]] = data.length - 1;
	          } else {// c=aaa
	            data[index[p[0]]].push(p[1]);
	          }
	        }
	      }
	    }
	  })();
	  return {
	    // 获得参数,类似request.getParameter()
	    parm : function(o) { // o: 参数名或者参数次序
	      try {
	        return (typeof(o) == 'number' ? data[o][0] : data[index[o]][0]);
	      } catch (e) {
	      }
	    },
	    //获得参数组, 类似request.getParameterValues()
	    parmValues : function(o) { //  o: 参数名或者参数次序
	      try {
	        return (typeof(o) == 'number' ? data[o] : data[index[o]]);
	      } catch (e) {}
	    },
	    //是否含有parmName参数
	    hasParm : function(parmName) {
	      return typeof(parmName) == 'string' ? typeof(index[parmName]) != 'undefined' : false;
	    },
	    // 获得参数Map ,类似request.getParameterMap()
	    parmMap : function() {
	      var map = {};
	      try {
	        for (var p in index) {  map[p] = data[index[p]];  }
	      } catch (e) {}
	      return map;
	    }
	  }
	}();
	
// 将data中的数据set到元素
//如: data={payReq:{prjname:"姓名"}}
//调用如下:setFormValueUseJson(data.payReq,"payReq");
//会自动将name为 payReq.payReq 的元素设定值为 姓名
function setFormValueUseJson(data,prefix){
	for(var item in data){
		var tmp = $("[name='"+ prefix+ "\\."+ item +"']");
		if (tmp.length == 0){
			tmp = $("[id='"+ prefix+ "\\."+ item +"']");
		}
		var str = data[item];
		str = str ==null ? "":str;
		               
		if(tmp.is("div")){
			tmp.val(str);
		}
		else if(tmp.is("span")){
			tmp.text(str);
		}
		else if(tmp.is("select")){
			tmp.attr("value",str);
		}
		else {
			tmp.val(data[item]);
		}
		//data[item]= v;
	}
	return data;
}
	
// 将元素的值set到data
//如: data={payReq:{prjname:"姓名"}}
//调用如下:getFormValueUseJson(data.payReq,"payReq");
//假设name为 payReq.payReq 元素的值为 张三
//调用后 data.payReq.prjname 为张三
function getFormValueUseJson(data,prefix){
	for(var item in data){
		var tmp = $("[name='"+ prefix+ "\\."+ item +"']");
		if (tmp.length == 0){
			tmp = $("[id='"+ prefix+ "\\."+ item +"']");
		}
		if (tmp.length != 0){
			if(tmp.is("div")){
				data[item] =  tmp.val();
			}
			else if(tmp.is("span")){
				data[item] = tmp.text();
			}
			
			else {
				data[item] =  tmp.val() ;
			}
		}
	}
	return data;
}
	
//将tag标签下所有input a button textarea 都设定为disable
//使用事例: disableTag("#div1")
function disableTag(tag){
	var tagobj = $(tag);
	tagobj.find("input").each(function(i){
		$(this).attr("disabled","disabled");
     });
	/**
	tagobj.find("a").each(function(i){
		$(this).hide();
     }); */
	tagobj.find("button").each(function(i){
		$(this).addClass("dis-ui-primary-bg") ; 
		if ($(this).hasClass("ui-button-primary")) {
			$(this).removeClass("ui-button-primary") ;
			$(this).addClass("ui-button-primary-") ;
			$(this).css("background-color", "#00F") ;
		}
		$(this).attr("disabled","disabled");
		$(this).addClass("fakeDisabled");
     });
	tagobj.find("textarea").each(function(i){
		$(this).attr("disabled","disabled");
     });
	tagobj.find("select").each(function(i){
		$(this).attr("disabled","disabled");
     });
}
	
//将tag标签下所有input a button textarea 都设定为disable, 除了id = tagid的元素之外
//使用事例: disableTag2("#div1","button")
function disableTag2(tag, tagid){
	var tagobj = $(tag);
	tagobj.find("input[id != "+tagid+"]").each(function(i){
		$(this).attr("disabled","disabled");
     });
	/**
	tagobj.find("a[id != "+tagid+"]").each(function(i){
		$(this).hide();
     }); */
	tagobj.find("button[id != "+tagid+"]").each(function(i){
		$(this).addClass("dis-ui-primary-bg") ; 
		if ($(this).hasClass("ui-button-primary")) {
			$(this).removeClass("ui-button-primary") ;
			$(this).addClass("ui-button-primary-") ;
		}
		$(this).attr("disabled","disabled");
		$(this).addClass("fakeDisabled");
     });
	tagobj.find("textarea[id != "+tagid+"]").each(function(i){
		$(this).attr("disabled","disabled");
     });
	tagobj.find("select[id != "+tagid+"]").each(function(i){
		$(this).attr("disabled","disabled");
     });
}

//将tag标签下所有input a button textarea 都设定为disable, 除了id 在 tagids 之内
//使用事例: disableTag3("#div1",["#button","button2"])
function disableTag3(tag, tagids){
	var tagobj = $(tag);
	var str = "";
	for(var index in tagids){
		if (typeof tagids[index] == "string"){
	        var obj = tagids[index];
	        str = str + "[id !=" + obj +"]";
	   }
	}
	tagobj.find("input" + str).each(function(i){
		$(this).attr("disabled","disabled");
   });
	/**
	tagobj.find("a" + str).each(function(i){
		$(this).hide();
   }); */
	tagobj.find("button" + str).each(function(i){
		//add by liuyayuan
		$(this).addClass("dis-ui-primary-bg") ; 
		if ($(this).hasClass("ui-button-primary")) {
			$(this).removeClass("ui-button-primary") ;
			$(this).addClass("ui-button-primary-") ;
		}
		$(this).attr("disabled","disabled");
		$(this).addClass("fakeDisabled");
   });
	tagobj.find("textarea" + str).each(function(i){
		$(this).attr("disabled","disabled");
   });
	tagobj.find("select" + str).each(function(i){
		$(this).attr("disabled","disabled");
   });
}

//置按钮不可用 
//使用事例: disablebtn($("btgen"));
function disablebtn(obj){
	obj.addClass("dis-ui-primary-bg") ; 
	if (obj.hasClass("ui-button-primary")) {
		obj.removeClass("ui-button-primary") ;
		obj.addClass("ui-button-primary-") ;
	}
	obj.attr("disabled","disabled");
	obj.addClass("fakeDisabled");
}

//置按钮可用
//使用事例:enablebtn($("#btprint"));
function enablebtn(obj){
	obj.removeClass("dis-ui-primary-bg") ; 
	if (obj.hasClass("ui-button-primary-")) {
		obj.removeClass("ui-button-primary-") ;
		obj.addClass("ui-button-primary") ;
	}
	obj.removeAttr("disabled");
	obj.removeClass("fakeDisabled");
	obj.removeClass("ui-button-disabled");
	obj.removeClass("ui-state-disabled");
}
	
//将tag标签下所有input a button textarea 的disable属性去除
function enableTag(tag){
	var tagobj = $(tag);
	tagobj.find("input").each(function(i){
		$(this).removeAttr("disabled");
    });
	tagobj.find("a").each(function(i){
		$(this).show();
    });
	tagobj.find("button").each(function(i){
		$(this).removeClass("dis-ui-primary-bg") ; 
		if ($(this).hasClass("ui-button-primary-")) {
			$(this).removeClass("ui-button-primary-") ;
			$(this).addClass("ui-button-primary") ;
			$(this).css("background", "") ;
		}
		$(this).removeAttr("disabled");
		$(this).removeClass("fakeDisabled");
    });
	tagobj.find("textarea").each(function(i){
		$(this).removeAttr("disabled");
    });
	tagobj.find("select").each(function(i){
		$(this).removeAttr("disabled");
    });
}
	
//将json格式(对象)转换成url形式
//如: req:{'name':'张三','address':'地址'}
//调用是getUrlParam(req,'req')
//得到的字符串: req.name=张三&req.address=地址&     此处的中文实际会被自动转码
//注意值会被url转码	
function getUrlParam( obj,prefix){
	var str = "";
	for(var item in obj){
		if (typeof obj[item]  != "undefined" && obj[item] != null){
			str = str + prefix +"." +item +"=" + encodeURI(obj[item]) + "&" ;
		}
	}
	return str;
}
	
function getUrlParamComponent( obj,prefix){
	var str = "";
	for(var item in obj){
		if (typeof obj[item]  != "undefined" && obj[item] != null){
			str = str + prefix +"." +item +"=" + encodeURIComponent(obj[item]) + "&" ;
		}
	}
	return str;
}
	
//str 组织成 url的参数
//如: getUrlParamStr("name","张三")
//得到的字符串: name=张三&     此处的中文实际会被自动转码
//注意值会被url转码		
function getUrlParamStr( obj, name){
	var str = name + "=" + encodeURI(obj) + "&" ;
	return str;
}

function getUrlParamStrComponent( obj, name){
	var str = name + "=" + encodeURIComponent(obj) + "&" ;
	return str;
}

//将json格式(数组)转换成url形式
//如: reqlist:[{'name':'张三','address':'地址1'},{'name':'李四','address':'地址2'}]
//调用是getUrlParam(reqlist,'reqlist')
//得到的字符串: reqlist[0].name=张三&reqlist[0].address=地址&reqlist[1].name=李四&reqlist[1].address=地址2&     此处的中文实际会被自动转码
//注意值会被url转码	
function getUrlParamArray(obj,prefix){
	var j = 0;
	var str = "";
	for(var i = 0; i < obj.length; i ++){
		if (typeof obj[i] == "object"){
        	   var data = obj[i];
        	   str = str + getUrlParam(data,prefix+"[" + j+"]");
        	   j ++;
 	   }
	}
	return str;
}
	
function initShrink(doc){
	$("h3[data-shrink]").each(function(){
		sender = $(this);
		sender.bind("hover",function(){
			
		});
		sender.bind("click",function(){
			
		});
	});
}
	
//其中tableobj 为table 的jquery对象
//dataobj 为 流程日志表的json数组对象
function insertFlowLogs(tableobj, dataobj){
	var tbody = tableobj.find("tbody");
	for(var i = 0; i < dataobj.length; i ++){
		var item = dataobj[i];
		var tr = $(document.createElement("tr"));
	    if(i % 2 == 0){
	           tr.attr("class","odd");
	    }
	    else{
	        tr.attr("class","even");
	    }
	    
	    var td0 = createtd(item.flowid)
	    td0.attr("style","display:none");
	    tr.append(td0);
	    tr.append(createtd(item.nodename));
	    tr.append(createtd(item.opermantypename));
	    tr.append(createtd(item.operman));
	    tr.append(createtd(item.operrolename));
	    tr.append(createtd(item.actionname));
	    tr.append(createtd(item.operdata));
	    tr.append(createtd(item.opertime));
	    tbody.append(tr);
	}
}
	
function createtd(value){
	var td = $(document.createElement("td"));
    var span = $(document.createElement("span"));
    span.text(value);
    td.append(span);
    return td;
}

//验证表单
function validationForm(obj){
	var tagobj = $(obj);
	/**add */
	$(".title-ctrl", $(document)).each(function(i,item){
        var $cbody = $(item).next(".content-ctrl") ;
        var len = $cbody.find("[data-validation-engine*=validate]").length ;
        if ($cbody.is(":hidden") && len >= 1) {
            $cbody.show() ;
        }
    });
    return tagobj.validationEngine("validate");
}

function printPreview_public(orient, pageName, url, certhtmlconf){
	
	printPreview_public2(orient, pageName, url
		, certhtmlconf.topmargin, certhtmlconf.leftmargin, certhtmlconf.width, certhtmlconf.height
		, certhtmlconf.footertopmargin, certhtmlconf.footerleftmargin, certhtmlconf.footerwidth, certhtmlconf.footerheight
		, certhtmlconf.footercontent);
}

/**
 * 打印函数
 * orient:打印方向及纸张类型，数字型
 * pageName: 所选纸张类型名，字符型
 * url: 打印的url地址
 * topmargin: 页面上边距
 * leftmargin: 页面左边距
 * width: 页面宽度
 * height: 页面高度
 * footertopmargin:页脚上边距
 * footerleftmargin:页脚左边距
 * footerwidth:页脚宽度
 * footerheight:页脚高度
 * footercontent:页脚打印内容
 */
function printPreview_public2(orient, pageName, url
			, topmargin, leftmargin, width, height
			, footertopmargin, footerleftmargin, footerwidth,footerheight
			, footercontent){

	LODOP=getLodop(document.getElementById('LODOP'), document.getElementById('LODOP_EM'));     
	LODOP.SET_PRINT_PAGESIZE(orient, 0, 0, pageName);
	LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
	LODOP.ADD_PRINT_HTM(topmargin,leftmargin,width,height,"URL:"+url);
	
	if (footercontent != null && footercontent != undefined && footercontent != ''){
		LODOP.ADD_PRINT_TEXT(footertopmargin, footerleftmargin, footerwidth, footerheight, footercontent);
		LODOP.SET_PRINT_STYLEA(0, "ItemType", 2);
		LODOP.SET_PRINT_STYLEA( 0,"HOrient", 1);
	}
	LODOP.PREVIEW();	
}

function printPreview_public3(orient, pageName, innerhtml
            , topmargin, leftmargin, width, height
            , footertopmargin, footerleftmargin, footerwidth,footerheight
            , footercontent){

    LODOP=getLodop(document.getElementById('LODOP'), document.getElementById('LODOP_EM'));     
    LODOP.SET_PRINT_PAGESIZE(orient, 0, 0, pageName);
    LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
    LODOP.ADD_PRINT_HTM(topmargin,leftmargin,width,height,innerhtml);
    
    if (footercontent != null && footercontent != undefined && footercontent != ''){
        LODOP.ADD_PRINT_TEXT(footertopmargin, footerleftmargin, footerwidth, footerheight, footercontent);
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 2);
        LODOP.SET_PRINT_STYLEA( 0,"HOrient", 1);
    }
    LODOP.PREVIEW();    
}

/**
 * 调整遮罩层高度
 * @param bg 遮罩div对象
 * @author wangzhengke
 */
function adjustAjaxbg(bg){
	var _body = $("body"); //body对象
	var _bg ; //遮罩div对象
	if(bg && bg.length > 0) {
		_body = $(bg.eq(0)).closest("body");
	}
	if(_body && _body.length > 0) {		
		_bg = $(".pb-background", $(_body));
	}
	if(_bg && _bg.length > 0) {
		$(_bg).eq(0).css("height",$(_body).height());
	}
}