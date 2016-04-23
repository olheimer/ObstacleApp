package com.obstacle3.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.obstacle3.app.R;
import com.obstacle3.app.model.DroneSetup_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Locale;

@EActivity
public class DroneSetupActivity extends AppCompatActivity {

    @Pref
    DroneSetup_ droneSetup;

    @ViewById(R.id.drone_flight_height)
    EditText maxFLightHeight;

    @ViewById(R.id.drone_max_speed)
    EditText maxSpeed;

    @ViewById(R.id.drone_setup_mass)
    EditText mass;

    @ViewById(R.id.drone_setup_done)
    ImageButton btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validateForOK();

        maxFLightHeight.setText(""+droneSetup.maxFlightHeight().get());
        maxSpeed.setText(""+droneSetup.maxSpeed().get());
        mass.setText(""+droneSetup.maxSpeed().get());



    }

    @Click(R.id.drone_setup_done)
    public void droneSetupDone()
    {
        droneSetup.edit().mass().put(Float.valueOf(mass.getText().toString())).apply();
        droneSetup.edit().maxFlightHeight().put(Integer.valueOf(maxFLightHeight.getText().toString())).apply();
        droneSetup.edit().maxSpeed().put(Float.valueOf(maxSpeed.getText().toString())).apply();

        finish();
    }

    @TextChange(R.id.drone_max_speed)
    void onTextMaxSpeed(CharSequence text, TextView tv, int before, int start, int count) {
        validateForOK();
    }
    @TextChange(R.id.drone_setup_mass)
    void onTextMass(CharSequence text, TextView tv, int before, int start, int count) {
        validateForOK();
    }

    @TextChange(R.id.drone_flight_height)
    void onTextFlightHeight(CharSequence text, TextView tv, int before, int start, int count) {
        validateForOK();
    }

    private void validateForOK()
    {
        if(!mass.getText().toString().isEmpty() &&
                !maxFLightHeight.getText().toString().isEmpty() &&
                !maxSpeed.getText().toString().isEmpty())
        {
            btnDone.setVisibility(View.VISIBLE);
        }
        else
        {
            btnDone.setVisibility(View.INVISIBLE);
        }
    }

}
