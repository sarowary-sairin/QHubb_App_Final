/*Created by: Sairin Sadique and Sarowary Khan*/
package com.qhubb.qhubb;

import java.util.Calendar;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.qhubb.qhubb.AddQueuePost.updateTwitterStatus;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteEditQueue extends Activity {

	DatabaseHandler db; /*database handler*/
	int id; /*element id*/
	String s,t,d; /*string for status, time and date*/
	EditText status,stime,sdate; /*text for status, time, and date*/
	static String PREFERENCE_NAME = "twitter_oauth"; /*oauth*/
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token"; /*oauth token*/
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret"; /*oauth token secret*/
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn"; /*check if twitter login*/
	private static SharedPreferences mSharedPreferences;
	String statuss;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.deleteedit); /*xml delete edit queue*/
	        mSharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
	        postqueue(); /*call on postqueue function*/
	        status=(EditText) findViewById(R.id.editText1); /*status for queue post*/
	        stime=(EditText) findViewById(R.id.editText2); /*time for queue post*/
	        sdate=(EditText) findViewById(R.id.editText3); /*date for queue post*/
	        Button edit=(Button) findViewById(R.id.button1); /*edit button for queue post*/
	        Button delete=(Button) findViewById(R.id.button2); /*delete button for queue post*/
	        
	        id=getIntent().getExtras().getInt("id"); /*get element ID*/
	        s=getIntent().getStringExtra("status"); /*get element status*/
	        t=getIntent().getStringExtra("time"); /*get element time*/
	        d=getIntent().getStringExtra("date"); /*get element date*/
	        status.setText(s); /*set element status*/
	        stime.setText(t); /*set element time*/
	        sdate.setText(d); /*set element date*/
	        db=new DatabaseHandler(getApplicationContext());
	        edit.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	/*update the database if user chooses to edit*/
	            	db.updateContact(id, status.getText().toString(), stime.getText().toString(), sdate.getText().toString());
	                Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
	                startActivity(new Intent(DeleteEditQueue.this,UpdateQueuePost.class));
	            }
	        });
	        
	        
	        delete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	/*delete the database if user chooses to delete*/
	                db.deleteQueue(id);
	                Toast.makeText(getApplicationContext(), "Data Successfully Deleted", Toast.LENGTH_SHORT).show();
	                startActivity(new Intent(DeleteEditQueue.this,UpdateQueuePost.class));

	            }
	        });
	 }
	  @Override
			public void onBackPressed() {
				startActivity(new Intent(getApplicationContext(),ViewTwitterActivity.class));
	  }	
	  public void postqueue()
		{
			DatabaseHandler db; /*database handler*/
			List<MeetingAttribute> contacts; /*queue data*/
			Calendar c = Calendar.getInstance(); /*calendar instance*/
			db=new DatabaseHandler(getApplicationContext()); /*Call on database handler*/
			
		      
		        
		        contacts = db.getAllQueuedata(); /*get all queue data*/
				 
		        for (MeetingAttribute cn : contacts) {
						String[] time = cn.getTime().split(":"); /*get time*/
						int hours=Integer.parseInt(time[0]); /*get hour*/
						int mint=Integer.parseInt(time[1]); /*get minute*/
						String[] date=cn.getDate().split("/"); /*get date*/
						int month=Integer.parseInt(date[0]); /*get month*/
						int day=Integer.parseInt(date[1]); /*get day*/
						int year=Integer.parseInt(date[2]); /*get year*/
						int seconds = c.get(Calendar.SECOND); /*get seconds*/
						if(year<=c.get(Calendar.YEAR)) /*if year less than in calendar*/
						{
							if(month<(c.get(Calendar.MONTH)+1)) /*if month less than next sequential month in calendar*/
							{
								db.deleteQueue(cn.getId());  /*delete queue if instructed by user*/
								statuss=cn.getstutus(); /*get status*/
								new updateTwitterStatus().execute(statuss);/*update status*/
                                /*post queue through twitter permission by access token and access token secret*/
								Toast.makeText(getApplicationContext(), "queue posted", Toast.LENGTH_SHORT).show();
								MainActivity.accessTokenKey=null;
								MainActivity.accessTokenKeySecret=null;
								startActivity(new Intent(getApplicationContext(),MainActivity.class));
							}
							if(month==(c.get(Calendar.MONTH)+1)) /*if month less than next sequential month in calendar*/
							{
								if(day<=c.get(Calendar.DAY_OF_MONTH)) /*if day less than day of the month in calendar*/
								{
									if(hours<=c.get(Calendar.HOUR_OF_DAY)) /*if hour less than hour of day in calendar*/
									{
										if(mint<=c.get(Calendar.MINUTE))  /*if minute less than minute in calendar*/
										{
											db.deleteQueue(cn.getId());  /*delete queue if instructed by user*/
											statuss=cn.getstutus(); /*get status*/
											new updateTwitterStatus().execute(statuss); /*update status*/
                                            /*post queue through twitter permission by access token and access token secret*/
											Toast.makeText(getApplicationContext(), "queue posted complete date", Toast.LENGTH_SHORT).show();
											MainActivity.accessTokenKey=null;
											MainActivity.accessTokenKeySecret=null;
											startActivity(new Intent(getApplicationContext(),MainActivity.class));

										}
									}
								}
							}
						}
						
		        }
		}

         /*update twitter status*/
		class updateTwitterStatus extends AsyncTask<String, String, String> {

			/*Before starting background thread Show Progress Dialog*/
			@Override
			protected void onPreExecute() {
				
			}

			/*getting Places JSON*/
			protected String doInBackground(String... args) {
				Log.d("Tweet Text", "> " + args[0]);
				String status = args[0];
				try {
					ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(MainActivity.consumerKey); /*twitter key*/
					builder.setOAuthConsumerSecret(MainActivity.consumerSecret); /*twitter key secret*/
					
					/*Access Token*/
					String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
					/*Access Token Secret*/
					String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
					
					AccessToken accessToken = new AccessToken(access_token, access_token_secret);
					Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
					
					/*Update status*/
					twitter4j.Status response = twitter.updateStatus(status);
					
					
					
					Log.d("Status", "> " + response.getText()); /*get response of update post status*/
				} catch (TwitterException e) {
					/*Error in updating status*/
					Log.d("Twitter Update Error", e.getMessage());
				}
				return null;
			}

			/*After completing background task Dismiss the progress dialog and show
			the data in UI Always use runOnUiThread(new Runnable()) to update UI
			from background thread, otherwise you will get error*/
			protected void onPostExecute(String file_url) {
				/*dismiss the dialog after getting all products*/
				
				
				Toast.makeText(getApplicationContext(),"Tweet updated Successfully",Toast.LENGTH_SHORT).show();
				
			}
		}
}
