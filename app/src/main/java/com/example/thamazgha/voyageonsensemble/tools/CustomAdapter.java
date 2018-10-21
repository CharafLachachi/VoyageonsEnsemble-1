package com.example.thamazgha.voyageonsensemble.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thamazgha.voyageonsensemble.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.PublicationViewHolder> {
    private Context context;
    private ArrayList<PublicationItem> publicationsList;

    public CustomAdapter(Context context, ArrayList<PublicationItem> publicationsList) {
        this.context = context;
        this.publicationsList = publicationsList;
    }


    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_item_view, parent, false);
        return new PublicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder publicationViewHolder, int position) {
        PublicationItem currentItem = publicationsList.get(position);

        String img_url = currentItem.getImg_url();
        int pub_owner = currentItem.getPub_owner();
        double roomPrice = currentItem.getRoomPrice();
        int nbPers = currentItem.getNbPers();
        String checkOutDate = currentItem.getCheckOutDate();
        String chekInDate = currentItem.getChekInDate();
        String city = currentItem.getCity();
        String hotelName = currentItem.getHotelName();

        publicationViewHolder.pub_owner.setText("owner"+pub_owner);
        publicationViewHolder.roomPrice.setText("Price : "+roomPrice);
        publicationViewHolder.nbPers.setText(nbPers+" participants");
        publicationViewHolder.checkOutDate.setText(checkOutDate);
        publicationViewHolder.chekInDate.setText(chekInDate);
        publicationViewHolder.city.setText("Destination : "+city);
        publicationViewHolder.hotelName.setText("Hotel : "+hotelName);

      //  Picasso.with(context).load(img_url).fit().centerInside().into(publicationViewHolder)
        ;
       Picasso.get().load(img_url).fit().centerInside().into(publicationViewHolder.meteo);
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
        }
    }
}
