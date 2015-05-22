/*Created by: Sairin Sadique and Sarowary Khan*/
package com.qhubb.qhubb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

public class Tweetqueue extends Activity{
	
	DatabaseHandler db; /*database handler*/
	List<MeetingAttribute> contacts;  /*queue data*/
	public void postqueue()
	{
		DatabaseHandler db; /*database handler*/
		List<MeetingAttribute> contacts; /*queue data*/
		Calendar c = Calendar.getInstance(); /*calender instance*/
		db=new DatabaseHandler(Tweetqueue.this); /*Call on database handler*/
		
	      
	        
	        contacts = db.getAllQueuedata(); /*get all queue data*/
			 
	        for (MeetingAttribute cn : contacts) {
					String[] time = cn.getTime().split(":"); /*get time*/
					int hours=Integer.parseInt(time[0]); /*get hours*/
					int mint=Integer.parseInt(time[1]); /*get minute*/
					String[] date=cn.getDate().split("/"); /*get date*/
					int month=Integer.parseInt(date[0]); /*get month*/
					int day=Integer.parseInt(date[1]); /*get day*/
					int year=Integer.parseInt(date[2]); /*get year*/
					int seconds = c.get(Calendar.SECOND); /*get seconds*/
					if(year<=c.get(Calendar.YEAR)) /*if year less than in calendar*/
					{
						if(month<c.get(Calendar.MONTH)) /*if month less than next sequential month in calendar*/
						{
							Toast.makeText(Tweetqueue.this, "queue posted", Toast.LENGTH_SHORT).show();
						}
						if(month==c.get(Calendar.MONTH)) /*if month less than next sequential month in calendar*/
						{
							if(day<=c.get(Calendar.DAY_OF_MONTH)) /*if day less than day of the month in calendar*/
							{
								if(hours<=c.get(Calendar.HOUR_OF_DAY)) /*if hour less than hour of day in calendar*/
								{
									if(mint<=c.get(Calendar.MINUTE)) /*if minute less than minute in calendar*/
									{
										Toast.makeText(Tweetqueue.this, "queue posted complete date", Toast.LENGTH_SHORT).show();

									}
								}
							}
						}
					}
					
	        }
		
		
		
	}

}
