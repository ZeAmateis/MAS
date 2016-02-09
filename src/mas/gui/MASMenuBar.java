package mas.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.tree.DefaultMutableTreeNode;

import mas.MAS;
import mas.MASLang;
import mas.MASMainConfig;
import mas.MASMainConfig.EnumMainConfig;
import mas.project.IMASProjectElement;
import mas.project.MASProject;

/**
 * @author SCAREX
 * 
 */
public class MASMenuBar extends JMenuBar implements ActionListener, MenuListener
{
    private static final long serialVersionUID = -6394631766691329465L;
    private JMenu filesMenu = new JMenu(MASLang.translate("menu.file"));
    private JMenu editMenu = new JMenu(MASLang.translate("menu.edit"));
    private JMenu helpMenu = new JMenu(MASLang.translate("menu.help"));
    // File
    private final JMenuItem newItem = createAltMenuItem(MASLang.translate("menu.file.new"), KeyEvent.VK_N);
    private final JMenuItem openItem = createAltMenuItem(MASLang.translate("menu.file.open"), KeyEvent.VK_O);
    private final JMenuItem quitItem = createAltMenuItem(MASLang.translate("menu.file.quit"), KeyEvent.VK_Q);
    private final JMenuItem saveItem = createCtrlMenuItem(MASLang.translate("menu.file.save"), KeyEvent.VK_S);
    private final JMenuItem saveAsItem = createAltMenuItem(MASLang.translate("menu.file.save_as"), KeyEvent.VK_S);
    private final JMenu exportMenu = new JMenu(MASLang.translate("menu.file.export"));
    private final JMenuItem noneExportationModuleItem = new JMenuItem(MASLang.translate("menu.file.export.none"));
    // Edit
    private final JMenuItem undoItem = createCtrlMenuItem(MASLang.translate("menu.edit.undo"), KeyEvent.VK_Z);
    private final JMenuItem redoItem = createCtrlMenuItem(MASLang.translate("menu.edit.redo"), KeyEvent.VK_Y);
    private final JMenuItem deleteItem = createAltMenuItem(MASLang.translate("menu.edit.delete"), KeyEvent.VK_DELETE);
    private final JMenuItem copyItem = createCtrlMenuItem(MASLang.translate("menu.edit.copy"), KeyEvent.VK_C);
    private final JMenuItem cutItem = createCtrlMenuItem(MASLang.translate("menu.edit.cut"), KeyEvent.VK_X);
    private final JMenuItem pasteItem = createCtrlMenuItem(MASLang.translate("menu.edit.paste"), KeyEvent.VK_V);
    private final JMenuItem preferencesItem = createAltMenuItem(MASLang.translate("menu.edit.preferences"), KeyEvent.VK_P);
    // Help
    private final JMenuItem gitItem = createSimpleMenuItem(MASLang.translate("menu.help.git"));
    private final JMenuItem langProjectItem = createSimpleMenuItem(MASLang.translate("menu.help.langProject"));
    private final JMenuItem scarexWebsiteItem = createSimpleMenuItem(MASLang.translate("menu.help.scarexWebsite"));
    private final JMenuItem reloadLangFiles = createSimpleMenuItem(MASLang.translate("menu.help.reload_lang_files"));

    public MASMenuBar() {
        super();

        this.initFilesMenu();
        this.initEditMenu();
        this.initHelpMenu();
    }

    private void initFilesMenu() {
        this.filesMenu.add(this.newItem);
        this.filesMenu.add(this.openItem);

        this.filesMenu.addSeparator();

        this.filesMenu.add(this.saveItem);
        this.filesMenu.add(this.saveAsItem);
        this.filesMenu.add(this.exportMenu);
        this.exportMenu.addMenuListener(this);
        this.noneExportationModuleItem.setEnabled(false);

        this.filesMenu.addSeparator();

        this.filesMenu.add(this.quitItem);

        this.add(this.filesMenu);
    }

