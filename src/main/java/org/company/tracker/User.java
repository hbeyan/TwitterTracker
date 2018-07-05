package org.company.tracker;

import java.util.ArrayList;
import java.util.Date;

public class User {
	private String id;
	private String date;
	private String name;
	private Date formattedDate;
	private ArrayList<Message> userMessages;
	
	public User(String id, String date, String name, Date formattedDate){
		this.id = id;
		this.date = date;
		this.name = name;
		this.formattedDate = formattedDate;
		this.userMessages = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(Date formattedDate) {
		this.formattedDate = formattedDate;
	}
	
	public boolean equals(Object obj) {
        return (this.id.equals(((User) obj).id));
	}
	
	public boolean addMessagae(Message msg){
		return userMessages.add(msg);
	}
	
	public void sortMessages(){
		userMessages.sort((o1, o2) -> o1.getFormattedDate().compareTo(o2.getFormattedDate()));
	}
	
	public void printMessages(){
    	for(Message msg:userMessages){
    		System.out.println("\tTweet Id:" + msg.getId() + " User's Id:" + msg.getUser() + " Tweet Date:" + msg.getDate().toString());   	
    		System.out.println("\tText:"  + msg.getText() + "\n");
    	}
	}


}
