package org.dailyfreshbasket.com.in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.activities.Home;
import org.dailyfreshbasket.com.in.adapters.CustomAdapterForListViewUserDatailDrawer;

import java.util.ArrayList;

import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.*;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationDrawerForUserDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationDrawerForUserDetail extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ListView lv;
    Context context;
    private View containerView;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mdrawerToggle;
    ArrayList Rightlist;
    private ProgressDialog pd=null;
    final Fragment LayoutFragments[]={new Fragment_MyProfile(),new Fragment_ChangePassword(),new Fragment_MyAddress()};

    int[] iconsListView={R.drawable.ic_019_user,
            R.drawable.ic_password,
            R.drawable.ic_040_shop,
            R.drawable.ic_019_user};
    String[] titleListView ={"My Profile","Change Password","My Addresses","Log Out"};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public NavigationDrawerForUserDetail() {
        // Required empty public constructor
    }

    public static NavigationDrawerForUserDetail newInstance(String param1, String param2) {
        NavigationDrawerForUserDetail fragment = new NavigationDrawerForUserDetail();
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
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer_for_user_detail,container,false);
        lv=(ListView) layout.findViewById(R.id.listViewUserDatailDrawer);
        lv.setAdapter(new CustomAdapterForListViewUserDatailDrawer(getContext(),titleListView ,iconsListView));
        lv.setOnItemClickListener(this);
        pd=new ProgressDialog(getActivity());
        return layout;
    }
    public void setUp(int fragmentId, DrawerLayout dl, Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mdrawerlayout = dl;
        mdrawerToggle = new ActionBarDrawerToggle(getActivity(), dl, toolbar, R.string.drawer_open, R.string.drawer_close) {
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

            /*@Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset<0.6)
                    tb.setAlpha(1-slideOffset);

            }*/
        };
        mdrawerlayout.setDrawerListener(mdrawerToggle);
        mdrawerlayout.post(new Runnable() {
            @Override
            public void run() {
                mdrawerToggle.syncState();
            }
        });
    }
    private void SetFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.UserDetailsesFragrmentSwitcher, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment=null;
        mdrawerlayout.closeDrawer(Gravity.LEFT);
        switch (position){
            case 0:
                if(readPrefes(getActivity(),PREF_KEY_LOGIN,false+"").equals("true")){
                    Log.d("ISLOGIN",readPrefes(getActivity(),PREF_KEY_LOGIN,false+""));
                fragment = LayoutFragments[0];
                SetFragment(fragment);
                }
                else{
                    Log.d("ISLOGIN"," login "+readPrefes(getActivity(),PREF_KEY_LOGIN,false+""));
                }
                break;

            case 1:
                if(readPrefes(getActivity(),PREF_KEY_LOGIN,false+"").equals("true")){
                    Log.d("ISLOGIN",readPrefes(getActivity(),PREF_KEY_LOGIN,false+""));
                fragment = LayoutFragments[1];
                SetFragment(fragment);
                }
                break;
            case 2:
                if(readPrefes(getActivity(),PREF_KEY_LOGIN,false+"").equals("true")){
                    Log.d("ISLOGIN",readPrefes(getActivity(),PREF_KEY_LOGIN,false+""));
                    fragment = LayoutFragments[2];
                    SetFragment(fragment);
                }
                break;
            case 3:
                pd = ProgressDialog.show(getActivity(), "Loggin out", "wait...");
                saveToPref(getActivity(),PREF_KEY_EMAIL,"Please Login");
                saveToPref(getActivity(),PREF_KEY_USER_PASS,"");
                saveToPref(getActivity(),PREF_KEY_LOGIN,false+"");
                saveToPref(getActivity(),PREF_KEY_NAME,"not given");
                saveToPref(getActivity(),PREF_KEY_PROFILE_PIC,null);
                saveToPref(getActivity(),PREF_KEY_UID,"");
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                Intent go=new Intent(getActivity(),Home.class);
                                getActivity().startActivity(go);
                                getActivity().finish();
                            }
                        }
                );
                break;
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
}
