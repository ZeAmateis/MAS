package mas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

import mas.MASLang;
import mas.utils.JframeUtils;

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
    private final JSpinner xSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 359, 1));
    private final JSpinner ySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 359, 1));
    private final JSpinner zSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 359, 1));

    public MASRightPanel() {
        super();

        this.initGui();
        JframeUtils.changeComponentsState(this, false);
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
        SliderSpinnerListener xListener = new SliderSpinnerListener(this.xSlider, this.xSpinner);
        this.xSlider.addChangeListener(xListener);
        this.xSpinner.addChangeListener(xListener);
        MouseWheelSliderSpinnerListener xWheelListener = new MouseWheelSliderSpinnerListener(this.xSlider, this.xSpinner);
        this.xSlider.addMouseWheelListener(xWheelListener);
        this.xSpinner.addMouseWheelListener(xWheelListener);
        rotXPanel.setMaximumSize(new Dimension(800, 40));
        rotXPanel.add(this.xSpinner, BorderLayout.EAST);
        rotPanel.add(rotXPanel);

        JPanel rotYPanel = new JPanel(new BorderLayout());
        rotYPanel.add(new JLabel("Y"), BorderLayout.WEST);
        rotYPanel.add(this.ySlider = new JSlider(0, 359, 0), BorderLayout.CENTER);
        this.ySlider.setFocusable(false);
        SliderSpinnerListener yListener = new SliderSpinnerListener(this.ySlider, this.ySpinner);
        this.ySlider.addChangeListener(yListener);
        this.ySpinner.addChangeListener(yListener);
        MouseWheelSliderSpinnerListener yWheelListener = new MouseWheelSliderSpinnerListener(this.ySlider, this.ySpinner);
        this.ySlider.addMouseWheelListener(yWheelListener);
        this.ySpinner.addMouseWheelListener(yWheelListener);
        rotYPanel.setMaximumSize(new Dimension(800, 40));
        rotYPanel.add(this.ySpinner, BorderLayout.EAST);
        rotPanel.add(rotYPanel);

        JPanel rotZPanel = new JPanel(new BorderLayout());
        rotZPanel.add(new JLabel("Z"), BorderLayout.WEST);
        rotZPanel.add(this.zSlider = new JSlider(0, 359, 0), BorderLayout.CENTER);
        this.zSlider.setFocusable(false);
        SliderSpinnerListener zListener = new SliderSpinnerListener(this.zSlider, this.zSpinner);
        this.zSlider.addChangeListener(zListener);
        this.zSpinner.addChangeListener(zListener);
        MouseWheelSliderSpinnerListener zWheelListener = new MouseWheelSliderSpinnerListener(this.zSlider, this.zSpinner);
        this.zSlider.addMouseWheelListener(zWheelListener);
        this.zSpinner.addMouseWheelListener(zWheelListener);
        rotZPanel.setMaximumSize(new Dimension(800, 40));
        rotZPanel.add(this.zSpinner, BorderLayout.EAST);
        rotPanel.add(rotZPanel);

        rotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), MASLang.translate("panel.rotation")));
        this.add(rotPanel);
    }

    private void initPositionPanel() {
        JPanel posPanel = new JPanel();
        posPanel.setLayout(new BoxLayout(posPanel, BoxLayout.X_AXIS));

        JFormattedTextField f = new JFormattedTextField(new DecimalFormat("0.000"));
        f.setValue(0);
        posPanel.add(f);
        JFormattedTextField f1 = new JFormattedTextField(new DecimalFormat("0.000"));
        f1.setValue(0);
        posPanel.add(f1);
        JFormattedTextField f2 = new JFormattedTextField(new DecimalFormat("0.000"));
        f2.setValue(0);
        posPanel.add(f2);

        posPanel.setMaximumSize(new Dimension(800, 60));
        posPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), MASLang.translate("panel.position")));
        this.add(posPanel);
    }

    private void initSizePanel() {
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));

        sizePanel.add(new JSpinner());
        sizePanel.add(new JSpinner());
        sizePanel.add(new JSpinner());

        sizePanel.setMaximumSize(new Dimension(800, 50));
        sizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), MASLang.translate("panel.size")));
        this.add(sizePanel);
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
