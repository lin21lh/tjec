package com.wfzcx.fam.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.datascope.ConditionFilter;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
/**
 * 
 * @ClassName: DeptComponent 
 * @Description: TODO机构公用类
 * @author XinPeng
 * @date 2015年4月20日 下午8:12:53
 */
public class DeptComponent {
	
	static SysDeptDao sysDeptDao;
	static MapDataDaoI mapDataDao;
	
	public static void init() {
		if (sysDeptDao == null) {
			sysDeptDao = (SysDeptDao) WebContextFactoryUtil.getBean("jbf.sys.dept.SysDeptDao");
		}
		if (mapDataDao == null) {
			mapDataDao = (MapDataDaoI) WebContextFactoryUtil.getBean("com.jbf.common.dao.MapDataDao");
		}
	} 
	/**
	 *  add By XinPeng 2015年4月20日20:04:23
	 * @Title: getCurAndLowerCode 
	 * @Description: TODO返回当前机构的本级以及下级机构
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 */
	public static List<String> getCurAndLowerCode() {
		init();
		SysUser user = SecureUtil.getCurrentUser();
		SysDept dept = sysDeptDao.get(user.getOrgcode());
		return getLowerDept(dept);
	}
	public static List<String> getLowerDept(SysDept dept) {
		List<String> returnList = new ArrayList();
		returnList.add(dept.getCode());
		DetachedCriteria dc = DetachedCriteria.forClass(SysDept.class);
		dc.add(Property.forName("superitemid").eq(dept.getItemid()));
		dc.add(Property.forName("status").eq(0));//正常
		List<SysDept> lowerDept = (List<SysDept>) sysDeptDao.findByCriteria(dc);
		for (int i = 0; i < lowerDept.size(); i++) {
			SysDept deptDetail = lowerDept.get(i);
			if(deptDetail.getIsleaf()==0){
				returnList.addAll(getLowerDept(deptDetail));
			}else{
				returnList.add(deptDetail.getCode());
			}
		}
		return returnList;
	}
	/**
	 * 返回当前用户权限范围内的预算单位，用,分隔，如果'001','002','003'
	 * 如果是预算单位，则返回本级及以下，如果是业务科室则返回全部1=1，如果是银行或者其他，则返回1<>1
	 * @Title: getCurAndLowerCodeForString 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param fieldName 字段名称
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public static String getCurAndLowerCodeForString(String fieldName){
		init();
		SysUser user = SecureUtil.getCurrentUser();
		SysDept dept = sysDeptDao.get(user.getOrgcode());
		//获取机构类型，1：业务科室，2：预算单位3：银行
		Long agencycat = dept.getAgencycat();
		if(agencycat ==2){//预算单位权限为本级及以下
			List<String> list =getCurAndLowerCode();
			String returnString = "";
			for (int i = 0; i < list.size(); i++) {
				returnString +=list.get(i);
				if(list.size()-1!=i){
					returnString +=",";
				}
			}
			returnString = returnString.replaceAll(",", "','");
			if (!"".equals(returnString)) {
				returnString = fieldName +" in('"+returnString+"')";
			}
			return returnString;
		}else if(agencycat ==1){//业务科室有全部权限
			return "(1=1)";
		}else{//其他情况没有权限
			return "(1<>1)";
		}
	}
	/**
	 * 预算单位树数据权限过滤
	 * @Title: getCurAndLowerCodeForYsdw 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param menuid 菜单id
	 * @param fieldName 关联字段
	 * @return 设定文件
	 */
	public static String getCurAndLowerCodeForYsdw(String menuid,String fieldName){
		init();
		boolean condFilter = false;
		try {
			condFilter = ConditionFilter.isHasDataRight(Long.parseLong(menuid),"BDGAGENCY");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!condFilter) {//数据权限为空
			SysUser user = SecureUtil.getCurrentUser();
			SysDept dept = sysDeptDao.get(user.getOrgcode());
			//获取菜单相关信息
			String sql = "select t.datascopemode from sys_resource t where t.resourceid="+menuid;
			List datalist = mapDataDao.queryListBySQL(sql);
			Integer datascopemode =null;
			if(!datalist.isEmpty()){
				Map map =(Map) datalist.get(0);
				datascopemode  =Integer.parseInt(StringUtil.stringConvert(map.get("datascopemode")));
			}
			String mrFilter ="";
			String conditionFilters ="";
			//获取机构类型，1：业务科室，2：预算单位3：银行
			Long agencycat = dept.getAgencycat();
			if(agencycat ==2){//预算单位权限为本级及以下
				List<String> list =getCurAndLowerCode();
				String returnString = "";
				for (int i = 0; i < list.size(); i++) {
					returnString +=list.get(i);
					if(list.size()-1!=i){
						returnString +=",";
					}
				}
				returnString = returnString.replaceAll(",", "','");
				if (!"".equals(returnString)) {
					returnString = fieldName +" in('"+returnString+"')";
				}
				mrFilter = returnString;
			}else if(agencycat ==1){//业务科室有全部权限
				mrFilter =  "(1=1)";
			}else{//其他情况没有权限
				mrFilter = "(1<>1)";
			}
			switch (datascopemode) {
				case 0: //禁止优先
					if (!"".equals(mrFilter)) {//如果数据权限为空，则查询其默认权限
						conditionFilters = mrFilter;
					}else {//
						conditionFilters = "(1<>1)";
					}
					break;
				case 1: //允许优先
					 if (!"".equals(mrFilter)) {//如果数据权限为空，则查询其默认权限
						 conditionFilters = mrFilter;
					}else {//
						conditionFilters = "(1=1)";
					}
					break;
				default:
					break;
			}
			return conditionFilters;
		}else {//如果有数据权限，则以数据权限为准，树在查询时已经集成了数据权限。
			return "(1=1)";
		}
	}
	public static String getCurAndLowerCodeForPpms(String fieldName){
		init();
		SysUser user = SecureUtil.getCurrentUser();
		SysDept dept = sysDeptDao.get(user.getOrgcode());
		//获取机构类型，1：PPP中心，2：政府职能部门3：社会资本
		Long agencycat = dept.getAgencycat();
		if(agencycat ==1){//PPP中心全部
			return "(1=1)";
		}else if(agencycat ==2 ||agencycat ==3){//政府职能部门和社会资本
			List<String> list =getCurAndLowerCode();
			String returnString = "";
			for (int i = 0; i < list.size(); i++) {
				returnString +=list.get(i);
				if(list.size()-1!=i){
					returnString +=",";
				}
			}
			returnString = returnString.replaceAll(",", "','");
			if (!"".equals(returnString)) {
				returnString = fieldName +" in('"+returnString+"')";
			}
			return returnString;
		}else{//其他情况没有权限
			return "(1<>1)";
		}
	}
}
