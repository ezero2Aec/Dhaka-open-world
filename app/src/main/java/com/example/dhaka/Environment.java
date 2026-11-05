package com.example.dhaka;

import android.opengl.GLES20;
import com.example.dhaka.Building;
import java.util.ArrayList;
import java.util.List;
import android.opengl.Matrix;

public class Environment {
    private final float[] groundColor = {0.4f, 0.35f, 0.3f, 1f};
    private final float[] roadColor = {0.25f, 0.25f, 0.25f, 1f};
    private final float[] sidewalkColor = {0.6f, 0.6f, 0.6f, 1f};

    private final Mesh groundMesh;
    private final Mesh roadMesh;
    private final Mesh leftSidewalkMesh;
    private final Mesh rightSidewalkMesh;
    private final java.util.List<Building> houses = new java.util.ArrayList<>();

    private int groundProgram;
    private int roadProgram;
    private int buildingProgram;

    private int groundColorHandle;
    private int roadColorHandle;

    public Environment() {
        groundMesh = createGroundMesh(200, 40);
        roadMesh = createRoadMesh(200, 6);
        // Create sidewalks (width 2) on each side of the road
        leftSidewalkMesh = createSidewalkMesh(200, 2, -(6/2 + 1));
        rightSidewalkMesh = createSidewalkMesh(200, 2, (6/2 + 1));
        // Create houses in a compact neighborhood
        // Road runs horizontally along X axis (Z = 0). Sidewalks are on both sides of the road.
        // House positions are placed beyond the sidewalks (outside the road width).
        // Left side houses (negative X)
        houses.add(new Building(-8f, 0f, -7f, 4f, 6f, 4f, new float[]{0.7f, 0.6f, 0.5f, 1f}));
        houses.add(new Building(-8f, 0f, 7f, 4f, 6f, 4f, new float[]{0.7f, 0.6f, 0.5f, 1f}));
        // Right side houses (positive X)
        houses.add(new Building(8f, 0f, -7f, 4f, 6f, 4f, new float[]{0.6f, 0.5f, 0.7f, 1f}));
        houses.add(new Building(8f, 0f, 7f, 4f, 6f, 4f, new float[]{0.6f, 0.5f, 0.7f, 1f}));
    }

    public void initGLResources() {
        groundProgram = createShaderProgram(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);
        roadProgram = createShaderProgram(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);
        buildingProgram = createShaderProgram(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);

        groundColorHandle = GLES20.glGetUniformLocation(groundProgram, "vColor");
        roadColorHandle = GLES20.glGetUniformLocation(roadProgram, "vColor");
        groundColorHandle = GLES20.glGetUniformLocation(buildingProgram, "vColor");
    }

    public void initMeshes() {
        String vs = Shader.DEFAULT_VERTEX_SHADER;
        String fs = Shader.DEFAULT_FRAGMENT_SHADER;
        groundMesh.init(vs, fs);
        roadMesh.init(vs, fs);
        leftSidewalkMesh.init(vs, fs);
        rightSidewalkMesh.init(vs, fs);
        // No building mesh; houses are drawn individually
    }

