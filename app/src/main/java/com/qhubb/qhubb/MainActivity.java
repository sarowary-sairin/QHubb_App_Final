package com.qhubb.qhubb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qhubb.qhubb.R;
import com.qhubb.qhubb.UpdateStatus.updateTwitterStatus;
import com.qhubb.qhubb.network.ActivityTask;
import com.qhubb.qhubb.util.Service;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends Activity {

    private Button btnLogout;
    private Button btnChangePassword;
    private String email;
    private String deactive;
    private Button btnTwitter;
    private Button btnDeactivate;
    private Button btnTwitterLogout;
    private TextView textDeactivateMessage;

    private static Twitter twitter;
    public static String consumerKey = "5VvmZqXyH5erqjwKXVu54Te1V";
    public static String consumerSecret ="T4y42yCkTc1XabtwBKdmXtEOSzKwOXKgdKBOQcay4U8inOAl2z";
    public static String callbackUrl = "http://javatechig.android.app";
    private static RequestToken requestToken;

    public static final int WEBVIEW_REQUEST_CODE = 100;
    private String oAuthVerifier = null;

    public static String accessTokenKey = null;
    public static String accessTokenKeySecret = null;
    public static String username = null;
    
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";
	private static SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnTwitter = (Button) findViewById(R.id.btnTwitter);
        btnTwitterLogout = (Button) findViewById(R.id.btnTwitterLogout);
        btnDeactivate = (Button) findViewById(R.id.btnDeactivate);
        textDeactivateMessage = (TextView) findViewById(R.id.textDeactivateMessage);

        email = (String) getIntent().getSerializableExtra("email");
        deactive = (String) getIntent().getSerializableExtra("deactive");

        oAuthVerifier = getString(R.string.twitter_oauth_verifier);

        /* Enabling strict mode */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
        
        if ("1".equals(deactive)){
            btnDeactivate.setText("Activate");
            btnChangePassword.setEnabled(false);
            btnTwitter.setEnabled(false);
        }

        btnTwitter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                twitter();
            }
        });

        /**
         * Change Password Activity Started
         **/
        btnChangePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent chgpass = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                chgpass.putExtra("email", email);
                startActivity(chgpass);
            }

        });

        btnTwitterLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                logoutTwitter();
            }
        });

        btnDeactivate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                final View view1 = view;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                String prompt = "";
