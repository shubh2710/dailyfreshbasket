package org.dailyfreshbasket.co.in.informations;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shubham on 2/22/2017.
 */

public class Products_in_Search_details {
    private ArrayList<String> pics=new ArrayList<>();
    private String product_name, product_id,product_actualprice,product_mrp,product_shopid;
    private String product_stockquantity;
    private String product_cartquantity;
    private String product_rating;
    private String product_offer;
    private String discount;
    private String features;

    public String getProduct_specs() {
        return product_specs;
    }

    public void setProduct_specs(String product_specs) {
        this.product_specs = product_specs;
    }

    private String product_specs;
    private Date createDate;
    private int available;
    private boolean isAdded=false;
    public int quantity=0;
    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public void add_product(String discount, String features, String product_name, String product_id, String product_actualprice, String product_mrp, String product_shopid, String product_stockquantity, String product_cartquantity, String product_rating, String product_offer, ArrayList<String> pics){
        this.product_name=product_name;
        this.discount=discount;
        this.features=features;
        this.product_id=product_id;
        this.product_actualprice=product_actualprice;
        this.product_mrp=product_mrp;
        this.product_shopid=product_shopid;
        this.product_cartquantity=product_cartquantity;
        this.product_stockquantity=product_stockquantity;
        this.product_offer=product_offer;
        this.product_rating=product_rating;
        this.pics=pics;
//        this.product_specs = specs1;

    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_actualprice() {
        return product_actualprice;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public String getProduct_shopid() {
        return product_shopid;
    }

    public String getProduct_stockquantity() {
        return product_stockquantity;
    }

    public String getProduct_cartquantity() {
        return product_cartquantity;
    }

    public String getProduct_rating() {
        return product_rating;
    }

    public String getProduct_offer() {
        return product_offer;
    }
}
