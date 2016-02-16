package mas.render;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import mas.MAS;
import mas.config.IKeyCallback;
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
    public static final float[] vertices = {
            0F, 0.0625F, 0F, 0F, 0F, 0F,
            0.0625F, 0F, 0F, 0.0625F,
            0.0625F, 0F, 0F, 0.0625F,
            0.0625F, 0F, 0F, 0.0625F,
            0.0625F, 0F, 0.0625F,
            0.0625F, 0.0625F, 0.0625F,
            0.0625F, 0.0625F, 0F,
            0.0625F, 0F, 0F, 0.0625F,
            0F, 0.0625F, 0.0625F,
            0.0625F, 0.0625F, 0F,
            0.0625F, 0F, 0F, 0F, 0F, 0F,
            0F, 0.0625F, 0F, 0.0625F,
            0.0625F, 0F, 0.0625F,
            0.0625F, 0F, 0.0625F, 0F,
            0.0625F, 0.0625F, 0F,
            0.0625F, 0.0625F, 0.0625F,
            0F, 0F, 0.0625F, 0F, 0F, 0F,
            0.0625F, 0F, 0F, 0.0625F,
            0F, 0.0625F };
    public static final float[] textureCoords = {
            0, 0, 0, 1, 1, 1, 1, 0, 0,
            0, 0, 1, 1, 1, 1, 0, 0, 0,
            0, 1, 1, 1, 1, 0, 0, 0, 0,
            1, 1, 1, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 0, 0, 0, 0, 1, 1,
            1, 1, 0 };
    public static final int[] indices = {
            0, 1, 3, 3, 1, 2, 4, 5, 7,
            7, 5, 6, 8, 9, 11, 11, 9,
            10, 12, 13, 15, 15, 13, 14,
            16, 17, 19, 19, 17, 18, 20,
            21, 23, 23, 21, 22 };
    private List<Terrain> terrains = new ArrayList<Terrain>();
    public static RawModel CUBE_MODEL;
    public static ModelTexture CUBE_TEXTURE_TEST;
    public static TexturedModel TEXTURED_MODEL_TEST;
    public static volatile boolean isRenderStarted = false;
    private static final Camera camera = new Camera();

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

            camera.getPosition().translate(0F, 4F, -3F);
            camera.setRotYaw(180);
            camera.setRotPitch(30);

            CUBE_MODEL = ModelLoader.loadToVAO(vertices, textureCoords, indices);
            CUBE_TEXTURE_TEST = new ModelTexture(ModelLoader.loadTexture(MAS.class.getResourceAsStream("/test_texture_by_Whathefrench.png")));
            TEXTURED_MODEL_TEST = new TexturedModel(CUBE_MODEL, CUBE_TEXTURE_TEST);

            Terrain terrain = new Terrain(-0.9F, -0.9F, new ModelTexture(ModelLoader.loadTexture(MAS.class.getResourceAsStream("/terrain.png"))));
            addTerrain(terrain);

            isRenderStarted = true;
            while (!Display.isCloseRequested() && mas.isRunning()) {
                dim = mas.getNewCanvasSize().getAndSet(null);
                if (dim != null) GL11.glViewport(0, 0, dim.width, dim.height);

                while (Keyboard.next()) {
                    if (Keyboard.getEventKeyState()) {
                        for (IKeyCallback c : MAS.getMAS().getGLKeyListeners()) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (!c.keyPressed(Keyboard.getEventKey())) MAS.getMAS().removeListener(c);
                                }
                            });
                        }
                    }
                }

                camera.move();

                Renderer.prepare();
                shader.start();
                shader.loadViewMatrix(camera);
                Renderer.initProjectionMatrix();

                for (Entity e : MAS.getMAS().getProject().getAllEntities()) {
                    Renderer.renderEntity(e, shader);
                }

                shader.stop();

                tShader.start();
                tShader.loadViewMatrix(camera);
                Renderer.renderTerrains(terrains, tShader);
                tShader.stop();

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

    /**
     * @return the camera
     */
    public static Camera getCamera() {
        return camera;
    }
}
