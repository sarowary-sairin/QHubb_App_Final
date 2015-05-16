package com.qhubb.qhubb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by jonathanalinovi on 5/15/15.
 * there should be two options here
 * log in fb
 * log in twitter
 */
public class afterLogIn extends Activity
{
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