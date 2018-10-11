package com.jbf.workflow.vo;

public class EventSourceVO {

	// 事件的源活动节点
	String srcActi;
	
	// 事件的目标活动节点，当事件类型为start,end时，这个参数为空
	String tgtActi;
	
	// 迁称线名称，当事件类型为start,end时，这个参数为空
	String transition;

	/**
	 * 事件类型有start , end ,take三类 <br/>
	 * start指的是活动节点的开始事件 <br/>
	 * end指的是活动节点的结束事件 <br/>
	 * take指的是迁移线的经过事件
	 */
	String type;

	public String getSrcActi() {
		return srcActi;
	}

	public void setSrcActi(String srcActi) {
		this.srcActi = srcActi;
	}

	public String getTgtActi() {
		return tgtActi;
	}

	public void setTgtActi(String tgtActi) {
		this.tgtActi = tgtActi;
	}

	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
