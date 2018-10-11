package com.freework.freedbm.dao;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.tablecfg.AbstractTableCfg;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * dto基类
 * @author 程风岭
 * @category
 */
public abstract class ManagerDTO implements BaseDTO,java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4921715390119321495L;
	private transient boolean[]  updateFieldIndexs=new boolean[this.getCfg().getFields().length];
	private transient int updateFieldIndexsSize=0;
	
	protected void addUpdateField(int index,JdbcForDTO field){
	
		if(!updateFieldIndexs[index]){
		    updateFieldIndexs[index]=true;
		    updateFieldIndexsSize++;
		}
	}
	
	protected void addUpdateField(FieldInfoEnum f){
		addUpdateField(f.ordinal(), f.getFieldInfo());
	}
	
	public  JdbcForDTO[] updateField(){
		JdbcForDTO fieldInfos[]=new JdbcForDTO[updateFieldIndexsSize];
		int i=0;
		 JdbcForDTO[] fields= this.getCfg().getFields();
		for (int j=0;j< updateFieldIndexs.length;j++) {
			if(updateFieldIndexs[j]){
				fieldInfos[i]=fields[j];i++;
				if(i==updateFieldIndexsSize)break;
			}
			
		}
		
			return fieldInfos;
	
	}
	public  void clearUpdateFieldIndexs(){
		 updateFieldIndexs=new boolean[this.getCfg().getFields().length];

	}
	public  void removeUpdateFieldIndexs(int index){
		 updateFieldIndexs[index]=false;

	}
	public int updateColIndexsSize(){
		return updateFieldIndexsSize;
	}
	abstract protected AbstractTableCfg getCfg();
	
	public TableDataManager 	managerCfg(){
		return this.getCfg();
	}
	public TableQuery	queryCfg(){
		return this.getCfg();
	}
	

}
