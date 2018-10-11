	var isList = 0;
	var isSelectCert = 0;
	var FTCtrl;
//	function CheckATLCOM()
//	{
//		// 不通过声明object定义来加载控件，防止出现没有安装控件的时候，IE浏览器从微软的服务器去搜索文件，从而导致IE加载网银系统的时候的延时
//		try
//		{
//			FTCtrl = new ActiveXObject("ET199AX.ET199AD.1");
////			 alert("USBKey控件加载正常");
//		}
//		catch (e)
//		{
////			 alert("没有检测到USBKey控件，请重新安装USBKey软件");
//		}
//			
//	}


	function CheckSoft()
	{
		/*
		 * CheckUsbkeySoft 功能：检测是否安装了usbkey的软件 返回结果：0表示已经安装，其余为未安装
		 */
		var ret = FTCtrl.CheckUsbkeySoft;
		if(ret == 0)
		{
			alert("已经安装usbkey软件");
		}
		else
		{
			alert("未安装usbkey软件");
		}
	}

	function VerifyPinDialog()
	{
		/*
		 * VerifyPinByWnd 功能：验证用户pin 如果有多只usbkey，会弹出设备选择对话框，再让用户输入pin码 返回结果：
		 * 0：成功 0x1003：usbkey已经被拔出 -2：用户取消了操作 -1：usbkey的用户pin被锁死
		 * 1-9：也是失败，表示用户pin的剩余可重试次数
		 */
		var ret = FTCtrl.VerifyPinByWnd;
		if(ret == 0)
		{
			alert("验证userpin成功");
			/*
			 * GetSelectedSN 功能：获取验证pin成功后，返回用户所操作的usbkey的序列号
			 * 输入为要取得第几个usbkey的序列号，0表示取第一个
			 */
			var ukeySn = FTCtrl.GetSelectedSN(0);
			alert("您说操作的usbkey的序列号为："+ukeySn);

		}
		else if(ret == 0x1003)
		{
			alert("usbkey已经被拔出");
		}
		else if(ret == -2)
		{
			alert("用户取消了验证pin的操作");
		}
		else if(ret == 0x2016)
		{
			alert("USBKey中没有证书");
		}
		else if(ret == 0x1000)
		{
			alert("其他错误");
		}
				
	}
	
	// 得到插入的Usbkey数量
	function getUsbkeyNumber(){
		if(!FTCtrl){
			return 0;
		}
		
		return FTCtrl.ListUsbkeyNumber;
	}
	
	// 得到插入的Usbkey数量
	function hasUsbkey(){
		var ukeyCount = getUsbkeyNumber();// ukey数
		if(ukeyCount == 0) {
			return false;
		}
		
		return true;
	}
	
	// 枚举所有USBKEY的数量和硬件序列号
	function ListToken()
	{
		var i = 0;
		// 先删除select所有option
		var j = selSN.length;
		 for(i=j-1; i>=0; i--)
		{
			 selSN.remove(i);
		}
			/*
			 * ListUsbkeyNumber 功能：返回所有已经插入电脑的USB Key的个数
			 * 
			 * 返回结果: 0-10：表示已经连接到电脑的USB Key数量
			 */
		var tokenCount = FTCtrl.ListUsbkeyNumber;
		if(tokenCount == 0)
		{
			isList = 0;
			alert("未检测到USB Key，请确认已经插入USBKEY");
			return;
		}
		
		
	
			isList = 1;
			var tempStr;
			for(i=0; i< tokenCount;i++)
			{
				tempStr = FTCtrl.GetSelectedSN(i);
				// alert(tempStr);
				selSN.options[selSN.options.length] = new Option(tempStr, tempStr);
			}
	}
		// 初始化USB Key，会清空USB K的所有数据
		function Init()
		{
			if(isList == 0)
			{
				alert("请先枚举设备，并确认已经有USB Key插入.");
				return;
			}
				/*
				 * InitUsbkey 功能：初始化USBKEY，清空所有数据，并重新设置USBKEY的用户密码 参数1：硬件序列号
				 * 
				 * 返回结果： 0：表示成功，其他为错误码 初始化后，userpin恢复为：88888888
				 */
			var ret = FTCtrl.InitUsbkey(selSN.options[selSN.selectedIndex].value);
			if(ret == 0)
			{
				alert("初始化USBKEY成功");
				return;
			}
			else
				alert("初始化USBKEY失败");
		}

		// UKEY中是否有数字证书
		function hasCert(){
			var certNum = FTCtrl.GetAllCertNum;
			return certNum == 0 ? false : true;
		}
		
		// 得到证书CN
		function getCertCn(i){
			if(!i){
				i = 0;
			}
			
			return FTCtrl.GetCertCn(i);
		}

		function EnumCert()
		{
			var i = 0;
			var j = selCert.length;
			for(i=j-1; i>=0; i--)
			{
				 selCert.remove(i);
			}

			// 枚举所有已经插入的ePass2001里的证书信息
			
			/*
			 * GetAllCertNum 功能：返回所有已插入的ePass2001的数字证书数量
			 */
			var certNum = FTCtrl.GetAllCertNum;
			if(certNum == 0)
			{
				alert("未检测到数字证书，请确认已经插入包含数字证书的USBKEY");
				return;
			}

			/*
			 * GetAllCertCn 功能：返回所有USB Key里证书的CN字段信息
			 * 
			 */
			
			isSelectCert = 1;
			var tempStr;
			for(i=0; i< certNum;i++)
			{
				tempStr = FTCtrl.GetCertCn(i);
				selCert.options[selCert.options.length] = new Option(tempStr, tempStr);
			}
			
		}

		
		function GetCertInfo()
		{
			if(isSelectCert == 0)
			{
				alert("请先枚举数字证书");
				return;
			}
			
			var strCn = selCert.options[selCert.selectedIndex].value;
			
			// 获取指定证书的开始日期
			var startDate = FTCtrl.GetCertStarteTime(strCn);
			alert("证书有效期开始日期："+startDate);

			
			// 获取指定证书的结束日期
			var endDate = FTCtrl.GetCertEndTime(strCn);
			alert("证书有效期结束日期："+endDate);

			// 获取证书序列号
			var strSN = FTCtrl.GetCertSN(strCn);
			alert("证书序列号："+strSN);
		}

		function IsHaveSpecCert()
		{
			var cnLen = CERTCN.value.length;
			if(cnLen < 1)
			{
				alert("请输入证书的CN字段内容");
				CERTCN.focus();
				return;
			}

			/*
			 * IsHaveSpecCert 功能：判断是否包含特定CN字段的证书 返回值：0表示有改证书，其他为错误吗
			 * 
			 */
			var ret = FTCtrl.IsHaveSpecCert(CERTCN.value);
			if(ret ==0)
			{
					alert("包括指定的数字证书");
			}
			else
			{
				alert("不包括指定的数字证书");
			}
			
		}

