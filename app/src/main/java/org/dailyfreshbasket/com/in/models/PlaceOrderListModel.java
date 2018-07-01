package org.dailyfreshbasket.com.in.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubham on 4/30/2018.
 */

public class PlaceOrderListModel implements Parcelable {
   private String pid;
   private int quantity;
   private int rs;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PlaceOrderListModel(String pid, int quantity,int rs) {
        this.pid=pid ;
        this.quantity=quantity;
        this.rs=rs;
    }
    public static final Creator<PlaceOrderListModel> CREATOR = new Creator<PlaceOrderListModel>() {
        @Override
        public PlaceOrderListModel createFromParcel(Parcel in) {
            String pid = in.readString();
            int quantity = in.readInt();
            int rs=in.readInt();
            return new PlaceOrderListModel(pid,quantity,rs);
        }
        @Override
        public PlaceOrderListModel[] newArray(int size) {
            return new PlaceOrderListModel[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.pid);
        parcel.writeInt(this.quantity);
        parcel.writeInt(this.rs);
    }
}
