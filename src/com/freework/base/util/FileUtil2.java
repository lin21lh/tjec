package com.freework.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileUtil2 {
	public static boolean CreateFile(String destFileName) {
	    File file = new File(destFileName);
	    if (file.exists()) {
	     System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�Ŀ���ļ��Ѵ��ڣ�");
	     return false;
	    }
	    if (destFileName.endsWith(File.separator)) {
	     System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�Ŀ�겻����Ŀ¼��");
	     return false;
	    }
	    if (!file.getParentFile().exists()) {
	     System.out.println("Ŀ���ļ�����·�������ڣ�׼������������");
	     if (!file.getParentFile().mkdirs()) {
	      System.out.println("����Ŀ¼�ļ����ڵ�Ŀ¼ʧ�ܣ�");
	      return false;
	     }
	    }
	    // ����Ŀ���ļ�
	    try {
	     if (file.createNewFile()) {
	      System.out.println("���������ļ�" + destFileName + "�ɹ���");
	      return true;
	     } else {
	      System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�");
	      return false;
	     }
	    } catch (IOException e) {
	     e.printStackTrace();
	     System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�");
	     return false;
	    }
	}


	public static boolean createDir(String destDirName) {
	    File dir = new File(destDirName);
	    if(dir.exists()) {
	     System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�Ŀ��Ŀ¼�Ѵ��ڣ�");
	     return false;
	    }
	    if(!destDirName.endsWith(File.separator))
	     destDirName = destDirName + File.separator;
	    // ��������Ŀ¼
	    if(dir.mkdirs()) {
	     System.out.println("����Ŀ¼" + destDirName + "�ɹ���");
	     return true;
	    } else {
	     System.out.println("����Ŀ¼" + destDirName + "�ɹ���");
	     return false;
	    }
	}


	public static String createTempFile(String prefix, String suffix, String dirName) {
	    File tempFile = null;
	    try{
	    if(dirName == null) {
	     // ��Ĭ���ļ����´�����ʱ�ļ�
	     tempFile = File.createTempFile(prefix, suffix);
	     return tempFile.getCanonicalPath();
	    }
	    else {
	     File dir = new File(dirName);
	     // �����ʱ�ļ�����Ŀ¼�����ڣ����ȴ���
	     if(!dir.exists()) {
	      if(!FileUtil2.createDir(dirName)){
	       System.out.println("������ʱ�ļ�ʧ�ܣ����ܴ�����ʱ�ļ�����Ŀ¼��");
	       return null;
	      }
	     }
	     tempFile = File.createTempFile(prefix, suffix, dir);
	     return tempFile.getCanonicalPath();
	    }
	    } catch(IOException e) {
	     e.printStackTrace();
	     System.out.println("������ʱ�ļ�ʧ��" + e.getMessage());
	     return null;
	    }
	}
	
	   /**
		 * ��ȡ�ļ�����
		 * 
		 * @param filePathAndName
		 *            String �� c:\\1.txt ����·��
		 * @return boolean
		 */
	public static String readFile(String filePathAndName) {
		String fileContent = "";
		try {
			File f = new File(filePathAndName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line;
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݲ�������");
			e.printStackTrace();
		}
		return fileContent;
	}

	public static void writeFile(String filePathAndName, String fileContent) {
		try {
			File f = new File(filePathAndName);
			if (!f.exists()) {
				CreateFile(filePathAndName);
				//f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(f), "UTF-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			System.out.println("д�ļ����ݲ�������");
			e.printStackTrace();
		}
	} 

	public static void main(String[] args) {
	    // ����Ŀ¼
	    String dirName = "c:/test/test0/test1";
// CreateFileUtil.createDir(dirName);
	    // �����ļ�
	    String fileName = dirName + "/test2/testFile.txt";
	    FileUtil2.CreateFile(fileName);
	    // ������ʱ�ļ�
	    String prefix = "temp";
	    String suffix = ".txt";
	    for(int i = 0; i < 10; i++) {
	     System.out.println("��������ʱ�ļ�:" + FileUtil2.createTempFile(prefix, suffix, dirName));
	    }
	  
	}
}
