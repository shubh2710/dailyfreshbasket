package org.dailyfreshbasket.co.in.activities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.RecycleViewAdapterMyCartProductList;
import org.dailyfreshbasket.co.in.informations.Products_in_cart_details;
import org.dailyfreshbasket.co.in.models.PlaceOrderListModel;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_GUESTCART;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_MYCART;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.*;


public class ActivityMyCart extends AppCompatActivity implements getResponce {

    private Toolbar toolbar;
    private RecyclerView recyclerview;
    private CardView detailView;
    public RecycleViewAdapterMyCartProductList adaptor;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private Button checkOut;
    private int Nettotal=0;
    private TextView subtotal,dilavery,tax,discount,netTota,itemCount;
    private List<Products_in_cart_details> product_List=Collections.emptyList();
    private ProgressDialog pd;
    private ArrayList<PlaceOrderListModel> orderProductList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vollyConnection= VollyConnection.getsInstance();
        context=this;
        pd = ProgressDialog.show(this, "Loading Cart", "wait...");
        pd.setCancelable(true);
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        setContentView(R.layout.activity_my_cart);
        setup();
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("My Cart");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview=(RecyclerView)findViewById(R.id.rc_Product_List_in_cart);
        product_List=new ArrayList<>();
        orderProductList=new ArrayList();
        product_List.clear();
        checkOut=(Button) findViewById(R.id.b_checkout);

