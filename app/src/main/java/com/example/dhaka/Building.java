package com.example.dhaka;

import android.opengl.Matrix;

public class Building {
    private final float[] mModelMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];
    private final float[] normalMatrix = new float[16];

    private final Mesh mainMesh;
    private final Mesh roofMesh;
    private final Mesh doorMesh;

    private final float x, y, z;
    private final float width, height, depth;

    private float[] color;

    public Building(float x, float y, float z, float width, float height, float depth, float[] color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.color = color;

        mainMesh = createBoxMesh(width, height, depth);
        roofMesh = createBoxMesh(width * 1.1f, 0.3f, depth * 1.1f);
        doorMesh = createBoxMesh(0.6f, 1.2f, 0.1f);

        Matrix.setIdentityM(normalMatrix, 0);
    }

    public void draw(float[] vpMatrix) {
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, y, z);
        Matrix.scaleM(mModelMatrix, 0, width, height, depth);

        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mModelMatrix, 0);
        mainMesh.draw(mvpMatrix, normalMatrix, new float[]{0.5f, 0.5f, 0.5f, 1f});

        float[] roofMatrix = mModelMatrix.clone();
        Matrix.translateM(roofMatrix, 0, 0, height * 0.45f, 0);
        roofMesh.draw(mvpMatrix, normalMatrix, new float[]{0.3f, 0.3f, 0.4f, 1f});

        float[] doorMatrix = mModelMatrix.clone();
        Matrix.translateM(doorMatrix, 0, 0, -height * 0.25f, depth * 0.5f);
        Matrix.scaleM(doorMatrix, 0, 0.6f, 1.2f, 0.1f);
        doorMesh.draw(mvpMatrix, normalMatrix, new float[]{0.2f, 0.2f, 0.1f, 1f});
    }

    public boolean contains(float px, float pz, float radius) {
        float halfW = width / 2;
        float halfD = depth / 2;
        return px + radius > x - halfW && px - radius < x + halfW &&
               pz + radius > z - halfD && pz - radius < z + halfD;
    }

    private Mesh createBoxMesh(float w, float h, float d) {
        float[] vertices = {
            // Front
            -w/2, 0, d/2, w/2, 0, d/2, w/2, h, d/2, -w/2, h, d/2,
            // Back
            w/2, 0, -d/2, -w/2, 0, -d/2, -w/2, h, -d/2, w/2, h, -d/2,
            // Left
            -w/2, 0, -d/2, -w/2, 0, d/2, -w/2, h, d/2, -w/2, h, -d/2,
            // Right
            w/2, 0, d/2, w/2, 0, -d/2, w/2, h, -d/2, w/2, h, d/2,
            // Top
            -w/2, h, d/2, -w/2, h, -d/2, w/2, h, -d/2, w/2, h, d/2,
            // Bottom
            -w/2, 0, -d/2, w/2, 0, -d/2, w/2, 0, d/2, -w/2, 0, d/2
        };
        short[] indices = {
            0,1,2, 0,2,3, 4,5,6, 4,6,7, 8,9,10, 8,10,11,
            12,13,14, 12,14,15, 16,17,18, 16,18,19, 20,21,22, 20,22,23
        };
        return new Mesh(vertices, null, indices, null);
    }
}
