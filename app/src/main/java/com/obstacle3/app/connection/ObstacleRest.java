package com.obstacle3.app.connection;

import android.content.Context;

import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.obstacle3.app.model.GenerateMapRequest;
import com.obstacle3.app.model.GenerateMapRequestFlightArea;
import com.obstacle3.app.model.GenerateMapResponse;


/**
 * Created by oliverheim on 23.04.16.
 */
//@Rest(rootUrl = "http://172.17.68.6:3000/api", converters = { StringHttpMessageConverter.class, GsonHttpMessageConverter.class })
public class ObstacleRest {
    
    RequestQueue requestQueue;
    
    private String baseUrl = "http://172.17.68.6:3000/api";
    
    public ObstacleRest(Context context)
    {
        requestQueue = Volley.newRequestQueue(context);
    }
    
    public void getRandomMap()
    {
        GenerateMapRequest request = new GenerateMapRequest();
        request.accuracy = 20;
        GenerateMapRequestFlightArea fa = new GenerateMapRequestFlightArea();
        fa.lat = 49.783871;
        fa.lon = 9.976217;
        fa.length = 300;
        fa.width = 300;
        request.flightarea = fa;

        requestQueue.add(new GsonRequest<>(baseUrl + "/generate-map/random", GenerateMapResponse.class, request, new Response.Listener<GenerateMapResponse>() {
            @Override
            public void onResponse(GenerateMapResponse response) {
                int i = 1;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int i = 1;
            }
        }));
    }

}
