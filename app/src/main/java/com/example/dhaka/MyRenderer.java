package com.example.dhaka;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    private final Cube cube;
    private final float[] mVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private float angle;

    public MyRenderer() {
        cube = new Cube();
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        cube.init();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        Matrix.setLookAtM(mViewMatrix, 0,
                0, 0, 3, // eye
                0, 0, 0, // center
                0, 1, 0); // up

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angle, 0, 1, 0);
        Matrix.rotateM(mModelMatrix, 0, angle * 0.5f, 1, 0, 0);

        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, temp, 0);

        cube.draw(mVPMatrix);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }
}
