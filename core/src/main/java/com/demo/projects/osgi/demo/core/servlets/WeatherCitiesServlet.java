package com.demo.projects.osgi.demo.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.demo.projects.osgi.demo.core.service.WeatherService;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Weather Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "osgidemo/weathercities" })
public class WeatherCitiesServlet extends SlingSafeMethodsServlet {

	@Reference
	WeatherService weatherService;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		// set fallback
		request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
		ResourceResolver resolver = request.getResourceResolver();
		// Create an ArrayList to hold data
		List<Resource> fakeResourceList = new ArrayList<Resource>();
		ValueMap vm = null;
		// Add 5 values to drop down!
		for (int i = 0; i < weatherService.getCities().size(); i++) {
			// allocate memory to the Map instance
			vm = new ValueMapDecorator(new HashMap<String, Object>());
			// Specify the value and text values
			String Value = weatherService.getCities().get(i);
			String Text = weatherService.getCities().get(i).toUpperCase();
			// populate the map
			vm.put("value", Value);
			vm.put("text", Text);
			fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
		}
		// Create a DataSource that is used to populate the drop-down control
		DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
		request.setAttribute(DataSource.class.getName(), ds);

	}
}
