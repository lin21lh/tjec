package com.jbf.sys.login.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.dom4j.DocumentException;



public class CaUserIdentityService {
	private CAMsgService ca = new CAMsgService();
	private String authURL = "http://localhost:6180/MessageService";
	private String appId = "wfpt";
	private String userCodeAttributesName = "app.usercode";
	private String divcode = "";// 区位码
	private List<String> UMSAttributes = new LinkedList<String>();

	// 读取配置文件
	public Properties getProperties() {
		Properties properties = new Properties();
		try {
			String path = this.getClass().getClassLoader().getResource("/")
					.getPath();
			// System.out.println(path);

			path = path.replaceAll("classes", "") + "caConfig.properties";
			File file = new File(path);
//			System.out.println(file);
			if (file.isFile())
				properties.load(new FileInputStream(file));

		} catch (IOException e) {
		}
		return properties;
	}

	public CaUserIdentityService() {
		Properties properties = getProperties();
		String authURL = properties.getProperty("authURL");
		if (authURL != null)
			this.authURL = authURL;
		String appId = properties.getProperty("appId");
		if (appId != null)
			this.appId = appId;
		String userCodeAttributesName = properties
				.getProperty("userCodeAttributesName");
		String divcode = properties.getProperty("divcode");
		if (userCodeAttributesName != null)
			this.userCodeAttributesName = userCodeAttributesName;
		if (divcode != null)
			this.divcode = divcode;

		UMSAttributes = Arrays.asList(properties.getProperty("UMSAttributes")
				.split(","));

	}

	public String caLogin(String clientIP, String signed_data,
			String original_data) throws SendMsgException {
		ReceiveMsgInfo msg;
		try {
			if (original_data == null)
				throw new SendMsgException("登陆失败", "0");
			original_data = new sun.misc.BASE64Encoder().encode(original_data.getBytes());
			msg = ca.caMessageServiceCheck(authURL, clientIP, signed_data,original_data, appId, UMSAttributes);
			if (!msg.isSuccess()) {
				throw new SendMsgException("[" + msg.getErrCode() + "]"
						+ msg.getErrDesc(), "0");
			}
			String account = (String) msg.getAttributeNodeMap().get(
					userCodeAttributesName);
			if (account == null || "".equals(account))
				throw new SendMsgException("ums系统用户编号未配置", "0");

			if (account.equals("admin"))
				throw new SendMsgException(
						"对不起\"admin\"在本系统中为不可用用户,请您使用其他用户登录。", "0");

			return account;
		} catch (SendMsgException e) {
			e.printStackTrace();
			throw new SendMsgException("0", "[" + e.getStatusCode() + "]"
					+ e.getMessage());
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new SendMsgException("ca认证服务解析出错", "0");
		}

	}

}
