package com.bill.notificationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilterConfig
{

	@Bean
	CommonsRequestLoggingFilter logFilter()
	{
		CommonsRequestLoggingFilter logFilter = new CommonsRequestLoggingFilter();
		logFilter.setIncludeQueryString(true);
		logFilter.setIncludePayload(true);
		logFilter.setIncludeHeaders(true);
		logFilter.setMaxPayloadLength(10000);
		logFilter.setAfterMessagePrefix("REQUEST DATA: ");
		
		return logFilter;
	}
}