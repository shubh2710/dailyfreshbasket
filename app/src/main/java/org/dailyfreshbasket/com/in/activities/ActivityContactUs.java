package org.dailyfreshbasket.com.in.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.dailyfreshbasket.com.in.R;

import java.util.Locale;

public class ActivityContactUs extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button chnage_delivery;
    private Button contact_us;
    private Button exchnage;
    private ImageView find,probelm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("Contact Us");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        find=(ImageView) findViewById(R.id.iv_contact_find);
        contact_us=(Button)findViewById(R.id.b_contact_contact);
        exchnage=(Button)findViewById(R.id.b_contact_return);
        chnage_delivery=(Button)findViewById(R.id.b_contact_chnage);
        probelm=(ImageView) findViewById(R.id.iv_contact_problem);
        exchnage.setOnClickListener(this);
        chnage_delivery.setOnClickListener(this);
        probelm.setOnClickListener(this);
        find.setOnClickListener(this);

        contact_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.iv_contact_find:
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 25.446944,81.8491673);
                intent= new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.b_contact_contact:
                try{
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("Daily Fresh Basket")
                            .setMessage("Do you want ot make phone call ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9140526575"));
                                    if (ActivityCompat.checkSelfPermission(ActivityContactUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(ActivityContactUs.this, new String[]{Manifest.permission.CALL_PHONE}, 122);
                                        /// / Toast.makeText(ActivityContactUs.this,"No premission for makinng phone calls",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    startActivity(intent);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }catch (Exception e){

                }
                break;
            case R.id.b_contact_return:
                try{
                    intent = new Intent(this, ProblemActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                }
                break;
            case R.id.b_contact_chnage:
                try{
                    intent = new Intent(this, ProblemActivity.class);

                    startActivity(intent);
                }catch (Exception e){
                }
                break;
            case R.id.iv_contact_problem:
                intent=new Intent(ActivityContactUs.this,ProblemActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9140526575"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
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