        final String uid=readPrefes(this,PREF_KEY_UID,"");
        String guid=readPrefes(this,PREF_KEY_GUESTKEY,"");
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=false;
                orderProductList.clear();
                for(Products_in_cart_details p:product_List){
                    if(Integer.parseInt(p.getProduct_cartquantity())<=Integer.parseInt(p.getProduct_stockquantity()) && Integer.parseInt(p.getProduct_cartquantity())>0){
                        orderProductList.add(new PlaceOrderListModel(p.getProduct_id(),Integer.parseInt(p.getProduct_cartquantity()),Integer.parseInt(p.getProduct_actualprice())));
                        flag=true;
                    }else{
                        Toast.makeText(ActivityMyCart.this,"Reduse product quantity or delete product",Toast.LENGTH_LONG).show();
                        flag=false;
                        break;
                    }
                }
                if(uid.length()>5  && flag){
                    if(Nettotal>=100){
                        Intent confirm=new Intent(context, ConfirmOrderActivity.class);
                        Bundle details=new Bundle();
                        details.putInt("total",Nettotal);
                        details.putBoolean("fromCart",true);
                        details.putParcelableArrayList("list",orderProductList);
                        confirm.putExtra("extra",details);
                        context.startActivity(confirm);
                    }else{
                        Toast.makeText(ActivityMyCart.this,"can't check out less then 100 Rs order",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent confirm=new Intent(getBaseContext(), SignInOrLoginTabs.class);
                    if(flag)
                    startActivity(confirm);
                }

            }
        });
        adaptor=new RecycleViewAdapterMyCartProductList(this,product_List);
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
        if(uid.length()>1){
            requestShowCart(uid,"","list",false);
        }else if(guid.length()>2){
            requestShowCart(guid,"","list",true);
        }
    }
    //private TextView menuCount;
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem menuItem = menu.findItem(R.id.mycart);
       /* View actionView = MenuItemCompat.getActionView(menuItem);
        menuCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(0);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });*/
        return true;
    }
    public void setupBadge(int add) {
        if(add==-2){
            Home.menuCounter=0;
        }else{
            Home.menuCounter+=add;
        }
        if (Home.menuCount != null) {

            if (Home.menuCounter == 0) {
                if (Home.menuCount.getVisibility() != View.GONE) {
                    Home.menuCount.setVisibility(View.GONE);
                }
            } else {
                Home.menuCount.setText(String.valueOf(Math.min(Home.menuCounter, 99)));
                if (Home.menuCount.getVisibility() != View.VISIBLE) {
                    Home.menuCount.setVisibility(View.VISIBLE);
                }
            }
        }
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
        private ActivityMyCart.ClickListener clicklistner;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ActivityMyCart.ClickListener clicklistner) {
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
    } public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int postion);
    }

    public void requestShowCart(final String uid,final String pid,final String email,boolean isguest){
        if(isguest){
            SendRequest req=new SendRequest(URL_GUESTCART,this,3);
            Map<String,String> params = new HashMap<String, String>();
            params.put("guid",uid);
            params.put("password","");
            params.put("key","GET");
            req.setParameters(params);
            req.send();
        }else{
            SendRequest req=new SendRequest(URL_MYCART,this,3);
            Map<String,String> params = new HashMap<String, String>();
            params.put("uid",uid);
            params.put("password","");
            params.put("key","GET");
            req.setParameters(params);
            req.send();
        }
    }

    @Override
    public void responce(String res, int requestCode) {
        pd.dismiss();
        Log.d("mycart", "responce: "+res);
        try {
            parseJSONResponce(new JSONObject(res));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseJSONResponce(JSONObject response) {

        if(response==null || response.length()==0){
            return;
        }
        try {
            Boolean result = response.getBoolean("success");
            if (result) {
                setupBadge(-2);
                if(!response.isNull("data")){
                    JSONArray cartArray = response.getJSONArray("data");
                    for (int i = 0; i < cartArray.length(); i++) {
                        setupBadge(1);
                        JSONObject item = cartArray.getJSONObject(i);
                        String cid=item.getString("cid");
                        if (!item.isNull("productList")) {
                            JSONArray Productarray = item.getJSONArray("productList");
                            for (int j = 0; j < Productarray.length(); j++) {
                                Products_in_cart_details prouctDetails = new Products_in_cart_details();
                                JSONObject currentObjecr = Productarray.getJSONObject(j);
                                String product_id = currentObjecr.getString("pid");
                                String product_name = currentObjecr.getString("name");
                                String product_mrp = currentObjecr.getString("mrp");
                                String product_actualproce = currentObjecr.getString("actualPrice");
                                String product_stockquantity = currentObjecr.getString("quqntity");
                                String product_cartquantity = item.getString("quantity");
                                String product_rating = currentObjecr.getString("rating");
                                String product_offer = currentObjecr.getString("offer");
                                ArrayList<String> pics = new ArrayList<>();
                                if(!currentObjecr.isNull("url")) {
                                    JSONArray product_pic = currentObjecr.getJSONArray("url");
                                    for (int k = 0; k < product_pic.length(); k++) {
                                        pics.add(product_pic.getString(k));
                                    }
                                }
                                Log.d("mycart list", "responce: "+product_id);
                                prouctDetails.add_product(cid,product_name, product_id, product_actualproce, product_mrp, "", product_stockquantity, product_cartquantity, product_rating, product_offer, pics);
                                // add row date like recent to array list
                                product_List.add(prouctDetails);
                                setdataUpdate();
                                adaptor.notifyDataSetChanged();
                            }

                        }

                    }

                }
                if(product_List.size()<1){
                    Toast.makeText(this,"no product in your cart",Toast.LENGTH_LONG).show();
                    detailView.setVisibility(View.GONE);
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void setup(){
        detailView=(CardView) findViewById(R.id.ll_cartViewDetails);
        subtotal=(TextView)findViewById(R.id.tv_productIncart_subTotal);
        tax=(TextView)findViewById(R.id.tv_productIncart_tax);
        discount=(TextView)findViewById(R.id.tv_productIncart_discount);
        netTota=(TextView)findViewById(R.id.tv_productIncart_netTotal);
        itemCount=(TextView)findViewById(R.id.tv_productIncart_itemCount);
        netTota.setText("Rs "+0);
        subtotal.setText("Rs "+0);
        //dilavery.setText(10+"rs");
        tax.setText("Rs "+0);
        discount.setText(0+"% Off");
    }
    public void setdataUpdate(){
        int discoun=0;
        int tex=0;
        int total=0;
        int quntity=0;
        int count=0;
        int dileverycharge=10;
        int extracharge=0;
        Nettotal=0;
        for(Products_in_cart_details product:product_List){
            int price= Integer.parseInt(product.getProduct_actualprice());
            quntity=Integer.parseInt(product.getProduct_cartquantity());
            count++;
            total+=price;
            int amt=(price*quntity);
            discoun += Integer.parseInt(product.getProduct_offer());
            Log.d("calculate", "setdataUpdate: mrp" +price +" quantity"+quntity+" totl "+amt);
            Nettotal+=amt;
        }
        subtotal.setText("Rs "+Nettotal);
        tax.setText("Rs 0");
        itemCount.setText("("+count+" items)");
        //dilavery.setText(dileverycharge+" rs");
        netTota.setText("Rs "+Nettotal);
        discount.setText(String.valueOf(discoun/count)+"% Off");
    }
}