/************************************************************
 * 类名：SysDicUISchemeServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：界面方案服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-06  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.dto.CodeType;
import com.jbf.base.dic.dto.Column;
import com.jbf.base.dic.dto.EditSchemeDTO;
import com.jbf.base.dic.dto.GridFieldDTO;
import com.jbf.base.dic.dto.GridSchemeDTO;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.service.SysDicUISchemeService;
import com.jbf.base.dic.ui.resolve.DicUICSchemeResolve;
import com.jbf.base.dic.ui.resolve.EasyUIDataGridSchemeResolve;
import com.jbf.base.dic.ui.resolve.EasyUIFormResolve;
import com.jbf.base.dic.ui.resolve.EasyUITreeSchemeResolve;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.dao.SysDicUISchemeDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.base.tabsdef.po.SysDicUIScheme;
import com.jbf.base.tabsdef.vo.TableColumnTreeVo;
import com.jbf.common.TableNameConst;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Service
public class SysDicUISchemeServiceImpl implements SysDicUISchemeService {

    @Autowired
    SysDicUISchemeDao dicUISchemeDao;
    @Autowired
    SysDicTableDao dicTableDao;
    @Autowired
    SysDicElementDao dicElementDao;
    @Autowired
    SysDicColumnDao dicColumnDao;

    @Override
    public Map getScheme(String elementcode, String tablecode) throws AppException, IOException {
    	Map modelMap = new HashMap();
        SysDicUIScheme dicUIScheme = dicUISchemeDao.getByTablecode(tablecode);
        if (dicUIScheme == null)
            throw new AppException("数据项【" + elementcode + "】未查找到正在使用的界面方案");
        
        SysDicElement dicElement = dicElementDao.getByElementcode(elementcode);
        SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		if (dicTable == null)
			throw new AppException("数据项【" + dicElement.getElementcode() + "-" + dicElement.getElementname() + "】未找到对应的数据表");

        CodeType codeType = CodeType.getCodeType(dicElement.getCodetype());
        modelMap.put("codetype", codeType);
        switch (codeType) {
		case NoneCode: //
			modelMap.put("editScheme", this.getEditScheme(dicElement, tablecode, dicUIScheme));
			modelMap.put("gridSchemeID", this.getGridSchemeVM());
			modelMap.put("gridScheme", this.getGridScheme(elementcode, tablecode, dicUIScheme));
			modelMap.put("tablecode", tablecode);
			modelMap.put("keycolumn", dicTable.getKeycolumn().toLowerCase());
			break;
		case OrderCode:
			modelMap.put("editScheme", this.getEditScheme(dicElement, tablecode, dicUIScheme));
			modelMap.put("gridSchemeID", this.getGridSchemeVM());
			modelMap.put("gridScheme", this.getGridScheme(elementcode, tablecode, dicUIScheme));
			modelMap.put("tablecode", tablecode);
			modelMap.put("keycolumn", dicTable.getKeycolumn().toLowerCase());
			break;
		case LayerCode:
			modelMap.put("editScheme", this.getEditScheme(dicElement, tablecode, dicUIScheme));
			modelMap.put("treeSchemeID", this.getTreeSchemeVM());
			modelMap.put("treeScheme", this.getTreeScheme(elementcode, tablecode, dicUIScheme));
			modelMap.put("keycolumn", dicTable.getKeycolumn().toLowerCase());
			modelMap.put("tablecode", tablecode);
			System.out.println(modelMap.get("treeScheme"));
			break;
		default:
			break;
		}
        return modelMap;
    }
    
	@Override
	public String getEditScheme(SysDicElement dicElement, String tablecode, SysDicUIScheme dicUIScheme) throws AppException, IOException {
		
		EditSchemeDTO editSchemeDTO = new DicUICSchemeResolve().getEditScheme(dicUIScheme.getFormscheme());
		
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		if (dicTable == null)
			throw new AppException("数据项【" + dicElement.getElementcode() + "-" + dicElement.getElementname() + "】未找到对应的数据表");
		
		editSchemeDTO.setDicTable(dicTable);
		
		List<SysDicColumn> colList = dicColumnDao.findColumnsByTablecode(dicTable.getTablecode());
		Map<String, SysDicColumn> columnMap = new HashMap<String, SysDicColumn>();
		
		for (SysDicColumn col : colList) {
			columnMap.put(col.getColumncode().toLowerCase(), col);
		}
		
		editSchemeDTO.setColumnMap(columnMap);
		
		return new EasyUIFormResolve().getFormScheme(editSchemeDTO, dicElement);
	}
	
	public String getBusinessFormScheme(String tablecode) throws AppException, IOException {
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode.toUpperCase());
		if (dicTable == null)
			throw new AppException("表" +tablecode + "未查找到");
		
		SysDicUIScheme formUIScheme = dicUISchemeDao.getByTablecode(tablecode.toUpperCase());
		if (formUIScheme == null)
			throw new AppException("表" +tablecode + "对应的界面方案未查找到");
		
		EditSchemeDTO formSchemeDTO = new DicUICSchemeResolve().getEditScheme(formUIScheme.getFormscheme());
		formSchemeDTO.setDicTable(dicTable);
		List<SysDicColumn> colList = dicColumnDao.findColumnsByTablecode(dicTable.getTablecode());
		Map<String, SysDicColumn> columnMap = new HashMap<String, SysDicColumn>();
		
		for (SysDicColumn col : colList) {
			columnMap.put(col.getColumncode().toLowerCase(), col);
		}
		
		formSchemeDTO.setColumnMap(columnMap);
		
		return new EasyUIFormResolve().getFormScheme(formSchemeDTO, null);
	}



    @Override
    public String getGridScheme(String elementcode, String tablecode, SysDicUIScheme dicUIScheme) throws AppException, IOException {

//        if (dicUIScheme == null) {
//            dicUIScheme = dicUISchemeDao.getUISchemeByTablecode(tablecode);
//            if (dicUIScheme == null)
//                throw new AppException("数据项【" + elementcode + "】未查找到正在使用的界面方案");
//        }
    	if (StringUtil.isBlank(dicUIScheme.getListscheme()))
    		throw new AppException("datatable.datagridshceme.undefined", new String[] {tablecode});
    	
        GridSchemeDTO gridSchemeDTO = new DicUICSchemeResolve().getGridScheme(dicUIScheme.getListscheme());
        List<SysDicColumn> colList = dicColumnDao.findColumnsByTablecode(tablecode.toUpperCase());
        Map<String, SysDicColumn> columnMap = new HashMap<String, SysDicColumn>();

        for (SysDicColumn col : colList) {
            columnMap.put(col.getColumncode().toLowerCase(), col);
        }

        gridSchemeDTO.setColumnMap(columnMap);
        return new EasyUIDataGridSchemeResolve().getGridScheme(gridSchemeDTO, elementcode,  tablecode);
    }

    @Override
    public String getTreeScheme(String elementcode, String tablecode, SysDicUIScheme dicUIScheme) throws AppException {
        return new EasyUITreeSchemeResolve().getTreeScheme(tablecode, elementcode);
    }

    public EditSchemeDTO getESDto(String editScheme) throws IOException {

        return new DicUICSchemeResolve().getEditScheme(editScheme);
    }

    @Override
    public String getGridSchemeVM() {
        // TODO Auto-generated method stub
        return new EasyUIDataGridSchemeResolve().getGridSchemeVM();
    }
    
    public String getTreeSchemeVM() {
    	return new EasyUITreeSchemeResolve().getTreeSchemeVM();
    }

    @Override
    public void saveDicUISchema(SysDicUIScheme dicUISchema) {
        dicUISchemeDao.save(dicUISchema);
    }
    
    public List<SysDicColumn> findColumns(String tablecode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		dc.add(Property.forName("tablecode").eq(tablecode.toUpperCase()));
		return (List<SysDicColumn>) dicColumnDao.findByCriteria(dc);
    }
    
    public List<TableColumnTreeVo> findColumnsTree(String tablecode, Long schemeid) throws IOException {
    	List<SysDicColumn> columns = findColumns(tablecode);
    	
		SysDicUIScheme dicUIScheme = null;
		Map<String, Boolean> isHiddenMap = null;
		if (schemeid != null)
			dicUIScheme = dicUISchemeDao.getByTablecode(tablecode, schemeid);
		
    	if (dicUIScheme != null && StringUtil.isNotBlank(dicUIScheme.getListscheme())) {
    		isHiddenMap = new HashMap<String, Boolean>();
    		GridSchemeDTO gridSchemeDTO = new DicUICSchemeResolve().getGridScheme(dicUIScheme.getListscheme());
    		List<GridFieldDTO> gfields = gridSchemeDTO.getGridField();
    		
    		for (GridFieldDTO gridField : gfields) {
    			if (gridField.getIshidden() == 1)
    				isHiddenMap.put(gridField.getName().toLowerCase(), true);
    			else
    				isHiddenMap.put(gridField.getName().toLowerCase(), false);
    		}
    	}
    	
    	List<TableColumnTreeVo> list = new ArrayList<TableColumnTreeVo>(columns.size());
    	TableColumnTreeVo columnVo = null;
    	for (SysDicColumn dicColumn : columns) {
    		columnVo = new TableColumnTreeVo();
    		columnVo.setColumncode(dicColumn.getColumncode().toLowerCase());
    		columnVo.setText(dicColumn.getColumnname());
    		if (isHiddenMap != null && isHiddenMap.get(dicColumn.getColumncode().toLowerCase()) != null)
    			columnVo.setChecked(!isHiddenMap.get(dicColumn.getColumncode().toLowerCase()));
    		else
    			columnVo.setChecked(false);
    		columnVo.setIsLeaf(true);
    		columnVo.setLevelno(1);
    		columnVo.setId(dicColumn.getColumnid().toString());
    		list.add(columnVo);
    	}
    	
    	return list;
    }
    
    public Map getDatagridUIScheme(String tablecode, Long schemeid) throws IOException {
    	
    	Map colMap = new HashMap<String, String>();
		List<Column> frozenColumns = new ArrayList<Column>();
		List<Column> columns = new ArrayList<Column>();
		Map<String, String> transMap = new HashMap<String, String>();
		List<SysDicColumn> cols = dicColumnDao.findColumnsByTablecode(tablecode);
		for (SysDicColumn dicColumn : cols) {
			transMap.put(dicColumn.getColumncode().toLowerCase(), dicColumn.getColumnname());
		}
		Column column = null;
		SysDicUIScheme dicUIScheme = null;
		if (schemeid != null)
			dicUIScheme = dicUISchemeDao.getByTablecode(tablecode, schemeid);
    	if (dicUIScheme != null && StringUtil.isNotBlank(dicUIScheme.getListscheme())) {
    		GridSchemeDTO gridSchemeDTO = new DicUICSchemeResolve().getGridScheme(dicUIScheme.getListscheme());
    		List<GridFieldDTO> gfields = gridSchemeDTO.getGridField();
    		
    		for (GridFieldDTO gridField : gfields) {
      			column = new Column(gridField.getName(), false, transMap.get(gridField.getName().toLowerCase()) + "", gridField.getWidth(), gridField.getIshidden()==1);
    			columns.add(column);	
    		}
    	} else {
    		for (SysDicColumn dicColumn : cols) {
    			column = new Column(dicColumn.getColumncode().toLowerCase(), false, dicColumn.getColumnname(), "120", true);
    			if (dicColumn.getIskeycol().equals("1"))
    				frozenColumns.add(column);
    			else
    				columns.add(column);
    		}
    	}
    	
    	colMap.put("frozenColumns", frozenColumns);
    	colMap.put("columns", columns);
    	
    	return colMap;
    }
    
    public List<Map> queryDicUIScheme(String tablecode) {
    	return dicUISchemeDao.queryBySQL(TableNameConst.SYS_DICUISCHEME, Arrays.asList("schemeid", "tablecode", "schemename", "used", "remark"), "upper(tablecode)='" + tablecode.toUpperCase() + "'");
    }
    
    public Long copyUIToTable(Long schemeid) {
    	
    	SysDicUIScheme dicUIScheme = dicUISchemeDao.get(schemeid);
    	dicUIScheme.setSchemeid(null);
    	dicUIScheme.setSchemename(dicUIScheme.getSchemename() + "-副本");
    	dicUIScheme.setUsed(Byte.valueOf("0"));
    	Long newSchemeid = (Long) dicUISchemeDao.save(dicUIScheme);
    	return newSchemeid;
    }
}
