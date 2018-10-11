/**
 * 加载页面初始化jQuery脚本
 */
$(function() {

	$.extend($.fn.validatebox.defaults.rules, {
		minLength : {
			validator : function(value, param) {
				return value.length >= param[0];
			},
			message : '输入不能少于{0} 个字符长度！'
		},
		//验证速递单号,数字和字母
	    couriernum: {
	        validator: function (value) {
	            return /^[0-9A-Za-z]+$/.test(value);
	        },
	        message: '只能输入数字或字母'
	    }
	});

	// Logo菜单动态效果
	// dynLogoTopMenu();

	// 默认刷新显示第一个一级菜单的菜单树
	refreshMenuTree(menus[0]);
	
	//默认显示选中的以及菜单
	bindTopMenuEventFirst(menuid);

	// 绑定一级菜单响应事件
	bindTopMenuEvent();

	// 绑定左侧树形菜单的响应事件
	bindMenuTreeEvent();

	// 绑定右键事件
	bindRigthMouseEvent();

	bindCornerMenuEvent();

	// 添加选中页签监听事件
	addOnSelectEvent();

	// 加载首页
	loadWelcomeIndex();

	// 加载公告留言评论
	alertMsgNotice();

});

// 右下角弹出留言公告别人反馈未阅读的列表
function alertMsgNotice() {
	$.post(contextpath
			+ "sys/notice/SysNoticeManageController/getCommonUnreadList.do",
			"", function(data) {

				if (data != "") {
					// 右下角弹出提示框
			$.messager.show( {
				title : '反馈通知',
				msg : data,
				timeout : 5000,
				showType : 'slide',
				width : '300',
				height : '250',
				timeout : 0
			});
			$(".messager-body").css("overflow", "auto");
		}

	}, "json");
}

// 点击提示框信息链接
function commentDetailIndex(ele,messageType, noticeid) {

	var showBaseDetail = contextpath
			+ "sys/notice/SysNoticeManageController/showBaseDetail.do";
	var noticeCommonListUrl = contextpath
			+ "sys/notice/SysNoticeManageController/noticeCommonList.do";

	var setHeight;
	if (messageType == '1') {
		setHeight = 530;
	} else {
		setHeight = 540;
	}
	//设置访问过来a标签颜色
	$(ele).attr('class','visited');
	
	showlistModalDialog(setHeight, "baseDetailForm", "反馈详情", showBaseDetail
			+ "?messageType=" + messageType + "&flag=commonlist&noticeId="
			+ noticeid, noticeCommonListUrl + "?noticeid=" + noticeid
			+ "&messageType=" + messageType, noticeid, messageType);
}

// 展示评论详情
function showlistModalDialog(width, form, title, href, url, noticeid,
		messageType) {

	var datagridHigt;
	if (messageType == '1') {
		datagridHigt = 413;
	} else {
		datagridHigt = 423;
	}
	var icon = 'icon-view';
	parent.$
			.modalDialog( {
				title : title,
				iconCls : icon,
				width : 800,
				height : width,
				href : href,
				onLoad : function() {

					mdDialog = parent.$.modalDialog.handler;

					var f = parent.$.modalDialog.handler.find('#' + form);
					f.form("load",contextpath+ "sys/notice/SysNoticeManageController/getNoticeInfo.do?noticeid="+ noticeid);

					// 将获取内容的值赋值给div
					$.post(contextpath+ "sys/notice/SysNoticeManageController/getClobContentVal.do",{noticeid : noticeid}, 
							function(data) {
									// 给父窗口的控件设值
									mdDialog.find("#contentEdit").html(data);
								}, "json");

					// 加载datagrid
					mdDialog.find("#noticecommonlist").datagrid( {
						width : 784,
						height : datagridHigt,
						stripe : true,
						singleSelect : true,
						rownumbers : true,
						pagination : true,
						remoteSort : false,
						multiSort : true,
						pageSize : 30,
						url : url,
						showFooter : true,
						columns : [ [ {
							field : "title",
							title : "标题",
							halign : 'center',
							width : 200,
							sortable : true
						}, {
							field : "commentusername",
							title : "反馈人",
							halign : 'center',
							width : 100,
							sortable : true
						}, {
							field : "commentcontent",
							title : "反馈内容",
							halign : 'center',
							width : 200,
							sortable : true
						}, {
							field : "commenttime",
							title : "反馈时间",
							halign : 'center',
							width : 150,
							sortable : true
						} ] ]
					});

					// 展示上传附件
					showFileDiv(mdDialog.find("#filetd"), false, "NOTICE",
							noticeid, "");

				},
				buttons : [ {
					text : "关闭",
					iconCls : "icon-cancel",
					handler : function() {
						parent.$.modalDialog.handler.dialog('close');
					}
				} ]
			});
}

