package mas.project;

import java.util.List;

/**
 * @author SCAREX
 *
 */
public interface IMASProjectElement
{
    public String getDisplayedName();
    
    public List<IMASProjectElement> getElements();
}
