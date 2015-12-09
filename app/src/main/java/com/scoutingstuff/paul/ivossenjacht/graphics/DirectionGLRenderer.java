package com.scoutingstuff.paul.ivossenjacht.graphics;
/**
 * Created by user on 8/27/15.
 */

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.scoutingstuff.paul.ivossenjacht.gameProcess;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.util.Comparator;
import java.util.TreeMap;




public class DirectionGLRenderer implements GLSurfaceView.Renderer {
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mMVPRMatrix = new float[16];
    private gameProcess gp;
    private float surface_width;
    private float surface_height;
    private float angle = 0;

    public DirectionGLRenderer(){
        if (gp == null) {
            gp = new gameProcess();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);
    }
    @Override
    public void onDrawFrame(GL10 unused) {

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // Create a rotation transformation for the triangle
        //angle += 0.1;
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, 1.0f);
        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(mMVPRMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        if (gp != null) {
            gp.draw(mMVPRMatrix);
        }
    }




    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        float half_width = 1;
        float half_height = 1;
        if (ratio < 1) {
            half_height = 1/ratio;

        } else {
             half_width = ratio;
        }
        Matrix.frustumM(mProjectionMatrix, 0, -half_width, half_width, -half_height, half_height, 2.99f, 7);
        gp.onSurfaceChanged(-half_width, half_width, -half_height, half_height);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float getSurface_width() {
        return surface_width;
    }

    public float getSurface_height() {
        return surface_height;
    }

    public boolean isLandscape() {
        return surface_width >= surface_height;
    }

    public boolean isPortrait() {
        return surface_width <= surface_height;
    }

    public gameProcess getGameProcess() {
        return gp;
    }
}