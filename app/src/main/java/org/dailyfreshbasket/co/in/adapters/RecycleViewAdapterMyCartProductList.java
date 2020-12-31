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
import org.dailyfreshbasket.co.in.activities.ActivityMyCart;
import org.dailyfreshbasket.co.in.activities.ShowCase;
import org.dailyfreshbasket.co.in.informations.Products_in_cart_details;
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
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

/**
 * Created by shubham on 2/19/2017.
 */
public class RecycleViewAdapterMyCartProductList extends RecyclerView.Adapter<RecycleViewAdapterMyCartProductList.MyViewHolder> implements getResponce {
    private List<Products_in_cart_details> data= Collections.emptyList();
    private LayoutInflater inflater;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ProgressDialog pd;
    private  int selectedCart=-1;
    private int delPos=-1;
    private ActivityMyCart activity;
    public RecycleViewAdapterMyCartProductList(ActivityMyCart activity, List<Products_in_cart_details> data){
        inflater= LayoutInflater.from(activity);
        this.data=data;
        this.activity=activity;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();

        Log.d("NOTIFIE","constructor called");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.row_cart_view,parent,false);
        Log.d("NOTIFIE","onCreateView called");
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final Products_in_cart_details currentdata=data.get(position);
        final String id=currentdata.getProduct_id();
        Log.d("NOTIFIE","onbindViewholder called");
        selectedCart=position;
        if(currentdata.getPics().size()>0)
        setdata(currentdata.getProduct_name(),
                currentdata.getProduct_actualprice(),
                currentdata.getProduct_id(),
                currentdata.getProduct_shopid(),
                currentdata.getProduct_cartquantity(),
                currentdata.getProduct_stockquantity(),
                currentdata.getPics().get(0),
                holder);
        else
            setdata(currentdata.getProduct_name(),
                    currentdata.getProduct_actualprice(),
                    currentdata.getProduct_id(),
                    currentdata.getProduct_shopid(),
                    currentdata.getProduct_cartquantity(),
                    currentdata.getProduct_stockquantity(),
                    URL_DEFULT_PIC,
                    holder);
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentdata.quantity<100 && currentdata.quantity<Integer.parseInt(currentdata.getProduct_stockquantity())){
                    holder.tv_quantity.setText((++currentdata.quantity)+"");
                    currentdata.setProduct_cartquantity(currentdata.quantity+"");
                    activity.setdataUpdate();
                    updateCartQuantity(currentdata.quantity+"",currentdata.getCid());
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentdata.quantity>1){
                    holder.tv_quantity.setText(--currentdata.quantity+"");
                    currentdata.setProduct_cartquantity(currentdata.quantity+"");
                    activity.setdataUpdate();
                    updateCartQuantity(currentdata.quantity+"",currentdata.getCid());
                    activity.setdataUpdate();
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pd = ProgressDialog.show(activity, "Removing item", "wait...");
                pd.setCancelable(true);
                delPos=position;
                String email=readPrefes(activity,PREF_KEY_EMAIL,"");
                requestRemoveItemCart(currentdata.getCid(),position);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle details=new Bundle();
                details.putString("id",currentdata.getProduct_id());
                Intent showcase=new Intent(activity, ShowCase.class);
                showcase.putExtra("extra",details);
                activity.startActivity(showcase);
                activity.finish();
            }
        });
        Log.d("POSITION","postion  on bind"+position);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

   public void updateCartQuantity(String q,String cid){
        try{
            pd= ProgressDialog.show(activity, "Updating item", "wait...");
            pd.setCancelable(false);
            int quantity=Integer.parseInt(q);
            String uid=readPrefes(activity,PREF_KEY_UID,"");
            String guid=readPrefes(activity,PREF_KEY_GUESTKEY,"");
            if(guid.length()>5){
                SendRequest req=new SendRequest(URL_GUESTCART,this,1);
                Map<String,String> params = new HashMap<String, String>();
                params.put("cid",cid);
                params.put("quantity",quantity+"");
                params.put("password","");
                params.put("guid",guid);
                params.put("key","UPDATE");
                req.setParameters(params);
                req.send();
            }else{
                SendRequest req=new SendRequest(URL_MYCART,this,1);
                Map<String,String> params = new HashMap<String, String>();
                params.put("cid",cid);
                params.put("quantity",quantity+"");
                params.put("password","");
                params.put("uid",uid);
                params.put("key","UPDATE");
                req.setParameters(params);
                req.send();
            }

        }catch (Exception e){

        }
   }
    @Override
    public void responce(String res, int requestCode) {
       switch (requestCode){
         case 1:
             try {
                 pd.dismiss();
                 JSONObject result=new JSONObject(res);
                 if(result.getBoolean("success")){
                     //Products_in_cart_details currentdata=data.get(selectedCart);
                     //currentdata.setProduct_cartquantity(result.getString("data"));
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             break;
           case 2:

               break;
           case 3:
               pd.dismiss();
               JSONObject obj = null;
               try {
                   obj = new JSONObject(res);
                   parseJSONResponce(obj,delPos);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               break;
       }

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_quantity,tv_name,tv_subname,tv_rate,tv_shopname;
        private ImageView image;
        private ImageButton remove;
        private TextView stock;
        private int q=1;
        private Button plus,minus;
        public MyViewHolder(View itemView) {
           super(itemView);
            setup(itemView);
           Log.d("NOTIFIE","MyviewHoldder called");
       }
        public void setup(View itemView){
            stock=(TextView)itemView.findViewById(R.id.tv_cart_stock);
            plus=(Button)itemView.findViewById(R.id.b_cart_product_plus);
            minus=(Button)itemView.findViewById(R.id.b_cart_product_minus);
            tv_quantity=(TextView)itemView.findViewById(R.id.tv_cart_product_quanitiy);
            tv_name=(TextView)itemView.findViewById(R.id.tv_product_in_cart_name);
            tv_rate=(TextView)itemView.findViewById(R.id.tv_product_in_cart_rate);
            tv_subname=(TextView)itemView.findViewById(R.id.tv_product_in_cart_subname);
            image=(ImageView)itemView.findViewById(R.id.iv_product_image_in_cart_layout);
            remove=(ImageButton)itemView.findViewById(R.id.b_cartView_removeProduct);
            remove.setImageResource(R.drawable.ic_delete_black_24px);
        }
   }

    public void setdata(String name,String rate,String subname,String shopname,String quantity,String stock,String imageURL
            ,MyViewHolder holder){
        holder.tv_name.setText(name);
        if(Integer.parseInt(stock)>0){
            holder.stock.setText("avaliable "+stock);
            holder.stock.setTextColor(Color.BLACK);
        }else{
            holder.stock.setText("avaliable "+stock);
            holder.stock.setTextColor(Color.RED);
        }
        holder.tv_rate.setText("Rs "+rate);
        holder.tv_subname.setText(subname);
        holder.tv_quantity.setText(quantity);
        Picasso.with(activity)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }
    public void requestRemoveItemCart(final String cid,final int pos){
        String uid=readPrefes(activity,PREF_KEY_UID,"");
        String guid=readPrefes(activity,PREF_KEY_GUESTKEY,"");
        if(guid.length()>5){
            SendRequest req=new SendRequest(URL_GUESTCART,this,3);
            Map<String,String> params = new HashMap<String, String>();
            params.put("guid",uid);
            params.put("cid",cid);
            params.put("key","REMOVE");
            params.put("password","");
            req.setParameters(params);
            req.send();
        }else{
            SendRequest req=new SendRequest(URL_MYCART,this,3);
            Map<String,String> params = new HashMap<String, String>();
            params.put("uid",uid);
            params.put("cid",cid);
            params.put("key","REMOVE");
            params.put("password","");
            req.setParameters(params);
            req.send();
        }

    }
    private void parseJSONResponce(JSONObject response,int position) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Boolean result=response.getBoolean("success");
            if(result){
                data.remove(position);
                activity.setupBadge(-1);
                Toast.makeText(activity,"one item remove", Toast.LENGTH_SHORT).show();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,data.size());
                activity.setdataUpdate();
                Log.d("JSON_CART_RESULT",result.toString());
                Toast.makeText(activity,"Producted removed",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(activity,"Can't remove",Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
}
