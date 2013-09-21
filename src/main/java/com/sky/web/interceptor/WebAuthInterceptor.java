package com.sky.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sky.web.session.Session;
import com.sky.web.session.SessionKey;
import com.sky.web.session.SessionMap;
import com.sky.web.tools.CommonUtils;
import com.sky.web.tools.CookieUtil;

public class WebAuthInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(WebAuthInterceptor.class);
	private LocaleResolver localeResolver;
	private String sessionAttrName;

	public WebAuthInterceptor(LocaleResolver localeResolver, String sessionAttrName) {
		this.localeResolver = localeResolver;
		this.sessionAttrName = sessionAttrName;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		SessionKey key = CookieUtil.getSessionKey();
		Session session = null;
		try {
			if (key != null) {
				session = SessionMap.getInstance().get(key);
			}
			if (key == null || session == null) {
				session = new Session(CommonUtils.genUUID());
				CookieUtil.setSessionKey();
				logger.debug("Session : {}", session.getSessionKey().getUniqueID());
			}
			session.setLocale(localeResolver.resolveLocale(httpRequest));
			httpRequest.setAttribute(sessionAttrName, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

}
