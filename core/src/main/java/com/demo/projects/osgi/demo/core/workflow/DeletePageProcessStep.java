package com.demo.projects.osgi.demo.core.workflow;

import java.util.Collections;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

@Component(property = { "process.label=AEM Training Delete Process Step" })
public class DeletePageProcessStep implements WorkflowProcess {

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		String pagePath = workItem.getWorkflowData().getPayload().toString();

		Map<String, Object> subServiceAuthInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
				(Object) "osgidemo-workflow-service");
		try {
			ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(subServiceAuthInfo);
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			Page page = pageManager.getPage(pagePath);
			if (page != null) {
				pageManager.delete(page, false, true);
			}
			resourceResolver.close();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WCMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
