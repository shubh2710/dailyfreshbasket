package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.RecycleViewAdapterMyWishlistProduct;
import org.dailyfreshbasket.com.in.informations.Products_in_wishList_details;
import org.dailyfreshbasket.com.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_DEFULT_PIC;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_WISHLIST;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.*;


public class ActivityMyWishList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerview;
    public RecycleViewAdapterMyWishlistProduct adaptor;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private TextView subtotal,dilavery,tax,discount,netTota,itemCount;
    private List<Products_in_wishList_details> product_List=Collections.emptyList();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vollyConnection= VollyConnection.getsInstance();
        context=this;
        pd = ProgressDialog.show(this, "Loading WishList", "wait...");
        pd.setCancelable(true);

        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        setContentView(R.layout.activity_my_wishlist);
        setTitle("WishList");
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview=(RecyclerView)findViewById(R.id.rc_Product_List_in_wishlist);
        product_List=new ArrayList<>();
        product_List.clear();
        adaptor=new RecycleViewAdapterMyWishlistProduct(this,product_List);
        recyclerview.setAdapter(adaptor);
        recyclerview.setFocusable(false);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        String uid=readPrefes(this,PREF_KEY_UID,"");
        if(uid.length()>1){
            requestShowWishList(uid,"","list");
        }else {
            Toast.makeText(this, "you must login first", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.rightDrawerOpingIcon:
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.mycart:
                Intent cart=new Intent(this,ActivityMyCart.class);
                startActivity(cart);
                finish();
                break;
            case R.id.searchBar:
                finish();
                Intent search=new Intent(this,ActivitySearchBox.class);
                startActivity(search);
                break;
        }
        return false;
    }
    public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clicklistner;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ClickListener clicklistner) {
            this.clicklistner = clicklistner;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerview.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && clicklistner != null) {
                        clicklistner.onLongClick(childView, recyclerview.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clicklistner != null && gestureDetector.onTouchEvent(e)) {
                clicklistner.onClick(childView, rv.getChildPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int postion);
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public void requestShowWishList(final String uid,final String pass,final String data){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_WISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj);
                            Log.d("WISHLIST",response);

                        } catch (Exception t) {
                            Log.e("JSON_CART", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",uid);
                params.put("password",pass);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    private void parseJSONResponce(JSONObject response) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Boolean result=response.getBoolean("success");
            if(result) {
                if (!response.isNull("data")) {
                    JSONArray Productarray=response.getJSONArray("data");
                    Log.d("PRODUCTLIST",Productarray.toString());
                    for(int i=0;i<Productarray.length();i++) {
                        JSONObject wishlist=Productarray.getJSONObject(i);
                        String wid=wishlist.getString("wid");
                        JSONArray products=wishlist.getJSONArray("productList");
                        for(int j=0;j<products.length();j++){
                            Products_in_wishList_details prouctDetails=new Products_in_wishList_details();
                            JSONObject currentObjecr = products.getJSONObject(j);
                            String product_id = currentObjecr.getString("pid");
                            String product_name = currentObjecr.getString("name");
                            String product_mrp = currentObjecr.getString("mrp");
                            String product_actualproce = currentObjecr.getString("actualPrice");
                            Boolean product_stock=false;
                            if(Integer.parseInt(currentObjecr.getString("quqntity"))>0)
                            product_stock = true;
                            String product_rating = currentObjecr.getString("rating");
                            String product_offer = currentObjecr.getString("offer");
                            ArrayList<String> pics=new ArrayList();
                            if(!currentObjecr.isNull("url")){
                                JSONArray product_pic=currentObjecr.getJSONArray("url");
                                for(int k=0;k<product_pic.length();k++){
                                    pics.add(product_pic.getString(k));
                                }
                            }else{
                                pics.add(URL_DEFULT_PIC);
                            }

                            if(product_id!="null"){
                                prouctDetails.add_product(wid,product_name,product_id,product_actualproce,product_mrp,product_stock,"",product_rating,product_offer,pics);

                                product_List.add(prouctDetails);
                                adaptor.notifyDataSetChanged();
                            }
                            Log.d("WishList PRODUCTS", product_id + product_mrp + product_name +"\n");
                        }
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
