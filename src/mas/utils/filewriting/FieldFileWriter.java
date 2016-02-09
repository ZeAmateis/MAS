package mas.utils.filewriting;

import mas.utils.StringUtils;

/**
 * @author SCAREX
 *
 */
public class FieldFileWriter
{
    protected int modifiers;
    protected String name;
    protected String type;
    protected boolean autoInit;
    protected String init;

    public FieldFileWriter(int modifiers, String name, String type, boolean autoInit, String init) {
        this.modifiers = modifiers;
        this.name = name;
        this.type = type;
        this.autoInit = autoInit;
        this.init = init;
    }

    /**
     * @return the modifiers
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * @param modifiers
     *            the modifiers to set
     */
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the autoInit
     */
    public boolean isAutoInit() {
        return autoInit;
    }

    /**
     * @param autoInit
     *            the autoInit to set
     */
    public void setAutoInit(boolean autoInit) {
        this.autoInit = autoInit;
    }

    /**
     * @return the init
     */
    public String getInit() {
        return init;
    }

    /**
     * @param init
     *            the init to set
     */
    public void setInit(String init) {
        this.init = init;
    }

    public String toString(String indentation) {
        indentation += JavaFileWriter.modifiersToString(this.modifiers) + this.type + " " + formatName(name, this.modifiers);
        if (this.init != null && !this.init.isEmpty()) {
            indentation += " = " + this.init + ";";
        } else if (this.autoInit) {
            indentation += " = new " + this.type + "();";
        } else {
            indentation += ";";
        }
        return indentation;
    }

    public static String formatName(String name, int modifiers) {
        String[] words = StringUtils.charSplit(name, ' ');
        if (JavaFileWriter.isFinal(modifiers)) {
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].toUpperCase();
            }
            return String.join("_", words);
        }
        String s = words[0];
        for (int i = 1; i < words.length; i++) {
            s += words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }
        return s;
    }
}
