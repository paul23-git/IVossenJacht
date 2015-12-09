package com.scoutingstuff.paul.ivossenjacht.graphics;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by user on 9/2/15.
 */
public class TriangleShape extends  BasicShape{
    public TriangleShape(Vec3d[] vertices, short[] order, float[] color){
        super(GLES20.GL_TRIANGLES,vertices,order,color);
    }
    public TriangleShape(double[] vertices, short[] order, float[] color){
        super(GLES20.GL_TRIANGLES, vertices, order, color);
    }

    public TriangleShape(float[] vertices, short[] order, float[] color) {
        super(GLES20.GL_TRIANGLES, vertices, order, color);
    }
}
