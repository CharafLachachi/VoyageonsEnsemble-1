package com.example.thamazgha.voyageonsensemble.tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.thamazgha.voyageonsensemble.R;

public class Item extends RecyclerView.ViewHolder{

    public String owner;
    private float roomPrice;
    private int nbPers;
    private String checkOutDate;
    private String chekInDate;
    private String city;
    private String hotelName;
    String imageURL;



    public Item(@NonNull View itemView, String owner, float roomPrice,
                int nbPers, int radius, String checkOutDate, String chekInDate,
                String city, String hotelName, String imageURL) {
        super(itemView);

            this.owner = owner;
            this.roomPrice = roomPrice;
            this.nbPers = nbPers;
            this.checkOutDate = checkOutDate;
            this.chekInDate = chekInDate;
            this.city = city;
            this.hotelName = hotelName;
        this.imageURL = imageURL;

    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(float roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getNbPers() {
        return nbPers;
    }

    public void setNbPers(int nbPers) {
        this.nbPers = nbPers;
    }


    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getChekInDate() {
        return chekInDate;
    }

    public void setChekInDate(String chekInDate) {
        this.chekInDate = chekInDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }



}
