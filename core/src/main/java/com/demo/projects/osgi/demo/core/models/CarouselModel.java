package com.demo.projects.osgi.demo.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselModel {

	@Self
	private SlingHttpServletRequest request;

	//@OSGiService
	//private WeatherService weatherService;

	@ValueMapValue
	private String text;

	@ChildResource
	private Resource links;

	private String message;

	public String getText() {
		return text;
	}

	private String temperature;

	private String num1;

	private List<String> headerLinks;

	@PostConstruct
	private void init() {
		this.message = "This is the message comming from post contstruct method";
		this.num1 = request.getParameter("num1");
		//this.temperature = weatherService.getTemperature("90", "89");
		this.headerLinks = populateModel(this.links);
	}

	public List<String> populateModel(Resource resource) {
		List<String> list = new ArrayList<String>();
		if (resource != null) {
			list = new ArrayList<String>();
			Iterator<Resource> linkResource = resource.listChildren();
			while (linkResource.hasNext()) {
				String text = linkResource.next().getValueMap().get("text", String.class);
				list.add(text);
			}
		}
		return list;
	}

	public String getMessage() {
		return message;
	}

	public String getNum1() {
		return num1;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public List<String> getHeaderLinks() {
		return headerLinks;
	}

	public void setHeaderLinks(List<String> headerLinks) {
		this.headerLinks = headerLinks;
	}
}
