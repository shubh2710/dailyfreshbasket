package org.dailyfreshbasket.com.in.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.models.MyOrderDetailProduct;

import java.util.Collections;
import java.util.List;

public class RecycleViewAdapterRowDetail extends RecyclerView.Adapter<RecycleViewAdapterRowDetail.MyViewHolder>{

    private Context context;
    private List<MyOrderDetailProduct> data= Collections.emptyList();
    private LayoutInflater inflater;

    public RecycleViewAdapterRowDetail(Context context, List<MyOrderDetailProduct> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view=inflater.inflate(R.layout.row_order_detail,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyOrderDetailProduct current=data.get(position);
            holder.name.setText(current.getName());
            holder.quantiyt.setText(""+current.getQuanity());
            Picasso.with(context)
                .load(current.getUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.icon);
            holder.amt.setText(""+current.getPrice());
        }
    @Override
    public int getItemCount() {
        return data.size();
    }
   public class MyViewHolder extends RecyclerView.ViewHolder{
       private TextView name,quantiyt,amt;
       private ImageView icon;
       public MyViewHolder(View view) {
           super(view);
           icon=(ImageView) view.findViewById(R.id.iv_row_detail_po_image);
           name=(TextView) view.findViewById(R.id.tv_row_detail_po_productname);
           quantiyt=(TextView) view.findViewById(R.id.tv_row_detail_po_productquantity);
           amt=(TextView) view.findViewById(R.id.tv_row_detail_po_amount);
       }
   }
}
