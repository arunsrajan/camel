package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SQLComponentRoute extends RouteBuilder {

	@Bean("mysqlDataSource")
	public BasicDataSource datasource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/productsdb");
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		return dataSource;
	}
	
	@Override
	public void configure() throws Exception {
		
		from("direct:getproductprice")
		.to("sql:select price from products where id = :#${exchangeProperty[productid]}?dataSource=#mysqlDataSource")
		.setBody().simple("${body.get(0).get(\"price\")}");

	}

}
