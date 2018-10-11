/**
 * 数据字典下拉菜单
 * 
 * @param comboid
 *            下拉框ID
 * @param elementcode
 *            数据项编码
 * @param idColumn
 *            隐藏值列名 {默认 字典表ID}
 * @param textColumn
 *            显示值列名 {多列 以逗号隔开 例如 code,name 默认code-name}
 * @param jQueryHandle
 *            用于某些弹框时传入jquery对象
 * @param noClear 
 */
function comboboxFunc(comboid, elementcode, idColumn, textColumn, jQueryHandle, noClear) {
	var url = contextpath
			+ "base/dic/dicElementValSetController/queryByElementcode.do?elementcode="
			+ elementcode;
	if (idColumn)
		url += "&idColumn=" + idColumn;

	if (textColumn)
		url += "&textColumn=" + textColumn;

	// 1.Form表单页面中的combobox的选择器的获取方式与$("#selector")不同，直接将选择器作为参数传入进来
	// 2.当前列表的页面传入的是combobox的id字符串，通过$("#selector")可以直接而获取到。
	var jcomboBox = comboid;
	if (typeof comboid == "string") {
		if (jQueryHandle) {
			jcomboBox = jQueryHandle.find("#" + comboid);
		} else {
			jcomboBox = $("#" + comboid);
		}
	}

	return jcomboBox.combobox({
		editable: false,
		url : url,
		valueField : "id",
		textField : "text",
		panelHeight : '160px',
		onChange: function(nv, ov) {
			if(noClear){
				return;
			}
			var icon = jcomboBox.combobox("textbox").prev("span.textbox-addon").find("a:first");
			if (nv) {
				icon.css('visibility', 'visible');
			} else {
				icon.css('visibility', 'hidden');
			}
		}
	});
}

/**
 * 数据字典下拉菜单 (数据权限范围内)
 * @param menuid 资源（菜单）ID
 * @param comboid 下拉框ID
 * @param elementcode 数据项编码
 * @param idColumn 隐藏值列名 {默认 字典表ID}
 * @param textColumn  显示值列名 {多列 以逗号隔开 例如 code,name 默认code-name}
 * @param jQueryHandle 用于某些弹框时传入jquery对象
 * @param noClear 是否加清除按钮
 * @returns
 */
function comboboxFuncByCondFilter(menuid, comboid, elementcode, idColumn, textColumn, jQueryHandle, noClear) {
	var url = contextpath
			+ "base/dic/dicElementValSetController/queryCbByElementcodeByCFilter.do?menuid=" +
			menuid + "&elementcode="+ elementcode;
	if (idColumn)
		url += "&idColumn=" + idColumn;
	if (textColumn)
		url += "&textColumn=" + textColumn;

	// 1.Form表单页面中的combobox的选择器的获取方式与$("#selector")不同，直接将选择器作为参数传入进来
	// 2.当前列表的页面传入的是combobox的id字符串，通过$("#selector")可以直接而获取到。
	var jcomboBox = comboid;
	if (typeof comboid == "string") {
		if (jQueryHandle) {
			jcomboBox = jQueryHandle.find("#" + comboid);
		} else {
			jcomboBox = $("#" + comboid);
		}
	}
	var orgCount = 0;
	return jcomboBox.combobox({
		editable: false,
		url : url,
		valueField : "id",
		textField : "text",
		panelHeight : 'auto',
		panelMaxHeight :150,
		onChange: function(nv, ov) {
			if(noClear){
				return;
			}
			var icon = jcomboBox.combobox("textbox").prev("span.textbox-addon").find("a:first");
			if (nv) {
				icon.css('visibility', 'visible');
			} else {
				icon.css('visibility', 'hidden');
			}
		}
	});
}

/**
 * 弹出操作提示信息窗口
 * 
 * @param msg
 *            提示信息描述
 * @param fn
 *            窗口关闭时回调处理函数
 */
