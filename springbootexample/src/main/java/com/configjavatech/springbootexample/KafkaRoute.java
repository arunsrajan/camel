package com.configjavatech.springbootexample;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//kafka-topics.bat --create --topic mytopic --bootstrap-server localhost:9092

//@Component
public class KafkaRoute extends RouteBuilder {

	@Autowired
	private CamelContext context;
	
	@Override
	public void configure() throws Exception {
		context.addComponent("activemq",ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));
		from("file:C:/DEVELOPMENT/orders")
		.to("kafka://orders?brokers=localhost:9092");
		from("kafka://orders?brokers=localhost:9092")
		.log("${body}")
		.split(xpath("/orders/order"))
			.choice()
			.when(xpath("//order/type/text() = 'vegetables'"))
				.to("activemq:queue:vegetable")
			.when(xpath("//order/type/text() = 'fruits'"))
				.to("activemq:queue:fruits")
			.otherwise()
				.to("kafka://product?brokers=localhost:9092");
	}

}
