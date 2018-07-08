package com.demo.projects.osgi.demo.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.demo.projects.osgi.demo.core.pojo.Link;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

	@ValueMapValue
	private String logoText;
	
	private List<Link> headerLinks;
	
	@ChildResource
	private Resource links;

	public String getLogoText() {
		return logoText;
	}

	public void setLogoText(String logoText) {
		this.logoText = logoText;
	}

	@PostConstruct
	protected void init() {
		this.headerLinks = new ArrayList<>();
		if(links != null){
			Iterator<Resource> linksIterator = links.listChildren();
			while(linksIterator.hasNext()){
				Resource linkResource = linksIterator.next();
				String listTitle = linkResource.getValueMap().get("linktitle", String.class);
				String path = linkResource.getValueMap().get("path", String.class);
				Link link = new Link();
				link.setLink(path);
				link.setTitle(listTitle);
				this.headerLinks.add(link);
			}
		}
	}

	public List<Link> getHeaderLinks() {
		return headerLinks;
	}

	public void setHeaderLinks(List<Link> headerLinks) {
		this.headerLinks = headerLinks;
	}
}
