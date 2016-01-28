package mas.utils;

import java.awt.Component;
import java.awt.Container;

/**
 * @author SCAREX
 *
 */
public class JframeUtils
{
    public static void changeComponentsState(Container container, boolean enable) {
        for (Component comp : container.getComponents()) {
            comp.setEnabled(enable);
            if (comp instanceof Container) changeComponentsState((Container) comp, enable);
        }
    }
}
