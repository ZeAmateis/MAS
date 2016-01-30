package mas.project;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import mas.entity.Entity;

/**
 * @author SCAREX
 *
 */
public class MASProject
{
    protected String name;
    protected List<IMASProjectElement> elements;
    protected MASProjectDirectory rootDirectory;
    protected DefaultTreeModel treeModel;
    protected Entity selectedEntity;

    public MASProject(String name, List<IMASProjectElement> elements) {
        this.name = name;
        this.elements = elements;
        this.rootDirectory = new MASProjectDirectory(name, new ArrayList<IMASProjectElement>());
        this.treeModel = new MASProjectTreeModel(new MASProjectDirectory(this.getName(), elements));
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the selectedEntity
     */
    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    /**
     * @param selectedEntity the selectedEntity to set
     */
    public void setSelectedEntity(Entity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    /**
     * @return the elements
     */
    public List<IMASProjectElement> getElements() {
        return elements;
    }

    /**
     * @param elements
     *            the elements to set
     */
    public void setElements(List<IMASProjectElement> elements) {
        this.elements = elements;
    }

    public void addElement(IMASProjectElement element) {
        this.elements.add(element);
        ((MASProjectDirectory) this.treeModel.getRoot()).add((MutableTreeNode) element);
        this.treeModel.reload();
    }

    public void addElement(MASProjectDirectory parent, DefaultMutableTreeNode element) {
        parent.add(element);
        this.treeModel.reload();
    }

    public void removeElement(IMASProjectElement element) {
        this.elements.remove(element);
    }

    public void removeElementInTree(IMASProjectElement element) {
        ((DefaultMutableTreeNode) element).removeFromParent();
        this.treeModel.reload();
    }

    public TreeModel getTreeModel() {
        return this.treeModel;
    }

    public List<Entity> getAllEntities() {
        return getEntitiesInDirectory((MASProjectDirectory) this.treeModel.getRoot());
    }

    protected List<Entity> getEntitiesInDirectory(MASProjectDirectory dir) {
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for (int i = 0; i < ((DefaultMutableTreeNode) dir).getChildCount(); i++) {
            IMASProjectElement e = (IMASProjectElement) ((DefaultMutableTreeNode) dir).getChildAt(i);
            if (e instanceof MASProjectDirectory) {
                entities.addAll(getEntitiesInDirectory((MASProjectDirectory) e));
            } else {
                entities.add((Entity) e);
            }
        }
        return entities;
    }

    /**
     * @return the rootDirectory
     */
    public MASProjectDirectory getRootDirectory() {
        return rootDirectory;
    }

    public static class MASProjectTreeModel extends DefaultTreeModel
    {
        private static final long serialVersionUID = 582385433844958162L;

        public MASProjectTreeModel(TreeNode root) {
            super(root);
        }

        @Override
        public boolean isLeaf(Object node) {
            return !(node instanceof MASProjectDirectory);
        }
    }
}
