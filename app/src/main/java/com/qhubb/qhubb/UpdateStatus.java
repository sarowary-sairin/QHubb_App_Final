package com.qhubb.qhubb;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateStatus extends Activity {
	
	// Progress dialog
		ProgressDialog pDialog;
		static String PREFERENCE_NAME = "twitter_oauth";
		static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
		static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
		static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
		private static SharedPreferences mSharedPreferences;
		String status="";

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.updatestatus);
	        Button update=(Button) findViewById(R.id.button1);
	        final EditText statuss=(EditText) findViewById(R.id.editText1);
	        mSharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
	        update.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	status=statuss.getText().toString();
	            	new updateTwitterStatus().execute(status);
	            }
	        });
	 }
	
	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**Z 	
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UpdateStatus.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
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
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
					// Clearing EditText field
					
				}
				
			});
			
			
		}
	}
}
