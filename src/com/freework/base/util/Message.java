package com.freework.base.util;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 *
 * @author cfl
 */
@XmlRootElement(name = "msg")
public class Message implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String info;
	
    private boolean success;
  
	private Map<String,String> userData;
	public Message(){	super();}
	public Message(boolean success,String info){
		this.info = info;
		this.success = success;
	}
	public Message(String info, boolean success, Map<String,String> userData) {
			super();
			this.info = info;
			this.success = success;
			this.userData = userData;
	}
	@XmlAttribute
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    @XmlAttribute
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the userData
     */
    @XmlElement()
    @XmlJavaTypeAdapter(MapAdapter.class)
    public Map<String,String> getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(Map<String,String> userData) {
        this.userData = userData;
    }
    
    public Message setUserData(String key,String value) {
    	if(userData==null){
    		userData=new HashMap<String,String>();
    	}
    	userData.put(key, value);
    	return this;
    }

}
