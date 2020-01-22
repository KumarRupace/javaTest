package com.java.dao;

import javax.persistence.*;

//Author: Zhang Xin

@Entity
public class Customer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String firstName;
	private String lastName;
	private long number;
	@Column(unique = true)
	private String nric;
	
	
	public Customer(){}
	public Customer(String nric, String firstName, String lastName, long number){
		
		this.nric = nric;
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
	}

	//Getter and setter methods
	public String getNric() {
		return nric;
	}

	public void setNric(String value) {
		nric = value;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String value){
		firstName = value;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public void setLastName(String value){
		lastName = value;
	}
	
	public long getNumber(){
		return number;
	}
	
	public void setNumber(long value){
		number = value;
	}
	
	//Organise and print out customer's information
	@Override
	public String toString() {
    return String.format(
        "Customer[id=%d, nric='%s', firstName='%s', lastName='%s', number=%d]",
        id, nric, firstName, lastName, number);
  }

}
