package mas.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import mas.MAS;
import mas.project.MASProject;

/**
 * @author SCAREX
 *
 */
public class MASStateBar extends JPanel
{
    private static final long serialVersionUID = 7886857069396188415L;
    protected final JProgressBar progBar = new JProgressBar();
    protected final JLabel labelBar = new JLabel("");

    public MASStateBar() {
        super();

        this.initGui();
    }

    private void initGui() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(Box.createRigidArea(new Dimension(10, 20)));

        this.add(new JLabel(MAS.VERSION));

        this.add(new JSeparator(SwingConstants.VERTICAL));

        this.add(new JLabel(MASProject.COMPILER_VERSION));

        this.add(new JSeparator(SwingConstants.VERTICAL));

        this.progBar.setMaximum(-1);
        this.progBar.setStringPainted(true);
        this.add(this.progBar);

        this.add(this.labelBar);
    }

    /**
     * @return the progBar
     */
    public JProgressBar getProgBar() {
        return progBar;
    }

    public void setLabel(String s) {
        this.labelBar.setText(s);
    }
}
