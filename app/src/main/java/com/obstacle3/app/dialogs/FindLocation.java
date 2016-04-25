package com.obstacle3.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.obstacle3.app.R;
import com.obstacle3.app.connection.GeolocateRest;
import com.obstacle3.app.model.Location;

import java.util.ArrayList;

/**
 * Created by oliverheim on 23.04.16.
 */
public class FindLocation extends Dialog {

    ListViewCompat mResults;
    LocationAdapter mAdapter;
    LocationSelectedListener selectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_find_location);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mResults  = (ListViewCompat)findViewById(R.id.dialog_find_location_results_wrapper);
        mAdapter = new LocationAdapter(getContext(), R.layout.find_location_list_elem);
        mResults.setAdapter(mAdapter);

        findViewById(R.id.dialog_find_location_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });

        mResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedListener.onLocationSelected(mAdapter.mLocations.get(position));
                dismiss();
            }
        });

        ((EditText)findViewById(R.id.dialog_find_location_query)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public FindLocation(Context context, LocationSelectedListener listener) {
        super(context);
        selectedListener = listener;
    }

    public class LocationAdapter extends ArrayAdapter<String>
    {
        ArrayList<Location> mLocations;

        public LocationAdapter(Context context, int resource) {
            super(context, resource);
        }

        public void updateData(ArrayList<Location> locations)
        {
            this.clear();
            mLocations = locations;
            for (Location l:
                 locations) {
                this.add(l.name);
            }

            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    public interface LocationSelectedListener
    {
        void onLocationSelected(Location l);
    }

    private void query()
    {
        String query = ((EditText)findViewById(R.id.dialog_find_location_query)).getText().toString();
        (new GeolocateRest(getContext())).query(query, new GeolocateRest.LocationReceivedListener() {
            @Override
            public void onError() {
                Toast.makeText(getContext(), "Sorry, geocoding doesn't work at the moment. Please use long press on map to set new location.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationReceived(ArrayList<Location> response) {
                mAdapter.updateData(response);
            }
        });
    }
}
