package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedMessageAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class AggregateEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("activemq:queue:aggregate")
		.aggregate(new GroupedMessageAggregationStrategy())
		.constant(true).completionSize(100)
		.split(body())
		.log("${body}");

	}

}
