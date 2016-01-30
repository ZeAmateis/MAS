package mas.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.lwjgl.util.vector.Vector3f;

import mas.MAS;
import mas.MASLang;
import mas.entity.Entity;
import mas.project.IMASProjectElement;
import mas.project.MASProjectDirectory;
import mas.render.ThreadRendering;
import mas.utils.SwingUtils;

/**
 * @author SCAREX
 * 
 */
public class MASLeftPanel extends JPanel
{
    private static final long serialVersionUID = 7629643125798843681L;
    protected JTree tree = new JTree();

    public MASLeftPanel() {
        super();

        this.initGui();
    }

    private void initGui() {
        this.setLayout(new BorderLayout());
        this.add(Box.createRigidArea(new Dimension(200, 2)), BorderLayout.NORTH);
        this.add(this.tree, BorderLayout.CENTER);
        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) return;
                    tree.setSelectionPath(path);
                    IMASProjectElement element = (IMASProjectElement) path.getLastPathComponent();
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem modifyItem = new JMenuItem(MASLang.translate("project.modify"));
                    modifyItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String s = JOptionPane.showInputDialog(null, MASLang.translate("project.get_name"));
                            if (s != null && !s.isEmpty()) {
                                MAS.getMAS().getProject().changeName((DefaultMutableTreeNode) element, s);
                            }
                        }
                    });
                    popup.add(modifyItem);
                    JMenuItem deleteItem = new JMenuItem(MASLang.translate("project.delete"));
                    deleteItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            delete(element);
                        }
                    });
                    popup.add(deleteItem);
                    if (element instanceof MASProjectDirectory) {
                        JMenu addMenu = new JMenu(MASLang.translate("project.add"));
                        JMenuItem dirItem = new JMenuItem(MASLang.translate("project.directory"));
                        dirItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String s = JOptionPane.showInputDialog(null, MASLang.translate("project.get_name"));
                                if (s != null && !s.isEmpty()) {
                                    MAS.getMAS().getProject().addElement((MASProjectDirectory) element, new MASProjectDirectory(s, new ArrayList<>()));
                                }
                            }
                        });
                        addMenu.add(dirItem);
                        JMenuItem entityItem = new JMenuItem(MASLang.translate("project.entity"));
                        entityItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String s = JOptionPane.showInputDialog(null, MASLang.translate("project.get_name"));
                                if (s != null && !s.isEmpty()) {
                                    MAS.getMAS().getProject().addElement((MASProjectDirectory) element, new Entity(s, ThreadRendering.TEXTURED_MODEL_TEST, new Vector3f(), 0, 0, 0, new Vector3f(1F, 1F, 1F)));
                                }
                            }
                        });
                        addMenu.add(entityItem);
                        popup.add(addMenu);
                    }
                    popup.show(tree, e.getX(), e.getY());
                }
            }
        });
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (e.getPath().getLastPathComponent() instanceof Entity) {
                    MAS.getMAS().getProject().setSelectedEntity((Entity) e.getPath().getLastPathComponent());
                    SwingUtils.changeComponentsState(MAS.getMAS().getRIGHT_PANEL(), true);
                    MAS.getMAS().getRIGHT_PANEL().loadValuesForEntity(MAS.getMAS().getProject().getSelectedEntity());
                } else {
                    SwingUtils.changeComponentsState(MAS.getMAS().getRIGHT_PANEL(), false);
                }
            }
        });

        this.setMinimumSize(new Dimension(400, 200));
    }

    public void delete(IMASProjectElement element) {
        MAS.getMAS().getProject().removeElementInTree(element);
    }

    public void setModel(TreeModel model) {
        this.tree.setModel(model);
    }
}
