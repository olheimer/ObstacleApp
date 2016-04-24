package com.obstacle3.app.model;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by oliverheim on 23.04.16.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface AppSettingsModel {
    @DefaultString("http://172.17.68.6:3000/api")
    String restapiurl();

    @DefaultInt(200)
    int accuracy();

    @DefaultInt(5000)
    int flightCorridorSize();

    @DefaultBoolean(false)
    boolean useSateliteMap();
}
