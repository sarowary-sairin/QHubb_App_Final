package com.qhubb.qhubb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

public class Tweetqueue extends Activity{
	
	DatabaseHandler db;
	List<MeetingAttribute> contacts;
	public void postqueue()
	{
		DatabaseHandler db;
		List<MeetingAttribute> contacts;
		Calendar c = Calendar.getInstance(); 
		db=new DatabaseHandler(Tweetqueue.this);
		
	      
	        
	        contacts = db.getAllQueuedata();    
			 
	        for (MeetingAttribute cn : contacts) {
					String[] time = cn.getTime().split(":");
					int hours=Integer.parseInt(time[0]);
					int mint=Integer.parseInt(time[1]);
					String[] date=cn.getDate().split("/");
					int month=Integer.parseInt(date[0]);
					int day=Integer.parseInt(date[1]);
					int year=Integer.parseInt(date[2]);
					int seconds = c.get(Calendar.SECOND);
					if(year<=c.get(Calendar.YEAR))
					{
						if(month<c.get(Calendar.MONTH))
						{
							Toast.makeText(Tweetqueue.this, "queue posted", Toast.LENGTH_SHORT).show();
						}
						if(month==c.get(Calendar.MONTH))
						{
							if(day<=c.get(Calendar.DAY_OF_MONTH))
							{
								if(hours<=c.get(Calendar.HOUR_OF_DAY))
								{
									if(mint<=c.get(Calendar.MINUTE))
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
