package com.demo.projects.osgi.demo.core.use;

import java.util.Date;

import com.adobe.cq.sightly.WCMUsePojo;
import com.demo.projects.osgi.demo.core.Person;

public class CarouselUse extends WCMUsePojo{

	private Person person;
	
	private String message;
	
	@Override
	public void activate() throws Exception {
		this.message = "This is a WCM Use example";
		this.dob = getProperties().get("dob", Date.class).toString();
		long num1 = Long.valueOf(getRequest().getParameter("num1"));
		long num2 = Long.valueOf(getRequest().getParameter("num2"));
		this.addition = num1 + num2;
		
		this.person = new Person();
		person.setAge("5");
		
	}
	
	private String dob;
	private long addition;
	
	
	public String getMessage() {
		return message;
	}


	public long getAddition() {
		return addition;
	}


	public void setAddition(long addition) {
		this.addition = addition;
	}


	public String getDob() {
		return dob;
	}





	public Person getPerson() {
		return person;
	}


	public void setPerson(Person person) {
		this.person = person;
	}

}
