package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.provider.CalendarContract;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {

    public static final String SHARED_PREFS = "sharedPrefs";
    public ArrayList<SearchResultItem> searchresultlist;
    public WeakReference<SearchActivity> SearchActivityWeakReference;
    private Context context;
    private JWT localStorage;
     String api ;

    public SearchResultsAdapter(Context context, ArrayList<SearchResultItem> searchresultlist,
                                String localStorage, SearchActivity searchActivity) {
        this.context = context;
        this.searchresultlist = searchresultlist;
        this.localStorage = new JWT(localStorage);
        this.SearchActivityWeakReference = new WeakReference<SearchActivity>(searchActivity);
        api = context.getString(R.string.api)+"";
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
        final int pos = position;
        String img_url = currentItem.getImg_url();
        double roomPrice = currentItem.getRoomPrice();
        final String checkOutDate = currentItem.getCheckOutDate();
        final String chekInDate = currentItem.getChekInDate();
        String city = currentItem.getCity();
        final String hotelName = currentItem.getHotelName();
        String picture_url = currentItem.getPicture();
        searchResultViewHolder.roomPrice.setText("Price : " + roomPrice);
        searchResultViewHolder.checkOutDate.setText(checkOutDate);
        searchResultViewHolder.chekInDate.setText(chekInDate);
        searchResultViewHolder.city.setText("Destination : " + city);
        searchResultViewHolder.hotelName.setText("Hotel : " + hotelName);

        searchResultViewHolder.meteo.setImageResource(getResourceID("we_"+img_url,"drawable",context));
        Picasso.with(context).load(picture_url).into(searchResultViewHolder.picture);

        searchResultViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareHAndler(pos);
                long checkI = 0, checkO = 0;
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date ci = f.parse(chekInDate);
                    checkI = ci.getTime();

                    Date co = f.parse(checkOutDate);
                    checkO = co.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                onAddEventClicked("city", checkI, checkO);
            }
        });


    }

    private void shareHAndler(int pos) {


        SearchResultItem currentItem = searchresultlist.get(pos);
        currentItem.setIdUser("1");/*this.localStorage.getClaim("id").asString()*/
       // String currentJson = new Gson().toJson(currentItem.getReceivedJson());

        JSONObject json = null;
        try {
            //json = new JSONObject(currentJson);
            currentItem.getReceivedJson().put("idUser",Integer.parseInt(this.localStorage.getClaim("id").asString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, api+"/sharePublication", currentItem.getReceivedJson(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("yes", response.toString());

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

    public void onAddEventClicked(String dest, long checkI, long checkO) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, checkI);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, checkO);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, "Voyage");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Destination :" + dest);
        intent.putExtra(CalendarContract.Events.CALENDAR_COLOR, "#e6c229");

        context.startActivity(intent);
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {

        public ImageView meteo;
        public TextView roomPrice;
        public TextView checkOutDate;
        public TextView chekInDate;
        public TextView city;
        public TextView hotelName;
        public Button share;
        public ImageView picture;

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
            picture = itemView.findViewById(R.id.picture);
        }
    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }
}