// function easyui_info(msg, fn) {
// parent.$.messager.alert("提示", msg, "info", fn);
// }
function easyui_info(msg, fn) {

	parent.$.messager.show({
		title : '提示',
		msg : msg,
		showType : 'fade',
		timeout : 1000,
		style : {
			right : '',
			top : document.body.height / 2 - 125,
			bottom : '',
			left : document.body.width / 2 - 50
		}
	});
	setTimeout(fn, 0);
}

/**
 * 弹出报错警告
 * 
 * @param msg
 *            消息内容
 * @param fn
 *            回调函数
 */
function easyui_warn(msg, fn) {
	parent.$.messager.alert("警告", msg, "warning", fn);
}

function easyui_solicit(msg, fn) {
	//parent.$.messager.solicit.defaults = {yesText: "保存", noText: "不保存", cancelText: "取消"};
	parent.$.messager.solicit("提示", msg, fn);
}

/**
 * 弹出能自动消失的提示窗口
 * 
 * @param msg
 */
function easyui_show(msg) {
	$.messager.show({
		title : '提示',
		msg : msg,
		timeout : 2000,
		showType : 'fade',
		style : {
			right : '',
			bottom : ''
		}
	});
}
/**
 * 弹出能自动消失的提示窗口(依赖于父页面)
 * 
 * @param msg
 */
function easyui_show_parent(msg) {
	parent.$.messager.show({
		title : '提示',
		msg : msg,
		timeout : 2000,
		showType : 'fade',
		style : {
			right : '',
			bottom : ''
		}
	});
}
/**
 * 自动处理返回的resultmsg对象
 * 
 * @param result_msg
 *            服务端返回的resultmsg对象
 * @param succFunc
 *            操作成功的回调函数
 * @param errFunc
 *            失败回调函数
 * @param default_error_msg
 *            服务端返回非json时的默认的错误信息
 */
function easyui_auto_notice(result_msg, succFunc, errFunc, default_error_msg) {
	default_error_msg = default_error_msg || '操作过程发生异常，请查看日志信息!';
	try {
		if (result_msg == null || result_msg.title == undefined
				|| result_msg.title == null) {
			easyui_warn(result_msg.title, fn);
		} else {
			if (result_msg.success == true) {
				easyui_info(result_msg.title, succFunc);
			} else {
				easyui_warn(result_msg.title, errFunc);
			}
		}
	} catch (e) {
		easyui_warn(default_error_msg, errFunc);
	}

}

