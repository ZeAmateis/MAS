package mas.render;

/**
 * @author SCAREX
 *
 */
public class RawModel
{
	private int vaoID;
	private int vertexCount;

	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * @return the vaoID
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * @return the vertixCount
	 */
	public int getVertexCount() {
		return vertexCount;
	}
}
