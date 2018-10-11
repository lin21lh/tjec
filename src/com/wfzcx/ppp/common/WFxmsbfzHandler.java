package com.wfzcx.ppp.common;

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
 * 项目申报分支处理
 * @ClassName: WFxmsbfzHandler 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年8月26日 上午11:21:59
 */
@Component("com.wfzcx.ppp.common.WFxmsbfzHandler")
public class WFxmsbfzHandler implements WfDecisionBusinessHandler {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ParamCfgComponent pcfg;
	@Override
	public String decide(String execId, String actiId, Map variables,
			SysUser user, ProcessDefinitionVO def){
		String sql = "select zfzyzc from t_xmxx t where t.wfid='"+execId+"'";
		List list = mapDataDao.queryListBySQL(sql);
		if(list.isEmpty()){
			try {
				throw new Exception("未找到wfid为"+execId+"的业务记录！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map map = (Map) list.get(0);
		//是否政府资源支出
		String zfzyzc = StringUtil.stringConvert(map.get("zfzyzc"));
		String outPath ="";
		if(zfzyzc.equals("1")){//是 财政管理，流程结束
			outPath = pcfg.findGeneralParamValue("XMSBFZ", "LCJS");
		}else if(zfzyzc.equals("0")){//否 发送至发改委审批
			outPath = pcfg.findGeneralParamValue("XMSBFZ", "FGWSP");
		}
		return outPath;
	}

}
