package com.obstacle3.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.obstacle3.app.R;
import com.obstacle3.app.model.AppSettingsModel_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public class AppSettings extends AppCompatActivity {

    @Pref
    AppSettingsModel_ settings;

    @ViewById(R.id.content_app_settings_btn_done)
    ImageButton doneBtn;

    @ViewById(R.id.app_settings_server_url)
    EditText baseUrl;

    @ViewById(R.id.app_settings_fc_patch_size)
    EditText patchSize;

    @ViewById(R.id.content_app_settings_showsattelite)
    SwitchCompat showSatelite;

    @ViewById(R.id.app_settings_fc_size)
    EditText fcSize;

    int accuracy = 200;
    int validatedFcSize = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        baseUrl.setText(settings.restapiurl().get());
        patchSize.setText(Integer.toString(settings.accuracy().get()));
        fcSize.setText(Integer.toString(settings.flightCorridorSize().get()));
        showSatelite.setChecked(settings.useSateliteMap().get());



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Click(R.id.content_app_settings_btn_done)
    public void done()
    {
        String basUrl = baseUrl.getText().toString();
        if(basUrl.endsWith("/") || basUrl.endsWith("\\"))
        {
            basUrl = basUrl.substring(0,basUrl.length()-1);
        }
        settings.edit().restapiurl().put(basUrl).apply();
        settings.edit().accuracy().put(accuracy).apply();
        settings.edit().flightCorridorSize().put(validatedFcSize).apply();
        settings.edit().useSateliteMap().put(showSatelite.isChecked()).apply();
        finish();
    }

    @TextChange(R.id.app_settings_server_url)
    public void onUrlChanged(CharSequence text, TextView tv, int before, int start, int count) {
        validateInput();
    }

    @TextChange(R.id.app_settings_fc_patch_size)
    public void onPatchSizeChanged(CharSequence text, TextView tv, int before, int start, int count) {
        validateInput();
    }

    @TextChange(R.id.app_settings_fc_size)
    public void onFcSizeChanged(CharSequence text, TextView tv, int before, int start, int count) {
        validateInput();
    }

    private void validateInput()
    {
        boolean valid = true;
        String basUrl = baseUrl.getText().toString();

        if(!URLUtil.isHttpUrl(basUrl) && !URLUtil.isHttpsUrl(basUrl))
        {
            valid  = false;
        }

        try
        {
            accuracy = Integer.valueOf(patchSize.getText().toString());
        }catch (NumberFormatException e)
        {
            valid = false;
        }

        try
        {
            validatedFcSize = Integer.valueOf(fcSize.getText().toString());
            if(validatedFcSize% accuracy !=0) //fc must be multiple of patch
            {
                valid = false;
            }
        }catch (NumberFormatException e)
        {
            valid = false;
        }


        if(valid)
        {
            doneBtn.setVisibility(View.VISIBLE);
        }
        else {
            doneBtn.setVisibility(View.INVISIBLE);
        }
    }

}
