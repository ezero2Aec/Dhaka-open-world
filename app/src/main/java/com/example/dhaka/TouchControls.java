package com.example.dhaka;

import android.graphics.Canvas;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * GTA-style mobile controls for open-world gameplay.
 * Includes:
 * - Virtual joystick on left side
 * - RUN, JUMP buttons on right side
 * - Touch area for camera control (right side drag)
 */
public class TouchControls {
    // Joystick (left side)
    private final float[] joystickPos = {0, 0};
    private float joystickBaseRadius;
    private float joystickStickRadius;
    private float joystickMaxDistance;

    private float joystickX, joystickY;

    // Buttons (right side)
    private final float buttonRadius = 35;
    private float runButtonX, runButtonY;
    private float jumpButtonX, jumpButtonY;
    private float cameraButtonX, cameraButtonY;

    // Touch tracking
    private float joystickStartX, joystickStartY;
    private float joystickTouchX, joystickTouchY;
    private int joystickTouchId = -1;

    private float cameraStartX, cameraStartY;
    private int cameraTouchId = -1;

    private float buttonTouchX, buttonTouchY;
    private int buttonTouchId = -1;

    // Paints for rendering
    private final Paint joystickBasePaint = new Paint();
    private final Paint joystickStickPaint = new Paint();
    private final Paint buttonPaint = new Paint();
    private final Paint buttonPressedPaint = new Paint();
    private final Paint textPaint = new Paint();

    // Input state
    private float moveX = 0;
    private float moveY = 0;
    private boolean runPressed = false;
    private boolean jumpPressed = false;
    private boolean cameraDrag = false;
    private float cameraDragX = 0;
    private float cameraDragY = 0;

    // Button states
    private boolean isRunPressed = false;
    private boolean isJumpPressed = false;
    private boolean isCameraPressed = false;

    public TouchControls(int width, int height) {
        // Initialize paint objects
        joystickBasePaint.setColor(Color.argb(100, 255, 255, 255));
        joystickBasePaint.setStyle(Paint.Style.FILL);
        joystickBasePaint.setAntiAlias(true);

        joystickStickPaint.setColor(Color.argb(180, 255, 255, 255));
        joystickStickPaint.setStyle(Paint.Style.FILL);
        joystickStickPaint.setAntiAlias(true);

        buttonPaint.setColor(Color.argb(150, 0, 150, 255));
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setAntiAlias(true);

        buttonPressedPaint.setColor(Color.argb(200, 0, 180, 255));
        buttonPressedPaint.setStyle(Paint.Style.FILL);
        buttonPressedPaint.setAntiAlias(true);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(16);
        textPaint.setAntiAlias(true);

        // Position controls based on screen size
        float baseScale = Math.min(width, height) / 480f;
        joystickBaseRadius = 50 * baseScale;
        joystickStickRadius = 25 * baseScale;
        joystickMaxDistance = 40 * baseScale;

        // Place joystick on left side
        joystickX = joystickBaseRadius + 20;
        joystickY = height - joystickBaseRadius - 20;

        // Place buttons on right side
        float rightMargin = 20;
        float bottomMargin = 20;

        runButtonX = width - rightMargin - buttonRadius;
        runButtonY = height - bottomMargin - buttonRadius - 10;

        jumpButtonX = width - rightMargin - buttonRadius - 10;
        jumpButtonY = height - bottomMargin - buttonRadius * 2 - 30;

        cameraButtonX = width - rightMargin - buttonRadius;
        cameraButtonY = height - bottomMargin - buttonRadius * 3 - 50;
    }

    /**
     * Handle touch events from the GLSurfaceView.
     * @param action MotionEvent action code
     * @param id Pointer ID
     * @param x Touch X coordinate
     * @param y Touch Y coordinate
     * @return true if event was handled
     */
    public boolean onTouchEvent(int action, int id, float x, float y) {
        // Reset input state each frame
        moveX = 0;
        moveY = 0;
        runPressed = false;
        jumpPressed = false;
        cameraDrag = false;
        cameraDragX = 0;
        cameraDragY = 0;

        // Reset button pressed states
        isRunPressed = false;
        isJumpPressed = false;
        isCameraPressed = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
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
                    buttonTouchX = runButtonX;
                    buttonTouchY = runButtonY;
                    isRunPressed = true;
                    return true;
                }

