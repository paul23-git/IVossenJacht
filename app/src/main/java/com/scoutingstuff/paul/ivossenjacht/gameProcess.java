package com.scoutingstuff.paul.ivossenjacht;

import android.hardware.GeomagneticField;
import android.location.Location;

import com.scoutingstuff.paul.ivossenjacht.graphics.CircleOutline;
import com.scoutingstuff.paul.ivossenjacht.graphics.DrawLayer;
import com.scoutingstuff.paul.ivossenjacht.graphics.GraphicsObject;
import com.scoutingstuff.paul.ivossenjacht.graphics.Line;
import com.scoutingstuff.paul.ivossenjacht.graphics.SimpleLine;
import com.scoutingstuff.paul.ivossenjacht.graphics.Vec3d;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by user on 9/2/15.
 */
public class gameProcess implements LocationTracker.LocationUpdateListener, IOrientationTracker.OrientationUpdateListener, DownloadLocationTask.DownloadListener, UploadLocationTask.Uploadistener {
    private final TreeMap<Integer, DrawLayer> DrawLayerList = new TreeMap<>();
    private double[] direction_part = new double[4];
    private double[] upgrade_part = new double[4];
    private GraphicsObject screen_split;
    private GraphicsObject arrow;
    private GraphicsObject north_arrow;
    private GraphicsObject radar_background;
    private double phone_azimuth;
    private  boolean Landscape = false;
    private Location goal;
    private double bearing;
    public MainActivity dbg_activity;
    private Location location;
    private String name;
    private String password;
    private int drawcalls = 0;
    private int orientation_calls = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public gameProcess(){
        DrawLayer baselayer = addNewLayer(0);


        float color[] = { 1.0f, 1.0f, 0.0f, 1.0f };
        DrawLayer l = addNewLayer(1);
        DrawLayer l_top = addNewLayer(2);
        phone_azimuth = 0;

        //l.registerObject(GOUtilityCreators.create_line(new Vec3d(-1,-1,0), new Vec3d(0,0, 0), 0.005, 0.2, 8, false, true, color ));
        screen_split = GOUtilityCreators.create_line(new Vec3d(0,-1,0), new Vec3d(0,1, 0), 0.01, 0.2, 8, false, false, color );
        arrow = new GraphicsObject(new Vec3d(0,0,0), 0, new Line(0.8,0.02,0.15,6,false,true, new float[]{ 1.0f, 0.0f, 0.0f, 1.0f }));
        arrow.setVisible(false);
        north_arrow = new GraphicsObject(new Vec3d(0,0,0), phoneAzimuthToAngle(phone_azimuth), new Line(0.4,0.02,0.3,6,false,true, new float[]{ 0.0f, 0.0f, 1.0f, 1.0f }));
        radar_background = new GraphicsObject(new Vec3d(0.5,0,0), phoneAzimuthToAngle(phone_azimuth), new SimpleLine(100,color)); //new CircleOutline(0.5,new float[]{ 1.0f, 1.0f, 1.0f, 1.0f }));
        l.registerObject(screen_split);
        l.registerObject(arrow);
        l_top.registerObject(north_arrow);
        l.registerObject(radar_background);

        goal = new Location("endpoint");
        //goal.setLongitude(5.474486211896874);
        //goal.setLatitude(51.350543006010426);
        goal.setLongitude(0);
        goal.setLatitude(90);
        goal.setAltitude(0);

        name = null;
        password = null;
    }

    gameProcess(Location lastLocation) {
        this();
        this.location = lastLocation;
        updateLocation();
    }
    public DrawLayer addNewLayer(int depth) {
        if (DrawLayerList.containsKey(depth))  return null;
        DrawLayer l = new DrawLayer(depth);
        DrawLayerList.put(depth, l);
        return l;
    }

    public void addObjectToDraw(GraphicsObject obj, int layer) {
        DrawLayer l = DrawLayerList.get(layer);
        if (l == null) {
            l = addNewLayer(layer);
        }
        l.registerObject(obj);
    }
    public void draw(float[] mMVPRMatrix){
        //north_arrow.setOrientation(north_arrow.getOrientation() + 0.1);

        for (TreeMap.Entry<Integer, DrawLayer> entry :
                DrawLayerList.entrySet()) {
            entry.getValue().draw(mMVPRMatrix);
        }
        ++drawcalls;

    }

