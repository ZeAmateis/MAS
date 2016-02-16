package mas.render.model;

import mas.render.texture.ModelTexture;

/**
 * @author SCAREX
 *
 */
public class TexturedModel
{
    protected RawModel model;
    protected ModelTexture texture;

    public TexturedModel(RawModel model, ModelTexture texture) {
        this.model = model;
        this.texture = texture;
    }

    /**
     * @return the model
     */
    public RawModel getModel() {
        return model;
    }

    /**
     * @return the texture
     */
    public ModelTexture getTexture() {
        return texture;
    }

    public void clean() {
        this.model.clean();
        this.texture.clean();
    }
}
