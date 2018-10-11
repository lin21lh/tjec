package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.Connection;

import com.freework.base.util.SqlUtil;
import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

/**
 * 查询一列存多个逗号分隔
 * @author Administrator
 *
 */
public class GroupLinkName implements LinkType {
	private static final long serialVersionUID = -6908274293722938884L;
	LinkInfo linkInfo = null;
	boolean isConnection = true;

	public GroupLinkName(String linkColName, String linkValueColName, String sourceName, String tableName) {
		this.linkInfo = new LinkInfo(linkColName, linkValueColName, sourceName, tableName);
	}

	public LinkInfo getLinkInfo() {
		return linkInfo;
	}

	public Object getValue(Object obj, FieldInfo fi) {
		Object sourceValue = fi.getValue(obj);
		return obj;
	}

	public String getName() {
		return "string";
	}

	public Class getReturnedClass() {
		return String.class;
	}

	public boolean isCollectionType() {
		return false;
	}

	public String getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs) {
		return null;

	}

	public Object getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs, Connection con) {
		String tablename = linkInfo.tableName;
		String code = linkInfo.linkValueColName;
		JdbcForDTO source = null;
		for (int i = 0; i < jdbcForDTOs.length; i++) {
			if (jdbcForDTOs[i].getColName().equals(linkInfo.sourceName)) {
				source = jdbcForDTOs[i];
				break;
			}
		}
		if (source == null) {
			System.out.println("cfl:" + linkInfo.sourceName + "不存在数据!!!!!!");
		}
		boolean isString = source.getType().getReturnedClass() == String.class;

		Object sourceValue = source == null ? null : source.getValue(dto);
		
		if(null==sourceValue)
			return null;
		
		String[] vals = sourceValue.toString().split(",");
		
		StringBuffer resultBuffer = new StringBuffer();
		for(String val : vals){
			String result = SqlUtil.querySingle(con, tablename, code, isString ? linkInfo.linkColName + "='" + val + "'" : linkInfo.linkColName + "=" + val);
			resultBuffer.append(result).append(",");
		}
		resultBuffer.setLength(resultBuffer.length()-1);
		
		return resultBuffer.toString();
	}

	public boolean isConnection() {
		return isConnection;
	}

}
