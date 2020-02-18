package com.nandha.easynotes.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nandha.easynotes.model.Note;

@Service
public class RabbitMQReceiver {
	
	private static final Logger logger = LogManager.getLogger(RabbitMQReceiver.class);
	
	@RabbitListener(queues="${easynotes.rabbitmq.queue}")
	public void receivedMessage(Note note) {
		String str = new Gson().toJson(note);
		JSONObject jsonObject = new JSONObject(str);
		logger.debug("Received Message:" + jsonObject);
	}
}
