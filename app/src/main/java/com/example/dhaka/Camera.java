package com.example.dhaka;

import android.opengl.Matrix;

public class Camera {
    private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mVpMatrix = new float[16];
    private final float[] mNormalMatrix = new float[16];

    private float eyeX, eyeY, eyeZ;
    private float lookAtX, lookAtY, lookAtZ;
    private float upX, upY, upZ;

    private float azimuth = 180f;
    private float pitch = 20f;
    private float distance = 8f;

    private float targetX, targetY, targetZ;

    public Camera() {
        upX = 0; upY = 1; upZ = 0;
        eyeX = 0; eyeY = 2; eyeZ = distance;
        lookAtX = 0; lookAtY = 0; lookAtZ = 0;
        targetX = 0; targetY = 0; targetZ = 0;
    }

    public void setTarget(float x, float y, float z) {
        targetX = x;
        targetY = y;
        targetZ = z;
        // Keep look‑at point synchronized with the target so the camera
        // always points at the player centre.
        lookAtX = targetX;
        lookAtY = targetY;
        lookAtZ = targetZ;
    }

    public void update(float aspectRatio) {
        float cosAzimuth = (float) Math.cos(Math.toRadians(azimuth));
        float sinAzimuth = (float) Math.sin(Math.toRadians(azimuth));
        float cosPitch = (float) Math.cos(Math.toRadians(pitch));
        float sinPitch = (float) Math.sin(Math.toRadians(pitch));

        eyeX = targetX + distance * cosPitch * cosAzimuth;
        eyeY = targetY + distance * sinPitch;
        eyeZ = targetZ + distance * cosPitch * sinAzimuth;

        Matrix.setLookAtM(mViewMatrix, 0,
            eyeX, eyeY, eyeZ,
            lookAtX, lookAtY, lookAtZ,
            upX, upY, upZ);

        float ratio = aspectRatio;
        float near = 0.1f;
        float far = 100f;
        float top = (float) (Math.tan(Math.toRadians(45.0) / 2) * near);
        float bottom = -top;
        float right = top * aspectRatio;
        float left = -right;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        Matrix.multiplyMM(mVpMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        float[] rotation = new float[16];
        Matrix.setRotateEulerM(rotation, 0, pitch, azimuth, 0);
        for (int i = 0; i < 16; i++) {
            mNormalMatrix[i] = rotation[i];
        }
    }

    public float[] getViewMatrix() { return mViewMatrix; }
    public float[] getProjectionMatrix() { return mProjectionMatrix; }
    public float[] getVPMatrix() { return mVpMatrix; }
    public float[] getNormalMatrix() { return mNormalMatrix; }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public void addAzimuth(float delta) {
        this.azimuth += delta;
        if (this.azimuth >= 360f) this.azimuth -= 360f;
        if (this.azimuth < 0f) this.azimuth += 360f;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        if (this.pitch > 45f) this.pitch = 45f;
        if (this.pitch < -45f) this.pitch = -45f;
    }

    public void addPitch(float delta) {
        this.pitch += delta;
        if (this.pitch > 45f) this.pitch = 45f;
        if (this.pitch < -45f) this.pitch = -45f;
    }

    public void setDistance(float distance) {
        this.distance = distance;
        if (this.distance < 4f) this.distance = 4f;
        if (this.distance > 15f) this.distance = 15f;
    }
}
