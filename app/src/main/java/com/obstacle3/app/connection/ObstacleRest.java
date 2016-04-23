package com.obstacle3.app.connection;

import android.content.Context;

import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.obstacle3.app.model.GenerateMapRequest;
import com.obstacle3.app.model.GenerateMapRequestFlightArea;
import com.obstacle3.app.model.GenerateMapResponse;
import com.obstacle3.app.model.MapType;
import com.obstacle3.app.model.MapTypeResponse;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


/**
 * Created by oliverheim on 23.04.16.
 */

public class ObstacleRest {
    
    RequestQueue requestQueue;
    
    private String baseUrl = "http://172.17.68.6:3000/api";
    
    public ObstacleRest(Context context)
    {
        requestQueue = Volley.newRequestQueue(context);
    }
    
    public void getRandomMap(double lat, double lon, int length, int width, int accuracy, final MapReceivedListener listener)
    {
        GenerateMapRequest request = new GenerateMapRequest();
        request.accuracy = accuracy;
        GenerateMapRequestFlightArea fa = new GenerateMapRequestFlightArea();
        fa.lat = lat;
        fa.lon = lon;
        fa.length = length;
        fa.width = width;
        request.flightarea = fa;

        requestQueue.add(new GsonRequest<>(baseUrl + "/generate-map/random", GenerateMapResponse.class, request, new Response.Listener<GenerateMapResponse>() {
            @Override
            public void onResponse(GenerateMapResponse response) {
                listener.onMapReceived(new GeoPoint(response.lat,response.lon),response.classification, response.accuracy);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        }));
    }

    public void getMapTypes(final MapTypeReceivedListener listener)
    {
        requestQueue.add(new GsonRequest<>(baseUrl + "/get-maptypes/", MapTypeResponse.class, null, new Response.Listener<MapTypeResponse>() {
            @Override
            public void onResponse(MapTypeResponse response) {
                listener.onMapTypesReceived(response.maptypes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        }));
    }

    public interface MapReceivedListener
    {
        void onError();
        void onMapReceived(GeoPoint ul, int[][]classification, int accuracy);
    }

    public interface MapTypeReceivedListener
    {
        void onError();
        void onMapTypesReceived(MapType mapTypes[]);
    }

}
