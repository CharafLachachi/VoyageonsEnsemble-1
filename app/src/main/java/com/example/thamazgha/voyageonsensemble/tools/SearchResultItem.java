package com.example.thamazgha.voyageonsensemble.tools;

import org.json.JSONObject;

public class SearchResultItem {

    private String img_url;
    private double roomPrice;
    private String checkOutDate;
    private String chekInDate;
    private String city;
    private String hotelName;
    private String picture;
    private JSONObject receivedJson;



    private String idUser;

    public SearchResultItem(String img_url, double roomPrice, String checkOutDate, String chekInDate, String city, String hotelName, String picture, JSONObject receivedJson) {
        this.img_url = img_url;
        this.roomPrice = roomPrice;
        this.checkOutDate = checkOutDate;
        this.chekInDate = chekInDate;
        this.city = city;
        this.hotelName = hotelName;
        this.picture =picture;
        this.receivedJson = receivedJson;
    }


    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public JSONObject getReceivedJson() {
        return receivedJson;
    }

    public void setReceivedJson(JSONObject receivedJson) {
        this.receivedJson = receivedJson;
    }
}
