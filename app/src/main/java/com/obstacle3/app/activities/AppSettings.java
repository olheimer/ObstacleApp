package com.obstacle3.app.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.obstacle3.app.R;
import com.obstacle3.app.model.AppSettingsModel;
import com.obstacle3.app.model.AppSettingsModel_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

@EActivity
public class AppSettings extends AppCompatActivity {

    @Pref
    AppSettingsModel_ settings;

    @ViewById(R.id.content_app_settings_btn_done)
    ImageButton doneBtn;

    @ViewById(R.id.app_settings_server_url)
    EditText baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        baseUrl.setText(settings.restapiurl().get());


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
        finish();
    }

    @TextChange(R.id.app_settings_server_url)
    public void onUrlChanged(CharSequence text, TextView tv, int before, int start, int count) {

        if(!URLUtil.isHttpUrl(text.toString()) &&!URLUtil.isHttpsUrl(text.toString()))
        {
            doneBtn.setVisibility(View.INVISIBLE);
        }
        else {
            doneBtn.setVisibility(View.VISIBLE);
        }
    }

}
