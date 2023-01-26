package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//kafka-topics.bat --create --topic mytopic --bootstrap-server localhost:9092

//@Component
public class KafkaRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
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
