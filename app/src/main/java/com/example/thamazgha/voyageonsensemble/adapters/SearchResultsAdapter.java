package com.example.thamazgha.voyageonsensemble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thamazgha.voyageonsensemble.R;
import com.example.thamazgha.voyageonsensemble.tools.PublicationItem;
import com.example.thamazgha.voyageonsensemble.tools.SearchResultItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>{

    private Context context;
    public ArrayList<SearchResultItem> searchresultlist;

    public SearchResultsAdapter(Context context, ArrayList<SearchResultItem> searchresultlist) {
        this.context = context;
        this.searchresultlist = searchresultlist;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultsAdapter.SearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder searchResultViewHolder, int position) {
        SearchResultItem currentItem = searchresultlist.get(position);

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

        searchResultViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
