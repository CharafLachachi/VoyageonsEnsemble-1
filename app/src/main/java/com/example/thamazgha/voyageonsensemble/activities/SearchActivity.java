package com.example.thamazgha.voyageonsensemble.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.adapters.SearchResultsAdapter;
import com.example.thamazgha.voyageonsensemble.services.SearchService;
import com.example.thamazgha.voyageonsensemble.tools.SearchResultItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SearchActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView mRecyclerView;
    private SearchResultsAdapter mSearchResultsAdapter;
    private ArrayList<SearchResultItem> mSearchResultsList;
    private Button destination;
    private EditText pers,price,radius;


    public static final String SHARED_PREFS = "sharedPrefs";
    SearchService searchService;
    ImageView dateButton;
    Calendar now;
    String urlsearch;
    Toolbar toolbar;
    String date1, date2;
    private TextView dateTextView;
    private TextView timeTextView;
    private boolean mAutoHighlight;
    private Button search;
    private String localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        urlsearch = getString(R.string.api) + "/search";
        searchService = new SearchService(this);


        /*toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);


        dateButton = (ImageView) findViewById(R.id.dateButton);
        dateTextView = (TextView) findViewById(R.id.date_range);
        destination = (Button) findViewById(R.id.destination);
        search = (Button) findViewById(R.id.search);

        pers = (EditText) findViewById(R.id.pers);
        price = (EditText) findViewById(R.id.price);
        radius = (EditText) findViewById(R.id.radius);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsList = new ArrayList<SearchResultItem>();

        localStorage = getLocalStorage("token");
        mSearchResultsAdapter = new SearchResultsAdapter(this, mSearchResultsList, localStorage, this);
        mRecyclerView.setAdapter(mSearchResultsAdapter);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        SearchActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(true);
                dpd.setAccentColor(Color.WHITE);
                dpd.setAutoHighlight(mAutoHighlight);
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactApi();
                Toast.makeText(SearchActivity.this, dateTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "You picked the following date: From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        dateTextView.setText(date);

        //ZonedDateTime zdt = ZonedDateTime.of(year,monthOfYear,dayOfMonth,0,0,0,0,TimeZone.getDefault().toZoneId());

        date1 = year  + "-" + (++monthOfYear) + "-" + dayOfMonth;
        date2 =yearEnd  + "-" + (++monthOfYearEnd) + "-" + dayOfMonthEnd;

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

    }

    private String getLocalStorage(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("tokenn", sharedPreferences.getString(s, ""));
        return sharedPreferences.getString(s, "");
    }


    public void contactApi() {
       final JSONObject json = new JSONObject();

        try {
            //create city json
            // create cities array
            JSONArray jsonarray = new JSONArray();
            jsonarray.put(getLocalStorage("cityName"));
            // put cities array in request json
            json.put("cities",jsonarray);
            json.put("chekInDate", date1+"T22:00:00.000Z");
            json.put("checkOutDate", date2+"T22:00:00.000Z");
            json.put("radius",radius.getText());
            json.put("nbPers",pers.getText());
            json.put("price",price.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(SearchActivity.this, json.toString(), Toast.LENGTH_LONG).show();




        //TODO ajouter json au jsonarray

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsearch,
                 new Response.Listener<String>() {

            @Override
            public void onResponse(String respon) {


                try {
                    Log.d("response", respon);
                    JSONArray response = new JSONArray(respon);
                    for (int i = 0; i < response.length(); i++) {

                        Log.e("responnnse",response.toString());
                        JSONObject search_result = response.getJSONObject(i);
                        double roomPrice = search_result.getDouble("roomPrice");
                        String checkOutDate = search_result.getString("checkOutDate");
                        String chekInDate = search_result.getString("chekInDate");
                        String city = search_result.getString("city");
                        String hotelName = search_result.getString("hotelName");
                        String picture_url = search_result.getString("picture");
                        JSONObject weather = search_result.getJSONObject("weather");
                        String img_url = weather.getString("icon");

                        mSearchResultsList.add(new SearchResultItem(img_url, roomPrice, checkOutDate, chekInDate, city, hotelName,picture_url,search_result));
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
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return json.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        QueueSingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private String formateDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        DateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        String s = "";
        try {
            s = df.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("Date",s);
        return s;
    }


}

