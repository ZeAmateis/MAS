package mas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Stream;

import mas.utils.StringUtils;
import mas.utils.SystemUtils;

/**
 * @author SCAREX
 *
 */
public class MASMainConfig
{
    private static final File configFile = new File(System.getProperty("user.dir"), "config.txt");
    private static TreeMap<EnumMainConfig, String> configMap = new TreeMap<EnumMainConfig, String>();

    public static void loadConfig() throws IOException {
        if (!configFile.exists() && !configFile.createNewFile()) throw new IOException("Cannot create config file");

        Stream<String> lines = Files.lines(configFile.toPath());
        lines.forEach(line -> {
            if (line.indexOf('=') > 0) {
                String[] s = StringUtils.charSplitTrim(line, '=');
                EnumMainConfig config;
                if (s.length >= 2 && (config = EnumMainConfig.getEnumMainConfig(s[0])) != null) configMap.put(config, s[1]);
            }
        });
        lines.close();
    }

    public static void saveConfig() throws IOException {
        if (!configFile.exists() && !configFile.createNewFile()) throw new IOException("Cannot create config file");
        ArrayList<String> lines = new ArrayList<String>();
        for (EnumMainConfig config : EnumMainConfig.values()) {
            lines.add(config.getKey() + "=" + (configMap.getOrDefault(config, config.getDefaultValue())));
        }
        Files.write(configFile.toPath(), lines);
    }

    public static void generateAndLoad() throws IOException {
        if (!configFile.exists()) {
            if (!configFile.createNewFile()) throw new IOException("Cannot create config file");

            ArrayList<String> lines = new ArrayList<String>();
            for (EnumMainConfig config : EnumMainConfig.values()) {
                lines.add(config.getKey() + "=" + config.getDefaultValue());
                configMap.put(config, config.getDefaultValue());
            }
            Files.write(configFile.toPath(), lines);
        } else {
            loadConfig();
        }
    }
    
    public static String getValue(EnumMainConfig config) {
        return configMap.get(config);
    }
    
    public static int getValueAsInt(EnumMainConfig config) {
        try {
            int i = Integer.parseInt(getValue(config));
            return i;
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }
    
    public static boolean getValueAsBoolean(EnumMainConfig config) {
        try {
            boolean flag = Boolean.parseBoolean(getValue(config));
            return flag;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static enum EnumMainConfig
    {
        APP_PATH("app_path", SystemUtils.getAppFolder("MAS").toString()),
        CHECK_FOR_UPDATES("check_for_updates", "true");

        private String key;
        private String defaultValue;

        private EnumMainConfig(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @return the defaultValue
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        public static EnumMainConfig getEnumMainConfig(String key) {
            if (key == null || key.length() == 0) return null;
            for (EnumMainConfig config : EnumMainConfig.values()) {
                if (config.getKey().equalsIgnoreCase(key)) return config;
            }
            return null;
        }
    }
}
