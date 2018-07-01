package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.RecycleViewAdapterAddressList;
import org.dailyfreshbasket.com.in.adapters.RecyclerTouchListner;
import org.dailyfreshbasket.com.in.models.addressModel;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.myInterface.setResultIntent;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_ADDRESS;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class SelectAddressActivity extends AppCompatActivity implements getResponce, setResultIntent {
    private RecyclerView recyclerview;
    public RecycleViewAdapterAddressList adaptor;
    private Context context;
    private ArrayList<addressModel> data= new ArrayList();
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_address);
    pd = ProgressDialog.show(this, "Loading address", "wait...");
    pd.setCancelable(true);
    data=new ArrayList<>();
    data.clear();
    getAddress();
    recyclerview=(RecyclerView)findViewById(R.id.rc_address_list);
    adaptor=new RecycleViewAdapterAddressList(this,this,data);
            recyclerview.setAdapter(adaptor);
            recyclerview.setFocusable(false);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new RecyclerTouchListner.ClickListener() {
        @Override
        public void onClick(View view, int position) {

        }
        @Override
        public void onLongClick(View view, int position) {
        }
    }));
    FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent select=new Intent(SelectAddressActivity.this,AddAddressActivity.class);
            startActivityForResult(select,2);
        }
    });
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAddress();
        pd=ProgressDialog.show(this, "Adding new address", "wait...");
        pd.setCancelable(true);
    }
public void getAddress(){
        String uid=readPrefes(this,PREF_KEY_UID,"");
        Log.d("uid", "getAddress uid: "+uid);
        SendRequest req=new SendRequest(URL_ADDRESS,this,1);
        Map<String,String> params = new HashMap<String, String>();
        params.put("password","");
        params.put("uid",uid);
        params.put("key","GET");
        req.setParameters(params);
        req.send();
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public void responce(String res, int requestCode) {
        Log.d("Address", "responce: "+res);
        data.clear();
        jsonparse(res);
        pd.dismiss();
    }
    public void jsonparse(String res){
        try {
            JSONObject obj=new JSONObject(res);
            boolean result=obj.getBoolean("success");
            if(result){
                if(!obj.isNull("data")){
                    JSONArray add=obj.getJSONArray("data");
                    for(int i=0;i<add.length();i++){
                        JSONObject list=add.getJSONObject(i);
                        addressModel address=new addressModel();
                        address.setAdi(list.getString("aid"));
                        address.setCity(list.getString("city"));
                        address.setLand(list.getString("landmark"));
                        address.setMobile(list.getString("mobile"));
                        address.setName(list.getString("name"));
                        address.setCharge(list.getInt("charge"));
                        address.setPin(list.getString("pincode"));
                        address.setState(list.getString("state"));
                        address.setStreet(list.getString("address"));
                        address.setType(list.getString("type"));
                        address.setUid(list.getString("uid"));
                        data.add(address);
                        adaptor.notifyDataSetChanged();
                    }
                }
                if(data.size()>0){
                    Toast.makeText(this,"select address",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"add a new address",Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setResultData(int position) {
        Intent intent = getIntent();
        intent.putExtra("aid",data.get(position).getAdi());
        intent.putExtra("name",data.get(position).getName());
        intent.putExtra("charge",data.get(position).getCharge());
        intent.putExtra("city",data.get(position).getCity());
        intent.putExtra("land",data.get(position).getLand());
        intent.putExtra("mobile",data.get(position).getMobile());
        intent.putExtra("street",data.get(position).getStreet());
        setResult(RESULT_OK, intent);
        finish();
    }
}
