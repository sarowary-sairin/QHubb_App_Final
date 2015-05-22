/*Created by: Sairin Sadique and Sarowary Khan*/
package com.qhubb.qhubb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qhubb.qhubb.network.ActivityTask;
import com.qhubb.qhubb.util.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class RegisterActivity extends Activity {

    private EditText txtFirstName; /*first name*/
    private EditText txtLastName; /*last name*/
    private EditText txtEmail; /*email*/
    private EditText txtPassword; /*password*/

    private Button btnRegister; /*register button*/
    private Button btnRegisterBack; /*register back button*/
    private TextView textRegisterMessage; /*register message*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFirstName = (EditText) findViewById(R.id.txtFirstName); /*first name text*/
        txtLastName = (EditText) findViewById(R.id.txtLastName); /*last name text*/
        txtEmail = (EditText) findViewById(R.id.txtEmail); /*email text*/
        txtPassword = (EditText) findViewById(R.id.txtPassword); /*password text*/
        btnRegister = (Button) findViewById(R.id.btnRegister); /*register button*/
        btnRegisterBack = (Button) findViewById(R.id.btnRegisterBack); /*back register button*/
        textRegisterMessage = (TextView) findViewById(R.id.textRegisterMessage); /*register message*/

        btnRegisterBack.setOnClickListener(new View.OnClickListener() { /*login activity start when button clicked*/
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() { /*register activity start when button closed*/
            @Override
            public void onClick(View view) {
                /*check password, first name, last name, and email*/
                if ( ( !txtPassword.getText().toString().equals("")) && ( !txtFirstName.getText().toString().equals("")) &&
                        ( !txtLastName.getText().toString().equals("")) && ( !txtEmail.getText().toString().equals("")) )
                {
                     execute(view);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show(); /*error if fields are empty*/
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Inflate the menu; this adds items to the action bar if it is present.*/
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    /*register execution*/
    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String email,password,fname,lname; /*string for email, password, first name, last name*/

        /*pre execution of register*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fname = txtFirstName.getText().toString();
            lname = txtLastName.getText().toString();
            email = txtEmail.getText().toString();
            password = txtPassword.getText().toString();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle("QHubb");
            pDialog.setMessage("...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        /*JSON used to communicate with the server, execution stage*/
        @Override
        protected JSONObject doInBackground(String... args) {


            Service userFunction = new Service();
            JSONObject json = userFunction.registerUser(fname, lname, email, password);

            return json;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /*Checks for success message.*/
            try {
                if (json.getString("success") != null) {
                    textRegisterMessage.setText("");
                    String res = json.getString("success");

                    String red = json.getString("error");

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        textRegisterMessage.setText("Successfully Registered");
                        Toast.makeText(getApplicationContext(),
                                "Successfully Registered", Toast.LENGTH_SHORT).show(); /*user successfully registered*/

                        Intent registered = new Intent(getApplicationContext(), LoginActivity.class);

                        /*Close all views before launching Registered screen*/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);

                        finish();
                    }

                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                        textRegisterMessage.setText("User already exists"); /*user exists*/
                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                        textRegisterMessage.setText("Invalid Email"); /*user enter invalid email*/
                    }

                }


                else{
                    pDialog.dismiss();

                    textRegisterMessage.setText("Error in registration"); /*error registering*/
                }

            } catch (JSONException e) {
                e.printStackTrace();


            }
        }
    }
    public void execute(View view){
        new ActivityTask(this, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
                textRegisterMessage, new ProcessRegister()).execute();
    }
}
