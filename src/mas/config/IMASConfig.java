package mas.config;

import java.awt.Component;

import mas.MASLang;
import mas.gui.PreferencesFrame;

/**
 * @author SCAREX
 *
 */
public interface IMASConfig
{
    public String getName();
    
    public default String getUnlocalizedName() {
        return "config." + this.getName();
    }
    
    public default String getLocalizedName() {
        return MASLang.translate(this.getUnlocalizedName());
    }
    
    public Object getValue(Object key);
    
    public Component getContent();
    
    public void setFrame(PreferencesFrame frame);
}
