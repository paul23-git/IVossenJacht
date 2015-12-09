package com.scoutingstuff.paul.ivossenjacht.graphics;

/**
 * Created by Paul on 12/9/2015.
 */
public class CircleOutline extends LineShape {
    static Vec3d[] CreateVecs(double r, int precision) {
        double step = 2*Math.PI/(double)precision;
        Vec3d[] ret = new Vec3d[precision ];
        for (int i =0; i < precision; i++) {
            double next_dir = i*step;
            ret[i] = new Vec3d(r * Math.cos(next_dir), r*Math.sin(next_dir), 0);
        }
        return ret;
    }
    static short[] CreateOrder(int precision) {
        short[] ret = new short[precision*2];
        for (short i =0; i < precision; i++) {
            ret[2*i] = i;
            ret[2*i + 1] = (short)(i+1);
        }
        ret[precision*2-1] = 0;
        return ret;
    }
    public CircleOutline(double r, float[] color) {
        this(r, 16, color);
    }
    public CircleOutline(double r, int precision, float[] color) {
        super(CreateVecs(r,precision), CreateOrder(precision), color);

    }
}
