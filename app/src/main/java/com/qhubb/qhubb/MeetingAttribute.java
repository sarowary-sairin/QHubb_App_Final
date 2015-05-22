/*Created by: Sairin Sadique and Sarowary Khan*/
package com.qhubb.qhubb;

public class MeetingAttribute {
	
	
	int id; /*element id*/
	String status; /*element status*/
	String time; /*element time*/
	String date; /*element date*/
	
	
	
	public MeetingAttribute() {
		super();
	}
	public MeetingAttribute(int id, String status, String time,
			String date) {
		super();
		this.id = id; /*element's ID*/
		
		this.status = status; /*element's status*/
		this.time = time; /*element's time*/
		this.date = date; /*element's date*/
		
	}
	public MeetingAttribute( String status, String time,
			String date) {
		super();
		
		this.status = status; /*element's status*/
		this.time = time; /*element's time*/
		this.date = date; /*element's date*/
		
	}
	public int getId() {
		return id; /*get id*/
	}
	public void setId(int id) {
		this.id = id; /*set id*/
	}
	
	
	public String getTime() {
		return time; /*get time*/
	}
	public void setTime(String time) {
		this.time = time; /*set time*/
	}
	public String getDate() {
		return date; /*get date*/
	}
	public void setDate(String date) {
		this.date = date; /*set date*/
	}
	public String getstutus() {
		return status; /*get status*/
	}
	public void setstatus(String status) {
		this.status = status; /*set status*/
	}
	
	


}
