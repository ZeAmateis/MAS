package mas.entity;

import org.lwjgl.util.vector.Vector3f;

import mas.render.model.TexturedModel;

/**
 * @author SCAREX
 *
 */
public class Entity
{
    protected TexturedModel model;
    protected Vector3f position;
    protected float rotationX, rotationY, rotationZ;
    protected float scale;
    
    public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
    }
    
    public void increaseRotation(float dx, float dy, float dz) {
        this.rotationX += dx;
        this.rotationY += dy;
        this.rotationZ += dz;
    }

    /**
     * @return the model
     */
    public TexturedModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(TexturedModel model) {
        this.model = model;
    }

    /**
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * @return the rotationX
     */
    public float getRotationX() {
        return rotationX;
    }

    /**
     * @param rotationX the rotationX to set
     */
    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    /**
     * @return the rotationY
     */
    public float getRotationY() {
        return rotationY;
    }

    /**
     * @param rotationY the rotationY to set
     */
    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    /**
     * @return the rotationZ
     */
    public float getRotationZ() {
        return rotationZ;
    }

    /**
     * @param rotationZ the rotationZ to set
     */
    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    /**
     * @return the scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(float scale) {
        this.scale = scale;
    }
}
