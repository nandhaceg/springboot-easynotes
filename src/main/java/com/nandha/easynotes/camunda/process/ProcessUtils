package com.nandha.easynotes.camunda.process;

import com.example.easynotes.service.NoteService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ProcessUtils {

	@Autowired private ProcessEngine processEngine;

	private static final Logger logger = LogManager.getLogger(NoteService.class);

	ProcessInstance startProcessInstance(
			String processKey, String businessKey, Map<String, Object> variables) {
		ProcessInstance processInstance;
		if (StringUtils.isEmpty(businessKey)) {
			processInstance =
					processEngine.getRuntimeService().startProcessInstanceByKey(processKey, null, variables);
			logger.info("Started process instance with process definition {}", processKey);
		} else {
			processInstance =
					processEngine
							.getRuntimeService()
							.startProcessInstanceByKey(processKey, businessKey, variables);
			logger.info(
					"Started process instance {} with process definition {}", businessKey, processKey);
		}

		return processInstance;
	}

	void sendMessage(String businessKey, Map<String, Object> expectedVariables, String messageName) {
		logger.info(
				"Send message {} to process by business key {} and variables {}",
				messageName,
				businessKey,
				expectedVariables);
		try {
			MessageCorrelationResult result;
			if (StringUtils.isEmpty(businessKey)) {
				result =
						processEngine
								.getRuntimeService()
								.createMessageCorrelation(messageName)
								.processInstanceVariablesEqual(expectedVariables)
								.correlateWithResult();
			} else {
				result =
						processEngine
								.getRuntimeService()
								.createMessageCorrelation(messageName)
								.processInstanceBusinessKey(businessKey)
								.processInstanceVariablesEqual(expectedVariables)
								.correlateWithResult();
			}
			if (result.getResultType() == MessageCorrelationResultType.Execution) {
				logger.info(
						"Sent message {} to process instance {} with variables {}",
						messageName,
						businessKey,
						expectedVariables);
			} else {
				logger.warn(
						"Unexpected message correlation result {} when sending message {} to process instance {} with variables {}",
						result,
						messageName,
						businessKey,
						expectedVariables);
			}
		} catch (MismatchingMessageCorrelationException ex) {
			logger.warn(
					"Message {} can't be correlated with process instance {} with variables {}",
					messageName,
					businessKey,
					expectedVariables);
		}
	}

	Object getVariable(ProcessInstance processInstance, String variableName) {
		return processEngine.getRuntimeService().getVariable(processInstance.getId(), variableName);
	}

	void setVariable(String processInstanceKey, String variableName, Object variableValue) {
		ProcessInstance processInstance =
				processEngine
						.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceBusinessKey(processInstanceKey)
						.singleResult();
		processEngine
				.getRuntimeService()
				.setVariable(processInstance.getId(), variableName, variableValue);
	}

	void setVariable(
			Map<String, Object> procInstMatching, String variableName, Object variableValue) {
		logger.info(
				"Setting variable {} to {} in process instance that matches with {}",
				variableName,
				variableValue,
				procInstMatching);
		ProcessInstanceQuery query = processEngine.getRuntimeService().createProcessInstanceQuery();
		for (Map.Entry<String, Object> matchingEntry : procInstMatching.entrySet()) {
			query = query.variableValueEquals(matchingEntry.getKey(), matchingEntry.getValue());
		}

		if (query.count() != 1) {
			logger.warn(
					"Variable set {} is matched to {} process instances", procInstMatching, query.count());
			return;
		}

		ProcessInstance processInstance = query.singleResult();
		processEngine
				.getRuntimeService()
				.setVariable(processInstance.getId(), variableName, variableValue);
		logger.info(
				"Finished setting variable {} to {} in process instance {}",
				variableName,
				variableValue,
				processInstance.getId());
	}

	void deleteActiveProcessesInstances() {
		List<ProcessInstance> processInstances =
				processEngine.getRuntimeService().createProcessInstanceQuery().active().list();
		for (ProcessInstance processInstance : processInstances) {
			logger.info(
					"Delete process instance {} of definition {}",
					processInstance.getProcessInstanceId(),
					processInstance.getProcessDefinitionId());
			try {
				processEngine
						.getRuntimeService()
						.deleteProcessInstance(
								processInstance.getProcessInstanceId(), "Clear active process instances");
			} catch (Exception ex) {
				// Deleting a child process instance causes BadUserException.
				logger.info(
						"Unable to delete process instance {} of definition {}",
						processInstance.getProcessInstanceId(),
						processInstance.getProcessDefinitionId());
			}
		}
	}
}
