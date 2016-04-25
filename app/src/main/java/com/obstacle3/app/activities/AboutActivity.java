package com.obstacle3.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.obstacle3.app.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView linkGitHub = ((TextView)findViewById(R.id.about_link_github));
        linkGitHub.setText(Html.fromHtml("<u>https://github.com/olheimer/ObstacleApp</u>"));

        linkGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("https://github.com/olheimer/ObstacleApp"), "text/html");

                startActivity(i);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
