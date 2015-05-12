package com.qhubb.qhubb;

/**
 * Created by Jonathan Alinovi
 * This activity allows users to view the contents of their OWN social media profiles, access
 * accountsettings, and view statistical information.
 *
 * At the top of the screen, users are able to see a statistical measure of their social media
 * "success".
 *
 * Users are able to scroll through an aggregated "feed" of THEIR tweets and facebook posts.
 * Users can tap on facebook posts to view comments that post received.
 * Users can tap on tweets to view replies that tweet received.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class myProfile extends Activity {
    private Button newsfeed_button;
    private Button accountSettings_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        newsfeed_button = (Button) findViewById(R.id.newsfeed_button);
        accountSettings_button = (Button) findViewById(R.id.accountSettings_button);


        newsfeed_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent=new Intent(view.getContext(), Timelines.class);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });

        accountSettings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent=new Intent(view.getContext(), accountSettings.class);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
    }
}
