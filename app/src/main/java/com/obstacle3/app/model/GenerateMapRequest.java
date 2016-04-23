package com.obstacle3.app.model;

import java.io.Serializable;

/**
 * Created by oliverheim on 23.04.16.
 */
public class GenerateMapRequest implements Serializable{
    public GenerateMapRequestFlightArea flightarea;
    /**
     * In meters (resolution of one patch)
     */
    public int accuracy ;
}
