package org.dailyfreshbasket.com.in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_RESETPASSWORD;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;


public class Fragment_ChangePasswordNoEmail extends Fragment implements getResponce {
    private Button verify,send;
    private EditText email,otp;
    private ProgressDialog pd;
    String TextEmail;
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_change_password, viewGroup, false);
            verify =(Button)view.findViewById(R.id.b_password_verify);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd = ProgressDialog.show(getActivity(), "Verifying otp", "wait...");
                    pd.setCancelable(true);
                 verifyOTP(otp.getText().toString());
                }
            });
            send=(Button)view.findViewById(R.id.b_password_send);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd = ProgressDialog.show(getActivity(), "Sending Otp", "wait...");
                    pd.setCancelable(true);
                    sendOtp();
                }
            });
            email=(EditText)view.findViewById(R.id.et_password_email);
            otp=(EditText)view.findViewById(R.id.et_password_otp);
            TextEmail=email.getText().toString().trim();
            if(TextEmail.equals("Please Login"))
                email.setText("");
            else{
                email.setText(TextEmail);
            }
            return view;
        }

    private void sendOtp() {
        TextEmail=email.getText().toString().trim();
        String uid =readPrefes(getContext(),PREF_KEY_UID,"");
        SendRequest req=new SendRequest(URL_RESETPASSWORD,this,1);
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",TextEmail);
        params.put("key","RESET");
        params.put("password","");
        req.setParameters(params);
        req.send();
    }

    private void verifyOTP(String s) {
        TextEmail=email.getText().toString().trim();
        SendRequest req=new SendRequest(URL_RESETPASSWORD,this,2);
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",TextEmail);
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
        Log.d("responce otp", "responce: "+requestCode+" "+res);
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
            JSONObject responce=new JSONObject(res);
            if(responce.getBoolean("success") && code==1){
                Toast.makeText(getActivity()," Otp  Send To your email",Toast.LENGTH_LONG).show();
            }else if(responce.getBoolean("success") && code==2){
                Toast.makeText(getActivity(),"Otp Verified",Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("otp", otp.getText().toString());
                bundle.putString("email",TextEmail);
                Fragment myObj =new FragmentNewPassword();
                myObj.setArguments(bundle);
                SetFragment(myObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void SetFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.forgetpassword, fragment);
        transaction.commit();
    }
}