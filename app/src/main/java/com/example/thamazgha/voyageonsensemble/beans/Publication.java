package com.example.thamazgha.voyageonsensemble.beans;

public class Publication {

    private int Pub_id;
    private int owner;
    private float roomPrice;
    private int nbPers;
    private int radius;
    private String checkOutDate;
    private String chekInDate;


    public int getPub_id() {
        return Pub_id;
    }

    public void setPub_id(int pub_id) {
        Pub_id = pub_id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
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



    public Publication(int pub_id, int owner, float roomPrice, int nbPers, int radius, String checkOutDate, String chekInDate) {
        Pub_id = pub_id;
        this.owner = owner;
        this.roomPrice = roomPrice;
        this.nbPers = nbPers;
        this.radius = radius;
        this.checkOutDate = checkOutDate;
        this.chekInDate = chekInDate;
    }

}
