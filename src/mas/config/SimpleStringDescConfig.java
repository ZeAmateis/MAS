package mas.config;

import java.awt.Component;
import java.text.Format;
import java.text.ParseException;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import mas.MASLang;
import mas.gui.PreferencesFrame;

/**
 * @author SCAREX
 *
 */
public class SimpleStringDescConfig implements IMASConfig
{
    protected final String name;
    protected TreeMap<String, Object[]> configMap = new TreeMap<String, Object[]>();

    public SimpleStringDescConfig(String name) {
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
        table.setDefaultEditor(Object.class, new ConfigCellEditor());
        table.setDefaultRenderer(Object.class, new ConfigTableCellRenderer());
        return new JScrollPane(table);
    }

    @Override
    public void setFrame(PreferencesFrame frame) {}

    public String getDescription(String key) {
        return (String) this.configMap.get(key)[0];
    }

    public String getDefaultValue(String key) {
        return (String) this.configMap.get(key)[1];
    }

    public Format getFormat(String key) {
        Object[] value = this.configMap.get(key);
        return value.length > 3 ? (Format) value[3] : null;
    }

    public String getStringValue(String key) {
        return (String) this.configMap.get(key)[2];
    }

    public int getIntValue(String key) {
        return Integer.parseInt(this.getStringValue(key));
    }

    public float getFloatValue(String key) {
        return Float.parseFloat(this.getStringValue(key));
    }

    public byte getByteValue(String key) {
        return Byte.parseByte(this.getStringValue(key));
    }

    public double getDoubleValue(String key) {
        return Double.parseDouble(this.getStringValue(key));
    }

    public void registerConfig(String key, String value) {
        this.configMap.put(key, new Object[] {
                MASLang.translate(this.getUnlocalizedName() + "." + key + ".desc"),
                value, value });
    }

    public void registerConfig(String key, String value, Format format) {
        this.configMap.put(key, new Object[] {
                MASLang.translate(this.getUnlocalizedName() + "." + key + ".desc"),
                value, value, format });
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
            return col == 0 ? MASLang.translate(getUnlocalizedName() + "." + key) : getValue(key);
        }
    }

    public class ConfigCellEditor extends DefaultCellEditor
    {
        private static final long serialVersionUID = 9203863700587692095L;
        private int row;
        private int col;

        public ConfigCellEditor() {
            super(new JTextField());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.col = column;
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public Object getCellEditorValue() {
            if (col == 1) {
                String key = configMap.keySet().toArray(new String[0])[row];
                Object[] value = configMap.get(key);
                String s = (String) super.getCellEditorValue();
                Format f = getFormat(key);
                if (f != null) {
                    try {
                        f.parseObject(s);
                        value[2] = s;
                        configMap.put(key, value);
                    } catch (ParseException e) {}
                } else {
                    value[2] = s;
                    configMap.put(key, value);
                }
            }
            return super.getCellEditorValue();
        }
    }

    public class ConfigTableCellRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 3699372716031124686L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String key = (String) configMap.keySet().toArray()[row];
            ((DefaultTableCellRenderer) c).setToolTipText(column == 0 ? getDescription(key) : getDefaultValue(key));
            return c;
        }
    }
}
