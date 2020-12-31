package org.dailyfreshbasket.co.in.models;

/**
 * Created by shubham on 5/2/2018.
 */

public class MyOrderModel {
    private String oid;
    private String uid;
    private String order_number;
    private int status;
    private String create_date;
    private String aid;
    private String bid;
    private int amount;
    private String delivery_date;
    private String delivery_shift;
    private int amount_status;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getStatus() {
        switch (status){
            case 0:
                return "Cancelled";
            case 1:
                return "Placed successfully";
            case 2:
                return "dispatched";
            case 3:
                return "Shipped";
            case 4:
                return "Waiting";
            case 5:
                return "Delivered";
        }
        return "unavilable";
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_shift() {
        switch (Integer.parseInt(delivery_shift)){
            case 0:
               return "First (9am - 12pm)";
            case 1:
                return "Second (2pm - 5pm)";
        }
        return "";
    }

    public void setDelivery_shift(String delivery_shift) {
        this.delivery_shift = delivery_shift;
    }

    public int getAmount_status() {
        return amount_status;
    }

    public void setAmount_status(int amount_status) {
        this.amount_status = amount_status;
    }
}
