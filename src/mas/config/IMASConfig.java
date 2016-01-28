package mas.config;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import mas.MAS;
import mas.MASLang;
import mas.gui.PreferencesFrame;
import mas.utils.StringUtils;

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

    public default File getConfigFile() {
        return new File(MAS.CONFIG_DIRECTORY, this.getName() + ".config");
    }

    public void saveConfig();

    public default void saveLinesToConfigFile(List<String> lines) throws IOException {
        Files.write(this.getConfigFile().toPath(), lines);
    }

    public default Stream<String> getConfigFileByLines() throws IOException {
        return Files.lines(this.getConfigFile().toPath());
    }

    public default void forEachLineSplitInConfigFile(Consumer<String[]> cons, char splitter) throws IOException {
        this.getConfigFileByLines().forEach(line -> {
            String[] s = StringUtils.charSplitTrim(line, splitter);
            if (s.length > 0) cons.accept(s);
        });
    }

    public void readConfig();

    public Object getValue(Object key);

    public Component getContent();

    public void setFrame(PreferencesFrame frame);
}
