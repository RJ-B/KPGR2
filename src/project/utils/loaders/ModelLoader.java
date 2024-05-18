package project.utils.loaders;

import project.nodes.Node;
import project.nodes.groups.Material;
import project.nodes.leaf.Geometry3D;
import project.utils.loaders.mtl.MtlMaterialData;
import project.utils.loaders.obj.ObjLoader;
import project.utils.loaders.obj.ObjObjectData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída pro načítání grafu scény
 */

public class ModelLoader {

    /**
     * Načte OBJ a MTL soubory a transformuje je do grafu scény.
     * @param modelPath cesta k OBJ souboru
     * @return graf scény
     */

    public static List<Node> loadModel(String modelPath) throws IOException {
        ObjLoader objLoader = new ObjLoader();
        List<ObjObjectData> dataList = objLoader.load(modelPath);

        List<Node> nodes = new ArrayList<>();
        for (ObjObjectData objData : dataList) {
            Geometry3D geometry3D = new Geometry3D(objData.getObjectName(), objData.getVertexBuffer(),
                    objData.getTextCoordBuffer(), objData.getNormalBuffer(), objData.getIndexBuffer());

            if (objData.getMaterial() != null) {
                MtlMaterialData materialData = objData.getMaterial();

                float d = materialData.getD() != null ? materialData.getD() : 1;

                if(materialData.getKa() != null && materialData.getKd() != null && materialData.getKs() != null) {
                    Material material = new Material(
                            new float[]{ materialData.getKa()[0], materialData.getKa()[1], materialData.getKa()[2], d },
                            new float[]{ materialData.getKd()[0], materialData.getKd()[1], materialData.getKd()[2], d },
                            new float[]{ materialData.getKs()[0], materialData.getKs()[1], materialData.getKs()[2], d },
                            materialData.getNs(),materialData.getName(),materialData.getD());
                    material.add(geometry3D);

                    nodes.add(material);
                }

            } else {
                nodes.add(geometry3D);
            }
        }
        return nodes;
    }
}
