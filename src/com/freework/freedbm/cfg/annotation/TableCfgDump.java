package com.freework.freedbm.cfg.annotation;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;


public class TableCfgDump extends ClassLoader implements Opcodes {

	public Class<?> findClassTableCfg(String dtoname) {
		Class c = null;
		
		int index = dtoname.lastIndexOf(".");
		String name = dtoname;
		if (index != -1)
			name = name.substring(index + 1, name.length());
		name = "com/freework/freedbm/cfg/annotation/" + name+ "TableCfg";
	
		byte[] data;
		try {
			String name2=name.replace("/", ".");
			System.out.println(name2);

			if (null !=(c= super.findLoadedClass(name2)))
				return c;

			data = dump(name, dtoname);
//			FileOutputStream out=new FileOutputStream("C:\\aa.class");
//			out.write(data);
//			out.close();
			
			c = defineClass(name2, data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
		
	}
	
	public Class<?> findClass(String name2) {
		
		try {
			return Class.forName(name2);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] dump(String name, String dtoname) throws Exception {
		
//		ClassWriter cw = new ClassWriter(0);
//		MethodVisitor mv;
//		cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, name, null,
//				"com/freework/freedbm/cfg/annotation/TableCfg", null);
//
//		{
//			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//			mv.visitCode();
//			Label l0 = new Label();
//			mv.visitLabel(l0);
//			mv.visitLineNumber(3, l0);
//			mv.visitVarInsn(ALOAD, 0);
//			mv.visitMethodInsn(INVOKESPECIAL,
//					"com/freework/freedbm/cfg/annotation/TableCfg",
//					"<init>", "()V");
//			mv.visitInsn(RETURN);
//			Label l1 = new Label();
//			mv.visitLabel(l1);
//			mv.visitLocalVariable("this", "L" + name + ";", null, l0, l1, 0);
//			mv.visitMaxs(1, 1);
//			mv.visitEnd();
//		}
//		{
//			mv = cw.visitMethod(ACC_PUBLIC, "newInstance",
//					"()Ljava/lang/Object;", null, null);
//			mv.visitCode();
//			
//			Label l0 = new Label();
//			mv.visitLabel(l0);
//			mv.visitLineNumber(7, l0);
//			mv.visitTypeInsn(NEW, dtoname.replace(".", "/"));
//			mv.visitInsn(DUP);
//			mv.visitMethodInsn(INVOKESPECIAL, dtoname.replace(".", "/"),
//					"<init>", "()V");
//			mv.visitInsn(ARETURN);
//			Label l1 = new Label();
//			mv.visitLabel(l1);
//			mv.visitLocalVariable("this", "L" + name + ";", null, l0, l1, 0);
//			mv.visitMaxs(2, 1);
//			mv.visitEnd();
//		}
//		cw.visitEnd();
//
//		return cw.toByteArray();
			return null;
		
	}
}
