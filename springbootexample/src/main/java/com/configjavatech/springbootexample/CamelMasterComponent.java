package com.configjavatech.springbootexample;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamelMasterComponent extends RouteBuilder {
	
	@Autowired
	CamelContext context;
	
	@Override
	public void configure() throws Exception {
		
		from("zookeeper-master:activemqgroup:activemq:queue:master")
		.delay(4000)
		.log("${body}");
		
		from("zookeeper-master:filegroup:file:/DEVELOPMENT/master")
		.delay(4000)
		.log("${body}");
	}
	
	
	
}
