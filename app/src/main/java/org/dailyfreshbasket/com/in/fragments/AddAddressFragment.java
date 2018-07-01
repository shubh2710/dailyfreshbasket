package org.dailyfreshbasket.com.in.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.activities.AddLandmarkRequest;
import org.dailyfreshbasket.com.in.models.addressModel;
import org.dailyfreshbasket.com.in.models.selectedAddressModel;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_ADDRESS;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;


public class AddAddressFragment extends Fragment implements getResponce ,AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText name,mobile,line,state,city,pin;
    private Spinner land;
    private Button save;
    private TextView address,tv_requestland;
    private addressModel a;
    private int selectedLand=0;
    private int chrage=0;
    private ProgressDialog pd;
    private ArrayAdapter<String> adapter;
    private ArrayList<selectedAddressModel> lands;
    private String list[];
    public AddAddressFragment() {
        // Required empty public constructor
    }
    public static AddAddressFragment newInstance(String param1, String param2) {
        AddAddressFragment fragment = new AddAddressFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_address, container, false);

            lands=new ArrayList();
            list=new String[1];
            list[0]="";
            getAddressList();
            pd=ProgressDialog.show(getActivity(), "Adding new address", "wait...");
            pd.setCancelable(true);
            tv_requestland=(TextView)view.findViewById(R.id.tv_requestland);
            name=(EditText) view.findViewById(R.id.et_add_name);
            mobile=(EditText) view.findViewById(R.id.et_add_mobile);
            line=(EditText) view.findViewById(R.id.et_add_line1);
            pin=(EditText) view.findViewById(R.id.et_add_pin);
            land=(Spinner) view.findViewById(R.id.s_spinner_landmark);
            state=(EditText) view.findViewById(R.id.et_add_state);
            city=(EditText) view.findViewById(R.id.et_add_city);
            save=(Button) view.findViewById(R.id.b_add_save_address);
            address=(TextView)view.findViewById(R.id.tv_add_address);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,list);
            land.setOnItemSelectedListener(this);

            land.setAdapter(adapter);
            tv_requestland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent req=new Intent(getActivity(),AddLandmarkRequest.class);
                startActivityForResult(req,122);
            }
        });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pinText=pin.getText().toString().trim();
                    String nameText=name.getText().toString().trim();
                    String mobileText=mobile.getText().toString().trim();
                    String lineText=line.getText().toString().trim();
                    String landText=lands.get(selectedLand).getLand().trim();
                    String stateText=state.getText().toString().trim();
                    String cityText=city.getText().toString().trim();
                    if(nameText.length()>2 && mobileText.length()>9 && lineText.length()>5){
                        a=new addressModel();
                        pd=ProgressDialog.show(getActivity(), "Adding new address", "wait...");
                        pd.setCancelable(true);
                        a.setState(stateText);
                        a.setPin(pinText);
                        a.setType("1");
                        a.setName(nameText);
                        a.setMobile(mobileText);
                        a.setLand(landText);
                        a.setStreet(lineText);
                        a.setCity(cityText);
                        a.setCharge(chrage);
                        addAddress(a);
                    }else{
                        Toast.makeText(getActivity(),"Please Add valid Name and Mobile no",Toast.LENGTH_LONG).show();
                    }

                }
            });
        return view;
    }

    private String[] getAddressArray()
    {
       list=new String [lands.size()];
        for(int i=0;i<lands.size();i++){
            list[i]=lands.get(i).getLand();
            Log.d("address", "getAddressArray: "+lands.get(i).getLand());
        }
        return list;
    }

    private void getAddressList() {
        String uid=readPrefes(getActivity(),PREF_KEY_UID,"");
        SendRequest req=new SendRequest(URL_ADDRESS,this,2);
        Map<String,String> params = new HashMap<String, String>();
        params.put("password","");
        params.put("uid",uid);
        params.put("key","LIST");
        req.setParameters(params);
        req.send();
    }
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(lands.size()>0){
            selectedLand=position;
            chrage=lands.get(position).getCharge();
            city.setEnabled(false);
            state.setEnabled(false);
            pin.setEnabled(false);
            city.setText(lands.get(position).getCity());
            state.setText(lands.get(position).getState());
            pin.setText(lands.get(position).getPin());

        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void addAddress(addressModel a){
        String uid=readPrefes(getActivity(),PREF_KEY_UID,"");
        Log.d("uid", "getAddress uid: "+uid  +" "+a.toString());
        SendRequest req=new SendRequest(URL_ADDRESS,this,1);
        Map<String,String> params = new HashMap<String, String>();
        params.put("password","");
        params.put("uid",uid);
        params.put("key","ADD");
        params.put("name",a.getName());
        params.put("type",a.getType());
        params.put("charge",a.getCharge()+"");
        params.put("street",a.getStreet());
        params.put("landmark",a.getLand());
        params.put("city",a.getCity());
        params.put("mobile",a.getMobile());
        params.put("state",a.getState());
        params.put("pin",a.getPin());
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
        switch(requestCode){
            case 1:
                Log.d("ADD Address", "responce: 1 "+res);
                try {
                    JSONObject obj=new JSONObject(res);
                    boolean result=obj.getBoolean("success");
                    if(result){
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        transaction.replace(R.id.UserDetailsesFragrmentSwitcher, new Fragment_MyAddress());
                        //transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try{
                    Log.d("ADD Address", "responce: 2 "+res);
                    JSONObject obj=new JSONObject(res);
                    boolean result=obj.getBoolean("success");
                    if(result){
                        if(!obj.isNull("data")){
                            JSONArray list=obj.getJSONArray("data");
                            selectedAddressModel model;
                            for(int i=0;i<list.length();i++){
                                model=new selectedAddressModel();
                                JSONObject address=list.getJSONObject(i);
                                model.setCity(address.getString("city"));
                                model.setLand(address.getString("name"));
                                model.setLmid(address.getString("lmid"));
                                model.setPin(address.getString("pin"));
                                model.setState(address.getString("state"));
                                model.setCharge(address.getInt("charge"));
                                lands.add(model);
                            }
                            getAddressArray();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,list);
                    land.setAdapter(adapter);
                } catch(JSONException ee) {
                    ee.printStackTrace();
                }
                break;
        }

    }
}
