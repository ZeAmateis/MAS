package mas.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.lwjgl.util.vector.Vector3f;

import mas.MAS;
import mas.entity.Entity;
import mas.render.ThreadRendering;

/**
 * @author SCAREX
 *
 */
public class MASProject
{
    public static final String COMPILER_VERSION = "=compiler-version=";
    protected String name;
    protected String path;
    protected DefaultTreeModel treeModel;
    protected Entity selectedEntity;

    public MASProject(String name, List<IMASProjectElement> elements) {
        this.name = name;
        this.treeModel = new MASProjectTreeModel(new MASProjectDirectory(this.getName(), elements));
    }

    public MASProject(String name, MASProjectDirectory root) {
        root.setUserObject(name);
        this.treeModel = new MASProjectTreeModel(root);
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
     * @param selectedEntity
     *            the selectedEntity to set
     */
    public void setSelectedEntity(Entity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public void addElement(MASProjectDirectory parent, DefaultMutableTreeNode element) {
        parent.add(element);
        this.treeModel.reload();
    }

    public void removeElementInTree(IMASProjectElement element) {
        ((DefaultMutableTreeNode) element).removeFromParent();
        this.treeModel.reload();
    }

    public void changeName(DefaultMutableTreeNode element, String name) {
        if (element.isRoot()) this.setName(name);
        element.setUserObject(name);
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

    public byte[] compileProject() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        writeUTF8String(b, COMPILER_VERSION);
        writeUTF8String(b, this.name);
        writeElement(b, (IMASProjectElement) this.treeModel.getRoot());
        return b.toByteArray();
    }

    public void saveProject() {
        File f;
        if (this.path != null && !this.path.isEmpty() && !(f = new File(path)).isDirectory() && f.getName().endsWith(this.name + ".mas")) {
            long t = System.currentTimeMillis();
            try {
                Files.write(f.toPath(), compileProject());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Time to compile : " + (System.currentTimeMillis() - t));
        } else {
            JFileChooser fc = new JFileChooser(this.path);
            f = new File("", this.name + ".mas");
            fc.setSelectedFile(f);
            fc.setFileFilter(new FileNameExtensionFilter("MAS File", "mas"));
            int ret = fc.showSaveDialog(MAS.getMAS());
            if (ret == JFileChooser.APPROVE_OPTION) {
                f = fc.getSelectedFile();
                this.path = f.getAbsolutePath();
                long t = System.currentTimeMillis();
                try {
                    Files.write(f.toPath(), compileProject());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Time to compile : " + (System.currentTimeMillis() - t));
            }
        }
    }

    public void saveAsProject() {
        JFileChooser fc = new JFileChooser(this.path);
        File f = this.path != null && !this.path.isEmpty() && this.path.endsWith(this.name + ".mas") ? new File(this.path) : new File("", this.name + ".mas");
        fc.setSelectedFile(f);
        fc.setFileFilter(new FileNameExtensionFilter("MAS File", "mas"));
        int ret = fc.showSaveDialog(MAS.getMAS());
        if (ret == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            this.path = f.getAbsolutePath();
            long t = System.currentTimeMillis();
            try {
                Files.write(f.toPath(), compileProject());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Time to compile : " + (System.currentTimeMillis() - t));
        }
    }

    public static void writeUTF8String(ByteArrayOutputStream b, String s) throws IOException {
        b.write(s.getBytes(Charset.forName("UTF-8")));
        b.write(-1);
    }

    public static void writeElement(ByteArrayOutputStream b, IMASProjectElement element) throws IOException {
        if (element instanceof MASProjectDirectory) {
            MASProjectDirectory d = (MASProjectDirectory) element;
            writeUTF8String(b, (String) d.getUserObject());
            b.write(-2);
            writeInt(b, d.getChildCount());
            for (int i = 0; i < d.getChildCount(); i++) {
                writeElement(b, (IMASProjectElement) d.getChildAt(i));
            }
        } else if (element instanceof Entity) {
            Entity e = (Entity) element;
            writeUTF8String(b, (String) e.getUserObject());
            b.write(-3);

            Vector3f vecPos = e.getPosition();
            writeFloat(b, vecPos.getX());
            writeFloat(b, vecPos.getY());
            writeFloat(b, vecPos.getZ());

            writeInt(b, e.getRotationX());
            writeInt(b, e.getRotationY());
            writeInt(b, e.getRotationZ());

            Vector3f vecScale = e.getScale();
            writeFloat(b, vecScale.getX());
            writeFloat(b, vecScale.getY());
            writeFloat(b, vecScale.getZ());
        }
    }

    public static void writeFloat(ByteArrayOutputStream b, float f) throws IOException {
        b.write(ByteBuffer.allocate(4).putFloat(f).array());
        b.write(-1);
    }

    public static void writeInt(ByteArrayOutputStream b, int i) throws IOException {
        b.write(ByteBuffer.allocate(4).putInt(i).array());
        b.write(-1);
    }

    public static MASProject decompile(byte[] bytes) throws IOException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        System.out.println("Compiled version : " + readUTF8String(b));
        String name = readUTF8String(b);
        System.out.println("Reading project : " + name);
        MASProjectDirectory root = (MASProjectDirectory) readElement(b);
        return new MASProject(name, root);
    }

    public static void openFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("MAS file", "mas"));
        int ret = fc.showOpenDialog(MAS.getMAS());
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                MAS.getMAS().setProject(decompile(Files.readAllBytes(f.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object[] readBytesUntil(ByteArrayInputStream bi, byte b) throws IOException {
        byte[] tb = new byte[1];
        ArrayList<Byte> ba = new ArrayList<>();
        int i = 0;
        while (bi.read(tb) != -1 && tb[0] != b) {
            ba.add(i, (Byte) tb[0]);
            i++;
        }
        return ba.toArray();
    }

    public static String readUTF8String(ByteArrayInputStream b) throws IOException {
        return new String(convertToPrimitiveBytes(readBytesUntil(b, (byte) -1)), Charset.forName("UTF-8"));
    }

    public static float readFloat(ByteArrayInputStream b) throws IOException {
        return ByteBuffer.wrap(convertToPrimitiveBytes(readBytesUntil(b, (byte) -1))).getFloat();
    }

    public static int readInt(ByteArrayInputStream b) throws IOException {
        return ByteBuffer.wrap(convertToPrimitiveBytes(readBytesUntil(b, (byte) -1))).getInt();
    }

    public static IMASProjectElement readElement(ByteArrayInputStream b) throws IOException {
        String s = readUTF8String(b);
        byte[] tb = new byte[1];
        b.read(tb);
        if (tb[0] == -2) {
            int size = readInt(b);
            IMASProjectElement[] elements = new IMASProjectElement[size];
            for (int i = 0; i < size; i++) {
                elements[i] = readElement(b);
            }
            return new MASProjectDirectory(s, Arrays.asList(elements));
        } else if (tb[0] == -3) {
            float posX = readFloat(b);
            float posY = readFloat(b);
            float posZ = readFloat(b);
            Vector3f vecPos = new Vector3f(posX, posY, posZ);

            int rotX = readInt(b);
            int rotY = readInt(b);
            int rotZ = readInt(b);

            float sizeX = readFloat(b);
            float sizeY = readFloat(b);
            float sizeZ = readFloat(b);
            Vector3f vecSize = new Vector3f(sizeX, sizeY, sizeZ);
            return new Entity(s, ThreadRendering.TEXTURED_MODEL_TEST, vecPos, rotX, rotY, rotZ, vecSize);
        }
        return null;
    }

    public static byte[] convertToPrimitiveBytes(Object[] b) {
        byte[] b0 = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            b0[i] = (byte) b[i];
        }
        return b0;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
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
