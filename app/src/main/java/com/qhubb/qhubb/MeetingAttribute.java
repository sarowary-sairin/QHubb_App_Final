package com.qhubb.qhubb;

public class MeetingAttribute {
	
	
	int id;
	String status;
	String time;
	String date;
	
	
	
	public MeetingAttribute() {
		super();
	}
	public MeetingAttribute(int id, String status, String time,
			String date) {
		super();
		this.id = id;
		
		this.status = status;
		this.time = time;
		this.date = date;
		
	}
	public MeetingAttribute( String status, String time,
			String date) {
		super();
		
		this.status = status;
		this.time = time;
		this.date = date;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getstutus() {
		return status;
	}
	public void setstatus(String status) {
		this.status = status;
	}
	
	


}
