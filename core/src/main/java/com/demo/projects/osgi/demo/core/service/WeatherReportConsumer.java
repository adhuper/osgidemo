package com.demo.projects.osgi.demo.core.service;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, immediate = true, property = {
		JobConsumer.PROPERTY_TOPICS +"=osgidemo/weatherjob/consumer" })
public class WeatherReportConsumer implements JobConsumer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Override
	public JobResult process(Job arg0) {
		
		// process the job and return the result
		return JobResult.OK;
	}
}
