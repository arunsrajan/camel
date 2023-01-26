package com.configjavatech.springbootexample;

import static org.apache.camel.support.builder.PredicateBuilder.or;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageFilterEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("activemq:queue:products")
		.log("${body}")
		.split(xpath("/orders/order"))
		.filter(or(xpath("//order/type/text() = 'pencil'"),xpath("//order/type/text() = 'pen'")))
		.to("activemq:queue:pencilpenqueue");

	}

}
