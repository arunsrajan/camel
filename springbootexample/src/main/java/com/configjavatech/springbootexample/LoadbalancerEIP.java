package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class LoadbalancerEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
//		from("timer:lb?period=2000")
//		.loadBalance().roundRobin().to("direct:url1","direct:url2","direct:url3");
		
		
//		from("timer:server?period=2000")
//		.loadBalance().failover().to("http://localhost:9090/path1","http://localhost:9081/path2","http://localhost:9082/path3");
//		
		from("timer:server?period=2000")
		.loadBalance().weighted(true, "4,2,1").to("http://localhost:9090/path1","http://localhost:9081/path2","http://localhost:9082/path3");
		
		from("direct:url1")
		.log("${body} url1");
		
		from("direct:url2")
		.log("url2 ${body}");
		
		
		from("direct:url3")
		.log("url3 ${body}");
		
		from("jetty://http://localhost:9090/path1")
		.log("path1");
		
		from("jetty://http://localhost:9081/path2")
		.log("path2");
		
		from("jetty://http://localhost:9082/path3")
		.log("path3");
	}

}
