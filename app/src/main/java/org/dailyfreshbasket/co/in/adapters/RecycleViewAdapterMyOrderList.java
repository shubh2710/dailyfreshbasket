package org.dailyfreshbasket.co.in.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.ActivityOrderList;
import org.dailyfreshbasket.co.in.activities.CancelOrderActivity;
import org.dailyfreshbasket.co.in.activities.OrderDetailActivity;
import org.dailyfreshbasket.co.in.models.MyOrderModel;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_UPDATEORDER;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

/**
 * Created by shubham on 5/2/2018.
 */

public class RecycleViewAdapterMyOrderList  extends RecyclerView.Adapter<RecycleViewAdapterMyOrderList.MyViewHolder> implements getResponce {
    private LayoutInflater inflater;
    private ActivityOrderList activity;
    private ArrayList<MyOrderModel> list;
    public RecycleViewAdapterMyOrderList(ActivityOrderList activityOrderList, ArrayList<MyOrderModel> list) {
        inflater= LayoutInflater.from(activityOrderList);
        this.list=list;
        this.activity=activityOrderList;
    }
    @Override
    public RecycleViewAdapterMyOrderList.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view=inflater.inflate(R.layout.row_order_list,parent,false);
        RecycleViewAdapterMyOrderList.MyViewHolder holder=new RecycleViewAdapterMyOrderList.MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecycleViewAdapterMyOrderList.MyViewHolder holder,final int i) {
        final MyOrderModel currentdata =list.get(i);
        Log.d("order cancel", "onBindViewHolder: "+currentdata.getStatus());
        if(currentdata!=null){
            if(currentdata.getStatus().equals("Cancelled")){
                holder.tv_status.setTextColor(Color.RED);
                holder.cancel.setEnabled(false);
            }else{
                holder.tv_status.setTextColor(Color.GREEN);
                holder.cancel.setEnabled(true);
            }
            holder.tv_status.setText("Status: "+currentdata.getStatus()+"");
            holder.tv_shift.setText("Shift: "+currentdata.getDelivery_shift()+"");
            holder.tv_date.setText(currentdata.getCreate_date());
            holder.tv_orderId.setText("Order Id: "+currentdata.getOrder_number());
            holder.tv_name.setText("Bill Id: "+currentdata.getBid());
            holder.tv_rs.setText("Rs "+currentdata.getAmount());
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentdata.getStatus().equals("Placed successfully")){
                        Toast.makeText(activity,"You Can't cancel this Order",Toast.LENGTH_LONG).show();
                    }else if(currentdata.getStatus().equals("Cancelled")){
                        Toast.makeText(activity,"Already Cancelled",Toast.LENGTH_LONG).show();
                    }else{
                        Intent cancel=new Intent(activity , CancelOrderActivity.class);
                        Bundle details=new Bundle();
                        details.putString("oid",currentdata.getOid());
                        details.putString("o_no",currentdata.getOrder_number());
                        cancel.putExtra("extra",details);
                        activity.startActivityForResult(cancel,1);
                    }

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent order=new Intent(activity, OrderDetailActivity.class);
                        order.putExtra("oid",currentdata.getOid());
                        activity.startActivity(order);
                }
            });
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new android.support.v7.app.AlertDialog.Builder(activity)
                            .setTitle("Daily Fresh Basket")
                            .setMessage("Do you really want to delete order?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                     if(currentdata.getStatus().equals("Cancelled") || currentdata.getStatus().equals("Delivered")){
                                            delOrder(currentdata.getOid());
                                            notifyItemRemoved(i);
                                            notifyItemRangeChanged(i,list.size());
                                            list.remove(i);
                                     }else{
                                         Toast.makeText(activity,"you can not delete this order",Toast.LENGTH_LONG).show();
                                     }
                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delOrder(String oid){
        try{
            String uid=readPrefes(activity,PREF_KEY_UID,"");
            SendRequest req=new SendRequest(URL_UPDATEORDER,this,4);
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            params.put("oid",oid);
            params.put("key","DELETE");
            req.setParameters(params);
            req.send();
        }catch (Exception e){

        }
    }


    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_orderId,tv_date,tv_name,tv_shift,tv_rs,tv_status;
        private ImageView icon;
        private Button cancel;
        private ImageView del;
        public MyViewHolder(View itemView) {
            super(itemView);
            setup(itemView);
            Log.d("NOTIFIE","MyviewHoldder called");
        }
        public void setup(View itemView){
            del=(ImageView)itemView.findViewById(R.id.b_orderList_delete);
            cancel=(Button)itemView.findViewById(R.id.b_orderList_cancel);
            tv_name=(TextView)itemView.findViewById(R.id.tv_orderList_name);
            tv_orderId=(TextView)itemView.findViewById(R.id.tv_orderList_id);
            tv_date=(TextView)itemView.findViewById(R.id.tv_orderList_date);
            tv_shift=(TextView)itemView.findViewById(R.id.tv_orderList_shift);
            tv_rs=(TextView) itemView.findViewById(R.id.tv_orderList_rs);
            tv_status = (TextView) itemView.findViewById(R.id.tv_orderList_status);
        }
    }
    @Override
    public void responce(String res, int requestCode) {
        Log.d("dele order", "responce: "+res);
    }
}
