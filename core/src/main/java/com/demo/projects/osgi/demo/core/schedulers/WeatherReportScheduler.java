package com.demo.projects.osgi.demo.core.schedulers;

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
public class WeatherReportScheduler implements Runnable {

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

	@Override
	public void run() {
		Session session = null;
		try {
			Map<String, Object> subServiceAuthInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,

					(Object) "weather-service");			
			ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(subServiceAuthInfo);

			session = resourceResolver.adaptTo(Session.class);
			Node node = session.getNode("/etc/osgidemo/weather-report");

			List<String> cities = weatherService.getCities();
			for (String city : cities) {
				double temperature = weatherService.getWeather(city);
				if (node.hasNode(city)) {
					Node cityNode = node.getNode(city);
					cityNode.setProperty("temp", temperature);
				} else {
					Node cityNode = node.addNode(city, "nt:unstructured");
					cityNode.setProperty("temp", temperature);
				}
			}
			session.save();
			session.logout();
		} catch (LoginException e) {
			logger.error(e.getMessage(), e);
		} catch (PathNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (RepositoryException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}
		}

	}
	@Activate
	protected void activate(final Config config) {
	}

}
