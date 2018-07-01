package org.dailyfreshbasket.com.in.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.dailyfreshbasket.com.in.R;
import org.dailyfreshbasket.com.in.adapters.MypagerAdapter;
import org.dailyfreshbasket.com.in.fragments.NavigationDrawerFragment;
import org.dailyfreshbasket.com.in.fragments.NavigationDrawerFragmentRight;
import org.dailyfreshbasket.com.in.myInterface.getResponce;
import org.dailyfreshbasket.com.in.networks.SendRequest;
import org.dailyfreshbasket.com.in.tabs.SlidingTabLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_GUESTCART;
import static org.dailyfreshbasket.com.in.informations.GetDomain.KEYS.URL_MYCART;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_GUESTKEY;
import static org.dailyfreshbasket.com.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class Home extends AppCompatActivity implements MaterialTabListener, getResponce {
    private SlidingTabLayout mTabs;
    private ViewPager mPager;
    private MaterialTabHost tabHost;
    private Toolbar toolbar;
    private MypagerAdapter myadapter;
    public static int menuCounter = 0;
    public static TextView menuCount;
    NavigationDrawerFragmentRight drawerFragment_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerlayout), toolbar);
        drawerFragment_right = (NavigationDrawerFragmentRight)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawerRight);
        drawerFragment_right.setUp(R.id.fragment_navigation_drawerRight, (DrawerLayout) findViewById(R.id.drawerlayout), toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        myadapter = new MypagerAdapter(getSupportFragmentManager(), getResources());
        mPager.setAdapter(myadapter);
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
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
                            //.setIcon(myadapter.geticon(i))
                            .setText(myadapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new android.support.v7.app.AlertDialog.Builder(Home.this)
                            .setTitle("Daily Fresh Basket")
                            .setMessage("Do you want ot make phone call ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9140526575"));
                                    if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.CALL_PHONE}, 122);
                                        //Toast.makeText(Home.this,"No premission for makinng phone calls",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } catch (Exception e) {

                }
            }
        });

        try {
            if (readPrefes(this, PREF_KEY_UID, "").length() > 5) {

            } else {
                if (readPrefes(this, PREF_KEY_GUESTKEY, "").length() < 5) {
                    saveToPref(this, PREF_KEY_GUESTKEY, createTransactionID());
                    Log.d("UUID", "onCreate: " + createTransactionID());
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        getCartCount();
    }

    public String createTransactionID() throws Exception {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(10);
    }

    public static void saveToPref(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sheredPreference = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sheredPreference.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public void getCartCount() {
        String uid = readPrefes(this, PREF_KEY_UID, "");
        String guid = readPrefes(this, PREF_KEY_GUESTKEY, "");
        if (uid.length() > 1) {
            SendRequest req = new SendRequest(URL_MYCART, this, 3);
            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", uid);
            params.put("password", "");
            params.put("key", "GET");
            req.setParameters(params);
            req.send();
        } else if (guid.length() > 4) {
            SendRequest req = new SendRequest(URL_GUESTCART, this, 3);
            Map<String, String> params = new HashMap<String, String>();
            params.put("guid", guid);
            params.put("password", "");
            params.put("key", "GET");
            req.setParameters(params);
            req.send();
        } else {
            setupBadge(-2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBadge(0);
    }

    @Override
    public void responce(String res, int requestCode) {
        Log.d("HOME HOME", "responce: " + res);
        if (res == null || res.length() == 0) {
            return;
        }
        try {
            JSONObject response = new JSONObject(res);
            Boolean result = response.getBoolean("success");
            if (result) {
                setupBadge(-2);
                JSONArray cartArray = response.getJSONArray("data");
                for (int i = 0; i < cartArray.length(); i++) {
                    setupBadge(1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String readPrefes(Context context, String prefesName, String defaultValue) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName, defaultValue);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.home_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.mycart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        menuCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge(0);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.searchBar:
                Intent search = new Intent(this, ActivitySearchBox.class);
                startActivity(search);
                break;
            case R.id.rightDrawerOpingIcon:
                drawerFragment_right.OpenDrawer();
                break;
            case R.id.mycart:
                ///mMenuCounter--;
                //setupBadge(mMenuCounter);
                Intent cart = new Intent(this, ActivityMyCart.class);
                startActivity(cart);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBadge(int add) {
        if (add == -2) {
            menuCounter = 0;
        } else {
            menuCounter += add;
        }


        if (menuCount != null) {
            Log.d("menu", "setupBadge: " + menuCount.getText());
            if (menuCounter == 0) {
                if (menuCount.getVisibility() != View.GONE) {
                    menuCount.setVisibility(View.GONE);
                }
            } else {
                menuCount.setText(String.valueOf(Math.min(menuCounter, 99)));
                if (menuCount.getVisibility() != View.VISIBLE) {
                    menuCount.setVisibility(View.VISIBLE);
                }
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == 122) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9140526575"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }else
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Intent searchActivity = new Intent(this, SearchActivity.class);
                searchActivity.putExtra("bar", result.getContents());
                startActivity(searchActivity);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
