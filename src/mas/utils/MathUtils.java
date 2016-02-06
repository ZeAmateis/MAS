package mas.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import mas.render.Camera;

/**
 * @author SCAREX
 *
 */
public class MathUtils
{
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector3f scale) {
        Matrix4f m = new Matrix4f();
        m.setIdentity();
        m.translate(translation);
        m.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        m.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        m.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        m.scale(scale);
        return m;
    }

    public static Matrix4f createViewMatrix(Camera c) {
        Matrix4f m = new Matrix4f();
        m.setIdentity();
        m.rotate((float) Math.toRadians(c.getRotPitch()), new Vector3f(1, 0, 0));
        m.rotate((float) Math.toRadians(c.getRotYaw()), new Vector3f(0, 1, 0));
        m.rotate((float) Math.toRadians(c.getRoll()), new Vector3f(0, 0, 1));
        Vector3f pos = c.getPosition().negate(null);
        m.translate(pos);
        return m;
    }
}
