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

	DatabaseHandler db;
	int id;
	String s,t,d;
	EditText status,stime,sdate;
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	private static SharedPreferences mSharedPreferences;
	String statuss;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.deleteedit);
	        mSharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
	        postqueue();
	        status=(EditText) findViewById(R.id.editText1);
	        stime=(EditText) findViewById(R.id.editText2);
	        sdate=(EditText) findViewById(R.id.editText3);
	        Button edit=(Button) findViewById(R.id.button1);
	        Button delete=(Button) findViewById(R.id.button2);
	        
	        id=getIntent().getExtras().getInt("id");
	        s=getIntent().getStringExtra("status");
	        t=getIntent().getStringExtra("time");
	        d=getIntent().getStringExtra("date");
	        status.setText(s);
	        stime.setText(t);
	        sdate.setText(d);
	        db=new DatabaseHandler(getApplicationContext());
	        edit.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	db.updateContact(id, status.getText().toString(), stime.getText().toString(), sdate.getText().toString());
	                Toast.makeText(getApplicationContext(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
	                startActivity(new Intent(DeleteEditQueue.this,UpdateQueuePost.class));
	            }
	        });
	        
	        
	        delete.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
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
			DatabaseHandler db;
			List<MeetingAttribute> contacts;
			Calendar c = Calendar.getInstance(); 
			db=new DatabaseHandler(getApplicationContext());
			
		      
		        
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
							if(month<(c.get(Calendar.MONTH)+1))
							{
								db.deleteQueue(cn.getId());
								statuss=cn.getstutus();
								new updateTwitterStatus().execute(statuss);
								Toast.makeText(getApplicationContext(), "queue posted", Toast.LENGTH_SHORT).show();
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

			/**Z 	
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				
			}

			/**
			 * getting Places JSON
			 * */
			protected String doInBackground(String... args) {
				Log.d("Tweet Text", "> " + args[0]);
				String status = args[0];
				try {
					ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(MainActivity.consumerKey);
					builder.setOAuthConsumerSecret(MainActivity.consumerSecret);
					
					// Access Token 
					String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
					// Access Token Secret
					String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
					
					AccessToken accessToken = new AccessToken(access_token, access_token_secret);
					Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
					
					// Update status
					twitter4j.Status response = twitter.updateStatus(status);
					
					
					
					Log.d("Status", "> " + response.getText());
				} catch (TwitterException e) {
					// Error in updating status
					Log.d("Twitter Update Error", e.getMessage());
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog and show
			 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
			 * from background thread, otherwise you will get error
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				
				
				Toast.makeText(getApplicationContext(),"Tweet updated Successfully",Toast.LENGTH_SHORT).show();
				
			}
		}
}
