package org.dailyfreshbasket.co.in.informations;

import java.util.ArrayList;

/**
 * Created by shubham on 2/22/2017.
 */

public class Front_page_catagory_details {
    private String name_product;
    private String id_product;
    private String rate_product;
    private int available;
    private String mrp_product;
    private String tag_product;
    private ArrayList<String> pic_product;
    private boolean isAdded=false;
    public int quantity=0;

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String type;
    public void first_product_catagory_details(String name, String id, String rate , String mrp,String tags,int available, ArrayList<String> pic){
        this.name_product=name;
        this.id_product=id;
        this.rate_product=rate;
        this.mrp_product=mrp;
        this.pic_product=pic;
        this.tag_product=tags;
        this.available=available;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getRate_product() {
        return rate_product;
    }
    public String getType() {
        return type;
    }

    public String getName_product() {
        return name_product;
    }

    public String getId_product() {
        return id_product;
    }

    public String getMrp_product() {
        return mrp_product;
    }

    public String getTag_product() {
        return tag_product;
    }

    public ArrayList<String> getPic_product() {
        return pic_product;
    }
}
