package com.wfzcx.fam.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.common.WfDecisionBusinessHandler;
import com.jbf.workflow.vo.ProcessDefinitionVO;
/**
 * 根据业务流程的内容反馈相应流程路径
 * @ClassName: WFaccountChangeHandler 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年8月26日 上午11:21:59
 */
@Component("com.wfzcx.fam.common.WFaccountChangeHandler")
public class WFaccountChangeHandler implements WfDecisionBusinessHandler {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ParamCfgComponent pcfg;
	@Override
	public String decide(String execId, String actiId, Map variables,
			SysUser user, ProcessDefinitionVO def){
		String sql = "select changetype from fa_applications t where t.wfid='"+execId+"'";
		List list = mapDataDao.queryListBySQL(sql);
		if(list.isEmpty()){
			try {
				throw new Exception("未找到wfid为"+execId+"的业务记录！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map map = (Map) list.get(0);
		String changeType = StringUtil.stringConvert(map.get("changetype"));
		String outPath = pcfg.findGeneralParamValue("ZHBGFZ", changeType);
		return outPath;
	}

}
