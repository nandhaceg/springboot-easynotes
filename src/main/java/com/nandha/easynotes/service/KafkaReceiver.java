package com.nandha.easynotes.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.properties.ApplicationProperties;

@Service
public class KafkaReceiver {
	
	private static final Logger logger = LogManager.getLogger(KafkaReceiver.class);
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@KafkaListener(topics = "${message.topic.name}", groupId = "group-id")
	public void listen(String message) {
		logger.debug("Received Messasge in group - group-id: " + message);
	}
	
	@KafkaListener(topics = "${note.topic.name}", groupId = "note" , containerFactory = "noteKafkaListenerFactory")
 	public void noteListener(Note note) {
		logger.debug("Recieved note message: " + note);
    	}
}
