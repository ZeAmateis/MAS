package mas.utils.filewriting;

import java.util.ArrayList;

/**
 * @author SCAREX
 *
 */
public class MethodFileWriter
{
    protected int modifiers;
    protected String name;
    protected String type;
    protected ArrayList<String> annotations = new ArrayList<>();
    protected ArrayList<String[]> parameters = new ArrayList<>();
    protected ArrayList<String> lines = new ArrayList<>();

    public MethodFileWriter(int modifiers, String name, String type, ArrayList<String[]> parameters) {
        this.modifiers = modifiers;
        this.name = name;
        this.type = type;
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
     * @return the parameters
     */
    public ArrayList<String[]> getParameters() {
        return parameters;
    }

    /**
     * @return the lines
     */
    public ArrayList<String> getLines() {
        return lines;
    }

    public void addParameter(String ... params) {
        if (params.length > 2) params = new String[] {
                params[0], params[1] };
        this.parameters.add(params);
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    /**
     * @return the annotations
     */
    public ArrayList<String> getAnnotations() {
        return annotations;
    }
    
    public void addAnnotation(String annotation) {
        this.annotations.add(annotation);
    }

    public String toString(String indentation) {
        String text = "";
        
        for (String annotation : this.annotations) {
            text += indentation + "@" + annotation + "\n";
        }
        
        text += indentation + JavaFileWriter.modifiersToString(this.modifiers) + this.type + " " + this.name;
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
