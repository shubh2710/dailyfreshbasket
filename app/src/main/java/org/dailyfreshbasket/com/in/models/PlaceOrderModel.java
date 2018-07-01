package org.dailyfreshbasket.com.in.models;

/**
 * Created by shubham on 4/28/2018.
 */

public class PlaceOrderModel {
    String name;
    int quatity;
    String price;
    String image;
    String pid;

    public PlaceOrderModel(String pid, String name, int quatity, String price, String image) {
        this.name = name;
        this.pid=pid;
        this.quatity = quatity;
        this.price = price;
        this.image=image;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
