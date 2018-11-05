package com.example.thamazgha.voyageonsensemble.tools;

public class SearchResultItem {

    private String img_url;
    private double roomPrice;
    private String checkOutDate;
    private String chekInDate;
    private String city;
    private String hotelName;


    private String userId;

    public SearchResultItem(String img_url, double roomPrice, String checkOutDate, String chekInDate, String city, String hotelName) {
        this.img_url = img_url;
        this.roomPrice = roomPrice;
        this.checkOutDate = checkOutDate;
        this.chekInDate = chekInDate;
        this.city = city;
        this.hotelName = hotelName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
