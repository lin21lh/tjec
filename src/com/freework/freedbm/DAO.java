package com.freework.freedbm;

import java.util.List;

import com.freework.freedbm.bean.Criteria;
import com.freework.freedbm.bean.DBExecute;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.dao.OrderInfo;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.util.PageGridParam;
import com.freework.freedbm.util.TotalResult;
/**
 * 数据库操作dao基类接口
 * @author 程风岭
 * @category
 */
public interface DAO<dto2 extends DTO,E extends FieldInfoEnum>  {
	    public int insert(dto2 dto);
	    public int insertByKey(dto2 dto);
	    public int update(dto2 dto);
	    public int updateAll(dto2 dto);
	    public int delete(dto2 dto);
	    public dto2 getObject(dto2 key);
	    
	    public List<dto2> query(dto2 dto);
	    public List<dto2> query(dto2 dto,E ...e);
	    
	    
	    public List<dto2> query(dto2 dto,Criteria where);
	    public List<dto2> query(dto2 dto,Criteria where,E ...e);

	    
	    public TotalResult<dto2> queryPage( dto2 dto, PageGridParam gridParam,Criteria where) ;
	    public TotalResult<dto2> queryPage( dto2 dto, PageGridParam gridParam);
	    
	    public TotalResult<dto2> queryPage( int start,  int limit, dto2 dto);
	    public TotalResult<dto2> queryPage( int start,  int limit, dto2 dto,FieldInfoEnum... e2);

	    
	    public TotalResult<dto2> queryPage( int start,  int limit, dto2 dto,Criteria where);
		public TotalResult<dto2> queryPage( int start,  int limit, dto2 dto ,Criteria where,FieldInfoEnum... e2) ;
		public int getCount(dto2 dto,JdbcForDTO jdbcForDTO);
		public int getCountNotThis(dto2 dto,JdbcForDTO codecol,JdbcForDTO key);


}
