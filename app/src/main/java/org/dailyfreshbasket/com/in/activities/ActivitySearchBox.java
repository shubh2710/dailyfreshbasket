package org.dailyfreshbasket.com.in.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.ListViewSearchAdapter;
import org.dailyfreshbasket.com.in.adapters.RecentListViewSearchAdapter;
import org.dailyfreshbasket.com.in.database.RecentSearchDataBase;
import org.dailyfreshbasket.com.in.informations.Products_in_Search_details;
import org.dailyfreshbasket.com.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_SEARCH;


public class ActivitySearchBox extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, View.OnClickListener {

    ListView list,recentList;
    ListViewSearchAdapter Listadapter;
    RecentListViewSearchAdapter RecentListadapter;
    SearchView editsearch;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    ProgressBar prog;
    RecentSearchDataBase mydb;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private List<Products_in_Search_details> product_List= Collections.emptyList();
    ArrayList<String> arraylist = new ArrayList<String>();
    ArrayList<String> Recentarraylist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_box);
        setTitle("Sort");
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        context=this;
         prog = (ProgressBar) findViewById(R.id.progressBar3);
        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setFocusable(true);
        editsearch.setIconified(false);
        editsearch.requestFocusFromTouch();
        editsearch.setQueryHint("type key to search products");
        editsearch.setOnQueryTextListener(this);
        mydb=new RecentSearchDataBase(this,"recent_search_db");
        mydb.open();
        Recentarraylist=mydb.getSearchsList();
        mydb.close();
        Log.d("RECENT_SEARCH",Recentarraylist.toString());
        recentList=(ListView) findViewById(R.id.SearchView_RecentSerchlist);
        RecentListadapter = new RecentListViewSearchAdapter(this, Recentarraylist,editsearch);
        recentList.setAdapter(RecentListadapter);

        list = (ListView) findViewById(R.id.SearchView_listview);
        Listadapter = new ListViewSearchAdapter(this, arraylist,editsearch);
        list.setAdapter(Listadapter);
        list.setOnItemClickListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("SEARCH",query);
        mydb.open();
        mydb.createEntry(query);
        mydb.close();
        Intent searchActivity=new Intent(this,SearchActivity.class);
        searchActivity.putExtra("search",query);
        startActivity(searchActivity);
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length()>0){
        prog.setVisibility(View.VISIBLE);
        recentList.setVisibility(View.GONE);
        }
        else{
            prog.setVisibility(View.GONE);
            arraylist.clear();
            Listadapter.notifyDataSetChanged();
        }
        search(newText,"");
        return false;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent searchActivity=new Intent(this,SearchActivity.class);
        searchActivity.putExtra("search",editsearch.getQuery());
        startActivity(searchActivity);
    }

    @Override
    public void onClick(View v) {

        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setPrompt("Scan a barcode or QRcode");

        integrator.setOrientationLocked(false);

        integrator.initiateScan();

    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Intent searchActivity=new Intent(this,SearchActivity.class);
                searchActivity.putExtra("bar",result.getContents());
                startActivity(searchActivity);

            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }
    public void search(final String search,final String type){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            prog.setVisibility(View.GONE);
                            Log.d("JSON_Search",response.toString());
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj,search);
                        } catch (Exception t) {
                            prog.setVisibility(View.GONE);
                            Log.e("JSON_Search", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prog.setVisibility(View.GONE);
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("search","search");
                //params.put("data",search);
                params.put("key","TAGS");
                params.put("searchkey",search);
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
            if(result){
                arraylist.clear();
                JSONArray Productarray=response.getJSONArray("data");
                for(int i=0;i<Productarray.length();i++) {
                    String list= Productarray.getString(i);
                    arraylist.add(list);

                }
                Listadapter.notifyDataSetChanged();
            }
            else {

                }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}