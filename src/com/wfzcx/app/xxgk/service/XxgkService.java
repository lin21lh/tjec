package com.wfzcx.app.xxgk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wfzcx.aros.print.dao.NoticeDao;
import com.wfzcx.aros.print.po.Noticebaseinfo;

@Scope("prototype")
@Service("com.wfzcx.app.xxgk.service.XxgkService")
public class XxgkService {

	@Autowired
	private NoticeDao noticeDao;
	
	public Noticebaseinfo mesDetail(String noticeid){
		return noticeDao.get(noticeid);
	}
}
