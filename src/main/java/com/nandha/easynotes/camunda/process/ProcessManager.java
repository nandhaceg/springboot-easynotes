package com.nandha.easynotes.camunda.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessManager {

	private static final Logger logger = LogManager.getLogger(ProcessManager.class);

	@Autowired ProcessUtils processUtils;

}
