//根据原文和证书产生认证数据包
function doDataProcess(Auth_Content) {
	if (Auth_Content == "") {
		alert("认证原文不能为空!");
		return false;
	}

	// 控制证书为一个时，不弹出证书选择框

	var JITDSignOcx=document.getElementById("JITDSignOcx");
	JITDSignOcx.SetCertChooseType(1);
	JITDSignOcx.SetCert("SC", "", "", "", "", "");
	if (JITDSignOcx.GetErrorCode() != 0) {
		alert("错误码：" + JITDSignOcx.GetErrorCode() + "　错误信息："
				+ JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
		return false;
	} else {
		var temp_DSign_Result = JITDSignOcx.DetachSignStr("", Auth_Content);
		if (JITDSignOcx.GetErrorCode() != 0) {
			alert("错误码：" + JITDSignOcx.GetErrorCode() + "　错误信息："
					+ JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
			return false;
		}
		document.getElementById("signed_data").value = temp_DSign_Result;
		document.getElementById("original_data").value = Auth_Content;
	}

	return true;
}

// CA登陆(ExtJS)
function loginCA(myform, passwordName) {
	if (!passwordName) {
		passwordName = "j_password";
	}
	if (myform.getForm().findField(passwordName).getValue() == '') {
		myform.getForm().findField(passwordName).focus();
		alert("请输入您的密码");
		return;
	}
	if (!hasCert()) {
		alert("UKEY中没是有数字证书!");
		return false;
	}
	myform.getForm().findField("CERTCN").setValue(getCertCn());
	if (!VerifyPin()) {
		myform.getForm().findField(passwordName).setValue("");
		myform.getForm().findField(passwordName).focus();
		return;
	}

	Ext
			.onReady(function() {
				Ext.lib.Ajax
						.request(
								'POST',
								'../login.do?action=getCAOriginal',
								{
									success : function(response) {
										var Auth_Content = response.responseText;// 得到CA认证原文

										if (doDataProcess(Auth_Content)) {
											myform.getForm().el.dom.action = "../login.do?action=doCaLogin";
											myform.getForm().el.dom.submit();
										}
									},
									failure : function(form, action) {
									}
								}, null);

			})
}