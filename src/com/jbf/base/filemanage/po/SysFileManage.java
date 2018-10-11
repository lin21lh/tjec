/************************************************************
 * 类名：SysFileManage.java
 *
 * 类别：PO
 * 功能：附件管理PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-7-30  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.filemanage.po;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "SYS_FILEMANAGE")
public class SysFileManage {

	private Long itemid;
	private String elementcode;
	private String keyid;
	private String usercode;
	private String createtime;
	private String title;
	private String originalfilename;
	private String filename;
	private Integer savemode;
	private String filepath;
	private Blob content;
	private String stepid;
	private Long filesize;
	private String remark;
	private String omitFileName;
	private String userName;
	
	public SysFileManage() {
		
	}
	
	public SysFileManage(String elementcode, String keyid, String usercode, String createtime, String title, String originalfilename,
			String filename, Integer savemode,  String filepath, Blob content, String stepid, Long filesize, String remark) {
		this.elementcode = elementcode;
		this.keyid = keyid;
		this.usercode = usercode;
		this.createtime = createtime;
		this.title = title;
		this.originalfilename = originalfilename;
		this.filename = filename;
		this.savemode = savemode;
		this.filepath = filepath;
		this.content = content;
		this.stepid = stepid;
		this.filesize = filesize;
		this.remark = remark;
	}
	
	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_FILEMANAGE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ITEMID", nullable = false, scale = 0)
	public Long getItemid() {
		return itemid;
	}
	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	@Column(name = "ELEMENTCODE", length = 50)
	public String getElementcode() {
		return elementcode;
	}
	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}
	@Column(name = "KEYID", length = 50)
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}

	@Column(name = "USERCODE", length = 50)
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	@Column(name = "CREATETIME", length = 19)
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "ORIGINALFILENAME", length = 255)
	public String getOriginalfilename() {
		return originalfilename;
	}
	public void setOriginalfilename(String originalfilename) {
		this.originalfilename = originalfilename;
	}
	@Column(name = "FILENAME", length = 100)
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	@Column(name = "SAVEMODE", nullable = false, scale = 0)
	public Integer getSavemode() {
		return savemode;
	}
	public void setSavemode(Integer savemode) {
		this.savemode = savemode;
	}
	@Column(name = "FILEPATH", length = 255)
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	@Column(name = "CONTENT")
	public Blob getContent() {
		return content;
	}
	public void setContent(Blob content) {
		this.content = content;
	}
	@Column(name = "STEPID", length = 50)
	public String getStepid() {
		return stepid;
	}
	public void setStepid(String stepid) {
		this.stepid = stepid;
	}
	@Column(name = "FILESIZE", nullable = false, scale = 0)
	public Long getFilesize() {
		return filesize;
	}
	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}
	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public void setOmitFileName(String omitFileName) {
		this.omitFileName = omitFileName;
	}
	@Transient
	public String getOmitFileName() {
		return omitFileName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Formula("(select a.username from sys_user a where  a.usercode=USERCODE)")
	public String getUserName() {
		return userName;
	}
	
	
}
