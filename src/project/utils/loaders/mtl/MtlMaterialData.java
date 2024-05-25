package project.utils.loaders.mtl;

/**
 * Třída obsahující data o materiálu, které jsou v MTL souboru
 */

public class MtlMaterialData {

    private final String name;
    private final float[] ka;
    private final float[] kd;
    private final float[] ks;
    private final Float ns;
    private final Float ni;
    private final Float d;

    /**
     * Illumination model
     */
    private final Integer illum;
    private final String map_Kd;

    public MtlMaterialData(String name, float[] ka, float[] kd, float[] ks, Float ns, Float ni, Float d, Integer illum, String map_Kd) {
        this.name = name;
        this.ka = ka;
        this.kd = kd;
        this.ks = ks;
        this.ns = ns;
        this.ni = ni;
        this.d = d;
        this.illum = illum;
        this.map_Kd = map_Kd;
    }

    public String getName() {
        return name;
    }

    /**
     * Ambient color
     */
    public float[] getKa() {
        return ka;
    }

    /**
     * Diffuse color
     */
    public float[] getKd() {
        return kd;
    }

    /**
     * Specular color
     */
    public float[] getKs() {
        return ks;
    }

    /**
     * Specular highlights
     */
    public Float getNs() {
        return ns;
    }

    /**
     * Optical density
     */
    public Float getNi() {
        return ni;
    }

    /**
     * Dissolve
     */
    public Float getD() {
        return d;
    }

    /**
     * Illumination model
     */
    public Integer getIllum() {
        return illum;
    }

    /**
     * Color texture file to be applied to the diffuse reflectivity of the material
     */
    public String getMap_Kd() {
        return map_Kd;
    }
}