$.modalDialog = function(options) {
	if ($.modalDialog.handler == undefined) {// 避免重复弹出
		var opts = $.extend( {
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.modalDialog.handler = undefined;
				$(this).dialog('destroy');
			}
		}, options);
		opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
		return $.modalDialog.handler = $('<div/>').dialog(opts);
	}
};
//在modalDialog窗口上弹出窗口
$.modalDialog2 = function(options) {
	if ($.modalDialog.handler2 == undefined) {// 避免重复弹出
		var opts = $.extend( {
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.modalDialog.handler2 = undefined;
				$(this).dialog('destroy');
			}
		}, options);
		opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
		return $.modalDialog.handler2 = $('<div/>').dialog(opts);
	}
};

/**
 * 另一种非异步的弹窗功能插件 使用该控件时，请在options去掉href属性，改为html属性 同时，去掉onLoad属性，使用onOpen属性
 */

$.fastModalDialog = function(options) {
	if (!$.fastModalDialog.handler) {
		$.fastModalDialog.handler = new Array();
	}

	if (!options.dialogID) {
		options.dialogID = 'default_dialog';
	}
	if ($.fastModalDialog.handler[options.dialogID] == undefined) {// 避免重复弹出
		var opts = $.extend( {
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.fastModalDialog.handler[options.dialogID] = undefined;
				$(this).dialog('destroy');
			},
			closed : true
		}, options);
		opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数

		$.fastModalDialog.handler[options.dialogID] = $(options.html).dialog(
				opts);
		$.fastModalDialog.handler[options.dialogID].dialog('open');
		return $.fastModalDialog.handler[options.dialogID];
	} else {
		easyui_warn("重复的弹出窗口！");
	}
};

function dynLogoTopMenu() {
	var dleft = $('.top_background li.active').offset().left
			- $('.top_background').offset().left - 5;
	var dwidth = $('.top_background li.active').width() + "px";

	$('.top_background .floatr').css( {
		"left" : dleft + "px",
		"width" : dwidth
	});

	$('.top_background li')
			.hover(
					function() {
						var left = $(this).offset().left
								- ($(this).parents('.top_background').offset().left + 5);
						var width = $(this).width() + "px";
						var sictranslate = "translate(" + left + "px, 0px)";
						$(this).parent('.top_background ul').next('div.floatr')
								.css( {
									"width" : width,
									"-webkit-transform" : sictranslate,
									"-moz-transform" : sictranslate
								});
					},
					function() {
						var sibs = $(this)
								.siblings('.top_background li.active');
						if (sibs.length <= 0) {
							return;
						}
						var left = sibs.offset().left
								- ($(this).parents('.top_background').offset().left + 5);
						var width = sibs.width() + "px";
						var sictranslate = "translate(" + left + "px, 0px)";
						$(this).parent('.top_background ul').next('div.floatr')
								.css( {
									"width" : width,
									"-webkit-transform" : sictranslate,
									"-moz-transform" : sictranslate
								});
					}).click(function() {
				$(this).siblings('.top_background li').removeClass('active');
				$(this).addClass('active');
				return false;
			});
}

/**
 * 绑定一级菜单的点击事件
 */
/*function bindTopMenuEvent() {
	$("div.top_menu ul li a").click(function() {
		var menuid = $(this).attr("menuid");
		for ( var i = 0; i < menus.length; i++) {
			var topMenu = menus[i];
			if (topMenu.id == menuid) {
				if (topMenu.children) {
					refreshMenuTree(topMenu);
				}
				break;
			}
		}
	});
}
*/
/**
 * 绑定一级菜单的点击事件
 * 修改：刘俊波 2016/2/13
 */
function bindTopMenuEvent(){
	$("div.top_menu ul li a").click(function(){
		var menuid = $(this).attr("menuid");
		for(var i=0; i<menus.length; i++){
			var topMenu = menus[i];
			$("div.top_menu ul li a").css("background-color","");
			$("div.top_menu ul li a").css("-moz-border-radius","15px");
			$("div.top_menu ul li a").css("-webkit-border-radius","15px");
			$("div.top_menu ul li a").css("font-size","13px");
			if(topMenu.id == menuid){
				if(topMenu.children){
					$(this).css("background-color","#38B0DC");
					$(this).css("-moz-border-radius","15px");
					$(this).css("-webkit-border-radius","15px");
					$(this).css("font-size","15px");
					
					refreshMenuTree(topMenu);
				}
				break;
			}
		}
	});
}
/**
 * 绑定跳转后选中的一级菜单的点击事件
 */
