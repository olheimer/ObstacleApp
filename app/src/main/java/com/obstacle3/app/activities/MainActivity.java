package com.obstacle3.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.obstacle3.app.Map;
import com.obstacle3.app.R;
import com.obstacle3.app.connection.ObstacleRest;
import com.obstacle3.app.dialogs.FindLocation;
import com.obstacle3.app.model.Location;
import com.obstacle3.app.model.MapType;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
//TODO: Use http://nominatim.openstreetmap.org/search?q=<Querystring>&format=json for geocoding
//TODO: Allow user to use his current location as position for Server Query
@EActivity
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_manage)
        {
            startActivity(new Intent(this,DroneSetupActivity_.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        initMap();
    }

    private void initMap()
    {
        initMap(48.8583, 2.2944);
    }

    private void initMap(double lat, double lon)
    {
        Map map = (Map) findViewById(R.id.map);
        map.invalidate();
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(lat,lon);
        mapController.animateTo(startPoint);
        mapController.setZoom(14);

        (new ObstacleRest((this))).getMapTypes(new ObstacleRest.MapTypeReceivedListener() {
            @Override
            public void onError() {

            }

            @Override
            public void onMapTypesReceived(MapType[] mapTypes) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.content_main_maptype_select_wrapper);
                LayoutInflater inflater  = getLayoutInflater();

                ll.removeAllViews();

                for (MapType mapType:
                     mapTypes) {
                    Button selectButton = (Button) inflater.inflate(R.layout.maptype_select_button,ll,false);
                    selectButton.setText(mapType.name);
                    ll.addView(selectButton);
                }
            }
        });
    }

    private void init()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else
        {
            initMap();
        }
    }

    @Click(R.id.content_main_set_location_btn)
    public void setLocation()
    {
        FindLocation dialog = (new FindLocation(this, new FindLocation.LocationSelectedListener() {
            @Override
            public void onLocationSelected(Location l) {
                initMap(l.lat,l.lon);
            }
        }));
        dialog.show();
    }


}
