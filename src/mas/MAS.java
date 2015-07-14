package mas;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * @author SCAREX
 * 
 */
public class MAS extends JFrame
{
	private static final long serialVersionUID = -285116092617886296L;
	private static MAS INSTANCE;
	private boolean run = true;
	private final AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();

	private final Canvas modelCanvas = new Canvas();

	public static void main(String[] args) {
		INSTANCE = new MAS();
	}

	public static MAS getMAS() {
		return INSTANCE;
	}

	public MAS() {
		this.setTitle("MAS");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLayout(new BorderLayout());

		this.initCanvas();

		this.setMinimumSize(new Dimension(400, 400));
		this.pack();
		this.setVisible(true);

		this.initDisplay();
	}

	private void initCanvas() {
		modelCanvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				newCanvasSize.set(modelCanvas.getSize());
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("closing");
				run = false;
			}
		});

		this.add(modelCanvas, BorderLayout.CENTER);

		try {
			Display.setParent(this.modelCanvas);
			Display.setVSyncEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initDisplay() {
		try {
			Display.create();

			Dimension dim;
			while (!Display.isCloseRequested() && run) {
				dim = newCanvasSize.getAndSet(null);

				if (dim != null) {
					GL11.glViewport(0, 0, dim.width, dim.height);
				}

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Display.update();
			}
			Display.destroy();
			this.dispose();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
