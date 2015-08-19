package mas.render;

import java.awt.Dimension;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import mas.MAS;
import mas.shaders.BasicShader;

public class ThreadRendering extends Thread
{

    private final MAS mas;

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

            float[] vertices = { -0.5F,
                    0.5F, 0.0F, -0.5F,
                    -0.5F, 0.0F, 0.5F,
                    -0.5F, 0.0F, 0.5F,
                    0.5F, 0.0F };
            int[] indices = { 0, 1, 3,
                    3, 1, 2 };
            RawModel model = ModelLoader.loadToVAO(vertices, indices);

            while (!Display.isCloseRequested() && mas.isRunning()) {
                dim = mas.getNewCanvasSize().getAndSet(null);
                if (dim != null) GL11.glViewport(0, 0, dim.width, dim.height);
                Renderer.prepare();
                shader.start();

                Renderer.render(model);

                shader.stop();
                Display.update();
            }
            shader.cleanUp();
            ModelLoader.cleanUp();
            Display.destroy();
            MAS.getMAS().dispose();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }
}
