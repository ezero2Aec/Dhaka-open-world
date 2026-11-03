package com.example.dhaka;

import android.opengl.Matrix;

public class Player {
    private final float[] mModelMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];
    
    private final Mesh bodyMesh;
    private final Mesh headMesh;
    private final Mesh[] armMeshes = new Mesh[2];
    private final Mesh[] legMeshes = new Mesh[2];
    
    private float x, y, z;
    private float rotation;
    private float height = 1.8f;
    private float speed = 0.1f;
    private float runSpeed = 0.2f;
    private float jumpVelocity = 0;
    private float velocityY = 0;
    
    private boolean isGrounded = true;
    private boolean isRunning = false;
    private boolean isJumping = false;
    
    private float animationTime = 0;
    private float armAngle = 0;
    private float legAngle = 0;
    
    private final float[] normalMatrix = new float[16];
    
    public Player() {
        bodyMesh = createCapsuleMesh(0.4f, 0.8f, 16);
        headMesh = createCapsuleMesh(0.25f, 0.25f, 16);
        
        float armWidth = 0.15f, armHeight = 0.5f;
        armMeshes[0] = createCapsuleMesh(armWidth, armHeight, 16);
        armMeshes[1] = createCapsuleMesh(armWidth, armHeight, 16);
        
        float legWidth = 0.15f, legHeight = 0.6f;
        legMeshes[0] = createCapsuleMesh(legWidth, legHeight, 16);
        legMeshes[1] = createCapsuleMesh(legWidth, legHeight, 16);
        
        x = 0; y = height / 2; z = 0;
        rotation = 0;
        
        Matrix.setIdentityM(normalMatrix, 0);
    }
    
    public void update(float dt, float moveX, float moveY, boolean running) {
        isRunning = running;
        speed = isRunning ? runSpeed : 0.1f;
        
        if (isGrounded) {
            if (moveX != 0 || moveY != 0) {
                float angle = (float) Math.atan2(moveX, -moveY);
                rotation = (float) Math.toDegrees(angle);
                x += moveX * speed;
                z += moveY * speed;
            }
        }
        
        if (!isGrounded) {
            // Apply gravity
            velocityY += -0.25f * dt;
            y += velocityY * dt;
            // Ground collision
            if (y <= height / 2) {
                y = height / 2;
                velocityY = 0;
                isGrounded = true;
                isJumping = false;
            }
        }
        
        float animSpeed = isRunning ? 10f : 6f;
        animationTime += animSpeed * dt;
        armAngle = (float) Math.sin(animationTime) * 0.5f;
        legAngle = (float) Math.sin(animationTime) * 0.6f;
        
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, y, z);
        Matrix.rotateM(mModelMatrix, 0, rotation, 0, 1, 0);
    }
    
    public void jump() {
        if (isGrounded) {
            isGrounded = false;
            isJumping = true;
            velocityY = 0.35f;
        }
    }
    
    public void draw(float[] vpMatrix, float[] normMatrix) {
        System.arraycopy(normMatrix, 0, normalMatrix, 0, 16);
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mModelMatrix, 0);
        bodyMesh.draw(mvpMatrix, normalMatrix, new float[]{0.3f, 0.3f, 0.5f, 1f});
        
        float[] headMatrix = mModelMatrix.clone();
        Matrix.translateM(headMatrix, 0, 0, height * 0.65f, 0);
        headMesh.draw(mvpMatrix, normalMatrix, new float[]{0.8f, 0.6f, 0.4f, 1f});
        
        float[] leftArmMatrix = mModelMatrix.clone();
        Matrix.translateM(leftArmMatrix, 0, 0.35f, height * 0.3f, 0);
        Matrix.rotateM(leftArmMatrix, 0, -armAngle, 1, 0, 0);
        armMeshes[0].draw(mvpMatrix, normalMatrix, new float[]{0.8f, 0.6f, 0.4f, 1f});
        
        float[] rightArmMatrix = mModelMatrix.clone();
        Matrix.translateM(rightArmMatrix, 0, -0.35f, height * 0.3f, 0);
        Matrix.rotateM(rightArmMatrix, 0, armAngle, 1, 0, 0);
        armMeshes[1].draw(mvpMatrix, normalMatrix, new float[]{0.8f, 0.6f, 0.4f, 1f});
        
        float[] leftLegMatrix = mModelMatrix.clone();
        Matrix.translateM(leftLegMatrix, 0, 0.12f, -0.1f, 0);
        Matrix.rotateM(leftLegMatrix, 0, legAngle, 1, 0, 0);
        legMeshes[0].draw(mvpMatrix, normalMatrix, new float[]{0.2f, 0.2f, 0.3f, 1f});
        
        float[] rightLegMatrix = mModelMatrix.clone();
        Matrix.translateM(rightLegMatrix, 0, -0.12f, -0.1f, 0);
        Matrix.rotateM(rightLegMatrix, 0, -legAngle, 1, 0, 0);
        legMeshes[1].draw(mvpMatrix, normalMatrix, new float[]{0.2f, 0.2f, 0.3f, 1f});
    }
    
    private Mesh createCapsuleMesh(float radius, float height, float segments) {
        int seg = (int) segments;
        float[] vertices = new float[seg * 3];
        short[] indices = new short[seg * 6];
        float[] normals = new float[seg * 3];
        float[] color = new float[seg * 4];
        
        for (int i = 0; i < seg; i++) {
            float angle = (float) i / seg * (float) Math.PI * 2;
            int base = i * 3;
            vertices[base] = (float) Math.cos(angle) * radius;
            vertices[base + 1] = 0;
            vertices[base + 2] = (float) Math.sin(angle) * radius;
            normals[base] = vertices[base];
            normals[base + 1] = 0;
            normals[base + 2] = vertices[base + 2];
            color[base * 4] = 0.5f;
            color[base * 4 + 1] = 0.5f;
            color[base * 4 + 2] = 0.5f;
            color[base * 4 + 3] = 1f;
        }
        
        int idx = 0;
        for (int i = 0; i < seg; i++) {
            int next = (i + 1) % seg;
            int base = i * 3;
            indices[idx++] = (short) base;
            indices[idx++] = (short) next;
            indices[idx++] = (short) next;
            indices[idx++] = (short) base;
            indices[idx++] = (short) (base + 3);
            indices[idx++] = (short) (base + 3);
        }
        
        return new Mesh(vertices, normals, indices, color);
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getHeight() { return height; }
}
