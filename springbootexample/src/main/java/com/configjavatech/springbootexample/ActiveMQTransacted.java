package com.configjavatech.springbootexample;

import java.util.concurrent.atomic.AtomicLong;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQTransacted extends RouteBuilder {

	KafkaIdempotentRepository kafkaIdempotentRepository = new KafkaIdempotentRepository("idempotentorders", "kafka:9092");
	
	@Autowired
	CamelContext context;
	
	@Bean("counter")
	@Scope(scopeName = "singleton")
	public AtomicLong counter() {
		return new AtomicLong(0);
	}
	
	@Override
	public void configure() throws Exception {
		
		context.getRegistry().bind("kafkaidempotent", kafkaIdempotentRepository);
		
//		from("activemq:queue:actmq?connectionFactory=#cf&acknowledgementModeName=CLIENT_ACKNOWLEDGE")
//		.split(jsonpath("$..book[*]"))
//		.log("${body}");
		
		from("activemq:topic:orders?connectionFactory=#cf&acknowledgementModeName=CLIENT_ACKNOWLEDGE")
		.split(jsonpath("$..book"))
		.log("${body}")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				JmsMessage jmsmessage = exchange.getIn().getBody(JmsMessage.class);
				Thread.sleep(6000);
				System.out.println("JMSMessage transaction");
				jmsmessage.getJmsMessage().acknowledge();
				System.out.println("JMSMessage transaction sleep start");
				Thread.sleep(6000);
				System.out.println("JMSMessage transaction sleep");
			}
			
		});
		
//		from("activemq:topic:orders?connectionFactory=#cf&subscriptionDurable=true&acknowledgementModeName=CLIENT_ACKNOWLEDGE&clientId=MyClientId1&durableSubscriptionName=MyConsumer1")
//		.split(jsonpath("$..book[*]"))
//		.log("MyClientId1 ${body}");
//		
//		from("activemq:topic:orders?connectionFactory=#cf&subscriptionDurable=true&acknowledgementModeName=CLIENT_ACKNOWLEDGE&clientId=MyClientId2&durableSubscriptionName=MyConsumer1")
//		.split(jsonpath("$..book[*]"))
//		.log("MyClientId2 ${body}");
		
		from("activemq:queue:idempotentorders?connectionFactory=#cf&acknowledgementModeName=CLIENT_ACKNOWLEDGE")
		.idempotentConsumer(body()).idempotentRepository("kafkaidempotent").log("first ${body}");
		
		
	}

	@Bean("cf")
	public ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory();
	}

}
