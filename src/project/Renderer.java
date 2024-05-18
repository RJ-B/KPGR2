package project;

import global.AbstractRenderer;
import global.GLCamera;
import global.GlutUtils;
import lwjglutils.OGLTexture2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import project.scene.PlaneScene;
import project.scene.Scene;
import project.scene.model.PlaneModel;
import project.utils.types.Disposable;
import project.utils.types.InputVector;
import transforms.Vec3D;
import transforms.Mat4PerspRH;

import java.io.IOException;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import project.nodes.groups.textures.Texture;

public class Renderer extends AbstractRenderer implements Disposable {

    private Scene scene;
    private GLCamera camera;
    private final float[] mousePosition = { 0f, 0f };

    private boolean cursorLocked = false;
    private final InputVector inputVector = new InputVector();

    private boolean wireframe = false;

    private float azimuth = 0;
    private float zenith = 0;

    private long lastUpdate;
    private long lastFpsUpdate;
    private int fps = 0;

    private boolean showAxes = true;


    public Renderer() {
        super();

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long window, int button, int action, int mods) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);

                glfwGetCursorPos(window, xBuffer, yBuffer);

                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    mousePosition[0] = (float) xBuffer.get(0);
                    mousePosition[1] = (float) yBuffer.get(0);

                    if (!cursorLocked) {
                        cursorLocked = true;
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    }
                }
            }
        };

        //ovládání myší, při locknutém kurzorum (při stisku levévé tlačítka myši, je získán rozdíl aktuální a původní pozice myši.
        //na základě tohoto výpočtu je vypočítána nová hodnota zenithu a azimuthu a tím je pohnuto kamerou
        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (cursorLocked) {
                    float dx = (float) x - mousePosition[0];
                    float dy = (float) y - mousePosition[1];
                    mousePosition[0] = (float) x;
                    mousePosition[1] = (float) y;

                    zenith += -dy / width * 180;
                    if (zenith > 90) zenith = 90;
                    if (zenith <= -90) zenith = -90;

                    azimuth += dx / height * 180;
                    azimuth %= 360;

                    camera.setAzimuth(Math.toRadians(azimuth));
                    camera.setZenith(Math.toRadians(zenith));
                }
            }
        };

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    switch (key) {
                        case GLFW_KEY_W:
                            inputVector.keyFp = 1;
                            break;
                        case GLFW_KEY_S:
                            inputVector.keyFn = -1;
                            break;
                        case GLFW_KEY_A:
                            inputVector.keySn = -1;
                            break;
                        case GLFW_KEY_D:
                            inputVector.keySp = 1;
                            break;
                        case GLFW_KEY_SPACE:
                            inputVector.keyVp = 1;
                            break;
                        case GLFW_KEY_LEFT_SHIFT:
                            inputVector.keyVn = -1;
                            break;
                        case GLFW_KEY_R:
                            wireframe = !wireframe;
                            break;
                        case GLFW_KEY_F:
                            if(!PlaneModel.isAccelerating) {PlaneModel.isAccelerating = true;}
                            else {
                                PlaneModel.isDecelerating = true;
                                PlaneModel.isAccelerating = false;
                            }
                            PlaneModel.elapsedTime = 0;
                            break;
                        case GLFW_KEY_E:
                            showAxes = !showAxes;
                            break;

                        case GLFW_KEY_ESCAPE:
                            if (cursorLocked) {
                                cursorLocked = false;
                                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                            }
                            break;
                    }
                } else if (action == GLFW_RELEASE) {
                    switch (key) {
                        case GLFW_KEY_W:
                            inputVector.keyFp = 0;
                            break;
                        case GLFW_KEY_S:
                            inputVector.keyFn = 0;
                            break;
                        case GLFW_KEY_A:
                            inputVector.keySn = 0;
                            break;
                        case GLFW_KEY_D:
                            inputVector.keySp = 0;
                            break;
                        case GLFW_KEY_SPACE:
                            inputVector.keyVp = 0;
                            break;
                        case GLFW_KEY_LEFT_SHIFT:
                            inputVector.keyVn = 0;
                    }
                }
            }
        };

    }

    @Override
    public void init() {
        super.init();
        glClearColor(0.35f, 0.35f, 0.35f, 1.0f); //barva pozadí

        glEnable(GL_DEPTH_TEST); //zapnutí hloubkového testování
        glDisable(GL_CULL_FACE);

        glPolygonMode(GL_FRONT, GL_FILL);
        glPolygonMode(GL_BACK, GL_FILL);
        glEnable(GL_NORMALIZE);

        // Nastavení modelview matice
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //gluLookAt(30, 20, 40, 0, 0, 0, 0, 1, 0); // pohled na bod (0,0,0)


        //glEnable(GL_LIGHT0);  // Zapnutí světla
        //float[] lightPos = {0.0f, 0.0f, 10.0f, 1.0f};  // Pozice světla
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPos);

        glShadeModel(GL_SMOOTH);  // Aktivace Gouraudova stínování

        camera(); //vytvoření kamery

        glMatrixMode(GL_PROJECTION); //transformace zobrazovacího objemu - projekce
        glLoadIdentity();

        Mat4PerspRH.perspective(60, width / (float) height, 0.001f, 100.0f); //transformace zobrazovacího objemu - perspektiva

        scene = new PlaneScene();
    }

    /**
     * metoda pro vytvoření kamery first person na pozici (0, 0, 10)
     */
    private void camera(){
        camera = new GLCamera();
        camera.setPosition(new Vec3D(0, 0, 4));
        camera.setFirstPerson(true);
    }

    /**
     * metoda pro vykreslení os
     */
    private void renderAxes() {
        if (!showAxes) {
            return;
        }
        glLineWidth(2);
        glBegin(GL_LINES);

        glColor3f(1f,0,0);
        glVertex3f(0,0,0);
        glVertex3f(1.5f,0,0);

        glColor3f(0,1f,0);
        glVertex3f(0,0,0);
        glVertex3f(0,1.5f,0);

        glColor3f(0,0,1f);
        glVertex3f(0,0,0);
        glVertex3f(0,0,1.5f);

        glEnd();
        glColor3f(1f,1f,1f);
    }


    @Override
    public void display() {
        long now = System.currentTimeMillis();
        long deltaTime = now - lastUpdate;
        lastUpdate = now;

        if (now - lastFpsUpdate >= 500) {
            lastFpsUpdate = now;
            fps = (int) Math.round(1000d / deltaTime);
        }

        glViewport(0,0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (wireframe) {
            glLineWidth(0.000001f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        } else {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.setMatrix();

        inputVector.update(deltaTime);
        float cameraSpeed = 0.002f;
        camera.forward(inputVector.getForward() * cameraSpeed);
        camera.right(inputVector.getStrafe() * cameraSpeed);
        camera.up(inputVector.getVertical() * cameraSpeed);

        renderAxes();

        scene.update(deltaTime);

        scene.draw();

        textRenderer.addStr2D(5, 20, "Nadpis: Model reálného světa: Letadlo");
        textRenderer.addStr2D(5, 35, "Autor: Rostislav Jirák, Datum: 22.04.2024");

        int ovladaniY = 65;
        if (cursorLocked) {
            textRenderer.addStr2D(5, ovladaniY, "[ESC] - Odemknutí myši");
            ovladaniY += 15;
            textRenderer.addStr2D(5, ovladaniY, "[Myš] - Rozhlížení se kamerou");
        } else {
            textRenderer.addStr2D(5, ovladaniY, "[LMB] - Rozhlížení se kamerou");
        }
        textRenderer.addStr2D(5, ovladaniY + 15, "[WASD] - Pohyb");
        textRenderer.addStr2D(5, ovladaniY + 30, "[Space] - Pohyb nahoru");
        textRenderer.addStr2D(5, ovladaniY + 45, "[Shift] - Pohyb dolů");
        textRenderer.addStr2D(5, ovladaniY + 60, "[R] - " + (wireframe ? "Vypnutí" : "Zapnutí") + " drátového modelu");
        textRenderer.addStr2D(5, ovladaniY + 75, "[F] - " + (PlaneModel.isAccelerating ? "Vypnutí" : "Zapnutí") + " animace");
        textRenderer.addStr2D(5, ovladaniY + 90, "[E] - " + (showAxes ? "Vypnutí" : "Zapnutí") + " Os");

        textRenderer.addStr2D(5, height - 5, String.format("FPS: %d", fps));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (scene != null) scene.dispose();
    }
}
