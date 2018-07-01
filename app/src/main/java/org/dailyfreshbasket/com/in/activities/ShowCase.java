package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.dailyfreshbasket.com.in.adapters.RecycleViewAdapterSmilerProduct;
import org.dailyfreshbasket.com.in.informations.Products_in_Search_details;
import org.dailyfreshbasket.com.in.models.product;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.dailyfreshbasket.com.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dailyfreshbasket.com.in.R;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.*;

public class ShowCase extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener, getResponce {

    private SliderLayout mDemoSlider;
    private Toolbar toolbar;
    private ArrayList<String> pics;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private ProgressDialog pd=null;
    private ImageView wish;
    private RatingBar rb;
    private boolean incart=false;
    private  boolean isRated=false;

    private List<Products_in_Search_details> product_List= Collections.emptyList();
    private TextView tv_name,tv_subname,tv_mrp,tv_offer,tv_rate,tv_quantity,tv_discount;
   private Button buy,addToCart;
   private RecyclerView rc;
   private RecycleViewAdapterSmilerProduct adapter;
    String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        setContentView(R.layout.activity_show_case);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setup();
        pics=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle details=getIntent().getBundleExtra("extra");
        try{
                 pid=details.getString("id");
                pd = ProgressDialog.show(this, "Get Product", "wait...");
                pd.setCancelable(true);
                getProduct(pid);

        }catch (Exception e){

        }

