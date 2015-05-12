package com.qhubb.qhubb;

/**
 * Created by jonathanalinovi on 5/12/15.
 * Recoding "Timelines.java" and "queued.xml"...
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;



public class homeFeed extends Activity {
    public static String[] timeArray;
    ListView msgList;
    ArrayList<MessageDetails> details;
    AdapterView.AdapterContextMenuInfo info;

    private Button myProfile_button;
    private Button composePost_button;

    /*@Override*/
    protected void onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homefeed);

        myProfile_button = (Button) findViewById(R.id.myProfile_button);
        composePost_button = (Button) findViewById(R.id.composePost_button);


        myProfile_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), myProfile.class);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });

        composePost_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Tweetthroughfragment.class);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });

        /*Don't know...*/
        inflater.inflate(R.layout.homefeed, container, false);
        msgList = (ListView) findViewById(R.id.MessageList);
        postqueue();
        ConfigurationBuilder builder = new ConfigurationBuilder();

        try {
            timeArray = (String[]) getIntent().getSerializableExtra("timeArray");
            String[] descriptionArray = (String[]) getIntent().getSerializableExtra("descriptionArray");
            Toast.makeText(getApplicationContext(), "" + Tweetthroughfragment.tweet, Toast.LENGTH_LONG).show();


            details = new ArrayList<MessageDetails>();
            for (int i = 0; i < timeArray.length; i++) {
                MessageDetails detail;
                detail = new MessageDetails();
                detail.setTime(timeArray[i]);
                detail.setDesc(descriptionArray[i]);
                details.add(detail);
            }

            msgList.setAdapter(new CustomAdapter(details, getApplicationContext()));


        } catch (NullPointerException e) {
            MainActivity.accessTokenKey = null;
            MainActivity.accessTokenKeySecret = null;
            Toast.makeText(getApplicationContext(), "Timeline updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }
    }

    public void postqueue() {
        DatabaseHandler db;
        List<MeetingAttribute> contacts;
        Calendar c = Calendar.getInstance();
        db = new DatabaseHandler(getApplicationContext());


        contacts = db.getAllQueuedata();

        for (MeetingAttribute cn : contacts) {
            String[] time = cn.getTime().split(":");
            int hours = Integer.parseInt(time[0]);
            int mint = Integer.parseInt(time[1]);
            String[] date = cn.getDate().split("/");
            int month = Integer.parseInt(date[0]);
            int day = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);
            int seconds = c.get(Calendar.SECOND);
            if (year <= c.get(Calendar.YEAR)) {
                if (month < c.get(Calendar.MONTH)) {
                    Toast.makeText(getApplicationContext(), "queue posted", Toast.LENGTH_SHORT).show();
                    MainActivity.accessTokenKey = null;
                    MainActivity.accessTokenKeySecret = null;
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                if (month == c.get(Calendar.MONTH)) {
                    if (day <= c.get(Calendar.DAY_OF_MONTH)) {
                        if (hours <= c.get(Calendar.HOUR_OF_DAY)) {
                            if (mint <= c.get(Calendar.MINUTE)) {
                                Toast.makeText(getApplicationContext(), "queue posted complete date", Toast.LENGTH_SHORT).show();
                                MainActivity.accessTokenKey = null;
                                MainActivity.accessTokenKeySecret = null;
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                        }
                    }
                }
            }
        }
    }
}

