package com.example.thamazgha.voyageonsensemble.tools;

public class Publication {

    private int pub_id;
    private String img_url;
    private int pub_owner;
    private double roomPrice;
    private int nbPers;
    private String checkOutDate;
    private String chekInDate;
    private String city;
    private String hotelName;

    public Publication(int pub_id, String img_url, int pub_owner, double roomPrice,
                           int nbPers, String checkOutDate, String chekInDate,
                           String city, String hotelName) {
        this.pub_id = pub_id;
        this.img_url = img_url;
        this.pub_owner = pub_owner;
        this.roomPrice = roomPrice;
        this.nbPers = nbPers;
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

    public int getPub_owner() {
        return pub_owner;
    }

    public void setPub_owner(int pub_owner) {
        this.pub_owner = pub_owner;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
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

    public int getPub_id() {
        return pub_id;
    }

    public void setPub_id(int pub_id) {
        this.pub_id = pub_id;
    }
}
