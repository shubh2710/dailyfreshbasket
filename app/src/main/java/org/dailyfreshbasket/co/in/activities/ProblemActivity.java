package org.dailyfreshbasket.co.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_COMPLAIN;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_EMAIL;

public class ProblemActivity extends AppCompatActivity implements getResponce {

    private Button submit;
    private EditText msg,orderNo;
    private Spinner subject;
    private String subjectText=null;
    private ProgressDialog pd;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("Custmore Support");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        submit=(Button)findViewById(R.id.b_problem_submit);
        msg=(EditText)findViewById(R.id.et_problem_reson);
        orderNo=(EditText)findViewById(R.id.et_problem_orderNo);
        subject=(Spinner) findViewById(R.id.s_problem);
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectText =subject.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderNo.getText().toString().length()>2 && subjectText!=null){
                    pd=ProgressDialog.show(ProblemActivity.this, "Adding new address", "wait...");
                    pd.setCancelable(true);
                    sendComplain();
                }else{
                    Toast.makeText(ProblemActivity.this,"please select subject and enter valid order id",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sendComplain(){
        String email=readPrefes(this,PREF_KEY_EMAIL,"");
        SendRequest req=new SendRequest(URL_COMPLAIN,this,2);
        Map<String,String> params = new HashMap<String, String>();
        params.put("password","");
        params.put("uid","");
        params.put("email",email);
        params.put("order_no",orderNo.getText().toString());
        params.put("subject",subjectText);
        params.put("reason",msg.getText().toString());
        req.setParameters(params);
        req.send();
    }
    @Override
    public void responce(String res, int requestCode) {
        pd.dismiss();
        Log.d("complain", "responce: "+res);
        try {
            JSONObject responce=new JSONObject(res);
            if(responce.getBoolean("success")){
                Toast.makeText(this,"your complain successfully registered",Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
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
