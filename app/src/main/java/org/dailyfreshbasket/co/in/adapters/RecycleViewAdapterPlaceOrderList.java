package org.dailyfreshbasket.co.in.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.PlaceNowActivity;
import org.dailyfreshbasket.co.in.models.PlaceOrderModel;
import org.dailyfreshbasket.co.in.networks.VollyConnection;

import java.util.Collections;
import java.util.List;

import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

public class RecycleViewAdapterPlaceOrderList extends RecyclerView.Adapter<RecycleViewAdapterPlaceOrderList.MyViewHolder> {

    private List<PlaceOrderModel> data= Collections.emptyList();
    private LayoutInflater inflater;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ProgressDialog pd;
    private  int selectedCart=-1;
    private PlaceNowActivity activity;
    public RecycleViewAdapterPlaceOrderList(PlaceNowActivity activity, List<PlaceOrderModel> data){
        inflater= LayoutInflater.from(activity);
        this.data=data;
        this.activity=activity;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        Log.d("NOTIFIE","constructor called");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.row_place_order_layout,parent,false);
        Log.d("NOTIFIE","onCreateView called");
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final PlaceOrderModel currentdata=data.get(position);
        final String id=currentdata.getName();
        Log.d("NOTIFIE","onbindViewholder called");
        selectedCart=position;
        holder.name.setText(currentdata.getName());
        holder.price.setText("Rs "+currentdata.getPrice());
        holder.quantity.setText(currentdata.getQuatity()+"");
        holder.quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try{
                    if(s.length()>0 &&  Integer.parseInt(s.toString())>0){
                        data.get(position).setQuatity(Integer.parseInt(s+""));
                    }else{
                        data.get(position).setQuatity(0);
                        Toast.makeText(activity,"can't place 0 quantity",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){

                }


            }
        });
        Picasso.with(activity)
                .load(currentdata.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.icon);
        Log.d("POSITION","postion  on bind"+position);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,price;
        private  EditText quantity;
        private ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            setup(itemView);
           Log.d("NOTIFIE","MyviewHoldder called");
       }
        public void setup(View itemView){
            name=(TextView)itemView.findViewById(R.id.tv_placeorder_name);
            quantity=(EditText) itemView.findViewById(R.id.tv_place_quantity);
            price=(TextView)itemView.findViewById(R.id.tv_place_rupees);
             icon=(ImageView)itemView.findViewById(R.id.iv_placeorder_image);
            icon.setImageResource(R.drawable.ic_delete_black_24px);
        }
   }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
}