package com.configjavatech.springbootexample;

import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ehcache.EhcacheConstants;
import org.springframework.stereotype.Component;

@Component
public class EhCacheComponentRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("file:/DEVELOPMENT/products")
		.split(body().tokenize("\n"))
			.log("New Line ${body}")
			.setBody().simple("${body.split(\",\")}")
			.log("Splitted body ${body[0]} ${body[1]}")
			.setHeader(EhcacheConstants.ACTION,constant(EhcacheConstants.ACTION_PUT))
			.setHeader(EhcacheConstants.KEY,simple("${body[0]}"))
			.setHeader(EhcacheConstants.VALUE,simple("${body[1]}"))		
			.to("ehcache:products")
		.end();
		
		from("jetty://http://0.0.0.0:1090/removeall")
		.setHeader(EhcacheConstants.ACTION,constant(EhcacheConstants.ACTION_CLEAR))
		.to("ehcache:products")
		.setBody(constant("All keys and values removed successfully"));
		
		from("jetty://http://0.0.0.0:1090/get")
		.log("${header[productid]}")
		.setProperty("productid").header("productid")
		.setBody(constant(""))
		.setHeader(EhcacheConstants.ACTION,constant(EhcacheConstants.ACTION_GET))
		.setHeader(EhcacheConstants.KEY,exchangeProperty("productid"))
		.to("ehcache:products")
		.log("body=${body}")
		.choice()
			.when(PredicateBuilder.or(body().isNull(),body().isEqualTo("")))
			.to("direct:getproductprice")
			.setHeader(EhcacheConstants.ACTION,constant(EhcacheConstants.ACTION_PUT))
			.setHeader(EhcacheConstants.KEY,exchangeProperty("productid"))
			.setHeader(EhcacheConstants.VALUE,body())
			.to("ehcache:products")
			.endChoice()
			.end()
		.log("${body}");
		
		from("jetty://http://0.0.0.0:1090/getall")
		.setBody(constant(""))
		.setHeader(EhcacheConstants.ACTION,constant(EhcacheConstants.ACTION_GET_ALL))
		.setHeader(EhcacheConstants.KEYS,header("productid"))
		.to("ehcache:products")
		.log("${body}");
		
	}

}
