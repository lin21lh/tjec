package com.jbf.sys.notice.po;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 留言公告主体信息
 * 
 * @ClassName: SysNotice
 * @Description: 留言公告实体类
 * @author songxiaojie
 * @date 2015年5月8日
 */
@Entity
@Table(name = "SYS_NOTICE")
@SequenceGenerator(name="SEQ_NOTICE",sequenceName="SEQ_NOTICE")
@GenericGenerator(name = "SEQ_NOTICE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_NOTICE") })
public class SysNotice implements Serializable{

	/**
	 * 注释
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	private Long noticeid;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 状态： 0保存，1发布，2撤销发布，3作废'
	 */
	private String status;
	
	/**
	 * 发送时间
	 */
	private String releasetime;
	
	/**
	 * 有效时间
	 */
	private String validtime;
	
	/**
	 * 重要性1=高、2=中、3=低
	 */
	private String priorlevel;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 创建人账号
	 */
	private String usercode;
	
	/**
	 * 类型：1公告，2留言
	 */
	private String messagetype;
	
	/**
	 * 评论是否公开 0：否 1：是
	 */
	private String commentopen;
	
	/**
	 * 阅读次数
	 */
	private Integer readcount;
	
	/**
	 * 是否发送短信 0 否，1 是
	 */
	private String messageflag;
	
	/**
	 * 创建时间
	 */
	private String createtime;
	
	/**
	 * 状态名称
	 */
	private String statusname;
	
	/**
	 * 重要性名称
	 */
	private String priorlevelname;
	
	/**
	 * 短信发送名称
	 */
	private String messageflagname;
	
	/**
	 * 接收人
	 */
	private String receiveusername;
	
	/**
	 * 接收人编码
	 */
	private String receiveusercode;
	
	/**
	 * 无参构造函数
	 */
	public SysNotice()
	{
		
	}

	@Id
	@Column(name = "NOTICEID", nullable = false, scale = 0)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_NOTICE")
	public Long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(Long noticeid) {
		this.noticeid = noticeid;
	}

	@Column(name = "TITLE", nullable = false, length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "CONTENT", nullable = true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "RELEASETIME", nullable = true, length = 20)
	public String getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}

	@Column(name = "VALIDTIME", nullable = true, length = 20)
	public String getValidtime() {
		return validtime;
	}

	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}

	@Column(name = "PRIORLEVEL", nullable = true)
	public String getPriorlevel() {
		return priorlevel;
	}

	public void setPriorlevel(String priorlevel) {
		this.priorlevel = priorlevel;
	}

	@Column(name = "REMARK", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "USERCODE", nullable = false, length = 50)
	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	@Column(name = "MESSAGETYPE", nullable = false)
	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	@Column(name = "COMMENTOPEN", nullable = true)
	public String getCommentopen() {
		return commentopen;
	}

	public void setCommentopen(String commentopen) {
		this.commentopen = commentopen;
	}

	@Column(name = "READCOUNT", nullable = true)
	public Integer getReadcount() {
		return readcount;
	}

	public void setReadcount(Integer readcount) {
		this.readcount = readcount;
	}

	@Column(name = "MESSAGEFLAG", nullable = true)
	public String getMessageflag() {
		return messageflag;
	}

	public void setMessageflag(String messageflag) {
		this.messageflag = messageflag;
	}

	@Column(name = "CREATETIME", nullable = false, length = 20)
	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Formula("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICESTATUS' and t.status=0  and t.code=status)")
	public String getStatusname() {
		return statusname;
	}

	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	@Formula("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICELEVEL' and t.status=0  and t.code=priorlevel)")
	public String getPriorlevelname() {
		return priorlevelname;
	}

	public void setPriorlevelname(String priorlevelname) {
		this.priorlevelname = priorlevelname;
	}

	@Formula("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICESENDMSG' and t.status=0  and t.code=messageflag)")
	public String getMessageflagname() {
		return messageflagname;
	}
	 
	
	public void setMessageflagname(String messageflagname) {
		this.messageflagname = messageflagname;
	}

	@Formula("(select replace(WMSYS.WM_CONCAT(t.username),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=noticeid)")
	public String getReceiveusername() {
		return receiveusername;
	}

	public void setReceiveusername(String receiveusername) {
		this.receiveusername = receiveusername;
	}

	@Formula("(select replace(WMSYS.WM_CONCAT(t.usercode),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=noticeid)")
	public String getReceiveusercode() {
		return receiveusercode;
	}

	public void setReceiveusercode(String receiveusercode) {
		this.receiveusercode = receiveusercode;
	}

}
