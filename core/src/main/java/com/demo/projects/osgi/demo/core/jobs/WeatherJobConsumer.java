package com.demo.projects.osgi.demo.core.jobs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.projects.osgi.demo.core.service.WeatherService;

@Component(service = JobConsumer.class, immediate = true, property = {
		JobConsumer.PROPERTY_TOPICS + "=osgidemo/job/consumer/weather" })
public class WeatherJobConsumer implements JobConsumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	WeatherService weatherService;

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Override
	public JobResult process(Job arg0) {
		Session session = null;
		try {
			Map<String, Object> subServiceAuthInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
					(Object) "weather-service");
			ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(subServiceAuthInfo);

			session = resourceResolver.adaptTo(Session.class);
			Node node = session.getNode("/etc/osgidemo/weather-report");

			String city = (String) arg0.getProperty("city");
			double temperature = weatherService.getWeather(city);
			if (node.hasNode(city)) {
				Node cityNode = node.getNode(city);
				cityNode.setProperty("temp", temperature);
			} else {
				Node cityNode = node.addNode(city, "nt:unstructured");
				cityNode.setProperty("temp", temperature);
			}

			session.save();
			session.logout();
		} catch (LoginException e) {
			logger.error(e.getMessage(), e);
			return JobResult.FAILED;
		} catch (PathNotFoundException e) {
			logger.error(e.getMessage(), e);
			return JobResult.FAILED;
		} catch (RepositoryException e) {
			logger.error(e.getMessage(), e);
			return JobResult.FAILED;
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}
		}
		return JobResult.OK;
	}

}