    private void initEditMenu() {
        this.editMenu.add(this.undoItem);
        this.editMenu.add(this.redoItem);

        this.editMenu.addSeparator();

        this.editMenu.add(this.deleteItem);

        this.editMenu.addSeparator();

        this.editMenu.add(this.copyItem);
        this.editMenu.add(this.cutItem);
        this.editMenu.add(this.pasteItem);

        this.editMenu.addSeparator();

        this.editMenu.add(this.preferencesItem);

        this.add(this.editMenu);
    }

    private void initHelpMenu() {
        this.helpMenu.add(this.gitItem);
        this.helpMenu.add(this.langProjectItem);
        this.helpMenu.add(this.scarexWebsiteItem);
        this.helpMenu.add(this.reloadLangFiles);

        this.add(this.helpMenu);
    }

    public JMenuItem createAltMenuItem(String text, int key) {
        JMenuItem j = new JMenuItem(text, key);
        j.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.ALT_MASK));
        j.setMnemonic(key);
        j.setDisplayedMnemonicIndex(0);
        j.addActionListener(this);
        return j;
    }

    public JMenuItem createCtrlMenuItem(String text, int key) {
        JMenuItem j = new JMenuItem(text, key);
        j.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
        j.addActionListener(this);
        return j;
    }

    public JMenuItem createSimpleMenuItem(String text) {
        JMenuItem j = new JMenuItem(text);
        j.addActionListener(this);
        return j;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.quitItem) {
            MAS.getMAS().shutDown();
        } else if (e.getSource() == this.gitItem) {
            browse("https://github.com/SCAREXgaming/MAS");
        } else if (e.getSource() == this.scarexWebsiteItem) {
            browse("http://scarex.fr");
        } else if (e.getSource() == this.langProjectItem) {
            browse("https://github.com/SCAREXgaming/MASLang");
        } else if (e.getSource() == this.preferencesItem) {
            PreferencesFrame frame = new PreferencesFrame();
            frame.setVisible(true);
        } else if (e.getSource() == this.saveItem) {
            MAS.getMAS().getProject().saveProject();
        } else if (e.getSource() == this.saveAsItem) {
            MAS.getMAS().getProject().saveAsProject();
        } else if (e.getSource() == this.openItem) {
            MASProject.openFile();
        } else if (e.getSource() == this.newItem) {
            MAS.getMAS().setProject(new MASProject(MASLang.translate("menu.file.new"), new ArrayList<>()));
        } else if (e.getSource() == this.deleteItem) {
            if (MAS.getMAS().getLEFT_PANEL().getTree().getLastSelectedPathComponent() != null) {
                int ret = JOptionPane.showConfirmDialog(MAS.getMAS(), MASLang.translate("menu.edit.delete.confirmation", ((DefaultMutableTreeNode) MAS.getMAS().getLEFT_PANEL().getTree().getLastSelectedPathComponent()).getUserObject()), null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    MAS.getMAS().getProject().removeElementInTree((IMASProjectElement) MAS.getMAS().getLEFT_PANEL().getTree().getLastSelectedPathComponent());
                }
            }
        } else if (e.getSource() == this.reloadLangFiles) {
            MAS.getMAS().getStateBar().getProgBar().setIndeterminate(true);
            MAS.getMAS().getStateBar().setLabel("Reloading lang files...");
            MASLang.load(new File(MASMainConfig.getValue(EnumMainConfig.APP_PATH)));
            MAS.getMAS().getStateBar().getProgBar().setIndeterminate(false);
            MAS.getMAS().getStateBar().setLabel("Lang files loaded");
        }
    }

    public static void browse(String uri) {
        try {
            Desktop.getDesktop().browse(new URI(uri));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void menuSelected(MenuEvent e) {
        if (e.getSource() == this.exportMenu) {
            this.exportMenu.removeAll();
            if (MAS.getMAS().getExportationModuleList().size() > 0) {
                MAS.getMAS().getExportationModuleList().forEach(module -> {
                    JMenuItem item = new JMenuItem(module.getName());
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            module.export(MAS.getMAS().getProject());
                        }
                    });
                    this.exportMenu.add(item);
                });
            } else {
                this.exportMenu.add(this.noneExportationModuleItem);
            }
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}
}
