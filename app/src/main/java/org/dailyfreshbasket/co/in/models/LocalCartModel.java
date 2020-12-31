package org.dailyfreshbasket.co.in.models;

/**
 * Created by shubham on 5/17/2018.
 */

public class LocalCartModel {
    String pid;
    int quantity;
    int id;

    public LocalCartModel(String pid, int quantity, int id) {
        this.pid = pid;
        this.quantity = quantity;
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
