package org.dailyfreshbasket.com.in.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.dailyfreshbasket.com.in.R;
import java.io.ByteArrayOutputStream;

public class ShereThisAppActivity extends AppCompatActivity {
String mPath;
    ProgressDialog prog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shere_this_app);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int permissionCheck = ContextCompat.checkSelfPermission(ShereThisAppActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ShereThisAppActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                } else {
                    readFile();
                }
            }
        }).start();
    }
public void   readFile(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.share);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "ic_launcher_round", null);
        Uri imageUri = Uri.parse(path);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, "first ever vegetable market online store in allahabad at your door step, get our app now https://play.google.com/store/apps/details?id=org.dailyfreshbasket.com.in");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(intent , "Share"));
        finish();
        Log.i("image", "pathj"+mPath);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    readFile();
                }
                break;
            default:
                break;
        }
    }
}
