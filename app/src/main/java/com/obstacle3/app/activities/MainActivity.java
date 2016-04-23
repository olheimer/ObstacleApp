package com.obstacle3.app.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.obstacle3.app.Map;
import com.obstacle3.app.R;
import com.obstacle3.app.connection.ObstacleRest;
import com.obstacle3.app.dialogs.FindLocation;
import com.obstacle3.app.model.Location;
import com.obstacle3.app.model.MapType;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

//TODO: Allow user to use his current location as position for Server Query
@EActivity
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Location currentLocation;

    ArrayList<Button> mapSelectButtons = new ArrayList<>();

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
        final Map map = (Map) findViewById(R.id.map);
        map.getOverlays().clear();
        map.invalidate();
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(currentLocation.lat, currentLocation.lon);
        mapController.setZoom(14);
        mapController.animateTo(startPoint);

        (new ObstacleRest((this))).getMapTypes(new ObstacleRest.MapTypeReceivedListener() {
            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, R.string.maptypes_not_loadable,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMapTypesReceived(MapType[] mapTypes) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.content_main_maptype_select_wrapper);
                LayoutInflater inflater  = getLayoutInflater();

                ll.removeAllViews();

                mapSelectButtons.clear();

                for (final MapType mapType:
                     mapTypes) {
                    Button selectButton = (Button) inflater.inflate(R.layout.maptype_select_button,ll,false);
                    selectButton.setText(mapType.name);

                    selectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            (new ObstacleRest(MainActivity.this)).getMap(currentLocation.lat, currentLocation.lon, 5000, 5000, 200, new ObstacleRest.MapReceivedListener() {
                                @Override
                                public void onError() {
                                    Toast.makeText(MainActivity.this, R.string.map_loading_error,Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onMapReceived(GeoPoint ul, int[][] classification, int accuracy) {
                                    //GeoPoint loc = new GeoPoint(currentLocation.lat,currentLocation.lon);
                                    ((Map) findViewById(R.id.map)).createClassifiedMapOverlay(ul,accuracy, classification);
                                    //((Map) findViewById(R.id.map)).addClassifiedPatch(2000,loc, Color.parseColor("#000000"));
                                    //((Map) findViewById(R.id.map)).createClassifiedMapOverlay(ul,10000, new int[][]{{0,8},{0,8},{15,0}});

                                    for (Button mapSelect :
                                            mapSelectButtons) {
                                        mapSelect.setTextColor(ContextCompat.getColor(MainActivity.this,android.R.color.primary_text_light));
                                    }

                                    ((Button)v).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.activeMapType));

                                    addMapLongPressOverlay(map);

                                }
                            },mapType.maptype);
                        }
                    });

                    mapSelectButtons.add(selectButton);

                    ll.addView(selectButton);
                }
            }
        });

        addMapLongPressOverlay(map);

    }

    private void init()
    {
        currentLocation = new Location();
        currentLocation.lat = 40.7767168;
        currentLocation.lon = -111.9905248;

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
                currentLocation = l;
                initMap();
            }
        }));
        dialog.show();
    }

    private void addMapLongPressOverlay(Map map)
    {
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(final GeoPoint p) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Fly here?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentLocation.lon = p.getLongitude();
                        currentLocation.lat = p.getLatitude();
                        initMap();
                    }
                });
                builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });
        map.getOverlays().add(0, mapEventsOverlay);
    }


}
