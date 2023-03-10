package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SplitterEIP extends RouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		
		from("activemq:queue:splitin")
		.log("${body}")
		.split(body().tokenize("\n"))
		.to("direct:line");

		from("direct:line")
		.log("${body}")
		.to("activemq:queue:splitout");

	}

}
