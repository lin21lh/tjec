/**
 * 登录相关操作的脚本处理
 */
$(function(){
	
	//注册Enter回车事件
	$("form input").keydown(function(e){
		if(e.keyCode == 13){
			submitButton();
		}
	});
	
});



function submitButton() {
	if (document.loginForm.username.value == '') {

		showMessage("用户名输入为空，请输入用户名！");
		document.loginForm.username.focus();
		return;
	} 
	if (document.loginForm.password.value == '') {
		alert("密码输入为空，请输入密码！");
		document.loginForm.password.focus();
		return;
	}
	if (verificationcodeEnabled == true) {
		if (document.loginForm.verifyCode.value == '') {
			alert("请输入验证码！");
			document.loginForm.verifyCode.focus();
			return;
		}
	}
	document.loginForm.submit();
}

//更换验证码
function changeVerifyCode() {
	//2、如果用<img>实现，则修改<img src=url>的url
	//这里有一个小技巧，如果给url赋相同的值，浏览器不会重新发出请求，因此用js生成一个即时毫秒数做url中的参数
	t = new Date().getTime();
	document.loginForm.verifyCodeImg.src = "exclude/verfiyCode.do?t=" + t;
	document.loginForm.verifyCode.focus();
}

function pluginsDownload(){
	
	var iWidth = 200;
    var iHeight = 400;
    var iTop = (window.screen.availHeight - iHeight ) / 2;
    var iLeft = (window.screen.availWidth - iWidth) / 2  - 200;
	
//	window.showModalDialog ("pluginsDownload.html",{},"dialogWidth=400px;dialogHeight=250px;status=no");
	 window.open ("pluginsDownload.html",'newwindow','height=200,width=400,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
}
