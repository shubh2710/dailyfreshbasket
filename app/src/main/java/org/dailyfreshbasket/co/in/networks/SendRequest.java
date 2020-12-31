package org.dailyfreshbasket.co.in.networks;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.dailyfreshbasket.co.in.myInterface.getResponce;

import java.util.HashMap;
import java.util.Map;

public class SendRequest {
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private String URL="http://securemyfile.in/apis/Userlogin.php?";
    private getResponce res;
    public Map<String,String> map;
    private  int requestCode;
    public SendRequest(String url, getResponce res, int requestCode) {
        this.URL=url;
        this.requestCode=requestCode;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        this.res=res;
    }
    public void setParameters(Map<String,String> map){
            this.map=map;
    }
    public void send(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        res.responce(response,requestCode);
                        /*try {
                            Log.d("JSON_result",response.toString());

                            JSONObject obj = new JSONObject(response);

                        } catch (Exception t) {
                            Log.e("JSON_result", t.toString() +":"+ response );
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //params.put("email",Sendemail);
                //params.put("password",Sendpassword);
                params=map;
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
}