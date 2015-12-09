package com.scoutingstuff.paul.ivossenjacht.graphics;

/**
 * Created by Paul on 12/2/2015.
 */
public class Vec3d {
    public Vec3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec3d(double[] coords){
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
    }
    public Vec3d(Vec3d o) {
        this.x = o.getX();
        this.y = o.getY();
        this.z = o.getZ();
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    public void setY(double y) {
        this.y = y;
    }
    public Vec3d sum(Vec3d other) {
        return new Vec3d(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    public Vec3d diff(Vec3d other) {
        return new Vec3d(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public double length(){
        return Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z));
    }

    public double inclination(){
        return Math.atan2(this.y, this.x);
    }
    public double azimuth(){
        return Math.acos(this.z / this.length());
    }


    private double x;
    private double y;
    private double z;

}
