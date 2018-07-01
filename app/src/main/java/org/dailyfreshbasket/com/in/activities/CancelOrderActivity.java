package org.dailyfreshbasket.com.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_GETORDER;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class CancelOrderActivity extends AppCompatActivity implements getResponce {
    private String oid,o_no;
    private TextView tv_oid,tv_o_no;
    private EditText message;
    private Button cancel;
    private ProgressDialog pd=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        Bundle details=getIntent().getBundleExtra("extra");
        try{

            oid=details.getString("oid");
            o_no=details.getString("o_no");
        }catch (Exception e){

        }
        setdata();
    }
    private void setdata() {
        tv_o_no=(TextView) findViewById(R.id.tv_cancel_no);
        tv_oid=(TextView) findViewById(R.id.tv_cancel_oid);
        message=(EditText) findViewById(R.id.et_cancel);
        cancel=(Button) findViewById(R.id.b_cancel);
        tv_oid.setText("Order id "+oid);
        tv_o_no.setText("Order no "+o_no);
       final  Context context=this;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(context)
                        .setTitle("Daily Fresh Basket")
                        .setMessage("Do you really want to cancel order?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                cancelOrder();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }
    public void cancelOrder(){
        try{
            pd= ProgressDialog.show(this, "Cencelling Order", "wait...");
            String uid=readPrefes(this,PREF_KEY_UID,"");
            SendRequest req=new SendRequest(URL_GETORDER,this,4);
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            params.put("oid",oid);
            params.put("ono",o_no);
            params.put("key","CANCEL");
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
    public void responce(String res, int requestCode) {
        pd.dismiss();
        Log.d("order cancle", "responce: "+res);
        if(res !=null && res.length()>4){
            try {
                JSONObject respoce=new JSONObject(res);
                if (respoce.getBoolean("success")){
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
