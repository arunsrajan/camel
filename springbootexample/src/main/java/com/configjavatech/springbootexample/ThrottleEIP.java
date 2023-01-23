package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ThrottleEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("activemq:queue:throttle")
		.throttle(10).timePeriodMillis(3000)
		.threads(10)
		.to("seda:process");
		
		from("seda:process")
		.log("${body}");
	}

}
