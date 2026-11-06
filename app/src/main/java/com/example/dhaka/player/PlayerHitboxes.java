package com.example.dhaka.player;

/**
 * Player collision detection and response.
 */
public class PlayerHitboxes {

    private final float collisionRadius;
    private final float collisionHeight;
    private final float groundY;

    private boolean isGrounded;
    private float velocityY;

    public PlayerHitboxes() {
        this.collisionRadius = 0.3f;
        this.collisionHeight = 1.8f;
        this.groundY = 0;
        this.isGrounded = true;
        this.velocityY = 0;
    }

    public float getCollisionRadius() {
        return collisionRadius;
    }

    public float getCollisionHeight() {
        return collisionHeight;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void applyGravity(float dt) {
        if (!isGrounded) {
            velocityY -= 9.8f * dt;
        }
    }

    public void jump() {
        if (isGrounded) {
            isGrounded = false;
            velocityY = 0.6f;
        }
    }

    public void updatePosition(float deltaTime, float[] outY) {
        if (!isGrounded) {
            float deltaY = velocityY * deltaTime;
            outY[0] = deltaY;

            if (outY[0] <= -groundY) {
                outY[0] = -groundY;
                isGrounded = true;
                velocityY = 0;
            }
        } else {
            outY[0] = 0;
        }
    }

    public boolean checkBuildingCollision(float x, float z, float playerRadius,
                                          float houseX, float houseZ,
                                          float houseWidth, float houseDepth) {
        float halfHouseWidth = houseWidth / 2;
        float halfHouseDepth = houseDepth / 2;

        float closestX = Math.max(houseX - halfHouseWidth, Math.min(x, houseX + halfHouseWidth));
        float closestZ = Math.max(houseZ - halfHouseDepth, Math.min(z, houseZ + halfHouseDepth));

        float dx = x - closestX;
        float dz = z - closestZ;
        float distance = (float) Math.sqrt(dx * dx + dz * dz);

        return distance < (playerRadius + 0.1f);
    }

    public void resolveBuildingCollision(float[] outX, float[] outZ,
                                          float houseX, float houseZ,
                                          float houseWidth, float houseDepth) {
        float halfHouseWidth = houseWidth / 2 + collisionRadius;
        float halfHouseDepth = houseDepth / 2 + collisionRadius;

        float minX = houseX - halfHouseWidth;
        float maxX = houseX + halfHouseWidth;
        float minZ = houseZ - halfHouseDepth;
        float maxZ = houseZ + halfHouseDepth;

        outX[0] = outX[0];
        outZ[0] = outZ[0];

        if (outX[0] < minX) {
            outX[0] = minX;
        } else if (outX[0] > maxX) {
            outX[0] = maxX;
        }

        if (outZ[0] < minZ) {
            outZ[0] = minZ;
        } else if (outZ[0] > maxZ) {
            outZ[0] = maxZ;
        }
    }
}
