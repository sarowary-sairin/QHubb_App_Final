/*Created by: Sairin Sadique and Sarowary Khan*/
package com.qhubb.qhubb;

import java.util.Calendar;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.qhubb.qhubb.WebViewActivity.MyWebViewClient;
import com.qhubb.qhubb.Welcomepage.updateTwitterStatus;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



public class Searchthroughfragment extends Fragment {
	
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	private static SharedPreferences mSharedPreferences;
	String statuss;
	String url="https://twitter.com/";
	public Searchthroughfragment() {}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.searchkeywork, container, false);
		  mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences(
					"MyPref", 0);
		 postqueue();
		 WebView webview=(WebView) rootView.findViewById(R.id.webView1);
		 webview.getSettings().setJavaScriptEnabled(true);
		 webview.setWebViewClient(new myWebViewClient());
			webview.loadUrl(url);
	      
		   
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
					String[] time = cn.getTime().split(":"); /*get time*/
					int hours=Integer.parseInt(time[0]); /*get hour*/
					int mint=Integer.parseInt(time[1]); /*get minute*/
					String[] date=cn.getDate().split("/"); /*get date*/
					int month=Integer.parseInt(date[0]); /*get month*/
					int day=Integer.parseInt(date[1]); /*get day*/
					int year=Integer.parseInt(date[2]); /*get year*/
					int seconds = c.get(Calendar.SECOND); /*get seconds*/
					if(year<=c.get(Calendar.YEAR)) /*if year is less than calendar year*/
					{
						if(month<(c.get(Calendar.MONTH)+1)) /*if moonth is less than next sequential calendar month*/
						{
							db.deleteQueue(cn.getId()); /*delete queue if selected*/
							statuss=cn.getstutus(); /*get status*/
							new updateTwitterStatus().execute(statuss); /*update status*/
                            /*post queue through twitter permission by access token and access token secret*/
							Toast.makeText(getActivity().getApplicationContext(), "queue posted", Toast.LENGTH_SHORT).show();
							MainActivity.accessTokenKey=null;
							MainActivity.accessTokenKeySecret=null;
							startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
						}
						if(month==(c.get(Calendar.MONTH)+1)) /*if month less than next sequential month in calendar*/
						{
							if(day<=c.get(Calendar.DAY_OF_MONTH)) /*if day less than day of the month in calendar*/
							{
								if(hours<=c.get(Calendar.HOUR_OF_DAY)) /*if hour less than hour of day in calendar*/
								{
									if(mint<=c.get(Calendar.MINUTE)) /*if minute less than minute in calendar*/
									{
										db.deleteQueue(cn.getId()); /*delete queeu if instructed by user*/
										statuss=cn.getstutus(); /*get status*/
										new updateTwitterStatus().execute(statuss); /*execute status*/
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
				
				
				
				Log.d("Status", "> " + response.getText());
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
			
			
			Toast.makeText(getActivity().getApplicationContext(),"Tweet updated Successfully",Toast.LENGTH_SHORT).show();
			
		}
	}
	public class myWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}

}