    public void onSurfaceChanged(double left, double  right, double bot, double top) {
        DrawLayer layer = screen_split.getLayer();
        layer.unregisterObject(screen_split);
        if ((right-left) >= (top-bot)) {
            screen_split = GOUtilityCreators.create_line(new Vec3d(0,top,0), new Vec3d(0,bot, 0), 0.01, 0, 1, false, false, screen_split.getColor() );
            this.direction_part[0] = left;
            this.direction_part[1] = (left+right)/2;
            this.direction_part[2] = bot;
            this.direction_part[3] = top;
            this.upgrade_part[0] = (left+right)/2;
            this.upgrade_part[1] = right;
            this.upgrade_part[2] = top;
            this.upgrade_part[3] = bot;
            this.Landscape = true;
        } else {
            screen_split = GOUtilityCreators.create_line(new Vec3d(left, 0, 0), new Vec3d(right, 0, 0), 0.01, 0, 1, false, false, screen_split.getColor());
            this.direction_part[0] = left;
            this.direction_part[1] = right;
            this.direction_part[2] = (top + bot) / 2;
            this.direction_part[3] = top;
            this.upgrade_part[0] = left;
            this.upgrade_part[1] = right;
            this.upgrade_part[2] = bot;
            this.upgrade_part[3] = (top + bot) / 2;
            this.Landscape = false;
        }
        double l = 0.75 * Math.min(this.direction_part[1] - this.direction_part[0], this.direction_part[3] - this.direction_part[2]);
        arrow.reShape(new Line(l, 0.02, 0.15, 6, false, true, new float[]{1.0f, 0.0f, 0.0f, 1.0f}));
        updateArrow(arrow);
        north_arrow.reShape(new Line(l / 2, 0.02, 0.3, 6, false, true, new float[]{0.0f, 0.0f, 1.0f, 1.0f}));
        north_arrow.setOriginCoords((this.direction_part[1] + this.direction_part[0]) / 2, (this.direction_part[3] + this.direction_part[2]) / 2, 0);

        layer.registerObject(screen_split);
    }

    @Override
    public void onLocationUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime){
        this.location = newLoc;
        updateLocation();
    }

    @Override
    public void onOrientationSensorUpdate(float[] old_orientation, float[] new_orientation) {
        //double _phone_azimuth = Math.toDegrees(new_orientation[0]);
        double decl = 0;
        if (location != null) {
            GeomagneticField geoField = new GeomagneticField(
                    Double.valueOf(location.getLatitude()).floatValue(),
                    Double.valueOf(location.getLongitude()).floatValue(),
                    Double.valueOf(location.getAltitude()).floatValue(),
                    System.currentTimeMillis()
            );
            decl = geoField.getDeclination();
        }

        double _phone_azimuth = Math.toDegrees(new_orientation[0]) + decl;

        boolean isl = isLandscape();
        phone_azimuth = _phone_azimuth + (isl ? 90 : 0);
        double d = phoneAzimuthToAngle(-phone_azimuth);
        north_arrow.setOrientation(d);
        arrow.setOrientation(phoneBearingAzimuthToAngle(this.bearing, this.phone_azimuth));
        updateArrow(arrow);
    }

    public void updateLocation() {
        if (location != null) {
            this.bearing = this.location.bearingTo(this.goal);
            arrow.setOrientation(phoneBearingAzimuthToAngle(this.bearing, this.phone_azimuth));
            updateArrow(arrow);
            arrow.setVisible(true);
        } else {
            arrow.setVisible(false);
        }
    }

    private void updateArrow(GraphicsObject a) {
        double l = 0.75 * Math.min(this.direction_part[1] - this.direction_part[0], this.direction_part[3] - this.direction_part[2]);
        double d = Math.toRadians(a.getOrientation());
        double px = (this.direction_part[1] + this.direction_part[0])/2;
        double py = (this.direction_part[3] + this.direction_part[2])/2;
        a.setPosition(new Vec3d(px - l / 2 * Math.cos(d), py - l / 2 * Math.sin(d), 0));


    }
    private static double phoneBearingAzimuthToAngle(double bearing, double azimuth) {
        double d = bearing - azimuth;
        if (d < 0) {
            d += 360;
        }
        d = d<=90 ? 90-d : ((450)-d);
        return d;
    }
    private static double phoneAzimuthToAngle(double azimuth) {
        double d = azimuth;
        if (d < 0) {
            d += 360;
        }
        d = d<=90 ? 90-d : ((450)-d);
        return d;
    }
    public boolean isLandscape() {
        return this.Landscape;
    }


    @Override
    public void onDownloadComplete(String input) {
        String[] s = input.split("\n");
        if (s.length > 0) {
            String target = s[0];
            String[] data = target.split(",");
            if (data.length > 4) {
                try {
                    double longitude = Double.parseDouble(data[1]);
                    double latitude = Double.parseDouble(data[2]);
                    double altitude = Double.parseDouble(data[3]);
                    //Location l = new Location(data[0]);
                    goal.setLongitude(longitude);
                    goal.setLatitude(latitude);
                    goal.setAltitude(altitude);
                    updateLocation();
                } catch (NumberFormatException e) {

                }
            }
        }
    }

    @Override
    public void onDownloadStart() {
    }

    @Override
    public void onUploadComplete(String input) {
        onDownloadComplete(input);
    }

    @Override
    public HashMap<String, String> onUploadStart() {
        HashMap<String,String> H = new HashMap<>();
        if (name != null) {
            H.put("name", new String(name));
            H.put("pass", new String(password));
            if (location != null) {
                H.put("pos", new String(location.getLongitude() + "," + location.getLatitude() + "," + location.getAltitude()));
            }
        }
        return H;
    }
}

class LayerCompare implements Comparator<DrawLayer> {
    @Override
    public int compare(DrawLayer e1, DrawLayer e2) {
        return e1.getDepth() - e2.getDepth();
    }
}

