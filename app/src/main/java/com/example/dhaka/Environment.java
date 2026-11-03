package com.example.dhaka;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Environment {
    private final float[] skyColor = {0.4f, 0.6f, 0.9f, 1f};
    private final float[] groundColor = {0.35f, 0.35f, 0.35f, 1f};
    private final float[] roadColor = {0.2f, 0.2f, 0.2f, 1f};

    private final Mesh groundMesh;
    private final Mesh roadMesh;
    private final Mesh treeMesh;
    private final Mesh[] buildings;
    private final float[] treePositions;

    private int groundProgram;
    private int roadProgram;
    private int treeProgram;

    private int groundColorHandle;
    private int roadColorHandle;
    private int treeColorHandle;

    public Environment() {
        // Geometry creation – does not require an OpenGL context.
        groundMesh = createGroundMesh(200, 40);
        roadMesh = createRoadMesh(200, 40);
        treeMesh = createTreeMesh();
        buildings = createBuildings();
        treePositions = createTreePositions();
        // Shaders will be compiled later when a GL context is available via initGLResources().
    }

    /** Called from GameRenderer.onSurfaceCreated after a valid GL context exists. */
    public void initGLResources() {
        groundProgram = createShaderProgram(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);
        roadProgram = createShaderProgram(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);
        treeProgram = createShaderProgram(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);

        groundColorHandle = GLES20.glGetUniformLocation(groundProgram, "vColor");
        roadColorHandle = GLES20.glGetUniformLocation(roadProgram, "vColor");
        treeColorHandle = GLES20.glGetUniformLocation(treeProgram, "vColor");
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
                int iidx = (i * seg + j) * 6;
                indices[iidx] = (short) (i * seg + j);
                indices[iidx + 1] = (short) (i * seg + j + 1);
                indices[iidx + 2] = (short) ((i + 1) * seg + j + 1);
                indices[iidx + 3] = (short) (i * seg + j);
                indices[iidx + 4] = (short) ((i + 1) * seg + j + 1);
                indices[iidx + 5] = (short) ((i + 1) * seg + j);
                idx++;
            }
        }
        return new Mesh(vertices, null, indices, groundColor);
    }

    private Mesh createRoadMesh(float size, float segments) {
        int seg = (int) segments;
        float[] vertices = new float[seg * seg * 3];
        short[] indices = new short[seg * seg * 6];
        int idx = 0;
        for (int i = 0; i < seg; i++) {
            for (int j = 0; j < seg; j++) {
                float x = -size / 2 + (float) j / seg * size;
                float z = -size / 2 + (float) i / seg * size;
                if (Math.abs(x) < 3 || Math.abs(z) < 3) {
                    int base = idx * 3;
                    vertices[base] = x;
                    vertices[base + 1] = 0;
                    vertices[base + 2] = z;
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
        }
        return new Mesh(vertices, null, indices, roadColor);
    }

    private Mesh createTreeMesh() {
        float[] vertices = {0, 0, 0, 0, 0.8f, 0, 0.3f, 0.8f, 0, 0, 1.5f, 0, -0.3f, 0.8f, 0};
        short[] indices = {0, 1, 2, 0, 2, 4, 0, 4, 3, 0, 3, 1};
        return new Mesh(vertices, null, indices, new float[]{0.1f, 0.5f, 0.2f, 1f});
    }

    private Mesh[] createBuildings() {
        // Placeholder – building meshes omitted for now.
        return new Mesh[0];
    }

    private float[] createBuildingVertices(float w, float h, float d) {
        return new float[] {
            -w/2, 0, d/2, w/2, 0, d/2, w/2, h, d/2, -w/2, h, d/2,
            w/2, 0, -d/2, -w/2, 0, -d/2, -w/2, h, -d/2, w/2, h, -d/2
        };
    }

    private float[] createTreePositions() {
        float[] trees = new float[48];
        int idx = 0;
        for (int i = 0; i < 24; i++) {
            trees[idx++] = (float) Math.random() * 160 - 80;
            trees[idx++] = (float) Math.random() * 160 - 80;
        }
        return trees;
    }

    public void draw(float[] vpMatrix) {
        GLES20.glUseProgram(groundProgram);
        GLES20.glUniform4fv(groundColorHandle, 1, groundColor, 0);
        groundMesh.draw(vpMatrix, null, groundColor);

        GLES20.glUseProgram(roadProgram);
        GLES20.glUniform4fv(roadColorHandle, 1, roadColor, 0);
        roadMesh.draw(vpMatrix, null, roadColor);

        GLES20.glUseProgram(treeProgram);
        for (int i = 0; i < 24; i++) {
            float[] model = new float[16];
            Matrix.setIdentityM(model, 0);
            Matrix.translateM(model, 0, treePositions[i * 2], 0, treePositions[i * 2 + 1]);
            float[] mvp = new float[16];
            Matrix.multiplyMM(mvp, 0, vpMatrix, 0, model, 0);
            treeMesh.draw(mvp, null, new float[]{0.1f, 0.5f, 0.2f, 1f});
        }
    }
}
