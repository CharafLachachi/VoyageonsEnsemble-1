package com.example.thamazgha.voyageonsensemble.tools;

import android.content.Context;
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
import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.activities.DashboardActivity;
import com.example.thamazgha.voyageonsensemble.services.DashboardService;
import com.example.thamazgha.voyageonsensemble.volley.QueueSingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.PublicationViewHolder> {
    private Context context;
    private ArrayList<PublicationItem> publicationsList;
    protected WeakReference<DashboardActivity> DAshboardActivityWeakReference;

    DashboardService dashboardService;

    public CustomAdapter(Context context, ArrayList<PublicationItem> publicationsList,DashboardActivity dashboardActivity) {
        this.context = context;
        this.publicationsList = publicationsList;
        this.DAshboardActivityWeakReference = new WeakReference<DashboardActivity>(dashboardActivity);
        this.dashboardService = new DashboardService(publicationsList,this.DAshboardActivityWeakReference);
    }


    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_item_view, parent, false);
        return new PublicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PublicationViewHolder publicationViewHolder, final int position) {
        PublicationItem currentItem = publicationsList.get(position);

        String img_url = currentItem.getImg_url();
        final int pub_owner = currentItem.getPub_owner();
        double roomPrice = currentItem.getRoomPrice();
        int nbPers = currentItem.getNbPers();
        String checkOutDate = currentItem.getCheckOutDate();
        String chekInDate = currentItem.getChekInDate();
        String city = currentItem.getCity();
        final String hotelName = currentItem.getHotelName();

        final int pub_id = currentItem.getPub_id();
        publicationViewHolder.pub_owner.setText("owner"+pub_owner);
        publicationViewHolder.roomPrice.setText("Price : "+roomPrice);
        publicationViewHolder.nbPers.setText(nbPers+" participants");
        publicationViewHolder.checkOutDate.setText(checkOutDate);
        publicationViewHolder.chekInDate.setText(chekInDate);
        publicationViewHolder.city.setText("Destination : "+city);
        publicationViewHolder.hotelName.setText("Hotel : "+hotelName);


        Picasso.with(context).load(img_url).into(publicationViewHolder.meteo);
        publicationViewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("msg du bouton join",publicationViewHolder.pub_owner.getText().toString());
                Toast.makeText(context, hotelName, Toast.LENGTH_SHORT).show();
                dashboardService.joinHandler(1, pub_id,position);

            }


        });
    }


    @Override
    public int getItemCount() {
        return this.publicationsList.size();
    }


    public class PublicationViewHolder extends RecyclerView.ViewHolder {

        public ImageView meteo;
        public TextView pub_owner;
        public TextView roomPrice;
        public TextView nbPers;
        public TextView checkOutDate;
        public TextView chekInDate;
        public TextView city;
        public TextView hotelName;

        public Button join;

        public ConstraintLayout parentLayaout;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            meteo = itemView.findViewById(R.id.meteo);
            pub_owner = itemView.findViewById(R.id.owner);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            nbPers = itemView.findViewById(R.id.nbPrs);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
            chekInDate = itemView.findViewById(R.id.chekInDate);
            city = itemView.findViewById(R.id.city);
            hotelName = itemView.findViewById(R.id.hotelName);
            join = itemView.findViewById(R.id.join);
            parentLayaout = itemView.findViewById(R.id.parent_layaout);

        }
    }
}
