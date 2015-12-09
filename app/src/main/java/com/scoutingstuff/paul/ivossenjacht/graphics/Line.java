package com.scoutingstuff.paul.ivossenjacht.graphics;

/**
 * Created by Paul on 12/2/2015.
 */

import java.util.ArrayList;
import java.util.Arrays;


public class Line extends TriangleShape {
    static private Vec3d[] sizesToVertices(double length, double width, double pointer_width_mul, double pointer_length_mul, boolean start, boolean end) {
        width /= 2;
        double l_p = length * pointer_length_mul * ((start) ? 1 : 0);
        double w_p = width * pointer_width_mul;
        double l_e = length - length * pointer_length_mul * ((end) ? 1 : 0);
        ArrayList<Vec3d> vectors = new ArrayList<>();

        if (start) {
            vectors.addAll(Arrays.asList(new Vec3d[]{new Vec3d(0, 0, 0), new Vec3d(l_p, -w_p, 0), new Vec3d(l_p, w_p, 0)}));
        }
        vectors.addAll(Arrays.asList(new Vec3d[]{new Vec3d(l_p, -width, 0), new Vec3d(l_e, -width, 0),  new Vec3d(l_p, width, 0),  new Vec3d(l_e, width, 0)}));
        if (end) {
            vectors.addAll(Arrays.asList(new Vec3d[]{new Vec3d(length, 0, 0), new Vec3d(l_e, -w_p, 0), new Vec3d(l_e, w_p, 0)}));
        }
        return vectors.toArray(new Vec3d[vectors.size()]);
    }

    static private short[] toOrder(boolean start, boolean end) {
        int i=0;
        int n = 3 * ((start) ? 1 : 0) + 3 * ((end) ? 1 : 0) + 6;
        short[] r = new short[n];
        if (start) {
            for (;i<3;++i) {
                r[i] = (short) i;
            }
        }
        int e = i + 3;
        for (;i<e;++i) {
            r[i] = (short) i;
        }
        e = i + 3;
        for (;i<e;++i) {
            r[i] = (short) (i - 2);
        }
        if (end) {
            e = i + 3;
            for (;i<e;++i) {
                r[i] = (short) (i-2);
            }
        }
        return r;

    }
    public Line(double length, double width, double pointer_length_mul, double pointer_width_mul, boolean start, boolean end, float[] color) {
        super(sizesToVertices(length, width, pointer_width_mul, pointer_length_mul, start ,end), toOrder(start, end),  color);
    }
}
