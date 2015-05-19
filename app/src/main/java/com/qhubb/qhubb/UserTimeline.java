package com.qhubb.qhubb;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.qhubb.qhubb.R;

import java.util.ArrayList;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.examples.timeline.GetUserTimeline;

public class UserTimeline extends ListActivity {
    ProgressDialog pDialog;
    private static String url_get_followers = "https://api.twitter.com/1.1/followers/list.json?cursor=-1&screen_name=abrar__ahmad&skip_status=true&include_user_entities=false";
    private static final String TWITTER_KEY = "HcyoJzWDGz5YlhQqtsC966jkd";
    private static final String TWITTER_SECRET = "HcjlxNfFdYgx46AiNFdoxjEXNzQYVJ071P3Epkm9OxZXwf8RHo";
    public static String consumerKey = "5VvmZqXyH5erqjwKXVu54Te1V";
    public static String consumerSecret ="T4y42yCkTc1XabtwBKdmXtEOSzKwOXKgdKBOQcay4U8inOAl2z";
    public static String callbackUrl = "http://javatechig.android.app";
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    private static SharedPreferences mSharedPreferences;
    ArrayList<String> listValues;
    ArrayAdapter<String> myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listValues = new ArrayList<String>();

        new GetUserTimeline().execute();
        myAdapter = new ArrayAdapter <String>(this,R.layout.row_list_followers, R.id.listText, listValues);

        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
        setListAdapter(myAdapter);
        setContentView(R.layout.activity_user_timeline);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class GetUserTimeline extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserTimeline.this);
            pDialog.setMessage("Loading User Timeline details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(consumerKey);
                builder.setOAuthConsumerSecret(consumerSecret);

                // Access Token
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
                ResponseList<twitter4j.Status> Users  = twitter.getUserTimeline();
                //name = twitter.getScreenName();
                Log.d("SCENARIO NAME", " "+Users.size());
                for(int i = 0; i<Users.size(); i++){
                    twitter4j.Status s = Users.get(i);

                    //JSONObject jsonObject = (JSONObject) s;
                    String date = s.getCreatedAt().toString();
                    String txt = s.getText().toString();
                    String txt2 = s.getUser().getScreenName();
                    Integer txt3 = s.getRetweetCount();
                    Integer txt4 = s.getFavoriteCount();
                    listValues.add("@"+ txt2 + ": " + txt+" \n "+ "Retweet: " + txt3 + " \n " + "Favorite: "+ txt4 + "\n" + date);
                    Log.d("SCENARIO NAME", " " + s);
                    Log.d("SCENARIO NAME", " " + date);
                    Log.d("SCENARIO NAME", " "+txt);
                    Log.d("SCENARIO NAME", " "+txt2);
                    Log.d("SCENARIO NAME", " "+txt3);
                    Log.d("SCENARIO NAME", " "+txt4);}
            } catch (Exception e) {
                // Error in updating status
                Log.d("Twitter Update Error", " "+e);
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            //myAdapter = new ArrayAdapter <String>(this,R.layout.row_list_followers, R.id.listText, listValues);
            setListAdapter(myAdapter);
            pDialog.dismiss();

        }
    }
}
