package org.dailyfreshbasket.co.in.activities;

import android.app.ProgressDialog;
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

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_LANDREQUEST;

public class AddLandmarkRequest extends AppCompatActivity implements getResponce {

    private EditText et_area,et_land,et_pin;
    ProgressDialog pd;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_landmark_request);
        et_area=(EditText)findViewById(R.id.et_add_area);
        et_land=(EditText)findViewById(R.id.et_add_landmark);
        et_pin=(EditText)findViewById(R.id.et_add_pin);
        add=(Button) findViewById(R.id.b_add_land_request);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area=et_area.getText().toString().trim();
                String land =et_land.getText().toString().trim();
                String p=et_pin.getText().toString().trim();
                if(area.length()>5){
                    if(land.length()>5){
                        if(p.length()>5){
                            sendRequest(land,area,p);
                        }else{
                            Toast.makeText(AddLandmarkRequest.this,"pin length more then 5",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AddLandmarkRequest.this,"landmark length more then 5",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddLandmarkRequest.this,"area length more then 5",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void sendRequest(String land, String area, String p) {
        pd= ProgressDialog.show(this, "Requesting", "wait...");
        pd.setCancelable(true);
        SendRequest req=new SendRequest(URL_LANDREQUEST,this,3);
        Map<String,String> params = new HashMap<String, String>();
        params.put("area",area);
        params.put("land",land);
        params.put("pin",p);
        req.setParameters(params);
        req.send();
    }

    @Override
    public void responce(String res, int requestCode) {
        Log.d("request", "responce: "+res);
        pd.dismiss();
        try {
            JSONObject responce=new JSONObject(res);
            if(responce.getBoolean("success")){
                Toast.makeText(this,"request added",Toast.LENGTH_SHORT).show();
                finish();
                setResult(RESULT_OK);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
