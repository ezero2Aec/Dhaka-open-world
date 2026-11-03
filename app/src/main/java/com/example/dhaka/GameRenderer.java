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
    
    public GameRenderer(int width, int height) {
        // Ensure non-zero dimensions to avoid division by zero in later calculations.
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
        // Initialize OpenGL resources that require a valid context.
        environment.initGLResources();
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
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
        float aspect = (float) screenWidth / screenHeight;
        Matrix.frustumM(mProjectionMatrix, 0, -aspect * 0.1f, aspect * 0.1f,
            -0.1f, 0.1f, 0.1f, 100f);
        
        player.update(deltaTime, touchControls.getMoveX(), touchControls.getMoveY(),
            touchControls.getSprintPressed());
        
        if (touchControls.getJumpPressed()) {
            player.jump();
        }
        
        camera.setTarget(player.getX(), player.getY() + player.getHeight() * 0.5f, player.getZ());
        camera.update(aspect);
        
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        
        environment.draw(camera.getVPMatrix());
        player.draw(camera.getVPMatrix(), camera.getNormalMatrix());
    }
    
    public boolean onTouchEvent(int action, int id, float x, float y) {
        return touchControls.onTouchEvent(action, id, x, y);
    }
    
    public float getCameraDragX() { return touchControls.getCameraDragX(); }
    public float getCameraDragY() { return touchControls.getCameraDragY(); }
    public boolean isCameraDrag() { return touchControls.isCameraDrag(); }
}
