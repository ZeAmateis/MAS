package mas.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * @author SCAREX
 * 
 */
public class MASLeftPanel extends JPanel
{
    private static final long serialVersionUID = 7629643125798843681L;

    public MASLeftPanel() {
        super();

        this.initGui();
    }

    private void initGui() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(300, 20)));

        this.setMinimumSize(new Dimension(400, 200));
    }
}
