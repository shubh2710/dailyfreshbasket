package org.dailyfreshbasket.co.in.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.SearchActivity;
import org.dailyfreshbasket.co.in.informations.Front_page_catagory_details;
import org.dailyfreshbasket.co.in.networks.VollyConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_BANNER;

/**
 * Created by shubham on 2/19/2017.
 */

public class RecycleViewAdapterShopping extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity context;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Button more;
    private Bitmap image=null;
    private final int USER = 0, IMAGE = 1;
    private List<List<Front_page_catagory_details>> data=new ArrayList<List<Front_page_catagory_details>>();
    private LayoutInflater inflater;
    private List<Front_page_catagory_details> singleRowData= Collections.emptyList();
    private ArrayList<String> types=new ArrayList<>();
    public RecycleViewAdapterShopping(Activity context){
        inflater= LayoutInflater.from(context);
        this.context=context;
        Log.d("NOTIFIE","constructor called");
    }
    public void setData( List<List<Front_page_catagory_details>> data,ArrayList<String> types){
        this.types=types;
        this.data=data;
        Log.d("DaTaInAdptor",data.toString());
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder=null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        switch(viewType){
            case USER:
                View view=inflater.inflate(R.layout.custom_row_products,parent,false);
                viewHolder=new MyViewHolder1(view);
                break;
            case IMAGE:
                View view2=inflater.inflate(R.layout.sliderlayout,parent,false);
                viewHolder=new MyViewHolder2(view2);
                break;
        }

               return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
        switch (viewHolder.getItemViewType()){
            case USER:
                MyViewHolder1 vh1 = (MyViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case IMAGE:
                MyViewHolder2 vh2 = (MyViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
        }

        Log.d("POSITION"," Vertical postion  on bind"+position);
    }
    private void configureViewHolder1(final MyViewHolder1 vh1, final int position) {
        singleRowData=new ArrayList<>();
        singleRowData=data.get(position-1);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivity=new Intent(context,SearchActivity.class);
//                searchActivity.putExtra("search",types.get(position-1));
                searchActivity.putExtra("search",vh1.type.getText().toString());
                context.startActivityForResult(searchActivity,3);
            }
        });
        if(types.get(position-1).equals("Recent")){
            vh1.type.setText("Newly Added");
        }else if(types.get(position-1).equals("Recenty Watched")){
            vh1.type.setText("Recently Watched");
        }else{
            vh1.type.setText(types.get(position - 1));
        }
        vh1.RecyclerViewadaptor.setProductsrow(singleRowData);
        vh1.RecyclerViewadaptor.notifyDataSetChanged();
    }
    private void configureViewHolder2(MyViewHolder2 vh2,int position) {
            vh2.DemoSlider.startAutoCycle();
    }
    @Override
    public int getItemCount() {
        return data.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return IMAGE;
        } else  {
            return USER;
        }
    }
   public class MyViewHolder1 extends RecyclerView.ViewHolder{
       TextView type;
       private RecyclerView recyclerview;
       private LinearLayout sliderlayout;
       private HorizontalRecycleViewAdapterForFrontProducts RecyclerViewadaptor;

       public MyViewHolder1(View itemView) {
           super(itemView);
           Log.d("NOTIFIE","MyviewHoldder called");
           more=(Button)itemView.findViewById(R.id.b_catagory_selection);
           type=(TextView)itemView.findViewById(R.id.tvCatagoryType);
           recyclerview=(RecyclerView) itemView.findViewById(R.id.rvHorizontalRowList);
           sliderlayout=(LinearLayout)itemView.findViewById(R.id.ll_sliderLayout);
           RecyclerViewadaptor=new HorizontalRecycleViewAdapterForFrontProducts(context);
           recyclerview.setAdapter(RecyclerViewadaptor);
           LinearLayoutManager layoutManager
                   = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
           recyclerview.setLayoutManager(layoutManager);
           recyclerview.addOnItemTouchListener(new RecyclerTouchListner(context, recyclerview, new ClickListener() {
               @Override
               public void onClick(View view, int pos) {
               }
               @Override
               public void onLongClick(View view, int position) {
               }
           }));
       }
   }
    public class MyViewHolder2 extends RecyclerView.ViewHolder implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
        private LinearLayout sliderlayout;
        private SliderLayout DemoSlider;
        public MyViewHolder2(View itemView) {
            super(itemView);
            sliderlayout=(LinearLayout)itemView.findViewById(R.id.ll_sliderLayout);
            DemoSlider =(SliderLayout) itemView.findViewById(R.id.demosliderlayout);
            HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
            file_maps.put("Recent",R.drawable.home);
            for(String name : file_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(context);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
//                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                DemoSlider.addSlider(textSliderView);
            }
            updateBanners();
            DemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            DemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            DemoSlider.setCustomAnimation(new DescriptionAnimation());
            DemoSlider.setDuration(4000);
            DemoSlider.addOnPageChangeListener(this);

        }

        public  void updateBanners(){
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,URL_BANNER, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
                    try{
                        parseJSONResponceForbanner(response);
                    }catch (Exception e){
                        Log.d("JSONarraybanner",e.toString()+" :"+response.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JSONarraybanner",error.toString());
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        }
        private void parseJSONResponceForbanner(JSONObject response) {
            if(response==null || response.length()==0){
                return;
            }
            try{
                String pic="";
                Log.d("JSONarraybanner",response.toString());
                boolean success=response.getBoolean("success");
                if(success) {
                    JSONArray arrayBanner=response.getJSONArray("list");
                    DemoSlider.removeAllSliders();
                    for(int i=0;i<arrayBanner.length();i++){
                        //String text=arrayBanner.getString("description");
                        String tag=arrayBanner.getJSONObject(i).getString("tag");
                        String image=arrayBanner.getJSONObject(i).getString("url");
                        TextSliderView textSliderView = new TextSliderView(context);
                        // initialize a SliderLayout
                        textSliderView
                                //.description(text)
                                .image(image)
                                .setScaleType(BaseSliderView.ScaleType.Fit);
//                                .setOnSliderClickListener(this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",image);
                        textSliderView.getBundle()
                                .putString("tag",tag);
                        DemoSlider.addSlider(textSliderView);
                        Log.d("PIC",pic);
                }

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            DemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            DemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            DemoSlider.setCustomAnimation(new DescriptionAnimation());
            DemoSlider.setDuration(4000);
            DemoSlider.addOnPageChangeListener(this);
        }


        @Override
        public void onSliderClick(BaseSliderView slider) {
            Log.d("slid click", "onSliderClick: "+slider.getBundle().getString("tag"));
            if(slider.getBundle().getString("tag").length()>2){
                Intent search2=new Intent(context,SearchActivity.class);
                search2.putExtra("search",slider.getBundle().getString("tag"));
                context.startActivity(search2);
            }
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
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                    return super.onScroll(e1, e2, distanceX, distanceY);
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
    }
