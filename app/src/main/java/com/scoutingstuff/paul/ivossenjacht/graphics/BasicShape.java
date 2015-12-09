package com.scoutingstuff.paul.ivossenjacht.graphics;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by user on 9/2/15.
 */
public class BasicShape {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private int drawMode;


    // number of coordinates per vertex in this array
    protected int COORDS_PER_VERTEX ;
    protected int vertexStride; // 4 bytes per vertex

    protected final int triangleCount;
    protected final float myColor[] = new float[4];


    private static float[] flatten_vertices(Vec3d[] vertices) {
        float[] r = new float[vertices.length * 3];
        int i = 0;
        for (Vec3d v : vertices){
            r[i++] = (float)v.getX();
            r[i++] = (float)v.getY();
            r[i++] = (float)v.getZ();
        }
        return r;
    }
    public static float[] convertDoublesToFloats(double[] input)
    {
        if (input == null)
        {
            return null; // Or throw an exception - your choice
        }
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++)
        {
            output[i] = (float)input[i];
        }
        return output;
    }
    public BasicShape(int drawmode, Vec3d[] vertices, short[] order, float[] color) {
        this(drawmode, flatten_vertices(vertices), order, color);
    }
    public BasicShape(int drawmode, double[] vertices, short[] order, float[] color){
        this(drawmode, convertDoublesToFloats(vertices), order, color);
    }

    public BasicShape(int drawmode, float[] vertices, short[] order, float[] color) {
        setDrawMode(drawmode);
        System.arraycopy(color, 0, this.myColor, 0, color.length);
        this.triangleCount = order.length;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                order.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(order);
        drawListBuffer.position(0);

        int vertexShader = DirectionGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = DirectionGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);


        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }




    private final int mProgram;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vSizes;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vSizes;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public void draw(float[] mvpMatrix) {
        int mSizesHandle;
        int mColorHandle;
        int mMVPMatrixHandle;

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mSizesHandle = GLES20.glGetAttribLocation(mProgram, "vSizes");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mSizesHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mSizesHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);



        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, myColor, 0);





        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");


        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawElements(this.drawMode, triangleCount,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mSizesHandle);
    }

    public int getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
        switch (drawMode) {
            case GLES20.GL_POINTS:
                COORDS_PER_VERTEX = 1;
                break;
            case GLES20.GL_LINE_STRIP:
                COORDS_PER_VERTEX = 2;
                break;
            case GLES20.GL_LINES:
                COORDS_PER_VERTEX = 2;
                break;
            case GLES20.GL_LINE_LOOP:
                COORDS_PER_VERTEX = 2;
                break;
            case GLES20.GL_TRIANGLE_STRIP:
                COORDS_PER_VERTEX = 3;
                break;
            case GLES20.GL_TRIANGLE_FAN:
                COORDS_PER_VERTEX = 3;
                break;
            case GLES20.GL_TRIANGLES:
                COORDS_PER_VERTEX = 3;
                break;
        }
        vertexStride = COORDS_PER_VERTEX * 4;
    }

    public float[] getMyColor() {
        return myColor;
    }
    public void setMyColor(float[] color) {
        System.arraycopy(color, 0, this.myColor, 0, color.length);
    }

    public int getTriangleCount() {
        return triangleCount;
    }
}
