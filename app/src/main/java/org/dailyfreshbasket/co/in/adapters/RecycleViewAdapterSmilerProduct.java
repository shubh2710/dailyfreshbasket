package org.dailyfreshbasket.co.in.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.Home;
import org.dailyfreshbasket.co.in.activities.PlaceNowActivity;
import org.dailyfreshbasket.co.in.activities.ShowCase;
import org.dailyfreshbasket.co.in.informations.Products_in_Search_details;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_DEFULT_PIC;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_GUESTCART;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_MYCART;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_GUESTKEY;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;


/**
 * Created by shubham on 2/19/2017.
 */
public class RecycleViewAdapterSmilerProduct extends RecyclerView.Adapter<RecycleViewAdapterSmilerProduct.MyViewHolder> implements getResponce {

    private List<Products_in_Search_details> data= Collections.emptyList();
    private LayoutInflater inflater;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ProgressDialog pd;
    private ShowCase activity;
    private boolean whichLayout=false;
    public RecycleViewAdapterSmilerProduct(ShowCase activity, List<Products_in_Search_details> data, boolean whichLayout){
        inflater= LayoutInflater.from(activity);
        this.data=data;
        this.activity=activity;
        this.whichLayout=whichLayout;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        Log.d("NOTIFIE","constructor called");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(whichLayout)
            view=inflater.inflate(R.layout.row_search_view_list_layout,parent,false);
        else
            view=inflater.inflate(R.layout.row_search_view,parent,false);
        Log.d("NOTIFIE","onCreateView called");
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final Products_in_Search_details currentdata=data.get(position);
        final String id=currentdata.getProduct_id();
        Log.d("NOTIFIE","onbindViewholder called");
        holder.tv_name.setText(currentdata.getProduct_name());
        holder.tv_rate.setText("Rs "+currentdata.getProduct_actualprice());
        holder.tv_subname.setText(currentdata.getFeatures());
holder.offerTv.setText(currentdata.getProduct_offer()+"% Off");
        int stockQuantity=Integer.parseInt(currentdata.getProduct_stockquantity());
        if(stockQuantity>1){
            holder.stock.setVisibility(View.GONE);
            holder.avaiable.setTextColor(Color.BLACK);
            holder.avaiable.setVisibility(View.VISIBLE);
            holder.avaiable.setText("Available: "+stockQuantity);
        }else{
            holder.stock.setVisibility(View.VISIBLE);
            holder.avaiable.setVisibility(View.INVISIBLE);
        }

        holder.tv_dicount.setText(currentdata.getDiscount()+"% Off");
        holder.tv_mrp.setText("Rs "+currentdata.getProduct_mrp());
        if(currentdata.getProduct_rating()!=null)
        holder.rb.setRating(Float.parseFloat(currentdata.getProduct_rating()));
        else
            holder.rb.setRating(0);
        if(currentdata.getPics()!=null && currentdata.getPics().size()>0){

            Picasso.with(activity)
                    .load(currentdata.getPics().get(0))
                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);
        }else{
            Picasso.with(activity)
                    .load(URL_DEFULT_PIC)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Front_page_catagory_details current=singleRowData.get(pos);
                Bundle details=new Bundle();
                details.putString("id",currentdata.getProduct_id());
                Intent showcase=new Intent(activity, ShowCase.class);
                showcase.putExtra("extra",details);
                activity.startActivity(showcase);
            }
        });
        if(currentdata.isAdded()){
            holder.addCart.setVisibility(View.GONE);
            holder.ll.setVisibility(View.VISIBLE);
        }else{
            holder.addCart.setVisibility(View.VISIBLE);
            holder.ll.setVisibility(View.GONE);
        }
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.quantity.setText(++currentdata.quantity+"");
                updateCartQuantity(currentdata.quantity+"",currentdata.getProduct_id());

            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.quantity.setText(--currentdata.quantity+"");
                updateCartQuantity(currentdata.quantity+"",currentdata.getProduct_id());
            }
        });
        holder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle details=new Bundle();
                details.putString("id",currentdata.getProduct_id());
                Intent showcase=new Intent(activity, PlaceNowActivity.class);
                showcase.putExtra("extra",details);
                activity.startActivity(showcase);
            }
        });
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.addCart.setVisibility(View.GONE);
                    holder.ll.setVisibility(View.VISIBLE);
                    currentdata.quantity++;
                    pd = ProgressDialog.show(activity, "Adding Item into cart", "wait...");
                    pd.setCancelable(true);
                    addToMyCart(currentdata.getProduct_id());
                    //requestAddToCart(uid,pid,email,"1","add");

            }
        });
    }
    private void addToMyCart(String id_product) {
        String uid=readPrefes(activity,PREF_KEY_UID,"");
        String guid=readPrefes(activity,PREF_KEY_GUESTKEY,"");
        if(uid.length()>3){
            SendRequest req=new SendRequest(URL_MYCART,this,3);
            Map<String,String> params = new HashMap<String, String>();
            params.put("pid",id_product);
            params.put("password","");
            params.put("uid",uid);
            params.put("key","ADD");
            req.setParameters(params);
            req.send();
        }else if(guid.length()>3){
            Log.d("sending guest request", "addToMyCart: ");
            SendRequest req=new SendRequest(URL_GUESTCART,this,3);
            Map<String,String> params = new HashMap<String, String>();
            params.put("pid",id_product);
            params.put("guid",guid);
            params.put("key","ADD");
            req.setParameters(params);
            req.send();
        }
    }
    public void updateCartQuantity(String q,String pid){
        try{
            pd= ProgressDialog.show(activity, "Updating item", "wait...");
            int quantity=Integer.parseInt(q);
            String guid=readPrefes(activity,PREF_KEY_GUESTKEY,"");
            String uid=readPrefes(activity,PREF_KEY_UID,"");
            if(guid.length()>5){
                SendRequest req=new SendRequest(URL_GUESTCART,this,4);
                Map<String,String> params = new HashMap<String, String>();
                params.put("pid",pid);
                params.put("quantity",quantity+"");
                params.put("password","");
                params.put("guid",guid);
                params.put("key","UID_CART");
                req.setParameters(params);
                req.send();
            }else{
                SendRequest req=new SendRequest(URL_MYCART,this,4);
                Map<String,String> params = new HashMap<String, String>();
                params.put("pid",pid);
                params.put("quantity",quantity+"");
                params.put("password","");
                params.put("uid",uid);
                params.put("key","UID_CART");
                req.setParameters(params);
                req.send();
            }
        }catch (Exception e){
        }
    }
    @Override
    public void responce(String res, int requestCode) {
        switch (requestCode){
            case 4:
                try {
                    Log.d("JSON Responce", res.toString());
                    JSONObject responce = new JSONObject(res);
                    if (responce.getBoolean("success")) {
                        pd.dismiss();
                        Toast.makeText(activity,"product added in your cart",Toast.LENGTH_LONG).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    Log.d("JSON Responce", res.toString());
                    JSONObject responce = new JSONObject(res);
                    if (responce.getBoolean("success")) {
                        setupBadge(1);
                        pd.dismiss();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                break;

        }
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
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,avaiable,tv_subname,tv_rate,tv_dicount,tv_mrp,quantity,offerTv;
       private RatingBar rb;
       private LinearLayout ll;
       private int quanitity=0;
       private Button minus,plus,buynow,addCart;
        private ImageView image;
        private LinearLayout stock;
        public MyViewHolder(View itemView) {
           super(itemView);
            setup(itemView);
           Log.d("NOTIFIE","MyviewHoldder called");
       }
        public void setup(View itemView){
            avaiable=(TextView) itemView.findViewById(R.id.search_product_aviable);
            stock=(LinearLayout)itemView.findViewById(R.id.productViewStock);
            tv_name=(TextView)itemView.findViewById(R.id.tv_product_in_search_name);
            tv_rate=(TextView)itemView.findViewById(R.id.tv_product_in_search_rate);
            tv_dicount=(TextView)itemView.findViewById(R.id.tv_product_in_search_discount);
            tv_subname=(TextView)itemView.findViewById(R.id.tv_product_in_search_subname);
            tv_mrp=(TextView)itemView.findViewById(R.id.tv_product_in_search_mrp);
            tv_mrp.setPaintFlags(tv_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            image=(ImageView)itemView.findViewById(R.id.iv_product_image_in_search_layout);
            rb=(RatingBar)itemView.findViewById(R.id.ratingBar);
            minus=(Button)itemView.findViewById(R.id.b_search_product_minus);
            plus=(Button)itemView.findViewById(R.id.b_search_product_plus);
            buynow=(Button)itemView.findViewById(R.id.b_search_product_buynow);
            addCart=(Button)itemView.findViewById(R.id.b_search_product_addTocart);
            ll=(LinearLayout)itemView.findViewById(R.id.ll_quanitytLayout);
            quantity=(TextView)itemView.findViewById(R.id.tv_search_product_quanitiy);
            offerTv = itemView.findViewById(R.id.offer_tv);
        }
   }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
}