                // Check JUMP button
                if (isInCircle(x, y, jumpButtonX, jumpButtonY, buttonRadius)) {
                    buttonTouchId = id;
                    buttonTouchX = jumpButtonX;
                    buttonTouchY = jumpButtonY;
                    isJumpPressed = true;
                    return true;
                }

                // Check camera control button
                if (isInCircle(x, y, cameraButtonX, cameraButtonY, buttonRadius)) {
                    cameraTouchId = id;
                    cameraStartX = x;
                    cameraStartY = y;
                    isCameraPressed = true;
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Update joystick
                if (id == joystickTouchId) {
                    float dx = x - joystickTouchX;
                    float dy = y - joystickTouchY;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    float angle = (float) Math.atan2(dx, -dy);
                    float maxDist = Math.min(dist, joystickMaxDistance);
                    joystickPos[0] = maxDist * (float) Math.sin(angle);
                    joystickPos[1] = maxDist * (float) Math.cos(angle);
                    moveX = joystickPos[0] / joystickMaxDistance;
                    moveY = joystickPos[1] / joystickMaxDistance;
                    return true;
                }

                // Update camera drag
                if (id == cameraTouchId) {
                    float dx = x - cameraStartX;
                    float dy = y - cameraStartY;
                    cameraDrag = true;
                    cameraDragX = dx;
                    cameraDragY = dy;
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (id == joystickTouchId) {
                    joystickTouchId = -1;
                    joystickPos[0] = 0;
                    joystickPos[1] = 0;
                    moveX = 0;
                    moveY = 0;
                    return true;
                }

                if (id == buttonTouchId) {
                    buttonTouchId = -1;
                    return true;
                }

                if (id == cameraTouchId) {
                    cameraTouchId = -1;
                    cameraDrag = false;
                    return true;
                }
                break;
        }
        return false;
    }

    /** Check if a point is inside a circle */
    private boolean isInCircle(float x, float y, float cx, float cy, float r) {
        float dx = x - cx;
        float dy = y - cy;
        return (dx * dx + dy * dy) <= (r * r);
    }

    /** Draw controls on the screen */
    public void draw(Canvas canvas) {
        // Draw joystick
        canvas.drawCircle(joystickX, joystickY, joystickBaseRadius, joystickBasePaint);
        canvas.drawCircle(joystickX + joystickPos[0], joystickY + joystickPos[1],
            joystickStickRadius, joystickStickPaint);

        // Draw RUN button
        Paint runPaint = isRunPressed ? buttonPressedPaint : buttonPaint;
        canvas.drawCircle(runButtonX, runButtonY, buttonRadius, runPaint);
        canvas.drawText("RUN", runButtonX - 18, runButtonY + 5, textPaint);

        // Draw JUMP button
        Paint jumpPaint = isJumpPressed ? buttonPressedPaint : buttonPaint;
        canvas.drawCircle(jumpButtonX, jumpButtonY, buttonRadius, jumpPaint);
        canvas.drawText("JUMP", jumpButtonX - 20, jumpButtonY + 5, textPaint);

        // Draw camera button
        canvas.drawCircle(cameraButtonX, cameraButtonY, buttonRadius, buttonPaint);
        canvas.drawText("CAM", cameraButtonX - 16, cameraButtonY + 5, textPaint);
    }

    // ========== GETTERS ==========

    public float getMoveX() { return moveX; }
    public float getMoveY() { return moveY; }
    public boolean isRunPressed() { return isRunPressed; }
    public boolean isJumpPressed() { return isJumpPressed; }
    public boolean isCameraDrag() { return cameraDrag; }
    public float getCameraDragX() { return cameraDragX; }
    public float getCameraDragY() { return cameraDragY; }

    /** Reset camera drag after processing */
    public void consumeCameraDrag() {
        cameraDrag = false;
        cameraDragX = 0;
        cameraDragY = 0;
    }
}
