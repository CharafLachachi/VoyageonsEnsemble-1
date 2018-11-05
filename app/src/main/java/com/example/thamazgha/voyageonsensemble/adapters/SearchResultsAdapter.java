package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.activities.DashboardActivity;
import com.example.thamazgha.voyageonsensemble.activities.LoginActivity;
import com.example.thamazgha.voyageonsensemble.activities.SearchActivity;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.tools.SearchResultItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>{

    public static final String SHARED_PREFS = "sharedPrefs";
    private Context context;
    public ArrayList<SearchResultItem> searchresultlist;
    private JWT localStorage;

    public WeakReference<SearchActivity> SearchActivityWeakReference;

    public SearchResultsAdapter(Context context, ArrayList<SearchResultItem> searchresultlist,
                                String localStorage, SearchActivity searchActivity) {
        this.context = context;
        this.searchresultlist = searchresultlist;
        this.localStorage = new JWT(localStorage);
        this.SearchActivityWeakReference = new WeakReference<SearchActivity>(searchActivity);

    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultsAdapter.SearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder searchResultViewHolder, final int position) {
        SearchResultItem currentItem = searchresultlist.get(position);
        final int pos =position ;
        String img_url = currentItem.getImg_url();
        double roomPrice = currentItem.getRoomPrice();
        String checkOutDate = currentItem.getCheckOutDate();
        String chekInDate = currentItem.getChekInDate();
        String city = currentItem.getCity();
        final String hotelName = currentItem.getHotelName();

        searchResultViewHolder.roomPrice.setText("Price : "+roomPrice);
        searchResultViewHolder.checkOutDate.setText(checkOutDate);
        searchResultViewHolder.chekInDate.setText(chekInDate);
        searchResultViewHolder.city.setText("Destination : "+city);
        searchResultViewHolder.hotelName.setText("Hotel : "+hotelName);
        Picasso.with(context).load(img_url).into(searchResultViewHolder.meteo);

        final String url = Resources.getSystem().getString(R.string.api);
        searchResultViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              shareHAndler(pos);
            }
        });


    }

    private void shareHAndler(int pos) {


        SearchResultItem currentItem = searchresultlist.get(pos);
        currentItem.setUserId("");

        String currentJson = new Gson().toJson(currentItem);
        currentItem.setUserId(this.localStorage.getClaim("id").asString());
        JSONObject json = null;
        try {
            json = new JSONObject(currentJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Resources.getSystem().getString(R.string.api),json , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("yes",response.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        SearchActivity activity = SearchActivityWeakReference.get();
        QueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        return this.searchresultlist.size();
    }





    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {

        public ImageView meteo;
        public TextView roomPrice;
        public TextView checkOutDate;
        public TextView chekInDate;
        public TextView city;
        public TextView hotelName;
        public Button share;

        public ConstraintLayout parentLayaout;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            meteo = itemView.findViewById(R.id.meteo);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
            chekInDate = itemView.findViewById(R.id.chekInDate);
            city = itemView.findViewById(R.id.city);
            hotelName = itemView.findViewById(R.id.hotelName);
            share = itemView.findViewById(R.id.share);
            parentLayaout = itemView.findViewById(R.id.item_search_result);
        }
    }

}
