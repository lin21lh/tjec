package com.jbf.sys.notice.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 留言公告阅读记录实体类
 * 
 * @ClassName: SysNoticeRead
 * @Description: 留言公告阅读记录实体类
 * @author songxiaojie
 * @date 2015年5月8日
 */
@Entity
@Table(name = "SYS_NOTICE_READ")
@SequenceGenerator(name="SEQ_NOTICE_READ",sequenceName="SEQ_NOTICE_READ")
@GenericGenerator(name = "SEQ_NOTICE_READ", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_NOTICE_READ") })
public class SysNoticeRead implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	private Long readid;
	
	/**
	 * 公告留言编码
	 */
	private Long noticeid;
	
	/**
	 * 阅读者编码
	 */
	private String readuser;
	
	/**
	 * 阅读时间
	 */
	private String readtime;
	
	/**
	 * 无参构造函数
	 */
	public SysNoticeRead()
	{
		
	}

	@Id
	@Column(name = "READID", nullable = false, scale = 0)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_NOTICE_READ")
	public Long getReadid() {
		return readid;
	}

	public void setReadid(Long readid) {
		this.readid = readid;
	}

	@Column(name = "NOTICEID", nullable = false)
	public Long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(Long noticeid) {
		this.noticeid = noticeid;
	}

	@Column(name = "READUSER", nullable = true, length=20)
	public String getReaduser() {
		return readuser;
	}

	public void setReaduser(String readuser) {
		this.readuser = readuser;
	}

	@Column(name = "READTIME", nullable = true, length=20)
	public String getReadtime() {
		return readtime;
	}

	public void setReadtime(String readtime) {
		this.readtime = readtime;
	}
}
