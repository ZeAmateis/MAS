package mas.project;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.reflections.Reflections;

/**
 * @author SCAREX
 *
 */
public class ModuleScannerThread extends Thread
{
    public final File modulesDirectory;

    public ModuleScannerThread(String dir) {
        this.modulesDirectory = new File(dir, "modules");
        this.modulesDirectory.mkdirs();
    }

    @Override
    public void run() {
        System.out.println("Loading jar modules");
        File[] jars = this.modulesDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                System.out.println("Testing name : " + name);
                return name.endsWith(".jar");
            }
        });
        for (File f : jars) {
            System.out.println("Loading jar : " + f.getName());
            loadJar(f);
        }
        System.out.println("Scanning for modules");
        long t = System.currentTimeMillis();

        Reflections ref = new Reflections();
        Set<Class<?>> candidates = ref.getTypesAnnotatedWith(MASModule.class);
        candidates.forEach(c -> {
            try {
                System.out.println("Initiliazing candidate : " + c.getName());
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

    private static void loadJar(File f) {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        try {
            Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            m.setAccessible(true);
            m.invoke(sysloader, f.toURI().toURL());
            System.out.println("Jar loaded : " + f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
