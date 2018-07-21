package com.demo.projects.osgi.demo.core.workflow;

import org.osgi.service.component.annotations.Component;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(property = { "chooser.label=AEM Training Approver Chooser" })
public class DynamicApproverSelection implements ParticipantStepChooser {

	@Override
	public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaData)
			throws WorkflowException {

		String pagePath = workItem.getWorkflowData().getPayload().toString();
		if (pagePath.startsWith("/content/we-retail")) {
			return "amit";
		} else {
			return "admin";
		}
	}

}
