package org.dailyfreshbasket.com.in;

import android.app.Application;
import android.content.Context;

/**
 * Created by shubham on 2/20/2017.
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }
    public static MyApplication getsInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
