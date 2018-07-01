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
import org.dailyfreshbasket.com.in.informations.infoOfProducts;
import org.dailyfreshbasket.com.in.networks.VollyConnection;

import java.util.Collections;
import java.util.List;

/**
 * Created by shubham on 2/19/2017.
 */
public class RecycleViewAdapterProducts extends RecyclerView.Adapter<RecycleViewAdapterProducts.MyViewHolder>{
    private Context context;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private List<infoOfProducts> data= Collections.emptyList();
    private LayoutInflater inflater;
    public RecycleViewAdapterProducts(Context context, List<infoOfProducts> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.card_view_list,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        infoOfProducts current=data.get(position);
        holder.title.setText(current.title);
        Picasso.with(context)
                .load("https://cdn2.mikroe.com/img/2016/12/14175238/20-pic-compilers-vtft-20off-offer-2016.jpg")
                .resize(200,200)
                .centerInside()
                .placeholder(R.drawable.placeholder)
                .into(holder.icon);
        Log.d("POSITION","postion  on bind"+position);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
   public class MyViewHolder extends RecyclerView.ViewHolder{
       TextView title;
       ImageView icon;
       public MyViewHolder(View itemView) {
           super(itemView);
           title=(TextView) itemView.findViewById(R.id.product_name);
           icon=(ImageView) itemView.findViewById(R.id.product_photo);
       }

   }

}
