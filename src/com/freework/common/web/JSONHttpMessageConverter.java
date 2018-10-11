package com.freework.common.web;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.freework.base.util.unmodifiableMap.UnmodifiableKeyMap;
public class JSONHttpMessageConverter extends
		AbstractHttpMessageConverter<Object> {
	public final static int BUF_SZIE=4096;
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public static final SerializerFeature feature[]={
			SerializerFeature.QuoteFieldNames,
		    SerializerFeature.SkipTransientField,
		    SerializerFeature.WriteEnumUsingToString
	    };
	public JSONHttpMessageConverter() {  
        super(new MediaType("application", "json",DEFAULT_CHARSET));  
    }  
	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

		InputStream in = inputMessage.getBody();

		byte[] buf = new byte[512];
		for (;;) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}

			if (len > 0) {
				baos.write(buf, 0, len);
			}
		}

		byte[] bytes = baos.toByteArray();
		return JSON.parseObject(bytes, 0, bytes.length, DEFAULT_CHARSET.newDecoder(),clazz);
	}
	@Override
	protected boolean supports(Class<?> clazz) {
		return clazz!=String.class;
	}
	
	@Override
	protected void writeInternal(Object t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
//		Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
//		SerializeWriter serializeWriter=new SerializeWriter(new OutputStreamWriter(outputMessage.getBody(), charset),BUF_SZIE);
//		for (int i = 0; i < feature.length; i++) {serializeWriter.config(feature[i], true);}
//        JSONSerializer serializer = new JSONSerializer(serializeWriter);
//        serializer.write(t);
//        serializer.close();
	}
	
	
	private Charset getContentTypeCharset(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			return contentType.getCharSet();
		}
		else {
			return DEFAULT_CHARSET;
		}
	}
	
	public boolean canRead(Class<?> clazz, MediaType mediaType){
		 return false;
	 }
}
