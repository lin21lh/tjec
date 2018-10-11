package com.wfzcx.aros.xzfy.service;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.xzfy.dao.BCallbackInvoicesDao;
import com.wfzcx.aros.xzfy.dao.BCaseDeliveryReceiptInfoDao;
import com.wfzcx.aros.xzfy.po.BCallbackInvoicesInfo;
import com.wfzcx.aros.xzfy.po.BCaseDeliveryReceiptInfo;

/**
 * 行政复议文书送达廉政回访service
 *
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzfy.service.BCaseDeliveryReceiptInfoService")
public class DeliveryReceiptAndCallbackService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	BCaseDeliveryReceiptInfoDao receiptDao;
	@Autowired
	BCallbackInvoicesDao callBackDao;

	
	/**
	 * 廉政回访分页查询
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryCallBackGrid(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		sql.append(" select t.ciid,t.caseid,t.isattitude,t.islegal, t.iscorruption,t.isfavoritism,t.issatisfaction,t.remark,t.interviewee, t.contactway,t.interviewtime, t.userid,t.operator,t.opttime ");
		sql.append(", (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0 and a.code = t.isattitude) isattitudename ");
		sql.append(", (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0 and a.code = t.islegal) islegalname ");
		sql.append(", (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0 and a.code = t.isfavoritism) isfavoritismname ");
		sql.append(", (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0 and a.code = t.iscorruption) iscorruptionname ");
		sql.append(", (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0 and a.code = t.issatisfaction) issatisfactionname ");

		//查询采购结果审核通过的项目
		sql.append(" from B_CALLBACKINVOICES t ");
		sql.append(" where t.caseid = '"+caseid+"' ");
		sql.append("  order by t.opttime ");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	/**
	 * 保存廉政回访信息
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String saveCallBack(Map<String, Object> param) throws Exception{
		BCallbackInvoicesInfo data = new BCallbackInvoicesInfo();
		//取值
		String id = StringUtil.stringConvert(param.get("ciid"));
		if(StringUtils.isEmpty(id)) {
			BeanUtils.populate(data, param);
			// 获取当前登录用户信息
			SysUser user = SecureUtil.getCurrentUser();
			data.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			data.setOperator(user.getUsercode());
			data.setUserid(user.getUserid());
			callBackDao.save(data);
		}else {
			data = callBackDao.get(id);
			BeanUtils.populate(data, param);
			data.setCiid(id);
			callBackDao.update(data);
		}
		return "";
	}
	
	/**
	 * 删除廉政回访信息
	 * @param id
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteCallBackById(String id) throws Exception {
		callBackDao.delete(id);
		return "";
	}
	
	
	/**
	 * 分页查询
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryReceiptGrid(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		sql.append(" select t.cdrid, t.caseid, t.docname,t.doccode, t.receiver,t.deliverydate, t.deliverysite, t.deliveryway,  t.processagent, t.processserver,t.remark,  t.userid,  t.operator,t.opttime");
		//查询采购结果审核通过的项目
		sql.append(" from B_CASEDELIVERYRECEIPT t ");
		sql.append(" where t.caseid = '"+caseid+"' ");
		sql.append("  order by t.opttime ");
		System.out.println("sql--"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	/**
	 * 保存文书送达信息
	 * @param param
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String saveReceipt(Map<String, Object> param) throws Exception{
		BCaseDeliveryReceiptInfo data = new BCaseDeliveryReceiptInfo();
		//取值
		String id = StringUtil.stringConvert(param.get("cdrid"));
		if(StringUtils.isEmpty(id)) {
			BeanUtils.populate(data, param);
			// 获取当前登录用户信息
			SysUser user = SecureUtil.getCurrentUser();
			data.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			data.setOperator(user.getUsercode());
			data.setUserid(user.getUserid());
			receiptDao.save(data);
		}else {
			data = receiptDao.get(id);
			BeanUtils.populate(data, param);
			data.setCdrid(id);
			receiptDao.update(data);
		}
		return "";
	}
	
	/**
	 * 删除文书送达信息
	 * @param id
	 * @return
	 * @throws Exception 设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteReceiptById(String id) throws Exception {
		receiptDao.delete(id);
		return "";
	}
	
}
