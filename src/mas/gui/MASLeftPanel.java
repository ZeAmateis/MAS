package mas.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author SCAREX
 * 
 */
public class MASLeftPanel extends JPanel
{
    private static final long serialVersionUID = 7629643125798843681L;
    private JTree cubeTree;

    public MASLeftPanel() {
        super();

        this.initGui();
    }

    private void initGui() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane treeScrollPane = new JScrollPane(this.cubeTree = new JTree(new DefaultMutableTreeNode("MAS is a really cool project")));
        this.add(treeScrollPane);
    }
}
