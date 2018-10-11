package com.jbf.sys.systemConfiguration;

import com.jbf.common.util.StringUtil;


public class SystemCfg {

	private static boolean multiDataSourceEnabled = false; //是否启用多数据源
	private static boolean verificationcodeEnabled = false; //登录是否启用验证码验证
	private static boolean weatherEnable = false;
	
	public SystemCfg() {
		
	}
	
	public static boolean MultiDataSourceEnabled() {
		return multiDataSourceEnabled;
	}
	
	public void setMultiDataSourceEnabled(String multiDataSourceEnabled) {
		if (StringUtil.isNotBlank(multiDataSourceEnabled))
			this.multiDataSourceEnabled = Boolean.parseBoolean(multiDataSourceEnabled);
	}
	
	public static boolean VerificationcodeEnabled() {
		return verificationcodeEnabled;
	}
	
	public void setVerificationcodeEnabled(String verificationcodeEnabled) {
		if (StringUtil.isNotBlank(verificationcodeEnabled))
			this.verificationcodeEnabled = Boolean.parseBoolean(verificationcodeEnabled);
	}
	
	public static boolean WeatherEnable() {
		return weatherEnable;
	}

	public void setWeatherEnable(String weatherEnable) {
		if (StringUtil.isNotBlank(weatherEnable))
			this.weatherEnable = Boolean.parseBoolean(weatherEnable);
	}
}
