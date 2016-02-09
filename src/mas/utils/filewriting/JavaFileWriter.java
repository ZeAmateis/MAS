package mas.utils.filewriting;

import java.util.ArrayList;

/**
 * @author SCAREX
 *
 */
public class JavaFileWriter
{
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 3;
    public static final int STATIC = 4;
    public static final int FINAL = 8;

    protected int modifiers;
    protected String name;
    protected String packageName;
    protected String extension;
    protected ArrayList<String> packages = new ArrayList<>();
    protected ArrayList<String> annotations = new ArrayList<>();
    protected ArrayList<String> implementations = new ArrayList<>();
    protected ArrayList<FieldFileWriter> fields = new ArrayList<>();
    protected ArrayList<ConstructorFileWriter> constructors = new ArrayList<>();
    protected ArrayList<MethodFileWriter> methods = new ArrayList<>();

    public JavaFileWriter(int modifiers, String name, String packageName, String extension, ArrayList<String> packages, ArrayList<String> annotations, ArrayList<String> implementations) {
        this.modifiers = modifiers;
        this.name = name;
        this.packageName = packageName;
        this.packages = packages;
        this.annotations = annotations;
        this.extension = extension;
        this.implementations = implementations;
    }

    public JavaFileWriter(int modifiers, String name, String packageName) {
        this.modifiers = modifiers;
        this.name = name;
        this.packageName = packageName;
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
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension
     *            the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the implementations
     */
    public ArrayList<String> getImplementations() {
        return implementations;
    }

    /**
     * @return the fields
     */
    public ArrayList<FieldFileWriter> getFields() {
        return fields;
    }

    /**
     * @return the methods
     */
    public ArrayList<MethodFileWriter> getMethods() {
        return methods;
    }

    public void addImplementation(String implementation) {
        this.implementations.add(implementation);
    }

    public void addField(FieldFileWriter field) {
        this.fields.add(field);
    }

    public void addMethod(MethodFileWriter method) {
        this.methods.add(method);
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the packages
     */
    public ArrayList<String> getPackages() {
        return packages;
    }

    public void addPackage(String packageName) {
        this.packages.add(packageName);
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

    /**
     * @return the constructors
     */
    public ArrayList<ConstructorFileWriter> getConstructors() {
        return constructors;
    }

    public void addConstructor(ConstructorFileWriter constructor) {
        this.constructors.add(constructor);
    }

    public static String modifiersToString(int modifiers) {
        String s = "";
        switch (modifiers & 3) {
        case PUBLIC:
            s += "public ";
            break;
        case PRIVATE:
            s += "private ";
            break;
        case PROTECTED:
            s += "protected ";
            break;
        }

        if (isStatic(modifiers)) s += "static ";
        if (isFinal(modifiers)) s += "final ";
        return s;
    }

    public static boolean isStatic(int modifiers) {
        return (modifiers & STATIC) > 0;
    }

    public static boolean isFinal(int modifiers) {
        return (modifiers & FINAL) > 0;
    }

    @Override
    public String toString() {
        String text = "package " + this.packageName + ";\n\n";
        for (String packageName : this.packages) {
            text += "import " + packageName + ";\n";
        }
        text += "\n";

        for (String annotation : this.annotations) {
            text += "@" + annotation + "\n";
        }

        text += modifiersToString(this.modifiers) + "class " + this.name;
        if (this.extension != null && !this.extension.isEmpty()) text += " extends " + this.extension;
        if (!this.implementations.isEmpty()) text += "implements " + String.join(", ", this.implementations);
        text += "\n{\n";

        if (!this.fields.isEmpty()) {
            for (FieldFileWriter ffw : this.fields) {
                text += ffw.toString("    ") + "\n";
            }
            text += "\n";
        }

        if (!this.constructors.isEmpty()) {
            for (ConstructorFileWriter ffw : this.constructors) {
                text += ffw.toString("    ") + "\n";
            }
            text += "\n";
        }

        for (int i = 0; i < this.methods.size(); i++) {
            MethodFileWriter mfw = this.methods.get(i);
            text += mfw.toString("    ") + "\n" + (i + 1 < this.methods.size() ? "\n" : "");
        }

        text += "}";
        return text;
    }
}
