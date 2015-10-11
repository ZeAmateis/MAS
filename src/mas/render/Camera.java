package mas.render;

import java.text.DecimalFormat;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import mas.config.SimpleKeyBindingGLDescConfig;
import mas.config.SimpleStringDescConfig;
import mas.gui.PreferencesFrame;

/**
 * @author SCAREX
 *
 */
public class Camera
{
    private final SimpleStringDescConfig config = new SimpleStringDescConfig("movement");
    private final SimpleKeyBindingGLDescConfig keybindConfig = new SimpleKeyBindingGLDescConfig("movement_keyboard");
    private Vector3f position = new Vector3f(0.0F, 0.0F, 0.0F);
    private float rotPitch;
    private float rotYaw;
    private float roll;

    public Camera() {
        config.registerConfig("movement_ratio", "0.06", new DecimalFormat("0.0"));
        config.registerConfig("mouse_sensibility", "0.3", new DecimalFormat("0.0"));
        PreferencesFrame.registerConfig(config);
        
        keybindConfig.registerConfig("forward", Keyboard.KEY_Z);
        keybindConfig.registerConfig("backward", Keyboard.KEY_S);
        keybindConfig.registerConfig("right", Keyboard.KEY_D);
        keybindConfig.registerConfig("left", Keyboard.KEY_Q);
        keybindConfig.registerConfig("up", Keyboard.KEY_SPACE);
        keybindConfig.registerConfig("down", Keyboard.KEY_LSHIFT);
        PreferencesFrame.registerConfig(keybindConfig);
    }

    public void move() {
        if (Mouse.isButtonDown(1)) {
            this.rotYaw += Mouse.getDX() * config.getFloatValue("mouse_sensibility");
            this.rotPitch -= Mouse.getDY() * config.getFloatValue("mouse_sensibility");
            if (this.rotYaw > 360.0F) this.rotYaw = 0.0F;
            if (this.rotYaw < -360.0F) this.rotYaw = 0.0F;
            if (this.rotPitch > 80.0F) this.rotPitch = 80.0F;
            if (this.rotPitch < -80.0F) this.rotPitch = -80.0F;
        }
        float yRatio = config.getFloatValue("movement_ratio");
        if (Keyboard.isKeyDown(keybindConfig.getKeyValue("forward"))) {
            this.position.x += Math.sin(Math.toRadians(this.rotYaw)) * yRatio;
            this.position.z -= Math.cos(Math.toRadians(this.rotYaw)) * yRatio;
        }
        if (Keyboard.isKeyDown(keybindConfig.getKeyValue("backward"))) {
            this.position.x -= Math.sin(Math.toRadians(this.rotYaw)) * yRatio;
            this.position.z += Math.cos(Math.toRadians(this.rotYaw)) * yRatio;
        }
        if (Keyboard.isKeyDown(keybindConfig.getKeyValue("right"))) {
            this.position.x += Math.sin(Math.toRadians(this.rotYaw + 90.0F)) * yRatio;
            this.position.z -= Math.cos(Math.toRadians(this.rotYaw + 90.0F)) * yRatio;
        }
        if (Keyboard.isKeyDown(keybindConfig.getKeyValue("left"))) {
            this.position.x -= Math.sin(Math.toRadians(this.rotYaw + 90.0F)) * yRatio;
            this.position.z += Math.cos(Math.toRadians(this.rotYaw + 90.0F)) * yRatio;
        }
        if (Keyboard.isKeyDown(keybindConfig.getKeyValue("up"))) this.position.y += yRatio;
        if (Keyboard.isKeyDown(keybindConfig.getKeyValue("down"))) this.position.y -= yRatio;
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
