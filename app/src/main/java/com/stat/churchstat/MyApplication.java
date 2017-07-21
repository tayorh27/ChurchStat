package com.stat.churchstat;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.stat.churchstat.Database.DatabaseDb;
import com.stat.churchstat.Database.RatingDb;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sanniAdewale on 03/01/2017.
 */

public class MyApplication extends Application{
    private static MyApplication sInstance;
    private static DatabaseDb database;
    private static RatingDb ratingDb;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sInstance = this;
        database = new DatabaseDb(this);
        ratingDb = new RatingDb(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    public synchronized static DatabaseDb getWritableDatabase(){
        if (database == null){
            database = new DatabaseDb(getAppContext());
        }
        return database;

    }

    public synchronized static RatingDb getWritableRatings(){
        if (ratingDb == null){
            ratingDb = new RatingDb(getAppContext());
        }
        return ratingDb;

    }
}
