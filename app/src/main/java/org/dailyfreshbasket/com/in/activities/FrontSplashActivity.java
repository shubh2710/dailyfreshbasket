package org.dailyfreshbasket.com.in.activities;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.networks.VollyConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_BANNER;

public class FrontSplashActivity extends AppCompatActivity {

    ProgressBar prog;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_splash);
        prog = (ProgressBar) findViewById(R.id.pb_frontSplashPage);
        vollyConnection= VollyConnection.getsInstance();
        final FrameLayout rl=(FrameLayout)findViewById(R.id.ll_frontpage);
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        prog.setVisibility(View.VISIBLE);
      /*  rl.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(FrontSplashActivity.this)
                        .radius(10)
                        .sampling(2)
                        .async()
                        .animate(500)
                        .onto(rl);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetOn();
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkNet("a","");
                }
            }, SPLASH_TIME_OUT);
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
    public void checkNet(final String search,final String type){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BANNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("JSON_Search",response.toString());
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj,search);
                        } catch (Exception t) {
                            Log.e("JSON_Search", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkNet("a","");
                                Log.d("Check connection","check.....");
                            }
                        }, SPLASH_TIME_OUT);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("search","search");
                //params.put("data",search);
                params.put("email","email");
                params.put("password","password");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    private void parseJSONResponce(JSONObject response,String search) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Boolean result=response.getBoolean("success");
            if(result!=null){
                Intent i = new Intent(FrontSplashActivity.this, Home.class);
                startActivity(i);
                finish();
            }
            else {
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
