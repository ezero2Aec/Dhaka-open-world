package com.example.dhaka;

import android.opengl.Matrix;

/**
 * 3D Player Character - GTA Vice City style protagonist.
 * Low-poly human model with:
 * - Head with face details
 * - Torso with short-sleeved shirt
 * - Arms with hands
 * - Legs with trousers
 * - Proper body proportions
 * - Walking/running animations
 */
public class Player {
    // Model matrix for the player
    private final float[] mModelMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];

    // Meshes for body parts
    private final Mesh headMesh;
    private final Mesh torsoMesh;
    private final Mesh[] armMeshes = new Mesh[2];
    private final Mesh[] handMeshes = new Mesh[2];
    private final Mesh[] legMeshes = new Mesh[2];
    private final Mesh[] shoeMeshes = new Mesh[2];

    // Position and rotation
    private float x, y, z;
    private float rotation;

    // Physics properties
    private final float height = 1.8f;      // Player height
    private final float walkSpeed = 0.12f;  // Walking speed
    private final float runSpeed = 0.25f;   // Running speed
    private float velocityY = 0;

    // State flags
    private boolean isGrounded = true;
    private boolean isRunning = false;
    private boolean isJumping = false;

    // Animation state
    private float animationTime = 0;
    private float armAngle = 0;
    private float legAngle = 0;
    private float headBounce = 0;

    // Normal matrix for lighting
    private final float[] normalMatrix = new float[16];

    public Player() {
        // Build 3D character mesh
        headMesh = createHeadMesh();
        torsoMesh = createTorsoMesh();

        float armWidth = 0.12f, armHeight = 0.45f;
        armMeshes[0] = createArmMesh(armWidth, armHeight);
        armMeshes[1] = createArmMesh(armWidth, armHeight);

        handMeshes[0] = createHandMesh();
        handMeshes[1] = createHandMesh();

        float legWidth = 0.14f, legHeight = 0.55f;
        legMeshes[0] = createLegMesh(legWidth, legHeight);
        legMeshes[1] = createLegMesh(legWidth, legHeight);

        shoeMeshes[0] = createShoeMesh();
        shoeMeshes[1] = createShoeMesh();

        // Initial position
        x = 0;
        y = height / 2;
        z = 0;
        rotation = 0;

        Matrix.setIdentityM(normalMatrix, 0);
    }

    /** Initialize all meshes with shader programs (must be called after GL context exists) */
    public void initMeshes() {
        String vs = Shader.DEFAULT_VERTEX_SHADER;
        String fs = Shader.DEFAULT_FRAGMENT_SHADER;
        headMesh.init(vs, fs);
        torsoMesh.init(vs, fs);
        armMeshes[0].init(vs, fs);
        armMeshes[1].init(vs, fs);
        handMeshes[0].init(vs, fs);
        handMeshes[1].init(vs, fs);
        legMeshes[0].init(vs, fs);
        legMeshes[1].init(vs, fs);
        shoeMeshes[0].init(vs, fs);
        shoeMeshes[1].init(vs, fs);
    }

    /** Update player physics and animation */
    public void update(float dt, float moveX, float moveY, boolean running) {
        isRunning = running;
        float speed = isRunning ? runSpeed : walkSpeed;

        // Movement and rotation
        if (isGrounded && (moveX != 0 || moveY != 0)) {
            float angle = (float) Math.atan2(moveX, -moveY);
            rotation = (float) Math.toDegrees(angle);
            x += moveX * speed;
            z += moveY * speed;
        }

        // Gravity and jumping
        if (!isGrounded) {
            velocityY += -0.4f * dt;  // Gravity
            y += velocityY * dt;
            if (y <= height / 2) {
                y = height / 2;
                velocityY = 0;
                isGrounded = true;
                isJumping = false;
            }
        }

        // Animation based on movement
        float animSpeed = isRunning ? 12f : 8f;
        animationTime += animSpeed * dt;

        // Limb oscillation
        armAngle = (float) Math.sin(animationTime) * 0.6f;
        legAngle = (float) Math.sin(animationTime) * 0.7f;

        // Head bounce
        headBounce = isGrounded && (moveX != 0 || moveY != 0)
            ? (float) Math.abs(Math.sin(animationTime * 2)) * 0.05f
            : 0;

        // Build model matrix
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, y + headBounce, z);
        Matrix.rotateM(mModelMatrix, 0, rotation, 0, 1, 0);
    }

    /** Jump if on ground */
    public void jump() {
        if (isGrounded) {
            isGrounded = false;
            isJumping = true;
            velocityY = 0.5f;  // Jump velocity
        }
    }

    /** Render all body parts */
    public void draw(float[] vpMatrix, float[] normMatrix) {
        System.arraycopy(normMatrix, 0, normalMatrix, 0, 16);
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mModelMatrix, 0);

        // Skin color
        float[] skinColor = {0.85f, 0.7f, 0.6f, 1f};
        // Shirt color (1980s casual)
        float[] shirtColor = {0.2f, 0.6f, 0.9f, 1f};
        // Trousers color
        float[] pantsColor = {0.3f, 0.25f, 0.2f, 1f};
        // Shoes color
        float[] shoeColor = {0.15f, 0.15f, 0.15f, 1f};

        // Draw torso
        torsoMesh.draw(mvpMatrix, normalMatrix, shirtColor);

        // Draw head
        headMesh.draw(mvpMatrix, normalMatrix, skinColor);

        // Calculate arm positions based on animation
        float armOffsetY = height * 0.4f;
        float armOffsetX = 0.35f;
        float legOffsetY = -0.1f;
        float legOffsetX = 0.12f;

        // Left arm
        float[] leftArmMatrix = mModelMatrix.clone();
        Matrix.translateM(leftArmMatrix, 0, armOffsetX, armOffsetY, 0);
        Matrix.rotateM(leftArmMatrix, 0, -armAngle, 1, 0, 0);
        armMeshes[0].draw(mvpMatrix, normalMatrix, shirtColor);

        // Left hand
        float[] leftHandMatrix = mModelMatrix.clone();
        Matrix.translateM(leftHandMatrix, 0, armOffsetX * 1.4f, armOffsetY - 0.35f, 0);
        handMeshes[0].draw(mvpMatrix, normalMatrix, skinColor);

        // Right arm
        float[] rightArmMatrix = mModelMatrix.clone();
        Matrix.translateM(rightArmMatrix, 0, -armOffsetX, armOffsetY, 0);
        Matrix.rotateM(rightArmMatrix, 0, armAngle, 1, 0, 0);
        armMeshes[1].draw(mvpMatrix, normalMatrix, shirtColor);

        // Right hand
        float[] rightHandMatrix = mModelMatrix.clone();
        Matrix.translateM(rightHandMatrix, 0, -armOffsetX * 1.4f, armOffsetY - 0.35f, 0);
        handMeshes[1].draw(mvpMatrix, normalMatrix, skinColor);

        // Left leg
        float[] leftLegMatrix = mModelMatrix.clone();
        Matrix.translateM(leftLegMatrix, 0, legOffsetX, legOffsetY, 0);
        Matrix.rotateM(leftLegMatrix, 0, legAngle, 1, 0, 0);
        legMeshes[0].draw(mvpMatrix, normalMatrix, pantsColor);

        // Left shoe
        float[] leftShoeMatrix = mModelMatrix.clone();
        Matrix.translateM(leftShoeMatrix, 0, legOffsetX, -height * 0.45f, 0);
        shoeMeshes[0].draw(mvpMatrix, normalMatrix, shoeColor);

        // Right leg
        float[] rightLegMatrix = mModelMatrix.clone();
        Matrix.translateM(rightLegMatrix, 0, -legOffsetX, legOffsetY, 0);
        Matrix.rotateM(rightLegMatrix, 0, -legAngle, 1, 0, 0);
        legMeshes[1].draw(mvpMatrix, normalMatrix, pantsColor);

        // Right shoe
        float[] rightShoeMatrix = mModelMatrix.clone();
        Matrix.translateM(rightShoeMatrix, 0, -legOffsetX, -height * 0.45f, 0);
        shoeMeshes[1].draw(mvpMatrix, normalMatrix, shoeColor);
    }

    // ========== MESH CREATION METHODS ==========

    /** Create a sphere-like head mesh */
    private Mesh createHeadMesh() {
        int rings = 8, sectors = 12;
        int vertexCount = (rings + 1) * sectors;
        float[] vertices = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] colors = new float[vertexCount * 4];
        short[] indices = new short[rings * sectors * 6];

        int v = 0, i = 0;
        for (int r = 0; r <= rings; r++) {
            float ringAngle = (float) r / rings * (float) Math.PI;
            float sinRing = (float) Math.sin(ringAngle);
            float cosRing = (float) Math.cos(ringAngle);
            for (int s = 0; s < sectors; s++) {
                float sectorAngle = (float) s / sectors * (float) Math.PI * 2;
                int base = v * 3;
                vertices[base] = sinRing * (float) Math.cos(sectorAngle);
                vertices[base + 1] = cosRing;
                vertices[base + 2] = sinRing * (float) Math.sin(sectorAngle);
                normals[base] = vertices[base];
                normals[base + 1] = vertices[base + 1];
                normals[base + 2] = vertices[base + 2];
                // Skin color with slight variation
                colors[base * 4] = 0.85f + (float) Math.random() * 0.05f;
                colors[base * 4 + 1] = 0.7f;
                colors[base * 4 + 2] = 0.6f;
                colors[base * 4 + 3] = 1f;
                v++;
            }
        }

        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < sectors; s++) {
                int nextS = (s + 1) % sectors;
                int v1 = r * sectors + s;
                int v2 = r * sectors + nextS;
                int v3 = (r + 1) * sectors + nextS;
                int v4 = (r + 1) * sectors + s;
                int base = i * 6;
                indices[base] = (short) v1;
                indices[base + 1] = (short) v2;
                indices[base + 2] = (short) v3;
                indices[base + 3] = (short) v1;
                indices[base + 4] = (short) v3;
                indices[base + 5] = (short) v4;
                i++;
            }
        }

        return new Mesh(vertices, normals, indices, colors);
    }

    /** Create a rectangular torso mesh (box) */
    private Mesh createTorsoMesh() {
        // Front, back, left, right, top, bottom faces
        float w = 0.35f, h = 0.6f, d = 0.2f;
        float[] vertices = {
            // Front face
            -w/2, 0, d/2, w/2, 0, d/2, w/2, h, d/2, -w/2, h, d/2,
            // Back face
            w/2, 0, -d/2, -w/2, 0, -d/2, -w/2, h, -d/2, w/2, h, -d/2,
            // Top face
            -w/2, h, -d/2, w/2, h, -d/2, w/2, h, d/2, -w/2, h, d/2,
            // Bottom face
            -w/2, 0, d/2, w/2, 0, d/2, w/2, 0, -d/2, -w/2, 0, -d/2,
            // Left face
            -w/2, 0, d/2, -w/2, 0, -d/2, -w/2, h, -d/2, -w/2, h, d/2,
            // Right face
            w/2, 0, -d/2, w/2, 0, d/2, w/2, h, d/2, w/2, h, -d/2
        };
        short[] indices = {
            // Front
            0, 1, 2, 0, 2, 3,
            // Back
            4, 5, 6, 4, 6, 7,
            // Top
            8, 9, 10, 8, 10, 11,
            // Bottom
            12, 13, 14, 12, 14, 15,
            // Left
            16, 17, 18, 16, 18, 19,
            // Right
            20, 21, 22, 20, 22, 23
        };
        float[] normals = {
            // Front
            0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
            // Back
            0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
            // Top
            0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
            // Bottom
            0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
            // Left
            -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
            // Right
            1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0
        };
        float[] colors = new float[24 * 4];
        for (int i = 0; i < 24; i++) {
            colors[i * 4] = 0.2f;
            colors[i * 4 + 1] = 0.6f;
            colors[i * 4 + 2] = 0.9f;
            colors[i * 4 + 3] = 1f;
        }
        return new Mesh(vertices, normals, indices, colors);
    }

    /** Create a cylinder arm mesh */
    private Mesh createArmMesh(float radius, float height) {
        int segments = 10;
        float[] vertices = new float[segments * 3];
        float[] normals = new float[segments * 3];
        float[] colors = new float[segments * 4];

        for (int i = 0; i < segments; i++) {
            float angle = (float) i / segments * (float) Math.PI * 2;
            int base = i * 3;
            vertices[base] = (float) Math.cos(angle) * radius;
            vertices[base + 1] = 0;
            vertices[base + 2] = (float) Math.sin(angle) * radius;
            normals[base] = vertices[base];
            normals[base + 1] = 0;
            normals[base + 2] = vertices[base + 2];
            colors[base * 4] = 0.2f;
            colors[base * 4 + 1] = 0.6f;
            colors[base * 4 + 2] = 0.9f;
            colors[base * 4 + 3] = 1f;
        }

        short[] indices = new short[segments * 6];
        int idx = 0;
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            int base = i * 3;
            indices[idx++] = (short) base;
            indices[idx++] = (short) next;
            indices[idx++] = (short) (base + 3);
            indices[idx++] = (short) (base + 3);
            indices[idx++] = (short) next;
            indices[idx++] = (short) (next + 3);
        }

        return new Mesh(vertices, normals, indices, colors);
    }

    /** Create a simple hand mesh */
    private Mesh createHandMesh() {
        float[] vertices = {
            0, 0, 0, 0.05f, 0, 0.08f, -0.05f, 0, 0.08f
        };
        short[] indices = {0, 1, 2};
        float[] normals = {0, 0, 1, 0, 0, 1, 0, 0, 1};
        float[] colors = {0.85f, 0.7f, 0.6f, 1f, 0.85f, 0.7f, 0.6f, 1f, 0.85f, 0.7f, 0.6f, 1f};
        return new Mesh(vertices, normals, indices, colors);
    }

    /** Create a cylinder leg mesh */
    private Mesh createLegMesh(float radius, float height) {
        int segments = 8;
        float[] vertices = new float[segments * 3];
        float[] normals = new float[segments * 3];
        float[] colors = new float[segments * 4];

        for (int i = 0; i < segments; i++) {
            float angle = (float) i / segments * (float) Math.PI * 2;
            int base = i * 3;
            vertices[base] = (float) Math.cos(angle) * radius;
            vertices[base + 1] = 0;
            vertices[base + 2] = (float) Math.sin(angle) * radius;
            normals[base] = vertices[base];
            normals[base + 1] = 0;
            normals[base + 2] = vertices[base + 2];
            colors[base * 4] = 0.3f;
            colors[base * 4 + 1] = 0.25f;
            colors[base * 4 + 2] = 0.2f;
            colors[base * 4 + 3] = 1f;
        }

        short[] indices = new short[segments * 6];
        int idx = 0;
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            int base = i * 3;
            indices[idx++] = (short) base;
            indices[idx++] = (short) next;
            indices[idx++] = (short) (base + 3);
            indices[idx++] = (short) (base + 3);
            indices[idx++] = (short) next;
            indices[idx++] = (short) (next + 3);
        }

        return new Mesh(vertices, normals, indices, colors);
    }

    /** Create a simple shoe mesh */
    private Mesh createShoeMesh() {
        float[] vertices = {
            -0.1f, 0, 0.1f, 0.1f, 0, 0.1f, 0.1f, 0, -0.1f,
            -0.1f, 0, 0.1f, 0.1f, 0, -0.1f, -0.1f, 0, -0.1f
        };
        short[] indices = {0, 1, 2, 3, 4, 5};
        float[] normals = {0, -1, 0, 0, -1, 0};
        float[] colors = new float[6 * 4];
        for (int i = 0; i < 6; i++) {
            colors[i * 4] = 0.15f;
            colors[i * 4 + 1] = 0.15f;
            colors[i * 4 + 2] = 0.15f;
            colors[i * 4 + 3] = 1f;
        }
        return new Mesh(vertices, normals, indices, colors);
    }

    // ========== GETTERS ==========

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getCollisionRadius() { return 0.3f; }
    public void setPosition(float x, float z) {
        this.x = x;
        this.z = z;
    }
    public float getHeight() { return height; }
}
