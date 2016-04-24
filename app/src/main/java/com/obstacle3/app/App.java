package com.obstacle3.app;

import android.app.Application;

import com.obstacle3.app.model.AppSettingsModel;
import com.obstacle3.app.model.AppSettingsModel_;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by oliverheim on 22.04.16.
 */
@EApplication
public class App extends Application {
    @Pref
    static AppSettingsModel_ settings;

    static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }


    public static AppSettingsModel_ getSettings()
    {
        return settings;
    }
}
