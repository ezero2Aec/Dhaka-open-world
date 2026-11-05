package com.example.dhaka;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer implements GLSurfaceView.Renderer {
    private final Camera camera;
    private final Player player;
    private final Environment environment;
    private final TouchControls touchControls;
    private int screenWidth, screenHeight;

    private float[] mVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    private float deltaTime = 0;
    private long lastFrameTime;

    // Camera settings
    private float cameraAzimuth = 180f;
    private float cameraPitch = 20f;
    private float cameraDistance = 8f;

    public GameRenderer(int width, int height) {
        this.screenWidth = (width > 0) ? width : 1;
        this.screenHeight = (height > 0) ? height : 1;
        camera = new Camera();
        player = new Player();
        environment = new Environment();
        touchControls = new TouchControls(this.screenWidth, this.screenHeight);
        lastFrameTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.4f, 0.6f, 0.9f, 1f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        environment.initGLResources();
        player.initMeshes();
        environment.initMeshes();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int w, int h) {
        screenWidth = w;
        screenHeight = h;
        GLES20.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        long now = System.currentTimeMillis();
        deltaTime = (now - lastFrameTime) / 1000f;
        if (deltaTime > 0.1f) deltaTime = 0.1f;
        lastFrameTime = now;

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float aspect = (float) screenWidth / screenHeight;

        // Update player
        // Store previous position for collision rollback
        float prevX = player.getX();
        float prevZ = player.getZ();
        player.update(deltaTime, touchControls.getMoveX(), touchControls.getMoveY(),
            touchControls.isRunPressed());
        // Simple collision with houses
        if (environment.collides(player.getX(), player.getZ(), player.getCollisionRadius())) {
            // revert to previous safe position
            // (Assume Player has setters or we can directly set fields via reflection – we add simple setter methods)
            player.setPosition(prevX, prevZ);
        }

        // Handle jump
        if (touchControls.isJumpPressed()) {
            player.jump();
        }

        // Update camera from touch input
        if (touchControls.isCameraDrag()) {
            float sensitivity = 0.3f;
            cameraAzimuth -= touchControls.getCameraDragX() * sensitivity;
            cameraPitch -= touchControls.getCameraDragY() * sensitivity;
            cameraAzimuth = cameraAzimuth % 360f;
            if (cameraAzimuth < 0) cameraAzimuth += 360f;
            cameraPitch = Math.max(-45f, Math.min(45f, cameraPitch));
            touchControls.consumeCameraDrag();
        }

        // Set camera to follow player
        camera.setTarget(player.getX(), player.getY() + player.getHeight() * 0.6f, player.getZ());
        camera.setAzimuth(cameraAzimuth);
        camera.setPitch(cameraPitch);
        camera.setDistance(cameraDistance);
        camera.update(aspect);

        // Draw 3D world
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);

        environment.draw(camera.getVPMatrix());
        player.draw(camera.getVPMatrix(), camera.getNormalMatrix());
    }

    public boolean onTouchEvent(int action, int id, float x, float y) {
        return touchControls.onTouchEvent(action, id, x, y);
    }
}
