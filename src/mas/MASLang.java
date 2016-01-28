package mas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import mas.MASMainConfig.EnumMainConfig;
import mas.utils.IOUtils;

/**
 * This class is used to translate the program.
 * 
 * @author SCAREX
 */
public class MASLang
{
    public static File langFolder;
    private static HashMap<String, String> langMap;
    private static HashMap<String, String> enMap;

    public static void load(File folder) {
        if (!(langFolder = new File(folder, "lang")).exists()) langFolder.mkdirs();
        MASLang.downloadIfNeedsUpdate(langFolder, Locale.US);
        MASLang.downloadIfNeedsUpdate(langFolder, Locale.getDefault());
        MASLang.parseLangFiles();
    }

    public static void downloadIfNeedsUpdate(File folder, Locale l) {
        File f = new File(folder, l + ".lang");
        if (!f.exists()) {
            try {
                IOUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/SCAREXgaming/MASLang/master/" + l + ".lang"), f);
            } catch (IOException e) {
                System.out.println("Lang " + l + " isn't currently supported or cannot be downloaded, you can help with translation at https://github.com/SCAREXgaming/MASLang");
            }
            return;
        } else {
            if (!MAS.DEBUG) {
                if (MASMainConfig.getValueAsBoolean(EnumMainConfig.CHECK_FOR_UPDATES)) {
                    Thread t = new Thread(new LangCheckRunnable(langFolder, l));
                    t.setUncaughtExceptionHandler(MAS.EXCEPTION_HANDLER);
                    t.start();
                }
            }
        }
    }

    private static void parseLangFiles() {
        enMap = MASLang.parseFile(new File(langFolder, "en_US.lang"));
        if (new File(langFolder, Locale.getDefault() + ".lang").exists()) langMap = parseFile(new File(langFolder, Locale.getDefault() + ".lang"));
    }

    public static HashMap<String, String> parseFile(File f) {
        HashMap<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String l;
            while ((l = br.readLine()) != null) {
                int i;
                if (l.length() > 0 && l.charAt(0) != '#' && (i = l.indexOf('=')) != -1) {
                    map.put(l.substring(0, i).trim(), l.substring(i + 1).trim());
                }
            }
        } catch (Exception e) {
            System.err.println("Cannot parse file : " + f.getName());
            e.printStackTrace();
        }
        return map;
    }

    public static String translate(String key) {
        return langMap != null && langMap.containsKey(key) ? langMap.get(key) : (enMap.containsKey(key) ? enMap.get(key) : key);
    }

    public static String translate(String key, Object ... params) {
        return String.format(langMap != null && langMap.containsKey(key) ? langMap.get(key) : (enMap.containsKey(key) ? enMap.get(key) : key), params);
    }
}
