package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.jwt.JWT;
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.activities.MainActivity;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.PublicationViewHolder> {
    final String api;
    public ArrayList<PublicationItem> publicationsList;
    public static final String SHARED_PREFS = "sharedPrefs";
    String jwtUser;
    public WeakReference<MainActivity> MainActivityWeakReference;
    private Context context;


    //DashboardService dashboardService;

    public CustomAdapter(Context context, ArrayList<PublicationItem> publicationsList, MainActivity mainActivity) {
        this.context = context;
        this.publicationsList = publicationsList;
        this.MainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        api = "http://192.168.1.44:8080/DAR_PROJECT";

        //this.dashboardService = new DashboardService(publicationsList,this.DAshboardActivityWeakReference);
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_item_view, parent, false);
        return new PublicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder publicationViewHolder, final int position) {
        PublicationItem currentItem = publicationsList.get(position);

        String img_url = currentItem.getImg_url();
        final String userNameOwner = currentItem.getUserNameOwner();
        double roomPrice = currentItem.getRoomPrice();
        int nbPers = currentItem.getNbPers();
        final String checkOutDate = currentItem.getCheckOutDate();
        final String chekInDate = currentItem.getChekInDate();
        final String city = currentItem.getCity();
        final String hotelName = currentItem.getHotelName();
        String picture_url = currentItem.getPicture();
        final int pub_id = currentItem.getPub_id();
        final String ownerName = currentItem.getOwnerName();
        // Jai change pub_id peut poser probelem pour join
        publicationViewHolder.pub_owner.setText(ownerName);
        publicationViewHolder.roomPrice.setText(String.valueOf(roomPrice));
        publicationViewHolder.nbPers.setText(String.valueOf(nbPers));
        publicationViewHolder.checkOutDate.setText(checkOutDate);
        publicationViewHolder.chekInDate.setText(chekInDate);
        publicationViewHolder.city.setText(city);
        publicationViewHolder.hotelName.setText(hotelName);
        // Verify publication is full
        if (currentItem.getAbonnesCount() == nbPers)
        publicationViewHolder.join.setEnabled(false);

        // Verify if currentUser is owner of the publication
        if (isCurrentUserOwnerOfPublication(currentItem.getUserNameOwner())){
            publicationViewHolder.join.setVisibility(View.GONE);
            publicationViewHolder.quit.setVisibility(View.GONE);
        }


        publicationViewHolder.meteo.setImageResource(getResourceID("we_"+img_url,"drawable",context));
        //Picasso.with(context).load(img_url).fit().into(publicationViewHolder.meteo);
        Picasso.with(context).load(picture_url).into(publicationViewHolder.picture);

        publicationViewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, hotelName, Toast.LENGTH_SHORT).show();
                joinHandler(1, pub_id, position);

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

                onAddEventClicked(city, checkI, checkO);
            }


        });

        publicationViewHolder.quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitHandler(1, pub_id, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.publicationsList.size();
    }


    public void joinHandler(int abo_id, int pub_id, final int position) {
        MainActivity activity = MainActivityWeakReference.get();
        String url = api + "/join";
        JSONObject json = new JSONObject();

        final int pos = position;
        try {

            //TODO recuperer userID apartir de connexionactivity
            json.put("userid", 1);
            json.put("pubid", pub_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        publicationsList.get(pos).setNbPers(publicationsList.get(pos).getNbPers() + 1);
                        notifyItemChanged(position);
                        Log.d("Me : server responce", response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                        Log.e("me : errorCustomAdapter", "jsonObjectRequest");
                    }
                });

        QueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public void quitHandler(int abo_id, int pub_id, int position) {
        MainActivity activity = MainActivityWeakReference.get();
        String url = api + "/UnsubscribeToPublication";

        JSONObject json = new JSONObject();

        final int pos = position;
        try {
            //TODO recuperer userID apartir de connexionactivity
            json.put("UserId", 1);
            json.put("publicationId", pub_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Me : server responce",response.toString());
                        publicationsList.get(pos).setNbPers(publicationsList.get(pos).getNbPers() - 1);
                        notifyItemChanged(pos);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                });

        QueueSingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
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


    public static class PublicationViewHolder extends RecyclerView.ViewHolder {

        public ImageView meteo;
        public TextView pub_owner;
        public TextView roomPrice;
        public TextView nbPers;
        public TextView checkOutDate;
        public TextView chekInDate;
        public TextView city;
        public TextView hotelName;
        public ImageView picture;
        public Button join;
        public Button quit;


        public RelativeLayout parentLayaout;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            meteo = itemView.findViewById(R.id.meteo);
            picture = itemView.findViewById(R.id.picture);
            pub_owner = itemView.findViewById(R.id.owner);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            nbPers = itemView.findViewById(R.id.nbPrs);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
            chekInDate = itemView.findViewById(R.id.chekInDate);
            city = itemView.findViewById(R.id.city);
            hotelName = itemView.findViewById(R.id.hotelName);
            join = itemView.findViewById(R.id.join);
            quit = itemView.findViewById(R.id.quit);
            parentLayaout = itemView.findViewById(R.id.parent_layaout);

        }


    }

    private boolean isCurrentUserOwnerOfPublication(String pubOwner){
        jwtUser = getLocalStorage();
        JWT jwtuser = new JWT(jwtUser);
        String currentUserId = jwtuser.getClaim("id").asString();
        if (currentUserId.equals(pubOwner)) return true;
        return false;
    }
    private String getLocalStorage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("tokenn", sharedPreferences.getString("token", ""));
        return sharedPreferences.getString("token", "");
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
