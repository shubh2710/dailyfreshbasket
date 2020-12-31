package org.dailyfreshbasket.co.in.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.RecycleViewAdapterMySearchActivity;
import org.dailyfreshbasket.co.in.informations.MinMax;
import org.dailyfreshbasket.co.in.informations.Products_in_Search_details;
import org.dailyfreshbasket.co.in.informations.filtersortInformation;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.*;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;
import static org.dailyfreshbasket.co.in.informations.UserDetailKeys.KEYS.KEY_PASSWORD;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerview;
    public RecycleViewAdapterMySearchActivity adaptor;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private boolean isGrid=true;
    private boolean isFilter=false;
    private boolean isSort=false;
    private int sortType=0;
    private int page=1;
    private ImageButton layout;
    private Button sort,filter;
    private TextView Searchresult;
    private List<Products_in_Search_details> product_List= Collections.emptyList();
    private ProgressDialog pd;
    private boolean canScrol=true;
    private filtersortInformation filters;
    private String queary="",bar=null,KeySearch="";
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        context=this;
        toolbar=(Toolbar)findViewById(R.id.appbar);
        Log.d("INTENT_SEARCH",queary);
//        Toast.makeText(context, ""+getIntent().getStringExtra("search"), Toast.LENGTH_LONG).show();

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview=(RecyclerView)findViewById(R.id.rc_ProductSerach);
        product_List=new ArrayList<>();
        product_List.clear();
        setrecyclerView(isGrid);
        recyclerview.setFocusable(false);
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLastItemDisplaying(recyclerView) && canScrol) {
                    canScrol=false;
                    Log.d("scrolle", "onScrollStateChanged: requesting page"+page);
                    search(KeySearch,"search");
                    pd = ProgressDialog.show(SearchActivity.this, "Loading search", "wait...");
                    pd.setCancelable(true);
                }
            }
        });
        sort=(Button)findViewById(R.id.b_searchview_sort);
        filter=(Button)findViewById(R.id.b_searchview_filter);
        layout=(ImageButton)findViewById(R.id.b_searchview_layout);
        Searchresult=(TextView)findViewById(R.id.tv_activitySerch_result);
        sort.setOnClickListener(this);
        layout.setOnClickListener(this);
        filter.setOnClickListener(this);
        try{
            if(getIntent().getStringExtra("search").equals("Recently Watched")){
                queary = "Recenty,Watched";
            }else if(getIntent().getStringExtra("search").equals("Newly Added")){
                queary = "Recent";
            }else {
                queary = getIntent().getStringExtra("search");
            }
            Log.d("INTENT_SEARCH",queary);
        }catch (Exception e){
        }
        if(bar!=null){
            search(bar,"bar");
            pd = ProgressDialog.show(this, "Loading search", "wait...");
            pd.setCancelable(true);
            setTitle(bar.replace("Recenty,Watched","Recently Watched"));
            Searchresult.setText("Searching for bar code "+bar);
        }else{
            if(queary!=null && queary.equals("Recent")){
                sortType=4;
            }else if(queary.equals("High Rating")){
                //sortType=3;
            }
            pd = ProgressDialog.show(this, "Loading search", "wait...");
            pd.setCancelable(true);
            KeySearch=queary.replace(" ",",");
            KeySearch= queary.replace("All","");
            search(KeySearch,"search");
            Log.d("search quary", "onCreate: "+KeySearch);
            setTitle(queary.replace("Recenty,Watched","Recently Watched"));
            Searchresult.setText("searching for "+queary);
        }
    }
    private void setrecyclerView(Boolean layout) {
        GridLayoutManager glm;
        if(layout){
            adaptor=new RecycleViewAdapterMySearchActivity(this,product_List,true);
            recyclerview.setAdapter(adaptor);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        }
        else{
            adaptor=new RecycleViewAdapterMySearchActivity(this,product_List,false);
            recyclerview.setAdapter(adaptor);
            glm = new GridLayoutManager(this, 2);
            recyclerview.setLayoutManager(glm);
        }
    }
    String URL;
    public void search(final String search,final String type){
//        if(search.equals("RecentyWatched")) {
//             URL = URL_GET_PRODUCT;
//        }else{
//            URL = URL_SEARCH;
//        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("JSON_Search",response.toString());
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj,search);
                        } catch (Exception t) {
                            pd.dismiss();
                            Searchresult.setText("oops something is not correct");
                            Log.e("JSON_Search", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        Searchresult.setText("slow net connection");
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
               // params.put("search","search");
                //params.put("data",search);

                String uid=readPrefes(SearchActivity.this,PREF_KEY_UID,"Please Login");
                String pass=readPrefes(SearchActivity.this,KEY_PASSWORD,"Please Login");

                    params.put("key", "SEARCH");
                    params.put("searchkey", search);
                    params.put("sort", sortType + "");
                    params.put("page", page + "");

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
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }
    private void parseJSONResponce(JSONObject response,String search) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Log.d("search", "parseJSONResponce: "+response);
            Boolean result=response.getBoolean("success");
            if(result){
               // product_List.clear();
                if(!response.isNull("data")){
                    page++;
                    canScrol=true;
                    JSONArray Productarray=response.getJSONArray("data");
                    for(int i=0;i<Productarray.length();i++) {
                        Products_in_Search_details prouctDetails=new Products_in_Search_details();
                        JSONObject currentObjecr = Productarray.getJSONObject(i);
                        String product_id = currentObjecr.getString("pid");
                        String product_name = currentObjecr.getString("name");
                        String product_mrp = currentObjecr.getString("mrp");
                        String discount=currentObjecr.getString("discount");
                        String sub=currentObjecr.getString("feature");
                        String product_actualproce = currentObjecr.getString("actualPrice");
                        String product_stockquantity = currentObjecr.getString("quqntity");
                        String product_rating = currentObjecr.getString("rating");
                        String product_offer = currentObjecr.getString("offer");
                        String createDate=currentObjecr.getString("createDate");
                        ArrayList<String> pic=new ArrayList();
                        if(!currentObjecr.isNull("url")){
                            JSONArray product_pic=currentObjecr.getJSONArray("url");
                            for (int k = 0; k < product_pic.length(); k++) {
                                pic.add(product_pic.getString(k));
                            }
                        }
                        Searchresult.setText("Total "+(i+1)+" product found in search matching to "+"'"+search+"'");
                        if(product_id != null){
                            prouctDetails.add_product(discount,sub,product_name
                                    ,product_id
                                    ,product_actualproce
                                    ,product_mrp
                                    ,""
                                    ,product_stockquantity
                                    ,""
                                    ,product_rating,product_offer
                                    ,pic
                            );
                            Date date=null;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                date= format.parse(createDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            prouctDetails.setCreateDate(date);
                            product_List.add(prouctDetails);
                        }
                        Log.d("Search Products", product_id + product_mrp + product_name +"\n");
                        pd.dismiss();
                    }
                    if(sortType==3)
                        sortData();
                    adaptor.notifyDataSetChanged();
                }else{
                    if(page>1){
                        Toast.makeText(this,"no more products",Toast.LENGTH_LONG).show();
                    }
                    Searchresult.setText("No product found matching to "+"'"+search+"'");
                    adaptor.notifyDataSetChanged();
                    pd.dismiss();
                }

            }
            else {
                Searchresult.setText("No product found matching to "+"'"+search+"'");
                product_List.clear();
                adaptor.notifyDataSetChanged();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void sortData() {
        switch(sortType){
            case 0:
                break;
            case 1:
                Collections.sort(product_List, new Comparator<Products_in_Search_details>(){
                    public int compare(Products_in_Search_details s1, Products_in_Search_details s2) {
                        return Integer.parseInt(s1.getProduct_actualprice())-Integer.parseInt(s2.getProduct_actualprice());
                    }
                });
                break;
            case 2:
                Collections.sort(product_List, new Comparator<Products_in_Search_details>(){
                    public int compare(Products_in_Search_details s1, Products_in_Search_details s2) {
                        return Integer.parseInt(s2.getProduct_actualprice())-Integer.parseInt(s1.getProduct_actualprice());
                    }
                });
                break;
            case 3:
                Collections.sort(product_List, new Comparator<Products_in_Search_details>(){
                    public int compare(Products_in_Search_details s1, Products_in_Search_details s2) {
                        return Integer.parseInt(s1.getProduct_rating())-Integer.parseInt(s2.getProduct_rating());
                    }
                });
                break;
            case 4:
                Collections.sort(product_List, new Comparator<Products_in_Search_details>(){
                    public int compare(Products_in_Search_details s1, Products_in_Search_details s2) {
                        return s1.getCreateDate().compareTo(s2.getCreateDate());
                    }
                });
                break;

        }
    }
    public static TextView menuCount;
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.mycart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        menuCount = (TextView) actionView.findViewById(R.id.cart_badge);
        menuCount.setText(Home.menuCounter+"");
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }
    public void setupBadge(int add) {
        if(add==-2){
            Home.menuCounter=0;
        }else{
            Home.menuCounter+=add;
        }
        if (menuCount != null) {

            if (Home.menuCounter == 0) {
                if (menuCount.getVisibility() != View.GONE) {
                    menuCount.setVisibility(View.GONE);
                }
            } else {
                menuCount.setText(String.valueOf(Math.min(Home.menuCounter, 99)));
                if (menuCount.getVisibility() != View.VISIBLE) {
                    menuCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.rightDrawerOpingIcon:
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.mycart:
                Intent cart=new Intent(this,ActivityMyCart.class);
                startActivity(cart);
                break;
            case R.id.searchBar:
                finish();
                Intent search=new Intent(this,ActivitySearchBox.class);
                startActivity(search);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.b_searchview_sort:
                Intent sort=new Intent(this,SortActivity.class);
                startActivityForResult(sort,3);
                break;
            case R.id.b_searchview_layout:
                if(isGrid){
                    isGrid=false;
                    layout.setImageResource(R.drawable.ic_format_list);
                    setrecyclerView(isGrid);
                }else{
                    isGrid=true;
                    layout.setImageResource(R.drawable.ic_grid_format);
                    setrecyclerView(isGrid);
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==3) {
            if (resultCode == Activity.RESULT_OK) {
                try{
                    sortType=data.getIntExtra("type",0);
                    Log.d("sorting", "onActivityResult: "+sortType);
                    page=1;
                    product_List.clear();
                    search(KeySearch,"search");
                    adaptor.notifyDataSetChanged();
                    pd = ProgressDialog.show(this, "Loading search", "wait...");
                    pd.setCancelable(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
    public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clicklistner;
        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ClickListener clicklistner) {
            this.clicklistner = clicklistner;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerview.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && clicklistner != null) {
                        clicklistner.onLongClick(childView, recyclerview.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clicklistner != null && gestureDetector.onTouchEvent(e)) {
                clicklistner.onClick(childView, rv.getChildPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int postion);
    }
    public void removedata(ArrayList<MinMax> list){
    }
}