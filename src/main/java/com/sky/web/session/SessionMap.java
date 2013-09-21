package com.sky.web.session;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionMap {
	private static final Logger logger = LoggerFactory.getLogger(SessionMap.class);
	private static SessionMap singleton;
	private Cache cache;
	private CacheManager cacheManager;

	private SessionMap() throws Exception {
		this.cacheManager = CacheManager.getInstance();
		this.cache = cacheManager.getCache(this.getClass().getSimpleName());
	}

	public synchronized Session get(SessionKey key) {
		try {
			Element element = (Element) this.cache.get(key);
			if (element != null) {
				return (Session) element.getObjectValue();
			} else {
				logger.info("session timeout");
			}
		} catch (Exception e) {
			logger.error(key.toString());
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void replace(SessionKey key, Session session) {
		Element element = new Element(key, session);
		this.cache.replace(element);
	}

	public synchronized void put(SessionKey key, Session session) {
		Element element = new Element(key, session);
		this.cache.put(element);
	}

	public synchronized void remove(SessionKey key) {
		cache.remove(key);
	}

	public static SessionMap getInstance() throws Exception {
		if (singleton == null) {
			return createInstance();
		}
		return singleton;
	}

	private static SessionMap createInstance() throws Exception {
		if (singleton == null) {
			singleton = new SessionMap();
		}
		return singleton;
	}

	public void destroy() {
		this.cacheManager.shutdown();
	}
}