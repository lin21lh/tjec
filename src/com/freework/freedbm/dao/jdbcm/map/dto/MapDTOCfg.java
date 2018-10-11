package com.freework.freedbm.dao.jdbcm.map.dto;


import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnumUtil;
import com.freework.freedbm.cfg.id.IdentifierGenerator;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.unmodifiableMap.KeysIndex;

public class MapDTOCfg extends KeysIndex implements TableDataManager,java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3619086819775244936L;
	private String insertSQLKey="";
	private String insertSQL="";
	private String tableName="";
	private String updateSQL="";
	private String querySQL="";
	 JdbcForDTO[]  fields=null;
	private JdbcForDTO[] pkeys=null;
	private IdentifierGenerator identifierGenerator;
	
	public void setIdentifierGenerator(IdentifierGenerator identifierGenerator) {
		this.identifierGenerator = identifierGenerator;
	}

	public IdentifierGenerator getIdentifierGenerator(){
		return identifierGenerator;
	}
	
	public MapDTOCfg(){
	}
	
	 void setTableName(String tableName) {
		this.tableName = tableName;
		insertSQL=DB_TYPE.getInsertSql(getTableName(), fields);
		insertSQLKey=DB_TYPE.getInsertSqlKey(getTableName(), fields);
		querySQL=DB_TYPE.getQuerySql(getTableName(),fields,null);
		updateSQL=DB_TYPE.getUpdateSql(getTableName(),fields);
	}

	public MapDTOCfg(String tableName, JdbcForDTO[] fields) {
		this.tableName = tableName;
		this.fields = fields;
		String fieldName[]=new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldName[i]=fields[i].getName();
		}
		this.setNames(fieldName);
		pkeys=FieldInfoEnumUtil.getPkey(fields);
		insertSQL=DB_TYPE.getInsertSql(getTableName(), fields);
		insertSQLKey=DB_TYPE.getInsertSqlKey(getTableName(), fields);
		querySQL=DB_TYPE.getQuerySql(getTableName(),fields,null);
		updateSQL=DB_TYPE.getUpdateSql(getTableName(),fields);
		
	}
	public String getInsertSQLKey() {
		return insertSQLKey;
	}

	public String getInsertsql() {
		return insertSQL;
	}

	public String getTableName() {
		return tableName;
	}

	public String getUpdatesql() {
		return updateSQL;
	}

	public String getQuerysql() {
		return querySQL;
	}

	public Object newInstance() {
		return new MapDTO(this);
	}
	public Class getDTOClass() {
		return MapDTO.class;
	}
	public JdbcForDTO[] getFields() {
		return fields;
	}
	public JdbcForDTO[] getPKey() {
		return pkeys;
	}

	public JdbcForDTO getField(java.lang.String name) {
		Integer index=this.getIndex(name);
		if(index==null)
			return null;
		else
			return fields[index];
			
	}
	
	
	
}
