package com.freework.queryData.control;

import javax.servlet.http.HttpSession;

public class SessionValueImpl implements SessionValue {
	@Override
	public Object getValue(HttpSession session, String name) {
		if(session==null)
			return null;
		return session.getAttribute(name);
	}

}
