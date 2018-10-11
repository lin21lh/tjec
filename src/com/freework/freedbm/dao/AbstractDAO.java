package com.freework.freedbm.dao;
import java.util.ArrayList;
import java.util.List;

import com.freework.freedbm.DTO;
import com.freework.freedbm.bean.Criteria;
import com.freework.freedbm.bean.Order;
import com.freework.freedbm.bean.WhereResult;
import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.WhereOperators;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.Param.p;
import com.freework.freedbm.util.DTOByCfg;

/**
 * 数据库操作dao抽象类
 * @author 程风岭
 * @category
 */
public abstract class AbstractDAO  {
	public WhereResult getWhere(JdbcForDTO[] whereJdbcForDTOs,DTO dto){
		return this.getWhere(whereJdbcForDTOs, dto, 0);
	}
	public WhereResult getWhere(JdbcForDTO[] whereJdbcForDTOs,DTO dto,int size){
		StringBuilder sql = new StringBuilder(32);
		boolean isFirst=false;
		List<Param>  params=new ArrayList<Param>(whereJdbcForDTOs.length+size);
		for (int i = 0; i < whereJdbcForDTOs.length; i++) {
			WhereOperators operators=whereJdbcForDTOs[i].getLike();
			
			
			Object value=whereJdbcForDTOs[i].getValue(dto);
			if(value != null){
				if(!(value instanceof String)||!value.equals("")){
					if (isFirst) {
						sql.append(" and ");
					}else{
						isFirst=true;
					}
					
					
					if (value instanceof String) {
						value=((String)value).trim();
					}
					params.add(p.param((SQLType)whereJdbcForDTOs[i].getType(), value,operators instanceof Like?(Like)operators:null));
					if(operators==null)
						sql.append(whereJdbcForDTOs[i].getColName()).append("=?");
					else{
						sql.append(whereJdbcForDTOs[i].getColName()).append(" ").append(operators.getOperator()).append(" ? ");
					}
				}
			}
		
		}
		return new WhereResult(params,sql);
	}
	public Param[] getParams(JdbcForDTO[] ufieldInfos,DTO dto){
		 Param[] ps=new  Param[ufieldInfos.length];
		 for (int i = 0; i < ps.length; i++) {
			 	Like like= ufieldInfos[i].getLike() instanceof Like?(Like)ufieldInfos[i].getLike():null;
				ps[i]=p.param((SQLType)ufieldInfos[i].getType(), ufieldInfos[i].getValue(dto),like);
		}
		return ps;
		
		
		
	}
	
	public WhereResult getWhere(DTO dto,Order order){
		JdbcForDTO[] ufieldInfos = DTOByCfg.getUpdateField(dto,true);
		WhereResult  mywhere=getWhere(ufieldInfos,dto);
		if(order!=null){
			String orderSql=order.getOrder();
			if(!"".equals(orderSql)){
				mywhere.order(orderSql);
			}
		}
	
		return mywhere;
	}
	public WhereResult getWhere(DTO dto,Criteria where){
		JdbcForDTO[] ufieldInfos = DTOByCfg.getUpdateField(dto,true);
		WhereResult  mywhere;
		if(where!=null){
			mywhere=getWhere(ufieldInfos,dto,where.getValues().size());
			if(where.getSql().length()!=0){
				if(mywhere.isWhereBlank()){
					mywhere.setWhere(where.getSql());
				}else{
					mywhere.and(where.getSql());
				}
			}
			String orderSql=where.getOrder();
			if(!"".equals(orderSql)){
				mywhere.order(orderSql);
			}
			mywhere.getParams().addAll(where.getValues());

		}else{
			 mywhere=getWhere(ufieldInfos,dto);
		}
	
		return mywhere;
	}
}
