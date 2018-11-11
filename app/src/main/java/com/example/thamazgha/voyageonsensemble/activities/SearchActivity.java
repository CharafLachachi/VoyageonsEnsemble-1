package com.example.thamazgha.voyageonsensemble.activities;


import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.adapters.SearchResultsAdapter;
import com.example.thamazgha.voyageonsensemble.services.SearchService;
import com.example.thamazgha.voyageonsensemble.tools.SearchResultItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

        date1 = dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        date2 = dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;

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
        JSONObject json = new JSONObject();
        JSONObject city = new JSONObject();
        try {
            json.put("city", getLocalStorage("cityName"));
            json.put("checkIn", formateDate(date1));
            json.put("checkOut", formateDate(date2));
            json.put("radius",radius.getText());
            json.put("nbPers",pers.getText());
            json.put("roomPrice",price.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(SearchActivity.this, json.toString(), Toast.LENGTH_LONG).show();

        JSONArray jsonarray = new JSONArray();
        jsonarray.put(json);


        //TODO ajouter json au jsonarray

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlsearch,
                jsonarray, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {

                        Log.e("responnnse",response.toString());
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

    private String formateDate(String date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String s = "";
        try {
            s = df.format(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }


}