function bindTopMenuEventFirst(menuid){
	for(var i=0; i<menus.length; i++){
		var topMenu = menus[i];
		$("div.top_menu ul li a").css("background-color","");
		//$("div.top_menu ul li a").css("-moz-border-radius","15px");
		//$("div.top_menu ul li a").css("-webkit-border-radius","15px");
		if(topMenu.id == menuid){
			if(topMenu.children){
				$("a[menuid="+menuid+"]").css("background-color","#38B0DC");
				$("a[menuid="+menuid+"]").css("-moz-border-radius","15px");
				$("a[menuid="+menuid+"]").css("-webkit-border-radius","15px");
				$("a[menuid="+menuid+"]").css("font-size","15px");
				refreshMenuTree(topMenu);
			}
			break;
		}
		//若点击不存在的权限按钮，默认显示第一个菜单
		if(i==(menus.length-1)){
			refreshMenuTree(menus[0]);
		}
	}
}

/**
 * 添加选中监听事件
 */
function addOnSelectEvent() {

	$("#tabs")
			.tabs(
					{
						"onSelect" : function(title) {

							var tab = $("#tabs").tabs("getSelected");
							var iframeWindow = window.frames["iframe_"
									+ tab.attr('id')];

							if (iframeWindow != undefined
									&& iframeWindow.showReload != undefined
									&& iframeWindow.showReload() instanceof Function) {
								iframeWindow.showReload();
							}
						}
					});
}

function selectHomeTab() {
	hideCornerMenusItems();
	$("#tabs").tabs("select", 0);
}

function logoutSystem() {
	hideCornerMenusItems();
	$.messager.confirm('确认', '是否注销用户?', function(r) {
		if (r) {
			window.location.href = contextpath;
		}
	});
}

