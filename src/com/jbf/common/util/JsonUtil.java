package com.jbf.common.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

public class JsonUtil {
	
	public static final SerializerFeature[] features = { 
		SerializerFeature.WriteMapNullValue, // 输出空置字段
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
                                                  };

	/**
	 * 将对象转换为JSONString 使用双引号
	 * @param value
	 * @return
	 */
	public static String createJsonString(Object value) {
		
		String alibabaJson = JSON.toJSONString(value);
		
		return alibabaJson;
	}
	
	/**
	 * 将对象转换为JSONString 使用单引号
	 * @param value
	 * @return
	 */
	public static String createJsonUseSingleQuotes(Object value) {
		
		String singleQuotMarks = JSON.toJSONString(value, SerializerFeature.UseSingleQuotes);
		
		return singleQuotMarks;
	}
	
	/**
	 * 将JsonString转换为Map类型数据
	 * @param text JsonString字符串
	 * @return
	 */
	public static Map createMapByJsonString(String text)  throws Exception{
		
		return (Map)createObjectByJsonString(text, HashMap.class);
	}
	
	/**
	 * 将JsonString转换为指定Object对象
	 * @param text JsonString字符串
	 * @param cls 指定的Object对象
	 * @return
	 */
	public static <T> Object createObjectByJsonString(String text, Class<T> cls) throws Exception {
		Object obj = null;
		try {
			obj = JSON.parseObject(text, cls);
			
		} catch (Exception e) {
			new Exception("传入数据：" + text + "不符合JSON格式！");
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static <T> List<T> createListObjectByJson(String text, Class<T> cls) {
		List<T> list = null;
		try {
			list = JSONArray.parseArray(text, cls);
		} catch (Exception e) {
			new Exception("传入数据：" + text + "不符合JSON格式！");
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	/**
	 * 转换为JSON格式数据
	 * @param object 传入需要转换的对象(可以是实体类、List集合、Map对象等)
	 * @param features 输出格式转换
	 * @return
	 * @throws Exception
	 */
	public static String toJSONString(Object object, SerializerFeature ...features) throws Exception  {

		return toJSONString(object, null, features);
	}
	
	/**
	 * 转换为JSON格式数据
	 * @param object 传入需要转换的对象(可以是实体类、List集合、Map对象等)
	 * @param replaceValues 需要值中的字符
	 * @param features 输出格式转换
	 * @return
	 * @throws Exception
	 */
	public static String toJSONString(Object object, final char[] replaceValues, SerializerFeature ...features) throws Exception {
		
		if (replaceValues != null && replaceValues.length%2 != 0) {
			throw new Exception("替换字符串配置错误!");
		}
		SerializeWriter out = new SerializeWriter();
		
		String s;
		JSONSerializer serializer = new JSONSerializer(out);
		for (SerializerFeature feature : features) {
			serializer.config(feature, true);
		}
		serializer.getValueFilters().add(new ValueFilter() {
			
			public Object process(Object o, String s, Object value) {
				if (null != value) {
					System.err.println(replaceValues != null && value instanceof String);
					if (replaceValues != null && value instanceof String) {
						for (int n=0; n<replaceValues.length; n++) {
							if (n%2 == 1)
								continue;
							
							value = value.toString().replace(replaceValues[n], replaceValues[n+1]);
						}
					}
					return value;
				} else {
					return "";
				}
			}
		});
		
		serializer.write(object);
		s = out.toString();
		out.close();
		return s;
	}
	


	    private static int convertDepth = 8;
	    private static String timestampPattern = "yyyy-MM-dd HH:mm:ss";
	    private static String datePattern = "yyyy-MM-dd";

	    public static void setConvertDepth(int convertDepth) {
	        if (convertDepth < 2)
	            throw new IllegalArgumentException("convert depth can not less than 2.");
	        JsonUtil.convertDepth = convertDepth;
	    }

	    public static void setTimestampPattern(String timestampPattern) {
	        if (timestampPattern == null || "".equals(timestampPattern.trim()))
	            throw new IllegalArgumentException("timestampPattern can not be blank.");
	        JsonUtil.timestampPattern = timestampPattern;
	    }

	    public static void setDatePattern(String datePattern) {
	        if (datePattern == null || "".equals(datePattern.trim()))
	            throw new IllegalArgumentException("datePattern can not be blank.");
	        JsonUtil.datePattern = datePattern;
	    }

	    public static String mapToJson(Map map, int depth) {
	        if (map == null)
	            return "null";

	        StringBuilder sb = new StringBuilder();
	        boolean first = true;
	        Iterator iter = map.entrySet().iterator();

	        sb.append('{');
	        while (iter.hasNext()) {
	            if (first)
	                first = false;
	            else
	                sb.append(',');

	            Map.Entry entry = (Map.Entry) iter.next();
	            toKeyValue(String.valueOf(entry.getKey()),
	                       entry.getValue(),
	                       sb,
	                       depth);
	        }
	        sb.append('}');
	        return sb.toString();
	    }

	    private static String toKeyValue(String key,
	                                     Object value,
	                                     StringBuilder sb,
	                                     int depth) {
	        sb.append('\"');
	        if (key == null)
	            sb.append("null");
	        else
	            escape(key, sb);
	        sb.append('\"').append(':');

	        sb.append(toJson(value, depth));

	        return sb.toString();
	    }

	    public static String listToJson(List list, int depth) {
	        if (list == null)
	            return "null";

	        boolean first = true;
	        StringBuilder sb = new StringBuilder();
	        Iterator iter = list.iterator();

	        sb.append('[');
	        while (iter.hasNext()) {
	            if (first)
	                first = false;
	            else
	                sb.append(',');

	            Object value = iter.next();
	            if (value == null) {
	                sb.append("null");
	                continue;
	            }
	            sb.append(toJson(value, depth));
	        }
	        sb.append(']');
	        return sb.toString();
	    }

	    /**
	     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters
	     * (U+0000 through U+001F).
	     */
	    private static String escape(String s) {
	        if (s == null)
	            return null;
	        StringBuilder sb = new StringBuilder();
	        escape(s, sb);
	        return sb.toString();
	    }

	    private static void escape(String s, StringBuilder sb) {
	        for (int i = 0; i < s.length(); i++) {
	            char ch = s.charAt(i);
	            switch (ch) {
	            case '"':
	                sb.append("\\\"");
	                break;
	            case '\\':
	                sb.append("\\\\");
	                break;
	            case '\b':
	                sb.append("\\b");
	                break;
	            case '\f':
	                sb.append("\\f");
	                break;
	            case '\n':
	                sb.append("\\n");
	                break;
	            case '\r':
	                sb.append("\\r");
	                break;
	            case '\t':
	                sb.append("\\t");
	                break;
	            case '/':
	                sb.append("\\/");
	                break;
	            default:
	                if ((ch >= '\u0000' && ch <= '\u001F')
	                    || (ch >= '\u007F' && ch <= '\u009F')
	                    || (ch >= '\u2000' && ch <= '\u20FF')) {
	                    String str = Integer.toHexString(ch);
	                    sb.append("\\u");
	                    for (int k = 0; k < 4 - str.length(); k++) {
	                        sb.append('0');
	                    }
	                    sb.append(str.toUpperCase());
	                } else {
	                    sb.append(ch);
	                }
	            }
	        }
	    }

	    public static String toJson(Object value) {
	        return toJson(value, convertDepth);
	    }

	    public static String toJson(Object value, int depth) {
	        if (value == null || (depth--) < 0)
	            return "null";

	        if (value instanceof String)
	            return "\"" + escape((String) value) + "\"";

	        if (value instanceof Double) {
	            if (((Double) value).isInfinite() || ((Double) value).isNaN())
	                return "null";
	            else
	                return value.toString();
	        }

	        if (value instanceof Float) {
	            if (((Float) value).isInfinite() || ((Float) value).isNaN())
	                return "null";
	            else
	                return value.toString();
	        }

	        if (value instanceof Number)
	            return value.toString();

	        if (value instanceof Boolean)
	            return value.toString();

	        if (value instanceof java.util.Date) {
	            if (value instanceof java.sql.Timestamp)
	                return "\""
	                       + new SimpleDateFormat(timestampPattern).format(value)
	                       + "\"";
	            if (value instanceof java.sql.Time)
	                return "\"" + value.toString() + "\"";
	            return "\""
	                   + new SimpleDateFormat(datePattern).format(value)
	                   + "\"";
	        }

	        if (value instanceof Map) {
	            return mapToJson((Map) value, depth);
	        }

	        if (value instanceof List) {
	            return listToJson((List) value, depth);
	        }

	        String result = otherToJson(value, depth);
	        if (result != null)
	            return result;

	        // 类型无法处理时当作字符串处理,否则ajax调用返回时js无法解析
	        // return value.toString();
	        return "\"" + escape(value.toString()) + "\"";
	    }

	    private static String otherToJson(Object value, int depth) {
	        if (value instanceof Character) {
	            return "\"" + escape(value.toString()) + "\"";
	        }

	        if (value instanceof Object[]) {
	            Object[] arr = (Object[]) value;
	            List list = new ArrayList(arr.length);
	            for (int i = 0; i < arr.length; i++)
	                list.add(arr[i]);
	            return listToJson(list, depth);
	        }
	        if (value instanceof Enum) {
	            return "\"" + ((Enum) value).name() + "\"";
	        }

	        return beanToJson(value, depth);
	    }

	    private static String beanToJson(Object model, int depth) {
	        Map map = new HashMap();
	        Method[] methods = model.getClass().getMethods();
	        for (Method m : methods) {
	            String methodName = m.getName();
	            int indexOfGet = methodName.indexOf("get");
	            if (indexOfGet == 0 && methodName.length() > 3) { // Only getter
	                String attrName = methodName.substring(3);
	                if (!attrName.equals("Class")) { // Ignore Object.getClass()
	                    Class<?>[] types = m.getParameterTypes();
	                    if (types.length == 0) {
	                        try {
	                            Object value = m.invoke(model);
	                            map.put(StringUtil.firstCharToLowerCase(attrName),
	                                    value);
	                        }
	                        catch (Exception e) {
	                            throw new RuntimeException(e.getMessage(), e);
	                        }
	                    }
	                }
	            } else {
	                int indexOfIs = methodName.indexOf("is");
	                if (indexOfIs == 0 && methodName.length() > 2) {
	                    String attrName = methodName.substring(2);
	                    Class<?>[] types = m.getParameterTypes();
	                    if (types.length == 0) {
	                        try {
	                            Object value = m.invoke(model);
	                            map.put(StringUtil.firstCharToLowerCase(attrName),
	                                    value);
	                        }
	                        catch (Exception e) {
	                            throw new RuntimeException(e.getMessage(), e);
	                        }
	                    }
	                }
	            }
	        }
	        return mapToJson(map, depth);
	    }
}
