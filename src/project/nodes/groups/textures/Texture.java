package project.nodes.groups.textures;

import lwjglutils.OGLTexture2D;
import project.nodes.groups.NodeGroup;
import java.io.IOException;
import java.util.Objects;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

/**
 * Třída pro vytvoření a renderování textury pro jednotlivé části objektu
 */
public class Texture extends NodeGroup {

    private OGLTexture2D texture;
    private TextureWrapping wrapping = TextureWrapping.REPEAT;
    private TextureFiltering filtering = TextureFiltering.LINEAR;

    public Texture(String fileName) throws IOException {
        Objects.requireNonNull(fileName);
        texture = new OGLTexture2D(fileName);
    }

    public Texture(String fileName, TextureWrapping wrapping, TextureFiltering filtering) throws IOException {
        Objects.requireNonNull(fileName);
        this.wrapping = wrapping;
        this.filtering = filtering;
        this.texture = new OGLTexture2D(fileName);
    }

    public TextureWrapping getWrapping() {
        return wrapping;
    }

    public void setWrapping(TextureWrapping wrapping) {
        this.wrapping = wrapping;
    }

    public TextureFiltering getFiltering() {
        return filtering;
    }

    public void setFiltering(TextureFiltering filtering) {
        this.filtering = filtering;
    }

    private int getGlWrapping(){
        switch (wrapping){
            case REPEAT:
                return GL_REPEAT;
            case MIRRORED_REPEAT:
                return GL_MIRRORED_REPEAT;
            case CLAMP_TO_EDGE:
                return GL_CLAMP_TO_EDGE;
            case CLAMP_TO_BORDER:
                return GL_CLAMP_TO_BORDER;
            default:
                throw new UnsupportedOperationException("Wrapping value is not supported.");
        }
    }

    private int getGlFiltering(){
        switch (filtering){
            case NEAREST:
                return GL_NEAREST;
            case LINEAR:
                return GL_LINEAR;
            case NEAREST_MIPMAP_NEAREST:
                return GL_NEAREST_MIPMAP_NEAREST;
            case LINEAR_MIPMAP_NEAREST:
                return GL_LINEAR_MIPMAP_NEAREST;
            case NEAREST_MIPMAP_LINEAR:
                return GL_NEAREST_MIPMAP_LINEAR;
            case LINEAR_MIPMAP_LINEAR:
                return GL_LINEAR_MIPMAP_LINEAR;
            default:
                throw new UnsupportedOperationException("Filtering value is not supported.");
        }
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, getGlWrapping());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, getGlWrapping());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, getGlFiltering());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, getGlFiltering());

        texture.bind();
        super.render();

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_TEXTURE_2D);
    }
}
