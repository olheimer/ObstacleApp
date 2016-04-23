package com.obstacle3.app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.obstacle3.app.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView wv = (WebView) findViewById(R.id.abtout_webview_link_githun);
        wv.loadData("<html><body style=\"background-color:transparent;\"><a style=\"color:#000000\" href=\"https://github.com/olheimer/ObstacleApp\">https://github.com/olheimer/ObstacleApp</a></body></html>","text/html","UTF-8");
        wv.setBackgroundColor(Color.TRANSPARENT);
        wv.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
