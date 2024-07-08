package com.example.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessControlConfig implements Filter 
{
	@Value("${cors.urls}")
	private String allowedOrigins;

	private List<String> allowedOriginList;
	
    @Override
    public void init(FilterConfig config) throws ServletException 
    {
    	allowedOriginList = Arrays.asList(allowedOrigins.split(","));
    }
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException 
    {
        final HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        if(origin!=null)
        {   
        	if (allowedOriginList.contains(origin)) 
            {
            	response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Access-Control-Allow-Credentials, Access-Control-Allow-Origin");
                response.setHeader("Access-Control-Max-Age", "3600");
            }
        }
        
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) 
        {
        	response.setStatus(HttpServletResponse.SC_OK);
        } 
        else
        {
        	chain.doFilter(req, res);
        }
    }
    
    @Override
    public void destroy() {}

}