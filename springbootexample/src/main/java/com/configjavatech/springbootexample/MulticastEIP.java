package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MulticastEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("jetty://http://0.0.0.0:1090/multicast")
		.log("${body}")
		.multicast().to("direct:firstep","direct:secondep","direct:thirdep");
		
		from("direct:firstep")
		.log("firstep");
		
		from("direct:secondep")
		.log("secondep");
		
		from("direct:thirdep")
		.log("thirdep");
	}

}
