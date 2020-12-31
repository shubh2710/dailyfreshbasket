package org.dailyfreshbasket.co.in.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.activities.ActivityMyCart;
import org.dailyfreshbasket.co.in.activities.ActivityMyWishList;
import org.dailyfreshbasket.co.in.activities.ActivityOrderList;
import org.dailyfreshbasket.co.in.activities.SignInOrLoginTabs;
import org.dailyfreshbasket.co.in.activities.signInOrLogIn;
import org.dailyfreshbasket.co.in.adapters.CustomAdapterForListViewRightDrawer;
import org.dailyfreshbasket.co.in.networks.VollyConnection;

import jp.wasabeef.blurry.Blurry;

import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;


public class NavigationDrawerFragmentRight extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lv;
    private ImageView currentUserprofilePic,profileBackImage;
    private Button loginButton;
    private FrameLayout profileback;
    private TextView currentUserName;
    private TextView currentUserEmail;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    Context context;
    int[] iconsListView={R.drawable.ic_035_shopping_cart,
            R.drawable.ic_017_wishlist,
            R.drawable.ic_036_order,
            R.drawable.ic_019_user};
    String[] titleListView ={"My Cart","My Wishlist","My Orders","My Account"};
    public static final String PREF_DRAWERFILE_NAME="testprefs";
    public static final String KEY_USER_LEAREN="ueser_learned_drawer";
    private boolean mUserLearnedDrawer;
    private View containerView;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mdrawerToggle;
    private boolean mFromSavedInstanetState;

    public NavigationDrawerFragmentRight() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(DrawerreadPrefes(getActivity(),KEY_USER_LEAREN,"false"));
        if(savedInstanceState==null){
            mFromSavedInstanetState=true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer_right,container,false);
        lv=(ListView) layout.findViewById(R.id.NavigationRightList);
        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        loginButton=(Button) layout.findViewById(R.id.b_DirectLoginButton);
        loginButton.setOnClickListener(this);
        currentUserEmail=(TextView)layout.findViewById(R.id.tvLoginCurrentemail);
        currentUserName=(TextView)layout.findViewById(R.id.tvCurrentUserName);
        profileBackImage=(ImageView) layout.findViewById(R.id.iv_profile_backImage);
        profileback=(FrameLayout)layout.findViewById(R.id.llfragmentNavigation_right);
        currentUserprofilePic=(ImageView)layout.findViewById(R.id.iv_rightdrwaerCurrentProfilePic);
        Blurry.with(getActivity())
                .from(BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.profileback))
                .into(profileBackImage);
        setCurrentUserDate();
       lv.setAdapter(new CustomAdapterForListViewRightDrawer(getContext(),titleListView ,iconsListView));
        lv.setOnItemClickListener(this);
        return layout;
    }
    private void setCurrentUserDate()
    {
        if(readPrefes(getActivity(),PREF_KEY_UID,"").length()>5){
            loginButton.setVisibility(View.GONE);
            currentUserEmail.setText(readPrefes(getActivity(),PREF_KEY_EMAIL,"Email not given"));
            currentUserName.setText(readPrefes(getActivity(),PREF_KEY_NAME,"Name not Given"));
            Log.d("PROfile PIC",readPrefes(getActivity(),PREF_KEY_PROFILE_PIC,""));
            if(readPrefes(getActivity(),PREF_KEY_PROFILE_PIC,"")!=null){
                try{
                    Picasso.with(getActivity())
                            .load(readPrefes(getActivity(),PREF_KEY_PROFILE_PIC,""))
                            .noFade()
                            .into(currentUserprofilePic);
                    Log.d("Profile called  1","this called 1");
                }catch (Exception e){
                    e.printStackTrace();
                    currentUserprofilePic.setImageResource(R.drawable.ic_019_user);
                    Log.d("Profile called  2","this called 2");
                }
            }
            else {
                Log.d("Profile called  3","this called 3");
                currentUserprofilePic.setImageResource(R.drawable.ic_019_user);
            }
        }else{
            loginButton.setVisibility(View.VISIBLE);
            currentUserEmail.setText(readPrefes(getActivity(),PREF_KEY_EMAIL,"Please Login"));
            currentUserName.setText(readPrefes(getActivity(),PREF_KEY_NAME,"not given"));
            currentUserprofilePic.setImageResource(R.drawable.ic_019_user);
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public void setUp(int fragmentId, DrawerLayout dl, Toolbar toolbar) {
        containerView=getActivity().findViewById(fragmentId);
        mdrawerlayout=dl;
        mdrawerToggle=new ActionBarDrawerToggle(getActivity(),dl,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(mUserLearnedDrawer && !mFromSavedInstanetState){
            mdrawerlayout.openDrawer(containerView);
        }
        mdrawerlayout.setDrawerListener(mdrawerToggle);
        mdrawerlayout.post(new Runnable() {
            @Override
            public void run() {
            mdrawerToggle.syncState();
            }
        });
        mdrawerlayout.closeDrawer(Gravity.RIGHT);
    }
    public void Closedrawer(){
        mdrawerlayout.closeDrawer(Gravity.RIGHT);
    }
    public void OpenDrawer(){
        mdrawerlayout.openDrawer(Gravity.RIGHT);
    }
    public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_DRAWERFILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    public static String DrawerreadPrefes(Context context, String prefesName,String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_DRAWERFILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                mdrawerlayout.closeDrawer(Gravity.RIGHT);
                Intent mycart=new Intent(getActivity(),ActivityMyCart.class);
                startActivity(mycart);
                break;
            case 1:
                mdrawerlayout.closeDrawer(Gravity.RIGHT);
                Intent mywishlist=new Intent(getActivity(),ActivityMyWishList.class);
                startActivity(mywishlist);
                break;
            case 2:
                mdrawerlayout.closeDrawer(Gravity.RIGHT);
                Intent order=new Intent(getActivity(),ActivityOrderList.class);
                startActivity(order);
                break;
            case 3:
                mdrawerlayout.closeDrawer(Gravity.RIGHT);
                Intent go=new Intent(getActivity(),signInOrLogIn.class);
                startActivity(go);
                break;
            case 4:
                mdrawerlayout.closeDrawer(Gravity.RIGHT);
                Toast.makeText(getActivity(),"Setting not available right now",Toast.LENGTH_LONG).show();
                //Intent g=new Intent(getActivity(),SettingsActivity.class);
                //startActivity(g);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        Intent login =new Intent(getActivity(), SignInOrLoginTabs.class);
        getActivity().startActivity(login);
    }
}
