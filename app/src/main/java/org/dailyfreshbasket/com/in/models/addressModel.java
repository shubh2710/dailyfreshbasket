package org.dailyfreshbasket.com.in.models;

/**
 * Created by shubham on 4/25/2018.
 */

public class addressModel {
    private String uid,adi,name,mobile,street,land,state,city,type,pin;
    private int charge=0;

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "addressModel{" +
                "uid='" + uid + '\'' +
                ", adi='" + adi + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", street='" + street + '\'' +
                ", land='" + land + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
