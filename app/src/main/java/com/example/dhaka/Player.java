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
        /** Create a UV sphere head mesh */
    private Mesh createHeadMesh() {
        // Parameters for sphere
        int rings = 8;   // latitude divisions
        int sectors = 8; // longitude divisions
        float radius = 0.5f;

        // Compute vertex count and allocate buffers
        int vertexCount = (rings + 1) * (sectors + 1);
        float[] vertices = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] colors = new float[vertexCount * 4];

        // Generate vertices, normals, and colors
        int v = 0;
        for (int r = 0; r <= rings; r++) {
            float phi = (float) Math.PI * r / rings; // 0 .. PI
            float y = (float) Math.cos(phi) * radius;
            float sinPhi = (float) Math.sin(phi);
            for (int s = 0; s <= sectors; s++) {
                float theta = (float) (2 * Math.PI) * s / sectors; // 0 .. 2PI
                float x = (float) Math.cos(theta) * sinPhi * radius;
                float z = (float) Math.sin(theta) * sinPhi * radius;

                // Position
                vertices[v] = x;
                vertices[v + 1] = y;
                vertices[v + 2] = z;

                // Normal (unit vector)
                float len = (float) Math.sqrt(x * x + y * y + z * z);
                normals[v] = x / len;
                normals[v + 1] = y / len;
                normals[v + 2] = z / len;

                // Color (skin tone)
                int c = v / 3 * 4;
                colors[c] = 0.85f;
                colors[c + 1] = 0.7f;
                colors[c + 2] = 0.6f;
                colors[c + 3] = 1f;

                v += 3;
            }
        }

        // Compute index count and allocate buffer
        int indexCount = rings * sectors * 6; // 2 triangles per quad
        short[] indices = new short[indexCount];
        int i = 0;
        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < sectors; s++) {
                short cur = (short) (r * (sectors + 1) + s);
                short next = (short) (cur + sectors + 1);

                // First triangle of quad
                indices[i++] = cur;
                indices[i++] = (short) (cur + 1);
                indices[i++] = next;

                // Second triangle of quad
                indices[i++] = (short) (cur + 1);
                indices[i++] = (short) (next + 1);
                indices[i++] = next;
            }
        }

        // Defensive validation
        if (indices.length % 3 != 0) {
            throw new RuntimeException("Index count not divisible by 3: " + indices.length);
        }
        for (short idx : indices) {
            if (idx < 0 || idx >= vertexCount) {
                throw new RuntimeException("Index out of bounds: " + idx + " (vertexCount=" + vertexCount + ")");
            }
        }
        if (vertices.length != vertexCount * 3) {
            throw new RuntimeException("Vertex buffer size mismatch");
        }
        if (normals.length != vertexCount * 3) {
            throw new RuntimeException("Normal buffer size mismatch");
        }
        if (colors.length != vertexCount * 4) {
            throw new RuntimeException("Color buffer size mismatch");
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

    /**
     * Create a simple box mesh used for arms, legs and shoes.
     * @param w width (x dimension)
     * @param h height (y dimension)
     * @param d depth (z dimension)
     * @param color RGBA color array (length 4)
     */
    private Mesh createBoxMesh(float w, float h, float d, float[] color) {
        // 24 vertices (4 per face) same layout as torso
        float[] vertices = {
            // Front face
            -w/2, 0, d/2, w/2, 0, d/2, w/2, h, d/2, -w/2, h, d/2,
            // Back face
            -w/2, 0, -d/2, w/2, 0, -d/2, w/2, h, -d/2, -w/2, h, -d/2,
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
            0, 1, 2, 0, 2, 3,
            4, 5, 6, 4, 6, 7,
            8, 9, 10, 8, 10, 11,
            12, 13, 14, 12, 14, 15,
            16, 17, 18, 16, 18, 19,
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
            colors[i * 4] = color[0];
            colors[i * 4 + 1] = color[1];
            colors[i * 4 + 2] = color[2];
            colors[i * 4 + 3] = color[3];
        }
        return new Mesh(vertices, normals, indices, colors);
    }

    /** Create a cylinder arm mesh */
        private Mesh createArmMesh(float width, float height) {
        // Use a simple box for the arm
        float depth = width; // make it a cube-like shape
        return createBoxMesh(width, height, depth, new float[]{0.2f, 0.6f, 0.9f, 1f});
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
    private Mesh createLegMesh(float width, float height) {
        // Use a simple box for the leg
        float depth = width; // make it a cube-like shape
        return createBoxMesh(width, height, depth, new float[]{0.3f, 0.25f, 0.2f, 1f});
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
