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
 * 留言公告接收人实体类
 * 
 * @ClassName: SysNoticeReceive
 * @Description: 留言公告接收人实体类
 * @author songxiaojie
 * @date 2015年5月8日
 */
@Entity
@Table(name = "SYS_NOTICE_RECEIVE")
@SequenceGenerator(name="SEQ_NOTICE_RECEIVE",sequenceName="SEQ_NOTICE_RECEIVE")
@GenericGenerator(name = "SEQ_NOTICE_RECEIVE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_NOTICE_RECEIVE") })
public class SysNoticeReceive implements Serializable{

	/**
	 * 注释
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	private Long receiveid;
	
	/**
	 * 留言公告编码
	 */
	private Long noticeid;
	
	/**
	 * 接受者编码
	 */
	private String receiveuser;
	
	/**
	 * 阅读标示：0:未阅读 1：已阅读
	 */
	private String readflag;
	
	/**
	 * 阅读时间
	 */
	private String readtime;
	
	/**
	 * 无参构造函数
	 */
	public SysNoticeReceive()
	{
		
	}

	@Id
	@Column(name = "RECEIVEID", nullable = false, scale = 0)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_NOTICE_RECEIVE")
	public Long getReceiveid() {
		return receiveid;
	}

	public void setReceiveid(Long receiveid) {
		this.receiveid = receiveid;
	}

	@Column(name = "NOTICEID", nullable = false)
	public Long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(Long noticeid) {
		this.noticeid = noticeid;
	}

	@Column(name = "RECEIVEUSER", nullable = true, length=20)
	public String getReceiveuser() {
		return receiveuser;
	}

	public void setReceiveuser(String receiveuser) {
		this.receiveuser = receiveuser;
	}

	@Column(name = "READFLAG", nullable = true)
	public String getReadflag() {
		return readflag;
	}

	public void setReadflag(String readflag) {
		this.readflag = readflag;
	}

	@Column(name = "READTIME", nullable = true, length=20)
	public String getReadtime() {
		return readtime;
	}

	public void setReadtime(String readtime) {
		this.readtime = readtime;
	}
}
