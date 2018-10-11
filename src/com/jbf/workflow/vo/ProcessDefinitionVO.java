/**
 * 流程定义VO
 */
package com.jbf.workflow.vo;

public class ProcessDefinitionVO {

	String key;
	Integer version;

	public ProcessDefinitionVO(String key, Integer version) {
		this.key = key;
		this.version = version;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