function editPassword() {
	hideCornerMenusItems();

	$.fastModalDialog( {
				title : '修改密码',
				width : 280,
				height : 210,
				html : $('#div_pswd_edit_form').html(),
				dialogID : 'pswddlg',
				onOpen : function() {
					$.fastModalDialog.handler['pswddlg'].find('#pswdold')
							.validatebox( {
								required : true,
								missingMessage : '请输入原密码'
							});
					$.fastModalDialog.handler['pswddlg'].find('#pswdnew')
							.validatebox( {
								required : true,
								missingMessage : '请输入新密码',
								validType : [ 'minLength[6]' ]
							});
					$.fastModalDialog.handler['pswddlg'].find('#pswdnew2')
							.validatebox( {
								required : true,
								missingMessage : '请确认新密码'
							});

					$.fastModalDialog.handler['pswddlg'].find('#pswdnew')
							.keyup(function(e) {
								AuthPasswd($(this).val());
							});
				},
				buttons : [
						{
							text : "确定",
							iconCls : "icon-ok",
							handler : function() {
								var form = $.fastModalDialog.handler['pswddlg']
										.find('#_pswd_edit_form');

								var ps1 = $.fastModalDialog.handler['pswddlg']
										.find('#pswdnew').val();
								var ps2 = $.fastModalDialog.handler['pswddlg']
										.find('#pswdnew2').val();
								if (ps1 != ps2) {
									easyui_warn('两次输入的新密码不一致，请重新填写！');
									return;
								}
								if (form.form('validate')) {
									// 发起请求
									form
											.form(
													'submit',
													{
														url : contextpath
																+ '/sys/SysUserController/editPassword.do?username='
																+ _global_usercode_,
														success : function(data) {
															try {
																data = eval("("
																		+ data
																		+ ")");
															} catch (e) {
															}
															easyui_auto_notice(
																	data,
																	function() {
																		$.fastModalDialog.handler['pswddlg']
																				.dialog('close');
																	},
																	function() {
																	},
																	'修改过程中发生异常！');

														}
													});

								} else {
									easyui_warn('请填写必填项后再提交！');
								}

							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								$.fastModalDialog.handler['pswddlg']
										.dialog('close');
							}
						} ]
			});
}
// 个人信息维护
function editPersonalInfo() {
	hideCornerMenusItems();

	// 查询个人详细信息

	$
			.fastModalDialog( {
				title : '信息维护',
				width : 550,
				height : 270,
				html : $('#div_personalinfo_edit_form').html(),
				dialogID : 'personalinfodlg',
				onOpen : function() {
					// 初始化每个控件
					var modalDialog = $.fastModalDialog.handler['personalinfodlg'];
					modalDialog.find('#usercode').textbox( {});
					modalDialog.find('#username').textbox( {});
					modalDialog.find('#orgname').textbox( {});
					modalDialog.find('#phone').textbox( {});
					modalDialog.find('#weixin').textbox( {});
					modalDialog.find('#qq').textbox( {});
					modalDialog.find('#email').textbox( {});
					modalDialog.find('#createtime').textbox( {});
					modalDialog.find('#remark').textbox( {});

					// 页面赋值
					$
							.post(
									contextpath + '/sys/SysUserInfoController/showPersonalInfo.do',
									function(data) {
										// 赋值
										modalDialog.find('#usercode').textbox(
												'setValue', data.usercode);
										modalDialog.find('#username').textbox(
												'setValue', data.username);
										modalDialog.find('#orgname').textbox(
												"setValue", data.orgname);
										// 如果扩展表中有信息，则赋值
										var count = data.countExt;
										if (count > 0) {
											modalDialog.find('#phone').textbox(
													'setValue', data.phone);
											modalDialog.find('#weixin')
													.textbox('setValue',
															data.weixin);
											modalDialog.find('#qq').textbox(
													'setValue', data.qq);
											modalDialog.find('#email').textbox(
													'setValue', data.email);
										}

										modalDialog.find('#createtime')
												.textbox('setValue',
														data.createtime);
										modalDialog.find('#overduedate')
												.textbox('setValue',
														data.overduedate);
										modalDialog.find('#remark').textbox(
												'setValue', data.remark);

									}, "json");
					// 获取手机、QQ、邮箱、微信进行格式校验
				},
				buttons : [
						{
							text : "确定",
							iconCls : "icon-ok",
							handler : function() {
								var form = $.fastModalDialog.handler['personalinfodlg']
										.find('#_personalinfo_edit_form');

								if (form.form('validate')) {
									// 发起请求
									form
											.form(
													'submit',
													{
														url : contextpath + '/sys/SysUserInfoController/editPersonalInfo.do',
														success : function(data) {
															try {
																data = eval("("
																		+ data
																		+ ")");
															} catch (e) {
															}
															easyui_auto_notice(
																	data,
																	function() {
																		$.fastModalDialog.handler['personalinfodlg']
																				.dialog('close');
																	},
																	function() {
																	},
																	'修改过程中发生异常！');

														}
													});

								} else {
									easyui_warn('请填写必填项后再提交！');
								}

							}
						},
						{
							text : "取消",
							iconCls : "icon-cancel",
							handler : function() {
								$.fastModalDialog.handler['personalinfodlg']
										.dialog('close');
							}
						} ]
			});
}

function showAboutWindow() {
	$.messager.alert('关于', 'JBF基础开发框架 V1.0', 'info');
}
/**
 * 绑定左侧导航菜单栏的菜单树的响应事件
 */
function bindMenuTreeEvent() {
	$('.cs-navi-tab').click(function() {
		var $this = $(this);
		var href = $this.attr('src');
		var menuid = $this.id();
		var title = $this.text();
		addTab(title, href, menuid);
	});
}

/**
 * 刷新左侧导航菜单
 * 
 * @param topMenu
 *            顶级菜单
 */
function refreshMenuTree(topMenu) {
	$("#mt").empty();
	$("#mt").tree( {
		data : [ topMenu ],
		onClick : function(node) {
			if (!node.webpath || node.webpath == "-") {
				return;
			}
			addTab(node.text, node.webpath, node.id);
		}
	});

	var rootNode = $("#mt").tree('getRoot');
	if (rootNode != null)
		$("#mt").tree('expand', rootNode.target);
}

/**
 * 添加页签
 * 
 * @param title
 *            页签标题，菜单节点名称
 * @param url
 *            页签中URL，请求页面的URL
 */
