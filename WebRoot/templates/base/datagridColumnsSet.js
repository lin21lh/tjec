/**
 * 获取列属性
 * @param opt
 * @param ss
 */
function getColumnopt(opt, colopts) {
	if (opt.title != undefined) //1
		colopts.push(opt.title);
	colopts.push("~");
	
	if (opt.field != undefined) //2
		colopts.push(opt.field);
	colopts.push("~");
	
	if (opt.width != undefined) //3
		colopts.push(opt.width);
	colopts.push("~");
	
	if (opt.align != undefined) //4
		colopts.push(opt.align);
	colopts.push("~");
	
	if (opt.resizable != undefined) //5
		colopts.push(opt.resizable);
	colopts.push("~");
	
	if (opt.sortable != undefined) //6
		colopts.push(opt.sortable);
	colopts.push("~");
	
	if (opt.hidden != undefined) //7
		colopts.push(opt.hidden);
	colopts.push("~");
	
	if (opt.order != undefined) //8
		colopts.push(opt.order);
	colopts.push("~");
	
	if (opt.fixed != undefined) //9
		colopts.push(opt.fixed);
	colopts.push("~");
	
	if (opt.checkbox != undefined) //10
		colopts.push(opt.checkbox);
	colopts.push("~");
	
	if (opt.formatter != undefined) //11
		colopts.push(opt.formatter.toString().replace(/[\r\n]/g,""));
}
/**
 * 保存列设置
 * @param datagrid
 * @param menuid
 */
function saveColumnsSet(datagrid, filename, menuid) {
	var frozenColumns = datagrid.datagrid("getColumnFields", true);
	var columns = datagrid.datagrid("getColumnFields");
	var opt;
	var colopts=[];
	for (var i=0; i<frozenColumns.length; i++) {
		opt = dicElementDataGrid.datagrid("getColumnOption", frozenColumns[i]);
		if (i != 0)
			colopts.push("`");
		getColumnopt(opt, colopts);
	 }
	colopts.push("￥");
	for (var i=0; i<columns.length; i++) {
		opt = dicElementDataGrid.datagrid("getColumnOption", columns[i]);
		if (i != 0)
			colopts.push("`");
		
		getColumnopt(opt, colopts);
	}
	
	$.post(contextpath + 'base/setcolumn/setColumnController/saveColumnSet.do', {
			colopts : colopts.join(""),
			filename : filename,
			menuid : menuid
		}, function(result) {
		if (result.success) {
			easyui_show(result.title);
		} else {
			if (result.title) {
				$.messager.alert('警告', result.title, 'warnning');
			} else {
				$.messager.alert('警告', '删除过程中发生异常', 'warnning');
			}
		}
	}, 'json');
	// var name = usercode + "_" + menuid;
	// var val = value + "$" + unValue;
	// addCookie(name, val, 365, "/");
	// easyui_info("保存列成功！");
}

/**
 * 获取保存列
 * @param datagrid
 * @param menuid

function getColumnSet(datagrid, usercode, menuid) {
	var name = usercode + "_" + menuid;
	var columns = getCookieValue(name);
	debugger;
	if (columns) {
		var col = columns.split("$");
		if (col[0] && col[0]!=1 && col[1] && col[1]!=1) {
			datagrid.datagrid({
				frozenColumns : [eval("[" + col[0] + "]")],
				columns : [eval("[" + col[1] + "]")]
			});
		} else if (!col[0] && col[1]) {
			datagrid.datagrid({
				columns : [eval("[" + col[1] + "]")]
			});
		} else if (col[0] && !col[1]) {
			datagrid.datagrid({
				frozenColumns : [eval("[" + col[0] + "]")]
			});
		}
		
	}
} */
/**
 * 还原列设置
 * @param datagrid
 * @param menuid
 */
function resetColumnSet(filename, menuid) {
	/**
	var name = usercode + "_" + menuid;
	deleteCookie(name);
	easyui_info("还原列成功，请重新打开此菜单！");*/
	$.post(contextpath + 'base/setcolumn/setColumnController/resetColumnSet.do', {
		filename : filename,
		menuid : menuid
	}, function(result) {
		if (result.success) {
			easyui_show(result.title);
		} else {
			easyui_info(result.title);
		}
	}, 'json');
}

/**添加设置cookie**/  
function addCookie(name, value, days, path){   
    var name = escape(name);  
    var value = escape(value);  
    var expires = new Date();
    expires.setTime(expires.getTime() + days * 3600000 * 24);  
    //path=/，表示cookie能在整个网站下使用，path=/temp，表示cookie只能在temp目录下使用  
    path = path == "" ? "" : ";path=" + path;  
    //GMT(Greenwich Mean Time)是格林尼治平时，现在的标准时间，协调世界时是UTC  
    //参数days只能是数字型
    var _expires = (typeof days) == "string" ? "" : ";expires=" + expires.toUTCString();  
    document.cookie = name + "=" + value + _expires + path;
}
/**获取cookie的值，根据cookie的键获取值**/ 
function getCookieValue(name){ 
    //用处理字符串的方式查找到key对应value  
    var name = escape(name);  
    //读cookie属性，这将返回文档的所有cookie  
    var allcookies = document.cookie;         
    //查找名为name的cookie的开始位置  
    name += "=";  
    var pos = allcookies.indexOf(name);
    //如果找到了具有该名字的cookie，那么提取并使用它的值  
    if (pos != -1){                                             //如果pos值为-1则说明搜索"version="失败  
        var start = pos + name.length;                  //cookie值开始的位置  
        var end = allcookies.indexOf(";",start);        //从cookie值开始的位置起搜索第一个";"的位置,即cookie值结尾的位置  
        if (end == -1) end = allcookies.length;        //如果end值为-1说明cookie列表里只有一个cookie  
        var value = allcookies.substring(start,end); //提取cookie的值 
        return unescape(value);                           //对它解码        
    }else{  //搜索失败，返回空字符串  
        return "";  
    }  
}

/**根据cookie的键，删除cookie，其实就是设置其失效**/  
function deleteCookie(name) {   
    var name = escape(name);  
    var date=new Date(); 
    date.setTime(date.getTime()-10000); 
    document.cookie=name+"=1$1;expire="+date.toGMTString()+";path=/"; 
} 