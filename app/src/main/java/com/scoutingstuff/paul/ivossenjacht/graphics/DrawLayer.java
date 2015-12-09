package com.scoutingstuff.paul.ivossenjacht.graphics;

import com.scoutingstuff.paul.ivossenjacht.graphics.GraphicsObject;

import java.util.HashSet;

/**
 * Created by user on 9/2/15.
 */
public class DrawLayer {
    private int myDepth;
    private final HashSet<GraphicsObject> myObjects = new HashSet<>();
    public DrawLayer(int depth) {
        myDepth = depth;
    }

    public void registerObject(GraphicsObject obj) {
        myObjects.add(obj);
        obj.setLayer(this);
    }
    public void unregisterObject(GraphicsObject obj) {
        myObjects.remove(obj);
        obj.setLayer(null);
    }

    public int getDepth() {
        return myDepth;
    }

    public void draw(float[] mvpMatrix) {
        for (GraphicsObject obj:
             myObjects) {
            if (obj != null && obj.isVisible()) {
                obj.draw(mvpMatrix);
            }
        }
    }
}
