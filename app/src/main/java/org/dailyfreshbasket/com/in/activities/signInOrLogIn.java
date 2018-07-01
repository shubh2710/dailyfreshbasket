package org.dailyfreshbasket.com.in.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.fragments.Fragment_ChangePassword;
import org.dailyfreshbasket.com.in.fragments.Fragment_MyAddress;
import org.dailyfreshbasket.com.in.fragments.Fragment_MyPhoneNumber;
import org.dailyfreshbasket.com.in.fragments.Fragment_MyProfile;
import org.dailyfreshbasket.com.in.fragments.Fragment_MyProfilePic;
import org.dailyfreshbasket.com.in.fragments.Fragment_MySavedCards;
import org.dailyfreshbasket.com.in.fragments.NavigationDrawerForUserDetail;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.*;

public class signInOrLogIn extends AppCompatActivity {

    private Toolbar toolbar;
    private Button next;
    private boolean ifLogin=false;
    private  Fragment fragmen=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_log_in);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("My Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerForUserDetail drawerFragment=(NavigationDrawerForUserDetail)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_forUserDatail);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_forUserDatail,(DrawerLayout)findViewById(R.id.drawerlayoutSignOrLogin),toolbar);
        final Fragment LayoutFragments[]={new Fragment_MyProfile(),new Fragment_ChangePassword(),new Fragment_MyAddress(),
                                    new Fragment_MyProfilePic(),new Fragment_MyPhoneNumber(),new Fragment_MySavedCards()};
        ifLogin=Boolean.valueOf(readPrefes(this,PREF_KEY_LOGIN,"false"));
        if(!ifLogin){
            Intent go=new Intent(this, SignInOrLoginTabs.class);
            startActivity(go);
            finish();
        }
        else{
            fragmen = LayoutFragments[0];
            SetFragment(fragmen);
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==122){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"request added we will get back you soon.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void SetFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.UserDetailsesFragrmentSwitcher, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
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