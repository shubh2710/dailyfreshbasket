package org.dailyfreshbasket.co.in.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.models.addressModel;
import org.dailyfreshbasket.co.in.models.selectedAddressModel;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_ADDRESS;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class AddAddressActivity extends AppCompatActivity implements getResponce, AdapterView.OnItemSelectedListener {
    private EditText name, mobile, line, state, city, pin;
    private Spinner landSpinner;
    private Button save;
    private TextView address, tv_requestland;
    private addressModel a;
    private int charge = 0;
    private int selectedLand = 0;
    private ProgressDialog pd;
    private ArrayAdapter<String> adapter;
    private addressModel adrs;
    private ArrayList<selectedAddressModel> lands;
    private String list[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_address);
        lands = new ArrayList<>();
//        list = new String[1];
//        list[0] = "Please select area";
        getAddressList();
        pd = ProgressDialog.show(this, "Adding new address", "wait...");
        pd.setCancelable(true);
        tv_requestland = (TextView) findViewById(R.id.tv_requestland);
        name = (EditText) findViewById(R.id.et_add_name);
        mobile = (EditText) findViewById(R.id.et_add_mobile);
        line = (EditText) findViewById(R.id.et_add_line1);
        pin = (EditText) findViewById(R.id.et_add_pin);
        landSpinner = (Spinner) findViewById(R.id.s_spinner_landmark);
        state = (EditText) findViewById(R.id.et_add_state);
        city = (EditText) findViewById(R.id.et_add_city);
        save = (Button) findViewById(R.id.b_add_save_address);
        address = (TextView) findViewById(R.id.tv_add_address);
        try {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
            landSpinner.setOnItemSelectedListener(this);
            landSpinner.setAdapter(adapter);
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
        tv_requestland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent req = new Intent(AddAddressActivity.this, AddLandmarkRequest.class);
                startActivityForResult(req, 122);
            }
        });
        landSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLand = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String pinText = pin.getText().toString().trim();
                    String nameText = name.getText().toString().trim();
                    String mobileText = mobile.getText().toString().trim();
                    String lineText = line.getText().toString().trim();
                    String landText = lands.get(selectedLand).getLand().toString().trim();
                    String stateText = state.getText().toString().trim();
                    String cityText = city.getText().toString().trim();
                    if (nameText.length() > 2 && mobileText.length() == 10 && lineText.length() > 5 && pinText.length()==6) {
                        a = new addressModel();
                        pd = ProgressDialog.show(AddAddressActivity.this, "Adding new address", "wait...");
                        pd.setCancelable(true);
                        a.setState(stateText);
                        a.setPin(pinText);
                        a.setType("1");
                        a.setCharge(lands.get(selectedLand).getCharge());
                        a.setName(nameText);
                        a.setMobile(mobileText);
                        a.setLand(landText);
                        a.setStreet(lineText);
                        a.setCity(cityText);
                        addAddress(a);
                        adrs = a;

                    } else {
                        Toast.makeText(AddAddressActivity.this, "Please Add valid Name, Mobile and atleast 5 characters in address", Toast.LENGTH_LONG).show();
                    }
                } catch (IndexOutOfBoundsException ine) {
                    ine.printStackTrace();
                }
            }
        });
    }

    private String[] getAddressArray() {
        try {
            list = new String[lands.size()];
            for (int i = 0; i < lands.size(); i++) {
                list[i] = lands.get(i).getLand();
                Log.d("address", "getAddressArray: " + lands.get(i).getLand());
            }
        }catch (NullPointerException ne){
            ne.printStackTrace();
            Log.d("Get_Add",ne.getMessage());
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "request added we will get back to you soon.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getAddressList() {
        String uid = readPrefes(this, PREF_KEY_UID, "");
        SendRequest req = new SendRequest(URL_ADDRESS, this, 2);
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", "");
        params.put("uid", uid);
        params.put("key", "LIST");
        req.setParameters(params);
        req.send();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (lands.size() > 0) {
            selectedLand = position;
            city.setEnabled(false);
            state.setEnabled(false);
            pin.setEnabled(false);
            charge = lands.get(position).getCharge();
            city.setText(lands.get(position).getCity());
            state.setText(lands.get(position).getState());
            pin.setText(lands.get(position).getPin());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (lands.size() > 0) {
        selectedLand = 0;
        city.setEnabled(false);
        state.setEnabled(false);
        pin.setEnabled(false);
        charge = lands.get(0).getCharge();
        city.setText(lands.get(0).getCity());
        state.setText(lands.get(0).getState());
        pin.setText(lands.get(0).getPin());
        }
    }

    public void addAddress(addressModel a) {
        String uid = readPrefes(this, PREF_KEY_UID, "");
        Log.d("uid", "getAddress uid: " + uid + " " + a.toString());
        SendRequest req = new SendRequest(URL_ADDRESS, this, 1);
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", "");
        params.put("uid", uid);
        params.put("key", "ADD");
        params.put("type", a.getType());
        params.put("name", a.getName());
        params.put("charge", a.getCharge() + "");
        params.put("street", a.getStreet());
        params.put("landmark", a.getLand());
        params.put("city", a.getCity());
        params.put("mobile", a.getMobile());
        params.put("state", a.getState());
        params.put("pin", a.getPin());
        req.setParameters(params);
        req.send();
    }

    public static String readPrefes(Context context, String prefesName, String defaultValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName, defaultValue);
    }

    @Override
    public void responce(String res, int requestCode) {

        pd.dismiss();
        switch (requestCode) {
            case 1:
                Log.d("ADD Address", "responce: 1 " + res);
                try {
                    JSONObject obj = new JSONObject(res);
                    boolean result = obj.getBoolean("success");
                    if (result) {
                        adrs.getAdi();
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Log.d("ADD Address", "responce: 2 " + res);
                    JSONObject obj = new JSONObject(res);
                    boolean result = obj.getBoolean("success");
                    if (result) {
                        if (!obj.isNull("data")) {
                            JSONArray list = obj.getJSONArray("data");
                            selectedAddressModel model;
                            for (int i = 0; i < list.length(); i++) {
                                model = new selectedAddressModel();
                                JSONObject address = list.getJSONObject(i);
                                model.setCity(address.getString("city"));
                                model.setLand(address.getString("name"));
                                model.setLmid(address.getString("lmid"));
                                model.setPin(address.getString("pin"));
                                model.setCharge(address.getInt("charge"));
                                model.setState(address.getString("state"));
                                lands.add(model);
                            }
                            getAddressArray();

                        }
                    }
                    adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
                    adapter.notifyDataSetChanged();
                    landSpinner.setAdapter(adapter);
                } catch (JSONException ee) {
                    ee.printStackTrace();
                }
                break;
        }

    }
}