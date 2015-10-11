package mas.config;

/**
 * @author SCAREX
 *
 */
public interface IKeyCallback
{
    /**
     * @return false if you want to remove the listener
     */
    public boolean keyPressed(int key);
}
