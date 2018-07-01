package org.dailyfreshbasket.com.in.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.informations.information;

import java.util.Collections;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>{

    private Context context;
    private List<information> data= Collections.emptyList();
    private LayoutInflater inflater;
    Resources resources;

    public RecycleViewAdapter(Context context, List<information> data, Resources resources){
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.resources=resources;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view=inflater.inflate(R.layout.custome_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        information current=data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
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
           title=(TextView) itemView.findViewById(R.id.tvListTitle);
           icon=(ImageView) itemView.findViewById(R.id.ivListIcon);
       }

   }

}
