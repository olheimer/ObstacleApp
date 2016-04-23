package com.obstacle3.app.model;

import java.io.Serializable;

/**
 * Created by oliverheim on 23.04.16.
 */
public class GenerateMapRequestFlightArea implements Serializable {
    public double lat;
    public double lon;
    public int length;
    public int width;
}
