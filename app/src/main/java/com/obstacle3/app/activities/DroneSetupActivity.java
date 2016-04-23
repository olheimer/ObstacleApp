package com.obstacle3.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.obstacle3.app.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity
public class DroneSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Click(R.id.drone_setup_done)
    public void droneSetupDone()
    {
        finish();
    }

}
