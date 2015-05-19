package com.qhubb.qhubb;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.qhubb.qhubb.R;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TimelineActivity extends ListActivity {

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
    String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            name = bundle.get("MentionTimeline").toString();
        }
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);
        //String name=null;
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
            ResponseList<Status> asc  = twitter.getMentionsTimeline();
            name = twitter.getScreenName();
            Log.d("SCENARIO NAME",name);
        } catch (Exception e) {
            // Error in updating status
            Log.d("Twitter Update Error", e.getMessage());
        }


        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(name)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeline);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
}

