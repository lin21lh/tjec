package com.wfzcx.ppms.httx.po;

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
 * ProHttx entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_HTTX")
@SequenceGenerator(name="SEQ_PRO_HTTX",sequenceName="SEQ_PRO_HTTX")
@GenericGenerator(name = "SEQ_PRO_HTTX", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_HTTX") })
public class ProHttx implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer httxid;
	private Integer projectid;
	private String htmc;
	private String htlb;
	private String jfmc;
	private String jflxr;
	private String jflxrdh;
	private String jfdz;
	private String yfmc;
	private String yflxr;
	private String yflxrdh;
	private String yfdz;
	private String htqdrq;
	private String htzxksrq;
	private String htzxjsrq;
	private Double htje;
	private String htnr;
	private String htfj;
	private String htzt;
	private String cjr;
	private String cjsj;
	private String xgr;
	private String xgsj;
	private String sfzj;
	private Integer sjhttxid;
	private Integer orgcode;

	// Constructors

	/** default constructor */
	public ProHttx() {
	}

	/** minimal constructor */
	public ProHttx(Integer httxid, Integer projectid, String htmc, String htlb, String jfmc, String jflxr,
			String jflxrdh, String yfmc, String yflxr, String yflxrdh, String htqdrq, String htzxksrq, String htzxjsrq,
			Double htje, String htnr, String htzt, String cjr, String cjsj, String sfzj,String jfdz,String yfdz) {
		this.httxid = httxid;
		this.projectid = projectid;
		this.htmc = htmc;
		this.htlb = htlb;
		this.jfmc = jfmc;
		this.jflxr = jflxr;
		this.jflxrdh = jflxrdh;
		this.yfmc = yfmc;
		this.yflxr = yflxr;
		this.yflxrdh = yflxrdh;
		this.htqdrq = htqdrq;
		this.htzxksrq = htzxksrq;
		this.htzxjsrq = htzxjsrq;
		this.htje = htje;
		this.htnr = htnr;
		this.htzt = htzt;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.sfzj = sfzj;
		this.jfdz = jfdz;
		this.yfdz = yfdz;
	}

	/** full constructor */
	public ProHttx(Integer httxid, Integer projectid, String htmc, String htlb, String jfmc, String jflxr,
			String jflxrdh, String yfmc, String yflxr, String yflxrdh, String htqdrq, String htzxksrq, String htzxjsrq,
			Double htje, String htnr, String htfj, String htzt, String cjr, String cjsj, String xgr, String xgsj,
			String sfzj, Integer sjhttxid,Integer orgcode,String jfdz,String yfdz) {
		this.httxid = httxid;
		this.projectid = projectid;
		this.htmc = htmc;
		this.htlb = htlb;
		this.jfmc = jfmc;
		this.jflxr = jflxr;
		this.jflxrdh = jflxrdh;
		this.yfmc = yfmc;
		this.yflxr = yflxr;
		this.yflxrdh = yflxrdh;
		this.htqdrq = htqdrq;
		this.htzxksrq = htzxksrq;
		this.htzxjsrq = htzxjsrq;
		this.htje = htje;
		this.htnr = htnr;
		this.htfj = htfj;
		this.htzt = htzt;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.xgr = xgr;
		this.xgsj = xgsj;
		this.sfzj = sfzj;
		this.sjhttxid = sjhttxid;
		this.orgcode = orgcode;
		this.jfdz = jfdz;
		this.yfdz = yfdz;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_HTTX")
	@Column(name = "HTTXID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getHttxid() {
		return this.httxid;
	}

	public void setHttxid(Integer httxid) {
		this.httxid = httxid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "HTMC", nullable = false, length = 100)
	public String getHtmc() {
		return this.htmc;
	}

	public void setHtmc(String htmc) {
		this.htmc = htmc;
	}

	@Column(name = "HTLB", nullable = false, length = 2)
	public String getHtlb() {
		return this.htlb;
	}

	public void setHtlb(String htlb) {
		this.htlb = htlb;
	}

	@Column(name = "JFMC", nullable = false, length = 100)
	public String getJfmc() {
		return this.jfmc;
	}

	public void setJfmc(String jfmc) {
		this.jfmc = jfmc;
	}

	@Column(name = "JFLXR", nullable = false, length = 50)
	public String getJflxr() {
		return this.jflxr;
	}

	public void setJflxr(String jflxr) {
		this.jflxr = jflxr;
	}

	@Column(name = "JFLXRDH", nullable = false, length = 50)
	public String getJflxrdh() {
		return this.jflxrdh;
	}

	public void setJflxrdh(String jflxrdh) {
		this.jflxrdh = jflxrdh;
	}

	@Column(name = "JFDZ", nullable = false, length = 100)
	public String getJfdz() {
		return this.jfdz;
	}

	public void setJfdz(String jfdz) {
		this.jfdz = jfdz;
	}
	
	@Column(name = "YFMC", nullable = false, length = 100)
	public String getYfmc() {
		return this.yfmc;
	}

	public void setYfmc(String yfmc) {
		this.yfmc = yfmc;
	}

	@Column(name = "YFLXR", nullable = false, length = 50)
	public String getYflxr() {
		return this.yflxr;
	}

	public void setYflxr(String yflxr) {
		this.yflxr = yflxr;
	}

	@Column(name = "YFLXRDH", nullable = false, length = 50)
	public String getYflxrdh() {
		return this.yflxrdh;
	}

	public void setYflxrdh(String yflxrdh) {
		this.yflxrdh = yflxrdh;
	}

	@Column(name = "YFDZ", nullable = false, length = 100)
	public String getYfdz() {
		return this.yfdz;
	}

	public void setYfdz(String yfdz) {
		this.yfdz = yfdz;
	}
	
	@Column(name = "HTQDRQ", nullable = false, length = 20)
	public String getHtqdrq() {
		return this.htqdrq;
	}

	public void setHtqdrq(String htqdrq) {
		this.htqdrq = htqdrq;
	}

	@Column(name = "HTZXKSRQ", nullable = false, length = 20)
	public String getHtzxksrq() {
		return this.htzxksrq;
	}

	public void setHtzxksrq(String htzxksrq) {
		this.htzxksrq = htzxksrq;
	}

	@Column(name = "HTZXJSRQ", nullable = false, length = 20)
	public String getHtzxjsrq() {
		return this.htzxjsrq;
	}

	public void setHtzxjsrq(String htzxjsrq) {
		this.htzxjsrq = htzxjsrq;
	}

	@Column(name = "HTJE", nullable = false, precision = 20)
	public Double getHtje() {
		return this.htje;
	}

	public void setHtje(Double htje) {
		this.htje = htje;
	}

	@Column(name = "HTNR", nullable = false, length = 1000)
	public String getHtnr() {
		return this.htnr;
	}

	public void setHtnr(String htnr) {
		this.htnr = htnr;
	}

	@Column(name = "HTFJ", length = 100)
	public String getHtfj() {
		return this.htfj;
	}

	public void setHtfj(String htfj) {
		this.htfj = htfj;
	}

	@Column(name = "HTZT", nullable = false, length = 1)
	public String getHtzt() {
		return this.htzt;
	}

	public void setHtzt(String htzt) {
		this.htzt = htzt;
	}

	@Column(name = "CJR", nullable = false, length = 20)
	public String getCjr() {
		return this.cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	@Column(name = "CJSJ", nullable = false, length = 20)
	public String getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	@Column(name = "XGR", length = 20)
	public String getXgr() {
		return this.xgr;
	}

	public void setXgr(String xgr) {
		this.xgr = xgr;
	}

	@Column(name = "XGSJ", length = 20)
	public String getXgsj() {
		return this.xgsj;
	}

	public void setXgsj(String xgsj) {
		this.xgsj = xgsj;
	}

	@Column(name = "SFZJ", nullable = false, length = 1 )
	public String getSfzj() {
		return this.sfzj;
	}

	public void setSfzj(String sfzj) {
		this.sfzj = sfzj;
	}

	@Column(name = "SJHTTXID", precision = 20, scale = 0)
	public Integer getSjhttxid() {
		return this.sjhttxid;
	}

	public void setSjhttxid(Integer sjhttxid) {
		this.sjhttxid = sjhttxid;
	}
	
	@Column(name = "ORGCODE", precision = 20, scale = 0)
	public Integer getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(Integer orgcode) {
		this.orgcode = orgcode;
	}

}