package com.scoutingstuff.paul.ivossenjacht.graphics;

/**
 * Created by Paul on 12/9/2015.
 */
public class SimpleLine extends LineShape {
    public SimpleLine(double length, float[] color) {
        super(new Vec3d[]{new Vec3d(0,0,0),new Vec3d(length, 0, 0) }, new short[]{0,1}, color );
    }
}
