/*Created by: Sairin Sadique and Sarowary Khan*/
package com.qhubb.qhubb;

import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Text;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import com.qhubb.qhubb.UpdateStatus.updateTwitterStatus;

import android.R.fraction;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tweetthroughfragment extends Fragment {

    public static int tweet=0;
    /*Progress dialog*/
    ProgressDialog pDialog;
    static String PREFERENCE_NAME = "twitter_oauth"; /*oauth*/
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token"; /*oauth token*/
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret"; /*oauth token secret*/
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn"; /*check if logged in*/
    private static SharedPreferences mSharedPreferences;
    String status="";
    EditText statuss;
    String statusss;

    public Tweetthroughfragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.updatestatus, container, false);

        Button update=(Button) rootView.findViewById(R.id.button1); /*update button*/
        Button btnCancel=(Button) rootView.findViewById(R.id.btnCancel); /*cancel button*/
        Button postStatusUpdateButton=(Button) rootView.findViewById(R.id.postStatusUpdateButton); /*post status button*/
        statuss=(EditText) rootView.findViewById(R.id.editText1); /*text for status*/
        final TextView welcome=(TextView) rootView.findViewById(R.id.textView1);
        mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences(
                "MyPref", 0);
        /*start updateTwitterStatus activity when button clicked*/
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=statuss.getText().toString();
                new updateTwitterStatus().execute(status);

            }
        });
        /*start facebookstuff activity when button clicked*/
        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), FacebookStuff.class);
                startActivityForResult(myIntent, 0);
            }
        });

        /*start viewtwitteractivity activity when button clicked*/
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),ViewTwitterActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        return rootView;
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



                Log.d("Status", "> " + response.getText()); /*update status*/
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
            statuss.setText("");
            tweet=1;
            Toast.makeText(getActivity().getApplicationContext(),"Tweet updated Successfully",Toast.LENGTH_SHORT).show();
            MainActivity.accessTokenKey=null;
            MainActivity.accessTokenKeySecret=null;
            startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));

        }
    }


    public void postqueue()
    {
        DatabaseHandler db; /*database handler*/
        List<MeetingAttribute> contacts; /*queue data*/
        Calendar c = Calendar.getInstance(); /*calender instance*/
        db=new DatabaseHandler(getActivity().getApplicationContext()); /*Call on database handler*/



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
                    db.deleteQueue(cn.getId()); /*delete queue if instructed by user*/
                    status=cn.getstutus(); /*get status*/
                    new updateTwitterStatus().execute(status); /*update status*/
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
                            if(mint<=c.get(Calendar.MINUTE))  /*if minute less than minute in calendar*/
                            {
                                db.deleteQueue(cn.getId()); /*delete queue if instructed by user*/
                                status=cn.getstutus(); /*get status*/
                                new updateTwitterStatus().execute(status); /*update status*/
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