// 验证密码
 function VerifyPin() {
// var cnLen = CERTCN.value.length;
// if(cnLen < 1)
// {
// alert("请输入证书的CN字段内容");
// CERTCN.focus();
// return;
// }

			var oldpinLength = document.getElementById("usbkeypswd").value.length;
				
			/*
			 * VeryfyUSerpiByCN 功能：验证密码 参数1：证书的CN内容 参数2：密码内容
			 * 返回值：0表示成功，如果不成功，可以调用GetPinInfoByCN去获取密码的当前重试次数
			 */
			var ret = FTCtrl.VerifyUserpinByCN(document.getElementById("CERTCN").value,document.getElementById("usbkeypswd").value);
			// alert(ret);
			if(ret !=0)
			{
				if(ret == 0x2009)
				{
					alert("没有找到该CN的证书");
					return false;
				}
				else if(ret == 0x1004)
				{
					// alert("验证用户密码失败");
					ret = FTCtrl.GetPinInfoByCN(document.getElementById("CERTCN").value);
					if(ret == 0x2009)
					{
						alert("没有找到该CN的证书");
						return false;
					}
					else
					{
						if(ret == 0){
							alert("验证用户密码失败,UKey密码已经锁定");
						} else {
							alert("验证用户密码失败,密码可重试次数为："+ret +" 次");
						}
						return false;
					}
				}
				else 
				{
					// alert("其他错误");
				}				
					
			}
			
	return true;
}

// 验证密码
 function VerifyPin2(password) {
// var cnLen = CERTCN.value.length;
// if(cnLen < 1)
// {
// alert("请输入证书的CN字段内容");
// CERTCN.focus();
// return;
// }

			var certCn = getCertCn();
				
			/*
			 * VeryfyUSerpiByCN 功能：验证密码 参数1：证书的CN内容 参数2：密码内容
			 * 返回值：0表示成功，如果不成功，可以调用GetPinInfoByCN去获取密码的当前重试次数
			 */
			var ret = FTCtrl.VerifyUserpinByCN(certCn,password);
			// alert(ret);
			if(ret !=0)
			{
				if(ret == 0x2009)
				{
					alert("没有找到该CN的证书");
					return false;
				}
				else if(ret == 0x1004)
				{
					// alert("验证用户密码失败");
					ret = FTCtrl.GetPinInfoByCN(certCn);
					if(ret == 0x2009)
					{
						alert("没有找到该CN的证书");
						return false;
					}
					else
					{
						if(ret == 0){
							alert("验证用户密码失败,UKey密码已经锁定");
						} else {
							alert("验证用户密码失败,密码可重试次数为："+ret +" 次");
						}
						return false;
					}
				}
				else 
				{
					// alert("其他错误");
				}				
					
			}
			
	return true;
}
//加载JITDSign控件
 
 function loadJITDSign(elm){
  if (!document.getElementById(elm)) return;
  var str = '';
  str += '<object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488" id="JITDSignOcx" width="0" codebase="./JITDSign.cab#version=2,0,24,28">';
  str += '</object>';
  document.getElementById(elm).innerHTML = str;
 }
 //加载et199控件
 function loadEt199(elm){  
  if (!document.getElementById(elm)) return;
  var str = '';
  str += '<object classid="clsid:6352F891-137D-486E-955C-8054C314EA0D" id="et199" width="0" codebase="./et199.CAB#version=1,0,0,1">';
  str += '</object>';
  document.getElementById(elm).innerHTML = str;
 }
 
 function CheckATLCOM()
	{
		//不通过声明object定义来加载控件，防止出现没有安装控件的时候，IE浏览器从微软的服务器去搜索文件，从而导致IE加载网银系统的时候的延时
		try
		{
			//alert('加载控件');
			FTCtrl = new ActiveXObject("ET199AX.ET199AD.1");
//			alert(FTCtrl.GetAllCertNum);
//			alert("USBKey控件加载正常");
		}
		catch (e)
		{
			alert("没有检测到USBKey控件，请下载并安装USBKey控件，然后关闭浏览器，重新登陆系统。");
			window.location.href="et199a.zip";
		}
			
	}
 