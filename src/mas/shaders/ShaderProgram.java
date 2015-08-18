package mas.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * @author SCAREX
 *
 */
public abstract class ShaderProgram
{
    protected int programID;
    protected int vertexShaderID;
    protected int fragmentShaderID;

    public ShaderProgram(InputStream vertexFile, InputStream fragmentFile) {
        this.vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        this.programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, this.vertexShaderID);
        GL20.glAttachShader(programID, this.fragmentShaderID);
        this.bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
    }

    public void start() {
        GL20.glUseProgram(this.programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(this.programID, this.vertexShaderID);
        GL20.glDetachShader(this.programID, this.fragmentShaderID);
        GL20.glDeleteProgram(this.vertexShaderID);
        GL20.glDeleteProgram(this.fragmentShaderID);
        GL20.glDeleteProgram(this.programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String name) {
        GL20.glBindAttribLocation(this.programID, attribute, name);
    }

    private static int loadShader(InputStream file, int type) {
        StringBuilder shader = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(file));
            String l;
            while ((l = r.readLine()) != null) {
                shader.append(l + "\n");
            }
            r.close();
        } catch (IOException e) {
            System.err.println("Couldn't read shader program");
            e.printStackTrace();
            return -1;
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shader);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE) return shaderID;
        return -1;
    }
}
