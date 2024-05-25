package project.nodes.leaf;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL30.*;

public class Geometry3D extends LeafNode {

    private String name;

    private int indicesCount;

    private int vaoId = -1; // vertex array buffer id
    private int vboId = -1; // vertex buffer id
    private int iboId = -1; // index buffer id
    private int nboId = -1; // normal buffer id
    private int tboId = -1; // texture coordinates buffer id

    protected int mode = GL_TRIANGLES;

    public Geometry3D(float[] positionsBuffer, float[] textCoordBuffer, float[] normalBuffer, int[] indexBuffer){
        this(null, positionsBuffer, textCoordBuffer, normalBuffer, indexBuffer);
    }

    public Geometry3D(String name, float[] positionsBuffer, float[] textCoordBuffer, float[] normalBuffer, int[] indexBuffer) {
        this(name, getFloatBuffer(positionsBuffer), getFloatBuffer(textCoordBuffer), getFloatBuffer(normalBuffer), getIntBuffer(indexBuffer), indexBuffer.length);
    }

    public Geometry3D(FloatBuffer positionsBuffer, FloatBuffer textCoordBuffer, FloatBuffer normalBuffer, IntBuffer indexBuffer, int indicesCount){
        this(null, positionsBuffer, textCoordBuffer, normalBuffer, indexBuffer, indicesCount);
    }

    public Geometry3D(String name, FloatBuffer positionsBuffer, FloatBuffer textCoordBuffer, FloatBuffer normalBuffer, IntBuffer indexBuffer, int indicesCount)
    {
        Objects.requireNonNull(positionsBuffer);
        Objects.requireNonNull(indexBuffer);

        this.name = name;

        if(indicesCount <= 0)
            throw new IllegalArgumentException("Number of indices must be greater than zero.");

        this.indicesCount = indicesCount;

        vaoId = glGenVertexArrays();
        vboId = createVertexBufferObject(0, 3, positionsBuffer);
        setVertexPointer(vboId, 3);

        iboId = createIndexBufferObject(indexBuffer);

        if(normalBuffer != null) {
            nboId = createVertexBufferObject(1, 3, normalBuffer);
            setNormalPointer(nboId);
        }

        if(textCoordBuffer != null) {
            tboId = createVertexBufferObject(2, 2, textCoordBuffer);
            setTextCoordPointer(tboId);
        }
    }

    public String getName() {
        return name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    protected int getIndicesCount() {
        return indicesCount;
    }

    protected void setIndicesCount(int indicesCount) {
        this.indicesCount = indicesCount;
    }

    private static FloatBuffer getFloatBuffer(float[] buffer) {
        if(buffer == null)
            return null;
        else
            return BufferUtils.createFloatBuffer(buffer.length).put(buffer).rewind();
    }

    private static IntBuffer getIntBuffer(int[] buffer) {
        if(buffer == null)
            return null;
        else
            return BufferUtils.createIntBuffer(buffer.length).put(buffer).rewind();
    }

    protected int createVertexBufferObject(int atribIndex, int size, float[] vertexBuffer) {
        return createVertexBufferObject(atribIndex, size, getFloatBuffer(vertexBuffer));
    }

    protected int createVertexBufferObject(int atribIndex, int size, FloatBuffer vertexBuffer) {
        glBindVertexArray(vaoId);
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(atribIndex, size, GL_FLOAT, false, 0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        return vboId;
    }

    protected int createIndexBufferObject(int[] indicesBuffer) {
        return  createIndexBufferObject(getIntBuffer(indicesBuffer));
    }

    protected int createIndexBufferObject(IntBuffer indicesBuffer) {
        int iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        return iboId;
    }

    protected void setVertexPointer(int vboId, int size) {
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glVertexPointer(size, GL_FLOAT, 0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    protected void setColorPointer(int cboId, int size) {
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, cboId);
        glColorPointer(size, GL_FLOAT, 0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    protected void setNormalPointer(int nboId) {
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, nboId);
        glNormalPointer(GL_FLOAT, 0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    protected void setTextCoordPointer(int tboId) {
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, tboId);
        glTexCoordPointer(2, GL_FLOAT, 0,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void render() {
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_INDEX_ARRAY);

        if (tboId > 0) glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (nboId > 0) glEnableClientState(GL_NORMAL_ARRAY);

        glDrawElements(mode, indicesCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_INDEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(iboId);
        glDeleteBuffers(nboId);
        glDeleteBuffers(tboId);
    }
}