        mDemoSlider =(SliderLayout)findViewById(R.id.slidershowCase);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.pic);
        for(String key : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                   // .description(name)
                    .image(file_maps.get(key))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",key);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.removeAllSliders();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopAutoCycle();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
    private TextView menuCount;

    public void setupBadge(int add) {
        if(add==-2){
            Home.menuCounter=0;
        }else{
            Home.menuCounter+=add;
        }
        if (menuCount != null) {

            if (Home.menuCounter == 0) {
                if (menuCount.getVisibility() != View.GONE) {
                    menuCount.setVisibility(View.GONE);
                }
            } else {
                menuCount.setText(String.valueOf(Math.min(Home.menuCounter, 99)));
                if (menuCount.getVisibility() != View.VISIBLE) {
                    menuCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public void search(final String search,final String type){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            Log.d("JSON_Search resent view",response.toString());
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj,search);
                        } catch (Exception t) {
                            Log.e("JSON_Search", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("search","search");
                //params.put("data",search);
                params.put("key","SEARCH");
                params.put("searchkey",search);
                params.put("page","1");
                params.put("sort","0");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    private void parseJSONResponce(JSONObject response,String search) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Log.d("search", "parseJSONResponce: "+response);
            Boolean result=response.getBoolean("success");
            if(result){
                product_List.clear();
                if(!response.isNull("data")){
                    JSONArray Productarray=response.getJSONArray("data");
                    for(int i=0;i<Productarray.length();i++) {
                        if(i>10)
                            break;
                        Products_in_Search_details prouctDetails=new Products_in_Search_details();
                        JSONObject currentObjecr = Productarray.getJSONObject(i);
                        String product_id = currentObjecr.getString("pid");
                        if(product_id.equals(pid))
                            continue;

                        String product_name = currentObjecr.getString("name");
                        String product_mrp = currentObjecr.getString("mrp");
                        String discount=currentObjecr.getString("discount");
                        String sub=currentObjecr.getString("feature");
                        String product_actualproce = currentObjecr.getString("actualPrice");
                        String product_stockquantity = currentObjecr.getString("quqntity");
                        String product_rating="0";
                        if(!currentObjecr.isNull("rating"))
                        product_rating = currentObjecr.getString("rating");
                        String product_offer = currentObjecr.getString("offer");
                        ArrayList<String> pic=new ArrayList();
                        if(!currentObjecr.isNull("url")){
                            JSONArray product_pic=currentObjecr.getJSONArray("url");
                            for (int k = 0; k < product_pic.length(); k++) {
                                pic.add(product_pic.getString(k));
                            }
                        }
                        if(product_id!="null"){
                            prouctDetails.add_product(discount,sub,product_name
                                    ,product_id
                                    ,product_actualproce
                                    ,product_mrp
                                    ,""
                                    ,product_stockquantity
                                    ,""
                                    ,product_rating,product_offer
                                    ,pic);
                            // add row date like recent to array list
                            product_List.add(prouctDetails);
                            adapter.notifyDataSetChanged();
                        }
                        Log.d("Search Products", product_id + product_mrp + product_name +"\n");
                    }
                }else{

                }

            }
            else {

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.mycart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        menuCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(0);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.mycart:
                finish();
                Intent cart=new Intent(this,ActivityMyCart.class);
                startActivity(cart);
                break;
            case R.id.searchBar:
                finish();
                Intent search=new Intent(this,ActivitySearchBox.class);
                startActivity(search);
                break;
        }
        return false;
    }
    public void setup(){
        buy=(Button) findViewById(R.id.b_showCase_buy);
        addToCart=(Button) findViewById(R.id.b_showCase_cart);
        buy.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        wish=(ImageView)findViewById(R.id.iv_showcase_wish);
        wish.setOnClickListener(this);
        rb=(RatingBar)findViewById(R.id.rb_rationbar);
        tv_name=(TextView)findViewById(R.id.tv_productName);
        tv_subname=(TextView)findViewById(R.id.tv_productSubName);
        tv_discount=(TextView)findViewById(R.id.tv_productDiscount);
        tv_quantity=(TextView)findViewById(R.id.tv_Quatity);
        tv_rate=(TextView)findViewById(R.id.tv_productPrice);
        rc=(RecyclerView)findViewById(R.id.rc_simlerProduct);
        product_List=new ArrayList<>();
        adapter=new RecycleViewAdapterSmilerProduct(this,product_List,true);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        //tv_type=(TextView)findViewById(R.id.tvShowCase_productType);
    }
    public void setdata(product p){
        tv_name.setText(p.getPname());
        tv_subname.setText(p.getPfeature());
        rb.setRating(p.getPrating());
        if(p.getPquantity()>0)
        tv_quantity.setText("In Stock "+p.getPquantity());
        else{
            tv_quantity.setText("Out Of Stock");
            tv_quantity.setTextColor(Color.RED);
        }
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                if(!isRated){
                    pd = ProgressDialog.show(context, "", "wait...");
                    pd.setCancelable(true);
                    SendRequest req=new SendRequest(URL_RATE_PRODUCT,ShowCase.this,5);
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("pid",pid);
                    params.put("uid","");
                    params.put("rating",rating+"");
                    req.setParameters(params);
                    req.send();
                }else {
                    Toast.makeText(ShowCase.this, "already rated", Toast.LENGTH_LONG).show();
                }

            }
        });
        tv_discount.setText(String.valueOf(p.getPdiscount())+"%");
        tv_rate.setText(String.valueOf(p.getPactualprice()));

        for(String pic:p.getUrl()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    // .description(name)
                    .image(pic)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",pic);
            mDemoSlider.addSlider(textSliderView);
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public void onClick(View v) {
        String email=null;
        String uid=null;
        String guid=null;
        switch (v.getId()){
            case R.id.iv_showcase_wish:
                wish.setImageResource(R.drawable.ic_like);
                pd = ProgressDialog.show(this, "Adding Item into wishlist", "wait...");
                pd.setCancelable(true);
                uid=readPrefes(this,PREF_KEY_UID,"");
                addToWishList(uid,pid);
                break;
            case R.id.b_showCase_buy:
                    BuyNow();
                break;
            case R.id.b_showCase_cart:
                email=readPrefes(this,PREF_KEY_EMAIL,"");
                uid=readPrefes(this,PREF_KEY_UID,"");
                guid=readPrefes(this,PREF_KEY_GUESTKEY,"");
                Log.d("guod", "onClick: "+guid);
                pd = ProgressDialog.show(this, "Adding Item into cart", "wait...");
                pd.setCancelable(true);
                if(email.length()>1 && uid.length()>1){
                    addToMyCart(pid);
                }else if(guid.length()>3){
                    addToMyCart(pid);
                }
                break;
        }
    }

    private void addToWishList(String uid,String pid) {
        SendRequest req=new SendRequest(URL_ADDWISHLIST,this,4);
        Map<String,String> params = new HashMap<String, String>();
        params.put("pid",pid);
        params.put("uid",uid);
        params.put("password","");
        req.setParameters(params);
        req.send();
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
    private void addToRecentView(String uid,String tags) {
        SendRequest req=new SendRequest(URL_MYCART,this,2);
        Map<String,String> params = new HashMap<String, String>();
        params.put("uid",uid);
        params.put("tags",tags);
        params.put("key","RECENT");
        params.put("password","");
        req.setParameters(params);
        req.send();
    }
    private void addToMyCart(String pid) {
        String uid=readPrefes(this,PREF_KEY_UID,"");
        String guid=readPrefes(this,PREF_KEY_GUESTKEY,"");
        if(!incart){
            if(uid.length()>5){
                SendRequest req=new SendRequest(URL_MYCART,this,3);
                Map<String,String> params = new HashMap<String, String>();
                params.put("pid",pid);
                params.put("password","");
                params.put("uid",uid);
                params.put("key","ADD");
                req.setParameters(params);
                req.send();
            }else if(guid.length()>3){
                Log.d("sending guest request", "addToMyCart: ");
                SendRequest req=new SendRequest(URL_GUESTCART,this,3);
                Map<String,String> params = new HashMap<String, String>();
                params.put("pid",pid);
                params.put("guid",guid);
                params.put("key","ADD");
                req.setParameters(params);
                req.send();
                //Toast.makeText(this, "You must login first", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }else{
            pd.dismiss();
            Toast.makeText(this, "already in your cart", Toast.LENGTH_LONG).show();
        }
    }
    private void BuyNow() {
        Bundle details=new Bundle();
        details.putString("id",pid);
        Intent showcase=new Intent(context, PlaceNowActivity.class);
        showcase.putExtra("extra",details);
        context.startActivity(showcase);
    }
    @Override
    public void responce(String res, int requestCode) {
        pd.dismiss();
        switch(requestCode){
            case 1:
                jsonparse(res);
                break;
            case 2:
                Log.d("recent view", "responce: "+res);
                break;
            case 3:
                addToMyCartJSON(res);
                break;
            case 4:
                Log.d("wish view", "responce: "+res);
                break;
            case 5:
                Log.d("rating", "responce: "+res);
                break;
        }
    }
    private void addToMyCartJSON(String res) {
        try {
            Log.d("JSON Responce", res.toString());
            JSONObject responce = new JSONObject(res);
            if (responce.getBoolean("success")) {
                incart=true;
                setupBadge(1);
                addToCart.setText("In cart");
            }
        }catch(JSONException e){
            e.printStackTrace();
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
                     product pM=new product();
                     JSONObject product=products.getJSONObject(i);
                     int actualPrice = product.getInt("actualPrice");
                     pM.setPactualprice(actualPrice);
                     String discount = product.getString("discount");
                     if(discount!=null)
                     pM.setPdiscount(Integer.valueOf(discount));
                     String feature = product.getString("feature");
                     pM.setPfeature(feature);
                     String pid = product.getString("pid");
                     pM.setPid(pid);
                     int mrp = product.getInt("mrp");
                     String name = product.getString("name");
                     String offer = product.getString("offer");
                     String quqntity = product.getString("quqntity");
                     double rating=0;
                     if(!product.isNull("rating"))
                     rating = product.getDouble("rating");
                     int sold = product.getInt("sold");
                     String specs = product.getString("specs");
                     String tags = product.getString("tags");
                     search(tags,"");
                     String uid=readPrefes(this,PREF_KEY_UID,"");
                     addToRecentView(uid,tags);
                     String createDate = product.getString("createDate");
                     pM.setPmrp(mrp);
                     pM.setPname(name);
                     pM.setPoffer(offer);
                     pM.setPquantity(Integer.valueOf(quqntity));
                     pM.setPrating((float)rating);
                     pM.setPsold(sold);
                     pM.setPspecs(specs);
                     pM.setCreateDate(createDate);

                     ArrayList<String> pics=new ArrayList();
                     if(!product.isNull("url")){
                         JSONArray pic=product.getJSONArray("url");
                         for(int j=0;j<pic.length();j++){
                             pics.add(pic.getString(j));
                         }
                     }
                     pM.setUrl(pics);
                     setdata(pM);
                 }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}