function addTab(title, url, menuid) {
	if ($('#tabs').tabs('exists', title)) {
		$('#tabs').tabs('select', title);// 选中不刷新
		// var currTab = $('#tabs').tabs('getSelected');
		// var url = $(currTab.panel('options').content).attr('src');
		// if (url != undefined && currTab.panel('options').title != 'Home') {
		// $('#tabs').tabs('update', {
		// tab : currTab,
		// options : {
		// content : createFrame(url, menuid)
		// }
		// });
		// }
	} else {
		var content = createFrame(url, menuid);
		$('#tabs').tabs('add', {
			id : menuid,
			title : title,
			content : content,
			closable : true
		});
	}
	tabClose();
}

/**
 * 创建iFrame，内嵌到Tab页签
 * 
 * @param url
 *            请求页面URL
 * @returns {String} 页面文本内容
 */
function createFrame(url, menuid) {
	if (url.indexOf("menuid") == -1) {
		if (url.indexOf("?") == -1) {
			url += "?menuid=" + menuid;
		} else {
			url += "&menuid=" + menuid;
		}
	}

	var s = '<iframe id="' + menuid + '" name="iframe_' + menuid
			+ '" scrolling="auto" frameborder="0"  src="' + url
			+ '" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose() {
	/* 双击关闭TAB选项卡 */
	$(".tabs-inner").dblclick(function() {
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close', subtitle);
	});
	/* 为选项卡绑定右键 */
	$(".tabs-inner").bind('contextmenu', function(e) {
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});

		var subtitle = $(this).children(".tabs-closable").text();

		$('#mm').data("currtab", subtitle);
		$('#tabs').tabs('select', subtitle);
		return false;
	});
}

/**
 * 绑定右键菜单事件
 */
function bindRigthMouseEvent() {

	// 刷新
	$('#mm-tabupdate').click(function() {
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		var menuid = $(currTab.panel('options').content).attr('id');
		if (url != undefined && currTab.panel('options').title != 'Home') {
			$('#tabs').tabs('update', {
				tab : currTab,
				options : {
					content : createFrame(url, menuid)
				}
			});
		}
	});
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close', currtab_title);
	});
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			if (t != '首页提醒') {
				$('#tabs').tabs('close', t);
			}
		});
	});
	// 关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		var nextall = $('.tabs-selected').nextAll();
		if (prevall.length > 0) {
			prevall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				if (t != '首页提醒') {
					$('#tabs').tabs('close', t);
				}
			});
		}
		if (nextall.length > 0) {
			nextall.each(function(i, n) {
				var t = $('a:eq(0) span', $(n)).text();
				if (t != '首页提醒') {
					$('#tabs').tabs('close', t);
				}
			});
		}
		return false;
	});
	// 关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function() {
		var nextall = $('.tabs-selected').nextAll();
		if (nextall.length == 0) {
			// msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});
	// 关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		if (prevall.length == 0) {
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});

	// 退出
	$("#mm-exit").click(function() {
		$('#mm').menu('hide');
	});
}

var corner_menu_showing = false;
/**
 * 右上角按钮点击事件
 */
function bindCornerMenuEvent() {
	$('#corner_button').hover(function() {
		var winW = document.body.clientWidth;
		$('#corner_menu_items')[0].style.left = (winW - 200) + 'px';
		$('#corner_menu_items').show();
		corner_menu_showing = false;
	}, function() {
		setTimeout(function() {
			if (!corner_menu_showing) {
				$('#corner_menu_items').hide();
			}
		}, 100);
	});

	$('#corner_menu_items').hover(function() {
		corner_menu_showing = true;
	}, function() {
		corner_menu_showing = false;
		setTimeout(function() {
			if (!corner_menu_showing) {
				$('#corner_menu_items').hide();
			}
		}, 100);
	});
}
/**
 * 隐藏右上角的按钮弹出层
 */
function hideCornerMenusItems() {
	$('#corner_menu_items').hide();
}
var temp;
function scroll(n) {
	temp = n;
	temp = $("div[id=nav]").scrollLeft() + temp;
	$("div[id=nav]").scrollLeft(temp);
}

/**
 * 
 */
function loadWelcomeIndex() {
//	var url = contextpath + '/sys/toRemind/ToRemindController/entry.do';
//	var url = contextpath + '/ppms/indexPage/IndexPageController/entry.do';
	var url = contextpath + '/aros/homepage/HomepageController/entry.do';
	var tab = $("#tabs").tabs('getTab', 0);
	$("#tabs")
			.tabs(
					'update',
					{
						tab : tab,
						options : {
							content : '<iframe id="0" name="iframe_0" scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>'
						}
					});
}

