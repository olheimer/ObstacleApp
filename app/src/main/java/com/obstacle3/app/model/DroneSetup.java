package com.obstacle3.app.model;

import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by oliverheim on 23.04.16.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface DroneSetup {
    @DefaultFloat(50.0f)
    float mass();

    @DefaultInt(1000)
    int maxFlightHeight();

    @DefaultFloat(5.0f)
    float maxSpeed();
}
