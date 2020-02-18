package com.nandha.easynotes.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nandha.easynotes.controllers.NoteController;
import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.repository.NoteRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Component
public class HystrixFallback {
	
	private static final Logger logger = LogManager.getLogger(NoteController.class);
	
	@Autowired
	NoteRepository noteRepository;
	
	@HystrixCommand(fallbackMethod = "fallbackHystrix" , commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds" , value = "1000")
	})
	public List<Note> getAllNotes() throws InterruptedException{
		
		logger.debug("Getting All Notes");
		Thread.sleep(3000);
		return noteRepository.findAll();
	}
	
	public List<Note> fallbackHystrix(){
		List<Note> emptyList = new ArrayList<Note>();
		logger.debug("Returning Empty List");
		return emptyList;
	}
}
