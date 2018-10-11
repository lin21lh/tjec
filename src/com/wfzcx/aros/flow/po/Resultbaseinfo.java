package com.wfzcx.aros.flow.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: Resultbaseinfo
 * @Description: 流程结果实体bean
 * @author MyEclipse Persistence Tools
 * @date 2016年8月17日 下午5:49:18
 * @version V1.0
 */
@Entity
@Table(name = "PUB_RESULTBASEINFO" )
public class Resultbaseinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	
	private String id;						//序列号
	private String processid;				//流程ID
	private String protype;					//流程类型
	private BigDecimal nodeid;				//节点编号
	private BigDecimal seqno;				//处理序列号
	private String opttype;					//处理标志
	private String caseid;					//案件ID
	private String operator;				//处理人
	private String starttime;				//接受时间
	private String endtime;					//结束时间
	private String result;					//处理结果/是否同意
	private String remark;					//处理意见
	private String reason;					//原因
	private Long userid; 					//用户ID
	private String sendunit;				//转送机关
	private String resultmsg;				//结果中文

	// Constructors

	/** default constructor */
	public Resultbaseinfo() {
	}

	/** minimal constructor */
	public Resultbaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public Resultbaseinfo(String id, String processid, String protype,
			BigDecimal nodeid, BigDecimal seqno, String opttype, String caseid,
			String operator, String starttime, String endtime, String result,
			String remark, String reason, Long userid, String sendunit, String resultmsg) {
		this.id = id;
		this.processid = processid;
		this.protype = protype;
		this.nodeid = nodeid;
		this.seqno = seqno;
		this.opttype = opttype;
		this.caseid = caseid;
		this.operator = operator;
		this.starttime = starttime;
		this.endtime = endtime;
		this.result = result;
		this.remark = remark;
		this.reason = reason;
		this.userid = userid;
		this.sendunit = sendunit;
		this.resultmsg = resultmsg;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PROCESSID", length = 32)
	public String getProcessid() {
		return this.processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	@Column(name = "PROTYPE", length = 2)
	public String getProtype() {
		return this.protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	@Column(name = "NODEID", precision = 22, scale = 0)
	public BigDecimal getNodeid() {
		return this.nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}

	@Column(name = "SEQNO", precision = 22, scale = 0)
	public BigDecimal getSeqno() {
		return this.seqno;
	}

	public void setSeqno(BigDecimal seqno) {
		this.seqno = seqno;
	}

	@Column(name = "OPTTYPE", length = 2)
	public String getOpttype() {
		return this.opttype;
	}

	public void setOpttype(String opttype) {
		this.opttype = opttype;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "STARTTIME", length = 30)
	public String getStarttime() {
		return this.starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	@Column(name = "ENDTIME", length = 30)
	public String getEndtime() {
		return this.endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Column(name = "RESULT", length = 2)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "REASON", length = 2)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "USERID", length = 19)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	@Column(name = "SENDUNIT", length = 100)
	public String getSendunit() {
		return sendunit;
	}

	public void setSendunit(String sendunit) {
		this.sendunit = sendunit;
	}

	@Column(name = "RESULTMSG", length = 100)
	public String getResultmsg() {
		return resultmsg;
	}

	public void setResultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}
}