package com.nandha.easynotes.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nandha.easynotes.controllers.NoteController;
import com.nandha.easynotes.exception.ResourceNotFoundException;
import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.repository.NoteRepository;

@Service
@CacheConfig(cacheNames = "notesCache")
public class NoteService {
	
	private static final Logger logger = LogManager.getLogger(NoteService.class);

	@Autowired
	NoteRepository noteRepository;
	
	@Autowired
	RabbitMQSender rabbitMQSender;
	
	@Autowired
	KieContainer kieContainer;
	
	@Cacheable
	public List<Note> getAllNotes(){
		
		logger.debug("Getting All Notes");
		return noteRepository.findAll();
	}
	
	@CacheEvict(allEntries = true)
	public Note createNote(Note note) {
		
		Note noteResponse = noteRepository.save(note);
		logger.debug("Creating a Note");
		rabbitMQSender.send(note);
		return noteResponse;
	}
	
	@CacheEvict(allEntries = true)
	public List<Note> createMultipleNote(List<Note> note){
		
		List<Note> noteResponse = noteRepository.saveAll(note);
		logger.debug("Creating multiple Notes");
		return noteResponse;
	}
	
	@Cacheable
	public Note getNoteById(Long noteId) {
		
		logger.debug("Getting note by Id"+noteId);
		return noteRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note" , "id" , noteId));
	}
	
	@CacheEvict(allEntries = true)
	public Note updateNote(Long noteId , Note noteDetails) {
		
		Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
		
		note.setTitle(noteDetails.getTitle());
		note.setContent(noteDetails.getContent());
		
		Note noteResponse = noteRepository.save(note);
		
		logger.debug("Updating Note by Id"+noteId);
		return noteResponse;
	}
	
	@CacheEvict(allEntries = true)
	public String deleteNote(Long noteId) {
		
		noteRepository.deleteById(noteId);
		return "Note deleted sucessfully";
	}
	
	public void validateSchema(Object obj) {
		
		String str = new Gson().toJson(obj);
		JSONObject jsonObject = new JSONObject(str);
		JSONObject jsonSchema = new JSONObject(
				new JSONTokener(NoteController.class.getResourceAsStream("/jsonSchema/noteSchema.json")));
		Schema schema = SchemaLoader.load(jsonSchema);
	    schema.validate(jsonObject);
	}
	
	public void droolValidator(Note note) {
		KieSession kieSession = kieContainer.newKieSession();
		kieSession.insert(note);
		kieSession.fireAllRules();
		kieSession.dispose();
	}
}