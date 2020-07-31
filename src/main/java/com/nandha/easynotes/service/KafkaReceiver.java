package com.nandha.easynotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.properties.ApplicationProperties;

@Service
public class KafkaReceiver {
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@KafkaListener(topics = "${message.topic.name}", groupId = "group-id")
	public void listen(String message) {
		System.out.println("Received Messasge in group - group-id: " + message);
	}
	
	@KafkaListener(topics = "${note.topic.name}", groupId = "note" , containerFactory = "noteKafkaListenerFactory")
    public void noteListener(Note note) {
        System.out.println("Recieved note message: " + note);
    }
}
