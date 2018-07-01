package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.CustomSpinnerAdapter;
import org.dailyfreshbasket.com.in.models.PlaceOrderListModel;
import org.dailyfreshbasket.com.in.models.addressModel;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_ADDRESS;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_PLACEORDER;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_ADDRESS;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener, getResponce, AdapterView.OnItemSelectedListener {

    private Button place,change;
    private TextView total,line,land,city,mobile,message,name;
    private TextView netTotal,charge;
    private int chargeAdd=0;
    private RadioGroup shift,method;
    private ArrayList<PlaceOrderListModel> list;
    private int Total=0;
    private int Intshift=1;
    private ProgressDialog pd;
    private String selected_aid;
    private Spinner addressList;
    private CustomSpinnerAdapter adapter;
    private ArrayList<addressModel> data= new ArrayList();
    private int IntMethod=0;
    Context context=this;
    private boolean fromCart=false;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        // get total rs and json of products;
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("Confirm Order");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle details=getIntent().getBundleExtra("extra");
        list=new ArrayList();
        list=details.getParcelableArrayList("list");
        Log.d("geting list", "onCreate: "+list);
        Total=details.getInt("total");
        fromCart=details.getBoolean("fromCart");
        setup();
        total.setText(Total+"");
        getAddress();
    }
    public void setup(){
        place=(Button)findViewById(R.id.b_confirm_place);
        change=(Button)findViewById(R.id.b_confirm_chnage);
        place.setOnClickListener(this);
        change.setOnClickListener(this);
        total=(TextView)findViewById(R.id.tv_confirm_total);
        line=(TextView)findViewById(R.id.tv_confirm_street);
        land=(TextView)findViewById(R.id.tv_confirm_land);
        city=(TextView)findViewById(R.id.tv_confirm_city);
        name=(TextView) findViewById(R.id.tv_confirm_address_name);
        mobile=(TextView)findViewById(R.id.tv_confirm_mobile);
        message=(TextView)findViewById(R.id.tv_confirm_message);
        netTotal=(TextView) findViewById(R.id.tv_confirm_netTotal);
        charge=(TextView) findViewById(R.id.tv_confirm_charge);
        shift=(RadioGroup) findViewById(R.id.rg_shift);
        method=(RadioGroup) findViewById(R.id.rg_method);
        method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (method.getCheckedRadioButtonId()){
                    case R.id.rb_confirm_card:
                        IntMethod=1;
                        message.setText("card payment not available right not");
                        Toast.makeText(context,"card payment not available right not",Toast.LENGTH_LONG).show();
                        message.setTextColor(Color.RED);
                        checkCanPlaced();
                        break;
                    case R.id.rb_confirm_cod:
                        IntMethod=0;
                        message.setTextColor(Color.GREEN);
                        checkCanPlaced();
                        break;
                    case R.id.rb_confirm_net:
                        IntMethod=2;
                        Toast.makeText(context,"net banking not available right not",Toast.LENGTH_LONG).show();
                        message.setText("Net banking not available right not");
                        message.setTextColor(Color.RED);
                        checkCanPlaced();
                        break;
                }
            }
        });
        shift.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Calendar cal = Calendar.getInstance();
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                Log.d("time", "onCheckedChanged: time"+currentHour);

                switch (shift.getCheckedRadioButtonId()){
                    case R.id.rb_confirm_dayshift:
                        if(currentHour>8 && currentHour<12){
                            shift.check(R.id.rb_confirm_nightshift);
                            Toast.makeText(ConfirmOrderActivity.this,"it will deliverd on second shift",Toast.LENGTH_LONG).show();
                        }else{
                            Intshift=0;
                        }
                        checkCanPlaced();
                        break;
                    case R.id.rb_confirm_nightshift:
                        if(currentHour>13){
                            shift.check(R.id.rb_confirm_dayshift);
                            Toast.makeText(ConfirmOrderActivity.this,"it will deliverd on tomorrow first shift",Toast.LENGTH_LONG).show();
                        }else{
                            Intshift=1;
                        }
                        checkCanPlaced();
                        break;
                }
            }
        });
        adapter=new CustomSpinnerAdapter(this,R.layout.address_row_spinner,data);
        addressList=(Spinner)findViewById(R.id.sp_confirmorder);
        addressList.setOnItemSelectedListener(this);
        addressList.setAdapter(adapter);
    }
    private void checkCanPlaced() {
        place.setEnabled(false);
        if(Intshift != -1 && IntMethod==0 && data.size()>0 ){
            place.setEnabled(true);
        }
    }
    public void getAddress(){
        pd=ProgressDialog.show(this, "wait...", "wait...");
        pd.setCancelable(true);
        String aid=readPrefes(this,PREF_KEY_ADDRESS,"");
        String uid=readPrefes(this,PREF_KEY_UID,"");
            Log.d("uid", "getAddress uid: "+uid);
            SendRequest req=new SendRequest(URL_ADDRESS,this,2);
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            params.put("key","GET");
            req.setParameters(params);
            req.send();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_confirm_chnage:
                    Intent select=new Intent(ConfirmOrderActivity.this,SelectAddressActivity.class);
                    startActivityForResult(select,2);
                break;
            case R.id.b_confirm_place:
                confirmAndplace();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==RESULT_OK){
                Log.d("result activyt", "onActivityResult: ");
               String city=data.getStringExtra("city");
                String aid=data.getStringExtra("aid");
                String name=data.getStringExtra("name");
                int charge=data.getIntExtra("charge",0);
                String land=data.getStringExtra("land");
                String mobile=data.getStringExtra("mobile");
                String street=data.getStringExtra("street");
                addressModel a=new addressModel();
                a.setCharge(charge);
                a.setAdi(aid);
                a.setMobile(mobile);
                a.setStreet(street);
                a.setType(1+"");
                a.setName(name);
                a.setCity(city);
                a.setLand(land);

                this.data.add(a);
                change.setText("CHANGE");
                this.land.setText(land);
                line.setText(street);
                this.city.setText(city);
                this.charge.setText(charge+"");
                netTotal.setText(Total+charge+"");
                this.mobile.setText(mobile);
                this.name.setText(name);
                selected_aid=aid;
                chargeAdd=charge;
            }
    }
    public void confirmAndplace(){
        if(data.size()>0){
            if(Intshift != -1 && IntMethod==0 ){
                pd=ProgressDialog.show(this, "Placeing Order", "wait...");
                pd.setCancelable(true);
                String uid=readPrefes(this,PREF_KEY_UID,"");
                Log.d("UID",uid);
                SendRequest req=new SendRequest(URL_PLACEORDER,this,1);
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid",uid);
                params.put("password","");
                params.put("jsonProducts",createJson());
                params.put("aid",selected_aid);
                Log.d("place order", "confirmAndplace: "+fromCart);
                params.put("fromCart",fromCart+"");
                params.put("shift",Intshift+"");
                params.put("total",(Total+chargeAdd)+"");
                req.setParameters(params);
                req.send();
            }else{
                Toast.makeText(this,"Please select shift and valid payment method",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Please Add Address",Toast.LENGTH_LONG).show();
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
public String createJson(){
    JSONArray productlist=new JSONArray();
    JSONObject product=null;
            for(PlaceOrderListModel p:list){
                try {
                    product=new JSONObject();
                    product.put("pid",p.getPid());
                    product.put("quanity",p.getQuantity());
                    product.put("price",p.getRs());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(product!=null)
                productlist.put(product);
            }
            Log.d("JSONLIST",productlist.toString());
    return productlist.toString();
}
public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
@Override
public void responce(String res, int requestCode) {
    pd.dismiss();
    switch (requestCode){
        case 1:
            Log.d("Responce", "responce: "+res);
            if(res !=null && res.length()>1){
                try {
                    JSONObject responce =new JSONObject(res);
                    if(responce.getBoolean("success")){
                        String orderNumber=responce.getInt("data")+"";
                        Log.d("order no", "responce: "+orderNumber);
                        Intent confirm=new Intent(ConfirmOrderActivity.this,ThankYouActivity.class);
                        Bundle details=new Bundle();
                        details.putString("oid",orderNumber);
                        confirm.putExtra("extra",details);
                        startActivity(confirm);
                        finish();
                    }else{
                        JSONArray result=responce.getJSONArray("data");
                        if(!result.getJSONObject(0).getBoolean("success")){
                            String q=result.getJSONObject(0).getString("quantiytAvaliable");
                            String name=result.getJSONObject(0).getString("product name");
                            new android.support.v7.app.AlertDialog.Builder(context)
                                    .setTitle("Daily Fresh Basket")
                                    .setMessage("\"product quantity is not avaliable of '"+name+"' avaliable quantity is "+q +" ,Only Limited stock is available\"")
                                    .setIcon(android.R.drawable.ic_dialog_dialer)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                        }})
                                    .show();
                        }
                        //message.setText("product quantity is not avaliable of '"+name+"' avaliable quantity is "+q +" ,Only Limited stock is available");
                        //message.setTextColor(Color.RED);
                        //Toast.makeText(this,"product quantity not avaliable of "+name+" avaliable quantity is "+q,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
        case 2:
            try{
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
                        address.setPin(list.getString("pincode"));
                        address.setCharge(list.getInt("charge"));
                        address.setState(list.getString("state"));
                        address.setStreet(list.getString("address"));
                        address.setType(list.getString("type"));
                        address.setUid(list.getString("uid"));
                        data.add(address);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
                if(data.size()>0){
                    addressModel a=data.get(0);
                    land.setText(a.getLand());
                    line.setText(a.getStreet());
                    city.setText(a.getCity());
                    mobile.setText(a.getMobile());
                    name.setText(a.getName());
                    selected_aid=a.getAdi();
                    chargeAdd=a.getCharge();
                    charge.setText(a.getCharge()+"");
                    change.setText("Change");
                    netTotal.setText(Total+a.getCharge()+"");
                }else{
                    change.setText("ADD");
                    charge.setText("0");
                    netTotal.setText((Total+0)+"");
                    land.setText("address not given");
                    line.setText("street not given");
                    city.setText("city not given");
                    mobile.setText("mobile not given");
                    name.setText("name not given");
                }

    } catch (JSONException e) {
        e.printStackTrace();
    }
            break;
    }
}
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    addressModel a=data.get(position);
                land.setText(a.getLand());
                line.setText(a.getStreet());
                city.setText(a.getCity());
                charge.setText(a.getCharge()+"");
                netTotal.setText(Total+a.getCharge()+"");
                mobile.setText(a.getMobile());
                name.setText(a.getName());
                selected_aid=a.getAdi();
                chargeAdd=a.getCharge();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
}