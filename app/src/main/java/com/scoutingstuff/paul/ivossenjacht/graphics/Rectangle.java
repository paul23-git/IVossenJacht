package com.scoutingstuff.paul.ivossenjacht.graphics;

/**
 * Created by user on 8/27/15.
 */
public class Rectangle extends TriangleShape {
    static private float[] sizesToVertices(float height, float width) {
        return new float[] { -width/2, height/2, 0,
                -width/2, -height/2, 0,
                width/2, height/2, 0,
                width/2, -height/2, 0};
    }

    public Rectangle(float height, float width, float[] color) {
        super(sizesToVertices(height, width), new short[]{0, 1, 2, 1, 2, 3}, color);
    }
}