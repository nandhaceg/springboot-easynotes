package com.nandha.easynotes.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@RefreshScope
public class ApplicationProperties {

	@Value("${config.Value:defaultValue}")
	private String configValue;
	
	@Value("${easynotes.rabbitmq.queue:easynotes.queue}")
	private String queueName;
	
	@Value("${easynotes.rabbitmq.exchange:easynotes.exchange}")
	private String exchange;
	
	@Value("${easynotes.rabbitmq.routingkey:easynotes.routingkey}")
	private String routingKey;
	
	@Value("${spring.rabbitmq.host:127.0.0.1}")
	private String rabbitMQIP;
	
	@Value("${spring.rabbitmq.port:5672}")
	private int rabbitMQPort;

	@Value("${spring.rabbitmq.username:guest}")
	private String rabbitMQUserName;
	
	@Value("${spring.rabbitmq.password:guest}")
	private String rabbitMQPassword;
	
	@Value("${spring.kafka.consumer.bootstrap-servers:localhost:9092}")
	private String bootstrapAddress;
	
	@Value("${message.topic.name:test}")
	private String stringTopic;
	
	@Value("${note.topic.name:note}")
	private String noteTopic;
	
	public String getRabbitMQIP() {
		return rabbitMQIP;
	}

	public void setRabbitMQIP(String rabbitMQIP) {
		this.rabbitMQIP = rabbitMQIP;
	}

	public int getRabbitMQPort() {
		return rabbitMQPort;
	}

	public void setRabbitMQPort(int rabbitMQPort) {
		this.rabbitMQPort = rabbitMQPort;
	}

	public String getRabbitMQUserName() {
		return rabbitMQUserName;
	}

	public void setRabbitMQUserName(String rabbitMQUserName) {
		this.rabbitMQUserName = rabbitMQUserName;
	}

	public String getRabbitMQPassword() {
		return rabbitMQPassword;
	}

	public void setRabbitMQPassword(String rabbitMQPassword) {
		this.rabbitMQPassword = rabbitMQPassword;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
	public String getBootstrapAddress() {
		return bootstrapAddress;
	}

	public void setBootstrapAddress(String bootstrapAddress) {
		this.bootstrapAddress = bootstrapAddress;
	}

	public String getStringTopic() {
		return stringTopic;
	}

	public void setStringTopic(String stringTopic) {
		this.stringTopic = stringTopic;
	}

	public String getNoteTopic() {
		return noteTopic;
	}

	public void setNoteTopic(String noteTopic) {
		this.noteTopic = noteTopic;
	}
	
}
