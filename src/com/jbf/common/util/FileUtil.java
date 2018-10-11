package com.jbf.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 文件操作工具类
 * 
 * @author zhaocz
 * 
 */
public class FileUtil{

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		return getExtension(filename, "");
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				return (filename.substring(i + 1)).toLowerCase();
			}
		}
		return defExt.toLowerCase();
	}

	/**
	 * 获取文件名称[不含后缀名]
	 * 
	 * @param
	 * @return String
	 */
	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
	}

	/**
	 * 获取文件名称[不含后缀名] 不去掉文件目录的空格
	 * 
	 * @param
	 * @return String
	 */
	public static String getFilePrefix2(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}

	/**
	 * 文件复制 方法摘要：这里一句话描述方法的用途
	 * 
	 * @param
	 * @return void
	 */
	public static void copyFile(String inputFile, String outputFile)
			throws FileNotFoundException {
		File sFile = new File(inputFile);
		File tFile = new File(outputFile);
		FileInputStream fis = new FileInputStream(sFile);
		FileOutputStream fos = new FileOutputStream(tFile);
		int temp = 0;
		byte[] buf = new byte[10240];
		try {
			while ((temp = fis.read(buf)) != -1) {
				fos.write(buf, 0, temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getSize(Long length) {
		if (length == null) {
			return "0 KB";
		}
		long lengthKB = length / 1024;
		if (lengthKB < 1024) {
			if (length % 1024 > 0) {
				lengthKB++;
			}
			if (lengthKB == 1024) {
				return "1 MB";
			} else {
				return lengthKB + " KB";
			}
		}
		DecimalFormat format = new DecimalFormat("0.##");
		BigDecimal lengthMB = new BigDecimal(length).divide(new BigDecimal(
				1024 * 1024), 2, RoundingMode.HALF_DOWN);
		if (lengthMB.compareTo(new BigDecimal(1024)) < 0) {
			return format.format(lengthMB) + " MB";
		}
		BigDecimal lengthGB = lengthMB.divide(new BigDecimal(1024), 2,
				RoundingMode.HALF_DOWN);
		return format.format(lengthGB) + " GB";
	}

	public static void writeToFile(InputStream is, File file) throws Exception {
		byte[] b = new byte[10240];
		int count = 0;
		FileOutputStream fos = new FileOutputStream(file);
		while ((count = is.read(b)) > 0) {
			fos.write(b, 0, count);
		}
		fos.flush();
		fos.close();
	}

}
