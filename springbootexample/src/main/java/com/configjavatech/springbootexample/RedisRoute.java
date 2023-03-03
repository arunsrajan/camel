package com.configjavatech.springbootexample;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.redis.RedisConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("jetty://http://0.0.0.0:1090/redisset")
		.setHeader(RedisConstants.COMMAND, constant("SET"))
		.setHeader(RedisConstants.KEY, header("key"))
		.setHeader(RedisConstants.VALUE, header("value"))
		.to("spring-redis://redis1:6379?redisTemplate=#redisTemplate");
		
		from("jetty://http://0.0.0.0:1090/redisget")
		.setHeader(RedisConstants.COMMAND, constant("GET"))
		.setHeader(RedisConstants.KEY, header("key"))
		.to("spring-redis://redis1:6379?redisTemplate=#redisTemplate");

	}
	
	@Bean("redisTemplate")
	public RedisTemplate redisTemplate() {
		RedisClusterConfiguration redis = new RedisClusterConfiguration();
		RedisNode redisNode = new RedisNode("redis1",6379);
		redis.addClusterNode(redisNode);
		redisNode = new RedisNode("redis2",6379);
		redis.addClusterNode(redisNode);
		redisNode = new RedisNode("redis3",6379);
		redis.addClusterNode(redisNode);
		redisNode = new RedisNode("redis4",6379);
		redis.addClusterNode(redisNode);
		redisNode = new RedisNode("redis5",6379);
		redis.addClusterNode(redisNode);
		redisNode = new RedisNode("redis6",6379);
		redis.addClusterNode(redisNode);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redis);
		jedisConnectionFactory.afterPropertiesSet();
		RedisTemplate<StringRedisSerializer,StringRedisSerializer> redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
		
	}

}
