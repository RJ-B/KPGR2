package project.utils.loaders.obj;

import project.utils.loaders.mtl.MtlMaterialData;

/**
 * Třída obsahující data o objektu, který máme v souboru OBJ
 */

public class ObjObjectData {

    private final String objectName;

    /**
     *  Vertex Coordinates
     */
    private final float[] vertexBuffer;

    /**
     * Texture Coordinates
     */
    private final float[] textCoordBuffer;

    /**
     * Normal Coordinates
     */
    private final float[] normalBuffer;

    /**
     * Index buffer
     */
    private final int[] indexBuffer;

    private final MtlMaterialData material;

    public ObjObjectData(String name, float[] vertexBuffer, float[] textCoordBuffer, float[] normalBuffer, int[] indexBuffer, MtlMaterialData material) {
        this.objectName = name;
        this.vertexBuffer = vertexBuffer;

        this.textCoordBuffer = textCoordBuffer == null || textCoordBuffer.length == 0 ? null : textCoordBuffer;
        this.normalBuffer = normalBuffer == null || normalBuffer.length == 0 ? null : normalBuffer;

        this.indexBuffer = indexBuffer;
        this.material = material;
    }


    /**
     * Vrátí název objektu.
     * @return objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Vrátí vertex buffer.
     * @return vertexBuffer
     */
    public float[] getVertexBuffer() {
        return vertexBuffer;
    }

    /**
     * Vrátí buffer souřadnic do textury.
     * @return textCoordBuffer
     */
    public float[] getTextCoordBuffer() {
        return textCoordBuffer;
    }

    /**
     * Vrátí buffer normálových vektorů.
     * @return normalBuffer
     */
    public float[] getNormalBuffer() {
        return normalBuffer;
    }

    /**
     * Vrátí index buffer.
     * @return indexBuffer
     */
    public int[] getIndexBuffer() {
        return indexBuffer;
    }

    /**
     * Vrátí materiálová data.
     * @return material
     */
    public MtlMaterialData getMaterial() {
        return material;
    }
}
