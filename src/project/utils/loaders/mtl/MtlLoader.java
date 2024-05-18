package project.utils.loaders.mtl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída pro načítání MTL souborů
 */

public class MtlLoader {

    List<MtlMaterialData> materials;

    /**
     * Name of the material
     */
    String materialName = null;

    /**
     * Ambient color
     */
    float[] ka = null;

    /**
     * Diffuse color
     */
    float[] kd = null;

    /**
     * Specular color
     */
    float[] ks = null;

    /**
     * Specular highlights
     */
    Float ns = null;

    /**
     * Optical density
     */
    Float ni = null;

    /**
     * Dissolve
     */
    Float d = null;

    /**
     * Illumination model
     */
    Integer illum = null;

    /**
     * Color texture file to be applied to the diffuse reflectivity of the material
     */
    String map_Kd = null;

    public MtlLoader(){
    }

    public List<MtlMaterialData> load(String filePath) throws IOException {
        InputStream is = MtlLoader.class.getResourceAsStream(filePath);

        if (is == null)
            throw new FileNotFoundException("File was not found: " + filePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            materials = new ArrayList<>();

            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                } else if (line.equals("")) {
                } else if (line.startsWith("newmtl ")) {

                    // Create MTL data object with accumulated data
                    if (materialName != null) {
                        MtlMaterialData materialData = new MtlMaterialData(materialName, ka, kd, ks, ns, ni, d, illum, map_Kd);
                        materials.add(materialData);
                    }

                    // Start new OBJ object
                    String[] s = line.split("\\s+");
                    if (s.length > 1) {
                        materialName = s[1];
                    }

                    clearAccumulatedData();
                } else if (line.startsWith("Ka ")) {
                    String[] s = line.split("\\s+");
                    ka = new float[3];
                    ka[0] = Float.parseFloat(s[1]);
                    ka[1] = Float.parseFloat(s[2]);
                    ka[2] = Float.parseFloat(s[3]);
                } else if (line.startsWith("Kd ")) {
                    String[] s = line.split("\\s+");
                    kd = new float[3];
                    kd[0] = Float.parseFloat(s[1]);
                    kd[1] = Float.parseFloat(s[2]);
                    kd[2] = Float.parseFloat(s[3]);
                } else if (line.startsWith("Ks ")) {
                    String[] s = line.split("\\s+");
                    ks = new float[3];
                    ks[0] = Float.parseFloat(s[1]);
                    ks[1] = Float.parseFloat(s[2]);
                    ks[2] = Float.parseFloat(s[3]);
                } else if (line.startsWith("Ns ")) {
                    String[] s = line.split("\\s+");
                    ns = Float.parseFloat(s[1]);
                } else if (line.startsWith("Ni ")) {
                    String[] s = line.split("\\s+");
                    ni = Float.parseFloat(s[1]);
                } else if (line.startsWith("d ")) {
                    String[] s = line.split("\\s+");
                    d = Float.parseFloat(s[1]);
                } else if (line.startsWith("illum ")) {
                    String[] s = line.split("\\s+");
                    illum = Integer.parseInt(s[1]);
                } else if (line.startsWith("map_Kd ")) {
                    String[] s = line.split("\\s+");

                    if (s.length > 1)
                        map_Kd = s[1];
                }
            }

            if (materialName != null) {
                MtlMaterialData materialData = new MtlMaterialData(materialName, ka, kd, ks, ns, ni, d, illum, map_Kd);
                materials.add(materialData);
            }

            return materials;
        } finally {

            clearAccumulatedData();
        }
    }

    private void clearAccumulatedData(){
        ka = null;
        kd = null;
        ks = null;
        ni = null;
        d = null;
        illum = null;
        map_Kd = null;
    }
}
