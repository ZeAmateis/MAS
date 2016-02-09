package mas.project;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;

/**
 * @author SCAREX
 *
 */
public class ModuleScannerThread extends Thread
{
    @Override
    public void run() {
        System.out.println("Scanning for modules");
        long t = System.currentTimeMillis();

        Reflections ref = new Reflections();
        Set<Class<?>> candidates = ref.getTypesAnnotatedWith(MASModule.class);
        candidates.forEach(c -> {
            try {
                Object o = c.newInstance();
                Method m = c.getMethod("start");
                m.invoke(o);
            } catch (Exception e) {
                System.out.println("Error while scanning candidate :");
                e.printStackTrace();
            }
        });
        System.out.println("End of scan : " + (System.currentTimeMillis() - t) + "ms");
    }
}
