package com.wfzcx.fam.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.workflow.common.BussinessDataStatusInjectionHandler;

@Component("com.wfzcx.fam.common.WFStatusInjectionHandler")
public class WFStatusInjectionHandler implements BussinessDataStatusInjectionHandler {
	@Autowired
	MapDataDaoI mapDataDao;
	@Override
	public void inject(String key, String verison, String actiId,
			String transition, String execId, String status,
			String handleUserName) throws Exception {
		String sql ="update fa_applications set wfstatus='"+status+"' where wfid='"+execId+"'";
		mapDataDao.updateTX(sql);
	}

}
