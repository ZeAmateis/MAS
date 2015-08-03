package mas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ThreadRendering extends Thread {

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

                if (dim != null) {
                    GL11.glViewport(0, 0, dim.width, dim.height);
                }

                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                Display.update();
            }
            Display.destroy();
            MAS.getMAS().dispose();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
