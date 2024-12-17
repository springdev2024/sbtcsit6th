package com.example.sbtcsit6th;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new CustomInterceptor()).addPathPatterns("/*");
//	}
//
//}
//
//class CustomInterceptor implements HandlerInterceptor {
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//
//		System.out.println(request.getRequestURI());
//
//		return true;
//	}
//
//}