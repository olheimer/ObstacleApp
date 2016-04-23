package com.obstacle3.app.connection;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.obstacle3.app.model.GeolocationResponse;
import com.obstacle3.app.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by oliverheim on 23.04.16.
 */
public class GeolocateRest {
    RequestQueue requestQueue;

    private String baseUrl = "http://nominatim.openstreetmap.org/search?q=QUERY&format=json";

    public GeolocateRest(Context context)
    {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void query(String query, final LocationReceivedListener listener)
    {
        try {
            requestQueue.add(new JsonArrayRequest(baseUrl.replace("QUERY", URLEncoder.encode(query, "UTF-8")), new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Gson g = (new GsonBuilder()).create();
                    ArrayList<Location> locations = new ArrayList<Location>();

                    for(int i = 0;i<response.length();i++)
                    {
                        JSONObject respObject;
                        try {
                            respObject = response.getJSONObject(i);
                        } catch (JSONException e) {
                            continue;
                        }

                        GeolocationResponse parsedResp = g.fromJson(respObject.toString(),GeolocationResponse.class);

                        Location l = new Location();
                        l.name = parsedResp.display_name;
                        l.lat = parsedResp.lat;
                        l.lon = parsedResp.lon;
                        locations.add(l);
                    }
                    listener.onLocationReceived(locations);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError();
                }
            }));
        } catch (UnsupportedEncodingException e) {
            listener.onError();
        }
    }

    public interface LocationReceivedListener
    {
        void onError();
        void onLocationReceived(ArrayList<Location> locations);
    }
}
