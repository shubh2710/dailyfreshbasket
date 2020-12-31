package org.dailyfreshbasket.co.in.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.ForgetPasswordActivity;
import org.dailyfreshbasket.co.in.activities.Home;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_GUESTCART;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_LOGIN;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.*;

public class Fragment_LogInForm extends Fragment implements View.OnClickListener, getResponce {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private EditText uemail;
    private EditText upassword;
    private Button loginGoogle;
    private Button login;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private String getemail;
    private String getpassword;
    private TextView forget;
    private String getname;
    private String getProfilePic;
    private String uid = null;
    private ProgressDialog pd = null;
    private static int RC_SIGH_IN = 0;
    private static String TAG = "Main Activity";
    private GoogleApiClient mGoogleAppClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListnear;

    private String mParam2;

    public Fragment_LogInForm() {
        // Required empty public constructor
    }

    public static Fragment_LogInForm newInstance(String param1, String param2) {
        Fragment_LogInForm fragment = new Fragment_LogInForm();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_login_layout, viewGroup, false);
        FirebaseApp.initializeApp(getActivity());
        forget = (TextView) view.findViewById(R.id.tv_forgetpassword);
        forget.setOnClickListener(this);
        uemail = (EditText) view.findViewById(R.id.et_login_email);
        upassword = (EditText) view.findViewById(R.id.et_login_password);
        login = (Button) view.findViewById(R.id.bSignIn_user_SignInbutton);
        loginGoogle = (Button) view.findViewById(R.id.sign_in_button);
        vollyConnection = VollyConnection.getsInstance();
        requestQueue = VollyConnection.getsInstance().getRequestQueue();
        mAuth = FirebaseAuth.getInstance();
        //googleLoginSetup();
        pd = new ProgressDialog(getActivity());
        view.findViewById(R.id.sign_in_button).setOnClickListener(this);
        login.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
        return view;
    }

    public void VollyRequest(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj);
                            Log.d("JSONOBJECTLOGIN", obj.toString());

                        } catch (Throwable t) {
                            Log.e("JSONBOJECTLOGIN", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("googleUID", password);
//                params.put("password",password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }

    public void VollyRequestForGmailLogin(final String email, final String guid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj);
                            Log.d("JSONOBJECTGmailLOGIN", obj.toString());
                        } catch (Throwable t) {
                            Log.d("JSONBOJECTGmailLOGIN", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JSONOBJECTGmailLOGIN", error.toString());
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("googleUID", guid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }

    public static String readPrefes(Context context, String prefesName, String defaultValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName, defaultValue);
    }

    public static void saveToPref(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sheredPreference = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sheredPreference.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    private void parseJSONResponce(JSONObject response) {
        if (response == null || response.length() == 0) {
            return;
        }
        boolean result = false;
        JSONObject data = null;
        try {
            Log.d("JSONarray", response.toString());
            result = response.getBoolean("success");
            pd.dismiss();
            if (result) {
                if (!response.isNull("data"))
                    data = response.getJSONObject("data");
                setdate(data.getString("email"), data.getString("name"), data.getString("uid"), data.getString("googleUid"), data.getString("pic"), true, getpassword);
            } else {
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }
                );
                setdate(null, null, null, null, null, false, getpassword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGH_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                ;
                if (user != null) {
                    Log.d("AUTH", "userLogout in" + user.getDisplayName());
                    Log.d("AUTH", "userLogout in" + user.getPhotoUrl());
                    Log.d("AUTH", "userLogout in" + uid);
                    getemail = user.getEmail();
                    uid = user.getUid();
                    getname = user.getDisplayName();
                    Uri uri = user.getPhotoUrl();
                    getProfilePic = String.valueOf(uri);
                    Log.d("CHECK LOGIN TYPE", user.getProviders().toString());
                    if (user.getProviders().toString().equals("[google.com]")) {
                        VollyRequestForGmailLogin(getemail, uid);
                    } else if (user.getProviders().toString().equals("[facebook.com]")) {
                        VollyRequestForGmailLogin(getemail, uid);
                    }
                }
            } else
                Toast.makeText(getActivity(), "can't loged in try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSignIn_user_SignInbutton:
                getemail = uemail.getText().toString();
                getpassword = upassword.getText().toString();
                if (getemail.length() > 5 && getpassword.length() > 5) {
                    pd = ProgressDialog.show(getActivity(), "Login with DailyFreshBask.com", "wait...");
                    pd.setCancelable(true);
                    VollyRequest(getemail, getpassword);
                } else
                    Toast.makeText(getActivity(), "email and password can not be empty", Toast.LENGTH_LONG).show();
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.tv_forgetpassword:
                Intent forget = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(forget);
                break;
        }
    }

    public void signIn() {
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                Log.d("AUTH", "userLogout in " + user.getEmail());
                Log.d("AUTH", "userLogout in " + user.getDisplayName());
                Log.d("AUTH", "userLogout in " + user.getPhotoUrl());
                Log.d("AUTH", "userLogout in " + user.getUid());
                Log.d("AUTH", "userLogout in " + user.getProviderId());
                getemail = user.getEmail();
                uid = user.getUid();
                getname = user.getDisplayName();
                Uri uri = user.getPhotoUrl();
                getProfilePic = String.valueOf(uri);
                if (user.getProviders().toString().equals("[google.com]")) {
                    VollyRequestForGmailLogin(getemail, uid);
                } else if (user.getProviders().toString().equals("[facebook.com]")) {
                    VollyRequestForGmailLogin(getemail, uid);
                }
            }
        } else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGH_IN);
        }
    }

    public void setdate(String email, String name, String uid, String guid, String profilepic, boolean login, String password) {
        if (login) {
            saveToPref(getActivity(), PREF_KEY_EMAIL, email);
            saveToPref(getActivity(), PREF_KEY_UID, uid);
            saveToPref(getActivity(), PREF_KEY_GOOGLE_UID, guid);
            saveToPref(getActivity(), PREF_KEY_USER_PASS, password);
            saveToPref(getActivity(), PREF_KEY_PROFILE_PIC, profilepic);
            saveToPref(getActivity(), PREF_KEY_LOGIN, login + "");
            String guestid = readPrefes(getActivity(), PREF_KEY_GUESTKEY, "");
            if (guestid.length() > 3)
                shiftCart(guestid, uid);
            saveToPref(getActivity(), PREF_KEY_GUESTKEY, null);
            saveToPref(getActivity(), PREF_KEY_NAME, name);

            getActivity().finish();
            Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();
            Intent go = new Intent(getActivity(), Home.class);
            startActivity(go);
            getActivity().finish();
        } else {
//            Toast.makeText(getActivity(),"Create Account First",Toast.LENGTH_LONG).show();
            saveToPref(getActivity(), PREF_KEY_EMAIL, "not giving");
            saveToPref(getActivity(), PREF_KEY_UID, "");
            saveToPref(getActivity(), PREF_KEY_USER_PASS, "");
            saveToPref(getActivity(), PREF_KEY_PROFILE_PIC, null);
            saveToPref(getActivity(), PREF_KEY_LOGIN, login + "");
            saveToPref(getActivity(), PREF_KEY_NAME, "not giving");
            Toast.makeText(getActivity(), "Log in error, Are you registered user?", Toast.LENGTH_LONG).show();
        }
    }

    private void shiftCart(String guestid, String uid) {
        SendRequest req = new SendRequest(URL_GUESTCART, this, 3);
        Map<String, String> params = new HashMap<String, String>();
        params.put("guid", guestid);
        params.put("uid", uid);
        params.put("password", "");
        params.put("key", "SHIFT");
        req.setParameters(params);
        req.send();
    }

    @Override
    public void responce(String res, int requestCode) {
        Log.d("shift", "responce: " + res);
    }
}