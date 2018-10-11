package com.wfzcx.aros.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.DateFormatUtils;

import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.po.Resultbaseinfo;

/**
 * @ClassName: FlowUtil
 * @Description: 操作流程集合
 * @author ybb
 * @date 2016年8月11日 下午4:44:04
 * @version V1.0
 */
public class FlowUtil {

	/**
	 * @Title: genSubmittedOperationData
	 * @Description: 生成处理标志为【已提交】的操作流程集合
	 * @author ybb
	 * @date 2016年8月11日 下午5:24:47
	 * @param pronodebaseinfo
	 * @param caseid
	 * @return
	 */
	public static Probaseinfo genSubmittedOperationData(String protype, BigDecimal nodeid, String caseid,
			Probaseinfo probaseinfo, SysUser user, String result, String remark, String reason) {

		if (null == probaseinfo.getId()) {
			probaseinfo.setProcessid(UUID());
			probaseinfo.setSeqno(BigDecimal.ONE);
		} else {
			probaseinfo.setSeqno(probaseinfo.getSeqno().add(BigDecimal.ONE));
		}
		
		probaseinfo.setProtype(protype);
		probaseinfo.setNodeid(nodeid);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
		probaseinfo.setCaseid(caseid);
		probaseinfo.setResult(result);
		probaseinfo.setRemark(remark);
		probaseinfo.setReason(reason);
		probaseinfo.setStarttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setEndtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		if (user != null){
			probaseinfo.setUserid(user.getUserid());
			probaseinfo.setOperator(user.getUsername());
		}
		
		return probaseinfo;
	}
	
	/**
	 * @Title: genAcceptedOperationData
	 * @Description: 生成处理标志为【已接收】的操作流程集合
	 * @author ybb
	 * @date 2016年8月11日 下午5:26:39
	 * @param pronodebaseinfo
	 * @param caseid
	 * @return
	 */
	public static Probaseinfo genAcceptedOperationData(String protype, BigDecimal nodeid, String caseid,
			Probaseinfo probaseinfo, SysUser user) {
		
		if (null == probaseinfo.getId()) {
			probaseinfo.setProcessid(UUID());
			probaseinfo.setSeqno(BigDecimal.ONE);
		} else {
			probaseinfo.setSeqno(probaseinfo.getSeqno().add(BigDecimal.ONE));
		}
		
		probaseinfo.setProtype(protype);
		probaseinfo.setNodeid(nodeid);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(caseid);
		probaseinfo.setStarttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setEndtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setUserid(user.getUserid());
		
		return probaseinfo;
	}
	
	/**
	 * @Title: genProcessedOperationData
	 * @Description: 生成处理标志为【已处理】的操作流程集合
	 * @author ybb
	 * @date 2016年8月16日 下午2:23:42
	 * @param protype
	 * @param nodeid
	 * @param caseid
	 * @param probaseinfo
	 * @param user
	 * @param result
	 * @param remark
	 * @return
	 */
	public static Probaseinfo genProcessedOperationData(String protype, BigDecimal nodeid, String caseid,
			Probaseinfo probaseinfo, SysUser user, String result, String remark) {
		
		if (null == probaseinfo.getId()) {
			
			probaseinfo.setProcessid(UUID());
			probaseinfo.setSeqno(BigDecimal.ONE);
			
		} else {
			probaseinfo.setSeqno(probaseinfo.getSeqno().add(BigDecimal.ONE));
		}
		
		probaseinfo.setProtype(protype);
		probaseinfo.setNodeid(nodeid);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED);
		probaseinfo.setCaseid(caseid);
		probaseinfo.setResult(result);
		probaseinfo.setRemark(remark);
		probaseinfo.setStarttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setEndtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setUserid(user.getUserid());
		
		return probaseinfo;
		
	}
	
	/**
	 * @Title: genReturnedOperationData
	 * @Description: 生成处理标志为【已退回】的操作流程集合
	 * @author ybb
	 * @date 2016年8月16日 下午5:04:01
	 * @param protype
	 * @param caseid
	 * @param probaseinfo
	 * @param user
	 * @param result
	 * @param remark
	 * @return
	 */
	public static Probaseinfo genReturnedOperationData(String protype, String caseid, Probaseinfo probaseinfo, SysUser user, String result, String remark) {

		if (null == probaseinfo.getId()) {
			
			probaseinfo.setProcessid(UUID());
			probaseinfo.setSeqno(BigDecimal.ONE);
			
		} else {
			probaseinfo.setSeqno(probaseinfo.getSeqno().add(BigDecimal.ONE));
		}
		probaseinfo.setProtype(protype);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_RETURNED);
		probaseinfo.setCaseid(caseid);
		probaseinfo.setStarttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setEndtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setResult(result);
		probaseinfo.setRemark(remark);
		if (user != null){
			probaseinfo.setUserid(user.getUserid());
			probaseinfo.setOperator(user.getUsername());
		}

		return probaseinfo;
	}
	
	/**
	 * @Title: genResultOperationData
	 * @Description: 生成处理结果办理流程【已办】或【不予办理】
	 * @author ybb
	 * @date 2016年8月17日 下午5:57:36
	 * @param protype
	 * @param nodeid
	 * @param caseid
	 * @param rbi
	 * @param user
	 * @return
	 */
	public static Resultbaseinfo genResultOperationData(String protype, BigDecimal nodeid, String caseid,
			Resultbaseinfo rbi, SysUser user) {

		rbi.setProtype(protype);
		rbi.setNodeid(nodeid);
		rbi.setSeqno(rbi.getSeqno().add(BigDecimal.ONE));
		rbi.setCaseid(caseid);
		rbi.setStarttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		rbi.setEndtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		if (user != null){
			rbi.setUserid(user.getUserid());
			rbi.setOperator(user.getUsername());
		}

		return rbi;
	}
	
	/**
	 * @Title: genForwardingOperationData
	 * @Description: 生成转发流程日志
	 * @author ybb
	 * @date 2016年9月5日 下午6:06:01
	 * @param protype
	 * @param nodeid
	 * @param caseid
	 * @param probaseinfo
	 * @param user
	 * @return
	 */
	public static Probaseinfo genForwardingOperationData(String protype, BigDecimal nodeid, String caseid,
			Probaseinfo probaseinfo, SysUser user) {
				
		probaseinfo.setProtype(protype);
		probaseinfo.setNodeid(nodeid);
		probaseinfo.setSeqno(probaseinfo.getSeqno().add(BigDecimal.ONE));
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_FORWARDING);
		probaseinfo.setCaseid(caseid);
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setStarttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setEndtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		probaseinfo.setUserid(user.getUserid());
		
		return probaseinfo;
	}
	
	/**
	 * @Title: UUID
	 * @Description: 生成32位的UUID
	 * @author ybb
	 * @date 2016年8月15日 下午4:41:49
	 * @return
	 */
	public static String UUID() {
		
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
}