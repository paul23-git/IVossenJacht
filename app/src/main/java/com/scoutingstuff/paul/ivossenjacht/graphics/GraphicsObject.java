package com.scoutingstuff.paul.ivossenjacht.graphics;

import android.opengl.Matrix;

/**
 * Created by user on 9/2/15.
 */
public class GraphicsObject {
    static int UIDcounter = 0;

    protected Vec3d originCoords;
    protected double myOrientation;
    protected BasicShape myShape;
    private final int UID;
    private DrawLayer layer;
    private boolean visible;


    public GraphicsObject(double[] Coordinates, double orientation, BasicShape shape) {
        this.originCoords = new Vec3d(Coordinates);
        this.myOrientation = orientation;
        this.myShape = shape;
        UID = ++UIDcounter;
        visible = true;
    }
    public GraphicsObject(Vec3d Coordinates, double orientation, BasicShape shape) {
        this.originCoords = new Vec3d(Coordinates);
        this.myOrientation = orientation;
        this.myShape = shape;
        UID = ++UIDcounter;
        visible = true;
    }

    private final float[] mLocalTransformMatrix = new float[16];
    private final float[] mLocalRotateMatrix = new float[16];
    private final float[] mLocalTranslateMatrix = new float[16];
    private final float[] mCombinedTransformMatrix = new float[16];

    public void draw(float[] mvpMatrix) {
        Matrix.setIdentityM(mLocalTranslateMatrix, 0);
        Matrix.translateM(mLocalTranslateMatrix, 0, (float)this.originCoords.getX(), (float)this.originCoords.getY(), (float)this.originCoords.getZ());
        Matrix.setRotateM(mLocalRotateMatrix, 0, (float)this.myOrientation, 0, 0, 1.0f);
        //Matrix.setIdentityM(mLocalRotateMatrix, 0);
        Matrix.multiplyMM(mLocalTransformMatrix, 0, mLocalTranslateMatrix, 0, mLocalRotateMatrix, 0);
        Matrix.multiplyMM(mCombinedTransformMatrix, 0, mvpMatrix, 0, mLocalTransformMatrix, 0);
        myShape.draw(mCombinedTransformMatrix);
    }

    public int getUID() {
        return UID;
    }

    public void setOriginCoords(float[] originCoords) {
        setOriginCoords(originCoords[0], originCoords[1], originCoords[2]);
    }
    public void setOriginCoords(double x, double y, double z) {
        originCoords.setX(x);
        originCoords.setY(y);
        originCoords.setZ(z);
    }
    public Vec3d getPosition() {
        return originCoords;
    }

    public double getOrientation() {
        return myOrientation;
    }

    public void rotate(double amount) {
        myOrientation += amount;
    }

    public DrawLayer getLayer() {
        return layer;
    }

    public void setLayer(DrawLayer layer) {
        this.layer = layer;
    }

    public float[] getColor() {
        return myShape.getMyColor();
    }

    public void setOrientation(double myOrientation) {
        this.myOrientation = myOrientation;
    }

    public void setPosition(Vec3d originCoords) {
        this.originCoords = originCoords;
    }

    public void reShape(BasicShape newshape) {
        this.myShape = newshape;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int hashCode() {
        return UID;
    }
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof GraphicsObject))
            return false;
        if (that == this)
            return true;

        GraphicsObject rhs = (GraphicsObject) that;
        return this.UID == rhs.UID;
    }


}
