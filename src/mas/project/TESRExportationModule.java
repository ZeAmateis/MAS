package mas.project;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.io.Files;

import mas.MAS;
import mas.MASLang;
import mas.entity.Entity;
import mas.utils.filewriting.ConstructorFileWriter;
import mas.utils.filewriting.FieldFileWriter;
import mas.utils.filewriting.JavaFileWriter;
import mas.utils.filewriting.MethodFileWriter;

/**
 * @author SCAREX
 *
 */
@MASModule
public class TESRExportationModule implements IMASExportationModule
{
    public void start() {
        MAS.getMAS().addExportationModule(this);
    }

    @Override
    public String getName() {
        return "TESR";
    }

    @Override
    public void export(MASProject project) {
        MAS.getMAS().getStateBar().getProgBar().setIndeterminate(true);
        MAS.getMAS().getStateBar().setLabel("Exporting...");
        JPanel optionPane = new JPanel(new GridLayout(4, 2));
        JCheckBox tesrCheckBox = new JCheckBox(MASLang.translate("exportation.tesr.generate_tesr"), null, true);
        JTextField tesrNameField = new JTextField(20);
        JTextField tesrPackage = new JTextField(20);
        tesrCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tesrNameField.setEnabled(tesrCheckBox.isSelected());
                tesrPackage.setEnabled(tesrCheckBox.isSelected());
            }
        });
        optionPane.add(tesrCheckBox);
        optionPane.add(tesrNameField);

        optionPane.add(new JLabel(MASLang.translate("exportation.tesr.tesr_package_name")));
        optionPane.add(tesrPackage);

        optionPane.add(new JLabel(MASLang.translate("exportation.tesr.model_name")));
        JTextField modelNameField = new JTextField(20);
        optionPane.add(modelNameField);

        optionPane.add(new JLabel(MASLang.translate("exportation.tesr.model_package_name")));
        JTextField modelPackage = new JTextField(20);
        optionPane.add(modelPackage);

        int ret = JOptionPane.showOptionDialog(null, optionPane, "Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        MAS.getMAS().getStateBar().getProgBar().setIndeterminate(false);
        if (ret == JOptionPane.OK_OPTION) {
            MAS.getMAS().getStateBar().getProgBar().setMaximum(2);
            if (tesrCheckBox.isSelected()) {
                MAS.getMAS().getStateBar().getProgBar().setMaximum(3);
                JFileChooser tesrFc = new JFileChooser();
                tesrFc.setSelectedFile(new File("", tesrNameField.getText() + ".java"));
                tesrFc.setFileFilter(new FileNameExtensionFilter("JAVA File", "java"));
                int tesrRet = tesrFc.showSaveDialog(MAS.getMAS());
                if (tesrRet != 0) return;
                File f = tesrFc.getSelectedFile();
                MAS.getMAS().getStateBar().setLabel("Exporting TESR");
                try {
                    exportTESR(f, tesrNameField.getText(), tesrPackage.getText(), modelNameField.getText(), modelPackage.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                MAS.getMAS().getStateBar().getProgBar().setValue(1);
            }
            JFileChooser modelFc = new JFileChooser();
            modelFc.setSelectedFile(new File("", modelNameField.getText() + ".java"));
            modelFc.setFileFilter(new FileNameExtensionFilter("JAVA file", "java"));
            int modelRet = modelFc.showSaveDialog(MAS.getMAS());
            if (modelRet != 0) return;
            MAS.getMAS().getStateBar().getProgBar().setValue(MAS.getMAS().getStateBar().getProgBar().getValue() + 1);
            MAS.getMAS().getStateBar().setLabel("Exporting model");
            try {
                exportModel(modelFc.getSelectedFile(), modelNameField.getText(), modelPackage.getText(), project);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            MAS.getMAS().getStateBar().getProgBar().setValue(MAS.getMAS().getStateBar().getProgBar().getMaximum());
            MAS.getMAS().getStateBar().setLabel("TESR exported");
        }
    }

    public static void exportTESR(File f, String name, String tesrPackage, String modelName, String modelPackage) throws IOException {
        JavaFileWriter jfw = new JavaFileWriter(JavaFileWriter.PUBLIC, name, tesrPackage);
        jfw.setExtension("TileEntitySpecialRenderer");
        jfw.addPackage("org.lwjgl.opengl.GL11");
        jfw.addPackage("net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer");
        jfw.addPackage("net.minecraft.tileentity.TileEntity");

        jfw.addField(new FieldFileWriter(JavaFileWriter.PUBLIC | JavaFileWriter.STATIC | JavaFileWriter.FINAL, "MODEL", modelName, true, null));

        MethodFileWriter renderMethod = new MethodFileWriter(JavaFileWriter.PUBLIC, "renderTileEntityAt", "void", new ArrayList<>());
        renderMethod.addAnnotation("Override");
        renderMethod.addParameter("TileEntity", "tile");
        renderMethod.addParameter("double", "x");
        renderMethod.addParameter("double", "y");
        renderMethod.addParameter("double", "z");
        renderMethod.addParameter("float", "prt");

        renderMethod.addLine("GL11.glPushMatrix();");
        renderMethod.addLine("GL11.glTranslated(x, y, z);");
        renderMethod.addLine("MODEL.renderAll();");
        renderMethod.addLine("GL11.glPopMatrix();");
        jfw.addMethod(renderMethod);
        Files.write(jfw.toString(), f, Charset.forName("UTF-8"));
    }

    public static void exportModel(File f, String modelName, String modelPackage, MASProject project) throws IOException {
        JavaFileWriter jfw = new JavaFileWriter(JavaFileWriter.PUBLIC, modelName, modelPackage);
        jfw.setExtension("ModelBase");
        jfw.addPackage("net.minecraft.client.model.ModelBase");
        jfw.addPackage("net.minecraft.client.model.ModelRenderer");

        ArrayList<String> renderLines = new ArrayList<>();
        ArrayList<String> constructorLines = new ArrayList<>();
        constructorLines.add("this.textureWidth = 512;");
        constructorLines.add("this.textureHeight = 512;");
        constructorLines.add("");
        List<Entity> entities = project.getAllEntities();
        entities.forEach(entity -> {
            String formattedName = FieldFileWriter.formatName((String) entity.getUserObject(), JavaFileWriter.PUBLIC);
            jfw.addField(new FieldFileWriter(JavaFileWriter.PUBLIC, formattedName, "ModelRenderer", false, null));
            renderLines.add("this." + formattedName + ".render(0.0625F);");
            constructorLines.add("this." + formattedName + " = new ModelRenderer(this, 0, 0);");
            Vector3f pos = entity.getPosition();
            constructorLines.add("this." + formattedName + ".addBox(" + pos.getX() * 16F + "F, " + pos.getY() * 16F + "F, " + pos.getZ() * 16F + "F, " + entity.getScaleX() + ", " + entity.getScaleY() + ", " + entity.getScaleZ() + ");");
            Vector3f offset = entity.getOffset();
            constructorLines.add("this." + formattedName + ".setRotationPoint(" + offset.getX() * 16F + "F, " + offset.getY() * 16F + "F, " + offset.getZ() * 16F + "F);");
            constructorLines.add("this." + formattedName + ".setTextureSize(0, 0);");
            constructorLines.add("setRotation(this." + formattedName + ", (float) Math.toRadians(" + entity.getRotationX() + "), (float) Math.toRadians(" + entity.getRotationY() + "), (float) Math.toRadians(" + entity.getRotationZ() + "));");
        });

        jfw.addConstructor(new ConstructorFileWriter(JavaFileWriter.PUBLIC, modelName, constructorLines, new ArrayList<>()));

        MethodFileWriter renderMethod = new MethodFileWriter(JavaFileWriter.PUBLIC, "renderAll", "void", new ArrayList<>());
        renderMethod.getLines().addAll(renderLines);
        jfw.addMethod(renderMethod);

        MethodFileWriter rotationMethod = new MethodFileWriter(JavaFileWriter.PUBLIC | JavaFileWriter.STATIC, "setRotation", "void", new ArrayList<>());
        rotationMethod.addParameter("ModelRenderer", "model");
        rotationMethod.addParameter("float", "x");
        rotationMethod.addParameter("float", "y");
        rotationMethod.addParameter("float", "z");
        rotationMethod.addLine("model.rotateAngleX = x;");
        rotationMethod.addLine("model.rotateAngleY = y;");
        rotationMethod.addLine("model.rotateAngleZ = z;");
        jfw.addMethod(rotationMethod);
        Files.write(jfw.toString(), f, Charset.forName("UTF-8"));
    }
}
