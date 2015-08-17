package mas.render;

import java.awt.Dimension;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import mas.MAS;

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
            while (!Display.isCloseRequested() && mas.isRunning()) {
                dim = mas.getNewCanvasSize().getAndSet(null);
                if (dim != null) GL11.glViewport(0, 0, dim.width, dim.height);
                Renderer.prepare();

                // Render...

                Display.update();
            }
            ModelLoader.cleanUp();
            Display.destroy();
            MAS.getMAS().dispose();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
