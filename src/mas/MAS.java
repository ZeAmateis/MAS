package mas;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;

import mas.MASMainConfig.EnumMainConfig;
import mas.config.IKeyCallback;
import mas.gui.MASLeftPanel;
import mas.gui.MASMenuBar;
import mas.gui.MASRightPanel;
import mas.gui.MASStateBar;
import mas.project.IMASProjectElement;
import mas.project.MASProject;
import mas.render.ThreadRendering;

/**
 * @author SCAREX
 * 
 */
public class MAS extends JFrame
{
    private static final long serialVersionUID = -285116092617886296L;
    private static MAS INSTANCE;
    public static final String VERSION = "=masversion=";
    public static final boolean DEBUG = "%DEBUG%" == "%" + "DEBUG%";
    private boolean isRunning = true;
    private ArrayList<IKeyCallback> keyListeners = new ArrayList<IKeyCallback>();
    public static File LOGS_FOLDER;
    public static final SimpleDateFormat SIMPLE_TIME = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    public static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            try {
                System.err.println("Error found in thread " + t.getName() + " : " + e.getMessage());
                e.printStackTrace();
                String name = t.getName() + "-" + SIMPLE_TIME.format(new Date());
                File f;
                byte i = 0;
                while ((f = new File(LOGS_FOLDER, name + "-" + i + ".log")).exists()) {
                    i++;
                }
                f.createNewFile();
                System.err.println("Error will be saved in file " + f.getAbsolutePath());
                PrintStream ps = new PrintStream(f);
                e.printStackTrace(ps);
            } catch (Exception e1) {
                System.err.println("Cannot write crash report !");
                e1.printStackTrace();
            }
        }
    };
    public static final PrintStream LOG_BASE_STREAM = System.out;
    public static final PrintStream LOG_BASE_ERR_STREAM = System.err;
    public static PrintStream LOG_FILE_STREAM;
    public static File CONFIG_DIRECTORY;
    public static final OutputStream LOG_STREAM = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            LOG_BASE_STREAM.write(b);
            LOG_FILE_STREAM.write(b);
        }
    };
    public static final OutputStream LOG_ERR_STREAM = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            LOG_BASE_ERR_STREAM.write(b);
            LOG_FILE_STREAM.write(b);
        }
    };

    private final AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    private AWTGLCanvas modelCanvas;

    private MASProject project;

    public static void main(String[] args) {
        try {
            MASMainConfig.generateAndLoad();

            LOGS_FOLDER = new File(MASMainConfig.getValue(EnumMainConfig.APP_PATH), "logs");
            LOGS_FOLDER.mkdirs();

            File latestLogsFile = new File(LOGS_FOLDER, "latest_logs.log");
            latestLogsFile.createNewFile();
            LOG_FILE_STREAM = new PrintStream(latestLogsFile);
            System.setOut(new PrintStream(LOG_STREAM));
            System.setErr(new PrintStream(LOG_ERR_STREAM));

            Thread.currentThread().setUncaughtExceptionHandler(MAS.EXCEPTION_HANDLER);

            System.out.println("Starting MAS " + MAS.VERSION);
            System.out.println("Compiler version : " + MASProject.COMPILER_VERSION);

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // setup lwjgl natives
            LWJGLSetup.load(new File(MASMainConfig.getValue(EnumMainConfig.APP_PATH)));
            MASLang.load(new File(MASMainConfig.getValue(EnumMainConfig.APP_PATH)));

            CONFIG_DIRECTORY = new File(MASMainConfig.getValue(EnumMainConfig.APP_PATH), "config");
            CONFIG_DIRECTORY.mkdirs();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        INSTANCE = new MAS();
    }

    public static MAS getMAS() {
        return INSTANCE;
    }

    private final MASMenuBar menuBar = new MASMenuBar();
    private final MASStateBar stateBar = new MASStateBar();
    private final MASLeftPanel LEFT_PANEL;
    private final MASRightPanel RIGHT_PANEL;

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

        LEFT_PANEL = new MASLeftPanel();
        this.add(LEFT_PANEL, BorderLayout.WEST);

        RIGHT_PANEL = new MASRightPanel();
        this.add(RIGHT_PANEL, BorderLayout.EAST);

        this.add(this.stateBar, BorderLayout.SOUTH);

        this.setMinimumSize(new Dimension(600, 600));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.initDisplay();

        project = new MASProject(MASLang.translate("menu.file.new"), new ArrayList<IMASProjectElement>());
        LEFT_PANEL.setModel(project.getTreeModel());
    }

    private void initCanvas() {
        try {
            this.modelCanvas = new AWTGLCanvas();
        } catch (LWJGLException e1) {
            e1.printStackTrace();
        }
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
        threadRendering.setUncaughtExceptionHandler(MAS.EXCEPTION_HANDLER);
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

    /**
     * @return the modelCanvas
     */
    public Canvas getModelCanvas() {
        return modelCanvas;
    }

    /**
     * Register an OpenGL key listener
     */
    public void registerKeyListener(IKeyCallback callback) {
        this.keyListeners.add(callback);
    }

    /**
     * Remove a specific listener
     */
    public void removeListener(Object o) {
        this.keyListeners.remove(o);
    }

    /**
     * @return keyListeners
     */
    public ArrayList<IKeyCallback> getGLKeyListeners() {
        return this.keyListeners;
    }

    /**
     * @return the project
     */
    public MASProject getProject() {
        return project;
    }

    /**
     * @param project
     *            the project to set
     */
    public void setProject(MASProject mproject) {
        project = mproject;
        MAS.getMAS().getLEFT_PANEL().setModel(project.getTreeModel());
        MAS.getMAS().getLEFT_PANEL().getTree().updateUI();
    }

    /**
     * @return the lEFT_PANEL
     */
    public MASLeftPanel getLEFT_PANEL() {
        return LEFT_PANEL;
    }

    /**
     * @return the rIGHT_PANEL
     */
    public MASRightPanel getRIGHT_PANEL() {
        return RIGHT_PANEL;
    }

    /**
     * @return the stateBar
     */
    public MASStateBar getStateBar() {
        return stateBar;
    }
}
