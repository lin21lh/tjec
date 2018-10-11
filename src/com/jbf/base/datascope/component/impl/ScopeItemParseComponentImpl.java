/************************************************************
 * 类名：ScopeItemParseComponentImpl
 *
 * 类别：组件类
 * 功能：组装数据权限项条件
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.component.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.datascope.component.ScopeItemParseComponent;
import com.jbf.base.datascope.component.impl.DatascopeComponentImpl.ScopeType;
import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.base.dic.component.DicElementComponent;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.TableNameConst;
import com.jbf.common.exception.AppException;

@Scope("prototype")
@Component
public class ScopeItemParseComponentImpl implements ScopeItemParseComponent {
	
	@Autowired
	DicElementComponent dicElementCp;
	
    public String getSql(SysDatascopeitem scopeitem, String tableAlias, String tableCode, Long userid, int scopeType) throws AppException {
		boolean hasSource = false;
		String elementCode = scopeitem.getElementcode().toUpperCase();
		SysDicElement elementPo = dicElementCp.getDicElement(elementCode);
		if(elementPo.getTablecode() != null)
		    hasSource = true;
		StringBuffer sqlBuffer = new StringBuffer();
		String columnCode = null;
		String startSql = getStartSql(scopeitem.getIsinclude());
		if(hasSource) {
		    SysDicTable elementTableInfo = dicElementCp.getDicTableByElementcode(elementCode);
		    String elementTableName = elementTableInfo.getTablecode();
		    if(elementTableName.equalsIgnoreCase(tableCode)) {
		    	if ((elementTableInfo.getTabletype().equals(Byte.valueOf("0")) || elementTableInfo.getTabletype().equals(Byte.valueOf("1"))) && scopeType == ScopeType.CODE) {
		    		columnCode = elementTableInfo.getCodecolumn();
		    	} else
		    		columnCode = elementTableInfo.getKeycolumn();
		    } else
		        columnCode = findColumnBySourceElement(tableCode, elementCode);
		    
			//【0=全部、1=多选】
			switch (scopeitem.getMatchtype()) {
			case 0: // 全部
			    sqlBuffer.append(startSql);
			    return sqlBuffer.append(getMultiAllSql(scopeitem, tableAlias, columnCode, scopeType)).toString();
			case 1: // 多选
				sqlBuffer.append(startSql);
				return sqlBuffer.append(getMultiChoiseSql(scopeitem, tableAlias, columnCode, scopeType)).toString();
			default:
				break;
			}
			return sqlBuffer.toString();
		} else {
			// 数字、金额、日期【2=大于等于、3=小于等于、4=等于、5=不等于、6=大于、7=小于、12=为null】，文本【8=左匹配、9=右匹配、10全匹配、11=等于、12=为null】
			SysDicColumn dicColumn = dicElementCp.getDicColumnBySourceElement(tableCode, elementCode);
			if (dicColumn == null)
				throw new AppException("在" + tableCode + "表里找不到字段与数据项" + elementCode + "对应！");
		    columnCode = dicColumn.getColumncode();
		    return getNotHasSourceSql(scopeitem, tableAlias, columnCode, scopeType).toString();
		}
    }
    
    private String getStartSql(byte isInclude) {
        String startSql;
        if(String.valueOf(isInclude).equals("1"))
            startSql = " (exists(select * from ";
        else
            startSql = " (not exists(select * from ";
        return startSql;
    }
    
    /**
     * 无源数据项 匹配
     * @param scopeitem
     * @param tableAlias
     * @param columnCode
     * @param isSQL
     * @return
     */
    private StringBuffer getNotHasSourceSql(SysDatascopeitem scopeitem, String tableAlias, String columnCode, int scopeType) {
    	MatchType matchType = MatchType.getCodeType(scopeitem.getDatatype(), scopeitem.getMatchtype());
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("(");
        sqlBuffer.append(tableAlias);
        sqlBuffer.append(".");
        sqlBuffer.append(columnCode);
        sqlBuffer.append(matchType.getName());
        sqlBuffer.append(matchType.getPrefix());
        if (!Byte.valueOf("12").equals(scopeitem.getMatchtype()) && !Byte.valueOf("13").equals(scopeitem.getMatchtype())) {        	
        	sqlBuffer.append(scopeitem.getscopevalue());
        }
        sqlBuffer.append(matchType.getSuffix());
        sqlBuffer.append(")");
        return sqlBuffer;
    }
    
    /**
     *  全部
     * @param scopeitem
     * @param tableAlias
     * @param columnCode
     * @return
     * @throws AppException
     */
    private StringBuffer getMultiAllSql(SysDatascopeitem scopeitem, String tableAlias, String columnCode, int scopeType) throws AppException {
    	String elementViewSql = null;
		elementViewSql = dicElementCp.getElementCodeSql(scopeitem.getElementcode().toUpperCase(), "");
		SysDicTable tableInfo = dicElementCp.getDicTableByElementcode(scopeitem.getElementcode().toUpperCase());
		String elementTableRelationColName = tableInfo.getKeycolumn();
		if (scopeType == ScopeType.CODE)
			elementTableRelationColName = tableInfo.getCodecolumn();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" ( ");
		sqlBuffer.append(elementViewSql);
		sqlBuffer.append(" ) ");
		sqlBuffer.append(tableAlias);
		sqlBuffer.append(" where ");
		sqlBuffer.append(tableAlias);
		sqlBuffer.append(".");
		sqlBuffer.append(columnCode);
		sqlBuffer.append("=");
		sqlBuffer.append(elementTableRelationColName);
		sqlBuffer.append("))");
		return sqlBuffer;
    }
    
    /**
     * 多选
     * @param scopeitem
     * @param tableAlias 表别名
     * @param columnCode
     * @return
     */
    private StringBuffer getMultiChoiseSql(SysDatascopeitem scopeitem, String tableAlias, String columnCode, int scopeType) {
    	String scopevalueKey = TableNameConst.SYS_SCOPEVALUE;
  
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" ");
        sqlBuffer.append(scopevalueKey);
        sqlBuffer.append(" where ");
        sqlBuffer.append(tableAlias);
        sqlBuffer.append(".");
        sqlBuffer.append(columnCode);
        sqlBuffer.append("= ");
        sqlBuffer.append(scopevalueKey);
        switch (scopeType) {
		case ScopeType.ID:
			 sqlBuffer.append(".valueid AND ");
			break;
		case ScopeType.CODE:
			 sqlBuffer.append(".valuecode AND ");
			break;
		}
        
        sqlBuffer.append(scopevalueKey);
        sqlBuffer.append(".Scopeitemid=");
        sqlBuffer.append(scopeitem.getScopeitemid());
        sqlBuffer.append("))");
        return sqlBuffer;
    }
    
    public String findColumnBySourceElement(String tableCode, String sourceElement) throws AppException {
        SysDicColumn column = dicElementCp.getDicColumnBySourceElement(tableCode, sourceElement);
        if(column == null)
            throw new AppException("在" + tableCode + "表里找不到字段与数据项" + sourceElement + "对应！");
        else
            return column.getColumncode();
    }
    
 // 数字、金额、日期【2=大于等于、3=小于等于、4=等于、5=不等于、6=大于、7=小于】，文本【8=左匹配、9=右匹配、10全匹配、4=等于】
    public enum MatchType {
    	
    	GEStr(">=", Arrays.asList("D"), Byte.parseByte("2"), "'", "'"),
    	GENum(">=", Arrays.asList("N", "Y"), Byte.parseByte("2"), "", ""), 
    	LEStr("<=", Arrays.asList("D"), Byte.parseByte("3"), "'", "'"), 
    	LENum("<=", Arrays.asList("N", "Y"), Byte.parseByte("3"), "", ""), 
    	EQStr("=", Arrays.asList("D", "S"), Byte.parseByte("4"),  "'", "'"),
    	EQNum("=", Arrays.asList("N", "Y"), Byte.parseByte("4"), "", ""),
    	NEStr("<>", Arrays.asList("D", "S"), Byte.parseByte("5"),  "'", "'"),
    	NENum("<>", Arrays.asList("N", "Y"), Byte.parseByte("5"), "", ""),
    	GTStr(">", Arrays.asList("D"), Byte.parseByte("6"),  "'", "'"),
    	GTNum(">", Arrays.asList("N", "Y"), Byte.parseByte("6"), "", ""),
    	LTStr("<", Arrays.asList("D"), Byte.parseByte("7"), "'", "'"),
    	LTNum("<", Arrays.asList("N", "Y"), Byte.parseByte("7"), "", ""),
    	LIKELEFT("like", Arrays.asList("S"), Byte.parseByte("8"), " '%", "'"),
    	LIKERIGHT("like", Arrays.asList("S"), Byte.parseByte("9"), " '", "%'"),
    	LIKEALL("like", Arrays.asList("S"), Byte.parseByte("10"), " '%", "%'"),
    	ISNULL(" is null", Arrays.asList("D", "N", "Y", "S"), Byte.parseByte("12"), "", ""),
    	ISNOTNULL(" is not null", Arrays.asList("D", "N", "Y", "S"), Byte.parseByte("13"), "", "");
    	
    	
    	private String name;
    	private List<String> datatypeList;
    	private Byte index;
    	private String prefix; //前缀
    	private String suffix; //后缀
    	
    	
    	private MatchType(String name, List<String> datatypeList, Byte index, String prefix, String suffix) {
    		this.name = name;
    		this.datatypeList = datatypeList;
    		this.index = index;
    		this.prefix = prefix;
    		this.suffix = suffix;
    	}
    	
        public static MatchType getCodeType(String datatype, Byte index) {  
            switch (index) {  
            case 2:
            	if (GENum.datatypeList.contains(datatype))
            		return MatchType.GENum;
            	else
            		return MatchType.GEStr;  
            case 3:
            	if (LENum.datatypeList.contains(datatype))
            		return MatchType.LENum;
            	else
            		return MatchType.LEStr;   
            case 4:
            	if (EQNum.datatypeList.contains(datatype))
            		return MatchType.LENum;
            	else
            		return MatchType.LEStr;
            case 5:
            	if (NENum.datatypeList.contains(datatype))
            		return MatchType.NENum;
            	else
            		return MatchType.NEStr;
            case 6:
            	if (GTNum.datatypeList.contains(datatype))
            		return MatchType.GTNum;
            	else
            		return MatchType.GEStr;
            case 7:
            	if (LTNum.datatypeList.contains(datatype))
            		return MatchType.LTNum;
            	else
            		return MatchType.LTStr;
            case 8:
            	return MatchType.LIKELEFT;
            case 9:
            	return MatchType.LIKERIGHT;
            case 10:
            	return MatchType.LIKEALL;
            case 12:
            	return MatchType.ISNULL;
            case 13:
            	return MatchType.ISNOTNULL;
            default :
            	return MatchType.EQStr;
            }  
        } 
    	
    	public String getName() {
    		return name;
    	}
    	public void setName(String name) {
    		this.name = name;
    	}
    	public List<String> getDatatypeList() {
			return datatypeList;
		}
    	public void setDatatypeList(List<String> datatypeList) {
			this.datatypeList = datatypeList;
		}
    	public Byte getIndex() {
    		return index;
    	}
    	public void setIndex(Byte index) {
    		this.index = index;
    	}
    	public String getPrefix() {
			return prefix;
		}
    	public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
    	public String getSuffix() {
			return suffix;
		}
    	public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
    }
}
