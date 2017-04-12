package org.ender.webservices.employeeManagement.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {//employee class holding employee data (firstname, lastname, position, id)
	
	private String firstName;
	private String lastName;
	private String position;
	private String id;
	
	public Employee(){}	//empty constructor needed for JSON support

	public Employee(String firstName, String lastName, String position,String id){
		
		this.firstName=firstName;
		this.lastName=lastName;
		this.position=position;
		this.id=id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
