package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.apache.camel.builder.PredicateBuilder.or;

import org.apache.camel.CamelContext;

@Component
public class MessageFilterEIP extends RouteBuilder {
	
	@Autowired
	CamelContext context;

	@Override
	public void configure() throws Exception {
		context.addComponent("ActiveMQ",ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
		from("ActiveMQ:queue:products")
		.log("${body}")
		.split(xpath("/orders/order"))
		.filter(or(xpath("//order/type/text() = 'pencil'"),xpath("//order/type/text() = 'pen'")))
		.to("ActiveMQ:queue:pencilpenqueue");

	}

}
