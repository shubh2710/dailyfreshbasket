package org.dailyfreshbasket.com.in.informations;

import java.util.ArrayList;

/**
 * Created by shubham on 2/22/2017.
 */

public class Products_in_wishList_details {
    private ArrayList<String> pics=new ArrayList<>();
    private Boolean product_stock;
    private String wid;

    private String product_name, product_id,product_actualprice,product_mrp,product_shopid,product_cartquantity,product_rating,product_offer;
    public void add_product(String wid,String product_name,String product_id,String product_actualprice,String product_mrp,Boolean product_stock,String product_cartquantity,String product_rating,String product_offer,ArrayList<String> pics){
        this.product_name=product_name;
        this.product_id=product_id;
        this.wid=wid;
        this.product_actualprice=product_actualprice;
        this.product_mrp=product_mrp;
        this.product_cartquantity=product_cartquantity;
        this.product_stock=product_stock;
        this.product_offer=product_offer;
        this.product_rating=product_rating;
        this.pics=pics;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
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

    public Boolean
    getProduct_stockquantity() {

        return product_stock;
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
