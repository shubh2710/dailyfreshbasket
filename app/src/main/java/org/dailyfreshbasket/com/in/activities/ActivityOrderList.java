package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.RecycleViewAdapterMyOrderList;
import org.dailyfreshbasket.com.in.adapters.RecyclerTouchListner;
import org.dailyfreshbasket.com.in.models.MyOrderModel;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_GETORDER;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class ActivityOrderList extends AppCompatActivity implements getResponce {

    private Toolbar toolbar;
    private RecyclerView recyclerview;
    private ArrayList<MyOrderModel> list;
    private RecycleViewAdapterMyOrderList adaptor;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("My Orders");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview=(RecyclerView)findViewById(R.id.rc_orderList);
        list=new ArrayList<>();
        adaptor=new RecycleViewAdapterMyOrderList(this,list);
        recyclerview.setAdapter(adaptor);
        recyclerview.setFocusable(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adaptor);
        recyclerview.setFocusable(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new RecyclerTouchListner.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        pd = ProgressDialog.show(this, "Loading Orders", "wait...");
        pd.setCancelable(true);
        getOrderList();
    }

    private void getOrderList() {
        SendRequest req=new SendRequest(URL_GETORDER,this,3);
        String uid=readPrefes(this,PREF_KEY_UID,"");
        if(uid.length()>1){
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            params.put("key","GET");
            req.setParameters(params);
            req.send();
        }else {
            Toast.makeText(this, "you must login first", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult: "+requestCode);
            if(requestCode==1){
                pd = ProgressDialog.show(this, "Loading Orders", "wait...");
                pd.setCancelable(true);
                getOrderList();
            }
    }

    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
    @Override
    public void responce(String res, int requestCode) {
        Log.d("OrderList: ",res);
        pd.dismiss();
        if(res !=null && res.length() >3){
                jsonParse(res);
        }
    }

    private void jsonParse(String res) {
        try {
            JSONObject responce =new JSONObject(res);
            if(!responce.isNull("data")){
                this.list.clear();
                if(responce.getBoolean("success")){
                    JSONArray list=responce.getJSONArray("data");
                    for(int i=0;i<list.length();i++){
                        JSONObject order=list.getJSONObject(i);
                        MyOrderModel o=new MyOrderModel();
                        o.setAid(order.getString("aid"));
                        o.setAmount(order.getInt("amount"));
                        o.setAmount_status(order.getInt("amount_status"));
                        o.setBid(order.getString("bid"));
                        o.setCreate_date(order.getString("create_date"));
                        o.setDelivery_date(order.getString("delivery_date"));
                        o.setDelivery_shift(order.getString("delivery_shift"));
                        o.setOid(order.getString("oid"));
                        o.setOrder_number(order.getString("order_number"));
                        o.setStatus(order.getInt("status"));
                        o.setUid(order.getString("uid"));
                        this.list.add(o);
                        adaptor.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
