/*Created by: Sairin Sadique and Sarowary Khan*/

package com.qhubb.qhubb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.qhubb.qhubb.AddQueuePost.updateTwitterStatus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class UpdateQueuePost extends Activity{
	DatabaseHandler db; /*database handler*/
	ArrayList<AdatpterValues> queue; /*arrray list of queue*/
	ListView lv1;
	List<MeetingAttribute> contacts;
	static String PREFERENCE_NAME = "twitter_oauth"; /*oauth*/
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token"; /*oauth token*/
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret"; /*oauth token secret*/
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn"; /*check twitter log in*/
	private static SharedPreferences mSharedPreferences;
	String statuss;

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.customlist);
	        lv1=(ListView) findViewById(R.id.ListView01);
	        db=new DatabaseHandler(getApplicationContext());
	        mSharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
	        postqueue(); /*call on post queue*/
	        
	        queue = new ArrayList<AdatpterValues>();
	        AdatpterValues sr1 = null;
	        
	        contacts = db.getAllQueuedata();    
			 
	        for (MeetingAttribute cn : contacts) {
	        	sr1=new AdatpterValues();
	        	sr1.setstatus("Status : "+cn.getstutus());
					sr1.setTime("Time : "+cn.getTime());
					sr1.setDate("Date : "+cn.getDate());
					queue.add(sr1);
	        }
		        lv1.setAdapter(new MyCustomBaseAdapter(UpdateQueuePost.this, queue));

		       
		        lv1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
			        //Toast.makeText(UpdateQueuePost.this, "position : "+contacts.get(arg2).getstutus(),Toast.LENGTH_SHORT ).show();
			        Intent i=new Intent(UpdateQueuePost.this,DeleteEditQueue.class);
			        i.putExtra("id", contacts.get(arg2).getId());
			        i.putExtra("status", contacts.get(arg2).getstutus());
			        i.putExtra("time", contacts.get(arg2).getTime());
			        i.putExtra("date", contacts.get(arg2).getDate());
			        startActivity(i);
			        finish();
			        
				}

		        });
	 }
	 @Override
		public void onBackPressed() {
			startActivity(new Intent(getApplicationContext(),ViewTwitterActivity.class));
			
	}
	 public void postqueue()
		{
			DatabaseHandler db;
			List<MeetingAttribute> contacts;
			Calendar c = Calendar.getInstance();
			db=new DatabaseHandler(getApplicationContext());



		        contacts = db.getAllQueuedata();

		        for (MeetingAttribute cn : contacts) {
						String[] time = cn.getTime().split(":"); /*set time*/
						int hours=Integer.parseInt(time[0]); /*set time*/
						int mint=Integer.parseInt(time[1]); /*set minute*/
						String[] date=cn.getDate().split("/"); /*set date*/
						int month=Integer.parseInt(date[0]); /*set month*/
						int day=Integer.parseInt(date[1]); /*set day*/
						int year=Integer.parseInt(date[2]); /*set year*/
						int seconds = c.get(Calendar.SECOND); /*set seconds*/
						if(year<=c.get(Calendar.YEAR)) /*if year is less than calendar year*/
						{
							if(month<(c.get(Calendar.MONTH)+1))/*if month is less than next sequential calender month*/
							{
								db.deleteQueue(cn.getId()); /*delete queu if selected*/
								statuss=cn.getstutus(); /*get status*/
								new updateTwitterStatus().execute(statuss); /*update status*/
								Toast.makeText(UpdateQueuePost.this, "queue posted", Toast.LENGTH_SHORT).show();
								MainActivity.accessTokenKey=null;
								MainActivity.accessTokenKeySecret=null;
								startActivity(new Intent(getApplicationContext(),MainActivity.class));
							}
							if(month==(c.get(Calendar.MONTH)+1))
							{
								if(day<=c.get(Calendar.DAY_OF_MONTH))
								{
									if(hours<=c.get(Calendar.HOUR_OF_DAY))
									{
										if(mint<=c.get(Calendar.MINUTE))
										{
											db.deleteQueue(cn.getId());
											statuss=cn.getstutus();
											new updateTwitterStatus().execute(statuss);
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
					builder.setOAuthConsumerKey(MainActivity.consumerKey);
					builder.setOAuthConsumerSecret(MainActivity.consumerSecret);
					
					/*Access Token*/
					String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
					/*Access Token Secret*/
					String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
					
					AccessToken accessToken = new AccessToken(access_token, access_token_secret);
					Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
					
					/*Update status*/
					twitter4j.Status response = twitter.updateStatus(status);
					
					
					
					Log.d("Status", "> " + response.getText());/*update status*/
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
