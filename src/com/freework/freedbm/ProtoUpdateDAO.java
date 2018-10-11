package com.freework.freedbm;

import java.sql.Connection;
import java.sql.SQLException;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
/**
 * ���ݿ����dao����ӿ�
 * @author �̷���
 * @category
 */
public interface ProtoUpdateDAO<d extends DTO,E extends FieldInfoEnum>  {
	    public int insert(d dto,Connection con)  throws SQLException ;
	    public int insertByKey(d dto,Connection con)  throws SQLException ;
	    public int update(d dto,Connection con)  throws SQLException ;
	    public int updateAll(d dto,Connection con)  throws SQLException ;
	    public int delete(d dto,Connection con)  throws SQLException ;

}
