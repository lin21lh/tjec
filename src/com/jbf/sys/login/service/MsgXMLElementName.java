package com.jbf.sys.login.service;

public class MsgXMLElementName {
	/******************************* 报文公共部分 ****************************/
	/** 报文根结点 */
	public static final String MSG_ROOT = "message";

	/** 报文头结点 */
	public static final String MSG_HEAD = "head";

	/** 报文体结点 */
	public static final String MSG_BODY = "body";

	/** 服务版本号 */
	public static final String MSG_VSERSION = "version";

	/** 服务版本值 */
	public static final String MSG_VSERSION_VALUE = "1.0";

	/** 服务类型 */
	public static final String MSG_SERVICE_TYPE = "serviceType";

	/** 服务类型值 */
	public static final String MSG_SERVICE_TYPE_VALUE = "AuthenService";

	/** 报文体 认证方式 */
	public static final String MSG_AUTH_MODE = "authMode";

	/** 报文体 证书认证方式 */
	public static final String MSG_AUTH_MODE_CERT_VALUE = "cert";

	/** 报文体 口令认证方式 */
	public static final String MSG_AUTH_MODE_PASSWORD_VALUE = "password";

	/** 报文体 属性集 */
	public static final String MSG_ATTRIBUTES = "attributes";

	/** 报文体 属性 */
	public static final String MSG_ATTRIBUTE = "attr";

	/** 报文体 属性名 */
	public static final String MSG_NAME = "name";

	/** 报文父级节点 */
	// --hegd
	public static final String MSG_PARENT_NAME = "parentName";

	/** 报文体 属性空间 */
	public static final String MSG_NAMESPACE = "namespace";
	/*********************************************************************/

	/******************************* 请求报文 ****************************/
	/** 报文体 应用ID */
	public static final String MSG_APPID = "appId";

	/** 访问控制 */
	public static final String MSG_ACCESS_CONTROL = "accessControl";

	public static final String MSG_ACCESS_CONTROL_TRUE = "true";

	public static final String MSG_ACCESS_CONTROL_FALSE = "false";

	/** 报文体 认证结点 */
	public static final String MSG_AUTH = "authen";

	/** 报文体 认证凭据 */
	public static final String MSG_AUTHCREDENTIAL = "authCredential";

	/** 报文体 客户端结点 */
	public static final String MSG_CLIENT_INFO = "clientInfo";

	/** 报文体 公钥证书 */
	public static final String MSG_CERT_INFO = "certInfo";

	/** 报文体 客户端结点 */
	public static final String MSG_CLIENT_IP = "clientIP";

	/** 报文体 detach认证请求包 */
	public static final String MSG_DETACH = "detach";

	/** 报文体 原文 */
	public static final String MSG_ORIGINAL = "original";

	/** 报文体 用户名 */
	public static final String MSG_USERNAME = "username";

	/** 报文体 口令 */
	public static final String MSG_PASSWORD = "password";

	/** 报文体 属性类型 */
	public static final String MSG_ATTRIBUTE_TYPE = "attributeType";

	/** 指定属性 portion */
	public static final String MSG_ATTRIBUTE_TYPE_PORTION = "portion";

	/** 指定属性 all */
	public static final String MSG_ATTRIBUTE_TYPE_ALL = "all";
	/*********************************************************************/

	/******************************* 响应报文 ****************************/
	/** 报文体 认证结果集状态 */
	public static final String MSG_MESSAGE_STATE = "messageState";

	/** 响应报文消息码 */
	public static final String MSG_MESSAGE_CODE = "messageCode";

	/** 响应报文消息描述 */
	public static final String MSG_MESSAGE_DESC = "messageDesc";

	/** 报文体 认证结果集 */
	public static final String MSG_AUTH_RESULT_SET = "authResultSet";

	/** 报文体 认证结果 */
	public static final String MSG_AUTH_RESULT = "authResult";

	/** 报文体 认证结果状态 */
	public static final String MSG_SUCCESS = "success";

	/** 报文体 认证错误码 */
	public static final String MSG_AUTH_MESSSAGE_CODE = "authMessageCode";

	/** 报文体 认证错误描述 */
	public static final String MSG_AUTH_MESSSAGE_DESC = "authMessageDesc";



}
