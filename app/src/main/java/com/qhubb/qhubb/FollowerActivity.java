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

import com.qhubb.qhubb.util.JSONParser1;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class FollowerActivity extends ListActivity {
    public JSONParser1 jsonParser; /*json parser*/
    private List<String> listValues; /*list for follower*/
    ProgressDialog pDialog;
    /*twitter key, key secret, callback url, oauth, oauth token, oauth token secret*/
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
    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        listValues = new ArrayList<String>(); /*list of follower*/
        jsonParser = new JSONParser1(); /*json parser*/
        new GetFollowers().execute(); /*execute get follower function*/
        myAdapter = new ArrayAdapter <String>(this,R.layout.row_list_followers, R.id.listText, listValues);
        /*assign the list adapter*/
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
        setListAdapter(myAdapter);
        setContentView(R.layout.activity_follower);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Inflate the menu; this adds items to the action bar if it is present.*/
        getMenuInflater().inflate(R.menu.menu_follower, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement*/
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Background Async Task to Get complete product details*/
    class GetFollowers extends AsyncTask<String, String, String> {

        /*Before starting background thread Show Progress Dialog*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FollowerActivity.this);
            pDialog.setMessage("Loading followers details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /*Getting product details in background thread*/
        protected String doInBackground(String... params) {

            Log.d("Tweet Text", "> ");
            String status = "";
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(consumerKey);
                builder.setOAuthConsumerSecret(consumerSecret);

                /*Access Token*/
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                /*Access Token Secret*/
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                /*Update status*/
                IDs response = twitter.getFollowersIDs(-1);
                do {
                    for (long id : response.getIDs()) {
                        String ID = "followers ID #" + id;
                        String[] firstname = ID.split("#");
                        String first_Name = firstname[0];
                        String Id = firstname[1];

                        Log.i("split...........", first_Name + Id);
                        String Name = twitter.showUser(id).getName();
                        listValues.add(Name);
                    }
                } while (response.hasNext());

                Log.d("Status", "> " + response); /*get response of update post status*/
            } catch (TwitterException e) {
                /*Error in updating status*/
                Log.d("Twitter Update Error", e.getMessage());
            }
            return null;
        }

        /*After completing background task Dismiss the progress dialog*/
        protected void onPostExecute(String file_url) {
            /*dismiss the dialog once got all details*/
            setListAdapter(myAdapter);
            pDialog.dismiss();

        }
    }
}