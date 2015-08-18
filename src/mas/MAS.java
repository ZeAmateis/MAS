package mas;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.opengl.Display;

import mas.render.ThreadRendering;

/**
 * @author SCAREX
 * 
 */
public class MAS extends JFrame {
	private static final long serialVersionUID = -285116092617886296L;
	private static MAS INSTANCE;
	private boolean isRunning = true;

	private final AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
	private final Canvas modelCanvas = new Canvas();

	private MASMenuBar menuBar = new MASMenuBar();

    public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // setup lwjgl natives
            LWJGLSetup.load(SystemUtils.getAppFolder("MAS"));
            MASLang.load(SystemUtils.getAppFolder("MAS"));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        INSTANCE = new MAS();
	}

	public static MAS getMAS() {
		return INSTANCE;
	}

	private MAS() {
		this.setTitle("MAS");
		this.setIconImage(this.getToolkit().getImage(MAS.class.getResource("/logo.png")));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isRunning = false;
			}
		});
		this.setLayout(new BorderLayout());

		this.setJMenuBar(this.menuBar);

		this.initCanvas();

		JPanel leftPanel = new MASLeftPanel();
		this.add(leftPanel, BorderLayout.WEST);

		JPanel rightPanel = new MASRightPanel();
		this.add(rightPanel, BorderLayout.EAST);

		this.setMinimumSize(new Dimension(600, 600));
		this.setLocationRelativeTo(null);
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

		this.add(modelCanvas, BorderLayout.CENTER);

		try {
			Display.setParent(this.modelCanvas);
			Display.setVSyncEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initDisplay() {
		Thread threadRendering = new ThreadRendering(this);
		threadRendering.start();
	}

	public void shutDown() {
		this.isRunning = false;
	}

    public AtomicReference<Dimension> getNewCanvasSize() {
        return newCanvasSize;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
