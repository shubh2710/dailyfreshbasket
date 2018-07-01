package org.dailyfreshbasket.com.in.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.activities.ShopShowCaseActivity;
import org.dailyfreshbasket.com.in.activities.ShowCase;
import org.dailyfreshbasket.com.in.informations.Products_in_cart_details;
import org.dailyfreshbasket.com.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.KEY_CART_MANAGER_URL;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

/**
 * Created by shubham on 2/19/2017.
 */
public class RecycleViewAdapterShopProducts extends RecyclerView.Adapter<RecycleViewAdapterShopProducts.MyViewHolder>{

    private List<Products_in_cart_details> data= Collections.emptyList();
    private LayoutInflater inflater;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ProgressDialog pd;
    private ShopShowCaseActivity activity;
    public RecycleViewAdapterShopProducts(ShopShowCaseActivity activity, List<Products_in_cart_details> data){
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

        setdata(currentdata.getProduct_name(),
                currentdata.getProduct_actualprice(),
                currentdata.getProduct_id(),
                currentdata.getProduct_shopid(),
                currentdata.getProduct_cartquantity(),
                currentdata.getPics().get(0),
                holder);
        holder.remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pd = ProgressDialog.show(activity, "Removing item", "wait...");
                pd.setCancelable(true);
                String email=readPrefes(activity,PREF_KEY_EMAIL,"");
                String uid=readPrefes(activity,PREF_KEY_UID,"");
                if(email.length()>1 && uid.length()>1){
                    requestRemoveItemCart(uid,id,email,"","delete",position);
                }else {
                    pd.dismiss();
                    Toast.makeText(activity, "you must login first", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle details=new Bundle();
                details.putStringArrayList("image",currentdata.getPics());
                details.putString("mrp",currentdata.getProduct_mrp());
                details.putString("rate",currentdata.getProduct_actualprice());
                details.putString("id",currentdata.getProduct_id());
                details.putString("name",currentdata.getProduct_name());
                details.putString("tag",currentdata.getProduct_offer());
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



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_subname,tv_rate;
        private ImageView image;
        private ImageButton remove;
        public MyViewHolder(View itemView) {
           super(itemView);
            setup(itemView);

           Log.d("NOTIFIE","MyviewHoldder called");

       }
        public void setup(View itemView){
            tv_name=(TextView)itemView.findViewById(R.id.tv_product_in_cart_name);
            tv_rate=(TextView)itemView.findViewById(R.id.tv_product_in_cart_rate);
            tv_subname=(TextView)itemView.findViewById(R.id.tv_product_in_cart_subname);
            image=(ImageView)itemView.findViewById(R.id.iv_product_image_in_cart_layout);
            remove=(ImageButton)itemView.findViewById(R.id.b_cartView_removeProduct);
            remove.setImageResource(R.drawable.ic_delete_black_24px);
        }
   }

    public void setdata(String name,String rate,String subname,String shopname,String quantity,String imageURL
            ,MyViewHolder holder){
        holder.tv_name.setText(name);
        holder.tv_rate.setText(rate);
        holder.tv_subname.setText(subname);
        //holder.tv_shopname.setText(shopname);
        //holder.et_quantity.setText(quantity);
        Picasso.with(activity)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }
    public void requestRemoveItemCart(final String uid,final String pid,final String email,final String quentity,final String data,final int pos){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_CART_MANAGER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONObject obj = new JSONObject(response);
                            Log.d("JSON_CART"+":"+uid+":"+pid+":"+email+":"+quentity+":", obj.toString());
                            parseJSONResponce(obj,uid,pid,email,quentity,pos);
                        } catch (Exception t) {
                            Log.e("JSON_CART", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",uid);
                params.put("email",email);
                params.put("pid",pid);
                params.put("data",data);
                params.put("quantity",quentity);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    private void parseJSONResponce(JSONObject response,String uid,String pid,String email,String quentity,int position) {
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
