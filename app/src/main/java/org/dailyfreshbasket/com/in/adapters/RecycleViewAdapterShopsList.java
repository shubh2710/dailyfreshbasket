package org.dailyfreshbasket.com.in.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.informations.infoOfShopDetails;
import org.dailyfreshbasket.com.in.networks.VollyConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by shubham on 2/19/2017.
 */
public class RecycleViewAdapterShopsList extends RecyclerView.Adapter<RecycleViewAdapterShopsList.MyViewHolder>{
    private Context context;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private List<infoOfShopDetails> data= Collections.emptyList();
    private ArrayList<infoOfShopDetails> filterData= new ArrayList<>();
    private LayoutInflater inflater;
    public RecycleViewAdapterShopsList(Context context, List<infoOfShopDetails> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
        filterData.addAll(data);
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.card_view_shoplist_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        infoOfShopDetails current=filterData.get(position);
        holder.shop_name.setText(current.shop_name);
        holder.shop_type.setText(current.shop_type);
        holder.shop_contect.setText(current.shop_contect);
        Picasso.with(context)
                .load("https://cdn2.mikroe.com/img/2016/12/14175238/20-pic-compilers-vtft-20off-offer-2016.jpg")
                .resize(200,200)
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .into(holder.shop_image);
        Log.d("POSITION","postion  on bind"+position);
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }
   public class MyViewHolder extends RecyclerView.ViewHolder{
       TextView shop_name;
       TextView shop_contect;
       TextView shop_type;
       ImageView shop_image;
       public MyViewHolder(View itemView) {
           super(itemView);
           shop_name=(TextView) itemView.findViewById(R.id.tv_shop_name);
           shop_contect=(TextView) itemView.findViewById(R.id.tvshopContetc);
           shop_type=(TextView) itemView.findViewById(R.id.tv_shop_type);
           shop_image=(ImageView) itemView.findViewById(R.id.shop_photo);
       }

   }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filterData.clear();
        if (charText.length() == 0) {
            filterData.addAll(data);
        } else {
            for (infoOfShopDetails wp : data) {
                if (wp.shop_name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    filterData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    //public void filter() {
       // textList.clear();
        //notifyDataSetChanged();
    //}
}
