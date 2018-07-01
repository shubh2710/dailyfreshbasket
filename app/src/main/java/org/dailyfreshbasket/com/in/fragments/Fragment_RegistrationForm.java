package org.dailyfreshbasket.com.in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.activities.Home;
import org.dailyfreshbasket.com.in.activities.OTPActivity;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.dailyfreshbasket.com.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_GUESTCART;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_SIGNUP;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_EMAIL;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_GUESTKEY;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_LOGIN;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_PROFILE_PIC;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_USER_PASS;
import static org.dailyfreshbasket.com.in.informations.UserDetailKeys.KEYS.KEY_PASSWORD;


public class Fragment_RegistrationForm extends Fragment implements getResponce {
    private EditText UserEmail;
    private EditText UserPassword;
    private EditText UserReEnterPassword;
    private Button SignInButton,googlesignup;
    private String Email;
    private String ReEmail;
    private String Password;
    private FirebaseAuth mAuth;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private String RePassword;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FirebaseUser user=null;
    private ProgressDialog pd=null;
    public Fragment_RegistrationForm() {
        // Required empty public constructor
    }
    public static Fragment_RegistrationForm newInstance(String param1, String param2) {
        Fragment_RegistrationForm fragment = new Fragment_RegistrationForm();
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
        }
    }
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.new_signup_layout, viewGroup, false);
            FirebaseApp.initializeApp(getActivity());
            UserEmail =(EditText)view.findViewById(R.id.et_signIn_user_email);
            UserPassword=(EditText)view.findViewById(R.id.et_signIn_user_password);
            UserReEnterPassword=(EditText)view.findViewById(R.id.et_signIn_user_repassword);
            googlesignup=(Button)view.findViewById(R.id.sign_up_button);
            SignInButton=(Button)view.findViewById(R.id.bSignIn_user_SignInbutton);
            pd=new ProgressDialog(getActivity());
            vollyConnection= VollyConnection.getsInstance();
            requestQueue=VollyConnection.getsInstance().getRequestQueue();
            googlesignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth=FirebaseAuth.getInstance();
                    signIn();
                }
            });
            SignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Email=UserEmail.getText().toString().trim();
                    Password=UserPassword.getText().toString().trim();
                    RePassword=UserReEnterPassword.getText().toString().trim();
                    if(Email.length()>5){
                        if(Password.equals(RePassword) && Password.length()>3){
                            Intent result=new Intent(getActivity(), OTPActivity.class);
                            result.putExtra("email",Email);
                            startActivityForResult(result,124);
                        }else {
                            Toast.makeText(getActivity(),"password is Not Matching",Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(getActivity(),"Email is not correct",Toast.LENGTH_LONG).show();
                    }
                }
            });
            return view;
        }
    public void signIn(){
        if(mAuth.getCurrentUser()!=null){
            user=mAuth.getCurrentUser();
            if(user!=null){
                Log.d("AUTH","userLog in "+user.getEmail());
                Log.d("AUTH","userLog in "+user.getDisplayName());
                Log.d("AUTH","userLog in "+user.getPhotoUrl());
                Log.d("AUTH","userLog in "+user.getUid());
                Log.d("AUTH","userLog in "+user.getProviderId());
                if(user.getProviders().toString().equals("[google.com]")){
                    Log.d("user uid", "onActivityResult: "+user.getUid());
                    Intent result=new Intent(getActivity(), OTPActivity.class);
                    result.putExtra("email",Email);
                    startActivityForResult(result,123);
                }
            }
        }
        else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    0);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                user=mAuth.getCurrentUser();
                if(user!=null ){
                    Log.d("AUTH","userLogout in :"+user.getDisplayName());
                    Log.d("AUTH","userLogout in :"+user.getPhotoUrl());
                    Log.d("AUTH","userLogout in :"+user.getUid());
                    Log.d("CHECK LOGIN TYPE",user.getProviders().toString());
                        Uri pic=user.getPhotoUrl();
                        Log.d("user uid", "onActivityResult: "+user.getUid());
                    Intent result=new Intent(getActivity(), OTPActivity.class);
                    result.putExtra("email",Email);
                    startActivityForResult(result,125);
                        //VollyRequest(user.getEmail(),null, pic.toString(),user.getDisplayName(),user.getUid());
                }
            }else
                Toast.makeText(getActivity(),"can't loged in try again",Toast.LENGTH_LONG).show();
        }else if(requestCode==123){
            pd = ProgressDialog.show(getActivity(), "Signing up", "wait...");
            if(resultCode==RESULT_OK && user !=null)
            VollyRequest(user.getEmail(),null,user.getPhotoUrl().toString(),user.getDisplayName(),user.getUid());
        }else if(requestCode==124){
            if(resultCode==RESULT_OK){
                if(Email.length()>5){
                    if(Password.equals(RePassword) && Password.length()>3){
                        pd = ProgressDialog.show(getActivity(), "Signing up", "wait...");
                        VollyRequest(Email,Password,"",Email,"");
                    }}
                }
        }else if(requestCode==125){
            if(resultCode==RESULT_OK){
                pd = ProgressDialog.show(getActivity(), "Signing up", "wait...");
                VollyRequest(user.getEmail(),null, user.getPhotoUrl().toString(),user.getDisplayName(),user.getUid());
            }
        }
    }
    public void VollyRequest(final String email,final String password,final String photo,final String name ,final String googleuid ){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj);
                            Log.d("JSONOBJECT", obj.toString());

                        } catch (Throwable t) {
                            Log.e("JSONBOJECT", "Could not parse malformed JSON: \"" + response + "\"");
                            pd.dismiss();
                        }
                        Log.d("USERERROR",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                }
                        );
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                if (password!=null){
                    params.put("name",name);
                    params.put("email",email);
                    params.put("password",password);
                    Log.d("JSON send",name +" "+email+" "+password);
                }else{
                    params.put("name",name);
                    params.put("email",email);
                    params.put("googleUID",googleuid);
                    params.put("photo",photo);
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    private void parseJSONResponce(JSONObject response) {
        if(response==null || response.length()==0){
            return;
        }
        boolean result=false;
        String uid=null;
        try{
            Log.d("JSONarray",response.toString());
            result=response.getBoolean("success");
            if(result){
              JSONObject  data= response.getJSONObject("data");

                setdate(data.getString("email"),data.getString("name"),data.getString("uid"),data.getString("pic"),true,Password);
                pd.dismiss();
            }else {
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }
                );
                setdate(null,null,null,null,false,"");
                pd.dismiss();
            }
        }catch (JSONException e){
            e.printStackTrace();
            pd.dismiss();
        }

    }
    public void setdate(String email,String name ,String uid,String profilepic,boolean login,String password){
        if(login){
            saveToPref(getActivity(),PREF_KEY_EMAIL,email);
            saveToPref(getActivity(),PREF_KEY_UID,uid);
            saveToPref(getActivity(),PREF_KEY_USER_PASS,password);
            saveToPref(getActivity(),PREF_KEY_PROFILE_PIC,profilepic);
            String guestid=readPrefes(getActivity(),PREF_KEY_GUESTKEY,"");
            if(guestid.length()>5)
                shiftCart(guestid,uid);
            saveToPref(getActivity(),PREF_KEY_LOGIN,login+"");
            saveToPref(getActivity(),PREF_KEY_NAME,name);
            getActivity().finish();
            Toast.makeText(getActivity(),"you loged in",Toast.LENGTH_LONG).show();
            Intent go=new Intent(getActivity(),Home.class);
            startActivity(go);
        }else{
            saveToPref(getActivity(),KEY_PASSWORD,"");
            saveToPref(getActivity(),PREF_KEY_EMAIL,"LOGIN");
            saveToPref(getActivity(),PREF_KEY_UID,"");
            saveToPref(getActivity(),PREF_KEY_PROFILE_PIC,null);
            saveToPref(getActivity(),PREF_KEY_LOGIN,login+"");
            saveToPref(getActivity(),PREF_KEY_NAME,"");
            Toast.makeText(getActivity(),"email already exist or try again later",Toast.LENGTH_LONG).show();
        }
    }
    private void shiftCart(String guestid, String uid) {
        SendRequest req=new SendRequest(URL_GUESTCART,this,3);
        Map<String,String> params = new HashMap<String, String>();
        params.put("guid",guestid);
        params.put("uid",uid);
        params.put("password","");
        params.put("key","SHIFT");
        req.setParameters(params);
        req.send();
    }

    @Override
    public void responce(String res, int requestCode) {

    }
}