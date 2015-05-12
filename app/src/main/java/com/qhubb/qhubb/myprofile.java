package com.qhubb.qhubb;

/**
 * Created by jonathanalinovi on 5/11/15.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class myprofile extends Activity {
    private Button newsfeed_button;
    private Button settings_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        newsfeed_button = (Button) findViewById(R.id.newsfeed_button);
        settings_button = (Button) findViewById(R.id.settings_button);


        newsfeed_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent=new Intent(view.getContext(), Timelines.class);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent=new Intent(view.getContext(), settingsscreen.class);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
    }
}
