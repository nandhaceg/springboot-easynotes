package com.nandha.easynotes.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
}