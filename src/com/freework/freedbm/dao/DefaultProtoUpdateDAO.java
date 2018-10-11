package com.freework.freedbm.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.freework.freedbm.DTO;
import com.freework.freedbm.ProtoUpdateDAO;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.id.IdentifierGenerator;
import com.freework.freedbm.cfg.tablecfg.TableCfg;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.JdbcManager;
import com.freework.freedbm.util.DTOByCfg;

/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class DefaultProtoUpdateDAO extends AbstractDAO implements ProtoUpdateDAO<DTO, FieldInfoEnum>{

	protected JdbcManager jm=JdbcManager.getJdbcManager();

	private final static  DefaultProtoUpdateDAO defaultUpdateDAO=new DefaultProtoUpdateDAO();
	private  DefaultProtoUpdateDAO(){
		 
	 }
	public static  DefaultProtoUpdateDAO getDefaultUpdateDAO(){
		return defaultUpdateDAO;
	}
	public int insertByKey(DTO dto, Connection con) throws SQLException {
		TableDataManager tablecfg = DTOByCfg.getTableDataManager(dto);
		JdbcForDTO[] fieldInfos = tablecfg.getFields();
		String sql = tablecfg.getInsertSQLKey();
		return jm.insert(dto, sql, fieldInfos, con, false);
	}

	
	
	public int insertBatchByKey(Connection con,List<DTO> dtos) throws SQLException{
		if(dtos.size()<=0)
			return 0;
		TableDataManager cfg= DTOByCfg.getTableDataManager(dtos.get(0));
		JdbcForDTO jdbcForDTOs[]=cfg.getFields();
		String sql=cfg.getInsertSQLKey();
		String tablename=cfg.getTableName();
		return jm.insertBatchByKey(con, dtos, jdbcForDTOs, sql, tablename);

	}
	
	public int insertBatch(Connection con,List<DTO> dtos) throws SQLException{
		if(dtos.size()<=0)
			return 0;
		TableDataManager cfg= DTOByCfg.getTableDataManager(dtos.get(0));
		JdbcForDTO jdbcForDTOs[]=cfg.getFields();
		String tablename=cfg.getTableName();
		JdbcForDTO key[]=cfg.getPKey();
		
		if(key.length==1){
			if(cfg.getIdentifierGenerator()==null){
				return jm.insertBatch(con, dtos, key[0], jdbcForDTOs, cfg.getInsertsql(), tablename);

			}else{
				for (DTO dto : dtos) {
					IdentifierGenerator idGenerator=cfg.getIdentifierGenerator();
					Serializable value=idGenerator.generate(con, dto);
					key[0].setValue(dto, value);
				}
				return jm.insertBatchByKey(con, dtos, jdbcForDTOs, cfg.getInsertSQLKey(), tablename);
			}
		}
		else
			return jm.insertBatchByKey(con, dtos, jdbcForDTOs, cfg.getInsertSQLKey(), tablename);
		
		
	}
	
	public  String getWhere(JdbcForDTO[] ufieldInfos){
			StringBuffer sql = new StringBuffer(100);
			for (int i = 0; i < ufieldInfos.length; i++) {
				sql.append(ufieldInfos[i].getColName()).append("=").append("?");
				if (i != ufieldInfos.length - 1) {
					
					sql.append(" and ");
				}
			}
			return sql.toString();
		}
	public int delete(DTO dto,  Connection con) throws SQLException{
		TableDataManager tablecfg =  DTOByCfg.getTableDataManager(dto);
		String sql="delete from "+tablecfg.getTableName()+" where "+this.getWhere(tablecfg.getPKey());
		return jm.update(dto, sql, tablecfg.getPKey(), con, false);
	}
	public int update(Connection con, String sql,Object values[],SQLType type) throws SQLException{
		return jm.update(con, sql, values, type);
		
	}
	public int update(Connection con, String sql,Object values[],SQLType[] types) throws SQLException{
		return jm.update(con, sql, values, types);
		
	}
	
	public int update(DTO dto, Connection con) throws SQLException {
		TableDataManager tablecfg =  DTOByCfg.getTableDataManager(dto);
		JdbcForDTO[] fieldInfos = DTOByCfg.getUpdateField(dto,false);
		String sql = TableCfg.DB_TYPE.getUpdateSql(tablecfg.getTableName(),fieldInfos);
		return jm.update(dto, sql, fieldInfos,con,  true);
	}

	public int updateAll(DTO dto, Connection con) throws SQLException {
		TableDataManager tablecfg =  DTOByCfg.getTableDataManager(dto);
		String sql = tablecfg.getUpdatesql();
		JdbcForDTO[] fieldInfos = tablecfg.getFields();
		return jm.update(dto,  sql, fieldInfos,con, true);

	}

	public int insert(DTO dto, Connection con) throws SQLException {
		TableDataManager tablecfg =  DTOByCfg.getTableDataManager(dto);
		String sql = tablecfg.getInsertsql();
		JdbcForDTO[] fieldInfos = tablecfg.getFields();
		if( tablecfg.getPKey().length==1){
			
			if(tablecfg.getIdentifierGenerator()==null){
				return jm.insert(con, dto, tablecfg.getPKey()[0], fieldInfos,  sql, tablecfg.getTableName());
			}else{
				IdentifierGenerator idGenerator=tablecfg.getIdentifierGenerator();
				Serializable value=idGenerator.generate(con, dto);
				tablecfg.getPKey()[0].setValue(dto, value);
				return insertByKey(dto, con);
			}
		}else{
			return insertByKey(dto, con);
		}
	}

	
}
