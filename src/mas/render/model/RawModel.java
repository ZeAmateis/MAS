package mas.render.model;

import mas.render.ModelLoader;

/**
 * @author SCAREX
 *
 */
public class RawModel
{
    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * @return the vaoID
     */
    public int getVaoID() {
        return vaoID;
    }

    /**
     * @return the vertixCount
     */
    public int getVertexCount() {
        return vertexCount;
    }
    
    public void clean() {
        ModelLoader.cleanVAO(vaoID);
    }
}