    private int createShaderProgram(String vs, String fs) {
        int vShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, vs);
        int fShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, fs);
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        GLES20.glLinkProgram(program);
        return program;
    }

    private Mesh createGroundMesh(float size, float segments) {
        int seg = (int) segments;
        float[] vertices = new float[seg * seg * 3];
        short[] indices = new short[seg * seg * 6];
        int idx = 0;

        for (int i = 0; i < seg; i++) {
            for (int j = 0; j < seg; j++) {
                int base = (i * seg + j) * 3;
                vertices[base] = -size / 2 + (float) j / seg * size;
                vertices[base + 1] = 0;
                vertices[base + 2] = -size / 2 + (float) i / seg * size;

                int iidx = idx * 6;
                indices[iidx] = (short) (i * seg + j);
                indices[iidx + 1] = (short) (i * seg + j + 1);
                indices[iidx + 2] = (short) ((i + 1) * seg + j + 1);
                indices[iidx + 3] = (short) (i * seg + j);
                indices[iidx + 4] = (short) ((i + 1) * seg + j + 1);
                indices[iidx + 5] = (short) ((i + 1) * seg + j);
                idx++;
            }
        }

        float[] normals = new float[seg * seg * 3];
        for (int i = 0; i < seg * seg * 3; i += 3) {
            normals[i + 1] = 1;
        }

        return new Mesh(vertices, normals, indices, groundColor);
    }

    private Mesh createRoadMesh(float length, float width) {
        // Simple rectangular road mesh (length along X, width along Z)
        float halfLength = length / 2;
        float halfWidth = width / 2;
        float[] vertices = {
            -halfLength, 0.02f, -halfWidth,
            halfLength, 0.02f, -halfWidth,
            halfLength, 0.02f, halfWidth,
            -halfLength, 0.02f, halfWidth
        };
        short[] indices = {0, 1, 2, 0, 2, 3};
        float[] normals = {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};
        return new Mesh(vertices, normals, indices, roadColor);
    }

    /** Create a sidewalk mesh offset from the road */
    private Mesh createSidewalkMesh(float length, float width, float offsetZ) {
        float halfLength = length / 2;
        float halfWidth = width / 2;
        // Vertices are shifted by offsetZ along Z axis
        float[] vertices = {
            -halfLength, 0.02f, -halfWidth + offsetZ,
            halfLength, 0.02f, -halfWidth + offsetZ,
            halfLength, 0.02f, halfWidth + offsetZ,
            -halfLength, 0.02f, halfWidth + offsetZ
        };
        short[] indices = {0, 1, 2, 0, 2, 3};
        float[] normals = {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};
        return new Mesh(vertices, normals, indices, sidewalkColor);
    }

    private Mesh createBuildingMesh() {
        float w = 3f, h = 8f, d = 3f;
        float[] vertices = {
            -w/2, 0, d/2, w/2, 0, d/2, w/2, h, d/2, -w/2, h, d/2,
            w/2, 0, -d/2, -w/2, 0, -d/2, -w/2, h, -d/2, w/2, h, -d/2,
            -w/2, h, -d/2, w/2, h, -d/2, w/2, h, d/2, -w/2, h, d/2
        };
        short[] indices = {0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11};
        float[] normals = {
            0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
            0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
            0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0
        };
        float[] colors = new float[12 * 4];
        for (int i = 0; i < 12; i++) {
            colors[i * 4] = 0.5f + (float) (i % 3) * 0.1f;
            colors[i * 4 + 1] = 0.5f + (float) (i % 3) * 0.1f;
            colors[i * 4 + 2] = 0.6f + (float) (i % 3) * 0.05f;
            colors[i * 4 + 3] = 1f;
        }
        return new Mesh(vertices, normals, indices, colors);
    }

    private float[] createBuildingPositions() {
        float[] buildings = new float[20 * 2];
        int idx = 0;
        for (int i = 0; i < 20; i++) {
            float angle = (float) i / 20 * (float) Math.PI * 2;
            float radius = 15 + (float) Math.random() * 30;
            buildings[idx++] = (float) Math.cos(angle) * radius;
            buildings[idx++] = (float) Math.sin(angle) * radius;
        }
        return buildings;
    }

    public void draw(float[] vpMatrix) {
        GLES20.glUseProgram(groundProgram);
        GLES20.glUniform4fv(groundColorHandle, 1, groundColor, 0);
        groundMesh.draw(vpMatrix, null, groundColor);

        // Draw left sidewalk
        GLES20.glUseProgram(roadProgram);
        GLES20.glUniform4fv(roadColorHandle, 1, sidewalkColor, 0);
        leftSidewalkMesh.draw(vpMatrix, null, sidewalkColor);
        // Draw right sidewalk
        GLES20.glUniform4fv(roadColorHandle, 1, sidewalkColor, 0);
        rightSidewalkMesh.draw(vpMatrix, null, sidewalkColor);
        // Draw road
        GLES20.glUniform4fv(roadColorHandle, 1, roadColor, 0);
        roadMesh.draw(vpMatrix, null, roadColor);

        GLES20.glUseProgram(buildingProgram);
        // Draw houses
        for (Building house : houses) {
            house.draw(vpMatrix);
        }
    }

    /**
     * Simple collision test against all houses.
     */
    public boolean collides(float x, float z, float radius) {
        for (Building house : houses) {
            if (house.contains(x, z, radius)) {
                return true;
            }
        }
        return false;
    }
}
