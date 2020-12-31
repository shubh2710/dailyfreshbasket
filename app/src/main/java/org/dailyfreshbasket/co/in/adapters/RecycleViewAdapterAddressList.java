package org.dailyfreshbasket.co.in.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.models.addressModel;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.myInterface.setResultIntent;
import org.dailyfreshbasket.co.in.networks.SendRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_ADDRESS;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class RecycleViewAdapterAddressList extends RecyclerView.Adapter<RecycleViewAdapterAddressList.MyViewHolder> implements getResponce {

    private Context context;
    private List<addressModel> data= Collections.emptyList();
    private LayoutInflater inflater;
    private setResultIntent set;
    public RecycleViewAdapterAddressList(Context context, setResultIntent set, List<addressModel> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.set=set;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view=inflater.inflate(R.layout.row_address,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final addressModel current=data.get(position);
        holder.address.setText("Address: "+(position+1));
        holder.city.setText("city: "+current.getCity());
        holder.land.setText("landmark: "+current.getLand());
        holder.line.setText("street: "+current.getStreet());
        holder.mobile.setText("mobile: "+current.getMobile());
        holder.name.setText("name: "+current.getName());
        holder.pin.setText("pin: "+current.getPin());
        holder.state.setText("state: "+current.getState());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.setResultData(position);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(context)
                 .setTitle("Daily Fresh Basket")
                        .setMessage("Do you really want to delete this address? Do not delete address if you use this address in any recent order!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                delAddress(current.getAdi());
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,data.size());
                                data.remove(position);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    private void delAddress(String adi) {
        try{
            String uid=readPrefes(context,PREF_KEY_UID,"");
            SendRequest req=new SendRequest(URL_ADDRESS,this,4);
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            params.put("aid",adi);
            params.put("key","DELETE");
            req.setParameters(params);
            req.send();
        }catch (Exception e){

        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void responce(String res, int requestCode) {
        Log.d("address", "responce: "+res);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
       private TextView name,mobile,line,land,state,city,pin;
       private Button save,edit;
       private ImageView del;
       private TextView address;
       public MyViewHolder(View view) {
           super(view);
           del=(ImageView)view.findViewById(R.id.iv_address_del);
           pin=(TextView) view.findViewById(R.id.et_pin);
           name=(TextView) view.findViewById(R.id.et_name);
           mobile=(TextView) view.findViewById(R.id.et_mobile);
           line=(TextView) view.findViewById(R.id.et_line1);
           land=(TextView) view.findViewById(R.id.et_land);
           state=(TextView) view.findViewById(R.id.et_state);
           city=(TextView) view.findViewById(R.id.et_city);
           address=(TextView)view.findViewById(R.id.tv_address);
       }
   }
}
