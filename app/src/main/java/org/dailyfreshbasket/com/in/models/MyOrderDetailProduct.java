package org.dailyfreshbasket.com.in.models;

/**
 * Created by shubham on 5/5/2018.
 */

public class MyOrderDetailProduct {
    String pid;
    String opl;
    int quanity;
    int amount;
    int discount;
    String name;
    String price;
    String url;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOpl() {
        return opl;
    }

    public void setOpl(String opl) {
        this.opl = opl;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MyOrderDetailProduct{" +
                "pid='" + pid + '\'' +
                ", opl='" + opl + '\'' +
                ", quanity=" + quanity +
                ", amount=" + amount +
                ", discount=" + discount +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
