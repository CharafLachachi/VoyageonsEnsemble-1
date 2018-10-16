package com.example.thamazgha.voyageonsensemble.activities;


import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView email;
    private EditText password;
    private Button loginButton;
    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = (Button) findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginHandler();
            }
        });

        email = (AutoCompleteTextView) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);

        register = (TextView) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void loginHandler() {

        final String userEmail = email.getText().toString();
        final String userPassword = password.getText().toString();



        JSONObject json = new JSONObject();

        try {
            json.put("email",userEmail);
            json.put("password",userPassword);
            Toast.makeText(LoginActivity.this, json.toString(), Toast.LENGTH_SHORT).show();




        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://134.157.123.150:8080/DAR_PROJECT/SignIn";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        //register.setText("Response: " + response.toString().substring(0,10));
                        JWT jwtInfoUser = null;
                        try {
                            jwtInfoUser = new JWT(response.get("token").toString());
                            //jwt.getClaim("email");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(LoginActivity.this, jwtInfoUser.getClaim("email").asString(), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(LoginActivity.this, DashboardActivity.class));


                        Intent intentDashboard = new Intent( LoginActivity.this, DashboardActivity.class);
                        intentDashboard.putExtra(EXTRA_MESSAGE, jwtInfoUser);
                        startActivityForResult(intentDashboard, 0);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, "user inexistant", Toast.LENGTH_LONG).show();
                    }
                });

        // Instantiate the RequestQueue.
        QueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

