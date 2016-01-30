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
    protected String name;
    protected TexturedModel model;
    protected Vector3f position;
    protected int rotationX, rotationY,
            rotationZ;
    protected Vector3f scale;

    public Entity(String name, TexturedModel model, Vector3f position, int rotationX, int rotationY, int rotationZ, Vector3f scale) {
        super(name);
        this.name = name;
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
     * @return the scale
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * @param scale
     *            the scale to set
     */
    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
