package com.freework.freedbm.cfg.fieldcfg;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.beanutils.ConvertUtils;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.Type;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */

public class FieldInfo  implements JdbcForDTO,java.io.Serializable{
	private String name;
	private String colName;
	private String comments;
	private Type type;
	private Object defVal=null;

	

	private boolean isKey=false;
	private WhereOperators like=null;
	private Enum e=null;
	
	private boolean isDbCol;
	private transient Method writeMethod;
	private transient Method readMethod;
	public Method getWriteMethod() {
		return writeMethod;
	}


	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}


	public Method getReadMethod() {
		return readMethod;
	}


	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}


	public boolean isDbCol() {
		return isDbCol;
	}


public FieldInfo(String name, String colName, Type type, String comments,
		boolean isKey, WhereOperators like,Enum e) {
	super();
	this.name = name;
	this.colName = colName;
	this.comments = comments;
	this.type = type;
	this.isKey = isKey;
	this.like = like;
	this.e = e;
	this.isDbCol=(type instanceof SQLType);

}


public FieldInfo(String name, String colName, Type type) {
	super();
	this.name = name;
	this.colName = colName;
	this.type = type;
	this.isDbCol=(type instanceof SQLType);


	
}
public String getColName() {
	return colName;
}
public void setColName(String colName) {
	this.colName = colName;
}
public Type getType() {
	return type;
}
public void setType(Type type) {
	this.type = type;
	this.isDbCol=(type instanceof SQLType);

}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public boolean isKey() {
	return isKey;
}
public void setKey(boolean isKey) {
	this.isKey = isKey;
}


public void setComments(String comments) {
	this.comments = comments;
}
public String getComments() {
	return comments;
}
public WhereOperators getLike() {
	return like;
}
public void setLike(WhereOperators like) {
	this.like = like;
}

public Enum getE() {
	return e;
}

public void setE(Enum e) {
	this.e = e;
}

public Object getDefVal() {
	return defVal;
}


public void setDefVal(String defVal) {
	this.defVal = ConvertUtils.convert(defVal, this.getType().getReturnedClass());
}


public Object getValue(Object obj) {
	try {
		return readMethod.invoke(obj);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}


public void setValue(Object obj, Object Value) {
	try {
		writeMethod.invoke(obj,  Value);
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		e.printStackTrace();
	}
}
}
