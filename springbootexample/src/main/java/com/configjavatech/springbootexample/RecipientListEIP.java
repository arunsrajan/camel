package com.configjavatech.springbootexample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RecipientListEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jetty://http://localhost:9090/test")
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				if(exchange.getIn().getHeader("numberOfRecipients", Integer.class) == 1 ) {
					exchange.getIn().setHeader("Url","direct:oneurl");
				}
				else if(exchange.getIn().getHeader("numberOfRecipients", Integer.class) == 2 ) {
					exchange.getIn().setHeader("Url","direct:oneurl, direct:secondurl");
				}
				else {
					exchange.getIn().setHeader("Url","direct:oneurl, direct:secondurl,direct:thirdurl");
				}
			}
			
		})
		.recipientList(header("Url").tokenize(","));
		
		from("direct:oneurl")
		.log("First Url");
		from("direct:secondurl")
		.log("Second Url");
		from("direct:thirdurl")
		.log("Third Url");
	}

}
