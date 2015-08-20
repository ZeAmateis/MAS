package mas.render.terrain;

import mas.render.ModelLoader;
import mas.render.model.RawModel;
import mas.render.texture.ModelTexture;

/**
 * @author SCAREX
 *
 */
public class Terrain
{
    public static final float SIZE = 1000.0F;
    public static final int VERTEX_COUNT = 128;
    
    private float x;
    private float z;
    private RawModel model;
    private ModelTexture texture;
    
    public Terrain(float x, float z,ModelTexture texture) {
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.texture = texture;
        this.model = generateTerrain();
    }
    
    private RawModel generateTerrain(){
        int count = (int) (VERTEX_COUNT * VERTEX_COUNT);
        float[] vertices = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = 0;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return ModelLoader.loadToVAO(vertices, textureCoords, indices);
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
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
}
