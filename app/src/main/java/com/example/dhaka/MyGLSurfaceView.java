package com.example.dhaka;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

public class MyGLSurfaceView extends GLSurfaceView {
    private final GameRenderer renderer;
    private int screenWidth, screenHeight;
    
    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new GameRenderer(screenWidth, screenHeight);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                int action = e.getActionMasked();
                int id = e.getActionIndex();
                float x = e.getX(id);
                float y = e.getY(id);
                return renderer.onTouchEvent(action, id, x, y);
            }
        });
        setKeepScreenOn(true);
    }
    
    public void updateSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
}
