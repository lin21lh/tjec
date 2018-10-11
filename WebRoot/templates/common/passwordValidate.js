/**
 * 密码强度校验
 * @param password
 */
function AuthPasswd(password) {
    if(password.length >=6) {
        if(/[a-zA-Z]+/.test(password) && /[0-9]+/.test(password) && /\W+\D+/.test(password)) {
            noticeAssign(1);
        }else if(/[a-zA-Z]+/.test(password) || /[0-9]+/.test(password) || /\W+\D+/.test(password)) {
            if(/[a-zA-Z]+/.test(password) && /[0-9]+/.test(password)) {
                noticeAssign(-1);
            }else if(/\[a-zA-Z]+/.test(password) && /\W+\D+/.test(password)) {
                noticeAssign(-1);
            }else if(/[0-9]+/.test(password) && /\W+\D+/.test(password)) {
                noticeAssign(-1);
            }else{
                noticeAssign(0);
            }
        }
    }else{
        noticeAssign(null); 
    }
}
 
function noticeAssign(num) {
    if(num == 1) {
    	$.fastModalDialog.handler['pswddlg'].find('#weak').css({backgroundColor:'#009900',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#middle').css({backgroundColor:'#009900',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#strength').css({backgroundColor:'#009900',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#strength').html('很强');
        $.fastModalDialog.handler['pswddlg'].find('#middle').html('');
        $.fastModalDialog.handler['pswddlg'].find('#weak').html('');
    }else if(num == -1){
        $.fastModalDialog.handler['pswddlg'].find('#weak').css({backgroundColor:'#ffcc33',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#middle').css({backgroundColor:'#ffcc33',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#strength').css({backgroundColor:'',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#weak').html('');
        $.fastModalDialog.handler['pswddlg'].find('#middle').html('中');
        $.fastModalDialog.handler['pswddlg'].find('#strength').html('');
    }else if(num ==0) {
        $.fastModalDialog.handler['pswddlg'].find('#weak').css({backgroundColor:'#dd0000',fontSize:'10px'});
        $.fastModalDialog.handler['pswddlg'].find('#middle').css({backgroundColor:''});
        $.fastModalDialog.handler['pswddlg'].find('#strength').css({backgroundColor:''});
        $.fastModalDialog.handler['pswddlg'].find('#weak').html('弱');
        $.fastModalDialog.handler['pswddlg'].find('#middle').html('');
        $.fastModalDialog.handler['pswddlg'].find('#strength').html('');
    }else{
        $.fastModalDialog.handler['pswddlg'].find('#weak').html('&nbsp;');
        $.fastModalDialog.handler['pswddlg'].find('#middle').html('&nbsp;');
        $.fastModalDialog.handler['pswddlg'].find('#strength').html('&nbsp;');
        $.fastModalDialog.handler['pswddlg'].find('#weak').css({backgroundColor:''});
        $.fastModalDialog.handler['pswddlg'].find('#middle').css({backgroundColor:''});
        $.fastModalDialog.handler['pswddlg'].find('#strength').css({backgroundColor:''});
    }
}