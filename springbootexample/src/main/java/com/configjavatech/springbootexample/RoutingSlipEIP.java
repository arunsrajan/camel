package com.configjavatech.springbootexample;

import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RoutingSlipEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jetty://http://localhost:9080/routingslip")
		.setBody(constant(""))
		.process(new Processor() {
			Random rand = new Random(System.currentTimeMillis());
			@Override
			public void process(Exchange exchange) throws Exception {
				if(rand.nextBoolean()) {
					exchange.getIn().setHeader("routes", "direct:routeA,direct:routeB");
				} else {
					exchange.getIn().setHeader("routes", "direct:routeB,direct:routeC");
				}
				
			}
			
		})
		.routingSlip(header("routes").tokenize(","))		
		.log("${body}");
		
		
		from("direct:routeA")
		.setBody(body().append("directA"));
		
		from("direct:routeB")
		.setBody(body().append("directB"));
		
		from("direct:routeC")
		.setBody(body().append("directC"));
		
	}

}
