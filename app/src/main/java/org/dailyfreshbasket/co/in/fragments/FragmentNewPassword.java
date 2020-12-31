package org.dailyfreshbasket.co.in.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_RESETPASSWORD;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNewPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewPassword extends Fragment implements getResponce {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button chnage;
    private String otp;
    private String email;
    private ProgressDialog pd;
    private EditText newpass,repass;
    public FragmentNewPassword() {

    }
    // TODO: Rename and change types and number of parameters
    public static FragmentNewPassword newInstance(String param1, String param2) {
        FragmentNewPassword fragment = new FragmentNewPassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            otp = getArguments().getString("otp");
            email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_fragment_new_password, container, false);
        newpass=(EditText)view.findViewById(R.id.et_newpassword_pass);
        repass=(EditText)view.findViewById(R.id.et_newpassword_repass);
        chnage=(Button) view.findViewById(R.id.b_newpassword_change);
        chnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpass.getText().toString().trim().length()>5){
                    if(newpass.getText().toString().trim().equals(repass.getText().toString().trim())){
                        pd = ProgressDialog.show(getActivity(), "Updating Password", "wait...");
                        pd.setCancelable(true);
                        chnagePass(newpass.getText().toString(),email);
                    }else{
                        Toast.makeText(getActivity(),"password not matching",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"atleat 5 characters",Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    private void chnagePass(String s,String email) {
        //String email=readPrefes(getContext(),PREF_KEY_EMAIL,"");
        SendRequest req=new SendRequest(URL_RESETPASSWORD,this,2);
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",email);
        params.put("otp",otp);
        params.put("key","CHANGE");
        params.put("password",s);
        req.setParameters(params);
        req.send();
    }

    @Override
    public void responce(String res, int requestCode) {
        pd.dismiss();
        Log.d("chag pass", "responce: "+res);
        try {
            JSONObject responce=new JSONObject(res);
            if(responce.getBoolean("success")){
                Toast.makeText(getActivity(),"password updated",Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
