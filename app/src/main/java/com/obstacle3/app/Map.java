package com.obstacle3.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

import com.obstacle3.app.model.GenerateMapResponse;

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
     * @param accuracy in meter
     * @param ul
     */
    public void addClassifiedPatch(int accuracy, GeoPoint ul, int classificationColor)
    {
        Polygon square = new Polygon(getContext());
        square.setPoints(Polygon.pointsAsRect(ul,accuracy,accuracy));

        this.getOverlays().add(square);

        square.setFillColor(classificationColor);
        square.setStrokeColor(classificationColor);
        square.setStrokeWidth(2);



        invalidate();
    }

    /**
     *
     * @param ul upper left (most north/west) start point for classified patch map
     * @param accuracy in meters -> with and height of each classified patch which will be executed
     * @param classification classification for each patch. 0/0 is most upper left, length-1/length-1 most lower right patch
     */
    public void createClassifiedMapOverlay(GeoPoint ul, int accuracy, int[][]classification)
    {
        GeoPoint centerOfFirst = ul.destinationPoint(accuracy/2,90.0f); //go east
        centerOfFirst = centerOfFirst.destinationPoint(accuracy/2,180.0f); //go south

        int lineCount = 0;
        int colCount = 0;



        for (int[] lineClassification: classification) {

            colCount = 0;
            GeoPoint patchPoint = centerOfFirst.clone();
            for (int colClassification: lineClassification) {
                int red = (int)(255*(((float)(15-colClassification))/((float)15)));
                int green = (int) (255*(((float)colClassification)/((float)15)));
                int clasColor = Color.rgb(red,green,0);
                addClassifiedPatch(accuracy,patchPoint,getClassificationColor(colClassification));
                //go east
                patchPoint = patchPoint.destinationPoint(accuracy,90.0f);
                colCount++;
            }
            lineCount++;

            //Go south
            centerOfFirst = centerOfFirst.destinationPoint(accuracy,180.0f);
        }
    }

    /**
     *
     * @param classification 15: 100% occupied = red, 0: 0% occupied = green
     * @return
     */
    public int getClassificationColor(double classification)
    {
        float H = (float)(((15.0f - classification)/15.0f) * 120.0f); //Hue: 120 = green, 0 = red
        float S = 0.9f; // Saturation
        float B = 0.9f; // Brightness


        return Color.HSVToColor(80, new float[]{H,S,B});
    }
}
