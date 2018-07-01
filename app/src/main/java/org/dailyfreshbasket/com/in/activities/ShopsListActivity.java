package org.dailyfreshbasket.com.in.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.RecycleViewAdapterShopsList;
import org.dailyfreshbasket.com.in.informations.infoOfShopDetails;

public class ShopsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private GridLayoutManager lLayout;
    private RecyclerView recyclerview;
    public RecycleViewAdapterShopsList adapter;
    private Context context;
    SearchView editsearch;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=this;
        setContentView(R.layout.activity_shops_list);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editsearch = (SearchView) findViewById(R.id.searchViewShops);
        editsearch.setQueryHint("type key to search shops");
        editsearch.setOnQueryTextListener(this);
        lLayout = new GridLayoutManager(this, 1);
        recyclerview=(RecyclerView) findViewById(R.id.rcShopsList);
        adapter=new RecycleViewAdapterShopsList(this,getdata());
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(lLayout);
        recyclerview.setAdapter(adapter);
        //recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(context,"on click"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(context,"on longclick"+position,Toast.LENGTH_SHORT).show();
            }
        }));
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.searchBar:
                break;
            case R.id.rightDrawerOpingIcon:
                break;
        }
        return false;
    }

    public static List<infoOfShopDetails> getdata(){
        List<infoOfShopDetails> data=new ArrayList<>();
        String[] contect={"9847573453","9847573453","9847573453","9847573453","9847573453"};
        String[] type={"sweets","garments","sweets","medical","chore store"};
        String[] name={"gomti express","allahabad store","allahabad store","allahabad store","allahabad store"};
        for(int i=0;i<100;i++){
            infoOfShopDetails current =new infoOfShopDetails();
            current.shop_name=name[i%name.length];
            current.shop_type=type[i%type.length];
            current.shop_contect=contect[i%contect.length];

            data.add(current);
        }
        return data;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
    public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ShopsListActivity.ClickListener clicklistner;
        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ShopsListActivity.ClickListener clicklistner) {
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
