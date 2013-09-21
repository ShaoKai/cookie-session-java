package com.sky.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.velocity.app.Velocity;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.velocity.VelocityConfig;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.sky.web.interceptor.LoginAuthInterceptor;
import com.sky.web.interceptor.WebAuthInterceptor;
import com.sky.web.session.SessionMap;
import com.sky.web.tools.SessionHandlerMethodArgumentResolver;
import com.sky.web.tools.VelocityMultipleLayoutViewResolver;

@org.springframework.context.annotation.Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	private static String SESSION_ATTR_NAME = "";
	private static String LOCALE_ATTR_NAME = "";

	@Bean
	public ViewResolver getViewResolver() {

		VelocityMultipleLayoutViewResolver resolver = new VelocityMultipleLayoutViewResolver();
		resolver.setCache(false); // TODO enable cache in production
		resolver.setPrefix("");
		resolver.setSuffix(".vm");

		Map<String, String> tplMapping = new LinkedHashMap<String, String>();

		tplMapping.put("*dialog*", "layout/dialogLayout.vm");
		tplMapping.put("*", "layout/indexLayout.vm");

		resolver.setMappings(tplMapping);
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setToolboxConfigLocation("/WEB-INF/velocity-toolbox.xml");
		resolver.setViewClass(org.springframework.web.servlet.view.velocity.VelocityLayoutView.class);
		resolver.setExposeSpringMacroHelpers(true);
		resolver.setExposeRequestAttributes(true);
		resolver.setExposeSessionAttributes(false);

		/* static variable for velocity */
		Properties props = new Properties();
		props.put("conf", "");
		resolver.setAttributes(props);
		return resolver;

	}

	@Bean
	public VelocityConfig velocityConfig() {
		VelocityConfigurer cfg = new VelocityConfigurer();
		cfg.setResourceLoaderPath("/WEB-INF/views/");

		Properties props = new Properties();
		props.put(Velocity.INPUT_ENCODING, "UTF-8");
		props.put(Velocity.OUTPUT_ENCODING, "UTF-8");
		props.put(Velocity.VM_LIBRARY_AUTORELOAD, "true");
		props.put(Velocity.VM_LIBRARY, "velocityMacro.vm");
		// props.put(Velocity.RUNTIME_REFERENCES_STRICT_ESCAPE,true);
		cfg.setVelocityProperties(props);
		return cfg;

	}

	@Bean
	public LoginAuthInterceptor loginAuthInterceptor() {
		String loginFailedURL = "";
		LoginAuthInterceptor interceptor = new LoginAuthInterceptor(loginFailedURL, SESSION_ATTR_NAME);
		return interceptor;
	}

	public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setCookieName(LOCALE_ATTR_NAME);
		localeResolver.setDefaultLocale(new Locale(""));
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName(LOCALE_ATTR_NAME);
		return localeChangeInterceptor;
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new WebAuthInterceptor(localeResolver(), SESSION_ATTR_NAME));
		registry.addInterceptor(loginAuthInterceptor()).addPathPatterns("/home").addPathPatterns("/home/*");
		registry.addInterceptor(localeChangeInterceptor());
	}

	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SessionHandlerMethodArgumentResolver(SESSION_ATTR_NAME));
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/**");
	}

	@Bean
	public CacheManager cacheManager() {
		// Terracotta
		// ....

		// Ehcacahe
		CacheManager cacheManager = CacheManager.create();
		int MAX_CONCURRENT_SESSION = 20000;
		long SESSION_LIFESPAN_SECONDS = 30 * 60;
		cacheManager.addCache(new Cache(SessionMap.class.getSimpleName(), MAX_CONCURRENT_SESSION, false, false, 0, SESSION_LIFESPAN_SECONDS));

		return cacheManager;
	}

}
