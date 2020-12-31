package org.dailyfreshbasket.co.in.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.adapters.MypagerAdapterSignOrLogin;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class SignInOrLoginTabs extends AppCompatActivity implements MaterialTabListener {

    private ViewPager mPager;
    private MaterialTabHost tabHost;
    private Fragment fragment=null;
    private  MypagerAdapterSignOrLogin myadapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_login_tabs);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager=(ViewPager)findViewById(R.id.pagerSignOrLogin);
        myadapter= new MypagerAdapterSignOrLogin(getSupportFragmentManager(),getResources());
        mPager.setAdapter(myadapter);
        tabHost = (MaterialTabHost)findViewById(R.id.materialTabHostSignOrLogin);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < myadapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(myadapter.getPageTitle(i))
                            //.setIcon(myadapter.geticon(i))
                            .setTabListener(this)
            );
        }
    }
    @Override
    public void onTabSelected(MaterialTab tab) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
}
