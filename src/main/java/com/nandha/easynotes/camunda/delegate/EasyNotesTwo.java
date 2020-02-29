package com.nandha.easynotes.camunda.delegate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class EasyNotesTwo implements JavaDelegate {
	
	private static final Logger logger = LogManager.getLogger(EasyNotesTwo.class);

	@Override
	public void execute(DelegateExecution delegateExecution){
		logger.debug("Inside of EasyNotesTwo");
	}

}
