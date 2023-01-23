package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectSeda extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:test?period=5000")
		.to("direct:route1","direct:route2","direct:route3");

		from("direct:route1")
		.log("route1");
		
		from("direct:route2")
		.log("route2");
		
		from("direct:route3")
		.log("route3");
	}

}