// 扩展校验
$.extend(
				$.fn.validatebox.defaults.rules,
				{
					// 异步校验 判断是否已存在
					remoteIsExist : {
						validator : function(value, params) {
							var hidden_id = $("#" + params[1]).val();
							var url = contextpath
									+ "base/dic/validRuleController/validIsExist.do?tablecode="
									+ params[0] + "&id=" + hidden_id;
							if (params.length == 3 && params[2].length > 0)
								url += "&elementcode="
										+ $("#elementcode").val();

							var data = $.ajax({
								url : url,
								dataType : "json",
								data : {
									"value" : value
								},
								async : false,
								type : "POST"
							}).responseText;
							if (data == "0") {
								flag = true;
								$.data(document.body, "flag", flag);
							} else if (data == "1") {
								flag = false;
								$.data(document.body, "flag", flag);
							}
							return $.data(document.body, "flag");
						},

						message : '已被使用'
					},
					// 层码校验是否合法
					LayerCodeLegal : {
						validator : function(value, params) {
							var codeformat = params[0].split('-');
							var node = $('#' + params[1]).tree('getSelected');
							var opertype = $('#opertype').val();
							var levelno = node.levelno-1; //当前级次 是从1开始 ， 数组 从0开始
							var parentcode = node.code;
							var currentCode = value;
							if (opertype == 'add') {
								currentCode = value;
								if (currentCode.length != codeformat[0]) {
									$.fn.validatebox.defaults.rules.LayerCodeLegal.message = '编码格式为【'
											+ params[0] + '】';
									return false;
								}
							} else if (opertype == 'adddown') {
								if (value.indexOf(parentcode) != 0) {
									$.fn.validatebox.defaults.rules.LayerCodeLegal.message = '编码必须以父编码【'
											+ parentcode + '】作为前缀';
									return false;
								}
								currentCode = value.substring(parentcode.length);
								if (currentCode.length != codeformat[levelno]) {
									$.fn.validatebox.defaults.rules.LayerCodeLegal.message = '编码格式为【'
											+ params[0] + '】';
									return false;
								}
							} else if (opertype == 'edit') {
								var parentnode = $('#' + params[1]).tree('getParent', node.target);
								if (parentnode) {
									parentcode = parentnode.code;

									if (value.indexOf(parentcode) != 0) {
										$.fn.validatebox.defaults.rules.LayerCodeLegal.message = '编码必须以父编码【'
												+ parentcode + '】作为前缀';
										return false;
									}
									currentCode = value.substring(parentcode.length);
									if (currentCode.length != codeformat[levelno]) {
										$.fn.validatebox.defaults.rules.LayerCodeLegal.message = '编码格式为【' + params[0] + '】';
										return false;
									}
								} else {
									currentCode = value;
									if (currentCode.length != codeformat[0]) {
										$.fn.validatebox.defaults.rules.LayerCodeLegal.message = '编码格式为【' + params[0] + '】';
										return false;
									}
								}
							}
							return true;
						},
						message : ''// '编码不合法'
					},
					phonesIsRight : {
						validator : function(value, params) {
							var re = /^1[0-9][0-9]\d{8}(\,1[0-9][0-9]\d{8})*\s*$/;
							if(!re.test(value)){
								 $.fn.validatebox.defaults.rules.phonesIsRight.message = '手机号码格式为：13811112222,13833334444';
								return false;
							}
							if(value.length>50){
								 $.fn.validatebox.defaults.rules.phonesIsRight.message = '最大长度为50';
								return false;
							}
							return true;
						},
						message:''
					},
					telephonesIsRight : {
						validator : function(value, params) {
							var re = /^(\d{3,4}-)?\d{7,8}(\,(\d{3,4}-)?\d{7,8})*\s*$/;
							if(!re.test(value)){
								 $.fn.validatebox.defaults.rules.telephonesIsRight.message = '电话号码格式为：86056314,0531-86056314';
								return false;
							}
							if(value.length>50){
								 $.fn.validatebox.defaults.rules.telephonesIsRight.message = '最大长度为50';
								return false;
							}
							return true;
						},
						message:''
					},
					isNumber : {
						validator : function(value, params) {
							var re = /^[0-9]*$/;
							if(!re.test(value)){
								 $.fn.validatebox.defaults.rules.isNumber.message = '必须为数字';
								return false;
							}
							if(value.length>50){
								$.fn.validatebox.defaults.rules.isNumber.message = '最大长度为50个数字';
								return false;
							}
							return true;
						},
						message:''
					},
					isEmail : {
						validator : function(value, params) {
						var re = /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})(\,(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3}))*\s*$/;
						if(!re.test(value)){
							 $.fn.validatebox.defaults.rules.isEmail.message = '邮箱格式不对，例如邮箱格式为：1111111@qq.com,name@136.com';
							return false;
						}
						return true;
					},
						message:''
					}
				});

/**
 * 创建用于验证层码字段格式的验证框.
 * 另外，使用这个功能时，要在表单中增加个id为opertype的隐藏字段并传入edit、add或adddown来标识验证类型
 * 
 * @param targetInput
 *            输入框的id，与编码字段名是一样的
 * @param tableName
 *            表名
 * @param idColName
 *            表的主键列名
 * @param treeId
 *            树控件的id,一般层码结构由树进行显示
 * 
 * 
 */
function makeCodeValidationInputBox(targetInput, tableName, idColName, treeId) {
	$.post(contextpath
			+ '/base/tabsdef/DicColumnController/getTableColumnFormat.do', {
		tableName : tableName,
		columnName : targetInput
	}, function(map) {
		var codeFormat = map.codeFormat;
		if (codeFormat) {
			$('#' + targetInput).validatebox({
				validType : {
					remoteIsExist : [ tableName, idColName ],
					LayerCodeLegal : [ codeFormat, treeId ]
				}
			});
		} else {
			easyui_warn("编码字段校验器初始化失败！");
		}
	}, 'json');
}

