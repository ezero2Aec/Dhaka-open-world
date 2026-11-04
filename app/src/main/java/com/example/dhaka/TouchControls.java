package com.example.dhaka;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TouchControls {
    private final float[] joystickPos = {0, 0};
    private float joystickBaseRadius = 60;
    private float joystickStickRadius = 25;
    private float joystickMaxDistance = 50;
    
    private final float joystickX = 100;
    private final float joystickY = 400;
    
    private final float buttonSize = 50;
    private final float buttonSpacing = 15;
    
    private final float cameraButtonX = 600;
    private final float cameraButtonY = 200;
    private float cameraButtonRadius = 80;
    
    private float jumpButtonX;
    private float jumpButtonY;
    private float sprintButtonX;
    private float sprintButtonY;
    private float interactButtonX;
    private float interactButtonY;
    private float vehicleButtonX;
    private float vehicleButtonY;
    
    private float joystickStartX, joystickStartY;
    private float joystickTouchX, joystickTouchY;
    private int joystickTouchId = -1;
    
    private float cameraStartX, cameraStartY;
    private int cameraTouchId = -1;
    
    private final Paint joystickBasePaint = new Paint();
    private final Paint joystickStickPaint = new Paint();
    private final Paint buttonPaint = new Paint();
    private final Paint textPaint = new Paint();
    
    private boolean jumpPressed = false;
    private boolean sprintPressed = false;
    private boolean interactPressed = false;
    private boolean vehiclePressed = false;
    
    private float moveX = 0;
    private float moveY = 0;
    private boolean cameraDrag = false;
    private float cameraDragX = 0;
    private float cameraDragY = 0;
    
    public TouchControls(int width, int height) {
        joystickBasePaint.setColor(Color.argb(128, 255, 255, 255));
        joystickBasePaint.setStyle(Paint.Style.FILL);
        joystickBasePaint.setAntiAlias(true);
        
        joystickStickPaint.setColor(Color.argb(200, 255, 255, 255));
        joystickStickPaint.setStyle(Paint.Style.FILL);
        joystickStickPaint.setAntiAlias(true);
        
        buttonPaint.setColor(Color.argb(180, 100, 150, 200));
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setAntiAlias(true);
        
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        
        float baseY = height - 80;
        jumpButtonX = width - 280;
        jumpButtonY = baseY;
        sprintButtonX = width - 160;
        sprintButtonY = baseY;
        interactButtonX = width - 160;
        interactButtonY = baseY - 110;
        vehicleButtonX = width - 280;
        vehicleButtonY = baseY - 110;
    }
    
    public boolean onTouchEvent(int action, int id, float x, float y) {
        moveX = 0;
        moveY = 0;
        cameraDrag = false;
        cameraDragX = 0;
        cameraDragY = 0;
        
        jumpPressed = false;
        sprintPressed = false;
        interactPressed = false;
        vehiclePressed = false;
        
        switch (action) {
            case 0:
                if (Math.abs(x - joystickX) < joystickBaseRadius + 50 &&
                    Math.abs(y - joystickY) < joystickBaseRadius + 50) {
                    joystickTouchId = id;
                    joystickStartX = joystickX + joystickPos[0];
                    joystickStartY = joystickY + joystickPos[1];
                    joystickTouchX = x;
                    joystickTouchY = y;
                    return true;
                } else if (x > cameraButtonX - cameraButtonRadius &&
                           x < cameraButtonX + cameraButtonRadius &&
                           y > cameraButtonY - cameraButtonRadius &&
                           y < cameraButtonY + cameraButtonRadius) {
                    cameraTouchId = id;
                    cameraStartX = x;
                    cameraStartY = y;
                    return true;
                } else if (buttonPressed(x, y, jumpButtonX, jumpButtonY, buttonSize)) {
                    jumpPressed = true;
                    return true;
                } else if (buttonPressed(x, y, sprintButtonX, sprintButtonY, buttonSize)) {
                    sprintPressed = true;
                    return true;
                } else if (buttonPressed(x, y, interactButtonX, interactButtonY, buttonSize)) {
                    interactPressed = true;
                    return true;
                } else if (buttonPressed(x, y, vehicleButtonX, vehicleButtonY, buttonSize)) {
                    vehiclePressed = true;
                    return true;
                }
                break;
                
            case 2:
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
                } else if (id == cameraTouchId) {
                    float dx = x - cameraStartX;
                    float dy = y - cameraStartY;
                    cameraDrag = true;
                    cameraDragX = dx;
                    cameraDragY = dy;
                    return true;
                }
                break;
                
            case 1:
                if (id == joystickTouchId) {
                    joystickTouchId = -1;
                    joystickPos[0] = 0;
                    joystickPos[1] = 0;
                    return true;
                } else if (id == cameraTouchId) {
                    cameraTouchId = -1;
                    cameraDrag = false;
                    return true;
                }
                break;
        }
        return false;
    }
    
    private boolean buttonPressed(float x, float y, float bx, float by, float size) {
        return x > bx - size / 2 && x < bx + size / 2 &&
               y > by - size / 2 && y < by + size / 2;
    }
    
    public void draw(Canvas canvas) {
        canvas.drawCircle(joystickX + joystickPos[0], joystickY + joystickPos[1],
            joystickStickRadius, joystickStickPaint);
        canvas.drawCircle(joystickX, joystickY, joystickBaseRadius, joystickBasePaint);
        
        canvas.drawCircle(jumpButtonX, jumpButtonY, buttonSize, buttonPaint);
        canvas.drawCircle(sprintButtonX, sprintButtonY, buttonSize, buttonPaint);
        canvas.drawCircle(interactButtonX, interactButtonY, buttonSize, buttonPaint);
        canvas.drawCircle(vehicleButtonX, vehicleButtonY, buttonSize, buttonPaint);
        canvas.drawCircle(cameraButtonX, cameraButtonY, cameraButtonRadius, buttonPaint);
        
        canvas.drawText("JUMP", jumpButtonX - 25, jumpButtonY + 7, textPaint);
        canvas.drawText("SPRINT", sprintButtonX - 35, sprintButtonY + 7, textPaint);
        canvas.drawText("USE", interactButtonX - 18, interactButtonY + 7, textPaint);
        canvas.drawText("DRV", vehicleButtonX - 15, vehicleButtonY + 7, textPaint);
        canvas.drawText("CAM", cameraButtonX - 20, cameraButtonY + 7, textPaint);
    }
    
    public float getMoveX() { return moveX; }
    public float getMoveY() { return moveY; }
    public boolean getJumpPressed() { return jumpPressed; }
    public boolean getSprintPressed() { return sprintPressed; }
    public boolean getInteractPressed() { return interactPressed; }
    public boolean getVehiclePressed() { return vehiclePressed; }
    public boolean isCameraDrag() { return cameraDrag; }
    public float getCameraDragX() { return cameraDragX; }
    public float getCameraDragY() { return cameraDragY; }
    /** Reset camera drag after it has been processed by the renderer. */
    public void consumeCameraDrag() {
        cameraDrag = false;
        cameraDragX = 0;
        cameraDragY = 0;
    }
}
