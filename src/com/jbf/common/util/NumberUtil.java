package com.jbf.common.util;

import java.math.BigDecimal;

import org.apache.commons.beanutils.ConvertUtils;

public class NumberUtil {

	/**
	 * 两个对象相减
	 * 
	 * @param lval
	 * @param rval
	 * @return
	 */
	public static BigDecimal subtract(Object lval, Object rval) {
		BigDecimal demLval = lval != null ? bigDecimal(lval) : BigDecimal.ZERO;
		BigDecimal demRval = rval != null ? bigDecimal(rval) : BigDecimal.ZERO;
		return demLval.subtract(demRval);
	}

	/**
	 * 两个对象相加
	 * 
	 * @param lval
	 * @param rval
	 * @return
	 */
	public static BigDecimal add(Object lval, Object rval) {
		BigDecimal demLval = lval != null ? bigDecimal(lval) : BigDecimal.ZERO;
		BigDecimal demRval = rval != null ? bigDecimal(rval) : BigDecimal.ZERO;
		return demLval.add(demRval);
	}

	/**
	 * 两个对象相乘
	 * 
	 * @param lval
	 * @param rval
	 * @return
	 */
	public static BigDecimal multiply(Object lval, Object rval) {
		BigDecimal demLval = lval != null ? bigDecimal(lval) : BigDecimal.ZERO;
		BigDecimal demRval = rval != null ? bigDecimal(rval) : BigDecimal.ZERO;
		return demLval.multiply(demRval);
	}

	/**
	 * 两个对象相除
	 * 
	 * @param lval
	 * @param rval
	 * @return
	 */
	public static BigDecimal divide(Object lval, Object rval) {
		int scale = 6;
		return divide(lval, rval, scale);
	}

	/**
	 * 两个对象相除
	 * 
	 * @param lval
	 * @param rval
	 * @param scale保留的小数位数
	 * @return
	 */
	public static BigDecimal divide(Object lval, Object rval, int scale) {
		BigDecimal demLval = lval != null ? bigDecimal(lval) : BigDecimal.ZERO;
		BigDecimal demRval = rval != null ? bigDecimal(rval) : BigDecimal.ZERO;
		return demLval.divide(demRval, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static final int ten_2_pow = 100;
	public static final int ten_6_pow = 1000000;
	public static final int ten_8_pow = 100000000;

	public static double round(double number, int scale) {
		int multiple = 0;
		switch (scale) {
		case 2:
			multiple = ten_2_pow;
			break;
		case 6:
			multiple = ten_6_pow;
			break;
		case 8:
			multiple = ten_8_pow;
			break;
		default:
			multiple = (int) Math.pow(10, scale);

		}
		number = Math.round(number * multiple);
		return number / multiple;
	}

	public static BigDecimal bigDecimal(Object o) {
		BigDecimal demLval = null;
		if (o instanceof BigDecimal)
			demLval = (BigDecimal) o;
		else if (o instanceof String)
			demLval = new BigDecimal((String) o);
		else if (o instanceof Double)
			demLval = new BigDecimal(FormatUtil.fromDouble_6((Double) o));
		else if (o instanceof Float)
			demLval = new BigDecimal(FormatUtil.fromDouble_6(((Float) o)
					.doubleValue()));
		else if (o instanceof Number)
			demLval = new BigDecimal(((Number) o).longValue());
		else if (o != null)
			demLval = new BigDecimal(o.toString());
		return demLval;

	}

	public static BigDecimal sumArray(Object[] arrays) {
		BigDecimal sum = BigDecimal.ZERO;
		for (int j = 0; j < arrays.length; j++) {
			sum = add(sum, arrays[j]);
		}
		return sum;
	}

	public static int sumIntArray(int i[]) {
		int sum = 0;
		for (int j = 0; j < i.length; j++) {
			sum += i[j];
		}
		return sum;
	}

	public static int sumIntArray(int i[], int size) {
		int sum = 0;
		for (int j = 0; j < size; j++) {
			sum += i[j];
		}
		return sum;
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0)
			return false;
		int v = 0;

		for (int i = 0; i < length; i++) {
			v = str.charAt(i);

			if (v < 48 || v > 57) {
				if (!(v == '-' && i == 0) && v != '.')
					return false;
			}
		}
		return true;
	}

	public static <T extends Number> T doubleToNubmer(double number,
			Class<T> clazz) {
		if (clazz == Double.class) {
			return (T) new Double(number);
		} else if (clazz == Integer.class) {
			return (T) new Integer((int) number);
		} else if (clazz == Long.class) {
			return (T) new Long((long) number);
		} else if (clazz == BigDecimal.class)
			return (T) bigDecimal(number);
		else if (clazz == Float.class)
			return (T) new Float(number);
		else
			return (T) ConvertUtils.convert(FormatUtil.fromDouble_6(number),
					clazz);

	}
}