function webEncode(str) {
	return encodeURIComponent(str);
}

/**
 * From扩展 getData 获取数据接口
 * 
 * @param {Object}
 *            jq
 * @param {Object}
 *            params 设置为true的话，会把string型"true"和"false"字符串值转化为boolean型。
 */
$
		.extend(
				$.fn.form.methods,
				{
					getData : function(jq, params) {
						var formArray = jq.serializeArray();
						var oRet = {};
						for ( var i in formArray) {
							if (typeof (oRet[formArray[i].name]) == 'undefined') {
								if (params) {
									oRet[formArray[i].name] = (formArray[i].value == "true" || formArray[i].value == "false") ? formArray[i].value == "true"
											: formArray[i].value;
								} else {
									oRet[formArray[i].name] = formArray[i].value;
								}
							} else {
								if (params) {
									oRet[formArray[i].name] = (formArray[i].value == "true" || formArray[i].value == "false") ? formArray[i].value == "true"
											: formArray[i].value;
								} else {
									oRet[formArray[i].name] += ","
											+ formArray[i].value;
								}
							}
						}
						return oRet;
					}
				});

/**
 * Layout布局面板折叠起来，横向或纵向显示标题名称。
 */
$.extend($.fn.layout.paneldefaults, {
	onCollapse : function() {
		var layout = $(this).parents(".layout");
		var opts = $(this).panel("options");
		var expandKey = "expand" + opts.region.substring(0, 1).toUpperCase()
				+ opts.region.substring(1);
		var expandPanel = layout.data("layout").panels[expandKey];
		if (opts.region == "west" || opts.region == "east") {
			var split = [];
			if (opts.title) {
				for ( var i = 0; i < opts.title.length; i++) {
					split.push(opts.title.substring(i, i + 1));
				}
				expandPanel.panel("body").addClass("panel-title").css(
						"text-align", "center").html(split.join("<br>"));
			}
		} else {
			if (opts.title) {
				expandPanel.panel("setTitle", opts.title);
			}
		}
	}
});

//折叠
var panel_flag = [ '1', '1', '1', '1', '1' ];
function panelctl(_index) {
	if (panel_flag[_index] == '0') {
		$(panel_ctl_handles[_index].panelname).panel('close');
		$(panel_ctl_handles[_index].buttonid).linkbutton({
			iconCls : 'icon-collapse'
		});
		panel_flag[_index] = '1';
	} else {
		$(panel_ctl_handles[_index].panelname).panel('open');
		$(panel_ctl_handles[_index].buttonid).linkbutton({
			iconCls : 'icon-expand'
		});
		panel_flag[_index] = '0';
	}
	
	var gridname = panel_ctl_handles[_index].gridname;
	var panelname = panel_ctl_handles[_index].panelname;
	autoResizeGrid(gridname, panelname);
}

//折叠
var panel_flag = [ '1', '1', '1', '1', '1' ];
function panelctl_aros(_index) {
	if (panel_flag[_index] == '1') {
		$(panel_ctl_handles[_index].panelname).panel('close');
		$(panel_ctl_handles[_index].buttonid).linkbutton({
			iconCls : 'icon-collapse'
		});
		panel_flag[_index] = '0';
	} else {
		$(panel_ctl_handles[_index].panelname).panel('open');
		$(panel_ctl_handles[_index].buttonid).linkbutton({
			iconCls : 'icon-expand'
		});
		panel_flag[_index] = '1';
	}
	
	var gridname = panel_ctl_handles[_index].gridname;
	var panelname = panel_ctl_handles[_index].panelname;
	autoResizeGrid(gridname, panelname);
}

function autoResizeGrid(gridname, panelname){
	var dataGrid = $(gridname); 
	var opts = dataGrid.datagrid("options");
	var panel = $(panelname);
	dataGrid.datagrid("resize", {width:opts.width, height:opts.height - panel.height()});
}

