package org.dailyfreshbasket.co.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_varifyOtp;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

public class OTPActivity extends AppCompatActivity implements getResponce {

    private Button verify,send;
    private EditText email,otp;
    private ProgressDialog pd;
    String TextEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);
        verify =(Button)findViewById(R.id.b_password_verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(otp.getText().toString());
            }
        });
        send=(Button)findViewById(R.id.b_password_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
            }
        });
        email=(EditText)findViewById(R.id.et_password_email);
        email.setText(getIntent().getStringExtra("email"));
        otp=(EditText)findViewById(R.id.et_password_otp);
        sendOtp();
        //TextEmail=readPrefes(OTPActivity.this,PREF_KEY_EMAIL,"");
        //email.setText(TextEmail);

    }
    private void sendOtp() {
        pd = ProgressDialog.show(OTPActivity.this, "Sending Otp", "wait...");
        pd.setCancelable(true);
        SendRequest req=new SendRequest(URL_varifyOtp,this,1);
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",email.getText().toString());
        params.put("key","RESET");
        params.put("password","");
        req.setParameters(params);
        req.send();
    }

    private void verifyOTP(String s) {
        pd = ProgressDialog.show(OTPActivity.this, "Verifying otp", "wait...");
        pd.setCancelable(true);
        SendRequest req=new SendRequest(URL_varifyOtp,this,2);
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",email.getText().toString());
        params.put("otp",s);
        params.put("key","VERIFY");
        params.put("password","");
        req.setParameters(params);
        req.send();
    }

    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }

    @Override
    public void responce(String res, int requestCode) {
        pd.dismiss();
        Log.d("responce otp", "responce: "+requestCode);
        switch (requestCode){
            case 1:
                JSONParse(res,requestCode);
                break;
            case 2:
                JSONParse(res,requestCode);
                break;
        }
    }

    private void JSONParse(String res,int code) {
        try {
            Log.d("OPTRES", "JSONParse: "+res);
            JSONObject responce=new JSONObject(res);
            if(responce.getBoolean("success") && code==1){
                Toast.makeText(OTPActivity.this," Otp  Send To your email",Toast.LENGTH_LONG).show();
            }else if(responce.getBoolean("success") && code==2){
                Toast.makeText(OTPActivity.this,"Otp Verified",Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("OTPRES",e.toString());
        }
    }
}