/*Created by: Sairin Sadique and Sarowary Khan*/

package com.qhubb.qhubb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import twitter4j.conf.ConfigurationBuilder;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Friends extends Fragment {

    public Friends(){}

    public void followers() { /*follower activity*/
        Intent intent = new Intent(getActivity(), FollowerActivity.class);
        startActivity(intent);
    }

    public void following() { /*following activity*/
        Intent intent = new Intent(getActivity(), FollowingActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends, container, false);
        Button button3 = (Button) rootView.findViewById(R.id.button3); /*button for follower*/
        Button button4 = (Button) rootView.findViewById(R.id.button4); /*button for following*/

        /*start follower activity if button clicked*/
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), FollowerActivity.class);
                startActivityForResult(myIntent, 0);
            }});

        /*start following activity if button clicked*/
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), FollowingActivity.class);
                startActivityForResult(myIntent, 0);
            }});

        final TextView friends=(TextView) rootView.findViewById(R.id.textView2);
        friends.setText("Friends");
        return rootView;
    }
}