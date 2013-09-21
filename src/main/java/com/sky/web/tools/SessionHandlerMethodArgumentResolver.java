package com.sky.web.tools;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sky.web.session.Session;

public class SessionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	private String sessionAttrName;

	public SessionHandlerMethodArgumentResolver(String sessionAttrName) {
		super();
		this.sessionAttrName = sessionAttrName;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) {
		Object obj = webRequest.getAttribute(sessionAttrName, NativeWebRequest.SCOPE_REQUEST);
		return obj;
	}

	public boolean supportsParameter(MethodParameter param) {
		return (param.getParameterType().equals(Session.class));
	}
}