package com.nandha.easynotes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nandha.easynotes.properties.ApplicationProperties;

@Configuration
public class RabbitMQConfig {
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@Bean
	Queue easynotesQueue() {
		return new Queue(applicationProperties.getQueueName(),true);
	}
	
	@Bean
	DirectExchange easynotesExchange() {
		return new DirectExchange(applicationProperties.getExchange(),true,false,null);
	}
	
	@Bean
	Binding easynotesQueuBinding(
			@Qualifier("easynotesQueue")Queue queue,
			@Qualifier("easynotesExchange")DirectExchange exchange) {
		
		return BindingBuilder.bind(queue).to(exchange).with(applicationProperties.getRoutingKey());
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
        @Bean
	public RabbitTemplate rabbitTemplate() {
	final RabbitTemplate template = new RabbitTemplate(connectionFactory());
	template.setMessageConverter(jsonMessageConverter());
	return template;
	}
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory =
		    new CachingConnectionFactory(applicationProperties.getRabbitMQIP());
		connectionFactory.setPort(applicationProperties.getRabbitMQPort());
		connectionFactory.setUsername(applicationProperties.getRabbitMQUserName());
		connectionFactory.setPassword(applicationProperties.getRabbitMQPassword());
		return connectionFactory;
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}
}
