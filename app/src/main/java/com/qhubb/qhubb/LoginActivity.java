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

public class LoginActivity extends Activity {

    private Button btnLogin; /*login button*/
    private Button btnRegister; /*register button*/
    private Button btnForgotPassword; /*forgot password button*/
    private EditText txtEmail; /*enter text for email*/
    private EditText txtPassword; /*enter text for password*/
    private TextView textLoginMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); /*xml for login activity*/

        txtEmail = (EditText) findViewById(R.id.txtEmail); /*email text*/
        txtPassword = (EditText) findViewById(R.id.txtPassword); /*password text*/
        btnLogin = (Button) findViewById(R.id.btnLogin); /*login button*/
        btnRegister = (Button) findViewById(R.id.btnRegister); /*register button*/
        btnForgotPassword = (Button)findViewById(R.id.btnForgotPassword); /*forgot password button*/
        textLoginMessage = (TextView) findViewById(R.id.textLoginMessage); /*login message text*/

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                /*email and password not empty*/
                if (  ( !txtEmail.getText().toString().equals("")) && ( !txtPassword.getText().toString().equals("")) )
                {
                    execute(view); /*execute*/
                }
                else if ( ( !txtEmail.getText().toString().equals("")) ) /*password empty*/
                {
                    Toast.makeText(getApplicationContext(),
                            "Password empty", Toast.LENGTH_SHORT).show(); /*notify user password empty*/
                }
                else if ( ( !txtPassword.getText().toString().equals("")) ) /*email empty*/
                {
                    Toast.makeText(getApplicationContext(),
                            "Email empty", Toast.LENGTH_SHORT).show(); /*notify user email empty*/
                }
                else
                {
                    Toast.makeText(getApplicationContext(), /*email and password empty*/
                            "Email and Password field empty", Toast.LENGTH_SHORT).show(); /*notify user email and password empty*/
                }
            }
        });

        /*start register activity if button clicked*/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }});

        /*start forget password activity if button clicked*/
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), ForgotPasswordActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }});
    }

    public void execute(View view){
        new ActivityTask(this, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE),
                textLoginMessage, new ProcessLogin()).execute();
    }

    /*executing of login*/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String email,password;/*email and password of login*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email = txtEmail.getText().toString();
            password = txtPassword.getText().toString();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("QHubb");
            pDialog.setMessage("Attempting to Login");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /*JSON used to communicate with the server, execution stage*/
        @Override
        protected JSONObject doInBackground(String... args) {

            Service userFunction = new Service();
            JSONObject json = userFunction.loginUser(email, password);
            return json;
        }

        /*post execute stage*/
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString("success") != null) {

                    String res = json.getString("success");

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading...");
                        pDialog.setTitle("...");

                        /*If JSON array details are stored in SQlite it launches the User Panel.*/
                        Intent upanel = new Intent(getApplicationContext(), MainActivity.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        upanel.putExtra("email", email);
                        upanel.putExtra("deactive", json.getJSONObject("user").getString("deactive"));
                        pDialog.dismiss();
                        startActivity(upanel);
                        /*Close Login Screen*/
                        finish();
                    }else{

                        pDialog.dismiss();
                        textLoginMessage.setText(json.getString("error_msg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*Inflate the menu; this adds items to the action bar if it is present.*/
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
