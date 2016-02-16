package mas.render;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import mas.render.model.RawModel;

/**
 * @author SCAREX
 *
 */
public class ModelLoader
{
    private static HashMap<Integer, ArrayList<Integer>> vaos = new HashMap<>();
    private static ArrayList<Integer> textures = new ArrayList<Integer>();

    public static RawModel loadToVAO(float[] pos, float[] tCoords, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices, vaoID);
        storeDataInList(0, 3, pos, vaoID);
        storeDataInList(1, 2, tCoords, vaoID);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public static int loadTexture(InputStream is) {
        try {
            Texture texture = TextureLoader.getTexture("PNG", is);
            int textureID = texture.getTextureID();
            textures.add(textureID);
            return textureID;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void cleanUp() {
        vaos.forEach((vao, vbos) -> {
            GL30.glDeleteVertexArrays(vao);
            for (int vbo : vbos) GL15.glDeleteBuffers(vbo);
        });
        for (int t : textures) {
            GL11.glDeleteTextures(t);
        }
    }

    private static int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.put(vaoID, new ArrayList<>());
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private static void storeDataInList(int number, int size, float[] data, int vao) {
        int vboID = GL15.glGenBuffers();
        vaos.get(vao).add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buf = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(number, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private static void bindIndicesBuffer(int[] indices, int vao) {
        int vboID = GL15.glGenBuffers();
        vaos.get(vao).add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buf = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
    }

    private static IntBuffer storeDataInIntBuffer(int[] data) {
        return (IntBuffer) BufferUtils.createIntBuffer(data.length).put(data).flip();
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data) {
        return (FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip();
    }
    
    public static void cleanVAO(int id) {
        GL30.glDeleteVertexArrays(id);
        for (int vbo : vaos.get(id)) {
            cleanVBO(vbo);
        }
    }
    
    public static void cleanVBO(int id) {
        GL15.glDeleteBuffers(id);
    }
    
    public static void cleanTexture(int id) {
        GL11.glDeleteTextures(id);
    }
}
