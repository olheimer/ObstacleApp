package com.obstacle3.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.Polyline;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.bonuspack.utils.PolylineEncoder;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.drawing.OsmPath;
import org.osmdroid.views.overlay.PathOverlay;

/**
 * Created by oliverheim on 22.04.16.
 */
public class Map extends MapView {
    protected Map(Context context, ResourceProxy resourceProxy, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, resourceProxy, tileProvider, tileRequestCompleteHandler, attrs);
    }

    public Map(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Map(Context context) {
        super(context);
    }

    public Map(Context context, ResourceProxy resourceProxy) {
        super(context, resourceProxy);
    }

    public Map(Context context, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider) {
        super(context, resourceProxy, aTileProvider);
    }

    public Map(Context context, ResourceProxy resourceProxy, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, resourceProxy, aTileProvider, tileRequestCompleteHandler);
    }

    /**
     *
     * @param resolution in degrees
     * @param ul
     */
    public void addClassifiedPatch(double resolution, GeoPoint ul, int classificationColor)
    {
        Polygon square = new Polygon(getContext());
        square.setPoints(Polygon.pointsAsRect(ul,10000.0,10000.0));

        this.getOverlays().add(square);

        square.setFillColor(classificationColor);
        square.setStrokeColor(classificationColor);
        square.setStrokeWidth(2);



        invalidate();
    }
}