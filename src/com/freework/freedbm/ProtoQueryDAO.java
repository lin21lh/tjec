package com.freework.freedbm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.OrderInfo;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.util.TotalResult;
/**
 * 查询dao基类接口
 * @author 程风岭
 * @category
 */
public interface ProtoQueryDAO{
  
    public DTO getObject(Connection con,DTO key)throws SQLException;

    public List<DTO> query(Connection con,TableQuery tablecfg,String where,List<Param> ps,JdbcForDTO[]e,String order) throws SQLException;
    public List<DTO> query(Connection con,TableQuery tablecfg,String where,List<Param> ps,String order) throws SQLException;
	public TotalResult<DTO> queryPage(Connection con,int start,int limit,TableQuery tablecfg,String where,List<Param> ps,String order) throws SQLException ;
	public TotalResult<DTO> queryPage(Connection con, int start,int limit,TableQuery tablecfg,String where,List<Param> ps, JdbcForDTO[] e,String order) throws SQLException;
	
//    public TotalResult<DTO> queryPage(Connection con,int start,int limit,DTO dto) throws SQLException;
//    public TotalResult<DTO> queryPage(Connection con,int start,int limit,DTO dto,E ...e) throws SQLException;
//    public TotalResult<DTO> queryPage(Connection con,int start,int limit,DTO dto,String where) throws SQLException;
//    public TotalResult<DTO> queryPage(Connection con,int start,int limit,DTO dto,String where,E ...e) throws SQLException;
	   
}
