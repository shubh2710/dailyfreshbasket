package org.dailyfreshbasket.com.in.adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.fragments.FrontPageTabShopping;

/**
 * Created by shubham on 2/20/2017.
 */

public class MypagerAdapter extends FragmentPagerAdapter {
    String[] tabs;
    private Resources resources;
    int[] icons={R.drawable.ic_bucket_with_plus, R.drawable.ic_002_cart};
    String[] text={"Shopping"};
    public MypagerAdapter(FragmentManager fm, Resources resources){
        super(fm);
        this.resources=resources;
        tabs=text;
    }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:
                    fragment= FrontPageTabShopping.newInstance("","");
                    break;
            }
            return fragment;
        }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
    public String getText(int pos){
        return text[pos];
    }
    public Drawable geticon(int position){
        return resources.getDrawable(icons[position]);
    }

    @Override
    public int getCount() {
        return 1;
    }
}
