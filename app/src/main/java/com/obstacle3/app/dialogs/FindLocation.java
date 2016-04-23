package com.obstacle3.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ArrayAdapter;

import com.obstacle3.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliverheim on 23.04.16.
 */
public class FindLocation extends Dialog {

    ListViewCompat mResults;
    LocationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_find_location);

        mResults  = (ListViewCompat)findViewById(R.id.dialog_find_location_results_wrapper);
        mAdapter = new LocationAdapter(getContext(), android.R.layout.simple_list_item_1);
        mResults.setAdapter(mAdapter);

        Location l = new Location();
        l.name = "Test";
        l.lat = 1.0;
        l.lon = 1.0;

        ArrayList<Location> loc = new ArrayList<>();
        loc.add(l);
        mAdapter.updateData(loc);
    }

    public FindLocation(Context context) {
        super(context);
        init();
    }

    public FindLocation(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected FindLocation(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init()
    {

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

    public class Location
    {
        public String name;
        public double lat;
        public double lon;

    }
}
