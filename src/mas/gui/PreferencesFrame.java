package mas.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import mas.config.IMASConfig;

/**
 * @author SCAREX
 *
 */
public class PreferencesFrame extends JFrame
{
    private static final long serialVersionUID = -3438254184519396833L;
    private static TreeMap<String, IMASConfig> configMap = new TreeMap<String, IMASConfig>();

    public PreferencesFrame() {
        super();

        this.initFrame();
        this.setMinimumSize(new Dimension(400, 300));
        this.setLocationRelativeTo(null);
    }

    private void initFrame() {
        JTabbedPane tp = new JTabbedPane(JTabbedPane.LEFT);
        for (Entry<String, IMASConfig> e : configMap.entrySet()) {
            e.getValue().readConfig();
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel title = new JLabel(e.getValue().getLocalizedName());
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(title);
            panel.add(Box.createRigidArea(new Dimension(40, 24)));
            e.getValue().setFrame(this);
            panel.add(e.getValue().getContent());
            tp.add(e.getValue().getLocalizedName(), panel);
        }
        this.add(tp);
    }

    public static void registerConfig(IMASConfig config) {
        configMap.put(config.getName(), config);
    }
}