function autoResizeFormLayout(jq){
	//表单控件的自适应宽度
	var easyuiObjs = $(".easyui-combo, .easyui-datebox, easyui-datetimebox, .easyui-combobox, .easyui-combotree");
	if(jq){
		easyuiObjs = jq.find(".easyui-combo, .easyui-datebox, easyui-datetimebox, .easyui-combobox, .easyui-combotree");
	}
	
	easyuiObjs.each(function(i, comboObj){
		var textbox = $(comboObj).combo("textbox");
		textbox.parent().css({width:"100%"});
		textbox.css({width: "100%" });
		textbox.parent().find("span").css({"margin-left": "-10px"});
	});
}

/**
 * 下拉刷新Pager  只显示刷新和 共{total}条
 * @param datagrid_ Datagrid
 */
function refDropdownPager(datagrid_) {
	var p = datagrid_.datagrid('getPager');
	if (p) {
		$(p).pagination({
			layout:['refresh'], //只显示刷新按钮
			displayMsg : '共{total}条记录' //提示格式 共{total}记录
		});
	}
}

/**
 * 导出Excel 后台导出
 * @param datagrid_
 * @param paramJSON
 */
function outExcel(datagrid_, paramJSON) {
	
	var paramStr = '';
	for(var item in paramJSON) {
		if (paramStr.length == 0)
			paramStr += '?';
		else
			paramStr += '&';
		paramStr += item + '=' + paramJSON[item];
	}
	window.open(contextpath + 'base/excel/SysExcelOutController/outExcel.do' + paramStr, "_blank");
}

/**
 * 按导出类型导出excel
 * @param datagrid_ 导出数据列表 datagrid
 * @param outExcelType 导出类型 最多四个选项：1-导出选中数据（默认）、2-导出本页（不分页方式，此项选择无）、3-所有（现条件下所有）、4-全部（权限范围内全部
 * @param includeHidden 是否包含隐藏列
 * @param title 导出Excel文件名称 暂不支持中文
 * @param excelVersion excel版本
 * @param paramJSON 参数
 */
function outExcelByType(datagrid_, outExcelType, includeHidden, title, excelVersion, paramJSON) {
	switch (outExcelType) {
	case 1: //导出选中行
		var paramJSON = {
			includeHidden : false,
			title : 'account',
			excelVersion: '2007',
			outExcelModel : outExcelType
		};
		datagrid_.datagrid("outExcel",paramJSON);
		break;
	case 2: //当前页（不分页方式，此项选择无）
		var paramJSON = {
			includeHidden : false,
			title : 'account',
			excelVersion: '2007',
			outExcelModel : outExcelType
		};
		datagridAccount.datagrid("outExcel",paramJSON);
		break;
	case 3: //导出所有（现条件下所有包含数据权限范围内）
		if (!paramJSON)
			paramJSON = {};
		
		paramJSON.filename = 'outAccountExcel';
		paramJSON.excelVersion = '2007';
		paramJSON.outExcelModel = outExcelType;
		
		outExcel('', paramJSON);
		break;
	case 4: //导出全部（权限范围内全部）
		if (!paramJSON)
			paramJSON = {};
		
		paramJSON.filename = 'outAccountExcel';
		paramJSON.excelVersion = '2007';
		paramJSON.outExcelModel = outExcelType;
		
		outExcel('', paramJSON);
		break;
	default:
		break;
	}
};

/**
 * 数据权限配置查看
 * @param menuid 菜单ID
 */
function datascopeDetails(menuid) {
	var paramJSON = {};
	paramJSON.title = '数据权限查看';
	paramJSON.width = 900;
	paramJSON.height = 600;
	paramJSON.url = contextpath + '/base/datascope/DataRightDetailsController/detailsEntry.do?resourceid=' + menuid;
	
	showDialog(paramJSON);
}

/**
 * 
 * @param paramJSON title, url, rowData, formID, fill, width, height
 */
function showDialog(paramJSON) {
	parent.$.modalDialog({
		title : paramJSON.title,
		width : paramJSON.width ? paramJSON.width : 'auto',
		height : paramJSON.height ? paramJSON.height : 'auto',
		href : paramJSON.url,
		maximizable : true,
		onBeforOpen: function(){
			
		},
		onLoad: function(){
			
		}
	});
}