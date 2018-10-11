package com.freework.base.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class LoadDirClass {
	 Hashtable<Class, Object> table;
	String classpackage = null;
	Class myinterface = null;
	String path = null;

	Log log=LogFactory.getLog(LoadDirClass.class);

	public LoadDirClass(String classpackage, Class myinterface) {
		this.classpackage = classpackage;
		this.myinterface = myinterface;
		String packageDirName = classpackage.replace('.', '/');
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				packageDirName);
		try {
			path = java.net.URLDecoder.decode(url.getFile(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public LoadDirClass(Class myinterface) {
		this.myinterface = myinterface;
		this.classpackage = myinterface.getPackage().getName();

		URL url = myinterface.getResource("");
		try {

			path = java.net.URLDecoder.decode(url.getFile(), "utf-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public Object getObject(Class classname) {

		if (table == null)
			loadObjects(classpackage);

		return table.get(classname);
	}

	public Collection<Class> getClasses() {
		if (table == null)
			loadObjects(classpackage);
		return table.keySet();
	}

	public Collection getObjects() {
		if (table == null)
			loadObjects(classpackage);
		return table.values();
	}

	void loadObjects(String classpackage) {
		table = new Hashtable<Class, Object>();
		String classNames[] = getClassNames();

		for (int i = 0; i < classNames.length; i++) {

			Object object = loadObject(classpackage + "."
					+ classNames[i].replace(".class", ""), myinterface);

			if (object != null)
				table.put(object.getClass(), object);

		}

	}

	static Class[] getInterfaces(Class class1) {
		Class interfaces[] = class1.getInterfaces();
		if (class1.getSuperclass() != null)
			return getSuperClassInterfaces(class1.getSuperclass(), interfaces);
		else
			return interfaces;

	}

	static Class[] getSuperClassInterfaces(Class superClass,
			Class[] myinterfaces) {
		Class superClassInterfaces[] = superClass.getInterfaces();
		Class[] elementData = new Class[0];
		if (superClassInterfaces.length != 0 && myinterfaces.length != 0) {
			elementData = new Class[myinterfaces.length
					+ superClassInterfaces.length];
			System.arraycopy(myinterfaces, 0, elementData, 0,
					myinterfaces.length);
			System.arraycopy(superClassInterfaces, 0, elementData,
					myinterfaces.length, superClassInterfaces.length);
		} else if (superClassInterfaces.length != 0) {
			elementData = superClassInterfaces;
		} else if (myinterfaces.length != 0) {
			elementData = myinterfaces;
		}

		if (superClass.getSuperclass() != null)
			return getSuperClassInterfaces(superClass.getSuperclass(),
					elementData);
		else
			return elementData;

	}

	static Object loadObject(String className, Class myinterface) {

		Class interfaces[] = null;
		Class class1;
		try {
			class1 = Class.forName(className);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			return null;
		}
		if (!Modifier.isAbstract(class1.getModifiers())) {
			interfaces = getInterfaces(class1);
			if (interfaces != null && interfaces.length > 0) {
				for (int j = 0; j < interfaces.length; j++) {
					if (interfaces[j] == myinterface) {
						try {
							return class1.newInstance();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();

						}

					}
				}
			}
		}
		return null;
	}

	public String[] getDirClassNames(String path) {
		ArrayList<String> list = new ArrayList<String>();
		File file;
		file = new File(getLocalPath());
		File[] files = file.listFiles();
		String className = "";
		for (int i = 0; i < files.length; i++) {
			className = files[i].getName();
			if (className.length() > 6
					&& !className.equals("LoadObject.class")
					&& className.substring(className.length() - 6).equals(
							".class")) {
				list.add(files[i].getName());
			}
		}
		return list.toArray(new String[list.size()]);
	}


	public   String[] getJarClassNames(String jarpath,String packageD){
	  try {
		JarFile jfile = new JarFile(jarpath);
        Enumeration<JarEntry> entries = jfile.entries();  
        ArrayList<String> list = new ArrayList<String>();
        while (entries.hasMoreElements()) {  
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件  
            JarEntry entry = entries.nextElement();  
            String name = entry.getName();  
            
            if(name.startsWith(packageD)&&name.endsWith(".class")){
            	if(packageD.charAt(packageD.length()-1)=='/')
            	 name=name.replace(packageD, "");
            	else
            	 name=name.replace(packageD+"/", "");
            	 int index=name.lastIndexOf('/');
            	 if(index==-1)
            		 list.add(name.replace(packageD, ""));
            }
        }
        log.debug("loader:"+list.toString());
    	return list.toArray(new String[list.size()]);
	} catch (IOException e) {
		e.printStackTrace();
		log.debug("JarClassName", e);
	}
	  return new String[0];
	  //jfile.getEntry(packageD).
	  
  }
	String[] getClassNames() {
		String path = getLocalPath();
		int index = path.lastIndexOf(".jar!/");
		if (index != -1) {
			String jarpath = path.substring(0, index +4).replace("file:/", "");
			if(System.getProperty( "os.name").indexOf("Windows")==-1){
				jarpath="/"+jarpath;
			}
			String packageD = path.substring(index+6 );
			return getJarClassNames(jarpath, packageD);

		} else {
			return getDirClassNames(path);

		}
	}

	String getLocalPath() {

		return path;

	}

}
