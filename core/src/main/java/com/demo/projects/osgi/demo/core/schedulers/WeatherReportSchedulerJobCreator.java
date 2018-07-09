package com.demo.projects.osgi.demo.core.schedulers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.projects.osgi.demo.core.service.WeatherService;

@Designate(ocd = WeatherReportScheduler.Config.class)
@Component(service = Runnable.class)
public class WeatherReportSchedulerJobCreator implements Runnable {

	@ObjectClassDefinition(name = "Weather Report Scheduler", description = "Weather Report scheduler service")
	public static @interface Config {

		@AttributeDefinition(name = "Cron-job expression")
		String scheduler_expression() default "0 0/30 * 1/1 * ? *";

		@AttributeDefinition(name = "Concurrent task", description = "Whether or not to schedule this task concurrently")
		boolean scheduler_concurrent() default false;
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	WeatherService weatherService;

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Reference 
	JobManager jobManager;
	
	
	@Override
	public void run() {
		List<String> cities = weatherService.getCities();
		for (String city : cities) {
			Map<String, Object> jobProperties = new HashMap<String, Object>();
			jobProperties.put("city", city);
			jobManager.addJob("osgidemo/job/consumer/weather", jobProperties);
		}
	}
	@Activate
	protected void activate(final Config config) {
	}

}
