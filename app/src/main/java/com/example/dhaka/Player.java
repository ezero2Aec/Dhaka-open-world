package com.example.dhaka;

import com.example.dhaka.player.PlayerModel;
import com.example.dhaka.player.PlayerAnimation;
import com.example.dhaka.player.PlayerHitboxes;
import android.opengl.Matrix;

/**
 * Main player controller that integrates PlayerModel, PlayerAnimation, and PlayerHitboxes.
 */
public class Player {

    // Modular player components
    private final PlayerModel playerModel;
    private final PlayerAnimation playerAnimation;
    private final PlayerHitboxes playerHitboxes;

    // Position and rotation
    private float x, y, z;
    private float rotation;

    // Physics properties
    private final float height = 1.8f;
    private final float walkSpeed = 0.12f;
    private final float runSpeed = 0.25f;
    private float velocityY = 0;

    // Movement state
    private boolean isGrounded = true;
    private boolean isRunning = false;

    public Player() {
        playerModel = new PlayerModel();
        playerAnimation = new PlayerAnimation();
        playerHitboxes = new PlayerHitboxes();

        // Initial position
        x = 0;
        y = height / 2;
        z = 0;
        rotation = 0;
    }

    public void initMeshes() {
        playerModel.initMeshes(Shader.DEFAULT_VERTEX_SHADER, Shader.DEFAULT_FRAGMENT_SHADER);
    }

    public void update(float dt, float moveX, float moveY, boolean running) {
        isRunning = running;
        float speed = isRunning ? runSpeed : walkSpeed;

        // Update animation based on movement
        playerAnimation.update(dt, moveX, moveY, isGrounded, velocityY);

        // Movement and rotation
        if (isGrounded && (moveX != 0 || moveY != 0)) {
            float angle = (float) Math.atan2(moveX, -moveY);
            rotation = (float) Math.toDegrees(angle);
            x += moveX * speed;
            z += moveY * speed;
        }

        // Apply gravity
        playerHitboxes.applyGravity(dt);
        float[] deltaY = new float[1];
        playerHitboxes.updatePosition(dt, deltaY);
        y += deltaY[0];

        // Jump
        if (playerHitboxes.isGrounded() && playerHitboxes.getVelocityY() == 0) {
            isGrounded = true;
        }
    }

    public void jump() {
        playerHitboxes.jump();
    }

    public void draw(float[] vpMatrix, float[] normalMatrix) {
        playerModel.drawBody(vpMatrix, normalMatrix);
        playerModel.drawArms(vpMatrix, normalMatrix);
        playerModel.drawLegs(vpMatrix, normalMatrix);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getHeight() { return height; }
    public float getCollisionRadius() { return playerHitboxes.getCollisionRadius(); }
    public boolean isGrounded() { return playerHitboxes.isGrounded(); }

    public void setPosition(float x, float z) {
        this.x = x;
        this.z = z;
    }

    public float getArmAngleLeft() {
        return playerAnimation.getArmAngleLeft();
    }

    public float getArmAngleRight() {
        return playerAnimation.getArmAngleRight();
    }

    public float getLegAngleLeft() {
        return playerAnimation.getLegAngleLeft();
    }

    public float getLegAngleRight() {
        return playerAnimation.getLegAngleRight();
    }

    public float getHeadBounce() {
        return playerAnimation.getHeadBounce();
    }
}
