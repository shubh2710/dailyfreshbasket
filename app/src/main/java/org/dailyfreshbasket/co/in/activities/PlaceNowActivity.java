package org.dailyfreshbasket.co.in.activities;

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
import android.widget.Button;
import android.widget.Toast;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.RecycleViewAdapterPlaceOrderList;
import org.dailyfreshbasket.co.in.adapters.RecyclerTouchListner;
import org.dailyfreshbasket.co.in.models.PlaceOrderListModel;
import org.dailyfreshbasket.co.in.models.PlaceOrderModel;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_DEFULT_PIC;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_GET_PRODUCT;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class PlaceNowActivity extends AppCompatActivity implements getResponce {
    private ProgressDialog pd;
    public RecycleViewAdapterPlaceOrderList adaptor;
    private RecyclerView recyclerview;
    private String pid;
    private Button place;
    private Toolbar toolbar;
    private List<PlaceOrderModel> product_List= Collections.emptyList();
    ArrayList<PlaceOrderListModel> orderProductList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_now);
        product_List = new ArrayList<>();
        product_List.clear();
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("Place Order");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle details=getIntent().getBundleExtra("extra");
        try{
            pid=details.getString("id");
            pd = ProgressDialog.show(this, "Creating List", "wait...");
            pd.setCancelable(true);
                getProduct(pid);
        }catch (Exception e){
        }
        orderProductList=new ArrayList();
        place=(Button) findViewById(R.id.b_placeOrder);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=readPrefes(PlaceNowActivity.this,PREF_KEY_UID,"");
                if(uid.length()>5){
                    int price=0;
                    orderProductList.clear();
                    for(PlaceOrderModel p:product_List){
                        orderProductList.add(new PlaceOrderListModel(p.getPid(),p.getQuatity(),Integer.parseInt(p.getPrice())));
                        price=p.getQuatity()*Integer.parseInt(p.getPrice());
                    }
                    if(price>=100){
                        Intent confirm=new Intent(getBaseContext(), ConfirmOrderActivity.class);
                        Bundle details=new Bundle();
                        details.putInt("total",price);
                        details.putBoolean("fromCart",false);
                        details.putParcelableArrayList("list",orderProductList);
                        confirm.putExtra("extra",details);
                        startActivity(confirm);
                    }else{
                        Toast.makeText(PlaceNowActivity.this,"can't check out less then 100 Rs order",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Intent confirm=new Intent(getBaseContext(), SignInOrLoginTabs.class);
                    startActivity(confirm);
                }

            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.rc_place_product_list);
        adaptor = new RecycleViewAdapterPlaceOrderList(this, product_List);
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
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    private void getProduct(String pid) {
        SendRequest req=new SendRequest(URL_GET_PRODUCT,this,1);
        Map<String,String> params = new HashMap<String, String>();
        params.put("id",pid);
        params.put("key","id");
        params.put("uid","");
        params.put("password","");
        req.setParameters(params);
        req.send();
    }
    @Override
    public void responce(String res, int requestCode) {
        pd.dismiss();
        switch (requestCode){
            case 1:
                jsonparse(res);
                break;
            case 2:

                break;
        }

    }
    private void jsonparse(String res) {
        try {
            JSONObject responce=new JSONObject(res);
            Log.d("JSON Responce",responce.toString());
            if(responce.getBoolean("success")){
                pd.dismiss();
                if(!responce.isNull("data")){
                    JSONArray products = responce.getJSONArray("data");
                    for (int i=0;i<products.length();i++){
                        JSONObject product=products.getJSONObject(i);
                        int actualPrice = product.getInt("actualPrice");
                        String discount = product.getString("discount");

                        String feature = product.getString("feature");
                        String pid = product.getString("pid");
                        int mrp = product.getInt("mrp");

                        String name = product.getString("name");
                        String offer = product.getString("offer");
                        String quqntity = product.getString("quqntity");
                        int rating=0;
                        if(!product.isNull("rating"))
                            rating = product.getInt("rating");
                        int sold = product.getInt("sold");
                        String specs = product.getString("specs");
                        String tags = product.getString("tags");

                        String createDate = product.getString("createDate");
                        ArrayList<String> pics=new ArrayList();
                        if(!product.isNull("url")){
                            JSONArray pic=product.getJSONArray("url");
                            for(int j=0;j<pic.length();j++){
                                pics.add(pic.getString(i));
                            }
                        }
                        PlaceOrderModel pM=null;
                        if(pics.size()>0){
                            pM=new PlaceOrderModel(pid,name,1,actualPrice+"",pics.get(0));
                        }else{
                             pM=new PlaceOrderModel(pid,name,1,actualPrice+"",URL_DEFULT_PIC);
                        }
                        if(pM!=null)
                        product_List.add(pM);
                        adaptor.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
