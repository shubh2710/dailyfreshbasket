package org.dailyfreshbasket.co.in.adapters;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.ActivityMyWishList;
import org.dailyfreshbasket.co.in.activities.ShowCase;
import org.dailyfreshbasket.co.in.informations.Products_in_wishList_details;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_MOVEWISHLIST;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;


public class RecycleViewAdapterMyWishlistProduct extends RecyclerView.Adapter<RecycleViewAdapterMyWishlistProduct.MyViewHolder> implements getResponce {

    private List<Products_in_wishList_details> data= Collections.emptyList();
    private LayoutInflater inflater;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ProgressDialog pd;
    private int currentClick=-1;
    private ActivityMyWishList activity;
    public RecycleViewAdapterMyWishlistProduct(ActivityMyWishList activity, List<Products_in_wishList_details> data){
        inflater= LayoutInflater.from(activity);
        this.data=data;
        this.activity=activity;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        Log.d("NOTIFIE","constructor called");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.row_wishlist_view,parent,false);
        Log.d("NOTIFIE","onCreateView called");
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final Products_in_wishList_details currentdata=data.get(position);
        final String id=currentdata.getProduct_id();
        Log.d("NOTIFIE","onbindViewholder called");
    if(currentdata.getProduct_stockquantity())
        setdata(currentdata.getProduct_name(),
                currentdata.getProduct_actualprice(),
                currentdata.getProduct_id(),
                currentdata.getProduct_shopid(),
                "In Stock",
                currentdata.getPics().get(0),
                holder);
        else
            setdata(currentdata.getProduct_name(),
                    currentdata.getProduct_actualprice(),
                    currentdata.getProduct_id(),
                    currentdata.getProduct_shopid(),
                    "Out Of Stock",
                    currentdata.getPics().get(0),
                    holder);
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClick=position;
                pd = ProgressDialog.show(activity, "Adding Item into cart", "wait...");
                pd.setCancelable(true);
                String email=readPrefes(activity,PREF_KEY_EMAIL,"");
                String uid=readPrefes(activity,PREF_KEY_UID,"");
                if(email.length()>1 && uid.length()>1){
                requestAddToCart(currentdata.getWid(),currentdata.getProduct_id(),position);
                }else {
                    pd.dismiss();
                    Toast.makeText(activity, "you must login first", Toast.LENGTH_LONG).show();
                }
            }
            });
        holder.remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                currentClick=position;
                pd = ProgressDialog.show(activity, "Removing item", "wait...");
                pd.setCancelable(true);
                String email=readPrefes(activity,PREF_KEY_EMAIL,"");
                String uid=readPrefes(activity,PREF_KEY_UID,"");
                if(email.length()>1 && uid.length()>1){
                    requestRemoveItemWishList(currentdata.getWid(),position);
                }else {
                    pd.dismiss();
                    Toast.makeText(activity, "you must login first", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentClick=position;
                Bundle details=new Bundle();
                details.putString("id",currentdata.getProduct_id());
                Intent showcase=new Intent(activity, ShowCase.class);
                showcase.putExtra("extra",details);
                activity.startActivity(showcase);

            }
        });
        Log.d("POSITION","postion  on bind"+position);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void responce(String res, int requestCode) {
        switch (requestCode){
            case 1:
                parseJSONResponce(res,currentClick);

                break;
            case 2:
                parseJSONResponce(res,currentClick);
                break;
        }
        Log.d("RESPONCE ",requestCode+" "+res );
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_subname,tv_rate,tv_shopname;
        private TextView tv_quantity;
        private ImageView image;
        private ImageButton remove;
        private Button addToCart;
        public MyViewHolder(View itemView) {
           super(itemView);
            setup(itemView);
           Log.d("NOTIFIE","MyviewHoldder called");
       }
        public void setup(View itemView){
            tv_name=(TextView)itemView.findViewById(R.id.tv_product_in_wishlist_name);
            tv_rate=(TextView)itemView.findViewById(R.id.tv_product_in_wishlist_rate);
            tv_shopname=(TextView)itemView.findViewById(R.id.tv_product_in_wishlist_sub_shop_name);
            tv_subname=(TextView)itemView.findViewById(R.id.tv_product_in_wishlist_subname);
            tv_quantity=(TextView) itemView.findViewById(R.id.et_product_in_wishlist_quentity);
            image=(ImageView)itemView.findViewById(R.id.iv_product_image_in_wishlist_layout);
            remove=(ImageButton)itemView.findViewById(R.id.b_cartView_removeProduct);
            addToCart=(Button)itemView.findViewById(R.id.b_product_in_wishlist_addtocart);
        }
   }
    public void setdata(String name,String rate,String subname,String shopname,String quantity,String imageURL
            ,MyViewHolder holder){
        holder.tv_name.setText(name);
        holder.tv_rate.setText("Rs "+rate);
        holder.tv_subname.setText(subname);
        holder.tv_shopname.setText(shopname);
        if(quantity.equals("In Stock"))
        holder.tv_quantity.setTextColor(Color.GREEN);
        else
            holder.tv_quantity.setTextColor(Color.RED);
        holder.tv_quantity.setText(quantity);
        Picasso.with(activity)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }

    public void requestAddToCart(final String wid,final String pid,int pos){
        SendRequest req=new SendRequest(URL_MOVEWISHLIST,this,1);
        Map<String,String> params = new HashMap<String, String>();
        String uid=readPrefes(activity,PREF_KEY_UID,"");
        params.put("pid",pid);
        params.put("key","MOVE");
        params.put("uid",uid);
        params.put("password","");
        req.setParameters(params);
        req.send();
    }

    public void requestRemoveItemWishList(String wid,int pos){
        SendRequest req=new SendRequest(URL_MOVEWISHLIST,this,2);
        Map<String,String> params = new HashMap<String, String>();
        String uid=readPrefes(activity,PREF_KEY_UID,"");
        params.put("wid",wid);
        params.put("key","REMOVE");
        params.put("uid",uid);
        params.put("password","");
        req.setParameters(params);
        req.send();
    }
    private void parseJSONResponce(String res,int position) {
        pd.dismiss();
        try {
            JSONObject response=new JSONObject(res);
            if(response==null || response.length()==0){
                return;
            }
            try{
                Boolean result=response.getBoolean("success");
                if(result){
                    data.remove(position);
                    Toast.makeText(activity,"one item remove", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,data.size());
                    Log.d("JSON_WIshList_RESULT",result.toString());
                    Toast.makeText(activity,"Producted removed",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(activity,"Can't remove",Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
}
