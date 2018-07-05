package org.company.tracker;

import java.util.Date;

public class Message {
	private String id;
	private String date;
	private String text;
	private String user;
	private Date formattedDate;
	
	public Message(String id, String date,String text, String user, Date formattedDate){
		this.id = id;
		this.date = date;
		this.text = text;
		this.user = user;
		this.formattedDate = formattedDate;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public Date getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(Date formattedDate) {
		this.formattedDate = formattedDate;
	}
	
}
