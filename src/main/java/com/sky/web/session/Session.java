package com.sky.web.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Session implements Serializable {

	private static final long serialVersionUID = -1981150846009031805L;
	private SessionKey key;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private UserInfo userInfo;
	private Locale locale;

	public Session(long userKey) throws Exception {
		key = new SessionKey(userKey);
		SessionMap.getInstance().put(key, this);
	}

	public void deinit() throws Exception {
		attributes.clear();
		SessionMap.getInstance().remove(key);
	}

	public SessionKey getSessionKey() {
		return key;
	}

	public void setAttribute(String attrKey, Object value) {
		attributes.put(attrKey, value);
		try {
			SessionMap.getInstance().replace(key, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getAttribute(String attrKey) {
		return attributes.get(attrKey);
	}

	public void removeAttribute(String attrKey) {
		attributes.remove(attrKey);
		try {
			SessionMap.getInstance().replace(key, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	public void refreshUserInfo() {

	}

	public void setUserInfo(long userKey, String originalPassword) {

	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
