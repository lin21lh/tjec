package com.freework.freedbm.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.DAO;
import com.freework.freedbm.DTO;
import com.freework.freedbm.bean.Criteria;
import com.freework.freedbm.bean.DBExecute;
import com.freework.freedbm.bean.Delete;
import com.freework.freedbm.bean.Insert;
import com.freework.freedbm.bean.Update;
import com.freework.freedbm.bean.WhereResult;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnumUtil;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.Param.p;
import com.freework.freedbm.dao.jdbcm.QueryInfo;
import com.freework.freedbm.util.DTOByCfg;
import com.freework.freedbm.util.PageGridParam;
import com.freework.freedbm.util.PageTotalResult;
import com.freework.freedbm.util.TotalResult;

/**
 * @author 程风岭
 * @category
 
 */
public class DaoSupport<dto2 extends DTO, E extends FieldInfoEnum>
		extends NamedParameterJdbcDaoSupport  implements DAO<dto2, FieldInfoEnum> {
//	DataSource mydataSource=null;
	@Resource()
	public void setSuperDataSource(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	protected DefaultProtoQueryDAO qdao = DefaultProtoQueryDAO.getDefaultQueryDAO();
	protected DefaultProtoUpdateDAO udao = DefaultProtoUpdateDAO.getDefaultUpdateDAO();
	public int delete(final dto2 dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
						Integer r= udao.delete(dto, conn);
						
						return r;
					}
				});
	}
	

	public int insert(final dto2 dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
					
						Integer r=  udao.insert(dto, conn);
						
						return r;
					}
				});
	}
	public int insertByKey(final dto2 dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
						Integer r=  udao.insertByKey(dto, conn);
						
						
						return r;
					}
				});
	}
	
	
	public int update(final dto2 dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
						Integer r=  udao.update(dto, conn);
						
						
						return r;
					}
				});
	}

	
	public int updateAll(final dto2 dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
						Integer r=  udao.updateAll(dto, conn);
						
						return r;
					}
				});
	}
	
	
	public <T> T getObject(String sql,Class<T> requiredType,Object... args){
		QueryInfo qi =QueryInfo.getQueryInfo(sql, SQLTypeMap.getSQLType(requiredType), Arrays.asList(p.params(args)));
		return ((T) this.getObject(qi));
		
		
	}
	public int getCount(dto2 dto,JdbcForDTO jdbcForDTO){
		String tablename=DTOByCfg.getTableDataManager(dto).getTableName();
		String codecolName=jdbcForDTO.getColName();
		Object codeValue=jdbcForDTO.getValue(dto);
		String querySelect="select count(*) from "+tablename+" where "+codecolName+"=?";
		QueryInfo count =QueryInfo.getQueryInfo(querySelect, Cfg.Integer, Arrays.asList(p.param(codeValue)));
		Integer countInt=(Integer) this.getObject(count);
		return countInt;
	}
	
	public int getCount2(dto2 dto,JdbcForDTO ... jdbcForDTO){
		String tablename=DTOByCfg.getTableDataManager(dto).getTableName();
		StringBuffer sql = new StringBuffer("select count(*) from ");
		sql.append(tablename).append(" where 1=1 ");
		List<Param> plist = new ArrayList<Param>();
		for(JdbcForDTO jdto : jdbcForDTO){
			sql.append("and ").append(jdto.getColName()).append(" =? ");
			plist.add(p.param(jdto.getValue(dto)));
		}
		
		
		QueryInfo count =QueryInfo.getQueryInfo(sql.toString(), Cfg.Integer, plist);
		Integer countInt=(Integer) this.getObject(count);
		return countInt;
	}
	
	
	public int getCountNotThis(dto2 dto,JdbcForDTO codecol,JdbcForDTO key){
		String tablename=DTOByCfg.getTableDataManager(dto).getTableName();
		String keycol=key.getColName();
		Object keyvalue=key.getValue(dto);
		String codecolName=codecol.getColName();
		Object codeValue=codecol.getValue(dto);
		String querySelect="select count(*) from "+tablename+" where "+codecolName +"=? and "+keycol+"<>?";
		QueryInfo count =QueryInfo.getQueryInfo(querySelect, Cfg.Integer,Arrays.asList(p.param(codeValue),p.param(keyvalue)));
		Integer countInt=(Integer) this.getObject(count);
		return countInt;
	}
	
	/**
	 * @param dto
	 * @param key		主键名
	 * @param codecols	联合唯一列
	 * @return
	 */
	public int getCountNotThis2(dto2 dto,JdbcForDTO key,JdbcForDTO... codecols){
		String tablename=DTOByCfg.getTableDataManager(dto).getTableName();
		String keycol=key.getColName();
		Object keyvalue=key.getValue(dto);
		
		StringBuffer sql = new StringBuffer("select count(*) from ");
		sql.append(tablename).append(" where 1=1 ");
		List<Param> plist = new ArrayList<Param>();
		for(JdbcForDTO jdto : codecols){
			sql.append("and ").append(jdto.getColName()).append(" =? ");
			plist.add(p.param(jdto.getValue(dto)));
		}
		sql.append(" and ").append(keycol).append("<>?");
		plist.add(p.param(keyvalue));
		
		QueryInfo count =QueryInfo.getQueryInfo(sql.toString(), Cfg.Integer,plist);
		Integer countInt=(Integer) this.getObject(count);
		return countInt;
	}
	
	
	public int insertBatch(final List<dto2> dtos) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
						return udao.insertBatch(conn,(List<DTO>)dtos);
					}
				});
	}
	public int insertBatchByKey(final List<dto2> dtos) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(Connection conn)
							throws SQLException {
						return udao.insertBatchByKey(conn,(List<DTO>) dtos);
					}
				});
	}
	public dto2 getObject(final dto2 dto, final FieldInfoEnum... e) {
		return (dto2) super.getJdbcTemplate().execute(
				new ConnectionCallback<dto2>() {
					public dto2 doInConnection(Connection conn)
							throws SQLException {
						return (dto2) qdao.getObject(conn, dto,FieldInfoEnumUtil.getFieldInfos(e));
					}
				});
	}


	public dto2 getObject(final dto2 dto) {
		return (dto2) super.getJdbcTemplate().execute(
				new ConnectionCallback<dto2>() {
					public dto2 doInConnection(Connection conn)
							throws SQLException {
						return (dto2) qdao.getObject(conn, dto);
					}
				});
	}

	public int update(final String sql,final Object values[],final SQLType type) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						
						return udao.update(conn, sql, values, type);
					}
				});
	}
	public int update(final String sql,final Object... values) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						 SQLType types[]=new SQLType[values.length];
						
						for (int i = 0; i < values.length; i++) {
							if(values[i]==null)
								types[i]=Cfg.String;
							else
								types[i]=SQLTypeMap.getSQLType(values[i].getClass());
								
						}
						
						
						return udao.update(conn, sql, values, types);
					}
				});
	}


	public int delete(final String sql,final Object... params ) {
		return this.update(sql, params);
		
	}

	//@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public dto2 getDTOObject(final QueryInfo qi) {
		
		return  super.getJdbcTemplate().execute(
				new ConnectionCallback<dto2>() {
					public dto2 doInConnection(Connection conn)
							throws SQLException {
						return (dto2) qdao.getDTOObject(conn, qi);
					}
				});
		
		
	
	}

	public Object getObject(final QueryInfo qi) {
		
		return  super.getJdbcTemplate().execute(
				new ConnectionCallback<Object>() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						return qdao.getObject(conn, qi);
					}
				});
		
	}

	public List<dto2> query(final QueryInfo qi) {

		
		return  super.getJdbcTemplate().execute(
				new ConnectionCallback<List<dto2> >() {
					public List<dto2>  doInConnection(Connection conn)
							throws SQLException {
						return ( List<dto2>)qdao.query(conn, qi);
					}
				});
		
	}



	


	

	public  Update update(String tableName){
		Update update=new Update(tableName);
		update.setDao(this);
		return update;
	}
	public  Insert insert(String tableName){
		Insert insert=new Insert(tableName);
		insert.setDao(this);
		return insert;
	}
	
	public  Delete delete(String tableName){
		Delete delete=new Delete(tableName);
		delete.setDao(this);
		return delete;

	}

	@Override
	public List<dto2> query(dto2 dto) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,null);
		return this.query( tablecfg, wr);
	}
	@Override
	public List<dto2> query(dto2 dto, Criteria where) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,where);
		return this.query( tablecfg, wr);
	}

	@Override
	public List<dto2> query(dto2 dto, FieldInfoEnum... e) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,null);
		return this.query( tablecfg, wr,e);
	}
	
	@Override
	public List<dto2> query(dto2 dto, Criteria where, FieldInfoEnum... e) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,where);
		return this.query( tablecfg, wr,e);
	}

	
	@Override
	public TotalResult<dto2> queryPage(int start, int limit, dto2 dto) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,null);
		return this.queryPage(start, limit, tablecfg, wr);
	}
	
	@Override
	public TotalResult<dto2> queryPage(dto2 dto, PageGridParam gridParam) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,gridParam.getOrder(tablecfg));
		return this.queryPage(gridParam.getStartNum(), gridParam.getRp(), tablecfg, wr);
	}
	@Override
	public TotalResult<dto2> queryPage(dto2 dto, PageGridParam gridParam,
			Criteria where) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		where.order(gridParam.getOrder(tablecfg));

		WhereResult wr=qdao.getWhere(dto,where);
		return this.queryPage(gridParam.getStartNum(), gridParam.getRp(), tablecfg, wr);
	}

	public TotalResult<dto2> queryPage(dto2 dto, PageGridParam gridParam,
			Criteria where,FieldInfoEnum... e2) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		where.order(gridParam.getOrder(tablecfg));

		WhereResult wr=qdao.getWhere(dto,where);
		return this.queryPage(gridParam.getStartNum(), gridParam.getRp(), tablecfg, wr,e2);
	}

	@Override
	public TotalResult<dto2> queryPage(int start, int limit, dto2 dto,
			FieldInfoEnum... e2) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,null);
		return this.queryPage(start, limit, tablecfg, wr, e2);
	}


	@Override
	public TotalResult<dto2> queryPage(int start, int limit, dto2 dto,
			Criteria where) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,where);
		return this.queryPage(start, limit, tablecfg, wr);
	}


	@Override
	public TotalResult<dto2> queryPage(int start, int limit, dto2 dto,
			Criteria where, FieldInfoEnum... e2) {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(dto);
		WhereResult wr=qdao.getWhere(dto,where);
		return this.queryPage(start, limit, tablecfg, wr, e2);
	}
	public TotalResult<dto2> queryPage(final int start, final int limit,
			final TableQuery cfg, final WhereResult wr, final FieldInfoEnum[] e2) {
			return  super.getJdbcTemplate().execute(
					new ConnectionCallback<TotalResult<dto2>>() {
						public TotalResult<dto2> doInConnection(Connection conn)
								throws SQLException {
							return (TotalResult<dto2>) qdao.queryPage(conn, start, limit, cfg,wr.getWhere(), wr.getParams(),FieldInfoEnumUtil.getFieldInfos(e2),wr.getOrder());
						}
					});
		}
		public TotalResult<dto2> queryPage(final int start, final int limit,
				final TableQuery cfg,final WhereResult wr) {
			return  super.getJdbcTemplate().execute(
					new ConnectionCallback<TotalResult<dto2>>() {
						public TotalResult<dto2> doInConnection(Connection conn)
								throws SQLException {
							return (TotalResult<dto2>) qdao.queryPage(conn, start, limit, cfg,wr.getWhere(),wr.getParams() ,wr.getOrder());
						}
					});
			
		}
		public  List<dto2> query(final TableQuery cfg, Criteria where, final FieldInfoEnum... e2) {
			return this.query(cfg, where.toWhereResult(), e2);
		}
		public  List<dto2> query(final TableQuery cfg, Criteria where) {
			return this.query(cfg, where.toWhereResult());
		}
		public  List<dto2> query(final TableQuery cfg,final WhereResult wr, final FieldInfoEnum... e2) {
			return  super.getJdbcTemplate().execute(
					new ConnectionCallback< List<dto2>>() {
						public List<dto2> doInConnection(Connection conn)
								throws SQLException {
							return  ( List<dto2>) qdao.query(conn, cfg,wr.getWhere(), wr.getParams(),FieldInfoEnumUtil.getFieldInfos(e2),wr.getOrder());
						}
					});
			
		}
		public List<dto2>  query(final TableQuery cfg,final WhereResult wr) {
			return  super.getJdbcTemplate().execute(
					new ConnectionCallback<List<dto2> >() {
						public List<dto2>  doInConnection(Connection conn)
								throws SQLException {
							return (List<dto2> ) qdao.query(conn, cfg,wr.getWhere(), wr.getParams(),wr.getOrder());
						}
					});
			
		}


		
	
}
