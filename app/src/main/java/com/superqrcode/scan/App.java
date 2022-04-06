package com.superqrcode.scan;

import com.common.control.MyApplication;
import com.common.control.manager.AppOpenManager;
import com.google.gson.Gson;
import com.superqrcode.scan.db.RoomDatabase;
import com.superqrcode.scan.view.activity.SplashActivity;


public class App extends MyApplication {
    private static App instance;
    private Gson gson;
    private RoomDatabase database;


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        gson = new Gson();
        database = RoomDatabase.getDatabase(this);
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
    }

    @Override
    protected boolean isPurchased() {
        return BuildConfig.PURCHASED;
    }

    @Override
    protected boolean isShowAdsTest() {
        return BuildConfig.DEBUG || BuildConfig.TEST_AD;
    }

    @Override
    public boolean enableAdsResume() {
        return true;
    }

    @Override
    public String getOpenAppAdId() {
        return BuildConfig.open_app;
    }

    public RoomDatabase getDatabase() {
        return database;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
