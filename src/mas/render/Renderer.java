package mas.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import mas.MAS;
import mas.entity.Entity;
import mas.render.model.RawModel;
import mas.render.model.TexturedModel;
import mas.render.terrain.Terrain;
import mas.shaders.BasicShader;
import mas.shaders.TerrainShader;
import mas.utils.MathUtils;

/**
 * @author SCAREX
 *
 */
public class Renderer
{
    private static final float FOV = 80;
    private static final float NEAR_PLANE = 0.1F;
    private static final float FAR_PLANE = 1000;

    public static Matrix4f projectionMatrix;

    public static void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.1F, 0.1F, 1.0F, 1.0F);
    }

    public static void renderEntity(Entity e, BasicShader shader) {
        TexturedModel model = e.getModel();
        RawModel rawModel = model.getModel();

        GL30.glBindVertexArray(rawModel.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(e.getPosition(), e.getRotationX(), e.getRotationY(), e.getRotationZ(), e.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

        shader.loadProjectionMatrix(Renderer.projectionMatrix);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());

        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public static void renderTerrains(List<Terrain> terrains, TerrainShader shader) {
        for (Terrain t : terrains) {
            RawModel rawModel = t.getModel();

            GL30.glBindVertexArray(rawModel.getVaoID());

            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);

            Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(new Vector3f(t.getX(), 0.0F, t.getZ()), 0.0F, 0.0F, 0.0F, new Vector3f(1F, 1F, 1F));
            shader.loadTransformationMatrix(transformationMatrix);

            shader.loadProjectionMatrix(Renderer.projectionMatrix);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, t.getTexture().getId());

            GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);
        }
    }

    public static void initProjectionMatrix() {
        float a = (float) MAS.getMAS().getModelCanvas().getWidth() / (float) MAS.getMAS().getModelCanvas().getHeight();
        float yScale = (float) ((1.0F / Math.tan(Math.toRadians(FOV / 2.0F))) * a);
        float xScale = yScale / a;
        float frustrumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrumLength);
        projectionMatrix.m23 = -1.0F;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrumLength);
        projectionMatrix.m33 = 0.0F;
    }
}
