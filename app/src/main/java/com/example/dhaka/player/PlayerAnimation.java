package com.example.dhaka.player;

/**
 * Handles player animations including idle, walking, running, jumping, and landing.
 */
public class PlayerAnimation {

    public static enum State { IDLE, WALKING, RUNNING, JUMPING, LANDING }

    private State currentState;
    private float animationTime;
    private float transitionTimer;
    private static final float TRANSITION_DURATION = 0.2f;

    // Limb angles
    private float armAngleLeft;
    private float armAngleRight;
    private float legAngleLeft;
    private float legAngleRight;
    private float headBounce;

    public PlayerAnimation() {
        currentState = State.IDLE;
        animationTime = 0;
        transitionTimer = 0;
    }

    public State getState() {
        return currentState;
    }

    public void update(float dt, float moveX, float moveY, boolean isGrounded, float velocityY) {
        animationTime += dt;

        // Determine state based on movement and physics
        float speed = (float) Math.sqrt(moveX * moveX + moveY * moveY);

        if (!isGrounded && velocityY > 0) {
            currentState = State.JUMPING;
        } else if (!isGrounded && velocityY <= 0) {
            if (transitionTimer <= 0) {
                transitionTimer = TRANSITION_DURATION;
                currentState = State.LANDING;
            } else {
                transitionTimer -= dt;
                if (transitionTimer <= 0) {
                    transitionTimer = 0;
                    currentState = State.IDLE;
                }
            }
        } else if (speed > 0.1f) {
            if (transitionTimer <= 0) {
                transitionTimer = TRANSITION_DURATION;
                currentState = State.RUNNING;
            } else {
                transitionTimer -= dt;
                if (transitionTimer <= 0) {
                    transitionTimer = 0;
                    currentState = State.RUNNING;
                }
            }
        } else if (speed > 0.01f) {
            if (transitionTimer <= 0) {
                transitionTimer = TRANSITION_DURATION;
                currentState = State.WALKING;
            } else {
                transitionTimer -= dt;
                if (transitionTimer <= 0) {
                    transitionTimer = 0;
                    currentState = State.WALKING;
                }
            }
        } else {
            transitionTimer = 0;
            currentState = State.IDLE;
        }

        // Calculate limb angles based on state
        updateLimbAngles(dt, speed);
    }

    private void updateLimbAngles(float dt, float speed) {
        float animSpeed;
        float armMaxAngle;
        float legMaxAngle;
        float bounceAmount;

        switch (currentState) {
            case RUNNING:
                animSpeed = 12f;
                armMaxAngle = 0.8f;
                legMaxAngle = 0.9f;
                bounceAmount = 0.08f;
                break;
            case WALKING:
                animSpeed = 8f;
                armMaxAngle = 0.5f;
                legMaxAngle = 0.6f;
                bounceAmount = 0.05f;
                break;
            case IDLE:
                animSpeed = 2f;
                armMaxAngle = 0.1f;
                legMaxAngle = 0.05f;
                bounceAmount = 0.01f;
                break;
            default:
                animSpeed = 0;
                armMaxAngle = 0;
                legMaxAngle = 0;
                bounceAmount = 0;
                break;
        }

        float time = animationTime * animSpeed;

        armAngleLeft = (float) Math.sin(time) * armMaxAngle;
        armAngleRight = (float) Math.sin(time + (float) Math.PI) * armMaxAngle;
        legAngleLeft = (float) Math.sin(time + (float) Math.PI) * legMaxAngle;
        legAngleRight = (float) Math.sin(time) * legMaxAngle;
        headBounce = (float) Math.abs(Math.sin(time * 2)) * bounceAmount;
    }

    public float getArmAngleLeft() {
        return armAngleLeft;
    }

    public float getArmAngleRight() {
        return armAngleRight;
    }

    public float getLegAngleLeft() {
        return legAngleLeft;
    }

    public float getLegAngleRight() {
        return legAngleRight;
    }

    public float getHeadBounce() {
        return headBounce;
    }
}
