package com.demo.projects.osgi.demo.core.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.projects.osgi.demo.core.WeatherServletResponse;
import com.demo.projects.osgi.demo.core.service.WeatherService;
import com.google.gson.Gson;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Weather Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/osgidemo/get/weather",
		"sling.servlet.extensions=" + "json" })
public class WeatherServlet extends SlingSafeMethodsServlet {

	/**
	* 
	*/
	private static final long serialVersionUID = -2311664877357015713L;
	private static final long serialVersionUid = 1L;

	@Reference
	private ResourceResolverFactory resolverFactory;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		String cityName = req.getParameter("cityName");
		try {
			Session session = req.getResourceResolver().adaptTo(Session.class);
			Node node = session.getNode("/etc/osgidemo/weather-report/" + cityName);
			Property prop = node.getProperty("temp");
			double temperature = prop.getValue().getDouble();
			WeatherServletResponse wsr = new WeatherServletResponse();
			wsr.setTemperature(String.valueOf(temperature));
			resp.getWriter().write(new Gson().toJson(wsr));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
