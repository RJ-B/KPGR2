package project.utils.loaders.obj;

import project.utils.loaders.mtl.MtlLoader;
import project.utils.loaders.mtl.MtlMaterialData;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída pro načítání OBJ souborů
 */

public class ObjLoader {

    List<ObjObjectData> objects;
    List<MtlMaterialData> materials;

    List<float[]> vData; // List of Vertex Coordinates
    List<float[]> vtData; // List of Texture Coordinates
    List<float[]> vnData; // List of Normal Coordinates

    List<Float> vertexBuffer;
    List<Float> textCoordBuffer;
    List<Float> normalBuffer;
    List<Integer> indexBuffer;
    MtlMaterialData material;

    /**
     * Načte data z OBJ souboru
     * @param modelPath což je cesta k OBJ souboru
     * @return data obsažená v OBJ souboru
     */

    public List<ObjObjectData> load(String modelPath) throws IOException {
        InputStream is = ObjLoader.class.getResourceAsStream(modelPath);

        if (is == null)
            throw new FileNotFoundException("File was not found: " + modelPath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            objects = new ArrayList<>();
            vData = new ArrayList<>();
            vtData = new ArrayList<>();
            vnData = new ArrayList<>();

            String objectName = null;
            String line = null;
            while ((line = reader.readLine()) != null) {

                if (line.startsWith("#")) {

                } else if (line.equals("")) {
                    // Ignore whitespace data
                } else if (line.startsWith("mtllib")) {
                    String[] s = line.split("\\s+");
                    MtlLoader matLoader = new MtlLoader();
                    File modelFile = new File(modelPath);
                    Path matFilePath = Paths.get(modelFile.getParent(), s[1]);
                    materials = matLoader.load(matFilePath.toString().replace("\\", "/"));
                } else if (line.startsWith("usemtl ")) {

                    createNewObject(objectName);

                    String[] s = line.split("\\s+");
                    String materialName = s[1];
                    if (materials != null) {
                        for (MtlMaterialData mat : materials) {
                            if (mat.getName().equals(materialName)) {
                                material = mat;
                                break;
                            }
                        }
                    }
                } else if (line.startsWith("g ")) {

                    createNewObject(objectName);

                    // Start new OBJ object
                    String[] s = line.split("\\s+");
                    if (s.length > 1) {
                        objectName = s[1];
                    }
                } else if (line.startsWith("v ")) {
                    // Read in Vertex Data
                    vData.add(processData(line));

                } else if (line.startsWith("vt ")) {
                    // Read Texture Coordinates
                    vtData.add(processData(line));

                } else if (line.startsWith("vn ")) {
                    // Read Normal Coordinates
                    vnData.add(processData(line));

                } else if (line.startsWith("f ")) {
                    // Read Face (index) Data
                    if (vertexBuffer == null)
                        vertexBuffer = new ArrayList<>();

                    if (textCoordBuffer == null)
                        textCoordBuffer = new ArrayList<>();

                    if (normalBuffer == null)
                        normalBuffer = new ArrayList<>();

                    if (indexBuffer == null)
                        indexBuffer = new ArrayList<>();

                    processFaceData(line);

                }
            }

            createNewObject(objectName);

            return objects;

        } finally {

            vData = null;
            vtData = null;
            vnData = null;

            vertexBuffer = null;
            textCoordBuffer = null;
            normalBuffer = null;
            indexBuffer = null;
            material = null;

            objects = null;
            materials = null;
        }
    }

    private void createNewObject(String objectName) {
        // Create OBJ object with accumulated data
        if (vertexBuffer != null) {
            ObjObjectData objObjectData = new ObjObjectData(objectName,
                    floatListtoArray(vertexBuffer),
                    floatListtoArray(textCoordBuffer),
                    floatListtoArray(normalBuffer),
                    intListToArray(indexBuffer),
                    material);
            objects.add(objObjectData);
        }

        vertexBuffer    = null;
        textCoordBuffer = null;
        normalBuffer    = null;
        indexBuffer     = null;
        material        = null;
    }

    private int[] intListToArray(List<Integer> list){
        int[] array = new int[list.size()];
        int i = 0;

        for (Integer value : list) {
            array[i++] = value;
        }

        return array;
    }

    private float[] floatListtoArray(List<Float> list){
        float[] array = new float[list.size()];
        int i = 0;

        for (Float value : list) {
            array[i++] = value;
        }

        return array;
    }

    private float[] processData(String read) {
        String[] s = read.split("\\s+");
        return (processFloatData(s));
    }

    private float[] processFloatData(String[] sdata) {
        float[] data = new float[sdata.length - 1];
        for (int i = 0; i < data.length; i++) {
            data[i] = Float.parseFloat(sdata[i + 1]);
        }
        return data;
    }

    private void processFaceData(String fread){
        String[] s = fread.split("\\s+");

        String[] vertex1 = s[1].split("/");
        String[] vertex2 = s[2].split("/");
        String[] vertex3 = s[3].split("/");

        processVertex(vertex1);
        processVertex(vertex2);
        processVertex(vertex3);

        if(s.length > 4){
            String[] vertex4 = s[4].split("/");

            processVertex(vertex1);
            processVertex(vertex4);
            processVertex(vertex3);
        }
    }

    private void processVertex(String[] vertex) {
        int vertexIndex = Integer.parseInt(vertex[0]) - 1;

        float[] vertexCoord = vData.get(vertexIndex);
        vertexBuffer.add(vertexCoord[0]);
        vertexBuffer.add(vertexCoord[1]);
        vertexBuffer.add(vertexCoord[2]);

        int index = vertexBuffer.size() / 3 - 1;

        if(!vertex[1].isEmpty()) {
            int textureIndex = Integer.parseInt(vertex[1]) - 1;
            float[] textCoord = vtData.get(textureIndex);
            textCoordBuffer.add(textCoord[0]);
            textCoordBuffer.add(1 - textCoord[1]);
        }

        if(!vertex[2].isEmpty()){
            int normalIndex = Integer.parseInt(vertex[2]) - 1;
            float[] normal = vnData.get(normalIndex);
            normalBuffer.add(normal[0]);
            normalBuffer.add(normal[1]);
            normalBuffer.add(normal[2]);
        }

        indexBuffer.add(index);
    }
}
