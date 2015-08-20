package mas.render;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import mas.MAS;
import mas.entity.Entity;
import mas.render.model.RawModel;
import mas.render.model.TexturedModel;
import mas.render.terrain.Terrain;
import mas.render.texture.ModelTexture;
import mas.shaders.BasicShader;
import mas.shaders.TerrainShader;

public class ThreadRendering extends Thread
{
    private final MAS mas;
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public ThreadRendering(MAS mas) {
        super("Rendering");
        this.mas = mas;
    }

    @Override
    public void run() {
        try {
            Display.create();
            Dimension dim;

            BasicShader shader = new BasicShader();
            TerrainShader tShader = new TerrainShader();

            Camera camera = new Camera();
            camera.getPosition().translate(0.0F, 1.5F, 0.0F);

            float[] vertices = { -0.5f,
                    0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f };
            float[] textureCoords = { 0,
                    0, 0, 1, 1, 1, 1, 0,
                    0, 0, 0, 1, 1, 1, 1,
                    0, 0, 0, 0, 1, 1, 1,
                    1, 0, 0, 0, 0, 1, 1,
                    1, 1, 0, 0, 0, 0, 1,
                    1, 1, 1, 0, 0, 0, 0,
                    1, 1, 1, 1, 0 };
            int[] indices = { 0, 1, 3,
                    3, 1, 2, 4, 5, 7, 7,
                    5, 6, 8, 9, 11, 11,
                    9, 10, 12, 13, 15,
                    15, 13, 14, 16, 17,
                    19, 19, 17, 18, 20,
                    21, 23, 23, 21,
                    22 };
            RawModel model = ModelLoader.loadToVAO(vertices, textureCoords, indices);
            ModelTexture texture = new ModelTexture(ModelLoader.loadTexture(MAS.class.getResourceAsStream("/test_texture_by_Whathefrench.png")));
            TexturedModel tModel = new TexturedModel(model, texture);

            Entity entity = new Entity(tModel, new Vector3f(0.0F, 1.0F, -4.0F), 0.0F, 0.0F, 0.0F, 1.0F);
            
            Terrain terrain = new Terrain(-0.9F, -0.9F, new ModelTexture(ModelLoader.loadTexture(MAS.class.getResourceAsStream("/terrain.png"))));

            while (!Display.isCloseRequested() && mas.isRunning()) {
                dim = mas.getNewCanvasSize().getAndSet(null);
                if (dim != null) GL11.glViewport(0, 0, dim.width, dim.height);

                camera.move();
                addTerrain(terrain);

                Renderer.prepare();
                shader.start();
                shader.loadViewMatrix(camera);
                Renderer.initProjectionMatrix();

                Renderer.renderEntity(entity, shader);

                shader.stop();

                tShader.start();
                tShader.loadViewMatrix(camera);
                Renderer.renderTerrains(terrains, tShader);
                tShader.stop();
                terrains.clear();

                Display.update();
            }
            shader.cleanUp();
            tShader.cleanUp();
            ModelLoader.cleanUp();
            Display.destroy();
            MAS.getMAS().dispose();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

    public void addTerrain(Terrain t) {
        terrains.add(t);
    }
}
