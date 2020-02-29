package com.nandha.easynotes.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nandha.easynotes.camunda.process.ProcessManager;
import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.properties.ApplicationProperties;
import com.nandha.easynotes.service.HystrixFallback;
import com.nandha.easynotes.service.NoteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/notesapi")
@Api(value = "notesapp")
public class NoteController {

	private static final Logger logger = LogManager.getLogger(NoteController.class);
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@Autowired
	ProcessManager processManager;
	
	@Autowired
	NoteService noteService;

	@Autowired
	HystrixFallback hystrixFallback;
	
	//Hystrix Sample
	@RequestMapping(value = "/notes/hystrix", method = RequestMethod.GET , produces = "application/json")
	@ApiOperation(value = "Hystrix Fallback Method" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Sucessfully called hystix fallback"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public List<Note> getAllNotesHystrix() throws InterruptedException{
		
		logger.debug("Inside Hystrix NoteController");
		return hystrixFallback.getAllNotes();
	}
	
	
	//Validate Note
	@RequestMapping(value = "/notes/schemavalidate", method = RequestMethod.POST , produces = "application/json")
	@ApiOperation(value = "Validate a note" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Note Validated"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public void validateSchema(@Valid @RequestBody Object obj){
		
		logger.debug("Inside Schema Validation");
		
		noteService.validateSchema(obj);
	}
	
	
	//Start Camunda Workflow
	@RequestMapping(value = "/notes/startcamunda", method = RequestMethod.GET , produces = "application/json")
	@ApiOperation(value = "start camunda" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Sucessfully started camunda"),
		@ApiResponse(code = 401 , message = "You are not authorized to start camunda"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public void startCamunda(){
		
		logger.debug("Inside Camunda NoteController");
		processManager.easynotesGetAll();
	}
	
	
	//Get Cloud Config Data
	@RequestMapping(value = "/notes/getclouddata", method = RequestMethod.GET , produces = "application/json")
	@ApiOperation(value = "Get config data" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Sucessfully retrieved config data"),
		@ApiResponse(code = 401 , message = "You are not authorized to get data"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public void getCloudConfig(){
		
		logger.debug("Inside Cloud Config NoteController");
		logger.debug(applicationProperties.getConfigValue());
	}
	
	
	//Run rules engine
	@RequestMapping(value = "/notes/rules", method = RequestMethod.POST , produces = "application/json")
	@ApiOperation(value = "Run rules for note")
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Rule Passes"),
		@ApiResponse(code = 401 , message = "You are not authorized to run the rule"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public ResponseEntity<String> rulesValidation(@Valid @RequestBody Note note){
		
		logger.debug("Inside Run Engine NoteController");
		noteService.droolValidator(note);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	//Get All Notes
	@RequestMapping(value = "/notes", method = RequestMethod.GET , produces = "application/json")
	@ApiOperation(value = "Get all notes" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Sucessfully retrieved list"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public List<Note> getAllNotes(){
		
		logger.debug("Inside Getting all NoteController");
		List<Note> note = noteService.getAllNotes();
		return note;
	}
	
	
	//Create A Note
	@RequestMapping(value = "/notes", method = RequestMethod.POST , produces = "application/json")
	@ApiOperation(value = "Create a note" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Note Created"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public Note createNote(@Valid @RequestBody Note note){
		
		logger.debug("Inside Creating NoteController");
		return noteService.createNote(note);
	}
	
	
	//Create Multiple Note
	@RequestMapping(value = "/notes/all", method = RequestMethod.POST , produces = "application/json")
	@ApiOperation(value = "Create multiple note" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Notes Created"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public List<Note> createMultipleNotes(@Valid @RequestBody List<Note> note){
		
		logger.debug("Inside Creating NoteController");
		return noteService.createMultipleNote(note);
	}
	
	
	//Get A Single Note
	@RequestMapping(value = "/notes/{id}", method = RequestMethod.GET , produces = "application/json")
	@ApiOperation(value = "Get a single note" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Getting a single note"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public Note getNoteById(@PathVariable(value = "id")Long noteId){
		
		logger.debug("Inside Getting a single NoteController");
		Note note = noteService.getNoteById(noteId);
		return note;
	}
	
	
	//Update A Single Note
	@RequestMapping(value = "/notes/{id}", method = RequestMethod.PUT , produces = "application/json")
	@ApiOperation(value = "Update a single note" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Getting a single note"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public Note updateNote(@PathVariable(value = "id")Long noteId , @Valid @RequestBody Note noteDetails){
		
		logger.debug("Inside Updating a single NoteController");
		return noteService.updateNote(noteId,noteDetails);
	}
	
	
	//Deleting A Single Note
	@RequestMapping(value = "/notes/{id}", method = RequestMethod.DELETE , produces = "text/plain")
	@ApiOperation(value = "Delete a note" , response = Note.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200 , message = "Deleting a single note"),
		@ApiResponse(code = 401 , message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403 , message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404 , message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500 , message = "Internal server error")
	})
	public String deleteNote(@PathVariable(value = "id")Long noteId){
		
		logger.debug("Inside Deleting a single NoteController");
		return noteService.deleteNote(noteId);
	}
}
