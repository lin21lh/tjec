$(function(){
	var f = '<iframe width="610" scrolling="no"  frameborder="0" allowtransparency="true" src="http://i.tianqi.com/index.php?c=code&id=38&icon=1&num=1"></iframe>';
	if(weatherEnable=="true" || weatherEnable==true){
		$("#weather").append(f);
	}
});
/*function bigger(id){
	console.log("id="+id);
	var img = document.getElementById(id);
	img.width=img.width*1.1;
	img.height=img.height*1.1;
}
function smaller(id){
	var img = document.getElementById(id);
	img.width=img.width/1.1;
	img.height=img.height/1.1;
}
function rec(obj){
	console.log(obj.title);
}
function abc(obj){
	console.log(obj.title);
	
}*/
/*var changeStyle = "model";
window.onload=function(){
	change();
}*/
//风格切换
function change(){
	/*if(changeStyle=="model"){
		changeStyle  = "circle";
		var rt=new imgRound("center",150,150,690,160,400,0.006);
		setInterval(function(){rt.roundMove()},20);
		return;
	}
	if(changeStyle=="circle"){
		changeStyle  = "model";*/
		//location.replace("../WebRoot/templates/login/welcome_circle.vm");
		/*return;
	}*/
}
function imgRound(id,w,h,x,y,r,dv,rh,ah){
	if (ah==undefined) ah=1;
	if (rh==undefined) rh=10;
	var dv=dv*ah; //旋转速度
	var pi=3.1415926575;
	var d=pi/2;
	var pd=Math.asin(w/2/r);
	var smove=true;
	var imgArr=new Array();
	var objectId=id;
	var o=document.getElementById(objectId);
	o.style.position="relative";
	var arrimg=o.getElementsByTagName("img");
	var pn=arrimg.length; //图片数量
	var ed=pi*2/pn;
	for (n=0;n<arrimg.length;n++){
		var lk=arrimg[n].getAttribute("link");
		if (lk!=null) arrimg[n].setAttribute("title",lk);
		//定义点击事件
		/*arrimg[n].onclick=function(){
			if (this.getAttribute("link")!=null){
				if (this.getAttribute("target")!="_blank") window.location=(this.getAttribute("link"))
				else window.open(this.getAttribute("link"))
			}
		}*/
		arrimg[n].onmouseout=function(){smove=true;}
		arrimg[n].onmouseover=function(){smove=false;}
		arrimg[n].style.position="absolute";
		imgArr.push(arrimg[n]);
	}
	this.roundMove=function(){
		for (n=0;n<=pn-1;n++){
			var o=imgArr[n];
			var ta=Math.sin(d+ed*n),strFilter;
			if (ta<0) o.style.left=Math.cos(d+ed*n-pd)*r+x+"px";
			else o.style.left=Math.cos(d+ed*n+pd)*r+x+"px";
			o.style.top=ta*rh+rh+y+"px";
			var zoom=Math.abs(Math.sin((d+ed*n)/2+pi/4))*0.5+0.5;
			o.style.width=Math.abs(Math.cos(d+ed*n+pd)-Math.cos(d+ed*n-pd))*zoom*r+"px";
			o.style.height=zoom*h+"px";
			if (ta<0) {ta=(ta+1)*80+20;o.style.zIndex=0;}
			else {ta=100;o.style.zIndex=1}
			if (o.style.zIndex<=0) strFilter="FlipH(enabled:true)"
			else strFilter="FlipH(enabled:false)";
			strFilter=strFilter+" alpha(opacity="+ta+")";
			o.style.opacity=ta/100;
			o.style.filter=strFilter;
		}
		if (smove) d=d+dv;
	}
}

function logoutSystem() {
	hideCornerMenusItems();
	$.messager.confirm('确认', '是否注销用户?', function(r) {
		if (r) {
			window.location.href = contextpath + 'j_spring_security_logout.do';
		}
	});
}

function editPassword() {
	hideCornerMenusItems();

	$
			.fastModalDialog( {
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

/**
 * 隐藏右上角的按钮弹出层
 */
function hideCornerMenusItems() {
	$('#corner_menu_items').hide();
}

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