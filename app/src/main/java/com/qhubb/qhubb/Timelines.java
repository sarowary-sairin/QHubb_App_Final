package com.qhubb.qhubb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import twitter4j.conf.ConfigurationBuilder;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.qhubb.qhubb.MentionTimeline;

public class Timelines extends Fragment {


	public static String[] timeArray;
	ListView msgList;
    ArrayList<MessageDetails> details;
    AdapterView.AdapterContextMenuInfo info;
	public Timelines(){}

<<<<<<< HEAD
    public void timeline_user() {
        Intent intent = new Intent(getActivity(), UserTimeline.class);
        startActivity(intent);
    }

    public void timeline_mention(){
        Intent intent = new Intent(getActivity(), MentionTimeline.class);
        startActivity(intent);
    }

=======
    public void timeline() {
        Intent intent = new Intent(getActivity(), TimelineActivity.class);
        startActivity(intent);
    }

    public void timeline2(){
        Intent intent = new Intent(getActivity(),MentionTimeline.class);
        startActivity(intent);
    }
>>>>>>> origin/master
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.queued, container, false);
		Button myProfile_button =(Button) rootView.findViewById(R.id.myProfile_button);
		Button composePost_button =(Button) rootView.findViewById(R.id.composePost_button);
        Button Mentions =(Button) rootView.findViewById(R.id.Mentions);

        myProfile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                timeline_user();
=======
                timeline();
>>>>>>> origin/master
            }
        });
        Mentions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                timeline_mention();
=======
                timeline2();
>>>>>>> origin/master
            }
        });


		composePost_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				/*NEED TO CHANGE THIS TO DIRECT TO THE PROPER SCREEN, NOT VIEWTWITTERACTIVITY*/
				Intent myIntent = new Intent(view.getContext(), ViewTwitterActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});

        msgList = (ListView) rootView.findViewById(R.id.MessageList);
        postqueue();
        ConfigurationBuilder builder = new ConfigurationBuilder();

        try {
        	  timeArray = (String[]) getActivity().getIntent().getSerializableExtra("timeArray");
              String[] descriptionArray = (String[]) getActivity().getIntent().getSerializableExtra("descriptionArray");
              Toast.makeText(getActivity().getApplicationContext(), ""+Tweetthroughfragment.tweet, Toast.LENGTH_LONG).show();

              
              details = new ArrayList<MessageDetails>();
              for (int i = 0; i < timeArray.length; i++){
                  MessageDetails detail;
                  detail = new MessageDetails();
                  detail.setTime(timeArray[i]);
                  detail.setDesc(descriptionArray[i]);
                  details.add(detail);
              }

              msgList.setAdapter(new CustomAdapter(details , getActivity().getApplicationContext()));
              
              
        } catch(NullPointerException e) {
        	MainActivity.accessTokenKey=null;
        	MainActivity.accessTokenKeySecret=null;
        	Toast.makeText(getActivity().getApplicationContext(), "Timeline updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
            
        }
       return rootView;
    }
	public void postqueue()
	{
		DatabaseHandler db;
		List<MeetingAttribute> contacts;
		Calendar c = Calendar.getInstance(); 
		db=new DatabaseHandler(getActivity().getApplicationContext());
		
	      
	        
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
							Toast.makeText(getActivity().getApplicationContext(), "queue posted", Toast.LENGTH_SHORT).show();
							MainActivity.accessTokenKey=null;
							MainActivity.accessTokenKeySecret=null;
							startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
						}
						if(month==c.get(Calendar.MONTH))
						{
							if(day<=c.get(Calendar.DAY_OF_MONTH))
							{
								if(hours<=c.get(Calendar.HOUR_OF_DAY))
								{
									if(mint<=c.get(Calendar.MINUTE))
									{
										Toast.makeText(getActivity().getApplicationContext(), "queue posted complete date", Toast.LENGTH_SHORT).show();
										MainActivity.accessTokenKey=null;
										MainActivity.accessTokenKeySecret=null;
										startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));

									}
								}
							}
						}
					}
					
	        }
		
		
		
	}
}
