package com.jbf.sys.login.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class CAMsgService {

	/**
	 * 验证ca网管信息
	 * 
	 * @param authURL
	 * @param clientIP
	 * @param signed_data
	 * @param original_data
	 * @param appId
	 * @return
	 * @throws SendMsgException
	 * @throws DocumentException
	 */

	public ReceiveMsgInfo caMessageServiceCheck(String authURL,
			String clientIP, String signed_data, String original_data,
			String appId, List<String> UMSAttributes) throws 
			DocumentException, SendMsgException {
		String messagexml = getSendMsg(clientIP, signed_data, original_data,
				appId, UMSAttributes);
		//System.out.println(messagexml);
		String msg = sendAndReceive(authURL, messagexml);
		ReceiveMsgInfo info = receiveAnalysis(msg);
		return info;
	}

	/**
	 * 解析ca网管返回信息
	 * 
	 * @param respMessageXml
	 * @return
	 * @throws DocumentException
	 */
	public ReceiveMsgInfo receiveAnalysis(String respMessageXml)
			throws DocumentException {
		Document respDocument = null;
		Element headElement = null;
		Element bodyElement = null;
		String errCode = null;
		String errDesc = null;
		respDocument = DocumentHelper.parseText(respMessageXml);
//		System.out.println(respMessageXml);
		headElement = respDocument.getRootElement().element(
				MsgXMLElementName.MSG_HEAD);
		bodyElement = respDocument.getRootElement().element(
				MsgXMLElementName.MSG_BODY);
		ReceiveMsgInfo info = new ReceiveMsgInfo();
		/*** 1 解析报文头 ** 开始 **/
		if (headElement != null) {
			boolean state = Boolean
					.valueOf(
							headElement
									.elementTextTrim(MsgXMLElementName.MSG_MESSAGE_STATE))
					.booleanValue();
			if (state) {
				errCode = headElement
						.elementTextTrim(MsgXMLElementName.MSG_MESSAGE_CODE);
				errDesc = headElement
						.elementTextTrim(MsgXMLElementName.MSG_MESSAGE_DESC);
				info.setSuccess(false);

				info.setErrCode(errCode);
				info.setErrDesc(errDesc);
				return info;
			}
		}

		//System.out.println("解析报文头成功！\n");
		/* 解析报文体 */
		// 解析认证结果集
		Element authResult = bodyElement.element(
				MsgXMLElementName.MSG_AUTH_RESULT_SET).element(
				MsgXMLElementName.MSG_AUTH_RESULT);

		boolean isSuccess = Boolean.valueOf(
				authResult.attributeValue(MsgXMLElementName.MSG_SUCCESS))
				.booleanValue();
		if (!isSuccess) {
			errCode = authResult
					.elementTextTrim(MsgXMLElementName.MSG_AUTH_MESSSAGE_CODE);
			errDesc = authResult
					.elementTextTrim(MsgXMLElementName.MSG_AUTH_MESSSAGE_DESC);
			info.setSuccess(false);

			info.setErrCode(errCode);
			info.setErrDesc(errDesc);
			return info;
		}

		System.out.println("CA身份认证成功！\n");
		String ss = bodyElement.elementTextTrim("accessControlResult");
//		System.out.println(ss);

		// 解析用户属性列表
		Element attrsElement = bodyElement
				.element(MsgXMLElementName.MSG_ATTRIBUTES);
		Map attributeNodeMap = new HashMap();
		Map childAttributeNodeMap = new HashMap();
		StringBuilder keyes = null;
		if (attrsElement != null) {
			List attributeNodeList = attrsElement
					.elements(MsgXMLElementName.MSG_ATTRIBUTE);
			for (int i = 0; i < attributeNodeList.size(); i++) {
				keyes = new StringBuilder();
				Element userAttrNode = (Element) attributeNodeList.get(i);
				String msgParentName = userAttrNode
						.attributeValue(MsgXMLElementName.MSG_PARENT_NAME);
				String name = userAttrNode
						.attributeValue(MsgXMLElementName.MSG_NAME);
				String value = userAttrNode.getTextTrim();
				keyes.append(name);
				if (msgParentName != null && !msgParentName.equals("")) {
					keyes.append(",").append(msgParentName);
					childAttributeNodeMap.put(keyes.toString(), value);
				} else {
					attributeNodeMap.put(keyes.toString(), value);
				}

			}

			attributeNodeMap.putAll(childAttributeNodeMap);
//			System.out.println(attributeNodeMap);
			info.setAttributeNodeMap(attributeNodeMap);
			return info;
		} else
			return info;
	}

	/**
	 * 发送并接受ca网管信息
	 * 
	 * @param authURL
	 * @param messagexml
	 * @return
	 * @throws SendMsgException
	 */
	public String sendAndReceive(String authURL, String messagexml)
			throws SendMsgException {
		int statusCode = 500;
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		// HTTPClient对象
		httpClient = new HttpClient();
		postMethod = new PostMethod(authURL);

		// 设置报文传送的编码格式
		postMethod.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");

		// 执行postMethod
		try {
			/*** 2 设置发送认证请求内容 ** 开始 **/
			StringRequestEntity entity = new StringRequestEntity(messagexml,
					"text/xml", "UTF-8");
			postMethod.setRequestEntity(entity);
			/*** 2 设置发送认证请求内容 ** 结束 **/

			/*** 3 发送通讯报文与网关通讯 ** 开始 **/
			statusCode = httpClient.executeMethod(postMethod);
			/*** 3 发送通讯报文与网关通讯 ** 结束 **/
		} catch (Exception e) {
			System.out.println("与CA网关连接出现异常\n");
			throw new SendMsgException(e, String.valueOf(statusCode));

		}

		/****************************************************************
		 * 第七步：网关返回认证响应*
		 ****************************************************************/

		String respMessageXml = null;
		// 当返回200或500状态时处理业务逻辑
		if (statusCode == HttpStatus.SC_OK
				|| statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			// 从头中取出转向的地址
			try {
				/*** 4 接收通讯报文并处理 ** 开始 **/
				byte[] inputstr = postMethod.getResponseBody();

				ByteArrayInputStream ByteinputStream = new ByteArrayInputStream(
						inputstr);
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				int ch = 0;
				try {
					while ((ch = ByteinputStream.read()) != -1) {
						int upperCh = (char) ch;
						outStream.write(upperCh);
					}
				} catch (Exception e) {
					throw new SendMsgException(e, "00000");

				}

				// 200 表示返回处理成功
				if (statusCode == HttpStatus.SC_OK) {

					return new String(outStream.toByteArray(), "UTF-8");
				} else {
					// 500 表示返回失败，发生异常
					throw new SendMsgException(new String(outStream
							.toByteArray(), "UTF-8"), String
							.valueOf(statusCode));
				}

				/*** 4 接收通讯报文并处理 ** 结束 **/
			} catch (IOException e) {
				throw new SendMsgException(e, String.valueOf(statusCode));
			}
		} else
			throw new SendMsgException("未找到", String.valueOf(statusCode));

	}

	/**
	 * 获得发送xml信息
	 * 
	 * @param clientIP
	 * @param signed_data
	 * @param original_data
	 * @param appId
	 * @return
	 */
	String getSendMsg(String clientIP, String signed_data,
			String original_data, String appId, List<String> UMSAttributes) {
		Document reqDocument = DocumentHelper.createDocument();
		Element root = reqDocument.addElement(MsgXMLElementName.MSG_ROOT);
		Element requestHeadElement = root
				.addElement(MsgXMLElementName.MSG_HEAD);
		Element requestBodyElement = root
				.addElement(MsgXMLElementName.MSG_BODY);
		/* 组装报文头信息 */
		requestHeadElement.addElement(MsgXMLElementName.MSG_VSERSION).setText(
				MsgXMLElementName.MSG_VSERSION_VALUE);
		requestHeadElement.addElement(MsgXMLElementName.MSG_SERVICE_TYPE)
				.setText(MsgXMLElementName.MSG_SERVICE_TYPE_VALUE);

		/* 组装报文体信息 */

		// 组装客户端信息
		Element clientInfoElement = requestBodyElement
				.addElement(MsgXMLElementName.MSG_CLIENT_INFO);

		Element clientIPElement = clientInfoElement
				.addElement(MsgXMLElementName.MSG_CLIENT_IP);

		clientIPElement.setText(clientIP);

		// 组装应用标识信息
		requestBodyElement.addElement(MsgXMLElementName.MSG_APPID).setText(
				appId);

		Element authenElement = requestBodyElement
				.addElement(MsgXMLElementName.MSG_AUTH);

		Element authCredentialElement = authenElement
				.addElement(MsgXMLElementName.MSG_AUTHCREDENTIAL);

		// 组装证书认证信息
		authCredentialElement.addAttribute(MsgXMLElementName.MSG_AUTH_MODE,
				MsgXMLElementName.MSG_AUTH_MODE_CERT_VALUE);

		authCredentialElement.addElement(MsgXMLElementName.MSG_DETACH).setText(
				signed_data);
		authCredentialElement.addElement(MsgXMLElementName.MSG_ORIGINAL)
				.setText(original_data);

		// 支持X509证书 认证方式
		// 获取到的证书
		// javax.security.cert.X509Certificate x509Certificate = null;
		// certInfo 为base64编码证书
		// 可以使用
		// "certInfo =new BASE64Encoder().encode(x509Certificate.getEncoded());"
		// 进行编码
		// authCredentialElement.addElement(MSG_CERT_INFO).setText(certInfo);

		requestBodyElement.addElement(MsgXMLElementName.MSG_ACCESS_CONTROL)
				.setText(MsgXMLElementName.MSG_ACCESS_CONTROL_TRUE);

		// 组装口令认证信息
		// username = request.getParameter( "" );//获取认证页面传递过来的用户名/口令
		// password = request.getParameter( "" );
		// authCredentialElement.addAttribute(MSG_AUTH_MODE,MSG_AUTH_MODE_PASSWORD_VALUE
		// );
		// authCredentialElement.addElement( MSG_USERNAME ).setText(username);
		// authCredentialElement.addElement( MSG_PASSWORD ).setText(password);

		// 组装属性查询列表信息
		Element attributesElement = requestBodyElement
				.addElement(MsgXMLElementName.MSG_ATTRIBUTES);

		attributesElement.addAttribute(MsgXMLElementName.MSG_ATTRIBUTE_TYPE,
				MsgXMLElementName.MSG_ATTRIBUTE_TYPE_PORTION);

		// TODO 取公共信息
		addAttribute(attributesElement, "X509Certificate.SubjectDN",
				"http://www.jit.com.cn/cinas/ias/ns/saml/saml11/X.509");
		for (String string : UMSAttributes) {
			addAttribute(attributesElement, string,
					"http://www.jit.com.cn/ums/ns/user");

		}

		/*** 2 将认证请求报文写入输出流 ** 开始 **/

		// writer = new XMLWriter(outStream);
		// writer.write(reqDocument);
		// System.out.println(reqDocument.asXML());
		return reqDocument.asXML();

	}

	/**
	 * 向xml插入结点
	 */
	private void addAttribute(Element attributesElement, String name,
			String namespace) {
		Element attr = attributesElement
				.addElement(MsgXMLElementName.MSG_ATTRIBUTE);
		attr.addAttribute(MsgXMLElementName.MSG_NAME, name);
		attr.addAttribute(MsgXMLElementName.MSG_NAMESPACE, namespace);
	}
}
