package mas.utils;

import java.io.File;

public final class SystemUtils
{

    public enum OperatingSystem
    {
        WINDOWS,
        LINUX,
        MACOSX,
        SOLARIS,
        UNKNOWN
    }

    private static File appFolder;

    public static OperatingSystem getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("sunos") || os.contains("solaris")) {
            return OperatingSystem.SOLARIS;
        } else if (os.contains("unix") || os.contains("linux")) {
            return OperatingSystem.LINUX;
        } else if (os.contains("mac")) { return OperatingSystem.MACOSX; }
        return OperatingSystem.UNKNOWN;
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Returns the folder where app data is saved
     */
    public static File getAppFolder(String gameID) {
        if (appFolder == null) {
            String appdata = System.getenv("APPDATA");
            if (appdata != null)
                appFolder = new File(appdata, gameID);
            else
                appFolder = new File(System.getProperty("user.home"), gameID);
        }
        return appFolder;
    }

    public static void deleteRecursivly(File file) {
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null) for (File f : list) {
                deleteRecursivly(f);
                f.delete();
            }
        }
        file.delete();
    }

    public static void setAppFolder(File file) {
        appFolder = file;
    }
}
