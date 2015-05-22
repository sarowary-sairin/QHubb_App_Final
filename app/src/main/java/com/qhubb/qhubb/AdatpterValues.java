/*Created by: Sairin Sadique and Sarowary Khan*/

package com.qhubb.qhubb;

public class AdatpterValues {
	
	
	int id;
	String status; /*status for queue post*/
	String time; /*time for queue post*/
	String date; /*date for queue post**/
	
	
	
	public AdatpterValues() {
		super();
	}
	public AdatpterValues(int id, String status, String time,
			String date) {
		super();
		this.id = id; /*element's ID for queue post*/
		
		this.status = status; /*element's status for queue post*/
		this.time = time; /*element's time for queue post*/
		this.date = date; /*element's date for queue post*/
		
	}
	public AdatpterValues( String status, String time,
			String date) {
		super();
		
		this.status = status; /*element's status for queue post*/
		this.time = time; /*element's date for queue post*/
		this.date = date; /*element's time for queue post*/
		
	}
	public int getId() {
		return id; /*get ID for queue post*/
	}
	public void setId(int id) {
		this.id = id; /*set ID for queue post*/
	}
	
	
	public String getTime() {
		return time; /*get time for queue post*/
	}
	public void setTime(String time) {
		this.time = time; /*set time for queue post*/
	}
	public String getDate() {
		return date; /*get date for queue post*/
	}
	public void setDate(String date) {
		this.date = date; /*set date for queue post*/
	}
	public String getstutus() {
		return status; /*get status for queue post*/
	}
	public void setstatus(String status) {
		this.status = status; /*get status for queue post*/
	}
}
