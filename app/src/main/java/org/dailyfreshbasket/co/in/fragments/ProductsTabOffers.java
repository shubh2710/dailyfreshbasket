package org.dailyfreshbasket.co.in.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.RecycleViewAdapterProducts;
import org.dailyfreshbasket.co.in.informations.infoOfProducts;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsTabOffers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsTabOffers extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridLayoutManager lLayout;
    private RecyclerView recyclerview;
    public RecycleViewAdapterProducts adaptor;

    public ProductsTabOffers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsTabOffers.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsTabOffers newInstance(String param1, String param2) {
        ProductsTabOffers fragment = new ProductsTabOffers();
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
        lLayout = new GridLayoutManager(getActivity(), 1);
        View layout= inflater.inflate(R.layout.fragment_products_offer_tab, container, false);
        recyclerview=(RecyclerView) layout.findViewById(R.id.rcfragmentProductsList);
        adaptor=new RecycleViewAdapterProducts(getActivity(),getdate());
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(lLayout);
        recyclerview.setAdapter(adaptor);
        //recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(getContext(),"on click"+position,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(),"on longclick"+position,Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;
    }
    public static List<infoOfProducts> getdate(){
        List<infoOfProducts> data=new ArrayList<>();
        int[] icons={R.drawable.pic, R.drawable.pic,R.drawable.pic,R.drawable.pic,R.drawable.pic};
        String[] title ={"FrontPageTabShopping offer 10% off","Daily products get 10% off","Electronic product","Books","More"};
        for(int i=0;i<100;i++){
            infoOfProducts current =new infoOfProducts();
            current.iconId=icons[i%icons.length];
            current.title=title[i%title.length];

            data.add(current);

        }
        return data;
    }
    public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ProductsTabOffers.ClickListener clicklistner;
        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ProductsTabOffers.ClickListener clicklistner) {
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
}
