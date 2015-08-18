package mas.shaders;

import mas.MAS;

/**
 * @author SCAREX
 *
 */
public class BasicShader extends ShaderProgram
{
    public BasicShader() {
        super(MAS.class.getResourceAsStream("shaders/vertexShader.txt"), MAS.class.getResourceAsStream("shaders/fragmentShader.txt"));
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "pos");
        super.bindAttribute(1, "color");
    }
}
