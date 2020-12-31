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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.Home;
import org.dailyfreshbasket.co.in.activities.PlaceNowActivity;
import org.dailyfreshbasket.co.in.activities.ShowCase;
import org.dailyfreshbasket.co.in.informations.Front_page_catagory_details;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_GUESTCART;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_MYCART;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_GUESTKEY;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

/**
 * Created by shubham on 2/19/2017.
 */

public class HorizontalRecycleViewAdapterForFrontProducts extends RecyclerView.Adapter<HorizontalRecycleViewAdapterForFrontProducts.MyViewHolder> implements getResponce {

    private Context context;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ProgressDialog pd=null;
    private List<Front_page_catagory_details> RowProducts=new ArrayList<>();
    private LayoutInflater inflater;

    public HorizontalRecycleViewAdapterForFrontProducts(Context context){
        inflater= LayoutInflater.from(context);
        this.context=context;

        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
    }
    public void setProductsrow(List<Front_page_catagory_details> list){
        RowProducts=list;
        Log.d("DaTaInAdptor",list.toString());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.row_front_home_products,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final Front_page_catagory_details current=RowProducts.get(position);
        if(RowProducts.get(position).getPic_product().get(0)!=null)
        Picasso.with(context)
                .load(RowProducts.get(position).getPic_product().get(0))
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
        if(RowProducts.get(position).getAvailable()>1){
            holder.stock.setVisibility(View.GONE);
            holder.avaiable.setTextColor(Color.BLACK);
            holder.avaiable.setVisibility(View.VISIBLE);
            holder.avaiable.setText("Available: "+RowProducts.get(position).getAvailable());
        }else{
            holder.stock.setVisibility(View.VISIBLE);
            holder.avaiable.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(RowProducts.get(position).getName_product());
        holder.mrp.setText("Rs "+RowProducts.get(position).getMrp_product());
        int price ,mrpRate,discount=0;
        price=Integer.parseInt(RowProducts.get(position).getRate_product());
        mrpRate=Integer.parseInt(RowProducts.get(position).getMrp_product());
        try{
            discount=((mrpRate-price)*100)/mrpRate;
        }catch(Exception e){
            discount=0;
        }
        if(discount<1){
            holder.mrp.setVisibility(View.GONE);
            holder.dis.setVisibility(View.GONE);
        }else{
            holder.mrp.setVisibility(View.VISIBLE);
            holder.dis.setVisibility(View.VISIBLE);
        }
        holder.discount.setText(discount+"% Off");
        holder.rate.setText("Rs "+RowProducts.get(position).getRate_product());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Front_page_catagory_details current=singleRowData.get(pos);
                Bundle details=new Bundle();
                details.putString("id",RowProducts.get(position).getId_product());
                Intent showcase=new Intent(context, ShowCase.class);
                showcase.putExtra("extra",details);
                context.startActivity(showcase);
            }
        });
        if(current.isAdded()){
            holder.cart.setVisibility(View.GONE);
            holder.quantityLayout.setVisibility(View.VISIBLE);
        }else{
            holder.cart.setVisibility(View.VISIBLE);
            holder.quantityLayout.setVisibility(View.GONE);
        }
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.quantity<99 && current.getAvailable()>0 && current.getAvailable()>current.quantity){
                    holder.quanitity.setText(++current.quantity+"");
                    updateCartQuantity(current.quantity+"",RowProducts.get(position).getId_product());
                }else{
                    Toast.makeText(context,"out of stock only limted available",Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.quantity>1){
                    holder.quanitity.setText(--current.quantity+"");
                    updateCartQuantity(current.quantity+"",RowProducts.get(position).getId_product());
                    }
                }
        });
        holder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle details=new Bundle();
                details.putString("id",RowProducts.get(position).getId_product());
                Intent showcase=new Intent(context, PlaceNowActivity.class);
                showcase.putExtra("extra",details);
                context.startActivity(showcase);
            }
        });
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getAvailable()>0){
                    holder.cart.setVisibility(View.GONE);
                    holder.quantityLayout.setVisibility(View.VISIBLE);
                    current.setAdded(true);
                    current.quantity++;
                    pd = ProgressDialog.show(context, "Adding Item into cart", "wait...");
                    pd.setCancelable(false);
                    addToMyCart(RowProducts.get(position).getId_product());
                }else
                    Toast.makeText(context,"out of stock",Toast.LENGTH_SHORT).show();

                    //requestAddToCart(uid,pid,email,"1","add");

                    //Intent go = new Intent(context, SignInOrLoginTabs.class);
                    //context.startActivity(go);
            }
        });
    }

    private void addToMyCart(String id_product) {
        String uid=readPrefes(context,PREF_KEY_UID,"");
        String guid=readPrefes(context,PREF_KEY_GUESTKEY,"");
        if(uid.length()>1){
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
            //Toast.makeTexpd.dismiss();
        }
        }

    public void updateCartQuantity(String q,String pid){
        try{
            pd= ProgressDialog.show(context, "Updating item", "wait...");
            pd.setCancelable(true);
            int quantity=Integer.parseInt(q);
            String uid=readPrefes(context,PREF_KEY_UID,"");
            String guid=readPrefes(context,PREF_KEY_GUESTKEY,"");
            if(guid.length()>4){
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
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public int getItemCount() {
        return RowProducts.size();
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
                        Toast.makeText(context,"product added in your cart",Toast.LENGTH_LONG).show();
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
    public class MyViewHolder extends RecyclerView.ViewHolder{
       TextView name;
       TextView rate;
       TextView offer;
        TextView discount;
        TextView avaiable;
        TextView mrp;
        TextView quanitity;
       ImageView imageView;
        LinearLayout dis,stock;
       int quantityCount=0;
       LinearLayout quantityLayout;
       Button add,minus,cart,buynow;
       public MyViewHolder(View itemView) {
           super(itemView);
           stock=(LinearLayout)itemView.findViewById(R.id.productViewStock);
           dis=(LinearLayout) itemView.findViewById(R.id.layout_distcount);
           avaiable=(TextView)itemView.findViewById(R.id.home_product_avaiable);
           discount=(TextView)itemView.findViewById(R.id.tvslider_product_Discount);
           mrp=(TextView)itemView.findViewById(R.id.tvslider_product_mrp);
           mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           name=(TextView)itemView.findViewById(R.id.tvslider_product_name);
           rate=(TextView)itemView.findViewById(R.id.tvslider_product_rate);
           offer=(TextView)itemView.findViewById(R.id.tvslider_product_offer);
           imageView=(ImageView)itemView.findViewById(R.id.ivslider_product_image);
           quantityLayout=(LinearLayout) itemView.findViewById(R.id.ll_quanitytLayout);
           add=(Button)itemView.findViewById(R.id.b_home_product_plus);
           minus=(Button)itemView.findViewById(R.id.b_home_product_minus);
           cart=(Button)itemView.findViewById(R.id.b_home_product_addTocart);
           buynow=(Button)itemView.findViewById(R.id.b_home_product_buynow);
           quanitity=(TextView)itemView.findViewById(R.id.tv_home_product_quanitiy);
   }
}
}