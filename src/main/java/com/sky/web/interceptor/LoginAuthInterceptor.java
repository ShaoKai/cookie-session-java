package com.sky.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sky.web.session.Session;

public class LoginAuthInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LoginAuthInterceptor.class);
	private String loginFailedURL;
	private String sessionAttrName;

	public LoginAuthInterceptor(String loginFailedURL, String sessionAttrName) {
		this.loginFailedURL = loginFailedURL;
		this.sessionAttrName = sessionAttrName;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Session session = (Session) request.getAttribute(sessionAttrName);

		if (session == null || session.getUserInfo() == null) {
			response.sendRedirect(this.loginFailedURL);
			return false;
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
