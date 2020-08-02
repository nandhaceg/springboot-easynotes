package com.nandha.easynotes.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.nandha.easynotes.model.Note;

@Component
public class KafkaSender {
	
	private static final Logger logger = LogManager.getLogger(KafkaSender.class);
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
    	private KafkaTemplate<String, Note> noteKafkaTemplate;
	
	public void sendStringMessage(String topicName,String message) {
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
				
	    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
	 
	        @Override
	        public void onSuccess(SendResult<String, String> result) {
	            logger.debug("Sent message=[" + message + 
	              "] with offset=[" + result.getRecordMetadata().offset() + "]");
	        }
	        @Override
	        public void onFailure(Throwable ex) {
	            logger.debug("Unable to send message=[" 
	              + message + "] due to : " + ex.getMessage());
	        }
	    });
	}
	
	public void sendNoteMessage(String topicName, Note message) {
		ListenableFuture<SendResult<String, Note>> future = noteKafkaTemplate.send(topicName, message);
				
	    future.addCallback(new ListenableFutureCallback<SendResult<String, Note>>() {
	 
	        @Override
	        public void onSuccess(SendResult<String, Note> result) {
	            logger.debug("Sent message=[" + message + 
	              "] with offset=[" + result.getRecordMetadata().offset() + "]");
	        }
	        @Override
	        public void onFailure(Throwable ex) {
	            logger.debug("Unable to send message=[" 
	              + message + "] due to : " + ex.getMessage());
	        }
	    });
	}
}
