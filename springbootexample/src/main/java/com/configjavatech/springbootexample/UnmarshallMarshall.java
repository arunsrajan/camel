package com.configjavatech.springbootexample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class UnmarshallMarshall extends RouteBuilder {

	JacksonXMLDataFormat jacksonDataFormat = new JacksonXMLDataFormat();
	
	@Override
	public void configure() throws Exception {
		jacksonDataFormat.setUnmarshalType(Order.class);
		from("file:/DEVELOPMENT/orderstransform")
		.split(xpath("/orders/order"))
		.unmarshal(jacksonDataFormat)
		.to("direct:datetime")
		.aggregate(new GroupedBodyAggregationStrategy())
		.constant(true).completionSize(header(Exchange.SPLIT_SIZE))
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				OrdersFeed orders = new OrdersFeed();
				List<Order> orderL = (List)exchange.getIn().getBody(List.class);
				List<Order> ordersL = new ArrayList<>(orderL);
				
				orders.setOrders(ordersL);
				exchange.getIn().setBody(orders);
			}
			
		})
		.marshal().jacksonXml(true)
		.to("activemq:queue:orders");
		
		
		
		from("direct:datetime")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				Order order = exchange.getIn().getBody(Order.class);
				order.setDate(new Date().toString());
			}
			
		});

	}

}
