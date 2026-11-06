package com.example.dhaka.player;

/**
 * Virtual joystick and button controls for player input.
 */
public class PlayerControls {

    // Joystick
    private float[] moveVector = {0, 0};
    private float joystickBaseRadius;
    private float joystickStickRadius;
    private float joystickMaxDistance;
    private float joystickX, joystickY;

    // Buttons
    private final float buttonRadius;
    private float runButtonX, runButtonY;
    private float jumpButtonX, jumpButtonY;
    private float interactButtonX, interactButtonY;

    // Touch tracking
    private float joystickStartX, joystickStartY;
    private float joystickTouchX, joystickTouchY;
    private int joystickTouchId = -1;

    private int buttonTouchId = -1;

    // Input state
    private boolean runPressed = false;
    private boolean jumpPressed = false;
    private boolean interactPressed = false;

    public PlayerControls(int width, int height) {
        // Position joystick on left side
        float baseScale = Math.min(width, height) / 480f;
        joystickBaseRadius = 50 * baseScale;
        joystickStickRadius = 25 * baseScale;
        joystickMaxDistance = 40 * baseScale;

        joystickX = joystickBaseRadius + 20;
        joystickY = height - joystickBaseRadius - 20;

        // Position buttons on right side
        buttonRadius = 40 * baseScale;
        float rightMargin = 20 * baseScale;
        float bottomMargin = 20 * baseScale;

        runButtonX = width - rightMargin - buttonRadius;
        runButtonY = height - bottomMargin - buttonRadius * 3 - 40;

        jumpButtonX = width - rightMargin - buttonRadius;
        jumpButtonY = height - bottomMargin - buttonRadius * 2 - 20;

        interactButtonX = width - rightMargin - buttonRadius;
        interactButtonY = height - bottomMargin - buttonRadius;
    }

    public boolean onTouchEvent(int action, int id, float x, float y) {
        switch (action) {
            case 1: // ACTION_DOWN
            case 7: // ACTION_POINTER_DOWN
                // Check joystick
                if (isInCircle(x, y, joystickX, joystickY, joystickBaseRadius + 30)) {
                    joystickTouchId = id;
                    joystickStartX = joystickX;
                    joystickStartY = joystickY;
                    joystickTouchX = x;
                    joystickTouchY = y;
                    return true;
                }

                // Check RUN button
                if (isInCircle(x, y, runButtonX, runButtonY, buttonRadius)) {
                    buttonTouchId = id;
                    runPressed = true;
                    return true;
                }

                // Check JUMP button
                if (isInCircle(x, y, jumpButtonX, jumpButtonY, buttonRadius)) {
                    buttonTouchId = id;
                    jumpPressed = true;
                    return true;
                }

                // Check INTERACT button
                if (isInCircle(x, y, interactButtonX, interactButtonY, buttonRadius)) {
                    buttonTouchId = id;
                    interactPressed = true;
                    return true;
                }
                break;

            case 2: // ACTION_MOVE
                if (id == joystickTouchId) {
                    float dx = x - joystickTouchX;
                    float dy = y - joystickTouchY;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    float angle = (float) Math.atan2(dx, -dy);
                    float maxDist = Math.min(dist, joystickMaxDistance);

                    moveVector[0] = maxDist * (float) Math.sin(angle);
                    moveVector[1] = maxDist * (float) Math.cos(angle);

                    return true;
                }
                break;

            case 0: // ACTION_UP
            case 6: // ACTION_POINTER_UP
                if (id == joystickTouchId) {
                    joystickTouchId = -1;
                    moveVector[0] = 0;
                    moveVector[1] = 0;
                    return true;
                }

                if (id == buttonTouchId) {
                    buttonTouchId = -1;
                    runPressed = false;
                    jumpPressed = false;
                    interactPressed = false;
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean isInCircle(float x, float y, float cx, float cy, float r) {
        float dx = x - cx;
        float dy = y - cy;
        return (dx * dx + dy * dy) <= (r * r);
    }

    public float getMoveX() {
        return moveVector[0] / joystickMaxDistance;
    }

    public float getMoveY() {
        return moveVector[1] / joystickMaxDistance;
    }

    public boolean isRunPressed() {
        return runPressed;
    }

    public boolean isJumpPressed() {
        return jumpPressed;
    }

    public boolean isInteractPressed() {
        return interactPressed;
    }
}
