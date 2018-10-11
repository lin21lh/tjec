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
 * 留言公告评论实体类
 * 
 * @ClassName: SysNoticeComment
 * @Description: 留言公告评论实体类
 * @author songxiaojie
 * @date 2015年5月8日
 */
@Entity
@Table(name = "SYS_NOTICE_COMMENT")
@SequenceGenerator(name="SEQ_NOTICE_COMMENT",sequenceName="SEQ_NOTICE_COMMENT")
@GenericGenerator(name = "SEQ_NOTICE_COMMENT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_NOTICE_COMMENT") })
public class SysNoticeComment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 评论编码
	 */
	private Long commentid;
	
	/**
	 * 留言公告编码
	 */
	private Long noticeid;
	
	/**
	 * 接收人编码
	 */
	private Long receiveid;
	
	/**
	 * 评论内容
	 */
	private String commentContent;
	
	/**
	 * 评论人
	 */
	private String commentUser;
	
	/**
	 * 评论时间
	 */
	private String commentTime;
	
	/**
	 * 发布者阅读标示
	 */
	private String readFlag;
	
	/**
	 * 无参构造函数
	 */
	public SysNoticeComment()
	{
		
	}
	
	@Id
	@Column(name = "COMMENTID", nullable = false, scale = 0)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_NOTICE_COMMENT")
	public Long getCommentid() {
		return commentid;
	}

	public void setCommentid(Long commentid) {
		this.commentid = commentid;
	}

	@Column(name = "NOTICEID", nullable = false)
	public Long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(Long noticeid) {
		this.noticeid = noticeid;
	}

	@Column(name = "RECEIVEID", nullable = true)
	public Long getReceiveid() {
		return receiveid;
	}

	public void setReceiveid(Long receiveid) {
		this.receiveid = receiveid;
	}

	@Column(name = "COMMENT_CONTENT", nullable = false, length = 500)
	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	@Column(name = "COMMENT_USER", nullable = false, length = 20)
	public String getCommentUser() {
		return commentUser;
	}

	public void setCommentUser(String commentUser) {
		this.commentUser = commentUser;
	}

	@Column(name = "COMMENT_TIME", nullable = false, length = 20)
	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	@Column(name = "READ_FLAG", nullable = true, length = 2)
	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
}
