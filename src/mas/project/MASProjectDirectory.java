package mas.project;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * @author SCAREX
 *
 */
public class MASProjectDirectory extends DefaultMutableTreeNode implements IMASProjectElement
{
    private static final long serialVersionUID = 9195648370785681430L;

    public MASProjectDirectory(String name, List<IMASProjectElement> elements) {
        super(name);
        for (int i = 0; i < elements.size(); i++) {
            this.insert((MutableTreeNode) elements.get(i), i);
        }
    }
}
