package com.scoutingstuff.paul.ivossenjacht;

import com.scoutingstuff.paul.ivossenjacht.graphics.GraphicsObject;
import com.scoutingstuff.paul.ivossenjacht.graphics.Line;
import com.scoutingstuff.paul.ivossenjacht.graphics.Vec3d;

/**
 * Created by Paul on 12/2/2015.
 */
public class GOUtilityCreators {
    public static GraphicsObject create_line(Vec3d start, Vec3d end, double line_width, double pointer_length_mul, double pointer_width_mul, boolean start_pointer, boolean end_pointer, float[] color){
        Vec3d diff_vec = end.diff(start);
        double len  = diff_vec.length();
        double dir = Math.toDegrees(diff_vec.inclination());
        Line l = new Line(len, line_width, pointer_length_mul, pointer_width_mul, start_pointer, end_pointer, color);
        return new GraphicsObject(start,dir,l);
    }
}
