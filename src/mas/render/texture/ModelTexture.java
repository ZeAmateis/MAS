package mas.render.texture;

import mas.render.ModelLoader;

/**
 * @author SCAREX
 *
 */
public class ModelTexture
{
    private final int id;

    public ModelTexture(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    public void clean() {
        ModelLoader.cleanTexture(id);
    }
}
