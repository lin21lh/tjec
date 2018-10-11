package com.jbf.sys.notice.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.notice.dao.ISysNoticeDao;
import com.jbf.sys.notice.po.SysNotice;

/**
 * 留言公告管理dao层实现类
 * @ClassName: SysNoticeManageDaoImpl 
 * @Description: (留言公告管理dao层实现类) 
 * @author songxiaojie
 * @date 2015年5月8日 
 */
@Scope("prototype")
@Repository
public class SysNoticeDaoImpl extends GenericDao<SysNotice, Long> implements ISysNoticeDao {

	@Override
	public String getContentByNoticeId(Long noticeId) throws SQLException, IOException {
		
		String sql = "select t.content from sys_notice t where t.noticeid="+noticeId;
		//获取内容
		Clob content = (Clob)find(sql).get(0);
		return this.clobToString(content);
	}

	/**
	 * 将字CLOB转成STRING类型 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public String clobToString(Clob clob) throws SQLException, IOException { 

        String reString = ""; 
        Reader is = clob.getCharacterStream();// 得到流 
        BufferedReader br = new BufferedReader(is); 
        String getVal = br.readLine(); 
        StringBuffer sb = new StringBuffer(); 
        while (getVal != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING 
            sb.append(getVal); 
            getVal = br.readLine(); 
        } 
        reString = sb.toString(); 
        return reString; 
    }

	

}
