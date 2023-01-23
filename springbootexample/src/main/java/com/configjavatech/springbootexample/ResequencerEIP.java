package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ResequencerEIP extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:rand?delay=100")
		.transform().simple("${bean:rand?method=random}")
		.transform().simple("${body.nextInt(100)}")
		.resequence(body()).batch().size(10).timeout(4000L).allowDuplicates().reverse()
		.log("${body}\n");
	}

}
