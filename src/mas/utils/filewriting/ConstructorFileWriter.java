package mas.utils.filewriting;

import java.util.ArrayList;

/**
 * @author SCAREX
 *
 */
public class ConstructorFileWriter
{
    protected int modifiers;
    protected String className;
    protected ArrayList<String[]> parameters = new ArrayList<>();
    protected ArrayList<String> lines = new ArrayList<>();

    public ConstructorFileWriter(int modifiers, String className, ArrayList<String> lines, ArrayList<String[]> parameters) {
        this.modifiers = modifiers;
        this.className = className;
        this.lines = lines;
        this.parameters = parameters;
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
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the parameters
     */
    public ArrayList<String[]> getParameters() {
        return parameters;
    }

    public void addParameter(String ... params) {
        if (params.length > 2) params = new String[] {
                params[0], params[1] };
        this.parameters.add(params);
    }

    /**
     * @return the lines
     */
    public ArrayList<String> getLines() {
        return lines;
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public String toString(String indentation) {
        String text = indentation + JavaFileWriter.modifiersToString(this.modifiers) + this.className;
        String[] params = new String[this.parameters.size()];
        for (int i = 0; i < this.parameters.size(); i++) {
            String[] s = this.parameters.get(i);
            params[i] = s[0] + " " + s[1];
        }
        text += "(" + String.join(", ", params) + ") {\n";
        for (String s : this.lines) {
            text += indentation + "    " + s + "\n";
        }
        text += indentation + "}";
        return text;
    }
}
