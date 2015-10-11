package mas.config;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.lwjgl.input.Keyboard;

import mas.MAS;
import mas.MASLang;
import mas.gui.PreferencesFrame;

/**
 * @author SCAREX
 *
 */
public class SimpleKeyBindingGLDescConfig implements IMASConfig
{
    protected PreferencesFrame parentFrame;
    protected final String name;
    protected TreeMap<String, Object[]> configMap = new TreeMap<String, Object[]>();

    public SimpleKeyBindingGLDescConfig(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getValue(Object key) {
        return this.configMap.get(key)[2];
    }

    @Override
    public Component getContent() {
        JTable table = new JTable(new ConfigModel());
        table.getColumnModel().getColumn(1).setCellEditor(new ButtonCellEditor());
        table.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        return new JScrollPane(table);
    }

    @Override
    public void setFrame(PreferencesFrame frame) {
        this.parentFrame = frame;
    }

    public int getKeyValue(String key) {
        return (int) this.configMap.get(key)[2];
    }

    public String getDescription(String key) {
        return (String) this.configMap.get(key)[0];
    }

    public int getDefaultValue(String key) {
        return (int) this.configMap.get(key)[1];
    }

    public void registerConfig(String key, int value) {
        this.configMap.put(key, new Object[] {
                MASLang.translate(this.getUnlocalizedName() + "." + key + ".desc"),
                value, value });
    }

    public class ConfigModel extends DefaultTableModel
    {
        private static final long serialVersionUID = -4830748003353664206L;
        protected String[] colNames = new String[2];
        
        public ConfigModel() {
            super();
            colNames[0] = MASLang.translate("config.key");
            colNames[1] = MASLang.translate("config.value");
        }

        @Override
        public int getRowCount() {
            return configMap.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int col) {
            return colNames[col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 1;
        }

        @Override
        public Object getValueAt(int row, int col) {
            String key = configMap.keySet().toArray(new String[0])[row];
            return col == 0 ? MASLang.translate(getUnlocalizedName() + "." + key) : Keyboard.getKeyName(getKeyValue(key));
        }
    }

    public class ButtonCellEditor extends DefaultCellEditor implements IKeyCallback
    {
        private static final long serialVersionUID = 1004992374406589753L;
        protected JButton button;
        protected boolean isPushed;
        protected String label;
        protected int row;

        public ButtonCellEditor() {
            super(new JCheckBox());
            this.button = new JButton();
            this.button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ButtonCellEditor.this.fireEditingStopped();
                }
            });
            this.button.setOpaque(true);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.label = value == null ? "" : value.toString();
            button.setText((String) value);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (this.isPushed) {
                MAS.getMAS().registerKeyListener(this);
                parentFrame.setState(JFrame.ICONIFIED);
            }
            this.isPushed = false;
            return this.label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        public boolean keyPressed(int key) {
            int i = JOptionPane.showConfirmDialog(this.button, MASLang.translate("config.keybindinggl.keypressed", Keyboard.getKeyName(key), key));
            switch (i) {
            case 0:
                String k = configMap.keySet().toArray(new String[0])[row];
                Object[] o = configMap.get(k);
                o[2] = key;
                configMap.put(k, o);
                parentFrame.setState(JFrame.NORMAL);
                break;
            case 1:
                return true;
            }
            parentFrame.setState(JFrame.NORMAL);
            return false;
        }
    }

    public class ConfigTableCellRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 3699372716031124686L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String key = (String) configMap.keySet().toArray()[row];
            ((DefaultTableCellRenderer) c).setToolTipText(column == 0 ? getDescription(key) : "");
            return c;
        }
    }

    public class ButtonRenderer extends JButton implements TableCellRenderer
    {
        private static final long serialVersionUID = 3442554392987317736L;

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}
