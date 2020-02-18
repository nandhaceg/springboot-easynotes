package com.nandha.easynotes.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.properties.ApplicationProperties;

@Service
public class RabbitMQSender {
	
	private static final Logger logger = LogManager.getLogger(RabbitMQSender.class);
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public void send(Note note) {
		amqpTemplate.convertAndSend(applicationProperties.getExchange(), applicationProperties.getRoutingKey(), note);
		logger.debug("Message Posted to Queue");
	}
}
