package com.wfzcx.aros.bztx.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 标准体系基本信息表
 * @author zhaoxd
 *
 */
@Entity
@Table(name = "B_STANDBASEINFO")
public class StandBaseInfo implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	
	// Fields
	private String standId;   
	private String standardName; 
	private String stageType;    
	private String sysType;      
	private String remark;       
	private String operator;     
	private String opttime;

	// Constructors

	/** default constructor */
	public StandBaseInfo() {
	}

	/** minimal constructor */
	public StandBaseInfo(String standId) {
		this.standId = standId;
	}

	

	public StandBaseInfo(String standId, String standardName, String stageType, String sysType, String remark,
			String operator, String opttime) {
		super();
		this.standId = standId;
		this.standardName = standardName;
		this.stageType = stageType;
		this.sysType = sysType;
		this.remark = remark;
		this.operator = operator;
		this.opttime = opttime;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "STANDID", unique = true, nullable = false, length = 32)
	public String getStandId() {
		return standId;
	}

	public void setStandId(String standId) {
		this.standId = standId;
	}

	@Column(name = "STANDARDNAME", length = 100)
	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	@Column(name = "STAGETYPE", length = 2)
	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}

	@Column(name = "SYSTYPE", length = 2)
	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "OPERATOR", length = 30)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTTIME", length = 30)
	public String getOpttime() {
		return opttime;
	}

	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}

}
