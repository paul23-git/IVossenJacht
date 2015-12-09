package com.scoutingstuff.paul.ivossenjacht.graphics;

import android.opengl.GLES20;

/**
 * Created by Paul on 12/9/2015.
 */
public class LineShape extends  BasicShape{
    public LineShape(Vec3d[] vertices, short[] order, float[] color){
        super(GLES20.GL_LINES,vertices,order,color);
    }
    public LineShape(double[] vertices, short[] order, float[] color){
        super(GLES20.GL_LINES, vertices, order, color);
    }

    public LineShape(float[] vertices, short[] order, float[] color) {
        super(GLES20.GL_LINES, vertices, order, color);
    }
}
