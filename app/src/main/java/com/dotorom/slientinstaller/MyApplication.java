package com.dotorom.slientinstaller;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {

    private static  Context appContext=null;

    public static Context getAppContext(){
        return  appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext=getApplicationContext();
        Log.d("APP", "application crate");
    }



}
