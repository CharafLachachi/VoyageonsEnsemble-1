package com.example.thamazgha.voyageonsensemble.tools;

public class PublicationItem {

    private int pub_id;
    private String img_url;
    private double roomPrice;
    private int nbPers;
    private String checkOutDate;
    private String chekInDate;
    private String city;
    private String hotelName;
    private String picture;
    private String userNameOwner;
    private String ownerName;
    private int abonnesCount;


    public PublicationItem(int pub_id, String img_url, String userNameOwner, double roomPrice,
                           int nbPers, String checkOutDate, String chekInDate,
                           String city, String hotelName, String picture,String ownerName
    ,int abonnesCount) {
        this.pub_id = pub_id;
        this.img_url = img_url;
        this.roomPrice = roomPrice;
        this.nbPers = nbPers;
        this.checkOutDate = checkOutDate;
        this.chekInDate = chekInDate;
        this.city = city;
        this.hotelName = hotelName;
        this.userNameOwner = userNameOwner;
        this.picture = picture;
        this.ownerName = ownerName;
        this.abonnesCount = abonnesCount;
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

    public String getUserNameOwner() {
        return userNameOwner;
    }

    public void setUserNameOwner(String userNameOwner) {
        this.userNameOwner = userNameOwner;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getAbonnesCount() {
        return abonnesCount;
    }

    public void setAbonnesCount(int abonnesCount) {
        this.abonnesCount = abonnesCount;
    }
}