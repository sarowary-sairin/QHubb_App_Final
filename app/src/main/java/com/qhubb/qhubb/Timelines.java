/*Created by: Sairin Sadique and Sarowary Khan*/
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

    /*user timeline function*/
    public void timeline_user() {
        Intent intent = new Intent(getActivity(), UserTimeline.class);
        startActivity(intent);
    }

    /*mention timelne function*/
    public void timeline_mention(){
        Intent intent = new Intent(getActivity(), MentionTimeline.class);
        startActivity(intent);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.queued, container, false);
		Button myProfile_button =(Button) rootView.findViewById(R.id.myProfile_button); /*myrp0file button*/
		Button composePost_button =(Button) rootView.findViewById(R.id.composePost_button); /*compose button*/
        Button Mentions =(Button) rootView.findViewById(R.id.Mentions); /*mentions button*/

        /*start user timeline activity if button clicked*/
        myProfile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeline_user();
            }
        });
        /*start mention timeline activity if button clicked*/
        Mentions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeline_mention();
            }
        });


		composePost_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
		        /*NEED TO CHANGE VIEWTWITTERATIVITY*/
				Intent myIntent = new Intent(view.getContext(), ViewTwitterActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});

        msgList = (ListView) rootView.findViewById(R.id.MessageList);
        postqueue(); /*call on post queue function*/
        ConfigurationBuilder builder = new ConfigurationBuilder();

        try {
              /*time and description for timeline*/
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
		DatabaseHandler db; /*database handler*/
		List<MeetingAttribute> contacts; /*queue data*/
		Calendar c = Calendar.getInstance(); /*calender instance*/
		db=new DatabaseHandler(getActivity().getApplicationContext()); /*call on database handler*/



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
							Toast.makeText(getActivity().getApplicationContext(), "queue posted", Toast.LENGTH_SHORT).show();
							MainActivity.accessTokenKey=null;
							MainActivity.accessTokenKeySecret=null;
							startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
						}
						if(month==c.get(Calendar.MONTH)) /*if month less than next sequential month in calendar*/
						{
							if(day<=c.get(Calendar.DAY_OF_MONTH))  /*if day less than day of the month in calendar*/
							{
								if(hours<=c.get(Calendar.HOUR_OF_DAY)) /*if hour less than hour of day in calendar*/
								{
									if(mint<=c.get(Calendar.MINUTE))  /*if minute less than minute in calendar*/
									{
                                         /*post queue through twitter permission by access token and access token secret*/
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