//                Toast.makeText(getApplicationContext(),
//                        "deactive: " + deactive, Toast.LENGTH_SHORT).show();
                if ("1".equals(deactive)){
                    // set title
                    alertDialogBuilder.setTitle("Activation");
                    prompt = "Do you want to activate the account?";
                }else{
                    // set title
                    alertDialogBuilder.setTitle("Deactivation");
                    prompt = "Do you want to deactivate the account?";
                }


                // set dialog message
                alertDialogBuilder
                        .setMessage(prompt)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                execute(view1);
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }

        });

        if (accessTokenKey != null) {
            btnTwitterLogout.setVisibility(View.VISIBLE);
        }else{
            btnTwitterLogout.setVisibility(View.INVISIBLE);
        }

        /**
         *Logout from the User Panel which clears the data in Sqlite database
         **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Service logout = new Service();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);

                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
    }

    private void twitter(){

        boolean canDisplayList = false;

        //Toast.makeText(getApplicationContext(),
        //        "accessTokenKey: " + accessTokenKey, Toast.LENGTH_LONG).show();

        if (accessTokenKey != null){
            try{
                displayList();
                canDisplayList = true;
            } catch (Exception e) {

            }
        }
        //Toast.makeText(getApplicationContext(),
        //                "canDisplayList: " + canDisplayList, Toast.LENGTH_LONG).show();
        if (!canDisplayList) {
            try {
                final ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(consumerKey);
                builder.setOAuthConsumerSecret(consumerSecret);

                final Configuration configuration = builder.build();
                final TwitterFactory factory = new TwitterFactory(configuration);
                twitter = factory.getInstance();


                requestToken = twitter.getOAuthRequestToken(callbackUrl);

                /**
                 *  Loading twitter login page on webview for authorization
                 *  Once authorized, results are received at onActivityResult
                 *  */
                final Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL() + "&force_login=true");
                startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

            } catch (TwitterException e) {
                Log.e("TwitterException", e.toString());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void execute(View view){
        new ActivityTask(this, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
                textDeactivateMessage, new ProcessDeactivate()).execute();
    }

    private class ProcessDeactivate extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("QHubb");
            pDialog.setMessage("...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            Service userFunction = new Service();
            if ("1".equals(deactive)){
                JSONObject json = userFunction.activateAccount(email);
                return json;
            }else {
                JSONObject json = userFunction.deactivateAccount(email);
                return json;
            }
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {

                    String res = json.getString("success");

                    if(Integer.parseInt(res) == 1){
                        if ("1".equals(deactive)){
                            pDialog.setMessage("Activating...");
                            pDialog.setTitle("Activate");
                            btnChangePassword.setEnabled(true);
                            btnTwitter.setEnabled(true);
                            deactive = "0";
                            btnDeactivate.setText("DeActivate");
                        }else {
                            pDialog.setMessage("Deactivating...");
                            pDialog.setTitle("Deactivate");
                            btnChangePassword.setEnabled(false);
                            btnTwitter.setEnabled(false);
                            deactive = "1";
                            btnDeactivate.setText("Activate");
                        }
                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         *
                        Intent upanel = new Intent(getApplicationContext(), LoginActivity.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);*/
                        /**
                         * Close Login Screen
                         *
                        finish();*/
                        pDialog.dismiss();
                    }else{

                        pDialog.dismiss();
                        if ("1".equals(deactive)){
                            textDeactivateMessage.setText("Cannot activate account");
                        }else{
                            textDeactivateMessage.setText("Cannot deactivate account");
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String verifier = data.getExtras().getString(oAuthVerifier);
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                long userID = accessToken.getUserId();
                final User user = twitter.showUser(userID);


                //Toast.makeText(getApplicationContext(),
                //        "username: " + username, Toast.LENGTH_SHORT).show();

                accessTokenKey = twitter.getOAuthAccessToken().getToken();
                accessTokenKeySecret = twitter.getOAuthAccessToken().getTokenSecret();
                username = user.getName();
                
            	Editor e = mSharedPreferences.edit();

				// After getting access token, access token secret
				// store them in application preferences
				e.putString(PREF_KEY_OAUTH_TOKEN, accessTokenKey);
				e.putString(PREF_KEY_OAUTH_SECRET,accessTokenKeySecret);
				// Store login status - true
				e.commit(); // save changes

                displayList();

            } catch (Exception e) {
                Log.e("Twitter Login Failed", e.getMessage());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayList() throws Exception{

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecret);
        builder.setOAuthAccessToken(accessTokenKey);
        builder.setOAuthAccessTokenSecret(accessTokenKeySecret);
        Configuration configuration = builder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        Paging page = new Paging();
        page.setCount(100);
        List<Status> statuses = new ArrayList<>();

        //Toast.makeText(getApplicationContext(),
        //        "username ???: " + username, Toast.LENGTH_SHORT).show();

        statuses = twitter.getHomeTimeline();

        List<String> timeArray = new ArrayList<>();
        List<String> descriptionArray = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        for (Status s : statuses) {
            timeArray.add(sdf.format(s.getCreatedAt()));
            descriptionArray.add(s.getUser().getScreenName() + ": " + s.getText());
        }

        Intent twitterView = new Intent(getApplicationContext(), ViewTwitterActivity.class);
        twitterView.putExtra("timeArray", timeArray.toArray(new String[timeArray.size()]));
        twitterView.putExtra("descriptionArray", descriptionArray.toArray(new String[descriptionArray.size()]));

        startActivity(twitterView);
        finish();

        btnTwitterLogout.setVisibility(View.VISIBLE);
    }

    /**
     * log out twitter
     */
    private void logoutTwitter(){
        accessTokenKey = null;
        accessTokenKeySecret = null;
        username = null;

        btnTwitterLogout.setVisibility(View.INVISIBLE);
    }

}
