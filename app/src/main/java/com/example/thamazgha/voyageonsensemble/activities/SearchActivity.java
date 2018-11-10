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
import com.example.thamazgha.voyageonsensemble.adapters.CustomAdapter;
import com.example.thamazgha.voyageonsensemble.adapters.SearchResultsAdapter;
import com.example.thamazgha.voyageonsensemble.services.SearchService;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.tools.SearchResultItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    SearchService searchService;
    ImageView dateButton;
    Calendar now;
    String urlsearch;
    private RecyclerView mRecyclerView;
    private SearchResultsAdapter mSearchResultsAdapter;
    private ArrayList<SearchResultItem> mSearchResultsList;
    private Button destination;
    private TextView dateTextView;
    private TextView timeTextView;
    private boolean mAutoHighlight;
    private Button search;
    private String localStorage ;
    public static final String SHARED_PREFS = "sharedPrefs";
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        urlsearch = getString(R.string.api) + "/ic_search";

        searchService = new SearchService(this);
        dateButton = (ImageView) findViewById(R.id.dateButton);
        dateTextView = (TextView) findViewById(R.id.date_range);
        destination = (Button)  findViewById(R.id.destination);
        search = (Button) findViewById(R.id.search);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsList = new ArrayList<SearchResultItem>();

        localStorage = getLocalStorage();
        mSearchResultsAdapter = new SearchResultsAdapter(this, mSearchResultsList,localStorage,this);
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
                String dest = destination.getText().toString();
                String date1 = searchService.getDate1(dateTextView.getText().toString());
                String date2 = searchService.getDate2(dateTextView.getText().toString());

                //mSearchResultsList.addAll(searchService.contactApi(urlsearch, dest, date1, date2));
                //mSearchResultsAdapter.notifyDataSetChanged();
                contactApi();
                Toast.makeText(SearchActivity.this, dateTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
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
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

    }

    private String getLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Log.d("tokenn",sharedPreferences.getString("token",""));
        //return sharedPreferences.getString("token","");
        //TODO just for test | modify this later
        return sharedPreferences.getString("token", "");
    }





    public void contactApi(){
        JSONObject json = new JSONObject();

        try {
            json.put("destination", destination);
            json.put("date1", destination);
            json.put("date2", destination);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonarray = new JSONArray();
        //TODO ajouter json au jsonarray

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, urlsearch,
                jsonarray, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response",response.toString());
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
}

