package org.dailyfreshbasket.co.in.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.RecycleViewAdapterShopping;
import org.dailyfreshbasket.co.in.informations.Front_page_catagory_details;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.KEY_DOAMIN;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_GET_PRODUCT;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.UserDetailKeys.KEYS.KEY_PASSWORD;
import static org.dailyfreshbasket.co.in.informations.frontPageCatagoryKeys.KEYS.KEY_PHOTO;
import static org.dailyfreshbasket.co.in.informations.frontPageCatagoryKeys.KEYS.KEY_PRICE;
import static org.dailyfreshbasket.co.in.informations.frontPageCatagoryKeys.KEYS.KEY_PRODUCT_ID;
import static org.dailyfreshbasket.co.in.informations.frontPageCatagoryKeys.KEYS.KEY_PRODUCT_MRP;
import static org.dailyfreshbasket.co.in.informations.frontPageCatagoryKeys.KEYS.KEY_PRODUCT_NAME;
import static org.dailyfreshbasket.co.in.informations.frontPageCatagoryKeys.KEYS.KEY_TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrontPageTabShopping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontPageTabShopping extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private RecyclerView recyclerview;
    public RecycleViewAdapterShopping adaptor;
    private String mParam2;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    Bitmap load_image=null;
    ProgressBar prog;
    private SwipeRefreshLayout mSwiperefresh;
    private Front_page_catagory_details frontPageDetails;
    private ArrayList<String> types=new ArrayList<>();
    private List<List<Front_page_catagory_details>> FrontPageList=Collections.emptyList();
    public FrontPageTabShopping() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrontPageTabShopping.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontPageTabShopping newInstance(String param1, String param2) {
        FrontPageTabShopping fragment = new FrontPageTabShopping();
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
        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        //jsonRequest();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout=inflater.inflate(R.layout.fragment_shoping,container,false);
        mSwiperefresh=(SwipeRefreshLayout)layout.findViewById(R.id.sr_refresh);
        prog = (ProgressBar) layout.findViewById(R.id.progressBarHome);
        prog.setVisibility(View.VISIBLE);
        mSwiperefresh.setOnRefreshListener(this);
        CallCatgorysRequests();
        recyclerview=(RecyclerView) layout.findViewById(R.id.rcfragmentClothingList);
        FrontPageList=new ArrayList<>();
        FrontPageList.clear();
        adaptor=new RecycleViewAdapterShopping(getActivity());
        adaptor.setData(FrontPageList,types);
        recyclerview.setAdapter(adaptor);
        recyclerview.setFocusable(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        return layout;
    }
    public void VollyRequest(final String requestKey,final String uid,final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            recyclerview.setVisibility(View.VISIBLE);
                            Log.d("JECTHPMEPRODUCTLOADER"+":"+requestKey, obj.toString());
                            parseJSONResponce(obj,requestKey);
                        } catch (Exception t) {
                            Log.e("JECTHPMEPRODUCTLOADER", t.toString() +":"+ response );
                        }
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
                params.put("uid",uid);
                params.put("password",password);
                params.put("key",requestKey);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void parseJSONResponce(JSONObject response,String KEY) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            List<Front_page_catagory_details> front_page_rowDetaile_List=new ArrayList<>();
            JSONArray Productarray=null;
            Boolean result=response.getBoolean("success");
            if(result){
                if(!response.isNull("data") ){
                    mSwiperefresh.setRefreshing(false);
                    Productarray=response.getJSONArray("data");
                    prog.setVisibility(View.GONE);
                    if(Productarray.length()>0){
                        for(int i=0;i<Productarray.length();i++){
                            String product_name="", product_id="",product_mrp="",product_rate="",product_tag="";
                            int available=0;
                            JSONArray product_pic=new JSONArray();
                            frontPageDetails=new Front_page_catagory_details();
                            JSONObject currentObjecr=Productarray.getJSONObject(i);
                            frontPageDetails.setType(KEY);

                            if(currentObjecr.getString("quqntity")!=null)
                                available=currentObjecr.getInt("quqntity");
                            if(currentObjecr.getString(KEY_PRODUCT_NAME)!=null)
                                product_name=currentObjecr.getString(KEY_PRODUCT_NAME);

                            if(currentObjecr.getString(KEY_PRODUCT_ID)!=null)
                                product_id=currentObjecr.getString(KEY_PRODUCT_ID);

                            if(currentObjecr.getString(KEY_PRODUCT_MRP)!=null)
                                product_mrp=currentObjecr.getString(KEY_PRODUCT_MRP);

                            ArrayList<String> pics=new ArrayList<>();;
                            if(!currentObjecr.isNull(KEY_PHOTO))
                                product_pic=currentObjecr.getJSONArray(KEY_PHOTO);
                            else{
                                pics.add(KEY_DOAMIN+"/images/defult.png");
                            }
                            for(int j=0;j<product_pic.length();j++){
                                pics.add(product_pic.getString(j));
                            }
                            if(currentObjecr.getString(KEY_PRICE)!=null)
                                product_rate=currentObjecr.getString(KEY_PRICE);

                            product_tag=currentObjecr.getString(KEY_TAG);
                            Log.d("PRODUCTS",product_id+product_mrp+product_name+product_rate+product_tag+"\n");
                            if(!product_id.equals("null") && !product_id.isEmpty()){
                                frontPageDetails.first_product_catagory_details(product_name,product_id,product_rate,product_mrp,product_tag,available,pics);
                                // add row date like recent to array list
                                front_page_rowDetaile_List.add(frontPageDetails);
                            }
                        }
                        types.add(KEY);
                        FrontPageList.add(front_page_rowDetaile_List);
                        adaptor.setData(FrontPageList,types);
                        adaptor.notifyDataSetChanged();
                    }

               // adaptor.notifyDataSetChanged();

                }

                Log.d("PRODUCTS",Productarray.toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
            mSwiperefresh.setRefreshing(false);
            recyclerview.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onRefresh() {
        recyclerview.setVisibility(View.GONE);
        CallCatgorysRequests();

    }
    public void CallCatgorysRequests(){

        FrontPageList.clear();
        types.clear();
//        VollyRequest("High Rating","","");
        String uid=readPrefes(getActivity(),PREF_KEY_UID,"Please Login");
        String pass=readPrefes(getActivity(),KEY_PASSWORD,"Please Login");
        VollyRequest("Recent",uid,pass);
        VollyRequest("Recenty Watched",uid,pass);
        VollyRequest("Vegetables",uid,pass);
        VollyRequest("Fruits",uid,pass);
//        VollyRequest("All","50430830160214341001","0");
        VollyRequest("All",uid,pass);
       // VollyRequest("mostrated","","");
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private FrontPageTabShopping.ClickListener clicklistner;
    public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final FrontPageTabShopping.ClickListener clicklistner) {
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
        int action = e.getAction();
        // Toast.makeText(getActivity(),"HERE",Toast.LENGTH_SHORT).show();
        switch (action) {
            case MotionEvent.ACTION_POINTER_UP:
                rv.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                recyclerview.setNestedScrollingEnabled(true);

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
    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
public static interface ClickListener{
    public void onClick(View view, int position);
    public void onLongClick(View view, int postion);
}
}

