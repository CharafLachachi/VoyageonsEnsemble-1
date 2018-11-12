package com.example.thamazgha.voyageonsensemble.services;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.thamazgha.voyageonsensemble.activities.DashboardActivity;
import com.example.thamazgha.voyageonsensemble.activities.SearchActivity;
import com.example.thamazgha.voyageonsensemble.tools.SearchResultItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchService {

    public WeakReference<SearchActivity> searchActivityWeakReference;
    public ArrayList<SearchResultItem> searchResultsList;
    public SearchService(SearchActivity searchActivity) {
        this.searchActivityWeakReference = new WeakReference<SearchActivity>(searchActivity);
    }

    public List<SearchResultItem> contactApi(String urlsearch, String destination, String date1, String date2){
        JSONObject json = new JSONObject();
        searchResultsList = new ArrayList<SearchResultItem>();

        try {
            json.put("destination", destination);
            json.put("date1", destination);
            json.put("date2", destination);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonarray = new JSONArray();
        try {
            jsonarray.put(0,json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TODO ajouter json au jsonarray demander a charaf ce que le serveur reçoit

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

                    //    searchResultsList.add(new SearchResultItem(img_url, roomPrice, checkOutDate, chekInDate, city, hotelName));
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


        QueueSingleton.getInstance(searchActivityWeakReference.get()).addToRequestQueue(jsonArrayRequest);

        return searchResultsList;
    }

    public String getDate1(String text) {
        String[] s = text.split("- ");
        String[] ss = s[1].split(" ");

        String[] sss = ss[0].split("/");

        Date d = new Date(Integer.parseInt(sss[2]),Integer.parseInt(sss[1]),Integer.parseInt(sss[0]));

        String todayString =d.toString();
        return todayString;
    }

    public String getDate2(String text) {
        String[] s = text.split("- ");
        String[] ss = s[1].split(" ");
        String[] sss = ss[2].split("/");

        Date d = new Date(Integer.parseInt(sss[2]),Integer.parseInt(sss[1]),Integer.parseInt(sss[0]));

        String todayString =d.toString();
        return todayString;
    }
}
