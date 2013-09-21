package com.sky.web.session;

import com.sky.web.IConstants;

public class SessionKey {

	private static final long serialVersionUID = -9148408142895593862L;
	private long uniqueID;

	public SessionKey(long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public SessionKey(String cookie) {
		try {
			uniqueID = Long.parseLong(cookie);
		} catch (Exception e) {
			uniqueID = IConstants.INVALID_SESSION_ID;
		}
	}

	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (!(other instanceof SessionKey)) {
			return false;
		}

		return this.uniqueID == ((SessionKey) other).uniqueID;
	}

	public int hashCode() {
		return (int) (uniqueID ^ (uniqueID >>> 32));
	}

	public long getUniqueID() {
		return uniqueID;
	}

	public String toString() {
		return new Long(uniqueID).toString();
	}
}
