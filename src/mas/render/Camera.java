package mas.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author SCAREX
 *
 */
public class Camera
{
    private static final float HEIGHT_RATIO = 0.06F;
    private static final float MOUSE_RATIO = 0.3F;
    private Vector3f position = new Vector3f(0.0F, 0.0F, 0.0F);
    private float rotPitch;
    private float rotYaw;
    private float roll;

    public void move() {
        if (Mouse.isButtonDown(1)) {
            this.rotYaw += Mouse.getDX() * MOUSE_RATIO;
            this.rotPitch -= Mouse.getDY() * MOUSE_RATIO;
            if (this.rotYaw > 360.0F) this.rotYaw = 0.0F;
            if (this.rotYaw < -360.0F) this.rotYaw = 0.0F;
            if (this.rotPitch > 80.0F) this.rotPitch = 80.0F;
            if (this.rotPitch < -80.0F) this.rotPitch = -80.0F;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            this.position.x += Math.sin(Math.toRadians(this.rotYaw)) * HEIGHT_RATIO;
            this.position.z -= Math.cos(Math.toRadians(this.rotYaw)) * HEIGHT_RATIO;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.position.x -= Math.sin(Math.toRadians(this.rotYaw)) * HEIGHT_RATIO;
            this.position.z += Math.cos(Math.toRadians(this.rotYaw)) * HEIGHT_RATIO;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.position.x += Math.sin(Math.toRadians(this.rotYaw + 90.0F)) * HEIGHT_RATIO;
            this.position.z -= Math.cos(Math.toRadians(this.rotYaw + 90.0F)) * HEIGHT_RATIO;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            this.position.x -= Math.sin(Math.toRadians(this.rotYaw + 90.0F)) * HEIGHT_RATIO;
            this.position.z += Math.cos(Math.toRadians(this.rotYaw + 90.0F)) * HEIGHT_RATIO;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) this.position.y += HEIGHT_RATIO;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) this.position.y -= HEIGHT_RATIO;
    }

    /**
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @return the rotPitch
     */
    public float getRotPitch() {
        return rotPitch;
    }

    /**
     * @return the rotYaw
     */
    public float getRotYaw() {
        return rotYaw;
    }

    /**
     * @return the roll
     */
    public float getRoll() {
        return roll;
    }
}
