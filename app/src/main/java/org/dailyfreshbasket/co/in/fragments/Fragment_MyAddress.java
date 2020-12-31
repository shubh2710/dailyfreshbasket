package org.dailyfreshbasket.co.in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.RecycleViewAdapterAddressList;
import org.dailyfreshbasket.co.in.adapters.RecyclerTouchListner;
import org.dailyfreshbasket.co.in.models.addressModel;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.myInterface.setResultIntent;
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


public class Fragment_MyAddress extends Fragment implements getResponce, setResultIntent {

    private RecyclerView recyclerview;
    public RecycleViewAdapterAddressList adaptor;
    private Context context;
    private ArrayList<addressModel> data= new ArrayList();
    private ProgressDialog pd;

    public  Fragment_MyAddress(){

    }
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_my_address, viewGroup, false);
            pd = ProgressDialog.show(getActivity(), "Loading address", "wait...");
            pd.setCancelable(true);
            getAddress();
            recyclerview=(RecyclerView)view.findViewById(R.id.rc_address_list);
            data=new ArrayList<>();
            data.clear();
            adaptor=new RecycleViewAdapterAddressList(getActivity(),this,data);
            recyclerview.setAdapter(adaptor);
            recyclerview.setFocusable(false);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerview.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), recyclerview, new RecyclerTouchListner.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                }
                @Override
                public void onLongClick(View view, int position) {
                }
            }));
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SetFragment(new AddAddressFragment());
                }
            });
            return view;
        }
    private void SetFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.UserDetailsesFragrmentSwitcher, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
        public void getAddress(){
            String uid=readPrefes(getActivity(),PREF_KEY_UID,"");
            Log.d("uid", "getAddress uid: "+uid);
            SendRequest req=new SendRequest(URL_ADDRESS,this,1);
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            params.put("key","GET");
            req.setParameters(params);
            req.send();
        }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }

    @Override
    public void responce(String res, int requestCode) {
        Log.d("Address", "responce: "+res);
        jsonparse(res);
        pd.dismiss();
    }
    public void jsonparse(String res){
        try {
            JSONObject obj=new JSONObject(res);
            boolean result=obj.getBoolean("success");
            if(result){
                if(!obj.isNull("data")){
                    JSONArray add=obj.getJSONArray("data");
                    for(int i=0;i<add.length();i++){
                        JSONObject list=add.getJSONObject(i);
                        addressModel address=new addressModel();
                        address.setAdi(list.getString("aid"));
                        address.setCity(list.getString("city"));
                        address.setLand(list.getString("landmark"));
                        address.setMobile(list.getString("mobile"));
                        address.setName(list.getString("name"));
                        address.setPin(list.getString("pincode"));
                        address.setState(list.getString("state"));
                        address.setStreet(list.getString("address"));
                        address.setType(list.getString("type"));
                        address.setUid(list.getString("uid"));
                        data.add(address);
                        adaptor.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setResultData(int position) {

    }
}