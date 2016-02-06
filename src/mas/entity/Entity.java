package mas.entity;

import javax.swing.tree.DefaultMutableTreeNode;

import org.lwjgl.util.vector.Vector3f;

import mas.project.IMASProjectElement;
import mas.render.model.TexturedModel;

/**
 * @author SCAREX
 *
 */
public class Entity extends DefaultMutableTreeNode implements IMASProjectElement
{
    private static final long serialVersionUID = -6973246685675901807L;
    protected TexturedModel model;
    protected Vector3f position;
    protected Vector3f offset;
    protected int rotationX,
            rotationY, rotationZ;
    protected int scaleX, scaleY,
            scaleZ;

    public Entity(String name, TexturedModel model, Vector3f position, Vector3f offset, int rotationX, int rotationY, int rotationZ, int scaleX, int scaleY, int scaleZ) {
        super(name);
        this.model = model;
        this.position = position;
        this.offset = offset;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
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
     * @param model
     *            the model to set
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
     * @param position
     *            the position to set
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * @return the offset
     */
    public Vector3f getOffset() {
        return offset;
    }

    /**
     * @param offset
     *            the offset to set
     */
    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

    /**
     * @return the rotationX
     */
    public int getRotationX() {
        return rotationX;
    }

    /**
     * @param rotationX
     *            the rotationX to set
     */
    public void setRotationX(int rotationX) {
        this.rotationX = rotationX;
    }

    /**
     * @return the rotationY
     */
    public int getRotationY() {
        return rotationY;
    }

    /**
     * @param rotationY
     *            the rotationY to set
     */
    public void setRotationY(int rotationY) {
        this.rotationY = rotationY;
    }

    /**
     * @return the rotationZ
     */
    public int getRotationZ() {
        return rotationZ;
    }

    /**
     * @param rotationZ
     *            the rotationZ to set
     */
    public void setRotationZ(int rotationZ) {
        this.rotationZ = rotationZ;
    }

    /**
     * @return the scaleX
     */
    public int getScaleX() {
        return scaleX;
    }

    /**
     * @param scaleX
     *            the scaleX to set
     */
    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * @return the scaleY
     */
    public int getScaleY() {
        return scaleY;
    }

    /**
     * @param scaleY
     *            the scaleY to set
     */
    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    /**
     * @return the scaleZ
     */
    public int getScaleZ() {
        return scaleZ;
    }

    /**
     * @param scaleZ
     *            the scaleZ to set
     */
    public void setScaleZ(int scaleZ) {
        this.scaleZ = scaleZ;
    }
}
