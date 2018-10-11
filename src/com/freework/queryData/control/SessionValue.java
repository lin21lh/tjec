package com.freework.queryData.control;

import javax.servlet.http.HttpSession;

public interface SessionValue {
	public Object getValue(HttpSession session,String name);
}
