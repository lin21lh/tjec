$(function () {
	/*
	var url = contextpath + "/aros/xzfy/controller/CasebaseinfoController/xzfyInit.do";
	var content = createFrame(url, menuid);
	$('#tabs').tabs('add', {
		id: menuid,
		content: content
	});
	*/
	//行政应诉
	var XZYSUrl = contextpath + "aros/xzys/controller/init.do?menuid="+menuid;
	if (key == "XZYS") {
		$("#xzfyInit").attr("src", XZYSUrl);
	}
	
})

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

function logoutSystem() {
	$.messager.confirm('确认', '是否注销用户?', function(r) {
		if (r) {
			window.location.href = contextpath;
		}
	});
}

function editPassword() {
	$.fastModalDialog({
				title: '修改密码',
				width: 280,
				height: 210,
				html: $('#div_pswd_edit_form').html(),
				dialogID : 'pswddlg',
				onOpen : function() {
					$.fastModalDialog.handler['pswddlg'].find('#pswdold').validatebox({
								required : true,
								missingMessage : '请输入原密码'
							});
					$.fastModalDialog.handler['pswddlg'].find('#pswdnew').validatebox({
								required : true,
								missingMessage : '请输入新密码',
								validType : [ 'minLength[6]' ]
							});
					$.fastModalDialog.handler['pswddlg'].find('#pswdnew2').validatebox({
								required : true,
								missingMessage : '请确认新密码'
							});

					$.fastModalDialog.handler['pswddlg'].find('#pswdnew').keyup(function(e) {
								AuthPasswd($(this).val());
							});
				},
				buttons : [{
						text : "确定",
						iconCls : "icon-ok",
						handler : function() {
							var form = $.fastModalDialog.handler['pswddlg'].find('#_pswd_edit_form');
							var ps1 = $.fastModalDialog.handler['pswddlg'].find('#pswdnew').val();
							var ps2 = $.fastModalDialog.handler['pswddlg'].find('#pswdnew2').val();
							if (ps1 != ps2) {
								easyui_warn('两次输入的新密码不一致，请重新填写！');
								return;
							}
							if (form.form('validate')) {
								// 发起请求
								form.form('submit',
									{url : contextpath+ '/sys/SysUserController/editPassword.do?username='+ _global_usercode_,
										success : function(data) {
											try {
												data = eval("("+ data+ ")");
											} catch (e) {}
											easyui_auto_notice(data,
												function() {
													$.fastModalDialog.handler['pswddlg'].dialog('close');
												},
												function() {},
												'修改过程中发生异常！');
										}
									});

							} else {
								easyui_warn('请填写必填项后再提交！');
							}
						}
					},{
						text : "取消",
						iconCls : "icon-cancel",
						handler : function() {
							$.fastModalDialog.handler['pswddlg']
									.dialog('close');
						}
					}]
			});
}
// 个人信息维护
function editPersonalInfo() {
	// 查询个人详细信息
	$.fastModalDialog({
		title: '信息维护',
		width: 550,
		height: 270,
		html: $('#div_personalinfo_edit_form').html(),
		dialogID: 'personalinfodlg',
		onOpen: function() {
			// 初始化每个控件
			var modalDialog = $.fastModalDialog.handler['personalinfodlg'];
			modalDialog.find('#usercode').textbox({});
			modalDialog.find('#username').textbox({});
			modalDialog.find('#orgname').textbox({});
			modalDialog.find('#phone').textbox({});
			modalDialog.find('#weixin').textbox({});
			modalDialog.find('#qq').textbox({});
			modalDialog.find('#email').textbox({});
			modalDialog.find('#createtime').textbox({});
			modalDialog.find('#remark').textbox({});

			// 页面赋值
			$.post(contextpath + '/sys/SysUserInfoController/showPersonalInfo.do',
			function(data) {
				// 赋值
				modalDialog.find('#usercode').textbox('setValue', data.usercode);
				modalDialog.find('#username').textbox('setValue', data.username);
				modalDialog.find('#orgname').textbox("setValue", data.orgname);
				// 如果扩展表中有信息，则赋值
				var count = data.countExt;
				if (count > 0) {
					modalDialog.find('#phone').textbox('setValue', data.phone);
					modalDialog.find('#weixin').textbox('setValue', data.weixin);
					modalDialog.find('#qq').textbox('setValue', data.qq);
					modalDialog.find('#email').textbox('setValue', data.email);
				}
				modalDialog.find('#createtime').textbox('setValue', data.createtime);
				modalDialog.find('#overduedate').textbox('setValue', data.overduedate);
				modalDialog.find('#remark').textbox('setValue', data.remark);
			}, "json");// 获取手机、QQ、邮箱、微信进行格式校验
		},
		buttons: [{
				   text : "确定",
				   iconCls : "icon-ok",
					handler : function() {
						var form = $.fastModalDialog.handler['personalinfodlg'].find('#_personalinfo_edit_form');
						if (form.form('validate')) {
							// 发起请求
							form.form('submit',
									{url : contextpath + '/sys/SysUserInfoController/editPersonalInfo.do',
									success : function(data) {
										try {
											data = eval("("+ data+ ")");
										} catch (e) {}
										easyui_auto_notice(
											data,
											function() {
												$.fastModalDialog.handler['personalinfodlg'].dialog('close');
											},
											function() {},
											'修改过程中发生异常！');}
								});

						} else {
							easyui_warn('请填写必填项后再提交！');
						}
					}
				},{
					text : "取消",
					iconCls : "icon-cancel",
					handler : function() {
						$.fastModalDialog.handler['personalinfodlg']
								.dialog('close');
					}
				}]
	});
}

$.modalDialog = function(options) {
	if ($.modalDialog.handler == undefined) {// 避免重复弹出
		var opts = $.extend({
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