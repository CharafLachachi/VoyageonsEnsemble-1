package com.example.thamazgha.voyageonsensemble.tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ItemViewHolder>{
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView meteo;
        public TextView pub_owner;
        public TextView roomPrice;
        public TextView nbPers;
        public TextView checkOutDate;
        public TextView chekInDate;
        public TextView city;
        public TextView hotelName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            meteo = itemView.findViewById(R.id)
        }
    }



}