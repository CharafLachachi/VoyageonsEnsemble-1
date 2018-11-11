package com.example.thamazgha.voyageonsensemble.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.thamazgha.voyageonsensemble.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private  EditText firstname;
    private  EditText lastname;
    private EditText password;
    private AutoCompleteTextView email;
    private TextView goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        password = findViewById(R.id.password);
        goToLogin = findViewById(R.id.login);

        Button formBtn = (Button) findViewById(R.id.sign_up_button);
        formBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegister();
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToLogin();
            }
        });
    }

    public void sendRegister() {
        RequestQueue queue = Volley.newRequestQueue(this);
        // For test i create cities array

        JSONArray cities = new JSONArray();
        cities.put("Paris, France");
        cities.put("Lyon, France");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        params.put("firstname", firstname.getText().toString());
        params.put("lastname", lastname.getText().toString());
        params.put("username",email.getText().toString() );

        String api = getString(R.string.api);
        final String  url = api+"/SignUp";

        JSONObject paramJson = new JSONObject(params);
        try {
            paramJson.put("cities",cities);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        JsonObjectRequest req = new JsonObjectRequest(url, paramJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Context context = getApplicationContext();

                            CharSequence text = "Account created !!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    redirectToLogin();
                                }
                            },duration);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(req);

    }

    private void redirectToLogin(){
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

}

/*

    public void contactApi() {
        JSONObject json = new JSONObject();
        JSONObject city = new JSONObject();
        try {
            JSONArray cities = new JSONArray();
            city.put("cityName", getLocalStorage("cityName"));
            cities.put(city);
            json.put("checkIn", formateDate(date1));
            json.put("checkOut", formateDate(date2));
            json.put("cities", cities);
            Log.e("zzzzzzzzzzzzzzzz", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(SearchActivity.this, json.toString(), Toast.LENGTH_LONG).show();

        JSONArray jsonarray = new JSONArray();
        jsonarray.put(json);

        Log.e("tableauuuu", jsonarray.toString());
        //TODO ajouter json au jsonarray

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlsearch,
                jsonarray, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject search_result = response.getJSONObject(i);
                        double roomPrice = search_result.getDouble("roomPrice");
                        String checkOutDate = search_result.getString("checkOutDate");
                        String chekInDate = search_result.getString("chekInDate");
                        String city = search_result.getString("city");
                        String hotelName = search_result.getString("hotelName");

                        JSONObject weather = search_result.getJSONObject("weather");
                        String img_url = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";

                        mSearchResultsList.add(new SearchResultItem(img_url, roomPrice, checkOutDate, chekInDate, city, hotelName));
                        mSearchResultsAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }
        );
        QueueSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }
 */

