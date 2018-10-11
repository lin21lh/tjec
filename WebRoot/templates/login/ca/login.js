window.document.onkeydown = disableEnterKey;

var allowLogin = true;

var oldkeynum = 0; //记录旧key状态 ,是否插入key

var lrfs_loginmode = 'stdlogin'; //登陆状态

var CAOrginal_TEXT='';

$(function() {
	CheckATLCOM();
	loopingCheck();

	//setInterval(loopingCheck, 2000);
});

var xmode = true;
//检查key是否插入
function loopingCheck() {

	var keynum = getUsbkeyNumber();
	if (keynum == oldkeynum) {
		return;
	}

	oldkeynum = keynum;
	$('#CERTCN').val('');
	$('#CERTCN').val(getCertCn());
	if (keynum == 0) {
		changeLoginMode('stdlogin');
	} else {
		changeLoginMode('calogin');
	}

}

function disableEnterKey() {
	if (event.keyCode == 13) {
		if (document.loginForm.username.value == '') {
			showMessage("请输入用户名！");
			document.loginForm.username.focus();
			return;
		}
		if (document.loginForm.password.value == '') {
			showMessage("请输入密码！");
			document.loginForm.password.focus();
			return;
		}
		submitButton('LOGIN');
	}
}

function generateCAOrginal() {

	var num = "1234567890abcdefghijklmnopqrstopqrstuvwxyz";
	var size = 6;
	var res = '';
	for ( var i = 0; i < size; i++) {
		res+= num.charAt((Math.random() * 10000)%num.length);
	}
	return res;
}

function onLoad() {
	if (document.loginForm.username.value == "") {
		document.loginForm.username.focus();
	} else {
		document.loginForm.password.focus();
	}
}

function showMessage(msg) {
	msg_div.innerText = msg;
}
function submitButton() {
	if (lrfs_loginmode == 'stdlogin') {
		if (document.loginForm.username.value == '') {
			showMessage("用户名输入为空，请输入用户名！");
			document.loginForm.username.focus();
			return;
		} else {
			if (!loginCheck(document.loginForm.username.value)) {
				showMessage("用户名输入错误，请输入用户名！");
				document.loginForm.username.focus();
				return;
			}
		}
		if (document.loginForm.password.value == '') {
			showMessage("密码输入为空，请输入密码");
			document.loginForm.password.focus();
			return;
		}
		document.loginForm.action = "j_spring_security_login.do";
		document.loginForm.submit();
	} else {
		//CA验证登陆
		var passwd=$('#usbkeypswd').val();
		if(passwd==''||passwd==undefined){
			alert('请输入UKey密码!');
			return;
		}
		
		if (!hasCert()) {
			alert("UKEY中没有数字证书!");
			return;
		}
		
		$('#CERTCN').val(getCertCn());
		if (!VerifyPin()) {
			$('#usbkeypswd').val('');
			$('#usbkeypswd').focus();
			return;
		}
		CAOrginal_TEXT=generateCAOrginal();
		if(doDataProcess(CAOrginal_TEXT)){
			ca_login();
		}
		
	}
}
function ca_login(){
	var param={};
	param.signeddata = $('#signed_data').val()
	param.originaldata = $('#original_data').val()
	param.certcn = encodeURIComponent($('#CERTCN').val());
	$.post(contextpath+'caLoginValidation.do',param,function(r){
		if(r!=undefined&&r.success==true){
			document.loginForm.username.value=r.message;
			document.loginForm.signed_data.value=r.info;
			document.loginForm.calogin.value='true';
			document.loginForm.submit();
		}else{
			alert(r.info);
		}
		
	},'json');
}
function changeLoginMode(mode) {
	if (mode == "calogin") {
		$('#calogin')[0].style.display = "";
		$('#stdlogin')[0].style.display = "none";
		lrfs_loginmode = 'calogin';
	} else {
		$('#calogin')[0].style.display = "none";
		$('#stdlogin')[0].style.display = "";
		lrfs_loginmode = 'stdlogin';
	}
}
function pluginsDownload(){
	var iWidth = 200;
    var iHeight = 400;
    var iTop = (window.screen.availHeight - iHeight ) / 2;
    var iLeft = (window.screen.availWidth - iWidth) / 2  - 200;
	window.open ("pluginsDownload.html",'newwindow','height=200,width=400,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
}
