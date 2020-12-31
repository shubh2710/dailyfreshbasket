package org.dailyfreshbasket.co.in.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.dailyfreshbasket.co.in.R;

import java.util.HashMap;


public class MyFragment extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    private TextView textview;
    private SliderLayout mDemoSlider;
    public static MyFragment getInstence(int pos){
        MyFragment myFragment=new MyFragment();
        Bundle agrs=new Bundle();
        agrs.putInt("position",pos);
        myFragment.setArguments(agrs);
        return myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_my,container,false);
        textview=(TextView)layout.findViewById(R.id.tvposition);
        mDemoSlider = (SliderLayout) layout.findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.pic);
        file_maps.put("Big Bang Theory",R.drawable.pic);
        file_maps.put("House of Cards",R.drawable.pic);
        file_maps.put("Game of Thrones", R.drawable.pic);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);
        Bundle bundle=getArguments();
        if(bundle!=null){
            textview.setText("the page is "+bundle.getInt("position"));
        }
       /* RequestQueue requestQueue= VollySetup.getsInstance().getRequestQueue();
        StringRequest request=new StringRequest(Request.Method.GET,"http://php.net/",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "RESPONSE " + response, Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"errror"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);*/
        return layout;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }
    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();

        super.onStop();
    }
    @Override
    public void onResume() {
        mDemoSlider.startAutoCycle();
        super.onResume();
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
