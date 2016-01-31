package mas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.util.vector.Vector3f;

import mas.MAS;
import mas.MASLang;
import mas.entity.Entity;
import mas.utils.SwingUtils;

/**
 * @author SCAREX
 * 
 */
public class MASRightPanel extends JPanel
{
    private static final long serialVersionUID = 5032270231588795964L;
    private JSlider xSlider;
    private JSlider ySlider;
    private JSlider zSlider;
    private final JSpinner xRotationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 359, 1));
    private final JSpinner yRotationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 359, 1));
    private final JSpinner zRotationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 359, 1));
    private final JFormattedTextField xPositionField = new JFormattedTextField(new DecimalFormat("0.000"));
    private final JFormattedTextField yPositionField = new JFormattedTextField(new DecimalFormat("0.000"));
    private final JFormattedTextField zPositionField = new JFormattedTextField(new DecimalFormat("0.000"));
    private final JSpinner xSizeSpinner = new JSpinner(new SpinnerNumberModel(1D, 0D, 100D, 0.1D));
    private final JSpinner ySizeSpinner = new JSpinner(new SpinnerNumberModel(1D, 0D, 100D, 0.1D));
    private final JSpinner zSizeSpinner = new JSpinner(new SpinnerNumberModel(1D, 0D, 100D, 0.1D));

    public MASRightPanel() {
        super();

        this.initGui();
        SwingUtils.changeComponentsState(this, false);
    }

    private void initGui() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.initRotationPanel();
        this.add(Box.createRigidArea(new Dimension(20, 20)));
        this.initPositionPanel();
        this.add(Box.createRigidArea(new Dimension(20, 20)));
        this.initSizePanel();
    }

    private void initRotationPanel() {
        JPanel rotPanel = new JPanel();
        rotPanel.setLayout(new BoxLayout(rotPanel, BoxLayout.Y_AXIS));

        JPanel rotXPanel = new JPanel(new BorderLayout());
        rotXPanel.add(new JLabel("X"), BorderLayout.WEST);
        rotXPanel.add(this.xSlider = new JSlider(0, 359, 0), BorderLayout.CENTER);
        this.xSlider.setFocusable(false);
        SliderSpinnerListener xListener = new SliderSpinnerListener(this.xSlider, this.xRotationSpinner);
        this.xSlider.addChangeListener(xListener);
        this.xRotationSpinner.addChangeListener(xListener);
        this.xSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().setRotationX(xSlider.getValue());
            }
        });
        MouseWheelSliderSpinnerListener xWheelListener = new MouseWheelSliderSpinnerListener(this.xSlider, this.xRotationSpinner);
        this.xSlider.addMouseWheelListener(xWheelListener);
        this.xRotationSpinner.addMouseWheelListener(xWheelListener);
        rotXPanel.setMaximumSize(new Dimension(800, 40));
        rotXPanel.add(this.xRotationSpinner, BorderLayout.EAST);
        rotPanel.add(rotXPanel);

        JPanel rotYPanel = new JPanel(new BorderLayout());
        rotYPanel.add(new JLabel("Y"), BorderLayout.WEST);
        rotYPanel.add(this.ySlider = new JSlider(0, 359, 0), BorderLayout.CENTER);
        this.ySlider.setFocusable(false);
        SliderSpinnerListener yListener = new SliderSpinnerListener(this.ySlider, this.yRotationSpinner);
        this.ySlider.addChangeListener(yListener);
        this.yRotationSpinner.addChangeListener(yListener);
        this.ySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().setRotationY(ySlider.getValue());
            }
        });
        MouseWheelSliderSpinnerListener yWheelListener = new MouseWheelSliderSpinnerListener(this.ySlider, this.yRotationSpinner);
        this.ySlider.addMouseWheelListener(yWheelListener);
        this.yRotationSpinner.addMouseWheelListener(yWheelListener);
        rotYPanel.setMaximumSize(new Dimension(800, 40));
        rotYPanel.add(this.yRotationSpinner, BorderLayout.EAST);
        rotPanel.add(rotYPanel);

        JPanel rotZPanel = new JPanel(new BorderLayout());
        rotZPanel.add(new JLabel("Z"), BorderLayout.WEST);
        rotZPanel.add(this.zSlider = new JSlider(0, 359, 0), BorderLayout.CENTER);
        this.zSlider.setFocusable(false);
        SliderSpinnerListener zListener = new SliderSpinnerListener(this.zSlider, this.zRotationSpinner);
        this.zSlider.addChangeListener(zListener);
        this.zRotationSpinner.addChangeListener(zListener);
        this.zSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().setRotationZ(zSlider.getValue());
            }
        });
        MouseWheelSliderSpinnerListener zWheelListener = new MouseWheelSliderSpinnerListener(this.zSlider, this.zRotationSpinner);
        this.zSlider.addMouseWheelListener(zWheelListener);
        this.zRotationSpinner.addMouseWheelListener(zWheelListener);
        rotZPanel.setMaximumSize(new Dimension(800, 40));
        rotZPanel.add(this.zRotationSpinner, BorderLayout.EAST);
        rotPanel.add(rotZPanel);

        rotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), MASLang.translate("panel.rotation")));
        this.add(rotPanel);
    }

    private void initPositionPanel() {
        JPanel posPanel = new JPanel();
        posPanel.setLayout(new BoxLayout(posPanel, BoxLayout.X_AXIS));

        this.xPositionField.setValue(0);
        this.xPositionField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (MAS.getMAS() != null && MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().getPosition().setX(numberToFloatValue(xPositionField.getValue()));
            }
        });
        posPanel.add(this.xPositionField);
        this.yPositionField.setValue(0);
        this.yPositionField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (MAS.getMAS() != null && MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().getPosition().setY(numberToFloatValue(yPositionField.getValue()));
            }
        });
        posPanel.add(this.yPositionField);
        this.zPositionField.setValue(0);
        this.zPositionField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (MAS.getMAS() != null && MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().getPosition().setZ(numberToFloatValue(zPositionField.getValue()));
            }
        });
        posPanel.add(this.zPositionField);

        posPanel.setMaximumSize(new Dimension(800, 60));
        posPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), MASLang.translate("panel.position")));
        this.add(posPanel);
    }

    /**
     * Because the returned value from a formatted text field can be a double or
     * a long
     */
    public float numberToFloatValue(Object o) {
        if (o instanceof Float) return (float) o;
        if (o instanceof Long) return ((Long) o).floatValue();
        if (o instanceof Double) return ((Double) o).floatValue();
        return 0;
    }

    private void initSizePanel() {
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));

        this.xSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().getScale().setX(numberToFloatValue(xSizeSpinner.getValue()));
            }
        });
        sizePanel.add(this.xSizeSpinner);
        this.ySizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().getScale().setY(numberToFloatValue(ySizeSpinner.getValue()));
            }
        });
        sizePanel.add(this.ySizeSpinner);
        this.zSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (MAS.getMAS().getProject().getSelectedEntity() != null) MAS.getMAS().getProject().getSelectedEntity().getScale().setZ(numberToFloatValue(zSizeSpinner.getValue()));
            }
        });
        sizePanel.add(this.zSizeSpinner);

        sizePanel.setMaximumSize(new Dimension(800, 50));
        sizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), MASLang.translate("panel.size")));
        this.add(sizePanel);
    }

    public void loadValuesForEntity(Entity e) {
        this.xSlider.setValue(e.getRotationX());
        this.ySlider.setValue(e.getRotationY());
        this.zSlider.setValue(e.getRotationZ());

        Vector3f vecPos = e.getPosition();
        this.xPositionField.setValue(vecPos.getX());
        this.yPositionField.setValue(vecPos.getY());
        this.zPositionField.setValue(vecPos.getZ());

        Vector3f vecSize = e.getScale();
        this.xSizeSpinner.setValue(vecSize.getX());
        this.ySizeSpinner.setValue(vecSize.getY());
        this.zSizeSpinner.setValue(vecSize.getZ());
    }

    public static final class SliderSpinnerListener implements ChangeListener
    {
        private final JSlider slider;
        private final JSpinner spinner;

        public SliderSpinnerListener(JSlider slider, JSpinner spinner) {
            this.slider = slider;
            this.spinner = spinner;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == this.slider) {
                this.spinner.setValue(this.slider.getValue());
            } else if (e.getSource() == this.spinner) {
                this.slider.setValue((Integer) this.spinner.getValue());
            }
        }
    }

    public static final class MouseWheelSliderSpinnerListener implements MouseWheelListener
    {
        private final JSlider slider;
        private final JSpinner spinner;

        public MouseWheelSliderSpinnerListener(JSlider slider, JSpinner spinner) {
            this.slider = slider;
            this.spinner = spinner;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int i = -e.getWheelRotation();
            switch (e.getModifiers()) {
            case InputEvent.CTRL_MASK | InputEvent.ALT_MASK:
                i *= 90;
                break;
            case InputEvent.CTRL_MASK:
                i *= 10;
                break;
            }
            int j = this.slider.getValue() + i;
            j = Math.max(j, this.slider.getMinimum());
            j = Math.min(j, this.slider.getMaximum());
            this.slider.setValue(j);
            this.spinner.setValue(j);
        }
    }
}
