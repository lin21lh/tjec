package com.jbf.base.po;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class DBVersionPO {

	private Integer dbversion = 0;
	
	@Column(name = "DBVERSION", nullable = false, precision = 9, scale = 0)
	public Integer getDbversion() {
		return dbversion;
	}
	
	public void setDbversion(Integer dbversion) {
		this.dbversion = dbversion;
	}
}
